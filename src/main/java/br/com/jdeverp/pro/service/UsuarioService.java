package br.com.jdeverp.pro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.dto.AlterarSenhaDTO;
import br.com.jdeverp.pro.dto.LoginDTO;
import br.com.jdeverp.pro.dto.TokenDTO;
import br.com.jdeverp.pro.exception.MsgApiException;
import br.com.jdeverp.pro.model.ClienteFuncionario;
import br.com.jdeverp.pro.model.Role;
import br.com.jdeverp.pro.model.Usuario;
import br.com.jdeverp.pro.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsuarioService {

	@Autowired /* Injeção de dependência */
	private UsuarioRepository usuarioRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@Autowired
	private ClienteFuncionarioService clienteFuncionarioService;

	@Autowired
	private RoleService roleService;

	// Retorna o token de acesso para o ussuário que fez o login
	public TokenDTO login(LoginDTO dto) {
		Usuario usuario = buscaPorLogin(dto.getLogin());

		if (usuario == null) {
			throw new MsgApiException("Usuário não encontrado. ");
		}
		// Explicar porque tem que validar a senha
		boolean senhaValida = passwordEncoder.matches(dto.getSenha(), usuario.getSenha());

		if (!senhaValida) {
			throw new MsgApiException("Senha digitada é inválida. ");
		}

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha()));

		String token = jwtService.gerarToken(usuario);

		usuarioRepository.updateTokenSessaoLogin(usuario.getId(), token);

		return new TokenDTO(token);
	}

	// Utilizado o UsuarioLogadoService para gravar um novo usuário, pois o mesmo já
	// tem o id da empresa logada, então não precisa passar o id da empresa como
	// parametro
	public Usuario salvar(Usuario usuario) {
		if (usuarioRepository.existePorLogin(usuario.getLogin(), usuarioLogadoService.getEmpresaIdLogada())) {
			throw new MsgApiException(
					"Já existe um usuário com o mesmo login para a empresa logada, escolha outro login. ");
		}

		if (usuario.getSenha().length() < 5) {
			throw new MsgApiException("A senha deve ter mais de 5 caracteres. ");
		}

		if (usuarioRepository.existePorPessoa(usuario.getClienteFuncionario().getPessoa().getId(),
				usuarioLogadoService.getEmpresaIdLogada())) {
			throw new MsgApiException("Já existe um usuário vinculado a esta pessoa para a empresa logada. ");
		}

		if (usuario.getClienteFuncionario() == null) {
			throw new MsgApiException("O usuário deve estar vinculado a um cliente ou funcionário. ");
		}

		ClienteFuncionario clienteFuncionario = clienteFuncionarioService.findByPessoa(
				usuario.getClienteFuncionario().getPessoa().getId(), usuarioLogadoService.getEmpresaIdLogada());

		List<Role> roles = roleService.buscaPorAcesso("ROLE_USER");
		usuario.setAcessos(roles);
		usuario.setClienteFuncionario(clienteFuncionario);
		usuario.setEmpresa(usuarioLogadoService.getEmpresaLogada());
		usuario = usuarioRepository.saveAndFlush(usuario);

		clienteFuncionario.setUsuario(usuario);
		clienteFuncionarioService.salvar(clienteFuncionario);

		return usuario;

	}
	
	public Usuario atualizar(Usuario usuario) {
		
		
		if (usuarioRepository.existeOutroUsuarioComPessoa(usuario.getClienteFuncionario().getPessoa().getId(), usuario.getId(), usuarioLogadoService.getEmpresaIdLogada())) {
			throw new MsgApiException("Existe outro usuário associado a pessoa que foi selecionada nesta empresa.");
		}
		
		Usuario usuarioBanco = buscarPorId(usuario.getId(), usuarioLogadoService.getEmpresaIdLogada()).get();
		
		if (usuario.getAcessos() == null || usuario.getAcessos().isEmpty()) {
			usuario.setAcessos(usuarioBanco.getAcessos());
		}
		
		
		ClienteFuncionario clienteFuncionario =  clienteFuncionarioService.findByPessoa(usuario.getClienteFuncionario().getPessoa().getId(), usuarioLogadoService.getEmpresaIdLogada());
		
		usuario.setSenha(usuarioBanco.getSenha()); //Mantém a senha do banco porque terá um método específico para alterar a senha do usuário (criptografia)
		usuario.setClienteFuncionario(clienteFuncionario);
		usuario.setEmpresa(usuarioLogadoService.getEmpresaLogada());
		
		return usuarioRepository.save(usuario); 
		
	}
	
public void alterarSenha(AlterarSenhaDTO dto) {
		
		Usuario usuario = usuarioRepository.buscarPorId(dto.getId(), usuarioLogadoService.getEmpresaIdLogada()).get();
		
	  if (usuario == null) {
		  throw new MsgApiException("Usuário não encontrado.");
	  }	
	  
	  if (!dto.getNovaSenha().equals(dto.getConfirmarSenha())) {
		  throw new MsgApiException("A confirmação da senha não confere.");
	  }
	  
	  /*Conferencia se a nova senha igual a do banco e emite msg*/
	  if (passwordEncoder.matches(dto.getNovaSenha(), usuario.getSenha())) {
		  throw new MsgApiException("A nova senha deve ser diferente da atual");
	  }
	  
	  /*Conferencia se senha atual é mesma do banco e autoriza a troca de senha*/
	  if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
		  throw new MsgApiException("Senha atual inválida, é igual a do banco de dados, verifique e tente novamente.");
	  }
	  
	  //Encode serve para criptografar a senha antes de salvar no banco de dados
	  usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
	  
	  //Salva a nova senha no banco de dados passando o usuário com a nova senha criptografada
	  usuarioRepository.saveAndFlush(usuario);
		
	}
	
	

	public List<Usuario> findAll(Long idEmpresa) {

		return usuarioRepository.findAll(idEmpresa);
	}

	public Usuario buscaPorLogin(String login) {
		return usuarioRepository.buscaPorLogin(login);
	}

	public List<Usuario> buscaPorNome(String nome, Long idEmpresa) {
		return usuarioRepository.buscaPorNome(nome, idEmpresa);
	}

	public boolean existePorNome(String nome, Long idEmpresa) {
		return usuarioRepository.existePorNome(nome, idEmpresa);
	}

	public boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
		return usuarioRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
	}

	public void deleteById(Long id, Long idEmpresa) {
		usuarioRepository.deleteById(id, idEmpresa);
	}

	public long deleteAll(Long empresaID) {
		return usuarioRepository.deleteAll(empresaID);
	}

	void deletarAllById(Iterable<Long> ids, Long empresaId) {
		usuarioRepository.deletarAllById(ids, empresaId);
	}

	public List<Usuario> buscarPorIds(Iterable<Long> ids, Long empresaId) {
		return usuarioRepository.buscarPorIds(ids, empresaId);
	}

	boolean existsById(Long id, Long empresaId) {
		return usuarioRepository.existsById(id, empresaId);
	}

	public List<Usuario> listar(Long empresaId) {
		return usuarioRepository.listar(empresaId);
	}

	public Optional<Usuario> buscarPorId(Long id, Long empresaId) {
		return usuarioRepository.buscarPorId(id, empresaId);
	}

	public long total(Long empresaId) {
		return usuarioRepository.total(empresaId);
	}

	public Page<Usuario> listarPaginado(Long empresaId, Pageable pageable) {
		return usuarioRepository.listarPaginado(empresaId, pageable);
	}

}