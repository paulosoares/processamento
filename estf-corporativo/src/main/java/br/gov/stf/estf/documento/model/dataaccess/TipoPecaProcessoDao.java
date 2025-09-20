package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoPecaProcessoDao extends GenericDao<TipoPecaProcesso, Long> {
	public TipoPecaProcesso recuperar (String sigla) throws DaoException;
	
	public List pesquisarTipoPecaProcessoEletronico () throws DaoException;
	
	// WARN: Método utilizado no digitalizador de peças do e-STF
	public List<TipoPecaProcesso> pesquisarTipoPecaProcessoEletronicoOrdenadoDescricao() throws DaoException;
	
	// WARN: Método utilizado no digitalizador de peças do e-STF
	public TipoPecaProcesso recuperarTipoPecaProcessoEletronico(Long id) throws DaoException;
}
