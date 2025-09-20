package br.gov.stf.estf.processostf.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao.DeslocaPeticaoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.processostf.model.dataaccess.DeslocamentoPeticaoDao;
import br.gov.stf.estf.processostf.model.service.DeslocamentoPeticaoService;
import br.gov.stf.estf.processostf.model.service.PeticaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("deslocamentoPeticaoService")
public class DeslocamentoPeticaoServiceImpl extends GenericServiceImpl<DeslocaPeticao, DeslocaPeticaoId, DeslocamentoPeticaoDao> implements DeslocamentoPeticaoService {

	private final PeticaoService peticaoService;
	
	protected DeslocamentoPeticaoServiceImpl(DeslocamentoPeticaoDao dao, PeticaoService peticaoService) {
		super(dao);
		this.peticaoService = peticaoService;
		// TODO Auto-generated constructor stub
	}

	// retorna apenas um elemento de uma lista
	public DeslocaPeticao recuperarDeslocamentoPeticao(Guia guia) throws ServiceException {

		try {
			List<DeslocaPeticao> deslocaPeticaos = dao.recuperarDeslocamentoPeticaos(guia);
			if (deslocaPeticaos.size() > 0) {
				return deslocaPeticaos.get(0);
			} else {
				return null;
			}
		} catch (DaoException e) {

			throw new ServiceException(e.getMessage());
		}
	}

	public List<DeslocaPeticao> recuperarDeslocamentoPeticaos(Guia guia) throws ServiceException {

		try {
			List<DeslocaPeticao> deslocaPeticaos = dao.recuperarDeslocamentoPeticaos(guia);
			return deslocaPeticaos;
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void salvarRecebimentoPeticao(DeslocaPeticao deslocamentoPeticao) throws ServiceException {

		try {
			// coloca a data de recebimento no deslocamento
			deslocamentoPeticao.setDataRecebimento(new Date());
			Peticao peticao  = peticaoService.recuperarPorId(deslocamentoPeticao.getId().getPeticao().getId());
			peticao.setPendenteDigitalizacao(false);
			peticaoService.salvar(peticao);
			dao.salvar(deslocamentoPeticao);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void persistirDeslocamentoPeticao(DeslocaPeticao deslocamentoPeticao) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removerPeticao(DeslocaPeticao peticao) throws ServiceException {
		try {
			dao.removerPeticao(peticao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<DeslocaPeticao> pesquisarDataRecebimentoGuiaPeticao(Guia guia) throws ServiceException {
		try {
			return dao.pesquisarDataRecebimentoGuiaPeticao(guia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Long pesquisarSetorUltimoDeslocamento(Long seqObjetoIncidente) throws ServiceException {
		try {
			return dao.pesquisarSetorUltimoDeslocamento(seqObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public DeslocaPeticao recuperarUltimoDeslocamentoPeticao(Peticao peticao) throws ServiceException {
		try {
			return dao.recuperarUltimoDeslocamentoPeticao(peticao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<DeslocaPeticao> recuperarDeslocamentoPeticaoRecebimentoExterno(Guia guia) throws ServiceException{
		try {
			return dao.recuperarDeslocamentoPeticaoRecebimentoExterno(guia);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<DeslocaPeticao> recuperarDeslocamentoPeticaoRecebimentoExterno(Peticao peticao) throws ServiceException{
		try {
			return dao.recuperarDeslocamentoPeticaoRecebimentoExterno(peticao);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer recuperarUltimaSequencia(Guia guia) throws ServiceException {
		try {
			return dao.recuperarUltimaSequencia(guia);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}


}
