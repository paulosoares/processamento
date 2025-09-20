package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification
import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;
import static junit.framework.Assert.assertTrue;

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

public class OAdministradorPodeManterOPeríodoDeExclusãoDeMinistroDaDistribuiçãoDeProcessos {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;

	public OAdministradorPodeManterOPeríodoDeExclusãoDeMinistroDaDistribuiçãoDeProcessos(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Administracao - ExclusaoDeMinistroDaDistribuicao");
	}

	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).click().build().perform();	
	}

	public void cliqueNoBotãoNovo() throws Exception {
		
		browser.findElement(By.id("btnPesquisar")).click();
		Thread.sleep(1000);
		browser.findElement(By.id("btnLimpar")).click();
		Thread.sleep(1000);
		
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.elementToBeClickable(By.id("btnNovo")));
		
		browser.findElement(By.id("btnNovo")).click();
		Thread.sleep(1000);
	}

	public void preenchaOsCamposObrigatórios() throws Exception {
		
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='modalPanelEditarExclusaoContentTable']")));
		
		browser.findElement(By.id("itEditarDataInicialInputDate")).click();
		browser.findElement(By.xpath("//div[text()='00:00']")).click();
		browser.findElement(By.id("itEditarDataInicialTimeHours")).sendKeys("19");
		browser.findElement(By.id("itEditarDataInicialTimeEditorButtonOk")).click();
		browser.findElement(By.xpath("//div[text()='Aplicar']")).click();
		Thread.sleep(500);
		
		browser.findElement(By.id("selectEditarTipoExclusao")).click();
		browser.findElement(By.id("selectEditarTipoExclusao")).sendKeys("PRESIDENTE DO STF");
		browser.findElement(By.id("selectEditarTipoExclusao")).click();
		Thread.sleep(500);
		
		browser.findElement(By.id("selectEditarMinistro")).click();
		browser.findElement(By.id("selectEditarMinistro")).sendKeys("MIN. GILMAR MENDES");
		browser.findElement(By.id("selectEditarTipoExclusao")).click();
		Thread.sleep(500);
		
		browser.findElement(By.id("itEditarJustificativa")).sendKeys("Teste da TI: exclusão de ministro");
		Thread.sleep(500);
	}

	public void confirmeAAção() throws Exception {
		browser.findElement(By.id("btnEditarSalvar")).click();
		Thread.sleep(500);
	}

	public void verifiqueSeOsDadosForamIncluídos() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='lableMsg']")));
		WebElement tabelaExclusao = browser.findElement(By.xpath("//span[@id='lableMsg']"));
		verifyEquals("Período de exclusão de ministro incluído com sucesso!",tabelaExclusao.getText().trim());
		
		browser.findElement(By.id("btnEditarCancelar")).click();
		Thread.sleep(2000);
	}

	public void cliqueNoBotãoPesquisarDaTelaPrincipal() throws Exception {
		browser.findElement(By.id("btnPesquisar")).click();
		Thread.sleep(2000);
	}

	public void verifiqueSeOsDadosForamExibidos() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='tabExclusao']")));
		WebElement tabelaExclusao = browser.findElement(By.xpath("//table[@id='tabExclusao']/tbody/tr[1]/td/span"));
		assertTrue(!tabelaExclusao.getText().trim().equals(""));
	}

	public void removaORegistroIncluído() throws Exception {
		browser.findElement(By.xpath("//table[@id='tabExclusao']/tbody/tr[1]/td[7]/input[1]")).click();
		Thread.sleep(2000);
		WebDriverWait esperaResultado = new WebDriverWait(browser, 0);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.alertIsPresent());
		Alert alert = browser.switchTo().alert();
		alert.accept();
	}

	public void verifiqueSeORegistroFoiRemovido() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement tabelaExclusao = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span"));
		verifyEquals("Período de exclusão de ministro removido com sucesso!",tabelaExclusao.getText().trim());
	}

}
