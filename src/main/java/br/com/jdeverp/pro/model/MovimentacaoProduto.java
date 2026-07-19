package br.com.jdeverp.pro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.jdeverp.pro.enums.FormaPagamento;
import br.com.jdeverp.pro.enums.TipoMovimentacaoProduto;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "movimentacao_produto")
@SequenceGenerator(name = "seq_movimentacao_produto", sequenceName = "seq_movimentacao_produto", allocationSize = 1, initialValue = 1)
public class MovimentacaoProduto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movimentacao_produto")
	private Long id;

	
	@Column(nullable = false)
	private BigDecimal valor = BigDecimal.ZERO;

	@NotNull(message = "Informe o Tipo de Movimentação do Produto")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoMovimentacaoProduto tipoMovimentacaoProduto;

	@DecimalMin(value = "0.1", message = "Valor mínimo de pelo menos 0.1 deve ser informado")
	@Column(nullable = false)
	private double quantidade = 1.0;

	@Column(nullable = false)
	private LocalDateTime dataMovimento;

	@NotNull(message = "Produto deve ser informado")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
	private Produto produto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pedido_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pedido_fk"))
	private Pedido pedido;

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoMovimentacaoProduto getTipoMovimentacaoProduto() {
		return tipoMovimentacaoProduto;
	}

	public void setTipoMovimentacaoProduto(TipoMovimentacaoProduto tipoMovimentacaoProduto) {
		this.tipoMovimentacaoProduto = tipoMovimentacaoProduto;
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public LocalDateTime getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(LocalDateTime dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	

}
