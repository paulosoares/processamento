package br.gov.stf.estf.intimacao.model.dataaccess;


import java.util.List;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface AndamentoProcessoComunicacaoLocalDao {

	  /**
     * Salva o andamento associado a comunicação
     *
     * @param andamentoProcessoComunicacao
     * @return
     * @throws DaoException 
     */
	AndamentoProcessoComunicacao salvar(AndamentoProcessoComunicacao andamentoProcessoComunicacao) throws DaoException;
	
	List<AndamentoProcesso> pesquisarAndamentoProcesso(String sigla, Long numero) throws DaoException;
	
	List<AndamentoProcesso> pesquisarAndamentosProcessoIncidente(Long idProcessoIncidente) throws DaoException;
	
	AndamentoProcessoComunicacao recuperarAndamentoProcessoGeradoPelaComunicacao(Long idcomunicacao, Long idCodigoAndamento) throws DaoException;
}
