package br.com.treinaweb.javajobs.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.treinaweb.javajobs.auth.AuthenticatedUser;
import br.com.treinaweb.javajobs.dto.JwtResponse;
import br.com.treinaweb.javajobs.dto.UserDTO;
import br.com.treinaweb.javajobs.models.User;
import br.com.treinaweb.javajobs.repositories.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User foundUser = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
		
		return new AuthenticatedUser(foundUser);
	}

	/**
	 * Criação do JWTResponse
	 * 
	 * Antes de criar é preciso validar e autenticar as credenciais do usuário 
	 * 
	 * Processo:
	 * 1. Busca as credenciais do usuario no UserDTO
	 * 2. UsernamePasswordAuthenticationToken => Validação das credenciais do usuario 
	 * 											 a ser autenticado
	 * 3. Authentication => Autenticação das credenciais do usuario
	 * 4. Gera de fato o JWTResponse através do JWTService que gerencia e gera o TOKEN
	 * 
	 * @param userDTO
	 * @return
	 */
	public JwtResponse createJwtResponse(UserDTO userDTO) {
		
		//1. credenciais
		String username = userDTO.getUsername();
		String password = userDTO.getPassword();
		
		//2. Validacao das credenciais do usuário
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
		
		//3. Autenticação de fato do usuário
		Authentication authenticatedUser = authenticationManager.authenticate(authentication);
		
		//4. Gerando de fato o JWT
		String token = jwtService.generateToken(authenticatedUser);
		Date expiresAt = jwtService.getExpirationFromToken(token);
		
		return new JwtResponse(token, "Bearer", expiresAt);
	}
}
