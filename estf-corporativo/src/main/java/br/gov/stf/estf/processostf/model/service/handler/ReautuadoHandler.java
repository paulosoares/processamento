package br.gov.stf.estf.processostf.model.service.handler;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia.TipoProcessoDependenciaEnum;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.framework.model.service.ServiceException;

public class ReautuadoHandler extends AndamentoProcessoHandler {
	
	@Autowired
	private ProcessoDependenciaService processoDependenciaService; 
	
	@Override
	public String getSituacao(AndamentoProcesso andamentoProcesso) throws ServiceException {
		
		ProcessoDependencia exemplo = new ProcessoDependencia();
		exemplo.setAndamentoProcesso(andamentoProcesso.getId());
		exemplo.setClasseProcesso(andamentoProcesso.getSigClasseProces());
		exemplo.setNumeroProcesso(andamentoProcesso.getNumProcesso());
		exemplo.setTipoDependenciaProcesso(TipoProcessoDependenciaEnum.REAUTUADO.getCodigo());
		
		List<ProcessoDependencia> dependencias = processoDependenciaService.pesquisarPorExemplo(exemplo);
		
		String descricao = "";
		
		boolean achou = false;
		for (Iterator iterator = dependencias.iterator(); iterator.hasNext();) {
			ProcessoDependencia processoDependencia = (ProcessoDependencia) iterator.next();
			descricao += (achou ? ", " : "") + processoDependencia.getClasseProcessoVinculador() + "/" + processoDependencia.getNumeroProcessoVinculador() + " ";
			achou = true;
		} 
		
		return descricao;
	}

	@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {

		throw new ServiceException("Ainda não implementado.");
	}
}