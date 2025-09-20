package br.gov.stf.estf.jurisdicionado.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoAssociacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AssociacaoJurisdicionadoDao extends GenericDao<AssociacaoJurisdicionado, Long> {
	public List<AssociacaoJurisdicionado> pesquisarAssociacoes(Jurisdicionado jurisdicionado) throws DaoException;
	public List<AssociacaoJurisdicionado> pesquisarAssociacoesMembro(Jurisdicionado jurisdicionado) throws DaoException;
	public AssociacaoJurisdicionado recuperarPorGrupoMembro(PapelJurisdicionado grupo, PapelJurisdicionado membro, EnumTipoAssociacao tipoAssociacao) throws DaoException;
}
