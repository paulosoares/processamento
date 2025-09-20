package br.gov.stf.estf.documento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.ComunicacaoDao;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoPaginatorResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoSearch;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ComunicacaoService extends GenericService<Comunicacao, Long, ComunicacaoDao> {

	/**
	 * Pesquisa a quantidade de comunicações que estão em determinada fase.
	 * 
	 * @param faseComunicacao a fase atual da comunicação
	 * @param setor setor atual da comunicação
	 * @param usuario usuário de criação (opcional)
	 * @return a quantidade de comunicações na fase <code>faseComunicacao</code> no setor <code>setor</code> e com usuário de criação <code>usuario</code>
	 * @throws ServiceException caso ocorra algum problema durante a operação
	 */
	public int pesquisarPainelControle(TipoFaseComunicacao faseComunicacao, Setor setor, Usuario usuario) throws ServiceException;

	List<ComunicacaoDocumentoResult> pesquisarDocumentosElaborados(
			String siglaClasse, Long numeroProcesso, Long codigoFase,
			Long idSetorAtual, Long numeracaoUnica, Long anoNumeracaoUnica,
			String dataInicial, String dataFinal) throws ServiceException;

	public List<ComunicacaoDocumentoResult> pesquisarDocumentosElaborados(
			String siglaClasse, Long numeroProcesso, Long codigoFase,
			Long idSetorAtual, Long idSetorFase, Long numeracaoUnica,
			Long anoNumeracaoUnica, String dataInicial, String dataFinal)
			throws ServiceException;			

	
		public List<ComunicacaoDocumentoResult> pesquisarDocumentosElaboradosSigilosos(
			String siglaClasse, Long numeroProcesso, Long codigoFase,
			Long idSetorAtual, Long idSetorFase, Long numeracaoUnica,
			Long anoNumeracaoUnica, String dataInicial, String dataFinal)
			throws ServiceException;	
	
	public List<ComunicacaoDocumentoResult> pesquisarDocumentosUnidade(Date dataPesquisa, Long idSetor) throws ServiceException;;

	public List pesquisarDocumentos(Long idObjetoIncidente, Long codigoModelo, Setor setor, String usuario, Boolean buscaSomenteDocumentoFaseGerados)
			throws ServiceException;

	public ComunicacaoDocumentoPaginatorResult pesquisarDocumentosAssinatura(FiltroPesquisarDocumentosAssinatura filtro) throws ServiceException;

	public ComunicacaoIncidente salvaArquivoEletronico(Long idModelo, Long codigoMinistro, ObjetoIncidente<?> objetoIncidente, byte[] rtf, String tipoArquivo)
			throws ServiceException;

	public Comunicacao recuperarPorId(Long idComunicacao) throws ServiceException;

	List<ComunicacaoDocumentoResult> pesquisarDocumentosAssinatura(Setor setor, List<Long> listaTipoSituacaoDocomento, Long faseDocumento, List<Long> ids)
			throws ServiceException;
			
	public List pesquisarDocumentosAssinadosPorPeriodo(Long codigoSetor, String usuario, String dataInicial, String dataFinal) throws ServiceException;

	public int pesquisarDocumentosCount(Setor setor, Boolean buscaSomenteDocumentoFaseGerados) throws ServiceException;

	public int pesquisarDocumentosCorrecaoCount(Setor setor, String username) throws ServiceException;

	List<ComunicacaoDocumentoResult> pesquisarDocumentosCorrecao(Setor setor,String usuario) throws ServiceException;

	List<ComunicacaoDocumentoResult> pesquisarDocumentosAssinatura(Setor setor, List<Long> listaTipoSituacaoDocumento, Long faseDocumento) throws ServiceException;

	public List<Comunicacao> comunicacoesDoPeriodo(ComunicacaoSearch search) throws ServiceException;

	List pesquisarProcessosPorDocumento(Long tipoComunicacao, Long numeroDocumento, Long anoDocumento) throws ServiceException;

	public Comunicacao obterUltimaComunicacao(ObjetoIncidente objetoIncidente, Pessoa pessoa, ModeloComunicacao modeloComunicacao) throws ServiceException;

	void notificarOrgaosIntegrados(ObjetoIncidente objetosIncidente, ModeloComunicacao modeloComunicacao, Setor setor, Usuario usuario, AndamentoProcesso andamentoProcesso) throws ServiceException;

	public void atualizarSituacaoPecaProcessual(Long idComunicacao) throws ServiceException;

}
