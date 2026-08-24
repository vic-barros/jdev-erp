package br.com.jdeverp.pro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AlterarSenhaDTO {
	
	@NotNull(message = "O id do usuário é obrigatório.")
	private Long id;
	
	@NotBlank(message = "A senha atual é obrigatória.")
	@Size(min = 5, max = 50, message = "A senha atual deve ter entre 5 e 50 caracteres.")
	private String senhaAtual;
	
	@NotBlank(message = "A nova senha é obrigatória.")
	@Size(min = 5, max = 50, message = "A nova senha deve ter entre 5 e 50 caracteres.")
	private String novaSenha;
	
	@NotBlank(message = "A confirmação de senha é obrigatória.")
	@Size(min = 5, max = 50, message = "A confirmação de senha deve ter entre 5 e 50 caracteres.")
	private String confirmarSenha;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
	

}
