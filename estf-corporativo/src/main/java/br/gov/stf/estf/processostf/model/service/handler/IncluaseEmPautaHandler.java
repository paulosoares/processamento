package br.gov.stf.estf.processostf.model.service.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.processostf.model.service.OrigemAndamentoDecisaoService;
import br.gov.stf.framework.model.service.ServiceException;

public class IncluaseEmPautaHandler extends AndamentoProcessoHandler {
	
	@Autowired
	private OrigemAndamentoDecisaoService origemAndamentoDecisaoService;

	@Override
	public boolean precisaOrigemDecisao(Andamento andamento) {
		return true;
	}
	
	@Override
	public List<OrigemAndamentoDecisao> getOrigensDecisao(Andamento andamento) throws ServiceException {
		
		List<Long> idsOrigensDecisao = new ArrayList<Long>();
		idsOrigensDecisao.add(OrigemAndamentoDecisao.ConstanteOrigemDecisao.TRIBUNAL_PLENO.getCodigo());
		idsOrigensDecisao.add(OrigemAndamentoDecisao.ConstanteOrigemDecisao.PRIMEIRA_TURMA.getCodigo());
		idsOrigensDecisao.add(OrigemAndamentoDecisao.ConstanteOrigemDecisao.SEGUNDA_TURMA.getCodigo());
		idsOrigensDecisao.add(OrigemAndamentoDecisao.ConstanteOrigemDecisao.VICE_PRESIDENCIA.getCodigo());
		
		return origemAndamentoDecisaoService.pesquisarOrigensDecisao(idsOrigensDecisao);
	}
}