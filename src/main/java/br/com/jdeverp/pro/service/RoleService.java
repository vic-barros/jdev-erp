package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Role;
import br.com.jdeverp.pro.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	public List<Role> buscaPorAcesso(String acesso) {
		return roleRepository.buscaPorAcesso(acesso);
	}

	public boolean existePorAcesso(String acesso) {
		return roleRepository.existePorAcesso(acesso);
	}

	public boolean existePorAcessoDiferenteId(Long id, String acesso) {
		return roleRepository.existePorAcessoDiferenteId(id, acesso);
	}

	public void deleteById(Long id) {
		roleRepository.deleteById(id);
	}

}
