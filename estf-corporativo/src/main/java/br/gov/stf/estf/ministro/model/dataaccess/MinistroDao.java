package br.gov.stf.estf.ministro.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.usuario.model.util.TipoTurma;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface MinistroDao extends GenericDao<Ministro, Long> {
	public Ministro recuperarMinistro(Setor setor) throws DaoException;

	public List<Ministro> pesquisarMinistros(Boolean ativo, Boolean incluirGabinetePresidencia, MinistroPresidente ministroPresidente, Boolean primeiraTurma,
			Boolean segundaTurma, Boolean sessaoPlenaria) throws DaoException;

	public Ministro recuperarMinistro(String nome, Long id) throws DaoException;

	public Ministro recuperarPresidente(Boolean incluirGabinetePresidencia, Boolean primeiraTurma, Boolean segundaTurma, Boolean sessaoPlenaria)
			throws DaoException;

	public List<Ministro> pesquisarMinistrosAtivos() throws DaoException;

	public List<Ministro> pesquisarMinistros(String nomeMinistro, Boolean ativo) throws DaoException;

	/**
	 * metodo responsavel pór pesquisar o ministros
	 * 
	 * @param codigoMinistro
	 *            codigo do ministro
	 * @param sigla
	 *            sigla do ministro
	 * @param nome
	 *            nome do ministro
	 * @param codigoSetor
	 *            codigo do setor do ministro
	 * @param tipoTurma
	 *            turma do ministro
	 * @param ativo
	 *            ministro ativo
	 * @param semMinistroPresidente
	 *            não retorna o setor PRESIDENTE codigo do setor 600000179 mas o ministro presidente retorna normalmente
	 * @return
	 * @throws DaoException
	 */
	public List<Ministro> pesquisarMinistro(Long codigoMinistro, String sigla, String nome, Long codigoSetor, TipoTurma tipoTurma, Boolean ativo,
			Boolean semMinistroPresidente) throws DaoException;

	public Ministro recuperarMinistroRelator(ObjetoIncidente objetoIncidente) throws DaoException;

	public Ministro recuperarRelatorAcordao(ObjetoIncidente objetoIncidente) throws DaoException;

	public Ministro recuperarMinistroRelatorIncidente(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public Ministro recuperarMinistroRelatorIncidenteDataJulgamento(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public Ministro recuperarMinistroRevisorIncidente(Long idObjetoIncidente) throws DaoException;

	public Ministro recuperarMinistroPresidente(Date data) throws DaoException;

	Ministro recuperarRedatorAcordao(ObjetoIncidente objetoIncidente) throws DaoException;

}
