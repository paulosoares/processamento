package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PecaProcessoEletronicoComunicacaoDao extends GenericDao<PecaProcessoEletronicoComunicacao, Long>{

	public List<PecaProcessoEletronicoComunicacao> pesquisarPecasPelaComunicacao(Comunicacao comunicacao) throws DaoException;
	
}
