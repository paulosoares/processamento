package br.gov.stf.estf.julgamento.model.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.publicacao.Feriado;
import br.gov.stf.estf.julgamento.model.dataaccess.hibernate.SessaoDaoHibernate;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.service.ListaJulgamentoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.publicacao.model.service.ConteudoPublicacaoService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

public class SessaoServiceImplTest {
	
	@Mock
	private ListaJulgamentoService listaJulgamentoService;
	
	@Mock
	private JulgamentoProcessoService julgamentoProcessoService;
	
	@Mock
	private AndamentoProcessoService andamentoProcessoService;
	
	@Mock
	private ControleVotoService controleVotoService;
	
	@Mock
	private ConteudoPublicacaoService conteudoPublicacaoService;
	
	@Mock
	private SessaoDaoHibernate dao;

	private SessaoServiceImpl sessaoService;
	
	private SessaoServiceImplTestRepo repo = new SessaoServiceImplTestRepo();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		sessaoService = new SessaoServiceImpl(null);
		Whitebox.setInternalState(sessaoService, "listaJulgamentoService", listaJulgamentoService);
		Whitebox.setInternalState(sessaoService, "julgamentoProcessoService", julgamentoProcessoService);
		Whitebox.setInternalState(sessaoService, "andamentoProcessoService", andamentoProcessoService);
		Whitebox.setInternalState(sessaoService, "controleVotoService", controleVotoService);
		Whitebox.setInternalState(sessaoService, "conteudoPublicacaoService", conteudoPublicacaoService);
		Whitebox.setInternalState(sessaoService, "dao", dao);
	}
	
	@Test
	public void testFinalizarSessoesVirtuais_SemSessoesEncerrada() throws ServiceException, DaoException{
		List<Sessao> sessoes = new ArrayList<Sessao>();
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(sessoes).when(sessaoService).getListSessoesEncerradas();		
		sessaoService.finalizarSessoesVirtuais();
	}
	
	@Test
	public void testFinalizarSessoesVirtuais_SessoesEncerradaComDataFim() throws ServiceException, DaoException{
		Date dataFim = new Date();
		
		Sessao sessao01 = repo.getSessao(1);
		sessao01.setDataFim(dataFim);

		Sessao sessao02 = repo.getSessao(2);
		sessao02.setDataFim(dataFim);
		
		Sessao sessao03 = repo.getSessao(3);
		sessao03.setDataFim(dataFim);
		
		List<Sessao> sessoes = new ArrayList<Sessao>();
		sessoes.add(sessao01);
		sessoes.add(sessao02);
		sessoes.add(sessao03);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(sessoes).when(sessaoService).getListSessoesEncerradas();		
		sessaoService.finalizarSessoesVirtuais();
		
		Mockito.verify(sessaoService, Mockito.times(0)).finalizarSessaoVirtualListasJulgamento(Mockito.any(Sessao.class));
		Mockito.verify(sessaoService, Mockito.times(0)).alterarSessao(Mockito.any(Sessao.class));
		//Mockito.verify(serviceImpl, Mockito.times(0)).notificaResultadosSessaoEmail(Mockito.any(SessaoServiceImpl.EmailNotificacaoEncerramentoSessao.class));		
	}	
	
	@Test
	public void testFinalizarSessoesVirtuais_SessoesRepercusaoGeral() throws ServiceException, DaoException{
		Sessao sessao01 = repo.getSessao(1);
		sessao01.setTipoJulgamentoVirtual(TipoJulgamentoVirtual.REPERCUSSAO_GERAL.getId());

		Sessao sessao02 = repo.getSessao(2);
		sessao02.setTipoJulgamentoVirtual(TipoJulgamentoVirtual.REPERCUSSAO_GERAL.getId());
		
		Sessao sessao03 = repo.getSessao(3);
		sessao03.setTipoJulgamentoVirtual(TipoJulgamentoVirtual.REPERCUSSAO_GERAL.getId());
		
		List<Sessao> sessoes = new ArrayList<Sessao>();
		sessoes.add(sessao01);
		sessoes.add(sessao02);
		sessoes.add(sessao03);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(sessoes).when(sessaoService).getListSessoesEncerradas();		
		sessaoService.finalizarSessoesVirtuais();
		
		Mockito.verify(sessaoService, Mockito.times(0)).finalizarSessaoVirtualListasJulgamento(Mockito.any(Sessao.class));
		Mockito.verify(sessaoService, Mockito.times(0)).alterarSessao(Mockito.any(Sessao.class));
		//Mockito.verify(serviceImpl, Mockito.times(0)).notificaResultadosSessaoEmail(Mockito.any(Sessao.class));		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFinalizarSessoesVirtuais_SessoesListaJulgamento() throws ServiceException, DaoException, HibernateException, SQLException, ParseException{
		Sessao sessao01 = repo.getSessao(1);
		sessao01.setTipoJulgamentoVirtual(TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.getId());
		sessao01.setListaJulgamentoProcesso(null);
		sessao01.setDataPrevistaFim(DataUtil.string2Date("01/10/2016 00:00:00",true));
		sessao01.setAno((short) 2016);
		sessao01.setNumero(1L);

		Sessao sessao02 = repo.getSessao(2);
		sessao02.setTipoJulgamentoVirtual(TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.getId());
		sessao02.setListaJulgamentoProcesso(null);
		sessao02.setDataPrevistaFim(DataUtil.string2Date("01/10/2016 00:00:00",true));
		sessao02.setAno((short) 2016);
		sessao02.setNumero(2L);
		
		
		JulgamentoProcesso julgamentoProcesso = repo.getJulgamentoProcesso(1L);
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso);
		
		Sessao sessao03 = repo.getSessao(3);
		sessao03.setTipoJulgamentoVirtual(TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.getId());
		sessao03.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		sessao03.setDataPrevistaFim(DataUtil.string2Date("01/10/2016 00:00:00",true));
		sessao03.setAno((short) 2016);
		sessao03.setNumero(3L);
		
		List<Sessao> sessoes = new ArrayList<Sessao>();
		sessoes.add(sessao01);
		sessoes.add(sessao02);
		sessoes.add(sessao03);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(sessoes).when(sessaoService).getListSessoesEncerradas();
		Mockito.doNothing().when(sessaoService).finalizarSessaoVirtualListasJulgamento(Mockito.any(Sessao.class));
		Mockito.doNothing().when(sessaoService).alterarSessao(Mockito.any(Sessao.class));	
		Mockito.doNothing().when(sessaoService).notificaResultadosSessaoEmail(Mockito.any(List.class));
		Mockito.doReturn(Boolean.FALSE).when(dao).isProducao();
		sessaoService.finalizarSessoesVirtuais();
		
		Mockito.verify(sessaoService, Mockito.times(1)).finalizarSessaoVirtualListasJulgamento(Mockito.any(Sessao.class));
		Mockito.verify(sessaoService, Mockito.times(3)).alterarSessao(Mockito.any(Sessao.class));
		//Mockito.verify(serviceImpl, Mockito.times(3)).notificaResultadosSessaoEmail(Mockito.any(Sessao.class));
	}
		
	@Test
	public void testFinalizarSessaoVirtualListasJulgamento_processoDestaque() throws ServiceException, DaoException{
		JulgamentoProcesso julgamentoProcesso01 = repo.getJulgamentoProcesso(1L,TipoSituacaoProcessoSessao.DESTAQUE);
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso01);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		
		sessaoService = Mockito.spy(sessaoService);	
		sessaoService.finalizarSessaoVirtualListasJulgamento(sessao);
		
		Mockito.verify(andamentoProcessoService, Mockito.times(0)).salvar(Mockito.any(AndamentoProcesso.class));
		Mockito.verify(julgamentoProcessoService, Mockito.times(0)).alterar(Mockito.any(JulgamentoProcesso.class));
	}
	
	Sessao setUP_finalizarSessaoVirtualListasJulgamento(List<Texto> ListTextos,TipoSituacaoProcessoSessao situacaoProcesso) {
		Processo principal = repo.getObjetoIncidente(1,"1111");
		principal.setTextos(ListTextos);
		
		JulgamentoProcesso julgamentoProcesso01 = repo.getJulgamentoProcesso(1L,situacaoProcesso);
		julgamentoProcesso01.setObjetoIncidente(principal);
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso01);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		return sessao;
	}	
		
	@Test
	public void testFinalizarSessaoVirtualListasJulgamento_NaoJulgado_TextoEmenta() throws ServiceException, DaoException{
		TipoTexto tipoTexto = TipoTexto.EMENTA;
		TipoSituacaoProcessoSessao situacaoProcesso = TipoSituacaoProcessoSessao.NAO_JULGADO;
		
		List<Texto> listTextos = repo.getListTexto1elemento(tipoTexto);		
		Processo principal = repo.getObjetoIncidente(1,"1111");
		principal.setNumeroProcessual(1111L);
		principal.setTextos(listTextos);
		
		JulgamentoProcesso julgamentoProcesso01 = repo.getJulgamentoProcesso(1L,situacaoProcesso);
		julgamentoProcesso01.setObjetoIncidente(principal);
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso01);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(listTextos).when(sessaoService).getTextoEmentaVoto(julgamentoProcesso01);
		sessaoService.finalizarSessaoVirtualListasJulgamento(sessao);
		
		Mockito.verify(sessaoService, Mockito.times(0)).gerarControleVotoTexto(Mockito.any(Texto.class), Mockito.any(Processo.class), Mockito.any(Sessao.class),Mockito.anyLong());
		Mockito.verify(andamentoProcessoService, Mockito.times(1)).salvar(Mockito.any(AndamentoProcesso.class));
		Mockito.verify(julgamentoProcessoService, Mockito.times(1)).alterar(Mockito.any(JulgamentoProcesso.class));
	}
	
	@Test
	public void testFinalizarSessaoVirtualListasJulgamento_NaoJulgado_TextoVotoVogal() throws ServiceException, DaoException{
		TipoTexto tipoTexto = TipoTexto.VOTO_VOGAL;
		TipoSituacaoProcessoSessao situacaoProcesso = TipoSituacaoProcessoSessao.NAO_JULGADO;
		
		List<Texto> listTextos = repo.getListTexto1elemento(tipoTexto);		
		Processo principal = repo.getObjetoIncidente(1,"1111");
		principal.setNumeroProcessual(1111L);
		principal.setTextos(listTextos);
		
		JulgamentoProcesso julgamentoProcesso01 = repo.getJulgamentoProcesso(1L,situacaoProcesso);
		julgamentoProcesso01.setObjetoIncidente(principal);
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso01);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(listTextos).when(sessaoService).getTextoEmentaVoto(julgamentoProcesso01);
		Mockito.doNothing().when(sessaoService).gerarControleVotoTexto(Mockito.any(Texto.class), Mockito.any(Processo.class), Mockito.any(Sessao.class),Mockito.anyLong());
		
		sessaoService.finalizarSessaoVirtualListasJulgamento(sessao);
		
