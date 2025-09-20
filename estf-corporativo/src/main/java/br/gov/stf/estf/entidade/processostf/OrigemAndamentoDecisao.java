package br.gov.stf.estf.entidade.processostf;

import javax.persistence.CascadeType;
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
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;

@Entity
@Table(schema = "STF", name = "ORIGEM_DECISAO")
public class OrigemAndamentoDecisao extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2437078564777863394L;
	private String descricao;
	private Setor setor;
	private Boolean ativo;
	private Ministro ministro;

	public enum ConstanteOrigemDecisao {

		// Presencial
		PLENARIO_VIRTUAL((long) 43, "Plenário Virtual", null, TipoAmbienteConstante.PRESENCIAL), 
		TRIBUNAL_PLENO((long) 2, "Tribunal Pleno", TipoColegiadoConstante.SESSAO_PLENARIA, TipoAmbienteConstante.PRESENCIAL),
		PRIMEIRA_TURMA((long) 3, "Primeira Turma", TipoColegiadoConstante.PRIMEIRA_TURMA, TipoAmbienteConstante.PRESENCIAL), 
		SEGUNDA_TURMA((long) 4, "Segunda Turma", TipoColegiadoConstante.SEGUNDA_TURMA, TipoAmbienteConstante.PRESENCIAL),
		VICE_PRESIDENCIA((long) 44, "Vice-Presidência", null, TipoAmbienteConstante.PRESENCIAL), 
		PRESIDENCIA((long) 1, "Presidência", null, TipoAmbienteConstante.PRESENCIAL),
		
		// Virtual
		PRIMEIRA_TURMA_SESSAO_VIRTUAL(95L, "Primeira Turma - Sessão Virtual", TipoColegiadoConstante.PRIMEIRA_TURMA, TipoAmbienteConstante.VIRTUAL),
		SEGUNDA_TURMA_SESSAO_VIRTUAL(96L, "Segunda Turma - Sessão Virtual", TipoColegiadoConstante.SEGUNDA_TURMA, TipoAmbienteConstante.VIRTUAL),
		TRIBUNAL_PLENO_SESSAO_VIRTUAL(94L, "Tribunal Pleno - Sessão Virtual", TipoColegiadoConstante.SESSAO_PLENARIA, TipoAmbienteConstante.VIRTUAL);

		private Long codigo;
		private String descricao;
		private TipoColegiadoConstante colegiado;
		private TipoAmbienteConstante tipoAmbiente;

		private ConstanteOrigemDecisao(Long codigo, String descricao, TipoColegiadoConstante colegiado, TipoAmbienteConstante tipoAmbiente) {
			this.codigo = codigo;
			this.descricao = descricao;
			this.colegiado = colegiado;
			this.tipoAmbiente = tipoAmbiente;
		}

		public Long getCodigo() {
			return this.codigo;
		}

		public String getDescricao() {
			return descricao;
		}
		
		public TipoColegiadoConstante getColegiado() {
			return colegiado;
		}
		
		public TipoAmbienteConstante getTipoAmbiente() {
			return tipoAmbiente;
		}
		
		public static ConstanteOrigemDecisao valueOf(String siglaColegiado, String siglaTipoAmbiente) {
			for (ConstanteOrigemDecisao origemDecisao : values()) {
				if (origemDecisao.getColegiado() != null && origemDecisao.getTipoAmbiente() != null)
					if (origemDecisao.getColegiado().getSigla().equals(siglaColegiado) && origemDecisao.getTipoAmbiente().getSigla().equals(siglaTipoAmbiente))
						return origemDecisao;
			}
			return null;
		}
		
	}

	@Id
	@Column(name = "SEQ_ORIGEM_DECISAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EGAB.SEQ_ORIGEM_DECISAO", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@Column(name = "DSC_ORIGEM_DECISAO", unique = false, nullable = true, insertable = true, updatable = true, length = 80)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "FLG_ATIVO", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", insertable = false, updatable = false)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO", insertable = false, updatable = false)
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

}