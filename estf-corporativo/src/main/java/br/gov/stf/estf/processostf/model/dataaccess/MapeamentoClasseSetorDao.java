package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.MapeamentoClasseSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface MapeamentoClasseSetorDao extends GenericDao<MapeamentoClasseSetor, Long> {

	public abstract List<MapeamentoClasseSetor> recuperarMapeamentosDaClasse(String classe) throws DaoException;
	public List<String> buscaClasseDoSetor(Setor setorDoUsuario) throws DaoException;

}