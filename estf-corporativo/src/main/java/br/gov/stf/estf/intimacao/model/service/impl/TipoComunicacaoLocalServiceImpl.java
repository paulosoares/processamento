package br.gov.stf.estf.intimacao.model.service.impl;

import br.gov.stf.estf.documento.model.service.TipoComunicacaoService;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.intimacao.model.dataaccess.TipoComunicacaoLocalDao;
import br.gov.stf.estf.intimacao.model.service.TipoComunicacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.exception.TipoComunicacaoComDescricaoDiferenteUmException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Roberio.Fernandes
 */
@Service("tipoComunicacaoLocalService")
public class TipoComunicacaoLocalServiceImpl implements TipoComunicacaoLocalService {
  
    public static final long serialVersionUID = 1L;

    @Autowired
    private TipoComunicacaoService tipoComunicacaoService;
    @Autowired
    private TipoComunicacaoLocalDao tipoComunicacaoLocalDao;

    @Override
    public TipoComunicacao buscar(String descricao) throws TipoComunicacaoComDescricaoDiferenteUmException, ServiceException {
        List<TipoComunicacao> tipos = tipoComunicacaoService.pesquisarListaTiposModelos(descricao);
        if (tipos.size() != 1) {
            throw new TipoComunicacaoComDescricaoDiferenteUmException();
        }
        return tipos.get(0);
    }

    @Override
    public Long gerarProximoNumeroComunicacao(Long idTipoComunicacao) throws ServiceException {
        try {
			return tipoComunicacaoLocalDao.gerarProximoNumeroComunicacao(idTipoComunicacao);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
    }
}