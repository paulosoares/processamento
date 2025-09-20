package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.judiciario.TransacaoIntegracao;
import br.gov.stf.estf.processostf.model.dataaccess.TransacaoIntegracaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TransacaoIntegracaoDaoHibernate extends GenericHibernateDao<TransacaoIntegracao, Long> implements TransacaoIntegracaoDao { 

	private static final long serialVersionUID = 1L;
	
	private String remessaBaixaProcessual = "REMESSA_BAIXA_PROCESSUAL";
	private List<String> situacoes = Arrays.asList(new String[]{"AGUARDANDO_CONFIRMACAO", "FECHADA"});

	public TransacaoIntegracaoDaoHibernate() {
		super(TransacaoIntegracao.class);
	}

	@Override
	public Boolean houveRemessa(Long seqObjetoIncidente) throws DaoException{

		Session session = retrieveSession();
		String hql = "select count(t) from TransacaoIntegracao t where t.seqObjetoIncidente = :seqObjetoIncidente and t.fluxoNegocio = :fluxoNegocio and t.situacao in (:tipoSituacao)";
		
		Query query = session.createQuery(hql.toString());
		query.setLong("seqObjetoIncidente", seqObjetoIncidente);
		query.setString("fluxoNegocio", remessaBaixaProcessual);
		query.setParameterList("tipoSituacao", situacoes);
		 
		Object uniqueResult = query.uniqueResult();
		if ( ((Long)uniqueResult) > 0 )
			return true;
		
		return false;
	}

}
