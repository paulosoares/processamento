package br.gov.stf.estf.publicacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao.EstruturaPublicacaoId;
import br.gov.stf.estf.publicacao.model.dataaccess.EstruturaPublicacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface EstruturaPublicacaoService extends GenericService<EstruturaPublicacao, EstruturaPublicacaoId, EstruturaPublicacaoDao> {
	public String recuperarDescricao (Integer codigoCapitulo, Integer codigoMateria) throws ServiceException;
	public byte[] recuperarTitulo (Integer codigoCapitulo, Integer codigoMateria) throws ServiceException;
	public EstruturaPublicacao recuperar (Integer codCapitulo, Integer codMateria, Integer codConteudo ) throws ServiceException;
	public List<EstruturaPublicacao> pesquisar (Integer codigoCapitulo, Integer codigoMaterias, Integer codigoConteudo) throws ServiceException;
	public EstruturaPublicacao definirEstruturaDePublicacaoDeAta(Texto texto, Boolean isRepublicacao, Boolean isDespachoDecisaoPresidente) throws ServiceException;
}
