package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification

import static com.thoughtworks.twist.core.execution.TwistVerification.verify;
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

import br.jus.stf.processamento.service.ProcessamentoService;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class OGestorPodeReceberUmProcessoFísicoDesdeQueSaibaOSetorDeOrigemONúmeroEAnoDaGuia {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;
	@Autowired private ProcessamentoService processamentoService;

	public OGestorPodeReceberUmProcessoFísicoDesdeQueSaibaOSetorDeOrigemONúmeroEAnoDaGuia(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Deslocamento - ReceberDocumento");
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
		//WebElement resultadoSetor = browser.findElement(By.xpath("//select[@id='seletorSetores']/option[@selected='selected']"));
		//System.out.println("*** setor selecionado: "+ resultadoSetor.getText().trim());
		//System.out.println("*** novo setor: "+ setor);
		//if (!resultadoSetor.getText().trim().equals(setor.toUpperCase())) 	{
		browser.findElement(By.id("seletorSetores")).click();
		browser.findElement(By.xpath("//*[@id='seletorSetores']/option[@value='"+ setor +"']")).click();
		browser.findElement(By.id("altSetor")).click();
		WebElement resultadoAlteracaoSetor = browser.findElement(By.xpath("//*[@class='InfoMessage']"));
		verifyEquals("Setor alterado com sucesso!", resultadoAlteracaoSetor.getText().trim());
		//}
	}
	
	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).click().build().perform();	
	}

	public void informeOSetorDeOrigemEONúmeroEAnoDaGuia() throws Exception {
		System.out.println("*** Numero da guia: " + processamentoService.getNumeroGuia());
		System.out.println("*** Ano da guia: " + processamentoService.getAnoGuia());
		System.out.println("*** Setor de origem da guia: " + processamentoService.getSetorOrigemGuia());
		browser.findElement(By.id("itDescricaoOrig")).sendKeys(processamentoService.getSetorOrigemGuia());
		Thread.sleep(2000);
		browser.findElement(By.id("itDescricaoOrig")).sendKeys(Keys.ARROW_DOWN);
		browser.findElement(By.id("itDescricaoOrig")).sendKeys(Keys.ENTER);
		browser.findElement(By.id("itNumeroGuia")).sendKeys(processamentoService.getNumeroGuia());
		browser.findElement(By.id("itAnoGuia")).clear();
		browser.findElement(By.id("itAnoGuia")).sendKeys(processamentoService.getAnoGuia());
	}

	public void cliqueEmPesquisar() throws Exception {
		browser.findElement(By.id("btnPesquisar")).click();
	}

	public void verifiqueSeAGuiaFoiExibida() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span"));
		System.out.println("*** verifiqueSeAGuiaFoiExibida: "+ mensagem.getText().trim());
		if (mensagem.getText().trim().startsWith("Nenhum registro")){
			cliqueEmPesquisar();
		} else if (mensagem.getText().trim().startsWith("A origem da guia")) {
			browser.findElement(By.id("itDescricaoOrig")).sendKeys(" ");
			browser.findElement(By.id("itDescricaoOrig")).sendKeys(Keys.ARROW_DOWN);
			browser.findElement(By.id("itDescricaoOrig")).sendKeys(Keys.ENTER);
			cliqueEmPesquisar();
		} else {
			verify(mensagem.getText().trim().startsWith("Foram encontrados"));
		}
	}

	public void cliqueEmReceberAGuia() throws Exception {
		Thread.sleep(1000);
		browser.findElement(By.xpath("//input[@value='Receber Guia']")).click();
		Thread.sleep(5000);
	}

	public void verifiqueSeAGuiaFoiRecebidaComSucesso() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 20);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span"));
		System.out.println("*** verifiqueSeAGuiaFoiRecebidaComSucesso: "+ mensagem.getText().trim());
		verify(mensagem.getText().trim().endsWith("recebida com sucesso!"));
		
	}

}
