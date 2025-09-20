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
		// Para identificar se o setor � Coordenadoria de Sess�es da Primeira Turma, verificar se o C�digo do Cap�tulo � 3, que significa que essa se��o
		// publica no Di�rio de Justi�a no cap�tulo 3.
		if (setor.getCodigoCapitulo().equals(3)) {
			andamentoProcesso.setDescricaoObservacaoAndamento("UN�NIME");
		}
	}
}