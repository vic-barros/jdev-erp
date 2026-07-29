package br.com.jdeverp.pro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface JpaJdevRepository<T, ID> extends JpaRepository<T, ID> {
	
	//Listar Paginado
	Page<T>listarPaginado(Long empresaId, Pageable pageable);
	
	//Mostrar Total por Empresa
	long total(Long empresaId);
	
	//Buscar por ID
	Optional<T> buscarPorId(ID id, Long empresaId);
	
	//Listar tabela de acordo com a empresa
	List<T> listar(Long empresaId);
	
	//Se existe cadastro pelo ID
	boolean existsById(ID id, long empresaId);
	
	//Consulta cadastro pelo ID no banco
	List<T> buscarPorIds(Iterable<ID> ids, Long empresaId);
	
	//Deletar vários elementos por Id
	void deletarAllById(Iterable<ID> ids, Long empresaId);
	
	//Deletar tabela por inteira - apenas o registro de uma determinada empresa
	long deleteAll(Long empresaId);
	

}
