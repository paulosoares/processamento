package br.gov.stf.estf.assinatura.deslocamento.destinatario;

//import org.hibernate.Hibernate;

import br.gov.stf.estf.entidade.localizacao.Advogado;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
//import br.gov.stf.estf.entidade.processostf.Jurisdicionado;

public class DestinatarioBuilder {

	public static Destinatario build(Object objeto){
		
		if (objeto instanceof Advogado){
			return new DestinatarioAdvogadoAdapter((Advogado) objeto);	
		}else if (objeto instanceof Setor){
			return new DestinatarioSetorAdapter((Setor) objeto);	
		}else if (objeto instanceof Origem){
			return new DestinatarioOrigemAdapter((Origem) objeto);
		}
		throw new IllegalArgumentException("O objeto não pode ser mapeado para Destinatario!");
		
	}
	

}
