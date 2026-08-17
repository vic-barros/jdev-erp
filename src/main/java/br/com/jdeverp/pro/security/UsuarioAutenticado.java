package br.com.jdeverp.pro.security;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.jdeverp.pro.model.Usuario;

public class UsuarioAutenticado implements UserDetails {

	private static final long serialVersionUID = 1L; // Padrão

	// final porque não se altera e a classe de usuário será carregada aqui
	// Usuario que vem do banco de dados
	private final Usuario usuario;

	public UsuarioAutenticado(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return usuario.getAuthorities();
	}

	@Override
	public @Nullable String getPassword() {
		return usuario.getPassword();
	}

	@Override
	public String getUsername() {
		return usuario.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return usuario.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return usuario.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return usuario.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return usuario.isEnabled();
	}
	// Esses métodos estão sendo sobrescritos da Interface implementada
	// (UserDetails)
	// Estamos dando vida aos métodos

}
