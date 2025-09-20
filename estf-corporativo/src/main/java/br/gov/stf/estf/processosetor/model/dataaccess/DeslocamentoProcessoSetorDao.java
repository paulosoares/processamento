package br.gov.stf.estf.processosetor.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamento;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamentoPeticao;
import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DeslocamentoProcessoSetorDao extends GenericDao<HistoricoDeslocamento, Long> {

	public HistoricoDeslocamento recuperarDeslocamento(Long id)
	throws DaoException;

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, ProcessoSetor processoSetor,
			PeticaoSetor peticaoSetor, Date dataRemessa, String numeroSala, String numeroEstante, 
			String numeroPrateleira, String numeroArmario, String numeroColuna) throws DaoException;

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, ProcessoSetor processoSetor,
			Date dataRecebimento,Secao secaoOrigem,Date dataRemessa,Secao secaoDestino, 
			String numeroSala, String numeroEstante, String numeroPrateleira,
			String numeroArmario,String numeroColuna, Boolean localizadoSetor) throws DaoException;

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, String siglaClasseProcessual,
			Date dataRecebimento,Secao secaoOrigem,Date dataRemessa,Secao secaoDestino, 
			String numeroSala, String numeroEstante, String numeroPrateleira,
			String numeroArmario,String numeroColuna,Boolean semLocalizacao,Date dataInicio,Date dataFim,
			Boolean emTramite) throws DaoException;
	
	public Boolean persistirHistoricoDeslocamento(HistoricoDeslocamento historicoDeslocamento) 
	throws DaoException;
	
	public Boolean persistirHistoricoDeslocamentoPeticao(HistoricoDeslocamentoPeticao historicoDeslocamento) 
	throws DaoException;
	
	public Boolean excluirHistoricoDeslocamento(HistoricoDeslocamento deslocamento) 
	throws DaoException;
	
	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long idSetor, 
            Secao secaoOrigem, Secao secaoDestino)
    throws DaoException;
}
