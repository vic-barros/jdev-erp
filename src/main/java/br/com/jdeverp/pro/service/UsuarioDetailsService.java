package br.com.jdeverp.pro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Usuario;
import br.com.jdeverp.pro.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {
	// Carrega o usuário pelo seu Login, pega a Interface do UsuarioRepository e faz
	// a injeção de dependênci apra usá-la (Autowired)

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.buscaPorLogin(username);

		if (usuario != null) {
			return usuario;
		}
		throw new UsernameNotFoundException(username + ": " + "Usuário não encontrado no banco de dados.");
	}

}
