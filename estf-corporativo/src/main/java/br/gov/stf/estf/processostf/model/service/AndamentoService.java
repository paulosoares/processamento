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
	 * Pesquisar os andamentos autorizados para aquele setor e obedecendo �s seguintes regras:
	 * 
	 * 1. n�o retornar os andamentos de Interposi��o, Distribui��o, Redistribui��o e Registro;
	 * 2. n�o mostrar um andamento de processo findo se o mesmo j� tiver sido lan�ado anteriormente no processo;
	 * 3. n�o mostrar os andamentos 7600 e 7601;
	 * 4. n�o mostrar andamento de lan�amento indevido 7700;
	 * 5. se a classe processual n�o for ARE, n�o mostrar os andamentos 6249 e 6247 pois eles s�o exclusivos dessa classe.
	 */
	public List<Andamento> pesquisarAndamentosAutorizados(Long codigoSetor, Processo processo) throws ServiceException;

	public List<Andamento> pesquisarAndamentosAutorizadosParaLote(Setor setor) throws ServiceException;

	public boolean podeLancarAndamentoIndevido(Long codigoSetor, Andamento andamento, VerificadorPerfilService verificadorPerfilService) throws ServiceException;
	
	public boolean podeLancarAndamentoIndevido(Long codigoSetor, Andamento andamento, VerificadorPerfilService verificadorPerfilService, Processo processo) throws ServiceException;
	
	public boolean isGrupoDecisao(Andamento andamento) throws ServiceException;
	
	public boolean isNotificadoTribunalOrigem(Long seqAndamentoProcesso, Long codAndamento) throws ServiceException;
}
