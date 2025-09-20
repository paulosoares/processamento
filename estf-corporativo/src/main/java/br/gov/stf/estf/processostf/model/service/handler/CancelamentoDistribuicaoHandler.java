package br.gov.stf.estf.processostf.model.service.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processosetor.model.service.DistribuicaoProcessoSetorService;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.estf.processostf.model.service.exception.LancamentoIndevidoException;
import br.gov.stf.framework.model.service.ServiceException;

public class CancelamentoDistribuicaoHandler extends AndamentoProcessoHandler {
	
	@Autowired
	SituacaoMinistroProcessoService situacaoMinistroProcessoService; 
	
	@Autowired
	DistribuicaoProcessoSetorService distribuicaoProcessoSetorService;
	
	@Override
	public void verificarLancamentoIndevido(AndamentoProcesso andamentoProcessoAnulado) throws ServiceException {
		throw new LancamentoIndevidoException("Não é possível desfazer o lançamento do andamento Cancelamento de Distribuição. Nesse caso, o usuário deve redistribuir o processo!");
	}

	@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {

		Ministro ministro = situacaoMinistroProcessoService.recuperarMinistroRelatorAtual(processoAndamento);
		
		if (ministro == null) throw new ServiceException("Esse processo não está distribuído a nenhum ministro.");
		
		// Pesquisar o grupo de distribuição em que está o processo.
		Long grupoDistribuicao = distribuicaoProcessoSetorService.pesquisarGrupoDistribuicao(processoAndamento);

		if (grupoDistribuicao == null) throw new ServiceException("Não foi encontrado nenhum grupo de distribuição para o processo '" + processoAndamento.getIdentificacao() + "'");

		// Atualizar a quantidade no mapa.
		distribuicaoProcessoSetorService.subtrairMapaDistribuicao(1L, grupoDistribuicao, ministro);
			
		// Remover os ministros associados ao processo.
		situacaoMinistroProcessoService.remover(processoAndamento);
	}
}