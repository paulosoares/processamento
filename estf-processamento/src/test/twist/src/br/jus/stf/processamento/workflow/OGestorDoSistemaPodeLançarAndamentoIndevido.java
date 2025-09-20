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

public class OGestorDoSistemaPodeLançarAndamentoIndevido extends Comandos {

	@Autowired 
	private TwistScenarioDataStore scenarioStore;
	
	@Autowired
	private AndamentoPage andamento;
	
	@Autowired
	private PrincipalPage principal;
	
	public OGestorDoSistemaPodeLançarAndamentoIndevido(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Andamento - IndevidarAndamento");
	}

	public void cliqueNaBorrachaDaColunaAçõesDeUmDosAndamentosExibidos()
			throws Exception {
		acharClicarPorXpath(andamento.XP_BORRACHA);
		Thread.sleep(1000);
	}

	public void informeAObservação(String observacao) throws Exception {
		acharClicarPorIdSendKeys(andamento.ID_OBS_IND, observacao);
		Thread.sleep(1000);
	}

	public void informeAObservaçãoInterna(String observacaoInterna) throws Exception {
		acharClicarPorIdSendKeys(andamento.ID_OBS_INT_IND, observacaoInterna);
		Thread.sleep(1000);
	}

	public void confirmeAAção() throws Exception {
		acharClicarPorXpath(andamento.XP_BT_CONF_AND_IND);
		Thread.sleep(1000);
	}

	public void verifiqueSeOAndamentoIndevidadoFoiRiscado() throws Exception {
		esperarElementoPresentePorXpath(principal.XP_MSG_ALERTA);
		verificaElementoPorXpath(andamento.XP_COD_AND_IND_TAB, "7800");
		Thread.sleep(1000);
	}

	public void verifiqueSeOAndamentoFoiRegistradoComAsObservações(String andamentoLancado)
			throws Exception {
		verificaElementoPorXpath(andamento.XP_AND_IND_TAB, andamentoLancado);
		verificaElementoPorXpath(andamento.XP_OBS_IND_TAB, "Teste da TI - obs. interna: indevidar andamento");
	}

}
