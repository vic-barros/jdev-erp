package br.com.jdeverp.pro.enums;

public enum UnidadeMedida {
	CENTIMETRO("Centímetro"),
	METRO("Metro"),
	UNIDADE("Unidade"),
	QUILO("Quilo");
	
	private final String descricao;
	//Essa é a descrição para mostrar ao cliente
	
	private UnidadeMedida(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
