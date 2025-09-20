package br.jus.stf.processamento.page;

import org.springframework.stereotype.Component;

@Component
public class ExpedientePage {
	
	public ExpedientePage() {
		// TODO Auto-generated constructor stub
	}
	
	public final String ID_TABELA_RESULTADO	= "tableDocumentos";
	public final String XP_PRIM_REG_TABELA  = "//table[@id='tableDocumentos']/tbody/tr[1]/td[1]/input";
	public final String ID_BT_REVISAR      	= "botaoRevisar";
	public final String XP_SELECT_SETOR		= "//*[@id='modalPanelRevisarDocumentoContentTable']/tbody/tr[2]/td/form/div[1]/span/select";
	public final String XP_BT_OK       		= "//*[@id='modalPanelRevisarDocumentoForm']/div[2]/input[1]";

}
