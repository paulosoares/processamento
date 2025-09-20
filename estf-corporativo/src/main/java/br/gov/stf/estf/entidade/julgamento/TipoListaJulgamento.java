package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="TIPO_LISTA_JULGAMENTO",schema="JULGAMENTO")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TipoListaJulgamento extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = -5718036654436605385L;
	
	private String descricao;
	private Boolean ativo;

	public static TipoListaJulgamento LISTAS_DE_DEVOLUCOES_DE_VISTAS_DE_PROCESSOS_EM_LISTA = new TipoListaJulgamento(5L, "Listas de Devoluções de Vistas de Processos em Lista", true);
	public static TipoListaJulgamento LISTAS_DOS_RELATORES_REFERENDOS = new TipoListaJulgamento(7L, "Listas dos Relatores (Referendos)", true);
	public static TipoListaJulgamento LISTAS_DE_DESTAQUES_CANCELADOS = new TipoListaJulgamento(10L, "Listas de Destaques Cancelados", true);

	
	public TipoListaJulgamento() {
		super();
	}

	public TipoListaJulgamento(Long id, String descricao, Boolean ativo) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.ativo = ativo;
	}

	@Id
	@Column( name="SEQ_TIPO_LISTA_JULGAMENTO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_TIPO_LISTA_JULGAMENTO", allocationSize = 1 )	
	public Long getId() {
		return id;
	}
	
	@Column( name="DSC_TIPO_LISTA_JULGAMENTO" , unique=true, nullable=false, insertable=true, updatable=true )
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column( name="FLG_ATIVO", nullable=true, insertable=true, updatable=true, unique=false)   
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoListaJulgamento other = (TipoListaJulgamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
