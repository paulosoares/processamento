package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification

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

public class OGestorPodeManterOsProcessoDeInteresseDosAdvogados {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;

	public OGestorPodeManterOsProcessoDeInteresseDosAdvogados(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Consulta - ProcessoDeInteresse");
	}

	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).click().build().perform();	
	}
	
	public void digiteAClasseEONúmeroDoProcesso(String classeNumero)
			throws Exception {
		browser.findElement(By.id("idProcesso")).sendKeys(classeNumero);	
	}
	
	public void infomeOAdvogado(String codigo) throws Exception {
		browser.findElement(By.id("itAdvogado")).sendKeys(codigo + " ");
		WebDriverWait esperaPesquisa = new WebDriverWait(browser, 5);
		esperaPesquisa.ignoring(ElementNotVisibleException.class);
		esperaPesquisa.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaPesquisa.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sbAdvogado:suggest']/tbody/tr[1]"))).click();
		
		//browser.findElement(By.id("itAdvogado")).sendKeys(Keys.ARROW_DOWN);
		//browser.findElement(By.id("itAdvogado")).sendKeys(Keys.ENTER);
	}

	public void verifiqueSeOsProcessosDeInteresseForamExibidos() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='tabProcesso']")));
		WebElement tabelaProcesso = browser.findElement(By.xpath("//span[@id='pnlItemProcesso']/span"));
		verifyEquals("Processo(s) para carga", tabelaProcesso.getText().trim());
	}

	public void cliqueNoBotãoAdicionar() throws Exception {
		browser.findElement(By.id("btnIncluirProcesso")).click();
		Thread.sleep(2000);
	}

	public void verifiqueSeOProcessoFoiIncluido() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='tabProcesso']")));
		WebElement classeProcesso = browser.findElement(By.xpath("//table[@id='tabProcesso']/tbody/tr[1]/td[1]/table/tbody/tr/td[1]/span"));
		WebElement numeroProcesso = browser.findElement(By.xpath("//table[@id='tabProcesso']/tbody/tr[1]/td[1]/table/tbody/tr/td[2]/span"));
		System.out.println("*** Processo incluido: " + classeProcesso.getText() + numeroProcesso.getText());
		verifyEquals("ADC8", classeProcesso.getText().trim() + numeroProcesso.getText().trim());
	}

	public void removaOProcesso() throws Exception {
		browser.findElement(By.xpath("//table[@id='tabProcesso']/tbody/tr[1]/td[2]/input")).click();
		Thread.sleep(2000);
	}

	public void verifiqueSeOProcessoFoiRemovido() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 20);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='tabProcesso']")));
		WebElement classeProcesso = browser.findElement(By.xpath("//table[@id='tabProcesso']/tbody/tr[1]/td[1]/table/tbody/tr/td[1]/span"));
		WebElement numeroProcesso = browser.findElement(By.xpath("//table[@id='tabProcesso']/tbody/tr[1]/td[1]/table/tbody/tr/td[2]/span"));
		System.out.println("*** Proximo processo: " + classeProcesso.getText() + numeroProcesso.getText());
		verifyEquals("AI212502", classeProcesso.getText().trim() + numeroProcesso.getText().trim());
	}

}
