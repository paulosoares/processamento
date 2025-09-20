package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification
import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;

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

import br.jus.stf.processamento.page.ExpedientePage;
import br.jus.stf.processamento.page.PrincipalPage;
import br.jus.stf.processamento.util.Comandos;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class OGestorPodeEnviarDocumentoParaRevisão extends Comandos{

	@Autowired
	private TwistScenarioDataStore scenarioStore;
	
	@Autowired
	private ExpedientePage expediente;
	
	@Autowired
	private PrincipalPage principal;
	
	public OGestorPodeEnviarDocumentoParaRevisão(WebDriver browser) {
		this.browser = browser;
		System.out.println();
		System.out.println("#### Enviar para Revisão ####");
	}

	public void cliqueNoMenu(String menu) throws Exception {
		acessarRaizMenu(menu);
	}
	
	public void altereOSetorPara(String setor) throws Exception {
		acharClicarPorId(principal.ID_SETOR);
		selectComboValue(principal.ID_SETOR, setor);
		acharClicarPorId(principal.ID_BT_ALTERAR);
		verificaElementoPorXpath(principal.XP_MSG_INFO, "Setor alterado com sucesso!");
	}
	
	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		acessarRaizMenu(menu);
		acessarSubMenu(submenu);
	}

	public void verifiqueSeOsDocumentoForamExibidos() throws Exception {
		acharElementoPorId(expediente.ID_TABELA_RESULTADO);
	}

	public void selecioneUmDocumento() throws Exception {
		acharClicarPorXpath(expediente.XP_PRIM_REG_TABELA);
	}

	public void cliqueEmEncaminharParaRevisão() throws Exception {
		acharElementoPorId(expediente.ID_BT_REVISAR);
		acharClicarPorId(expediente.ID_BT_REVISAR);
	}

	public void informeOSetorDeDestino(String setor) throws Exception {
		selectComboValuePorXpath(expediente.XP_SELECT_SETOR, setor);
		acharClicarPorXpath(expediente.XP_BT_OK);
		aceitarAlert();                                                                                                 
	}

	public void verifiqueSeODocumentoFoiEncaminhadoComSucesso() throws Exception {
		verificaElementoPorXpath(principal.XP_MSG_INFO, "Documento(s) encaminhados(s) com sucesso!");
	}

}
