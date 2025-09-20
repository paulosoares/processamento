package br.gov.stf.estf.localizacao.model.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Destinatario;
import br.gov.stf.estf.localizacao.model.dataaccess.DestinatarioDao;
import br.gov.stf.estf.localizacao.model.service.DestinatarioService;
import br.gov.stf.estf.localizacao.model.util.DestinatarioOrgaoOrigemResult;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("destinatarioService")
public class DestinatarioServiceImpl extends GenericServiceImpl<Destinatario, Long, DestinatarioDao> implements DestinatarioService { 
	public DestinatarioServiceImpl(DestinatarioDao dao) {
		super(dao);
	}
	
	@Override
	public List<Destinatario> recuperarDestinatarioDaOrigem(Long codOrigem, String id) throws ServiceException {
		try {
			return dao.recuperarDestinatarioDaOrigem(codOrigem, id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
	@Override
	public List<Destinatario> pesquisarDestinatarioDescricao(String descricao)
			throws ServiceException {
		try {
			return dao.pesquisarDestinatarioDescricao(descricao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DestinatarioOrgaoOrigemResult> pesquisarDestinatario(Long codOrigem, Long codDestinatario) throws ServiceException {
		
		List<DestinatarioOrgaoOrigemResult> listaDestinatarios = Collections.emptyList();
		
		try {
			List<Object[]> destinatariosOrgaoOrigem = dao.pesquisarDestinatario(codOrigem, codDestinatario);
			if (CollectionUtils.isNotEmpty(destinatariosOrgaoOrigem)) {
				listaDestinatarios = recuperarDestinatariosResults(destinatariosOrgaoOrigem);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaDestinatarios;
	}
	
	private List<DestinatarioOrgaoOrigemResult> recuperarDestinatariosResults(List<Object[]> idsDocumentos) throws ServiceException {
		
		List<DestinatarioOrgaoOrigemResult> listaDestinatarios;
		listaDestinatarios = new ArrayList<DestinatarioOrgaoOrigemResult>();
		
		for (Object[] registro : idsDocumentos) {
			DestinatarioOrgaoOrigemResult destinatarioOrgaoOrigemResult = new DestinatarioOrgaoOrigemResult();
			Object nomeDestinatario = registro[0];
			Object codigoDestinatario = registro[1];
			Object enderecoDestinatario = registro[2];
			Object nomeOrgao = null;
			Object nomeProcedencia = null;
			
			if (registro[3] != null){
				nomeOrgao = registro[3];
			}else{
				nomeOrgao = "";
			}
			if (registro[4] != null){
				nomeProcedencia = registro[4];
			}else{
				nomeProcedencia = "";
			}
			
			Object nomeOrigem = registro[5];
			Object ativo = registro[6];
			
			destinatarioOrgaoOrigemResult.setEnderecoDestinatario(enderecoDestinatario.toString());
			destinatarioOrgaoOrigemResult.setCodigoDestinatario(((BigDecimal) codigoDestinatario).longValue());
			destinatarioOrgaoOrigemResult.setNomeDestinatario(nomeDestinatario.toString());
			destinatarioOrgaoOrigemResult.setNomeOrgao(nomeOrgao.toString());
			destinatarioOrgaoOrigemResult.setNomeProcedencia(nomeProcedencia.toString());
			destinatarioOrgaoOrigemResult.setNomeOrigem(nomeOrigem.toString());
			
			if (ativo.toString().equals("S")){
				destinatarioOrgaoOrigemResult.setAtivo(true);
			}else{
				destinatarioOrgaoOrigemResult.setAtivo(false);
			}
			
			listaDestinatarios.add(destinatarioOrgaoOrigemResult);
		}
		
		return listaDestinatarios;
	}

}
