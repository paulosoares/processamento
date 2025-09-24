/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.Responsavel;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.estf.processostf.model.service.TipoRecursoService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.entity.TipoSexo;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.jus.stf.estf.decisao.texto.support.ManterListaDeTextosException;
import br.jus.stf.estf.decisao.texto.support.ProcessoInvalidoParaListaDeTextosException;


/**
 * @author jacinto
 * @since 26.11.2015
 *
 */
@Action(id="replicarTextosActionFacesBean", 
		name="Replicar Varios Textos", 
		view="/acoes/texto/replicarVarios.xhtml",
		height=490)
@Restrict({ActionIdentification.CRIAR_TEXTOS_SEMELHANTES})
@RequiresResources(Mode.Many)
@CheckRestrictions
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO})
public class ReplicarTextosActionFacesBean extends
		ActionSupport<TextoDto> {

	private static final String PAGINA_LISTA_PROCESSOS = "listaDeProcessos";
	
	@Qualifier("textoServiceLocal")
	@Autowired
	private TextoService textoService;
	@Autowired
	private MinistroService ministroService;
	@Autowired
	private TipoRecursoService tipoRecursoService;
	@Autowired
	private SituacaoMinistroProcessoService situacaoMinistroProcessoService;
	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	@Autowired
	private UsuarioService usuarioService;
	
	private Responsavel responsavel;
	//private TextoDto texto;
	private Set<TextoDto> textos;
	private String identificacaoProcesso;
	
	private Collection<ObjetoIncidenteDto> processosParaSelecao;

	private String errorTitle;
	
	@Override
	public void load() {
		// Define o responsável padrão...
		responsavel = getUsuario();
		textos = getResources();
		
		/*
		texto = new TextoDto();
		texto.setId(getTextoSelecionado().getId());
		texto.setObservacao(getTextoSelecionado().getObservacao());
		texto.setIdUsuarioInclusao(getUsuario().getId());
		*/
		
	}
	
	private void adicionaProcessoNaTabela(ObjetoIncidenteDto objetoIncidente) throws ServiceException {
		getProcessosParaSelecao().add(objetoIncidente);
	}

	public void incluirProcessoSelecionado(ObjetoIncidenteDto objetoIncidenteSelecionado) {
		try {
			if (objetoIncidenteSelecionado == null) {
				throw new ProcessoInvalidoParaListaDeTextosException("Selecione um processo para inclusão na lista!");
			}
			if (isProcessoNaLista(objetoIncidenteSelecionado)) {
				addInformation("O processo selecionado já faz parte da lista!");
			} else {
				
				validaTextosProcessoDestino(textos,objetoIncidenteSelecionado, getMinistro());
				insereProcessoSelecionado(objetoIncidenteSelecionado, textos);
				
			}
		} catch (ProcessoInvalidoParaListaDeTextosException e) {
			addInformation(e.getMessage());			
		} catch (Exception e) {		
			if (e.getMessage()!=null) addError(e.getMessage());
		}	

		setIdentificacaoProcesso(null);
	}
	private void insereProcessoSelecionado(ObjetoIncidenteDto objetoIncidente, Set<TextoDto> listaTexto) throws ServiceException, ProcessoInvalidoParaListaDeTextosException {
		boolean possuiErros = false;
		for (TextoDto texto : listaTexto) {			
			Ministro ministroDoGabinete = getMinistro();
			try {						
				verificaTextoRegistrado(texto.getTipoTexto(), ministroDoGabinete, objetoIncidente);
				verificaControleDeVotos(texto, ministroDoGabinete, objetoIncidente, texto.getTipoTexto());
			}
			catch (ProcessoInvalidoParaListaDeTextosException e) {
				addInformation(e.getMessage());
				possuiErros = true;
			}
			
			
		}
		
		if (!possuiErros) adicionaProcessoNaTabela(objetoIncidente);
		
	}
	
	private boolean isProcessoNaLista(ObjetoIncidenteDto objetoIncidente) {
		if (objetoIncidente != null) {
			return isColecoesContemProcesso(objetoIncidente);
		}
		throw new RuntimeException("Ocorreu um erro ao recuperar o processo informado! Por favor, tente novamente!");
	}

	private boolean isColecoesContemProcesso(ObjetoIncidenteDto objetoIncidente) {
		return getProcessosParaSelecao().contains(objetoIncidente);
	}

	private void verificaControleDeVotos(TextoDto texto, Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente, TipoTexto tipoTextoDestino)
			throws ServiceException, ProcessoInvalidoParaListaDeTextosException {
		if (precisaValidarControleDeVotos(ministroDoGabinete, objetoIncidente, tipoTextoDestino)) {
			validaControleDeVotos(texto, ministroDoGabinete, objetoIncidente,tipoTextoDestino);
		}
	}

	private boolean precisaValidarControleDeVotos(Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente, TipoTexto tipoTextoDestino)
			throws ServiceException {
		
		return  !TipoTexto.DESPACHO.equals(tipoTextoDestino) && !TipoTexto.DECISAO_MONOCRATICA.equals(tipoTextoDestino) &&
					(!(isMinistroDoSetorRelatorDoProcesso(ministroDoGabinete, objetoIncidente) || getMinistroService()
					.isMinistroTemRelatoriaDaPresidencia(ministroDoGabinete, (Processo) objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()).getPrincipal())));
	}

	private void validaControleDeVotos(TextoDto texto, Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente, TipoTexto tipoTexto)
			throws ServiceException, ProcessoInvalidoParaListaDeTextosException {
		ControleVoto controleDeVoto = textoService.consultaControleDeVotoDoProcesso(tipoTexto, ministroDoGabinete, objetoIncidente);
		if (controleDeVoto == null && !TipoTexto.DESPACHO.equals(texto.getTipoTexto()) && !TipoTexto.DECISAO_MONOCRATICA.equals(texto.getTipoTexto())) {
			throw new ProcessoInvalidoParaListaDeTextosException(montaMensagemDeProcessoDeOutroRelator(objetoIncidente));
		}
	}

	private void verificaTextoRegistrado(TipoTexto tipoTexto, Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente)
			throws ServiceException, ProcessoInvalidoParaListaDeTextosException {
		if ((tipoTexto.equals(TipoTexto.ACORDAO) || tipoTexto.equals(TipoTexto.EMENTA)
				|| tipoTexto.equals(TipoTexto.RELATORIO) || tipoTexto.equals(TipoTexto.VOTO))
				&& textoService.existeTextoRegistradoParaProcesso(objetoIncidente, tipoTexto, ministroDoGabinete) ) {
			throw new ProcessoInvalidoParaListaDeTextosException(montaMensagemDeErroDeTextoRegistrado(objetoIncidente));
		}
	}

	private String montaMensagemDeProcessoDeOutroRelator(ObjetoIncidenteDto objetoIncidente) {
		StringBuilder sb = new StringBuilder();
		sb.append("O processo ");
		sb.append(objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()).getPrincipal().getIdentificacaoCompleta());
		sb.append(" pertence a outro relator.");
		return sb.toString();
	}

	private boolean isMinistroDoSetorRelatorDoProcesso(Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente)
			throws ServiceException {
		return getMinistroService().isMinistroRelatorDoProcesso(ministroDoGabinete, (Processo) objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()).getPrincipal());
	}

	private String montaMensagemDeErroDeTextoRegistrado(ObjetoIncidenteDto objetoIncidente) {
		StringBuilder sb = new StringBuilder();
		sb.append(objetoIncidente.getIdentificacao());
		sb.append(": Texto já registrado para este processo.");
		return sb.toString();
	}

	public void validateAndExecute() {
		cleanMessages();
		if( processosParaSelecao != null && processosParaSelecao.size() > 0 ) {
			if (responsavel == null) {
				addInformation("O responsável deve ser informado.");
			} else {
					
				for (TextoDto texto : textos) {
					texto.setResponsavel(responsavel.getId().toString());
					
					// Sempre seta como NÃO porque o texto está na fase EM ELABORAÇÃO
					texto.setLiberacaoAntecipada(false);
					
					try {
						try {
							objetoIncidenteService.registrarLogSistema(texto.getIdObjetoIncidente(), "CONSULTA_TEXTO", "Criar Replica de Texto: "+texto.getId()+" - "+texto.getTipoTexto().getDescricao(),texto.getId(),"STF.TEXTOS");
						} catch (ServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
						
			for (ObjetoIncidenteDto processo : processosParaSelecao) {
				try {
					validaTextosProcessoDestino(textos,processo, getMinistro());
				} catch (ManterListaDeTextosException e) {
					addError(e.getMessage());
				}
			}
						
			if (!hasMessages()) {
				execute();
			} else if (hasErrors()) {
				sendToErrors();
			}
		}
	}

	private void execute() {
		cleanMessages();
		try {
			for (TextoDto texto : textos) {
				textoService.criarListaTextosReplicados(texto, processosParaSelecao, getMinistro());
			}			
		} catch (Exception e) {
			addError(e.getMessage());
		}
		
		setRefresh(true);

		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			setErrorTitle("Erro ao gravar lista de textos.");
			sendToErrors();
		}
	}
	
	private void setErrorTitle(String error) {
		errorTitle = error;		
	}
	
	@Override
	public String getErrorTitle() {
		return errorTitle;
	}

	private void validaTextosProcessoDestino(Set<TextoDto> listaTextos, ObjetoIncidenteDto objetoIncidente, Ministro ministro) throws ManterListaDeTextosException {
		boolean possuiErros = false;
		for (TextoDto texto : listaTextos) {
						
			if (texto.getTipoTexto().equals(TipoTexto.ACORDAO) || texto.getTipoTexto().equals(TipoTexto.RELATORIO) || texto.getTipoTexto().equals(TipoTexto.EMENTA)) {
				for (TextoDto textoObjetoIncidente : textoService.recuperarTextos(objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()), false)) {
					if (textoObjetoIncidente.getTipoTexto().equals(texto.getTipoTexto())) {
						addError("O Texto com o tipo [" + texto.getTipoTexto().getDescricao() + "] já existe no processo " + objetoIncidente.getIdentificacao() + ".");
						possuiErros = true;
						//throw new ManterListaDeTextosException("O Texto com o tipo [" + texto.getTipoTexto().getDescricao() + "] já existe no processo " + objetoIncidente.getIdentificacao() + ".");
					}
				}
			} else if (texto.getTipoTexto().equals(TipoTexto.VOTO) || texto.getTipoTexto().equals(TipoTexto.VOTO_VOGAL) || texto.getTipoTexto().equals(TipoTexto.VOTO_VISTA)) {
				for (TextoDto textoObjetoIncidente : textoService.recuperarTextos(objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()), false)) {
					//Se for um voto e for do mesmo ministro, não deve deixar que o texto seja replicado (Só pode haver 1)
					if (textoObjetoIncidente.getTipoTexto().equals(texto.getTipoTexto()) && ministro.getId().equals(textoObjetoIncidente.getIdMinistro())) {
							
							addError(String.format("O Texto com o tipo [%s] já existe no processo %s para %s %s.",texto.getTipoTexto().getDescricao(), objetoIncidente.getIdentificacao(), getArtigoDoMinistro(ministro),ministro.getNome() ));
							possuiErros = true;
							//String mensagem =  String.format("O Texto com o tipo [%s] já existe no processo %s para %s %s.",texto.getTipoTexto().getDescricao(), objetoIncidente.getIdentificacao(), getArtigoDoMinistro(ministro),ministro.getNome() );
							//throw new ManterListaDeTextosException(mensagem);
						} 
					}
				}
		}
		if(possuiErros) {
			throw new ManterListaDeTextosException();
		}
	}	
	
	
	/**
	 * Verifica qual o artigo definido deve ser usado. Se for uma ministra, volta "a". Caso contrário, volta "o" 
	 * @param ministro
	 * @return
	 */
	private String getArtigoDoMinistro(Ministro ministro) {
		if (ministro.getTipoSexo().equals(TipoSexo.FEMININO)){
			return "a";
		}
		return "o";
	}

	public boolean isTextoDoProcesso(Texto textoGravado, ObjetoIncidente<Processo> objetoIncidente) throws ServiceException {
		return objetoIncidente.getId().equals(textoGravado.getObjetoIncidente().getId());
	}

	public void excluirProcessosSelecionados() {
		Collection<ObjetoIncidenteDto> processos = getProcessosParaSelecao();
		if(processos != null && processos.size()> 0) {
			Collection<ObjetoIncidenteDto> processosParaRetirar = new ArrayList<ObjetoIncidenteDto>();
			for (ObjetoIncidenteDto processo : processos) {
				if (processo.isSelected()) {
					processosParaRetirar.add(processo);
				}
			}
			processos.removeAll(processosParaRetirar);
		}
	}
	
	public void selectAll() {
		boolean check = !allChecked();
		for (ObjetoIncidenteDto processo : processosParaSelecao) {
			processo.setSelected(check);
		}
	}
	
	private boolean allChecked() {
    	for (ObjetoIncidenteDto dto : processosParaSelecao) {
    		if (!dto.isSelected()) {
    			return false;
    		}
    	}
    	return true;
    }

	public void voltarParaListaProcessos() {
		getDefinition().setFacet(PAGINA_LISTA_PROCESSOS);
	}
	
	public Collection<ObjetoIncidenteDto> getProcessosParaSelecao() {
		if (processosParaSelecao == null) {
			processosParaSelecao = new ArrayList<ObjetoIncidenteDto>();
		}
		return processosParaSelecao;
	}
	
	public List<TipoTexto> getTiposTexto() {
		List<TipoTexto> tipos = new ArrayList<TipoTexto>();
		
//		tipos.add(TipoTexto.DECISAO_MONOCRATICA);
//		tipos.add(TipoTexto.DESPACHO);
		tipos.add(TipoTexto.ACORDAO);
		tipos.add(TipoTexto.RELATORIO);
		tipos.add(TipoTexto.EMENTA);
		tipos.add(TipoTexto.VOTO);
		tipos.add(TipoTexto.OFICIO);
		tipos.add(TipoTexto.MEMORIA_DE_CASO);
	
		if( tipos != null && tipos.size() > 0 )
			Collections.sort(tipos);
		
		return tipos;
	}

	public TipoRecursoService getTipoRecursoService() {
		return tipoRecursoService;
	}

	public void setTipoRecursoService(TipoRecursoService tipoRecursoService) {
		this.tipoRecursoService = tipoRecursoService;
	}

	public SituacaoMinistroProcessoService getSituacaoMinistroProcessoService() {
		return situacaoMinistroProcessoService;
	}

	public void setSituacaoMinistroProcessoService(SituacaoMinistroProcessoService situacaoMinistroProcessoService) {
		this.situacaoMinistroProcessoService = situacaoMinistroProcessoService;
	}

	private MinistroService getMinistroService() {
		return ministroService;
	}

		
	public Responsavel getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Responsavel responsavel) {
		this.responsavel = responsavel;
	}

	public String getIdentificacaoProcesso() {
		return identificacaoProcesso;
	}

	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		this.identificacaoProcesso = identificacaoProcesso;
	}

	
	/*public Long getIdTipoTexto() {
		return idTipoTexto;
	}

	public void setIdTipoTexto(Long idTipoTexto) {
		this.idTipoTexto = idTipoTexto;
	}
	

	public TextoDto getTexto() {
		return texto;
	}

	public void setTexto(TextoDto texto) {
		this.texto = texto;
	}
	*/

	/*private TipoTexto getTipoTexto() {
		return TipoTexto.valueOf(idTipoTexto);
	}
	
	public void alterarTipoTexto(ValueChangeEvent event) {
		idTipoTexto = (Long) event.getNewValue();
	}*/
}
