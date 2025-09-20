package br.gov.stf.estf.processostf.model.service.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia.TipoProcessoDependenciaEnum;
import br.gov.stf.estf.entidade.processostf.ProcessoIntegracao;
import br.gov.stf.estf.localizacao.model.service.OrigemService;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.estf.processostf.model.service.HistoricoProcessoOrigemService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.service.ProcessoIntegracaoService;
import br.gov.stf.estf.processostf.model.service.exception.LancamentoIndevidoException;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfo;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos.ProcessoEAndamentoProcesso;
import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoBaixaHandler extends AndamentoProcessoHandler {

	@Autowired
	private AndamentoProcessoService andamentoProcessoService;
	
   @Autowired
   private ComunicacaoIncidenteService comunicacaoIncidenteService;	

	@Autowired
	HistoricoProcessoOrigemService historicoProcessoOrigemService;
	
	@Autowired
	GuiaService guiaService;
	
	@Autowired
	SetorService setorService;
	
	@Autowired
	ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	DeslocaProcessoService deslocaProcessoService;
	
	@Autowired
	ProcessoIntegracaoService processoIntegracaoService;

	@Autowired
	ProcessoDependenciaService processoDependenciaService;
	
	@Autowired
	private OrigemService origemService;
	
	@Override
	public boolean precisaConfirmacaoLancarAndamento(Processo processoAndamento) throws ServiceException {

		AndamentoProcesso exemplo = new AndamentoProcesso();
		exemplo.setSigClasseProces(processoAndamento.getSiglaClasseProcessual());
		exemplo.setNumProcesso(processoAndamento.getNumeroProcessual());
		exemplo.setCodigoAndamento(Andamentos.CONVERTIDO_EM_DILIGENCIA.getId());

		List<AndamentoProcesso> andamentosProcesso = andamentoProcessoService.pesquisarPorExemplo(exemplo);

		// Não permitir que seja lançado andamento de baixa se o processo já tiver o andamento 6001 - Convertido em diligência.
		return (andamentosProcesso != null && andamentosProcesso.size() > 0);
	}

	@Override
	public String getMensagemConfirmacao() {
		return "O andamento correto a lançar é o 7100 - BAIXA DOS AUTOS EM DILIGÊNCIA, GUIA Nº.";
	}

	@Override
	public boolean precisaVerificarCodigoOrigem() {
		return true;
	}

	@Override
	public void preRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem) throws ServiceException {
		// A origem só é passada quando é registrada a baixa na tela de andamento para um processo. Nessa tela o usuário escolhe a origem
		// Para andamento para vários processos, a origem não é informada e não tem como verificar. Neste caso, o usuário faz o andamento e depois o deslocamento físico do processo.
//		if (origem !=null){				
//			if (deslocaProcessoService.isBaixadoParaOrigem(processoAndamento, origem, andamentoProcesso.getTipoAndamento())) {
//				throw new AndamentoNaoAutorizadoException("Este processo já foi baixado para este tribunal e não é possível baixar um processo duas vezes para o mesmo tribunal.");
//			}
//		}
	}
	
	@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processo, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {
		if (Andamentos.BAIXA_EM_DILIGENCIA.getId().equals(andamentoProcesso.getCodigoAndamento())) {
			processo.setBaixa(true);
			
			if (processo.isEletronico())
				if (origemService.isOrigemAptaParaParaNotificacao(origem.getId()))
					processoIntegracaoService.notificarBaixaMNI(processo.getId(), andamentoProcesso.getId());
			
			return;
		}
		
		// realizar o deslocamento somente para processos em meio eletrônico
		/*
		if (processo.isEletronico()) {
			
			
			if(!(processoDependenciaService.getProcessoVinculador(processo) != null 
					&& andamentoProcesso != null 
					&& processoDependenciaService != null  
					&& deslocaProcessoService != null 
					&& (andamentoProcesso.getCodigoAndamento() == Andamentos.REMESSA_EXTERNA_DOS_AUTOS.getId().longValue()
					||  andamentoProcesso.getCodigoAndamento() == Andamentos.BAIXA_AO_ARQUIVO_DO_STF_GUIA_NO.getId().longValue()
					||  andamentoProcesso.getCodigoAndamento() == Andamentos.BAIXA_DEFINITIVA_DOS_AUTOS.getId().longValue()
					||  andamentoProcesso.getCodigoAndamento() == Andamentos.REMESSA_AO_JUIZO_COMPETENTE.getId().longValue())
					&& (deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo).getGuia().getCodigoOrgaoDestino().longValue() != Setor.CODIGO_SETOR_PGR.longValue()
					&& deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo).getGuia().getTipoOrgaoDestino().longValue() == Guia.CODIGO_ORIGEM_EXTERNO
					&& ((processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo().longValue() 
					&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null)
					||	(processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo().longValue() 
					&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null)
					||	(processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.REAUTUADO.getCodigo().longValue() 
					&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null ))))){
				deslocaProcessoService.deslocarProcessoParaSetorUsuario(processo, setor);
				deslocaProcessoService.darBaixaProcesso(processo, origem, setor, andamentoProcesso);
			}
			
			DeslocaProcesso deslocaProcesso = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo);
			String observacao = andamentoProcesso.getDescricaoObservacaoAndamento();
			observacao = observacao.equals("") ? "" : observacao + "\n";
			observacao = observacao + "Guia: " + deslocaProcesso.getGuia().getNumeroGuia() + "/" + deslocaProcesso.getGuia().getAnoGuia() + " - " + origem.getDescricao();
			andamentoProcesso.setDescricaoObservacaoAndamento(observacao);
		}
		*/
		
		andamentoProcessoService.persistirAndamentoProcesso(andamentoProcesso);
      if (comunicacao != null) {
         associarAndamentoProcessoComunicacao(andamentoProcesso, comunicacao);
         criarComunicacaoObjetoIncidente(processo.getId(), comunicacao, andamentoProcesso, FlagProcessoLote.P);
      }
      
      if (processo.isEletronico() && Arrays.asList(7100L, 7101L, 7104L, 7108L).contains(andamentoProcesso.getCodigoAndamento()))
    	  if (origemService.isOrigemAptaParaParaNotificacao(origem.getId()))
			processoIntegracaoService.notificarBaixaMNI(processo.getId(), andamentoProcesso.getId());
      
	}
	
	@Override
	public String posRegistroAndamento(ContainerGuiaProcessos containerDeGuiaEProcessos) throws ServiceException {

			String numAnoGuia = null;
			ArrayList<Long> processos = new ArrayList<Long>();
			
			//Loop para deslocar processos para o setor do usuário
			for (ProcessoEAndamentoProcesso processoEAndamentoProcesso : containerDeGuiaEProcessos.getProcessosEAndamentosProcessos()) {
				AndamentoProcessoInfo andamentoProcessoInfo = processoEAndamentoProcesso.getAndamentoProcessoInfo();
				Processo processo = processoEAndamentoProcesso.getProcesso();
				processos.add(processo.getId());
				
				AndamentoProcesso andamentoProcesso = processoEAndamentoProcesso.getAndamentoProcesso();
				
				if(!(processoDependenciaService.getProcessoVinculador(processo) != null 
						&& andamentoProcesso != null 
						&& processoDependenciaService != null  
						&& deslocaProcessoService != null 
						&& (andamentoProcesso.getCodigoAndamento() == Andamentos.REMESSA_EXTERNA_DOS_AUTOS.getId().longValue()
						||  andamentoProcesso.getCodigoAndamento() == Andamentos.BAIXA_AO_ARQUIVO_DO_STF_GUIA_NO.getId().longValue()
						||  andamentoProcesso.getCodigoAndamento() == Andamentos.BAIXA_DEFINITIVA_DOS_AUTOS.getId().longValue()
						||  andamentoProcesso.getCodigoAndamento() == Andamentos.REMESSA_AO_JUIZO_COMPETENTE.getId().longValue())
						&& (deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo).getGuia().getCodigoOrgaoDestino().longValue() != Setor.CODIGO_SETOR_PGR.longValue()
						&& deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo).getGuia().getTipoOrgaoDestino().longValue() == Guia.CODIGO_ORIGEM_EXTERNO
						&& ((processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo().longValue() 
						&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null)
						||	(processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo().longValue() 
						&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null)
						||	(processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.REAUTUADO.getCodigo().longValue() 
						&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null ))))){
					deslocaProcessoService.deslocarProcessoParaSetorUsuario(processo, andamentoProcessoInfo.getSetor());
					
				}
			}
			//Grava a guia
			numAnoGuia = deslocaProcessoService.darBaixaProcesso(containerDeGuiaEProcessos.getGuia(), processos);
			
			//Loop para setar o andamento no deslocamento.
			for (ProcessoEAndamentoProcesso processoEAndamentoProcesso : containerDeGuiaEProcessos.getProcessosEAndamentosProcessos()) {
				
				Processo processo = processoEAndamentoProcesso.getProcesso();
				AndamentoProcesso andamentoProcesso = processoEAndamentoProcesso.getAndamentoProcesso();

				//Seta o andamento no deslocamento
				DeslocaProcesso  deslocaProcesso = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo);
				deslocaProcesso.setAndamentoProcesso(andamentoProcesso);
				deslocaProcessoService.alterar(deslocaProcesso);
				
				String observacao = andamentoProcesso.getDescricaoObservacaoAndamento();
				observacao = observacao.equals("") ? "" : observacao + "\n";
				observacao = observacao + "Guia: " + deslocaProcesso.getGuia().getNumeroGuia() + "/" + deslocaProcesso.getGuia().getAnoGuia() + " - " + processoEAndamentoProcesso.getAndamentoProcessoInfo().getOrigem().getDescricao();
				andamentoProcesso.setDescricaoObservacaoAndamento(observacao);
			
				andamentoProcessoService.persistirAndamentoProcesso(andamentoProcesso);
				
			      if (processo.isEletronico() && Arrays.asList(7100L, 7101L, 7104L, 7108L).contains(andamentoProcesso.getCodigoAndamento()))
			    	  if (origemService.isOrigemAptaParaParaNotificacao(processoEAndamentoProcesso.getAndamentoProcessoInfo().getOrigem().getId()))
						processoIntegracaoService.notificarBaixaMNI(processo.getId(), andamentoProcesso.getId());
					
			  Comunicacao comunicacao = processoEAndamentoProcesso.getAndamentoProcessoInfo().getComunicacao();
		      if (comunicacao != null) {
		         associarAndamentoProcessoComunicacao(andamentoProcesso, comunicacao);
		         criarComunicacaoObjetoIncidente(processo.getId(), comunicacao, andamentoProcesso, FlagProcessoLote.P);
		      }
			}
		    return numAnoGuia;
	}

