package br.jus.stf.decisao.scenarios;

// JUnit Assert framework can be used for verification

import org.openqa.selenium.WebDriver;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class ConfiguraçõesDoGabinete {

	private WebDriver browser;

	@Autowired
	private TwistScenarioDataStore scenarioStore;

	public ConfiguraçõesDoGabinete(WebDriver browser) {
		this.browser = browser;
	}

	public void aOpçãoConfigurarOpçõesDoGabineteNãoDeveSerAcessadaSemAntesSelecionarUmSetor()
			throws Exception {
	
	}

	public void aOpçãoConfigurarOpçõesDoGabineteDeveSerExibidaSomenteParaUsuáriosComOPerfilMinistro()
			throws Exception {
	
	}

	public void aOpçãoConfigurarOpçõesDoGabineteDeveSerExibidaSomenteParaUsuáriosComOPerfilAdministrador()
			throws Exception {
	
	}

}
