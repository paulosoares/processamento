package br.jus.stf.processamento.contexto;

// JUnit Assert framework can be used for verification

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.processamento.page.LoginPage;
import br.jus.stf.processamento.util.Comandos;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class LogadoComo extends Comandos {

	@Autowired 
	private TwistScenarioDataStore scenarioStore;
	
	@Autowired
	private LoginPage login;
	
	private String nome;

	public LogadoComo(WebDriver browser) {
		this.browser = browser;
	}
	
	public void setUp(String userName) throws Exception {
		//This method is executed before the scenario execution starts.
		
		if (userName.equals("gestor")) {
			userName = "elianes"; //elianes
			nome = "ELIANE NESTOR DA SILVA SANTOS";
		} else if (userName.equals("administrador")) {
			userName = "adauto";
			nome = "ADAUTO CIDREIRA NETO";
		} else {
			System.out.println("----------------");
			System.out.println("O perfil informado não foi encontrado.");
			System.out.println("----------------");
		}

		// Primeiro acesso
		if (!browser.getCurrentUrl().contains("processamento")) {
			
			System.out.println("*** Usuario: " + nome.toUpperCase());
			browser.navigate().to("http://sistemast/processamento");
			acharClicarPorIdSendKeys(login.ID_USUARIO, userName);
			acharClicarPorIdSendKeys(login.ID_SENHA, userName);
			acharClicarPorXpath(login.XP_BT_ENTRAR);
			
		} else { //Autenticação com outro perfil
			
			WebElement usuarioLogado = acharPorXpathElemento(login.XP_NOM_USUARIO);
			
			//Verifica se o perfil necessário está logado
			if (!usuarioLogado.getText().trim().startsWith(nome.toUpperCase().trim())){
				
				//Sair da aplicação
				acessarRaizMenu("Sair");
				acharClicarPorXpath(login.XP_BT_SAIR);
				Thread.sleep(5000);
				
				//Fazer login
				acharClicarPorIdSendKeys(login.ID_USUARIO, userName);
				acharClicarPorIdSendKeys(login.ID_SENHA, userName);
				acharClicarPorXpath(login.XP_BT_ENTRAR);
				
				System.out.println("*** Novo usuario: " + nome.toUpperCase());
			}
		}
	}


	public void tearDown() throws Exception {
		//This method is executed after the scenario execution finishes.
	}

	public void tearDown(String string1) throws Exception {
	
	}

}
