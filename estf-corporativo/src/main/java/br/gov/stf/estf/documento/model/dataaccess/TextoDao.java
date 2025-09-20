package br.gov.stf.estf.documento.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.util.DadosDoTextoDynamicRestriction;
import br.gov.stf.estf.documento.model.util.TextoDynamicQuery;
import br.gov.stf.estf.documento.model.util.TextoSearchData;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.util.SearchResult;

public interface TextoDao extends GenericDao<Texto, Long> {
	public List<Texto> pesquisar(String sigla, Long numero, Long codigoRecurso, String tipoJulgamento, Long codigoMinistro)
			throws DaoException;

	public List<Texto> pesquisar(String sigla, Long numero, Long codRecurso, String julgamento, TipoTexto tipoTexto,
			Long codigoMinistro, Boolean orderAscDataSessao) throws DaoException;

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws DaoException;

	public Texto recuperar(String sigla, Long numero, Long codRecurso, String julgamento, TipoTexto tipoTexto)
			throws DaoException;

	public List<Texto> pesquisarDecisoesAta(String sigla, Long numero, Long codRecurso, String julgamento, Date dataAta)
			throws DaoException;

	public SearchResult<Texto> pesquisar(TextoSearchData sd) throws DaoException;

	public List<Texto> pesquisarTextosIguais(Texto texto) throws DaoException;

	public List<Texto> pesquisarTextosIguais(Long seqArquivoEletronico) throws DaoException;

	public List<FaseTextoProcesso> recuperarVersoesTexto(Long textoId) throws DaoException;

	public List<Texto> pesquisarTextoDoProcesso(DadosDoTextoDynamicRestriction consultaDinamica) throws DaoException;

	public List<Texto> recuperarTextosProcessos(List<Processo> listaProcessos, List<TipoTexto> listaTipoTexto)
			throws DaoException;;

	public void copiarConteudoArquivoEletronico(ArquivoEletronico arquivoEletronicoOrigem,
			ArquivoEletronico arquivoEletronicoDestino) throws DaoException;

	public List<Texto> pesquisarTextosExtratoAta(String siglaClasse, Long numProcesso, Long tipoRecurso, Long tipoJulgamento)
			throws DaoException;

	public Texto recuperarDecisaoAta(ObjetoIncidente<?> objetoIncidente, Date dataSessao) throws DaoException;

	public List<Texto> pesquisarTextosPublicacao(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto,
			Boolean orderAscDataSessao, Date dataSessao) throws DaoException;

	public Texto recuperarTextoPublicacao(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto) throws DaoException;

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Boolean orderAscDataSessao)
			throws DaoException;

	public List<Texto> pesquisarDecisoesAta(ObjetoIncidente<?> objetoIncidente, Date dataAta) throws DaoException;

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, Long codigoMinistro, Boolean orderAscDataSessao)
			throws DaoException;

	public Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto) throws DaoException;

	public Texto recuperarTextoEletronico(Integer idArquivoEletronico) throws DaoException;

	public Texto recuperar(Long idObjetoIncidente, TipoTexto tipoTexto, Long idMinistro) throws DaoException;

	public List<Texto> pesquisarTextosExtratoAta(TextoDynamicQuery consultaDinamica) throws DaoException;

	List<Texto> pesquisarTexto(TextoDynamicQuery consultaDinamica) throws DaoException;

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto... tipoTexto) throws DaoException;

	List<Texto> pesquisarTextosIguais(Texto texto, boolean limitarTextosDoMinistro) throws DaoException;

	public Texto recuperaArquivoEletronico(ControleVoto cv) throws DaoException;
	
	public Texto recarregar(Texto texto) throws DaoException;
	
	public Texto recuperarTextoEmentaSobreRepercussaoGeral(Long seqObjetoIncidente, Long codigoMinistro) throws DaoException;
	
	public Texto recuperarTextoDecisaoSobreRepercussaoGeral(Long seqObjetoIncidente, Long codigoMinistro) throws DaoException;

	public List<Texto> pesquisarTextoRepercussaoGeralVotoValido(Long seqObjetoIncidente) throws DaoException;
	
	public Texto recuperar(Long seqObjetoIncidente, Long codigoMinistro, TipoTexto tipoTexto, Date dataFim, Long seqVoto) throws DaoException;
	
	public Texto recuperar(Long seqObjetoIncidente, String tipoJulgamento, TipoTexto tipoTexto, Long codigoMinistro) throws DaoException;

	public Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Ministro ministro,
			Boolean textoPublico, Boolean dataSessaoPreenchida) throws DaoException;

	List<Texto> recuperarListaTextoEletronico(Integer idArquivoEletronico) throws DaoException;
	
	public boolean verificarExistenciaTexto(TextoSearchData sd) throws DaoException;

	public SearchResult<Texto> pesquisarTextoPorObjIncidenteTipoTextoMinistroOuPresidente(TextoSearchData sd) throws DaoException;

	public Texto recuperarTextoSemControleVoto(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws DaoException;
	
	public List<Texto> recuperarTextosReferendo(Long idObjetoIncidente) throws DaoException;

	void refresh(Texto entity) throws DaoException;

	List<Texto> recuperarTextosPorFaseTipoEspecificos(Long seqObjetoIncidente, List<Long> tipoTextos, FaseTexto fase) throws DaoException;
}
