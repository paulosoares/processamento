package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.util.DeslocaProcessoDynamicQuery;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DeslocaProcessoDao extends GenericDao<DeslocaProcesso, DeslocaProcessoId> {
	public Boolean deslocarProcesso(Processo processo) throws DaoException;

	public List<DeslocaProcesso> consultaDeslocamentoOrdenadoGuiaDecrescente(String classeDoProcesso, Long numeroDoProcesso)
			throws DaoException;

	public void persistirDeslocamentoProcesso(DeslocaProcesso deslocamentoProcesso) throws DaoException;
	
	List<DeslocaProcesso> pesquisar(DeslocaProcessoDynamicQuery consultaDinamica) throws DaoException;

	DeslocaProcesso recuperarUltimoDeslocamento(DeslocaProcessoDynamicQuery consultaDinamica) throws DaoException;
	
	public void receberProcesso(DeslocaProcesso deslocamentoProcesso) throws DaoException;
	
/*	public List<DeslocaProcesso> recuperarStatusDeslocamentoProcesso(Guia guia) throws DaoException;
*/
	public List<DeslocaProcesso> recuperarDeslocamentoProcessos(Guia guia) throws DaoException;
	
	
	public Long pesquisarSetorUltimoDeslocamento(Processo processo) throws DaoException;
	
	public void removerProcesso(DeslocaProcesso processo) throws DaoException;
	
	public List<DeslocaProcesso> pesquisarDataRecebimentoGuiaProcesso(Guia guia) throws DaoException;
	
	List<DeslocaProcesso> recuperarProcessosPeloSetor(Long codigoSetor) throws DaoException;
	
	public DeslocaProcesso recuperarUltimoDeslocamentoProcesso(Processo processo) throws DaoException;
	
	void deslocaProcessoSetor(Long codigoOrgaoOrigem, Long codigoOrgaoDestino, String tipoOrgaoOrigem, String tipoOrgaoDestino, Long seqObjetoIncidente) throws DaoException;

	List<DeslocaProcesso> recuperarDeslocamentoProcessosRecebimentoExterno(Guia guia) throws DaoException;
	
	public void deslocarProcesso(Processo processo, Long codigoOrgaoOrigem,
			Long codigoOrgaoDestino, Integer tipoOrgaoOrigem,
			Integer tipoOrgaoDestino) throws DaoException;

	DeslocaProcesso recuperaDeslocamentoProcessoRecebimento(DeslocaProcesso deslocaProcesso, Long setorUsuario) throws DaoException;
	
	public List<DeslocaProcesso> recuperaPorProcessoOrigemExterna(Processo processo) throws DaoException;
	
	public void alterarPkDeslocaProcesso(DeslocaProcesso deslocaProcesso, Guia novaGuia,AndamentoProcesso andamentoProcesso, Integer numSequencia) throws DaoException;
	
	public boolean isBaixadoParaOrigem(Processo processo, Origem origem, Andamento andamento) throws DaoException;

	public Integer recuperarUltimaSequencia(Guia guia) throws DaoException;
	
	public DeslocaProcesso recuperarUltimaRemessaProcesso(String siglaProcesso, Long numeroProcesso) throws DaoException;

	Long pesquisarSetorUltimoDeslocamento(Long seqObjetoIncidente) throws DaoException;

	public void atualizaAndamento(DeslocaProcesso deslocaProcesso, Long idAndamentoProcesso) throws DaoException;

	public void atualizarDeslocamento(Long idDeslocaProcesso, Long idProcesso) throws DaoException;

}
