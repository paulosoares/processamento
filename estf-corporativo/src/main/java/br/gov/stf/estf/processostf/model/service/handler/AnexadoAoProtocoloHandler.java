package br.gov.stf.estf.processostf.model.service.handler;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia.TipoProcessoDependenciaEnum;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.framework.model.service.ServiceException;

public class AnexadoAoProtocoloHandler extends AndamentoProcessoHandler {
	
	@Autowired
	private ProcessoDependenciaService processoDependenciaService; 
	
	@Override
	public String getSituacao(AndamentoProcesso andamentoProcesso) throws ServiceException {
		
		ProcessoDependencia exemplo = new ProcessoDependencia();
		exemplo.setAndamentoProcesso(andamentoProcesso.getId());
		exemplo.setClasseProcesso(andamentoProcesso.getSigClasseProces());
		exemplo.setNumeroProcesso(andamentoProcesso.getNumProcesso());
		exemplo.setTipoDependenciaProcesso(TipoProcessoDependenciaEnum.ANEXADO_O_PROTOCOLO_NO.getCodigo());
		
		List<ProcessoDependencia> dependencias = processoDependenciaService.pesquisarPorExemplo(exemplo);
		
		String descricao = "";
		
		boolean achou = false;
		for (Iterator iterator = dependencias.iterator(); iterator.hasNext();) {
			ProcessoDependencia processoDependencia = (ProcessoDependencia) iterator.next();
			descricao += (achou ? ", " : "") + processoDependencia.getNumeroProtocoloVinculador() + "/" + processoDependencia.getAnoProtocoloVinculador() + " ";
			achou = true;
		} 
		
		return descricao;
	}
}