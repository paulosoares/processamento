package br.gov.stf.estf.jurisdicionado.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.EmprestimoAutosProcesso;
import br.gov.stf.estf.entidade.jurisdicionado.util.EmprestimoAutosResult;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.EmprestimoAutosProcessoDao;
import br.gov.stf.estf.jurisdicionado.model.exception.JurisdicionadoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface EmprestimoAutosProcessoService extends GenericService<EmprestimoAutosProcesso, Long, EmprestimoAutosProcessoDao> {
	
	List<EmprestimoAutosResult> pesquisarAutos(String nomeJurisdicionado, Long objetoIncidente, Date dataInicial,
			Date dataFinal, Long idSituacao) throws ServiceException;

	public Boolean existeEmprestimoParaAssociacao(AssociacaoJurisdicionado associacao) throws ServiceException;
	
	void excluirCarga(EmprestimoAutosResult emp, Setor setorAutenticado, 
			Usuario usu) throws ServiceException, SQLException, JurisdicionadoException;
	
	void salvarCobranca(EmprestimoAutosResult emp, Setor setorAutenticado, Usuario usu,
			String observacao) throws ServiceException, SQLException;
	
	void excluirCobranca(EmprestimoAutosResult emp, Setor setorAutenticado, Usuario usu) 
		throws ServiceException, JurisdicionadoException;

	public GuiaId salvarCarga(EmprestimoAutosProcesso emprestimo,
			  Guia guia,
			  ArrayList<Long> listaSeqObjetosIncidentes, 
			  List<AssociacaoJurisdicionado> associacaoParaIncluir, 
			  List<AssociacaoJurisdicionado> associacaoParaExcluir,
			  Usuario usuario) throws ServiceException;
	
	public Guia salvarRecebimento(ArrayList<Long> listaSeqObjetosIncidentes, 
            Usuario usuario, String observacao, boolean advogado) throws ServiceException;
	
	public EmprestimoAutosProcesso recuperarEmprestimoPorDeslocamento(DeslocaProcesso deslocaProcesso) throws ServiceException;
	
	public Guia salvarRecebimentoPeticoes(ArrayList<Long> listaSeqObjetosIncidentes, Usuario usuario) throws ServiceException;
	
	public void salvarVinculo(List<AssociacaoJurisdicionado> associacaoParaIncluir, List<AssociacaoJurisdicionado> associacaoParaExcluir) throws ServiceException;

	public String getNomeAdvogadoOuAutorizado(Guia guia) throws ServiceException;

	public Boolean existeEmprestimoNaGuiaDeAutos(Guia guia) throws ServiceException;
	
	public Boolean existeEmprestimoParaObjetoIncidente(Long idObjetoIncidente, boolean devolucao) throws ServiceException;

	public List<Guia> salvarRecebimentoDeVariasOrigens(List<DeslocaProcesso> deslocaProcessos, Usuario usuario, Boolean isAdvogado,  Map<Long,String> observacoes) throws ServiceException;
}
