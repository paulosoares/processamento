package br.gov.stf.estf.processostf.model.service.handler;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.ServiceException;

public class DecisaoUnanimeHandler extends AndamentoProcessoHandler {

	@Override
	public void preRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem) throws ServiceException {

		if (andamentoProcesso.getOrigemAndamentoDecisao().getId().longValue() == OrigemAndamentoDecisao.ConstanteOrigemDecisao.PRIMEIRA_TURMA.getCodigo().longValue()) {
			andamentoProcesso.setDescricaoObservacaoAndamento("UNÂNIME");
		}
	}
}