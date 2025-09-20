package br.gov.stf.estf.documento.model.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jdom.JDOMException;

import com.lowagie.text.DocumentException;

import br.gov.stf.estf.documento.model.dataaccess.TextoDao;
import br.gov.stf.estf.documento.model.exception.TextoException;
import br.gov.stf.estf.documento.model.exception.TextoInvalidoParaPecaException;
import br.gov.stf.estf.documento.model.service.exception.TextoNaoPertenceAListaDeTextosIguaisException;
import br.gov.stf.estf.documento.model.service.exception.TextoNaoPodeSerAlteradoException;
import br.gov.stf.estf.documento.model.service.exception.TransicaoDeFaseInvalidaException;
import br.gov.stf.estf.documento.model.util.AssinaturaDto;
import br.gov.stf.estf.documento.model.util.IConsultaDadosDoTexto;
import br.gov.stf.estf.documento.model.util.TextoSearchData;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.estf.entidade.usuario.Responsavel;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;

public interface TextoService extends GenericService<Texto, Long, TextoDao> {
	/**
	 * Pesquisa textos de acordo com os crit�rios informados
	 * 
	 * @param sigla
	 *            sigla do processo
	 * @param numero
	 *            n�mero do processo
	 * @param codigoRecurso
	 *            c�digo do recurso
	 * @param tipoJulgamento
	 *            tipo de julgamento
	 * @param codigoMinistro
	 *            c�digo do ministro
	 * @return
	 * @throws ServiceException
	 */
	public List<Texto> pesquisar(String sigla, Long numero, Long tipoRecurso, Long tipoJulgamento, Long codigoMinistro)
			throws ServiceException;

	/**
	 * Pesquisa textos de acordo com os crit�rios informados
	 * 
	 * @param sigla
	 *            sigla do processo
	 * @param numero
	 *            n�mero do processo
	 * @param tipoRecurso
	 *            c�digo do recurso
	 * @param julgamento
	 *            tipo de julgamento. Ex: M�rito;
	 * @param tipoTexto
	 *            tipo do texto, Ex: Ementa, Ac�rd�o, Relat�rio...
	 * @param orderAscDataSessao
	 *            ordenar de acordo com a data da sess�o de julgamento do
	 *            processo. acendente.
	 * @return
	 * @throws ServiceException
	 */
	public List<Texto> pesquisar(String sigla, Long numero, Long tipoRecurso, Long julgamento, TipoTexto tipoTexto,
			Boolean orderAscDataSessao) throws ServiceException;

	/**
	 * Pesquisa textos de acordo com os crit�rios informados
	 * 
	 * @param sigla
	 *            sigla do processo
	 * @param numero
	 *            n�mero do processo
	 * @param tipoRecurso
	 *            c�digo do recurso
	 * @param julgamento
	 *            tipo de julgamento. Ex: M�rito;
	 * @param tipoTexto
	 *            tipo do texto, Ex: Ementa, Ac�rd�o, Relat�rio...
	 * @param codigoMinistro
	 *            c�digo do ministro
	 * @param orderAscDataSessao
	 *            ordenar de acordo com a data da sess�o de julgamento do
	 *            processo. acendente.
	 * @return
	 * @throws ServiceException
	 */
	public List<Texto> pesquisar(String sigla, Long numero, Long tipoRecurso, Long julgamento, TipoTexto tipoTexto,
			Long codigoMinistro, Boolean orderAscDataSessao) throws ServiceException;

	/**
	 * Recupera as vers�es que este texto possui
	 * 
	 * @param texto
	 * @return
	 * @throws ServiceException
	 */
	public List<FaseTextoProcesso> recuperarVersoesTexto(Long textoId) throws ServiceException;

