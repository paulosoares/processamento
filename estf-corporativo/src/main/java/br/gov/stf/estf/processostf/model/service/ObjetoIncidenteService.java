package br.gov.stf.estf.processostf.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.enuns.SituacaoIncidenteJulgadoOuNao;
import br.gov.stf.estf.processostf.model.dataaccess.ObjetoIncidenteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

@SuppressWarnings("rawtypes")
public interface ObjetoIncidenteService extends GenericService<ObjetoIncidente, Long, ObjetoIncidenteDao> {
	
	public ObjetoIncidente<?> recuperar (Short ano, Long numero) throws ServiceException;
	
	public ObjetoIncidente<?> recuperar (String siglaClasse, Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento) throws ServiceException;
	
	public List<ObjetoIncidente<?>> pesquisarObjetosComTextosIguais(List<Texto> textosIguais) throws ServiceException;
	
	/**
	 * 
	 * @param peticao
	 * @return retorna o ObjetoIncidente de um PROCESSO, RECURSO ou INCIDENTE
	 * @throws ServiceException
	 */
	public ObjetoIncidente recuperar(Peticao peticao) throws ServiceException;
	
	public List<ObjetoIncidente<?>> pesquisar(Long idObjetoIncidentePrincipal, TipoObjetoIncidente... tiposPermitidos) throws ServiceException;

	public List<ObjetoIncidente<?>> pesquisar(Long idObjetoIncidentePrincipal) throws ServiceException;
	
	public String inserirDeslocamento(Guia guia, ArrayList objetos, Long codigoSetorUsuario) throws ServiceException, SQLException;
	public String inserirDeslocamento(Guia guia, ArrayList objetos, Long codigoSetorUsuario, Boolean recebimentoAutomatico) throws ServiceException, SQLException;

	public String inserirDeslocamento(Guia guia, ArrayList objetos, boolean recAutomarico) throws ServiceException, SQLException;
	
	public void excluirItemProcessoNaGuia(DeslocaProcesso processo) throws ServiceException;
	
	public void excluirItemPeticaoNaGuia(DeslocaPeticao peticao) throws ServiceException;
	
	public void inserirProcessoPeticaoNaGuia(Guia guia, ArrayList objetos) throws ServiceException, SQLException;
	
	public void excluirGuia(Guia guia) throws DaoException, ServiceException;

	public void inserirProcessoPeticaoNaGuia(Guia guia, Object deslocamento) throws ServiceException;
	
	public void salvarRecebimentoProcessos(List<DeslocaProcesso> documentos) throws ServiceException;
	
	public void salvarRecebimentoPeticoes(List<DeslocaPeticao> documentos) throws ServiceException;
	
	SituacaoIncidenteJulgadoOuNao recuperarSituacaoJulgamentoIncidente(Long idObjetoIncidente) throws ServiceException;

	boolean isObjetoIncidenteJulgado(ObjetoIncidente<?> incidente) throws ServiceException;

	List<ObjetoIncidente<?>> pesquisarListaImportacaoUsuario(String usuario) throws ServiceException;

	ObjetoIncidente<?> deproxy(ObjetoIncidente<?> objetoIncidente) throws ClassCastException;

	List<String> getListaPreferenciasComMarcacoesPorObjetoIncidente(Long codObjetoIncidente) throws ServiceException;

	List<String> getListaPreferenciasPorObjetoIncidente(Long codObjetoIncidente) throws ServiceException;

	String inserirDeslocamento(Guia guia, ArrayList objetos, boolean recAutomatico, boolean deslocamentoAutomatico) throws ServiceException, SQLException;
	
	public void registrarLogSistema(ObjetoIncidente objetoIncidente, String dscTransacao, String dscFuncionalidade, Long chaveTabela, String nomeTabela) throws ServiceException;
	
	public void registrarLogSistema(Long idObjetoIncidente, String dscTransacao, String dscFuncionalidade, Long chaveTabela, String nomeTabela) throws ServiceException;
}
