package br.jus.stf.estf.decisao.texto.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.processostf.model.service.AgrupadorService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;

public class GerenciarCategoriaActionFacesBeanTest {

	@Mock
	private AgrupadorService agrupadorService;
	
	@Mock
	private UsuarioLogadoService usuarioLogadoService;

	private GerenciarCategoriaActionFacesBean gerenciarCategoriaAfb;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		gerenciarCategoriaAfb = new GerenciarCategoriaActionFacesBean();
		Whitebox.setInternalState(gerenciarCategoriaAfb, "agrupadorService", agrupadorService);
		Whitebox.setInternalState(gerenciarCategoriaAfb, "usuarioLogadoService", usuarioLogadoService);
	}

	@Test
	public void testExcluirCategoria() throws ServiceException {
		Agrupador agrupador = new Agrupador();
		agrupador.setDescricao("MEU AGRUPADOR 01");
		Setor s = new Setor();
		s.setId(600000453L);
		s.setSigla("GM CÁRMEN LÚCIA");
		s.setNome("GABINETE MINISTRA CÁRMEN LÚCIA");
		agrupador.setSetor(s);

		gerenciarCategoriaAfb = Mockito.spy(gerenciarCategoriaAfb);

		Mockito.doNothing().when(gerenciarCategoriaAfb).addInformation(Matchers.anyString());
		Mockito.doNothing().when(gerenciarCategoriaAfb).carregarCategorias();

		gerenciarCategoriaAfb.excluirCategoria(agrupador);

		Mockito.verify(agrupadorService).excluir(agrupador);

		Mockito.verify(gerenciarCategoriaAfb).addInformation(GerenciarCategoriaActionFacesBean.MSG_EXCLUSAO_SUCESSO);
	}

	@Test(expected = ServiceException.class)
	public void testRecuperarCategoriasExcecao() throws ServiceException {
		gerenciarCategoriaAfb = Mockito.spy(gerenciarCategoriaAfb);

		Ministro m = Mockito.mock(Ministro.class, Mockito.RETURNS_DEEP_STUBS);

		Mockito.when(usuarioLogadoService.getMinistro()).thenReturn(m);
		Mockito.when(m.getSetor().getId()).thenReturn(600000453L);

		Mockito.doThrow(ServiceException.class).when(agrupadorService).recuperarCategoriasDoSetor(600000453L);

		@SuppressWarnings("unused")
		List<Agrupador> categorias = gerenciarCategoriaAfb.recuperarCategorias();
	}

	@Test
	public void adicionarNovaCategoria() throws ServiceException {
		Agrupador categoria = criarCategoriaExemplo();
		gerenciarCategoriaAfb = Mockito.spy(gerenciarCategoriaAfb);
		Mockito.doReturn(new ArrayList<Agrupador>()).when(gerenciarCategoriaAfb).pesquisarCategoria(categoria);
		gerenciarCategoriaAfb.salvar(categoria);

		Mockito.verify(agrupadorService).salvar(categoria);		
	}
	
	@Test
	public void editarCategoriaExistente() throws ServiceException {
		Agrupador categoriaExemplo = criarCategoriaExemplo();
		categoriaExemplo.setId(1L);
		
		Mockito.when(gerenciarCategoriaAfb.pesquisarCategoria(categoriaExemplo)).thenReturn(new ArrayList<Agrupador>());		
		
		gerenciarCategoriaAfb.salvar(categoriaExemplo);
		Mockito.verify(agrupadorService).salvar(categoriaExemplo);		
	}
	
	@Test (expected = RuntimeException.class)
	public void lancarExceçãoAoAdicionarCategoriaJáExistente() throws ServiceException {
		Agrupador categoriaExemplo = criarCategoriaExemplo();
		
		Mockito.when(gerenciarCategoriaAfb.pesquisarCategoria(categoriaExemplo)).thenReturn(Arrays.asList(categoriaExemplo));
		
		gerenciarCategoriaAfb.salvar(categoriaExemplo);
	}
	
	private Agrupador criarCategoriaExemplo() {
		Agrupador agrupador = new Agrupador();
		agrupador.setDescricao("Categoria Exemplo");
		Setor s = new Setor();
		s.setId(600000453L);
		s.setSigla("GM CÁRMEN LÚCIA");
		s.setNome("GABINETE MINISTRA CÁRMEN LÚCIA");
		agrupador.setSetor(s);
		
		return agrupador;
	}
	
	@After
	public void tearDown() {

	}
}
