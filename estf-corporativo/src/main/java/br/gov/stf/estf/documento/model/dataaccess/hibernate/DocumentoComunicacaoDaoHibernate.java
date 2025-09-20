package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoComunicacaoDao;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;


@SuppressWarnings("unchecked")
@Repository
public class DocumentoComunicacaoDaoHibernate extends GenericHibernateDao<DocumentoComunicacao, Long> implements DocumentoComunicacaoDao{

	public DocumentoComunicacaoDaoHibernate() {
		super(DocumentoComunicacao.class);
	}
	
	public DocumentoComunicacao recuperarNaoCancelado(Comunicacao comunicacao) throws DaoException {
		DocumentoComunicacao documentoComunicacao = null;
		/*		List<DocumentoTexto> lista = new LinkedList<DocumentoTexto>();

				List<Long> listaTipoSituacaoDocumento = new LinkedList<Long>();
				listaTipoSituacaoDocumento.add(0,TipoSituacaoDocumento.CANCELADO_AUTOMATICAMENTE.getCodigo());
				listaTipoSituacaoDocumento.add(1,TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO.getCodigo());*/

		try {

			Session session = retrieveSession();

			String hql = "SELECT dc FROM DocumentoComunicacao dc, DocumentoEletronicoView de "
					+ "WHERE dc.documentoEletronico.id = de.id" + " AND dc.comunicacao.id = ? "
					+ " AND de.descricaoStatusDocumento <> ? ";

			Query q = session.createQuery(hql);

			q.setLong(0, comunicacao.getId());
			q.setString(1, DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO);


			documentoComunicacao = (DocumentoComunicacao) q.uniqueResult();
		} catch (Exception e) {

			throw new DaoException(e);
		}
		return documentoComunicacao;
	}


	public void recarregarDocumentoComunicacao(DocumentoComunicacao documentoComunicacao) throws DaoException{
		Session session = retrieveSession();
		session.refresh(documentoComunicacao);
	}
	@Override
	public List<DocumentoComunicacao> pesquisarDocumentosPelaComunicacao(Comunicacao comunicacao)
			throws DaoException {
		List<DocumentoComunicacao> docs = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PecaProcessoEletronicoComunicacao.class);

			c.add(Restrictions.eq("comunicacao.id", comunicacao.getId()));
			docs = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return docs;
	}
	
	
	
}
