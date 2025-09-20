package br.gov.stf.estf.publicacao.model.service.impl;

import java.util.Date;

import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.publicacao.AtaJulgamento;
import br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta;
import br.gov.stf.estf.publicacao.model.dataaccess.AtaJulgamentoDao;
import br.gov.stf.estf.publicacao.model.service.AtaJulgamentoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * Implementação para service <code>AtaJulgamentoService</code.
 * 
 * @author Rodrigo Barreiros
 * @since 08.06.2009
 */
@Component
public class AtaJulgamentoServiceImpl extends GenericServiceImpl<AtaJulgamento, AtaJulgamento.AtaJulgamentoId, AtaJulgamentoDao> implements AtaJulgamentoService {

    public AtaJulgamentoServiceImpl(AtaJulgamentoDao dao) { 
    	super(dao); 
    }
    
    /* (non-Javadoc)
     * @see br.gov.stf.estf.publicacao.model.service.AtaJulgamentoService#recuperarPor(java.util.Date, int, br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta)
     */
    @Override
    public AtaJulgamento recuperarPor(Date dataSessao, int colegiado, CategoriaAta categoria) {
    	return dao.recuperarPor(dataSessao, colegiado, categoria);
    }

}
