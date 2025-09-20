package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.processostf.model.util.ConsultaObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.model.service.ServiceException;

public interface ObjetoIncidenteDao extends GenericDao<ObjetoIncidente, Long> {

	public ObjetoIncidente recuperar(ConsultaObjetoIncidente consulta) throws DaoException;

	public Protocolo recuperar(Short ano, Long numero) throws DaoException;
	
	public ObjetoIncidente recuperar(Peticao peticao) throws DaoException;
	
	public List<ObjetoIncidente<?>> pesquisar(Long idObjetoIncidentePrincipal, TipoObjetoIncidente... tiposPermitidos) throws DaoException;

	public List<ObjetoIncidente<?>> pesquisar(Long idObjetoIncidentePrincipal) throws DaoException;
	
	boolean isIncidenteJulgado(Long idObjetoIncidente) throws DaoException;

	List<ObjetoIncidente<?>> pesquisarListaImportacaoUsuario(String usuario) throws DaoException;
	
	public void registrarLogSistema(ObjetoIncidente objetoIncidente, String dscTransacao, String dscFuncionalidade, Long chaveTabela, String nomeTabela) throws DaoException;
	
	public void registrarLogSistema(Long idObjetoIncidente, String dscTransacao, String dscFuncionalidade, Long chaveTabela, String nomeTabela) throws DaoException;
}
