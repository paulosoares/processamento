package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseConversaoDao;
import br.gov.stf.estf.processostf.model.service.ClasseConversaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("classeConversaoService")
public class ClasseConversaoServiceImpl extends GenericServiceImpl<ClasseConversao, String, ClasseConversaoDao> 
	implements ClasseConversaoService {
    public ClasseConversaoServiceImpl(ClasseConversaoDao dao) { super(dao); }
    
    public List pesquisarClasseAntiga() throws ServiceException {

        List classes = null;

        try {

            classes = dao.pesquisarClasseAntiga();

        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        return classes;
    }

}
