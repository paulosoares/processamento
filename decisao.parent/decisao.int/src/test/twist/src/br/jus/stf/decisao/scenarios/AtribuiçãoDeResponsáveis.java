package br.jus.stf.decisao.scenarios;

// JUnit Assert framework can be used for verification

import org.openqa.selenium.WebDriver;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class AtribuiçãoDeResponsáveis {

	private WebDriver browser;

	@Autowired
	private TwistScenarioDataStore scenarioStore;

	public AtribuiçãoDeResponsáveis(WebDriver browser) {
		this.browser = browser;
	}

	public void deveSerPossívelPermitirOsResponsáveisDeUmTextoAUmUsuárioOuGrupoDoSetor()
			throws Exception {
	
	}

	public void deveSerPossívelTrocarOResponsávelNaTransiçãoDeFaseDosTextos()
			throws Exception {
	
	}

}
