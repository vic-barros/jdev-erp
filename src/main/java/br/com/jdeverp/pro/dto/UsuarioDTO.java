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

	
	

}