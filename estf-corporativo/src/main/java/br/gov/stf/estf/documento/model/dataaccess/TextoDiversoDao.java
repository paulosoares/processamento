package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TextoDiverso;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TextoDiversoDao extends GenericDao<TextoDiverso, Long> {
	public List<TextoDiverso> pesquisar (Long codigoSetor, TipoTexto... tiposTexto) throws DaoException;
	public TextoDiverso recuperar (TipoTexto tipoTexto) throws DaoException;
	
	public TextoDiverso recuperar( String descricaoTextoDiverso, TipoTexto tipoTexto) throws DaoException;
	
	public List<TextoDiverso> pesquisar(Long codigoSetor, String descricao) throws DaoException;
}
