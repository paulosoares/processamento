package br.jus.stf.estf.decisao.texto.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.documento.model.service.exception.TransicaoDeFaseInvalidaException;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.estf.entidade.usuario.Responsavel;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoOcultoException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.jus.stf.estf.decisao.texto.support.TextoBloqueadoException;

/**
 * Classe base para ação de transição de estados de textos. Fornece métodos
 * para execução dos serviços de transição.
 * 
 * @author Rodrigo.Barreiros
 * @since 02.06.2010
 * @param <T> o tipo do recurso; nesse caso será sempre texto
 */
public abstract class AbstractAlterarFaseDoTextoActionFacesBean<T> extends ActionSupport<TextoDto> implements
		ActionInterface<TextoDto> {

	protected Set<Long> textosProcessados = new HashSet<Long>();
	protected Set<TextoDto> textosInvalidos = new HashSet<TextoDto>();
	protected Set<TextoDto> textosIguaisAdicionados = new HashSet<TextoDto>();
	private String observacao;
	private Responsavel responsavel;
	@Qualifier("textoServiceLocal")
	@Autowired
	protected TextoService textoService;
	
	@Qualifier("usuarioService")
	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Valida e executa ação de transição para a lista de textos selecionados na tela.
	 * 
	 * <p>A verificação é realizada para determinar se um texto pertence
	 * a lista de textos iguais. Se sim, adicionar um aviso informando
	 * que o texto pertence a essa lista.
	 * 
	 * <p>Caso a validação não tenha capturado tenho texto em lista de textos
	 * iguais, executa a ação de transição, caso contrário, direciona
	 * o usuário para tela de confirmação.
	 */
	public void validateAndExecute() {
		for (TextoDto texto : getResources()) {
			adicionaInformacoesDeTextosIguais(texto, getResources());
		}
		defineFluxoExecucao();
	}

	/**
	 * Método que avalia se a ação deve ser executada imediatamente, ou se há alguma informação que deve ser mostrada antes.
	 */
	protected void defineFluxoExecucao() {
		// Remove os textos inválidos para executar a transição.
		getResources().removeAll(textosInvalidos);
		if (getResources().size() > 0) {
			if (hasInformations()) {
				sendToInformations();
			} else {
				execute();
			}
		} else {
			sendToErrors();
		}
	}

	/**
	 * Verifica se um texto possui textos iguais, e adiciona as mensagens correspondentes.
	 * Não adiciona textos iguais que já tenham sido selecionados no resultado da pesquisa.
	 * @param texto
	 */
	protected void adicionaInformacoesDeTextosIguais(TextoDto texto, Set<TextoDto> textosSelecionados) {
		try {
			List<Texto> textosIguais = recuperaTextosIguaisParaTransicaoDeFase(texto);
			if (textosIguais != null && textosIguais.size() > 0) {
				textosIguais.add(0, textoService.recuperarTextoPorId(texto.getId()));
				for (Texto textoIgual : textosIguais) {
					TextoDto textoIgualDto = new TextoDto();
					textoIgualDto.setId(textoIgual.getId());
					// Adiciona a mensagem mesmo que o texto tenha sido selecionado.
					if (! textosIguaisAdicionados.contains(textoIgualDto)) {
						addInformation(textoIgual.getIdentificacaoCompleta());
						textosIguaisAdicionados.add(textoIgualDto);
					}
				}
			}
		} catch (TransicaoDeFaseInvalidaException e) {
			textosInvalidos.add(texto);
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, texto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", texto.toString(), getMensagemDeErroPadrao(e)));
		} catch (ProcessoOcultoException e) {
			textosInvalidos.add(texto);
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, texto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", texto.toString(), getMensagemDeErroPadrao(e)));
		}
	}

	protected List<Texto> recuperaTextosIguaisParaTransicaoDeFase(TextoDto texto)
			throws TransicaoDeFaseInvalidaException, ProcessoOcultoException {
		return textoService.pesquisarTextosIguaisParaTransicaoFase(texto, getDestino());
	}

	/**
	 * Executa a ação de transição de textos, dentro de um contexto protegido
	 * para execução de ações.
	 * 
	 * @see ActionSupport#execute(ActionCallback)
	 */
	public void execute() {
		execute(new ActionCallback<TextoDto>() {
			public void doInAction(TextoDto texto) throws Exception {
				doExecute(texto);
			}
		});
		setRefresh(true);
	}

	/**
	 * Deve ser implementado pela subclasses para execução das regras
	 * de transição de estado.
	 * 
	 * @param texto o texto a ser transitado
	 * @throws Exception lançada caso ocorra algum problema inesperado
	 */
	protected void doExecute(TextoDto texto) throws Exception {
		try {
			textoService.alterarFase(texto, getDestino(), textosProcessados, getObservacao(), getResponsavel());
		} catch (TextoBloqueadoException e) {
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, texto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", texto.toString(), getMensagemDeErroPadrao(e)));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Retorna o tipo de transição que deverá ser executada.
	 * 
	 * @return o tipo de transição
	 */
	protected abstract TipoTransicaoFaseTexto getDestino();

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Set<TextoDto> getTextosIguaisAdicionados() {
		return textosIguaisAdicionados;
	}

	public void setTextosIguaisAdicionados(Set<TextoDto> textosIguaisAdicionados) {
		this.textosIguaisAdicionados = textosIguaisAdicionados;
	}

	public Responsavel getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Responsavel responsavel) {
		this.responsavel = responsavel;
	}
}
