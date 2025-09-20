package br.gov.stf.estf.repercussaogeral.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.ministro.AfastamentoMinistroView;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.repercussaogeral.model.util.RepercussaoGeralSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.model.entity.BaseEntity;
import br.gov.stf.framework.util.SearchResult;

@SuppressWarnings("unchecked")
public interface RepercussaoGeralDao extends GenericDao<BaseEntity, Long> {

	public SearchResult pesquisarRepercussaoGeral(
			RepercussaoGeralSearchData searchData) throws DaoException;

	public SearchResult pesquisarRepercussaoGeralSQL(
			RepercussaoGeralSearchData searchData, String ordem)
			throws DaoException;

	public SearchResult pesquisarRepercussaoGeralPlenarioVirtual(
			RepercussaoGeralSearchData searchData) throws DaoException;

	public List<JulgamentoProcesso> pesquisarRepercussaoGeralFinalizadosSemAndamento()
			throws DaoException;

	public List<JulgamentoProcesso> pesquisarRepercussaoGeralFinalizadoTeste(
			Long idObjetoIncidente) throws DaoException;

	public List<Ministro> recuperarMinistrosNaoManifesto(Long idObjetoIncidente, Long idSessao)
			throws DaoException;

	public List<AfastamentoMinistroView> recuperarAfastamentoMinistro(
			List<Ministro> ministros, Date datInicio) throws DaoException;

	public List<JulgamentoProcesso> pesquisarRepercussaoGeralRecesso(
			Date dataInicioRecesso, Date dataFimRecesso) throws DaoException;

	public List<JulgamentoProcesso> processoPendentesMinistroNotificacao(
			Long idMinistro) throws DaoException;

	public List<Ministro> recuperarMinistroSemManifestacaoComTexto(
			Long idIncidenteJulgamento) throws DaoException;

	public List<JulgamentoProcesso> recuperarJulgamentoSemManifestacaoComTexto()
			throws DaoException;

	public List<Usuario> buscaUsuariosNotificacao(String dsc_perfil)
			throws DaoException;

	public Long buscarMaxNumeroTema() throws DaoException;

	public String pesquisarDecisaoRGPackage(Long idObjetoIncidente)
			throws DaoException;
	
	public Long pesquisarManifestacaoRGPackage(String siglaClasse, Long numeroProcesso,  Long objetoIncidente) throws DaoException;

}
