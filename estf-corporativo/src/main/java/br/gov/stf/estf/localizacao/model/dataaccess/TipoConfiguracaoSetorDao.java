package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.TipoConfiguracaoSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoConfiguracaoSetorDao extends GenericDao<TipoConfiguracaoSetor, Long> {

	public TipoConfiguracaoSetor recuperarTipoConfiguracaoSetor(Long id)
	throws DaoException;
	
	public List<TipoConfiguracaoSetor> pesquisarTipoConfiguracaoSetor(String sigla, Boolean ativo, 
			Long idSetorResticao, Boolean restringirUtilizaEGab, Boolean localizacao, Boolean configuracaoSetor)
	throws DaoException;
	
    
}
