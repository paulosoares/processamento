package br.gov.stf.estf.entidade.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name="TIPO_GRUPO_CONTROLE_USUARIO", schema="JUDICIARIO")
public class TipoGrupoUsuarioControle extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = -8344088851154178483L;
	
	private Long id;
	private Usuario usuario;
	private TipoGrupoControle tipoGrupoControle;
	private FlagGenericaGrupoUsuario flagAtivo;
	
	@Id
	@Column( name="SEQ_TIPO_GRUPO_CONT_USUARIO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JUDICIARIO.SEQ_TIPO_GRUPO_CONT_USUARIO", allocationSize = 1 )	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_USUARIO", insertable = true, updatable = true)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_TIPO_GRUPO_CONTROLE", insertable = true, updatable = true)
	public TipoGrupoControle getTipoGrupoControle() {
		return tipoGrupoControle;
	}

	public void setTipoGrupoControle(TipoGrupoControle tipoGrupoControle) {
		this.tipoGrupoControle = tipoGrupoControle;
	}

	
	@Column(name = "FLG_ATIVO")
	@Enumerated(EnumType.STRING)
	public FlagGenericaGrupoUsuario getFlagAtivo() {
		return flagAtivo;
	}

	public void setFlagAtivo(FlagGenericaGrupoUsuario flagAtivo) {
		this.flagAtivo = flagAtivo;
	}
	
	
	
	
	public enum FlagGenericaGrupoUsuario {
		N("Não"), S("Sim");

		private String descricao;

		FlagGenericaGrupoUsuario(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	};
	
}
