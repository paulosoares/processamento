package br.gov.stf.estf.processostf.model.service.handler;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.ServiceException;

public class RetiradoPautaHandler extends AndamentoProcessoHandler {

	@Override
	public void preRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem) throws ServiceException {
		// Para identificar se o setor é Coordenadoria de Sessões da Primeira Turma, verificar se o Código do Capítulo é 3, que significa que essa seção
		// publica no Diário de Justiça no capítulo 3.
		if (setor.getCodigoCapitulo().equals(3)) {
			andamentoProcesso.setDescricaoObservacaoAndamento("UNÂNIME");
		}
	}
}