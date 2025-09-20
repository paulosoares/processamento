package br.jus.stf.processamento.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.processamento.util.Comandos;

@Component
public class PrincipalPage extends Comandos{

	public PrincipalPage() {
		// TODO Auto-generated constructor stub
	}
	
	public final String ID_SETOR 		= "seletorSetores";
	public final String ID_BT_ALTERAR 	= "altSetor";
	public final String XP_MSG_INFO		= "//*[@class='InfoMessage']";
	public final String XP_MSG_ALERTA 	= "//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span[@class='WarningMessage']";
	
	public void altereOSetorPara(String setor) throws Exception {
		acharClicarPorId(principal.ID_SETOR);
		selectComboValue(principal.ID_SETOR, setor);
		acharClicarPorId(principal.ID_BT_ALTERAR);
		verificaElementoPorXpath(principal.XP_MSG_INFO, "Setor alterado com sucesso!");
	}
}
