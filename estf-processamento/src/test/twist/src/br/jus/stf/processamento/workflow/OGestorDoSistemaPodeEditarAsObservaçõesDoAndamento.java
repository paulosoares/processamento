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

public class OGestorDoSistemaPodeEditarAsObserva��esDoAndamento extends Comandos{

	@Autowired 
	private TwistScenarioDataStore scenarioStore;
	
	@Autowired
	private AndamentoPage andamento;
	
	@Autowired
	private PrincipalPage principal;

	public OGestorDoSistemaPodeEditarAsObserva��esDoAndamento(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Andamento - EditarObservacao");
	}

	public void clicarNoL�pisDaColunaA��esDeUmDosAndamentosExibidos()
			throws Exception {
		acharClicarPorXpath(andamento.XP_LAPIS);
	}

	public void informeAObserva��o(String observacao) throws Exception {
		acharClicarPorIdSendKeys(andamento.ID_OBS_EDITAR, observacao);
	}

	public void informeAObserva��oInterna(String observacaoInterna) throws Exception {
		acharClicarPorIdSendKeys(andamento.ID_OBS_INT_EDITAR, observacaoInterna);
	}

	public void confirmeAA��o() throws Exception {
		acharClicarPorXpath(andamento.XP_BT_CONF_EDITAR);
		Thread.sleep(2000);
	}

	public void verifiqueSeOAndamentoFoiEditadoComAsNovasObserva��es()
			throws Exception {
		verificaElementoPorXpath(andamento.XP_OBS_TAB, "Teste da TI - obs. interna: edi��o da observa��o");
	}

}
