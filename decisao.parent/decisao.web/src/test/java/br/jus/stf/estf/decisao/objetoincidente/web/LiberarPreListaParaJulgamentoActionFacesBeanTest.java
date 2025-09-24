package br.jus.stf.estf.decisao.objetoincidente.web;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.publicacao.Feriado;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.julgamento.model.service.TipoListaJulgamentoService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.estf.publicacao.model.service.FeriadoService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.support.DadosAgendamentoDto;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoNaoPodeSerAgendadoException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoPrecisaDeConfirmacaoException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoTipoRecursoPodePlanarioVirtualException;
import br.jus.stf.estf.decisao.objetoincidente.support.SustentacaoOralException;
import br.jus.stf.estf.decisao.objetoincidente.support.TipoAgendamento;
import br.jus.stf.estf.decisao.objetoincidente.support.TipoColegiadoAgendamento;
import br.jus.stf.estf.decisao.objetoincidente.support.ValidacaoLiberacaoParaJulgamentoException;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;

public class LiberarPreListaParaJulgamentoActionFacesBeanTest {

	@Mock
	private SessaoService sessaoService;
	
	@Mock
	private ObjetoIncidenteService objetoIncidenteService;

	@Mock
	private PreListaJulgamentoService preListaJulgamentoService;

	@Mock
	private FeriadoService feriadoService;

	@Mock
	private TipoListaJulgamentoService tipoListaJulgamentoService;

	private LiberarPreListaParaJulgamentoActionFacesBean actionFacesBean;

