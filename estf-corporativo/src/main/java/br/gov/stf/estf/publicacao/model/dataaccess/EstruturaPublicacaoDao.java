package br.gov.stf.estf.publicacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao.EstruturaPublicacaoId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface EstruturaPublicacaoDao extends GenericDao<EstruturaPublicacao, EstruturaPublicacaoId> {
	public String recuperarDescricao (Integer codigoCapitulo, Integer codigoMateria) throws DaoException;
	public List<EstruturaPublicacao> pesquisarEstruturasPorCodigoCapitulo (byte codigoCapitulo) throws DaoException;
	public List<EstruturaPublicacao> recuperarTodos()throws DaoException;
	public EstruturaPublicacao recuperar (Integer codCapitulo, Integer codMateria, Integer codConteudo ) throws DaoException;
	public List<EstruturaPublicacao> pesquisar (Integer codigoCapitulo, Integer codigoMaterias, Integer codigoConteudo) throws DaoException;
}
