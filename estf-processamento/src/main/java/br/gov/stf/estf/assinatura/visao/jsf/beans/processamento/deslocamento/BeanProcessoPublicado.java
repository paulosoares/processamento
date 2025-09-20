package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import br.gov.stf.estf.assinatura.relatorio.RelatorioAcordaoPublicado;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanProcessoPublicado extends AssinadorBaseBean {
	
	private static final long serialVersionUID = 1L;

	private Date dataPublicacao;
	private List<CheckableDataTableRowWrapper> processosPublicados; // lista da tela
	private List<RelatorioAcordaoPublicado> listaPublicados; // lista do relatório
	private String codigoSetorPublicacaoAcordao;
	private String deslocadoParaSecaoAcordao;

	private static final Object DATA_PUBLICACAO = new Object();
	private static final Object PROCESSOS_PUBLICADOS = new Object();
	private static final Object LISTA_PUBLICADOS = new Object();
	
	public static final Long COD_SETOR_PBULICACAO_DE_ACORDAOS = (long) 600000903;

	public BeanProcessoPublicado() {
		setDeslocadoParaSecaoAcordao("N");
		restaurarSessao();
	}
	
	public void atualizarSessao(){
		setAtributo(DATA_PUBLICACAO, getDataPublicacao());
		setAtributo(PROCESSOS_PUBLICADOS, getProcessosPublicados());
		setAtributo(LISTA_PUBLICADOS, getListaPublicados());
	}
	@SuppressWarnings("unchecked")
	public void restaurarSessao() {
		setDataPublicacao((Date) getAtributo(DATA_PUBLICACAO));
		setProcessosPublicados((List<CheckableDataTableRowWrapper>) getAtributo(PROCESSOS_PUBLICADOS));
		setListaPublicados((List<RelatorioAcordaoPublicado>) getAtributo(LISTA_PUBLICADOS));
	}
	
	public void pesquisar() {
		try {
			if (dataPublicacao == null) {
				reportarAviso("Favor informar uma data de publicação para efetuar a pesquisa.");
				return;
			}
			
			setCodigoSetorPublicacaoAcordao(COD_SETOR_PBULICACAO_DE_ACORDAOS.toString());
			
			if (getCodigoSetorPublicacaoAcordao() == null) {
				reportarAviso("Impossível recuperar o código do setor responsável pela publicação");
				return;
			}
		    
			SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");    
		    String dtPublicacao = out.format(dataPublicacao);   		
			List<RelatorioAcordaoPublicado> acordaosPublicados = getProcessamentoRelatorioService()
					.recuperarAcordaoPublicado(dtPublicacao, getCodigoSetorPublicacaoAcordao(), getDeslocadoParaSecaoAcordao());
			setProcessosPublicados(getCheckableDataTableRowWrapperList(acordaosPublicados));
			setListaPublicados(acordaosPublicados);
			atualizarSessao();
			if (acordaosPublicados == null || acordaosPublicados.size() == 0) {
				reportarAviso("Não foi encontrado nenhum registro com a data informada!");
			}
		} catch (Exception e) {
			reportarErro("Não foi possível recuperar as informações sobre os acórdãos: " + e.getMessage());
		}
	}
	
	public String imprimir() {
		try {
		    SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");    
		    String dtPublicacao = out.format(dataPublicacao);   		
			byte[] arquivo = null;

			setCodigoSetorPublicacaoAcordao(COD_SETOR_PBULICACAO_DE_ACORDAOS.toString());
			if (getCodigoSetorPublicacaoAcordao() == null) {
				reportarAviso("Impossível recuperar o código do setor responsável pela publicação");
				return "";
			}
			arquivo = getProcessamentoRelatorioService().criarRelatorioAcordaoPublicado(dtPublicacao, getListaPublicados(), 
					getCodigoSetorPublicacaoAcordao(), getDeslocadoParaSecaoAcordao());

			ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
			mandarRespostaDeDownloadDoArquivo(input);
			return "Ok";
		} catch (ServiceException e) {
			reportarErro("Erro ao imprimir o relatório: " + e.getMessage());
			return "";
		}
	}

	private void mandarRespostaDeDownloadDoArquivo(ByteArrayInputStream input) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-disposition", "attachment; filename=\"AcordaosPublicadosNaSecao.pdf\"");
		response.setContentType("application/pdf");
		try {
			IOUtils.copy(input, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		facesContext.responseComplete();
	}
	
	public void atualizaOpcaoDeslocamentoParaAcordao(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			setDeslocadoParaSecaoAcordao((String) event.getOldValue());
		}
		applyStateInHttpSession();
	}
	
	public void limpar(){
		setDataPublicacao(null);
		setListaPublicados(null);
		setProcessosPublicados(null);
		atualizarSessao();
		setDeslocadoParaSecaoAcordao("N");
	}

	
	public void pesquisar(ActionEvent evt){
		pesquisar();
	}
	
	public void limpar(ActionEvent evt){
		limpar();
	}
	
	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setProcessosPublicados(List<CheckableDataTableRowWrapper> processosPublicados) {
		this.processosPublicados = processosPublicados;
	}

	public List<CheckableDataTableRowWrapper> getProcessosPublicados() {
		return processosPublicados;
	}
	public List<RelatorioAcordaoPublicado> getListaPublicados() {
		return listaPublicados;
	}

	public void setListaPublicados(List<RelatorioAcordaoPublicado> listaPublicados) {
		this.listaPublicados = listaPublicados;
	}

	public void setCodigoSetorPublicacaoAcordao(String codigoSetorPublicacaoAcordao) {
		this.codigoSetorPublicacaoAcordao = codigoSetorPublicacaoAcordao;
	}

	public String getCodigoSetorPublicacaoAcordao() {
		return codigoSetorPublicacaoAcordao;
	}

	public String getDeslocadoParaSecaoAcordao() {
		return deslocadoParaSecaoAcordao;
	}

	public void setDeslocadoParaSecaoAcordao(String deslocadoParaSecaoAcordao) {
		this.deslocadoParaSecaoAcordao = deslocadoParaSecaoAcordao;
	}

}
