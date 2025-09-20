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

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

/**
 * Classe que mapeia a tabela JUDICIARIO.MAPEAMENTO_CLASSE_SETOR
 * 
 * @author demetrius.jube
 * 
 */
@Entity
@Table(schema = "JUDICIARIO", name = "MAPEAMENTO_CLASSE_SETOR")
public class MapeamentoClasseSetor extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Setor setor;
	private Classe classe;
	private TipoIncidentePreferencia tipoPreferencia;
	private Boolean ativo;
	private Date dataInicial;
	private Date dataFinal;

	@Override
	@Id
	@Column(name = "SEQ_MAPEAMENTO_CLASSE_SETOR", insertable = false, updatable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_MAPEAMENTO_CLASSE_SETOR", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR")
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_CLASSE")
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_PREFERENCIA")
	public TipoIncidentePreferencia getTipoPreferencia() {
		return tipoPreferencia;
	}

	public void setTipoPreferencia(TipoIncidentePreferencia tipoPreferencia) {
		this.tipoPreferencia = tipoPreferencia;
	}

	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Column(name = "DAT_INICIAL")
	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	@Column(name = "DAT_FINAL")
	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

}
