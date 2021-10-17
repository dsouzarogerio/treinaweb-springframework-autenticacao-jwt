package br.com.treinaweb.javajobs.services;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Classe gerenciadora de token
 * 
 * @author dsouzarogerio
 *
 */
@Service
public class JwtService {
	
	/**
	 * Constantes SIGNIN_KEY => Chave de assinatura do token JWT
	 * IMPORTANTE!!!
	 * Não faça essa atribuição em produção
	 * Geralmente esta chave é configurada como variavel 
	 * de ambiente onde a aplicação está sendo executada.
	 * 
	 * Constante EXPIRATION_TIME => Tempo de duração do token JWT
	 */
	private static final String SIGNIN_KEY = "MSulvPs9su0wm1RghtIK8kFE1fLGkXXw";
	private static final int EXPIRATION_TIME = 30;
	
	/**
	 * Constante SIGNIN_KEY_REFRESH => Chave de assinatura do token JWT
	 * IMPORTANTE!!!
	 * Garanta que o valor da SIGNIN_KEY_REFRESH 
	 * seja diferente da SIGNIN_KEY
	 * 
	 * Constante REFRESH_EXPIRATION_TIME => Tempo de duração do token JWT
	 */
	private static final String REFRESH_SIGNIN_KEY = "VMZ7kJajrz9ScEl1w0XJo4LZEYsn37xP";
	private static final int REFRESH_EXIRATION_TIME = 60;
	

	/**
	 * 
	 * Método responsável por gerar o Token JWT
	 * retornando a chamada do metodo generateToken 
	 * com os parametros correspondentes ao Token
	 * 
	 * @param authentication
	 * @return
	 */
	public String generateToken(Authentication authentication) {
		return generateToken(SIGNIN_KEY, authentication.getName(), EXPIRATION_TIME);
	}
	
	/**
	 * Método de geração do refreshToken
	 * 
	 * retorno com a chamada do método generateToken 
	 * com os parametros correspondentes ao refreshToken
	 * 
	 * @param username
	 */
	public String generateRefreshToken(String username) {
		return generateToken(REFRESH_SIGNIN_KEY, username, REFRESH_EXIRATION_TIME);
	}
	
	/**
	 * Método para receber o Token 
	 * e o periodo de sua expiração
	 * 
	 * @param token
	 * @return
	 */
	public Date getExpirationFromToken(String token) {
		Claims claims = getClaims(token, SIGNIN_KEY);
		return claims.getExpiration();
	}
	
	/**
	 * Método para captura do usuário 
	 * que está dentro do Token
	 * 
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		Claims claims = getClaims(token, SIGNIN_KEY);
		return claims.getSubject();
	}
	
	/**
	 * Método que captura o usuario 
	 * que está dentro do refreshToken
	 * 
	 * @param refreshToken
	 * @return
	 */
	public String getUsernameFromRefreshToken(String refreshToken) {
		Claims claims = getClaims(refreshToken, REFRESH_SIGNIN_KEY);
		return claims.getSubject();
	}
	
	/**
	 * 
	 * Método privado para gerar o Token JWT
	 * 
	 * Passo-a-passo:
	 * 
	 * 1. Implementação de HashMap com as claims 
	 * 	  claims => são dados criptografados no TOKEN JWT
	 * 2. currenteDate => data atual do Token
	 * 3. expirationDate => período de expiração do TOKEN
	 * 4. Gerando de fato o TOKEN:
	 * 		A -> .setClaims => inicia configuração do claim 
		 * 	B -> .setSubject => configura o usuario representado pelo token 
		 * 	C -> .setIssuedAt() => configura a data em que o Token foi gerado 
		 * 	D -> .setExpiration() => configura o período de expiração do Token
		 * 	E -> .signWith() => informação da chave de assinatura, 
		 * 		  recebe os parametros dealgoritmo e chave 
		 * 	F -> .compact() => compacta todos os dados e gera o Token Jwt
		 * 
	 * @param signinkey
	 * @param subject
	 * @param expirationTime
	 * @return 
	 */
	private String generateToken(String signinkey, String subject, int expirationTime) {
		
		// 1. Implmentação chave e valor das claims
		Map<String, Object> claims = new HashMap<>();

		// 2. Data atual
		Instant currentDate = Instant.now();
		
		// 3. Período de expiração do Token
		Instant expirationDate = currentDate.plusSeconds(expirationTime);

		 // 4. Gerando de fato o Token utilizando a classe JWTS
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(currentDate.toEpochMilli()))
				.setExpiration(new Date(expirationDate.toEpochMilli()))
				.signWith(SignatureAlgorithm.HS512, signinkey)
				.compact();
	}
	
	/**
	 * Método privado para decodificação do Token
	 * 
	 * .parser() 					=> inicio da decodificação
	 * .setSigningKey(SIGNIN_KEY) 	=> configura a chave tendo como parametro 
	 * 								   o valor da constante SIGNIN_KEY 
	 * .parseClaimsJws(token) 		=> decodifica os dados constando no token
	 * .getBody() 					=> pega o corpo de dados do claim
	 * 
	 * @param token
	 * @return
	 */
	private Claims getClaims(String token, String signinkey) {
		return Jwts.parser()
				.setSigningKey(signinkey)
				.parseClaimsJws(token)
				.getBody();
	}

}
