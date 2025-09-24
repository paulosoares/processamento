package br.jus.stf.estf.decisao.texto.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.gov.stf.estf.documento.model.exception.TextoException;
import br.gov.stf.estf.documento.model.exception.TextoInvalidoParaPecaException;
import br.gov.stf.estf.documento.model.service.exception.NaoExisteDocumentoAssinadoException;
import br.gov.stf.estf.documento.model.service.exception.TextoNaoPodeSerAlteradoException;
import br.gov.stf.estf.documento.model.service.exception.TransicaoDeFaseInvalidaException;
import br.gov.stf.estf.documento.model.util.AssinaturaDto;
import br.gov.stf.estf.entidade.documento.ArquivoEletronicoView;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.TipoJulgamento;
import br.gov.stf.estf.entidade.usuario.Responsavel;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.processostf.model.service.exception.DuplicacaoChaveAntigaException;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoOcultoException;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.texto.support.ConsultaDadosDoTextoDto;
import br.jus.stf.estf.decisao.texto.support.SetorInativoException;
import br.jus.stf.estf.decisao.texto.support.TextoBloqueadoException;
import br.jus.stf.estf.decisao.texto.support.TextoComSituacaoDaPublicacaoVO;
import br.jus.stf.estf.decisao.texto.support.TextoNaoPodeSerRestritoException;

/**
 * Fornece serviços de negócio relacionados a Textos.
 * 
 * <p>Deve ser o ponto único de acesso a serviços que implementam regras 
 * de negócio que envolvem textos.
 * 
 * @author Rodrigo.Barreiros
 * @since 15.04.2010
 */
public interface TextoService {

	/**
	 * Altera a fase de um texto, aplicando todas as regras pertinentes.
	 * 
	 * @param texto o identificador do texto
	 * @param tipoTransicao o tipo de transição
	 * @param textosProcessados Ids dos textos que já foram processados
	 * @param observacao Observação da transição de fase
	 * @param responsavel Responsável pela fase
	 * @throws TransicaoDeFaseInvalidaException lançada caso
	 * o tipo de transição seja inválida
	 * @throws TextoBloqueadoException 
	 * @throws ProcessoOcultoException 
	 */
	void alterarFase(TextoDto texto, TipoTransicaoFaseTexto tipoTransicao, Set<Long> textosProcessados, String observacao, Responsavel responsavel)
			throws TransicaoDeFaseInvalidaException, TextoBloqueadoException, ProcessoOcultoException;
	
	/**
	 * Libera um dado texto para publicação.
	 * 
	 * @param texto o identificador do texto
	 * @param texto O texto que será liberado
	 * @param textosProcessados Ids dos textos que já foram processados
	 * @param liberarRtj Indica se quer que o texto seja liberado para RTJ
	 * @param usuario TODO
	 * @param observacao Observação da transição de fase
	 * @throws TransicaoDeFaseInvalidaException lançada caso
	 * o texto não possa ser transitado para publicação
	 * @return Coleção de mensagens dos textos processados para ser mostrada ao cliente.
	 * @throws TransicaoDeFaseInvalidaException
	 * @throws TextoBloqueadoException 
	 * @throws ProcessoOcultoException 
	 * @throws SetorInativoException 
	 */
	Collection<String> liberarParaPublicacao(TextoDto texto, Set<Long> textosProcessados, boolean liberarRtj, Principal usuario, String observacao, Responsavel responsavel) throws TransicaoDeFaseInvalidaException, TextoBloqueadoException, ProcessoOcultoException, SetorInativoException;

	/**
	 * Recupera a lista de textos relacionados a um dado objeto incidente e a
	 * um dado ministro. Alguns restrições:
	 * - O Texto deve ser, efetivamente, do Ministro e
	 * - Somente Textos que não sejam de Decisão.
	 * 
	 * @param objetoIncidente o objeto incidente do texto
	 * @param ministro o ministro solicitado
	 * @return a lista de textos
	 */
	List<TextoDto> recuperarTextos(ObjetoIncidente<?> objetoIncidente, Ministro ministro, Principal principal) throws ServiceException;

