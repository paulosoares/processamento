package br.gov.stf.estf.entidade.jurisdicionado;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoAssociacao;

@Entity
@Table(schema = "JUDICIARIO", name = "ASSOCIACAO_JURISDICIONADO")
public class AssociacaoJurisdicionado extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private PapelJurisdicionado grupo;
	private PapelJurisdicionado membro;
	private EnumTipoAssociacao tipoAssociacao;
	private Boolean ativo;
	
	@Id
	@Column(name = "SEQ_ASSOCIACAO_JURISDICIONADO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_ASSOCIACAO_JURISDICIONADO", allocationSize = 1)
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	@Column(name = "TIP_ASSOCIACAO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
		@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoAssociacao")})
	public EnumTipoAssociacao getTipoAssociacao() {
		return tipoAssociacao;
	}
	
	public void setTipoAssociacao(EnumTipoAssociacao tipoAssociacao) {
		this.tipoAssociacao = tipoAssociacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO_GRUPO", referencedColumnName="SEQ_PAPEL_JURISDICIONADO", nullable = false, insertable = true, updatable = false)
	public PapelJurisdicionado getGrupo() {
		return grupo;
	}
	
	public void setGrupo(PapelJurisdicionado grupo) {
		this.grupo = grupo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ASSOCIADO_MEMBRO", referencedColumnName="SEQ_PAPEL_JURISDICIONADO", nullable = false, insertable = true, updatable = false)
	public PapelJurisdicionado getMembro() {
		return membro;
	}
	
	public void setMembro(PapelJurisdicionado membro) {
		this.membro = membro;
	}
	@Column(name = "FLG_ATIVO")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean flagAtivo) {
		this.ativo = flagAtivo;
	}


}
