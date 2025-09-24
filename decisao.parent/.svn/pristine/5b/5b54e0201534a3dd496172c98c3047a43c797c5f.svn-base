package br.jus.stf.estf.decisao.objetoincidente.web;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.AgrupadorService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.AgrupadorLocal;
import br.jus.stf.estf.decisao.texto.service.TextoService;

public class CategorizarProcessoActionFacesBeanTest {
	
	@Mock
	private TextoService textoService;

	@Mock
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Mock
	private AgrupadorService agrupadorService;
	
	private CategorizarProcessoActionFacesBean categorizarProcessoActionFacesBean;	
	
	private AgrupadorRepo agrupadorRepo = new AgrupadorRepo();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		categorizarProcessoActionFacesBean = new CategorizarProcessoActionFacesBean();
		Whitebox.setInternalState(categorizarProcessoActionFacesBean, "textoService", textoService);
		Whitebox.setInternalState(categorizarProcessoActionFacesBean, "objetoIncidenteService", objetoIncidenteService);
		Whitebox.setInternalState(categorizarProcessoActionFacesBean, "agrupadorService", agrupadorService);
	}
	
	@Test
	public void test_getAgrupadoresLocaisCategoriasRestantes() throws ServiceException {
		Agrupador categoria01 = agrupadorRepo.getAgrupador(1, "teste 01");
		Agrupador categoria02 = agrupadorRepo.getAgrupador(2, "teste 02");
		Agrupador categoria03 = agrupadorRepo.getAgrupador(3, "teste 03");
		
		List<Agrupador> categoriasRestantesInformado = new ArrayList<Agrupador>();
		categoriasRestantesInformado.add(categoria01);
		categoriasRestantesInformado.add(categoria02);
		categoriasRestantesInformado.add(categoria03);
		
		long idSetor = 1;
		
		List<AgrupadorLocal> ListaRetorno = new ArrayList<AgrupadorLocal>();
		categorizarProcessoActionFacesBean = Mockito.spy(categorizarProcessoActionFacesBean);		
		Mockito.doReturn(idSetor).when(categorizarProcessoActionFacesBean).getSetorUsuario();
		Mockito.when(agrupadorService.recuperarCategoriasDoSetor(idSetor)).thenReturn(categoriasRestantesInformado);
		
		ListaRetorno = categorizarProcessoActionFacesBean.getAgrupadoresLocaisCategoriasRestantes();
		
		boolean listaVazia = ListaRetorno.isEmpty();
		int qtdItensLista = ListaRetorno.size();
		
		assertEquals(false, listaVazia);
		assertEquals(3, qtdItensLista);
	}
	
	@Test
	public void test_getCategoriasAdicionadasEmCatSelecionada() throws ServiceException {
		List<ObjetoIncidente<?>> listaObjetosIncidentesInformada = new ArrayList<ObjetoIncidente<?>>();

		Processo objetoIncidente01 = agrupadorRepo.getObjetoIncidente(1,"1111");
		Processo objetoIncidente02 = agrupadorRepo.getObjetoIncidente(2,"2222");
		listaObjetosIncidentesInformada.add(objetoIncidente01);
		listaObjetosIncidentesInformada.add(objetoIncidente02);
		
		Agrupador categoria01 = agrupadorRepo.getAgrupador(1, "teste 01");
		Agrupador categoria02 = agrupadorRepo.getAgrupador(2, "teste 02");
		List<Agrupador> listaCatObjIncid01 = new ArrayList<Agrupador>();
		listaCatObjIncid01.add(categoria01);
		List<Agrupador> listaCatObjIncid02 = new ArrayList<Agrupador>();
		listaCatObjIncid02.add(categoria01);
		listaCatObjIncid02.add(categoria02);
		
		Mockito.when(agrupadorService.recuperarCategoriasDoObjetoIncidente(objetoIncidente01.getId())).thenReturn(listaCatObjIncid01);
		Mockito.when(agrupadorService.recuperarCategoriasDoObjetoIncidente(objetoIncidente02.getId())).thenReturn(listaCatObjIncid02);
		
		List<AgrupadorLocal> ListaRetorno = new ArrayList<AgrupadorLocal>();
		ListaRetorno = categorizarProcessoActionFacesBean.getCategoriasAdicionadasEmCatSelecionada(listaObjetosIncidentesInformada);
		
		boolean listaVazia = ListaRetorno.isEmpty();
		int qtdItensLista = ListaRetorno.size();
		
		assertEquals(false, listaVazia);
		assertEquals(2, qtdItensLista);
		assertEquals(true, categorizarProcessoActionFacesBean.isTemCategoria());
		assertEquals(2, ListaRetorno.get(0).getQtdObjIncidentes());
	}
	
	@Test
	public void test_getCategoriasAdicionadasEmTodosObjetosIncidentes() throws ServiceException {
		int qtdObjetosIncidentes = 5;
		
		AgrupadorLocal categoria01 = agrupadorRepo.getAgrupadorLocal(1, "teste 01", false, 1);
		AgrupadorLocal categoria02 = agrupadorRepo.getAgrupadorLocal(2, "teste 02", false, qtdObjetosIncidentes);
		AgrupadorLocal categoria03 = agrupadorRepo.getAgrupadorLocal(3, "teste 03", false, 1);
		
		List<AgrupadorLocal> categoriasAdicionadasInformada = new ArrayList<AgrupadorLocal>();
		categoriasAdicionadasInformada.add(categoria01);
		categoriasAdicionadasInformada.add(categoria02);
		categoriasAdicionadasInformada.add(categoria03);
		
		List<AgrupadorLocal> ListaRetorno = new ArrayList<AgrupadorLocal>();
		
		ListaRetorno = categorizarProcessoActionFacesBean.getCategoriasAdicionadasEmTodosObjetosIncidentes(qtdObjetosIncidentes,categoriasAdicionadasInformada);
		
		boolean listaVazia = ListaRetorno.isEmpty();
		int qtdItensLista = ListaRetorno.size();
		
		assertEquals(false, listaVazia);
		assertEquals(1, qtdItensLista);
	}	
	
	@Test
	public void test_salvarCategorizacao_Null() throws ServiceException {
		List<AgrupadorLocal> categoriasAdicionadasNull = new ArrayList<AgrupadorLocal>();
		
		categorizarProcessoActionFacesBean = Mockito.spy(categorizarProcessoActionFacesBean);
		Mockito.doNothing().when(categorizarProcessoActionFacesBean).addError(categorizarProcessoActionFacesBean.MSG_ERRO_CATEGORIA_NAO_SELECIONADA);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdicionadasNull);
		categorizarProcessoActionFacesBean.salvarCategorizacao();
	}
	
	@Test
	public void test_salvarCategorizacao_UmProcesoInclusao() throws ServiceException {
		AgrupadorLocal categoria01 = agrupadorRepo.getAgrupadorLocal(1, "teste 01", false, 1);
		List<AgrupadorLocal> categoriasAdicionadas = new ArrayList<AgrupadorLocal>();
		categoriasAdicionadas.add(categoria01);
		
		List<ObjetoIncidente<?>> listaObjetosIncidentesInformada = new ArrayList<ObjetoIncidente<?>>();
		Processo objetoIncidente01 = agrupadorRepo.getObjetoIncidente(1,"1111");
		listaObjetosIncidentesInformada.add(objetoIncidente01);
		
		List<Agrupador> listaCatObjIncid01 = new ArrayList<Agrupador>();
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdicionadas);
		categorizarProcessoActionFacesBean.setObjetosIncidentes(listaObjetosIncidentesInformada);
		Mockito.when(agrupadorService.recuperarCategoriasDoObjetoIncidente(objetoIncidente01.getId())).thenReturn(listaCatObjIncid01);
		
		categorizarProcessoActionFacesBean = Mockito.spy(categorizarProcessoActionFacesBean);
		Mockito.doNothing().when(categorizarProcessoActionFacesBean).inserirObjetoIncidenteemPreListaJulgamento(objetoIncidente01);
		categorizarProcessoActionFacesBean.salvarCategorizacao();
		
		Mockito.verify(categorizarProcessoActionFacesBean).sendToConfirmation();
	}
	
	@Test (expected = IllegalStateException.class)
	public void test_salvarCategorizacao_UmProcesoInclusao_falha() throws ServiceException {
		List<AgrupadorLocal> categoriasAdicionadas = new ArrayList<AgrupadorLocal>();
		AgrupadorLocal categoria02 = agrupadorRepo.getAgrupadorLocal(2, "teste 02", false, 1);		
		categoriasAdicionadas.add(categoria02);
		
		List<ObjetoIncidente<?>> listaObjetosIncidentesInformada = new ArrayList<ObjetoIncidente<?>>();
		Processo objetoIncidente01 = agrupadorRepo.getObjetoIncidente(1,"1111");
		listaObjetosIncidentesInformada.add(objetoIncidente01);
		
		Agrupador agruprador01 = agrupadorRepo.getAgrupador(1, "teste 01");
		List<Agrupador> listaCatObjIncid01 = new ArrayList<Agrupador>();
		listaCatObjIncid01.add(agruprador01);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdicionadas);
		categorizarProcessoActionFacesBean.setObjetosIncidentes(listaObjetosIncidentesInformada);
		Mockito.when(agrupadorService.recuperarCategoriasDoObjetoIncidente(objetoIncidente01.getId())).thenReturn(listaCatObjIncid01);

		categorizarProcessoActionFacesBean = Mockito.spy(categorizarProcessoActionFacesBean);		
		categorizarProcessoActionFacesBean.salvarCategorizacao();
		
		Mockito.verify(categorizarProcessoActionFacesBean).addError(categorizarProcessoActionFacesBean.MSG_ERRO_APENAS_UMA_CATEGORIA);
		Mockito.verify(categorizarProcessoActionFacesBean).sendToErrors();
	}
	
	
	@Test(expected = IllegalStateException.class)
	public void test_salvarCategorizacao_VariosProcesoInclusao_falha() throws ServiceException {
		List<AgrupadorLocal> categoriasAdicionadas = new ArrayList<AgrupadorLocal>();
		AgrupadorLocal categoria01 = agrupadorRepo.getAgrupadorLocal(1, "teste 01", false, 1);
		AgrupadorLocal categoria02 = agrupadorRepo.getAgrupadorLocal(2, "teste 02", false, 1);		
		categoriasAdicionadas.add(categoria01);
		categoriasAdicionadas.add(categoria02);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdicionadas);

		categorizarProcessoActionFacesBean = Mockito.spy(categorizarProcessoActionFacesBean);
		categorizarProcessoActionFacesBean.salvarCategorizacao();
		
		Mockito.verify(categorizarProcessoActionFacesBean).addError(categorizarProcessoActionFacesBean.MSG_ERRO_APENAS_UMA_CATEGORIA);
		Mockito.verify(categorizarProcessoActionFacesBean).sendToErrors();
	}	


	@Test
	public void test_sdicionarSelecionados_CategoriaSelacionado() {
		AgrupadorLocal categoria01 = agrupadorRepo.getAgrupadorLocal(1, "teste 01", false, 1);
		AgrupadorLocal categoria02 = agrupadorRepo.getAgrupadorLocal(2, "teste 02", true , 1);
		
		List<AgrupadorLocal> categoriasAdcionadas = new ArrayList<AgrupadorLocal>();
		categoriasAdcionadas.add(categoria01);
		
		List<AgrupadorLocal> categoriasRestantes = new ArrayList<AgrupadorLocal>();
		categoriasRestantes.add(categoria01);
		categoriasRestantes.add(categoria02);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdcionadas);
		categorizarProcessoActionFacesBean.setCategoriasRestantes(categoriasRestantes);
		
		categorizarProcessoActionFacesBean.adicionarSelecionados();
		
		boolean listaVaziaCategoriasRestantes = categorizarProcessoActionFacesBean.getCategoriasRestantes().isEmpty();
		int qtdItensListaCategoriasAdcionais = categorizarProcessoActionFacesBean.getCategoriasAdicionadas().size();
		
		assertEquals(true, listaVaziaCategoriasRestantes);
		assertEquals(2, qtdItensListaCategoriasAdcionais);
	}
	
	@Test
	public void test_adicionarSelecionados_CategoriaNaoSelacionado() {
		AgrupadorLocal categoria01 = agrupadorRepo.getAgrupadorLocal(1, "teste 01", false, 1);
		AgrupadorLocal categoria02 = agrupadorRepo.getAgrupadorLocal(2, "teste 02", false, 1);
		
		List<AgrupadorLocal> categoriasAdcionadas = new ArrayList<AgrupadorLocal>();
		categoriasAdcionadas.add(categoria01);
		
		List<AgrupadorLocal> categoriasRestantes = new ArrayList<AgrupadorLocal>();
		categoriasRestantes.add(categoria01);
		categoriasRestantes.add(categoria02);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdcionadas);
		categorizarProcessoActionFacesBean.setCategoriasRestantes(categoriasRestantes);
		
		categorizarProcessoActionFacesBean.adicionarSelecionados();
		
		boolean listaVaziaCategoriasRestantes = categorizarProcessoActionFacesBean.getCategoriasRestantes().isEmpty();
		int qtdItensListaCategoriasAdcionais = categorizarProcessoActionFacesBean.getCategoriasAdicionadas().size();
		
		assertEquals(false, listaVaziaCategoriasRestantes);
		assertEquals(1, qtdItensListaCategoriasAdcionais);
	}	

	@Test
	public void test_removerSelecionados_CategoriaSelacionada() {
		AgrupadorLocal categoria01 = agrupadorRepo.getAgrupadorLocal(1, "teste 01", true , 1);
		AgrupadorLocal categoria02 = agrupadorRepo.getAgrupadorLocal(2, "teste 02", false, 1);
		
		List<AgrupadorLocal> categoriasAdcionadas = new ArrayList<AgrupadorLocal>();
		categoriasAdcionadas.add(categoria01);
		
		List<AgrupadorLocal> categoriasRestantes = new ArrayList<AgrupadorLocal>();
		categoriasRestantes.add(categoria01);
		categoriasRestantes.add(categoria02);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdcionadas);
		categorizarProcessoActionFacesBean.setCategoriasRestantes(categoriasRestantes);
		categorizarProcessoActionFacesBean.removerSelecionados();
		boolean listaVaziaCategoriasAdicionadas = categorizarProcessoActionFacesBean.getCategoriasAdicionadas().isEmpty();
		int qtdItensListaCategoriasRestantes = categorizarProcessoActionFacesBean.getCategoriasRestantes().size();
		assertEquals(true, listaVaziaCategoriasAdicionadas);
		assertEquals(3, qtdItensListaCategoriasRestantes);
	}
	
	@Test
	public void test_removerSelecionados_CategoriaNaoSelacionado() {
		AgrupadorLocal categoria01 = agrupadorRepo.getAgrupadorLocal(1, "teste 01", false, 1);
		AgrupadorLocal categoria02 = agrupadorRepo.getAgrupadorLocal(2, "teste 02", false, 1);
		
		List<AgrupadorLocal> categoriasAdcionadas = new ArrayList<AgrupadorLocal>();
		categoriasAdcionadas.add(categoria01);
		
		List<AgrupadorLocal> categoriasRestantes = new ArrayList<AgrupadorLocal>();
		categoriasRestantes.add(categoria01);
		categoriasRestantes.add(categoria02);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdcionadas);
		categorizarProcessoActionFacesBean.setCategoriasRestantes(categoriasRestantes);
		categorizarProcessoActionFacesBean.removerSelecionados();
		boolean listaVaziaCategoriasAdicionadas = categorizarProcessoActionFacesBean.getCategoriasAdicionadas().isEmpty();
		assertEquals(false, listaVaziaCategoriasAdicionadas);
	}	

	@Test
	public void test_getCategoriasRestantes() {
		AgrupadorLocal categoria01 = agrupadorRepo.getAgrupadorLocal(1, "teste 01", true, 1);
		AgrupadorLocal categoria02 = agrupadorRepo.getAgrupadorLocal(2, "teste 02", true, 1);
		
		List<AgrupadorLocal> categoriasAdcionadas = new ArrayList<AgrupadorLocal>();
		categoriasAdcionadas.add(categoria01);
		
		List<AgrupadorLocal> categoriasRestantes = new ArrayList<AgrupadorLocal>();
		categoriasRestantes.add(categoria01);
		categoriasRestantes.add(categoria02);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasAdcionadas);
		categorizarProcessoActionFacesBean.setCategoriasRestantes(categoriasRestantes);
		List<AgrupadorLocal> categoriasRetorno = categorizarProcessoActionFacesBean.getCategoriasRestantes();
		categoriasRestantes.remove(categoria01);
		assertEquals(categoriasRestantes, categoriasRetorno);
	}

	@Test
	public void test_getCategoriasAdicionadas() {
		AgrupadorLocal categoria = agrupadorRepo.getAgrupadorLocal(1, "teste 01", true, 1);
		List<AgrupadorLocal> categoriasInformadas = new ArrayList<AgrupadorLocal>();
		categoriasInformadas.add(categoria);
		
		categorizarProcessoActionFacesBean.setCategoriasAdicionadas(categoriasInformadas);
		List<AgrupadorLocal> categoriasRetorno = categorizarProcessoActionFacesBean.getCategoriasAdicionadas();
		assertEquals(categoriasInformadas, categoriasRetorno);
	}


}
