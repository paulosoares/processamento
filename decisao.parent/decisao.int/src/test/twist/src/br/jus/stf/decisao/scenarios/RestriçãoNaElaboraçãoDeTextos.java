package br.jus.stf.decisao.scenarios;

// JUnit Assert framework can be used for verification

import org.openqa.selenium.WebDriver;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

public class RestriçãoNaElaboraçãoDeTextos {

	private WebDriver browser;

	@Autowired
	private TwistScenarioDataStore scenarioStore;

	public RestriçãoNaElaboraçãoDeTextos(WebDriver browser) {
		this.browser = browser;
	}

	public void nãoDeveSerPossívelVisualizarUmTextoRestritoAOutroUsuárioNaFase(
			String string1) throws Exception {
	
	}

	public void nãoDeveSerPossívelEditarUmTextoRestritoAOutroUsuárioNaFase(
			String string1) throws Exception {
	
	}

	public void semAConfiguraçãoDoSetorSelecionadaTextosIniciadosOuEmElaboraçãoDevemVirSemRestrição()
			throws Exception {
	
	}

}
