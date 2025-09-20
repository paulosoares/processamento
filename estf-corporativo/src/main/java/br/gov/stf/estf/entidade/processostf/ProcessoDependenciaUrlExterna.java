package br.gov.stf.estf.entidade.processostf;

public class ProcessoDependenciaUrlExterna {
	
	private ProcessoDependencia processoDependencia;
	private String consultaUrlExterna;

	public void setProcessoDependencia(ProcessoDependencia processoDependencia) {
		this.processoDependencia = processoDependencia;
	}

	public ProcessoDependencia getProcessoDependencia() {
		return processoDependencia;
	}

	public void setConsultaUrlExterna(String consultaUrlExterna) {
		this.consultaUrlExterna = consultaUrlExterna;
	}

	public String getConsultaUrlExterna() {
		return consultaUrlExterna;
	}

}
