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

public class OGestorPodeDeslocarProcessosFísicos {

	private WebDriver browser;
	
	@Autowired private TwistScenarioDataStore scenarioStore;
	@Autowired private ProcessamentoService processamentoService;

	public OGestorPodeDeslocarProcessosFísicos(WebDriver browser) {
		this.browser = browser;
		System.out.println("");
		System.out.println("#### Deslocamento - RemeterDocumento");
	}

	public void cliqueNoMenu(String menu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		acaoMenu.moveToElement(raizMenu).click().build().perform();	
	}
	
	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).click().build().perform();	
	}

	public void altereOSetorPara(String setor) throws Exception {
		processamentoService.setSetorOrigemProcesso(setor);
		WebElement resultadoSetor = browser.findElement(By.xpath("//*[@id='seletorSetores']/option[@selected='selected']"));
		resultadoSetor.getText().trim();
		if (!resultadoSetor.getText().trim().equals(setor.toUpperCase())) 	{
			browser.findElement(By.id("seletorSetores")).click();
			browser.findElement(By.xpath("//*[@id='seletorSetores']/option[@value='"+ setor +"']")).click();
			browser.findElement(By.id("altSetor")).click();
			WebElement resultadoAlteracaoSetor = browser.findElement(By.xpath("//*[@class='InfoMessage']"));
			verifyEquals("Setor alterado com sucesso!", resultadoAlteracaoSetor.getText().trim());
		}
	}

	public void informeOSetorDeDestino(String destino) throws Exception {
		processamentoService.setSetorDestinoProcesso(destino);
		browser.findElement(By.id("itDescricaoDestinatario")).sendKeys(destino+" ");
		WebDriverWait esperaPesquisa = new WebDriverWait(browser, 20);
		esperaPesquisa.ignoring(ElementNotVisibleException.class);
		esperaPesquisa.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaPesquisa.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sbSetor:suggest']/tbody/tr[1]"))).click();
		
		//browser.findElement(By.id("itDescricaoDestinatario")).sendKeys(Keys.ARROW_DOWN);
		//browser.findElement(By.id("itDescricaoDestinatario")).sendKeys(Keys.ENTER);
	}

	public void informeOProcessoQueSeráDeslocado() throws Exception {
		browser.findElement(By.id("itDocumento")).sendKeys(processamentoService.getClasseNumeroProcesso());
		System.out.println("*** Processo: " + processamentoService.getClasseNumeroProcesso());
		Thread.sleep(1000);
		browser.findElement(By.id("btnIncluirDocumento")).click();
	
	}
	
	public void informeAObservação(String observacao) throws Exception {
		browser.findElement(By.id("itObservacao")).sendKeys(observacao);
		Thread.sleep(2000);
	}

	public void confirmeODeslocamento() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 10);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.elementToBeClickable(By.id("btnDeslocar")));
		
		browser.findElement(By.id("btnDeslocar")).click();
		
//		Actions acao = new Actions(browser);
//		acao.moveToElement(botao).click().build().perform();
	}

	public void verifiqueSeAGuiaFoiGeradaComSucesso() throws Exception {
		WebDriverWait esperaResultado = new WebDriverWait(browser, 20);
		esperaResultado.ignoring(ElementNotVisibleException.class);
		esperaResultado.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaResultado.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='pnlMessages']")));
		WebElement mensagem = browser.findElement(By.xpath("//table[@id='pnlMessages']/tbody/tr/td/table/tbody/tr/td/span"));
		System.out.println("*** Mensagem: "+ mensagem.getText().trim());
		if (mensagem.getText().trim().startsWith("Favor informar o processo")){
			informeOProcessoQueSeráDeslocado();
			confirmeODeslocamento();
		} else {
			verify(mensagem.getText().trim().startsWith("Deslocamento Efetuado!"));
		}
		
		browser.findElement(By.xpath("//table[@id='modalPanelGuiaContentTable']/tbody/tr[2]/td/form/table/tbody/tr/td/input")).click();
		
	}

}
