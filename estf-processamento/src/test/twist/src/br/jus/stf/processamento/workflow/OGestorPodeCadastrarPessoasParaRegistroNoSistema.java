package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification

import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class OGestorPodeCadastrarPessoasParaRegistroNoSistema {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;

	public OGestorPodeCadastrarPessoasParaRegistroNoSistema(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Carga - CadastrarPessoa ");
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

	public void cliqueNoBotão(String string1) throws Exception {
		browser.findElement(By.id("btnRenderizaTelaCadastro")).click();
		Thread.sleep(2000);
	}

	public void informeDadosDeUmPreposto() throws Exception {
		browser.findElement(By.xpath("//table[@id='tipoPapelJurisdicionado']/tbody/tr/td[3]/input")).click();
		browser.findElement(By.xpath("//span[@id='pnlCentralCadastroDiv']/div[2]/div[2]/span[2]/input")).sendKeys("CLARISSE SOUZA DE ANDRADE");
		browser.findElement(By.xpath("//span[@id='pnlCentralCadastroDiv']/div[2]/div[3]/span[2]/input")).sendKeys("clarissesa@gmail.com");
		browser.findElement(By.xpath("//span[@id='pnlCentralCadastroDiv']/div[2]/div[4]/span[2]/input[1]")).sendKeys("69922551187");
		browser.findElement(By.xpath("//table[@id='panelObservacao']/tbody/tr[2]/td/textarea")).sendKeys("Teste da TI: Cadastro de preposto");
		Thread.sleep(2000);
		
		browser.findElement(By.id("itDataValidadeJurisInputDate")).click();
		WebDriverWait esperaPesquisa = new WebDriverWait(browser, 10);
		esperaPesquisa.ignoring(ElementNotVisibleException.class);
		esperaPesquisa.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaPesquisa.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='itDataValidadeJuris']")));
		browser.findElement(By.xpath("//div[text()='>']")).click();
		
		WebDriverWait esperaResultado = new WebDriverWait(browser, 5);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='1']")));
		browser.findElement(By.xpath("//td[text()='1']")).click();
		Thread.sleep(2000);
	}

	public void adicioneUmEndereço() throws Exception {
		browser.findElement(By.xpath("//div[@id='idDestinatarioPanel']/div/div[2]/span[2]/input")).sendKeys("70650554");
		browser.findElement(By.xpath("//div[@id='idDestinatarioPanel']/div/div[2]/span[2]/input")).sendKeys(Keys.TAB);
		
		WebDriverWait esperaPesquisa = new WebDriverWait(browser, 30);
		esperaPesquisa.ignoring(ElementNotVisibleException.class);
		esperaPesquisa.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaPesquisa.until(ExpectedConditions.visibilityOfElementLocated(By.id("dvAguarde")));
		
		Thread.sleep(5000);
		browser.findElement(By.xpath("//div[@id='idDestinatarioPanel']/div/div[4]/span[2]/input")).sendKeys("505");
		Thread.sleep(1000);
		browser.findElement(By.xpath("//div[@id='idDestinatarioPanel']/div/div[6]/span[2]/input")).click();
		Thread.sleep(2000);
	}

	public void adicioneUmTelefone() throws Exception {
		browser.findElement(By.id("inputCodTel")).sendKeys("61");
		browser.findElement(By.id("inputTelefone")).sendKeys("3233-3333");
		browser.findElement(By.xpath("//div[@id='idDestinatarioContato']/div/div[2]/span[3]/input")).click();
		Thread.sleep(2000);
	}
	
	public void salveOsDados() throws Exception {
		browser.findElement(By.xpath("//span[@id='pnlCentralCadastroDiv']/div[5]/span/input[@value='Salvar']")).click();
		Thread.sleep(5000);
	}

	public void verifiqueSeAOperaçãoFoiRealizadaComSucesso() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span[@class='InfoMessage']"));
		System.out.println("*** verifiqueSeAOperaçãoFoiRealizadaComSucesso: " +mensagem.getText().trim());
		verifyEquals("Operação realizada com sucesso.", mensagem.getText().trim());
	}

}
