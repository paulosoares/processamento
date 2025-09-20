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

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class OGestorPodeEnviarODocumentoParaAssinatura {

	private WebDriver browser;

	@Autowired
	private TwistScenarioDataStore scenarioStore;

	public OGestorPodeEnviarODocumentoParaAssinatura(WebDriver browser) {
		this.browser = browser;
		System.out.println();
		System.out.println("#### Enviar para Assinar ####");
	}

	public void cliqueNoMenu(String menu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		acaoMenu.moveToElement(raizMenu).click().build().perform();	
	}
	
	public void altereOSetorPara(String setor) throws Exception {
		Thread.sleep(5000);
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.id("seletorSetores")));
		browser.findElement(By.id("seletorSetores")).click();
		browser.findElement(By.xpath("//*[@id='seletorSetores']/option[@value='"+ setor +"']")).click();
		browser.findElement(By.id("altSetor")).click();
		WebElement resultadoAlteracaoSetor = browser.findElement(By.xpath("//*[@class='InfoMessage']"));
		verifyEquals("Setor alterado com sucesso!", resultadoAlteracaoSetor.getText().trim());
	}
	
	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).click().build().perform();	
	}	
	
	public void verifiqueSeOsDocumentoForamExibidos() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='tableDocumentos']")));
		esperaResultado.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@id='tableDocumentos']/tbody/tr[1]/td[1]/input")));
	}

	public void selecioneUmDocumento() throws Exception {
		Thread.sleep(5000);
		browser.findElement(By.xpath("//table[@id='tableDocumentos']/tbody/tr[1]/td[1]/input")).click();
	}

	public void cliqueEmEncaminharParaAssinatura() throws Exception {
		Thread.sleep(2000);
		browser.findElement(By.id("botaoEncaminhar")).click();
	}

	public void informeOSetorDeDestino(String setor) throws Exception {
		Thread.sleep(2000);

		browser.findElement(By.xpath("//*[@id='modalPanelEncaminharDocumentoContentTable']/tbody/tr[2]/td/form/div[2]/span/select")).click();
		browser.findElement(By.xpath("//*[@id='modalPanelEncaminharDocumentoContentTable']/tbody/tr[2]/td/form/div[2]/span/select/option[@value='"+ setor +"']")).click();
		browser.findElement(By.xpath("//*[@id='modalPanelEncaminharDocumentoContentTable']/tbody/tr[2]/td/form/div[3]/span[1]/input")).click();
	}

	public void verifiqueSeODocumentoFoiEncaminhadoComSucesso() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span[@class='InfoMessage']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span[@class='InfoMessage']"));
		System.out.println("*** verifiqueSeODocumentoFoiEncaminhadoComSucesso: " +mensagem.getText().trim());
		verifyEquals("Documento(s) encaminhados(s) com sucesso!", mensagem.getText().trim());
	}
}
