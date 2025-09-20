package br.gov.stf.estf.alerta.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.alerta.model.dataaccess.AlertaDao;
import br.gov.stf.estf.alerta.model.util.AlertaSearchData;
import br.gov.stf.estf.entidade.alerta.Alerta;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.model.util.TipoOrdem;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AlertaService extends GenericService<Alerta, Long, AlertaDao> {

	/**
	 * @deprecated Utilizar o método {@link #pesquisarAlerta(AlertaSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<Alerta> pesquisarAlerta(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor, Boolean limitarPesquisa, Boolean oderByProcesso, Boolean oderByProtocolo,
			Boolean oderByDataAndamento, TipoOrdem tipoOrdem) throws ServiceException;

	public List<Alerta> pesquisarAlerta(AlertaSearchData searchData) throws ServiceException;

	/**
	 * @deprecated Utilizar o método {@link #recuperarQuantidadeAlerta(AlertaSearchData)} ao invés deste.
	 */
	@Deprecated
	public Integer recuperarQuantidadeAlerta(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor) throws ServiceException;

	public Integer recuperarQuantidadeAlerta(AlertaSearchData searchData) throws ServiceException;

	public Boolean gravaAlertaJDBC(List<Alerta> listaAlerta) throws ServiceException;

	public Boolean persistirAlerta(List<Alerta> listaAlerta) throws ServiceException;

	// Novo método criado por muller.mendes
	
	public Boolean gravaTodosAlertaJDBC(Usuario usuario) throws ServiceException;
}

