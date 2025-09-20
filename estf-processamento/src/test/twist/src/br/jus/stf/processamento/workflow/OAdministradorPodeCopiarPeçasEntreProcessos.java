package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification
import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;
import static com.thoughtworks.twist.core.execution.TwistVerification.verifyTrue;
import static junit.framework.Assert.assertTrue;

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

public class OAdministradorPodeCopiarPeçasEntreProcessos {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;

	public OAdministradorPodeCopiarPeçasEntreProcessos(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Administracao - CopiarPecasEntreProcessos");
	}

	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).click().build().perform();	
	}
	
	public void informeOProcessoDeOrigemEDeDestino(String origem, String destino)
			throws Exception {
		browser.findElement(By.id("itProcesso")).click();
		Thread.sleep(2000);
		browser.findElement(By.id("itProcesso")).sendKeys(origem+" ");
		WebDriverWait esperaPesquisa = new WebDriverWait(browser, 30);
		esperaPesquisa.ignoring(ElementNotVisibleException.class);
		esperaPesquisa.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaPesquisa.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sbProcesso:suggest']/tbody/tr[1]"))).click();
		Thread.sleep(1000);
		browser.findElement(By.id("itObjetoIncidente")).sendKeys(destino+" ");
		esperaPesquisa = new WebDriverWait(browser, 10);
		esperaPesquisa.ignoring(ElementNotVisibleException.class);
		esperaPesquisa.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaPesquisa.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sbObjetoIncidente:suggest']/tbody/tr[2]"))).click();
	}

	public void selecioneAsOpçõesNoFimDoProcessoEInserirInformaçõesDoProcessoOrigem()
			throws Exception {
		Thread.sleep(1000);
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='listaAssociadosDataTable:n']")));
		Thread.sleep(1000);
		browser.findElement(By.xpath("//table[@id='numeroSequencia']/tbody/tr/td[2]/input")).click(); //("numeroSequencia:1")).click();
		browser.findElement(By.id("inserirInformacao")).click();
	}

	public void selecioneAsPeçasQueSerãoCopiadas() throws Exception {
		browser.findElement(By.xpath("//table[@id='listaAssociadosDataTable:n']/tbody/tr[2]/td/div/div/input")).click(); // Segunda peça
		browser.findElement(By.xpath("//table[@id='listaAssociadosDataTable:n']/tbody/tr[3]/td/div/div/input")).click(); // Terceira peça
	}

	public void confirmeAAção() throws Exception {
		browser.findElement(By.id("copiarPecas")).click();
		Thread.sleep(2000);
	}

	public void verifiqueSeAsPeçasForamCopiadasComSucesso() 
			throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span")); 
		verifyEquals("As peças foram copiadas com sucesso.", mensagem.getText().trim());
		
	}



}
