package br.com.treinaweb.javajobs.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.treinaweb.javajobs.dto.JwtResponse;
import br.com.treinaweb.javajobs.dto.UserDTO;
import br.com.treinaweb.javajobs.models.User;
import br.com.treinaweb.javajobs.services.AuthenticationService;
import br.com.treinaweb.javajobs.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	/**
	 * Rota de cadastro do usuário
	 * 
	 * @param userDTO
	 * @return
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public User create(@RequestBody @Valid UserDTO userDTO) {
		return userService.create(userDTO);
	}
	
	/** 
	 * Rota de autenticação do usuário
	 *  
	 * @param userDTO
	 * @return
	 */
	@PostMapping("/auth")
	public JwtResponse auth(@RequestBody @Valid UserDTO userDTO) {
		return authenticationService.createJwtResponse(userDTO);
	}
	/**
	 * Rota de autenticacao refreshToken
	 * 
	 * @param refreshToken
	 * @return
	 */
	@PostMapping("/refresh/{refreshToken}")
	public JwtResponse refresh(@PathVariable String refreshToken) {
		return authenticationService.createJwtResponse(refreshToken);
	}

}
