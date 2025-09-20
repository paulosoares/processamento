package br.jus.stf.processamento.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import br.jus.stf.processamento.page.PrincipalPage;

import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;

public class Comandos extends SeleneseTestBase {
	
	protected static WebDriver browser;
	public static Selenium selenium;
	public static String mainHandle;
	public static WebDriverWait wait;
	
	public static PrincipalPage principal;
	
	public static void acessarRaizMenu(String caminhoMenu) {
		Actions action = new Actions(browser);
		String elemento = "//*[@summary='main menu']//span[text()='" + caminhoMenu.trim() + "']";
		esperarElementoPresentePorXpath(elemento);
		WebElement raizMenu = browser.findElement(By.xpath(elemento));
		action.moveToElement(raizMenu).click().build().perform();
	}

	public static void acessarSubMenu(String caminhoMenu) {
		Actions action = new Actions(browser);
		String elemento = "//*[@summary='sub menu']//td[text()='" + caminhoMenu.trim() + "']";
		esperarElementoPresentePorXpath(elemento);
		WebElement subNivelMenu = browser.findElement(By.xpath(elemento));
		action.moveToElement(subNivelMenu).click().build().perform();
	}
	
	public static void esperarElementoClicavelePorId(String elemento){
		WebDriverWait waitElementoPresente = new WebDriverWait(browser, 30);
		waitElementoPresente.ignoring(ElementNotVisibleException.class);
		waitElementoPresente.pollingEvery(500, TimeUnit.MILLISECONDS);
		waitElementoPresente.until(ExpectedConditions.elementToBeClickable(By.id(elemento)));
	}
	
	public static void esperarElementoDesaparecerPorId(String elemento){
		WebDriverWait waitElementoPresente = new WebDriverWait(browser, 30);
		waitElementoPresente.pollingEvery(500, TimeUnit.MILLISECONDS);
		waitElementoPresente.until(ExpectedConditions.invisibilityOfElementLocated(By.id(elemento)));
	}

	public static void esperarElementoPresentePorId(String elemento){
		WebDriverWait waitElementoPresente = new WebDriverWait(browser, 30);
		waitElementoPresente.ignoring(ElementNotVisibleException.class);
		waitElementoPresente.pollingEvery(500, TimeUnit.MILLISECONDS);
		waitElementoPresente.until(ExpectedConditions.visibilityOfElementLocated(By.id(elemento)));
	}
	
