package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TextoPeticaoDao 
extends GenericDao <TextoPeticao, Long> {
	public List<TextoPeticao> pesquisar (Integer numeroMateria, Short anoMateria) throws DaoException;

}