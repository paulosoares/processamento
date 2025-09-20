package br.gov.stf.estf.repercussaogeral.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.julgamento.EnvolvidoSessao;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.entidade.util.DadosTextoDecisao;
import br.gov.stf.estf.julgamento.model.exception.TemaException;
import br.gov.stf.estf.processostf.model.service.exception.IncidenteJulgamentoException;
import br.gov.stf.estf.repercussaogeral.model.dataaccess.RepercussaoGeralDao;
import br.gov.stf.estf.repercussaogeral.model.util.RepercussaoGeralSearchData;
import br.gov.stf.framework.model.entity.BaseEntity;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;

public interface RepercussaoGeralService extends GenericService<BaseEntity, Long, RepercussaoGeralDao> {

	/**
	 * método responsavel por calcular um novo prazo para os processos que
	 * finalizarão durante o recesso (criado para alimento o novo esquema de
	 * julgamento)
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public Boolean persistirRepercussaoGeralJulgamentoRecesso() throws ServiceException;

	public SearchResult pesquisarRepercussaoGeral(RepercussaoGeralSearchData searchData) throws ServiceException;

	public SearchResult pesquisarRepercussaoGeralPlenarioVirtual(RepercussaoGeralSearchData searchData) throws ServiceException;

	public SearchResult pesquisarRepercussaoGeralSQL(RepercussaoGeralSearchData searchData, String ordem) throws ServiceException;

	public void persistirRepercussaoGeralJulgamentoAgendado(Date dataAgendado) throws ServiceException;

	public Boolean persistirRepercussaoGeralJulgamento(Processo processo, Ministro ministroAutenticado, List<VotoJulgamentoProcesso> listaTipoVotoMinistro, Texto texto, boolean b, UsuarioEGab usuarioEGab, Boolean ignorarCpc, Sessao sessao) throws ServiceException, RepercussaoGeralException;

	public Boolean persistirAndamentoFimRepercusaoGeral() throws ServiceException;

	public Boolean persistirTemaRepercussaoGeral(Tema tema, Date dataSessao) throws ServiceException, TemaException;

	public Boolean verificaDiaAtualRecesso() throws ServiceException;

	public void persistirNotificacaoRepercussaoGeral() throws ServiceException;

	public void persistirNotificacaoDivergenteMinsitro() throws ServiceException;

	public byte[] recuperarTextoDecisao(Processo processo, String fontFamily, String fontSize) throws ServiceException;

	public Boolean naoPermiteAlteracaoTextoDecisao(ObjetoIncidente<?> oi) throws ServiceException;

	public JulgamentoProcesso recuperarJulgamentoRepercussaGeral(Processo processo) throws ServiceException;

	public Boolean julgamentoFinalizado(Processo processo) throws ServiceException;

	public Long buscaNumeroMaximoTema() throws ServiceException;

	public Boolean recuperarPossuiVotoDivergente(Processo processo) throws ServiceException;

	public DadosTextoDecisao recuperarDadosTextoDecisao(Processo processo) throws ServiceException;

	public DadosTextoDecisao recuperarDadosTextoDecisao(Long objetoIncidenteId) throws ServiceException;
	
	public boolean verificarPodeCriarDecisaoRepercussaoGeral(Long seqObjetoIncidente) throws ServiceException;

	public IncidenteJulgamento recuperarIncidenteJulgamento(ObjetoIncidente obj, String sigTipoRecurso) throws ServiceException,
			IncidenteJulgamentoException;

	public void persistirRegistroSubstituicaoRepercussaoGeral(List<AndamentoProcesso> listaAndamentoProcesso, List<ProcessoTema> listaProcessoTema,
			String userName, Setor setor) throws ServiceException;

	public Long persistirRegistroJulgamentoRepercussaoGeral(String tipoJulgamento, String dataSessaoJulgamento, Long origemDecisao,
			Processo processo, int tipoRegistroJulgamento, String observacao, boolean jaEhLeadingCase, String userName, Setor setor)
			throws ServiceException;

	public Long persistirRegistroInicioSuspensaoJulgamentoRGMerito(List<Long> listaCodAndamentoProcesso, String tipoSuspensao,
			String dataSessaoJulgamento, Long origemDecisao, Processo processo, String motivoSuspensao, String julgamento, boolean jaEhLeadingCase,
			String userName, Setor setor) throws ServiceException;

	public String consultarDecisaoRGPackage(Long idObjetoIncidente) throws ServiceException;
	
	public Long pesquisarManifestacaoRGPackage(String siglaClasse, Long numeroProcesso,  Long objetoIncidente) throws ServiceException;

	/**
	 * Recuperar o tipo de recurso
	 * @param obj
	 * @param siglaTipoRecurso
	 * @return
	 * @throws ServiceException
	 * @throws IncidenteJulgamentoException
	 */
	IncidenteJulgamento insereIncidente(ObjetoIncidente obj, String siglaTipoRecurso) throws ServiceException, IncidenteJulgamentoException;

	/**
	 * Método para recuperar o incidente do julgamento
	 * @param obj
	 * @param sigTipoRecurso
	 * @return
	 * @throws ServiceException
	 */
	IncidenteJulgamento recuperarIncidente(ObjetoIncidente obj, String sigTipoRecurso) throws ServiceException;
	IncidenteJulgamento recuperarIncidentePeloPai(ObjetoIncidente<?> objetoIncidente, TipoIncidenteJulgamento tipoIncidenteJulgamento) throws ServiceException;

	List<EnvolvidoSessao> recuperarMinistrosEnvolvidos(Sessao sessao) throws ServiceException;
}
