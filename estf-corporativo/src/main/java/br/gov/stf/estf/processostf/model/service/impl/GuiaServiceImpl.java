package br.gov.stf.estf.processostf.model.service.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.GuiaDao;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("guiaService")
public class GuiaServiceImpl extends GenericServiceImpl<Guia, GuiaId, GuiaDao> implements GuiaService {

	public GuiaServiceImpl(GuiaDao dao) {
		super(dao);
	}

	/**
	 * Verifica se a guia é de petição (return true, se o total de petições é maior que zero) 
	 * ou processo (return false, se o total de petições não é maior que zero)
	 * @throws  
	 */
	@Override
	public boolean isPeticao(Guia guia) throws ServiceException {
		try {
		if (recuperarTotalPeticao(guia) > 0) {
//		if (dao.isPeticao(guia)) {
			return true;
		} else {
			return false;
		}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Long recuperarTotalPeticao(Guia guia) throws ServiceException {
		Long totalPeticao = null;
		try {
			totalPeticao = dao.recuperarTotalPeticao(guia);
			return totalPeticao;
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Long recuperarTotalProcesso(Guia guia) throws ServiceException {
		Long totalProcesso = null;
		try {
			totalProcesso = dao.recuperarTotalProcesso(guia);
			return totalProcesso;
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Recupera a quantidade de itens de uma guia (quantidades de processos ou petições)
	 */
	
	@Override
	public Long recuperarTotalItem(Guia guia) throws ServiceException {
		Long total = null;
		try {
			if (this.isPeticao(guia)) {
				total = dao.recuperarTotalPeticao(guia);
			} else {
				total = dao.recuperarTotalProcesso(guia);
			}
			return total;
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Long persistirGuia(Guia guia) throws ServiceException {
		Long numGuia = null;
		try {
			numGuia = dao.persistirGuia(guia);

			return numGuia;

		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void callInserirProcessoPeticaoNaGuia(Guia guia, String tipoObjetoIncidente) throws ServiceException, SQLException {
		try {
			dao.callInserirProcessoPeticaoNaGuia(guia, tipoObjetoIncidente);
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			throw new ServiceException(e);
		} 
	}
	
	/**
	 * o método removerProcessoPeticaoDaGuia(Guia guia) dever ser utilizado como substituto a este.
	 */
	@Deprecated
	public void callremoverProcessoPeticaoNaGuia(Guia guia, String tipoObjetoIncidente) throws ServiceException {
		try {
			dao.callRemoverProcessoPeticaoNaGuia(guia, tipoObjetoIncidente);
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
/*	@Override
	public void removerProcessoPeticaoDaGuia(Guia guia) throws ServiceException {
		try {
			if (isPeticao(guia)) {
				dao.cancelarGuiaPeticao(guia);
			} else {
				dao.cancelarGuiaProcesso(guia);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}*/
	
	@Override
	public void cancelarGuiaPeticao(DeslocaPeticao peticao) throws ServiceException {
		try {
			dao.cancelarGuiaPeticao(peticao);
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void cancelarGuiaProcesso(DeslocaProcesso processo) throws ServiceException {
		try {
			dao.cancelarGuiaProcesso(processo);
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	//
	// Tipo de órgão de destino/origem. Domínio: 1- Advogado, 2- Interno, 3-
	// Externo.
	//
	// TODO: Colocar como método depreciado
	public String callDeslocamento(Guia guia, Long codigoSetorUsuario) throws ServiceException {
		return callDeslocamento(guia, codigoSetorUsuario, false);
	}

	@Override
	public String callDeslocamento(Guia guia, Long codigoSetorUsuario, Boolean recebimentoAutomatico) throws ServiceException {

		String numAnoGuia = null;
		try {
			numAnoGuia = dao.callDeslocamento(guia, codigoSetorUsuario, recebimentoAutomatico);

			return numAnoGuia;

		} catch (DaoException e) {
			// e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public List<Guia> recuperarGuia(Guia guia) throws ServiceException {
		List<Guia> guias = Collections.emptyList();
		try {
			guias = dao.recuperarGuia(guia);
			return guias;

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Guia> getListarDocumentosGuia(GuiaId guiaId)
			throws ServiceException {
		try {
			return dao.getListarDocumentosGuia(guiaId);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Boolean existeEndereco(EnderecoDestinatario end) throws ServiceException{
		try {
			return dao.existeEndereco(end);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	// utilizar recuperarGuia(Guia guia, Processo processo, Peticao peticao)
	@Deprecated
	public List<Guia> recuperarGuia(Guia guia, Processo processo, Peticao peticao, boolean naoRecebidos, String tipoGuia, boolean isProcesso, boolean isPeticao, boolean pesquisarTodos) throws ServiceException {
		List<Guia> guiasProcessos = Collections.emptyList();
		List<Guia> guiasPeticaos = Collections.emptyList();
		List<Guia> guias = Collections.emptyList();
		boolean pesquisarProcesso = false;
		boolean pesquisarPeticao = false;
		boolean temArgumentoProcesso = false;
		boolean temArgumentoPeticao = false;
		boolean temArgumentoGuia = false;
		try {
			// se existir um critério de pesquisa para processo ou se não
			// existir,
			// mas existir o critério guia pesquisar em DeslocaProcesso
			
			temArgumentoGuia = (guia.getId().getNumeroGuia() != null || guia.getId().getAnoGuia() != null || guia.getId().getCodigoOrgaoOrigem()!= null ||
					 			guia.getDataRemessa() != null || guia.getCodigoOrgaoDestino() != null);
			temArgumentoPeticao = (peticao.getNumeroPeticao() != null);
			temArgumentoProcesso = (processo.getNumeroProcessual() != null);
			pesquisarProcesso = (temArgumentoGuia || temArgumentoProcesso);
			pesquisarPeticao = (temArgumentoGuia || temArgumentoPeticao);
				
			if (isProcesso) {
				guiasProcessos = dao.recuperarGuia(guia, processo, naoRecebidos);
			}else if (isPeticao) {
				guiasPeticaos = dao.recuperarGuia(guia, peticao, naoRecebidos);
			}else if(pesquisarTodos){
				guiasProcessos = dao.recuperarGuia(guia, processo, naoRecebidos);
				guiasPeticaos = dao.recuperarGuia(guia, peticao, naoRecebidos);
			}
			
			if ((guiasProcessos.size() > 0) && (guiasPeticaos.size() > 0)) {
				guias = guiasProcessos;
				guias.addAll(guiasPeticaos);
			} else {
				if (guiasProcessos.size() > 0) {
					guias = guiasProcessos;
				} else {
					guias = guiasPeticaos;
				}
			}
			
			return guias;

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	// retorna guias de processos e/ou petiçõs somente a receber ou a receber juntamente com as recebidas.
	@Override
	public List<Guia> recuperarGuia(Guia guia, Processo processo, Peticao peticao) throws ServiceException {
		List<Guia> guiasProcessos = Collections.emptyList();
		List<Guia> guiasPeticaos = Collections.emptyList();
		List<Guia> guias = Collections.emptyList();
		try {
				
			if (processo != null && peticao == null) {
				guiasProcessos = dao.recuperarGuia(guia, processo, true);
			} else if (processo == null && peticao != null) {
				guiasPeticaos = dao.recuperarGuia(guia, peticao, true);
			} else if(processo == null && peticao == null){
				guiasProcessos = dao.recuperarGuia(guia, processo, true);
				guiasPeticaos = dao.recuperarGuia(guia, peticao, true);
			}
			
			if ((guiasProcessos.size() > 0) && (guiasPeticaos.size() > 0)) {
				guias = guiasProcessos;
				guias.addAll(guiasPeticaos);
			} else {
				if (guiasProcessos.size() > 0) {
					guias = guiasProcessos;
				} else {
					guias = guiasPeticaos;
				}
			}
			
			return guias;

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void alterarGuia(Guia guia) throws ServiceException {
		try {
			dao.alterarGuia(guia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * o método removerProcessoPeticaoDaGuia(Guia guia) dever ser utilizado como substituto a este.
	 */
	@Deprecated
	@Override
	public void callRemoverGuia(Guia guia) throws ServiceException {
		if(guia.getTipoGuia().equals("PRO")){
			List<DeslocaProcesso> processos = Collections.emptyList();
			
		}
	}

	@Override
	public Long recuperarProximoNumeroGuia() throws ServiceException {
		Long proximoNumero;
		try {
			proximoNumero = dao.recuperarProximoNumeroGuia();
			return proximoNumero;

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Guia geraGuiaVazia(Guia guia) throws ServiceException {
		try {
			Guia novaGuia = dao.geraGuiaVazia(guia);
			return novaGuia;

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public boolean isEletronico(Guia guia) throws ServiceException {
		try {
			return dao.isEletronico(guia);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Boolean temPermissaoAlterarGuia(Guia guia) throws ServiceException {
		try {
			return dao.temPermissaoAlterarGuia(guia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	
}
