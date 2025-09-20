package br.gov.stf.estf.documento.model.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TipoComunicacaoDao;
import br.gov.stf.estf.documento.model.service.TipoComunicacaoService;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoComunicacaoService")
public class TipoComunicacaoServiceImpl extends GenericServiceImpl<TipoComunicacao, Long, TipoComunicacaoDao> implements TipoComunicacaoService {

	public TipoComunicacaoServiceImpl(TipoComunicacaoDao dao) {
		super(dao);
	}

	@Override
	public TipoComunicacao pesquisarTipoModelo(Long idTipoModelo) throws ServiceException {
		TipoComunicacao tipoComunicacao = new TipoComunicacao();

		try {
			tipoComunicacao = dao.pesquisarTipoModelo(idTipoModelo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return tipoComunicacao;
	}

	@Override
	public List<TipoComunicacao> pesquisarListaTiposModelos(String nomeTipoModelo) throws ServiceException {
		List<TipoComunicacao> list = Collections.emptyList();

		try {
			list = dao.pesquisarListaTiposModelos(nomeTipoModelo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return list;
	}

	@Override
	public TipoComunicacao pesquisarUnicoTipoModelo(String nomeTipoModelo, Long codigoTipoPermissao) throws ServiceException {
		Validate.notEmpty(nomeTipoModelo, "O nome do tipo de modelo não pode ser nulo");
		Validate.notNull(codigoTipoPermissao, "O código do tipo de permissão não pode ser nulo.");

		TipoComunicacao tipoComunicacao = null;

		try {
			tipoComunicacao = dao.pesquisarUnicoTipoModelo(nomeTipoModelo, codigoTipoPermissao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return tipoComunicacao;
	}

	@Override
	public List<TipoComunicacao> pesquisarListaTiposModelos(String nomeTipoModelo, Setor setor) throws ServiceException {
		List<TipoComunicacao> list = Collections.emptyList();

		try {
			list = dao.pesquisarListaTiposModelos(nomeTipoModelo, setor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return list;
	}

	@Override
	public Long pesquisaNumeracaoUnicaModelo(Long idTipoComunicacao) throws ServiceException {
		Long numeracaoUnica = null;
		
		try {
			numeracaoUnica = dao.pesquisaNumeracaoUnicaModelo(idTipoComunicacao);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return numeracaoUnica;
	}
}
