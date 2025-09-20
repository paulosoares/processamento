/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.IncidenteAnaliseDao;
import br.gov.stf.estf.jurisprudencia.model.service.AcordaoJurisprudenciaService;
import br.gov.stf.estf.jurisprudencia.model.service.IncidenteAnaliseService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Service("incidenteAnaliseService")
public class IncidenteAnaliseServiceImpl extends
		GenericServiceImpl<IncidenteAnalise, Long, IncidenteAnaliseDao>
		implements IncidenteAnaliseService {

	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;

	@Autowired
	private AcordaoJurisprudenciaService acordaoJurisprudenciaService;

	protected IncidenteAnaliseServiceImpl(IncidenteAnaliseDao dao) {
		super(dao);
	}

	@Override
	public IncidenteAnalise recuperar(ObjetoIncidente<?> objetoIncidente)
			throws ServiceException {
		try {
			return dao.recuperar(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] recuperarVisualizacaoSucessivo(
			IncidenteAnalise incidenteAnalise) throws ServiceException {
		try {
			return dao.recuperarVisualizacaoSucessivo(incidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String recuperarInformacaoPublicacao(
			IncidenteAnalise incidenteAnalise) throws ServiceException {
		try {
			return dao.recuperarInformacaoPublicacao(incidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] recuperarVisualizacaoLegislacoes(
			IncidenteAnalise incidenteAnalise) throws ServiceException {
		try {
			return dao.recuperarVisualizacaoLegislacoes(incidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public byte[] recuperarVisualizacaoIndexacaoPrincipal(
			IncidenteAnalise incidenteAnalise) throws ServiceException {
		try {
			return dao
					.recuperarVisualizacaoIndexacaoPrincipal(incidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void carregarAcordaoPublicado(Long seqObjetoIncidente,
			Long seqDataPublicacao) throws ServiceException {
		try {
			dao.carregarAcordaoPublicado(seqObjetoIncidente, seqDataPublicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<IncidenteAnalise> pesquisarSucessivos(
			IncidenteAnalise incidenteAnalise) throws ServiceException {
		try {
			return dao.pesquisarSucessivos(incidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void salvarTipoMateria(IncidenteAnalise incidenteAnalise)
			throws ServiceException {
		try {
			dao.alterar(incidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public IncidenteAnalise recuperarPorObjetoIncidente(Long idObjetoIncidente)
			throws ServiceException {
		return recuperar(objetoIncidenteService
				.recuperarPorId(idObjetoIncidente));
	}

	@Override
	public void salvarIncidenteAnaliseDesvinculado(
			IncidenteAnalise incidenteAnalise) throws ServiceException {
		salvar(incidenteAnalise);
		incluirEntradaSJUR(incidenteAnalise);
	}

	@Override
	public void salvarPublicacao(Long idIncidenteAnalise, String txtPublicacao)
			throws ServiceException {
		
		IncidenteAnalise incidenteAnalise = recuperarPorId(idIncidenteAnalise);
		incidenteAnalise.setPublicacao(txtPublicacao);
		
		salvar(incidenteAnalise);

		if (incidenteAnalise.getIncidenteAnalisePai() == null) {
			acordaoJurisprudenciaService.salvarPublicacao(incidenteAnalise
					.getAcordaoJurisprudencia().getId(), txtPublicacao);
		} else {
			acordaoJurisprudenciaService
					.atualizarDocumentoAcordaoMesmoSentidoAcordaoPrincipal(incidenteAnalise
							.getIncidenteAnalisePai());
		}
	}

	private void incluirEntradaSJUR(IncidenteAnalise incidenteAnalise)
			throws ServiceException {
		acordaoJurisprudenciaService.inserirProcessoPrincipal(incidenteAnalise);
	}

	@Override
	public Boolean temRepercussaoGeral(Long objetoIncidenteId)
			throws ServiceException {

		//TODO Descomentar caso um dia a repercussão geral seja integrada aqui
		return false;
		
/*		try {
			return this.dao.temRepercussaoGeral(objetoIncidenteId);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(
					"Erro ao definir se um processo é uma repercussão geral", e);
		}
*/	}

	@Override
	public Integer quantidadeAcordaosMesmoSentido(Long incidenteAnaliseId) {

		return this.dao.quantidadeAcordaosMesmoSentido(incidenteAnaliseId);
	}

	@Override
	public byte[] recuperarVisualizacaoIndexacaoParagrafo(
			IncidenteAnalise incidenteAnalise, long idParagrafo)
			throws ServiceException {
		try {
			return dao
					.recuperarVisualizacaoIndexacaoParagrafo(incidenteAnalise, idParagrafo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}		
	}

}