	/**
	 * Recupera a lista de textos relacionados a um dado objeto incidente.
	 * Alguns restrições:
	 * - Somente Textos associados a outro Ministro;
	 * - Somente Textos Públicos e
	 * - Somente Textos que não sejam de Decisão.
	 * 
	 * @param objetoIncidente o objeto incidente do texto
	 * @return a lista de textos
	 */
	List<TextoDto> recuperarTextos(ObjetoIncidente<?> objetoIncidente);
	
	/**
	 * Recupera a lista de textos relacionados a um dado objeto incidente.
	 * @param objetoIncidente o objeto incidente do texto
	 * @param adicionarControleDeVotos
	 * @return a lista de textos
	 */
	List<TextoDto> recuperarTextos(ObjetoIncidente<?> objetoIncidente, boolean adicionarControleDeVotos);

	/**
	 * Pesquisa listas de texto dado o nome ou parte do nome da lista.
	 * 
	 * @param nome o nome da lista
	 * @return a lista de listas
	 */
	List<ListaTextos> pesquisarListasTextos(String string);

	/**
	 * Recupera os textos iguais ao texto relacionado a um dao identificador
	 * 
	 * @param id o identificador do texto
	 * @return a lista de textos iguais
	 */
	List<Texto> recuperarTextosIguais(Long id);

	/**
	 * Recupera o Texto dado o identificador.
	 * 
	 * @param id o identificador do texto
	 * @return o texto
	 */
	Texto recuperarTextoPorId(Long id);

	/**
	 * Cancela a assinatura de um determinado texto.
	 * @param texto Identificador do texto
	 * @param transicaoDeFase A transição de fase que está ocorrendo 
	 * @param textosProcessados Os textos que já foram processados por essa ação.
	 * @param observacao Observação inserida pelo usuário que justifica o cancelamento da assinatura.
	 * @throws TextoBloqueadoException 
	 * @throws ProcessoOcultoException 
	 */
	void cancelarAssinatura(TextoDto texto, TipoTransicaoFaseTexto transicaoDeFase, Set<Long> textosProcessados, String observacao, Responsavel responsavel) throws TextoBloqueadoException, ProcessoOcultoException;

	/**
	 * Verifica se o texto está bloqueado para edição por algum usuário.
	 * @param texto
	 * @throws ServiceException
	 * @throws TextoBloqueadoException 
	 */
	void verificaTextoBloqueado(Texto texto) throws ServiceException, TextoBloqueadoException;

	/**
	 * Verifica se o textoDto está bloqueado para edição por algum usuário.
	 * @param texto
	 * @throws ServiceException
	 * @throws TextoBloqueadoException 
	 */
	void verificaTextoBloqueado(TextoDto texto) throws ServiceException, TextoBloqueadoException;

	/**
	 * Recupera o ArquivoEletronicoView pelo id.
	 * @param id
	 * @return
	 */
	ArquivoEletronicoView recuperarArquivoEletronicoViewPeloId(Long id);
	
	/**
	 * Recupera a ementa de um determinado texto.
	 * @param texto
	 * @param ministro
	 * @return
	 * @throws ServiceException 
	 */
	Texto recuperarEmenta(Texto texto, Ministro ministro) throws ServiceException;
	
	Texto recuperarEmenta(TextoDto textoDto, Ministro ministro) throws ServiceException;

	Texto recuperarDecisaoRepercussaoGeral(Texto texto, Ministro ministro) throws ServiceException;
	
	Texto recuperarEmentaRepercussaoGeral(Texto texto, Ministro ministro) throws ServiceException;
	
	Texto recuperarEmentaRepercussaoGeral(TextoDto textoDto, Ministro ministro) throws ServiceException;

	
	/**
	 * Recupera a lista de textos iguais que será levada em consideração para a transição de fase.
	 * O método avalia quais textos serão afetados, segundo as regras de transição de fase, que são as
	 * seguintes:
	 * 1)Se for uma transição progressiva, vai trazer todos os registros que estejam entre a fase
	 * do texto base e a fase de destino da transição
	 * 2)Se for uma transição regressiva, retornará todos os registros que estejam em uma fase
	 * menor que a do texto base e maior que a do destino.
	 * @param textoDto
	 * @param tipoTransicao
	 * @return
	 * @throws TransicaoDeFaseInvalidaException Quando uma transição Liberar para publicação é enviada em uma lista com textos não assinados.
	 * @throws ProcessoOcultoException 
	 */
	List<Texto> pesquisarTextosIguaisParaTransicaoFase(TextoDto textoDto, TipoTransicaoFaseTexto tipoTransicao)
			throws TransicaoDeFaseInvalidaException, ProcessoOcultoException;

