package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TextoPeticaoDao;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TextoPeticaoService extends GenericService <TextoPeticao, Long, TextoPeticaoDao> {
	
	public List<TextoPeticao> pesquisar (Integer numeroMateria, Short anoMateria) throws ServiceException;
	
}