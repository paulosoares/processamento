package br.gov.stf.estf.jurisdicionado.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.EmprestimoAutosProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface EmprestimoAutosProcessoDao extends GenericDao<EmprestimoAutosProcesso, Long> {
	
	List<EmprestimoAutosProcesso> pesquisarAutos(String nomeDestinatario, Long objetoIncidente, Date dataInicial,
			Date dataFinal, Long idSituacao) throws DaoException;
	
	public Boolean existeEmprestimoParaAssociacao(AssociacaoJurisdicionado associacao) throws DaoException;
	
	public EmprestimoAutosProcesso recuperarEmprestimoPorDeslocamento(DeslocaProcesso deslocaProcesso) throws DaoException;
	public Boolean existeEmprestimoNaGuiaDeAutos(Guia guia) throws DaoException;
	public String getNomeAdvogadoOuAutorizado(Guia guia, boolean existeEmprestimo) throws DaoException;
	public Boolean existeEmprestimoParaObjetoIncidente(Long idObjetoIncidente, boolean devolucao) throws DaoException;

}
