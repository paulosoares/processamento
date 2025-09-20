package br.gov.stf.estf.alerta.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.alerta.model.util.AlertaSearchData;
import br.gov.stf.estf.entidade.alerta.Alerta;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.model.util.TipoOrdem;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AlertaDao extends GenericDao<Alerta, Long> {

	/**
	 * 
	 * @deprecated Utilizar o método {@link #pesquisarAlerta(AlertaSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<Alerta> pesquisarAlerta(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor, Boolean limitarPesquisa, Boolean oderByProcesso, Boolean oderByProtocolo,
			Boolean oderByDataAndamento, TipoOrdem tipoOrdem) throws DaoException;

	public List<Alerta> pesquisarAlerta(AlertaSearchData searchData) throws DaoException;

	/**
	 * 
	 * @deprecated Utilizar o método {@link #recuperarQuantidadeAlerta(AlertaSearchData)} ao invés deste.
	 */
	@Deprecated
	public Integer recuperarQuantidadeAlerta(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor) throws DaoException;

	public Integer recuperarQuantidadeAlerta(AlertaSearchData searchData) throws DaoException;

	public Boolean persistirAlerta(Alerta alerta) throws DaoException;

	public Boolean gravaAlertaJDBC(Alerta alerta) throws DaoException;
	
	//Novo método criado por muller.mendes.
	
	public Boolean gravaTodosAlertaJDBC(Usuario usuario) throws DaoException;

}