//		Mockito.verify(serviceImpl, Mockito.times(1)).gerarControleVotoTexto(Mockito.any(Texto.class), Mockito.any(Processo.class), Mockito.any(Sessao.class),Mockito.anyLong());
		Mockito.verify(andamentoProcessoService, Mockito.times(1)).salvar(Mockito.any(AndamentoProcesso.class));
		Mockito.verify(julgamentoProcessoService, Mockito.times(1)).alterar(Mockito.any(JulgamentoProcesso.class));
	}
	
	@Test
	public void testFinalizarSessaoVirtualListasJulgamento_NaoJulgado_TextoVoto() throws ServiceException, DaoException{
		TipoTexto tipoTexto = TipoTexto.VOTO;
		TipoSituacaoProcessoSessao situacaoProcesso = TipoSituacaoProcessoSessao.NAO_JULGADO;
		
		List<Texto> listTextos = repo.getListTexto1elemento(tipoTexto);		
		Processo principal = repo.getObjetoIncidente(1,"1111");
		principal.setNumeroProcessual(1111L);
		principal.setTextos(listTextos);
		
		JulgamentoProcesso julgamentoProcesso01 = repo.getJulgamentoProcesso(1L,situacaoProcesso);
		julgamentoProcesso01.setObjetoIncidente(principal);
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso01);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(listTextos).when(sessaoService).getTextoEmentaVoto(julgamentoProcesso01);
		sessaoService.finalizarSessaoVirtualListasJulgamento(sessao);
		
		Mockito.verify(sessaoService, Mockito.times(0)).gerarControleVotoTexto(Mockito.any(Texto.class), Mockito.any(Processo.class), Mockito.any(Sessao.class),Mockito.anyLong());
		Mockito.verify(andamentoProcessoService, Mockito.times(1)).salvar(Mockito.any(AndamentoProcesso.class));
		Mockito.verify(julgamentoProcessoService, Mockito.times(1)).alterar(Mockito.any(JulgamentoProcesso.class));
	}
	
	@Test
	public void testFinalizarSessaoVirtualListasJulgamento_suspenso_textoEmenta() throws ServiceException, DaoException{
		TipoSituacaoProcessoSessao situacaoProcesso = TipoSituacaoProcessoSessao.SUSPENSO;
		TipoTexto tipoTexto = TipoTexto.EMENTA;
		
		List<Texto> listTextos = repo.getListTexto1elemento(tipoTexto);		
		Processo principal = repo.getObjetoIncidente(1,"1111");
		principal.setTextos(listTextos);
		
		JulgamentoProcesso julgamentoProcesso01 = repo.getJulgamentoProcesso(1L,situacaoProcesso);
		julgamentoProcesso01.setObjetoIncidente(principal);
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso01);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(listTextos).when(sessaoService).getTextoEmentaVoto(julgamentoProcesso01);
		sessaoService.finalizarSessaoVirtualListasJulgamento(sessao);
		
		Mockito.verify(sessaoService, Mockito.times(0)).gerarControleVotoTexto(Mockito.any(Texto.class), Mockito.any(Processo.class), Mockito.any(Sessao.class),Mockito.anyLong());
		Mockito.verify(andamentoProcessoService, Mockito.times(0)).salvar(Mockito.any(AndamentoProcesso.class));
		Mockito.verify(julgamentoProcessoService, Mockito.times(0)).alterar(Mockito.any(JulgamentoProcesso.class));
	}	
	
	@Test
	public void testFinalizarSessaoVirtualListasJulgamento_suspenso_TextoVotoVogal() throws ServiceException, DaoException{
		TipoSituacaoProcessoSessao situacaoProcesso = TipoSituacaoProcessoSessao.SUSPENSO;
		TipoTexto tipoTexto = TipoTexto.VOTO_VOGAL;
		
		List<Texto> listTextos = repo.getListTexto1elemento(tipoTexto);		
		Processo principal = repo.getObjetoIncidente(1,"1111");
		principal.setTextos(listTextos);
		
		JulgamentoProcesso julgamentoProcesso01 = repo.getJulgamentoProcesso(1L,situacaoProcesso);
		julgamentoProcesso01.setObjetoIncidente(principal);
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso01);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(listTextos).when(sessaoService).getTextoEmentaVoto(julgamentoProcesso01);
		Mockito.doNothing().when(sessaoService).gerarControleVotoTexto(Mockito.any(Texto.class), Mockito.any(Processo.class), Mockito.any(Sessao.class),Mockito.anyLong());
		
		sessaoService.finalizarSessaoVirtualListasJulgamento(sessao);
		
		Mockito.verify(sessaoService, Mockito.times(0)).gerarControleVotoTexto(Mockito.any(Texto.class), Mockito.any(Processo.class), Mockito.any(Sessao.class),Mockito.anyLong());
		Mockito.verify(andamentoProcessoService, Mockito.times(0)).salvar(Mockito.any(AndamentoProcesso.class));
		Mockito.verify(julgamentoProcessoService, Mockito.times(0)).alterar(Mockito.any(JulgamentoProcesso.class));
	}
	
	@Test
	public void testFinalizarSessaoVirtualListasJulgamento_suspenso_TextoVoto() throws ServiceException, DaoException{
		TipoSituacaoProcessoSessao situacaoProcesso = TipoSituacaoProcessoSessao.SUSPENSO;
		TipoTexto tipoTexto = TipoTexto.VOTO;
		
		List<Texto> listTextos = repo.getListTexto1elemento(tipoTexto);		
		Processo principal = repo.getObjetoIncidente(1,"1111");
		principal.setTextos(listTextos);
		
		JulgamentoProcesso julgamentoProcesso01 = repo.getJulgamentoProcesso(1L,situacaoProcesso);
		julgamentoProcesso01.setObjetoIncidente(principal);
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = new ArrayList<JulgamentoProcesso>();
		listaJulgamentoProcesso.add(julgamentoProcesso01);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setListaJulgamentoProcesso(listaJulgamentoProcesso);
		
		sessaoService = Mockito.spy(sessaoService);
		Mockito.doReturn(listTextos).when(sessaoService).getTextoEmentaVoto(julgamentoProcesso01);
		sessaoService.finalizarSessaoVirtualListasJulgamento(sessao);
		
		Mockito.verify(sessaoService, Mockito.times(0)).gerarControleVotoTexto(Mockito.any(Texto.class), Mockito.any(Processo.class), Mockito.any(Sessao.class),Mockito.anyLong());
		Mockito.verify(andamentoProcessoService, Mockito.times(0)).salvar(Mockito.any(AndamentoProcesso.class));
		Mockito.verify(julgamentoProcessoService, Mockito.times(0)).alterar(Mockito.any(JulgamentoProcesso.class));
	}	
	
	@Test
	public void testIncrementoSequenciaControleVoto_indexVotoVogal1() throws ServiceException{
		int indexVotoVogal = 1;
		Long proximaSequenciaVoto = 0L;
		Long proximaSequenciaVotoEsperado = 55L;		
		Long proximaSequenciaVotoRetorno = sessaoService.incrementoSequenciaControleVoto(indexVotoVogal, proximaSequenciaVoto);
		assertEquals(proximaSequenciaVotoEsperado,proximaSequenciaVotoRetorno);
	}	
	
	public List<Texto>  getListTextosMinCarmenLucia(){		
		Ministro minCarmenLucia = repo.getMinistro(42L);
		Texto textoCL01 = repo.getTextoComMinstro(4201L,minCarmenLucia);
		Texto textoCL02 = repo.getTextoComMinstroTipoTexto(4202L,minCarmenLucia,TipoTexto.EMENTA);
		Texto textoCL03 = repo.getTextoComMinstroTipoTexto(4203L,minCarmenLucia,TipoTexto.RELATORIO);
		Texto textoCL04 = repo.getTextoComMinstroTipoTexto(4204L,minCarmenLucia,TipoTexto.VOTO_VOGAL);
		
		List<Texto> listTextosRetorno = new ArrayList<Texto>();
		listTextosRetorno.add(textoCL01);
		listTextosRetorno.add(textoCL02);
		listTextosRetorno.add(textoCL03);
		listTextosRetorno.add(textoCL04);
		
		return listTextosRetorno;
	}
	
	public List<Texto>  getListTextosMinLuzFux(){
		Ministro minLuzFux = repo.getMinistro(45L);
		Texto textoLF01 = repo.getTextoComMinstro(4501L,minLuzFux);
		Texto textoLF02 = repo.getTextoComMinstroTipoTexto(4502L,minLuzFux,TipoTexto.EMENTA);
		Texto textoLF03 = repo.getTextoComMinstroTipoTexto(4503L,minLuzFux,TipoTexto.RELATORIO);
		Texto textoLF04 = repo.getTextoComMinstroTipoTexto(4504L,minLuzFux,TipoTexto.VOTO_VOGAL);
		
		List<Texto> listTextosRetorno = new ArrayList<Texto>();
		listTextosRetorno.add(textoLF01);
		listTextosRetorno.add(textoLF02);
		listTextosRetorno.add(textoLF03);
		listTextosRetorno.add(textoLF04);
		
		return listTextosRetorno;
	}
	
	public List<Texto>  getListTextos(){			
		List<Texto> listTextosRetorno = new ArrayList<Texto>();
		listTextosRetorno.addAll(this.getListTextosMinCarmenLucia());
		listTextosRetorno.addAll(this.getListTextosMinLuzFux());		
		return listTextosRetorno;
	}	
	
	public Set<VotoJulgamentoProcesso>  getListVotoJulgamentoProcesso(){
		Ministro minRosaReber   = repo.getMinistro(46L);
		Ministro minLuzFux      = repo.getMinistro(45L);
		Ministro minCarmenLucia = repo.getMinistro(42L);
		
		VotoJulgamentoProcesso vjp01 = repo.getVotoJulgamentoProcesso(1L);
		vjp01.setMinistro(minRosaReber);
		VotoJulgamentoProcesso vjp02 = repo.getVotoJulgamentoProcesso(1L);
		vjp02.setMinistro(minLuzFux);
		VotoJulgamentoProcesso vjp03 = repo.getVotoJulgamentoProcesso(1L);
		vjp03.setMinistro(minCarmenLucia);
		
		Set<VotoJulgamentoProcesso> listaVotoJulgamentoProcesso = new HashSet<VotoJulgamentoProcesso>();
		listaVotoJulgamentoProcesso.add(vjp01);
		listaVotoJulgamentoProcesso.add(vjp02);
		listaVotoJulgamentoProcesso.add(vjp03);	
		
		return listaVotoJulgamentoProcesso;
	}	
	
	@Test
	public void testGetTextoEmentaVoto() throws ServiceException, DaoException{
		List<Texto> listTextosEsperado = new ArrayList<Texto>();
		listTextosEsperado.addAll(this.getListTextosMinLuzFux());
		listTextosEsperado.addAll(this.getListTextosMinCarmenLucia());
		
		List<Texto> listTextosInformados = this.getListTextos();
		Processo objetoIncidente = repo.getObjetoIncidente(1,"1111");
		objetoIncidente.setTextos(listTextosInformados);
		
		Set<VotoJulgamentoProcesso>  listaVotoJulgamentoProcesso = getListVotoJulgamentoProcesso();
		
		JulgamentoProcesso julgamentoProcesso = repo.getJulgamentoProcesso(1L,TipoSituacaoProcessoSessao.JULGADO);
		julgamentoProcesso.setObjetoIncidente(objetoIncidente);
		julgamentoProcesso.setListaVotoJulgamentoProcesso(listaVotoJulgamentoProcesso);
		
		
		List<Texto> listTextosRetorno = sessaoService.getTextoEmentaVoto(julgamentoProcesso);
		assertEquals(TipoTexto.EMENTA,listTextosRetorno.get(0).getTipoTexto());
		assertEquals(TipoTexto.VOTO_VOGAL,listTextosRetorno.get(1).getTipoTexto());
		assertEquals(TipoTexto.EMENTA,listTextosRetorno.get(2).getTipoTexto());
		assertEquals(TipoTexto.VOTO_VOGAL,listTextosRetorno.get(3).getTipoTexto());
	}
		
	@Test
	public void testDefineAndamentoProcessoSessaoVirtualListasJulgamento_NaoIncidenteJulgamento() throws ServiceException, DaoException{
		Processo objetoIncidente = repo.getObjetoIncidente(1,"1111");
		
		JulgamentoProcesso julgamentoProcesso = repo.getJulgamentoProcesso(1L,TipoSituacaoProcessoSessao.SUSPENSO);
		julgamentoProcesso.setObjetoIncidente(objetoIncidente);
		Long proximoNumeroSequencia = 2L;
		Mockito.doReturn(proximoNumeroSequencia).when(andamentoProcessoService).recuperarProximoNumeroSequencia(julgamentoProcesso.getObjetoIncidente());
		
		AndamentoProcesso andamento = sessaoService.defineAndamentoProcessoSessaoVirtualListasJulgamento(julgamentoProcesso);
		assertEquals(objetoIncidente,andamento.getObjetoIncidente());
		assertEquals(proximoNumeroSequencia,andamento.getNumeroSequencia());
	}	
	
	@Test
	public void testDefineAndamentoProcessoSessaoVirtualListasJulgamento_IncidenteJulgamento() throws ServiceException, DaoException{
		Processo principal = repo.getObjetoIncidente(1,"1111");
		
		IncidenteJulgamento objetoIncidente = new IncidenteJulgamento();
		objetoIncidente.setPrincipal(principal);
		
		JulgamentoProcesso julgamentoProcesso = repo.getJulgamentoProcesso(1L,TipoSituacaoProcessoSessao.SUSPENSO);
		julgamentoProcesso.setObjetoIncidente(objetoIncidente);
		Long proximoNumeroSequencia = 2L;
		Mockito.doReturn(proximoNumeroSequencia).when(andamentoProcessoService).recuperarProximoNumeroSequencia(julgamentoProcesso.getObjetoIncidente());
		
		AndamentoProcesso andamento = sessaoService.defineAndamentoProcessoSessaoVirtualListasJulgamento(julgamentoProcesso);
		assertEquals(null,andamento.getObjetoIncidente());
		assertEquals(proximoNumeroSequencia,andamento.getNumeroSequencia());
	}
	
	@Test
	public void testGetDataSessao() throws ServiceException, DaoException, ParseException{
		Date dataFimSessao = DataUtil.string2Date("22/09/2016",false);
		
		Sessao sessao = repo.getSessao(1);
		sessao.setDataPrevistaFim(dataFimSessao);
		sessao.setDataFim(dataFimSessao);
		
		Date dataFimRetorno  = sessaoService.getDataFimSessaoMaisUmDiaSemHora(sessao);
		Date dataFimEsperada = DataUtil.string2Date("23/09/2016",false);
		
		assertEquals(dataFimEsperada,dataFimRetorno);
	}
	
	@Test
	public void sessaoDentroPrazoCPC_Mesa() throws ParseException, ServiceException {
		Long idTipoAndamento = Andamentos.APRESENTACAO_EM_MESA.getId();
		
		Sessao sessao = this.sessaoVerificarCPC("11/05/2016 00:00:00",true);
		
		int ano = 2016;
		int mesAtual = Calendar.MAY;
		int dia = 11;
		Calendar hoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = feriadosMaioCalendar();				
		List<Feriado> feriadosMes5 = feriadosMaioFeriados();
		
//		actionFacesBean = Mockito.spy(actionFacesBean);
//		Mockito.doReturn(feriadosMes5).when(feriadoService).recuperarProximosFeriados(hoje, 3);
		
		boolean respeitaPrazoCPC2016Experado = true;
		boolean respeitaPrazoCPC2016Retorno   = sessaoService.sessaoDentroDoPrazo(idTipoAndamento,sessao,hoje,feriados,false, new StringBuffer(), TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO);
		assertEquals(respeitaPrazoCPC2016Experado,respeitaPrazoCPC2016Retorno);
	}
	
	private Sessao sessaoVerificarCPC(String dataString,boolean usarDataPrevistaInicio) throws ParseException{
		Date dataPrevistaInicio = DataUtil.string2Date(dataString);		
		Sessao sessao = getSessao(1L);
		if(usarDataPrevistaInicio){
			sessao.setDataPrevistaInicio(dataPrevistaInicio);
		}else{
			sessao.setDataInicio(dataPrevistaInicio);
		}		
		return sessao;
	}
	
	private Sessao getSessao(long id){
		Colegiado colegiado = getColegiado(Colegiado.TRIBUNAL_PLENO);
		Sessao sessaoRetorno = new Sessao();
		sessaoRetorno.setId(id);
		Date dataPrevistaInicio = new Date();
		sessaoRetorno.setDataInicio(dataPrevistaInicio);
		sessaoRetorno.setDataPrevistaInicio(dataPrevistaInicio);
		sessaoRetorno.setColegiado(colegiado);
		sessaoRetorno.setTipoAmbiente(TipoAmbienteConstante.PRESENCIAL.getSigla());
		sessaoRetorno.setTipoSessao(TipoSessaoConstante.ORDINARIA.getSigla());
		return sessaoRetorno;
	}
	
	private Colegiado getColegiado(String id){
		Colegiado colegiado = new Colegiado();
		colegiado.setId(id);
		return colegiado;
	}
	
	public List<Calendar> feriadosMaioCalendar(){
		int ano = 2016;
		int mesAtual = Calendar.MAY;
		int dia = 1;
		Calendar feriado20160501 = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = new ArrayList<Calendar>();
		feriados.add(feriado20160501);
		
		return feriados;
	}
	
	public List<Feriado> feriadosMaioFeriados(){
		List<Feriado> feriados2016 = recuperaFeriados();
		List<Feriado> feriadosMes5 = new ArrayList<Feriado>();
		feriadosMes5.add(feriados2016.get(5));
		
		return feriadosMes5;
	}
	
	private List<Feriado> recuperaFeriados(){
		List<Feriado> feriados = new ArrayList<Feriado>();
		
		//0
		Feriado feriado0101 = new Feriado();
		feriado0101.setId("012016");
		feriado0101.setDia("01");
		feriados.add(feriado0101);		
		
		//1
		Feriado feriado0209 = new Feriado();//Carnaval
		feriado0209.setId("022016");
		feriado0209.setDia("09");
		feriados.add(feriado0209);
		
		//2
		Feriado feriado0325 = new Feriado();//Sexta-feira da Paixão
		feriado0325.setId("032016");
		feriado0325.setDia("25");
		feriados.add(feriado0325);
		
		//3
		Feriado feriado0327 = new Feriado();//Páscoa
		feriado0327.setId("032016");
		feriado0327.setDia("27");
		feriados.add(feriado0327);
		
		//4
		Feriado feriado0421 = new Feriado(); //Tiradentes
		feriado0421.setId("042016");
		feriado0421.setDia("21");
		feriados.add(feriado0421);
		
		//5
		Feriado feriado0501 = new Feriado(); //Dia do Trabalho
		feriado0501.setId("052016");
		feriado0501.setDia("01");
		feriados.add(feriado0501);
		
		//6
		Feriado feriado0526 = new Feriado(); //Corpus Christi
		feriado0526.setId("052016");
		feriado0526.setDia("26");
		feriados.add(feriado0526);
		
		//7
		Feriado feriado1102 = new Feriado(); //Finados
		feriado1102.setId("112016");
		feriado1102.setDia("02");
		feriados.add(feriado1102);			
		
		//8
		Feriado feriado1115 = new Feriado(); //Proclamação da República
		feriado1115.setId("112016");
		feriado1115.setDia("15");
		feriados.add(feriado1115);		
		
		//9
		Feriado feriado1225 = new Feriado(); //Natal
		feriado1225.setId("122016");
		feriado1225.setDia("25");
		feriados.add(feriado1225);		
		
		return feriados;
	}
	
	@Test
	public void testDataFim_feriado_08_12() {
		String dataLiberacao = "03/12/2021";
		String dataFimEsperada  = "13/12/2021";
		String[] feriados = new String[]{"08/12/2021"};
		
		String dataFimRetornada = retornarDataFimSessao(dataLiberacao, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	@Test
	public void testDataFim_14_06_2019() {
		String dataLiberacao = "14/06/2019";
		String dataFimEsperada  = "24/06/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataFimRetornada = retornarDataFimSessao(dataLiberacao, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	@Test
	public void testDataFim_21_06_2019() {
		String dataLiberacao = "21/06/2019";
		String dataFimEsperada  = "28/06/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataFimRetornada = retornarDataFimSessao(dataLiberacao, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	@Test
	public void testDataFim_28_06_2019() {
		String dataLiberacao = "28/06/2019";
		String dataFimEsperada  = "06/08/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataFimRetornada = retornarDataFimSessao(dataLiberacao, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	@Test
	public void testDataFim_05_07_2019() {
		String dataLiberacao = "05/07/2019";
		String dataFimEsperada  = "08/08/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataFimRetornada = retornarDataFimSessao(dataLiberacao, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	@Test
	public void testDataFim_09_08_2019() {
		String dataLiberacao = "09/08/2019";
		String dataFimEsperada  = "16/08/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataFimRetornada = retornarDataFimSessao(dataLiberacao, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	@Test
	public void testDataFim_19_12_2019() {
		String dataLiberacao = "19/12/2019";
		String dataFimEsperada  = "07/02/2020";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019", "20/12/2019"};
		
		String dataFimRetornada = retornarDataFimSessao(dataLiberacao, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	@Test
	public void testDataFim_20_12_2019() {
		String dataLiberacao = "20/12/2019";
		String dataFimEsperada  = "10/02/2020";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataFimRetornada = retornarDataFimSessao(dataLiberacao, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	private String retornarDataFimSessao(String dataPrevistaInicio, String[] feriados) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Calendar dataPrevistaInicioCalendar = new GregorianCalendar();
			dataPrevistaInicioCalendar.setTime(formatter.parse(dataPrevistaInicio));
			
			// Lista de Feriados
			List<Calendar> listaFeriados = new ArrayList<Calendar>();
			for (String feriadoString : feriados) {
				Calendar feriado = new GregorianCalendar();
				feriado.setTime(formatter.parse(feriadoString));
				listaFeriados.add(feriado);
			}
			
			Date dataFim 	= DataUtil.datSessaoFim(dataPrevistaInicioCalendar.getTime(), listaFeriados);
			
			return formatter.format(dataFim);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		 return null;
	}
	
	@Test
	public void sessaoDentroPrazoCPCP_PAUTA_FALSE_dataPrevista() throws ParseException, ServiceException {		
		Long idTipoAndamento = Andamentos.INCLUSAO_EM_PAUTA.getId();
		
		Sessao sessao = this.sessaoVerificarCPC("12/05/2016 00:00:00",true);
		sessao.setDataInicio(null);
		
		int ano = 2016;
		int mesAtual = Calendar.MAY;
		int dia = 11;		
		Calendar hoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = this.feriadosMaioCalendar();				
		List<Feriado> feriadosMes5 = this.feriadosMaioFeriados();
		
//		actionFacesBean = Mockito.spy(actionFacesBean);
//		Mockito.doReturn(feriadosMes5).when(feriadoService).recuperarProximosFeriados(hoje, 3);
		
		when(conteudoPublicacaoService.isPautaFechada(sessao.getColegiado(), Calendar.getInstance())).thenReturn(true);
		
		boolean respeitaPrazoCPC2016Experadao = false;
		boolean respeitaPrazoCPC2016Retorno   = sessaoService.sessaoDentroDoPrazo(idTipoAndamento,sessao,hoje,feriados,false, new StringBuffer(), TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO);
		assertEquals(respeitaPrazoCPC2016Experadao,respeitaPrazoCPC2016Retorno);
	}
	
	@Test
	public void sessaoDentroPrazoCPC_PAUTA_FALSE_dataInicial() throws ParseException, ServiceException {		
		Long idTipoAndamento = Andamentos.INCLUSAO_EM_PAUTA.getId();
		
		Sessao sessao = this.sessaoVerificarCPC("12/05/2016 00:00:00",false);
		sessao.setDataPrevistaInicio(null);
		
		int ano = 2016;
		int mesAtual = Calendar.MAY;
		int dia = 11;		
		Calendar hoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = this.feriadosMaioCalendar();				
		List<Feriado> feriadosMes5 = this.feriadosMaioFeriados();
		
		boolean respeitaPrazoCPC2016Experadao = false;
		boolean respeitaPrazoCPC2016Retorno   = sessaoService.sessaoDentroDoPrazo(idTipoAndamento,sessao,hoje,feriados,false, new StringBuffer(), TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO);
		assertEquals(respeitaPrazoCPC2016Experadao,respeitaPrazoCPC2016Retorno);
	}	
	
	@Test
	public void sessaoDentroPrazoCPCP_PAUTA_TRUE_diaExato() throws ParseException, ServiceException {
		Long idTipoAndamento = Andamentos.INCLUSAO_EM_PAUTA.getId();
		
		Sessao sessao = this.sessaoVerificarCPC("23/05/2016 00:00:00",true);
		
		int ano = 2016;
		int mesAtual = Calendar.MAY;
		int dia = 11;		
		Calendar hoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = this.feriadosMaioCalendar();				
		List<Feriado> feriadosMes5 = this.feriadosMaioFeriados();
		
		boolean respeitaPrazoCPC2016Experadao = true;
		boolean respeitaPrazoCPC2016Retorno   = sessaoService.sessaoDentroDoPrazo(idTipoAndamento,sessao,hoje,feriados,false, new StringBuffer(), TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO);
		assertEquals(respeitaPrazoCPC2016Experadao,respeitaPrazoCPC2016Retorno);
	}
	
	@Test
	public void sessaoDentroPrazoCPC_PAUTA_TRUE_diaFuturo() throws ParseException, ServiceException {
		Long idTipoAndamento = Andamentos.INCLUSAO_EM_PAUTA.getId();
		
		Sessao sessao = this.sessaoVerificarCPC("31/05/2016 00:00:00",true);
		
		int ano = 2016;
		int mesAtual = Calendar.MAY;
		int dia = 11;		
		Calendar hoje = new GregorianCalendar(ano, mesAtual, dia);
		List<Calendar> feriados = this.feriadosMaioCalendar();				
		List<Feriado> feriadosMes5 = this.feriadosMaioFeriados();
		
		boolean respeitaPrazoCPC2016Experadao = true;
		boolean respeitaPrazoCPC2016Retorno   = sessaoService.sessaoDentroDoPrazo(idTipoAndamento,sessao,hoje,feriados,false, new StringBuffer(), TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO);
		assertEquals(respeitaPrazoCPC2016Experadao,respeitaPrazoCPC2016Retorno);
	}
	
	@Test
	public void liberarProcessoNoDia_27_06_2016_RetornarSessaoNoDia_05_08_2016() {
		String dataLiberacao = "27/06/2016";
		String dataSessaoEsperada  = "05/08/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_14_07_2016_RetornarSessaoNoDia_12_08_2016() {
		String dataLiberacao = "14/07/2016";
		String dataSessaoEsperada  = "12/08/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_01_08_2016_RetornarSessaoNoDia_12_08_2016() {
		String dataLiberacao = "01/08/2016";
		String dataSessaoEsperada  = "12/08/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	private String[] getFeriados() {
		return new String[]{"1/01/2016","2/01/2016","3/01/2016","4/01/2016","5/01/2016","6/01/2016","10/02/2016","8/02/2016","9/02/2016","23/03/2016","24/03/2016","25/03/2016","26/03/2016","21/04/2016","1/05/2016","26/05/2016","11/08/2016","7/09/2016","12/10/2016","28/10/2016","01/11/2016","15/11/2016","2/11/2016","20/12/2016","21/12/2016","22/12/2016","23/12/2016","24/12/2016","25/12/2016","26/12/2016","27/12/2016","28/12/2016","29/12/2016","30/12/2016","31/12/2016","8/12/2016", "07/07/2018", "12/02/2024", "13/02/2024", "14/02/2024", "27/03/2024", "28/03/2024", "29/03/2024"};
	}

	@Test
	public void liberarProcessoNoDia_02_08_2016_RetornarSessaoNoDia_12_08_2016() {
		String dataLiberacao = "02/08/2016";
		String dataSessaoEsperada  = "12/08/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_08_08_2016_RetornarSessaoNoDia_19_08_2016() {
		String dataLiberacao = "08/08/2016";
		String dataSessaoEsperada  = "19/08/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_15_08_2016_RetornarSessaoNoDia_26_08_2016() {
		String dataLiberacao = "15/08/2016";
		String dataSessaoEsperada  = "26/08/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_19_08_2016_RetornarSessaoNoDia_26_08_2016() {
		String dataLiberacao = "19/08/2016";
		String dataSessaoEsperada  = "02/09/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_06_09_2016_RetornarSessaoNoDia_16_09_2016() {
		String dataLiberacao = "06/09/2016";
		String dataSessaoEsperada  = "16/09/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_05_09_2016_RetornarSessaoNoDia_16_09_2016() {
		String dataLiberacao = "05/09/2016";
		String dataSessaoEsperada  = "16/09/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_24_10_2016_RetornarSessaoNoDia_11_11_2016() {
		String dataLiberacao = "24/10/2016";
		String dataSessaoEsperada  = "11/11/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_27_10_2016_RetornarSessaoNoDia_11_11_2016() {
		String dataLiberacao = "27/10/2016";
		String dataSessaoEsperada  = "11/11/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_31_10_2016_RetornarSessaoNoDia_11_11_2016() {
		String dataLiberacao = "31/10/2016";
		String dataSessaoEsperada  = "11/11/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_05_12_2016_RetornarSessaoNoDia_16_12_2016() {
		String dataLiberacao = "05/12/2016";
		String dataSessaoEsperada  = "16/12/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_12_12_2016_RetornarSessaoNoDia_03_02_2017() {
		String dataLiberacao = "12/12/2016";
		String dataSessaoEsperada  = "03/02/2017";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}

	@Test
	public void liberarProcessoNoDia_21_08_2018_RetornarSessaoNoDia_31_08_2018() {
		String dataLiberacao = "21/08/2018";
		String dataSessaoEsperada  = "31/08/2018";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_28_08_2018_RetornarSessaoNoDia_07_09_2018() {
		String dataLiberacao = "28/08/2018";
		String dataSessaoEsperada  = "07/09/2018";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_04_04_2019_RetornarSessaoNoDia_19_04_2019() {
		String dataLiberacao = "04/04/2019";
		String dataSessaoEsperada  = "19/04/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_05_04_2016_RetornarSessaoNoDia_15_04_2016() {
		String dataLiberacao = "05/04/2016";
		String dataSessaoEsperada  = "15/04/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_07_04_2016_RetornarSessaoNoDia_22_04_2016() {
		String dataLiberacao = "07/04/2016";
		String dataSessaoEsperada  = "22/04/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_08_04_2016_RetornarSessaoNoDia_22_04_2016() {
		String dataLiberacao = "08/04/2016";
		String dataSessaoEsperada  = "22/04/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_13_04_2016_RetornarSessaoNoDia_29_04_2016() {
		String dataLiberacao = "13/04/2016";
		String dataSessaoEsperada  = "29/04/2016";
		String[] feriados = getFeriados();
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_02_02_2018_RetornarSessaoNoDia_16_02_2018() {
		String dataLiberacao = "02/02/2018";
		String dataSessaoEsperada  = "16/02/2018";
		String[] feriados = new String[]{"12/02/2018", "13/02/2018", "28/02/2018"};
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_18_06_2019_RetornarSessaoNoDia_28_06_2019() {
		String dataLiberacao = "18/06/2019";
		String dataSessaoEsperada  = "28/06/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_26_06_2019_RetornarSessaoNoDia_09_08_2019() {
		String dataLiberacao = "26/06/2019";
		String dataSessaoEsperada  = "09/08/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_21_06_2019_RetornarSessaoNoDia_02_08_2019() {
		String dataLiberacao = "21/06/2019";
		String dataSessaoEsperada  = "02/08/2019";
		String[] feriados = new String[]{"20/06/2019", "11/08/2019"};
		
		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	private String retornarDataInicioSessao(String dataLiberacao, String[] feriados) {
		return retornarDataInicioSessao(dataLiberacao, feriados, false);
	}
	
	private String retornarDataInicioSessao(String dataLiberacao, String[] feriados, Boolean ignorarCpc) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Calendar dataLiberacaoCalendar = new GregorianCalendar();
			dataLiberacaoCalendar.setTime(formatter.parse(dataLiberacao));
			return retornarDataInicioSessao(dataLiberacaoCalendar, feriados, ignorarCpc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private String retornarDataInicioSessao(Calendar dataLiberacaoCalendar, String[] feriados, Boolean ignorarCpc) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			// Lista de Feriados
			List<Calendar> listaFeriados = new ArrayList<Calendar>();
			for (String feriadoString : feriados) {
				Calendar feriado = new GregorianCalendar();
				feriado.setTime(formatter.parse(feriadoString));
				listaFeriados.add(feriado);
			}

			Colegiado colegiado = new Colegiado();
			colegiado.setId("TP");
			Sessao sessao = sessaoService.montarSessaoVirtual(dataLiberacaoCalendar, listaFeriados, colegiado, TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO, ignorarCpc);
			
			return formatter.format(sessao.getDataPrevistaInicio());
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 return null;
	}
	
	@Test
	public void testMontarSessaoVirtual() throws ParseException, ServiceException{
		Colegiado colegiado = new Colegiado();
		colegiado.setAtivo(Boolean.TRUE);
		colegiado.setDescricao("Primeira Turma");
		colegiado.setId("1T");

		Calendar dataLiberacaoLista = (Calendar) Calendar.getInstance().clone();

		Sessao sessaoRetorno = sessaoService.montarSessaoVirtual(dataLiberacaoLista,null,colegiado, TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO, false);

		assertEquals(null, sessaoRetorno.getDataInicio());
		assertEquals(null, sessaoRetorno.getDataFim());
		assertEquals(TipoAmbienteConstante.VIRTUAL.getSigla(), sessaoRetorno.getTipoAmbiente());
		assertEquals(Sessao.TipoSessaoConstante.ORDINARIA.getSigla(), sessaoRetorno.getTipoSessao());		
	}
	
	@Test
	public void testDefinicaoPrazoSessao() throws ServiceException {
		Calendar dataLiberacaoTeste = Calendar.getInstance(); // 1/07/2016
		dataLiberacaoTeste.set(Calendar.YEAR, 2016);
		dataLiberacaoTeste.set(Calendar.MONTH, 6);
		dataLiberacaoTeste.set(Calendar.DAY_OF_MONTH, 31);

		Calendar dataLiberacaoLista = dataLiberacaoTeste;
		Calendar prazoMinimo = sessaoService.recuperaProximoDiaRespeitandoPrazo(dataLiberacaoLista, null, false, false, new StringBuffer());

		Date dataPrevistaInicio = DataUtil.dateSessaoInicio(prazoMinimo.getTime());
		// Short ano = actionFacesBean.getAnoSessaoVirtual(dataPrevistaInicio);
		Date dataPrevistaFim = DataUtil.datSessaoFim(dataPrevistaInicio, null);

		Calendar dataPrevistaInicioCorreta = (Calendar) Calendar.getInstance().clone();
		dataPrevistaInicioCorreta.set(Calendar.YEAR, 2016);
		dataPrevistaInicioCorreta.set(Calendar.MONTH, 7);
		dataPrevistaInicioCorreta.set(Calendar.DAY_OF_MONTH, 12);
		dataPrevistaInicioCorreta.set(Calendar.HOUR_OF_DAY, 00); // Dia com 24 horas, se usar HOUR será dia AM e PM
		dataPrevistaInicioCorreta.set(Calendar.MINUTE, 00);
		dataPrevistaInicioCorreta.set(Calendar.SECOND, 00);
		dataPrevistaInicioCorreta.set(Calendar.MILLISECOND, 0);

		Calendar dataPrevistaFimCorreta = (Calendar) Calendar.getInstance().clone(); // 18/08/2016
		dataPrevistaFimCorreta.set(Calendar.YEAR, 2016);
		dataPrevistaFimCorreta.set(Calendar.MONTH, 7);
		dataPrevistaFimCorreta.set(Calendar.DAY_OF_MONTH, 19);
		dataPrevistaFimCorreta.set(Calendar.HOUR_OF_DAY, 23); // Dia com 24 horas, se usar HOUR será dia AM e PM
		dataPrevistaFimCorreta.set(Calendar.MINUTE, 59);
		dataPrevistaFimCorreta.set(Calendar.SECOND, 59);
		dataPrevistaFimCorreta.set(Calendar.MILLISECOND, 999);

		assertTrue(dataPrevistaInicio.compareTo(dataPrevistaInicioCorreta.getTime()) == 0);
		assertTrue(dataPrevistaFim.equals(dataPrevistaFimCorreta.getTime()));
	}
	
	@Test
	public void dataSessaoRepercussaoGeralTest() throws ParseException, ServiceException {
		String[] feriados = new String[]{"1/01/2023", "2/01/2023", "3/01/2023", "4/01/2023", "5/01/2023", "6/01/2023", "20/02/2023", "21/02/2023", "05/04/2023", "06/04/2023", "21/04/2023", "7/04/2023", "1/05/2023", "8/06/2023", "11/08/2023", "7/09/2023", "12/10/2023", "1/11/2023", "2/11/2023", "15/11/2023", "20/12/2023", "21/12/2023", "22/12/2023", "23/12/2023", "24/12/2023", "25/12/2023", "26/12/2023", "27/12/2023", "28/12/2023", "29/12/2023", "30/12/2023", "31/12/2023", "8/12/2023"};

//		testarDataInicio("16/02/2023", "17/02/2023", feriados);
		testarDataInicio("16/02/2023", "03/03/2023", feriados);
//		testarDataInicio("19/04/2023", "21/04/2023", feriados);
		testarDataInicio("19/04/2023", "05/05/2023", feriados);
//		testarDataInicio("03/07/2023", "04/08/2023", feriados);
//		testarDataInicio("24/07/2023", "04/08/2023", feriados);
		testarDataInicio("03/07/2023", "11/08/2023", feriados);
		testarDataInicio("24/07/2023", "11/08/2023", feriados);
//		testarDataInicio("12/10/2023", "13/10/2023", feriados);
		testarDataInicio("12/10/2023", "27/10/2023", feriados);
//		testarDataInicio("15/12/2023", "02/02/2024", feriados);
//		testarDataInicio("19/12/2023", "02/02/2024", feriados);
//		testarDataInicio("31/01/2024", "02/02/2024", feriados);
		testarDataInicio("15/12/2023", "09/02/2024", feriados);
		testarDataInicio("19/12/2023", "09/02/2024", feriados);
		testarDataInicio("31/01/2024", "09/02/2024", feriados);
//		testarDataInicio("03/02/2023", "10/02/2023", feriados);
		testarDataInicio("03/02/2023", "17/02/2023", feriados);
//		testarDataInicio("21/04/2023", "28/04/2023", feriados);
		testarDataInicio("21/04/2023", "05/05/2023", feriados);

		testarDataFim("17/02/2023", "28/02/2023", feriados);
		testarDataFim("31/03/2023", "12/04/2023", feriados);
		testarDataFim("07/04/2023", "17/04/2023", feriados);
		testarDataFim("21/04/2023", "02/05/2023", feriados);
		testarDataFim("28/04/2023", "08/05/2023", feriados);
		testarDataFim("02/06/2023", "12/06/2023", feriados);
		testarDataFim("30/06/2023", "07/08/2023", feriados);
		testarDataFim("04/08/2023", "14/08/2023", feriados);
		testarDataFim("11/08/2023", "21/08/2023", feriados);
		testarDataFim("01/09/2023", "11/09/2023", feriados);
		testarDataFim("06/10/2023", "16/10/2023", feriados);
		testarDataFim("27/10/2023", "07/11/2023", feriados);
		testarDataFim("10/11/2023", "20/11/2023", feriados);
		testarDataFim("01/12/2023", "11/12/2023", feriados);
		testarDataFim("08/12/2023", "18/12/2023", feriados);
		testarDataFim("15/12/2023", "05/02/2024", feriados);
	}

	private void testarDataInicio(String dataLiberacao, String dataInicioEsperada, String[] feriados) {
		String dataInicioRetornada = retornarDataInicioSessao(dataLiberacao, feriados);
		assertEquals(dataInicioEsperada, dataInicioRetornada);
	}
	
	private void testarDataFim(String dataPrevistaInicio, String dataFimEsperada, String[] feriados) {
		String dataFimRetornada = retornarDataFimSessao(dataPrevistaInicio, feriados);
		assertEquals(dataFimEsperada, dataFimRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_08_02_2024_RetornarSessaoNoDia_09_02_2024() {
		String dataLiberacao = "08/02/2024";
		String dataSessaoEsperada  = "09/02/2024";
		String[] feriados = getFeriados();

		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados, true);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}
	
	@Test
	public void liberarProcessoNoDia_09_02_2024_RetornarSessaoNoDia_16_02_2024() {
		String dataLiberacao = "09/02/2024";
		String dataSessaoEsperada  = "16/02/2024";
		String[] feriados = getFeriados();

		String dataSessaoRetornada = retornarDataInicioSessao(dataLiberacao, feriados, true);
		assertEquals(dataSessaoEsperada, dataSessaoRetornada);
	}

}