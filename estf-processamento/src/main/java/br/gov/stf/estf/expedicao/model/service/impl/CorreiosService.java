package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import br.gov.stf.estf.correios.dto.PrePostagemResquest;
import br.gov.stf.framework.model.service.ServiceException;

public interface CorreiosService {

	List<String> getServicos() throws ServiceException;

	PrePostagem enviar(PrePostagemResquest prePostagemRequest) throws ServiceException;

}