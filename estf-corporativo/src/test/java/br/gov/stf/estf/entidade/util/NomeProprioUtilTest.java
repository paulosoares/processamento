package br.gov.stf.estf.entidade.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class NomeProprioUtilTest {

	@Test
	public void testPrimeiraMaiuscula() {
		testPrimeiraMaiusculaNull();

		testPrimeiraMaiusculaEmpty();

		testPrimeiraMaiusculaCaixaAlta();

		testPrimeiraMaiusculaCaixaBaixa();

		testPrimeiraMaiusculaCaixaCorreta();

		testPrimeiraMaiusculaCaixaAleatoria();

		testPrimeiraMaiusculaPreposicoes();

		testPrimeiraMaiusculaConjuncoes();
	}

	private void testPrimeiraMaiusculaNull() {
		String nome = null;

		try {
			NomeProprioUtil.primeiraMaiuscula(nome);
		} catch (IllegalArgumentException exception) {
			assertNotNull(exception.getLocalizedMessage());
			return;
		}

		fail("Era esperada uma IllegalArgumentException.");
	}

	private void testPrimeiraMaiusculaEmpty() {
		String nome = "";

		try {
			NomeProprioUtil.primeiraMaiuscula(nome);
		} catch (IllegalArgumentException exception) {
			assertNotNull(exception.getLocalizedMessage());
			return;
		}

		fail("Era esperada uma IllegalArgumentException.");
	}

	private void testPrimeiraMaiusculaCaixaAlta() {
		String nome = "SUPREMO TRIBUNAL FEDERAL";
		String nomeAlterado = NomeProprioUtil.primeiraMaiuscula(nome);

		assertNotNull(nomeAlterado);
		assertEquals("Supremo Tribunal Federal", nomeAlterado);
	}

	private void testPrimeiraMaiusculaCaixaBaixa() {
		String nome = "supremo tribunal federal";
		String nomeAlterado = NomeProprioUtil.primeiraMaiuscula(nome);

		assertNotNull(nomeAlterado);
		assertEquals("Supremo Tribunal Federal", nomeAlterado);
	}

	private void testPrimeiraMaiusculaCaixaCorreta() {
		String nome = "Supremo Tribunal Federal";
		String nomeAlterado = NomeProprioUtil.primeiraMaiuscula(nome);

		assertNotNull(nomeAlterado);
		assertEquals("Supremo Tribunal Federal", nomeAlterado);
	}

	private void testPrimeiraMaiusculaCaixaAleatoria() {
		String nome = "sUpReMo tRIBUnal FEDEral";
		String nomeAlterado = NomeProprioUtil.primeiraMaiuscula(nome);

		assertNotNull(nomeAlterado);
		assertEquals("Supremo Tribunal Federal", nomeAlterado);
	}

	private void testPrimeiraMaiusculaPreposicoes() {
		String nome = "supremo tribunal federal do brasil";
		String nomeAlterado = NomeProprioUtil.primeiraMaiuscula(nome);

		assertNotNull(nomeAlterado);
		assertEquals("Supremo Tribunal Federal do Brasil", nomeAlterado);
	}

	private void testPrimeiraMaiusculaConjuncoes() {
		String nome = "supremo tribunal federal e nacional";
		String nomeAlterado = NomeProprioUtil.primeiraMaiuscula(nome);

		assertNotNull(nomeAlterado);
		assertEquals("Supremo Tribunal Federal e Nacional", nomeAlterado);
	}
}
