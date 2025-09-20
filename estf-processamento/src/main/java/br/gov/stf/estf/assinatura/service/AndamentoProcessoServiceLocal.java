package br.gov.stf.estf.assinatura.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos;
import br.gov.stf.framework.model.service.ServiceException;

public interface AndamentoProcessoServiceLocal {

	public void salvarAndamentoBaixa(List<ContainerGuiaProcessos> containerDeGuias, Setor setor) throws ServiceException, Exception;
}
