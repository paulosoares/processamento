package br.jus.stf.estf.decisao.objetoincidente.web;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.estf.util.TipoAmbiente;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.support.util.ListaJulgamentoUI;

public class GerenciarListasLiberadasActionFacesBeanTest {
	
	@Mock
	private SessaoService sessaoService;
	
	@Mock
	private LiberarPreListaParaJulgamentoActionFacesBean liberarPreListaParaJulgamentoActionFacesBean;
	
	private GerenciarListasLiberadasActionFacesBean actionFacesBean;
	
	private AgrupadorRepo agrupadorRepo = new AgrupadorRepo();	

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		actionFacesBean = new GerenciarListasLiberadasActionFacesBean();
		Whitebox.setInternalState(actionFacesBean, "sessaoService", sessaoService);
		Whitebox.setInternalState(actionFacesBean, "liberarPreListaParaJulgamentoActionFacesBean", liberarPreListaParaJulgamentoActionFacesBean);
	}
	
	private List<ListaJulgamento> getListDeListaJulgamento() {		
		ListaJulgamento listaJulgamento01 = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		ListaJulgamento listaJulgamento02 = agrupadorRepo.getListaJulgamento(2L,"Lista2");		
		List<ListaJulgamento> listasListaJulgamentoRetorno = new ArrayList<ListaJulgamento>();
		listasListaJulgamentoRetorno.add(listaJulgamento01);
		listasListaJulgamentoRetorno.add(listaJulgamento02);
		return listasListaJulgamentoRetorno;
	}	

	private List<ListaJulgamentoUI> listaJulgamentoUIMock() {		
		ListaJulgamento listaJulgamento01 = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		ListaJulgamento listaJulgamento02 = agrupadorRepo.getListaJulgamento(2L,"Lista2");
		String descricaoColegiado = TipoColegiadoConstante.valueOfSigla(Colegiado.PRIMEIRA_TURMA).getDescricao();
		ListaJulgamentoUI listaJulgamentoUI01 = new ListaJulgamentoUI(listaJulgamento01,descricaoColegiado,TipoAmbienteConstante.PRESENCIAL.getDescricao());
		ListaJulgamentoUI listaJulgamentoUI02 = new ListaJulgamentoUI(listaJulgamento02,descricaoColegiado,TipoAmbienteConstante.PRESENCIAL.getDescricao());		
		List<ListaJulgamentoUI> listaJulgamentoUI = new ArrayList<ListaJulgamentoUI>();
		listaJulgamentoUI.add(listaJulgamentoUI01);
		listaJulgamentoUI.add(listaJulgamentoUI02);
		return listaJulgamentoUI;
	}		

	@Test
	public void load() throws ServiceException {
		List<ListaJulgamentoUI> listasListaJulgamentoUI = this.listaJulgamentoUIMock();		
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doNothing().when(actionFacesBean).gabineteSelecionado();
		Mockito.doReturn(listasListaJulgamentoUI).when(actionFacesBean).carregalistaJulgamentoUI();
		actionFacesBean.load();
		Mockito.verify(actionFacesBean).setListasListaJulgamentoUI(listasListaJulgamentoUI);
	}
	
	@Test(expected = Exception.class)
	public void imprimir_erro() throws ServiceException {		
		List<ListaJulgamento> listDeListaJulgamento = getListDeListaJulgamento();
		
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn(listDeListaJulgamento).when(actionFacesBean).getListasListaJulgamentoSelecinadas();
		
		actionFacesBean.imprimir();
		Mockito.verify(actionFacesBean).addWarning(GerenciarListasLiberadasActionFacesBean.MAIL_LIST_PLENARIO);
	}	
	
	@Test(expected = Exception.class)
	public void mail_erro() throws ServiceException {
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doThrow(ServiceException.class).when(actionFacesBean).getListasListaJulgamentoSelecinadas();		
		actionFacesBean.mail();
		Mockito.verify(actionFacesBean).addError(GerenciarListasLiberadasActionFacesBean.MSG_ERRO_MAIL);
	}	
	
	@Test
	public void carregalistaJulgamentoUI() throws ServiceException {
		long idMinistro = 1L;
		
		List<ListaJulgamento> listasPlenario     = new ArrayList<ListaJulgamento>();
		List<ListaJulgamento> listasPrimeiraTurma= new ArrayList<ListaJulgamento>();
		List<ListaJulgamento> listasSuegundaTurma= getListDeListaJulgamento();
		Colegiado colegiado = agrupadorRepo.getColegiado(Colegiado.SEGUNDA_TURMA);
		
		listasSuegundaTurma.get(0).getSessao().setColegiado(colegiado);
		listasSuegundaTurma.get(1).getSessao().setColegiado(colegiado);
		
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn(idMinistro).when(actionFacesBean).getIdMinistro();
		Mockito.doReturn(listasPlenario).when(actionFacesBean).recuperaListasLiberadasColegiado(idMinistro,TipoColegiadoConstante.SESSAO_PLENARIA);
		Mockito.doReturn(listasPrimeiraTurma).when(actionFacesBean).recuperaListasLiberadasColegiado(idMinistro,TipoColegiadoConstante.PRIMEIRA_TURMA);
		Mockito.doReturn(listasSuegundaTurma).when(actionFacesBean).recuperaListasLiberadasColegiado(idMinistro,TipoColegiadoConstante.SEGUNDA_TURMA);
		
		List<ListaJulgamentoUI> listasListaJulgamentoUIRetorno = new ArrayList<ListaJulgamentoUI>();
		listasListaJulgamentoUIRetorno = actionFacesBean.carregalistaJulgamentoUI();		
		assertEquals(2, listasListaJulgamentoUIRetorno.size());
	}

	
	@Test
	public void getDataInicio_getNomeListaComData() throws ServiceException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateSessaoPlenario = "01-08-2016 14:00:00";
		Date dateInicioExperado = sdf.parse(dateSessaoPlenario);		
		
		Sessao sessao = agrupadorRepo.getSessao(1L);
		sessao.setDataInicio(dateInicioExperado);
		ListaJulgamento listaJulgamento = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		listaJulgamento.setSessao(sessao);
		
		String dataSessoExperado = "01/08/2016 14:00:00";
		
		Mockito.when(liberarPreListaParaJulgamentoActionFacesBean.getDescricaoDataSessao(sessao)).thenReturn(dataSessoExperado);
		
		String nomeListaRetono = actionFacesBean.getNomeListaComData(listaJulgamento);
		assertEquals("Lista1 de 01/08/2016 14:00:00",nomeListaRetono);
	}	
	
	@Test
	public void recuperaDestinatario_AmbienteNaoProducao() throws ServiceException {
		TipoColegiadoConstante colegiadoDestino = TipoColegiadoConstante.SESSAO_PLENARIA;

		actionFacesBean = Mockito.spy(actionFacesBean);
		String remetente = "usuario@stf.gob.br";
		Mockito.doReturn(remetente).when(actionFacesBean).getRemetente();
		Mockito.doReturn(TipoAmbiente.DESENVOLVIMENTO).when(actionFacesBean).getTipoAmbiente();
		
		String mailRetorno = actionFacesBean.recuperaDestinatario(colegiadoDestino);
		assertEquals(remetente,mailRetorno);
	}	
	
	@Test
	public void recuperaDestinatario_plenario() throws ServiceException {
		TipoColegiadoConstante colegiadoDestino = TipoColegiadoConstante.SESSAO_PLENARIA;

		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn("usuario@stf.gob.br").when(actionFacesBean).getRemetente();
		Mockito.doReturn(TipoAmbiente.PRODUCAO).when(actionFacesBean).getTipoAmbiente();		
		
		String mailRetorno = actionFacesBean.recuperaDestinatario(colegiadoDestino);
		assertEquals(mailRetorno, GerenciarListasLiberadasActionFacesBean.MAIL_LIST_PLENARIO);
	}

	@Test
	public void recuperaDestinatario_1turma() throws ServiceException {
		TipoColegiadoConstante colegiadoDestino = TipoColegiadoConstante.PRIMEIRA_TURMA;
		
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn("usuario@stf.gob.br").when(actionFacesBean).getRemetente();
		Mockito.doReturn(TipoAmbiente.PRODUCAO).when(actionFacesBean).getTipoAmbiente();
		
		String mailRetorno = actionFacesBean.recuperaDestinatario(colegiadoDestino);;
		assertEquals(mailRetorno, GerenciarListasLiberadasActionFacesBean.MAIL_LIST_TURMA01);
	}
	
	@Test
	public void recuperaDestinatario_2turma() throws ServiceException {
		TipoColegiadoConstante colegiadoDestino = TipoColegiadoConstante.SEGUNDA_TURMA;
				
		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doReturn("usuario@stf.gob.br").when(actionFacesBean).getRemetente();
		Mockito.doReturn(TipoAmbiente.PRODUCAO).when(actionFacesBean).getTipoAmbiente();
		
		String mailRetorno = actionFacesBean.recuperaDestinatario(colegiadoDestino);
		assertEquals(mailRetorno, GerenciarListasLiberadasActionFacesBean.MAIL_LIST_TURMA02);
	}
	
	@Test (expected = Exception.class)
	public void getListasListaJulgamentoSelecinadas_erro() throws ServiceException {
		List<ListaJulgamentoUI> listDeListaJulgamentoUI = this.listaJulgamentoUIMock();
		listDeListaJulgamentoUI.get(0).setSelected(false);
		listDeListaJulgamentoUI.get(1).setSelected(false);
		actionFacesBean.setListasListaJulgamentoUI(listDeListaJulgamentoUI);
		
		@SuppressWarnings("unused")
		List<ListaJulgamento> retorno   = actionFacesBean.getListasListaJulgamentoSelecinadas();
	}	
	
	@Test
	public void getListasListaJulgamentoSelecinadas_ok() throws ServiceException {
		List<ListaJulgamentoUI> listDeListaJulgamentoUI = this.listaJulgamentoUIMock();
		Long idEsperado = listDeListaJulgamentoUI.get(0).getInstancia().getId();
		listDeListaJulgamentoUI.get(0).setSelected(true);
		listDeListaJulgamentoUI.get(1).setSelected(false);
		actionFacesBean.setListasListaJulgamentoUI(listDeListaJulgamentoUI);		

		List<ListaJulgamento> retorno   = actionFacesBean.getListasListaJulgamentoSelecinadas();
		assertEquals(1, retorno.size());
		assertEquals(idEsperado, retorno.get(0).getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void separaEmailPorColegiado_ok() throws Exception {
		Colegiado colegiadoT01 = agrupadorRepo.getColegiado(Colegiado.PRIMEIRA_TURMA);
		Colegiado colegiadoT02 = agrupadorRepo.getColegiado(Colegiado.SEGUNDA_TURMA);
		Colegiado colegiadoPle = agrupadorRepo.getColegiado(Colegiado.TRIBUNAL_PLENO);
		
		Sessao sessaoT01 = agrupadorRepo.getSessao(1L);
		sessaoT01.setColegiado(colegiadoT01);
		Sessao sessaoT02 = agrupadorRepo.getSessao(2L);
		sessaoT02.setColegiado(colegiadoT02);
		Sessao sessaoPle = agrupadorRepo.getSessao(3L);
		sessaoPle.setColegiado(colegiadoPle);
		
		
		ListaJulgamento listaJulgamento01 = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		listaJulgamento01.setSessao(sessaoPle);
		
		ListaJulgamento listaJulgamento02 = agrupadorRepo.getListaJulgamento(2L,"Lista2");
		ListaJulgamento listaJulgamento03 = agrupadorRepo.getListaJulgamento(3L,"Lista3");
		listaJulgamento02.setSessao(sessaoT01);
		listaJulgamento03.setSessao(sessaoT01);
		
		ListaJulgamento listaJulgamento04 = agrupadorRepo.getListaJulgamento(4L,"Lista4");
		listaJulgamento04.setSessao(sessaoT02);
		
		
		List<ListaJulgamento> listDeListaJulgamentoDestinoPlenario = new ArrayList<ListaJulgamento>();
		listDeListaJulgamentoDestinoPlenario.add(listaJulgamento01);
		
		List<ListaJulgamento> listDeListaJulgamentoDestinoTurma01  = new ArrayList<ListaJulgamento>();
		listDeListaJulgamentoDestinoTurma01.add(listaJulgamento02);
		listDeListaJulgamentoDestinoTurma01.add(listaJulgamento03);		
		
		List<ListaJulgamento> listDeListaJulgamentoDestinoTurma02  = new ArrayList<ListaJulgamento>();
		listDeListaJulgamentoDestinoTurma02.add(listaJulgamento04);
		
		List<ListaJulgamento> listDeListaJulgamento = new ArrayList<ListaJulgamento>();
		listDeListaJulgamento.addAll(listDeListaJulgamentoDestinoPlenario);
		listDeListaJulgamento.addAll(listDeListaJulgamentoDestinoTurma01);
		listDeListaJulgamento.addAll(listDeListaJulgamentoDestinoTurma02);
		

		actionFacesBean = Mockito.spy(actionFacesBean);
		Mockito.doNothing().when(actionFacesBean).validarListasMesmaData(Mockito.anyList(), Mockito.anyInt());
		Mockito.doNothing().when(actionFacesBean).geraEmailColegiado(listDeListaJulgamentoDestinoPlenario, TipoColegiadoConstante.SESSAO_PLENARIA);
		Mockito.doNothing().when(actionFacesBean).geraEmailColegiado(listDeListaJulgamentoDestinoTurma01, TipoColegiadoConstante.PRIMEIRA_TURMA);
		Mockito.doNothing().when(actionFacesBean).geraEmailColegiado(listDeListaJulgamentoDestinoTurma02, TipoColegiadoConstante.SEGUNDA_TURMA);
		
		actionFacesBean.separaEmailPorColegiado(listDeListaJulgamento);
		
		Mockito.verify(actionFacesBean, Mockito.times(1)).geraEmailColegiado(listDeListaJulgamentoDestinoPlenario,TipoColegiadoConstante.SESSAO_PLENARIA);
		Mockito.verify(actionFacesBean, Mockito.times(1)).geraEmailColegiado(listDeListaJulgamentoDestinoTurma01,TipoColegiadoConstante.PRIMEIRA_TURMA);
		Mockito.verify(actionFacesBean, Mockito.times(1)).geraEmailColegiado(listDeListaJulgamentoDestinoTurma02,TipoColegiadoConstante.SEGUNDA_TURMA);
	}
	
	private List<ListaJulgamento> getListDeListaJulgamento_validarListas() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateSessaoPlenario01 = "01-08-2016 14:00:00";
		String dateSessaoPlenario02 = "15-08-2016 14:00:00";
		
		
		Colegiado colegiadoPle = agrupadorRepo.getColegiado(Colegiado.TRIBUNAL_PLENO);
		
		Sessao sessaoPlenario01 = agrupadorRepo.getSessao(1L);
		sessaoPlenario01.setColegiado(colegiadoPle);
		sessaoPlenario01.setDataInicio(sdf.parse(dateSessaoPlenario01));
		sessaoPlenario01.setDataPrevistaInicio(sdf.parse(dateSessaoPlenario01));
		
		Sessao sessaoPlenario02 = agrupadorRepo.getSessao(2L);
		sessaoPlenario02.setColegiado(colegiadoPle);
		sessaoPlenario02.setDataInicio(sdf.parse(dateSessaoPlenario02));
		sessaoPlenario02.setDataPrevistaInicio(sdf.parse(dateSessaoPlenario02));
		
		ListaJulgamento listaJulgamento01 = agrupadorRepo.getListaJulgamento(1L,"Lista1");
		listaJulgamento01.setSessao(sessaoPlenario01);
		ListaJulgamento listaJulgamento02 = agrupadorRepo.getListaJulgamento(2L,"Lista2");
		listaJulgamento02.setSessao(sessaoPlenario02);
		
		List<ListaJulgamento> listDeListaJulgamento = new ArrayList<ListaJulgamento>();
		listDeListaJulgamento.add(listaJulgamento01);
		listDeListaJulgamento.add(listaJulgamento02);
		
		return listDeListaJulgamento;
	}
	
	@Test
	public void validarListasMesmaData_ok_index0() throws ServiceException, ParseException{
		List<ListaJulgamento> listDeListaJulgamento = this.getListDeListaJulgamento_validarListas();		
		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.validarListasMesmaData(listDeListaJulgamento,0);		
	}
	
	@Test (expected = ServiceException.class)
	public void validarListasMesmaData_falha() throws ServiceException, ParseException{		
		List<ListaJulgamento> listDeListaJulgamento = this.getListDeListaJulgamento_validarListas();
		
		Sessao sessaoAnterior = listDeListaJulgamento.get(0).getSessao();
//		Mockito.when(DataUtil.getDataInicioSessao(sessaoAnterior)).thenReturn(sessaoAnterior.getDataPrevistaInicio());
		
		Sessao sessaoCorrente = listDeListaJulgamento.get(1).getSessao();
//		Mockito.when(DataUtil.getDataInicioSessao(sessaoCorrente)).thenReturn(sessaoCorrente.getDataPrevistaInicio());
		
		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.validarListasMesmaData(listDeListaJulgamento,1);
	}
	
	
	@Test
	public void validarListasMesmaData_ok_mesmaDataColegiadosDiferentes() throws ServiceException, ParseException{
		String dateSessaoPlenario02 = "15/08/2016 14:00:00";
		Date dateSessao         = DataUtil.string2Date(dateSessaoPlenario02,true);
		
		Colegiado colegiadoT01 = agrupadorRepo.getColegiado(Colegiado.PRIMEIRA_TURMA);
		
		Sessao sessaoPlenario01 = agrupadorRepo.getSessao(1L);
		sessaoPlenario01.setColegiado(colegiadoT01);
		sessaoPlenario01.setDataInicio(dateSessao);
		sessaoPlenario01.setDataPrevistaInicio(dateSessao);
		
		
		List<ListaJulgamento> listDeListaJulgamento = this.getListDeListaJulgamento_validarListas();
		listDeListaJulgamento.get(0).setSessao(sessaoPlenario01);
		
		
		Sessao sessaoAnterior = listDeListaJulgamento.get(0).getSessao();
//		Mockito.when(DataUtil.getDataInicioSessao(sessaoAnterior)).thenReturn(sessaoAnterior.getDataPrevistaInicio());
		
		Sessao sessaoCorrente = listDeListaJulgamento.get(1).getSessao();
//		Mockito.when(DataUtil.getDataInicioSessao(sessaoCorrente)).thenReturn(sessaoCorrente.getDataPrevistaInicio());		
		
		actionFacesBean = Mockito.spy(actionFacesBean);
		actionFacesBean.validarListasMesmaData(listDeListaJulgamento,1);
	}	
}