	/**
	 * Verifica a restricao de uma collection de textos
	 * 
	 * @param texto Texto a ser verificado
	 * @param idUsuario Id do usuario
	 * @param idSetor Setor do usuario criador
	 * @return true caso a verificacao tenha ocorrido com sucesso, false caso contrario
	 */
	public boolean verificarRestricaoTextos(Collection<Texto> textos, String idUsuario, Long idSetor);

	public SearchResult<Texto> pesquisar(TextoSearchData sd) throws ServiceException;

	public Texto verificaArqEletronico(ControleVoto cv) throws ServiceException;

	public List<Texto> pesquisarTexto(ObjetoIncidente<?> objetoIncidente, Ministro ministroAutenticado,
			Boolean ministroDiferenteAutenticado) throws ServiceException, TextoException;

	public Texto recuperar(String sigla, Long numero, Long tipoRecurso, Long julgamento, TipoTexto tipoTexto)
			throws ServiceException;

	public Texto recuperarUltimaDecisao(String sigla, Long numero, Long tipoRecurso, Long julgamento)
			throws ServiceException;

	public List<Texto> pesquisarDecisoesAta(String sigla, Long numero, Long tipoRecurso, Long julgamento, Date dataAta)
			throws ServiceException;

	@SuppressWarnings("rawtypes")
	public List<Texto> pesquisarDecisoesAta(ObjetoIncidente objetoIncidente, Date dataAta) throws ServiceException;

	public Texto recuperarDecisaoAta(String sigla, Long numero, Long tipoRecurso, Long julgamento, Date dataAta)
			throws ServiceException;

	public Date recuperarUltimaDataSessao(String sigla, Long numero, Long tipoRecurso, Long julgamento,
			TipoTexto tipoTexto) throws ServiceException;

	public List<Texto> pesquisarTextosPublicacao(String sigla, Long numeroProcessual, Long tipoRecurso,
			Long julgamento, TipoTexto tipoTexto, boolean orderAscDataSessao, Date dataSessao) throws ServiceException;

	public Texto recuperarTextoPublicacao(String sigla, Long numero, Long tipoRecurso, Long julgamento,
			TipoTexto tipoTexto) throws ServiceException;

	public List<Texto> pesquisarTextosIguais(Texto texto) throws ServiceException;

	/**
	 * Executa a transi��o entre duas fases (origem e destino). Segue o seguinte
	 * algoritmo: 
	 * 01 - Cria uma nova inst�ncia de FaseTextoProcesso, que representar� a fase de origem; 
	 * 02 - Associa, � inst�ncia de FaseTextoProcesso, o arquivo eletr�nico recuperado seguindo a seguinte regra: 
	 * 		- Se o arquivo eletr�nico associado ao texto selecionado for igual ao arquivo eletr�nico gravado na fase anterior, associar o mesmo arquivo
	 * 			gravado na fase anterior, sen�o, associar uma nova c�pia do arquivo
	 * 			associado ao texto selecionado; 
	 * 		- Obviamente se o arquivo estiver em elabora��o n�o haver� arquivo gravado; 
	 * 03 - Gera e associa, � inst�ncia de FaseTextoProcesso, o cabe�alho do documento; 
	 * 04 - Associa, � inst�ncia de FaseTextoProcesso, o documento PDF registrado na fase anterior, se houver; 
	 * 05 - Associa, � inst�ncia de FaseTextoProcesso, a data de transi��o: data corrente; 
	 * 06 - Associa, � inst�ncia de FaseTextoProcesso, o texto selecionado; 
	 * 07 - Salva a inst�ncia de FaseTextoProcesso; 
	 * 08 - Alterar a fase atual do texto selecionado; 
	 * 09 - Salva o texto selecionado.
	 * @param texto
	 * @param tipoTransicao
	 * @param textosProcessados
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 * @deprecated Esse m�todo ser� exclu�do com a nova vers�o do Decis�o, pois s� faz sentido no seu contexto.
	 */
	void alterarFaseDoTexto(Texto texto, TipoTransicaoFaseTexto tipoTransicao, Set<Long> textosProcessados)
			throws ServiceException, TransicaoDeFaseInvalidaException;

