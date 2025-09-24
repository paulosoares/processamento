package br.jus.stf.estf.decisao.pesquisa.web.pfb.textos;

import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.web.pfb.AbstractPrincipalPesquisarTest;
import br.jus.stf.estf.decisao.pesquisa.web.texto.TextoFacesBean;
import br.jus.stf.estf.decisao.support.action.support.ActionController;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedListDataModel;

public class PesquisarPreferenciaFavoritosTest extends
		AbstractPrincipalPesquisarTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private TextoFacesBean tfb;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ActionController<TextoDto> ac;

	@Before
	public void setUp() {
		mockPrincipal().setIdSetor(600000453L);
		mockPrincipal().mockUsuario().setUsuario("FULANO.SILVA", "7777","FULANO SILVA DE SOUZA");
		mockPrincipal().mockMinistro().mockSetor().setSetor(600000453L, "GM CÁRMEN LÚCIA","GABINETE MINISTRA CÁRMEN LÚCIA");
		super.setUp();
		addMockFacesBean(TextoDto.class, tfb);
		addMockActionController(TextoDto.class, ac);
	}

	@Test
	public void testPesquisarTextosFavoritosPrimeiro() {
		escolherPreferenciaFavoritosPrimeiro();
		pesquisarTextos();
		Pesquisa pesquisa = capturarPesquisa();
		Assert.assertTrue(
				"A pesquisa deveria ter sido feita com Favoritos Primeiro",
				ArrayUtils.contains(
						(String[]) pesquisa.get(Pesquisa.CHAVE_FAVORITOS),
						Pesquisa.FAVORITOS_PRIMEIRO));
	}

	@Test
	public void testPesquisarTextosFavoritosApenas() {
		escolherPreferenciaFavoritosApenas();
		pesquisarTextos();
		Pesquisa pesquisa = capturarPesquisa();
		Assert.assertTrue(
				"A pesquisa deveria ter sido feita com Favoritos Apenas",
				ArrayUtils.contains(
						(String[]) pesquisa.get(Pesquisa.CHAVE_FAVORITOS),
						Pesquisa.FAVORITOS_APENAS));
	}

	private Pesquisa capturarPesquisa() {
		ArgumentCaptor<Pesquisa> pesquisaCaptor = ArgumentCaptor
				.forClass(Pesquisa.class);
		Mockito.verify(tfb).search(pesquisaCaptor.capture(), Matchers.anyInt(),
				Matchers.anyInt());
		Pesquisa pesquisa = pesquisaCaptor.getValue();
		return pesquisa;
	}

	private static final String[] PREFERENCIA_FAVORITOS_PRIMEIRO = { Pesquisa.FAVORITOS_PRIMEIRO };
	private static final String[] PREFERENCIA_FAVORITOS_APENAS = { Pesquisa.FAVORITOS_APENAS };

	private void escolherPreferenciaFavoritosPrimeiro() {
		pfb.setPreferenciaFavoritos(PREFERENCIA_FAVORITOS_PRIMEIRO);
	}

	private void escolherPreferenciaFavoritosApenas() {
		pfb.setPreferenciaFavoritos(PREFERENCIA_FAVORITOS_APENAS);
	}

	private void pesquisarTextos() {
		pfb.pesquisar(TextoDto.class);
		@SuppressWarnings("unchecked")
		PagedListDataModel<TextoDto> dm = (PagedListDataModel<TextoDto>) pfb
				.getCurrentContext().getResult();
		dm.fetchPage(0, 25);
	}

	@After
	public void tearDown() {

	}

}
