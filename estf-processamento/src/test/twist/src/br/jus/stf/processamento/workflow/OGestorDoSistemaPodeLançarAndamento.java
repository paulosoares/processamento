package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification
import br.jus.stf.processamento.page.AndamentoPage;
import br.jus.stf.processamento.page.PrincipalPage;
import br.jus.stf.processamento.util.Comandos;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class OGestorDoSistemaPodeLan�arAndamento extends Comandos {

	@Autowired 
	private TwistScenarioDataStore scenarioStore;
	
	@Autowired
	private AndamentoPage andamento;
	
	@Autowired
	private PrincipalPage principal;

	public OGestorDoSistemaPodeLan�arAndamento(WebDriver browser) {
		this.browser = browser;
	}

	public void altereOSetorPara(String setor) throws Exception {
		alterarSetor(setor);
	}

	public void cliqueNoMenu(String menu) throws Exception {
		acessarRaizMenu(menu);
	}
	
	public void cliqueNoMenuENaOp��o(String menu, String submenu) throws Exception {
		acessarRaizMenu(menu);
		acessarSubMenu(submenu);
	}

	public void digiteAClasseEON�meroDoProcesso(String classeNumero) throws Exception {
		acharClicarPorIdSendKeys(andamento.ID_PROCESSO, classeNumero);	
	}

	public void escolhaOM�ritoDoProcesso() throws Exception {
		acharClicarPorId(andamento.ID_MERITO_PROC);			
	}

	public void informeOC�digoDoAndamento(String codigoAndamento) throws Exception {
		System.out.println("");
		System.out.println("#### Andamento - LancarAndamento");
		acharClicarPorIdSendKeys(andamento.ID_ANDAMENTO, codigoAndamento);
		Thread.sleep(1000);
		acharClicarPorId(andamento.ID_PRIM_ANDAMENTO);
		Thread.sleep(1000);
	}

	public void informeAObserva��o(String observacao) throws Exception {
		acharClicarPorIdSendKeys(andamento.ID_OBSERVACAO, observacao);
	}

	public void informeAObserva��oInterna(String observacaoInterna) throws Exception {
		acharClicarPorIdSendKeys(andamento.ID_OBS_INTERNA, observacaoInterna);
	}

	public void salveOAndamento() throws Exception {
		acharClicarPorId(andamento.ID_BT_SALVAR);
		Thread.sleep(2000);
	}

	public void verifiqueSeOAndamentoFoiLan�adoComSucesso() throws Exception {	
		verificaElementoPorXpath(andamento.XP_CODIGO_ANDAMENTO, "Teste da TI - obs.: lan�ar andamento");
	}
	
}
