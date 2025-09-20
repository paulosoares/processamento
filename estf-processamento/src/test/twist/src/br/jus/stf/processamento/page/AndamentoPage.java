package br.jus.stf.processamento.page;

import org.springframework.stereotype.Component;

@Component
public class AndamentoPage {
	
	public AndamentoPage() {
		// TODO Auto-generated constructor stub
	}
	
	//LANÇAR ANDAMENTO
	public final String ID_PROCESSO			= "idProcesso";
	public final String ID_MERITO_PROC      = "sgPesquisaProcesso:suggest";
	public final String ID_ANDAMENTO       	= "idAndamento";
	public final String ID_PRIM_ANDAMENTO  	= "sgPesquisaAndamento:suggest";
	public final String ID_OBSERVACAO  		= "observacao";
	public final String ID_OBS_INTERNA 		= "observacaoInterna";
	public final String ID_BT_SALVAR   		= "btNovoAndamento";
	public final String XP_CODIGO_ANDAMENTO = "//table[@id='tableAndamentoProcesso']/tbody/tr[1]/td[6]/span";
	//INDEVIDAR ANDAMENTO
	public final String XP_BORRACHA			= "//table[@id='tableAndamentoProcesso']/tbody/tr[1]/td[9]/table/tbody/tr/td[2]/a/img[contains(@title,'Lançar andamento indevido')]";
	public final String ID_OBS_IND		    = "idObservacaoIndevido";
	public final String ID_OBS_INT_IND		= "idObservacaoInternaIndevido";
	public final String XP_BT_CONF_AND_IND  ="//table[@id='modalPanelInformacoesLancamentoIndevidoContentTable']/tbody/tr[2]/td/form/div[2]/input[@value='Ok']";
	public final String XP_COD_AND_IND_TAB  = "//table[@id='tableAndamentoProcesso']/tbody/tr[2]/td[3]/span[contains(@style,'line-through')]";
	public final String XP_AND_IND_TAB		= "//table[@id='tableAndamentoProcesso']/tbody/tr[1]/td[4]/span";
	public final String XP_OBS_IND_TAB		= "//table[@id='tableAndamentoProcesso']/tbody/tr[1]/td[7]/span";
	//EDITAR OBSERVAÇÃO
	public final String XP_LAPIS   			= "//table[@id='tableAndamentoProcesso']/tbody/tr[1]/td[9]/table/tbody/tr/td[1]/a/img[contains(@title,'Editar as informações do andamento do processo')]";
	public final String ID_OBS_EDITAR		= "idObservacao";
	public final String ID_OBS_INT_EDITAR	= "idObservacaoInterna";
	public final String XP_BT_CONF_EDITAR	= "//table[@id='modalPanelEditarAndamentoContentTable']/tbody/tr[2]/td/form/div[2]/input[@value='Ok']";
	public final String XP_OBS_TAB 			= "//table[@id='tableAndamentoProcesso']/tbody/tr[1]/td[7]/span";
	//RETIRAR ANDAMENTO INDEVIDO
	public final String XP_BT_CANC_IND		= "//table[@id='tableAndamentoProcesso']/tbody/tr[1]/td[9]/table/tbody/tr/td[2]/a/img[contains(@title,'Desfazer andamento indevido')]";
	
	
}
