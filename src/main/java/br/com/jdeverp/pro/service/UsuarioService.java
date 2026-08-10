package br.com.jdeverp.pro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Usuario;
import br.com.jdeverp.pro.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsuarioService {

	@Autowired /* Injeção de dependência */
	private UsuarioRepository usuarioRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public List<Usuario> findAll(Long idEmpresa) {

		return usuarioRepository.findAll(idEmpresa);
	}

	public Usuario buscaPorLogin(String login) {
		return usuarioRepository.buscaPorLogin(login);
	}

	public List<Usuario> buscaPorNome(String nome, Long idEmpresa) {
		return usuarioRepository.buscaPorNome(nome, idEmpresa);
	}

	public boolean existePorNome(String nome, Long idEmpresa) {
		return usuarioRepository.existePorNome(nome, idEmpresa);
	}

	public boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
		return usuarioRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
	}

	public void deleteById(Long id, Long idEmpresa) {
		usuarioRepository.deleteById(id, idEmpresa);
	}

	public long deleteAll(Long empresaID) {
		return usuarioRepository.deleteAll(empresaID);
	}

	void deletarAllById(Iterable<Long> ids, Long empresaId) {
		usuarioRepository.deletarAllById(ids, empresaId);
	}

	public List<Usuario> buscarPorIds(Iterable<Long> ids, Long empresaId) {
		return usuarioRepository.buscarPorIds(ids, empresaId);
	}

	boolean existsById(Long id, Long empresaId) {
		return usuarioRepository.existsById(id, empresaId);
	}

	public List<Usuario> listar(Long empresaId) {
		return usuarioRepository.listar(empresaId);
	}

	public Optional<Usuario> buscarPorId(Long id, Long empresaId) {
		return usuarioRepository.buscarPorId(id, empresaId);
	}

	public long total(Long empresaId) {
		return usuarioRepository.total(empresaId);
	}

	public Page<Usuario> listarPaginado(Long empresaId, Pageable pageable) {
		return usuarioRepository.listarPaginado(empresaId, pageable);
	}

}