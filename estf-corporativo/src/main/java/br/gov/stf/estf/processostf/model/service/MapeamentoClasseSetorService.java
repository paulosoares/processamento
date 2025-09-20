package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.MapeamentoClasseSetor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.localizacao.model.service.exception.NaoExisteSetorParaDeslocamentoException;
import br.gov.stf.estf.processostf.model.dataaccess.MapeamentoClasseSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface MapeamentoClasseSetorService extends GenericService<MapeamentoClasseSetor, Long, MapeamentoClasseSetorDao> {

	public abstract Setor recuperarSetorDeDestinoDoDeslocamento(ObjetoIncidente<?> objetoIncidente) throws ServiceException,
			NaoExisteSetorParaDeslocamentoException;
	
	public List<String> buscaClasseDoSetor(Setor setorDoUsuario) throws ServiceException;

}