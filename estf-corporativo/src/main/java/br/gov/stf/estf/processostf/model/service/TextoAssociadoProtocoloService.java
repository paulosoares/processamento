package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo.TipoAssociacao;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.processostf.model.dataaccess.TextoAssociadoProtocoloDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TextoAssociadoProtocoloService extends GenericService<TextoAssociadoProtocolo, TextoAssociadoProtocolo.TextoAssociadoProtocoloId, TextoAssociadoProtocoloDao> {
	public List<TextoAssociadoProtocolo> pesquisar ( ConteudoPublicacao conteudoPublicacao, TipoAssociacao ...tipoAssociacao ) throws ServiceException;
	public TextoAssociadoProtocolo recuperar ( ProtocoloPublicado protocoloPublicado, TipoAssociacao tipoAssociacao ) throws ServiceException;
	public List<TextoAssociadoProtocolo> pesquisar ( ProtocoloPublicado protocoloPublicado ) throws ServiceException;
}
