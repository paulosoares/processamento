package br.gov.stf.estf.entidade.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="TIPO_GRUPO_CONTROLE", schema="JUDICIARIO")
public class TipoGrupoControle extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = 2474986332877790076L;
	
	private String dscTipoGrupoControle;
	private FlagTipoGrupoAtivo flagTipoGrupoControleAtivo;
	private String dscConsultaComplemento;
	
	
	@Id
	@Column( name="SEQ_TIPO_GRUPO_CONTROLE" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JUDICIARIO.SEQ_TIPO_GRUPO_CONTROLE", allocationSize = 1 )	
	public Long getId() {
		return id;
	}

	@Column(name="DSC_TIPO_GRUPO_CONTROLE")
	public String getDscTipoGrupoControle() {
		return dscTipoGrupoControle;
	}


	public void setDscTipoGrupoControle(String dscTipoGrupoControle) {
		this.dscTipoGrupoControle = dscTipoGrupoControle;
	}

	@Column(name = "FLG_ATIVO")
	@Enumerated(EnumType.STRING)
	public FlagTipoGrupoAtivo getFlagTipoGrupoControleAtivo() {
		return flagTipoGrupoControleAtivo;
	}

	public void setFlagTipoGrupoControleAtivo(
			FlagTipoGrupoAtivo flagTipoGrupoControleAtivo) {
		this.flagTipoGrupoControleAtivo = flagTipoGrupoControleAtivo;
	}

	@Column(name="DSC_CONSULTA_COMPLEMENTO", nullable=true, unique= false, updatable=true, insertable=true)
	public String getDscConsultaComplemento() {
		return dscConsultaComplemento;
	}

	public void setDscConsultaComplemento(String dscConsultaComplemento) {
		this.dscConsultaComplemento = dscConsultaComplemento;
	}
	
	
	public enum FlagTipoGrupoAtivo {
		N("Não"), S("Sim");

		private String descricao;

		FlagTipoGrupoAtivo(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	};
	
}