	/**
	 * Recupera o sequencial de um novo Documento Eletronico, sem inserir um novo registro 
	 * @param texto
	 * @return
	 */
	Long recuperarSequencialDoDocumentoEletronico(TextoDto texto);

	/**
	 * Verifica se os textos possuem alguma restrição 
	 * @param textos Lista de textos
	 * @param idUsuario Identificador do usuário
	 * @param idSetor Identificador do setor
	 * @return
	 */
	public boolean verificarRestricaoTextos(Collection<Texto> textos, String idUsuario, Long idSetor);
	
	/**
	 * Pesquisa os tipos de documento texto que são disponíveis para um determinado setor
	 * @param codSetor
	 * @return
	 */
	List<TipoDocumentoTexto> pesquisarTiposDocumentoTextoPorSetor(long codSetor);

	/**
	 * Recupera um tipo de documento texto pelo Id
	 * @param idTipoDocumentoTexto
	 * @return
	 */
	TipoDocumentoTexto recuperarTipoDocumentoTextoPorId(Long idTipoDocumentoTexto);

	TextoDto pesquisarEmentaParaTextoIgual(ConsultaDadosDoTextoDto consulta);

	boolean isTextoJaRegistradoParaProcesso(ConsultaDadosDoTextoDto consulta);

	List<TextoDto> pesquisar(ConsultaDadosDoTextoDto consulta);

	/**
	 * Criar textos replicados a partir de um texto para os objetosIncidente e
	 * para o ministro passados.
	 * @param texto texto base para replicação.
	 * @param objetosIncidente objetosIncidente que receberão os textos replicados.
	 * @param ministro ministro para o qual os textos serão criados.
	 */
	void criarListaTextosReplicados(TextoDto texto, Collection<ObjetoIncidenteDto> objetosIncidente, Ministro ministro);

	/**
	 * Verifica se já existe um texto para o objetoIncidente, tipoTexto e ministro
	 * @param objetoIncidente
	 * @param tipoTexto
	 * @param ministroDoGabinete
	 * @return
	 * @throws ServiceException
	 */
	boolean existeTextoRegistradoParaProcesso(ObjetoIncidenteDto objetoIncidente, TipoTexto tipoTexto,
			Ministro ministroDoGabinete) throws ServiceException;
	
	ControleVoto consultaControleDeVotoDoProcesso(TipoTexto tipoTexto, Ministro ministroDoGabinete,
			ObjetoIncidenteDto objetoIncidente) throws ServiceException;
	
	TextoDto recuperarEmenta(ObjetoIncidenteDto objetoIncidente) throws ServiceException;


	TextoDto recuperarAcordao(ObjetoIncidenteDto objetoIncidente) throws ServiceException;

	/**
	 * Recupera os tipos de texto que devem ser apresentados para um determinado objeto incidente.
	 * @param objetoIncidente
	 * @param ministro
	 * @return
	 */
	List<TipoTexto> recuperarTipoTextoPadrao(ObjetoIncidente<?> objetoIncidente, Ministro ministro);

	/**
	 * Recupera os tipos de incidentes cadastrados para seleção
	 * @param Classe Processual 
	 * @return Tipos de Incidente de Julgamento ordenados pela descrição
	 */
	List<TipoIncidenteJulgamento> recuperarTiposDeIncidentes(Classe classeProcessual);

	/**
	 * Recupera a proxima sequencia do tipo de incidente de julgamento.
	 * @param idObjetoIncidente O sequencial do objeto incidente
	 * @param idTipoIncidenteJulgamento O identificador do tipo de incidente de julgamento
	 * @return
	 */
	Integer recuperarProximaSequenciaCadeia(Long idObjetoIncidente, Long idTipoIncidenteJulgamento);
	
	/**
	 * Cria um novo incidente de julgamento
	 * @param idObjetoIncidente O Id do Objeto Incidente
	 * @param idTipoIncidenteJulgamento O tipo de incidente de julgamento a ser criado
	 * @param sequenciaCadeia A sequencia da cadeia
	 * @return
	 * @throws DuplicacaoChaveAntigaException 
	 */
	IncidenteJulgamento inserirIncidenteJulgamento(Long idObjetoIncidente, Long idTipoIncidenteJulgamento, Integer sequenciaCadeia) throws DuplicacaoChaveAntigaException;

