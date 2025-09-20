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
@Table(schema="EGAB",name="FLUXO_PROCESSO_SETOR")
public class FluxoTipoFaseSetor extends ESTFBaseEntity<Long> 
{
	private Setor setor;
	private TipoFaseSetor tipoFaseAntecessor;
	private TipoFaseSetor tipoFaseSucessor;

	@Id
	@Column( name="SEQ_FLUXO_PROCESSO_SETOR" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_FLUXO_PROCESSO_SETOR" ,allocationSize = 1)    
    public Long getId() {
        return id;
    }

    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="COD_SETOR", unique=false, nullable=true, insertable=true, updatable=true)	
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_TIPO_FASE_SETOR_ANTERIOR")
	public TipoFaseSetor getTipoFaseAntecessor() {
		return tipoFaseAntecessor;
	}

	public void setTipoFaseAntecessor(TipoFaseSetor tipoFaseAntecessor) {
		this.tipoFaseAntecessor = tipoFaseAntecessor;
	}

    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_TIPO_FASE_SETOR_SUCESSOR")
	public TipoFaseSetor getTipoFaseSucessor() {
		return tipoFaseSucessor;
	}

	public void setTipoFaseSucessor(TipoFaseSetor tipoFaseSucessor) {
		this.tipoFaseSucessor = tipoFaseSucessor;
	}	
}