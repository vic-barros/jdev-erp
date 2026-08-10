package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Plano;
import br.com.jdeverp.pro.repository.PlanoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PlanoService {

	@Autowired /* Injeção de dependência */
	private PlanoRepository planoRepository;

	/*
	 * Posso escrever query customizadas e dinâmicas, mais complexas do que no
	 * Repository
	 */
	@PersistenceContext
	private EntityManager entityManager;

	public List<Plano> findAll() {

		return planoRepository.findAll();
	}

	public List<Plano> buscaPorNome(String nome) {
		return planoRepository.buscaPorNome(nome);
	}

	public boolean existePorNome(String nome) {
		return planoRepository.existePorNome(nome);
	}

	public boolean existePorNomeDiferenteId(Long id, String nome) {
		return planoRepository.existePorNomeDiferenteId(id, nome);
	}

	public void deleteById(Long id) {
		planoRepository.deleteById(id);
	}

}