/**
    * Cria um objeto do tipo ComunicacaoIncidente para intimação e o associa à
    * comunicação informada.
    *
    * @param comunicacao
    * @param pdf
    * @param usuario
    * @return
    * @throws ServiceException
    */
   protected ComunicacaoIncidente criarComunicacaoObjetoIncidente(Long idObjetoIncidente, Comunicacao comunicacao, AndamentoProcesso andamentoProcesso, FlagProcessoLote tipoVinculo) throws ServiceException {
      ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperarPorId(idObjetoIncidente);
      ComunicacaoIncidente comunicacaoIncidente = new ComunicacaoIncidente();
      comunicacaoIncidente.setObjetoIncidente(objetoIncidente);
      comunicacaoIncidente.setTipoVinculo(tipoVinculo);
      comunicacaoIncidente.setComunicacao(comunicacao);
      comunicacaoIncidente.setAndamentoProcesso(andamentoProcesso);
      return comunicacaoIncidenteService.salvar(comunicacaoIncidente);
   }	

	/**
	 * Verifica condições adicionais ou ações necessárias antes de lançar um andamento indevido.
	 */
	@Override
	public void verificarLancamentoIndevido(AndamentoProcesso andamentoProcessoAnulado) throws ServiceException {
		
		if (!andamentoProcessoAnulado.getUltimoAndamento()) {
			throw new LancamentoIndevidoException("Só é possível invalidar um andamento de baixa se ele for o último andamento.");
		}
		
		if (processoIntegracaoService.isAvisoLido(andamentoProcessoAnulado)) {
			throw new LancamentoIndevidoException("Não é possível invalidar este anamdanto porque o aviso de baixa já foi consultado.");
		}
	}
	
	/**
	 * Este método é usado para executar algum procedimento adicional após a criação de um lançamento indevido.
	 */
	@Override
	public void lancarAndamentoIndevido(Processo processoAndamento, AndamentoProcesso andamentoProcessoAnulado, AndamentoProcesso andamentoProcessoIndevido) throws ServiceException {
		deslocaProcessoService.devolverDeslocamento(processoAndamento);
		
		List<ProcessoIntegracao> processosIntegracao = processoIntegracaoService.pesquisar(andamentoProcessoAnulado);
		for (Iterator iterator = processosIntegracao.iterator(); iterator.hasNext();) {
			
			ProcessoIntegracao processoIntegracao = (ProcessoIntegracao) iterator.next();
			
			processoIntegracaoService.excluir(processoIntegracao);
		}
	}
	
	public void verificarCancelamentoAndamentoIndevido() throws ServiceException {
		throw new ServiceException("Não é possível cancelar um andamento indevido de baixa.");
	}
}