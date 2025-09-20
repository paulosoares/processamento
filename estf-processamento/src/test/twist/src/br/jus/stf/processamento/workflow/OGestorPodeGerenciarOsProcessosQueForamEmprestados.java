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

public class OGestorPodeGerenciarOsProcessosQueForamEmprestados {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;

	public OGestorPodeGerenciarOsProcessosQueForamEmprestados(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Carga - RelatórioDeCargaDosAutos");
	}

	public void cliqueNoMenuSubmenuENaOpção(String menu, String submenu, String item) throws Exception {
		
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		
		acaoMenu.click(raizMenu).moveToElement(subMenu);
		WebElement itemMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+item+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).moveToElement(itemMenu).click().build().perform();
		Thread.sleep(2000);
	}
	
	public void informeOAutorizador(String autorizador) throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.id("pnlPesquisa")));
		browser.findElement(By.xpath("//span[@id='pnlPesquisa']/div[2]/span[2]/input[1]")).sendKeys(autorizador.toUpperCase());
		Thread.sleep(2000);
	}

	public void informeADataAtualNoPeríodo() throws Exception {
		browser.findElement(By.id("itDataInicioInputDate")).click();
		Thread.sleep(2000);
		browser.findElement(By.xpath("//table[@id='itDataInicio']//div[text()='Hoje']")).click();
		Thread.sleep(2000);
		browser.findElement(By.id("itDataFimInputDate")).click();
		Thread.sleep(2000);
		browser.findElement(By.xpath("//table[@id='itDataFim']//div[text()='Hoje']")).click();	
		Thread.sleep(2000);
	}

	public void realizeAPesquisa() throws Exception { 
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.elementToBeClickable(By.id("btnPesquisarJurisdicionado")));
		browser.findElement(By.id("btnPesquisarJurisdicionado")).click();
		Thread.sleep(2000);
	}

	public void verifiqueSeOsProcessosForamExibidos() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabelaAutos")));
		
	}

	public void selecioneOPrimeiroProcesso() throws Exception {
		browser.findElement(By.xpath("//table[@id='tabelaAutos']/tbody/tr[1]/td/input")).click();	
	}
	
	public void cliqueEmCobrar() throws Exception {
		browser.findElement(By.xpath("//table[@id='pnlCentral']/tbody/tr[3]/td/span/div/span/input[1]")).click();
		
		//Espera a modal aparecer
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='modalPanelObsCobrancaContentTable']")));
		
		//Confirma a ação
		browser.findElement(By.xpath("//table[@id='modalPanelObsCobrancaContentTable']//input[@value='OK']")).click();
	}

	public void verifiqueSeAQuantidadeDeCobrançasNoProcessoFoiAcrescida()
			throws Exception {
		//Verifica se a cobrança foi realizada com sucesso
		WebDriverWait esperaMensagem = new WebDriverWait(browser, 10);
		esperaMensagem.ignoring(ElementNotVisibleException.class);
		esperaMensagem.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaMensagem.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span[@class='InfoMessage']"));
		System.out.println("*** verifiqueSeAQuantidadeDeCobrançasNoProcessoFoiAcrescida: " +mensagem.getText().trim());
		verifyEquals("Cobrança realizada com sucesso.", mensagem.getText().trim());
	}

	public void cliqueEmInvalidarCobrança() throws Exception {
		browser.findElement(By.xpath("//table[@id='pnlCentral']/tbody/tr[3]/td/span/div/span/input[3]")).click();
		Thread.sleep(5000);
	}
	

	public void verifiqueSeAQuantidadeDeCobrançaFoiDecrescida() throws Exception {
		//Verifica se a cobrança foi realizada com sucesso
		WebDriverWait esperaMensagem = new WebDriverWait(browser, 10);
		esperaMensagem.ignoring(ElementNotVisibleException.class);
		esperaMensagem.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaMensagem.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span[@class='InfoMessage']"));
		System.out.println("*** selecioneOPrimeiroProcessoECliqueEmCobrar: " +mensagem.getText().trim());
		verifyEquals("Cobrança invalidada com sucesso.", mensagem.getText().trim());
	}

	public void cliqueEmInvalidarCarga() throws Exception {
		browser.findElement(By.xpath("//table[@id='pnlCentral']/tbody/tr[3]/td/span/div/span/input[2]")).click();
		Thread.sleep(5000);
	}

	public void verifiqueSeACargaFoiInvalidadaComSucesso() throws Exception {
		//Verifica se a cobrança foi realizada com sucesso
		WebDriverWait esperaMensagem = new WebDriverWait(browser, 10);
		esperaMensagem.ignoring(ElementNotVisibleException.class);
		esperaMensagem.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaMensagem.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span[@class='InfoMessage']"));
		System.out.println("*** verifiqueSeACargaFoiInvalidadaComSucesso: " +mensagem.getText().trim());
		verifyEquals("Carga invalidada com sucesso.", mensagem.getText().trim());
	}

}