	void verificaTextoLiberadoParaPublicacao(Texto texto) throws ServiceException, TextoNaoPodeSerAlteradoException;

	boolean isTextoJaRegistradoParaProcesso(IConsultaDadosDoTexto consulta) throws ServiceException;

	List<Texto> pesquisar(IConsultaDadosDoTexto consulta) throws ServiceException;

	/**
	 * Retira o texto da lista de textos iguais. Torna o texto semelhante
	 * �queles que pertence � lista de textos iguais.
	 */
	void tornarTextoSemelhante(Texto texto) throws ServiceException, TextoNaoPertenceAListaDeTextosIguaisException;

	/**
	 * M�todo usado anteriormente para assinatura. 
	 * @deprecated
	 * Utilizar o outro m�todo de assinatura, passando o sequencial do documento eletr�nico.
	 * @param texto
	 * @param tipo
	 * @param conteudoAssinado
	 * @param carimbo
	 * @param dataCarimboTempo
	 * @param siglaSistema
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 */
	@Deprecated
	public void assinarTexto(Texto texto, TipoDocumentoTexto tipo, byte[] conteudoAssinado, byte[] carimbo,
			Date dataCarimboTempo, String siglaSistema) throws ServiceException, TransicaoDeFaseInvalidaException;

	void copiarArquivoParaTexto(Texto texto, ArquivoEletronico arquivoEletronicoOriginal) throws ServiceException;

	/**
	 * Recupera os textos de uma lista de processos.
	 * 
	 * @param listaProcessos
	 * @param listaTipoTexto
	 *            tipo dos textos que devem ser recuperados. se n�o informado,
	 *            traz todos os encontrados
	 * @return
	 * @throws ServiceException
	 */
	public List<Texto> recuperarTextosProcessos(List<Processo> listaProcessos, List<TipoTexto> listaTipoTexto)
			throws ServiceException;

	Texto pesquisarEmentaParaTextoIgual(IConsultaDadosDoTexto consulta) throws ServiceException;

	/**
	 * m�todo repons�vel por verificar se o texto informado j� existe;
	 * 
	 * @author GuilhermeA
	 */
	public void validarNovoTexto(Texto texto) throws TextoException, ServiceException;

	public void validaExclusaoTexto(Texto texto) throws TextoException, ServiceException;

	/**
	 * Restaura a vers�o de um texto para uma fase anterior, substituindo a
	 * atual
	 * 
	 * @param faseTextoProcesso
	 * @throws ServiceException
	 */
	public void restaurarVersaoTexto(FaseTextoProcesso faseTextoProcesso) throws ServiceException;

	public List<Texto> pesquisarTextosExtratoAta(String siglaClasse, Long numProcesso, Long tipoRecurso,
			Long tipoJulgamento) throws ServiceException;

	// NOVOS SERVICOS UTILIZANDO O OBJETO INCIDENTE

	public Texto recuperarDecisaoAta(ObjetoIncidente<?> objetoIncidente, Date dataSessao) throws ServiceException;

	public List<Texto> pesquisarTextosPublicacao(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto,
			Boolean orderAscDataSessao, Date dataSessao) throws ServiceException;

