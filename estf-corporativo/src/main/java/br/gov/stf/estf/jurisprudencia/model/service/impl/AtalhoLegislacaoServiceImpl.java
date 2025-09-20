/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.AtalhoLegislacao;
import br.gov.stf.estf.entidade.jurisprudencia.LegislacaoIncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.TipoLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AtalhoLegislacaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.AtalhoLegislacaoService;
import br.gov.stf.estf.jurisprudencia.model.service.TipoLegislacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
@Service("atalhoLegislacaoService")
public class AtalhoLegislacaoServiceImpl extends GenericServiceImpl<AtalhoLegislacao, AtalhoLegislacao.AtalhoLegislacaoId, AtalhoLegislacaoDao>
		implements AtalhoLegislacaoService {

	@Autowired
	private TipoLegislacaoService tipoLegislacaoService;
	
	protected AtalhoLegislacaoServiceImpl(AtalhoLegislacaoDao dao) {
		super(dao);
	}

	@Override
	public AtalhoLegislacao recuperar(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise)
			throws ServiceException {
		try {
			return dao.recuperar(legislacaoIncidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<AtalhoLegislacao> pesquisarAtalhosLegislacao(String sigla, Long ano) throws ServiceException {
		try {
			return dao.pesquisarAtalhosLegislacao(sigla, ano);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public AtalhoLegislacao recuperar(String numero, Long ano) throws ServiceException {
		try {
			return dao.recuperar(numero, ano);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public AtalhoLegislacao recuperar(String numero, Long ano, Long norma, Long ambito) throws ServiceException {
		try {
			TipoLegislacao tipoNorma;
			TipoLegislacao tipoEscopoLegislacao;
			
			tipoNorma = tipoLegislacaoService.recuperarPorId(norma);
			tipoEscopoLegislacao = tipoLegislacaoService.recuperarPorId(ambito);
			
			return dao.recuperar(numero, ano, tipoNorma, tipoEscopoLegislacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
