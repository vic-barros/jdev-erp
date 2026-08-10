package br.com.jdeverp.pro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Pedido;
import br.com.jdeverp.pro.repository.PedidoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PedidoService {

	@Autowired /* Injeção de dependência */
	private PedidoRepository pedidoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public List<Pedido> findAll(Long idEmpresa) {

		return pedidoRepository.findAll(idEmpresa);
	}

	public List<Pedido> buscaPorNumeroPedido(String numeroPedido, Long idEmpresa) {
		return pedidoRepository.buscaPorNumeroPedido(numeroPedido, idEmpresa);
	}

	public boolean existePorNumeroPedido(String numeroPedido, Long idEmpresa) {
		return pedidoRepository.existePorNumeroPedido(numeroPedido, idEmpresa);
	}

	public boolean existePorNumeroPedidoDiferenteId(Long id, String numeroPedido, Long idEmpresa) {
		return pedidoRepository.existePorNumeroPedidoDiferenteId(id, numeroPedido, idEmpresa);
	}

	public void deleteById(Long id, Long idEmpresa) {
		pedidoRepository.deleteById(id, idEmpresa);
	}

	public long deleteAll(Long empresaID) {
		return pedidoRepository.deleteAll(empresaID);
	}

	void deletarAllById(Iterable<Long> ids, Long empresaId) {
		pedidoRepository.deletarAllById(ids, empresaId);
	}

	public List<Pedido> buscarPorIds(Iterable<Long> ids, Long empresaId) {
		return pedidoRepository.buscarPorIds(ids, empresaId);
	}

	boolean existsById(Long id, Long empresaId) {
		return pedidoRepository.existsById(id, empresaId);
	}

	public List<Pedido> listar(Long empresaId) {
		return pedidoRepository.listar(empresaId);
	}

	public Optional<Pedido> buscarPorId(Long id, Long empresaId) {
		return pedidoRepository.buscarPorId(id, empresaId);
	}

	public long total(Long empresaId) {
		return pedidoRepository.total(empresaId);
	}

	public Page<Pedido> listarPaginado(Long empresaId, Pageable pageable) {
		return pedidoRepository.listarPaginado(empresaId, pageable);
	}

}