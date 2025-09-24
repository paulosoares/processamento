/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.documento.model.service.exception.TextoNaoPodeSerAlteradoException;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.ColegiadoService;
import br.gov.stf.estf.julgamento.model.service.ListaJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.julgamento.model.util.ListaJulgamentoSearchData;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.jus.stf.estf.decisao.texto.support.ProcessoInvalidoParaListaDeTextosException;
import br.jus.stf.estf.decisao.texto.web.ReplicarTextoActionFacesBean.ListaJulgamentoSelecionadaLista;

/**
 * @author Paulo.Estevao
 * @since 21.07.2010
 */
public abstract class AbstractCriarEditarListaTextosIguaisActionFacesBean<T> extends ActionSupport<TextoDto> {
	
	@Qualifier("textoServiceLocal")
	@Autowired
	private TextoService textoService;
	@Autowired
	private MinistroService ministroService;
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	private String identificacaoProcesso;
	
	private Collection<ObjetoIncidenteDto> processosParaSelecao;
	private Collection<ObjetoIncidenteDto> processosValidos;
	private Collection<ObjetoIncidenteDto> processosComEmentaGerada;
	private Collection<String> mensagemDeProcessosComEmentaGerada;
	
	private String errorTitle;
	
	@Autowired
	private ListaJulgamentoService listaJulgamentoService;
	
	@Autowired
	private ColegiadoService colegiadoService;
	
	@Autowired
	private SessaoService sessaoService;	
	
	private List<SelectItem> listaColegiado;	
	private List<SelectItem> listaSessoes;
	private String idColegiadoPesquisa;
	private Long idRelatorPesquisa;
	private Long idSessaoPesquisa;
	private List<ListaJulgamentoSelecionadaLista> listasJulgamentoPesquisadas;
	private HashMap<String,List<SelectItem>> mapaRelatoresPorColegiado;
	private HashMap<String,List<SelectItem>> mapaSessoesPorColegiado;
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public void load() {
		try {
			if(getTextoSelecionado().isTextosIguais()) {
				carregaListaDeTextosIguais(getTextoSelecionado());
			}
			textoService.validaTextoParaAlteracao(getTextoSelecionado());
		} catch(ServiceException e) {
			addError(e.getMessage());
		} catch (TextoNaoPodeSerAlteradoException e) {
			addError(e.getMessage());
		}
		
		if(hasMessages()) {
			setErrorTitle("Erro ao carregar lista de textos iguais");
			sendToErrors();
		}
	}
	
	private void carregaListaDeTextosIguais(TextoDto texto) throws ServiceException {
		List<ObjetoIncidenteDto> objetosIncidente = textoService.recuperarProcessosListaTextosIguais(texto);
		for (ObjetoIncidenteDto oi : objetosIncidente) {
			getProcessosValidos().add(oi);
			getProcessosParaSelecao().add(oi);
		}
	}
	
	public void validateAndExecute() {
		if (getMensagemDeProcessosComEmentaGerada().isEmpty()) {
			execute(false);
		} else {
			for (String processo : getMensagemDeProcessosComEmentaGerada()) {
				addInformation(processo);
			}
			sendToInformations();
			getDefinition().setFacet("listaDeProcessosComEmentaGerada");
		}
	}
	
	public void excluirProcessosSelecionados() {
		Collection<ObjetoIncidenteDto> processos = getProcessosParaSelecao();
		Collection<ObjetoIncidenteDto> processosParaRetirar = new ArrayList<ObjetoIncidenteDto>();
		for (ObjetoIncidenteDto processo : processos) {
			if (processo.isSelected()) {
				processosParaRetirar.add(processo);
				getProcessosValidos().remove(processo);
				getProcessosComEmentaGerada().remove(processo);
			}
		}
		processos.removeAll(processosParaRetirar);
	}
	
	public void execute() {
		execute(false);
	}
	
	public void sobrescreverEmenta() {
		execute(true);
	}
		
