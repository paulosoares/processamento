package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.jurisprudencia.ItemControlePublicacaoDj;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

@Entity
@Table(schema = "JUDICIARIO", name = "ITEM_CONTROLE")
public class ItemControle extends ESTFAuditavelBaseEntity<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3378116543647967913L;
	
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private TipoControle tipoControle;
	private TipoSituacaoControle tipoSituacaoControle;
	private Usuario usuario;
	private String observacao;
	private Boolean pendencia;
	private ItemControlePublicacaoDj itemControlePublicacaoDj;
	private PecaProcessoEletronico pecaProcessoEletronico;
	
	@Id
	@Column(name = "SEQ_ITEM_CONTROLE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_ITEM_CONTROLE", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
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
	
	@Column(name = "DSC_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	
	//TODO _Não consegui determinar se está sendo usado
	@Column(name = "FLG_PENDENCIA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPendencia() {
		return pendencia;
	}
	
	public void setPendencia(Boolean pendencia) {
		this.pendencia = pendencia;
	}
	
	//TODO _Usado em AcoesService.encaminhar()... mas não parece ter nenhuma função específicas
	@OneToOne(fetch = FetchType.LAZY, mappedBy="itemControle")
	public ItemControlePublicacaoDj getItemControlePublicacaoDj() {
		return itemControlePublicacaoDj;
	}
	
	public void setItemControlePublicacaoDj(ItemControlePublicacaoDj itemControlePublicacaoDj) {
		this.itemControlePublicacaoDj = itemControlePublicacaoDj;
	}
	
	//Usado em itemControleService.buscaListaDeItemUsuario que não é usado por ninguém
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PECA_PROC_ELETRONICO", nullable = true)
	public PecaProcessoEletronico getPecaProcessoEletronico() {
		return pecaProcessoEletronico;
	}
	
	public void setPecaProcessoEletronico(PecaProcessoEletronico pecaProcessoEletronico) {
		this.pecaProcessoEletronico = pecaProcessoEletronico;
	}

	//============================================ MÉTODOS TRANSIENT ============================================	

	//TODO _Não é usado em lugar algum
	@Transient
	public String getNomeMinistroRelator() {
		String nomeRelator = " ";

		Ministro ministroRelatorAtual = getMinistroRelatorAtual();
		if (ministroRelatorAtual != null) {
			nomeRelator = ministroRelatorAtual.getNome();
		}

		return nomeRelator;
	}	
	
	@Transient
	private Ministro getMinistroRelatorAtual() {
		ObjetoIncidente<?> objetoIncidenteUnico = getObjetoIncidente();

		if (objetoIncidenteUnico != null) {
			try {
				return ((Processo) objetoIncidenteUnico.getPrincipal()).getMinistroRelatorAtual();
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}	
	
	//TODO _Usado por getIsEletronico e getSiglaTipoPreferencia que não usados por ninguém
	@Transient
	public Processo getProcesso() {
		return ObjetoIncidenteUtil.getProcesso(getObjetoIncidente());
	}
	
	//TODO _Não é usado em lugar algum
	@Transient
	public IncidenteJulgamento getIncidenteJulgamento() {
		return ObjetoIncidenteUtil.getIncidenteJulgamento(getObjetoIncidente());
	}
	
	//TODO _Usado em getisEletrônico que não é usado em lugar algum
	@Transient
	public Protocolo getProtocolo() {
		return ObjetoIncidenteUtil.getProtocolo(getObjetoIncidente());
	}
	
	//TODO _Não é usado em lugar algum
	@Transient
	public Peticao getPeticao() {
		return ObjetoIncidenteUtil.getPeticao(getObjetoIncidente());
	}
	
	//TODO _Não é usado em lugar algum
	@Transient
	public Boolean getIsEletronico() {
		if ((getProcesso() != null && getProcesso().getTipoMeioProcesso() != null && TipoMeioProcesso.ELETRONICO.getCodigo().
				equals(getProcesso().getTipoMeioProcesso().getCodigo())) || 
				(getProtocolo() != null && getProtocolo().getTipoMeioProcesso() != null && TipoMeioProcesso.ELETRONICO.getCodigo().
						equals(getProtocolo().getTipoMeioProcesso().getCodigo())))
			return true;
		else
			return false;
	}
	
	//TODO _Não é usado em lugar algum
	@Transient
	public String getSiglaTipoPreferencia(){
		StringBuffer siglaPreferencia = new StringBuffer();
		if (getProcesso() != null){
			if (getProcesso().getIncidentePreferencia() != null && getProcesso().getIncidentePreferencia().size() > 0){
				for(IncidentePreferencia incidentePreferencia : getProcesso().getIncidentePreferencia()){
					siglaPreferencia.append(incidentePreferencia.getTipoPreferencia().getSigla());
					siglaPreferencia.append(",");
				}
				siglaPreferencia.deleteCharAt(siglaPreferencia.length()-1);
			}
		}
		return siglaPreferencia.toString();
	}
}
