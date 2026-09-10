package br.com.jdeverp.pro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*DTO (ou Record) - Data Transfer Object = Objeto de transferencia de dados*/
public class UsuarioDTO {

	private Long id;
	private String pessoa;
	private Boolean liberado = true;
	private String empresa;
	private String tipoClienteFuncionario;
	
	//Adicinou ao DTO por conta do método salvar, que precisa dos atributos abaixos porque são obrigatórios, possuem anotação not null
	@NotBlank(message = "Login é obrigatório")
	private String login;
	
	private String senha;
	
	private Long clienteFuncionarioId;
	
	@NotNull(message = "Pessoa deve ser informada para cadastrar o usuário de acesso ao sistema.")
	private Long pessoaId;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPessoa() {
		return pessoa;
	}

	public void setPessoa(String pessoa) {
		this.pessoa = pessoa;
	}

	public Boolean getLiberado() {
		return liberado;
	}

	public void setLiberado(Boolean liberado) {
		this.liberado = liberado;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getTipoClienteFuncionario() {
		return tipoClienteFuncionario;
	}

	public void setTipoClienteFuncionario(String tipoClienteFuncionario) {
		this.tipoClienteFuncionario = tipoClienteFuncionario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Long getClienteFuncionarioId() {
		return clienteFuncionarioId;
	}

	public void setClienteFuncionarioId(Long clienteFuncionarioId) {
		this.clienteFuncionarioId = clienteFuncionarioId;
	}

	public Long getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}
	
	

	
	

}