	private void execute(boolean sobrescreverEmenta) {
		cleanMessages();
		try {
			if(sobrescreverEmenta) {
				getProcessosValidos().addAll(getProcessosComEmentaGerada());
				getProcessosComEmentaGerada().clear();
			}
			
			Collection<ObjetoIncidenteDto> escolhidos = new ArrayList<ObjetoIncidenteDto>();
			
			for(ObjetoIncidenteDto processo: getProcessosSelecionados())
				if (!getTextoSelecionado().getIdObjetoIncidente().equals(processo.getId()))
					escolhidos.add(processo);
			
			textoService.criarEditarListaTextosIguais(getTextoSelecionado(), escolhidos, getMinistro(), getUsuario(), sobrescreverEmenta);
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

	private Collection<ObjetoIncidenteDto> getProcessosSelecionados() {
		Collection<ObjetoIncidenteDto> todos = getProcessosParaSelecao();
		Collection<ObjetoIncidenteDto> selecionados = new ArrayList<ObjetoIncidenteDto>();
		
		for (ObjetoIncidenteDto processo : todos)
			if (processo.isSelected())
				selecionados.add(processo);
		
		return selecionados;
	}

	public void incluirProcessoSelecionado(ObjetoIncidenteDto objetoIncidenteSelecionado) {
		try {
			if (objetoIncidenteSelecionado == null) {
				throw new ProcessoInvalidoParaListaDeTextosException("Selecione um processo para inclusão na lista!");
			}
			if (isProcessoNaLista(objetoIncidenteSelecionado)) {
				addInformation("O processo selecionado já faz parte da lista!");
			} else {
				insereProcessoSelecionado(objetoIncidenteSelecionado, getTextoSelecionado());
			}
		} catch (ProcessoInvalidoParaListaDeTextosException e) {
			addInformation(e.getMessage());
		} catch (Exception e) {
			addError(e.getMessage());
		}		
		identificacaoProcesso = null;
	}
	
	public void selectAll() {
		boolean check = !allChecked();
		for (ObjetoIncidenteDto processo : processosValidos) {
			processo.setSelected(check);
		}
	}
	
	private boolean allChecked() {
    	for (ObjetoIncidenteDto dto : processosValidos) {
    		if (!dto.isSelected()) {
    			return false;
    		}
    	}
    	return true;
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
	
	
	
	private void insereProcessoSelecionado(ObjetoIncidenteDto objetoIncidente, TextoDto texto) throws ServiceException, ProcessoInvalidoParaListaDeTextosException {
		Ministro ministroDoGabinete = getMinistro();
		verificaTextoRegistrado(texto, ministroDoGabinete, objetoIncidente);
		verificaControleDeVotos(texto, ministroDoGabinete, objetoIncidente);
		adicionaProcessoNaTabela(objetoIncidente);
	}
	
	private void verificaTextoRegistrado(TextoDto texto, Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente)
			throws ServiceException, ProcessoInvalidoParaListaDeTextosException {
		if ((texto.getTipoTexto().equals(TipoTexto.ACORDAO) || texto.getTipoTexto().equals(TipoTexto.RELATORIO) || texto.getTipoTexto().equals(TipoTexto.VOTO))
				&& textoService.existeTextoRegistradoParaProcesso(objetoIncidente, texto.getTipoTexto(), ministroDoGabinete) ) {
			throw new ProcessoInvalidoParaListaDeTextosException(montaMensagemDeErroDeTextoRegistrado(objetoIncidente, texto));
		}
	}
	
	private String montaMensagemDeErroDeTextoRegistrado(ObjetoIncidenteDto objetoIncidente, TextoDto texto) {
		StringBuilder sb = new StringBuilder();
		sb.append(objetoIncidente.getIdentificacao());
		sb.append(": Texto já registrado para este processo.");
		return sb.toString();
	}
	
	private void verificaControleDeVotos(TextoDto texto, Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente)
			throws ServiceException, ProcessoInvalidoParaListaDeTextosException {
		if (precisaValidarControleDeVotos(ministroDoGabinete, objetoIncidente)) {
			validaControleDeVotos(texto, ministroDoGabinete, objetoIncidente);
		}
	}
	
	private void validaControleDeVotos(TextoDto texto, Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente)
			throws ServiceException, ProcessoInvalidoParaListaDeTextosException {
		ControleVoto controleDeVoto = textoService.consultaControleDeVotoDoProcesso(texto.getTipoTexto(), ministroDoGabinete, objetoIncidente);
		
		if (controleDeVoto == null && !TipoTexto.DESPACHO.equals(texto.getTipoTexto()) && !TipoTexto.DECISAO_MONOCRATICA.equals(texto.getTipoTexto()) && !TipoTexto.VOTO_VOGAL.equals(texto.getTipoTexto()))
			throw new ProcessoInvalidoParaListaDeTextosException(montaMensagemDeProcessoDeOutroRelator(objetoIncidente));
	}
	
	private String montaMensagemDeProcessoDeOutroRelator(ObjetoIncidenteDto objetoIncidente) {
		StringBuilder sb = new StringBuilder();
		sb.append("O processo ");
		sb.append(objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()).getPrincipal().getIdentificacaoCompleta());
		sb.append(" pertence a outro relator.");
		return sb.toString();
	}
	
	private boolean precisaValidarControleDeVotos(Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente)
		throws ServiceException {
	return !(isMinistroDoSetorRelatorDoProcesso(ministroDoGabinete, objetoIncidente) || getMinistroService()
			.isMinistroTemRelatoriaDaPresidencia(ministroDoGabinete, (Processo) objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()).getPrincipal()));
	}
	
	private void adicionaProcessoNaTabela(ObjetoIncidenteDto objetoIncidente) throws ServiceException {
		if (TipoTexto.EMENTA.equals(getTextoSelecionado().getTipoTexto())) {
			TextoDto ementaGerada = textoService.recuperaEmentaGeradaParaProcesso(getTextoSelecionado(), objetoIncidente);
			if (ementaGerada != null) {
				getProcessosComEmentaGerada().add(objetoIncidente);
				adicionaMensagemDeEmentaGerada(objetoIncidente, ementaGerada);
			} else {
				getProcessosValidos().add(objetoIncidente);
			}
		} else {
			getProcessosValidos().add(objetoIncidente);
		}
		getProcessosParaSelecao().add(objetoIncidente);
	}
	
	private void adicionaMensagemDeEmentaGerada(ObjetoIncidenteDto objetoIncidente, TextoDto ementaGerada) {
		getMensagemDeProcessosComEmentaGerada().add(objetoIncidente.getIdentificacao() + " - " + ementaGerada.getMinistro());
	}
	
	private boolean isMinistroDoSetorRelatorDoProcesso(Ministro ministroDoGabinete, ObjetoIncidenteDto objetoIncidente)
			throws ServiceException {
		return getMinistroService().isMinistroRelatorDoProcesso(ministroDoGabinete, (Processo) objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()).getPrincipal());
	}
	
