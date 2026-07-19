package br.com.jdeverp.pro.model;

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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "role_usuario", uniqueConstraints = {
		@UniqueConstraint(name = "unique_role_user", columnNames = { "acesso_id", "usuario_id" }) 
		})
@SequenceGenerator(name = "seq_role_usuario", sequenceName = "seq_role_usuario", allocationSize = 1, initialValue = 1)
public class RoleUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role_usuario")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acesso_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "acesso_fk"))
	private Role acesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "usuario_fk"))
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getAcesso() {
		return acesso;
	}

	public void setAcesso(Role acesso) {
		this.acesso = acesso;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

}
