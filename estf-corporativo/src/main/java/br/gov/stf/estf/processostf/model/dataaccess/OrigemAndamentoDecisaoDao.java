package br.gov.stf.estf.processostf.model.dataaccess;


// default package
// Generated 18/03/2008 11:02:23 by Hibernate Tools 3.1.0.beta5
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;


/**
 * DAO interface for domain model class TipoRecurso.
 * @see .TipoRecurso
 * @author Hibernate Tools
 */

public interface OrigemAndamentoDecisaoDao extends GenericDao <OrigemAndamentoDecisao, Long> {
   
	public OrigemAndamentoDecisao pesquisarOrigemDecisao(Setor setor) throws DaoException;
	public List<OrigemAndamentoDecisao> pesquisarOrigensSemMinistro() throws DaoException;
	public List<OrigemAndamentoDecisao> pesquisarOrigensComMinistroAtivo() throws DaoException;
	public List<OrigemAndamentoDecisao> pesquisarOrigensDecisao(List<Long> idsOrigem) throws DaoException;
}

