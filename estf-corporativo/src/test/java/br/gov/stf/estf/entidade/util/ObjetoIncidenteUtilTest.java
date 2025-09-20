package br.gov.stf.estf.entidade.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.entidade.processostf.TipoRecursoProcesso;

public class ObjetoIncidenteUtilTest {

	@Mock
	private ObjetoIncidente<Processo> objetoIncidente;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetIdentificacaoStringLong() {
		testGetIdentificacaoStringNullLongNull();

		testGetIdentificacaoStringNullLong();

		testGetIdentificacaoStringLongNull();

		testGetIdentificacaoStringEmptyLong();

		testGetIdentificacaoStringLongInvalidos();

		testGetIdentificacaoStringLongValidos();
	}

	private void testGetIdentificacaoStringNullLongNull() {
		String siglaCadeiaCompleta = null;
		Long numeroProcesso = null;

		try {
			ObjetoIncidenteUtil.getIdentificacao(siglaCadeiaCompleta, numeroProcesso);
		} catch (IllegalArgumentException exception) {
			assertNotNull(exception.getLocalizedMessage());
			return;
		}

		fail("Era esperada uma IllegalArgumentException.");
	}

	private void testGetIdentificacaoStringNullLong() {
		String siglaCadeiaCompleta = null;
		Long numeroProcesso = 1L;

		String identificacao = ObjetoIncidenteUtil.getIdentificacao(siglaCadeiaCompleta, numeroProcesso);

		assertNotNull(identificacao);
		assertEquals("1", identificacao);
	}

	private void testGetIdentificacaoStringLongNull() {
		String siglaCadeiaCompleta = "AI-AgR-ED-ED-ED-EDv-AgR-AgR-teste do teste";
		Long numeroProcesso = null;

		try {
			ObjetoIncidenteUtil.getIdentificacao(siglaCadeiaCompleta, numeroProcesso);
		} catch (IllegalArgumentException exception) {
			assertNotNull(exception.getLocalizedMessage());
			return;
		}

		fail("Era esperada uma IllegalArgumentException.");
	}

	private void testGetIdentificacaoStringEmptyLong() {
		String siglaCadeiaCompleta = "";
		Long numeroProcesso = 1L;

		String identificacao = ObjetoIncidenteUtil.getIdentificacao(siglaCadeiaCompleta, numeroProcesso);

		assertNotNull(identificacao);
		assertEquals("1", identificacao);
	}

	private void testGetIdentificacaoStringLongInvalidos() {
		String siglaCadeiaCompleta = "XXX";
		Long numeroProcesso = -1L;

		String identificacao = ObjetoIncidenteUtil.getIdentificacao(siglaCadeiaCompleta, numeroProcesso);

		assertNotNull(identificacao);
		assertEquals("XXX -1", identificacao);
	}

	private void testGetIdentificacaoStringLongValidos() {
		String siglaCadeiaCompleta = "AI-AgR-ED-ED-ED-EDv-AgR-AgR-teste do teste";
		Long numeroProcesso = 1L;

		String identificacao = ObjetoIncidenteUtil.getIdentificacao(siglaCadeiaCompleta, numeroProcesso);

		assertNotNull(identificacao);
		assertEquals("AI 1 AgR-ED-ED-ED-EDv-AgR-AgR-teste do teste", identificacao);

		siglaCadeiaCompleta = "AI";
		numeroProcesso = 1L;

		identificacao = ObjetoIncidenteUtil.getIdentificacao(siglaCadeiaCompleta, numeroProcesso);

		assertNotNull(identificacao);
		assertEquals("AI 1", identificacao);
	}

	/*@Test
	public void testGetIdentificacaoStringIntegerLong() {
		
	}*/

	@Test
	public void testGetIdentificacaoCompleta() {
		testGetIdentificacaoCompletaNull();

		testGetIdentificacaoCompletaEmpty();

		testGetIdentificacaoCompletaInvalida();

		testGetIdentificacaoCompletaValida();
	}

	private void testGetIdentificacaoCompletaNull() {
		String descricaoCadeia = null;
		Long numeroProcesso = null;

		String identificacaoCompleta = ObjetoIncidenteUtil.getIdentificacaoCompleta(descricaoCadeia, numeroProcesso);
		assertNotNull(identificacaoCompleta);
		assertEquals("null null", identificacaoCompleta);
	}

