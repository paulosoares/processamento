package br.gov.stf.estf.documento.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DocumentoTextoPeticaoDao extends GenericDao<DocumentoTextoPeticao, Long> {

	public List<DocumentoTextoPeticao> pesquisarDocumentosSetor(Setor setor, TipoSituacaoDocumento tipoSituacaoDocumento, Date dataInicio, Date dataFim, Short anoProtocolo, Long numeroProtocolo) throws DaoException;		
	
	public DocumentoTextoPeticao recuperar(DocumentoEletronico documentoEletronico)
	throws DaoException;
}
