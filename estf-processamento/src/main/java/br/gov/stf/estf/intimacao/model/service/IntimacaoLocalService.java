package br.gov.stf.estf.intimacao.model.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.intimacao.model.service.exception.AndamentoNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoGerouIntimacaoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoRatificadaException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteSemAceiteInitmacaoEletronicaException;
import br.gov.stf.estf.intimacao.model.service.exception.PecaNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.PessoaSemUsuarioException;
import br.gov.stf.estf.intimacao.model.service.exception.ProcessoNaoEletronicoException;
import br.gov.stf.estf.intimacao.model.service.exception.TipoModeloComunicacaoEnumInvalidoException;
import br.gov.stf.estf.intimacao.model.vo.TipoRecebimentoComunicacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ComunicacaoExternaDTO;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface IntimacaoLocalService {

    /**
     * Cria intimações físicas para as partes informadas nos processos
     * informados. Estas intimações podem ser cartas ou mandados, de acordo com
     * a existencia ou não de representação da parte informada em Brasília.
     *
     * @param usuarioCriador
     * @param modeloComunicacaoEnum
     * @param dataIntimacao
     * @param parte
     * @param setor
     * @param mapProcessos
     * @param responsavel
     * @param cargoResponsavel
     * @param numeroDJ
     * @return
     * @throws br.gov.stf.estf.intimacao.model.service.exception.ParteNaoPertencenteProcessoException
     * @throws br.gov.stf.estf.intimacao.model.service.exception.ParteNaoGerouIntimacaoException
     * @throws ServiceException
     */
    List<Comunicacao> criarIntimacoesFisicas(String usuarioCriador,
            ModeloComunicacaoEnum modeloComunicacaoEnum,
            Date dataIntimacao,
            Parte parte,
            Setor setor,
            Map<String, List<PecaDTO>> mapProcessos,
            String responsavel,
            String cargoResponsavel,
            Long numeroDJ,
            TipoMeioProcesso tipoMeioProcesso) throws ParteNaoPertencenteProcessoException, ParteNaoGerouIntimacaoException, ServiceException;

    /**
     * Cria uma intimação eletrônica para a parte informada, no processo
     * informado. Após a criação da comunicação, será criado um aviso para esta,
     * onde o controle do prazo da intimação eletrônica, bem como o seu envio,
     * será feito.
     *
     * @param usuarioCriador
     * @param setor
     * @param dataIntimacao
     * @param modeloComunicacaoEnum
     * @param objetoIncidente
     * @param partes
     * @param pecas
     * @param andamentos
     * @return
     * @throws TipoModeloComunicacaoEnumInvalidoException
     * @throws
     * br.gov.stf.estf.intimacao.model.service.exception.ProcessoNaoEletronicoException
     * @throws
     * br.gov.stf.estf.intimacao.model.service.exception.ParteSemAceiteInitmacaoEletronicaException
     * @throws
     * br.gov.stf.estf.intimacao.model.service.exception.ParteNaoRatificadaException
     * @throws ParteNaoPertencenteProcessoException
     * @throws PecaNaoPertencenteProcessoException
     * @throws AndamentoNaoPertencenteProcessoException
     * @throws ServiceException
     */
    Comunicacao criarIntimacoesEletronicas(String usuarioCriador,
            Setor setor,
            Date dataIntimacao,
            ModeloComunicacaoEnum modeloComunicacaoEnum,
            ObjetoIncidente objetoIncidente,
            List<Parte> partes,
            Set<ArquivoProcessoEletronico> pecas,
            Set<Andamento> andamentos) throws TipoModeloComunicacaoEnumInvalidoException,
            ProcessoNaoEletronicoException,
            ParteSemAceiteInitmacaoEletronicaException,
            ParteNaoRatificadaException,
            ParteNaoPertencenteProcessoException,
            PecaNaoPertencenteProcessoException,
            AndamentoNaoPertencenteProcessoException,
            ServiceException;

	List<Comunicacao> criarIntimacoesFisicas(long pessoaDestinatario,
			String usuarioCriador, ModeloComunicacaoEnum modeloComunicacaoEnum,
			Date dataIntimacao, Parte parte, Setor setor,
			Collection<PecaDTO> pecasProcessosIntimar, String responsavel,
			String cargoResponsavel, Long numeroDJ,
			DocumentoEletronico documentoAcordao,
			TipoMeioProcesso tipoMeioProcesso)
			throws ParteNaoPertencenteProcessoException,
			ParteNaoGerouIntimacaoException, ServiceException;

	Comunicacao criarIntimacao(String usuarioCriador, Setor setor,
			Date dataIntimacao, ModeloComunicacaoEnum modeloComunicacaoEnum,
			Set<Long> objetoIncidente, Long codigoPessoaDestinatario,
			List<PecaProcessoEletronico> pecas,
			List<AndamentoProcesso> andamentos,
			TipoFaseComunicacao tipoFaseComunicacao,
			String descricaoComunicacao, DocumentoEletronico documentoAcordao)
			throws TipoModeloComunicacaoEnumInvalidoException,
			ProcessoNaoEletronicoException,
			ParteSemAceiteInitmacaoEletronicaException,
			PessoaSemUsuarioException, ParteNaoPertencenteProcessoException,
			PecaNaoPertencenteProcessoException,
			AndamentoNaoPertencenteProcessoException, ServiceException;

	List<ComunicacaoExternaDTO> pesquisarComunicacaoExterna(String idParte,
			TipoRecebimentoComunicacaoEnum tipoRecebimentoComunicacaoEnum,
			String descricaoTipoComunicacao, String descricaoModelo,
			Date periodoEnvioInicio, Date periodoEnvioFim, Long idProcesso,
			Long idPreferemcia) throws ServiceException;

	List<TipoIncidentePreferencia> buscaTodasPreferencias() throws DaoException;
}
