package br.gov.stf.estf.processostf.model.service.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoDistribuicaoHandler extends AndamentoIndevidoNaoPermitidoHandler {

	@Autowired
	ProcessoService processoService;
	
	@Override
	public void preRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem) throws ServiceException {
		if (processoService.isProcessoDistribuido(processoAndamento)) {
			throw new ServiceException("Processo já distribuído não pode receber outro andamento de distribuição. Para isso, deve-se cancelar a distribuição ou gerar uma redistribuição.");
		}
	}
}