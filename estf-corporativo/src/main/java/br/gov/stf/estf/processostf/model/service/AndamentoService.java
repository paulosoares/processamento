package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AndamentoService extends
		GenericService<Andamento, Long, AndamentoDao> {
	public List<Andamento> pesquisar(Long codigoSetor) throws ServiceException;

	public List<Andamento> pesquisarTipoAndamento(Long id, String descricao,
			Boolean ativo) throws ServiceException;

	public Andamento recuperarTipoAndamento(Long id, String descricao)
			throws ServiceException;
	
	/**
	 * Pesquisar os andamentos autorizados para aquele setor e obedecendo às seguintes regras:
	 * 
	 * 1. não retornar os andamentos de Interposição, Distribuição, Redistribuição e Registro;
	 * 2. não mostrar um andamento de processo findo se o mesmo já tiver sido lançado anteriormente no processo;
	 * 3. não mostrar os andamentos 7600 e 7601;
	 * 4. não mostrar andamento de lançamento indevido 7700;
	 * 5. se a classe processual não for ARE, não mostrar os andamentos 6249 e 6247 pois eles são exclusivos dessa classe.
	 */
	public List<Andamento> pesquisarAndamentosAutorizados(Long codigoSetor, Processo processo) throws ServiceException;

	public List<Andamento> pesquisarAndamentosAutorizadosParaLote(Setor setor) throws ServiceException;

	public boolean podeLancarAndamentoIndevido(Long codigoSetor, Andamento andamento, VerificadorPerfilService verificadorPerfilService) throws ServiceException;
	
	public boolean podeLancarAndamentoIndevido(Long codigoSetor, Andamento andamento, VerificadorPerfilService verificadorPerfilService, Processo processo) throws ServiceException;
	
	public boolean isGrupoDecisao(Andamento andamento) throws ServiceException;
	
	public boolean isNotificadoTribunalOrigem(Long seqAndamentoProcesso, Long codAndamento) throws ServiceException;
}
