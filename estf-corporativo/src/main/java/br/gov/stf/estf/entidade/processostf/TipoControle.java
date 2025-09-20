/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(schema = "JUDICIARIO", name = "TIPO_CONTROLE")
public class TipoControle extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3232709873260399767L;
	
	private Long id;
	private String descricao;
	private String sigla;
	private Date dataAtualizacao;
	private List<ItemControle> listaItemControle;
	
	@Id
	@Column(name = "SEQ_TIPO_CONTROLE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.TIPO_CONTROLE", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DSC_TIPO_CONTROLE")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "SIG_TIPO_CONTROLE")
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ATUALIZACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_CONTROLE", referencedColumnName = "SEQ_TIPO_CONTROLE", insertable = false, updatable = false)
	public List<ItemControle> getListaItemControle() {
		return listaItemControle;
	}

	public void setListaItemControle(List<ItemControle> listaItemControle) {
		this.listaItemControle = listaItemControle;
	}
}
