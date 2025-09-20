package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.math.NumberUtils;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.relatorio.service.impl.ProcessamentoRelatorioServiceLocal;
import br.gov.stf.estf.assinatura.service.exception.ProcessoComOrigemGenericaException;
import br.gov.stf.estf.assinatura.service.exception.ProcessoSemOrigemDefinidaException;
import br.gov.stf.estf.assinatura.stficp.RequisicaoAssinaturaDocumentoComunicacao;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.assinatura.visao.util.InfoPecaVinculadoAndamentoDTO;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.ControleVotoDto;
import br.gov.stf.estf.documento.model.util.ResultadoControleVotoPDF;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.SituacaoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.estf.processostf.model.service.exception.AndamentoNaoAutorizadoException;
import br.gov.stf.estf.processostf.model.service.exception.ValidationException;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfoImpl;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.stfoffice.handler.HandlerException;

public class BeanRegistrarAndamentoParaVariosProcessos extends AbstractBeanRegistrarAndamento {

	private static final long serialVersionUID = 1L;

	public static final String TIPO_INCLUSAO_PROCESSO = "PROCESSO";
	public static final String TIPO_INCLUSAO_GUIA = "GUIA";
	public static final String TIPO_INCLUSAO_IMPORTACAO = "IMPORTACAO";

	private static final Object OBSERVACAO = new Object();
	private static final Object OBSERVACAO_INTERNA = new Object();
	private static final Object TIPO_INCLUSAO = new Object();
	private static final Object NUMERO_GUIA = new Object();
	private static final Object ANO_GUIA = new Object();
	private static final Object GUIA_IMPORTADA = new Object();
	private static final Object MAPA_INCIDENTES = new Object();
	private static final List<Long> ANDAMENTOS_NAO_PERMITIDOS  = new ArrayList<Long>(Arrays.asList(8204L,8238L));
	private static final String MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO = "MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO";
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final Long ANDAMENTO_VISTAPGR = 8303L;
	private static final Long ANDAMENTO_VISTA_AGU = 8302L;
	private static final Long ANDAMENTO_AUTOS_DISPONIBILIZADOS = 8561L;
	private static final Long ANDAMENTO_VISTAPGR_INTIMACAO = 8507L;
	private static final Long ANDAMENTO_VISTA_AO_MINISTRO = 8304L;
	private static final int ORGAO_EXTERNO = 3;
	private static final int ORGAO_INTERNO = 2;
	private static final Long ANDAMENTO_CANCELAMENTO_PROTOCOLO = 7116L;
	
	private String tipoInclusao;
	private String numeroGuia;
	private String anoGuia;
	private HtmlDataTable tabelaProcessos;
	private Guia guiaImportada;
	private String identificacaoProcessos;
	private List<ObjetoIncidente<?>> listaProcessoImportacao;
	private boolean lancarAndamentoEAssinarDocumento = false;
	private List<Processo> processosApenso = new ArrayList<Processo>();
	
	public static final Long CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO = 600000857L;
	private boolean processoInativo = false;
	private boolean existemAcordaosPendentesDePublicacao = false;
	private String mensagemDeRestricaoRegistroDeAndamento = null;
	
	public static List<Long> ORIGENS_GENERICAS = Arrays.asList(Origem.TRIBUNAL_REGIONAL_FEDERAL,Origem.TRIBUNAL_DE_JUSTICA_MILITAR_ESTADUAL,Origem.TRIBUNAL_JUSTICA_ESTADUAL,Origem.TRIBUNAL_DE_JUSTICA_ESTADUAL,Origem.TURMA_RECURSAL_DE_JUIZADOS_ESPECIAIS_ESTADUAIS, Origem.TURMA_RECURSAL_DOS_JUIZADOS_ESPECIAIS_FEDERAIS);
	public static List<Long> CODIGOS_BAIXA_REMESSA = Arrays.asList(7101L, 7104L, 7108L);

	private ObjetoIncidente<?> incidenteSelecionado;
	private Comunicacao comunicacao;
	private List<PecaDTO> listaPecasDTO;
	List<PecaProcessoEletronico> listaOriginal = new ArrayList<PecaProcessoEletronico>();
	
	public BeanRegistrarAndamentoParaVariosProcessos() {
		restaurarSessao();
	}

	public void setExisteProcessoNaLista(Boolean existeProcessoNaLista) {
		// Incluído apenas para que o JSF seja usado
	}

	public Boolean getExisteProcessoNaLista() {
		return processosSelecionados != null && processosSelecionados.size() > 0;
	}

	public Guia getGuiaImportada() {
		return guiaImportada;
	}

	public void setGuiaImportada(Guia guiaImportada) {
		this.guiaImportada = guiaImportada;
	}

	public HtmlDataTable getTabelaProcessos() {
		return tabelaProcessos;
	}

