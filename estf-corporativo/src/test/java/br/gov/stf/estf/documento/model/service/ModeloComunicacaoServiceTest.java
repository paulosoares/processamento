package br.gov.stf.estf.documento.model.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.gov.stf.estf.documento.model.dataaccess.ModeloComunicacaoDao;
import br.gov.stf.estf.documento.model.dataaccess.TipoComunicacaoDao;
import br.gov.stf.estf.documento.model.service.impl.ModeloComunicacaoServiceImpl;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.entity.Flag;
import br.gov.stf.framework.model.service.ServiceException;

public class ModeloComunicacaoServiceTest {

	private ModeloComunicacaoService modeloComunicacaoService;

	@Mock
	private ModeloComunicacaoDao modeloComunicacaoDao;
	@Mock
	private ArquivoEletronicoService arquivoEletronicoService;
	@Mock
	private TipoComunicacaoDao tipoComunicacaoDao;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		modeloComunicacaoService = new ModeloComunicacaoServiceImpl(modeloComunicacaoDao, arquivoEletronicoService, tipoComunicacaoDao);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPesquisarStringLongLongString() throws DaoException, ServiceException {
		String nomeModelo = "TESTE";
		Long idTipoComunicacao = 1L;
		Long idTipoPermissao = 1L;
		String flagAtivo = Flag.SIM;

		ModeloComunicacao modeloComunicacao = criarModeloComunicacao(nomeModelo, idTipoComunicacao, idTipoPermissao, null, flagAtivo);

		when(modeloComunicacaoDao.pesquisar(nomeModelo, idTipoComunicacao, idTipoPermissao, flagAtivo)).thenReturn(Arrays.asList(modeloComunicacao));

		List<ModeloComunicacao> modelos = modeloComunicacaoService.pesquisar(nomeModelo, idTipoComunicacao, idTipoPermissao, flagAtivo);
		assertNotNull(modelos);
		assertEquals(1, modelos.size());

		validarModelo(modelos.get(0), nomeModelo, idTipoComunicacao, idTipoPermissao, null, flagAtivo);
	}

	@Test
	public void testPesquisarStringLongSetorString() throws DaoException, ServiceException {
		String nomeModelo = "TESTE";
		Long idTipoComunicacao = 1L;
		Setor setor = new Setor();
		setor.setId(1L);
		String flagAtivo = Flag.SIM;

		ModeloComunicacao modeloComunicacao = criarModeloComunicacao(nomeModelo, idTipoComunicacao, 1L, setor, flagAtivo);

		when(modeloComunicacaoDao.pesquisar(nomeModelo, idTipoComunicacao, setor, flagAtivo)).thenReturn(Arrays.asList(modeloComunicacao));

		List<ModeloComunicacao> modelos = modeloComunicacaoService.pesquisar(nomeModelo, idTipoComunicacao, setor, flagAtivo);
		assertNotNull(modelos);
		assertEquals(1, modelos.size());

		validarModelo(modelos.get(0), nomeModelo, idTipoComunicacao, null, setor, flagAtivo);
	}

	@Test
	public void testIncluirNovoDocumento() throws ServiceException, DaoException {
		byte[] odt = { 0, 1, 2 };
		ArquivoEletronico arquivoEletronico = new ArquivoEletronico();
		arquivoEletronico.setConteudo(odt);

		String tipoArquivo = "PDF";

		Long idTipoComunicacao = 1L;
		TipoComunicacao tipoComunicacao = new TipoComunicacao();
		tipoComunicacao.setId(idTipoComunicacao);

		when(arquivoEletronicoService.salvar(arquivoEletronico)).thenReturn(arquivoEletronico);
		when(tipoComunicacaoDao.pesquisarTipoModelo(idTipoComunicacao)).thenReturn(tipoComunicacao);

		ModeloComunicacao modeloComunicacao = modeloComunicacaoService.incluirNovoDocumento(idTipoComunicacao, odt, tipoArquivo);

		assertNotNull(modeloComunicacao);

		ArquivoEletronico arquivoEletronicoRetornado = modeloComunicacao.getArquivoEletronico();
		assertNotNull(arquivoEletronicoRetornado);
		assertEquals(tipoArquivo, arquivoEletronicoRetornado.getFormato());
		assertArrayEquals(odt, arquivoEletronicoRetornado.getConteudo());

		assertEquals(tipoComunicacao, modeloComunicacao.getTipoComunicacao());
	}

	@Test
	public void testPesquisarModeloEscolhido() throws DaoException, ServiceException {
		Long idModelo = 1L;

		Long idTipoComunicacao = 1L;
		TipoComunicacao tipoComunicacao = new TipoComunicacao();
		tipoComunicacao.setId(idTipoComunicacao);

		ModeloComunicacao modeloComunicacao = new ModeloComunicacao();
		modeloComunicacao.setId(idModelo);
		modeloComunicacao.setTipoComunicacao(tipoComunicacao);

		when(tipoComunicacaoDao.pesquisarTipoModelo(idTipoComunicacao)).thenReturn(tipoComunicacao);
		when(modeloComunicacaoDao.pesquisarModeloEscolhido(idModelo, tipoComunicacao)).thenReturn(modeloComunicacao);

		ModeloComunicacao modeloPesquisado = modeloComunicacaoService.pesquisarModeloEscolhido(idModelo, idTipoComunicacao);
		assertNotNull(modeloPesquisado);
		assertEquals(idModelo, modeloPesquisado.getId());
		assertEquals(tipoComunicacao, modeloPesquisado.getTipoComunicacao());
	}

	private ModeloComunicacao criarModeloComunicacao(String nomeModelo, Long idTipoComunicacao, Long idTipoPermissao, Setor setor, String flagAtivo) {
		ModeloComunicacao modeloComunicacao = new ModeloComunicacao();
		modeloComunicacao.setDscModelo(nomeModelo);
		TipoComunicacao tipoComunicacao = new TipoComunicacao();
		tipoComunicacao.setId(idTipoComunicacao);
		TipoPermissaoModeloComunicacao tipoPermissao = new TipoPermissaoModeloComunicacao();
		tipoPermissao.setId(idTipoPermissao);
		tipoPermissao.setSetor(setor);
		modeloComunicacao.setTipoPermissao(tipoPermissao);
		modeloComunicacao.setTipoComunicacao(tipoComunicacao);
		modeloComunicacao.setFlagAtivo(flagAtivo);
		return modeloComunicacao;
	}

	private void validarModelo(ModeloComunicacao modeloPesquisado, String nomeModelo, Long idTipoComunicacao, Long idTipoPermissao, Setor setor,
			String flagAtivo) {
		assertNotNull(modeloPesquisado);

		assertEquals(nomeModelo, modeloPesquisado.getDscModelo());

		TipoComunicacao tipoComunicacao = modeloPesquisado.getTipoComunicacao();
		assertNotNull(tipoComunicacao);
		assertEquals(idTipoComunicacao, tipoComunicacao.getId());

		TipoPermissaoModeloComunicacao tipoPermissao = modeloPesquisado.getTipoPermissao();
		assertNotNull(tipoPermissao);

		if (idTipoPermissao != null) {
			assertEquals(idTipoPermissao, tipoPermissao.getId());
		}

		if (setor != null) {
			assertEquals(setor, tipoPermissao.getSetor());
		}

		assertEquals(flagAtivo, modeloPesquisado.getFlagAtivo());
	}
}
