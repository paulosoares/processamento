package br.gov.stf.estf.documento.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.DeslocamentoComunicacaoDao;
import br.gov.stf.estf.documento.model.service.DeslocamentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DeslocamentoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("deslocamentoComunicacaoService")
public class DeslocamentoComunicacaoServiceImpl extends
		GenericServiceImpl<DeslocamentoComunicacao, Long, DeslocamentoComunicacaoDao> implements
		DeslocamentoComunicacaoService {

	@Autowired(required = true)
	private SetorService setorService;

	@Autowired(required = true)
	private FaseComunicacaoService faseComunicacaoService;

	public DeslocamentoComunicacaoServiceImpl(DeslocamentoComunicacaoDao dao, SetorService setorService,
			FaseComunicacaoService faseComunicacaoService) {
		super(dao);
		this.setorService = setorService;
		this.faseComunicacaoService = faseComunicacaoService;
	}

	public void incluirDeslocamento(Comunicacao comunicacao, Long idSetor, TipoFaseComunicacao fase)
			throws ServiceException {

		try {
			// Recuperando o Setor
			Setor setor = setorService.recuperarPorId(idSetor);

			if (setor == null) {
				throw new ServiceException("O setor informado n�o foi localizado (c�digo: " + idSetor
						+ "). Favor entrar em contato com a TI.");
			}

			dao.incluirDeslocamento(comunicacao, setor);

			// Inclus�o da Fase
			// Inclui fases para as comunica��es que n�o est�o assinadas ou est�o assinadas e
			// necessitam de assinatura do Ministro
			if (fase != null) {
				String descricaoFaseAtual = null;
				
				try{
					if ( fase.equals(TipoFaseComunicacao.PDF_GERADO) || 
							fase.equals(TipoFaseComunicacao.EM_REVISAO) )
						descricaoFaseAtual = comunicacao.getObservacaoFaseAtual();
				} catch (NullPointerException e){
					//N�o tem descri��o, tem que ficar null
				}
				faseComunicacaoService.incluirFase(fase, comunicacao, descricaoFaseAtual, null);
			}

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
