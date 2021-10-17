package br.com.treinaweb.javajobs.filters;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.treinaweb.javajobs.dto.ApiError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

/**
 * Classe para tratar os erros/exceções 
 * causados dentro da camada de Filter
 * 
 * @author dsouzarogerio
 *
 */
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	/**
	 * Método para captura e execução da tratativa dos erros
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch(ExpiredJwtException | SignatureException | MalformedJwtException exception) {
			HttpStatus status = HttpStatus.UNAUTHORIZED;
			LocalDateTime timestamp = LocalDateTime.now();
			String path = request.getRequestURI();
			
			ApiError apiError = new ApiError(status.value(), timestamp, exception.getLocalizedMessage(), path, null);
			
			//status da resposta
			response.setStatus(status.value());
			//corpo da resposta
			response.getWriter().write(convertObjectToJson(apiError));
		}

	}

	/**
	 * Método para executar o processo de serialização do corpo da resposta de exceções
	 * 
	 * 1. Instanciar um objeto do Jackson - ObjectMapper --> responsavel por instanciar o processo de serialização
	 * 2. O ObjectMapper por padrão não serializa dados do tipo LocalDateTime, sendo preciso:
	 * 		A. Registar o módulo na classe JavaTimeModule
	 * 		B. Desabilitar a configuração automática de timestamp do Jackson
	 * 3. Por fim retorna o objeto serializado
	 * 
	 * @param object
	 * @return
	 * @throws JsonProcessingException
	 * 
	 */
	private String convertObjectToJson(Object object) throws JsonProcessingException {
		if(object == null) {
			return null;
		}
		//1. Instanciando ObjectMapper do Jackson
		ObjectMapper mapper = new ObjectMapper();
		
		//2. Serializacao do LocalDateTime
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		//3. Retorno do objeto serializado
		return mapper.writeValueAsString(object);
	}
}
