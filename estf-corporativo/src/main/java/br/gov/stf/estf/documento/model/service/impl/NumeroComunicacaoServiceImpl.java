package br.gov.stf.estf.documento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.NumeroComunicacaoDao;
import br.gov.stf.estf.documento.model.service.NumeroComunicacaoService;
import br.gov.stf.estf.entidade.documento.NumeroComunicacao;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("numeroComunicacaoService")
public class NumeroComunicacaoServiceImpl extends GenericServiceImpl<NumeroComunicacao, NumeroComunicacao.NumeroComunicacaoId, NumeroComunicacaoDao> implements NumeroComunicacaoService {

	protected NumeroComunicacaoServiceImpl(NumeroComunicacaoDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}
}
