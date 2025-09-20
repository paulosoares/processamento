package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( name="TIPO_COMPETENCIA_ENVOLVIDO", schema="JULGAMENTO" )
public class TipoCompetenciaEnvolvido extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8551049962948118412L;
	private String descricao;
	private String tipoAtuacao;
	
	
	public enum TipoAtuacaoConstante {		
		MINISTRO_PRESIDENTE("MINISTRO PRESIDENTE", 10L),
		MINISTRO("MINISTRO", 11L),
		SECRETARIO_SESSAO("SECRETARIO DA SESSÃO", 12L),
		COORDENADOR_SESSAO("COORDENADOR DA SESSÃO", 13L),
		SUSTENTAÇÃO_ORAL("SUSTENTAÇÃO ORAL", 14L),
		PROCURADOR_GERAL_REPUBLICA("PROCURADOR GERAL DA REPÚBLICA ", 15L),
		SUB_PROCURADOR_GERAL_REPUBLICA("SUB-PROCURADOR GERAL DA REPÚBLICA", 16L),
		MINISTRO_SUBSTITUTO("MINISTRO SUBSTITUTO", 17L),
		OUTROS("OUTROS", 18L);
		
		private String descricao;
		private Long codigo;
		
		private TipoAtuacaoConstante( String descricao, Long codigo ) {
			this.descricao = descricao;
			this.codigo = codigo;
		}
		
		public String getDescricao() {
			return descricao;
		}

		public Long getCodigo() {
			return codigo;
		}
		
	}
	
	

	@Id
	@Column( name="COD_TIPO_COMPETENCIA" )	
	public Long getId() {
		return id;
	}

	
	@Column( name="DSC_TIPO_COMPETENCIA", unique=false, nullable=false, insertable=true, updatable=true )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column( name="TIP_ATUACAO", unique=false, nullable=false, insertable=true, updatable=true, length=1 )
	public String getTipoAtuacao() {
		return tipoAtuacao;
	}
	public void setTipoAtuacao(String tipoAtuacao) {
		this.tipoAtuacao = tipoAtuacao;
	}

}
