package br.gov.stf.estf.entidade.documento.util;

import java.util.Comparator;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;

public class ComparatorPecaProcessoEletronico implements
		Comparator<PecaProcessoEletronico> {
	private boolean crescente = true;

	public ComparatorPecaProcessoEletronico(boolean crescente) {
		this.crescente = crescente;
	}

	@Override
	public int compare(PecaProcessoEletronico oi1, PecaProcessoEletronico oi2) {
		if (crescente) {
			int valor = 0;
			if(oi1.getNumeroOrdemPeca() < oi2.getNumeroOrdemPeca()){
				valor = -1;
			}else if (oi1.getNumeroOrdemPeca() > oi2.getNumeroOrdemPeca()){
				valor = 1;
			}else if( oi1.getNumeroOrdemPeca() == oi2.getNumeroOrdemPeca()){
				valor = 0;
			}
			return valor;
		}else{
			int valor = 0;
			if(oi1.getNumeroOrdemPeca() < oi2.getNumeroOrdemPeca()){
				valor = 1;
			}else if (oi1.getNumeroOrdemPeca() > oi2.getNumeroOrdemPeca()){
				valor = -1;
			}else if( oi1.getNumeroOrdemPeca() == oi2.getNumeroOrdemPeca()){
				valor = 0;
			}
			return valor;
		}
	}
}
