package br.gov.stf.estf.entidade.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@SuppressWarnings("serial")
@Entity
@Table( schema="GLOBAL", name="TIPO_CONFIGURACAO_USUARIO" )
public class TipoConfiguracaoUsuario extends ESTFBaseEntity<Long> {
	
	private String descricao;
	private String sistema;
	private Boolean ativo;
	
	public enum TipoConfiguracaoUsuarioEnum{
		GRUPO_USUARIO((long)1),
		PESQUISA((long) 2),
		PESQUISA_AVANCADA((long) 3);
		private Long codigo;
		
		private TipoConfiguracaoUsuarioEnum(Long codigo){
			this.codigo = codigo;
		}
		
		public Long getCodigo(){
			return this.codigo;
		}
		
	}
	
	@Id
	@Column( name="SEQ_TIPO_CONFIGURACAO_USUARIO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="GLOBAL.SEQ_TIPO_CONFIGURACAO_USUARIO", allocationSize = 1 )	
	public Long getId() {
		return id;
	}
	
	@Column( name="DSC_FINALIDADE", nullable=false )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column( name="SIG_SISTEMA", nullable=false )
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	
    @Column(name="FLG_ATIVA", nullable=false)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	
}