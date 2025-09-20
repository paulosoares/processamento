package br.gov.stf.estf.julgamento.model.service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.estf.julgamento.model.dataaccess.SessaoDao;
import br.gov.stf.estf.julgamento.model.util.SessaoResult;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SessaoService extends GenericService<Sessao, Long, SessaoDao> {

	/**
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @param dataPrevistaInicio
	 * @param dataPrevistaFim
	 * @param ano
	 * @param numero
	 * @param tipoSessao
	 * @param tipoColegiado
	 * @param tipoAmbiente
	 * @param numeroAnoPreenchido
	 * @return
	 * @throws ServiceException
	 */
	public List<Sessao> pesquisarSessaoSQL(Date dataInicio, Date dataFim, Date dataPrevistaInicio, Date dataPrevistaFim, Short ano, Long numero, String tipoSessao, String tipoColegiado,
																						String tipoAmbiente, Boolean numeroAnoPreenchido) throws ServiceException;

	public List<Sessao> pesquisarSessao(Date dataInicio, Date dataFim, Date dataPrevistaInicio, Date dataPrevistaFim, Short ano, Long numero, String tipoSessao, String tipoColegiado,
																						String tipoAmbiente, Boolean numeroAnoPreenchido) throws ServiceException;

	public List<Sessao> pesquisarSessoesVirtuaisDeListaNaoIniciadas() throws ServiceException;
	
	public Date recuperarDataSessaoJulgamento(String siglaClasse, Long numeroProcesso, Long recurso, String tipoJulgamento) throws ServiceException;

	public Long recuperarMaiorNumeroSessao() throws ServiceException;

	public List<Sessao> pesquisarSessao(Date dataInicioSessao, Date dataFimSessao, TipoAmbienteConstante tipoAmbiente, TipoSessaoConstante tipoSessao, String colegiado) throws ServiceException;
	
	public List<Sessao> pesquisarSessaoVirtual(Date dataInicioSessao, Date dataFimSessao, TipoAmbienteConstante tipoAmbiente, TipoSessaoConstante tipoSessao, String colegiado) throws ServiceException;

	public Sessao pesquisarSessao(Date dataInicioSessao, TipoAmbienteConstante tipoAmbiente, TipoSessaoConstante tipoSessao, String colegiado) throws ServiceException;

	public Sessao recuperar(Long seqObjetoIncidente) throws ServiceException;
	
	public List<Sessao> recuperarExclusivoDigital(Long seqObjetoIncidente) throws ServiceException;

	public Sessao recuperar(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	public Boolean recuperarJulgamentoEmAberto(Long seqObjetoIncidente) throws ServiceException;

	List<SessaoResult> pesquisarSessao(String colegiado, TipoAmbienteConstante tipoAmbienteSessao, String tipoSessao, Date dataBase) throws ServiceException;

	List<SessaoResult> pesquisarSessao(TipoColegiadoConstante colegiado, TipoAmbienteConstante tipoAmbienteSessao, String tipoSessao, Date dataBase) throws ServiceException;

	void refresh(Sessao sessao) throws ServiceException;

	public void encerrarSessao(Sessao sessaoAEncerrar, LinkedHashMap<JulgamentoProcesso, String> processos, Sessao sessaoDestinoProcessos, Map<ListaJulgamento, String> listas,
																						Sessao sessaoDestinoListas, Date dataEncerramentoSessao, String nomeUsuario, Setor setor)
																						throws ServiceException;

	/**
	 * Pesquisa as sessoes abertas e do tipo TipoAmbienteConstante.PRESENCIAL
	 * 
	 * @param colegiado
	 * @return
	 * @throws ServiceException
	 */
	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado) throws ServiceException;

	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado, TipoAmbienteConstante ambiente) throws ServiceException;

	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado, TipoAmbienteConstante ambiente, Boolean sessaoVirtualExtraordinaria) throws ServiceException;

	Long recuperarUltimoNumeroSessao(Sessao novaSessao) throws ServiceException;

	void reabrirUltimaSessao(Colegiado colegiado) throws ServiceException;

	Sessao alterarSessaoJulgamentoAudioVideo(Sessao sessao, SessaoAudioEVideo sessaoAudioEVideo) throws ServiceException;

	public void excluirSessaoJulgamentoAudioVideo(Sessao sessao) throws ServiceException;

	public void iniciarSessoesVirtuais() throws ServiceException;

	public void finalizarSessoesVirtuais() throws ServiceException;

	// ------ Os meteodos abaixo devem ser revistos -----

	/**
	 * Recupera um LIST de todas a listas de julgamento, liberadas de uma determinada sessao e um determinado ministro
	 * 
	 * @param idSessao
	 * @param idMinistro
	 * @return
	 * @throws ServiceException
	 */
	List<ListaJulgamento> recuperarListasJulgamentoMinistro(Long idSessao, Long idMinistro) throws ServiceException;

	/**
	 * Recupera um LIST de todas a listas de julgamento, liberadas das sessoes em aberto de um ministro para um determinado colegiado e ambiente.
	 * 
	 * @param idMinistro
	 * @param colegiado
	 * @param ambiente   TipoAmbienteConstante.PRESENCIAL ou TipoAmbienteConstante.VIRTUAL
	 * @return
	 * @throws ServiceException
	 */
	List<ListaJulgamento> recuperaListasLiberadasColegiado(long idMinistro, TipoColegiadoConstante colegiado, TipoAmbienteConstante ambiente) throws ServiceException;

	public List<Sessao> pesquisarSessoesVirtuaisFinalizadas(TipoColegiadoConstante colegiado, Boolean possuiListaJulgamento) throws ServiceException;

	public List<Sessao> pesquisarSessoesVirtuaisFinalizadasIndependePublicado(TipoColegiadoConstante colegiado, Boolean possuiListaJulgamento) throws ServiceException;

	public List<Sessao> pesquisarSessoesVirtuaisIniciadasOuAgendadas(TipoColegiadoConstante colegiado) throws ServiceException;

	public List<SessaoResult> pesquisarResultSessoesVirtuaisIniciadasOuAgendadas(TipoColegiadoConstante colegiado) throws ServiceException;

	public List<Sessao> pesquisarSessoesVirtuaisNaoIniciadas(Colegiado colegiado, TipoSessaoConstante tipoSessaoConstante, TipoJulgamentoVirtual tipoJulgamentoVirtual) throws ServiceException;

	public Long recuperarMaiorNumeroSessaoVirtual(Colegiado colegiado, short ano) throws ServiceException;

	public Boolean isMinistroParticipanteSessao(Sessao sessao, Ministro ministro);

	public Long incrementoSequenciaControleVoto(int indexVotoVogal, Long proximaSequenciaVoto) throws ServiceException;

	public List<Sessao> pesquisarSessoesPrevistas(Date dataBase, boolean maiorDataBase) throws ServiceException;

	public boolean sessaoDentroDoPrazo(Long idTipoAndamento, Sessao sessao, Calendar hoje, List<Calendar> feriados, boolean ignorarCpc, StringBuffer memoriaCalculo, TipoJulgamentoVirtual tipoJulgamentoVirtual);

	public Calendar recuperaProximoDiaRespeitandoPrazo(Calendar dataLiberacaoLista, List<Calendar> feriados, boolean pautaFechada, boolean ignorarCpc, StringBuffer memoriaCalculo);

	public Sessao montarSessaoVirtual(Calendar dataLiberacaoLista, List<Calendar> feriados, Colegiado colegiado, TipoJulgamentoVirtual tipoJulgamentoVirtual, boolean ignorarCpc) throws ServiceException;

	Sessao recuperarSessao(Calendar dataLiberacaoCalendar, Colegiado colegiado, Boolean ignorarCpc, TipoJulgamentoVirtual tipoJulgamentoVirtual) throws ServiceException;

	public boolean isExclusivoDigital(ObjetoIncidente oi) throws ServiceException;
}
