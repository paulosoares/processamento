package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema="STF", name="CLASSE_CONVERSAO")
public class ClasseConversao extends ESTFBaseEntity<String> {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6732236429197390404L;
	private String classeNova;

    public void setClasseNova(String classeNova) {
        this.classeNova = classeNova;
    }
    
    @Column( name="SIG_CLASSE_NOVA")     
    public String getClasseNova() {
        return classeNova;
    }
    
    @Id
    @Column( name="SIG_CLASSE_VELHA")     
    public String getId() {
        return id;
    }
}
