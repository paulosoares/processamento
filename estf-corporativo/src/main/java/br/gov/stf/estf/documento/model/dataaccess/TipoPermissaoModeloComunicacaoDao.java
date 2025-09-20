package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoPermissaoModeloComunicacaoDao extends
		GenericDao<TipoPermissaoModeloComunicacao, Long> {

	List<TipoPermissaoModeloComunicacao> pesquisarPermissoes(Setor setor,
			boolean incluirInstitucional) throws DaoException;

	List<TipoPermissaoModeloComunicacao> pesquisarPermissoes(
			String descricaoPermissao, Boolean exatamenteIgual)
			throws DaoException;
}
