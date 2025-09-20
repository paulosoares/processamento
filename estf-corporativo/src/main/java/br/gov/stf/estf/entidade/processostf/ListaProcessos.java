package br.gov.stf.estf.entidade.processostf;

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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.Lista;
import br.gov.stf.estf.entidade.localizacao.Setor;

/**
 * Representa um agrupamento de processos/recursos, ou seja, uma lista de processos/recursos.
 * 
 * @author Rodrigo Barreiros
 * @since 19.05.2009
 * 
 * @see Lista
 */
@Entity
@Table(schema="EGAB", name="GRUPO_PROCESSO_SETOR")
public class ListaProcessos extends Lista<ObjetoIncidente<?>> {
	
	private static final long serialVersionUID = -9126017002736077875L;

	@Id
    @Column(name="SEQ_GRUPO_PROCESSO_SETOR", insertable = false, updatable = false) 
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EGAB.SEQ_GRUPO_PROCESSO_SETOR", allocationSize=1)
    public Long getId() {
		return id;
    }

	@Column (name="NOM_GRUPO_PROCESSO_SETOR")
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
    
	@ManyToMany(targetEntity=ObjetoIncidente.class, fetch = FetchType.LAZY)
	@JoinTable(
		schema="EGAB",	
		name="PROCESSO_SETOR_GRUPO",
		joinColumns=@JoinColumn(name="SEQ_GRUPO_PROCESSO_SETOR"),
		inverseJoinColumns=@JoinColumn(name="SEQ_OBJETO_INCIDENTE")
	)
	@NotFound(action=NotFoundAction.IGNORE)
	public Set<ObjetoIncidente<?>> getElementos() {
		return elementos;
	}

}
