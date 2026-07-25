package br.com.jdeverp.pro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jdeverp.pro.model.Categoria;
import jakarta.transaction.Transactional;

@Repository
public interface CategoriaRepository extends JpaJdevRepository<Categoria, Long>{

	@Query("select c from Categoria c where c.empresa.id = :idEmpresa")
	List<Categoria> findAll(@Param("idEmpresa") Long idEmpresa);
	
	@Query("select c from categoria c where c.empresa.id = :idEmpresa " 
	+ "and upper(trim(c.nome)) " + "like upper(concat('%', trim(:nome), '%'))")
	List<Categoria> buscaPorNome(@Param("nome") String nome, @Param("idEmpresa") Long idEmpresa);
	
	@Query("select count(c.id) > 0 from Categoria c where c.empresa.id = :idEmpresa " 
		     + "and upper(trim(c.nome)) = upper(trim(:nome))")
	boolean existePorNome(@Param("nome") String nome, @Param("idEmpresa") Long idEmpresa);
	
	@Query("select count(c.id) > 0 from Categoria c where c.empresa.id = :idEmpresa " 
		     + "and upper(trim(c.nome)) = upper(trim(:nome)) and c.id <> :id")
	boolean existePorNomeDiferenteId(@Param("id") Long id, @Param("nome") String nome, @Param("idEmpresa") Long idEmpresa);
	
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from Categoria c where c.empresa.id = :idEmpresa and c.id = :id")
	void deleteById(@Param("id") Long id, @Param("idEmpresa") Long idEmpresa);
}
