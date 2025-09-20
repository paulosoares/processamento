package br.gov.stf.estf.documento.model.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TipoPermissaoModeloComunicacaoDao;
import br.gov.stf.estf.documento.model.service.TipoPermissaoModeloComunicacaoService;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoPermissaoModeloComunicacaoService")
public class TipoPermissaoModeloComunicacaoServiceImpl	extends	GenericServiceImpl<TipoPermissaoModeloComunicacao, Long, TipoPermissaoModeloComunicacaoDao>
		implements TipoPermissaoModeloComunicacaoService {

	public TipoPermissaoModeloComunicacaoServiceImpl(
			TipoPermissaoModeloComunicacaoDao dao) {
		super(dao);
	}

	@Override
	public List<TipoPermissaoModeloComunicacao> pesquisarPermissoes(
			Setor setor, boolean incluirInstitucional) throws ServiceException {
		List<TipoPermissaoModeloComunicacao> permissoes = Collections
				.emptyList();

		try {
			permissoes = dao.pesquisarPermissoes(setor, incluirInstitucional);
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}

		return permissoes;
	}

	@Override
	public List<TipoPermissaoModeloComunicacao> pesquisarPermissoes(
			String descricaoPermissao, Boolean exatamenteIgual)
			throws ServiceException {
		List<TipoPermissaoModeloComunicacao> permissoes = Collections
				.emptyList();

		try {
			permissoes = dao.pesquisarPermissoes(descricaoPermissao,
					exatamenteIgual);
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}

		return permissoes;
	}
	
	@Override
	 public TipoPermissaoModeloComunicacao recuperarPorId(Long idPermissao) throws ServiceException {
		 TipoPermissaoModeloComunicacao tipoPermissao = new TipoPermissaoModeloComunicacao();
		 try{
			 tipoPermissao = dao.recuperarPorId(idPermissao);
		 }catch (DaoException e) {
			 e.printStackTrace();
		}
		return tipoPermissao;
	 }
}
