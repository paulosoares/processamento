package br.jus.stf.processamento.page;

import org.springframework.stereotype.Component;

@Component
public class LoginPage {

	public LoginPage() {
		// TODO Auto-generated constructor stub
	}
	
	public final String ID_USUARIO 		= "username";
	public final String ID_SENHA		= "password";
	public final String XP_BT_ENTRAR	= "//input[@type='submit']";
	public final String XP_NOM_USUARIO	= "//table[@id='idPanelTituloPagina']/tbody/tr/td[2]/table/tbody/tr/td";
	public final String XP_BT_SAIR	 	= "//*[@id='modalPanelConfirmarSaidaContentTable']/tbody/tr/td/table/tbody/tr[2]/td/div/a[1]";
	
}
