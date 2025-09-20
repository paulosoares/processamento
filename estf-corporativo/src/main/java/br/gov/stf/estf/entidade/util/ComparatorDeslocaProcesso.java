package br.gov.stf.estf.entidade.util;

import java.util.Comparator;

import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;

/**
 * 
 * Essa classe foi criada para ordernar uma lista de Desloca Processos pelo o
 * numero sequencia.
 * 
 * @author rogerio.nunes
 * 
 */
public class ComparatorDeslocaProcesso implements Comparator<DeslocaProcesso> {

	private boolean crescente = true;
	
	public ComparatorDeslocaProcesso(boolean crescente) {
		this.crescente = crescente;
	}

	@Override
	public int compare(DeslocaProcesso oi1, DeslocaProcesso oi2) {
		if(crescente){
			int valor = 0;
			if(oi1.getNumeroSequencia() < oi2.getNumeroSequencia()){
				valor = -1;
			}else if (oi1.getNumeroSequencia() > oi2.getNumeroSequencia()){
				valor = 1;
			}else if( oi1.getNumeroSequencia() == oi2.getNumeroSequencia()){
				valor = 0;
			}
			return valor;
		}else{
			int valor = 0;
			if(oi1.getNumeroSequencia() < oi2.getNumeroProcesso()){
				valor = 1;
			}else if (oi1.getNumeroSequencia() > oi2.getNumeroSequencia()){
				valor = -1;
			}else if( oi1.getNumeroSequencia() == oi2.getNumeroSequencia()){
				valor = 0;
			}
			return valor;
		}
	}

}
