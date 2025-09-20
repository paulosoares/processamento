/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

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

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ItemControle;
import br.gov.stf.estf.entidade.publicacao.Publicacao;

/**
 * @author Paulo.Estevao
 * @since 10/08/2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "ITEM_CONTROLE_PUBLICACAO_DJ")
public class ItemControlePublicacaoDj extends ESTFAuditavelBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5255931055097109007L;
	private ItemControle itemControle;
	private Publicacao publicacao;
	
	private Long id;

	@Override
	@Id
	@Column(name = "SEQ_ITEM_CONTROLE_PUBL_DJ")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_ITEM_CONTROLE_PUBL_DJ", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ITEM_CONTROLE")
	public ItemControle getItemControle() {
		return itemControle;
	}
	
	public void setItemControle(ItemControle itemControle) {
		this.itemControle = itemControle;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DATA_PUBLICACOES")
	public Publicacao getPublicacao() {
		return publicacao;
	}
	
	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

}
