package br.com.treinaweb.javajobs.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.treinaweb.javajobs.models.User;

public class AuthenticatedUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	private User user;
	
	
	public AuthenticatedUser() { }
	
	public AuthenticatedUser(User user) {
		this.user = user;
	}



	public User getUser() {
		return user;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