	private MinistroService getMinistroService() {
		return ministroService;
	}
	
	public Collection<ObjetoIncidenteDto> getProcessosParaSelecao() {
		if (processosParaSelecao == null) {
			processosParaSelecao = new ArrayList<ObjetoIncidenteDto>();
		}
		return processosParaSelecao;
	}
	
	public Collection<ObjetoIncidenteDto> getProcessosValidos() {
		if (processosValidos == null) {
			processosValidos = new ArrayList<ObjetoIncidenteDto>();
		}
		return processosValidos;
	}
	
	public Collection<ObjetoIncidenteDto> getProcessosComEmentaGerada() {
		if (processosComEmentaGerada == null) {
			processosComEmentaGerada = new ArrayList<ObjetoIncidenteDto>();
		}
		return processosComEmentaGerada;
	}
	
	public Collection<String> getMensagemDeProcessosComEmentaGerada() {
		if (mensagemDeProcessosComEmentaGerada == null) {
			mensagemDeProcessosComEmentaGerada = new ArrayList<String>();
		}
		return mensagemDeProcessosComEmentaGerada;
	}
	
	public TextoDto getTextoSelecionado() {
		if (getResources() != null && getResources().size() == 1) {
			return (TextoDto) getResources().toArray()[0];
		}
		throw new IllegalArgumentException("Não é possível recuperar o texto selecionado!");
	}
	
