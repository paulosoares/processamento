package br.gov.stf.estf.expedicao.visao;

import static br.gov.stf.estf.expedicao.visao.Util.mandarRespostaDeDownloadDoArquivoExcel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.estf.corp.entidade.UnidadeFederacao;
import br.gov.stf.estf.corp.model.service.MunicipioService;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.expedicao.entidade.ContratoPostagem;
import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.entidade.RemessaListaRemessa;
import br.gov.stf.estf.expedicao.entidade.RemessaTipoServico;
import br.gov.stf.estf.expedicao.entidade.RemessaVolume;
import br.gov.stf.estf.expedicao.entidade.TipoComunicacaoExpedicao;
import br.gov.stf.estf.expedicao.entidade.TipoEmbalagem;
import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.entidade.TipoServicoCompativel;
import br.gov.stf.estf.expedicao.entidade.UnidadePostagem;
import br.gov.stf.estf.expedicao.entidade.VwEndereco;
import br.gov.stf.estf.expedicao.model.service.ListaRemessaService;
import br.gov.stf.estf.expedicao.model.service.RemessaService;
import br.gov.stf.estf.expedicao.model.service.impl.ExpedicaoRelatorioServiceLocal;
import br.gov.stf.estf.expedicao.model.util.FinalizarRemessaDTO;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.estf.expedicao.model.util.RelatorioRemessaDTO;
import br.gov.stf.estf.expedicao.model.util.TipoEmbalagemEnum;
import br.gov.stf.estf.expedicao.model.util.TipoEntregaEnum;
import br.gov.stf.estf.expedicao.model.util.TipoServicoCodigoEnum;
import br.gov.stf.estf.expedicao.visao.comparator.ListaRemessaNumeroLista;
import br.gov.stf.estf.expedicao.visao.vo.NumeroAnoVo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.InCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.SearchCriterion;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Bean que mantém informações a respeito da lista de remessa da expedição.
 *
 * @author roberio.fernandes
 */
public class BeanListaRemessa extends AssinadorBaseBean implements SelecionaBeanDestinatario, SelecionaCep, PesquisaListaRemessa {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(BeanListaRemessa.class);
	public static final String BARRA = "/";
	private static final String SEPARADOR_LISTAS_REMESSA = ";";
	public static final String PAIS = "Brasil";
	private static final int UM_MEGABYTE = 512418 * 2;

	private final ListaRemessaNumeroLista listaRemessaNumeroLista = new ListaRemessaNumeroLista();
	
	private BeanVisualizarListaRemessa beanVisualizarListaRemessa;
	private PesquisaListaRemessaDto pesquisaListaRemessaDto = new PesquisaListaRemessaDto();
	private List<Remessa> remessas = new ArrayList<Remessa>();
	private Integer qtdRegistros;
	private String codigoUnidadePostagem;
	private String codigoTipoServico;
	private String codigoTipoEmbalagem;
	private String codigoTipoDocumento;
	private String codigoTipoServicoAdiconalAnterior[];
	private String codigoTipoServicoAdiconal[];
	private String codigoTipoServicoAdiconalObrigatorio[];
	private TipoEntregaEnum tipoEntrega;
	private String idDestinatario;
	private Remessa remessa;
	private TipoEmbalagemEnum tipoEmbalagem;
	private Integer qtdVolumes;
	private List<RemessaVolume> listaVolumes;
	private String listasRemessa;
	private ListaRemessa listaRemessas;
	private TipoServico tipoServico;
	private UnidadePostagem unidadePostagem;
	private List<ListaRemessa> conjuntoRemessas;
	private String arquivoUpload;
	private boolean isPronto;
	private UploadedFile uploadedFile;
	private String fileName;
	private HtmlDataTable remessaAManipular;
	private boolean exibirBotaoAdicionar;
	private List<String> listaCidade;
	private List<String> listaUF;
	private boolean listaSemRemessa;
	private String dataCriacao;
	private List<TipoServico> listaEtiquetas;
	private boolean validacaoTemp;
	private Date dataFinalizacao;
	private boolean flagExibirModalPesquisaCep = false;
	private List<RelatorioRemessaDTO> consultaRemessasPorVolume;
	private String observacaoDestinatario = null;

	private Long idAndamentoSelecionado;
	private Andamento andamentoSelecionado;

	private String numeroAnoListaRemessa;

	private String mensagemValidacaoFinalizacaoListaRemessa;
	
	public String getMensagemValidacaoFinalizacaoListaRemessa() {
		return mensagemValidacaoFinalizacaoListaRemessa;
	}
	
	public void setMensagemValidacaoFinalizacaoListaRemessa(String mensagemValidacaoFinalizacaoListaRemessa) {
		this.mensagemValidacaoFinalizacaoListaRemessa = mensagemValidacaoFinalizacaoListaRemessa;
	}
	
	public BeanVisualizarListaRemessa getBeanVisualizarListaRemessa() {
		return beanVisualizarListaRemessa;
	}

	public void setBeanVisualizarListaRemessa(BeanVisualizarListaRemessa beanVisualizarListaRemessa) {
		this.beanVisualizarListaRemessa = beanVisualizarListaRemessa;
	}

	public BeanListaRemessa() {
		novo();
	}

