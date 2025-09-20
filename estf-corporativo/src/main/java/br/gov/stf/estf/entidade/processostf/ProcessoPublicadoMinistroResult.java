package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;

public class ProcessoPublicadoMinistroResult {
	private ProcessoPublicado processoPublicado;
	private Ministro ministroRelator;
	private Texto texto;
	
	
	public Texto getTexto() {
		return texto;
	}
	public void setTexto(Texto texto) {
		this.texto = texto;
	}
	public ProcessoPublicado getProcessoPublicado() {
		return processoPublicado;
	}
	public void setProcessoPublicado(ProcessoPublicado processoPublicado) {
		this.processoPublicado = processoPublicado;
	}
	public Ministro getMinistroRelator() {
		return ministroRelator;
	}
	public void setMinistroRelator(Ministro ministroRelator) {
		this.ministroRelator = ministroRelator;
	}
	public ProcessoPublicadoMinistroResult(ProcessoPublicado processoPublicado,
			Ministro ministroRelator, Texto texto) {
		super();
		this.processoPublicado = processoPublicado;
		this.ministroRelator = ministroRelator;
		this.texto = texto;
	}
	
	
}
