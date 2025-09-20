package br.jus.stf.processamento.workflow;

// JUnit Assert framework can be used for verification

import org.openqa.selenium.WebDriver;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class Regras {

	private WebDriver browser;

	@Autowired
	private TwistScenarioDataStore scenarioStore;

	public Regras(WebDriver browser) {
		this.browser = browser;
	}

}