	public String getIdentificacaoProcesso() {
		return identificacaoProcesso;
	}

	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		this.identificacaoProcesso = identificacaoProcesso;
	}
	
	private void setErrorTitle(String error) {
		errorTitle = error;
	}
	
	@Override
	public String getErrorTitle() {
		return errorTitle;
	}

	public List<SelectItem> getListaColegiado(){
		if (listaColegiado != null)
			return listaColegiado;
		listaColegiado = new ArrayList<SelectItem>();
		try{
			SelectItem branco = new SelectItem("0", "");
			List<Colegiado> colegiados = colegiadoService.pesquisar();
			listaColegiado.add(branco);
			for (Colegiado colegiado : colegiados){
				listaColegiado.add(new SelectItem(colegiado.getId(), colegiado.getDescricao()));
			}
		}catch(ServiceException e){
			addError("Problema ao recuperar relação de colegiados: " + e.getMessage());
		}
		return listaColegiado;				
	}
	
	public void setListaColegiado(List<SelectItem> lista){
		this.listaColegiado = lista;
	}
	
	public List<SelectItem> getListaSessoes(){		
		if (mapaSessoesPorColegiado == null)
			mapaSessoesPorColegiado = new HashMap<String, List<SelectItem>>();
		if (mapaSessoesPorColegiado.get(idColegiadoPesquisa) != null)
			return mapaSessoesPorColegiado.get(idColegiadoPesquisa);

		String tipoColegiado = idColegiadoPesquisa;
		if (idColegiadoPesquisa.equals("0"))
			tipoColegiado = null;
		
		
		List<SelectItem> listaRetorno = new ArrayList<SelectItem>();
		listaRetorno.add(new SelectItem("0", ""));
		try {
			List<Sessao> sessoes = sessaoService.pesquisarSessao(null, null, TipoAmbienteConstante.VIRTUAL, null, tipoColegiado);						
			List<SelectItem> listaAux = formatarOrdenarSessoesVirtuais(sessoes);
			listaRetorno.addAll(listaAux);
			mapaSessoesPorColegiado.put(idColegiadoPesquisa, listaRetorno);
		} catch (ServiceException e) {
			addError("Erro ao recuperar relação de sessões.");		
		}
								
		return listaRetorno;
	}
	
	private List<SelectItem> formatarOrdenarSessoesVirtuais(List<Sessao> listaSessoes) {
		List<Sessao> listaSessoesTemp = new ArrayList<Sessao>();
		for (Sessao sessao : listaSessoes){
			if (sessao.getTipoJulgamentoVirtual() == null || !sessao.getTipoJulgamentoVirtual().equals(Sessao.TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.getId()))
				continue;
			listaSessoesTemp.add(sessao);
		}
		
		Collections.sort(listaSessoesTemp, new Comparator<Sessao>(){
			@Override
			public int compare(Sessao s1, Sessao s2) {
				if (!s1.getAno().equals(s2.getAno()))
					return -s1.getAno().compareTo(s2.getAno());
				if ( s1.getNumero() == null){
					if(s1.getNumero() == s2.getNumero())
						return s1.getColegiado().getDescricao().compareTo(s2.getColegiado().getDescricao()) * (-1);
					else
						return 1;						
				}
				else{
					if (s2.getNumero() == null)
						return -1;
					else{
						if (s1.getNumero().equals(s2.getNumero()))
							return s1.getColegiado().getDescricao().compareTo(s2.getColegiado().getDescricao());												
						return -s1.getNumero().compareTo(s2.getNumero());
					}
				}				
			}			
		});
		
		List<SelectItem> listaSelectSessoes = new ArrayList<SelectItem>();
		for (Sessao sessao : listaSessoesTemp) {						
						
			String colegiado = sessao.getColegiado().getDescricao();
			Long numero = sessao.getNumero();
			String dataInicio = sessao.getDataInicio() == null?  formatter.format(sessao.getDataPrevistaInicio()): formatter.format(sessao.getDataInicio());
			String dataFim    = sessao.getDataFim() == null?  formatter.format(sessao.getDataPrevistaFim()): formatter.format(sessao.getDataFim());
			String label = numero + " Sessão Virtual (" + colegiado + ") - " +  dataInicio + " a " + dataFim;
			SelectItem e = new SelectItem(sessao.getId(), label);
			listaSelectSessoes.add(e);
		}
		
		return listaSelectSessoes;
	}
	
	public void pesquisarListasJulgamento(){
		
		setListasJulgamentoPesquisadas(new ArrayList<ListaJulgamentoSelecionadaLista>());
		ListaJulgamentoSearchData searchData = new ListaJulgamentoSearchData();
		if (idColegiadoPesquisa != null && !idColegiadoPesquisa.equals("0")){
			if (idColegiadoPesquisa.equals("1T"))
				searchData.setPrimeiraTurma(Boolean.TRUE);
			else if (idColegiadoPesquisa.equals("2T"))
				searchData.setSegundaTurma(Boolean.TRUE);
			else if (idColegiadoPesquisa.equals("TP"))
				searchData.setPlenario(Boolean.TRUE);			
		}
		
		if(idRelatorPesquisa != null && idRelatorPesquisa != 0)
			searchData.setCodigoMinistroRelator(idRelatorPesquisa);
		if (idSessaoPesquisa != null && idSessaoPesquisa != 0)
			searchData.setIdSessao(String.valueOf(idSessaoPesquisa));
		
		try{
			SearchResult<ListaJulgamento> pesquisa = listaJulgamentoService.pesquisarListaJulgamentoPlenarioVirtual(searchData);
			List<ListaJulgamento> listasRetornadas = (List<ListaJulgamento>)pesquisa.getResultCollection();						
			if (listasRetornadas == null || listasRetornadas.isEmpty()){
				addInformation("Nenhuma lista encontrada.");
				return;
			}
			for(ListaJulgamento lista : listasRetornadas){
				ListaJulgamentoSelecionadaLista listaPesquisada = new ListaJulgamentoSelecionadaLista();
				listaPesquisada.setListaJulgamento(lista);
				listaPesquisada.setSelected(Boolean.FALSE);
				listaPesquisada.setDescricao(lista.getNome() + " - " + lista.getMinistro().getNome());
				getListasJulgamentoPesquisadas().add(listaPesquisada);
			}
			
		}catch(ServiceException e){
			addError("Erro ao pesquisar as listas de julgamento.");
		}		
	}
	
	public void selectAllListasPesquisados() {
		if (listasJulgamentoPesquisadas == null || listasJulgamentoPesquisadas.isEmpty())
			return;
		boolean check = !allListasPesquisadasChecked();
		for (ListaJulgamentoSelecionadaLista lista : listasJulgamentoPesquisadas) {
			lista.setSelected(check);
		}
	}
	
	private boolean allListasPesquisadasChecked() {
    	for (ListaJulgamentoSelecionadaLista lista : listasJulgamentoPesquisadas) {
    		if (!lista.getSelected()) {
    			return false;
    		}
    	}
    	return true;
    }
	
	public void adicionarListaProcessos(){
		for (ListaJulgamentoSelecionadaLista lista : listasJulgamentoPesquisadas){
			if (!lista.getSelected())
				continue;			
			try{
				ListaJulgamento listaAux = listaJulgamentoService.recuperarPorId(lista.getListaJulgamento().getId());
				for (ObjetoIncidente<?> oi : listaAux.getElementos()){					
					ObjetoIncidenteDto dto = ObjetoIncidenteDto.valueOf(oi);
					incluirProcessoSelecionado(dto);
				}
				setListasJulgamentoPesquisadas(new ArrayList<ListaJulgamentoSelecionadaLista>());
			}catch(ServiceException e){
				addError("Ocorreu um erro ao recuperar os processos das listas de julgamento.");
			}
		}
	}
	
	public String getIdColegiadoPesquisa() {
		if(idColegiadoPesquisa == null)
			idColegiadoPesquisa = "0";
		return idColegiadoPesquisa;
	}

	public void setIdColegiadoPesquisa(String idColegiadoPesquisa) {		
		this.idColegiadoPesquisa = idColegiadoPesquisa;
	}

	public Long getIdRelatorPesquisa() {
		return idRelatorPesquisa;
	}
		
	public void setIdRelatorPesquisa(Long idRelatorPesquisa) {
		this.idRelatorPesquisa = idRelatorPesquisa;
	}

	public Long getIdSessaoPesquisa() {
		return idSessaoPesquisa;
	}

	public void setIdSessaoPesquisa(Long idSessaoPesquisa) {
		this.idSessaoPesquisa = idSessaoPesquisa;
	}
	
	public void setListasSelecionadas(List<SelectItem> lista){
		
	}
	
	public List<SelectItem> getListasSelecionadas(){
		List<SelectItem> l = new ArrayList<SelectItem>();
		l.add(new SelectItem("0", "lista 1"));
		return l;
	}
	
	public List<ListaJulgamentoSelecionadaLista> getListasJulgamentoPesquisadas() {
		if (listasJulgamentoPesquisadas == null)
			listasJulgamentoPesquisadas = new ArrayList<ListaJulgamentoSelecionadaLista>();
		return listasJulgamentoPesquisadas;
	}

	public void setListasJulgamentoPesquisadas(
			List<ListaJulgamentoSelecionadaLista> listasJulgamentoPesquisadas) {
		this.listasJulgamentoPesquisadas = listasJulgamentoPesquisadas;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaRelator() {		
		if (mapaRelatoresPorColegiado == null)
			mapaRelatoresPorColegiado = new HashMap<String, List<SelectItem>>();
		if (mapaRelatoresPorColegiado.get(idColegiadoPesquisa) != null)
			return mapaRelatoresPorColegiado.get(idColegiadoPesquisa);
		
		List<Ministro> relatores = pesquisarRelator(idColegiadoPesquisa);
		List<SelectItem> listaRetorno = new ArrayList<SelectItem>();
		listaRetorno.add(new SelectItem("0", ""));
		for (Ministro relator : relatores){
			listaRetorno.add(new SelectItem(relator.getId(), relator.getNome()));
		}
		Collections.sort(listaRetorno, new Comparator<SelectItem>(){

			@Override
			public int compare(SelectItem s1, SelectItem s2) {
				return s1.getLabel().compareTo(s2.getLabel());
			}

			
			
		});
		mapaRelatoresPorColegiado.put(idColegiadoPesquisa, listaRetorno);
		if(idColegiadoPesquisa.equals("0"))
			mapaRelatoresPorColegiado.put(Colegiado.TRIBUNAL_PLENO, listaRetorno); //a lista para o plenário deve incluir todos os ministros, sem necessidade de filtro		
		return listaRetorno;
	}
	
	private List<Ministro> pesquisarRelator(String idColegiado){
		Boolean primeiraTurma = Boolean.FALSE, segundaTurma = Boolean.FALSE, plenario = Boolean.FALSE;
		if (idColegiado == null)
			idColegiado = "0";
		if (idColegiado.equals(Colegiado.PRIMEIRA_TURMA))
			primeiraTurma = Boolean.TRUE;
		else if (idColegiado.equals(Colegiado.SEGUNDA_TURMA))
			segundaTurma = Boolean.TRUE;
		else if (idColegiado.equals(Colegiado.TRIBUNAL_PLENO))
			plenario = Boolean.TRUE;
		
		try{
			List<Ministro> ministros = ministroService.pesquisarMinistros(Boolean.TRUE, Boolean.TRUE, primeiraTurma, segundaTurma, plenario);
			return ministros;
		}catch(ServiceException e){
			addError("Erro ao recuperar relação de ministros");
			return new ArrayList<Ministro>();
			
		}
			
	}
	
	public class ListaJulgamentoSelecionadaLista{
		
		private ListaJulgamento listaJulgamento;
		private Boolean selected;
		private String descricao;
		
		public ListaJulgamento getListaJulgamento() {
			return listaJulgamento;
		}
		public void setListaJulgamento(ListaJulgamento listaJulgamento) {
			this.listaJulgamento = listaJulgamento;
		}
		public Boolean getSelected() {
			return selected;
		}
		public void setSelected(Boolean selected) {
			this.selected = selected;
		}				
		
		public String getDescricao(){
			return descricao;
		}
		
		public void setDescricao(String descricao){
			this.descricao = descricao;
		}
	}
	
	
}
