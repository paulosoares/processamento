package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.ServiceException;

public class BeanAutorizarBaixaProcesso extends AssinadorBaseBean {
	/**
	 * @throws ServiceException 
	 * 
	 */
	
	public BeanAutorizarBaixaProcesso() throws ServiceException {
//		setProcessoSelecionado(new Processo());
//		setIdentificacaoProcesso (null);
//		setChkAutorizadoBaixa(null);
		restaurarSessao();
	}
	
	private static final long serialVersionUID = 1L;
	
	//private List<ObjetoIncidente<?>> incidentes;
//	@KeepStateInHttpSession
	private String identificacaoProcesso;
	private Processo processoSelecionado;
//	private String baixa;
	private Boolean chkAutorizadoBaixa;

	private static final Object IDENTIFICACAO_PROCESSO = new Object();
	private static final Object PROCESSO_SELECIONADO = new Object();
	private static final Object AUTORIZADO_BAIXA = new Object();
	
	private void restaurarSessao() throws ServiceException {
		setIdentificacaoProcesso((String) getAtributo(IDENTIFICACAO_PROCESSO));
		setProcessoSelecionado((Processo) getAtributo(PROCESSO_SELECIONADO));
		setChkAutorizadoBaixa((Boolean) getAtributo(AUTORIZADO_BAIXA));
	}
	
	private void atualizaSessao() {
		if (identificacaoProcesso != null && processoSelecionado != null && chkAutorizadoBaixa != null) {
			setAtributo(IDENTIFICACAO_PROCESSO, identificacaoProcesso);
			setAtributo(PROCESSO_SELECIONADO, processoSelecionado);
			setAtributo(AUTORIZADO_BAIXA, chkAutorizadoBaixa);
		}
	}

	public List pesquisaSuggestionBox(Object suggest) {
		try {
			List<Processo> processos = getProcessoService().pesquisarProcesso(suggest.toString().toUpperCase());
			processos.removeAll(Collections.singletonList(null));
			
			return processos;
		} catch (Exception e) {
			reportarErro("Erro na pesquisa processual. ", e.getMessage());
			e.printStackTrace();
			return new ArrayList<Processo>();
		}
	}
	
	public void salvarAutorizacao(ActionEvent evt) {
		try {
			if (processoSelecionado == null) {
				Long numProc = ProcessoParser.getNumero(identificacaoProcesso);
				String classProc = ProcessoParser.getSigla(identificacaoProcesso);
				processoSelecionado = getProcessoService().recuperarProcesso(classProc, numProc);
			}
			if (processoSelecionado == null) {
				reportarAviso("Não foi possível recuperar o processo selecionado.");
				return;
			}
			
			getProcessoService().alterarBaixaProcesso(processoSelecionado, chkAutorizadoBaixa);
			
			reportarInformacao("Operação realizada com sucesso!");
		} catch (Exception e) {
			reportarErro("A atualização não foi efetuada. Erro identificado: " + e.getMessage());
		}
	}

	// gets/sets
	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		this.identificacaoProcesso = identificacaoProcesso;
		atualizaSessao();
	}

	public String getIdentificacaoProcesso() {
		return identificacaoProcesso;
	}

	public void setProcessoSelecionado(Processo processoSelecionado) throws ServiceException {
		if (processoSelecionado == null) return;
		// refazer a pesquisa para atualizar o cache do hibernate
		Processo processo = getProcessoService().recuperarPorId(processoSelecionado.getId());
		//
		this.processoSelecionado = processo;
		if (this.processoSelecionado != null) {
			setChkAutorizadoBaixa (this.processoSelecionado.getBaixa());
		}
		atualizaSessao();
		
	}

	public Processo getProcessoSelecionado() {
		return processoSelecionado;
	}

	public void setChkAutorizadoBaixa(Boolean chkAutorizadoBaixa) {
		this.chkAutorizadoBaixa = chkAutorizadoBaixa;
	}

	public Boolean getChkAutorizadoBaixa() {
		return chkAutorizadoBaixa;
	}

}