	public Texto recuperarTextoPublicacao(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto)
			throws ServiceException;

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Boolean orderAscDataSessao)
			throws ServiceException;

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Ministro ministro)
			throws ServiceException;

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto... tipoTexto) throws ServiceException;

	public List<Texto> recuperarTextosReferendo(Long idObjetoIncidente) throws ServiceException;

	public Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto) throws ServiceException;

	public Texto recuperarTextoEletronico(Integer idArquivoEletronico) throws ServiceException;

	public Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Long idMinistro) throws ServiceException;
	public Texto recuperar(Long idObjetoIncidente, TipoTexto decisao, Long idMinistro) throws ServiceException;
	public Texto recuperarTextoSemControleVoto(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws ServiceException;

	List<Texto> pesquisarTextosExtratoAta(Long idObjetoIncidente) throws ServiceException;

	/**
	 * m�todo responsavel por persistir texto 1) Quando o texto for novo,
	 * verifica se o mesmo pode ser inserido na base de dados 2) Quando o objeto
	 * for do tipo IncidenteJulgamento e ainda n�o tenha sido inserido no banco,
	 * e chamada a fun��o para inseri-lo.
	 * 
	 * @param texto
	 * @return
	 * @throws ServiceException
	 * @throws TextoException
	 */
	public Texto persistir(Texto texto) throws ServiceException, TextoException;

	public List<TipoTexto> recuperarTipoTextoPadrao(ObjetoIncidente<?> objetoIncidente, Ministro ministroAutenticado)
			throws ServiceException;

	/**
	 * M�todo que exclui uma lista de textos excluindo todos os relacionamentos
	 * de cada texto.
	 * 
	 * @param textos
	 *            a lista de textos
	 */
	public void excluirTextosComRelacionamentos(Collection<Texto> textos) throws ServiceException;

	/**
	 * M�todo que exclui um texto, assim como todos os relacionamentos que esse
	 * texto possa ter.
	 * 
	 * @param texto
	 * @throws ServiceException
	 */
	void excluirTextoComRelacionamentos(Texto texto) throws ServiceException;

	/**
	 * M�todo para alterar a fase de um texto. N�o afeta os textos iguais.
	 * 
	 * @param texto
	 * @param tipoTransicao
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 */
	void alterarFase(Texto texto, TipoTransicaoFaseTexto tipoTransicao) throws ServiceException,
			TransicaoDeFaseInvalidaException;
	
	/**
	 * M�todo para alterar a fase de um texto informando uma observa��o. N�o afeta os textos iguais.
	 * 
	 * @param texto
	 * @param tipoTransicao
	 * @param observacao
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 */
	void alterarFase(Texto texto, TipoTransicaoFaseTexto tipoTransicao, String observacao, Responsavel Responsavel) throws ServiceException,
			TransicaoDeFaseInvalidaException;

	/**
	 * Permite cancelar a assinatura de um texto, executando a transi��o
	 * informada
	 * @param texto
	 * @param transicao
	 * @param textosProcessados
	 * @throws TransicaoDeFaseInvalidaException
	 * @throws TextoInvalidoParaPecaException
	 * @throws ServiceException
	 * @deprecated Esse m�todo foi criado pelo Decis�o. A n�o ser que seja necess�rio por outra aplica��o, ser� movido para a TextoServiceLocal do Decis�o
	 */
	void cancelarAssinatura(Texto texto, TipoTransicaoFaseTexto transicao, Set<Long> textosProcessados)
			throws TransicaoDeFaseInvalidaException, TextoInvalidoParaPecaException, ServiceException;

	/**
	 * M�todo que retorna os textos iguais de um determinado texto, restringindo os textos ao ministro vinculado ao texto.
	 * @param texto Texto-base para a pesquisa
	 * @param limitarTextosDoMinistro Indica��o se � para restringir os textos ao Ministro
	 * @return
	 * @throws ServiceException 
	 */
	List<Texto> pesquisarTextosIguais(Texto texto, boolean limitarTextosDoMinistro) throws ServiceException;

	List<Texto> pesquisarTextosIguaisParaTransicaoFase(Texto texto, TipoTransicaoFaseTexto tipoTransicao)
			throws ServiceException, TransicaoDeFaseInvalidaException;

	/**
	 * Recupera o sequencial do documento eletr�nico que dever� ser utilizado na montagem do DocumentoEletronico.
	 * @param texto
	 * @return
	 * @throws ServiceException
	 */
	Long recuperarSequencialDoDocumentoEletronico(Texto texto) throws ServiceException;

	/**
	 * M�todo utilizado para assinar um texto digitalmente. O sequencial do documento eletr�nico deve ter sido recuperado anteriormente.
	 * @param texto
	 * @param tipo
	 * @param conteudoAssinado
	 * @param carimbo
	 * @param dataCarimboTempo
	 * @param siglaSistema
	 * @param sequencialDocumentoEletronico
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 * @deprecated Utilizar o m�todo que recebe a classe AssinaturaDto
	 */
	void assinarTexto(Texto texto, TipoDocumentoTexto tipo, byte[] conteudoAssinado, byte[] carimbo,
			Date dataCarimboTempo, String siglaSistema, Long sequencialDocumentoEletronico) throws ServiceException,
			TransicaoDeFaseInvalidaException;

	public Texto recarregar(Texto texto) throws ServiceException;

	/**
	 * M�todo para assinar um texto digitalmente.
	 * @param assinatura
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 */
	void assinarTexto(AssinaturaDto assinatura) throws ServiceException,
			TransicaoDeFaseInvalidaException;

	/**
	 * Cancela a assinatura de um texto, realizando as a��es de :
	 * 1) Excluir a juntada das pe�as eletr�nicas
	 * 2) Excluir o Documento Eletronico
	 * @param textoParaCancelar
	 * @throws ServiceException
	 * @throws TextoInvalidoParaPecaException
	 */
	void cancelarAssinaturaDoTexto(Texto textoParaCancelar)
			throws ServiceException, TextoInvalidoParaPecaException;
	
	public Texto recuperarTextoEmentaSobreRepercussaoGeral(Long seqObjetoIncidente, Long codigoMinistro) throws ServiceException;
	public Texto recuperarTextoDecisaoSobreRepercussaoGeral(Long seqObjetoIncidente, Long codigoMinistro) throws ServiceException;
	
	public List<Texto> pesquisarTextoRepercussaoGeralVotoValido(Long seqObjetoIncidente) throws ServiceException;
	
	public Texto recuperar(Long seqObjetoIncidente, Long codigoMinistro, TipoTexto tipoTexto, Date dataFim, Long seqVoto) throws ServiceException;
	
	public Texto recuperar(Long seqObjetoIncidente, String tipoJulgamento, TipoTexto tipoTexto, Long codigoMinistro) throws ServiceException;

	public void assinarTextoContingencialmente(AssinaturaDto assinatura) throws ServiceException, TransicaoDeFaseInvalidaException;

	Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Ministro ministro, Boolean textoPublico, Boolean dataSessaoPreenchida) throws ServiceException;

	List<Texto> recuperarListaTextoEletronico(Integer idArquivoEletronico) throws ServiceException;
	
	public boolean verificarExistenciaTexto(TextoSearchData sd) throws ServiceException;

	void alterarFase(AssinaturaDto assinatura
			       ,Texto texto
			       ,TipoTransicaoFaseTexto tipoTransicao
			       ,String observacao
			       ,Responsavel responsavel) throws ServiceException,
			TransicaoDeFaseInvalidaException;
	
	public List<Texto> pesquisar(ArquivoEletronico arquivoEletronico) throws ServiceException;

	public Texto liberarAntecipadamente(Long textoId, Boolean liberar) throws ServiceException;
	
	public SearchResult<Texto> pesquisarTextoPorObjIncidenteTipoTextoMinistroOuPresidente(TextoSearchData sd) throws ServiceException;
	
	public void preservarTextosRelatorAnteriorNaDesignacaoRedatorAcordao(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	//***
	
	public byte[] montarPDFAssinatura(Texto texto) throws FileNotFoundException, ServiceException, JDOMException,
	IOException, MontadorTextoServiceException, DocumentException;

	void refresh(Texto entity) throws ServiceException;
	
	public List<Texto> recuperarTextosPorFaseTipoEspecificos(Long seqObjetoIncidente, List<Long> tipoTextos, FaseTexto fase) throws ServiceException;
	
}
