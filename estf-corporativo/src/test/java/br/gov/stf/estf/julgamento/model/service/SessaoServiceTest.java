package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.julgamento.model.dataaccess.ColegiadoDao;
import br.gov.stf.estf.julgamento.model.dataaccess.SessaoDao;
import br.gov.stf.estf.julgamento.model.service.impl.ColegiadoServiceImpl;
import br.gov.stf.estf.julgamento.model.service.impl.SessaoServiceImplTestRepo;
import br.gov.stf.estf.julgamento.model.service.impl.SessaoServiceImpl;
import br.gov.stf.framework.model.service.ServiceException;

public class SessaoServiceTest {

	private SessaoService sessaoService;
	
	@Mock
	private SessaoDao sessaoDao;
	
	@Mock
	private ColegiadoDao colegiadoDao;

	private ColegiadoService colegiadoService;
	
	private SessaoServiceImplTestRepo repo = new SessaoServiceImplTestRepo();
			
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		sessaoService = new SessaoServiceImpl(sessaoDao);	
		colegiadoService = new ColegiadoServiceImpl(colegiadoDao);
	}
	
	@Test
	public void testIniciarSessaoVirtual() {
		try{
			sessaoService.iniciarSessoesVirtuais();
		}catch(ServiceException e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFinalizarSessoesVirtuais(){
		try{
			sessaoService.finalizarSessoesVirtuais();
		}catch(ServiceException e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void pesquisarSessoesVirtuaisNaoIniciadasTest(){
		try{	
			this.setUp();
			Colegiado primeiraTurma = repo.getColegiado(Colegiado.PRIMEIRA_TURMA);
			List<Sessao> sessoes = sessaoService.pesquisarSessoesVirtuaisNaoIniciadas(primeiraTurma, null, TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO);
			if (sessoes != null){
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
