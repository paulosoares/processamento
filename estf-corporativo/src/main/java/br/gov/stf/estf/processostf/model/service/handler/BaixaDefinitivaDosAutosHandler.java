package br.gov.stf.estf.processostf.model.service.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

public class BaixaDefinitivaDosAutosHandler extends AndamentoBaixaHandler {

	@Autowired
	PecaProcessoEletronicoService pecaProcessoEletronicoService;
	
//	@Autowired
//	ListaRemessaService listaRemessaService;
	
	@Override
	public void preRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem) throws ServiceException {
//		if (pecaProcessoEletronicoService.temPecasPendenteVisualizacao(processoAndamento)) {
//			throw new ServiceException("Não é possível registrar o andamento 7104, pois há peças pendentes de visualização.");
//		}
	}
	

	@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {
		if ( processoAndamento.isEletronico() && pecaProcessoEletronicoService.temPecasPendenteVisualizacao(processoAndamento)) {
			try {
				pecaProcessoEletronicoService.normalizaPecasObjetoIncidente(processoAndamento);
			} catch (DaoException e) {
				throw new ServiceException("Erro ao alterar publicidade das peças pendentes de visualização");
			}
		}
		
		super.posRegistroAndamento(andamentoProcesso, processoAndamento, processosPrincipais, peticao, setor, codigoUsuario, origem, comunicacao);
	}

}