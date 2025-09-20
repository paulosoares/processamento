package br.gov.stf.estf.jurisdicionado.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoAssociacao;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.AssociacaoJurisdicionadoDao;
import br.gov.stf.estf.jurisdicionado.model.service.AssociacaoJurisdicionadoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("associacaoJurisdicionadoService")
public class AssociacaoJurisdicionadoServiceImpl extends GenericServiceImpl<AssociacaoJurisdicionado, Long, AssociacaoJurisdicionadoDao> 
			implements AssociacaoJurisdicionadoService {

	public AssociacaoJurisdicionadoServiceImpl(AssociacaoJurisdicionadoDao dao){
		super(dao);
	}

	@Override
	public AssociacaoJurisdicionado recuperarPorGrupoMembro(PapelJurisdicionado grupo, PapelJurisdicionado membro, EnumTipoAssociacao tipoAssociacao) throws ServiceException {
		try {
			return dao.recuperarPorGrupoMembro(grupo, membro, tipoAssociacao);
		} catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public List<AssociacaoJurisdicionado> pesquisarAssociacoes(Jurisdicionado jurisdicionado) throws ServiceException {
		try {
			dao.limparSessao();
			return dao.pesquisarAssociacoes(jurisdicionado);
		} catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<AssociacaoJurisdicionado> pesquisarAssociacoesMembro(Jurisdicionado jurisdicionado) throws ServiceException {
		try {
			return dao.pesquisarAssociacoesMembro(jurisdicionado);
		} catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
	}

}
