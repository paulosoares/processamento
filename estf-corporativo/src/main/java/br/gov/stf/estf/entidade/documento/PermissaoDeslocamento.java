package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(schema = "JUDICIARIO", name = "PERMISSAO_DESLOCAMENTO")
public class PermissaoDeslocamento extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -471783042918213465L;
	private Long id;
	private Setor setorOrigem;
	private Setor setorDestino;
	private Boolean permissao;
	
	@Id
	@Column(name = "SEQ_PERMISSAO_DESLOCAMENTO", insertable = false, updatable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_PERMISSAO_DESLOCAMENTO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "COD_SETOR_ORIGEM", referencedColumnName = "COD_SETOR")
	public Setor getSetorOrigem() {
		return setorOrigem;
	}
	
	public void setSetorOrigem(Setor setorOrigem) {
		this.setorOrigem = setorOrigem;
	}
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COD_SETOR_DESTINO", referencedColumnName = "COD_SETOR")
	public Setor getSetorDestino() {
		return setorDestino;
	}
	
	public void setSetorDestino(Setor setorDestino) {
		this.setorDestino = setorDestino;
	}
	
	@Column(name = "FLG_PERMISSAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPermissao() {
		return permissao;
	}
	
	public void setPermissao(Boolean permissao) {
		this.permissao = permissao;
	}



}
