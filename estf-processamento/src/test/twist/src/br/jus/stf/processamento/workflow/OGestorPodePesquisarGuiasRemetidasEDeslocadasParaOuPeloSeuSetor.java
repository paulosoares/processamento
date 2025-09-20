package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification
import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class OGestorPodePesquisarGuiasRemetidasEDeslocadasParaOuPeloSeuSetor {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;

	public OGestorPodePesquisarGuiasRemetidasEDeslocadasParaOuPeloSeuSetor(
			WebDriver browser) {
		this.browser = browser;
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


	public void informeAGuia(String numeroGuia) throws Exception {
		browser.findElement(By.id("itNumeroGuia")).sendKeys(numeroGuia);
	}

	public void informeOAno(String anoGuia) throws Exception {
		browser.findElement(By.id("itAnoGuia")).clear();
		browser.findElement(By.id("itAnoGuia")).sendKeys(anoGuia);
	}
	
	public void selecioneAOpçãoNoCampo(String string1, String string2)
			throws Exception {
		browser.findElement(By.id("chkOrigemLotacaoUsuario")).click();
	}

	public void cliqueEmPesquisar() throws Exception {
		browser.findElement(By.id("btnPesquisar")).click();
	}

	public void verifiqueSeOResultadoFoiExibido() throws Exception {
		Thread.sleep(5000);
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabelaGuia")));
		WebElement guia = browser.findElement(By.xpath("//*[@id='tabelaGuia']/tbody/tr/td[1]"));
		verifyEquals("10794 / 2013", guia.getText().trim());
	}


}
