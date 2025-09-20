package br.gov.stf.estf.entidade.julgamento;

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

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( name="SITUACAO_JULGAMENTO", schema="JULGAMENTO" )
public class SituacaoJulgamento extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = -1726148445785465761L;
	private TipoSituacaoJulgamento tipoSituacaoJulgamento;
	private JulgamentoProcesso julgamentoProcesso;
	private Date dataSituacaoJulgamento;
	private Boolean atual;
	
	@Id
	@Column( name="SEQ_SITUACAO_JULGAMENTO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_SITUACAO_JULGAMENTO", allocationSize=1 )	
	public Long getId() {
		return id;
	}	
	
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
    @JoinColumn(name="COD_TIPO_SITUACAO_JULGAMENTO", unique=false, nullable=false, insertable=true, updatable=true )
	public TipoSituacaoJulgamento getTipoSituacaoJulgamento() {
		return tipoSituacaoJulgamento;
	}
	public void setTipoSituacaoJulgamento(
			TipoSituacaoJulgamento tipoSituacaoJulgamento) {
		this.tipoSituacaoJulgamento = tipoSituacaoJulgamento;
	}

	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
    @JoinColumn(name="SEQ_JULGAMENTO_PROCESSO", unique=false, nullable=false, insertable=true, updatable=true )
	public JulgamentoProcesso getJulgamentoProcesso() {
		return julgamentoProcesso;
	}
	public void setJulgamentoProcesso(JulgamentoProcesso julgamentoProcesso) {
		this.julgamentoProcesso = julgamentoProcesso;
	}

	@Temporal( TemporalType.TIMESTAMP )
	@Column( name="DAT_SITUACAO_JULGAMENTO", insertable=true, updatable=true, unique=false, nullable=false, length=7 )
	public Date getDataSituacaoJulgamento() {
		return dataSituacaoJulgamento;
	}
	public void setDataSituacaoJulgamento(Date dataSituacaoJulgamento) {
		this.dataSituacaoJulgamento = dataSituacaoJulgamento;
	}
	
	@Column( name="FLG_ATUAL", insertable=true, updatable=true, nullable=false, unique=false )
	@Type( type="br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )	
	public Boolean getAtual() {
		return atual;
	}
	public void setAtual(Boolean atual) {
		this.atual = atual;
	}


}
