package br.gov.stf.estf.documento.model.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.PermissaoDeslocamentoDao;
import br.gov.stf.estf.documento.model.service.PermissaoDeslocamentoService;
import br.gov.stf.estf.entidade.documento.PermissaoDeslocamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("permissaoDeslocamentoService")
public class PermissaoDeslocamentoServiceImpl extends GenericServiceImpl<PermissaoDeslocamento, Long, PermissaoDeslocamentoDao> implements PermissaoDeslocamentoService {
	
	protected PermissaoDeslocamentoServiceImpl(PermissaoDeslocamentoDao dao) {
		super(dao);		
	}
	
	@Override
	public List<PermissaoDeslocamento> pesquisar(Long setorOrigem, Long setorDestino, Boolean permissao) throws ServiceException {
		List<PermissaoDeslocamento> lista = Collections.emptyList();

		try {
			lista = dao.pesquisar(setorOrigem, setorDestino, permissao);
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}

		return lista;
	}
	
	@Override
	public boolean isDeslocamentoAutorizado(Long setorOrigem, Long setorDestino) throws ServiceException{
		try {
			List<PermissaoDeslocamento> lista = pesquisar(null,setorDestino,Boolean.TRUE);
			if(lista != null && !lista.isEmpty()){
				//significa que o setor destino aceita deslocamento somente daqueles setados como autorizados
				for(PermissaoDeslocamento pd : lista){
					if(pd.getSetorOrigem().getId().equals(setorOrigem)){
						return true;
					}
				}
				return false;
			}
			
			lista = pesquisar(null,setorDestino,Boolean.FALSE);
			if(lista != null && !lista.isEmpty()){
				//significa que o setor destino não aceita deslocamento daqueles setados como não autorizados
				for(PermissaoDeslocamento pd : lista){
					if(pd.getSetorOrigem().getId().equals(setorOrigem)){
						return false;
					}
				}
				return true;
			}
						
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
		return true;
	}
		
}
