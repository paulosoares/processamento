package br.gov.stf.estf.processostf.model.service;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.PreListaJulgamentoObjetoIncidenteDao;
import br.gov.stf.estf.processostf.model.service.impl.PreListaJulgamentoObjetoIncidenteServiceImpl;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

public class PreListaJulgamentoObjetoIncidenteServiceTest {
	
	private PreListaJulgamentoObjetoIncidenteService preListaJulgamentoObjetoIncidenteService;
	
	@Mock
	private PreListaJulgamentoObjetoIncidenteDao preListaJulgamentoObjetoIncidenteDao;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		preListaJulgamentoObjetoIncidenteService = new PreListaJulgamentoObjetoIncidenteServiceImpl(preListaJulgamentoObjetoIncidenteDao);
	}
	
	@Test
	public void testPesquisarProcessoEmLista() throws DaoException, ServiceException {
		Processo objetoIncidente = new Processo();
		objetoIncidente.setId(12345L);
		
		PreListaJulgamento preListaJulgamento = new PreListaJulgamento();
		preListaJulgamento.setId(123456L);

		preListaJulgamentoObjetoIncidenteService.pesquisarProcessoEmLista(objetoIncidente, preListaJulgamento);
		
		PreListaJulgamentoObjetoIncidente filtroPesquisa = new PreListaJulgamentoObjetoIncidente();
		filtroPesquisa.setObjetoIncidente(objetoIncidente);
		filtroPesquisa.setPreListaJulgamento(preListaJulgamento);
		
		Mockito.when(preListaJulgamentoObjetoIncidenteService.pesquisarPorExemplo(filtroPesquisa)).thenReturn(Arrays.asList(filtroPesquisa));
		
	}
}
