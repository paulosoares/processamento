package br.gov.stf.estf.entidade.localizacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "JUDICIARIO", name = "CONTATO_DESTINATARIO")
public class ContatoDestinatario extends ESTFBaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Destinatario destinatario;
	private String nomContato;
	private String enderecoEmail;
	private Integer codigoAreaTelefone;
	private Integer numeroTelefone;
	private Integer codigoAreaFax;
	private Integer numeroFax;
	

	@Id
	@Column(name = "SEQ_CONTATO_DESTINATARIO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_CONTATO_DESTINATARIO", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DESTINATARIO_ORIGEM", nullable = false, insertable = true, updatable = false)
	public Destinatario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}

	@Column(name = "NOM_CONTATO")
	public String getNomContato() {
		return nomContato;
	}

	public void setNomContato(String nomContato) {
		this.nomContato = nomContato;
	}

	@Column(name = "DSC_ENDERECO_EMAIL")
	public String getEnderecoEmail() {
		return enderecoEmail;
	}

	public void setEnderecoEmail(String enderecoEmail) {
		this.enderecoEmail = enderecoEmail;
	}

	@Column(name = "COD_AREA_TELEFONE")
	public Integer getCodigoAreaTelefone() {
		return codigoAreaTelefone;
	}

	public void setCodigoAreaTelefone(Integer codigoAreaTelefone) {
		this.codigoAreaTelefone = codigoAreaTelefone;
	}

	@Column(name = "NUM_TELEFONE")
	public Integer getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(Integer numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	@Column(name = "COD_AREA_FAX")
	public Integer getCodigoAreaFax() {
		return codigoAreaFax;
	}

	public void setCodigoAreaFax(Integer codigoAreaFax) {
		this.codigoAreaFax = codigoAreaFax;
	}

	@Column(name = "NUM_FAX")
	public Integer getNumeroFax() {
		return numeroFax;
	}

	public void setNumeroFax(Integer numeroFax) {
		this.numeroFax = numeroFax;
	}
	

}
