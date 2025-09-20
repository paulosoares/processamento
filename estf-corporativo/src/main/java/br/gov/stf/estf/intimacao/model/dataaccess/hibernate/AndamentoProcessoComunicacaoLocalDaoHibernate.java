package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento;
import br.gov.stf.estf.intimacao.model.dataaccess.AndamentoProcessoComunicacaoLocalDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

@Repository
public class AndamentoProcessoComunicacaoLocalDaoHibernate extends GenericHibernateDao<AndamentoProcessoComunicacao, Long> implements AndamentoProcessoComunicacaoLocalDao {

    private static final long serialVersionUID = 1L;

    public AndamentoProcessoComunicacaoLocalDaoHibernate() {
        super(AndamentoProcessoComunicacao.class);
    }

    @Override
    public AndamentoProcessoComunicacao salvar(AndamentoProcessoComunicacao andamentoProcessoComunicacao)
            throws DaoException {

        Session session = retrieveSession();
        try {
            session.save(andamentoProcessoComunicacao);
        } catch (HibernateException e) {
            throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
        } catch (RuntimeException e) {
            throw new DaoException("RuntimeException", e);
        }
        return andamentoProcessoComunicacao;
    }

    @SuppressWarnings("unchecked")
    public List<AndamentoProcesso> pesquisarAndamentoProcesso(String sigla, Long numero) throws DaoException {
        List<AndamentoProcesso> andamentos = null;
        StringBuffer sql = new StringBuffer();

        try {
            sql.append(" SELECT ap.* FROM STF.ANDAMENTO_PROCESSOS ap ");
            sql.append(" WHERE   ");

            if (sigla != null) {
                sql.append("  ap.SIG_CLASSE_PROCES =:sigla  ");
            }

            if (numero != null) {
                sql.append(" AND ap.NUM_PROCESSO =:numero  ");
            }

            sql.append(" order by ap.NUM_SEQUENCIA DESC");

            SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

            if (sigla != null) {
                sqlQuery.setString("sigla", sigla);
            }

            if (numero != null) {
                sqlQuery.setLong("numero", numero);
            }
            sqlQuery.addEntity(AndamentoProcesso.class);
            andamentos = sqlQuery.list();
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return andamentos;
    }

    @SuppressWarnings("unchecked")
	public List<AndamentoProcesso> pesquisarAndamentosProcessoIncidente(Long idProcessoIncidente) throws DaoException {
        List<AndamentoProcesso> andamentos = null;
        StringBuffer sql = new StringBuffer();

        try {
            sql.append(" SELECT ap.* FROM STF.ANDAMENTO_PROCESSOS ap ");
            sql.append(" WHERE ap.Seq_Objeto_Incidente = :idProcesso");            

            sql.append(" order by ap.NUM_SEQUENCIA DESC");

            SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
            
            sqlQuery.setLong("idProcesso", idProcessoIncidente);
           
            sqlQuery.addEntity(AndamentoProcesso.class);
            andamentos = sqlQuery.list();
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return andamentos;
    }
    
   	public AndamentoProcessoComunicacao recuperarAndamentoProcessoGeradoPelaComunicacao(
   			Long idComunicacao, Long idCodigoAndamento) throws DaoException {
    	
    	AndamentoProcessoComunicacao andamentoProcessoComunicacao = null;
    	StringBuffer sql = new StringBuffer();  
    	Session session = retrieveSession();
    	try {
           sql.append(" SELECT apc from AndamentoProcessoComunicacao apc ");
           sql.append(" WHERE apc.comunicacao.id = :idComunicacao AND ");
           sql.append(" apc.andamentoProcesso.codigoAndamento = :idCodigoAndamento AND" );
           sql.append(" apc.tipoVinculoAndamento = :tipoAndamentoComunicacao "); 
    	
           Query query = session.createQuery(sql.toString());
           query.setLong("idComunicacao", idComunicacao);
           query.setLong("idCodigoAndamento", idCodigoAndamento);
           query.setString("tipoAndamentoComunicacao", TipoVinculoAndamento.GERADO.getCodigo());
           andamentoProcessoComunicacao = (AndamentoProcessoComunicacao) query.uniqueResult();
           
    	} catch (Exception e) {
           throw new DaoException(e);
    	}           
    	return andamentoProcessoComunicacao;
     }    
}