package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.RoleUsuario;
import br.com.jdeverp.pro.repository.RoleUsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RoleUsuarioService {

	@Autowired /* Injeção de dependência */
	private RoleUsuarioRepository roleUsuarioRepository;

	@PersistenceContext
	private EntityManager entityManager;

	// ====================Métodos específicos para Usuário====================

	public List<RoleUsuario> findAllByUsuario(Long idUsuario, Long idEmpresa) {
		return roleUsuarioRepository.findAllByUsuario(idUsuario, idEmpresa);
	}

	// ====================Métodos específicos para Role====================

	public List<RoleUsuario> findAllByRoleAndEmpresa(Long idRole, Long idEmpresa) {
		return roleUsuarioRepository.findAllByRoleAndEmpresa(idRole, idEmpresa);
	}

	// ====================Métodos de validação====================

	public boolean existePorUsuarioERole(Long idUsuario, Long idRole, Long idEmpresa) {
		return roleUsuarioRepository.existePorUsuarioERole(idUsuario, idRole, idEmpresa);
	}

	// ====================Métodos de deleção====================

	public void deleteById(Long id) {
		roleUsuarioRepository.deleteById(id);
	}

	public void deleteByUsuarioAndRole(Long idUsuario, Long idRole, Long idEmpresa) {
		roleUsuarioRepository.deleteByUsuarioAndRole(idUsuario, idRole, idEmpresa);
	}

}