	public static void esperarElementoPresentePorXpath(String elemento){
		WebDriverWait waitElementoPresente = new WebDriverWait(browser, 30);
		waitElementoPresente.ignoring(ElementNotVisibleException.class);
		waitElementoPresente.pollingEvery(500, TimeUnit.MILLISECONDS);
		waitElementoPresente.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elemento)));
	}
	
	public static void esperarElementoPresentePorCss(String elemento){
		WebDriverWait waitElementoPresente = new WebDriverWait(browser, 30);
		waitElementoPresente.ignoring(ElementNotVisibleException.class);
		waitElementoPresente.pollingEvery(500, TimeUnit.MILLISECONDS);
		waitElementoPresente.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(elemento)));
	}
	
	public static void esperarElementoPresentePorName(String elemento){
		WebDriverWait waitElementoPresente = new WebDriverWait(browser, 30);
		waitElementoPresente.ignoring(ElementNotVisibleException.class);
		waitElementoPresente.pollingEvery(500, TimeUnit.MILLISECONDS);
		waitElementoPresente.until(ExpectedConditions.visibilityOfElementLocated(By.name(elemento)));
	}
	
	public static void esperarElementoPresentePorLinkText(String elemento){
		WebDriverWait waitElementoPresente = new WebDriverWait(browser, 30);
		waitElementoPresente.ignoring(ElementNotVisibleException.class);
		waitElementoPresente.pollingEvery(500, TimeUnit.MILLISECONDS);
		waitElementoPresente.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(elemento)));
	}
	
	public static void selectComboValueByIndex(final String elementId, final int index) {
		esperarElementoPresentePorId(elementId);
		final Select selectBox = new Select(browser.findElement(By.id(elementId)));
		selectBox.selectByIndex(index);
	}

	public static void selectComboValue(final String elementId, final String value) {
		esperarElementoPresentePorId(elementId);
		final Select selectBox = new Select(browser.findElement(By.id(elementId)));
		selectBox.selectByValue(value);
	}
	
	public static void selectComboValuePorXpath(final String elementId, final String value) {
		esperarElementoPresentePorXpath(elementId);
		final Select selectBox = new Select(browser.findElement(By.xpath(elementId)));
		selectBox.selectByValue(value);
	}

	public static void acharPreencherPorCss(String elemento, String valor) {
		esperarElementoPresentePorCss(elemento);
		WebElement element = browser.findElement(By.cssSelector(elemento));
		element.click();
		element.clear();
		element.sendKeys(valor);
	}

	public static void acharClicarPorId(String elemento) {
		esperarElementoClicavelePorId(elemento);
		WebElement element = browser.findElement(By.id(elemento));
		element.click();
	}

	public static void acharClicarPorIdSendKeys(String elemento, String valor) {
		esperarElementoClicavelePorId(elemento);
		WebElement element = browser.findElement(By.id(elemento));
		element.click();
		element.clear();
		element.sendKeys(valor);
	}

	public static void acharClicarPorXpath(String elemento) {
		esperarElementoPresentePorXpath(elemento);
		WebElement element = browser.findElement(By.xpath(elemento));
		element.click();

	}

	public static void acharClicarPorXpathSendKeys(String elemento) {
		esperarElementoPresentePorXpath(elemento);
		WebElement element = browser.findElement(By.xpath(elemento));
		element.click();
		element.clear();
		element.sendKeys("");
	}

	public static void acharClicarCheckBoxPorXpath(String elemento) {
		esperarElementoPresentePorXpath(elemento);
		WebElement element = browser.findElement(By.xpath(elemento));
		element.sendKeys("");
		if (element.isSelected())
			element.click();
		else
			while (!element.isSelected())
				element.click();
	}

	public static void acharClicarPorCss(String elemento) {
		esperarElementoPresentePorCss(elemento);
		WebElement element = browser.findElement(By.cssSelector(elemento));
		// element.sendKeys("");
		element.click();
	}

	public static void acharClicarPorCssSendKeys(String elemento) {
		esperarElementoPresentePorCss(elemento);
		WebElement element = browser.findElement(By.cssSelector(elemento));
		element.sendKeys("");
		element.click();
	}

	public static void acharClicarPorName(String elemento) {
		esperarElementoPresentePorName(elemento);
		WebElement element = browser.findElement(By.name(elemento));
		// element.sendKeys("");
		element.click();
	}

	public static void acharClicarPorLinkText(String elemento) {
		esperarElementoPresentePorLinkText(elemento);
		WebElement element = browser.findElement(By.linkText(elemento));
		// element.sendKeys("");
		element.click();
	}

	public static void acharPreencherPorId(String elemento, String valor) {
		esperarElementoPresentePorId(elemento);
		WebElement element = browser.findElement(By.id(elemento));
		element.click();
		element.sendKeys(valor);
	}

	public static void acharPreencherPorIdKey(String elemento, Keys valor) throws InterruptedException {
		WebElement element = browser.findElement(By.id(elemento));
		element.click();
		element.sendKeys(valor);
		Thread.sleep(500);
	}

	public static void acharPreencherPorXpath(String elemento, String valor) {
		esperarElementoPresentePorXpath(elemento);
		WebElement element = browser.findElement(By.xpath(elemento));
		element.sendKeys("");
		element.click();
		element.sendKeys(valor);
	}

	public static void acharPreencherPorName(String elemento, String valor) {
		esperarElementoPresentePorName(elemento);
		WebElement element = browser.findElement(By.name(elemento));
		element.click();
		element.sendKeys(valor);
	}

	public static void acharPorId(String elemento) {
		esperarElementoPresentePorId(elemento);
		browser.findElement(By.id(elemento));
	}

	public static void acharPorCss(String elemento) {
		esperarElementoPresentePorCss(elemento);
		browser.findElement(By.cssSelector(elemento));
	}

	public static void acharPorXpath(String elemento) {
		esperarElementoPresentePorXpath(elemento);
		browser.findElement(By.xpath(elemento));
	}

	/**
	 * Retorna o elemento encontrado. Caso contrário, retorna nulo.
	 * 
	 * @param xpathDoElemento
	 *            do elemento a ser encontrado
	 * @return WebElement do elemento
	 */
	public WebElement acharPorXpathElemento(String elemento) {
		WebElement element = null;

		try {
			esperarElementoPresentePorXpath(elemento);
			element = browser.findElement(By.xpath(elemento));
		} catch (TimeoutException e) {
			System.out.printf("Elemento %s não encontrado.\n", elemento);
		}

		return element;
	}

	public static void aceitarAlert() {
		WebDriverWait esperaConfirmacao = new WebDriverWait(browser, 10);
		esperaConfirmacao.ignoring(ElementNotVisibleException.class);
		esperaConfirmacao.pollingEvery(500, TimeUnit.MILLISECONDS);
		esperaConfirmacao.until(ExpectedConditions.alertIsPresent());
		Alert alert = browser.switchTo().alert();
		alert.accept();
	}

	public static void verificaElementoPorId(String elemento, String comparacao) throws Exception {
		esperarElementoPresentePorId(elemento);
		WebElement element = browser.findElement(By.id(elemento));
		if (!element.getText().equals(comparacao)) {
			throw new Exception();
		}
	}

	public static void verificaElementoPorXpath(String elemento, String comparacao) throws Exception {
		esperarElementoPresentePorXpath(elemento);
		String element = browser.findElement(By.xpath(elemento)).getText();
		Assert.assertEquals(element, comparacao);
	}
	
	public static void verificaElementoPorCss(String elemento, String comparacao) throws Exception {
		esperarElementoPresentePorCss(elemento);
		String element = browser.findElement(By.cssSelector(elemento)).getText();
		Assert.assertEquals(element, comparacao);
	}

	public static void alternarTela() {
		mainHandle = browser.getWindowHandle();
		Set<String> allHandles = browser.getWindowHandles();
		for (String currentHandle : allHandles) {

			if (!currentHandle.equals(mainHandle)) {
				browser.switchTo().window(currentHandle);
				break;
			}
		}
	}

	public static void verificarElementoPresentePorCss(String elemento) {
		esperarElementoPresentePorCss(elemento);
		browser.findElement(By.cssSelector(elemento));
	}

	public static void acharElementoSetaPraBaixoPorId(String elemento) {
		acharClicarPorId(elemento);
		WebElement element = browser.findElement(By.id(elemento));
		element.sendKeys(Keys.ARROW_DOWN);
		element.sendKeys(Keys.ENTER);
	}

	public static void apagarElementoPorId(String elemento, int caracteres) {
		int i;
		for (i = 0; i < caracteres; i++) {
			browser.findElement(By.id(elemento)).sendKeys(Keys.BACK_SPACE);
		}
	}

	public static void apagarElementoPorCss(String elemento, int caracteres) {
		int i;
		for (i = 0; i < caracteres; i++) {
			browser.findElement(By.cssSelector(elemento)).sendKeys(Keys.BACK_SPACE);
		}
	}

	public static void apagarElementoPorXpath(String elemento, int caracteres) {
		int i;
		esperarElementoPresentePorXpath(elemento);
		WebElement element = browser.findElement(By.xpath(elemento));
		element.sendKeys("");
		element.click();
		for (i = 0; i < caracteres; i++) {
			element.sendKeys(Keys.BACK_SPACE);
		}
	}

	public static void apagarElementoPorName(String elemento, int caracteres) {
		int i;
		esperarElementoPresentePorName(elemento);
		WebElement element = browser.findElement(By.name(elemento));
		element.sendKeys("");
		element.click();
		for (i = 0; i < caracteres; i++) {
			element.sendKeys(Keys.BACK_SPACE);
		}
	}

	public static String acharElementoPorXpath(String elemento) {
		esperarElementoPresentePorXpath(elemento);
		return browser.findElement(By.xpath(elemento)).getText();
	}
	
	public static String acharElementoPorId(String elemento) {
		esperarElementoPresentePorId(elemento);
		return browser.findElement(By.id(elemento)).getText();
	}

	public static void voltarTelaPrincipal() {
		if (browser.getWindowHandle() != mainHandle) {
			browser.close();
			browser.switchTo().window(mainHandle);
		}
	}

	private static String calculaDigitoVerificador(String numero) {
		Integer primDig, segDig;
		int soma = 0, peso = 10;
		for (int i = 0; i < numero.length(); i++)
			soma += Integer.parseInt(numero.substring(i, i + 1)) * peso--;

		if (soma % 11 == 0 | soma % 11 == 1)
			primDig = new Integer(0);
		else
			primDig = new Integer(11 - (soma % 11));

		soma = 0;
		peso = 11;
		for (int i = 0; i < numero.length(); i++)
			soma += Integer.parseInt(numero.substring(i, i + 1)) * peso--;

		soma += primDig.intValue() * 2;
		if (soma % 11 == 0 | soma % 11 == 1)
			segDig = new Integer(0);
		else
			segDig = new Integer(11 - (soma % 11));

		return primDig.toString() + segDig.toString();
	}

	public static String geraCPF() {
		String iniciais = "";
		Integer numero;
		for (int i = 0; i < 9; i++) {
			numero = new Integer((int) (Math.random() * 10));
			iniciais += numero.toString();
		}
		return iniciais + calculaDigitoVerificador(iniciais);
	}

	public static void alterarSetor(String setor) throws Exception {
		acharClicarPorId(principal.ID_SETOR);
		selectComboValue(principal.ID_SETOR, setor);
		acharClicarPorId(principal.ID_BT_ALTERAR);
		verificaElementoPorXpath(principal.XP_MSG_INFO, "Setor alterado com sucesso!");
	}
	
	public static boolean acharElementoTesteXpath(String elemento){
		/*
		 * Este método faz verificação da existência do elemento na tela. O diferencial dele é que caso não
		 * encontre o elemento, não é lançado uma exceção. Sendo este método ideal para "ifs"
		 */
		try {
			if(browser.findElement(By.xpath(elemento)) != null){
				return true;
			}
		} catch (WebDriverException e) {
			System.out.printf("O elemento %s não foi encontrado.\n", elemento);
			return false;
		}
		return false;		
	}

}
