package br.com.jdeverp.pro.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.constraints.br.CNPJ.Format;

import br.com.jdeverp.pro.enums.TipoPessoa;
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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pessoa", uniqueConstraints = {
		@UniqueConstraint(name = "unique_inscricao_estadual", columnNames = "inscEstadual"),
		@UniqueConstraint(name = "unique_cnpj", columnNames = "cnpj"),
		@UniqueConstraint(name = "unique_cpf", columnNames = "cpf"),
		@UniqueConstraint(name = "unique_email", columnNames = "email"),
})
@SequenceGenerator(name = "seq_pessoa", sequenceName = "seq_pessoa", allocationSize = 1, initialValue = 1)
public class Pessoa implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pessoa")
    private Long id;

    
    //Refere-se ao cadastro da empresa em multitanement
    @NotNull(message = "Empresa deve ser informada corretamente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
    private Empresa empresa;
    

    @NotBlank(message = "Nome deve ser informado")
    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "razao_social", nullable = true, length = 200)
    private String razaoSocial;

    @Column(name = "nome_fantasia", nullable = true, length = 200)
    private String nomeFantasia;

    @Column(name = "insc_estadual", nullable = true, length = 200, unique = true)
    private String inscEstadual;

    @CNPJ(format = Format.ALPHANUMERIC, message = "Informe o CNPJ corretamente")
    @Column(length = 50, unique = true)
    private String cnpj;

    @NotBlank(message = "Informe o Telefone corretamente")
    @Column(nullable = false, length = 20)
    private String telefone;

    @CPF(message = "Informe o CPF corretamente")
    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    @Email(message = "E-mail deve ser infromado corretamente")
    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @NotNull(message = "Tipo de Pessoa deve ser informado")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false)
    private TipoPessoa tipoPessoa;

    
    private Boolean ativo = true;

    @NotNull(message = "Data de cadastro deve ser infomada")
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro = LocalDate.now();

    @Column(columnDefinition = "text", nullable = true)
    private String observacao;

    @NotBlank(message = "Informe o CEP corretamente")
    @Column(nullable = false, length = 50)
    private String cep;

    @NotBlank(message = "Informe o nome da rua corretamente")
    @Column(nullable = false, length = 300)
    private String logradouro;

    @NotBlank(message = "Informe o Bairro corretamente")
    @Column(nullable = false, length = 150)
    private String bairro;

    @NotBlank(message = "Informe o Estado corretamente")
    @Column(nullable = false, length = 100)
    private String estado;

    @NotBlank(message = "Informe a Cidade corretamente")
    @Column(nullable = false, length = 100)
    private String cidade;

    @NotBlank(message = "Informe o País corretamente")
    @Column(nullable = false, length = 100)
    private String pais;

    @Column(nullable = true, length = 400)
    private String complemento;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getInscEstadual() {
        return inscEstadual;
    }

    public void setInscEstadual(String inscEstadual) {
        this.inscEstadual = inscEstadual;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}