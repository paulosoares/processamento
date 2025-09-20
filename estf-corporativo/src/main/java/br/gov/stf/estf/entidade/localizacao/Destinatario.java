package br.gov.stf.estf.entidade.localizacao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
/**
 * Classe responsável pelo mapeamento da tabela JUDICIARIO.DESTINATARIO_ORIGEM utilizada para manter os destinatários
 * de uma origem. Informação utilizada na baixa e expedição.
 * @author RicardoLe
 *
 */
@Entity
@Table(schema = "JUDICIARIO", name = "DESTINATARIO_ORIGEM")
public class Destinatario extends ESTFBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Origem origem;
	private String nomDestinatario;
	private List<EnderecoDestinatario> listaEnderecoDestinatario;
	private List<ContatoDestinatario> listaContatoDestinatario;
	private Boolean ativo;

	@Id
	@Column(name = "SEQ_DESTINATARIO_ORIGEM")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_DESTINATARIO_ORIGEM", allocationSize = 1)
	public Long getId() {
		return (Long) id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "COD_ORIGEM", unique = false, insertable = true, updatable = true)
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	@Column(name = "DSC_DESTINATARIO")
	public String getNomDestinatario() {
		return nomDestinatario;
	}

	public void setNomDestinatario(String nomDestinatario) {
		this.nomDestinatario = nomDestinatario;
	}

	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="destinatario")
    @org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	public List<EnderecoDestinatario> getListaEnderecoDestinatario() {
		return listaEnderecoDestinatario;
	}

	public void setListaEnderecoDestinatario(
			List<EnderecoDestinatario> listaEnderecoDestinatario) {
		this.listaEnderecoDestinatario = listaEnderecoDestinatario;
	}

	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="destinatario")
    @org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	public List<ContatoDestinatario> getListaContatoDestinatario() {
		return listaContatoDestinatario;
	}

	public void setListaContatoDestinatario(
			List<ContatoDestinatario> listaContatoDestinatario) {
		this.listaContatoDestinatario = listaContatoDestinatario;
	}

	/**
	 * Indica se o registro está ativo ou não.
	 */
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
}