	private void testGetIdentificacaoCompletaEmpty() {
		String descricaoCadeia = "";
		Long numeroProcesso = 0L;

		String identificacaoCompleta = ObjetoIncidenteUtil.getIdentificacaoCompleta(descricaoCadeia, numeroProcesso);
		assertNotNull(identificacaoCompleta);
		assertEquals(" 0", identificacaoCompleta);
	}

	private void testGetIdentificacaoCompletaInvalida() {
		String descricaoCadeia = "XXX";
		Long numeroProcesso = 1L;

		String identificacaoCompleta = ObjetoIncidenteUtil.getIdentificacaoCompleta(descricaoCadeia, numeroProcesso);
		assertNotNull(identificacaoCompleta);
		assertEquals("XXX 1", identificacaoCompleta);
	}

	private void testGetIdentificacaoCompletaValida() {
		String descricaoCadeia = "ADI";
		Long numeroProcesso = 100L;

		String identificacaoCompleta = ObjetoIncidenteUtil.getIdentificacaoCompleta(descricaoCadeia, numeroProcesso);
		assertNotNull(identificacaoCompleta);
		assertEquals("ADI 100", identificacaoCompleta);
	}

	@Test
	public void testGetDadosProcessuais() {
		testGetDadosProcessuaisObjetoComDados();

		testGetDadosProcessuaisObjetoSemDados();
	}

	private void testGetDadosProcessuaisObjetoComDados() {
		Classe classe = mock(Classe.class);

		Processo processo = mock(Processo.class);
		when(processo.getClasseProcessual()).thenReturn(classe);
		when(processo.getNumeroProcessual()).thenReturn(1L);

		when(objetoIncidente.getTipoObjetoIncidente()).thenReturn(TipoObjetoIncidente.PROCESSO);
		when(objetoIncidente.getPrincipal()).thenReturn(processo);

		DadosProcessuais dadosProcessuais = ObjetoIncidenteUtil.getDadosProcessuais(objetoIncidente);

		assertNotNull(dadosProcessuais);
		assertNull(dadosProcessuais.getTipoRecurso());
		assertEquals(TipoIncidenteJulgamento.MERITO, dadosProcessuais.getTipoJulgamento());
		assertNotNull(dadosProcessuais.getNumeroProcesso());
		assertEquals(1, (long) dadosProcessuais.getNumeroProcesso());
	}

	private void testGetDadosProcessuaisObjetoSemDados() {
		when(objetoIncidente.getTipoObjetoIncidente()).thenReturn(TipoObjetoIncidente.SUMULA);

		DadosProcessuais dadosProcessuais = ObjetoIncidenteUtil.getDadosProcessuais(objetoIncidente);

		assertNull(dadosProcessuais);
	}

	@Test
	public void testGetTipoJulgamento() {
		testGetTipoJulgamentoNull();

		testGetTipoJulgamentoIncidenteJulgamento();

		testGetTipoJulgamentoOutros();
	}

	private void testGetTipoJulgamentoNull() {
		TipoIncidenteJulgamento tipoJulgamento = ObjetoIncidenteUtil.getTipoJulgamento(null);

		assertNotNull(tipoJulgamento);
		assertEquals(TipoIncidenteJulgamento.MERITO, tipoJulgamento);
	}

	private void testGetTipoJulgamentoIncidenteJulgamento() {
		TipoIncidenteJulgamento tipoIncidenteJulgamento = mock(TipoIncidenteJulgamento.class);
		when(tipoIncidenteJulgamento.getId()).thenReturn(1L);

		IncidenteJulgamento incidenteJulgamento = mock(IncidenteJulgamento.class);
		when(incidenteJulgamento.getTipoJulgamento()).thenReturn(tipoIncidenteJulgamento);

		TipoIncidenteJulgamento tipoIncidenteJulgamentoRetornado = ObjetoIncidenteUtil.getTipoJulgamento(incidenteJulgamento);

		assertNotNull(tipoIncidenteJulgamentoRetornado);
		assertEquals(tipoIncidenteJulgamento, tipoIncidenteJulgamentoRetornado);
	}

	private void testGetTipoJulgamentoOutros() {
		TipoIncidenteJulgamento tipoJulgamento = ObjetoIncidenteUtil.getTipoJulgamento(objetoIncidente);

		assertNotNull(tipoJulgamento);
		assertEquals(TipoIncidenteJulgamento.MERITO, tipoJulgamento);
	}

