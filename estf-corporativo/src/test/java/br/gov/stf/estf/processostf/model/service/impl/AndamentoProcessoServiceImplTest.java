package br.gov.stf.estf.processostf.model.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.impl.SessaoServiceImplTestRepo;
import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoProcessoServiceImplTest {
	
	private AndamentoProcessoServiceImpl serviceImpl;
	
	private SessaoServiceImplTestRepo repo = new SessaoServiceImplTestRepo();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		serviceImpl = new AndamentoProcessoServiceImpl(null);
	}

	@Test
	public void testRecuperarProximoNumeroSequencia_ultimoZero() throws ServiceException {
		Long ultimoNumeroSequencia          = 0L;
		Long proximoNumeroSequenciaEsperado = 1L;
		
		Processo objetoIncidente = repo.getObjetoIncidente(1,"1111");		
		serviceImpl = Mockito.spy(serviceImpl);
		Mockito.doReturn(ultimoNumeroSequencia).when(serviceImpl).recuperarUltimoNumeroSequencia(objetoIncidente);		
		Long proximoNumeroSequenciaRetorno = serviceImpl.recuperarProximoNumeroSequencia(objetoIncidente);
		
		assertEquals(proximoNumeroSequenciaEsperado,proximoNumeroSequenciaRetorno);
	}
	
	@Test
	public void testRecuperarProximoNumeroSequencia_ultimoMil() throws ServiceException {
		Long ultimoNumeroSequencia          = 1000L;
		Long proximoNumeroSequenciaEsperado = 1001L;
		
		Processo objetoIncidente = repo.getObjetoIncidente(1,"1111");		
		serviceImpl = Mockito.spy(serviceImpl);
		Mockito.doReturn(ultimoNumeroSequencia).when(serviceImpl).recuperarUltimoNumeroSequencia(objetoIncidente);		
		Long proximoNumeroSequenciaRetorno = serviceImpl.recuperarProximoNumeroSequencia(objetoIncidente);
		
		assertEquals(proximoNumeroSequenciaEsperado,proximoNumeroSequenciaRetorno);
	}
	
}
