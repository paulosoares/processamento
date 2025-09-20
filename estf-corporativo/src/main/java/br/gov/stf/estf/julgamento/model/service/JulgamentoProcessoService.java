package br.gov.stf.estf.julgamento.model.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.TipoVoto;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.julgamento.model.dataaccess.JulgamentoProcessoDao;
import br.gov.stf.estf.julgamento.model.util.JulgamentoProcessoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;

public interface JulgamentoProcessoService extends GenericService<JulgamentoProcesso, Long, JulgamentoProcessoDao>{

	public List<JulgamentoProcesso> pesquisarProcessosSessao( Long idSessao ) throws ServiceException;
	
	public JulgamentoProcesso recuperarJulgamentoProcesso(Long id, Long idObjetoIncidente,
			TipoAmbienteConstante tipoAmbienteSessao, String colegiado)throws ServiceException;
	
	public SearchResult<JulgamentoProcesso> pesquisarJulgamentoProcesso(JulgamentoProcessoSearchData searchData)throws ServiceException;
	
	public List<JulgamentoProcesso> pesquisarJulgamentoProcesso(Long idOpjetoIncidentePrincipal,Long idMinistro, String ... sigTipoRecursos)throws ServiceException;
	
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoRG(Long idOpjetoIncidentePrincipal,Long idMinistro, String ... sigTipoRecursos)throws ServiceException;
	
	public List<ProcessoImagem> recuperarInteiroTeor(Long idObjetoIncidente, Processo processo) throws ServiceException;
	
	public List<ProcessoImagem> recuperarInteiroTeorRG(Long idObjetoIncidente, Processo processo) throws ServiceException;
	
	void definirSessaoJulgamento(ObjetoIncidente<?> objetoIncidente, Sessao sessao, String idUsuario, Setor setor, boolean lancarAndamentoApresentadoEmMesa) throws ServiceException;

	void definirSessaoJulgamento(ListaJulgamento listaJulgamento, Sessao sessao, String idUsuario, Setor setor) throws ServiceException;

	void definirSessaoJulgamento(ObjetoIncidente<?> objetoIncidente, Sessao sessao, String idUsuario, Setor setor, Integer ordemSessao) throws ServiceException;

	public Integer definirOrdemSessao(Sessao sessao) throws ServiceException;

	void removerJulgamentoProcesso(ObjetoIncidente<?> objetoIncidente, Sessao sessao) throws ServiceException;

	JulgamentoProcesso recuperar(ObjetoIncidente<?> objetoIncidente, Sessao sessao) throws ServiceException;
	
	JulgamentoProcesso recuperar(ObjetoIncidente<?> objetoIncidente, Sessao sessao, Date dataBase) throws ServiceException;

	public JulgamentoProcesso pesquisaSessaoNaoFinalizada (ObjetoIncidente<?> objetoIncidente, TipoAmbienteConstante tipoAmbiente) throws ServiceException;

	void refresh(JulgamentoProcesso julgamentoProcesso) throws ServiceException;

	void reordenarProcessos(JulgamentoProcesso julgamentoProcessoBase, Integer ordemDestino) throws ServiceException;

	void definirSessaoJulgamento(ObjetoIncidente<?> objetoIncidente, Sessao sessao, String idUsuario, Setor setor,
			Integer ordemSessao, boolean lancarAndamentoApresentadoEmMesa) throws ServiceException;
	
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoListaJulgamento(ListaJulgamento listaJulgamento) throws ServiceException;
	
	public Map<TipoVoto, List<VotoJulgamentoProcesso>> agruparVotosJulgamentoProcessoPorTipo(JulgamentoProcesso julgamentoProcesso);
	
	public JulgamentoProcesso pesquisaUltimoJulgamentoProcesso(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public JulgamentoProcesso clonarJulgamentoProcesso(Long julgamentoProcessoId, Long idSessao) throws DaoException;
	
	public JulgamentoProcesso clonarJulgamentoProcesso(Long julgamentoProcessoId, Long idSessao, Boolean clonarRascunhos) throws DaoException;
	
	List<VotoJulgamentoProcesso> recuperarVotosProcesso(Long julgamentoProcessoId) throws ServiceException;

	public JulgamentoProcesso pesquisaUltimoJulgamentoProcesso(ObjetoIncidente<?> objetoIncidente, Ministro ministroDestaque, TipoSituacaoProcessoSessao situacaoProcessoSessao, Boolean destaqueCancelado) throws ServiceException;
	
	void cancelarAgendamento(List<JulgamentoProcesso> lista) throws ServiceException;

}
