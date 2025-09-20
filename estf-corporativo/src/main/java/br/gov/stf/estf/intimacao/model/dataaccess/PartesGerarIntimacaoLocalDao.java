package br.gov.stf.estf.intimacao.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.intimacao.visao.dto.ParteIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.ParteProcessoIntimacaoDto;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface PartesGerarIntimacaoLocalDao {

    List<ParteIntimacaoDto> listarPartes(Date dataPublicacaoDj,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoProcesso,
            Boolean intimacaoRealizada,
            Boolean representanteComPrerrogIntPess,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoRepresentanteParte) throws DaoException;
    
    List<ParteProcessoIntimacaoDto> listarPartes(Date dataPublicacao,
    		TipoMeioIntimacaoEnum tipoMeioComunicacaoEnum) throws DaoException;


    List<String> obterUFParte(long seqPessoa) throws DaoException;

    List<Setor> obterSetor(String sigClasse, long numProcesso) throws DaoException;

    String obterClasseProcesso(String siglaClasseProcesso) throws DaoException;
}
