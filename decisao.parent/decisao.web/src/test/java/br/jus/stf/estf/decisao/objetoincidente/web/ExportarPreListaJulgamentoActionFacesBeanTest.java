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

import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.ProcessoListaJulgamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.support.PreListaJulgamentoReportSupport;
import br.jus.stf.estf.decisao.support.service.ConfiguracaoSistemaService;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;

public class ExportarPreListaJulgamentoActionFacesBeanTest {

	@Mock
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Mock
	private PreListaJulgamentoService preListaJulgamentoService;
	
	@Mock
	private ConfiguracaoSistemaService configuracaoSistemaService;
	
	@Mock
	private UsuarioLogadoService usuarioLogadoService;
	
	@Mock
	private ProcessoListaJulgamentoService processoListaJulgamentoService;
	
	private ExportarPreListaJulgamentoActionFacesBean exportarPreListaJulgamentoActionFacesBean;
	
	private AgrupadorRepo agrupadorRepo = new AgrupadorRepo();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		exportarPreListaJulgamentoActionFacesBean = new ExportarPreListaJulgamentoActionFacesBean();
		Whitebox.setInternalState(exportarPreListaJulgamentoActionFacesBean, "objetoIncidenteService", objetoIncidenteService);
		Whitebox.setInternalState(exportarPreListaJulgamentoActionFacesBean, "preListaJulgamentoService", preListaJulgamentoService);
		Whitebox.setInternalState(exportarPreListaJulgamentoActionFacesBean, "configuracaoSistemaService", configuracaoSistemaService);
		Whitebox.setInternalState(exportarPreListaJulgamentoActionFacesBean, "usuarioLogadoService", usuarioLogadoService);
		Whitebox.setInternalState(exportarPreListaJulgamentoActionFacesBean, "processoListaJulgamentoService", processoListaJulgamentoService);
	}
	
	
	@Test (expected = ServiceException.class)
	public void load_erro() throws ServiceException {
		exportarPreListaJulgamentoActionFacesBean = Mockito.spy(exportarPreListaJulgamentoActionFacesBean);
		Mockito.doThrow(ServiceException.class).when(exportarPreListaJulgamentoActionFacesBean).load();
		
		exportarPreListaJulgamentoActionFacesBean.load();
		Mockito.verify(exportarPreListaJulgamentoActionFacesBean).addError(ExportarPreListaJulgamentoActionFacesBean.MSG_ERRO_GERAR);
	}	
	
	

	@SuppressWarnings("unused")
	@Test (expected = ServiceException.class)
	public void convertePrelistaParaListaProcessos_erro() throws ServiceException {
		long idLista = (long) 1;
		
		PreListaJulgamento preLista = agrupadorRepo.getPreListaJulgamento(1, "Nome lista");
		List<PreListaJulgamentoObjetoIncidente> preListJulgObjetoIncidentesVazia = new ArrayList<PreListaJulgamentoObjetoIncidente>();
		preLista.setObjetosIncidentes(preListJulgObjetoIncidentesVazia);
		
		Mockito.when(preListaJulgamentoService.recuperarPorId(idLista)).thenReturn(preLista);
		Mockito.when(configuracaoSistemaService.isOrdenacaoNumerica()).thenReturn(false);
		
		List<ObjetoIncidente<?>> listaRetorno = exportarPreListaJulgamentoActionFacesBean.convertePrelistaParaListaProcessos(idLista);
		Mockito.verify(exportarPreListaJulgamentoActionFacesBean).addError(ExportarPreListaJulgamentoActionFacesBean.MSG_ERRO_GERAR);
	}

	@Test 
	public void convertePrelistaParaListaProcessos_ok() throws ServiceException {
		long idLista = (long) 1;
		
		PreListaJulgamento preLista = agrupadorRepo.getPreListaJulgamento(1, "Nome lista");
		List<PreListaJulgamentoObjetoIncidente> preListJulgObjetoIncidentesVazia = new ArrayList<PreListaJulgamentoObjetoIncidente>();
		preListJulgObjetoIncidentesVazia.add(agrupadorRepo.getPreListaJulgamentoObjetoIncidente(1, 1));
		preLista.setObjetosIncidentes(preListJulgObjetoIncidentesVazia);
		
		Mockito.when(preListaJulgamentoService.recuperarPorId(idLista)).thenReturn(preLista);
		Mockito.when(configuracaoSistemaService.isOrdenacaoNumerica()).thenReturn(false);
		
		List<ObjetoIncidente<?>> listaRetorno = exportarPreListaJulgamentoActionFacesBean.convertePrelistaParaListaProcessos(idLista);
		assertEquals(preListJulgObjetoIncidentesVazia.size(), listaRetorno.size());
	}
	
	@Test 
	public void  recuperarMinistroDaLista_getTextoObservacaoLista() throws ServiceException {
		int idObjetoIncidente = 1;
		String observacao = "texto observação";
				
		Processo objetoIncidente = agrupadorRepo.getObjetoIncidente(idObjetoIncidente, "111");

		ProcessoListaJulgamento incidenteLista = new ProcessoListaJulgamento();
		incidenteLista.setObservacao(observacao);
		
		long idListaJulgamento = 1L;
		String tipoLista = ExportarPreListaJulgamentoActionFacesBean.TIPO_LISTA;
		PreListaJulgamentoReportSupport preListaJulgamentoReportSupport = getPreListaJulgamentoReportSupport(tipoLista, idListaJulgamento);
		
		Mockito.when(objetoIncidenteService.recuperarPorId(Long.valueOf(idObjetoIncidente))).thenReturn(objetoIncidente);
		
		exportarPreListaJulgamentoActionFacesBean = Mockito.spy(exportarPreListaJulgamentoActionFacesBean);
		Mockito.doReturn(incidenteLista).when(exportarPreListaJulgamentoActionFacesBean).recuperarOrdemDoItemNaLista(idListaJulgamento, objetoIncidente);
		
		String observacaoRetorno = exportarPreListaJulgamentoActionFacesBean.getTextoObservacao(preListaJulgamentoReportSupport, objetoIncidente, tipoLista); 
		assertEquals(observacao,observacaoRetorno);
	}

	public PreListaJulgamentoReportSupport getPreListaJulgamentoReportSupport(String tipoLista, long idListaJulgamento) {
		PreListaJulgamentoReportSupport preListaJulgamentoReportSupport = new PreListaJulgamentoReportSupport();
		preListaJulgamentoReportSupport.setIdListaJulgamento(idListaJulgamento);
		preListaJulgamentoReportSupport.setTipoLista(tipoLista);
		return preListaJulgamentoReportSupport;
	}
	
	@Test 
	public void  recuperarMinistroDaLista_getTextoObservacaoPreLista() throws ServiceException {
		int idObjetoIncidente = 1;
		String tipoLista = ExportarPreListaJulgamentoActionFacesBean.TIPO_PRE_LISTA;
		String observacao = "texto observação";
				
		Processo objetoIncidente = agrupadorRepo.getObjetoIncidente(idObjetoIncidente, "111");
		PreListaJulgamentoObjetoIncidente incidenteLista = new PreListaJulgamentoObjetoIncidente();
		incidenteLista.setObservacao(observacao);
		
		Mockito.when(objetoIncidenteService.recuperarPorId(Long.valueOf(idObjetoIncidente))).thenReturn(objetoIncidente);
		Mockito.when(preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidente(objetoIncidente)).thenReturn(incidenteLista);
		
		String observacaoRetorno = exportarPreListaJulgamentoActionFacesBean.getTextoObservacao(null, objetoIncidente, tipoLista);
		assertEquals(observacao,observacaoRetorno);
	}	
	
	@Test 
	public void  recuperarMinistroDaLista_null() throws ServiceException {
		Mockito.when(usuarioLogadoService.getMinistro()).thenReturn(null);
		
		String MinistroRetorno = exportarPreListaJulgamentoActionFacesBean.recuperarMinistroDaLista();
		assertEquals(null,MinistroRetorno);
	}	
	
	@Test 
	public void  recuperarMinistroDaLista_MinRobertoBarroso() throws ServiceException {
		Ministro m = Mockito.mock(Ministro.class, Mockito.RETURNS_DEEP_STUBS);
		Mockito.when(usuarioLogadoService.getMinistro()).thenReturn(m);
		Mockito.when(m.getId()).thenReturn(ExportarPreListaJulgamentoActionFacesBean.idMinistroRobertoBarro);
		
		String MinistroRetorno = exportarPreListaJulgamentoActionFacesBean.recuperarMinistroDaLista();
		assertEquals(ExportarPreListaJulgamentoActionFacesBean.MIN_ROBERTO_BARROSO,MinistroRetorno);
	}
	
	@Test 
	public void  recuperarMinistroDaLista_OutroMinistro() throws ServiceException {
		String MinistroEsperado = "Ministro Qualquer";
		
		Ministro m = Mockito.mock(Ministro.class, Mockito.RETURNS_DEEP_STUBS);
		Mockito.when(usuarioLogadoService.getMinistro()).thenReturn(m);
		Mockito.when(m.getId()).thenReturn((long) 1);
		Mockito.when(m.getNomeMinistroCapsulado(true, false)).thenReturn(MinistroEsperado);
		
		
		String MinistroRetorno = exportarPreListaJulgamentoActionFacesBean.recuperarMinistroDaLista();
		assertEquals(MinistroEsperado,MinistroRetorno);
	}	

	@Test 
	public void  getNumItemLista_tipoNaoLista() throws ServiceException {
		Processo oi01 = agrupadorRepo.getObjetoIncidente(1, "111");
		Processo oi02 = agrupadorRepo.getObjetoIncidente(2, "111");
		Processo oi03 = agrupadorRepo.getObjetoIncidente(3, "111");
		
		List<ObjetoIncidente<?>> listaProcessos = new ArrayList<ObjetoIncidente<?>>();
		listaProcessos.add(oi01);
		listaProcessos.add(oi02);
		listaProcessos.add(oi03);
		
		Integer numItemListaEsperado = 3;
		
		PreListaJulgamentoReportSupport preListaJulgamentoReportSupport = getPreListaJulgamentoReportSupport(null,1L);
		
		Integer numItemListaRetorno = exportarPreListaJulgamentoActionFacesBean.getNumItemLista(preListaJulgamentoReportSupport, listaProcessos, oi03);
		assertEquals(numItemListaEsperado,numItemListaRetorno);
	}
	
	@Test 
	public void  getNumItemLista_tipoLista() throws ServiceException {
		Integer numItemListaEsperado = 83;
		
		Processo oi01 = agrupadorRepo.getObjetoIncidente(1, "111");
		Processo oi02 = agrupadorRepo.getObjetoIncidente(2, "222");
		Processo oi03 = agrupadorRepo.getObjetoIncidente(3, "333");
		
		List<ObjetoIncidente<?>> listaProcessos = new ArrayList<ObjetoIncidente<?>>();
		listaProcessos.add(oi01);
		listaProcessos.add(oi02);
		listaProcessos.add(oi03);
				
		Processo objetoIncidente = agrupadorRepo.getObjetoIncidente(1, "111");		
		
		ProcessoListaJulgamento incidenteLista = new ProcessoListaJulgamento();
		incidenteLista.setOrdemNaLista(numItemListaEsperado);
		
		long idListaJulgamento = 1L;
		String tipoLista = ExportarPreListaJulgamentoActionFacesBean.TIPO_LISTA;
		PreListaJulgamentoReportSupport preListaJulgamentoReportSupport = getPreListaJulgamentoReportSupport(tipoLista, idListaJulgamento);
		
		Mockito.when(processoListaJulgamentoService.recuperar(oi01)).thenReturn(incidenteLista);				
		
		exportarPreListaJulgamentoActionFacesBean = Mockito.spy(exportarPreListaJulgamentoActionFacesBean);
		Mockito.doReturn(incidenteLista).when(exportarPreListaJulgamentoActionFacesBean).recuperarOrdemDoItemNaLista(idListaJulgamento, objetoIncidente);
		
		Integer numItemListaRetorno = exportarPreListaJulgamentoActionFacesBean.getNumItemLista(preListaJulgamentoReportSupport, listaProcessos, oi01);
		assertEquals(numItemListaEsperado,numItemListaRetorno);
	}	
	
	@Test 
	public void  limpaTexto_semEspaco() throws ServiceException {
		String textoInformado = "";
		String textoEsperado  = null;
		String textoRetorno = exportarPreListaJulgamentoActionFacesBean.limpaTexto(textoInformado);
		assertEquals(textoEsperado,textoRetorno);
	}
	
	@Test 
	public void  limpaTexto_comEspaco() throws ServiceException {
		String textoInformado = " ";
		String textoEsperado  = null;
		String textoRetorno = exportarPreListaJulgamentoActionFacesBean.limpaTexto(textoInformado);
		assertEquals(textoEsperado,textoRetorno);
	}
	
	@Test 
	public void  limpaTexto_textoQualquer() throws ServiceException {
		String textoInformado = "Texto Qualquer";
		String textoEsperado  = "Texto Qualquer";
		String textoRetorno = exportarPreListaJulgamentoActionFacesBean.limpaTexto(textoInformado);
		assertEquals(textoEsperado,textoRetorno);
	}	
	
}