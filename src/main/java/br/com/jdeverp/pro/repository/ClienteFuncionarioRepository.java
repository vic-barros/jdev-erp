package br.com.jdeverp.pro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.jdeverp.pro.model.ClienteFuncionario;
import jakarta.transaction.Transactional;

@Repository
public interface ClienteFuncionarioRepository extends JpaJdevRepository<ClienteFuncionario, Long> {

    @Query("select c from ClienteFuncionario c where c.empresa.id = :idEmpresa")
    List<ClienteFuncionario> findAll(@Param("idEmpresa") Long idEmpresa);
    
    @Query("select c from ClienteFuncionario c where c.pessoa.id = :idPessoa and c.empresa.id = :idEmpresa")
    ClienteFuncionario findByPessoa(@Param("idPessoa") Long idPessoa, @Param("idEmpresa") Long idEmpresa);

    @Query("select c from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and unaccent(upper(trim(c.pessoa.nome))) "
            + "like unaccent(upper(concat('%', trim(:nome), '%'))) ")
    List<ClienteFuncionario> buscaPorNome(@Param("nome") String nome, @Param("idEmpresa") Long idEmpresa);

    @Query("select count(c.id) > 0 from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and unaccent(upper(trim(c.pessoa.nome))) = unaccent(upper(trim(:nome)))")
    boolean existePorNome(@Param("nome") String nome, @Param("idEmpresa") Long idEmpresa);

    @Query("select count(c.id) > 0 from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and unaccent(upper(trim(c.pessoa.nome))) = unaccent(upper(trim(:nome))) and c.id <> :id")
    boolean existePorNomeDiferenteId(@Param("id") Long id, @Param("nome") String nome,
            @Param("idEmpresa") Long idEmpresa);

    @Query("select c from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and c.usuario.id = :usuarioId")
    List<ClienteFuncionario> buscaPorUsuarioId(@Param("usuarioId") Long usuarioId, @Param("idEmpresa") Long idEmpresa);

    @Query("select c from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and c.pessoa.id = :pessoaId")
    List<ClienteFuncionario> buscaPorPessoaId(@Param("pessoaId") Long pessoaId, @Param("idEmpresa") Long idEmpresa);

    @Query("select count(c.id) > 0 from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and c.usuario.id = :usuarioId")
    boolean existePorUsuarioId(@Param("usuarioId") Long usuarioId, @Param("idEmpresa") Long idEmpresa);

    @Query("select count(c.id) > 0 from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and c.pessoa.id = :pessoaId")
    boolean existePorPessoaId(@Param("pessoaId") Long pessoaId, @Param("idEmpresa") Long idEmpresa);

    @Query("select count(c.id) > 0 from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and c.usuario.id = :usuarioId and c.id <> :id")
    boolean existePorUsuarioDiferenteId(@Param("id") Long id, @Param("usuarioId") Long usuarioId,
            @Param("idEmpresa") Long idEmpresa);

    @Query("select count(c.id) > 0 from ClienteFuncionario c where c.empresa.id = :idEmpresa "
            + "and c.pessoa.id = :pessoaId and c.id <> :id")
    boolean existePorPessoaDiferenteId(@Param("id") Long id, @Param("pessoaId") Long pessoaId,
            @Param("idEmpresa") Long idEmpresa);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ClienteFuncionario c where c.empresa.id = :idEmpresa and c.id = :id")
    void deleteById(@Param("id") Long id, @Param("idEmpresa") Long idEmpresa);

}
