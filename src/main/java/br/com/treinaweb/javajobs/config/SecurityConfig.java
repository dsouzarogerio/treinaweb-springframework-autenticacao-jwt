package br.com.treinaweb.javajobs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.treinaweb.javajobs.services.AuthenticationService;

/**
 * Classe responsável pela configuração do Spring Security
 * 
 * @author dsouzarogerio
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String API_JOBS_URL = "/api/v1/jobs/**";
	private static final String API_USERS_URL = "/api/v1/users/**";
	
	@Autowired
	private AuthenticationService authenticationService;
	
	//metodo relacionado a autorização da aplicação
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//desabilitar o ataque csrf
		http.csrf()
			.disable();
		
		//definindo as rotas publicas/privadas
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, API_JOBS_URL).permitAll() //permissao de rotas com método http GET
			.antMatchers(HttpMethod.POST, API_USERS_URL).permitAll()
			.anyRequest().authenticated(); //demais rotas deverão ser autenticadas
		
		//configuração para desabiltiar sessão da aplicação, para possibilitar a configuração/uso de token
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //não vai guardar estado da sessão	
	}
	
	//informando ao SpringSecuirty a classe que implementa o serviço de autenticação 
	//e qual o algoritmo de hash que ele vai utilizar
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService)
			.passwordEncoder(passwordEncoder());
	}

	//gerenciador de autenticação
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	//metodo para encriptar a senha
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
}