	public Date getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(Date dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public boolean isValidacaoTemp() {
		return validacaoTemp;
	}

	public void setValidacaoTemp(boolean validacaoTemp) {
		this.validacaoTemp = validacaoTemp;
	}

	public List<TipoServico> getListaEtiquetas() {
		return listaEtiquetas;
	}

	public void setListaEtiquetas(List<TipoServico> listaEtiquetas) {
		this.listaEtiquetas = listaEtiquetas;
	}

	public String getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public List<String> getListaCidade() {
		return listaCidade;
	}

	public void setListaCidade(List<String> listaCidade) {
		this.listaCidade = listaCidade;
	}

	public List<String> getListaUF() {
		return listaUF;
	}

	public void setListaUF(List<String> listaUF) {
		this.listaUF = listaUF;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public boolean isPronto() {
		return isPronto;
	}

	public void setPronto(boolean isPronto) {
		this.isPronto = isPronto;
	}

	public String getArquivoUpload() {
		return arquivoUpload;
	}

	public void setArquivoUpload(String arquivoUpload) {
		this.arquivoUpload = arquivoUpload;
	}

	public List<ListaRemessa> getConjuntoRemessas() {
		return conjuntoRemessas;
	}

	public void setConjuntoRemessas(List<ListaRemessa> conjuntoRemessas) {
		this.conjuntoRemessas = conjuntoRemessas;
	}

	public PesquisaListaRemessaDto getPesquisaListaRemessaDto() {
		return pesquisaListaRemessaDto;
	}

	public void setPesquisaListaRemessaDto(PesquisaListaRemessaDto pesquisaListaRemessaDto) {
		this.pesquisaListaRemessaDto = pesquisaListaRemessaDto;
	}

	public Integer getQtdRegistros() {
		return qtdRegistros;
	}

	public void setQtdRegistros(Integer qtdRegistros) {
		this.qtdRegistros = qtdRegistros;
	}

	public List<Remessa> getRemessas() {
		return remessas;
	}

	public void setRemessas(List<Remessa> remessas) {
		this.remessas = remessas;
	}

	public String getCodigoUnidadePostagem() {
		return codigoUnidadePostagem;
	}

	public void setCodigoUnidadePostagem(String codigoUnidadePostagem) {
		this.codigoUnidadePostagem = codigoUnidadePostagem;
	}

	public TipoEntregaEnum getTipoEntrega() {
		return tipoEntrega;
	}

	public void setTipoEntrega(TipoEntregaEnum tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}

	public String getIdDestinatario() {
		return idDestinatario;
	}

	public void setIdDestinatario(String idDestinatario) {
		this.idDestinatario = idDestinatario;
	}

	public String getCodigoTipoServico() {
		return codigoTipoServico;
	}

	public void setCodigoTipoServico(String codigoTipoServico) {
		this.codigoTipoServico = codigoTipoServico;
	}

	public Remessa getRemessa() {
		return remessa;
	}

	public void setRemessa(Remessa remessa) {
		this.remessa = remessa;
	}

	public TipoEmbalagemEnum getTipoEmbalagem() {
		return tipoEmbalagem;
	}

	public void setTipoEmbalagem(TipoEmbalagemEnum tipoEmbalagem) {
		this.tipoEmbalagem = tipoEmbalagem;
	}

	public String getCodigoTipoEmbalagem() {
		return codigoTipoEmbalagem;
	}

	public void setCodigoTipoEmbalagem(String codigoTipoEmbalagem) {
		this.codigoTipoEmbalagem = codigoTipoEmbalagem;
	}

	public String[] getCodigoTipoServicoAdiconal() {
		return codigoTipoServicoAdiconal;
	}

	public void setCodigoTipoServicoAdiconal(String[] codigoTipoServicoAdiconal) {
		this.codigoTipoServicoAdiconal = codigoTipoServicoAdiconal;
	}

	public String[] getCodigoTipoServicoAdiconalObrigatorio() {
		return codigoTipoServicoAdiconalObrigatorio;
	}

	public void setCodigoTipoServicoAdiconalObrigatorio(String[] codigoTipoServicoAdiconalObrigatorio) {
		this.codigoTipoServicoAdiconalObrigatorio = codigoTipoServicoAdiconalObrigatorio;
	}

	public Integer getQtdVolumes() {
		return qtdVolumes;
	}

	public void setQtdVolumes(Integer qtdVolumes) {
		this.qtdVolumes = qtdVolumes;
	}

	public List<RemessaVolume> getListaVolumes() {
		return listaVolumes;
	}

	public void setListaVolumes(List<RemessaVolume> listaVolumes) {
		this.listaVolumes = listaVolumes;
	}

	public String getCodigoTipoDocumento() {
		return codigoTipoDocumento;
	}

	public void setCodigoTipoDocumento(String codigoTipoDocumento) {
		this.codigoTipoDocumento = codigoTipoDocumento;
	}

	public String getListasRemessa() {
		return listasRemessa;
	}

	public void setListasRemessa(String listasRemessa) {
		this.listasRemessa = listasRemessa;
	}

	public ListaRemessa getListaRemessas() {
		return listaRemessas;
	}

	public void setListaRemessas(ListaRemessa listaRemessas) {
		this.listaRemessas = listaRemessas;
	}

	public String getObservacaoDestinatario() {
		return observacaoDestinatario;
	}

	public void setObservacaoDestinatario(String observacaoDestinatario) {
		this.observacaoDestinatario = observacaoDestinatario;
	}

	public TipoServico getTipoServico() {
		try {
			List<TipoServico> listaTipoServico = getTipoServicoService().listarTiposServicoPorTipoEntrega(tipoEntrega);
			for (TipoServico item : listaTipoServico) {
				if (item.getId().toString().equals(codigoTipoServico)) {
					tipoServico = item;
					break;
				}
			}
		} catch (Exception e) {
			reportarErro("Erro ao Salvar Lista de Remessas", e, LOG);
		}
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	public UnidadePostagem getUnidadePostagem() {
		return unidadePostagem;
	}

	public void setUnidadePostagem(UnidadePostagem unidadePostagem) {
		this.unidadePostagem = unidadePostagem;
	}

	public HtmlDataTable getRemessaAManipular() {
		return remessaAManipular;
	}

	public void setRemessaAManipular(HtmlDataTable remessaAManipular) {
		this.remessaAManipular = remessaAManipular;
	}	

	public Long getIdAndamentoSelecionado() {
		return idAndamentoSelecionado;
	}

	public void setIdAndamentoSelecionado(Long idAndamentoSelecionado) {
		this.idAndamentoSelecionado = idAndamentoSelecionado;
		validarFinalizarLista();
	}

	public boolean isExibirBotaoAdicionar() {
		return exibirBotaoAdicionar;
	}

	public void setExibirBotaoAdicionar(boolean exibirBotaoAdicionar) {
		this.exibirBotaoAdicionar = exibirBotaoAdicionar;
	}

	public boolean isFlagExibirModalPesquisaCep() {
		return flagExibirModalPesquisaCep;
	}

	public List<RelatorioRemessaDTO> getConsultaRemessasPorVolume() {
		return consultaRemessasPorVolume;
	}

	public void setConsultaRemessasPorVolume(List<RelatorioRemessaDTO> consultaRemessasPorVolume) {
		this.consultaRemessasPorVolume = consultaRemessasPorVolume;
	}

	public Integer getQtdListas() {
		List<Integer> numListas = new ArrayList<Integer>();
		for(Remessa rem : remessas){
			if(!numListas.contains((int) rem.getListaRemessa().getNumeroListaRemessa())){
				numListas.add((int) rem.getListaRemessa().getNumeroListaRemessa());
			}
		}
		return numListas.size();
	}

	public void pesquisarRemessas(ActionEvent evt) {
		executarPesquisa();
	}

	@Override
	public void executarPesquisa() {
		RemessaService remessaService = getRemessaService();
		setRemessas(new ArrayList<Remessa>());
		
		validarNumeroAnoListaRemessa();
		if(!validarListaRemessa()){
			return;
		}

		if (pesquisaListaRemessaDto.isVazio()) {
			reportarAviso("Favor informar pelo menos um campo para realizar a pesquisa");
		} else if ((isDataInicialMaiorQueDataFinal(
				pesquisaListaRemessaDto.getDataCriacaoInicio(),
				pesquisaListaRemessaDto.getDataCriacaoFim()))
				|| (isDataInicialMaiorQueDataFinal(
						pesquisaListaRemessaDto.getDataEnvioInicio(),
						pesquisaListaRemessaDto.getDataEnvioFim()))) {
			reportarAviso("Data Inicial maior que Data Final");
		} else {
			try {
				remessas = remessaService.pesquisar(pesquisaListaRemessaDto);
				setConsultaRemessasPorVolume(RelatorioRemessaDTO.criar(remessas));
				setQtdRegistros(remessas.size());
				setQtdVolumes(consultaRemessasPorVolume.size());
				if (CollectionUtils.isVazia(remessas)) {
					reportarAviso("Remessa não Localizada.");
				}
			} catch (Exception exception) {
				reportarErro("Erro ao pesquisar Lista de Remessas", exception, LOG);
			}
		}
	}
	
	private boolean validarListaRemessa() {
		try{
			if(pesquisaListaRemessaDto.getRemessasListaRemessa() != null && !pesquisaListaRemessaDto.getRemessasListaRemessa().trim().isEmpty()){
				String[] listas = pesquisaListaRemessaDto.getRemessasListaRemessa().trim().split(";");
				for(String lista : listas){
					try{
						String[] parteLista = lista.trim().split("/");
						Integer.parseInt(parteLista[0]);
						Integer.parseInt(parteLista[1]);
					}catch(Exception e){
						reportarErro("Lista de Remessa vinculada inválida: " + lista);
						return false;
					}
				}		
			}
		}catch(Exception e){
			reportarErro("Lista de Remessa vinculada inválida.", e, LOG);
			return false;
		}
		return true;
	}

	private void validarNumeroAnoListaRemessa() {
		if(getNumeroAnoListaRemessa() != null && !getNumeroAnoListaRemessa().trim().isEmpty()){
			String[] numAno = getNumeroAnoListaRemessa().split("/");
			try{
				pesquisaListaRemessaDto.setNumeroListaRemessa(Long.parseLong(numAno[0]));
				if(numAno.length == 2){
					pesquisaListaRemessaDto.setAnoListaRemessa(Long.parseLong(numAno[1]));
				}
				
			}catch(Exception e){
				reportarErro("Número/Ano inválido");
			}
		}
		
	}

	public void pesquisarRemessasFinalizadas(ActionEvent evt) {
		RemessaService remessaService = getRemessaService();
		setRemessas(new ArrayList<Remessa>());

		validarNumeroAnoListaRemessa();
		
		if(!validarListaRemessa()){
			return;
		}
		
		if (pesquisaListaRemessaDto.isVazio()) {
			reportarAviso("Favor informar pelo menos um campo para realizar a pesquisa");
		} else if ((isDataInicialMaiorQueDataFinal(
				pesquisaListaRemessaDto.getDataCriacaoInicio(),
				pesquisaListaRemessaDto.getDataCriacaoFim()))
				|| (isDataInicialMaiorQueDataFinal(
						pesquisaListaRemessaDto.getDataEnvioInicio(),
						pesquisaListaRemessaDto.getDataEnvioFim()))) {
			reportarAviso("Data Inicial maior que Data Final");
		} else {
			try {
				remessas = remessaService.pesquisarEnviadas(pesquisaListaRemessaDto);
				setConsultaRemessasPorVolume(RelatorioRemessaDTO.criar(remessas));
				setQtdRegistros(remessas.size());
				setQtdVolumes(consultaRemessasPorVolume.size());
				if (CollectionUtils.isVazia(remessas)) {
					reportarAviso("Lista de Remessa não Localizada.");
				}
			} catch (Exception exception) {
				reportarErro("Erro ao pesquisar Lista de Remessas", exception, LOG);
			}
		}
	}

	public void pesquisarFinalizacao(ActionEvent evt) {
		ListaRemessaService listaRemessaService = getListaRemessaService();
		setConjuntoRemessas(new ArrayList<ListaRemessa>());

		validarNumeroAnoListaRemessa();
		
		if (pesquisaListaRemessaDto.isVazio()) {
			reportarAviso("Favor informar pelo menos um campo para realizar a pesquisa");
		} else if ((isDataInicialMaiorQueDataFinal(
				pesquisaListaRemessaDto.getDataCriacaoInicio(),
				pesquisaListaRemessaDto.getDataCriacaoFim()))) {
			reportarAviso("Data Inicial maior que Data Final");
		} else {
			try {
				conjuntoRemessas = listaRemessaService.pesquisar(pesquisaListaRemessaDto, false);
				Collections.sort(conjuntoRemessas, listaRemessaNumeroLista);
				setQtdRegistros(conjuntoRemessas.size());
				if (CollectionUtils.isVazia(conjuntoRemessas)) {
					reportarAviso("Remessa não Localizada.");
				}
			} catch (Exception exception) {
				reportarErro("Erro ao pesquisar Lista de Remessas", exception, LOG);
			}
		}
	}

	public String finalizarListaRemessa() {
		String resultado = null;
		if (validarArquivoAnexo()) {
			if (idAndamentoSelecionado == null) {
				reportarAviso("Favor selecione um andamento para ser registrado.");
			}else{
				ListaRemessa listaRemessa = null;
		        try {
		        	andamentoSelecionado = null;
		        	if(idAndamentoSelecionado != null && !idAndamentoSelecionado.equals(0L)){
		        		andamentoSelecionado = getAndamentoService().recuperarPorId(idAndamentoSelecionado);
		        	}
		        	Andamento andamentoExpedido = getAndamentoService().recuperarPorId(7317L);
		            ListaRemessa listaRemessasRecuperada = getListaRemessaService().recuperarPorId(listaRemessas.getId());
	
		            byte[] arquivo = IOUtils.toByteArray(uploadedFile.getInputStream());
		            List<ListaRemessa> listadeListaRemessa = new ArrayList<ListaRemessa>();
		            listadeListaRemessa.add(listaRemessasRecuperada);
		            
		            for(Remessa remessa : listaRemessasRecuperada.getRemessas()){
		        		if(remessa.getRemessasListasEnviadas() != null && !remessa.getRemessasListasEnviadas().isEmpty()){
		        			for( RemessaListaRemessa rl : remessa.getRemessasListasEnviadas()){
		        				listadeListaRemessa.add(rl.getListaRemessa());
		        			}
		        		}
		            }
		            
		            Iterator<ListaRemessa> it = listadeListaRemessa.iterator();		            
		            
		            while(it.hasNext()){
		            	listaRemessa = it.next();
		            	listaRemessa = getListaRemessaService().recuperarPorId(listaRemessa.getId());
		            	if(listaRemessa.getRemessas() != null){
		            		listaRemessa.getRemessas().iterator(); //necessário para evitar erro de closed session.
		            	}
		            	listaRemessa.setImagemListaRemessa(arquivo);	
		            	System.out.println("--> Iniciando a finalização da lista " + listaRemessa.getNumeroListaRemessaAnoFormato());
		            	processaFinalizacaoListaRemessa(listaRemessa, andamentoExpedido);
		            }
		            reportarInformacao("Lista de Remessa " + listaRemessasRecuperada.getNumeroListaRemessaAnoFormato() + " finalizada com sucesso!");
		            resultado = "remessaFinalizacaoPesquisa";
		        } catch (ServiceException e) {
		        	if(listaRemessa != null){
		        		reportarErro("Lista de Remessa " + listaRemessa.getNumeroListaRemessaAnoFormato() + " - " + e.getMessage());
		        	}
		            LOG.error("Falha ao recuperar lista para finalização.", e);
		        } catch (IOException e) {
		            reportarErro("Falha no upload.");
		            LOG.error("Falha no upload." + e.getMessage(), e);
		        } catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return resultado;
	}


	public void validarFinalizarLista() {
		StringBuffer pendencias = new StringBuffer();
		try {
			ListaRemessa listaRemessasRecuperada = getListaRemessaService().recuperarPorId(listaRemessas.getId());
			List<ListaRemessa> listadeListaRemessa = new ArrayList<ListaRemessa>();
			listadeListaRemessa.add(listaRemessasRecuperada);

			for(Remessa remessa : listaRemessasRecuperada.getRemessas()){
				if(remessa.getRemessasListasEnviadas() != null && !remessa.getRemessasListasEnviadas().isEmpty()){
					for( RemessaListaRemessa rl : remessa.getRemessasListasEnviadas()){
						listadeListaRemessa.add(rl.getListaRemessa());
					}
				}
			}

			Iterator<ListaRemessa> it = listadeListaRemessa.iterator();	
			ListaRemessa listaRemessa = null;
			while(it.hasNext()){
				listaRemessa = it.next();
				listaRemessa = getListaRemessaService().recuperarPorId(listaRemessa.getId());
				if(listaRemessa.getRemessas() != null){
					listaRemessa.getRemessas().iterator(); //necessário para evitar erro de closed session.
				}

				for(Remessa remessa : listaRemessa.getRemessas()){	
					
					if(remessa.getNumeroComunicacao() != null && !remessa.getNumeroComunicacao().trim().isEmpty()){
						String[] nrCom = remessa.getNumeroComunicacao().replaceAll("[^0-9?/]", "").trim().split("/");
						if(nrCom.length != 2){
							pendencias.append("Número da comunicação informado incorretamente: ").append(remessa.getNumeroComunicacao()).append("\n");
						}else{				
							try{
								Integer ano = Integer.parseInt(nrCom[1]);
								
								if(ano == null || ano.intValue() < 1900 || ano.intValue() > 2100 ) {
									pendencias.append("Número da comunicação informado incorretamente: ").append(remessa.getNumeroComunicacao()).append("\n");
								}
							}catch(NumberFormatException e){
								pendencias.append("Número da comunicação informado incorretamente: ").append(remessa.getNumeroComunicacao()).append("\n");
							}
						}
					}
					
					List<Processo> listaProcessos = new ArrayList<Processo>();
					if(remessa.getGuiaDeslocamento() != null && !remessa.getGuiaDeslocamento().trim().isEmpty()){
						String[] guias = remessa.getGuiaDeslocamento().split(";");
						for(String g : guias){
							String[] guiaDeslocamento = g.split("/");
							GuiaId guiaId = new GuiaId();
							guiaId.setNumeroGuia(Long.parseLong(guiaDeslocamento[0].trim()));
							guiaId.setAnoGuia(Short.valueOf(guiaDeslocamento[1].trim()));
							guiaId.setCodigoOrgaoOrigem(getSetorUsuarioAutenticado().getId());
							Guia guia = getGuiaService().recuperarPorId(guiaId);
							if(guia == null){
								pendencias.append("A origem da guia ").append(g).append(" diverge do setor do usuário.\n");
							}
						}
					}else{
						if(remessa.getVinculo() != null){
							String[] listaProc = remessa.getVinculo().split(";");
							for(String pr : listaProc){
								if(!pr.trim().isEmpty()){
									Processo proc = recuperarProcesso(pr.trim());
									if(proc != null){
										listaProcessos.add(proc);
									}else{
										pendencias.append("Não foi possível localizar o processo \"").append(pr.trim()).append("\".\n");
									}
								}
							}
						}
					}
										
					if(!listaProcessos.isEmpty()){		
						Iterator<Processo> lista = listaProcessos.iterator();
						while(lista.hasNext()){
							Processo processo = lista.next();
							ObjetoIncidente<?> oi = getObjetoIncidenteService().recuperarPorId(processo.getId());
							if(oi==null){
								pendencias.append("Necessário privilégio adequado para finalizar lista que faz referência a processo oculto.\n");
							}
							
						}
					}	
				}
			}			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(pendencias.length() == 0){
			setMensagemValidacaoFinalizacaoListaRemessa(null);
		}else{
			setMensagemValidacaoFinalizacaoListaRemessa(pendencias.toString());
		}
	}
	
	private Processo recuperarProcesso(String vinculo) throws ServiceException {
		if(vinculo != null && !vinculo.isEmpty()){
			String classProc = ProcessoParser.getSigla(vinculo);
			Long numProc = ProcessoParser.getNumero(vinculo);

			if (StringUtils.isVazia(classProc) || numProc == null) {
				return null;
			}

			return getProcessoService().recuperarProcesso(classProc, numProc);
		}
		return null;
	}

	private void processaFinalizacaoListaRemessa(ListaRemessa listaRemessa, Andamento andamentoDefault) throws ServiceException, DaoException {        
		Date dataCriacaoLocal = br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util.inicioDia(listaRemessa.getDataCriacao());
        Date dataInformada = getDataFinalizacao();
        Date agora = new Date();
		Date dataEnvio = br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util.inicioDia(dataInformada);
    	if (dataEnvio.after(agora)) {
    		reportarAviso("A data de remessa definida não pode ser posterior ao dia de hoje!");
    	} else if (dataEnvio.before(dataCriacaoLocal)) {
    		reportarAviso("A data de remessa definida não pode ser anterior à Data de Criação!");
    	} else {
    		listaRemessa.setDataEnvio(dataEnvio);
    		listaRemessa.setUsuarioEnvio(getUser().getUsername());
    		listaRemessa.setDataFinalizacao(agora);
    		
        	FinalizarRemessaDTO finalizarListaRemessaDTO = new FinalizarRemessaDTO.Builder().setListaRemessa(listaRemessa)
					.setSetorUsuarioAutenticado(getSetorUsuarioAutenticado())
					.setAndamento(andamentoSelecionado)
					.setAndamentoDefaut(andamentoDefault)
					.setIdUsuarioLogado(getUser().getUsername())
					.builder();
        	getListaRemessaService().finalizarListaRemessa(finalizarListaRemessaDTO);
        	
			conjuntoRemessas.remove(listaRemessa);
			setQtdRegistros(conjuntoRemessas.size());
    	}		
	}


	private boolean validarArquivoAnexo() {
		boolean resultado = true;
		if (uploadedFile == null) {
			reportarAviso("Para a finalização é obrigatório anexar um arquivo PDF à lista. O tamanho máximo permitido para o Upload é de 10 Mb.");
			resultado = false;
		} else {
	        Long tamanhoArquivo = uploadedFile.getSize();
	        Long tamanhoMaximoPermitido = (long) (UM_MEGABYTE * 10);
	        if (!uploadedFile.getContentType().contains("pdf")) {
	        	reportarAviso("Arquivo inválido. O arquivo deve ser do tipo PDF.");
				resultado = false;
	        } else if(tamanhoArquivo > tamanhoMaximoPermitido) {
	        	reportarAviso("O tamanho do arquivo é maior que o máximo permitido.");
				resultado = false;
	        }
		}
		return resultado;
	}

	public void limparCampos(ActionEvent evt) {
		remessas.clear();
		pesquisaListaRemessaDto.limpar();
		numeroAnoListaRemessa = null;
		setConjuntoRemessas(new ArrayList<ListaRemessa>());
		setListaRemessas(new ListaRemessa());
	}

	public String remessaPesquisa() {
		novo(null);
		return "remessaPesquisa";
	}

	public String retornoTelaInicial() {
		if (listaSemRemessa){
			novo(null);
			return "remessaPesquisa";
		} else {
			return "visualizarRemessa";
		}
	}

	public String remessaPesquisaExterna() {
		novo(null);
		return "remessaPesquisaExterna";
	}

	public String remessaFinalizacaoPesquisa() {
		novo(null);
		return voltarRemessaFinalizacaoPesquisa();
	}

	public String voltarRemessaFinalizacaoPesquisa() {
		return "remessaFinalizacaoPesquisa";
	}

	private Set<String> gerarSet(String[] lista) {
		Set<String> resultado = new HashSet<String>();
		if (lista != null) {
                    resultado.addAll(Arrays.asList(lista));
		}
		return resultado;
	}

	public void marcarServicosAdicionaisDependentes(ActionEvent evt) {
		Set<String> listaAnterior = gerarSet(codigoTipoServicoAdiconalAnterior);
		Set<String> listaAtual = gerarSet(codigoTipoServicoAdiconal);
		String idTipoServicoAlterado = recuperarIdServicoAlterado(listaAnterior, listaAtual);
		boolean isMarcacao = listaAtual.size() > listaAnterior.size();
		TipoServico tipoServicoNecessariosAlterado;
		if (isMarcacao) {
			tipoServicoNecessariosAlterado = getTipoServico().getTipoServicoNecessarioAoAdicional(Long.parseLong(idTipoServicoAlterado));
		} else {
			tipoServicoNecessariosAlterado = getTipoServico().getTipoServicoAdicionalAoNecessario(Long.parseLong(idTipoServicoAlterado));
		}
		if (tipoServicoNecessariosAlterado != null) {
			Set<String> ids = new HashSet<String>(listaAtual);
			if (isMarcacao) {
				String idTipoServicoNecessariosAlterado = tipoServicoNecessariosAlterado.getId().toString();
				ids.add(idTipoServicoNecessariosAlterado);
			} else {
				ids.remove(idTipoServicoAlterado);
			}
			definirTiposServicosAdicionais(ids);
		}
		codigoTipoServicoAdiconalAnterior = codigoTipoServicoAdiconal;
	}

    private String recuperarIdServicoAlterado(Set<String> listaAnterior, Set<String> listaAtual) {
    	String idTipoServicoAlterado;
    	Set<String> listaAux;
    	if (listaAnterior.size() > listaAtual.size()) {
    		listaAux = new HashSet<String>(listaAnterior);
			listaAux.removeAll(listaAtual);
		} else {
			listaAux = new HashSet<String>(listaAtual);
			listaAux.removeAll(listaAnterior);
		}
    	idTipoServicoAlterado = listaAux.iterator().next();
    	return idTipoServicoAlterado;
    }

	private void definirTiposServicosAdicionais(Set<String> ids) {
		codigoTipoServicoAdiconal = new String[ids.size()];
		int indice = 0;
		for (String id : ids) {
			codigoTipoServicoAdiconal[indice++] = id;
		}
	}

	public String getLabelNumeroMalote() {
		String label = "Número do Malote";
		if (isExibirMalote()) {
			label += "*";
		}
		return label;
	}

	public String getLabelNumeroLacre() {
		String label = "Número do Lacre";
		if (isExibirMalote()) {
			label += "*";
		}
		return label;
	}

	public String getLabelGuiaDeslocamento() {
		String label = "Guia de Deslocamento";
		if (isExibirMalote()) {
			label += "*";
		}
		return label;
	}

	public void adicionar(ActionEvent evt) {
		adicionarTiposServicoObrigatorios();
		setObservacaoDestinatario(this.observacaoDestinatario);
		boolean validado = prepararParaAdicionar();
		if (validado && !isErroValidacaoAdicionarRemessa()) {
			setObservacaoDestinatario(null);
			remessas.add(0, remessa);
			limparTelaParaNovo(null);
		}
	}

	private void adicionarTiposServicoObrigatorios() {
		if (codigoTipoServico != null && !codigoTipoServico.isEmpty()) {
			for (TipoServicoCompativel tipoServicoCompativel : getTipoServico().getTiposServicosPrincipais()) {
				TipoServico tipoServicoLocal = tipoServicoCompativel.getTipoServicoAdicional();
				if (tipoServicoCompativel.isObrigatorio()) {
					remessa.addTipoServico(tipoServicoLocal);
				}
			}
		}
	}

	public void excluirRemessa(ActionEvent event) {
		Remessa remessaLocal = getRemessaSelecionada();
		remessas.remove(remessaLocal);
	}

	private Remessa getRemessaSelecionada() {
		Remessa remessaLocal = (Remessa) remessaAManipular.getRowData();
		return remessaLocal;
	}

	public String salvarLista() {
		String resultado = "";
		ListaRemessaService listaRemessaService = getListaRemessaService();
		boolean isCadastro = listaRemessas.getId() == null;
		try {
			if (isCorreios()) {
				ContratoPostagem contratoPostagem = getContratoPostagemVigente();
				listaRemessas.setContratoPostagem(contratoPostagem);
			}
			preparaListaRemessas();
			listaRemessaService.salvar(listaRemessas);
			reportarInformacao("Lista salva com sucesso!");
			setExibirBotaoAdicionar(false);
			beanVisualizarListaRemessa.visualizarListaRemessa(listaRemessas);
			resultado = retornoTelaInicial();
		} catch (Exception e) {
			if (isCadastro) {
				listaRemessas.setId(null);
				listaRemessas.setAnoListaRemessa(0);
				listaRemessas.setNumeroListaRemessa(0);
				for (Remessa remessa : listaRemessas.getRemessas()) {
					remessa.setId(null);
					for (RemessaVolume volume : remessa.getVolumes()) {
						volume.setId(null);
					}
					if (remessa.getRemessasTiposServico() != null) {
						for (RemessaTipoServico tipoServico : remessa.getRemessasTiposServico()) {
							tipoServico.setId(null);
						}
					}
					if (remessa.getRemessasListasEnviadas() != null) {
						for (RemessaListaRemessa listaRemessa : remessa.getRemessasListasEnviadas()) {
							listaRemessa.setId(null);
						}
					}
				}
			}
			if (e.getMessage() != null) {
				reportarErro("Retorno os Correios ao validar os dados: ", e, LOG);
			}else {
				reportarErro("Erro ao Salvar Lista de Remessas. O serviços dos Correios pode estar indisponível. Tente novamente em minutos.", e, LOG);
			}
		}
		return resultado;
	}

	private boolean isErroValidacaoAdicionarRemessa() {
		String mensagemErro;
		if (tipoEntrega == null) {
			mensagemErro = getMensagemCampoObrigatorio("Tipo de Entrega");
		} else if (isMalote()) {
			mensagemErro = validarCamposObrigatoriosMalote();
		} else if (isCorreios() || isEntregaPortaria()) {
			mensagemErro = validarCamposObrigatoriosCorreiosEntregaPortaria();
		} else {
			mensagemErro = verificarCamposObrigatorios(codigoTipoServico, "Serviço",
					remessa.getDescricaoPrincipal(), "Destinatário",
					remessa.getCep(), "CEP",
					remessa.getUf(), "UF",
					remessa.getCidade(), "Cidade",
					remessa.getLogradouro(), "Logradouro",
					remessa.getBairro(), "Bairro",
					remessa.getNumero(), "Número");
		}

		if (!mensagemErro.isEmpty()) {
			reportarAviso(mensagemErro);
		}

		return (validacaoTemp = !mensagemErro.isEmpty());
	}

	private String validarCamposObrigatoriosCorreiosEntregaPortaria() {
		String mensagemErro = "";
		if (isCorreios() && br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util.isStringNulaOuVazia(codigoUnidadePostagem)) {
			mensagemErro = getMensagemCampoObrigatorio("Unidade de Postagem");
		} else if (isCorreios() && remessa.getTipoEmbalagem() == null) {
			mensagemErro = getMensagemCampoObrigatorio("Tipo de Remessa");
		} else if(isCorreios()){ 
			mensagemErro = verificarCamposObrigatorios(codigoTipoServico, "Serviço",
					remessa.getDescricaoPrincipal(), "Destinatário",
					remessa.getCep(), "CEP",
					remessa.getUf(), "UF",
					remessa.getCidade(), "Cidade",
					remessa.getLogradouro(), "Logradouro",
					remessa.getBairro(), "Bairro",
					remessa.getNumero(), "Número");
		} else if (!isTodosVolumesComPeso()) {
			mensagemErro = getMensagemCampoObrigatorio("Peso");
		} else if (!remessa.isDadosComunicacaoValidos()) {
			mensagemErro = getMensagemCampoObrigatorio("Número de Documento");
		}
		return mensagemErro;
	}

	private String validarCamposObrigatoriosMalote() {
		String mensagemErro = verificarCamposObrigatorios(remessa.getMalote(), "Número do Malote",
				remessa.getLacre(), "Número do Lacre",
				remessa.getGuiaDeslocamento(), "Guia de Deslocamento");
		return mensagemErro;
	}

	/**
	 * Este método verifica se os campos informados foram preenchidos, e gera uma mensagem caso não sejam gera uma mesagem.
	 * Para isso, devem ser informados os pares, "conteúdo do campo" e "nome do campo" no array de varargs.
	 * 
	 * @param valoresENomesCampos
	 * @return
	 */
	private String verificarCamposObrigatorios(String...valoresENomesCampos) {
		String mensagemErro = "";
		if (valoresENomesCampos != null) {
			int posicaoValor = 0;
			int posicaoNome = 1;
			int incremento = 2;
			int ultimaPosicaoNome = valoresENomesCampos.length;
			while (posicaoNome <= ultimaPosicaoNome) {
				String valor = valoresENomesCampos[posicaoValor];
				if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util.isStringNulaOuVazia(valor)) {
					String nome = valoresENomesCampos[posicaoNome];
					mensagemErro = getMensagemCampoObrigatorio(nome);
					break;
				}
				posicaoValor += incremento;
				posicaoNome += incremento;
			}
		}
		return mensagemErro;
	}

	private boolean isEntregaPortaria() {
		return TipoEntregaEnum.ENTREGA_PORTARIA.equals(tipoEntrega);
	}

	private boolean isMalote() {
		return TipoEntregaEnum.MALOTE.equals(tipoEntrega);
	}

	private boolean isCorreios() {
		return TipoEntregaEnum.CORREIOS.equals(tipoEntrega);
	}

	private String getMensagemCampoObrigatorio(String nomeCampo) {
		return "Favor preencher o campo obrigatório '" + nomeCampo + "'.";
	}

	private boolean isTodosVolumesComPeso() {
		boolean retorno = true;

		for (RemessaVolume item : remessa.getVolumes()) {
			if (item.getPesoGramas() == null || item.getPesoGramas() <= 0) {
				retorno = false;
                                break;
			}
		}
		return retorno;
	}

	private boolean prepararParaAdicionar() {
		if (validarGuias() && separaListasRemessa() && preparaVolumes()) {
			preparaTipoDocumento();
			preparaTipoEmbalagem();
			preparaServicoAdicional();
			return true;
		} else {
			return false;
		}
	}

	public boolean isExibirCampoUnidadePostagem() {
		boolean retorno = false;
		if (tipoEntrega != null && isCorreios()) {
			retorno = true;
		}
		return retorno;
	}

	public boolean isExibirCampoTipoRemessa() {
		boolean retorno = false;
		if (isCorreios()) {
			retorno = true;
		}
		return retorno;
	}

	public boolean isVerificarTipoDocumento() {
		boolean retorno = false;
		try {
			for (TipoComunicacaoExpedicao item : getTipoComunicacaoExpedicaoService().listarTiposComunicacao()) {
				if (item.isExigeNumeracao() && item.getId().toString().equals(codigoTipoDocumento)) {
					retorno = true;
                                        break;
				}
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar Tipo Documento", e, LOG);
		}
		return retorno;
	}

	public boolean isExibirCampoTipoServico() {
		boolean retorno = false;
		List<SelectItem> items = getComboTipoServico();
		if (items.size() > 2) {
			retorno = true;
		} else {
			for (SelectItem selectItem : items) {
				Object valor = selectItem.getValue();
				if (valor != null) {
					codigoTipoServico = valor.toString();
					break;
				}
			}
		}
		return retorno;
	}

	public boolean isExibirCampoTipoEmbalagem() {
		boolean retorno = false;
		if (isCorreios()
				&& (getRadioTipoEmbalagem().size() > 1)) {
			retorno = true;
		}
		return retorno;
	}

	public boolean isExibirCampoServicoAdicional() {
		boolean retorno = false;
		if (isExibirCampoTipoEmbalagem()
			|| (isCorreios()
			&& TipoEmbalagemEnum.CAIXA_PACOTE.equals(tipoEmbalagem))) {
			retorno = true;
		}
		return retorno;
	}

	public boolean isExibirCorreiosOuPortaria() {
		boolean retorno = false;
		if ((tipoEntrega != null
				&& (isCorreios()
						|| isEntregaPortaria()))) {
			retorno = true;
		}
		return retorno;
	}

	public boolean isExibirMalote() {
		return isMalote();
	}

	public void novo(ActionEvent evt) {
		novo();
	}

	private void novo() {
		setObservacaoDestinatario(null);
		setExibirBotaoAdicionar(true);
		limparTelaCabecalho(null);
		limparTelaParaNovo(null);
		setListaRemessas(new ListaRemessa());
		setRemessas(new ArrayList<Remessa>());
		setTipoServico(null);
		setUnidadePostagem(null);
		setListaRemessas(new ListaRemessa());
		setConjuntoRemessas(new ArrayList<ListaRemessa>());
		setPesquisaListaRemessaDto(new PesquisaListaRemessaDto());
		setDataFinalizacao(new Date());
		flagExibirModalPesquisaCep = false;
		numeroAnoListaRemessa = null;
	}

	public void limparTelaCabecalho(ActionEvent evt) {
		setTipoEntrega(null);
		setCodigoUnidadePostagem(null);
		setCodigoTipoServico(null);
	}

	public void limparTelaParaNovo(ActionEvent evt) {
		setCodigoTipoServicoAdiconal(null);
		setCodigoTipoEmbalagem(null);
		setTipoEmbalagem(null);
		setRemessa(new Remessa());
		remessa.setPais(PAIS);
		setListaVolumes(new ArrayList<RemessaVolume>());
		setQtdVolumes(1);
		geraListaVolumes(null);
		setListasRemessa(null);
		setCodigoTipoDocumento(null);
		numeroAnoListaRemessa = null;
	}

	public void limparCamposCadastroRemessa(ActionEvent evt) {
		setCodigoTipoEmbalagem(null);
	}

	public void limparCamposCadastro(ActionEvent evt) {
		setCodigoUnidadePostagem(null);
		setCodigoTipoServico(null);
		setCodigoTipoServicoAdiconal(null);
	}

	public List<SelectItem> getComboUnidadeDePostagem() {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		try {
			for (UnidadePostagem item : getUnidadePostagemService().listarAtivos()) {
				lista.add(new SelectItem(item.getCodigoUnidadePostagem(), item.getNomeUnidadePostagem()));
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar Unidades de Postagem", e, LOG);
		}
		return lista;
	}

	public List<String> getComboUF() {
		listaUF = new ArrayList<String>();
		try {
			for (UnidadeFederacao item : getUnidadeFederacaoService().listarAtivos()) {
				listaUF.add(item.getSigla());
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar Unidades da Federação", e, LOG);
		}
		return listaUF;
	}

	public void configurarMunicipio(ActionEvent e) throws ServiceException {
		remessa.setCidade("");
    	MunicipioService municipioService = getMunicipioService();
    	listaCidade = new ArrayList<String>();
    	for(Municipio m : municipioService.listarAtivosTipoMunicipio(remessa.getUf())) {
    		listaCidade.add(m.getNome());
    	}
    	setListaCidade(listaCidade);
    }

	public List<SelectItem> getComboMunicipio() {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		lista.add(new SelectItem(null, null));
		if (getRemessa().getUf() != null && !getRemessa().getUf().isEmpty()) {
			try {
				for (Municipio item : getMunicipioService().listarAtivosTipoMunicipio(getRemessa().getUf())) {
					lista.add(new SelectItem(item.getNome(), item.getNome()));
				}
			} catch (Exception e) {
				reportarErro("Erro ao pesquisar Municipios", e, LOG);
			}
		}
		return lista;
	}

	public void selecionarServico(ActionEvent event) {
		tipoEmbalagem = null;
		codigoTipoEmbalagem = "";
	}

	public List<SelectItem> getComboTipoServico() {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		listaEtiquetas = new ArrayList<TipoServico>();
		if (tipoEntrega != null) {
			lista.add(new SelectItem(null, null));
			try {
				for (TipoServico item : getTipoServicoService().listarTiposServicoPorTipoEntrega(tipoEntrega)) {
					lista.add(new SelectItem(item.getId(), item.getNome()));
					listaEtiquetas.add(item);
				}
			} catch (Exception e) {
				reportarErro("Erro ao pesquisar Unidades de Postagem", e, LOG);
			}
		}
		return lista;
	}

	public List<SelectItem> getComboTipoDocumento() {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		lista.add(new SelectItem(null, null));
		try {
			for (TipoComunicacaoExpedicao item : getTipoComunicacaoExpedicaoService().listarTiposComunicacao()) {
				lista.add(new SelectItem(item.getId(), item.getDescricao()));
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar Tipo Documento", e, LOG);
		}
		return lista;
	}

	public List<SelectItem> getTiposRemessa() {
		List<SelectItem> lista = new LinkedList<SelectItem>();
        TipoServicoCodigoEnum tipoServicoCodigoEnum = TipoServicoCodigoEnum.buscarPeloCodigo(codigoTipoServico);
		if (!TipoServicoCodigoEnum.PAC.equals(tipoServicoCodigoEnum)) {
			lista.add(new SelectItem(TipoEmbalagemEnum.ENVELOPE, TipoEmbalagemEnum.ENVELOPE.getDescricao()));
		}
		if (!TipoServicoCodigoEnum.CARTA.equals(tipoServicoCodigoEnum)) {
			lista.add(new SelectItem(TipoEmbalagemEnum.CAIXA_PACOTE, TipoEmbalagemEnum.CAIXA_PACOTE.getDescricao()));
		}
		return lista;
	}

	public List<SelectItem> getRadioTipoEmbalagem() {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		if (tipoEmbalagem != null) {
			try {
				List<TipoEmbalagem> listaTipoEmbalagem = getTipoEmbalagemService().listarTiposServicoPorTipoEntrega(tipoEmbalagem);
				if (listaTipoEmbalagem.size() == 1) {
					TipoEmbalagem tipoEmbalagemLocal = listaTipoEmbalagem.get(0);
					codigoTipoEmbalagem = tipoEmbalagemLocal.getId().toString();
				} else {
					for (TipoEmbalagem tipoEmbalagemLocal : listaTipoEmbalagem) {
						lista.add(new SelectItem(tipoEmbalagemLocal.getId(), tipoEmbalagemLocal.getNome()));
					}
				}
			} catch (Exception e) {
				reportarErro("Erro ao pesquisar Tipo de Embalagem", e, LOG);
			}
		}

		return lista;
	}

	public List<SelectItem> getCheckTipoServicoAdicionais() {
		List<TipoServico> listaTipoServico = new ArrayList<TipoServico>();
		remessa.getTiposServico().clear();
		if (codigoTipoServico != null && !codigoTipoServico.isEmpty()) {
			for (TipoServicoCompativel tipoServicoCompativel : getTipoServico().getTiposServicosPrincipais()) {
				TipoServico tipoServicoLocal = tipoServicoCompativel.getTipoServicoAdicional();
				if (!tipoServicoCompativel.isObrigatorio()) {
					listaTipoServico.add(tipoServicoLocal);
				}
			}
		}
		Collections.sort(listaTipoServico);
		List<SelectItem> lista = new LinkedList<SelectItem>();
		for (TipoServico tipoServicoLocal : listaTipoServico) {
			lista.add(new SelectItem(tipoServicoLocal.getId().toString(), tipoServicoLocal.getNome()));
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaAndamentos(){
		
		List<SelectItem> lista = (List<SelectItem>) getRequestValue("listaAndamentos");
		if(lista==null || lista.isEmpty()){
			lista = new ArrayList<SelectItem>();
			lista.add(new SelectItem(0, "<< Sem andamento >>"));
			setIdAndamentoSelecionado(0L);
			//Se a lista de remessa tiver sido reaberta, então não permitir lançar andamentos
			if(listaRemessas.getDataFinalizacao() == null){
				try {
					List<Long> idAndamentos = new ArrayList<Long>(Arrays.asList(7104L, 7100L, 7101L, 7108L, 7317L));
					List<SearchCriterion> search = new ArrayList<SearchCriterion>();
					search.add(new InCriterion<Long>("id", idAndamentos));
					List<Andamento> andamentos = getAndamentoService().pesquisarPorExemplo(new Andamento(), search);
					if(andamentos != null && !andamentos.isEmpty()){
						for(Andamento anda : andamentos){
							lista.add(new SelectItem(anda.getId(), anda.getIdentificacao()));
						}
					}
					setIdAndamentoSelecionado(7317L);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			setRequestValue("listaAndamentos", lista);
		}
		
		return lista;
	}

	public void geraListaVolumes(ActionEvent evt) {
		listaVolumes.clear();
		if (qtdVolumes < 1) {
			reportarAviso("Quantidade de volumes deve ser maior que 0.");
			qtdVolumes = 1;
		}
		if (qtdVolumes != null) {
			for (int indice = 1; indice <= qtdVolumes; indice++) {
				RemessaVolume remessaVolume = new RemessaVolume();
				remessaVolume.setNumeroVolume(indice);
				remessaVolume.setPesoGramas(0);
				listaVolumes.add(remessaVolume);
			}
		}
	}

	private boolean isDataInicialMaiorQueDataFinal(Date dtInicio, Date dtFim) {
		if (dtInicio != null && dtFim != null) {
			return dtInicio.compareTo(dtFim) > 0;
		} else {
			return false;
		}
	}

	private List<NumeroAnoVo> carregarNumerosAnos(String nomeCampo, String listaNumeroAno) throws Exception {
		List<NumeroAnoVo> resultado = new ArrayList<NumeroAnoVo>();
		String[] listas = null;
		if (listaNumeroAno != null && !listaNumeroAno.isEmpty()) {
			listas = listaNumeroAno.split(SEPARADOR_LISTAS_REMESSA);
		}
		if (listas != null) {
			for (String idLista : listas) {
				String idListaAux = idLista.trim();
				if (!idListaAux.isEmpty()) {
					NumeroAnoVo numeroAno = new NumeroAnoVo(idListaAux);
					if (numeroAno.getNumero() == null || numeroAno.getAno() == null) {
							throw new Exception("Número da " + nomeCampo + " inválido ('" + idListaAux.trim() + "').");
					}
					resultado.add(numeroAno);
				}
			}
		}
		return resultado;
	}

	private boolean validarGuias() {
		try {
			carregarNumerosAnos("Guia de deslocamento", remessa.getGuiaDeslocamento());
			return true;
		} catch (Exception e) {
			reportarAviso(e.getMessage());
			return false;
		}
	}

	private boolean separaListasRemessa() {
		boolean resultado = true;
		try {
			ListaRemessaService listaRemessaService = getListaRemessaService();
			String nomeCampo = "Lista(s) de Remessa";
			List<NumeroAnoVo> listaNumeroAno = carregarNumerosAnos(nomeCampo, listasRemessa);
			List<ListaRemessa> listListaRemessa = new ArrayList<ListaRemessa>();
			for (NumeroAnoVo numeroAno : listaNumeroAno) {
				ListaRemessa listaRemessa;
				try {
					listaRemessa = listaRemessaService.pesquisar(numeroAno.getNumero(), numeroAno.getAno());
					if (listaRemessa == null) {
						reportarAviso("Número da " + nomeCampo + " inválido ('" + numeroAno + "').");
						resultado = false;
						break;
					} else {
						listListaRemessa.add(listaRemessa);
					}
				} catch (ServiceException e) {
					reportarAviso("Número da " + nomeCampo + " inválido ('" + numeroAno + "').");
					resultado = false;
					break;
				}
			}
			if (resultado) {
				remessa.setListasEnviadas(listListaRemessa);
			}
		} catch (Exception e) {
			reportarAviso(e.getMessage());
			resultado = false;
		}
		return resultado;
	}

	public String getPais() {
		return PAIS;
	}

	private void preparaTipoDocumento() {
		try {
			for (TipoComunicacaoExpedicao item : getTipoComunicacaoExpedicaoService().listarTiposComunicacao()) {
				if (item.getId().toString().equals(codigoTipoDocumento)) {
					remessa.setTipoComunicacao(item);
				}
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar Tipo Documento", e, LOG);
		}
	}

	private void preparaTipoEmbalagem() {
		remessa.setTipoEmbalagem(null);
		try {
			List<TipoEmbalagem> lista = getTipoEmbalagemService().listarTiposServicoPorTipoEntrega(tipoEmbalagem);
			if (lista.size() == 1) {
				remessa.setTipoEmbalagem(lista.get(0));
			} else {
				for (TipoEmbalagem item : lista) {
					if (item.getId().toString().equals(codigoTipoEmbalagem)) {
						remessa.setTipoEmbalagem(item);
					}
				}
			}

		} catch (Exception e) {
			reportarErro("Erro ao pesquisar Tipo Embalagem", e, LOG);
		}
	}

	private void preparaUnidadePostagem() {
		try {
			for (UnidadePostagem item : getUnidadePostagemService().listarAtivos()) {
				if (item.getId().toString().equals(codigoUnidadePostagem)) {
					unidadePostagem = item;
					break;
				}
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar UnidadePostagem", e, LOG);
		}
	}

	private boolean preparaVolumes() {
		boolean resultado = true;
		if (resultado) {
			if (!isMalote()) {
				for (RemessaVolume remessaVolume : listaVolumes) {
					if (remessaVolume.getPesoGramas() < 1) {
						reportarErro("O peso dos volumes devem ser maiores que zero.");
						resultado = false;
						break;
					}
				}
			}
			if (resultado) {
				remessa.setVolumes(listaVolumes);
			}
		}
		return resultado;
	}

	private void preparaListaRemessas() {
		preparaUnidadePostagem();
		if (isCorreios()) {
			getListaRemessas().setUnidadePostagem(unidadePostagem);
		} else if (isEntregaPortaria() || isMalote()) {
			getListaRemessas().setUnidadePostagem(null);
		}
		for (Remessa remessaLocal : remessas) {
			limparDadosTipoEntregaRemessa(remessaLocal);
		}
		getListaRemessas().setRemessas(new ArrayList<Remessa>(remessas));
		getListaRemessas().setTipoServico(getTipoServico());
		getListaRemessas().setUsuarioCriacao(getUser().getUsername());
	}

	private void limparDadosTipoEntregaRemessa(Remessa remessa) {
		if (isEntregaPortaria()) {
			remessa.clearTiposServico();
			remessa.setTipoEmbalagem(null);
		} else if(isMalote()){
			setCodigoUnidadePostagem(null);
			setCodigoTipoServico(null);
			setTipoEmbalagem(null);
		}
	}

	public void preparaServicoAdicional() {
		if (codigoTipoServicoAdiconal != null && codigoTipoServico != null && !codigoTipoServico.isEmpty()) {
			try {
				List<TipoServico> tiposServicosDependentesDosSelecionados = new ArrayList<TipoServico>();
				for (TipoServicoCompativel tipoServicoCompativel : getTipoServico().getTiposServicosPrincipais()) {
					TipoServico tipoServicoAdicional = tipoServicoCompativel.getTipoServicoAdicional();
					TipoServico tipoServicoNecessario = tipoServicoCompativel.getTipoServicoNecessario();
					for (String codTipoServicoAdiconal : codigoTipoServicoAdiconal) {
						if (tipoServicoAdicional.getId().toString().equals(codTipoServicoAdiconal)) {
							remessa.addTipoServico(tipoServicoAdicional);
							if (tipoServicoNecessario != null) {
								tiposServicosDependentesDosSelecionados.add(tipoServicoNecessario);
							}
						}
					}
				}
				tiposServicosDependentesDosSelecionados.removeAll(remessa.getTiposServico());
				if (!tiposServicosDependentesDosSelecionados.isEmpty()) {
					reportarErro("Tipo de serviço obrigatório não marcado");
				}
			} catch (Exception e) {
				reportarErro("Erro ao pesquisar Tipos de Servicos Adicionais", e, LOG);
			}
		}
	}

	public boolean isExisteRemessa() {
		return (remessas != null && !remessas.isEmpty());
	}

	public String getTituloLista() {
		String textoNumero;
		String textoData;
		Date data;
		String texto ="Lista de Remessa";
		if (listaRemessas != null && listaRemessas.getId() != null) {
			textoNumero = listaRemessas.getNumeroListaRemessa() + "";
			data = listaRemessas.getDataCriacao();
			textoData = br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util.getDiaMes(data)
					+ BARRA
					+ br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util.getMes(data)
					+ BARRA
					+ br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util.getAno(data);
			texto = texto+ " - Lista nº " + textoNumero
					+ " - Criada em " + textoData;
		} 
		return texto;
	}

	public String abrirFinalizacao() {
		listaRemessas.setObservacao(null);
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dataCriacao = sdf.format(listaRemessas.getDataCriacao());
		setDataFinalizacao(new Date());
		setRequestValue("listaAndamentos", null);
		return "remessaFinalizacao";
	}

	private ContratoPostagem getContratoPostagemVigente() throws ServiceException {
		ContratoPostagem contratoPostagem = getContratoPostagemService().buscarVigente(new Date());
		if (contratoPostagem == null) {
			throw new ServiceException("Nenhum contrato de cartão de postagem vigente encontrado.");
		}
		return contratoPostagem;
	}

	@Override
	public String getNomeComponenteRerenderizar() {
		return "panelRemessa";
	}

	@Override
	public void limparCamposDestinatario() {
		remessa.setDescricaoAnterior("");
		remessa.setDescricaoPrincipal("");
		remessa.setDescricaoPosterior("");
		remessa.setLogradouro("");
		remessa.setNumero("");
		remessa.setComplemento("");
		remessa.setBairro("");
	    remessa.setCidade("");
	    remessa.setCep("");
	    remessa.setUf("");
	    remessa.setNomeContato("");
	    remessa.setEmail("");
	    remessa.setCodigoAreaTelefone("");
	    remessa.setNumeroTelefone("");
	    remessa.setCodigoAreaFax("");
	    remessa.setNumeroFax("");
	    remessa.setAgrupador("");
	    remessa.setCodigoOrigem(null);
	}

	@Override
	public void receberDestinatarioSelecionado(DestinatarioListaRemessa destinatarioListaRemessa) throws ServiceException {
		if (destinatarioListaRemessa != null) {
			remessa.setDescricaoAnterior(destinatarioListaRemessa.getDescricaoAnterior());
			remessa.setDescricaoPrincipal(destinatarioListaRemessa.getDescricaoPrincipal());
			remessa.setDescricaoPosterior(destinatarioListaRemessa.getDescricaoPosterior());
			remessa.setLogradouro(destinatarioListaRemessa.getLogradouro());
			remessa.setNumero(destinatarioListaRemessa.getNumero());
			remessa.setComplemento(destinatarioListaRemessa.getComplemento());
			remessa.setBairro(destinatarioListaRemessa.getBairro());
			remessa.setUf(destinatarioListaRemessa.getMunicipio().getSiglaUf());
			configurarMunicipio(null);
		    remessa.setCidade(destinatarioListaRemessa.getMunicipio().getNome());
		    remessa.setCep(destinatarioListaRemessa.getCep());
		    remessa.setNomeContato(destinatarioListaRemessa.getNomeContato());
		    remessa.setEmail(destinatarioListaRemessa.getEmail());
		    remessa.setCodigoAreaTelefone(destinatarioListaRemessa.getCodigoAreaTelefone());
		    remessa.setNumeroTelefone(destinatarioListaRemessa.getNumeroTelefone());
		    remessa.setCodigoAreaFax(destinatarioListaRemessa.getCodigoAreaFax());
		    remessa.setNumeroFax(destinatarioListaRemessa.getNumeroFax());
		    remessa.setAgrupador(destinatarioListaRemessa.getAgrupador());
		    remessa.setCodigoOrigem(destinatarioListaRemessa.getCodigoOrigem());
		    this.setObservacaoDestinatario(destinatarioListaRemessa.getObservacao());
		}
	}

	public void excluirRemessaDaListaDeRemessas(ActionEvent event) {
		boolean excluirLista = false;
		try {
			ListaRemessa lista = remessa.getListaRemessa();
    		if (lista.getRemessas().size() > 1) {
	    		excluirLista = false;
    			RemessaService remessaService = getRemessaService();
	    		remessaService.excluir(remessa);
	    		reportarInformacao("Remessa excluída com sucesso.");
    		} else {
	    		excluirLista = true;
    			ListaRemessaService listaRemessaService = getListaRemessaService();
    			lista = listaRemessaService.recuperarPorId(lista.getId());
    			listaRemessaService.excluir(lista);
    			reportarInformacao("Lista excluída por não possuir Remessas!");
    		}
    		executarPesquisa(); 
		} catch (ServiceException e) {
			if (excluirLista) {
				reportarInformacao("Erro ao excluir lista de remessa.");
			} else {
				reportarInformacao("Erro ao excluir remessa.");
			}
            LOG.error(e.getMessage(), e);
            LOG.error(e.getStackTrace()[0], e);
		}
    }

	public void reabrirRemessaDaListaDeRemessas(ActionEvent event) {
		try {
			ListaRemessa lista = remessa.getListaRemessa();
			getListaRemessaService().reabrirListaRemessa(lista);;
			reportarInformacao("Lista de remessa reaberta com sucesso.");
			executarPesquisa(); 
		} catch (ServiceException e) {
			reportarInformacao("Erro ao reabrir a lista de remessa.");
			LOG.error(e.getMessage(), e);
			LOG.error(e.getStackTrace()[0], e);
		}
	}
	
	public void pesquisarDestinatarioPeloId(ActionEvent event) {
		if (idDestinatario != null && !idDestinatario.isEmpty()) {
			try {
				DestinatarioListaRemessa destinatarioListaRemessa = getDestinatarioListaRemessaService().recuperarPorId(Long.parseLong(idDestinatario));
				if (destinatarioListaRemessa != null) {
					receberDestinatarioSelecionado(destinatarioListaRemessa);
				} else {
					reportarAviso("Nenhum destinatário encontrado com o Código de Barras informado.");
				}
			} catch (NumberFormatException e) {
				reportarErro("Código de Barras de Destinatário inválido.");
			} catch (ServiceException e) {
				reportarErro("Código de Barras de Destinatário inválido.");
			}
		} else {
			reportarAviso("Nenhum Código de Barras de Destinatário informado.");
		}
	}

	public void pesquisarCEP(ActionEvent event) {
		try {
			flagExibirModalPesquisaCep = false;
			limparCamposSelecaoCep();
			String cepBusca = remessa.getCep().trim();
			if (cepBusca.isEmpty()) {
				flagExibirModalPesquisaCep = true;
			} else {
				List<VwEndereco> listaEnderecos = getVwEnderecoService().pesquisar(cepBusca);
				if (listaEnderecos != null && !listaEnderecos.isEmpty()) {
					VwEndereco endereco = listaEnderecos.get(0);
					if (endereco != null) {
						receberObjetoSelecionadoSelecaoCep(endereco);
					}
				} else {
					if (cepBusca.length() < 8) {
						reportarAviso("Por favor, preencha corretamente o campo CEP para a realização da pesquisa");
					} else {
						reportarAviso("CEP não localizado.");
					}
				}
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar CEP", e, LOG);
			LOG.error(e.getMessage(), e);
			LOG.error(e.getStackTrace()[0], e);
		}
	}

	public void relatorioExcel(ActionEvent event) {
		ExpedicaoRelatorioServiceLocal expedicaoRelatorioService = new ExpedicaoRelatorioServiceLocal();
		byte[] arquivo = expedicaoRelatorioService.criarRelatorioExcel(remessas, qtdRegistros, "rel_consulta_remessa");
		ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
		mandarRespostaDeDownloadDoArquivoExcel(input, "ConsultaListaRemessa");
	}

	@Override
	public String getNomeComponenteRerenderizarSelecaoCep() {
		return "panelRemessa";
	}

	@Override
	public void limparCamposSelecaoCep() {
		remessa.setBairro("");
		remessa.setLogradouro("");
		remessa.setCidade("");
		remessa.setUf("");
	}

	@Override
	public void receberObjetoSelecionadoSelecaoCep(VwEndereco endereco) {
		flagExibirModalPesquisaCep = false;
		try {
			Municipio municipio = getMunicipioService().buscarMunicipioCorrespondente(endereco.getSeqMunicipio());
			remessa.setBairro(endereco.getBairro() != null ? endereco.getBairro() : "");
			remessa.setCep(endereco.getCep());
			remessa.setLogradouro(endereco.getLogradouro());
			remessa.setCidade(municipio.getNome());
			remessa.setUf(endereco.getUf());
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar por CEP", e, LOG);
		}
	}

	@Override
	public void manipularErroAoExecutarPesquisaSelecaoCep(Exception exception) {
		reportarErro("Erro ao pesquisar por CEP", exception, LOG);
	}

	@Override
	public void cancelarPesquisa() {
		flagExibirModalPesquisaCep = false;
	}
	
	public String getNumeroAnoListaRemessa() {
		return numeroAnoListaRemessa;
	}
	
	public void setNumeroAnoListaRemessa(String numeroAnoListaRemessa) {
		this.numeroAnoListaRemessa = numeroAnoListaRemessa;
	}
	
	public boolean getListaReaberta(){
		return listaRemessas.getDataFinalizacao() != null;
	}
	
	@SuppressWarnings("deprecation")
	public void recuperarStringListaProcessosDoDocumento(ActionEvent evt){
		if(getCodigoTipoDocumento() != null && getRemessa() != null && getRemessa().getNumeroComunicacao() != null){
			try{			
				String[] numDoc = getRemessa().getNumeroComunicacao().split("/");
				List lista = getComunicacaoService().pesquisarProcessosPorDocumento(Long.parseLong(getCodigoTipoDocumento()), Long.parseLong(numDoc[0]), Long.parseLong(numDoc[1]));
				StringBuffer listaProcesso = new StringBuffer(); 
				if(lista != null && !lista.isEmpty()){
					for(Object reg : lista){
						if (lista.indexOf(reg) > 0) {
							listaProcesso.append("; ");
						}						
						Object[] registro = (Object[]) reg;
						listaProcesso.append((String) registro[1]).append(" ").append(((BigDecimal) registro[2]).longValue());
					}
					getRemessa().setVinculo(listaProcesso.toString());
				}
			}catch(Exception e){
			//	return null;
			}
		}
		//return null;
	}
}