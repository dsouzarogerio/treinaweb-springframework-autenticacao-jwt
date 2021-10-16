package br.com.treinaweb.javajobs.dto;

import java.util.Date;

/**
 * Classe de retorno da action de  autenticação
 * 
 * @author dsouzarogerio
 *
 */
public class JwtResponse {
	
	private String token;
	
	private String type;
	
	private Date expiresAt;

	public JwtResponse() {	}

	public JwtResponse(String token, String type, Date expiresAt) {
		this.token = token;
		this.type = type;
		this.expiresAt = expiresAt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}
}
