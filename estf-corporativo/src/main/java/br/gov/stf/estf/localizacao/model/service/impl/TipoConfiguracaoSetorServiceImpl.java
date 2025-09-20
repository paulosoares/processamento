package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.TipoConfiguracaoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoConfiguracaoSetorDao;
import br.gov.stf.estf.localizacao.model.service.TipoConfiguracaoSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoConfiguracaoSetorService")
public class TipoConfiguracaoSetorServiceImpl extends GenericServiceImpl<TipoConfiguracaoSetor, Long, TipoConfiguracaoSetorDao>  
implements TipoConfiguracaoSetorService {
	
	
	
	public TipoConfiguracaoSetorServiceImpl(TipoConfiguracaoSetorDao daoTipoConfiguracaoSetor)
	{
		super(daoTipoConfiguracaoSetor);
		
	}

	public TipoConfiguracaoSetor recuperarTipoConfiguracaoSetor(Long id)
	throws ServiceException
	{
		TipoConfiguracaoSetor tipoConfiguracaoRecuperado = null;

		try
		{
			tipoConfiguracaoRecuperado = dao.recuperarTipoConfiguracaoSetor(id);
		}
		catch(DaoException e) {
			throw new ServiceException(e);
		}

		return tipoConfiguracaoRecuperado;
	}
	
	public List<TipoConfiguracaoSetor> pesquisarTipoConfiguracaoSetor(String sigla, Boolean ativo, 
			Long idSetorResticao, Boolean restringirUtilizaEGab, Boolean localizacao, Boolean configuracaoSetor)
	throws ServiceException
	{
		List<TipoConfiguracaoSetor> tiposConfiguracaoPesquisados = null;
		
		try
		{
			tiposConfiguracaoPesquisados = 
				dao.pesquisarTipoConfiguracaoSetor(sigla, ativo, 
						idSetorResticao, restringirUtilizaEGab, localizacao, configuracaoSetor);
		}
		catch(DaoException e) {
			throw new ServiceException(e);
		}
		
		return tiposConfiguracaoPesquisados;
	}
	

}