	/**
	 * Método que verifica se o novo texto pode ser criado.
	 * @param texto
	 */
	void validarNovoTexto(Texto texto);

	void validaExclusaoTexto(TextoDto texto) throws TextoException, ServiceException;

	void excluirTextos(Collection<TextoDto> textos) throws ServiceException;

	List<ObjetoIncidenteDto> recuperarProcessosListaTextosIguais(TextoDto texto) throws ServiceException;

	TextoDto recuperaEmentaGeradaParaProcesso(TextoDto texto, ObjetoIncidenteDto objetoIncidente) throws ServiceException;

	void verificaTextoLiberadoParaPublicacao(TextoDto texto) throws ServiceException, TextoNaoPodeSerAlteradoException;
	
	/**
	 * Verifica se um texto pode ser alterado. Em caso negativo, lança uma exceção do tipo TextoNaoPodeSerAlteradoException
	 * @param texto Texto a ser alterado
	 * @throws ServiceException
	 * @throws TextoNaoPodeSerAlteradoException
	 */
	void validaTextoParaAlteracao(TextoDto texto) throws ServiceException, TextoNaoPodeSerAlteradoException;
	
	/**
	 * Retorna uma lista contendo os textos iguais, excluindo o texto base
	 * @param texto Texto base para pesquisa de textos iguais
	 * @return Lista de textos iguais ao texto base
	 * @throws ServiceException
	 */
	List<TextoDto> pesquisarTextosIguais(TextoDto texto) throws ServiceException;

	/**
	 * Cria ou editar (caso já exista) lista de textos iguais
	 * @param texto Texto base para lista
	 * @param processosValidos Processos da lista de textos iguais
	 * @param ministro Ministro dos textos da lista
	 * @param usuarioLogado Usuario de inclusao do texto
	 * @param sobrescreverEmenta Indicativo se os textos de Ementa dos processos de destino devem ser sobrescritos
	 * @throws TransicaoDeFaseInvalidaException
	 */
	void criarEditarListaTextosIguais(TextoDto texto, Collection<ObjetoIncidenteDto> processosValidos, Ministro ministro, 
			Usuario usuarioLogado, boolean sobrescreverEmenta) throws TransicaoDeFaseInvalidaException;
	
	/**
	 * Junta as peças relacionadas a um dado texto informando se essas peças
	 * devem ser disponibilizadas, ou não, para exibição na internet.
	 * @param textoDto o texto a que estão relacionadas as peças
	 * @param textosProcessados ids dos textos que já foram processados
	 * @param disponibilizarNaInternet o flag indicando se as peças
	 * @param usuario TODO
	 * @param observacao observação inserida pelo usuário
	 * @return 
	 * @throws NaoExisteDocumentoAssinadoException
	 * @throws TextoInvalidoParaPecaException
	 * @throws TextoJaJuntadoException
	 * @throws TextoBloqueadoException 
	 * @throws ProcessoOcultoException 
	 */
	Collection<String> juntarPecas(TextoDto textoDto,
			Set<Long> textosProcessados, boolean disponibilizarNaInternet, Principal usuario, String observacao, Responsavel responsavel)
			throws NaoExisteDocumentoAssinadoException,
			TextoInvalidoParaPecaException, TextoJaJuntadoException, TextoBloqueadoException, ProcessoOcultoException;
	
	/**
	 * Método que verifica se a situação do texto permite que ele seja liberado para publicação.
	 * @param textos
	 * @return
	 * @throws ServiceException
	 */
	List<TextoComSituacaoDaPublicacaoVO> consultarSituacoesDePublicacaoDosTextos(Collection<TextoDto> textos);

	/**
	 * Suspende a publicação dos textos enviados.
	 * @param texto
	 * @param textosProcessados
	 * @param observacao
	 * @param usuario
	 * @return
	 * @throws TransicaoDeFaseInvalidaException 
	 * @throws TextoInvalidoParaPecaException 
	 * @throws ProcessoOcultoException 
	 */
	Collection<String> suspenderPublicacao(TextoDto texto, Set<Long> textosProcessados, String observacao, Principal usuario, Responsavel responsavel) throws TransicaoDeFaseInvalidaException, TextoInvalidoParaPecaException, ProcessoOcultoException;