	@Test
	public void testGetTipoRecurso() {
		testGetTipoRecursoNull();

		testGetTipoRecursoRecursoProcesso();

		testGetTipoRecursoPeticao();

		testGetTipoRecursoIncidenteJulgamento();

		testGetTipoRecursoOutros();
	}

	private void testGetTipoRecursoNull() {
		TipoRecurso tipoRecurso = ObjetoIncidenteUtil.getTipoRecurso(null);

		assertNull(tipoRecurso);
	}

	private void testGetTipoRecursoRecursoProcesso() {
		TipoRecursoProcesso tipoRecursoProcesso = mock(TipoRecursoProcesso.class);
		when(tipoRecursoProcesso.getId()).thenReturn(1L);

		RecursoProcesso recursoProcesso = mock(RecursoProcesso.class);
		when(recursoProcesso.getTipoRecursoProcesso()).thenReturn(tipoRecursoProcesso);

		TipoRecurso tipoRecurso = ObjetoIncidenteUtil.getTipoRecurso(recursoProcesso);

		assertNotNull(tipoRecurso);
		assertEquals(tipoRecursoProcesso, tipoRecurso);
	}

	private void testGetTipoRecursoPeticao() {
		TipoRecurso tipoRecurso = mock(TipoRecurso.class);
		when(tipoRecurso.getId()).thenReturn(1L);

		Peticao peticao = mock(Peticao.class);
		when(peticao.getTipoRecurso()).thenReturn(tipoRecurso);

		TipoRecurso tipoRecursoRetornado = ObjetoIncidenteUtil.getTipoRecurso(peticao);

		assertNotNull(tipoRecurso);
		assertEquals(tipoRecursoRetornado, tipoRecurso);
	}

	private void testGetTipoRecursoIncidenteJulgamento() {
		TipoIncidenteJulgamento tipoIncidenteJulgamento = mock(TipoIncidenteJulgamento.class);
		when(tipoIncidenteJulgamento.getId()).thenReturn(1L);

		IncidenteJulgamento incidenteJulgamento = mock(IncidenteJulgamento.class);
		when(incidenteJulgamento.getTipoJulgamento()).thenReturn(tipoIncidenteJulgamento);

		TipoRecurso tipoRecurso = ObjetoIncidenteUtil.getTipoRecurso(incidenteJulgamento);

		assertNotNull(tipoRecurso);
		assertEquals(tipoIncidenteJulgamento, tipoRecurso);
	}

	private void testGetTipoRecursoOutros() {
		TipoRecurso tipoRecurso = ObjetoIncidenteUtil.getTipoRecurso(objetoIncidente);

		assertNull(tipoRecurso);
	}

	@Test
	public void testGetRecursoProcesso() {
		testGetRecursoProcessoNull();

		testGetRecursoProcessoRecursoProcesso();

		testGetRecursoProcessoOutros();
	}

	private void testGetRecursoProcessoNull() {
		RecursoProcesso recursoProcesso = ObjetoIncidenteUtil.getRecursoProcesso(null);

		assertNull(recursoProcesso);
	}

	private void testGetRecursoProcessoRecursoProcesso() {
		RecursoProcesso recursoProcesso = mock(RecursoProcesso.class);

		RecursoProcesso recursoProcessoRetornado = ObjetoIncidenteUtil.getRecursoProcesso(recursoProcesso);

		assertNotNull(recursoProcessoRetornado);
		assertEquals(recursoProcesso, recursoProcessoRetornado);
	}

	private void testGetRecursoProcessoOutros() {
		RecursoProcesso recursoProcesso = ObjetoIncidenteUtil.getRecursoProcesso(objetoIncidente);

		assertNull(recursoProcesso);
	}

	@Test
	public void testGetIncidenteJulgamento() {
		testGetIncidenteJulgamentoNull();

		testGetIncidenteJulgamentoIncidenteJulgamento();

		testGetIncidenteJulgamentoOutros();
	}

	private void testGetIncidenteJulgamentoNull() {
		IncidenteJulgamento incidenteJulgamento = ObjetoIncidenteUtil.getIncidenteJulgamento(null);

		assertNull(incidenteJulgamento);
	}

	private void testGetIncidenteJulgamentoIncidenteJulgamento() {
		IncidenteJulgamento incidenteJulgamento = mock(IncidenteJulgamento.class);

		IncidenteJulgamento incidenteJulgamentoRetornado = ObjetoIncidenteUtil.getIncidenteJulgamento(incidenteJulgamento);

		assertNotNull(incidenteJulgamentoRetornado);
		assertEquals(incidenteJulgamento, incidenteJulgamentoRetornado);
	}

