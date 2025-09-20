package br.gov.stf.estf.documento.model.dataaccess;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DeslocamentoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DeslocamentoComunicacaoDao  extends GenericDao<DeslocamentoComunicacao, Long> {
	

	
	public void incluirDeslocamento(Comunicacao comunicacao, Setor setor) throws DaoException;
}
