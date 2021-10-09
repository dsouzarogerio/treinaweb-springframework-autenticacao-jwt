package br.com.treinaweb.javajobs.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.treinaweb.javajobs.dto.UserDTO;
import br.com.treinaweb.javajobs.models.User;

/**
 * classe de conversão de DTO para Entidade
 * 
 * @author dsouzarogerio
 *
 */
@Component
public class UserMapper {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User toModel(UserDTO userDTO) {
		User user = new User();
		
		user.setUsername(userDTO.getUsername());
		
		//implementação do hash no atributo password
		String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
		user.setPassword(hashedPassword);
		
		return user;
	}

}
