package br.gov.stf.estf.entidade.publicacao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;

@Entity
@Table(name = "CAPITULOS", schema = "STF")
public class EstruturaPublicacao extends ESTFBaseEntity<EstruturaPublicacao.EstruturaPublicacaoId> {
	public static final Integer COD_CAPITULO_PRESIDENCIA = 1;
	public static final Integer COD_CAPITULO_PLENARIO = 2;
	public static final Integer COD_CAPITULO_PRIMEIRA_TURMA = 3;
	public static final Integer COD_CAPITULO_SEGUNDA_TURMA = 4;
	public static final Integer COD_CAPITULO_TERCEIRA_TURMA = 20;
	public static final Integer COD_CAPITULO_ACORDAOS = 5;
	public static final Integer COD_CAPITULO_SECRETARIA_JUDICIARIA = 6;
	public static final Integer COD_MATERIA_SESSAO_SOLENE = 2;
	public static final Integer COD_MATERIA_SESSAO_ORDINARIA = 3;
	public static final Integer COD_MATERIA_SESSAO_EXTRAORDINARIA = 4;
	public static final Integer COD_MATERIA_PAUTA_JULGAMENTO = 1;
	public static final Integer COD_MATERIA_REPERCUSSAO_GERAL = 7;
	public static final Integer COD_MATERIA_REPUBLICACAO_REPERCUSSAO_GERAL = 11;
	public static final Integer COD_CONTEUDO_RELACAO_PROCESSO = 50;
	
	
	public static final Integer COD_MATERIA_TEXTO_DE_FECHAMENTO_DE_DESPACHOS = 1;
	public static final Integer COD_MATERIA_ORIGINARIOS = 2;
	public static final Integer COD_MATERIA_RECURSOS = 3;
	public static final Integer COD_MATERIA_REPUBLICACOES = 5;
	public static final Integer COD_MATERIA_PROCESSOS_DE_COMPETENCIA_DA_PRESIDENCIA = 7;
	public static final Integer COD_MATERIA_DECISOES_E_DESPACHOS_DO_PRESIDENTE = 10;
	
	

	private static final long serialVersionUID = -3050524227988517802L;

	private Short ordemImpressao;
	private Boolean conteudo;
	private Boolean ativo;
	private String descricao;
	private Short ordemImpressaoCap;
	private ArquivoEletronico arquivoEletronico;

	@Id
	public EstruturaPublicacaoId getId() {
		return id;
	}

	@Column(name = "ORDEM_IMPRESSAO", nullable = false, precision = 3, scale = 0)
	public Short getOrdemImpressao() {
		return this.ordemImpressao;
	}

	public void setOrdemImpressao(Short ordemImpressao) {
		this.ordemImpressao = ordemImpressao;
	}

	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	@Column(name = "FLG_CONTEUDO", length = 1)
	public Boolean getConteudo() {
		return this.conteudo;
	}

	public void setConteudo(Boolean flgConteudo) {
		this.conteudo = flgConteudo;
	}

	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	@Column(name = "FLG_ATIVO", length = 1)
	public Boolean getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Boolean flgAtivo) {
		this.ativo = flgAtivo;
	}

	@Column(name = "DSC_MATERIA", length = 200)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String dscMateria) {
		this.descricao = dscMateria;
	}

	@Column(name = "ORDEM_IMPRESSAO_CAP", precision = 4, scale = 0)
	public Short getOrdemImpressaoCap() {
		return this.ordemImpressaoCap;
	}

	public void setOrdemImpressaoCap(Short ordemImpressaoCap) {
		this.ordemImpressaoCap = ordemImpressaoCap;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO")
	public ArquivoEletronico getArquivoEletronico() {
		return this.arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoTexto) {
		this.arquivoEletronico = arquivoTexto;
	}

	@Embeddable
	public static class EstruturaPublicacaoId implements java.io.Serializable {

		private static final long serialVersionUID = -5686031808465116547L;
		protected Integer codigoCapitulo;
		protected Integer codigoMateria;
		protected Integer codigoConteudo;

		@Column(name = "COD_CAPITULO", nullable = false, precision = 2, scale = 0)
		public Integer getCodigoCapitulo() {
			return this.codigoCapitulo;
		}

		public void setCodigoCapitulo(Integer codCapitulo) {
			this.codigoCapitulo = codCapitulo;
		}

		@Column(name = "COD_MATERIA", nullable = false, precision = 2, scale = 0)
		public Integer getCodigoMateria() {
			return this.codigoMateria;
		}

		public void setCodigoMateria(Integer codMateria) {
			this.codigoMateria = codMateria;
		}

		@Column(name = "COD_CONTEUDO", nullable = false, precision = 2, scale = 0)
		public Integer getCodigoConteudo() {
			return this.codigoConteudo;
		}

		public void setCodigoConteudo(Integer codConteudo) {
			this.codigoConteudo = codConteudo;
		}

		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if ((other == null))
				return false;
			if (!(other instanceof EstruturaPublicacaoId))
				return false;
			EstruturaPublicacaoId castOther = (EstruturaPublicacaoId) other;

			return (this.getCodigoCapitulo() == castOther.getCodigoCapitulo())
					&& (this.getCodigoMateria() == castOther.getCodigoMateria())
					&& (this.getCodigoConteudo() == castOther.getCodigoConteudo());
		}

		public int hashCode() {
			int result = 17;

			result = 37 * result + this.getCodigoCapitulo();
			result = 37 * result + this.getCodigoMateria();
			result = 37 * result + this.getCodigoConteudo();
			return result;
		}

	}
}