	private void testGetIncidenteJulgamentoOutros() {
		IncidenteJulgamento incidenteJulgamento = ObjetoIncidenteUtil.getIncidenteJulgamento(objetoIncidente);

		assertNull(incidenteJulgamento);
	}

	@Test
	public void testGetProcesso() {
		testGetProcessoNull();

		testGetProcessoProcesso();

		testGetProcessoOutros();
	}

	private void testGetProcessoNull() {
		Processo processo = ObjetoIncidenteUtil.getProcesso(null);

		assertNull(processo);
	}

	private void testGetProcessoProcesso() {
		Processo processo = mock(Processo.class);
		when(processo.getId()).thenReturn(1L);

		Processo processoRetornado = ObjetoIncidenteUtil.getProcesso(processo);

		assertNotNull(processoRetornado);
		assertEquals(processo, processoRetornado);
	}

	private void testGetProcessoOutros() {
		Processo processo = mock(Processo.class);
		when(processo.getId()).thenReturn(1L);
		when(objetoIncidente.getPrincipal()).thenReturn(processo);

		Processo processoRetornado = ObjetoIncidenteUtil.getProcesso(objetoIncidente);

		assertNotNull(processoRetornado);
		assertEquals(processo, processoRetornado);
	}

	@Test
	public void testGetProtocolo() {
		testGetProtocoloNull();

		testGetProtocoloProcessoAnteriorProtocolo();

		testGetProtocoloProcessoAnteriorOutros();

		testGetProtocoloRecursoProcesso();

		testGetProtocoloIncidenteJulgamento();

		testGetProtocoloProtocolo();

		testGetProtocoloOutros();
	}

	private void testGetProtocoloNull() {
		Protocolo protocolo = ObjetoIncidenteUtil.getProtocolo(null);

		assertNull(protocolo);
	}

	@SuppressWarnings("unchecked")
	private void testGetProtocoloProcessoAnteriorProtocolo() {
		Protocolo protocolo = mock(Protocolo.class);
		when(protocolo.getId()).thenReturn(1L);

		Processo processo = mock(Processo.class);
		when(processo.getAnterior()).thenReturn(protocolo);

		Protocolo protocoloRetornado = ObjetoIncidenteUtil.getProtocolo(processo);

		assertNotNull(protocoloRetornado);
		assertEquals(protocolo, protocoloRetornado);
	}

	@SuppressWarnings("unchecked")
	private void testGetProtocoloProcessoAnteriorOutros() {
		Peticao peticao = mock(Peticao.class);
		when(peticao.getId()).thenReturn(1L);

		Processo processo = mock(Processo.class);
		when(processo.getAnterior()).thenReturn(peticao);

		Protocolo protocoloRetornado = ObjetoIncidenteUtil.getProtocolo(processo);

		assertNull(protocoloRetornado);
	}

	@SuppressWarnings("unchecked")
	private void testGetProtocoloRecursoProcesso() {
		Protocolo protocolo = mock(Protocolo.class);
		when(protocolo.getId()).thenReturn(1L);

		Processo processo = mock(Processo.class);
		when(processo.getAnterior()).thenReturn(protocolo);

		RecursoProcesso recursoProcesso = mock(RecursoProcesso.class);
		when(recursoProcesso.getPrincipal()).thenReturn(processo);

		Protocolo protocoloRetornado = ObjetoIncidenteUtil.getProtocolo(recursoProcesso);

		assertNotNull(protocoloRetornado);
		assertEquals(protocolo, protocoloRetornado);
	}

	@SuppressWarnings("unchecked")
	private void testGetProtocoloIncidenteJulgamento() {
		Protocolo protocolo = mock(Protocolo.class);
		when(protocolo.getId()).thenReturn(1L);

		Processo processo = mock(Processo.class);
		when(processo.getAnterior()).thenReturn(protocolo);

		IncidenteJulgamento incidenteJulgamento = mock(IncidenteJulgamento.class);
		when(incidenteJulgamento.getPrincipal()).thenReturn(processo);

		Protocolo protocoloRetornado = ObjetoIncidenteUtil.getProtocolo(incidenteJulgamento);

		assertNotNull(protocoloRetornado);
		assertEquals(protocolo, protocoloRetornado);
	}

