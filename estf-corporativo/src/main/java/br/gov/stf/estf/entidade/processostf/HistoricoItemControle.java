/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.usuario.Usuario;

/**
 * @author paulo.estevao
 * @since 27.06.2012
 */
@Entity
@Table(schema = "JUDICIARIO", name = "HISTORICO_ITEM_CONTROLE")
public class HistoricoItemControle extends ESTFAuditavelBaseEntity<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3378116543647967913L;
	
	private Long id;
	private ItemControle itemControle;
	private ObjetoIncidente<?> objetoIncidente;
	private TipoControle tipoControle;
	private TipoSituacaoControle tipoSituacaoControle;
	private Usuario usuario;
	private Date dataHistorico;
	
	@Id
	@Column(name = "SEQ_HISTORICO_ITEM_CONTROLE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_HISTORICO_ITEM_CONTROLE", allocationSize = 1)
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
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_CONTROLE")
	public TipoControle getTipoControle() {
		return tipoControle;
	}
	
	public void setTipoControle(TipoControle tipoControle) {
		this.tipoControle = tipoControle;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_SITUACAO_CONTROLE")
	public TipoSituacaoControle getTipoSituacaoControle() {
		return tipoSituacaoControle;
	}
	
	public void setTipoSituacaoControle(TipoSituacaoControle tipoSituacaoControle) {
		this.tipoSituacaoControle = tipoSituacaoControle;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_USUARIO")
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_HISTORICO")
	public Date getDataHistorico() {
		return dataHistorico;
	}
	
	public void setDataHistorico(Date dataHistorico) {
		this.dataHistorico = dataHistorico;
	}

}
