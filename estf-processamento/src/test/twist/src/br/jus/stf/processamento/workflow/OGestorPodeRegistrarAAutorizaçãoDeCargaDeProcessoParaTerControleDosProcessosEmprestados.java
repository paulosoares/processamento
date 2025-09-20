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

public class OGestorPodeRegistrarAAutorizaçãoDeCargaDeProcessoParaTerControleDosProcessosEmprestados {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;
	@Autowired private ProcessamentoService processamentoService;

	public OGestorPodeRegistrarAAutorizaçãoDeCargaDeProcessoParaTerControleDosProcessosEmprestados(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Carga - AutorizaçãoECargaDeProcessos ");
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


	public void cliqueNoMenu(String menu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		acaoMenu.moveToElement(raizMenu).click().build().perform();	
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

	public void adicioneOProcessoParaCarga() throws Exception {
		System.out.println("*** adicioneOProcessoParaCarga: "+ processamentoService.getClasseNumeroProcesso());
		browser.findElement(By.id("idProcesso")).sendKeys(processamentoService.getClasseNumeroProcesso());
		browser.findElement(By.id("idProcesso")).sendKeys(Keys.ENTER);	
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.id("itResponsavel")));
	}

	public void adicioneOResponsávelPelaCarga() throws Exception {
		browser.findElement(By.id("itResponsavel")).sendKeys("2110882"); // 22318682453 - ARAKEN OLIVEIRA ou 2110882 ALEXANDRE CÉSAR DEL GROSSI - 78216648987
		WebDriverWait esperaPesquisa = new WebDriverWait(browser, 10);
		esperaPesquisa.ignoring(ElementNotVisibleException.class);
		esperaPesquisa.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaPesquisa.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sbResponsavel:suggest']/tbody/tr[1]"))).click();
		WebDriverWait divDesaparecer = new WebDriverWait(browser, 30);
		divDesaparecer.ignoring(ElementNotVisibleException.class);
		divDesaparecer.pollingEvery(1000, TimeUnit.MILLISECONDS);
		divDesaparecer.until(ExpectedConditions.invisibilityOfElementLocated(By.id("//div[@id='ProcessandoPopupDivBack']")));
		Thread.sleep(25000);
		browser.findElement(By.id("btnAddAutorizador")).click();
		Thread.sleep(2000);
		browser.findElement(By.xpath("//table[@id='pnlItemResponsavel']/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/input")).click();
	}

	public void adicioneOAutorizador() throws Exception {
		browser.findElement(By.id("itResponsavel")).sendKeys("22318682453"); // ARAKEN OLIVEIR
		WebDriverWait divDesaparecer = new WebDriverWait(browser, 50);
		divDesaparecer.ignoring(ElementNotVisibleException.class);
		divDesaparecer.pollingEvery(1000, TimeUnit.MILLISECONDS);
		divDesaparecer.until(ExpectedConditions.invisibilityOfElementLocated(By.id("//div[@id='ProcessandoPopupDivBack']")));
		Thread.sleep(2000);
		browser.findElement(By.id("btnAddAutorizado")).click();
	}
	
	public void informeAObservação(String observacao) throws Exception {
		browser.findElement(By.id("itObsDeslocamento")).sendKeys(observacao);
	}

	public void informeADataParaDevolução() throws Exception {
		browser.findElement(By.id("itDataDevolucaoInputDate")).click();
		browser.findElement(By.xpath("//div[text()='Hoje']")).click();
		Thread.sleep(2000);
	}

	public void confirmeAAção() throws Exception {
		browser.findElement(By.id("btnSalvarCarga")).click();
		Thread.sleep(2000);
	}

	public void verifiqueSeOEmpréstimoFoiRealizadoComSucesso() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 30);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span"));
		verify(mensagem.getText().trim().startsWith("Empréstimo realizado com sucesso! Guia gerada:"));
		Thread.sleep(10000);
	}

}
