package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.TipoConfiguracaoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoConfiguracaoSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoConfiguracaoSetorService extends GenericService<TipoConfiguracaoSetor, Long, TipoConfiguracaoSetorDao> {

	public TipoConfiguracaoSetor recuperarTipoConfiguracaoSetor(Long id)
	throws ServiceException;
	
	public List<TipoConfiguracaoSetor> pesquisarTipoConfiguracaoSetor(String sigla, Boolean ativo, 
			Long idSetorResticao, Boolean restringirUtilizaEGab, Boolean localizacao, Boolean configuracaoSetor)
	throws ServiceException;
	
	
}
