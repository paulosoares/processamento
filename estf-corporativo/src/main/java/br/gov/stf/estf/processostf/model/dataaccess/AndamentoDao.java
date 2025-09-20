package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.model.service.ServiceException;

public interface AndamentoDao extends GenericDao<Andamento, Long> {
	public List<Andamento> pesquisar (Long codigoSetor) throws DaoException;
	
	public List<Andamento> pesquisarTipoAndamento(Long id, String descricao, Boolean ativo) throws DaoException;

	public Andamento recuperarTipoAndamento (Long codAndamento, String descricao ) throws DaoException;	

	/**
	 * Pesquisar os andamentos autorizados para aquele setor e obedecendo às seguintes regras:
	 * 
	 * 1. não retornar os andamentos de Interposição, Distribuição, Redistribuição e Registro;
	 * 2. não mostrar um andamento de processo findo se o mesmo já tiver sido lançado anteriormente no processo;
	 * 3. não mostrar os andamentos 7600 e 7601;
	 * 4. não mostrar andamento de lançamento indevido 7700;
	 * 5. se a classe processual não for ARE, não mostrar os andamentos 6249 e 6247 pois eles são exclusivos dessa classe.
	 * 
	 * @param codigoSetor
	 * @return
	 */
	public List<Andamento> pesquisarAndamentosAutorizados(Long codigoSetor) throws DaoException;

	public boolean podeLancarAndamentoIndevido(Long codigoSetor) throws DaoException;
    
	public boolean isGrupoDecisao(Andamento andamento) throws DaoException;
	
	public boolean isNotificadoTribunalOrigem(Long seqAndamentoProcesso, Long codAndamento) throws ServiceException;
	
	public List<Andamento> pesquisarAndamentosAutorizadosParaLote(Setor setor) throws DaoException;
}
