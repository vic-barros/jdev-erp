package br.com.jdeverp.pro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Pessoa;
import br.com.jdeverp.pro.repository.PessoaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PessoaService {

	@Autowired /* Injeção de dependência */
	private PessoaRepository pessoaRepository;

	/*
	 * Posso escrever query customizadas e dinâmicas, mais complexas do que no
	 * Repository
	 */
	@PersistenceContext
	private EntityManager entityManager;

	public List<Pessoa> findAll(Long idEmpresa) {

		return pessoaRepository.findAll(idEmpresa);
	}

	public List<Pessoa> buscaPorNome(String nome, Long idEmpresa) {
		return pessoaRepository.buscaPorNome(nome, idEmpresa);
	}

	public boolean existePorNome(String nome, Long idEmpresa) {
		return pessoaRepository.existePorNome(nome, idEmpresa);
	}

	public boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
		return pessoaRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
	}

	public void deleteById(Long id, Long idEmpresa) {
		pessoaRepository.deleteById(id, idEmpresa);
	}

	public long deleteAll(Long empresaID) {
		return pessoaRepository.deleteAll(empresaID);
	}

	void deletarAllById(Iterable<Long> ids, Long empresaId) {
		pessoaRepository.deletarAllById(ids, empresaId);
	}

	public List<Pessoa> buscarPorIds(Iterable<Long> ids, Long empresaId) {
		return pessoaRepository.buscarPorIds(ids, empresaId);
	}

	boolean existsById(Long id, Long empresaId) {
		return pessoaRepository.existsById(id, empresaId);
	}

	public List<Pessoa> listar(Long empresaId) {
		return pessoaRepository.listar(empresaId);
	}

	public Optional<Pessoa> buscarPorId(Long id, Long empresaId) {
		return pessoaRepository.buscarPorId(id, empresaId);
	}

	public long total(Long empresaId) {
		return pessoaRepository.total(empresaId);
	}

	public Page<Pessoa> listarPaginado(Long empresaId, Pageable pageable) {
		return pessoaRepository.listarPaginado(empresaId, pageable);
	}

}