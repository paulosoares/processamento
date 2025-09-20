package br.gov.stf.estf.processostf.model.service.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.ControleVistaService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.ServiceException;

public class VistaAoMinistroHandler extends AndamentoProcessoHandler {

	@Autowired
	ControleVistaService controleVistaService;
	
	@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {
		
		// Já existe uma trigger que faz isso.
		/*
		ControleVista controleVista = new ControleVista();
		
		controleVista.setCodigoAndamento(andamentoProcesso.getCodigoAndamento().toString());
		controleVista.setDataAndamento(andamentoProcesso.getDataAndamento());
		controleVista.setSiglaClasseProcessual(processoAndamento.getSiglaClasseProcessual());
		controleVista.setNumeroProcessual(processoAndamento.getNumeroProcessual());
		controleVista.setNumeroSequencia(andamentoProcesso.getNumeroSequencia());
		controleVista.setDataVista(new Date());
		controleVista.setCodigoMinistro(andamentoProcesso.getOrigemAndamentoDecisao().getMinistro().getId());
		controleVista.setCodigoOrgaoOrigem(setor.getId());
		controleVista.setReuPreso(false);
		controleVista.setQuantidadeRenovacao(0);
		
		controleVistaService.salvar(controleVista);
		*/
	}
}