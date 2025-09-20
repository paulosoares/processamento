package br.gov.stf.estf.entidade;

/**
 * Representa um agrupamento de entidades, ou seja, uma lista de entidades. Inicialmente foi 
 * criada como uma abstração para lista de textos e lista de processos.
 * 
 * @author Rodrigo Barreiros
 * @since 19.05.2009
 * 
 * @see ListaProcessos
 * @see ListaTextos
 */
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import br.gov.stf.estf.entidade.localizacao.Setor;

public abstract class Lista<T> extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = -955352375476546983L;
	
	/** O nome da lista */
	protected String nome;
	/** Indica se a lista está ou não ativa */
	protected Boolean ativa;
	/** Os elementos que compõem a lista */
	protected Set<T> elementos;
	/** As lista são segmentas por setor. Cada setor possui suas próprias listas */
	protected Setor setor;

	public void addElementos(Collection<T> elementos) {
		if (getElementos() == null) {
			setElementos(new LinkedHashSet<T>());
		}
		getElementos().addAll(elementos);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getAtiva() {
		return ativa;
	}

	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}

    public Setor getSetor() {
        return this.setor;
    }
    
    public void setSetor(Setor setor) {
        this.setor = setor;
    }
    
	public Set<T> getElementos() {
		return elementos;
	}

	public void setElementos(Set<T> elementos) {
		this.elementos = elementos;
	}

}
