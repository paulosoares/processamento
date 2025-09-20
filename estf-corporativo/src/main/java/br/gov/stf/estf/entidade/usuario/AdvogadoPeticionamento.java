package br.gov.stf.estf.entidade.usuario;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "SINP", name = "ADVOGADO")
public class AdvogadoPeticionamento extends ESTFBaseEntity<Long> {
	private Blob	assinatura;
	private String	ativo;
	private Long	cpf;
	private String	flagEstagiario;
	private String	nome;
	private String	numeroOAB;


	@Lob
	@Column(name = "BIN_ASSINATURA_ADVOGADO")
	public Blob getAssinatura() {
		return assinatura;
	}


	@Column(name = "FLG_ATIVO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getAtivo() {
		return ativo;
	}


	@Column(name = "NUM_CPF", unique = false, nullable = true, insertable = true, updatable = true)
	public Long getCpf() {
		return cpf;
	}


	@Column(name = "FLG_ESTAGIARIO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getFlagEstagiario() {
		return flagEstagiario;
	}


	@Id
	@Column(name = "SEQ_ADVOGADO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "SINP.SEQ_ADVOGADO", allocationSize = 1)
	public Long getId() {
		return id;
	}


	@Column(name = "NOM_ADVOGADO", unique = false, nullable = false, insertable = false, updatable = false, length = 240)
	public String getNome() {
		return nome;
	}


	@Column(name = "NUM_OAB", unique = false, nullable = false, insertable = true, updatable = true, length = 12)
	public String getNumeroOAB() {
		return numeroOAB;
	}


	public void setAssinatura(Blob assinatura) {
		this.assinatura = assinatura;
	}


	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}


	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}


	public void setFlagEstagiario(String flagEstagiario) {
		this.flagEstagiario = flagEstagiario;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public void setNumeroOAB(String numeroOAB) {
		this.numeroOAB = numeroOAB;
	}


}
