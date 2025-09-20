package br.gov.stf.estf.processostf.model.service.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.GrupoAndamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoComunicacaoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.OrigemAndamentoDecisaoService;
import br.gov.stf.estf.processostf.model.service.RecursoProcessoService;
import br.gov.stf.estf.processostf.model.service.exception.AndamentoNaoAutorizadoException;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.AscendantOrder;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.DescendantOrder;
import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoProcessoHandler {

	@Autowired
	private OrigemAndamentoDecisaoService origemAndamentoDecisaoService;

	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private ComunicacaoIncidenteService comunicacaoIncidenteService;	

	@Autowired
	private AndamentoService andamentoService;

	@Autowired
	private RecursoProcessoService recursoProcessoService;

	@Autowired
	private AndamentoProcessoService andamentoProcessoService;
	
	@Autowired
	private AndamentoProcessoComunicacaoService andamentoProcessoComunicacaoService;
	
	public AndamentoProcessoHandler() {
	}

	public AndamentoProcessoHandler(OrigemAndamentoDecisaoService origemAndamentoDecisaoService, AndamentoService andamentoService,
			RecursoProcessoService recursoProcessoService, AndamentoProcessoService andamentoProcessoService) {
		this.origemAndamentoDecisaoService = origemAndamentoDecisaoService;
		this.andamentoService = andamentoService;
		this.recursoProcessoService = recursoProcessoService;
		this.andamentoProcessoService = andamentoProcessoService;
	}

	public String getSituacao(AndamentoProcesso andamentoProcesso) throws ServiceException {
		return "";
	}

	public final void verificarLancamentoDispositivo(AndamentoProcesso andamentoProcesso) throws ServiceException {
		if (andamentoProcessoService.isLancadoPorDispositivo(andamentoProcesso))
			throw new ServiceException("Este andamento não pode ser tornado indevido pois é consequência do lançamento de um dispositivo judicial.");
	}

	/**
	 * Este método é usado para executar algum procedimento adicional após a criação de um lançamento indevido.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public void lancarAndamentoIndevido(Processo processoAndamento, AndamentoProcesso andamentoProcessoAnulado, AndamentoProcesso andamentoProcessoIndevido) throws ServiceException {
	}

	/**
	 * Verifica condições adicionais ou ações necessárias antes de lançar um andamento indevido.
	 */
	public void verificarLancamentoIndevido(AndamentoProcesso andamentoProcessoAnulado) throws ServiceException {
	}

	public void cancelarAndamentoIndevido(AndamentoProcesso andamentoProcessoAnulado) throws ServiceException {
	}

	/**
	 * Verifica se o tipo do andamento necessita de um número de processo principal para lançar um andamento. Por exemplo, se o andamento for 'Apensado ao
	 * Processo nº', será necessário informar o processo no qual o outro processo será apensado.
	 * 
	 * @return
	 */
	public boolean precisaProcessoPrincipal() {
		return false;
	}

	public boolean precisaProcessosPrincipais() {
		return false;
	}

	public boolean precisaPeticao() {
		return false;
	}

	public boolean precisaOrigemDecisao(Andamento andamento) throws ServiceException {

		return isGrupoPedidoVista(andamento) || andamentoService.isGrupoDecisao(andamento);
	}

	public boolean isOrigemDecisaoObrigatoriaIndependenteSetorComDecisao(Andamento andamento) {
		return andamento.getId().equals(Andamentos.APRESENTACAO_EM_MESA.getId())
				|| andamento.getId().equals(Andamentos.INCLUSAO_EM_PAUTA.getId()) || isGrupoPedidoVista(andamento);
	}

	public List<OrigemAndamentoDecisao> getOrigensDecisao(Andamento andamento) throws ServiceException {

		List<OrigemAndamentoDecisao> origens = new ArrayList<OrigemAndamentoDecisao>();

		if (isGrupoPedidoVista(andamento)) {
			origens = origemAndamentoDecisaoService.pesquisarOrigensComMinistroAtivo();

		} else if (andamentoService.isGrupoDecisao(andamento)) {
			origens = origemAndamentoDecisaoService.pesquisar(new DescendantOrder("ativo"), new AscendantOrder("descricao"));
		}

		return origens;
	}

	private boolean isGrupoPedidoVista(Andamento andamento) {
		Long grupoAndamento = andamento.getGrupoAndamento();
		return grupoAndamento == null ? false
				: (grupoAndamento == GrupoAndamento.GruposAndamento.DEVOLUCAO_DE_PROCESSO_COM_VISTA.getCodigo() || grupoAndamento == GrupoAndamento.GruposAndamento.VISTA_JULGAMENTO
						.getCodigo());
	}

	public boolean precisaTipoDevolucao() {
		return false;
	}

	public boolean precisaConfirmacaoLancarAndamento(Processo processoAndamento) throws ServiceException {
		return false;
	}

	public String getMensagemConfirmacao() {
		return "";
	}

	public void preRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor,
			String codigoUsuario, Origem origem) throws ServiceException {
		if (andamentoProcesso.getCodigoAndamento() == 7103L) {				
	 		if ( !andamentoProcessoService.verificarAndamentoProcessoNaoIndevido(processoAndamento.getSiglaClasseProcessual(), processoAndamento.getNumeroProcessual(), new Long("8219")) &&
			     !andamentoProcessoService.verificarAndamentoProcessoNaoIndevido(processoAndamento.getSiglaClasseProcessual(), processoAndamento.getNumeroProcessual(), new Long("7106")) &&
			     !andamentoProcessoService.verificarAndamentoProcessoNaoIndevido(processoAndamento.getSiglaClasseProcessual(), processoAndamento.getNumeroProcessual(), new Long("2055"))) {
				throw new AndamentoNaoAutorizadoException("O lançamento 7103 não pode ser concluído, pois não existe lançamento anterior de 'Transitado em julgado' (8219) ou 'Processo findo' (7106)");
			}
		} 
	}

	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor,
			String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {
	}
	
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor,
			String codigoUsuario, Origem origem) throws ServiceException {
		posRegistroAndamento(andamentoProcesso, processoAndamento, processosPrincipais, peticao, setor, codigoUsuario, origem);
	}	
	
	public Long getCodigoRecurso(AndamentoProcesso andamentoProcessoAnterior, boolean isAndamentoVide) throws ServiceException {

		// É o primeiro andamento do processo?
		if (andamentoProcessoAnterior != null) {

			// O andamento anterior é um andamento de recurso?
			if (andamentoProcessoAnterior.getTipoAndamento().getRecurso() || isAndamentoVide) {
				return recursoProcessoService.pesquisar((Processo) andamentoProcessoAnterior.getObjetoIncidente().getPrincipal(),
						andamentoProcessoAnterior.getDataAndamento());
			} else {
				return andamentoProcessoAnterior.getRecurso();
			}
		} else { // 1º andamento do processo.
			return 0L;
		}
	}

	public boolean precisaVerificarCodigoOrigem() {
		return false;
	}
	
	public void verificarCancelamentoAndamentoIndevido() throws ServiceException {
	}
	
	/**
	 * Associa andamento processo comunicação a comunicação
	 *
	 * @param andamentos
	 * @param comunicacao
	 * @return
	 * @throws ServiceException
	 */
	protected void associarAndamentoProcessoComunicacao(AndamentoProcesso andamentoProcesso, Comunicacao comunicacao) throws ServiceException {
		AndamentoProcessoComunicacao andamentoProcessoComunicacao = new AndamentoProcessoComunicacao();
		andamentoProcessoComunicacao.setAndamentoProcesso(andamentoProcesso);
		andamentoProcessoComunicacao.setComunicacao(comunicacao);
		andamentoProcessoComunicacao.setTipoVinculoAndamento(TipoVinculoAndamento.RELACIONADO);
		andamentoProcessoComunicacaoService.salvar(andamentoProcessoComunicacao);
	}



	public String posRegistroAndamento(
			ContainerGuiaProcessos containerDeGuiaEProcessos)
			throws ServiceException {
		return null;
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
	
}