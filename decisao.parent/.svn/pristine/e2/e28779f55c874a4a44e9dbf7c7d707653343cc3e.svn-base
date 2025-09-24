package br.jus.stf.estf.decisao.objetoincidente.web;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.support.ValidacaoLiberacaoParaJulgamentoException;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.service.ConfiguracaoSistemaService;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;
import br.jus.stf.estf.decisao.support.util.ColunaDragAndDrop;

public class RevisarListasFacesBeanTest {
	
	@Mock
	private PreListaJulgamentoService preListaJulgamentoService;	
	
	@Mock
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Mock
	private ConfiguracaoSistemaService configuracaoSistemaService;
	
	@Mock
	private UsuarioLogadoService usuarioLogadoService;
	
	private RevisarListasFacesBean revisarListasFacesBean;
	
	private AgrupadorRepo agrupadorRepo = new AgrupadorRepo();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		revisarListasFacesBean = new RevisarListasFacesBean();
		Whitebox.setInternalState(revisarListasFacesBean, "configuracaoSistemaService", configuracaoSistemaService);
		Whitebox.setInternalState(revisarListasFacesBean, "preListaJulgamentoService", preListaJulgamentoService);
		Whitebox.setInternalState(revisarListasFacesBean, "objetoIncidenteService", objetoIncidenteService);
		Whitebox.setInternalState(revisarListasFacesBean, "usuarioLogadoService", usuarioLogadoService);
	}

	@Test
	public void hasErrorTest_false() {
		boolean retorno = revisarListasFacesBean.hasError();
		assertEquals(false, retorno);
	}	
	
	//@Test
	public void hasErrorTest_true() {
		revisarListasFacesBean.addError("msg de erro");
		boolean retorno = revisarListasFacesBean.hasError();
		assertEquals(true, retorno);
	}

	@Test
	public void getPossuiAlgumTexto_false(){
		boolean retorno = revisarListasFacesBean.getPossuiAlgumTexto();
		assertEquals(false, retorno);
	}
	
	@Test
	public void getPossuiAlgumTexto_true01(){
		revisarListasFacesBean.setTextoEmenta("Tem ementa");
		revisarListasFacesBean.setTextoRelatorio("Tem Relatorio");
		revisarListasFacesBean.setTextoVoto("Tem Voto");
		boolean retorno = revisarListasFacesBean.getPossuiAlgumTexto();
		assertEquals(true, retorno);
	}
	
	@Test
	public void getPossuiAlgumTexto_true02(){
		revisarListasFacesBean.setTextoEmenta(null);
		revisarListasFacesBean.setTextoRelatorio("Tem Relatorio");
		revisarListasFacesBean.setTextoVoto("Tem Voto");
		boolean retorno = revisarListasFacesBean.getPossuiAlgumTexto();
		assertEquals(true, retorno);
	}
	
	@Test
	public void getPossuiAlgumTexto_true03(){
		revisarListasFacesBean.setTextoEmenta(null);
		revisarListasFacesBean.setTextoRelatorio(null);
		revisarListasFacesBean.setTextoVoto("Tem Voto");
		boolean retorno = revisarListasFacesBean.getPossuiAlgumTexto();
		assertEquals(true, retorno);
	}	
	
	@Test
	public void init(){
		Setor setor = new Setor();
		setor.setId(6000000435L);

		Ministro ministro = new Ministro();
		ministro.setSetor(setor);
		
		revisarListasFacesBean = Mockito.spy(revisarListasFacesBean);
		Mockito.doNothing().when(revisarListasFacesBean).inicializarColunasDragAndDrop();
		Mockito.when(configuracaoSistemaService.isOrdenacaoNumerica()).thenReturn(false);
		Mockito.when(usuarioLogadoService.getMinistro()).thenReturn(ministro);
		
		revisarListasFacesBean.init();
	}
	
	@Test (expected = IllegalStateException.class)
	public void adicionarObjetoIncidente_NaoVerificaJulgado(){
		boolean verificarSeFoiJulgado = false;
		ObjetoIncidenteDto objetoIncidenteSelecionado = null;
		revisarListasFacesBean.setObjIncidenteSelecionado(objetoIncidenteSelecionado);
		revisarListasFacesBean.adicionarObjetoIncidente(verificarSeFoiJulgado);
		Mockito.verify(revisarListasFacesBean).addError(RevisarListasFacesBean.MSG_PROCESSO_OBRIGATORIO);
	}
	
	@Test (expected = IllegalStateException.class)
	public void adicionarObjetoIncidente_VerificaJulgadoJaEmLista() throws ServiceException {
		boolean verificarSeFoiJulgado = false;
		ObjetoIncidenteDto objetoIncidenteSelecionado = agrupadorRepo.getObjetoIncidenteDto(1,(long)1111);
		Processo objetoIncidente = agrupadorRepo.getObjetoIncidente(1,"1111");
		PreListaJulgamento preListaJulgamento = agrupadorRepo.getPreListaJulgamento(1, "Lista");

		revisarListasFacesBean.setObjIncidenteSelecionado(objetoIncidenteSelecionado);
		
		Mockito.when(objetoIncidenteService.recuperarPorId(objetoIncidenteSelecionado.getId())).thenReturn(objetoIncidente);
		Mockito.when(preListaJulgamentoService.recuperarPreListaJulgamentoAtiva(objetoIncidente)).thenReturn(preListaJulgamento);
		
		revisarListasFacesBean.adicionarObjetoIncidente(verificarSeFoiJulgado);		
		Mockito.verify(revisarListasFacesBean).addError(RevisarListasFacesBean.MSG_ERRO_NAO_ADICIONAR_LISTA);
	}
	
	@Test
	public void adicionarObjetoIncidente_VerificaJulgadoNaoEmLista() throws ServiceException {
		boolean verificarSeFoiJulgado = true;
		Ministro ministro = new Ministro();
		ministro.setId(1L);
		ObjetoIncidenteDto objetoIncidenteSelecionado = agrupadorRepo.getObjetoIncidenteDto(1,(long)1111);
		objetoIncidenteSelecionado.setIdRelator(ministro.getId());
		Processo objetoIncidente = agrupadorRepo.getObjetoIncidente(1,"1111");
		PreListaJulgamento preListaJulgamento = null;
		ColunaDragAndDrop<ObjetoIncidenteDto> coluna = new ColunaDragAndDrop<ObjetoIncidenteDto>(1L, "Mock", false);
		
		revisarListasFacesBean.setObjIncidenteSelecionado(objetoIncidenteSelecionado);
		
		revisarListasFacesBean = Mockito.spy(revisarListasFacesBean);
		Mockito.doReturn(false).when(revisarListasFacesBean).isObjetoIncidenteJulgado(objetoIncidenteSelecionado);
		Mockito.doReturn(PreListaJulgamentoMotivoAlteracao.MANUAL).when(revisarListasFacesBean).recuperaMotivoInclusao(objetoIncidenteSelecionado);
		Mockito.doNothing().when(revisarListasFacesBean).persistirProcessoNaLista(coluna.getId(), objetoIncidenteSelecionado.getId(), PreListaJulgamentoMotivoAlteracao.MANUAL);
		Mockito.doReturn(coluna).when(revisarListasFacesBean).recuperaColunaDrapAndDropSelecionada();
		Mockito.doNothing().when(revisarListasFacesBean).addError(CategorizarProcessoActionFacesBean.MSG_ERRO_NAO_FOI_POSSIVEL_CARREGAR_CAT_GABINETE);
		
		Mockito.when(objetoIncidenteService.recuperarPorId(objetoIncidenteSelecionado.getId())).thenReturn(objetoIncidente);
		Mockito.when(preListaJulgamentoService.recuperarPreListaJulgamentoAtiva(objetoIncidente)).thenReturn(preListaJulgamento);
		Mockito.when(usuarioLogadoService.getMinistro()).thenReturn(ministro);

		try {
			Mockito.doNothing().when(revisarListasFacesBean).validarMinistroRelatorOuVistor(objetoIncidente, ministro);
		} catch (ValidacaoLiberacaoParaJulgamentoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		revisarListasFacesBean.adicionarObjetoIncidente(verificarSeFoiJulgado);
	}
	
	@Test
	public void adicionarObjetoIncidente_VerificaJulgadoNaoEmLista02() throws ServiceException {
		boolean verificarSeFoiJulgado = true;
		Ministro ministro = new Ministro();
		ministro.setId(1L);
		ObjetoIncidenteDto objetoIncidenteSelecionado = agrupadorRepo.getObjetoIncidenteDto(1,(long)1111);
		objetoIncidenteSelecionado.setIdRelator(ministro.getId());
		Processo objetoIncidente = agrupadorRepo.getObjetoIncidente(1,"1111");
		PreListaJulgamento preListaJulgamento = null;
		
		revisarListasFacesBean.setObjIncidenteSelecionado(objetoIncidenteSelecionado);
		
		revisarListasFacesBean = Mockito.spy(revisarListasFacesBean);
		Mockito.doReturn(true).when(revisarListasFacesBean).isObjetoIncidenteJulgado(objetoIncidenteSelecionado);
		
		Mockito.when(objetoIncidenteService.recuperarPorId(objetoIncidenteSelecionado.getId())).thenReturn(objetoIncidente);
		Mockito.when(preListaJulgamentoService.recuperarPreListaJulgamentoAtiva(objetoIncidente)).thenReturn(preListaJulgamento);
		Mockito.when(usuarioLogadoService.getMinistro()).thenReturn(ministro);
		
		try {
			Mockito.doNothing().when(revisarListasFacesBean).validarMinistroRelatorOuVistor(objetoIncidente, ministro);
		} catch (ValidacaoLiberacaoParaJulgamentoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		revisarListasFacesBean.adicionarObjetoIncidente(verificarSeFoiJulgado);
	}
	
	public PreListaJulgamentoMotivoAlteracao recuperaMotivoInclusao_Mock(PreListaJulgamentoMotivoAlteracao motivoEsperado
			                                                            ,boolean TemRelatorio
			                                                            ,boolean Jajulgado
			                                                            ,boolean TemEmenta
			                                                            ,boolean TemVoto) throws ServiceException {
		ObjetoIncidenteDto objetoIncidenteSelecionado = agrupadorRepo.getObjetoIncidenteDto(1,(long)1111);
		Processo objetoIncidente = agrupadorRepo.getObjetoIncidente(1,"1111");
		
		revisarListasFacesBean.setObjIncidenteSelecionado(objetoIncidenteSelecionado);
		
		revisarListasFacesBean = Mockito.spy(revisarListasFacesBean);
		Mockito.doReturn(TemRelatorio).when(revisarListasFacesBean).temRelatorio(objetoIncidente);
		Mockito.doReturn(Jajulgado).when(revisarListasFacesBean).isObjetoIncidenteJulgado(objetoIncidenteSelecionado);		
		Mockito.doReturn(TemEmenta).when(revisarListasFacesBean).temEmenta(objetoIncidente);		
		Mockito.doReturn(TemVoto).when(revisarListasFacesBean).temVoto(objetoIncidente);	
		
		Mockito.when(objetoIncidenteService.recuperarPorId(objetoIncidenteSelecionado.getId())).thenReturn(objetoIncidente);
		
		PreListaJulgamentoMotivoAlteracao motivoRetornado = revisarListasFacesBean.recuperaMotivoInclusao(objetoIncidenteSelecionado);
		return motivoRetornado;
	}

	@Test
	public void recuperaMotivoInclusao_SemRelatorio() throws ServiceException {
		PreListaJulgamentoMotivoAlteracao motivoEsperado = PreListaJulgamentoMotivoAlteracao.SEM_RELATORIO;
		PreListaJulgamentoMotivoAlteracao motivoRetornado = this.recuperaMotivoInclusao_Mock(motivoEsperado, false, false, true, true);
		assertEquals(motivoEsperado, motivoRetornado);
	}	
	
	
	@Test
	public void recuperaMotivoInclusao_JaJulgado() throws ServiceException {
		PreListaJulgamentoMotivoAlteracao motivoEsperado = PreListaJulgamentoMotivoAlteracao.JA_JULGADO;
		PreListaJulgamentoMotivoAlteracao motivoRetornado = this.recuperaMotivoInclusao_Mock(motivoEsperado, true, true, true, true);
		assertEquals(motivoEsperado, motivoRetornado);
	}
	
	
	@Test
	public void recuperaMotivoInclusao_SemEmenta() throws ServiceException {
		PreListaJulgamentoMotivoAlteracao motivoEsperado = PreListaJulgamentoMotivoAlteracao.SEM_EMENTA;
		PreListaJulgamentoMotivoAlteracao motivoRetornado = this.recuperaMotivoInclusao_Mock(motivoEsperado, true, false, false, true);
		assertEquals(motivoEsperado, motivoRetornado);
	}
	
	@Test
	public void recuperaMotivoInclusao_SemVoto() throws ServiceException {
		PreListaJulgamentoMotivoAlteracao motivoEsperado = PreListaJulgamentoMotivoAlteracao.SEM_VOTO;
		PreListaJulgamentoMotivoAlteracao motivoRetornado = this.recuperaMotivoInclusao_Mock(motivoEsperado, true, false, true, false);
		assertEquals(motivoEsperado, motivoRetornado);
	}
	
	@Test
	public void recuperaMotivoInclusao_Manual() throws ServiceException {
		PreListaJulgamentoMotivoAlteracao motivoEsperado = PreListaJulgamentoMotivoAlteracao.MANUAL;
		PreListaJulgamentoMotivoAlteracao motivoRetornado = this.recuperaMotivoInclusao_Mock(motivoEsperado, true, false, true, true);
		assertEquals(motivoEsperado, motivoRetornado);
	}
	
	@Test (expected = Exception.class)
	public void salvarObservacaoIncidentePreLista_falha() throws ServiceException {
		String textoGrande = "Toda a gente já teve que enrolar ao escrever um texto. Seja durante um artigo, uma carta ou"
				            +" qualquer outro documento, o facto é que, para preencher a enorme quantidade de linhas, precisamos"
				            +" utilizar toda a nossa criatividade. Mas esse problema já tem solução. Com O Fabuloso Gerador de"
				            +" Lero-lero, não precisará mais se preocupar. Com O Fabuloso Gerador de Lero-lero, poderá gerar"
				            +" um texto com o tamanho que precisar. Basta escolher a quantidade de parágrafos que quer que seu"
				            +" texto tenha e clicar em Gerar para criar um texto (ideal para engrossar uma tese de mestrado,"
				            +" impressionar o seu chefe ou preparar discursos capazes de curar a insónia da plateia). Copie o"
				            +" texto gerado para um editor de texto e formate-o.";

		revisarListasFacesBean = Mockito.spy(revisarListasFacesBean);
		Mockito.doReturn(textoGrande).when(revisarListasFacesBean).getObservacao();
		
		revisarListasFacesBean.salvarObservacaoIncidentePreLista();
	}	
}