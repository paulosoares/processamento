package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;

@Entity
@Table(name="COLEGIADO", schema="JUDICIARIO")
public class Colegiado extends ESTFBaseEntity<String> {
	
	public static final String TRIBUNAL_PLENO = "TP";
	public static final String PRIMEIRA_TURMA = "1T";
	public static final String SEGUNDA_TURMA = "2T";
	public static final String TERCEIRA_TURMA = "3T";
	private static final long serialVersionUID = 366051753632956537L;
	private String descricao;
	private Boolean ativo;
	
	public enum TipoColegiadoConstante {
		PRIMEIRA_TURMA("Primeira Turma", Colegiado.PRIMEIRA_TURMA, EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA, "1ªT", "1ª Turma"),
		SEGUNDA_TURMA("Segunda Turma", Colegiado.SEGUNDA_TURMA, EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA, "2ªT", "2ª Turma"),
		TERCEIRA_TURMA("Terceira Turma", Colegiado.TERCEIRA_TURMA, EstruturaPublicacao.COD_CAPITULO_TERCEIRA_TURMA, "3ªT", "3ª Turma"),
		SESSAO_PLENARIA("Plenário", Colegiado.TRIBUNAL_PLENO, EstruturaPublicacao.COD_CAPITULO_PLENARIO, "TP", "Pleno");
		
		private String descricao;
		private String sigla;
		private Integer codigoCapitulo;
		private String siglaJurisprudencia;
		private String siglaEstendida;

		private TipoColegiadoConstante(String descricao, String sigla, Integer codigoCapitulo, String siglaJurisprudencia, String siglaEstendida){
			this.descricao = descricao;
			this.sigla = sigla;
			this.codigoCapitulo = codigoCapitulo;
			this.siglaJurisprudencia = siglaJurisprudencia;
			this.siglaEstendida = siglaEstendida;
		}
		
		public String getDescricao() {
			return this.descricao;
		}

		public String getSigla() {
			return sigla;
		}
		
		public Integer getCodigoCapitulo() {
			return codigoCapitulo;
		}
		
		public String getSiglaJurisprudencia() {
			return siglaJurisprudencia;
		}
		
		public String getSiglaEstendida() {
			return siglaEstendida;
		}

		public void setSiglaEstendida(String siglaEstendida) {
			this.siglaEstendida = siglaEstendida;
		}
		
		public static TipoColegiadoConstante valueOfSigla(String sigla) {
			for (TipoColegiadoConstante colegiado : values()) {
				if (colegiado.getSigla().equals(sigla)) {
					return colegiado;
				}
			}
			return null;
		}
		
		public static TipoColegiadoConstante valueOfCodigoCapitulo(Integer codigoCapitulo) {
			for ( TipoColegiadoConstante colegiado : values() ) {
				if ( colegiado.getCodigoCapitulo().equals( codigoCapitulo ) ) {
					return colegiado;
				}
			}
			return null;
		}
	}
	
	@Id
	@Column(name="COD_COLEGIADO" )
	public String getId() {
		return id;
	}

	@Column( name="DSC_COLEGIADO", nullable=false, updatable=false, insertable=false, unique=true )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column( name="FLG_ATIVO", insertable=false, updatable=false, nullable=false, unique=false )
	@Type( type="br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )	
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
