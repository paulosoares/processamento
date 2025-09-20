package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import br.gov.stf.estf.assinatura.relatorio.RelatorioProcessoInteresse;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.util.JurisdicionadoResult;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoInteresse;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanProcessoInteresse extends AssinadorBaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nomeAdvogado;
	private Long codigoAdvogado;
	private List<CheckableDataTableRowWrapper> listaProcessos;
	private String classeNumeroProcesso;
	private static final Object CLASSE_NUMERO_PROCESSO = new Object();
	private static final Object CODIGO_ADVOGADO = new Object();
	private static final Object NOME_ADVOGADO = new Object();
	private static final Object LISTA_PROCESSOS = new Object();
	private org.richfaces.component.html.HtmlDataTable tabelaProcessos;
	
	public BeanProcessoInteresse(){
		restaurarSessao();
	}
	
	private void atualizarSessao() {
		setAtributo(CLASSE_NUMERO_PROCESSO, classeNumeroProcesso);
		setAtributo(LISTA_PROCESSOS, listaProcessos);
		setAtributo(NOME_ADVOGADO, nomeAdvogado);
		setAtributo(CODIGO_ADVOGADO, codigoAdvogado);
	}
	
	@SuppressWarnings("unchecked")
	private void restaurarSessao() {
		setClasseNumeroProcesso((String) getAtributo(CLASSE_NUMERO_PROCESSO));
		if (getAtributo(LISTA_PROCESSOS) == null) {
			setAtributo(LISTA_PROCESSOS, new ArrayList<CheckableDataTableRowWrapper>());
		}
		setListaProcessos((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_PROCESSOS));
		setNomeAdvogado((String) getAtributo(NOME_ADVOGADO));
		setCodigoAdvogado((Long) getAtributo(CODIGO_ADVOGADO));
	}
	
	private String processoJaAdicionado(Processo processo, List<CheckableDataTableRowWrapper> processos) {
		for (CheckableDataTableRowWrapper checkable: processos) {
			Processo item = (Processo) checkable.getWrappedObject();
			if (item.getId().equals(processo.getId())) {
				return "Processo já adicionado.";
			}
		}
		return null;
	}
	
	public void incluirProcessoNaLista(ActionEvent evt) throws Exception{
		try {
			if (classeNumeroProcesso == null || classeNumeroProcesso.trim().equals("")) {
				return;
			}
			
			Long numeroProcessoInteresse = ProcessoParser.getNumero(classeNumeroProcesso);
			String classeProcessoInteresse = ProcessoParser.getSigla(classeNumeroProcesso);
			
			Processo processoInteresse = getProcessoService().recuperarProcesso(classeProcessoInteresse, numeroProcessoInteresse);
			if (processoInteresse == null) {
				reportarAviso("Processo não existe.");
				return;
			}
			if (codigoAdvogado == null) {
				reportarAviso("Favor informar um advogado.");
				return;
			}
			Jurisdicionado advogado = getJurisdicionadoService().recuperarPorId(codigoAdvogado);

			if (getProcessoInteresseService().recuperarProcessosInteresse(advogado, processoInteresse) != null) {
				reportarAviso("Processo de interesse já existe para o advogado selecionado");
				return;
			}
			
			CheckableDataTableRowWrapper chk = new CheckableDataTableRowWrapper(processoInteresse);
			if (listaProcessos == null) {
				listaProcessos = new ArrayList<CheckableDataTableRowWrapper>();
			}
			String msgCritica = processoJaAdicionado(processoInteresse, listaProcessos);
			if (msgCritica != null) {
				reportarAviso(msgCritica);
				return;
			}
			
			chk.setChecked(true);
			listaProcessos.add(0, chk);
			salvarProcessoInteresse(advogado, processoInteresse);
			atualizarSessao();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void salvarProcessoInteresse(Jurisdicionado advogado, Processo processo) throws ServiceException {
		ProcessoInteresse novoProcessoInteresse = new ProcessoInteresse();
		novoProcessoInteresse.setAdvogado(advogado);
		novoProcessoInteresse.setProcesso(processo);
		getProcessoInteresseService().salvar(novoProcessoInteresse);
		
	}
	
	public void limparTelaAction(ActionEvent evt){
		setNomeAdvogado("");
		setClasseNumeroProcesso("");
		setCodigoAdvogado(null);
		setListaProcessos(new LinkedList<CheckableDataTableRowWrapper>());
		atualizarSessao();
	}
	
	public boolean getIsApenso() {
		return false;
	}
	
	public List<JurisdicionadoResult> pesquisarAdvogado(Object value) throws ServiceException {
		List<JurisdicionadoResult> listaResult;
		listaResult = getJurisdicionadoService().pesquisarResult(value);
		return listaResult;
	}
	
	
	public void imprimirMovimentada(ActionEvent evt) {
		try {
			if (codigoAdvogado == null) {
				reportarAviso("Favor selecionar um advogado!");
				return;
			}
			Jurisdicionado advogado = getJurisdicionadoService().recuperarPorId(codigoAdvogado);
			if (!getProcessoInteresseService().existeMovimentada(advogado)) {
				reportarAviso("Não houve movimentada para os Processos de Interesse deste advogado.");
				return;
			}
			byte[] arquivo = getProcessamentoRelatorioService().criarRelatorioProcessoInteresse(codigoAdvogado, null);
			ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
			mandarRespostaDeDownloadDoArquivo(input, "Movimentada_"+ codigoAdvogado +".pdf");
		
		} catch (Exception e) {
			reportarErro("Ocorreu um erro na impressão do relatório de processos de interesse. " + e.getMessage());
		}
		
	}
	
	public void imprimirProcessoInteresse(ActionEvent evt) {
		try {
			if (codigoAdvogado == null) {
				reportarAviso("Favor selecionar um advogado!");
				return;
			}
			Jurisdicionado advogado = getJurisdicionadoService().recuperarPorId(codigoAdvogado);
			List<RelatorioProcessoInteresse> dadosRelatorio = new ArrayList<RelatorioProcessoInteresse>();
			for (CheckableDataTableRowWrapper processoInteresse: listaProcessos) {
				RelatorioProcessoInteresse dadoRelatorio = new RelatorioProcessoInteresse();
				dadoRelatorio.setDataAndamento(null);
				dadoRelatorio.setDescricaoAndamento(null);
				dadoRelatorio.setIdAdvogado(advogado.getId());
				dadoRelatorio.setNomeAdvogado(advogado.getNome());
				dadoRelatorio.setOab(advogado.getOab());
				Processo processo = (Processo) processoInteresse.getWrappedObject();
				dadoRelatorio.setIdentificacaoProcesso(processo.getSiglaClasseProcessual() + " " + 
						                               processo.getNumeroProcessual() );
				dadosRelatorio.add(dadoRelatorio);
			}
			
			byte[] arquivo = getProcessamentoRelatorioService().criarRelatorioProcessoInteresseSemMovimentada(codigoAdvogado, dadosRelatorio);
			ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
			mandarRespostaDeDownloadDoArquivo(input, "ProcessoInteresse_"+ codigoAdvogado +".pdf");
		
		} catch (Exception e) {
			reportarErro("Ocorreu um erro na impressão do relatório de processos de interesse. " + e.getMessage());
		}
		
	}
	
	private void mandarRespostaDeDownloadDoArquivo(ByteArrayInputStream input, String nomeRelatorio) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-disposition", "attachment; filename=\"" + nomeRelatorio + "\"");
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
	
	public void atualizarSessao(ActionEvent evt) {
		atualizarSessao();
	}
	
	public void recuperarProcessosDeInteresse() throws ServiceException {
		try {
			List<Processo> processos = new ArrayList<Processo>();
			
			if (codigoAdvogado == null) {
				reportarAviso("Advogado não localizado.");
				return;
			}
			Jurisdicionado advogado = getJurisdicionadoService().recuperarPorId(codigoAdvogado);
			List<ProcessoInteresse>  processosDeInteresse = getProcessoInteresseService().recuperarProcessosInteresse(advogado);
			
			for (ProcessoInteresse processoInteresse: processosDeInteresse) {
				processos.add(processoInteresse.getProcesso());
			}
			listaProcessos.clear();
			listaProcessos.addAll(getCheckableDataTableRowWrapperList(processos));
			atualizarSessao();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public String getSituacao(){
		try {
			CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaProcessos.getRowData();
			Jurisdicionado advogado = getJurisdicionadoService().recuperarPorId(codigoAdvogado);
			// recuperar o processo de interesse do processo selecionado
			ProcessoInteresse processoInteresse = getProcessoInteresseService().recuperarProcessosInteresse(advogado, (Processo)chkDataTable.getWrappedObject());
			if (processoInteresse == null) {
				return "Pendente";
			}
			return "Salvo";
		} catch (Exception e) {
			//reportarAviso("Erro na recuperação da situação.");
			return "";
		}
	}
	
	public void removerProcessoInteresseAction(ActionEvent evt) throws Exception{
		if (codigoAdvogado == null) {
			reportarAviso("Favor selecionar um advogado!");
			return;
		}
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaProcessos.getRowData();
		listaProcessos.remove(chkDataTable);
		Jurisdicionado advogado = getJurisdicionadoService().recuperarPorId(codigoAdvogado);
		// recuperar o processo de interesse do processo selecionado
		ProcessoInteresse processoInteresse = getProcessoInteresseService().recuperarProcessosInteresse(advogado, (Processo)chkDataTable.getWrappedObject());
		// se existir excluir também no banco
		if (processoInteresse != null) {
			getProcessoInteresseService().excluir(processoInteresse);
		}
		atualizarSessao();
	}
	
/*	public void salvar(ActionEvent evt) {
		// recuperar o advogado do banco
		if (codigoAdvogado == null) {
			reportarAviso("Favor selecionar um advogado.");
		}
		if (listaProcessos.size() == 0) {
			reportarAviso("Favor informar o(s) processo(s).");
		}
		try {
			Jurisdicionado advogado = getJurisdicionadoService().recuperarPorId(codigoAdvogado);
			List<Processo> processos = new ArrayList<Processo>();
			for (CheckableDataTableRowWrapper chkProcessoInteresse: listaProcessos) {
				if (chkProcessoInteresse.getChecked()) {
					Processo entityProcesso = (Processo) chkProcessoInteresse.getWrappedObject();
					processos.add(entityProcesso);
				}
			}
			getProcessoInteresseService().salvarVariosProcessosInteresse(processos, advogado);
			reportarInformacao("Processo(s) de interesse salvo(s) com sucesso.");
		} catch (Exception e) {
			reportarErro("Não foi possível salvar o(s) pocesso(s) de interesse.");
		}
	}
*/
	public void setNomeAdvogado(String nomeAdvogado) {
		this.nomeAdvogado = nomeAdvogado;
	}

	public String getNomeAdvogado() {
		return nomeAdvogado;
	}

	public void setCodigoAdvogado(Long codigoAdvogado) {
		this.codigoAdvogado = codigoAdvogado;
		setAtributo(CODIGO_ADVOGADO, codigoAdvogado);
	}

	public Long getCodigoAdvogado() {
		return codigoAdvogado;
	}

	public void setListaProcessos(List<CheckableDataTableRowWrapper> listaProcessos) {
		this.listaProcessos = listaProcessos;
	}

	public List<CheckableDataTableRowWrapper> getListaProcessos() {
		return listaProcessos;
	}

	public void setClasseNumeroProcesso(String classeNumeroProcesso) {
		this.classeNumeroProcesso = classeNumeroProcesso;
	}

	public String getClasseNumeroProcesso() {
		return classeNumeroProcesso;
	}

	public void setTabelaProcessos(org.richfaces.component.html.HtmlDataTable tabelaProcessos) {
		this.tabelaProcessos = tabelaProcessos;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaProcessos() {
		return tabelaProcessos;
	}

}
