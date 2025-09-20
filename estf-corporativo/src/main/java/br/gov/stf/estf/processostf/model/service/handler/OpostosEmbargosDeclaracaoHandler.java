package br.gov.stf.estf.processostf.model.service.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.framework.model.service.ServiceException;

public class OpostosEmbargosDeclaracaoHandler extends AndamentoProcessoHandler {

	@Autowired
	ProcessoService processoService;
	
	@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {
		
		// Bloquear a baixa.
		processoAndamento.setBaixa(false);
	}

	@Override
	public Long getCodigoRecurso(AndamentoProcesso andamentoProcessoAnterior, boolean isAndamentoVide) throws ServiceException {
		return super.getCodigoRecurso(andamentoProcessoAnterior, true);
	}
}