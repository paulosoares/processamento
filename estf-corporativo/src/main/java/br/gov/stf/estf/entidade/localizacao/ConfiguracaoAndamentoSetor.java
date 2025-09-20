package br.gov.stf.estf.entidade.localizacao;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.Andamento;

@Entity
@Table(schema = "EGAB", name = "CONFIGURACAO_ANDAMENTO_SETOR")
public class ConfiguracaoAndamentoSetor extends ESTFBaseEntity<Long> {

	private String descricao;
	private String sigla;
	private Setor setor;
	private Boolean ativo;
	private List<Andamento> tipoAndamentos;

	public enum ConstantSigAndamentoSetor {
		JULGAMENTO_MERITO_PROCESSO("JMP");

		private String sigla;

		private ConstantSigAndamentoSetor(String sigla) {
			this.sigla = sigla;
		}

		public String getSigla() {
			return this.sigla;
		}

	}

	@Id
	@Column(name = "SEQ_CONFIGURACAO_ANDA_SETOR")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EGAB.SEQ_CONFIGURACAO_ANDA_SETOR", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = false, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Column(name = "DSC_CONFIGURACAO_ANDA_SETOR", unique = false, nullable = false, insertable = true, updatable = true, length = 2000)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "SIG_CONFIGURACAO_ANDA_SETOR", unique = false, nullable = true, insertable = true, updatable = true, length = 2000)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "FLG_ATIVO", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(schema = "EGAB", name = "CONFIGURACAO_SETOR_ANDAMENTO", joinColumns = { @JoinColumn(name = "SEQ_CONFIGURACAO_ANDA_SETOR") }, inverseJoinColumns = { @JoinColumn(name = "COD_ANDAMENTO") })
	public List<Andamento> getTipoAndamentos() {
		return tipoAndamentos;
	}

	public void setTipoAndamentos(List<Andamento> tipoAndamentos) {
		this.tipoAndamentos = tipoAndamentos;
	}

}
