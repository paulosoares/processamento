package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification
import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.processamento.page.AndamentoPage;
import br.jus.stf.processamento.page.PrincipalPage;
import br.jus.stf.processamento.util.Comandos;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class OGestorDoSistemaPodeDesfazerAndamentoIndevido extends Comandos{

	@Autowired 
	private TwistScenarioDataStore scenarioStore;
	
	@Autowired
	private AndamentoPage andamento;
	
	@Autowired
	private PrincipalPage principal;
	
	public OGestorDoSistemaPodeDesfazerAndamentoIndevido(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Andamento - IndevidarAndamento");
	}

	public void cliqueNaBorrachaDaColunaAçõesDoAndamento7700CriadoAnteriormente()
			throws Exception {
		acharClicarPorXpath(andamento.XP_BT_CANC_IND);
		Thread.sleep(2000);
	}

	public void verifiqueSeOAndamentoIndevidadoFoiCanceladoComSucesso()
			throws Exception {
		verificaElementoPorXpath(principal.XP_MSG_ALERTA, "Andamento indevido cancelado com sucesso!"); 
	}

}
