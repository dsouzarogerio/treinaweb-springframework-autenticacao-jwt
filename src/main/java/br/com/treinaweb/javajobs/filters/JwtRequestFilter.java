package br.com.treinaweb.javajobs.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.treinaweb.javajobs.services.JwtService;

/**
 * Filtro para validar e buscar o usuário do Token
 * 
 * @author dsouzarogerio
 *
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter  {
	
	private static final String TOKEN_TYPE = "Bearer ";
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * Método para execução da lógica de captura do cabeçalho e validação do Token
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {

		String token = "";
		String username = "";
		
		//captura do parâmetro de autorização no Header 
		String authorizationHeader = request.getHeader("Authorization");
		
		//caso o Token esteja presente, é feita a captura do Token
		if(isTokenPresent(authorizationHeader)) {
			token = authorizationHeader.substring(TOKEN_TYPE.length());
			//captura do usuario contido no Token
			username = jwtService.getUsernameFromToken(token);
		}
		//caso não tenha usuário no contexto, obtenho o usuário e coloco no contexto do Spring
		if(isUsernameNotInContext(username)) {
			addUsernameInContext(request, username, token);
		}
		//chamar o método com o filtro especifico e passando a execução para os próximos filtros do FilterChain
		filterChain.doFilter(request, response);
	}
	
	/**
	 * Método privado para validar se o Token está presente
	 * 
	 * @param authorizationHeader
	 * @return
	 */
	private boolean isTokenPresent(String authorizationHeader) {
		return authorizationHeader != null && authorizationHeader.startsWith(TOKEN_TYPE);
	}
	
	/**
	 * Método privado para verificar se o contexto do Spring está com o usuário vazio
	 *  
	 * @param username
	 * @return
	 */
	private boolean isUsernameNotInContext(String username) {
		return !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null;
	}
	
	/**
	 * Método privado para adição do usuário no contexto do Spring
	 */
	private void addUsernameInContext(HttpServletRequest request, String username, String token) {
		//1. buscar usuário no banco
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		
		//2. Uma vez capturado o userDetails, deve ser colocado dentro do contexto do Spring
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		//3. setar em authentication qual a requisicao que foi feita para que a autenticação acontecesse
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		//4. colocando o username de fato no contexto do Spring Security
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
