package br.jus.stf.estf.decisao.objetoincidente.web;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.service.ListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.util.ListaJulgamentoUI;

public class CancelarLiberacaoParaJulgamentoActionFacesBeanTest {

	@Mock
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Mock
	private ListaJulgamentoService listaJulgamentoService;
	
	
	private Set<ObjetoIncidenteDto> resources;
	private List<ListaJulgamento> listas;
	
	private CancelarLiberacaoParaJulgamentoActionFacesBean actionFacesBean;
	
	private AgrupadorRepo agrupadorRepo = new AgrupadorRepo();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		actionFacesBean = new CancelarLiberacaoParaJulgamentoActionFacesBean();
		Whitebox.setInternalState(actionFacesBean, "objetoIncidenteService", objetoIncidenteService);
		Whitebox.setInternalState(actionFacesBean, "listaJulgamentoService", listaJulgamentoService);
	}
	
	private void setUp_adicionaMensagemProcessoPendente(String andamento
													   ,Integer codAgendamento
													   ,Long idTipoAndamentoListaJulgamento
													   ,ListaJulgamento listaJulgamento) {
		String identificacaoDoObjetoIncidente = "ARE 950082 AgR-ED";
		String mensgem = "["+identificacaoDoObjetoIncidente+"]: O andamento " + andamento + " será lançado automaticamente.";
		
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);		
		
		Mockito.when(objetoIncidenteService.processoEmListaJulgamento(agendamento)).thenReturn(listaJulgamento);
		Mockito.when(objetoIncidenteService.getAndamentoProcessoCanceladoListaJulgamento(listaJulgamento,agendamento)).thenReturn(idTipoAndamentoListaJulgamento);
		
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn(identificacaoDoObjetoIncidente).when(actionFacesBean).getIdentificacaoObjetoIncidente(agendamento);
		Mockito.doNothing().when(actionFacesBean).addAviso(mensgem);
		
		actionFacesBean.adicionaMensagemProcessoPendente(agendamento);
		Mockito.verify(actionFacesBean, Mockito.times(1)).addAviso(mensgem);
	}	
	
	@Test
	public void testAdicionaMensagemProcessoPendente_emLista_andamentoMesa() {
		String andamento                      = CancelarLiberacaoParaJulgamentoActionFacesBean.MSG_RETIRADO_MESA;
		Integer codAgendamento                = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;
		Long idTipoAndamentoListaJulgamento   = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_MESA;
		ListaJulgamento listaJulgamento       = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		
		setUp_adicionaMensagemProcessoPendente(andamento
											  ,codAgendamento
											  ,idTipoAndamentoListaJulgamento
											  ,listaJulgamento);
	}

	@Test
	public void testAdicionaMensagemProcessoPendente_emLista_andamentoPauta() {
		String andamento                      = CancelarLiberacaoParaJulgamentoActionFacesBean.MSG_RETIRADO_PAUTA;
		Integer codAgendamento                = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;
		Long idTipoAndamentoListaJulgamento   = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_PAUTA;
		ListaJulgamento listaJulgamento       = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		
		setUp_adicionaMensagemProcessoPendente(andamento
				  ,codAgendamento
				  ,idTipoAndamentoListaJulgamento
				  ,listaJulgamento);
	}
	
	@Test
	public void testAdicionaMensagemProcessoPendente_foraDeLista_andamentoMesa() {
		String andamento                      = CancelarLiberacaoParaJulgamentoActionFacesBean.MSG_RETIRADO_MESA;
		Integer codAgendamento                = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;
		Long idTipoAndamentoListaJulgamento   = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_MESA;
		ListaJulgamento listaJulgamento       = null;
		
		setUp_adicionaMensagemProcessoPendente(andamento
				  ,codAgendamento
				  ,idTipoAndamentoListaJulgamento
				  ,listaJulgamento);
	}	
	
	@Test
	public void testAdicionaMensagemProcessoPendente_foraDeLista_andamentoPauta() {
		String andamento                      = CancelarLiberacaoParaJulgamentoActionFacesBean.MSG_RETIRADO_PAUTA;
		Integer codAgendamento                = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Long idTipoAndamentoListaJulgamento   = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_MESA;
		ListaJulgamento listaJulgamento       = null;
		
		setUp_adicionaMensagemProcessoPendente(andamento
				  ,codAgendamento
				  ,idTipoAndamentoListaJulgamento
				  ,listaJulgamento);
	}
	
	@Test
	public void testIsProcessoEmListaParaJulgamento() throws ServiceException{
		ObjetoIncidenteDto objetoIncidenteDto = new ObjetoIncidenteDto();
		ObjetoIncidente<?> recuperarObjetoIncidentePorId = agrupadorRepo.getObjetoIncidente(1, "100");
		ListaJulgamento lista = new ListaJulgamento();
		objetoIncidenteDto.setId((long) 1.0);
		resources = new HashSet<ObjetoIncidenteDto>();
		resources.add(objetoIncidenteDto);
		listas = new ArrayList<ListaJulgamento>();
		listas.add(lista);
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn(resources).when(actionFacesBean).jUnitGetResources();
		//Mockito.when(objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidenteDto.getId())).thenReturn(recuperarObjetoIncidentePorId);
		Mockito.doReturn(recuperarObjetoIncidentePorId).when(objetoIncidenteService).recuperarObjetoIncidentePorId(objetoIncidenteDto.getId());
		Mockito.doReturn(listas).when(listaJulgamentoService).pesquisar(recuperarObjetoIncidentePorId, true);
		boolean retorno = actionFacesBean.isProcessoEmListaParaJulgamento();
		assertEquals(true,retorno);
	}
}
