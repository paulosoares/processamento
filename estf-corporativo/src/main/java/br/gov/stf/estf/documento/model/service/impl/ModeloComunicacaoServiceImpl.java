package br.gov.stf.estf.documento.model.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.ModeloComunicacaoDao;
import br.gov.stf.estf.documento.model.dataaccess.TipoComunicacaoDao;
import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("modeloComunicacaoService")
public class ModeloComunicacaoServiceImpl extends
		GenericServiceImpl<ModeloComunicacao, Long, ModeloComunicacaoDao>
		implements ModeloComunicacaoService {

	private final ArquivoEletronicoService arquivoEletronicoService;
	private final TipoComunicacaoDao tipoComunicacaoDao;

	public ModeloComunicacaoServiceImpl(ModeloComunicacaoDao dao,
			ArquivoEletronicoService arquivoEletronicoService,
			TipoComunicacaoDao tipoComunicacaoDao) {
		super(dao);
		this.arquivoEletronicoService = arquivoEletronicoService;
		this.tipoComunicacaoDao = tipoComunicacaoDao;
	}

	@Override
	public List<ModeloComunicacao> pesquisar(String nomeModelo,
			Long tipoModelo, Long tipoPermissao, String flagAtivo)
			throws ServiceException {
		List<ModeloComunicacao> modelos = Collections.emptyList();

		try {
			modelos = dao.pesquisar(nomeModelo, tipoModelo, tipoPermissao,
					flagAtivo);
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}

		return modelos;
	}

	@Override
	public List<ModeloComunicacao> pesquisar(String nomeModelo,
			Long tipoModelo, Setor setor, String flagAtivo)
			throws ServiceException {
		List<ModeloComunicacao> modelos = Collections.emptyList();

		try {
			modelos = dao.pesquisar(nomeModelo, tipoModelo, setor, flagAtivo);
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}

		return modelos;
	}
	
	

	@Override
	public List<TipoComunicacao> pesquisarTipoComunicacaoPeloSetorPermissao(Setor setor)
			throws ServiceException {
		List<TipoComunicacao> listaTipoComunicacao = Collections.emptyList();

		try {
			listaTipoComunicacao = dao.pesquisarTipoComunicacaoPeloSetorPermissao(setor);
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}

		return listaTipoComunicacao;
	}

	@Override
	public List<TipoComunicacao> pesquisarTipoComunicacao() throws ServiceException {
		List<TipoComunicacao> listaTipoComunicacao = Collections.emptyList();

		try {
			listaTipoComunicacao = dao.pesquisarTipoComunicacao();
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}

		return listaTipoComunicacao;
	}

	/**
	 * Método criado para contemplar a funcionalidade do AssinadorWeb para criar
	 * os modelos de documentos pela Secretaria Judiciaria. Este método além de
	 * criar o modelo, também cria o Arquivo Eletronico
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ModeloComunicacao incluirNovoDocumento(Long codigoTipoModelo,
			byte[] odt, String tipoArquivo) throws ServiceException {

		ArquivoEletronico arquivo = new ArquivoEletronico();
		TipoComunicacao tipo = new TipoComunicacao();

		arquivo.setFormato(tipoArquivo);
		arquivo.setConteudo(odt);

		try {
			arquivoEletronicoService.salvar(arquivo);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		try {
			tipo = tipoComunicacaoDao.pesquisarTipoModelo(codigoTipoModelo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		ModeloComunicacao modelo = new ModeloComunicacao();
		modelo.setTipoComunicacao(tipo);
		modelo.setArquivoEletronico(arquivo);

		return modelo;
	}

	@Override
	public ModeloComunicacao pesquisarModeloEscolhido(Long idModelo,
			Long idTipoModelo) throws ServiceException {

		ModeloComunicacao modeloEscolhido = new ModeloComunicacao();
		TipoComunicacao tipo = new TipoComunicacao();

		try {
			tipo = tipoComunicacaoDao.pesquisarTipoModelo(idTipoModelo);
		} catch (DaoException e1) {
			throw new ServiceException(e1);
		}

		try {
			modeloEscolhido = dao.pesquisarModeloEscolhido(idModelo, tipo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return modeloEscolhido;
	}


}
