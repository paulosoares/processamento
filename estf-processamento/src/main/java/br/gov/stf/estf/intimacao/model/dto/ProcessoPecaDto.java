package br.gov.stf.estf.intimacao.model.dto;


import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;

public class ProcessoPecaDto {
	
	private Processo processo;
	private PecaDTO peca;
	public Processo getProcesso() {
		return processo;
	}
	public void setProcesso(Processo processo) {
		this.processo = processo;
	}
	public PecaDTO getPeca() {
		return peca;
	}
	public void setPeca(PecaDTO peca) {
		this.peca = peca;
	}


	
}
