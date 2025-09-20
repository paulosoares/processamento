package br.gov.stf.estf.assinatura.visao.jsf.beans;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.entidade.configuracao.AlertaSistema;


public class BeanMensagemAtualizacao extends AssinadorBaseBean {

	private static final long serialVersionUID = 1420934043957382929L;
	private static final Log LOG = LogFactory.getLog(BeanMensagemAtualizacao.class);
	private String mensagemAtualizacaoSistema;


	public BeanMensagemAtualizacao() {
		carregarMensagemAtualizacaoSistema();
	}
	
	public void carregarMensagemAtualizacaoSistema(){
		try {
    		List<AlertaSistema> listaAlertaSistema = getAlertaSistemaService().recuperarValor("PROCESSAMENTO", "mensagem.atualizacao");
    		
    		Date dataAtual = new Date();
    		if(listaAlertaSistema != null && listaAlertaSistema.size()>0){
    			if(listaAlertaSistema.get(0).getDataInicial() != null && listaAlertaSistema.get(0).getDataFinal() != null){
    				if(dataAtual.after(listaAlertaSistema.get(0).getDataInicial()) && dataAtual.before(listaAlertaSistema.get(0).getDataFinal())){
    					mensagemAtualizacaoSistema = listaAlertaSistema.get(0).getValor();
    				}
    			} else if(listaAlertaSistema.get(0).getDataInicial() != null && listaAlertaSistema.get(0).getDataFinal() == null){
    				if(dataAtual.after(listaAlertaSistema.get(0).getDataInicial())){
    					mensagemAtualizacaoSistema = listaAlertaSistema.get(0).getValor();
    				}
    			}
    		}
		} catch (Exception e) {
			LOG.error("Falha na conexão com o banco de dados.", e);
		}
	}
	
	public String getMensagemAtualizacaoSistema() {
		return mensagemAtualizacaoSistema;
	}
	
	public String getMostrarMensagemAtualizacaoSistema() {
		String str = mensagemAtualizacaoSistema;
		mensagemAtualizacaoSistema = "";
		return str;
	}
	
	
}
