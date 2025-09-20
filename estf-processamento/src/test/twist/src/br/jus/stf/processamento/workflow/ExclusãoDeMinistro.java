package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification

import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class ExclusãoDeMinistro {

	private WebDriver browser;

	@Autowired
	private TwistScenarioDataStore scenarioStore;

	public ExclusãoDeMinistro(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Administração - ExclusãoDeMinistro");
	}

	public void cliqueNoBotãoNovo() throws Exception {
		browser.findElement(By.xpath("//form[@id='form']/table[3]/tbody/tr/td/span/input[2]")).click();
	}

	public void preenchaOsCamposObrigatórios() throws Exception {
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

	public void cliqueNoBotãoPesquisar() throws Exception {
		browser.findElement(By.id("btnPesquisar")).click();
		Thread.sleep(2000);
	}

}
