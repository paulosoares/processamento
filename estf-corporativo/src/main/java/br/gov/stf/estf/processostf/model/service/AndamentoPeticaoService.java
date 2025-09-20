package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoPeticao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoPeticaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AndamentoPeticaoService extends GenericService<AndamentoPeticao, Long, AndamentoPeticaoDao> {
	
	public Long recuperarUltimaSequencia(ObjetoIncidente processo) throws ServiceException;
	
	public AndamentoPeticao pesquisar(Andamento andamento, Peticao peticao) throws ServiceException;
}