	public void setTabelaProcessos(HtmlDataTable tabelaProcessos) {
		this.tabelaProcessos = tabelaProcessos;
	}

	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		this.numeroGuia = numeroGuia;
	}

	public String getAnoGuia() {
		if (anoGuia == null) {
			anoGuia = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		}
		return anoGuia;
	}

	public void setAnoGuia(String anoGuia) {
		this.anoGuia = anoGuia;
	}

	public String getTipoInclusao() {
		if (tipoInclusao == null) {
			tipoInclusao = TIPO_INCLUSAO_PROCESSO;
		}

		return tipoInclusao;
	}

	public void setTipoInclusao(String tipoInclusao) {
		this.tipoInclusao = tipoInclusao;
	}

	public Integer getNumeroProcessosSelecionados() {
		return getProcessosSelecionados().size();
	}

	public void atualizarSessao() {
		setAtributo(OBSERVACAO, observacao);
		setAtributo(OBSERVACAO_INTERNA, observacaoInterna);
		setAtributo(TIPO_INCLUSAO, tipoInclusao);
		setAtributo(NUMERO_GUIA, numeroGuia);
		setAtributo(ANO_GUIA, anoGuia);
		setAtributo(GUIA_IMPORTADA, guiaImportada);
		setAtributo(MAPA_INCIDENTES, mapaIncidentes);
		super.atualizarSessao();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void restaurarSessao() {
		observacao = (String) getAtributo(OBSERVACAO);
		observacaoInterna = (String) getAtributo(OBSERVACAO_INTERNA);
		tipoInclusao = (String) getAtributo(TIPO_INCLUSAO);
		numeroGuia = (String) getAtributo(NUMERO_GUIA);
		anoGuia = (String) getAtributo(ANO_GUIA);
		guiaImportada = (Guia) getAtributo(GUIA_IMPORTADA);
		mapaIncidentes = (Map<Processo, ObjetoIncidente<?>>) getAtributo(MAPA_INCIDENTES);
		mensagemDeRestricaoRegistroDeAndamento = (String)getAtributo(MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO);
		super.restaurarSessao();
	}
	
	public String getEstiloVisibilidadeImportacao(){
		return getEstiloVisibilidadeInclusaoPor(TIPO_INCLUSAO_IMPORTACAO);
	}

	public String getEstiloVisibilidadeGuia() {
		return getEstiloVisibilidadeInclusaoPor(TIPO_INCLUSAO_GUIA);
	}

	private String getEstiloVisibilidadeInclusaoPor(String tipoInclusaoReferencia) {
		if (!tipoInclusaoReferencia.equals(tipoInclusao)) {
			return "display:none";
		}
		return "";

	}

	public String getEstiloVisibilidadeProcesso() {
		return getEstiloVisibilidadeInclusaoPor(TIPO_INCLUSAO_PROCESSO);
	}

	public String getDataAtual() {
		return DATE_FORMAT.format(new Date());
	}

	@Override
	protected List<Andamento> doPesquisarAndamentosParaLista() throws ServiceException {
		return getAndamentoService().pesquisarAndamentosAutorizadosParaLote(getSetorUsuarioAutenticado());
	}

	@Override
	protected void doAdicionarProcesso(Processo processo) throws ServiceException {
		
		if (andamentoSelecionado != null && CODIGOS_BAIXA_REMESSA.contains(andamentoSelecionado.getId())) {
			String tipoMeioProcesso = processo.getTipoMeio();
			for (Processo processoItemLista : processosSelecionados) {
				if (!processoItemLista.getTipoMeio().equals(tipoMeioProcesso)) {
					reportarInformacao(String.format("[%s]: Para registrar este andamento todos os processos devem ser de mesma natureza (apenas físicos ou apenas eletrônicos)", processo.getIdentificacao()));
					return;
				}
			}
		}
		
		if (!getProcessosSelecionados().contains(processo)) {
			
			try{
				recuperarOrigemBaixaAutomatica(processo);
			} catch (Exception  e){
				reportarErro("Não foi possível recuperar a origem para baixa automatica!", e.getMessage());
				log.error("Nao foi possivel recuperar a origem para baixa automatica!", e);
				return;
			}
			
			getProcessosSelecionados().add(processo);
			montarMensagemRestricao();			
		}else{
			reportarAviso(String.format("[%s]: Processo já adicionado", processo.getIdentificacao()));
		}
		identificacaoProcesso = "";

		processoSelecionado = null;
	}

	public void atualizarTipoInclusao(ActionEvent evt) {
		limparDadosTipoInclusao();
	}

	private void limparDadosTipoInclusao() {
		getProcessosSelecionados().clear();
		setIdentificacaoProcesso(null);
		setNumeroGuia(null);
		setAnoGuia(null);
		setGuiaImportada(null);
	}

	public void importarGuia(ActionEvent evt) {
		try {
			if (NumberUtils.isNumber(numeroGuia) && NumberUtils.isNumber(anoGuia)) {
				pesquisaDadosDaGuia();
			} else {
				reportarAviso("Os dados da guia devem ser informados!");
			}
		} catch (Exception e) {
			reportarErro("Ocorreu um erro ao importar a Guia!", e.getMessage());
			log.error("Ocorreu um erro ao importar a Guia!", e);
		}
	}
	
	private void pesquisaDadosDaGuia() throws ServiceException, HandlerException {
		Guia guia = montaGuiaParaPesquisa();
		if (guia == null) {
			reportarAviso("Guia não encontrada!");
			return;
		}

		if ( (getGuiaService().isEletronico(guia)) && 
				(   andamentoSelecionado.getId().equals(new Long(7101)) ||
					andamentoSelecionado.getId().equals(new Long(7104)) ||
					andamentoSelecionado.getId().equals(new Long(7108)) ) ) {
			reportarAviso("A utilização da funcionalidade Registrar Andamento para vários Processos não é permitida para processos eletrônicos ou guias eletrônicas.");
			return;
		}
		// recuperar o destinatário do endereço (informação da baixa) para a observação do andamento
		if (guia.getEnderecoDestinatario() == null) {
			setObservacao(guia.getId().getNumeroGuia() + "/" + guia.getId().getAnoGuia() + " - " + guia.getDescricaoDestino());
		} else {
			setObservacao(guia.getId().getNumeroGuia() + "/" + guia.getId().getAnoGuia() + " - " + guia.getDescricaoDestino() + " / " + guia.getEnderecoDestinatario().getDestinatario().getNomDestinatario());
		}
		List<DeslocaProcesso> listaDeProcessos = getDeslocaProcessoService().recuperarDeslocamentoProcessos(guia);
		if (listaDeProcessos.size() > 0) {
			carregaDadosDaGuia(guia, listaDeProcessos);
		} else {
			processosSelecionados.clear();
			reportarAviso("Não existem processos na Guia de Deslocamento informada!");
		}
	}

	private void carregaDadosDaGuia(Guia guia, List<DeslocaProcesso> listaDeProcessos) throws ServiceException, HandlerException {
		
		processosSelecionados.clear();
		for (DeslocaProcesso deslocaProcesso : listaDeProcessos) {
			recuperarObjetoIncidente(deslocaProcesso.getId().getProcesso(), deslocaProcesso.getId().getProcesso().getIdentificacao());
			adicionarProcesso(deslocaProcesso.getId().getProcesso());
		}
		guiaImportada = guia;
		atualizarSessao();
	}

	private Guia montaGuiaParaPesquisa() throws ServiceException {
		Guia guia = new Guia();
		GuiaId guiaId = new GuiaId();
		guiaId.setAnoGuia(Short.parseShort(anoGuia));
		guiaId.setNumeroGuia(Long.parseLong(numeroGuia));
		guiaId.setCodigoOrgaoOrigem(getSetorUsuarioAutenticado().getId());
		guia.setId(guiaId);
		guia = getGuiaService().recuperarPorId(guiaId);
		return guia;
	}

	public void excluirProcesso(ActionEvent evt) {
		Processo processo = (Processo) tabelaProcessos.getRowData();
		processosSelecionados.remove(processo);
		montarMensagemRestricao();
	}

	public String getNomeMinistroRelator() {
		Processo processo = (Processo) tabelaProcessos.getRowData();
		Ministro ministroRelatorAtual = processo.getMinistroRelatorAtual();
		if (ministroRelatorAtual != null) {
			return ministroRelatorAtual.getNomeMinistroCapsulado(true, false, true);
		}
		return "Não distribuído";
	}
	
	public String getOrigemIdentificada() {
		Processo processo = (Processo) tabelaProcessos.getRowData();
		Origem origem = null;
		try {
			origem = recuperarOrigemBaixaAutomatica(processo);
		} catch (Exception e) {
			origem = null;
			reportarErro("Não foi possível identificar a origem!", e.getMessage());
			log.error("Nao foi possivel identificar a origem.", e);
		}
		return (origem!=null ? origem.getDescricao() : "");
	}
	
	public String getIdentificacaoObjetoIncidente() {
		Processo processo = (Processo) tabelaProcessos.getRowData();
		ObjetoIncidente<?> obj = mapaIncidentes.get(processo);
		if(obj != null){
			return obj.getIdentificacao();
		}
		return processo.getIdentificacao();
	}	

	public void limpar(ActionEvent evt) {
		limparDadosLancamentoParaVariosProcessos();
	}

	private void limparDadosLancamentoParaVariosProcessos() {
		setObservacao(null);
		setObservacaoInterna(null);
		limparDadosTipoInclusao();
		limparInformacoesAndamentoSelecionado();
		setLancarAndamentoEAssinarDocumento(false);
		mensagemDeRestricaoRegistroDeAndamento = null;
		comunicacao = null;
	}

	public void confirmarAssinarAndamento(){		
		setLancarAndamentoEAssinarDocumento(true);
		confirmarAndamento(null);
	}
	public void confirmarAndamento(){
		//Para verificar se o processo é eletronico, basta verificar o primeiro, pois a inclusão de 
		//processos na lista garante que todos os processos são do mesmo meio
		if (processosSelecionados.get(0).isEletronico()&& CODIGOS_BAIXA_REMESSA.contains(andamentoSelecionado.getId())) {
			confirmarAndamentoBaixa(null);
			
		} else
			confirmarAndamento(null);
	}
	
	public void confirmarAndamentoBaixa(ActionEvent event) {
		limparMensagens();
		
		setAtributo("requisicao", null);

		try {
			if (existeDadoAndamentoNaoInformado()) {
				reportarAviso("Por favor, informe os dados dos processos e do andamento a ser registrado.");
				return;
			}
			
			incidentes = new ArrayList<ObjetoIncidente<?>>();

			for (Processo processo : processosSelecionados) {
				ObjetoIncidente<?> inc = mapaIncidentes.get(processo);
				if(!incidentes.contains(inc)){
					incidentes.add(inc);
				}
			}				
			
			if(isAndamentoPermitido()){
				
				List<Long> origensGenericas = Arrays.asList(Origem.TRIBUNAL_REGIONAL_FEDERAL,Origem.TRIBUNAL_DE_JUSTICA_MILITAR_ESTADUAL,Origem.TRIBUNAL_DE_JUSTICA_ESTADUAL,Origem.TRIBUNAL_DE_JUSTICA_ESTADUAL,Origem.TURMA_RECURSAL_DE_JUIZADOS_ESPECIAIS_ESTADUAIS, Origem.TURMA_RECURSAL_DOS_JUIZADOS_ESPECIAIS_FEDERAIS);

				List<ContainerGuiaProcessos> containerDeGuias = new ArrayList<ContainerGuiaProcessos>();
				for (Processo processo : processosSelecionados) {
					
					//IDENTIFICAR A GUIA
					Origem origem = recuperarOrigemBaixaAutomatica(processo);
					if (origem == null)
						reportarInformacao(String.format(PROCESSO_SEM_ORIGEM_DEFINIDA, processo.getIdentificacao()));
					else if (origensGenericas.contains(origem.getId()))
						reportarInformacao(String.format(PROCESSO_COM_ORIGEM_GENERICA, processo.getIdentificacao()));
					if (hasErrors())
						return;
					
					ContainerGuiaProcessos findedGuiaAndamentoProcessoInfo = null;
					
					for (ContainerGuiaProcessos fgap : containerDeGuias) {
						//Se é o mesmo destino, a guia deve ser a mesma
						if (fgap.getGuia().getCodigoOrgaoDestino().equals(origem.getId())){
							findedGuiaAndamentoProcessoInfo = fgap;
							break;
						}
					}
						
					//Se a guia ainda não existe cria uma para posterior adição de processos
					if (findedGuiaAndamentoProcessoInfo == null){
						findedGuiaAndamentoProcessoInfo = new ContainerGuiaProcessos(getSetorUsuarioAutenticado().getId(), DeslocaProcesso.TIPO_ORGAO_INTERNO, origem, DeslocaProcesso.TIPO_ORGAO_EXTERNO);
						containerDeGuias.add(findedGuiaAndamentoProcessoInfo);
					} 
					
					ObjetoIncidente<?> incidenteSelecionado = mapaIncidentes.get(processo);
					normalizaPecasObjetoIncidente(incidenteSelecionado);

					//Monta os dados do andamento
					AndamentoProcessoInfoImpl andamentoProcessoInfo = montarAndamentoProcessoInfo();
					andamentoProcessoInfo.setOrigem(origem);
					
					findedGuiaAndamentoProcessoInfo.addProcessoEAndamentoProcesso(processo, andamentoProcessoInfo, null, incidenteSelecionado);
				}
				
				//Salva os andamentos e deslocamentos
				getAndamentoProcessoServiceLocal().salvarAndamentoBaixa(containerDeGuias, getSetorUsuarioAutenticado());
			}
			
			if (!hasErrors()) {
				reportarInformacao("Andamento registrado com sucesso!");
				limparInformacoesAndamentoSelecionado();
				limparInformacoesModais();
			}
		} catch (AndamentoNaoAutorizadoException e) {
			reportarErro("Andamento não autorizado para o setor!", e.getMessage());
			log.error("Andamento não autorizado para o setor!", e);
		} catch (ProcessoSemOrigemDefinidaException e) {
			reportarErro("Processo sem origem definida!", e.getMessage());
			log.error("Processo sem origem definida!", e);
		} catch (ProcessoComOrigemGenericaException e) {
			reportarErro("Processo com erro na origem!", e.getMessage());
			log.error("Processo com erro na origem!", e);
		} catch (ServiceException e) {
			reportarErro("Erro ao tornar publica a peça do processo!", e.getMessage());
			log.error("Erro ao tornar publica a peca do processo!", e);
		} catch (DaoException e) {
			reportarErro("Erro ao recuperar a peça do processo!", e.getMessage());
			log.error("Erro ao recuperar a peca do processo!", e);
		} catch (Exception e) {
			reportarErro("Ocorreu um erro ao gerar o Termo de Baixa/Remessa!", e.getMessage());
			log.error("Ocorreu um erro ao gerar o Termo de Baixa/Remessa!", e);
		} finally {
			atualizarSessao();
		}
	}

	public void confirmarAndamento(ActionEvent event) {
		limparMensagens();
		
		setAtributo("requisicao", null);
		feedbacks = new ArrayList<String>();

		try {
			if (existeDadoAndamentoNaoInformado()) {
				reportarAviso("Por favor, informe os dados dos processos e do andamento a ser registrado.");
				return;
			}
			
			incidentes = new ArrayList<ObjetoIncidente<?>>();

			for (Processo processo : processosSelecionados) {
				ObjetoIncidente<?> inc = mapaIncidentes.get(processo);
				if(!incidentes.contains(inc)){
					incidentes.add(inc);
				}
			}				
			
			if(isAndamentoPermitido()){
				
				TipoPecaProcesso tipoPecaProcesso = tipoPecaProcessoDoAndamentoSelecionado();
				Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());				
				ModeloComunicacao modelo = recuperarModeloComunicacaoDoAndamento();

				List<ComunicacaoDocumento> listaDocumentoPendentesAssinatura = new ArrayList<ComunicacaoDocumento>();
				
				boolean todosEletronicos = true;
				for (Processo processo : processosSelecionados) {
					if (!processo.isEletronico()) {
						todosEletronicos = false;
						break;
					}
				}
				
				List<Long> andamentosBaixa = Arrays.asList(new Long(BAIXA_EXTENA_AUTOS),new Long(BAIXA_DEFINITIVA));
				List<Long> origensGenericas = Arrays.asList(Origem.TRIBUNAL_REGIONAL_FEDERAL,Origem.TRIBUNAL_DE_JUSTICA_MILITAR_ESTADUAL,Origem.TRIBUNAL_DE_JUSTICA_ESTADUAL,Origem.TRIBUNAL_DE_JUSTICA_ESTADUAL,Origem.TURMA_RECURSAL_DE_JUIZADOS_ESPECIAIS_ESTADUAIS, Origem.TURMA_RECURSAL_DOS_JUIZADOS_ESPECIAIS_FEDERAIS);
				
				// [PROCESSAMENTO-3899]: baixa de processos em lote para os andamentos 7104 e 7101. Eu devia escrever este código na service, mas as regras estão todas aqui no bean. O problema é que isso compromete as transações. :-/
				if (andamentosBaixa.contains(andamentoSelecionado.getId()) && todosEletronicos) {
					for (Processo processo : processosSelecionados) {
						Origem origem = processo.getOrigemPrincipal();
						
						if (origem == null)
							reportarAviso(String.format("[%s]: Processo sem origem principal definida.", processo.getIdentificacao()));
						
						else if (origensGenericas.contains(origem.getId()))
							reportarAviso(String.format("[%s]: Processo com origem genérica cadastrada como principal.", processo.getIdentificacao()));
					}
				}

  					if(andamentoSelecionado.getId().equals(ANDAMENTO_VISTAPGR_INTIMACAO) 
  					 || andamentoSelecionado.getId().equals(ANDAMENTO_VISTAPGR) 
  					 || andamentoSelecionado.getId().equals(ANDAMENTO_VISTA_AGU)){
  						for (Processo processo : processosSelecionados) {
  							DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
    						if(processo.isEletronico()) {
    							if ( ultimoDeslocamento != null && ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(ORGAO_EXTERNO) 
    								&& (!ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_ORGAO_PGR) && (andamentoSelecionado.getId().equals(ANDAMENTO_VISTAPGR_INTIMACAO) || andamentoSelecionado.getId().equals(ANDAMENTO_VISTAPGR)) 
    								|| (!ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_ORGAO_AGU) && (andamentoSelecionado.getId().equals(ANDAMENTO_VISTA_AGU) )))) {

    								try {
    									List<Processo> processosApenso = recuperarApensos(processo);
    									deslocarProcesso(getSetorUsuarioAutenticado().getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
										deslocarProcessosApenso(processosApenso,usuario.getSetor());
									} catch (Exception e) {
										reportarErro("Algo deu errado ao tentar deslocar o(s) apenso(s) do(s) processo(s)!", e.getMessage());
										log.error("Algo deu errado ao tentar deslocar o(s) apenso(s) do(s) processo(s)!", e);
									}
  								}
    						}
  						}
  					}
				
  					
  					
  					if( andamentoSelecionado.getId().equals(ANDAMENTO_CANCELAMENTO_PROTOCOLO) ) {
  		  						for (Processo processo : processosSelecionados) {
  		  						 if(processo.getTipoMeio().equals("E")) {
  		  							DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
  	    						if ( ultimoDeslocamento != null && !ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO.longValue())) {
  	    							//deslocarProcesso(getSetorUsuarioAutenticado().getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
  	    							deslocarProcesso(CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO, processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
  	    							}
  	    						Processo processoAndamento = (Processo) processo;
  								
  											processoAndamento.setSituacao(SituacaoProcesso.PROCESSO_FINDO);
  											getProcessoService().flushSession();
  		  						}
  		  						}
  		  					}
  					
  					
				if (hasErrors())
					return;
				
				for (Processo processo : processosSelecionados) {
					ObjetoIncidente<?> incidenteSelecionado = mapaIncidentes.get(processo);
					if(incidenteSelecionado != null){
						
						try {
							normalizaPecasObjetoIncidente(incidenteSelecionado);
						} catch (ServiceException e) {
							reportarErro("Erro ao tornar publica a peça do processo: " + processo.getIdentificacao(), e.getMessage());
							log.error("Erro ao tornar publica a peca do processo: " + processo.getIdentificacao(), e);
						} catch (DaoException e) {
							reportarErro("Erro ao recuperar a peça do processo: " + processo.getIdentificacao(), e.getMessage());
							log.error("Erro ao recuperar a peca do processo: " + processo.getIdentificacao(), e);
						}
						
						AndamentoProcessoInfoImpl andamentoProcessoInfo = montarAndamentoProcessoInfo();
						andamentoProcessoInfo.setOrigem(processo.getOrigemPrincipal());
						
						if (processo.getOrigemPrincipal() != null)
							andamentoProcessoInfo.setOrigem(processo.getOrigemPrincipal());
						
						if (andamentosBaixa.contains(andamentoSelecionado.getId()) && todosEletronicos) {
							try {
								gerarCertidaoBaixa(processo, andamentoProcessoInfo);
							} catch (Exception e) {
								log.error("Ocorreu um erro ao montar o Termo de Baixa/Remessa do processo: " + processo.getIdentificacao(), e);
								reportarErro("Ocorreu um erro ao montar o Termo de Baixa/Remessa do processo:  " + processo.getIdentificacao(), e.getMessage());
							}
						} else {
							AndamentoProcesso andamento = getAndamentoProcessoService().salvarAndamento(andamentoProcessoInfo, processo, incidenteSelecionado);
							
							if (andamento != null) {
								try {
									InfoPecaVinculadoAndamentoDTO info = new InfoPecaVinculadoAndamentoDTO.Builder().setObjetoIncidente(isPecaDeMerito() ? processo : incidenteSelecionado)
											.setUsuario(usuario)
											.setSetorUsuario(getSetorUsuarioAutenticado())
											.setAndamentoProcesso(andamento)
											.setTipoPecaProcesso(tipoPecaProcesso)
											.setModeloComunicacao(modelo)
											.setIdAndamentoSelecionado(idAndamentoSelecionado())
											.setListaPartes(getListaInforPartes(isPecaDeMerito() ? processo : incidenteSelecionado))
											.setDescricaoPeca(tipoPecaProcesso != null ? tipoPecaProcesso.getDescricao() : null)
											.builder();
									ComunicacaoDocumentoResult comDoc = getProcessamentoRelatorioService().gerarPecaVinculadaAoAndamentoSelecionado(info);	
									
									comunicacao = null;
									
									if(andamento.getCodigoAndamento().equals(ANDAMENTO_VISTAPGR))
										try {
											comunicacao = gerarComunicacaoVistaPGR(andamento, false);
										} catch (IOException e) {
											log.error("Ocorreu um erro ao gerar a comunicação de vista para a PGR do processo: " + processo.getIdentificacao(), e);
											reportarErro("Ocorreu um erro ao gerar a comunicação de vista para a PGR do processo:  " + processo.getIdentificacao(), e.getMessage());
										}
									
									if(andamento.getCodigoAndamento().equals(ANDAMENTO_AUTOS_DISPONIBILIZADOS))
										try {
											comunicacao = gerarComunicacaoAutosDisponibilizados(andamento, false);
										} catch (IOException e) {
											log.error("Ocorreu um erro ao gerar a comunicação de autos disponibilizados do processo: " + processo.getIdentificacao(), e);
											reportarErro("Ocorreu um erro ao gerar a comunicação de autos disponibilizados do processo:  " + processo.getIdentificacao(), e.getMessage());
										}
									
									if(andamento.getCodigoAndamento().equals(ANDAMENTO_VISTA_AGU))
										try {
											comunicacao = gerarComunicacaoVistaAGU(andamento, false);
										} catch (IOException e) {
											log.error("Ocorreu um erro ao gerar a comunicação de vista para a AGU do processo: " + processo.getIdentificacao(), e);
											reportarErro("Ocorreu um erro ao gerar a comunicação de vista para a AGU do processo:  " + processo.getIdentificacao(), e.getMessage());
										}
									
									  if (comunicacao != null) {
								          associarAndamentoProcessoComunicacao(andamento, comunicacao);
								          criarComunicacaoObjetoIncidente(processo.getId(), comunicacao, andamento, FlagProcessoLote.P);
									    }
										
									  if(comDoc != null)
										listaDocumentoPendentesAssinatura.add(new ComunicacaoDocumento(comDoc));
									
									if (processo.isDeslocarObjetoIncidente())
										deslocarProcesso(incidenteSelecionado);
									
									if(isLancarAndamentoEAssinarDocumento() && getAndamentoGeraDocumento())					
										assinarDocumento(listaDocumentoPendentesAssinatura);
									
									if(andamento.getCodigoAndamento().equals(ANDAMENTO_VISTA_AO_MINISTRO))
										deslocarProcessoVistaMinistro(processo,andamento);
									
								} catch (HandlerException e) {
									getAndamentoProcessoService().excluir(andamento);
									log.error("Algo deu errado ao gerar a peca vinculada ao andamento! Andamento gerado, mas excluído do processo: " + processo.getIdentificacao(), e);
									reportarErro("Algo deu errado ao gerar a peca vinculada ao andamento! Andamento gerado, mas excluído: " + processo.getIdentificacao(), e.getMessage());
								} catch (DaoException e) {
									getAndamentoProcessoService().excluir(andamento);
									log.error("Algo deu errado ao gerar a comunicacao de vista a AGU! Andamento gerado, mas excluído: " + processo.getIdentificacao(), e);
									reportarErro("Algo deu errado ao gerar a comunicacao de vista a AGU! Andamento gerado, mas excluído: " + processo.getIdentificacao(), e.getMessage());
								} catch (InterruptedException e) {
									getAndamentoProcessoService().excluir(andamento);
									log.error("Algo deu errado ao gerar deslocamento de processo com vista a ministro! Andamento gerado, mas excluído: " + processo.getIdentificacao(), e);
									reportarErro("Algo deu errado ao gerar deslocamento de processo com vista a ministro! Andamento gerado, mas excluído: " + processo.getIdentificacao(), e.getMessage());
								}
							}
						}
					}
				}
				if (!hasErrors()) {
					reportarAviso("Andamento registrado com sucesso!");
				} 
			} else {
				reportarFeedbacks();
				reportarAviso("Algo deu errado ao gerar o andamento!");
			}
		} catch (ServiceException e1) {
			reportarErro("Algo deu errado ao registrar o andamento para o processo: " + processo.getIdentificacao(), e1.getMessage());
			log.error("Algo deu errado ao registrar o andamento para o processo: " + processo.getIdentificacao(), e1);

		} finally {
			limparInformacoesAndamentoSelecionado();
			limparInformacoesModais();		
			atualizarSessao();
		}
	}

	private boolean hasErrors() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if (facesContext.getMessages() != null && facesContext.getMessages().hasNext())
			return true;

		return false;
	}

	public String chamarAssinador(){
		reportarFeedbacks();		
		
		RequisicaoAssinaturaDocumentoComunicacao requisicao = (RequisicaoAssinaturaDocumentoComunicacao) getAtributo("requisicao");
		if(requisicao == null){
			return null;
		}
		setRequestValue(RequisicaoAssinaturaDocumentoComunicacao.REQUISICAO_ASSINADOR, requisicao);
		setAtributo("requisicao", null);
		setLancarAndamentoEAssinarDocumento(false);		
		return "assinarServlet";
	}

	private AndamentoProcessoInfoImpl montarAndamentoProcessoInfo() {
		
		if(LISTA_ANDAMENTOS_REQUER_TEMA.contains(andamentoSelecionado.getId())){
			observacao = "";
			if(getListaProcessoTema() != null && !getListaProcessoTema().isEmpty()){
				for(ProcessoTema pt : getListaProcessoTema()){
					observacao += "Tema nº " + pt.getTema().getNumeroSequenciaTema() + " - " + pt.getIdentificacaoSimples() + ", ";
				}
				observacao = observacao.substring(0, observacao.length()-2);
			}
		}
		
		AndamentoProcessoInfoImpl andamentoProcessoInfo = new AndamentoProcessoInfoImpl();
		andamentoProcessoInfo.setAndamento(getAndamentoSelecionado());
		andamentoProcessoInfo.setCodigoUsuario(getUser().getUsername());
		andamentoProcessoInfo.setIdOrigemDecisao(getIdOrigemDecisao());
		andamentoProcessoInfo.setIdPresidenteInterino(getIdPresidenteInterino());
		andamentoProcessoInfo.setIdTipoDevolucao(getIdTipoDevolucao());
		andamentoProcessoInfo.setObservacao(getObservacao());
		andamentoProcessoInfo.setObservacaoInterna(getObservacaoInterna());
		andamentoProcessoInfo.setSetor(getSetorUsuarioAutenticado());
		andamentoProcessoInfo.setPeticao(null);

		if (processoSelecionado != null) {
			if (processosSelecionados == null) processosSelecionados = new ArrayList<Processo>();
			processosSelecionados.add((Processo) processoSelecionado);
		}
		andamentoProcessoInfo.setProcessosPrincipais(processosSelecionados);
		andamentoProcessoInfo.setProcessosTemas(getListaProcessoTema());
		andamentoProcessoInfo.setUltimoAndamento(null);
		return andamentoProcessoInfo;
	}

	private boolean existeDadoAndamentoNaoInformado() {
		return andamentoSelecionado == null || processosSelecionados == null || processosSelecionados.size() == 0;
	}

	public Integer getQuantidadeVinculados() {
		Processo processo = (Processo) tabelaProcessos.getRowData();
		try {
			return getProcessoDependenciaService().getQuantidadeVinculados(processo);
		} catch (ServiceException e) {
			log.error("Algo deu errado na pesquisa dos processos vinculados!", e);
			reportarErro("Algo deu errado na pesquisa dos processos vinculados!", e.getMessage());
			return 0;
		}
	}

	@Override
	public java.util.List<Processo> getProcessosSelecionados() {
		if(processosSelecionados == null){
			processosSelecionados = new ArrayList<Processo>();
		}
		return processosSelecionados;
	};
	
	
	public String getIdentificacaoProcessos() {
		return identificacaoProcessos;
	}	
	
	public void setIdentificacaoProcessos(String identificacaoProcessos) {
		limparMensagens();
		
		if(StringUtils.isVazia(identificacaoProcessos))
			return;
		
		identificacaoProcessos = identificacaoProcessos.replaceAll("\n", ";").replaceAll("[^A-Za-z0-9;]", " ").replaceAll("  ", " ");
				
		String[] idtProcessos = identificacaoProcessos.split(";");
		for(int i = 0; i < idtProcessos.length; i++){
			setIdentificacaoProcesso(idtProcessos[i].trim());
		}
		
	}
	
	private void limparMensagens() {
		if (getMensagem() != null)
			getMensagem().clear();
	}

	public void importarListaExportadaPeloEGab(ActionEvent evt){		
		try {
			listaProcessoImportacao = getObjetoIncidenteService().pesquisarListaImportacaoUsuario(getUser().getUsername().toUpperCase());
		} catch (ServiceException e) {
			log.error("Algo deu errado na importação dos processos do Egab!", e);
			reportarErro("Algo deu errado na importação dos processos do Egab!", e.getMessage());
		}
	}	
	
	public Integer getQuantidadeListaExportadaPeloEGab(){
		if(listaProcessoImportacao != null){
			return listaProcessoImportacao.size();
		}
		return 0;
	}
	
	public void processarImportacaoListaExportadaPeloEGab(ActionEvent evt){		
		try {			
			listaProcessoImportacao = getObjetoIncidenteService().pesquisarListaImportacaoUsuario(getUser().getUsername().toUpperCase());
			if(listaProcessoImportacao != null && !listaProcessoImportacao.isEmpty()){
				for(ObjetoIncidente<?> oi : listaProcessoImportacao){
					Processo processo = null;
					
					if(oi instanceof Processo){
						processo = getProcessoService().recuperarPorId(oi.getId());
						recuperarObjetoIncidente(processo, processo.getIdentificacao());
					}
					else if(oi instanceof RecursoProcesso){
						RecursoProcesso rp = getRecursoProcessoService().recuperarPorId(oi.getId());
						List<Processo> processos = getProcessoService().pesquisarProcesso(rp.getIdentificacao());
						processo = processos.get(0);
						recuperarObjetoIncidente(processo, rp.getIdentificacao());
					}
					else if (oi.getPrincipal() != null) {
						processo = getProcessoService().recuperarPorId(oi.getPrincipal().getId());
						recuperarObjetoIncidente(processo, oi.getIdentificacao());
					}
					
					if(processo != null){
						setProcesso(processo);
					}
				}
			}else{
				reportarAviso("Nenhum processo localizado para ser importado!");
			}
		} catch (ServiceException e) {
			log.error("Algo deu errado ao importar a lista do egab!", e);
			reportarErro("Algo deu errado ao importar a lista do egab!", e.getMessage());

		}
	}
	
	public ObjetoIncidente<?> getIncidenteSelecionado() {
		return incidenteSelecionado;
	}

	public void setIncidenteSelecionado(ObjetoIncidente<?> incidenteSelecionado) {
		setIdentificacaoProcesso(incidenteSelecionado.getIdentificacao());
	}	
	
	public Processo getObjetoIncidentePrincipal (){ 
		return ((Processo) incidenteSelecionado.getPrincipal());
	}
	
	@Override
	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		if (StringUtils.isVazia(identificacaoProcesso)) {
			return;
		}
		
		String codigo = identificacaoProcesso;
		if (identificacaoProcesso.length() == 14){
			String siglaNumeroTrezeCarac = codigo.substring(0, codigo.length()-1); 
			codigo = siglaNumeroTrezeCarac;
		}else {
			codigo = identificacaoProcesso;
		}
		
		try {
			String classProc = ProcessoParser.getSigla(codigo);
			Long numProc = ProcessoParser.getNumero(codigo);
			
			if (StringUtils.isVazia(classProc) || numProc == null) {
				reportarAviso(String.format("[%s]: Processo inválido!", identificacaoProcesso));
				return;
			}
			
			Processo processo = getProcessoService().recuperarProcesso(classProc, numProc);
			
			if (processo == null) {
				reportarAviso(String.format("[%s]: Processo inexistente!", identificacaoProcesso));
				return;
			}
			recuperarObjetoIncidente(processo, identificacaoProcesso);
			setProcesso(processo);
		} catch (ServiceException e) {
			log.error("Nao foi possivel recuperar a identificacao do processo!", e);
			reportarAviso(String.format("[%s]: Processo inválido!", identificacaoProcesso));
		} 
		atualizarSessao();
	}	

	public boolean isLancarAndamentoEAssinarDocumento() {
		return lancarAndamentoEAssinarDocumento;
	}

	public void setLancarAndamentoEAssinarDocumento(boolean lancarAndamentoEAssinarDocumento) {
		this.lancarAndamentoEAssinarDocumento = lancarAndamentoEAssinarDocumento;
	}	
	
	@Override
	public void setAndamentoSelecionado(Andamento andamentoSelecionado) {
		montarMensagemRestricao();
		if(andamentoSelecionado != null && ANDAMENTOS_NAO_PERMITIDOS.contains(andamentoSelecionado.getId())){
			reportarAviso("O andamento " +andamentoSelecionado.getId()+" não é permitido no registro de andamento para vários processos.");
		}else{
			super.setAndamentoSelecionado(andamentoSelecionado);
		}
	}
	
	public void cancelarRegistroAndamento(ActionEvent event) {
		limparInformacoesAndamentoSelecionado();
		limparInformacoesModais();
	}

	public String getMensagemDeRestricaoRegistroDeAndamento() {
		return mensagemDeRestricaoRegistroDeAndamento;
	}

	public void setMensagemDeRestricaoRegistroDeAndamento(
			String mensagemDeRestricaoRegistroDeAndamento) {
		this.mensagemDeRestricaoRegistroDeAndamento = mensagemDeRestricaoRegistroDeAndamento;
	}
	
	public boolean isExistemAcordaosPendentesDePublicacao() {
		
		ControleVotoDto cvDto = new ControleVotoDto.Builder().setCvNaoLiberado(Boolean.FALSE)
							.setCvNaoRecebido(Boolean.FALSE).setCvAcordaoNaoPublicado(Boolean.FALSE)
							.setCvSemAcaoUmMinistro(Boolean.FALSE).setCvPorTodosOsMinistros(Boolean.FALSE)
							.setJulgamentoFinalizado(Boolean.FALSE).setRepercussaoGeral(Boolean.FALSE)
							.setCvDocAtivos(Boolean.FALSE).setCvCompleto(Boolean.FALSE).setIdObjetoIncidente(processo.getId())
							.builder();
			try {
				List<ResultadoControleVotoPDF> cvs = getControleVotoService().pesquisar(cvDto);
				
				for (ResultadoControleVotoPDF resultadoControleVotoPDF : cvs) {
					if (resultadoControleVotoPDF.getControleVoto().getObjetoIncidente().getDataPublicacao() == null){
						existemAcordaosPendentesDePublicacao = true;
						break;
					}
				}
			} catch (ServiceException e1) {
				log.error("Ocorreu um erro ao verificar acórdãos pendendes de publicação", e1);
				reportarErro("Ocorreu um erro ao verificar acórdãos pendendes de publicação!", e1.getMessage());
			} catch (ValidationException e1) {
				log.error("Erro ao verificar o controle de votos do acordao pendende de publicacao!", e1);
				reportarErro("Erro ao verificar o controle de votos do acórdão pendende de publicação!", e1.getMessage());
			}
		
		return existemAcordaosPendentesDePublicacao;
	}
	
	private void montarMensagemRestricao(){
		mensagemDeRestricaoRegistroDeAndamento = null;
		if (processosSelecionados != null){
			for (Processo processo: processosSelecionados){
				if (isProcessoInativo(processo)){
					String fechoFrase = " encontra(m)-se na situação \"Inativo\". Deseja continuar?"; 
					if (mensagemDeRestricaoRegistroDeAndamento == null){
						mensagemDeRestricaoRegistroDeAndamento = new String("O(s) processo(s) " + 
					    processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual().toString() + fechoFrase);
					} else {
						mensagemDeRestricaoRegistroDeAndamento = mensagemDeRestricaoRegistroDeAndamento.substring(0, mensagemDeRestricaoRegistroDeAndamento.indexOf(fechoFrase));
						mensagemDeRestricaoRegistroDeAndamento += ", " + processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual().toString() + fechoFrase;
					}
				}
			}
			setAtributo(MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO, mensagemDeRestricaoRegistroDeAndamento);			
		} else {
			mensagemDeRestricaoRegistroDeAndamento = null;
		}
	}

	public void setExistemAcordaosPendentesDePublicacao(
			boolean existemAcordaosPendentesDePublicacao) {
		this.existemAcordaosPendentesDePublicacao = existemAcordaosPendentesDePublicacao;
	}
	

	public boolean isProcessoInativo(Processo processo) {
		DeslocaProcesso ultimoDeslocamento = null;
		try {
			ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
			if ((ultimoDeslocamento!=null) && (ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO)))
				processoInativo = true;
			else
				processoInativo = false;
		} catch (ServiceException e) {
			log.error("Ocorreu um erro ao avaliar se o setor atual está inativo!", e);
			reportarErro("Ocorreu um erro ao avaliar se o setor atual está inativo!!", e.getMessage());
		}
		return processoInativo;
	}

	@Override
	public void posRegistrarAndamento(AndamentoProcesso andamentoProcesso) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void posRegistrarAndamento(AndamentoProcesso andamentoProcesso, String mensagem) throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	private Comunicacao gerarComunicacaoVistaPGR(AndamentoProcesso andamentoProcesso, boolean intimacao) throws ServiceException, IOException {
		
		String descricaoComunicacao = "COMUNICAÇÃO DE VISTA";
		Long numeroComunicacao = 142L;
		ModeloComunicacaoEnum modeloComunicacao = ModeloComunicacaoEnum.VISTA_A_PGR;
		Collection<Long> objetoIncedentes = Arrays.asList(andamentoProcesso.getObjetoIncidente().getId());
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		Processo processoAndamento = (Processo) andamentoProcesso.getObjetoIncidente().getPrincipal();
		ObjetoIncidente<?> objetoIncidente = andamentoProcesso.getObjetoIncidente();
		
		List<PecaProcessoEletronico> listaPecaRecuperada = null;
		DocumentoEletronico documentoEletronicoComunicacao = null;
		
		if (intimacao) {
			descricaoComunicacao = "COMUNICAÇÃO DE VISTA PARA FINS DE INTIMAÇÃO";
			numeroComunicacao = 121L;
			modeloComunicacao = ModeloComunicacaoEnum.INTIMACAO_DESPACHO_DECISAO_ACORDAO;
			
			PecaDTO pecaDTO = null;
			
			if (pecaSelecionada != null) {
				pecaDTO = listaPecasDTO.get(pecaSelecionada);
				
				for(PecaProcessoEletronico pecaProcessoEletronico: listaOriginal){
					if(pecaProcessoEletronico.getId() == pecaDTO.getId()){
						listaPecaRecuperada = Arrays.asList(pecaProcessoEletronico);
					}
				}
			}
			
			TextoAndamentoProcesso textoAndamentoProcesso =  new TextoAndamentoProcesso();
			textoAndamentoProcesso.setAndamentoProcesso(andamentoProcesso);
			textoAndamentoProcesso.setDataInclusao(new Date());
			
			if (pecaDTO != null)
				textoAndamentoProcesso.setSeqDocumento(pecaDTO.getDocumentos().get(0).getDocumentoEletronico().getId());
			
			getTextoAndamentoProcessoService().persistirTextoAndamentoProcesso(textoAndamentoProcesso);
			
		} else {
			String nomeProcesso = processoAndamento.getSiglaClasseProcessual() + " " + processoAndamento.getNumeroProcessual(); 
			String nomeArq = "TERMO_DE_VISTA_" + nomeProcesso.replaceAll(" ", "") + "_";
			byte[] arquivoEletronico = getProcessamentoRelatorioService().gerarPdfComunicacaoDeVista(andamentoProcesso.getObjetoIncidente(),usuario.getSetor(), ProcessamentoRelatorioServiceLocal.DESTINATARIO_PGR);
			nomePDFBaixa = getProcessamentoRelatorioService().gerarPDF(nomeArq, arquivoEletronico);
			String siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_VISTA_PGR;
			ArquivoProcessoEletronico arquivoProcessoEletronico = getArquivoProcessoEletronicoService().salvarPecaEletronica(arquivoEletronico, siglaTipoPeca, objetoIncidente, usuario.getSetor(), andamentoProcesso);
			getArquivoProcessoEletronicoService().flushSession();
			listaPecaRecuperada = Arrays.asList(arquivoProcessoEletronico.getPecaProcessoEletronico());
			documentoEletronicoComunicacao = arquivoProcessoEletronico.getDocumentoEletronico();
			getAndamentoProcessoService().flushSession();
		}
		
		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(numeroComunicacao);
		List<Origem> origem = getOrigemService().recuperarApenasPgr();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();

		if (processoAndamento.getTipoMeio().equals("E"))
		   return getComunicacaoServiceLocal().criarComunicacaoIntimacao(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modeloComunicacao, objetoIncedentes, TipoFaseComunicacao.ENVIADO, listaPecaRecuperada, null, numeroDocumento, descricaoComunicacao, null, documentoEletronicoComunicacao); 
		
		return null;
	}

	private Comunicacao gerarComunicacaoVistaAGU(AndamentoProcesso andamentoProcesso, boolean intimacao) throws ServiceException, IOException, DaoException {
		
		String descricaoComunicacao = "COMUNICAÇÃO DE VISTA";
		Long numeroComunicacao = 142L;
		ModeloComunicacaoEnum modeloComunicacao = ModeloComunicacaoEnum.VISTA_A_AGU;
		Collection<Long> objetoIncedentes = Arrays.asList(andamentoProcesso.getObjetoIncidente().getId());
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteService().recuperarPorId(andamentoProcesso.getObjetoIncidente().getId());
		
		List<PecaProcessoEletronico> listaPecaRecuperada = null;
		DocumentoEletronico documentoEletronicoComunicacao = null;
		
		String nomeProcesso = andamentoProcesso.getSigClasseProces() + " " + andamentoProcesso.getNumProcesso(); 
		String nomeArq = "TERMO_DE_VISTA_" + nomeProcesso.replaceAll(" ", "") + "_";
		byte[] arquivoEletronico = getProcessamentoRelatorioService().gerarPdfComunicacaoDeVista(andamentoProcesso.getObjetoIncidente(),usuario.getSetor(), ProcessamentoRelatorioServiceLocal.DESTINATARIO_AGU);
		nomePDFBaixa = getProcessamentoRelatorioService().gerarPDF(nomeArq, arquivoEletronico);
		String siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_VISTA_AGU;
		ArquivoProcessoEletronico arquivoProcessoEletronico = getArquivoProcessoEletronicoService().salvarPecaEletronica(arquivoEletronico, siglaTipoPeca, objetoIncidente, usuario.getSetor(), andamentoProcesso);
		getArquivoProcessoEletronicoService().flushSession();
		listaPecaRecuperada = Arrays.asList(arquivoProcessoEletronico.getPecaProcessoEletronico());
		documentoEletronicoComunicacao = arquivoProcessoEletronico.getDocumentoEletronico();
		getAndamentoProcessoService().flushSession();
		
		
		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(numeroComunicacao);
		List<Origem> origem = getOrigemService().recuperarApenasAgu();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();

		if (objetoIncidente.getTipoMeio().equals("E"))
			return getComunicacaoServiceLocal().criarComunicacaoIntimacao(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modeloComunicacao, objetoIncedentes, TipoFaseComunicacao.ENVIADO, listaPecaRecuperada, null, numeroDocumento, descricaoComunicacao, null, documentoEletronicoComunicacao); 
		
		return null;
	}
	
	private Comunicacao gerarComunicacaoAutosDisponibilizados(AndamentoProcesso andamentoProcesso, boolean intimacao) throws ServiceException, IOException {
		
		String descricaoComunicacao = "AUTOS DISPONIBILIZADOS à AUTORIDADE POLICIAL";
		Long numeroComunicacao = 123L;
		ModeloComunicacaoEnum modeloComunicacao = ModeloComunicacaoEnum.NOTIFICACAO_AUTOS_DISPONIBILIZADOS;
		Collection<Long> objetoIncedentes = Arrays.asList(andamentoProcesso.getObjetoIncidente().getId());
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		Processo processoAndamento = (Processo) andamentoProcesso.getObjetoIncidente().getPrincipal();
		ObjetoIncidente<?> objetoIncidente = andamentoProcesso.getObjetoIncidente();
		
		List<PecaProcessoEletronico> listaPecaRecuperada = null;
		DocumentoEletronico documentoEletronicoComunicacao = null;
		
		if (intimacao) {
			descricaoComunicacao = "AUTOS DISPONIBILIZADOS à AUTORIDADE POLICIAL";
			numeroComunicacao = 123L;
			modeloComunicacao = ModeloComunicacaoEnum.INTIMACAO_DESPACHO_DECISAO_ACORDAO;
			
			PecaDTO pecaDTO = null;
			
			if (pecaSelecionada != null) {
				pecaDTO = listaPecasDTO.get(pecaSelecionada);
				
				for(PecaProcessoEletronico pecaProcessoEletronico: listaOriginal){
					if(pecaProcessoEletronico.getId() == pecaDTO.getId()){
						listaPecaRecuperada = Arrays.asList(pecaProcessoEletronico);
					}
				}
			}
			
			TextoAndamentoProcesso textoAndamentoProcesso =  new TextoAndamentoProcesso();
			textoAndamentoProcesso.setAndamentoProcesso(andamentoProcesso);
			textoAndamentoProcesso.setDataInclusao(new Date());
			
			if (pecaDTO != null)
				textoAndamentoProcesso.setSeqDocumento(pecaDTO.getDocumentos().get(0).getDocumentoEletronico().getId());
			
			getTextoAndamentoProcessoService().persistirTextoAndamentoProcesso(textoAndamentoProcesso);
			
		} else {
			String nomeProcesso = processoAndamento.getSiglaClasseProcessual() + " " + processoAndamento.getNumeroProcessual(); 
			String nomeArq = "AUTOS_DISPONIBILIZADOS_" + nomeProcesso.replaceAll(" ", "") + "_";
			byte[] arquivoEletronico = getProcessamentoRelatorioService().gerarPdfComunicacaoAutosDisp(processoAndamento,usuario.getSetor(), ProcessamentoRelatorioServiceLocal.DESTINATARIO_DPF);
			nomePDFBaixa = getProcessamentoRelatorioService().gerarPDF(nomeArq, arquivoEletronico);
			String siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_TERMO_AUTOS_DISP;
			ArquivoProcessoEletronico arquivoProcessoEletronico = getArquivoProcessoEletronicoService().salvarPecaEletronica(arquivoEletronico, siglaTipoPeca, objetoIncidente, usuario.getSetor(), andamentoProcesso);
			getArquivoProcessoEletronicoService().flushSession();
			listaPecaRecuperada = Arrays.asList(arquivoProcessoEletronico.getPecaProcessoEletronico());
			documentoEletronicoComunicacao = arquivoProcessoEletronico.getDocumentoEletronico();
			getAndamentoProcessoService().flushSession();
		}
		
		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(numeroComunicacao);
		List<Origem> origem = getOrigemService().recuperarApenasDpf();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();

		if (processoAndamento.getTipoMeio().equals("E"))
		   return getComunicacaoServiceLocal().criarComunicacaoAutosDisponibilizados(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modeloComunicacao, objetoIncedentes, TipoFaseComunicacao.ENVIADO, listaPecaRecuperada, null, numeroDocumento, descricaoComunicacao, null, documentoEletronicoComunicacao); 
		
		return null;
	}
	
	private void associarAndamentoProcessoComunicacao(AndamentoProcesso andamentoProcesso, Comunicacao comunicacao) throws ServiceException {
		AndamentoProcessoComunicacao andamentoProcessoComunicacao = new AndamentoProcessoComunicacao();
		andamentoProcessoComunicacao.setAndamentoProcesso(andamentoProcesso);
		andamentoProcessoComunicacao.setComunicacao(comunicacao);
		andamentoProcessoComunicacao.setTipoVinculoAndamento(TipoVinculoAndamento.RELACIONADO);
		getAndamentoProcessoComunicacaoService().salvar(andamentoProcessoComunicacao);
	}	
	  @SuppressWarnings({ "rawtypes", "deprecation" })
	private ComunicacaoIncidente criarComunicacaoObjetoIncidente(Long idObjetoIncidente, Comunicacao comunicacao, AndamentoProcesso andamentoProcesso, FlagProcessoLote tipoVinculo) throws ServiceException {
	      ObjetoIncidente objetoIncidente = getObjetoIncidenteService().recuperarPorId(idObjetoIncidente);
	      ComunicacaoIncidente comunicacaoIncidente = new ComunicacaoIncidente();
	      comunicacaoIncidente.setObjetoIncidente(objetoIncidente);
	      comunicacaoIncidente.setTipoVinculo(tipoVinculo);
	      comunicacaoIncidente.setComunicacao(comunicacao);
	      comunicacaoIncidente.setAndamentoProcesso(andamentoProcesso);
	      return  getComunicacaoIncidenteService().salvar(comunicacaoIncidente);
	   }
	  
		public List<Processo> recuperarApensos(Processo processo) throws Exception {
			List<ProcessoDependencia> apensos = getProcessoDependenciaService().recuperarApensos(processo);
		
			for (ProcessoDependencia apenso: apensos ) {
				Processo processoApenso = getProcessoService().recuperarProcesso(apenso.getClasseProcesso(), apenso.getNumeroProcesso());
				processosApenso.add(processoApenso);
				recuperarApensos(processoApenso);
			}
			return processosApenso;
			
		}
		
		public void deslocarProcessosApenso(List<Processo> processosApenso , Setor setor) throws Exception {
			
			for (Processo processoApenso: processosApenso ) {
				DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processoApenso);
				if(ultimoDeslocamento.getCodigoOrgaoDestino() != setor.getId()){
					deslocarProcesso(setor.getId(), processoApenso, ORGAO_EXTERNO, ORGAO_INTERNO, andamentoSelecionado.getId());
				}
			}	
		}
	  
}
