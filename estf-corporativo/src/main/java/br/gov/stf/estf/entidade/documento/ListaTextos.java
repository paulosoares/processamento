
package br.gov.stf.estf.entidade.documento;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.Lista;
import br.gov.stf.estf.entidade.localizacao.Setor;

/**
 * Representa um agrupamento de textos, ou seja, uma lista de textos.
 * 
 * @author Rodrigo Barreiros
 * @since 14.05.2009
 * 
 * @see Lista
 */
@Entity
@Table(schema="DOC", name="LISTA_TEXTO")
public class ListaTextos extends Lista<Texto> {
	
	private static final long serialVersionUID = -955352375476546983L;
	
	@Id
    @Column(name="SEQ_LISTA_TEXTO", insertable = false, updatable = false) 
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "DOC.SEQ_LISTA_TEXTO", allocationSize=1)
    public Long getId() {
		return id;
    }

	@Column (name="DSC_LISTA_TEXTO")
	public String getNome() {
		return nome;
	}

	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtiva() {
		return ativa;
	}

    @ManyToOne(cascade={}, fetch=FetchType.LAZY)    
    @JoinColumn(name="COD_SETOR")
    public Setor getSetor() {
        return this.setor;
    }
    
	@ManyToMany(targetEntity=Texto.class, fetch = FetchType.LAZY)
	@JoinTable(
		schema="DOC",	
		name="TEXTO_LISTA_TEXTO",
		joinColumns=@JoinColumn(name="SEQ_LISTA_TEXTO"),
		inverseJoinColumns=@JoinColumn(name="SEQ_TEXTOS")
	)
	public Set<Texto> getElementos() {
		return elementos;
	}

}
