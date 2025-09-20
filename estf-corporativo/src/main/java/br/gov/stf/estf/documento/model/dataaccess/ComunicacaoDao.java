package br.gov.stf.estf.documento.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.util.ComunicacaoSearch;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ComunicacaoDao extends GenericDao<Comunicacao, Long> {

	public int pesquisarPainelControle(Long idFaseComunicacao, Long idSetor, String usuarioCriacao) throws DaoException;

	public List<Comunicacao> pesquisarDocumentosElaborados(String siglaClasse,
			Long numeroProcesso, Long codigoFase, Long idSetorAtual,
			Long idSetorFase, Long numeracaoUnica, Long anoNumeracaoUnica,
			String dataInicial, String dataFinal) throws DaoException;

	public List<Comunicacao> pesquisarDocumentosElaboradosSigilosos(String siglaClasse,
			Long numeroProcesso, Long codigoFase, Long idSetorAtual,
			Long idSetorFase, Long numeracaoUnica, Long anoNumeracaoUnica,
			String dataInicial, String dataFinal) throws DaoException;
	
	public List pesquisarDocumentosUnidade(Date dataPesquisa, Long idSetor) throws DaoException;

	public List pesquisarDocumentos(Long idObjetoIncidente, Long codigoModelo, Setor setor, String usuario, Boolean buscaSomenteDocumentoFaseGerados)
			throws DaoException;

	public Comunicacao recuperarPorId(Long idComunicacao) throws DaoException;

	public List pesquisarDocumentosAssinatura(FiltroPesquisarDocumentosAssinatura filtroPesquisarDocumentosAssinatura) throws DaoException;

	public List pesquisarDocumentosAssinadosPorPeriodo(Long codigoSetor, String usuario, String dataInicial, String dataFinal) throws DaoException;

	public int pesquisarDocumentosCount(Setor setor, Boolean buscaSomenteDocumentoFaseGerados) throws DaoException;

	public int pesquisarDocumentosCorrecaoCount(Setor setor, String username)  throws DaoException;

	public List pesquisarDocumentosCorrecao(Setor setor, String usuario) throws DaoException;

	int pesquisarDocumentosAssinaturaCount(FiltroPesquisarDocumentosAssinatura filtro) throws DaoException;

	List<Comunicacao> comunicacoesDoPeriodo(ComunicacaoSearch search) throws DaoException;

	List pesquisarProcessosPorDocumento(Long tipoComunicacao, Long numeroDocumento, Long anoDocumento) throws DaoException;

	public Comunicacao obterUltimaComunicacao(ObjetoIncidente objetoIncidente, Pessoa pessoa, ModeloComunicacao modeloComunicacao) throws DaoException;
	
	public void atualizarSituacaoPecaProcessual(Long idComunicacao)  throws DaoException;

}
