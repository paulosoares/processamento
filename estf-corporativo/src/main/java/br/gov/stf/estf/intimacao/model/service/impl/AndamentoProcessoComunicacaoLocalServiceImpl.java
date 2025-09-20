package br.gov.stf.estf.intimacao.model.service.impl;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.intimacao.model.dataaccess.AndamentoProcessoComunicacaoLocalDao;
import br.gov.stf.estf.intimacao.model.service.AndamentoProcessoComunicacaoLocalService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("andamentoProcessoComunicacaoLocalService")
public class AndamentoProcessoComunicacaoLocalServiceImpl implements AndamentoProcessoComunicacaoLocalService {

    @Autowired
    private AndamentoProcessoComunicacaoLocalDao dao;

    @Override
    public void salvar(AndamentoProcessoComunicacao andamentoProcessoComunicacao) throws ServiceException {
        try {
            dao.salvar(andamentoProcessoComunicacao);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<AndamentoProcesso> pesquisarAndamentoProcesso(String sigla, Long numero) throws ServiceException {
        try {
            return dao.pesquisarAndamentoProcesso(sigla, numero);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

	@Override
	public List<AndamentoProcesso> pesquisarAndamentosProcessoIncidente(Long idProcessoIncidente) throws ServiceException {
		try {
            return dao.pesquisarAndamentosProcessoIncidente(idProcessoIncidente);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}
	
	@Override
	public AndamentoProcessoComunicacao recuperarAndamentoProcessoGeradoPelaComunicacao(Long idcomunicacao, Long idCodigoAndamento) throws ServiceException {
		try {
			return dao.recuperarAndamentoProcessoGeradoPelaComunicacao(idcomunicacao, idCodigoAndamento);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
	}
}