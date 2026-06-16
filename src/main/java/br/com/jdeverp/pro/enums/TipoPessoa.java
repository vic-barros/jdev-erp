package br.com.jdeverp.pro.enums;

public enum TipoPessoa {
	PESSOA_FISICA("Pessoa Física"),
	PESSOA_JURIDICA("Pessoa Jurídica");
	
	private final String descricao;
	//Essa é a descrição para mostrar ao cliente
	
	private TipoPessoa(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
