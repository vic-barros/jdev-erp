package br.com.jdeverp.pro.model;

import java.time.LocalDateTime;

import br.com.jdeverp.pro.enums.PrioridadeChamado;
import br.com.jdeverp.pro.enums.StatusChamado;
import br.com.jdeverp.pro.enums.TipoChamado;
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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "chamado")
@SequenceGenerator(name = "seq_chamado", sequenceName = "seq_chamado", allocationSize = 1, initialValue = 1)
public class Chamado {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_chamado")
	private Long id;

	@NotNull(message = "Tipo do chamado deve ser informado")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoChamado tipoChamado;

	@NotNull(message = "Prioridade do chamado deve ser informada")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PrioridadeChamado prioridade;

	@NotNull(message = "Status do chamado deve ser informado")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusChamado status;

	@NotNull(message = "Título do chamado deve ser informado")
	@Column(nullable = false)
	private String titulo;

	@NotNull(message = "Decrição do chamado deve ser informada")
	@Column(nullable = false)
	private String descricao;

	@NotNull(message = "Data de abertura do chamado é obrigatória")
	@Column(nullable = false)
	private LocalDateTime dataAbertura;

	@NotNull(message = "Data de fechamento do chamado é obrigatória")
	@Column(nullable = false)
	private LocalDateTime dataFechamento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aberto_user_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "aberto_user_fk"))
	private Usuario abertoUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fechado_user_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fechado_user_fk"))
	private Usuario fechadoUser;

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

}
