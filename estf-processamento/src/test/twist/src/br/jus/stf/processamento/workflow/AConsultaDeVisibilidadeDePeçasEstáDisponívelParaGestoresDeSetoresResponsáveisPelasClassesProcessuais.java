package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification
import static com.thoughtworks.twist.core.execution.TwistVerification.verify;
import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;
import static com.thoughtworks.twist.core.execution.TwistVerification.verifyFalse;

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

public class AConsultaDeVisibilidadeDePeçasEstáDisponívelParaGestoresDeSetoresResponsáveisPelasClassesProcessuais {

	private WebDriver browser;

	@Autowired private TwistScenarioDataStore scenarioStore;

	public AConsultaDeVisibilidadeDePeçasEstáDisponívelParaGestoresDeSetoresResponsáveisPelasClassesProcessuais(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Consulta - VisibilidadeDaPeca");
	}
	
	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).click().build().perform();	
	}
	
	public void digiteAClasseEONúmeroDoProcesso(String classe, String processo)
			throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 30);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputClasse")));
		browser.findElement(By.id("inputClasse")).sendKeys(classe);
		browser.findElement(By.id("inputProcesso")).sendKeys(processo);
	}

	public void realizeAPesquisa() throws Exception {
		browser.findElement(By.id("btPesquisar")).click();
		Thread.sleep(2000);
	}
	
	public void verifiqueSeAsPeçasForamExibidas() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 15);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pecasPanel']")));
		WebElement tabelaProcesso = browser.findElement(By.xpath("//table[@id='pecasPanel']/tbody/tr[2]/td/span"));
		verifyEquals("Peças:", tabelaProcesso.getText().trim());
		
		//WebElement mensagem = browser.findElement(By.xpath("//*[@class='WarningMessage']"));
		//System.out.println("Consulta *** Mensagem: " +  mensagem.getText().trim());
		//assertf("Nenhuma peça encontrada.", mensagem.getText().trim());
	}
	
	public void altereOTipoDeVisibilidadeDaPrimeiraPeçaApresentadaParaPendente()
			throws Exception {
		browser.findElement(By.xpath("//table[@id='tablePecas']/tbody/tr[1]/td[9]/input")).click();
		Thread.sleep(20000);
	}

	public void verifiqueSeOTipoDeVisibilidadeFoiAlterado()
			throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 20);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(1000, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pecasPanel']")));
		WebElement resultadoAlteracaoPeca = browser.findElement(By.xpath("//*[@class='InfoMessage']"));
		verifyEquals("Tipo de acesso de documento alterado com sucesso!", resultadoAlteracaoPeca.getText().trim());
		System.out.println("Consulta *** Mensagem: " +  resultadoAlteracaoPeca.getText().trim());
	}
	
	public void altereOTipoDeVisibilidadeDaMesmaPeçaParaPública() throws Exception {
		browser.findElement(By.xpath("//table[@id='tablePecas']/tbody/tr[1]/td[8]/input")).click();
		Thread.sleep(20000);
	}

}
