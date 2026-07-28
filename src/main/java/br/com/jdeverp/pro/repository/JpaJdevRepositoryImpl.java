package br.com.jdeverp.pro.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class JpaJdevRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements JpaJdevRepository<T, ID> {

	private final Class<T> domainClass; /* Classe model */
	private final EntityManager entityManager; /* é o núcleo da persistência do JPA */

	public JpaJdevRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.domainClass = domainClass;
		this.entityManager = entityManager;

	}

	public JpaJdevRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.domainClass = entityInformation.getJavaType();
		this.entityManager = entityManager;
	}

	@Override
	public Page<T> listarPaginado(Long empresaId, Pageable pageable) {
		String entidade = domainClass.getSimpleName();
		boolean possuiEmpresa = possuiEmpresa();

		String jpql = "from " + entidade;

		if (possuiEmpresa) {
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

		if (possuiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}

		List<T> lista = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<T>(lista, pageable, total(empresaId));

	}

	// Faz a contagem de quantos registros de acordo com a empresa
	@Override
	public long total(Long empresaId) {
		String entidade = domainClass.getSimpleName();
		boolean possuiEmpresa = possuiEmpresa();

		String jpql = "select count(*) from " + entidade;

		if (possuiEmpresa) {
			jpql += " where empresa.id = :empresaId";
		}

		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);

		if (possuiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}

		return query.getSingleResult();
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
