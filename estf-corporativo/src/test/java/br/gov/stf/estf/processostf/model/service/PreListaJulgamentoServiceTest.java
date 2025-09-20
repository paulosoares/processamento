package br.gov.stf.estf.processostf.model.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.processostf.model.dataaccess.PreListaJulgamentoDao;
import br.gov.stf.estf.processostf.model.service.impl.PreListaJulgamentoServiceImpl;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

public class PreListaJulgamentoServiceTest {
	
	private PreListaJulgamentoService preListaJulgamentoService;
	
	@Mock
	private PreListaJulgamentoDao preListaJulgamentoDao;
	
	@Mock
	private PreListaJulgamentoObjetoIncidenteService preListaJulgamentoObjetoIncidenteService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		preListaJulgamentoService = new PreListaJulgamentoServiceImpl(preListaJulgamentoDao);
		Whitebox.setInternalState(preListaJulgamentoService, "preListaJulgamentoObjetoIncidenteService", preListaJulgamentoObjetoIncidenteService);
	}
	
	@Test
	public void testSetProcessoRevisado() throws DaoException, ServiceException {
		Processo processo = new Processo();
		processo.setId(12345L);
		
		PreListaJulgamento preLista = new PreListaJulgamento();
		
		PreListaJulgamentoObjetoIncidente relacionamento = new PreListaJulgamentoObjetoIncidente();
		relacionamento.setObjetoIncidente(processo);
		relacionamento.setPreListaJulgamento(preLista);
		relacionamento.setRevisado(Boolean.FALSE);
		
		preLista.setObjetosIncidentes(Arrays.asList(relacionamento));
		
		Usuario revisor = new Usuario();
		revisor.setId("paulo.soares");

		when(preListaJulgamentoObjetoIncidenteService.pesquisarProcessoEmLista(processo, preLista)).thenReturn(Arrays.asList(relacionamento));
		
		preListaJulgamentoService.alterarProcessoParaRevisado(processo, preLista, Boolean.TRUE, revisor);
		
		Assert.assertTrue(relacionamento.getRevisado());
	}

}
