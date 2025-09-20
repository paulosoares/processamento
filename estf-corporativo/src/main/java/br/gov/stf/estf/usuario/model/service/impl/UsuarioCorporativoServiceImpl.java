package br.gov.stf.estf.usuario.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.UsuarioCorporativo;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioCorporativoDao;
import br.gov.stf.estf.usuario.model.service.UsuarioCorporativoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("usuarioCorporativoService")
public class UsuarioCorporativoServiceImpl extends	GenericServiceImpl<UsuarioCorporativo, Integer, UsuarioCorporativoDao>
		implements UsuarioCorporativoService {

	@Autowired
	public UsuarioCorporativoServiceImpl(UsuarioCorporativoDao dao) {
		super(dao);
	}

}