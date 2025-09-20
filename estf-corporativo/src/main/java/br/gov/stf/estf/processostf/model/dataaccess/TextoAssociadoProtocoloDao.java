package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo.TipoAssociacao;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TextoAssociadoProtocoloDao extends GenericDao<TextoAssociadoProtocolo, TextoAssociadoProtocolo.TextoAssociadoProtocoloId> {
	public List<TextoAssociadoProtocolo> pesquisar ( ConteudoPublicacao conteudoPublicacao, TipoAssociacao ...tipoAssociacao ) throws DaoException;
	public List<TextoAssociadoProtocolo> pesquisar ( ProtocoloPublicado protocoloPublicado ) throws DaoException;
	public TextoAssociadoProtocolo recuperar ( ProtocoloPublicado protocoloPublicado, TipoAssociacao tipoAssociacao ) throws DaoException;
}
