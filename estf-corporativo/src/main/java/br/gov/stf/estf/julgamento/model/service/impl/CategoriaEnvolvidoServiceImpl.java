package br.gov.stf.estf.julgamento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.CategoriaEnvolvido;
import br.gov.stf.estf.julgamento.model.dataaccess.CategoriaEnvolvidoDao;
import br.gov.stf.estf.julgamento.model.service.CategoriaEnvolvidoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("categoriaEnvolvidoService")
public class CategoriaEnvolvidoServiceImpl extends GenericServiceImpl<CategoriaEnvolvido, String, CategoriaEnvolvidoDao> implements CategoriaEnvolvidoService {
    public CategoriaEnvolvidoServiceImpl(CategoriaEnvolvidoDao dao) { super(dao); } 
	
}
