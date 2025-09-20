package br.gov.stf.estf.publicacao.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.publicacao.model.dataaccess.PublicacaoDao;
import br.gov.stf.estf.publicacao.model.util.ProcessoProtocoloPublicacaoSearchData;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;

public interface PublicacaoService extends GenericService<Publicacao, Long, PublicacaoDao> {
	public Date recuperarDataPublicacaoProcesso (String siglaClasse, Integer numero, Long codigoRecurso, String tipoJulgamento) throws ServiceException;
	public Short recuperarNumeroUltimoDj() throws ServiceException;
	public List<Publicacao> pesquisarDjNaoPublicado () throws ServiceException;
	public Publicacao inserirNovoDj (Short numero, Short ano, Date divulgacao, Date previsao, byte[] odt, List<ConteudoPublicacao> materias) throws ServiceException;
	public Publicacao alterarDj (Publicacao publicacao, byte[] odt) throws ServiceException;
	public Publicacao inserirPdfDj(Publicacao publicacao, byte[] pdf) throws ServiceException;
	public void alterarDescomposicaoDj (Publicacao publicacao) throws ServiceException;
	public void alterarConfirmacaoDj (Publicacao publicacao, String siglaUsuario, Setor setor, String observacao) throws ServiceException;
	public Publicacao recuperar (Short anoEdicaoDje, Short numeroEdicaoDje) throws ServiceException;
	public SearchResult pesquisarProcessoProtocoloPublicacao(ProcessoProtocoloPublicacaoSearchData sd)throws ServiceException;
	public void alterarTipoAcessoDocumentos(Long idPublicacao) throws ServiceException;
	void salvarDjAssinado(Long idPublicacao,
			DocumentoEletronico documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo)
			throws ServiceException;
}
