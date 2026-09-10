package br.com.jdeverp.pro.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jdeverp.pro.dto.LoginDTO;
import br.com.jdeverp.pro.dto.TokenDTO;
import br.com.jdeverp.pro.dto.UsuarioDTO;
import br.com.jdeverp.pro.model.Usuario;
import br.com.jdeverp.pro.service.UsuarioLogadoService;
import br.com.jdeverp.pro.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;
	
	//Enviar dados para salvar no banco (POST)
	//Precisa deixar um usuário como ROLE_ADMIN para poder salvar outro usuário (INSERT no banco em Role_Usuario)
	@PostMapping("/salvar")
	public ResponseEntity<UsuarioDTO> salvar(@RequestBody @Valid UsuarioDTO usuarioDto) {
		
		UsuarioDTO usuarioSalvo = usuarioService.salvar(usuarioDto);
		
		return ResponseEntity.ok(usuarioSalvo);
	}

	/* Ponto de acesso (end-point): /api/usuario/login */
	// Como vamos enviar dados utilizaremos um post
	// Todo endpoint por padrão utiliza ResponseEntity
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO login) {

		TokenDTO tokenDto = usuarioService.login(login);
		
		return ResponseEntity.ok(tokenDto);

	}
	
	//Carregar dados e mostrar os dados no front (GET)
	@GetMapping("/listar")
	public ResponseEntity<List<UsuarioDTO>> listarUsuarios(){
		return ResponseEntity.ok(usuarioService.listar(usuarioLogadoService.getEmpresaIdLogada()));
		
	}
	
	@GetMapping("/buscarPorId/{id}")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable(required = true, value = "id") Long idUser){
		return ResponseEntity.ok(usuarioService.buscarPorIdDto(idUser, usuarioLogadoService.getEmpresaIdLogada()));
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<String> deletePorId(@PathVariable(required = true, value = "id") Long idUser){
		usuarioService.deleteById(idUser, usuarioLogadoService.getEmpresaIdLogada());
		return ResponseEntity.ok("Usuário deletado com sucesso!");
	}

}
