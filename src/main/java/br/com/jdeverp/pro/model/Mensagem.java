package br.com.jdeverp.pro.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "mensagem")
@SequenceGenerator(name = "seq_mensagem", sequenceName = "seq_mensagem", allocationSize = 1, initialValue = 1)
public class Mensagem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mensagem")
	private Long id;

	private LocalDateTime dataEnvio;

	private Boolean lida = false;

	@NotNull(message = "Conteúdo da mensagem deve ser informado")
	@Column(nullable = false, columnDefinition = "text")
	private String conteudo;

	@Column(nullable = false, columnDefinition = "text")
	private String arquivo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chamado_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "chamado_fk"))
	private Chamado chamado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "atendente_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "atendente_fk"))
	private Usuario atendente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "cliente_fk"))
	private Usuario cliente;

	// Refere-se ao cadastro da empresa em multitanement
	@NotNull(message = "Empresa deve ser informada corretamente")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
	private Empresa empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(LocalDateTime dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Boolean getLida() {
		return lida;
	}

	public void setLida(Boolean lida) {
		this.lida = lida;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public Chamado getChamado() {
		return chamado;
	}

	public void setChamado(Chamado chamado) {
		this.chamado = chamado;
	}

	public Usuario getAtendente() {
		return atendente;
	}

	public void setAtendente(Usuario atendente) {
		this.atendente = atendente;
	}

	public Usuario getCliente() {
		return cliente;
	}

	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	

}
