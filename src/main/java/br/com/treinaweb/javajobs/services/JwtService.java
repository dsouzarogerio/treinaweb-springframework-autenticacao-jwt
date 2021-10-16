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
	 * Constante SIGNIN_KEY => Chave de assinatura do token JWT
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
	 * 
	 * Método responsável por gerar o Token JWT
	 * 
	 * Passo-a-passo:
	 * 1. Implementação de HashMap com as claims 
	 * 	  claims => são dados criptografados no TOKEN JWT
	 * 2. currenteDate => data atual do Token
	 * 3. expirationDate => período de expiração do TOKEN
	 * 4. Geração do TOKEN
	 * 
	 * @param authentication
	 * @return
	 */
	public String generateToken(Authentication authentication) {
		//1. Implmentação chave e valor das claims
		Map<String, Object> claims = new HashMap<>();
		
		//2. Data atual
		Instant currentDate = Instant.now();
		//3. Período de expiração do Token
		Instant expirationDate = currentDate.plusSeconds(EXPIRATION_TIME);
		
		/**
		 * 4.  Gerando de fato o Token utilizando a classe JWTS
		 * 
		 * .setClaims 		=> 	inicia configuração do claim
		 * .setSubject 		=> 	configura o usuario representado pelo token
		 * .setIssuedAt() 	=> 	configura a data em que o Token foi gerado
		 * .setExpiration() => 	configura o período de expiração do Token
		 * .signWith() 		=> 	informação da chave de assinatura, recebe os parametros de algoritmo e chave
		 * .compact() 		=> 	compacta todos os dados e gera o Token Jwt
		 */
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(authentication.getName())
				.setIssuedAt(new Date(currentDate.toEpochMilli()))
				.setExpiration(new Date(expirationDate.toEpochMilli()))
				.signWith(SignatureAlgorithm.HS512, SIGNIN_KEY)
				.compact();
	}
	
	/**
	 * Método para receber o Token e o periodo de sua expiração
	 * 
	 * @param token
	 * @return
	 */
	public Date getExpirationFromToken(String token) {
		Claims claims = getClaims(token);
		return claims.getExpiration();
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
	private Claims getClaims(String token) {
		return Jwts.parser()
				.setSigningKey(SIGNIN_KEY)
				.parseClaimsJws(token)
				.getBody();
	}

}
