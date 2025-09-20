package br.gov.stf.estf.intimacao.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.intimacao.model.dataaccess.PartesGerarIntimacaoLocalDao;
import br.gov.stf.estf.intimacao.model.dataaccess.TipoMeioIntimacaoEnum;
import br.gov.stf.estf.intimacao.model.service.PartesGerarIntimacaoLocalService;
import br.gov.stf.estf.intimacao.visao.dto.ParteIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.ParteProcessoIntimacaoDto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("partesGerarIntimacaoLocalService")
public class PartesGerarIntimacaoLocalServiceImpl implements PartesGerarIntimacaoLocalService {

	private PartesGerarIntimacaoLocalDao dao;

    public PartesGerarIntimacaoLocalServiceImpl(PartesGerarIntimacaoLocalDao dao) {
        this.dao = dao;
    }

    @Override
    public List<ParteIntimacaoDto> listarPartes(Date dataPublicacaoDj,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoProcesso,
            Boolean intimacaoRealizada,
            Boolean representanteComPrerrogIntPess,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoRepresentanteParte) throws ServiceException {
        List<ParteIntimacaoDto> lista = null;
        try {
            lista = dao.listarPartes(dataPublicacaoDj,
                    tipoMeioIntimacaoProcesso,
                    intimacaoRealizada,
                    representanteComPrerrogIntPess,
                    tipoMeioIntimacaoRepresentanteParte);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return lista;
    }
    
    
    @Override
    public List<ParteProcessoIntimacaoDto> listarPartes(Date dataPublicacao, TipoMeioIntimacaoEnum tipoMeioComunicacaoEnum) throws ServiceException {
        List<ParteProcessoIntimacaoDto> lista = null;
        try {
            lista = dao.listarPartes(dataPublicacao, tipoMeioComunicacaoEnum);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return lista;
    }    
  

    @Override
    public List<String> obterUFParte(long seqPessoa) throws ServiceException {
        List<String> listaUf;
        try {
            listaUf = dao.obterUFParte(seqPessoa);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return listaUf;
    }

    @Override
    public String obterClasseProcesso(String siglaClasseProcesso) throws ServiceException {
        String descricaoClasseProcesso = null;
        try {
            descricaoClasseProcesso = dao.obterClasseProcesso(siglaClasseProcesso);
        } catch (DaoException de) {
            throw new ServiceException(de);
        }

        return descricaoClasseProcesso;
    }

    @Override
    public Setor obterSetor(String sigClasse, long numProcesso) throws ServiceException {
        List<Setor> lista;
        try {
            lista = dao.obterSetor(sigClasse, numProcesso);
            	
            return lista.get(0);
           
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}