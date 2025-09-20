package br.gov.stf.estf.documento.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.util.ControleVotoDto;
import br.gov.stf.estf.documento.model.util.ControleVotoDynamicQuery;
import br.gov.stf.estf.documento.model.util.ControleVotoDynamicRestriction;
import br.gov.stf.estf.documento.model.util.IConsultaDeControleDeVotoInteiroTeor;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ControleVotoDao extends GenericDao<ControleVoto, Long> {

	public List<ControleVoto> pesquisarControleVoto(Long sequencialObjetoIncidente, Long idMinistro, Date dataSessao, TipoTexto tipoTexto, Long idTexto)
			throws DaoException;

	public List<Object[]> pesquisar(ControleVotoDto controleVotoDto) throws DaoException;

	List<ControleVoto> pesquisarControleVoto(ControleVotoDynamicRestriction consulta) throws DaoException;

	public List<ControleVoto> pesquisarControleVoto(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter, boolean pesquisarObjetosDoProcesso,
			boolean filtrarPeloSetorComposicaoAcordao) throws DaoException;

	List<ControleVoto> pesquisarControleVoto2(ControleVotoDynamicQuery consultaDinamica) throws DaoException;

	public List<ControleVoto> pesquisarControleVoto(ObjetoIncidente<?> objetoIncidente, Ministro ministro, Date dataSessao) throws DaoException;

	@SuppressWarnings("rawtypes")
	public Long recuperarUltimaSequenciaVoto(ObjetoIncidente objetoIncidente) throws DaoException;

	@SuppressWarnings("rawtypes")
	public ControleVoto recuperar(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws DaoException;

	@SuppressWarnings("rawtypes")
	public List<Long> pesquisarControleVotoPDFNaoAssinado(ObjetoIncidente objetoIncidente) throws DaoException;

	void sincronizaControleVotoComTexto(Long seqTexto, Long seqVotos, Date dataSessao) throws DaoException;

	ControleVoto recuperarControleVoto(Long idControleVoto) throws DaoException;

	ControleVoto recuperarControleDeVotoSemSessao(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws DaoException;

	public List<ControleVoto> pesquisarControleVotoPorTipoTexto(ObjetoIncidente<?> oi,	TipoTexto... tiposTextos) throws DaoException;

	public List<ControleVoto> pesquisarControleVotoPorMinistro(ObjetoIncidente<?> oi, Ministro ministro) throws DaoException;
	
	public Boolean existemCompletos() throws DaoException;
	
	public List<Object> verificarSigilosos() throws DaoException;

	public Boolean existemSigilosos() throws DaoException;
}
