package br.jus.stf.estf.decisao.objetoincidente.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.entidade.processostf.TipoRecursoProcesso;
import br.gov.stf.estf.entidade.processostf.enuns.TipoPolo;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.support.ConsultaDeAgendamentoVO;
import br.jus.stf.estf.decisao.objetoincidente.support.DadosAgendamentoDto;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoPrecisaDeConfirmacaoException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoTipoRecursoPodePlanarioVirtualException;
import br.jus.stf.estf.decisao.objetoincidente.web.AgrupadorRepo;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.util.ListaJulgamentoUI;

public class ObjetoIncidenteServiceImplTest {
	
	@Mock
	private SetorService setorService;
	
	@Mock
	private AgendamentoService agendamentoService;	
	
	private ObjetoIncidenteServiceImpl objetoIncidenteService;
	
	private AgrupadorRepo agrupadorRepo = new AgrupadorRepo();	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		objetoIncidenteService = new ObjetoIncidenteServiceImpl();
		Whitebox.setInternalState(objetoIncidenteService, "setorService", setorService);
		Whitebox.setInternalState(objetoIncidenteService, "agendamentoService", agendamentoService);
	}
	
	Principal getPrincipal(){
		Setor setor = new Setor();
		Ministro  ministro  = new Ministro();
		ministro.setSetor(setor);
		Principal principal = new Principal();
		principal.setMinistro(ministro);		
		return principal;
	}
	
	@Test
	public void test_isAgendamentoParaIndice_esperadoTrue_AGENDAMENTO_INDICE() throws ServiceException {		
		Integer codAgendamento = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		boolean retorno = objetoIncidenteService.isAgendamentoParaIndice(agendamento);
		assertEquals(true,retorno);
	}
	
	@Test
	public void test_isAgendamentoParaIndice_esperadoTrue_AGENDAMENTO_JULGAMENTO() throws ServiceException {		
		Integer codAgendamento = Agendamento.COD_MATERIA_AGENDAMENTO_JULGAMENTO;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		boolean retorno = objetoIncidenteService.isAgendamentoParaIndice(agendamento);
		assertEquals(true,retorno);
	}
	
	@Test
	public void test_isAgendamentoParaIndice_esperadoFalse_AGENDAMENTO_PAUTA() throws ServiceException {		
		Integer codAgendamento = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		boolean retorno = objetoIncidenteService.isAgendamentoParaIndice(agendamento);
		assertEquals(false,retorno);
	}	

	boolean setUp_isAgendamentoDaMateria(Integer codAgendamento) {
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);		
		boolean retorno = objetoIncidenteService.isAgendamentoParaPauta(agendamento);
		return retorno;
	}	
	
	@Test
	public void test_isAgendamentoParaPauta_esperadoTrue_AGENDAMENTO_PAUTA() throws ServiceException {		
		Integer codAgendamento = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;		
		boolean retorno = setUp_isAgendamentoDaMateria(codAgendamento);
		assertEquals(true,retorno);
	}
	
	@Test
	public void test_isAgendamentoParaPauta_esperadoFalse_AGENDAMENTO_INDICE() throws ServiceException {		
		Integer codAgendamento = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;		
		boolean retorno = setUp_isAgendamentoDaMateria(codAgendamento);
		assertEquals(false,retorno);
	}
	
	@Test
	public void test_isAgendamentoParaPauta_esperadoFalse_AGENDAMENTO_JULGAMENTO() throws ServiceException {		
		Integer codAgendamento = Agendamento.COD_MATERIA_AGENDAMENTO_JULGAMENTO;		
		boolean retorno = setUp_isAgendamentoDaMateria(codAgendamento);
		assertEquals(false,retorno);
	}	
	
	boolean setUp_isIncluirAndamentoDeRetiradoDeMesa(boolean isSetorGabinete
			                                        ,boolean isAgendamentoParaIndice) throws ServiceException {
		
		Agendamento agendamento = Mockito.mock(Agendamento.class, Mockito.RETURNS_DEEP_STUBS);
		Setor setor             = Mockito.mock(Setor.class, Mockito.RETURNS_DEFAULTS);
		
		Mockito.when(setorService.isSetorGabinete(setor)).thenReturn(isSetorGabinete);
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(isAgendamentoParaIndice).when(objetoIncidenteService).isAgendamentoParaIndice(agendamento);
		
		boolean retorno = objetoIncidenteService.isIncluirAndamentoDeRetiradoDeMesa(agendamento,setor);
		return retorno;
	}	
	
	@Test
	public void test_isIncluirAndamentoDeRetiradoDeMesa_esperadoFalse_entradaFalseFalse() throws ServiceException {
		boolean isSetorGabinete         = false;
		boolean isAgendamentoParaIndice = false;
		
		boolean retorno = setUp_isIncluirAndamentoDeRetiradoDeMesa(isSetorGabinete, isAgendamentoParaIndice);
		assertEquals(false,retorno);
	}

	@Test
	public void test_isIncluirAndamentoDeRetiradoDeMesa_esperadoFalse_entradaFalseTrue() throws ServiceException {
		boolean isSetorGabinete         = false;
		boolean isAgendamentoParaIndice = true;
		
		boolean retorno = setUp_isIncluirAndamentoDeRetiradoDeMesa(isSetorGabinete, isAgendamentoParaIndice);
		assertEquals(false,retorno);
	}
	
	@Test
	public void test_isIncluirAndamentoDeRetiradoDeMesa_esperadoFalse_entradaTrueFalse() throws ServiceException {
		boolean isSetorGabinete         = true;
		boolean isAgendamentoParaIndice = false;
		
		boolean retorno = setUp_isIncluirAndamentoDeRetiradoDeMesa(isSetorGabinete, isAgendamentoParaIndice);
		assertEquals(false,retorno);
	}
	
	@Test
	public void test_isIncluirAndamentoDeRetiradoDeMesa_esperadoTrue_entradaTrueTrue() throws ServiceException {
		boolean isSetorGabinete         = true;
		boolean isAgendamentoParaIndice = true;
		
		boolean retorno = setUp_isIncluirAndamentoDeRetiradoDeMesa(isSetorGabinete, isAgendamentoParaIndice);
		assertEquals(true,retorno);
	}
	
	boolean setUp_isIncluirAndamentoDeRetiradoDePauta(boolean isSetorGabinete
			                                         ,boolean isAgendamentoParaIndice) throws ServiceException {

		Agendamento agendamento = Mockito.mock(Agendamento.class,Mockito.RETURNS_DEEP_STUBS);
		Setor setor = Mockito.mock(Setor.class, Mockito.RETURNS_DEFAULTS);

		Mockito.when(setorService.isSetorGabinete(setor)).thenReturn(isSetorGabinete);
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(isAgendamentoParaIndice).when(objetoIncidenteService).isAgendamentoParaPauta(agendamento);

		boolean retorno = objetoIncidenteService.isIncluirAndamentoDeRetiradoDePauta(agendamento, setor);
		return retorno;
	}

	@Test
	public void test_isIncluirAndamentoDeRetiradoDePauta_esperadoFalse_entradaFalseFalse() throws ServiceException {
		boolean isSetorGabinete = false;
		boolean isAgendamentoParaIndice = false;

		boolean retorno = setUp_isIncluirAndamentoDeRetiradoDePauta(isSetorGabinete, isAgendamentoParaIndice);
		assertEquals(false, retorno);
	}

	@Test
	public void test_isIncluirAndamentoDeRetiradoDePauta_esperadoFalse_entradaFalseTrue() throws ServiceException {
		boolean isSetorGabinete = false;
		boolean isAgendamentoParaIndice = true;

		boolean retorno = setUp_isIncluirAndamentoDeRetiradoDePauta(isSetorGabinete, isAgendamentoParaIndice);
		assertEquals(false, retorno);
	}

	@Test
	public void test_isIncluirAndamentoDeRetiradoDePauta_esperadoFalse_entradaTrueFalse() throws ServiceException {
		boolean isSetorGabinete = true;
		boolean isAgendamentoParaIndice = false;

		boolean retorno = setUp_isIncluirAndamentoDeRetiradoDePauta(isSetorGabinete, isAgendamentoParaIndice);
		assertEquals(false, retorno);
	}

	@Test
	public void test_isIncluirAndamentoDeRetiradoDePauta_esperadoTrue_entradaTrueTrue() throws ServiceException {
		boolean isSetorGabinete = true;
		boolean isAgendamentoParaIndice = true;

		boolean retorno = setUp_isIncluirAndamentoDeRetiradoDePauta(isSetorGabinete, isAgendamentoParaIndice);
		assertEquals(true, retorno);
	}
	
	void setUp_defineAndamentoProcessoCancelado(boolean incluirAndamentoDeRetiradoDeMesa
			                                   ,boolean isIncluirAndamentoDeRetiradoDePauta
			                                   ,Agendamento agendamento
			                                   ,ListaJulgamento listaJulgamento
			                                   ,Principal usuario
			                                   ,Setor setor) throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(listaJulgamento).when(objetoIncidenteService).processoEmListaJulgamento(agendamento);
		Mockito.doNothing().when(objetoIncidenteService).defineAndamentoProcessoCanceladoListaJulgamento(listaJulgamento,agendamento,usuario);
		Mockito.doReturn(incluirAndamentoDeRetiradoDeMesa).when(objetoIncidenteService).isIncluirAndamentoDeRetiradoDeMesa(agendamento, setor);
		Mockito.doReturn(isIncluirAndamentoDeRetiradoDePauta).when(objetoIncidenteService).isIncluirAndamentoDeRetiradoDePauta(agendamento, setor);
		Mockito.doNothing().when(objetoIncidenteService).inserirAndamentoDeRetiradoDeMesa(agendamento, usuario);
		Mockito.doNothing().when(objetoIncidenteService).inserirAndamentoDeRetiradoDePauta(agendamento, usuario, "");
	}
	
	@Test
	public void test_defineAndamentoProcessoCancelado_listaJulgamento() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean incluirAndamentoDeRetiradoDeMesa    = false;
		boolean isIncluirAndamentoDeRetiradoDePauta = false;		
		
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		ListaJulgamento listaJulgamento = new ListaJulgamento();
		Principal usuario       = this.getPrincipal();
		Setor setor             = usuario.getMinistro().getSetor();
		
		this.setUp_defineAndamentoProcessoCancelado(incluirAndamentoDeRetiradoDeMesa
				   								   ,isIncluirAndamentoDeRetiradoDePauta
				   								   ,agendamento
				   								   ,listaJulgamento
				   								   ,usuario
				   								   ,setor);		
		
		objetoIncidenteService.inserirAndamentoProcessoCancelado(usuario,agendamento,"");
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDeMesa(agendamento,usuario);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDePauta(agendamento,usuario,"");
	}	
	
	@Test
	public void test_defineAndamentoProcessoCancelado_soProcesso_andamentoRetiradoMesa() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean incluirAndamentoDeRetiradoDeMesa    = true;
		boolean isIncluirAndamentoDeRetiradoDePauta = false;
		
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		ListaJulgamento listaJulgamento = null;
		Principal usuario       = this.getPrincipal();
		Setor setor             = usuario.getMinistro().getSetor();
		
		this.setUp_defineAndamentoProcessoCancelado(incluirAndamentoDeRetiradoDeMesa
												   ,isIncluirAndamentoDeRetiradoDePauta
												   ,agendamento
												   ,listaJulgamento
												   ,usuario
												   ,setor);		
		
		objetoIncidenteService.inserirAndamentoProcessoCancelado(usuario,agendamento,"");
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).defineAndamentoProcessoCanceladoListaJulgamento(listaJulgamento,agendamento,usuario);
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).inserirAndamentoDeRetiradoDeMesa(agendamento,usuario);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDePauta(agendamento,usuario,"");
	}
	
	@Test
	public void test_defineAndamentoProcessoCancelado_soProcesso_andamentoRetiradoPauta() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean incluirAndamentoDeRetiradoDeMesa    = false;
		boolean isIncluirAndamentoDeRetiradoDePauta = true;
		
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		ListaJulgamento listaJulgamento = null;
		Principal usuario       = this.getPrincipal();
		Setor setor             = usuario.getMinistro().getSetor();
		
		this.setUp_defineAndamentoProcessoCancelado(incluirAndamentoDeRetiradoDeMesa
				   								   ,isIncluirAndamentoDeRetiradoDePauta
				   								   ,agendamento
				   								   ,listaJulgamento
				   								   ,usuario
				   								   ,setor);
		
		objetoIncidenteService.inserirAndamentoProcessoCancelado(usuario,agendamento,"");
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).defineAndamentoProcessoCanceladoListaJulgamento(listaJulgamento,agendamento,usuario);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDeMesa(agendamento,usuario);
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).inserirAndamentoDeRetiradoDePauta(agendamento,usuario,"");
	}
	
	@Test
	public void test_defineAndamentoProcessoCancelado_nenhumAndamento() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean incluirAndamentoDeRetiradoDeMesa    = false;
		boolean isIncluirAndamentoDeRetiradoDePauta = false;
		
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		ListaJulgamento listaJulgamento = null;
		Principal usuario       = this.getPrincipal();
		Setor setor             = usuario.getMinistro().getSetor();
		
		this.setUp_defineAndamentoProcessoCancelado(incluirAndamentoDeRetiradoDeMesa
				   								   ,isIncluirAndamentoDeRetiradoDePauta
				   								   ,agendamento
				   								   ,listaJulgamento
				   								   ,usuario
				   								   ,setor);
		
		objetoIncidenteService.inserirAndamentoProcessoCancelado(usuario,agendamento,"");
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).defineAndamentoProcessoCanceladoListaJulgamento(listaJulgamento,agendamento,usuario);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDeMesa(agendamento,usuario);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDePauta(agendamento,usuario,"");
	}	

	private void setUp_defineAndamentoProcessoCanceladoListaJulgamento(boolean isProcessoEmListaJulgamentoVirtual
			                                                          ,String tipoAmbiente
																	  ,Long idTipoAndamento
																	  ,Agendamento agendamento
																	  ,Principal usuario) throws ProcessoPrecisaDeConfirmacaoException, ServiceException {
		Andamento andamento = new Andamento();
		andamento.setId(idTipoAndamento);
		ListaJulgamento listaJulgamento = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		listaJulgamento.getSessao().setTipoAmbiente(tipoAmbiente);
		listaJulgamento.setAndamentoLiberacao(andamento);
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doNothing().when(objetoIncidenteService).validarAlterarListaJulgamentoVirtual(listaJulgamento);
		Mockito.doReturn(idTipoAndamento).when(objetoIncidenteService).getAndamentoProcessoCanceladoListaJulgamento(listaJulgamento,agendamento);
		Mockito.doNothing().when(objetoIncidenteService).inserirAndamentoDeRetiradoDeMesa(agendamento, usuario);
		Mockito.doNothing().when(objetoIncidenteService).inserirAndamentoDeRetiradoDePauta(agendamento, usuario,tipoAmbiente);
		
		objetoIncidenteService.defineAndamentoProcessoCanceladoListaJulgamento(listaJulgamento,agendamento,usuario);
	}
	
	@Test
	public void test_defineAndamentoProcessoCanceladoListaJulgamento_julgamentoVirtual() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean isProcessoEmListaJulgamentoVirtual  = true;
		String tipoAmbiente = null;
		if(isProcessoEmListaJulgamentoVirtual){
			tipoAmbiente = TipoAmbienteConstante.VIRTUAL.getSigla();
		}else{
			tipoAmbiente = TipoAmbienteConstante.PRESENCIAL.getSigla();
		}		
		Long idTipoAndamento = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_PAUTA;
		
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		Principal usuario       = this.getPrincipal();

		this.setUp_defineAndamentoProcessoCanceladoListaJulgamento(isProcessoEmListaJulgamentoVirtual
																  ,tipoAmbiente
															      ,idTipoAndamento
															      ,agendamento
															      ,usuario);
		
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).inserirAndamentoDeRetiradoDePauta(agendamento,usuario,tipoAmbiente);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDeMesa(agendamento,usuario);
	}

	@Test
	public void test_defineAndamentoProcessoCanceladoListaJulgamento_julgamentoPresencial_andamentoMesa() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean isProcessoEmListaJulgamentoVirtual  = false;
		String tipoAmbiente = null;
		if(isProcessoEmListaJulgamentoVirtual){
			tipoAmbiente = TipoAmbienteConstante.VIRTUAL.getSigla();
		}else{
			tipoAmbiente = TipoAmbienteConstante.PRESENCIAL.getSigla();
		}
		Long idTipoAndamento = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_MESA;
		
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		Principal usuario       = this.getPrincipal();

		this.setUp_defineAndamentoProcessoCanceladoListaJulgamento(isProcessoEmListaJulgamentoVirtual
				                                                  ,tipoAmbiente
															      ,idTipoAndamento
															      ,agendamento
															      ,usuario);
		
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDePauta(agendamento,usuario,tipoAmbiente);
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).inserirAndamentoDeRetiradoDeMesa(agendamento,usuario);
	}
	
	@Test
	public void test_defineAndamentoProcessoCanceladoListaJulgamento_julgamentoPresencial_andamentoPauta() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean isProcessoEmListaJulgamentoVirtual  = false;
		String tipoAmbiente = null;
		if(isProcessoEmListaJulgamentoVirtual){
			tipoAmbiente = TipoAmbienteConstante.VIRTUAL.getSigla();
		}else{
			tipoAmbiente = TipoAmbienteConstante.PRESENCIAL.getSigla();
		}		
		Long idTipoAndamento = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_PAUTA;
		
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		Principal usuario       = this.getPrincipal();

		this.setUp_defineAndamentoProcessoCanceladoListaJulgamento(isProcessoEmListaJulgamentoVirtual
																  ,tipoAmbiente
															      ,idTipoAndamento
															      ,agendamento
															      ,usuario);
		
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).inserirAndamentoDeRetiradoDePauta(agendamento,usuario,tipoAmbiente);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).inserirAndamentoDeRetiradoDeMesa(agendamento,usuario);
	}
		
	private Long setUp_getAndamentoProcessoCanceladoListaJulgamento(boolean isProcessoEmListaJulgamentoVirtual, Long idTipoAndamento) {
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);
		Andamento andamento = new Andamento();
		andamento.setId(idTipoAndamento);
		ListaJulgamento listaJulgamento = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		listaJulgamento.setAndamentoLiberacao(andamento);
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(isProcessoEmListaJulgamentoVirtual).when(objetoIncidenteService).isProcessoEmListaJulgamentoVirtual(listaJulgamento);
		
		Long retorno = objetoIncidenteService.getAndamentoProcessoCanceladoListaJulgamento(listaJulgamento,agendamento);
		return retorno;
	}
	
	@Test
	public void test_getAndamentoProcessoCanceladoListaJulgamento_virtual() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean isProcessoEmListaJulgamentoVirtual  = true;
		Long idTipoAndamento = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_PAUTA;
		
		Long retorno = setUp_getAndamentoProcessoCanceladoListaJulgamento(isProcessoEmListaJulgamentoVirtual, idTipoAndamento);
		assertEquals(ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_PAUTA, retorno);
	}
	
	@Test
	public void test_getAndamentoProcessoCanceladoListaJulgamento_persencialPauta() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean isProcessoEmListaJulgamentoVirtual  = false;
		Long idTipoAndamento = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_PAUTA;
		
		Long retorno = setUp_getAndamentoProcessoCanceladoListaJulgamento(isProcessoEmListaJulgamentoVirtual, idTipoAndamento);
		assertEquals(ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_PAUTA, retorno);
	}
	
	@Test
	public void test_getAndamentoProcessoCanceladoListaJulgamento_persencialMesa() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		boolean isProcessoEmListaJulgamentoVirtual  = false;
		Long idTipoAndamento = ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_MESA;
		
		Long retorno = setUp_getAndamentoProcessoCanceladoListaJulgamento(isProcessoEmListaJulgamentoVirtual, idTipoAndamento);
		assertEquals(ListaJulgamentoUI.TIPO_ANDAMENTO_LIBERACAO_MESA, retorno);
	}	
	
	@Test
	public void test_isProcessoEmListaJulgamentoVirtual_esperadoFalse_SessaoPresencial() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		ListaJulgamento listaJulgamento01 = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		boolean retorno = objetoIncidenteService.isProcessoEmListaJulgamentoVirtual(listaJulgamento01);	
		assertEquals(false, retorno);
	}
	
	@Test
	public void test_isProcessoEmListaJulgamentoVirtual_esperadoFalse_listasVazia() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		ListaJulgamento listaJulgamento01 = null;
		
		boolean retorno = objetoIncidenteService.isProcessoEmListaJulgamentoVirtual(listaJulgamento01);	
		assertEquals(false, retorno);
	}
	
	@Test
	public void test_isProcessoEmListaJulgamentoVirtual_esperadoFalse_listasNull() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		ListaJulgamento listaJulgamento01 = null;		
		boolean retorno = objetoIncidenteService.isProcessoEmListaJulgamentoVirtual(listaJulgamento01);	
		assertEquals(false, retorno);
	}	
	
	@Test
	public void test_isProcessoEmListaJulgamentoVirtual_esperadoTrue_SessaoVirtual() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		Sessao sessao = agrupadorRepo.getSessao(1L);
		sessao.setTipoAmbiente(TipoAmbienteConstante.VIRTUAL.getSigla());
		ListaJulgamento listaJulgamento01 = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		listaJulgamento01.setSessao(sessao);
		
		boolean retorno = objetoIncidenteService.isProcessoEmListaJulgamentoVirtual(listaJulgamento01);	
		assertEquals(true, retorno);
	}
	
	private ListaJulgamento setUp_validarAlterarListaJulgamentoVirtual(String stringDateSessao, String stringDataHoje) 
			throws ParseException
			     , ServiceException
			     , ProcessoPrecisaDeConfirmacaoException {
		Date dateDataSessao                   = DataUtil.string2Date(stringDateSessao,true);
		GregorianCalendar calendarDateSessao  = DataUtil.string2GregorianCalendar(stringDateSessao);		
		GregorianCalendar calendarDataHoje    = DataUtil.string2GregorianCalendar(stringDataHoje);
		
		Sessao sessao = agrupadorRepo.getSessao(1L);
		sessao.setDataInicio(dateDataSessao);
		sessao.setDataPrevistaInicio(dateDataSessao);
		
		ListaJulgamento listDeListaJulgamento = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		listDeListaJulgamento.setSessao(sessao);
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(calendarDateSessao).when(objetoIncidenteService).date2GregorianCalendar(dateDataSessao);
		Mockito.doReturn(calendarDataHoje).when(objetoIncidenteService).getNow();		
		
		return listDeListaJulgamento;
	}
	
	@Test (expected = ProcessoPrecisaDeConfirmacaoException.class)
	public void validarAlterarListaJulgamentoVirtual_sessaoIniciada() throws ServiceException, ParseException, ProcessoPrecisaDeConfirmacaoException{
		String stringDateSessao = "26/08/2016 00:00:00";
		String stringDataHoje   = "30/08/2016 00:00:00";
		
		ListaJulgamento listDeListaJulgamento = setUp_validarAlterarListaJulgamentoVirtual(stringDateSessao, stringDataHoje);
		objetoIncidenteService.validarAlterarListaJulgamentoVirtual(listDeListaJulgamento);
	}

	@Test (expected = ProcessoPrecisaDeConfirmacaoException.class)
	public void validarAlterarListaJulgamentoVirtual_sessaoIniciadaMesmoDia() throws ServiceException, ParseException, ProcessoPrecisaDeConfirmacaoException{
		String stringDateSessao = "26/08/2016 00:00:00";
		String stringDataHoje   = "26/08/2016 00:00:00";
		
		ListaJulgamento listDeListaJulgamento = setUp_validarAlterarListaJulgamentoVirtual(stringDateSessao, stringDataHoje);
		objetoIncidenteService.validarAlterarListaJulgamentoVirtual(listDeListaJulgamento);
	}
	
	@Test
	public void validarAlterarListaJulgamentoVirtual_sessaoNaoIniciada_1segundoAntes() throws ServiceException, ParseException, ProcessoPrecisaDeConfirmacaoException{
		String stringDateSessao = "26/08/2016 00:00:00";
		String stringDataHoje   = "25/08/2016 23:59:59";
		
		ListaJulgamento listDeListaJulgamento = setUp_validarAlterarListaJulgamentoVirtual(stringDateSessao, stringDataHoje);
		objetoIncidenteService.validarAlterarListaJulgamentoVirtual(listDeListaJulgamento);
	}
	
	@Test
	public void validarAlterarListaJulgamentoVirtual_sessaoNaoIniciada() throws ServiceException, ParseException, ProcessoPrecisaDeConfirmacaoException{
		String stringDateSessao = "02/11/2016 00:00:00";
		String stringDataHoje   = "30/08/2016 00:00:00";
		
		ListaJulgamento listDeListaJulgamento = setUp_validarAlterarListaJulgamentoVirtual(stringDateSessao, stringDataHoje);	
		objetoIncidenteService.validarAlterarListaJulgamentoVirtual(listDeListaJulgamento);
	}
	
	
	@Test
	public void test_processoEmListaJulgamento_esperadoFalse_MuitasListas() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);		
		
		ListaJulgamento listaJulgamento01 = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		ListaJulgamento listaJulgamento02 = agrupadorRepo.getListaJulgamento(2L,"Lista2");
		
		List<ListaJulgamento> listDeListaJulgamento = new ArrayList<ListaJulgamento>();
		listDeListaJulgamento.add(listaJulgamento01);
		listDeListaJulgamento.add(listaJulgamento02);
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(listDeListaJulgamento).when(objetoIncidenteService).carregarProcessosEmListaJulgamento(agendamento);		
		
		ListaJulgamento retorno = objetoIncidenteService.processoEmListaJulgamento(agendamento);
		assertEquals(null, retorno);
	}
	
	@Test
	public void test_processoEmListaJulgamento_esperadoFalse_listaNull() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);		
		
		List<ListaJulgamento> listDeListaJulgamento = null;
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(listDeListaJulgamento).when(objetoIncidenteService).carregarProcessosEmListaJulgamento(agendamento);		
		
		ListaJulgamento retorno = objetoIncidenteService.processoEmListaJulgamento(agendamento);
		assertEquals(null, retorno);
	}
	
	@Test
	public void test_processoEmListaJulgamento_esperadoFalse_listaVazia() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);		
		
		List<ListaJulgamento> listDeListaJulgamento = new ArrayList<ListaJulgamento>();
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(listDeListaJulgamento).when(objetoIncidenteService).carregarProcessosEmListaJulgamento(agendamento);		
		
		ListaJulgamento retorno = objetoIncidenteService.processoEmListaJulgamento(agendamento);
		assertEquals(null, retorno);
	}
	
	@Test
	public void test_processoEmListaJulgamento_esperadoFalse_umaLista() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		Integer codAgendamento  = Agendamento.COD_MATERIA_AGENDAMENTO_INDICE;
		Agendamento agendamento = agrupadorRepo.getAgendamento(codAgendamento);		
		
		ListaJulgamento listaJulgamento01 = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		
		List<ListaJulgamento> listDeListaJulgamento = new ArrayList<ListaJulgamento>();
		listDeListaJulgamento.add(listaJulgamento01);
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(listDeListaJulgamento).when(objetoIncidenteService).carregarProcessosEmListaJulgamento(agendamento);		
		
		ListaJulgamento retorno = objetoIncidenteService.processoEmListaJulgamento(agendamento);
		assertEquals(listaJulgamento01, retorno);
	}	
	
	@Test(expected = ProcessoPrecisaDeConfirmacaoException.class)
	public void test_consultaAgendamentoDoObjetoIncidente_exception() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		
		ConsultaDeAgendamentoVO consulta = new ConsultaDeAgendamentoVO();
		List<Agendamento> consultaAgendamentos = new ArrayList<Agendamento>();		
		ObjetoIncidenteDto objetoIncidente = new ObjetoIncidenteDto();
		objetoIncidente.setId(1L);
		
		Mockito.when(agendamentoService.consultaAgendamentos(consulta)).thenReturn(consultaAgendamentos);
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(consulta).when(objetoIncidenteService).setConsultaDeAgendamentoVO(objetoIncidente);		
		
		objetoIncidenteService.consultarAgendamentoDoObjetoIncidente(objetoIncidente);
	}	
	
	@Test
	public void test_consultaAgendamentoDoObjetoIncidente() throws ServiceException, ProcessoPrecisaDeConfirmacaoException {
		
		Agendamento agendamento0 = new Agendamento();
		agendamento0.setJulgado(false);
		
		ObjetoIncidenteDto objetoIncidente = new ObjetoIncidenteDto();
		objetoIncidente.setId(1L);
		
		ConsultaDeAgendamentoVO consulta = new ConsultaDeAgendamentoVO();
		consulta.setSequencialObjetoIncidente(objetoIncidente.getId());
		
		List<Agendamento> consultaAgendamentos = new ArrayList<Agendamento>();
		consultaAgendamentos.add(agendamento0);		
		
		Mockito.when(agendamentoService.consultaAgendamentos(consulta)).thenReturn(consultaAgendamentos);
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(consulta).when(objetoIncidenteService).setConsultaDeAgendamentoVO(objetoIncidente);		
		
		Agendamento retorno = objetoIncidenteService.consultarAgendamentoDoObjetoIncidente(objetoIncidente);
		assertEquals(agendamento0, retorno);
	}	
	
	private Sessao getMockSessao(Long idSessao, TipoAmbienteConstante ambiente) throws ServiceException { 
		String dataInicio = "11/03/2016 00:00:00";
		String dataFim    = "17/03/2016 23:59:59";
		
		Date dateStart = null;
		Date dateEnd   = null;
		
		dateStart = DataUtil.string2Date(dataInicio);
		dateEnd   = DataUtil.string2Date(dataFim);
		
		Colegiado colegiadoPle = agrupadorRepo.getColegiado(Colegiado.TRIBUNAL_PLENO);
		
		Sessao sessao = agrupadorRepo.getSessao(1L);
		sessao.setId(idSessao);
		sessao.setDataInicio(null);
		sessao.setDataPrevistaInicio(dateStart);
		sessao.setDataFim(null);
		sessao.setDataPrevistaInicio(dateEnd);
		sessao.setTipoAmbiente(ambiente.getSigla());
		sessao.setColegiado(colegiadoPle);
		sessao.setTipoSessao(Sessao.TipoSessaoConstante.ORDINARIA.getSigla());		

		return sessao;
	}

	public DadosAgendamentoDto setUp_contagemDasSessoesNaoFinalizadas(Integer valorAnterior, TipoAmbienteConstante ambiente) throws ServiceException {
		Sessao sessao = this.getMockSessao(1L, ambiente);
		DadosAgendamentoDto dadto = new DadosAgendamentoDto ();
		dadto.setSessao(sessao);		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(valorAnterior).when(objetoIncidenteService).recuperarNumUltimaListaVirtual(dadto,0);
		Mockito.doReturn(valorAnterior).when(objetoIncidenteService).recuperarNumUltimaListaPresencial(dadto,0);
		return dadto;
	}
	
	@Test
	public void contagemDasSessoesNaoFinalizadas_virtual01() throws ServiceException {
		Integer numValorEsperado = 1;
		Integer numValorMaiorListAnterior = numValorEsperado-1;
		DadosAgendamentoDto dadto = setUp_contagemDasSessoesNaoFinalizadas(numValorMaiorListAnterior,TipoAmbienteConstante.VIRTUAL);
		
		Integer numValorRetornado = objetoIncidenteService.contagemDasSessoesNaoFinalizadas(dadto);
		assertEquals(numValorEsperado,numValorRetornado);
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).recuperarNumUltimaListaVirtual(dadto,0);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).recuperarNumUltimaListaPresencial(dadto,0);
	}
	
	@Test
	public void contagemDasSessoesNaoFinalizadas_virtual53() throws ServiceException {
		Integer numValorEsperado = 53;
		Integer numValorMaiorListAnterior = numValorEsperado-1;
		DadosAgendamentoDto dadto = setUp_contagemDasSessoesNaoFinalizadas(numValorMaiorListAnterior,TipoAmbienteConstante.VIRTUAL);
		
		Integer numValorRetornado = objetoIncidenteService.contagemDasSessoesNaoFinalizadas(dadto);
		assertEquals(numValorEsperado,numValorRetornado);
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).recuperarNumUltimaListaVirtual(dadto,0);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).recuperarNumUltimaListaPresencial(dadto,0);
	}	
	
	@Test
	public void contagemDasSessoesNaoFinalizadas_presencial08() throws ServiceException {
		Integer numValorEsperado = 8;
		Integer numValorMaiorListAnterior = numValorEsperado-1;
		DadosAgendamentoDto dadto = setUp_contagemDasSessoesNaoFinalizadas(numValorMaiorListAnterior,TipoAmbienteConstante.PRESENCIAL);
		
		Integer numValorRetornado = objetoIncidenteService.contagemDasSessoesNaoFinalizadas(dadto);
		assertEquals(numValorEsperado,numValorRetornado);
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).recuperarNumUltimaListaVirtual(dadto,0);
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).recuperarNumUltimaListaPresencial(dadto,0);
	}
	
	public DadosAgendamentoDto setUp_recuperaNumUltimaListaPresencial(Integer esperado) throws ServiceException {
		Sessao sessao = this.getMockSessao(1L, TipoAmbienteConstante.VIRTUAL);
		List<Sessao> sessoesEmAberto = new ArrayList<Sessao>();
		sessoesEmAberto.add(sessao);
		
		DadosAgendamentoDto dadto = new DadosAgendamentoDto ();
		dadto.setSessao(sessao);
		dadto.setSessoesEmAberto(sessoesEmAberto);

		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(esperado).when(objetoIncidenteService).recuperarMaiorOrdemSessaoMinistroListaJulgamento(dadto.getSessao(), dadto.getMinistro());

		return dadto;
	}	
	
	@Test
	public void recuperaNumUltimaListaPresencial_item01() throws ServiceException {
		Integer esperado = 1;
		DadosAgendamentoDto dadto = setUp_recuperaNumUltimaListaPresencial(esperado);		
		Integer ordemSessaoMinistro = objetoIncidenteService.recuperarNumUltimaListaPresencial(dadto,0);
		assertEquals(esperado,ordemSessaoMinistro);
	}
	
	@Test
	public void recuperaNumUltimaListaPresencial_item51() throws ServiceException {
		Integer esperado = 50;
		DadosAgendamentoDto dadto = setUp_recuperaNumUltimaListaPresencial(esperado);
		Integer ordemSessaoMinistro = objetoIncidenteService.recuperarNumUltimaListaPresencial(dadto,0);
		assertEquals(esperado,ordemSessaoMinistro);
	}
	
	public Processo setUp_ValidarProcessosParaJulgamentoVirtual(Long idTipoRecurso, boolean classeProibida) {
		TipoRecurso tipoRecurso = mock(TipoRecurso.class);
		when(tipoRecurso.getId()).thenReturn(idTipoRecurso);
		Processo oi = agrupadorRepo.getObjetoIncidente(1,"1111");
		Processo principal = agrupadorRepo.getObjetoIncidente(2,"2222", Classe.SIGLA_ACAO_RESCISORIA);
		oi.setPrincipal(principal);
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(tipoRecurso).when(objetoIncidenteService).ObjetoIncidenteUtilGetTipoRecurso(oi);

		Mockito.doReturn(true).when(objetoIncidenteService).temEmentaRelatorioEVotoRevisados(oi);
		return oi;
	}
	
	
	@Test(expected=ProcessoTipoRecursoPodePlanarioVirtualException.class)
	public void testValidarProcessosParaJulgamentoVirtual_SemEmentaRelatorioEVoto() throws ProcessoTipoRecursoPodePlanarioVirtualException {
		Processo oi = setUp_ValidarProcessosParaJulgamentoVirtual(4L,false);
		Mockito.doReturn(false).when(objetoIncidenteService).temEmentaRelatorioEVotoRevisados(oi);
		objetoIncidenteService.validarProcessosParaJulgamentoVirtual(oi, false);
		
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).ObjetoIncidenteUtilGetTipoRecurso(oi);
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).temEmentaRelatorioEVotoRevisados(oi);
	}	
	
	@Ignore
	@Test(expected=ProcessoTipoRecursoPodePlanarioVirtualException.class)
	public void testValidarProcessosParaJulgamentoVirtual_TipoRecursoNaoListado() throws ProcessoTipoRecursoPodePlanarioVirtualException {
		Processo oi = setUp_ValidarProcessosParaJulgamentoVirtual(1L,false);		
		objetoIncidenteService.validarProcessosParaJulgamentoVirtual(oi, false);

		Mockito.verify(objetoIncidenteService, Mockito.times(1)).ObjetoIncidenteUtilGetTipoRecurso(oi);
	}
	
	@Ignore
	@Test(expected=ProcessoTipoRecursoPodePlanarioVirtualException.class)
	public void testValidarProcessosParaJulgamentoVirtual_TipoRecursoNaoListado02() throws ProcessoTipoRecursoPodePlanarioVirtualException {
		Processo oi = setUp_ValidarProcessosParaJulgamentoVirtual(2L,false);
		objetoIncidenteService.validarProcessosParaJulgamentoVirtual(oi, false);

		Mockito.verify(objetoIncidenteService, Mockito.times(1)).ObjetoIncidenteUtilGetTipoRecurso(oi);
	}	
	
	@Ignore
	@Test
	public void testValidarProcessosParaJulgamentoVirtual_AgravoRegimental_classeOk() throws ProcessoTipoRecursoPodePlanarioVirtualException {
		Processo oi = setUp_ValidarProcessosParaJulgamentoVirtual(4L,false);
		objetoIncidenteService.validarProcessosParaJulgamentoVirtual(oi, false);

		Mockito.verify(objetoIncidenteService, Mockito.times(1)).ObjetoIncidenteUtilGetTipoRecurso(oi);
	}
	
	
	@Test
	public void testValidarProcessosParaJulgamentoVirtual_EmbargoDeclaracao_classeOk() throws ProcessoTipoRecursoPodePlanarioVirtualException {
		Processo oi = setUp_ValidarProcessosParaJulgamentoVirtual(15L,false);
		objetoIncidenteService.validarProcessosParaJulgamentoVirtual(oi, false);

		Mockito.verify(objetoIncidenteService, Mockito.times(1)).ObjetoIncidenteUtilGetTipoRecurso(oi);
	}
	
	@Ignore
	public void liberarParaJulgamentoHCSemRepresentante() {
		Processo processo = agrupadorRepo.getHabeasCorpus();
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(processo).when(objetoIncidenteService).recuperarObjetoIncidentePorId(processo.getId());
		
		boolean temOABParaTodosOsRepresentantes = objetoIncidenteService.temOABParaTodosOsRepresentantes(processo);
		assertTrue(temOABParaTodosOsRepresentantes);
	}
	
	@Test
	public void liberarParaJulgamentoHCComRepresentante() {
		Processo processo = agrupadorRepo.getHabeasCorpus();
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.ATIVO, "123456");
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(processo).when(objetoIncidenteService).recuperarObjetoIncidentePorId(processo.getId());		
		
		boolean temOABParaTodosOsRepresentantes = objetoIncidenteService.temOABParaTodosOsRepresentantes(processo);
		assertTrue(temOABParaTodosOsRepresentantes);
	}
	
	@Test
	public void liberarParaJulgamentoProcessoComRepresentanteNoPoloAtivoComOABeRepresentanteNoPoloPassivoComOAB() {
		Processo processo = agrupadorRepo.getObjetoIncidente(1, "ADI100");
		agrupadorRepo.injetarParte(processo, TipoPolo.ATIVO);
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.ATIVO, "123456");
		
		agrupadorRepo.injetarParte(processo, TipoPolo.PASSIVO);
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.PASSIVO, "654321");
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(processo).when(objetoIncidenteService).recuperarObjetoIncidentePorId(processo.getId());
		
		boolean temOABParaTodosOsRepresentantes = objetoIncidenteService.temOABParaTodosOsRepresentantes(processo);
		assertTrue(temOABParaTodosOsRepresentantes);
	}
	
	@Test
	public void naoLiberarParaJulgamentoProcessoComRepresentanteNoPoloAtivoComOABeRepresentanteNoPoloPassivoSemOAB() {
		Processo processo = agrupadorRepo.getObjetoIncidente(1, "ADI100");
		agrupadorRepo.injetarParte(processo, TipoPolo.ATIVO);
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.ATIVO, "123456");
		
		agrupadorRepo.injetarParte(processo, TipoPolo.PASSIVO);
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.PASSIVO, null);
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(processo).when(objetoIncidenteService).recuperarObjetoIncidentePorId(processo.getId());
		
		processo.setPrincipal(processo);
		
		boolean retorno = !objetoIncidenteService.temOABParaTodosOsRepresentantes(processo);
		assertTrue(retorno);
	}
	
	@Test
	public void naoLiberarParaJulgamentoProcessoSomRepresentanteNoPoloAtivoComOABeRepresentanteNoPoloPassivoComOAB() {
		Processo processo = agrupadorRepo.getObjetoIncidente(1, "ADI100");
		agrupadorRepo.injetarParte(processo, TipoPolo.ATIVO);
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.ATIVO, null);
		
		agrupadorRepo.injetarParte(processo, TipoPolo.PASSIVO);
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.PASSIVO, "123456");
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(processo).when(objetoIncidenteService).recuperarObjetoIncidentePorId(processo.getId());		
		
		processo.setPrincipal(processo);
		
		boolean retorno = !objetoIncidenteService.temOABParaTodosOsRepresentantes(processo);
		assertTrue(retorno);
	}
	
	@Test
	public void naoLiberarParaJulgamentoProcessoSemRepresentanteNoPoloAtivoComOABeRepresentanteNoPoloPassivoSemOAB() {
		Processo processo = agrupadorRepo.getObjetoIncidente(1, "ADI100");
		agrupadorRepo.injetarParte(processo, TipoPolo.ATIVO);
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.ATIVO, null);
		
		agrupadorRepo.injetarParte(processo, TipoPolo.PASSIVO);
		agrupadorRepo.injetarRepresentante(processo, TipoPolo.PASSIVO, null);
		
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		Mockito.doReturn(processo).when(objetoIncidenteService).recuperarObjetoIncidentePorId(processo.getId());		
		
		processo.setPrincipal(processo);
		
		boolean retorno = !objetoIncidenteService.temOABParaTodosOsRepresentantes(processo);
		assertTrue(retorno);
	}
	
	@Test
	public void naoPermitirSustentacaoOralEmAgravoRegimentalDeAgravoEmRecursoEspecial() {
		objetoIncidenteService = Mockito.spy(objetoIncidenteService);
		
		Classe classe = new Classe();
		classe.setId(Classe.SIGLA_RECURSO_EXTRAORDINARIO_COM_AGRAVO);
		
		Processo principal = new Processo();
		principal.setClasseProcessual(classe);
		
		TipoRecursoProcesso tipoRecursoProcesso = new TipoRecursoProcesso();
		tipoRecursoProcesso.setSigla(TipoRecurso.SIGLA_AGRAVO);
		
		RecursoProcesso rp = new RecursoProcesso();
		rp.setTipoRecursoProcesso(tipoRecursoProcesso);
		rp.setPrincipal(principal);
		
		boolean permiteSustentacaoOral = objetoIncidenteService.permiteSustentacaoOral(rp);
		
		assertTrue(!permiteSustentacaoOral);
	}

}