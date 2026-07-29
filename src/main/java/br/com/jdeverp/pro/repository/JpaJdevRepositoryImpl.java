package br.com.jdeverp.pro.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class JpaJdevRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements JpaJdevRepository<T, ID> {

	private final Class<T> domainClass; /* Classe model */
	private final EntityManager entityManager; /* é o núcleo da persistência do JPA */
	private final boolean multiEmpresa; /* Para não ficar chamando em todos os métodos */

	public JpaJdevRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.domainClass = domainClass;
		this.entityManager = entityManager;
		multiEmpresa = possuiEmpresa(); // Já chamou o método no construtor

	}

	public JpaJdevRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.domainClass = entityInformation.getJavaType();
		this.entityManager = entityManager;
		multiEmpresa = possuiEmpresa();
	}

	@Override
	public Page<T> listarPaginado(Long empresaId, Pageable pageable) {

		String jpql = "from " + domainClass.getSimpleName();

		if (multiEmpresa) {
			jpql += " where empresa.id = :empresaId";
		}

		if (pageable.getSort().isSorted()) {
			jpql += " order by ";

			List<String> orders = new ArrayList<String>();
			for (Sort.Order order : pageable.getSort()) {
				orders.add(order.getProperty() + " " + order.getDirection().name());
			}

			jpql += String.join(",", orders);

		}

		TypedQuery<T> query = entityManager.createQuery(jpql, domainClass);

		if (multiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}

		List<T> lista = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<T>(lista, pageable, total(empresaId));

	}

	// Faz a contagem de quantos registros de acordo com a empresa
	@Override
	public long total(Long empresaId) {

		String jpql = "select count(*) from " + domainClass.getSimpleName();
		if (multiEmpresa) {
			jpql += " where empresa.id = :empresaId";
		}

		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);

		if (multiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}

		return query.getSingleResult();
	}

	@Override
	public Optional<T> buscarPorId(ID id, Long empresaId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<T> listar(Long empresaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(ID id, long empresaId) {
		// TODO Auto-generated method stub
		return false;
	}

	// Vamos precisar de uma dependência, para validar se esses Ids não são vazios
	@Override
	public List<T> buscarPorIds(Iterable<ID> ids, Long empresaId) {
		if (IterableUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}

		String jpql = "from " + domainClass.getSimpleName() + " where id in :ids";

		if (multiEmpresa) {
			jpql += " and empresa.id = :empresaId";
		}

		TypedQuery<T> query = entityManager.createQuery(jpql, domainClass);
		query.setParameter("ids", ids);

		if (multiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}

		return query.getResultList();
	}

	// Deletar todos por ID
	@Override
	public void deletarAllById(Iterable<ID> ids, Long empresaId) {
		String jpql = "delete from " + domainClass.getSimpleName() + " where id in :ids";

		if (multiEmpresa) {
			jpql += " and empresa.id = :empresaId";
		}

		Query query = entityManager.createQuery(jpql);
		query.setParameter("ids", ids);

		if (multiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}
		query.executeUpdate();

	}

	// Deletar todos de acordo com o ID da Empresa
	@Override
	public long deleteAll(Long empresaId) {
		String jpql = "delete from " + domainClass.getSimpleName();

		if (multiEmpresa) {
			jpql += " where empresa.id = :empresaId";
		}

		Query query = entityManager.createQuery(jpql);

		if (multiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}
		return query.executeUpdate();
	}

	private boolean possuiEmpresa() {
		try {
			domainClass.getDeclaredField("empresa");
			return true;
		} catch (NoSuchFieldException e) {
			return false;
		}

	}

}