	private AgrupadorRepo agrupadorRepo = new AgrupadorRepo();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		actionFacesBean = new LiberarPreListaParaJulgamentoActionFacesBean();
		Whitebox.setInternalState(actionFacesBean, "sessaoService", sessaoService);
		Whitebox.setInternalState(actionFacesBean, "objetoIncidenteService", objetoIncidenteService);
		Whitebox.setInternalState(actionFacesBean, "preListaJulgamentoService", preListaJulgamentoService);
		Whitebox.setInternalState(actionFacesBean, "feriadoService", feriadoService);
		Whitebox.setInternalState(actionFacesBean, "tipoListaJulgamentoService", tipoListaJulgamentoService);
	}

	public List<Calendar> feriadosMaioCalendar() {
		int ano = 2016;
		int mesAtual = Calendar.MAY;
		int dia = 1;
		Calendar feriado20160501 = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		feriados.add(feriado20160501);

		return feriados;
	}

	public List<Feriado> feriadosMaioFeriados() {
		List<Feriado> feriados2016 = this.recuperaFeriados();
		List<Feriado> feriadosMes5 = new ArrayList<Feriado>();
		feriadosMes5.add(feriados2016.get(5));

		return feriadosMes5;
	}

	@Test
	public void ambienteJulgamentoSelecionado_IdTipoAmbienteColegiado_Null() {
		actionFacesBean.setIdTipoAmbienteColegiado(null);

		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.ambienteJulgamentoSelecionado();
		Mockito.verify(actionFacesBean, Mockito.times(0)).setIdTipoAndamento(Andamentos.INCLUSAO_EM_PAUTA.getId());
	}

	@Test
	public void ambienteJulgamentoSelecionado_IdTipoAmbienteColegiado_PRESENCIAL() {
		actionFacesBean.setIdTipoAmbienteColegiado(TipoAmbienteConstante.PRESENCIAL.getSigla());

		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.ambienteJulgamentoSelecionado();
		Mockito.verify(actionFacesBean, Mockito.times(0)).setIdTipoAndamento(Andamentos.INCLUSAO_EM_PAUTA.getId());
	}

	@Test
	public void ambienteJulgamentoSelecionado_IdTipoAmbienteColegiado_VIRTUAL() {
		actionFacesBean.setIdTipoAmbienteColegiado(TipoAmbienteConstante.VIRTUAL.getSigla());

		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.ambienteJulgamentoSelecionado();
		Mockito.verify(actionFacesBean, Mockito.times(1)).setIdTipoAndamento(Andamentos.INCLUSAO_EM_PAUTA.getId());
	}

	void setUp_tipoColegiado(String tipoAmbiente, boolean andamentoNull, boolean colegiadoNotNull) {

		actionFacesBean.setIdTipoAmbienteColegiado(tipoAmbiente);
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn(andamentoNull).when(actionFacesBean).andamentoNull();
		Mockito.doReturn(colegiadoNotNull).when(actionFacesBean).colegiadoNotNull();
		Mockito.doReturn(false).when(actionFacesBean).getSessaoExtraordinaria();
	}

	@Test
	public void tipoColegiadoSelecionado_Presencial_andamentoFALSE_colegiadoFALSE() {
		String tipoAmbiente = TipoAmbienteConstante.PRESENCIAL.getSigla();
		this.setUp_tipoColegiado(tipoAmbiente, false, false);
		actionFacesBean.tipoColegiadoSelecionado();
		Mockito.verify(actionFacesBean, Mockito.times(0)).carregarSessoes();
	}

	@Test
	public void tipoColegiadoSelecionado_Presencial_andamentoTRUE_colegiadoTRUE() {
		String tipoAmbiente = TipoAmbienteConstante.PRESENCIAL.getSigla();
		this.setUp_tipoColegiado(tipoAmbiente, true, true);
		actionFacesBean.tipoColegiadoSelecionado();
		Mockito.verify(actionFacesBean, Mockito.times(0)).carregarSessoes();
	}

	@Test
	public void tipoColegiadoSelecionado_Presencial_andamentoTRUE_colegiadoFALSE() {
		String tipoAmbiente = TipoAmbienteConstante.PRESENCIAL.getSigla();
		boolean andamentoNull = true;
		boolean colegiadoNotNull = false;
		this.setUp_tipoColegiado(tipoAmbiente, andamentoNull, colegiadoNotNull);
		actionFacesBean.tipoColegiadoSelecionado();
		Mockito.verify(actionFacesBean, Mockito.times(0)).carregarSessoes();
	}

	@Test
	public void tipoColegiadoSelecionado_Virtual_andamentoFALSE_colegiadoTRUE() {
		String tipoAmbiente = TipoAmbienteConstante.VIRTUAL.getSigla();
		boolean andamentoNull = false;
		boolean colegiadoNotNull = true;
		this.setUp_tipoColegiado(tipoAmbiente, andamentoNull, colegiadoNotNull);
		actionFacesBean.tipoColegiadoSelecionado();
		Mockito.verify(actionFacesBean, Mockito.times(0)).carregarSessoes();
	}

	@Test
	public void tipoColegiadoSelecionado_Ambiente_null() {
		String tipoAmbiente = null;
		boolean andamentoNull = true;
		boolean colegiadoNotNull = false;
		this.setUp_tipoColegiado(tipoAmbiente, andamentoNull, colegiadoNotNull);
		actionFacesBean.tipoColegiadoSelecionado();
		Mockito.verify(actionFacesBean, Mockito.times(0)).carregarSessoes();
	}

	boolean setUp_isDesabilitarPainelTipoAndamento(String siglaTipoInformado) {
		actionFacesBean.setIdTipoAmbienteColegiado(siglaTipoInformado);
		boolean retorno = actionFacesBean.isDesabilitarPainelTipoAndamento();
		return retorno;
	}

	@Test
	public void isDesabilitarPainelTipoAndamento_IdTipoColegiadoAgendamento_Null() {
		String siglaTipoInformado = null;
		boolean retorno = this.setUp_isDesabilitarPainelTipoAndamento(siglaTipoInformado);
		assertEquals(false, retorno);
	}

	@Test
	public void isDesabilitarPainelTipoAndamento_IdTipoColegiadoAgendamento_PRESENCIAL() {
		String siglaTipoInformado = TipoAmbienteConstante.PRESENCIAL.getSigla();
		boolean retorno = this.setUp_isDesabilitarPainelTipoAndamento(siglaTipoInformado);
		assertEquals(false, retorno);
	}

	@Test
	public void isDesabilitarPainelTipoAndamento_IdTipoColegiadoAgendamento_VIRTUAL() {
		String siglaTipoInformado = TipoAmbienteConstante.VIRTUAL.getSigla();
		boolean retorno = setUp_isDesabilitarPainelTipoAndamento(siglaTipoInformado);
		assertEquals(true, retorno);
	}

	@Test
	public void colegiadoNotNull_False() throws ParseException, ServiceException {
		actionFacesBean.setIdTipoColegiadoAgendamento(null);
		boolean retorno = actionFacesBean.colegiadoNotNull();
		assertEquals(false, retorno);
	}

	@Test
	public void colegiadoNotNull_vazio() throws ParseException, ServiceException {
		actionFacesBean.setIdTipoColegiadoAgendamento("");
		boolean retorno = actionFacesBean.colegiadoNotNull();
		assertEquals(false, retorno);
	}

	@Test
	public void colegiadoNotNull_True() throws ParseException, ServiceException {
		actionFacesBean.setIdTipoColegiadoAgendamento(TipoColegiadoAgendamento.PLENARIO.getId());
		boolean retorno = actionFacesBean.colegiadoNotNull();
		assertEquals(true, retorno);
	}

	@Test
	public void ambienteNotNull_vazio() throws ParseException, ServiceException {
		actionFacesBean.setIdTipoAmbienteColegiado("");
		boolean retorno = actionFacesBean.ambienteNotNull();
		assertEquals(false, retorno);
	}

	@Test
	public void ambienteNotNull_True() throws ParseException, ServiceException {
		actionFacesBean.setIdTipoAmbienteColegiado(TipoAmbienteConstante.PRESENCIAL.getSigla());
		boolean retorno = actionFacesBean.ambienteNotNull();
		assertEquals(true, retorno);
	}

	@Test
	public void andamentoNull_vazio() throws ParseException, ServiceException {
		actionFacesBean.setIdTipoAndamento(null);
		boolean retorno = actionFacesBean.andamentoNull();
		assertEquals(true, retorno);
	}

	@Test
	public void andamentoNull_false() throws ParseException, ServiceException {
		boolean retorno = actionFacesBean.andamentoNull();
		assertEquals(true, retorno);
	}

	@Test
	public void andamentoNull_MESA_True() throws ParseException, ServiceException {
		actionFacesBean.setIdTipoAndamento(Andamentos.APRESENTACAO_EM_MESA.getId());
		boolean retorno = actionFacesBean.andamentoNull();
		assertEquals(false, retorno);
	}

	@Test
	public void carregarSessoes_virtual() throws ParseException, ServiceException {
		TipoColegiadoConstante colegiado = TipoColegiadoConstante.SESSAO_PLENARIA;
		String ambiente = TipoAmbienteConstante.VIRTUAL.getSigla();

		actionFacesBean.setIdTipoAmbienteColegiado(ambiente);
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn(colegiado).when(actionFacesBean).defineColegiado(Mockito.anyString());

		actionFacesBean.carregarSessoes();
		Mockito.verify(actionFacesBean, Mockito.times(0)).addError(Mockito.anyString());
		Mockito.verify(actionFacesBean, Mockito.times(0)).addWarning(Mockito.anyString());
	}

	@Test
	public void carregarSessoes_presencial() throws ParseException, ServiceException {
		TipoColegiadoConstante colegiado = TipoColegiadoConstante.SESSAO_PLENARIA;
		String ambiente = TipoAmbienteConstante.PRESENCIAL.getSigla();
		List<Sessao> sessoesPesquisa = new ArrayList<Sessao>();

		Mockito.when(sessaoService.pesquisar(colegiado, TipoAmbienteConstante.PRESENCIAL)).thenReturn(sessoesPesquisa);

		actionFacesBean.setIdTipoAmbienteColegiado(ambiente);
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn(colegiado).when(actionFacesBean).defineColegiado(Mockito.anyString());
		Mockito.doNothing().when(actionFacesBean).carregarSessoesPesquisadas(sessoesPesquisa);

		actionFacesBean.carregarSessoes();
		Mockito.verify(actionFacesBean, Mockito.times(0)).addError(Mockito.anyString());
		Mockito.verify(actionFacesBean, Mockito.times(0)).addWarning(Mockito.anyString());
	}

	@Test
	public void sessaoColegiadoEscolhidoDiferenteColegiadoPadraoMinistro_ColegiadoEscolhidoPlenario() throws ParseException, ServiceException {
		actionFacesBean.setIdTipoColegiadoAgendamento(TipoColegiadoAgendamento.PLENARIO.getId());

		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.sessaoColegiadoEscolhidoDiferenteColegiadoPadraoMinistro();
		Mockito.verify(actionFacesBean, Mockito.times(1)).setSessaoMinistroDiferente(false);
	}

	@Test
	public void sessaoColegiadoEscolhidoDiferenteColegiadoPadraoMinistro_ColegiadoEscolhido1T_ColegiadoMinistro2T() throws ParseException, ServiceException {
		String colegiadoAgendamento = TipoColegiadoAgendamento.PT.getId();
		TipoColegiadoConstante colegiadoMinistro = TipoColegiadoConstante.SEGUNDA_TURMA;

		actionFacesBean.setIdTipoColegiadoAgendamento(colegiadoAgendamento);
		actionFacesBean.setColegiadoMinistro(colegiadoMinistro);

		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.sessaoColegiadoEscolhidoDiferenteColegiadoPadraoMinistro();
		Mockito.verify(actionFacesBean, Mockito.times(1)).setSessaoMinistroDiferente(true);
	}

	@Test
	public void sessaoColegiadoEscolhidoDiferenteColegiadoPadraoMinistro_ColegiadoEscolhidoIgualColegiadoMinistro() throws ParseException, ServiceException {
		String colegiadoAgendamento = TipoColegiadoAgendamento.PT.getId();
		TipoColegiadoConstante colegiadoMinistro = TipoColegiadoConstante.PRIMEIRA_TURMA;

		actionFacesBean.setIdTipoColegiadoAgendamento(colegiadoAgendamento);
		actionFacesBean.setColegiadoMinistro(colegiadoMinistro);

		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.sessaoColegiadoEscolhidoDiferenteColegiadoPadraoMinistro();
		Mockito.verify(actionFacesBean, Mockito.times(1)).setSessaoMinistroDiferente(false);
	}

	@Test
	public void testDatInformado01Dominogo() throws ParseException {
		String dataString = "10/04/2016 00:00:00";
		Date dateInformado = DataUtil.string2Date(dataString);
		Date dateRetornoInicio = DataUtil.dateSessaoInicio(dateInformado);

		String dataStringExperado = "15/04/2016 00:00:00";
		Date dateInformadoEsperado = DataUtil.string2Date(dataStringExperado);

		assertEquals(dateInformadoEsperado, dateRetornoInicio);
	}

	@Test
	public void testDatInformado02Segunda() throws ParseException {
		String dataString = "11/04/2016 15:00:00";
		Date dateInformado = DataUtil.string2Date(dataString);
		Date dateRetornoInicio = DataUtil.dateSessaoInicio(dateInformado);

		String dataStringExperado = "15/04/2016 00:00:00";
		Date dateInformadoEsperado = DataUtil.string2Date(dataStringExperado);

		assertEquals(dateInformadoEsperado, dateRetornoInicio);
	}

	@Test
	public void testDatInformado03Terca() throws ParseException {
		String dataString = "12/04/2016 00:00:00";
		Date dateInformado = DataUtil.string2Date(dataString);
		Date dateRetornoInicio = DataUtil.dateSessaoInicio(dateInformado);

		String dataStringExperado = "15/04/2016 00:00:00";
		Date dateInformadoEsperado = DataUtil.string2Date(dataStringExperado);

		assertEquals(dateInformadoEsperado, dateRetornoInicio);
	}

	@Test
	public void testDatInformado04Quarta() throws ParseException {
		String dataString = "13/04/2016 15:00:00";
		Date dateInformado = DataUtil.string2Date(dataString);
		Date dateRetornoInicio = DataUtil.dateSessaoInicio(dateInformado);

		String dataStringExperado = "15/04/2016 00:00:00";
		Date dateInformadoEsperado = DataUtil.string2Date(dataStringExperado);

		assertEquals(dateInformadoEsperado, dateRetornoInicio);
	}

	@Test
	public void testDatInformado05Quinta() throws ParseException {
		String dataString = "14/04/2016 00:00:00";
		Date dateInformado = DataUtil.string2Date(dataString);
		Date dateRetornoInicio = DataUtil.dateSessaoInicio(dateInformado);

		String dataStringExperado = "15/04/2016 00:00:00";
		Date dateInformadoEsperado = DataUtil.string2Date(dataStringExperado);

		assertEquals(dateInformadoEsperado, dateRetornoInicio);
	}

	@Test
	public void testDatInformado06Sexta() throws ParseException {
		String dataString = "15/04/2016 00:00:00";
		Date dateInformado = DataUtil.string2Date(dataString);
		Date dateRetornoInicio = DataUtil.dateSessaoInicio(dateInformado);

		String dataStringExperado = "15/04/2016 00:00:00";
		Date dateInformadoEsperado = DataUtil.string2Date(dataStringExperado);

		assertEquals(dateInformadoEsperado, dateRetornoInicio);
	}

	@Test
	public void testDatInformado07Sabado() throws ParseException {
		String dataString = "16/04/2016 00:00:00";
		Date dateInformado = DataUtil.string2Date(dataString);
		Date dateRetornoInicio = DataUtil.dateSessaoInicio(dateInformado);

		String dataStringExperado = "22/04/2016 00:00:00";
		Date dateInformadoEsperado = DataUtil.string2Date(dataStringExperado);

		assertEquals(dateInformadoEsperado, dateRetornoInicio);
	}

	public Processo setUp_ValidaLiberacaoProcessoParaJulgamento(boolean virtual) throws ServiceException, ValidacaoLiberacaoParaJulgamentoException, ProcessoPrecisaDeConfirmacaoException,
																						ProcessoNaoPodeSerAgendadoException, ProcessoTipoRecursoPodePlanarioVirtualException, ParseException {
		Processo oi = agrupadorRepo.getObjetoIncidente(1, "1111");

		DadosAgendamentoDto dadosAgendamentoDto = new DadosAgendamentoDto();
		ObjetoIncidenteDto processo = new ObjetoIncidenteDto();

		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn(virtual).when(actionFacesBean).isAmbienteVirtual();
		Mockito.doReturn(true).when(objetoIncidenteService).temOABParaTodosOsRepresentantes(oi);

		Mockito.doReturn(TipoColegiadoConstante.SESSAO_PLENARIA).when(actionFacesBean).defineColegiado(Mockito.anyString());
		Mockito.doReturn(processo).when(actionFacesBean).objetoIndicenteDtoValueOf(oi);
		Mockito.doReturn(new Ministro()).when(actionFacesBean).getMinistro();
		Mockito.doReturn(new Usuario()).when(actionFacesBean).getUsuario();
		Mockito.doReturn(new Setor()).when(actionFacesBean).getSetorMinistro();
		Mockito.doReturn(dadosAgendamentoDto).when(actionFacesBean).montaDadosDoAgendamento(processo);

		Mockito.doNothing().when(objetoIncidenteService).verificaProcessoEmSessaoPrevista(oi);
		Mockito.doNothing().when(objetoIncidenteService).verificaProcessoEmListaJulgamentoPrevista(oi);
		Mockito.doNothing().when(objetoIncidenteService).validarProcessoParaAgendamento(Mockito.any(DadosAgendamentoDto.class));
		Mockito.doNothing().when(objetoIncidenteService).validarSituacaoDeJulgamento(oi);
		Mockito.doNothing().when(objetoIncidenteService).validarProcessosParaJulgamentoVirtual(oi, false);

		return oi;
	}

	@Test
	public void validaLiberacaoProcessoParaJulgamento_JulgamentoVirtual() throws Exception {
		Processo oi = setUp_ValidaLiberacaoProcessoParaJulgamento(true);

		Ministro ministro = new Ministro();
		ministro.setId(1L);

		oi.setRelatorIncidenteId(1L);
		Mockito.when(actionFacesBean.getMinistro()).thenReturn(ministro);

		actionFacesBean.validaLiberacaoProcessoParaJulgamento(oi);
		Mockito.verify(actionFacesBean, Mockito.times(1)).isAmbienteVirtual();
		Mockito.verify(objetoIncidenteService, Mockito.times(1)).validarProcessosParaJulgamentoVirtual(oi, false);
	}

	@Test
	public void validaLiberacaoProcessoParaJulgamento_JulgamentoReal() throws Exception {
		Processo oi = setUp_ValidaLiberacaoProcessoParaJulgamento(false);

		Ministro ministro = new Ministro();
		ministro.setId(1L);

		oi.setRelatorIncidenteId(1L);
		Mockito.when(actionFacesBean.getMinistro()).thenReturn(ministro);

		actionFacesBean.validaLiberacaoProcessoParaJulgamento(oi);
		Mockito.verify(actionFacesBean, Mockito.times(1)).isAmbienteVirtual();
		Mockito.verify(objetoIncidenteService, Mockito.times(0)).validarProcessosParaJulgamentoVirtual(oi, false);
	}

	@Test
	public void validarLiberacaoListaParaJulgamento_failProcessoTipoRecursoPodePlanarioVirtual() throws ServiceException, ValidacaoLiberacaoParaJulgamentoException,
																						ProcessoPrecisaDeConfirmacaoException, ProcessoNaoPodeSerAgendadoException,
																						ProcessoTipoRecursoPodePlanarioVirtualException, ParseException, SustentacaoOralException {

		PreListaJulgamentoObjetoIncidente preListaJulgamentoObjetoIncidente = agrupadorRepo.getPreListaJulgamentoObjetoIncidente(1, 1);
		List<PreListaJulgamentoObjetoIncidente> preListJulgObjetoIncidentes = new ArrayList<PreListaJulgamentoObjetoIncidente>();
		preListJulgObjetoIncidentes.add(preListaJulgamentoObjetoIncidente);

		PreListaJulgamento preListaJulgamento = agrupadorRepo.getPreListaJulgamento(1, "Lista");
		preListaJulgamento.setObjetosIncidentes(preListJulgObjetoIncidentes);

		Mockito.doReturn(preListaJulgamento).when(preListaJulgamentoService).recuperarPorId(preListaJulgamento.getId());

		actionFacesBean = Mockito.spy(actionFacesBean);
		String descObjetoIncidente = "AC 274 QO";
		Mockito.doReturn(descObjetoIncidente).when(actionFacesBean).stringFormatMensagem(preListaJulgamentoObjetoIncidente);
		Mockito.doThrow(new ProcessoTipoRecursoPodePlanarioVirtualException()).when(actionFacesBean).validaLiberacaoProcessoParaJulgamento(preListaJulgamentoObjetoIncidente.getObjetoIncidente());
		Mockito.doNothing().when(actionFacesBean).addError(Mockito.anyString());

		actionFacesBean.validarLiberacaoListaParaJulgamento(preListaJulgamento);
		Mockito.verify(actionFacesBean, Mockito.times(1)).addError(descObjetoIncidente + "null" + "\n");
		Mockito.verify(actionFacesBean, Mockito.times(0)).addWarning(Mockito.anyString());
	}

	private List<Feriado> recuperaFeriados() {
		List<Feriado> feriados = new ArrayList<Feriado>();

		// 0
		Feriado feriado0101 = new Feriado();
		feriado0101.setId("012016");
		feriado0101.setDia("01");
		feriados.add(feriado0101);

		// 1
		Feriado feriado0209 = new Feriado();// Carnaval
		feriado0209.setId("022016");
		feriado0209.setDia("09");
		feriados.add(feriado0209);

		// 2
		Feriado feriado0325 = new Feriado();// Sexta-feira da Paixão
		feriado0325.setId("032016");
		feriado0325.setDia("25");
		feriados.add(feriado0325);

		// 3
		Feriado feriado0327 = new Feriado();// Páscoa
		feriado0327.setId("032016");
		feriado0327.setDia("27");
		feriados.add(feriado0327);

		// 4
		Feriado feriado0421 = new Feriado(); // Tiradentes
		feriado0421.setId("042016");
		feriado0421.setDia("21");
		feriados.add(feriado0421);

		// 5
		Feriado feriado0501 = new Feriado(); // Dia do Trabalho
		feriado0501.setId("052016");
		feriado0501.setDia("01");
		feriados.add(feriado0501);

		// 6
		Feriado feriado0526 = new Feriado(); // Corpus Christi
		feriado0526.setId("052016");
		feriado0526.setDia("26");
		feriados.add(feriado0526);

		// 7
		Feriado feriado1102 = new Feriado(); // Finados
		feriado1102.setId("112016");
		feriado1102.setDia("02");
		feriados.add(feriado1102);

		// 8
		Feriado feriado1115 = new Feriado(); // Proclamação da República
		feriado1115.setId("112016");
		feriado1115.setDia("15");
		feriados.add(feriado1115);

		// 9
		Feriado feriado1225 = new Feriado(); // Natal
		feriado1225.setId("122016");
		feriado1225.setDia("25");
		feriados.add(feriado1225);

		return feriados;
	}

	public void recuperaFeriadosMes(List<Feriado> feriadosMesAtual, List<Feriado> feriadosMesProximo, int ano, int mesAtual, int dia, int anoProximo, int mesProximo, int qtdFeriadosEsperados)
																						throws ServiceException {

		List<Feriado> feriados = new ArrayList<Feriado>();
		feriados.addAll(feriadosMesAtual);
		feriados.addAll(feriadosMesProximo);

		List<Calendar> feriadosCalendar = DataUtil.tipoFeriado2Calendar(feriados);
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		Mockito.when(feriadoService.recuperarProximosFeriados(calendarHoje, 3)).thenReturn(feriadosCalendar);

		List<Calendar> feriadosRetornados = feriadoService.recuperarProximosFeriados(calendarHoje, 3);

		int qtdFeriadosRetornados = feriadosRetornados.size();
		assertEquals(qtdFeriadosEsperados, qtdFeriadosRetornados);
	}

	@Test
	public void recuperFeriados_Janeiro() throws ServiceException {
		List<Feriado> feriados2016 = this.recuperaFeriados();

		List<Feriado> feriadosMesAtual = new ArrayList<Feriado>();
		feriadosMesAtual.add(feriados2016.get(0));

		List<Feriado> feriadosMesProximo = new ArrayList<Feriado>();
		feriadosMesProximo.add(feriados2016.get(1));

		int ano = 2016;
		int mesAtual = Calendar.JANUARY;
		int dia = 3;
		int anoProximo = 2016;
		int mesProximo = Calendar.FEBRUARY;
		int qtdFeriadosEsperados = 2;
		this.recuperaFeriadosMes(feriadosMesAtual, feriadosMesProximo, ano, mesAtual, dia, anoProximo, mesProximo, qtdFeriadosEsperados);
	}

	@Test
	public void recuperFeriados_junho() throws ServiceException {
		List<Feriado> feriadosMesAtual = new ArrayList<Feriado>();
		List<Feriado> feriadosMesProximo = new ArrayList<Feriado>();

		int ano = 2016;
		int mesAtual = Calendar.JUNE;
		int dia = 15;
		int anoProximo = 2016;
		int mesProximo = Calendar.AUGUST;
		int qtdFeriadosEsperados = 0;
		this.recuperaFeriadosMes(feriadosMesAtual, feriadosMesProximo, ano, mesAtual, dia, anoProximo, mesProximo, qtdFeriadosEsperados);
	}

	@Test
	public void recuperFeriados_novembro() throws ServiceException {
		List<Feriado> feriados2016 = this.recuperaFeriados();

		List<Feriado> feriadosMesAtual = new ArrayList<Feriado>();
		feriadosMesAtual.add(feriados2016.get(7));
		feriadosMesAtual.add(feriados2016.get(8));

		List<Feriado> feriadosMesProximo = new ArrayList<Feriado>();
		feriadosMesProximo.add(feriados2016.get(0));// Janeiro de 2017

		int ano = 2016;
		int mesAtual = Calendar.NOVEMBER;
		int dia = 01;
		int anoProximo = 2017;
		int mesProximo = Calendar.JANUARY;
		int qtdFeriadosEsperados = 3;
		this.recuperaFeriadosMes(feriadosMesAtual, feriadosMesProximo, ano, mesAtual, dia, anoProximo, mesProximo, qtdFeriadosEsperados);
	}

	@Test
	public void recuperaMesAno_Janeiro() {
		int ano = 2016;
		int mesAtual = 1;
		String mesAno = DataUtil.recuperaMesAno(ano, mesAtual);
		assertEquals("012016", mesAno);
	}

	@Test
	public void recuperaMesAno_Setembro() {
		int ano = 2016;
		int mesAtual = 9;
		String mesAno = DataUtil.recuperaMesAno(ano, mesAtual);
		assertEquals("092016", mesAno);
	}

	@Test
	public void recuperaMesAno_Outrubro() {
		int ano = 2016;
		int mesAtual = 10;
		String mesAno = DataUtil.recuperaMesAno(ano, mesAtual);
		assertEquals("102016", mesAno);
	}

	@Test
	public void tipoFeriado2Calendar20160421() {
		Feriado feriado01 = new Feriado();
		feriado01.setId("042016");
		feriado01.setDia("21");

		List<Feriado> feriados = new ArrayList<Feriado>();
		feriados.add(feriado01);

		List<Calendar> feriadosCalendar = DataUtil.tipoFeriado2Calendar(feriados);

		Calendar feriadoCalendar01 = feriadosCalendar.get(0);
		assertEquals(21, feriadoCalendar01.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.APRIL, feriadoCalendar01.get(Calendar.MONTH));
		assertEquals(2016, feriadoCalendar01.get(Calendar.YEAR));
		assertEquals(0, feriadoCalendar01.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, feriadoCalendar01.get(Calendar.MINUTE));
	}

	@Test
	public void tipoFeriado2Calendar20160501() {
		Feriado feriado01 = new Feriado();
		feriado01.setId("052016");
		feriado01.setDia("01");

		List<Feriado> feriados = new ArrayList<Feriado>();
		feriados.add(feriado01);

		List<Calendar> feriadosCalendar = DataUtil.tipoFeriado2Calendar(feriados);

		Calendar feriadoCalendar01 = feriadosCalendar.get(0);
		assertEquals(0001, feriadoCalendar01.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.MAY, feriadoCalendar01.get(Calendar.MONTH));
		assertEquals(2016, feriadoCalendar01.get(Calendar.YEAR));
		assertEquals(0000, feriadoCalendar01.get(Calendar.HOUR_OF_DAY));
		assertEquals(0000, feriadoCalendar01.get(Calendar.MINUTE));
	}

	@Test
	public void tipoFeriado2Calendar20161225() {
		Feriado feriado01 = new Feriado();
		feriado01.setId("122016");
		feriado01.setDia("25");

		List<Feriado> feriados = new ArrayList<Feriado>();
		feriados.add(feriado01);

		List<Calendar> feriadosCalendar = DataUtil.tipoFeriado2Calendar(feriados);

		Calendar feriadoCalendar01 = feriadosCalendar.get(0);
		assertEquals(25, feriadoCalendar01.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.DECEMBER, feriadoCalendar01.get(Calendar.MONTH));
		assertEquals(2016, feriadoCalendar01.get(Calendar.YEAR));
		assertEquals(0000, feriadoCalendar01.get(Calendar.HOUR_OF_DAY));
		assertEquals(0000, feriadoCalendar01.get(Calendar.MINUTE));
	}

	@Test
	public void isDiaUtil_01Domingo() {
		int ano = 2016;
		int mesAtual = Calendar.APRIL;
		int dia = 17;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(false, isDiaUtil);
	}

	@Test
	public void isDiaUtil_02segunda() {
		int ano = 2016;
		int mesAtual = Calendar.APRIL;
		int dia = 18;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(true, isDiaUtil);
	}

	@Test
	public void isDiaUtil_03terca() {
		int ano = 2016;
		int mesAtual = Calendar.APRIL;
		int dia = 19;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(true, isDiaUtil);
	}

	@Test
	public void isDiaUtil_04quarta() {
		int ano = 2016;
		int mesAtual = Calendar.APRIL;
		int dia = 20;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(true, isDiaUtil);
	}

	@Test
	public void isDiaUtil_05quinta() {
		int ano = 2016;
		int mesAtual = Calendar.APRIL;
		int dia = 21;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(true, isDiaUtil);
	}

	@Test
	public void isDiaUtil_05quintaFeriado() {
		int ano = 2016;
		int mesAtual = Calendar.APRIL;
		int dia = 21;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = DataUtil.tipoFeriado2Calendar(this.recuperaFeriados());

		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(false, isDiaUtil);
	}

	@Test
	public void isDiaUtil_06Sexta() {
		int ano = 2016;
		int mesAtual = Calendar.APRIL;
		int dia = 22;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(true, isDiaUtil);
	}

	@Test
	public void isDiaUtil_07Sabado() {
		int ano = 2016;
		int mesAtual = Calendar.APRIL;
		int dia = 23;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(false, isDiaUtil);
	}

	@Test
	public void isDiaUtil_FeriasMinistroJulho() {
		int ano = 2016;
		int mesAtual = Calendar.JULY;
		int dia = 23;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(false, isDiaUtil);
	}

	@Test
	public void primeiroDeJulhoDeveSerDiaUtil() {
		int ano = 2016;
		int mesAtual = Calendar.JULY;
		int dia = 1;
		Calendar calendarHoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		Boolean isDiaUtil = DataUtil.isDiaUtil(calendarHoje, feriados);
		assertEquals(true, isDiaUtil);
	}

	@Test
	public void getTipoAgendamentoEscolhido_agendamentoIndice() {
		actionFacesBean.setIdTipoAndamento(Andamentos.APRESENTACAO_EM_MESA.getId());
		TipoAgendamento tipoAgendamentoExperado = TipoAgendamento.INDICE;
		TipoAgendamento tipoAgendamentoRetorno = actionFacesBean.getTipoAgendamentoEscolhido();
		assertEquals(tipoAgendamentoExperado, tipoAgendamentoRetorno);
	}

	@Test
	public void getTipoAgendamentoEscolhido_agendamentoPauta() {
		actionFacesBean.setIdTipoAndamento(Andamentos.INCLUSAO_EM_PAUTA.getId());
		TipoAgendamento tipoAgendamentoExperado = TipoAgendamento.PAUTA;
		TipoAgendamento tipoAgendamentoRetorno = actionFacesBean.getTipoAgendamentoEscolhido();
		assertEquals(tipoAgendamentoExperado, tipoAgendamentoRetorno);
	}

	@Test
	public void testLabelSessao_Ordinaria() throws ServiceException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateSessaoPlenario = "01-08-2016 14:00:00";
		Date dateInicioExperado = sdf.parse(dateSessaoPlenario);

		Sessao sessaoPlenario = agrupadorRepo.getSessao(1L);
		sessaoPlenario.setDataInicio(dateInicioExperado);

		String nomeListaRetono = actionFacesBean.labelSessao(sessaoPlenario);
		assertEquals("01/08/2016 14:00:00 - Plenário - Sessão Ordinária", nomeListaRetono);
	}

	@Test
	public void testLabelSessao_Virtual() throws ServiceException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInicioSessao = "05-08-2016 00:00:00";
		String dateFimSessao = "11-08-2016 23:59:00";

		Sessao sessao = agrupadorRepo.getSessao(1L);
		sessao.setTipoAmbiente(TipoAmbienteConstante.VIRTUAL.getSigla());
		sessao.setDataInicio(sdf.parse(dateInicioSessao));
		sessao.setDataPrevistaFim(sdf.parse(dateFimSessao));

		String nomeListaRetono = actionFacesBean.labelSessao(sessao);
		assertEquals("05/08/2016 a 11/08/2016 - Plenário - Sessão Virtual", nomeListaRetono);
	}

	@Test
	public void testGetDescricaoTipoSessao_MostraVirtual() throws ServiceException, ParseException {
		Sessao sessao = agrupadorRepo.getSessao(1L);
		sessao.setTipoAmbiente(TipoAmbienteConstante.VIRTUAL.getSigla());

		String retornoDescricaoTipoSessao = actionFacesBean.getDescricaoTipoSessao(sessao);
		assertEquals("Sessão Virtual", retornoDescricaoTipoSessao);
	}

	@Test
	public void testGetDescricaoTipoSessao_MostraOrdinaria() throws ServiceException, ParseException {
		Sessao sessao = agrupadorRepo.getSessao(1L);

		String retornoDescricaoTipoSessao = actionFacesBean.getDescricaoTipoSessao(sessao);
		assertEquals("Sessão Ordinária", retornoDescricaoTipoSessao);
	}

	@Test
	public void getDataInicio_presencial() throws ServiceException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateSessaoPlenario = "01-08-2016 14:00:00";
		Date dateInicioExperado = sdf.parse(dateSessaoPlenario);

		Sessao sessaoPlenario = agrupadorRepo.getSessao(1L);
		sessaoPlenario.setDataInicio(dateInicioExperado);

		Date dateInicioRetono = DataUtil.getDataInicioSessao(sessaoPlenario);
		assertEquals(dateInicioExperado, dateInicioRetono);
	}

	@Test
	public void getDataInicio_virtual_DataInicio() throws ServiceException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateSessaoPlenario01 = "01-08-2016 14:00:00";
		Date dateInicioExperado = sdf.parse(dateSessaoPlenario01);

		Sessao sessaoPlenario = agrupadorRepo.getSessao(1L);
		sessaoPlenario.setDataInicio(dateInicioExperado);
		sessaoPlenario.setDataPrevistaInicio(null);
		sessaoPlenario.setTipoAmbiente(TipoAmbienteConstante.VIRTUAL.getSigla());

		Date dateInicioRetono = DataUtil.getDataInicioSessao(sessaoPlenario);
		assertEquals(dateInicioExperado, dateInicioRetono);
	}

	@Test
	public void getDataInicio_virtual_DataPrevistaInicio() throws ServiceException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateSessaoPlenario01 = "01-08-2016 14:00:00";
		Date dateInicioExperado = sdf.parse(dateSessaoPlenario01);

		Sessao sessaoPlenario = agrupadorRepo.getSessao(1L);
		sessaoPlenario.setDataInicio(null);
		sessaoPlenario.setDataPrevistaInicio(dateInicioExperado);
		sessaoPlenario.setTipoAmbiente(TipoAmbienteConstante.VIRTUAL.getSigla());

		Date dateInicioRetono = DataUtil.getDataInicioSessao(sessaoPlenario);
		assertEquals(dateInicioExperado, dateInicioRetono);
	}

}