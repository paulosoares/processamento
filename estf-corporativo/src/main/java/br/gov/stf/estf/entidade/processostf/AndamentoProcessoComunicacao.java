package br.gov.stf.estf.entidade.processostf;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.Comunicacao;

@Entity
@Table(name = "ANDAMENTO_PROCESSO_COMUNICACAO", schema = "JUDICIARIO")
public class AndamentoProcessoComunicacao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -5871645711681029675L;

	private Long id;
	private AndamentoProcesso andamentoProcesso;
	private Comunicacao comunicacao;
	private TipoVinculoAndamento tipoVinculoAndamento;

	@Override
	@Id
	@Column(name = "SEQ_ANDAMENTO_PROC_COMUNICACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_ANDAMENTO_PROC_COMUNICACAO",  allocationSize=1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO", unique = false, insertable = true, updatable = true)
	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_COMUNICACAO", unique = false, insertable = true, updatable = true)
	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	@Column(name = "TIP_VINCULO_ANDAMENTO", nullable = true, updatable = false, insertable = false, unique = false, length = 1)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoVinculoAndamento getTipoVinculoAndamento() {
		return tipoVinculoAndamento;
	}
	
	
	public void setTipoVinculoAndamento(TipoVinculoAndamento tipoVinculoAndamento) {
		this.tipoVinculoAndamento = tipoVinculoAndamento;
	}

}