	private void testGetProtocoloProtocolo() {
		Protocolo protocolo = mock(Protocolo.class);
		when(protocolo.getId()).thenReturn(1L);

		Protocolo protocoloRetornado = ObjetoIncidenteUtil.getProtocolo(protocolo);

		assertNotNull(protocoloRetornado);
		assertEquals(protocolo, protocoloRetornado);
	}

	private void testGetProtocoloOutros() {
		Protocolo protocoloRetornado = ObjetoIncidenteUtil.getProtocolo(objetoIncidente);

		assertNull(protocoloRetornado);
	}

	@Test
	public void testGetPeticao() {
		testGetPeticaoNull();

		testGetPeticaoObjetoIncidenteAnteriorNull();

		testGetPeticaoObjetoIncidenteAnteriorPeticao();

		testGetPeticaoObjetoIncidenteAnteriorOutros();
	}

	private void testGetPeticaoNull() {
		Peticao peticao = ObjetoIncidenteUtil.getPeticao(null);

		assertNull(peticao);
	}

	private void testGetPeticaoObjetoIncidenteAnteriorNull() {
		Peticao peticaoRetornada = ObjetoIncidenteUtil.getPeticao(objetoIncidente);

		assertNull(peticaoRetornada);
	}

	@SuppressWarnings("unchecked")
	private void testGetPeticaoObjetoIncidenteAnteriorPeticao() {
		Peticao peticao = mock(Peticao.class);
		when(objetoIncidente.getAnterior()).thenReturn(peticao);

		Peticao peticaoRetornada = ObjetoIncidenteUtil.getPeticao(objetoIncidente);

		assertNotNull(peticaoRetornada);
		assertEquals(peticao, peticaoRetornada);
	}

	@SuppressWarnings("unchecked")
	private void testGetPeticaoObjetoIncidenteAnteriorOutros() {
		Protocolo protocolo = mock(Protocolo.class);
		when(objetoIncidente.getAnterior()).thenReturn(protocolo);

		Peticao peticao = ObjetoIncidenteUtil.getPeticao(objetoIncidente);

		assertNull(peticao);
	}

	@Test
	public void testGetSiglaCadeiaIncidente() {
		testGetSiglaCadeiaIncidenteNull();

		testGetSiglaCadeiaIncidenteRecursoProcesso();

		testGetSiglaCadeiaIncidenteIncidenteJulgamento();

		testGetSiglaCadeiaIncidenteProcesso();

		testGetSiglaCadeiaIncidenteOutros();
	}

	private void testGetSiglaCadeiaIncidenteNull() {
		String siglaCadeiaIncidente = ObjetoIncidenteUtil.getSiglaCadeiaIncidente(null);

		assertNull(siglaCadeiaIncidente);
	}

	private void testGetSiglaCadeiaIncidenteRecursoProcesso() {
		RecursoProcesso recursoProcesso = mock(RecursoProcesso.class);
		when(recursoProcesso.getSiglaCadeiaIncidente()).thenReturn("XXX");

		String siglaCadeiaIncidente = ObjetoIncidenteUtil.getSiglaCadeiaIncidente(recursoProcesso);

		assertNotNull(siglaCadeiaIncidente);
		assertEquals("XXX", siglaCadeiaIncidente);
	}

	private void testGetSiglaCadeiaIncidenteIncidenteJulgamento() {
		IncidenteJulgamento incidenteJulgamento = mock(IncidenteJulgamento.class);
		when(incidenteJulgamento.getSiglaCadeiaIncidente()).thenReturn("XXX");

		String siglaCadeiaIncidente = ObjetoIncidenteUtil.getSiglaCadeiaIncidente(incidenteJulgamento);

		assertNotNull(siglaCadeiaIncidente);
		assertEquals("XXX", siglaCadeiaIncidente);
	}

	private void testGetSiglaCadeiaIncidenteProcesso() {
		Processo processo = mock(Processo.class);
		when(processo.getSiglaClasseProcessual()).thenReturn("XXX");

		String siglaCadeiaIncidente = ObjetoIncidenteUtil.getSiglaCadeiaIncidente(processo);

		assertNotNull(siglaCadeiaIncidente);
		assertEquals("XXX", siglaCadeiaIncidente);
	}

	private void testGetSiglaCadeiaIncidenteOutros() {
		String siglaCadeiaIncidente = ObjetoIncidenteUtil.getSiglaCadeiaIncidente(objetoIncidente);

		assertNull(siglaCadeiaIncidente);
	}
}
