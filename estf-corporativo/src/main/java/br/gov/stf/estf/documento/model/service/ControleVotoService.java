package br.gov.stf.estf.documento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.ControleVotoDao;
import br.gov.stf.estf.documento.model.service.exception.TextoSemControleDeVotosException;
import br.gov.stf.estf.documento.model.util.ControleVotoDto;
import br.gov.stf.estf.documento.model.util.IConsultaDadosDoTexto;
import br.gov.stf.estf.documento.model.util.IConsultaDeControleDeVotoInteiroTeor;
import br.gov.stf.estf.documento.model.util.ResultadoControleVotoPDF;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.exception.ValidationException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ControleVotoService extends GenericService<ControleVoto, Long, ControleVotoDao> {

	public List<ControleVoto> pesquisarControleVoto(String siglaClasseProcessual, Long numeroProcesso, Long idMinistro,
			Long tipoRecurso, Long tipoJulgamento, Date dataSessao, TipoTexto tipoTexto, Long idTexto) throws ServiceException;

	public List<ControleVoto> pesquisar(String siglaClasse, Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento,
			Date dataSessao, Long codigoMinistro) throws ServiceException;

	public List<ResultadoControleVotoPDF> pesquisar(ControleVotoDto controleVotoDto) throws ServiceException, ValidationException;

	ControleVoto consultaControleDeVotosDoTexto(Texto texto) throws ServiceException, TextoSemControleDeVotosException;

	List<ControleVoto> pesquisarControleVoto(IConsultaDadosDoTexto consultaDadosDoTextoVO) throws ServiceException;

	public List<ControleVoto> pesquisarControleVoto(ObjetoIncidente<?> objetoIncidente, Long idMinistro, Date dataSessao,
			TipoTexto tipoTexto, Long idTexto) throws ServiceException;

	public List<ControleVoto> pesquisarControleVoto(ObjetoIncidente<?> objetoIncidente, Ministro ministro, Date dataSessao)
			throws ServiceException;

	public List<ControleVoto> pesquisarParaInteiroTeor(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter)
			throws ServiceException;

	List<ControleVoto> pesquisarParaInteiroTeor(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter,
			boolean pesquisarObjetosDoProcesso) throws ServiceException;
	
	public List<ControleVoto> pesquisarParaInteiroTeor(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter,
			boolean pesquisarObjetosDoProcesso, boolean filtrarPeloSetorComposicaoAcordao) throws ServiceException;	

	@SuppressWarnings("rawtypes")
	public Long recuperarProximaSequenciaVoto(ObjetoIncidente objetoIncidente) throws ServiceException;

	@SuppressWarnings("rawtypes")
	public ControleVoto recuperar(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws ServiceException;

	@SuppressWarnings("rawtypes")
	public Boolean recuperarExistenciaPDFNaoAssinado(ObjetoIncidente objetoIncidente) throws ServiceException;

	public void excluirTextoDoControleDeVotos(Texto texto) throws ServiceException;
	
	public void validarTipoTextoNovoControleVoto(ControleVoto controleVoto) throws ServiceException;
	
	@SuppressWarnings("rawtypes")
	public Long recuperarProximaSequenciaVotoRepercussaoGeral(ObjetoIncidente objetoIncidente) throws ServiceException;

	/**
	 * Sincroniza o Controle de Votos com os Textos
	 * @param seqTexto   SEQ_TEXTOS sequencia de votação do controle de votos
	 * @param seqVotos   SEQ_VOTOS
	 * @param dataSessao Data do inicio da sessão
	 * @throws ServiceException
	 */
	void sincronizaControleVotoComTexto(Long seqTexto, Long seqVotos, Date dataSessao) throws ServiceException;

	Long incrementoSequenciaControleVoto(Long seq);

	ControleVoto recuperarControleVoto(Long idControleVoto) throws ServiceException;

	public List<ControleVoto> pesquisarControleVoto(ObjetoIncidente objetoIncidente) throws ServiceException;

	ControleVoto recuperarControleDeVotoSemSessao(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws ServiceException;
	
	public List<ControleVoto> pesquisarControleVotoPorTipoTexto(ObjetoIncidente<?> oi, TipoTexto...tiposTextos) throws ServiceException;

	public List<ControleVoto> pesquisarControleVotoPorMinistro(ObjetoIncidente<?> oi, Ministro ministro) throws ServiceException ;

	public String inferirDataFimJulgamento(ObjetoIncidente objetoIncidente) throws ServiceException;
	
	public Boolean existemCompletos() throws ServiceException;
	
	public List<Object> verificarSigilosos() throws ServiceException;

	public Boolean existemSigilosos() throws ServiceException;
}
