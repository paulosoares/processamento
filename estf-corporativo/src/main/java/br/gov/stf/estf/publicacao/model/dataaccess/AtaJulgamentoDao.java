package br.gov.stf.estf.publicacao.model.dataaccess;

import java.util.Date;

import br.gov.stf.estf.entidade.publicacao.AtaJulgamento;
import br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * Interface de acesso aos dados de Ata de Julgamento.
 * 
 * @author Rodrigo Barreiros
 * @since 08.06.2009
 */
public interface AtaJulgamentoDao extends GenericDao <AtaJulgamento, AtaJulgamento.AtaJulgamentoId> {
	
    /* (non-Javadoc)
     * @see br.gov.stf.estf.publicacao.model.service.AtaJulgamentoService#recuperarPor(java.util.Date, int, br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta)
     */
	public AtaJulgamento recuperarPor(Date dataSessao, int colegiado, CategoriaAta categoria);

}
