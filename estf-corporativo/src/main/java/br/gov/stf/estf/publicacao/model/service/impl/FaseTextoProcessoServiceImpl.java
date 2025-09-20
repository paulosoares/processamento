package br.gov.stf.estf.publicacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.estf.publicacao.model.dataaccess.FaseTextoProcessoDao;
import br.gov.stf.estf.publicacao.model.service.FaseTextoProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * Implementação de <code>FaseTextoProcessoService</code>
 * 
 * @author Rodrigo.Barreiros
 * @since 25.05.2009
 */
@Service("faseTextoProcessoService")
public class FaseTextoProcessoServiceImpl extends GenericServiceImpl<FaseTextoProcesso, Long, FaseTextoProcessoDao> implements
		FaseTextoProcessoService {
	public FaseTextoProcessoServiceImpl(FaseTextoProcessoDao dao) {
		super(dao);
	}

	/**
	 * @see br.gov.stf.estf.publicacao.model.service.FaseTextoProcessoService#recuperarUltimaFaseDoTexto(br.gov.stf.estf.entidade.documento.Texto)
	 */
	public FaseTextoProcesso recuperarUltimaFaseDoTexto(Texto texto) throws ServiceException {
		try {
			return dao.recuperarUltimaFaseDoTexto(texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void excluirFasesDoTexto(Texto texto) throws ServiceException {
		List<FaseTextoProcesso> fasesDoTexto = pesquisarFasesDoTexto(texto);
		for (FaseTextoProcesso faseTextoProcesso : fasesDoTexto) {
			faseTextoProcesso.setCabecalhoTexto(null);
		}
		excluirTodos(fasesDoTexto);
	}

	public List<FaseTextoProcesso> pesquisarFasesDoTexto(Texto texto) throws ServiceException {
		try {
			return dao.pesquisarFasesDoTexto(texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