	/**
	 * Pesquisa os textos por objetoIncidente, tipoTexto e ministro 
	 * @param objetoIncidente
	 * @param tipoTexto
	 * @param ministro
	 * @return Lista de Textos
	 * @throws ServiceException 
	 */
	List<Texto> pesquisar(ObjetoIncidenteDto objetoIncidente,
			TipoTexto tipoTexto, Ministro ministro) throws ServiceException;

	/**
	 * Executa a restrição de acesso ao texto selecionado. Se existem textos iguais ao selecionado, uma mensagem indicando essa
	 * situação será exibida. O mesmo ocorrerá se o texto selecionado for público.
	 * 
	 * @param textoDto O texto que será restringido
	 * @param tipoRestricao O tipo da restrição
	 * @param siglaUsuario A sigla do usuário que solicitou a restrição
	 * @throws TextoNaoPodeSerRestritoException Quando o texto não puder ser restrito (está em lista de textos iguais ou público)
	 */
	void alterarAcessoDoTexto(TextoDto textoDto, TipoRestricao tipoRestricao, String siglaUsuario) throws TextoNaoPodeSerRestritoException;
	
	/**
	 * Desfaz a juntada das peças relacionadas a um dado texto.
	 * 
	 * @param textoDto o texto a que estão relacionadas as peças
	 * @param textosProcessados
	 * @param observacao
	 * @param principal 
	 * @throws ProcessoOcultoException 
	 */
	void desfazerJuntada(TextoDto textoDto, Set<Long> textosProcessados, String observacao, Responsavel responsavel) throws ProcessoOcultoException;

	/**
	 * Método que consulta quais são os controles de voto registrados para um determinado objeto incidente.
	 * @param objetoIncidente
	 * @return
	 * @throws ServiceException
	 */
	List<ControleVoto> consultarControleDeVotoDoProcesso(ObjetoIncidenteDto objetoIncidente) throws ServiceException;

	/**
	 * Recupera o tipo julgamento para o par tipoRecurso e numSequenciaCadeia
	 * @param seqTipoRecurso
	 * @param sequenciaCadeia
	 * @return
	 */
	TipoJulgamento recuperarTipoJulgamento(Long seqTipoRecurso, Long sequenciaCadeia);
	
	/**
	 * Recuperar um tipo de incidente julgamento pelo id.
	 * @param id
	 * @return
	 */
	TipoIncidenteJulgamento recuperarTipoIncidenteJulgamentoPorId(Long id);

	/**
	 * Retorna uma lista contendo os textos iguais, excluindo o texto base
	 * @param texto Texto base para pesquisa de textos iguais
	 * @param limitarTextosDoMinistro Restringe a pesquisa aos textos do ministro se for true
	 * @return Lista de textos iguais ao texto base
	 * @throws ServiceException
	 */
	List<TextoDto> pesquisarTextosIguais(TextoDto texto,
			boolean limitarTextosDoMinistro) throws ServiceException;

	Long recuperarSequencialDoUltimoDocumentoEletronico();

	void testarAssinaturaTexto(AssinaturaDto assinaturaDto);

	void assinarTextoContingencialmente(AssinaturaDto assinaturaDto);

	void republicarTexto(TextoDto texto);

	boolean isTipoIncidenteJulgamentoPermitidoParaClasse(TipoIncidenteJulgamento tipoIncidenteJulgamento, Classe classeProcessual);

	void validaAcessoTextosRestritos(Principal principal, List<TextoDto> dtos);

	void marcarComoFavoritos(Collection<TextoDto> textos) throws ServiceException ;

	void desmarcarComoFavoritos(Collection<TextoDto> textos) throws ServiceException ;
	
	public List<Texto> recuperarListaTextos(Collection<TextoDto> textos) throws ServiceException;

	public void liberarVisualizacaoAntecipada(Collection<TextoDto> textosDto, boolean liberacaoAntecipada) throws ServiceException;
	
	public boolean hasPerfilLiberarAcordaoParaPublicacaoProcessoSigiloso();
}
 