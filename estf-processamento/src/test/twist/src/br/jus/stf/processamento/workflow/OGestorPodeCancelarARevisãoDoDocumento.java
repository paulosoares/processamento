package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class OGestorPodeCancelarARevisãoDoDocumento {

	private WebDriver browser;

	@Autowired
	private TwistScenarioDataStore scenarioStore;

	public OGestorPodeCancelarARevisãoDoDocumento(WebDriver browser) {
		this.browser = browser;
		System.out.println();
		System.out.println("#### Cancelar Revisão ####");

	}
	
	public void cliqueNoMenuENaOpção(String menu, String submenu) throws Exception {
		Actions acaoMenu = new Actions(browser);
		WebElement raizMenu = browser.findElement(By.xpath("//*[@summary='main menu']//span[text()='" + menu+ "']"));
		WebElement subMenu = browser.findElement(By.xpath("//*[@summary='sub menu']//td[text()='"+submenu+"']"));
		acaoMenu.moveToElement(raizMenu).moveToElement(subMenu).click().build().perform();	
	}

}
