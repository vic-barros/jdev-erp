package br.com.jdeverp.pro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.dto.AlterarSenhaDTO;
import br.com.jdeverp.pro.dto.LoginDTO;
import br.com.jdeverp.pro.dto.TokenDTO;
import br.com.jdeverp.pro.dto.UsuarioDTO;
import br.com.jdeverp.pro.exception.MsgApiException;
import br.com.jdeverp.pro.model.ClienteFuncionario;
import br.com.jdeverp.pro.model.Role;
import br.com.jdeverp.pro.model.RoleUsuario;
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
	
	@Autowired
	private RoleUsuarioService roleUsuarioService;

	// Retorna o token de acesso para o ussuário que fez o login
	public TokenDTO login(LoginDTO dto) {
		Usuario usuario = buscaPorLogin(dto.getLogin());

		if (usuario == null) {
			throw new MsgApiException("Usuário não encontrado.", HttpStatus.UNAUTHORIZED);
		}

		if (!usuario.isEnabled()) {
			throw new MsgApiException("Usuário bloqueado, entre em contato com o administrador do sistema.",
					HttpStatus.UNAUTHORIZED);

		}

		if (usuario.getEmpresa().isBloqueio()) {
			throw new MsgApiException("Empresa bloqueada, entre em contato com o administrador do sistema.",
					HttpStatus.UNAUTHORIZED);
		}
		// Explicar porque tem que validar a senha
		boolean senhaValida = passwordEncoder.matches(dto.getSenha(), usuario.getSenha());

		if (!senhaValida) {
			throw new MsgApiException("Senha digitada é inválida.", HttpStatus.UNAUTHORIZED);
		}

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha()));

		String token = jwtService.gerarToken(usuario);

		usuarioRepository.updateTokenSessaoLogin(usuario.getId(), token, usuario.getEmpresa().getId());

		return new TokenDTO(token);
	}

	// Utilizado o UsuarioLogadoService para gravar um novo usuário, pois o mesmo já
	// tem o id da empresa logada, então não precisa passar o id da empresa como
	// parametro
	public UsuarioDTO salvar(UsuarioDTO usuarioDto) {
		// Só salva Usuário quem é admin

		if (!usuarioLogadoService.isAdmin()) {
			throw new MsgApiException("Apenas usuários com perfil de administrador podem cadastrar novos usuários.");
		}

		if (usuarioRepository.existePorLogin(usuarioDto.getPessoa(), usuarioLogadoService.getEmpresaIdLogada())) {
			throw new MsgApiException(
					"Já existe um usuário com o mesmo login para a empresa logada, escolha outro login. ");
		}

		if (usuarioDto.getSenha().length() < 5) {
			throw new MsgApiException("A senha deve ter mais de 5 caracteres. ");
		}

		if (usuarioRepository.existePorPessoa(usuarioDto.getPessoaId(), usuarioLogadoService.getEmpresaIdLogada())) {
			throw new MsgApiException("Já existe um usuário vinculado a esta pessoa para a empresa logada. ");
		}
		
		ClienteFuncionario clienteFuncionario = clienteFuncionarioService.findByPessoa(usuarioDto.getPessoaId(),
				usuarioLogadoService.getEmpresaIdLogada());

		if (clienteFuncionario == null) {
			throw new MsgApiException("O usuário deve estar vinculado a um cliente ou funcionário. ");
		}

		List<Role> roles = roleService.buscaPorAcesso("ROLE_USER");

		Usuario usuario = new Usuario();

		usuario.setLogin(usuarioDto.getLogin());
		// Precisa codificar a senha antes de salvar no banco de dados, para não salvar
		// a senha em texto puro
		usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));
		usuario.setLiberado(usuarioDto.getLiberado());
		usuario.setClienteFuncionario(clienteFuncionario);
		usuario.setEmpresa(usuarioLogadoService.getEmpresaLogada());
		usuario = usuarioRepository.saveAndFlush(usuario);
		
		for (Role role : roles) {
			RoleUsuario roleUsuario = new RoleUsuario();
			roleUsuario.setAcesso(role);
			roleUsuario.setUsuario(usuario);
			
			roleUsuarioService.salvar(roleUsuario);
		}

		clienteFuncionario.setUsuario(usuario);
		clienteFuncionarioService.salvar(clienteFuncionario);

		usuarioDto.setSenha("Ocultada"); //Não pode expor a senha na rede
		usuarioDto.setClienteFuncionarioId(clienteFuncionario.getId());
		usuarioDto.setEmpresa(clienteFuncionario.getEmpresa().getPessoa().getNome());
		usuarioDto.setTipoClienteFuncionario(clienteFuncionario.getTipoClienteFuncionario().name());
		usuarioDto.setId(usuario.getId());
		return usuarioDto;

	}

	public Usuario atualizar(Usuario usuario) {

		if (!usuarioLogadoService.isAdmin()) {
			throw new MsgApiException("Apenas usuários com perfil de administrador podem cadastrar novos usuários.");
		}

		if (usuarioRepository.existeOutroUsuarioComPessoa(usuario.getClienteFuncionario().getPessoa().getId(),
				usuario.getId(), usuarioLogadoService.getEmpresaIdLogada())) {
			throw new MsgApiException("Existe outro usuário associado a pessoa que foi selecionada nesta empresa.");
		}

		Usuario usuarioBanco = buscarPorId(usuario.getId(), usuarioLogadoService.getEmpresaIdLogada()).get();

		if (usuario.getAcessos() == null || usuario.getAcessos().isEmpty()) {
			usuario.setAcessos(usuarioBanco.getAcessos());
		}

		ClienteFuncionario clienteFuncionario = clienteFuncionarioService.findByPessoa(
				usuario.getClienteFuncionario().getPessoa().getId(), usuarioLogadoService.getEmpresaIdLogada());

		usuario.setSenha(usuarioBanco.getSenha()); // Mantém a senha do banco porque terá um método específico para
													// alterar a senha do usuário (criptografia)
		usuario.setClienteFuncionario(clienteFuncionario);
		usuario.setEmpresa(usuarioLogadoService.getEmpresaLogada());

		return usuarioRepository.save(usuario);

	}

	public void alterarSenha(AlterarSenhaDTO dto) {

		Usuario usuario = usuarioRepository.buscarPorId(dto.getId(), usuarioLogadoService.getEmpresaIdLogada()).get();

		if (usuario == null) {
			throw new MsgApiException("Usuário não encontrado.");
		}

		if (usuario.isEnabled()) {
			throw new MsgApiException("Usuário bloqueado, entre em contato com o administrador do sistema.",
					HttpStatus.UNAUTHORIZED);

		}

		if (usuario.getEmpresa().isBloqueio()) {
			throw new MsgApiException("Empresa bloqueada, entre em contato com o administrador do sistema.",
					HttpStatus.UNAUTHORIZED);
		}

		if (!dto.getNovaSenha().equals(dto.getConfirmarSenha())) {
			throw new MsgApiException("A confirmação da senha não confere.");
		}

		/* Conferencia se a nova senha igual a do banco e emite msg */
		if (passwordEncoder.matches(dto.getNovaSenha(), usuario.getSenha())) {
			throw new MsgApiException("A nova senha deve ser diferente da atual");
		}

		/* Conferencia se senha atual é mesma do banco e autoriza a troca de senha */
		if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
			throw new MsgApiException(
					"Senha atual inválida, é igual a do banco de dados, verifique e tente novamente.");
		}

		// Encode serve para criptografar a senha antes de salvar no banco de dados
		usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));

		// Salva a nova senha no banco de dados passando o usuário com a nova senha
		// criptografada
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

	// Antes de deletar o usuário precisa deletar o cliente_funcionario por cascata,
	// se não vai dar erro no end-point deletar a controller
	public void deleteById(Long id, Long idEmpresa) {
		clienteFuncionarioService.removeUserClienteFuncionarioId(id, idEmpresa);
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

	public List<UsuarioDTO> listar(Long empresaId) {

		List<UsuarioDTO> dtos = new ArrayList<UsuarioDTO>();
		List<Usuario> usuarios = usuarioRepository.listar(empresaId);

		for (Usuario usuario : usuarios) {
			UsuarioDTO dto = new UsuarioDTO();
			dto.setId(usuario.getId());
			dto.setPessoa(usuario.getClienteFuncionario().getPessoa().getNome());
			dto.setLiberado(usuario.isEnabled());
			dto.setEmpresa(usuario.getEmpresa().getPessoa().getNome());
			dto.setTipoClienteFuncionario(usuario.getClienteFuncionario().getTipoClienteFuncionario().name());
			dtos.add(dto);
		}

		return dtos;
	}

	public Optional<Usuario> buscarPorId(Long id, Long empresaId) {
		return usuarioRepository.buscarPorId(id, empresaId);
	}

	// Variação para usar no controller retornando o DTO
	public UsuarioDTO buscarPorIdDto(Long id, Long empresaId) {

		Optional<Usuario> usuario = usuarioRepository.buscarPorId(id, empresaId);

		if (!usuario.isPresent()) {
			throw new MsgApiException("Usuário não encontrado para a empresa logada.");
		}

		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(usuario.get().getId());
		dto.setPessoa(usuario.get().getClienteFuncionario().getPessoa().getNome());
		dto.setLiberado(usuario.get().isEnabled());
		dto.setEmpresa(usuario.get().getEmpresa().getPessoa().getNome());
		dto.setTipoClienteFuncionario(usuario.get().getClienteFuncionario().getTipoClienteFuncionario().name());

		return dto;
	}

	public long total(Long empresaId) {
		return usuarioRepository.total(empresaId);
	}

	public Page<Usuario> listarPaginado(Long empresaId, Pageable pageable) {
		return usuarioRepository.listarPaginado(empresaId, pageable);
	}

}