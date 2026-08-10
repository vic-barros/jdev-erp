package br.com.jdeverp.pro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jdeverp.pro.model.Chamado;
import jakarta.transaction.Transactional;

@Repository
public interface ChamadoRepository extends JpaJdevRepository<Chamado, Long> {

    @Query("select c from Chamado c where c.empresa.id = :idEmpresa")
    List<Chamado> findAll(@Param("idEmpresa") Long idEmpresa);

    @Query("select c from Chamado c where c.empresa.id = :idEmpresa "
            + "and unaccent(upper(trim(c.titulo))) "
            + "like unaccent(upper(concat('%', trim(:titulo), '%'))) ")
    List<Chamado> buscaPorTitulo(@Param("titulo") String titulo, @Param("idEmpresa") Long idEmpresa);

    @Query("select count(c.id) > 0 from Chamado c where c.empresa.id = :idEmpresa "
            + "and unaccent(upper(trim(c.titulo))) = unaccent(upper(trim(:titulo)))")
    boolean existePorTitulo(@Param("titulo") String titulo, @Param("idEmpresa") Long idEmpresa);

    @Query("select count(c.id) > 0 from Chamado c where c.empresa.id = :idEmpresa "
            + "and unaccent(upper(trim(c.titulo))) = unaccent(upper(trim(:titulo))) and c.id <> :id")
    boolean existePorTituloDiferenteId(@Param("id") Long id, @Param("titulo") String titulo,
            @Param("idEmpresa") Long idEmpresa);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Chamado c where c.empresa.id = :idEmpresa and c.id = :id")
    void deleteById(@Param("id") Long id, @Param("idEmpresa") Long idEmpresa);

}
