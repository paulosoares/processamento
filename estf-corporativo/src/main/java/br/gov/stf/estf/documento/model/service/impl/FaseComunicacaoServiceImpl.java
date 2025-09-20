package br.gov.stf.estf.documento.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.FaseComunicacaoDao;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.FaseComunicacao.FlagFaseAtual;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaAcessoDocumento;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("faseComunicacaoService")
public class FaseComunicacaoServiceImpl extends GenericServiceImpl<FaseComunicacao, Long, FaseComunicacaoDao> implements FaseComunicacaoService {

	private static final Long CODIGO_ANDAMENTO_DOCUMENTO_EXPEDIDO = 7317L;
	private static final Long CODIGO_ANDAMENTO_ASSINATURA_CANCELADA = 7700L;

	private final AndamentoProcessoService andamentoProcessoService;
	private final ComunicacaoIncidenteService comunicacaoIncidenteService;
	private final UsuarioService usuarioService;

	public FaseComunicacaoServiceImpl(FaseComunicacaoDao dao, AndamentoProcessoService andamentoProcessoService, UsuarioService usuarioService,
			ComunicacaoIncidenteService comunicacaoIncidenteService) {
		super(dao);
		this.andamentoProcessoService = andamentoProcessoService;
		this.usuarioService = usuarioService;
		this.comunicacaoIncidenteService = comunicacaoIncidenteService;
	}

	public FaseComunicacao pesquisarFaseAtual(Long idComunicacao) throws ServiceException {
		FaseComunicacao faseComunicacao = new FaseComunicacao();

		try {
			faseComunicacao = dao.pesquisarFaseAtual(idComunicacao);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar fase atual da comunicação com ID = " + idComunicacao, e);
		}

		return faseComunicacao;
	}

	public void incluirFase(TipoFaseComunicacao tipoFase, Comunicacao comunicacao, String observacao, String usuario) throws ServiceException {
		Date data = new Date(System.currentTimeMillis());
		FaseComunicacao faseComunicacao = criarFaseComunicacao(tipoFase, comunicacao, observacao, data);

		try {
			dao.incluirFase(faseComunicacao);

			boolean gerarAndamento = true;
			if(comunicacao.getModeloComunicacao().getFlagSemAndamento() !=null && comunicacao.getModeloComunicacao().getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S)) {
				gerarAndamento = false;
			}
			if(comunicacao.getFlagSemAndamento() != null && comunicacao.getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S )) {
				gerarAndamento = false;
			}
			if(comunicacao.getFlagSemAndamento() != null && comunicacao.getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.N )) {
				gerarAndamento = true;
			}
			
			if(gerarAndamento) {
				if (tipoFase == TipoFaseComunicacao.EXPEDIDO) {
					lancarAndamento(data, comunicacao, observacao, CODIGO_ANDAMENTO_DOCUMENTO_EXPEDIDO, usuario);
				} else if (tipoFase == TipoFaseComunicacao.ASSINATURA_CANCELADA) {
					lancarAndamento(data, comunicacao, observacao, CODIGO_ANDAMENTO_ASSINATURA_CANCELADA, usuario);
					}
				}
			

			/* if(!comunicacao.getModeloComunicacao().getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S)) {
				if (tipoFase == TipoFaseComunicacao.EXPEDIDO) {
					lancarAndamento(data, comunicacao, observacao, CODIGO_ANDAMENTO_DOCUMENTO_EXPEDIDO, usuario);
				} else if (tipoFase == TipoFaseComunicacao.ASSINATURA_CANCELADA) {
					lancarAndamento(data, comunicacao, observacao, CODIGO_ANDAMENTO_ASSINATURA_CANCELADA, usuario);
				}
			}*/
			
		} catch (Exception exception) {
			throw new ServiceException("Erro ao incluir fase " + tipoFase + " na comunicação " + comunicacao, exception);
		}
	}

	private FaseComunicacao criarFaseComunicacao(TipoFaseComunicacao tipoFase, Comunicacao comunicacao, String observacao, Date data) {
		FaseComunicacao faseComunicacao = new FaseComunicacao();
		faseComunicacao.setDataLancamento(data);
		faseComunicacao.setTipoFase(tipoFase);
		faseComunicacao.setFlagFaseAtual(FlagFaseAtual.S);
		faseComunicacao.setComunicacao(comunicacao);
		faseComunicacao.setObservacao(observacao);
		return faseComunicacao;
	}

	private void lancarAndamento(Date data, Comunicacao comunicacao, String observacao, Long codigoAndamento, String usuario) throws ServiceException {
		List<ComunicacaoIncidente> listaProcessosLote = new ArrayList<ComunicacaoIncidente>();
		listaProcessosLote.add(comunicacao.getComunicacaoIncidentePrincipal());

		List<ComunicacaoIncidente> listaProcessosLoteVinculados = comunicacaoIncidenteService.verificaSeExisteProcessosVinculados(comunicacao);
		listaProcessosLote.addAll(listaProcessosLoteVinculados);

		if (CollectionUtils.isNotEmpty(listaProcessosLote)) {
			for (ComunicacaoIncidente ci : listaProcessosLote) {
				AndamentoProcesso andamentoProcesso = salvarAndamentoProcesso(comunicacao, data, ci.getObjetoIncidente().getPrincipal(), codigoAndamento, usuario);
				atualizarComunicacaoIncidente(ci, andamentoProcesso);
			}
		}
	}

	private AndamentoProcesso salvarAndamentoProcesso(Comunicacao comunicacao, Date data, ObjetoIncidente<?> ob, Long codigoAndamento, String usuario)
			throws ServiceException {
		AndamentoProcesso andamentoProcesso = criarAndamentoProcesso(comunicacao, data, ob, codigoAndamento, usuario);

		try {
			andamentoProcessoService.incluir(andamentoProcesso);
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao salvar andamento processual: " + andamentoProcesso, exception);
		}

		return andamentoProcesso;
	}

	private AndamentoProcesso criarAndamentoProcesso(Comunicacao comunicacao, Date data, ObjetoIncidente<?> ob, Long codigoAndamento, String usuario)
			throws ServiceException {
		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		andamentoProcesso.setCodigoAndamento(codigoAndamento);
		andamentoProcesso.setDataAndamento(data);
		andamentoProcesso.setObjetoIncidente(ob);
		andamentoProcesso.setDescricaoObservacaoAndamento(criarObservacaoAndamentoComunicacao(comunicacao));
		andamentoProcesso.setNumeroSequencia(recuperarProximoNumeroSequenciaAndamento(ob));
		andamentoProcesso.setCodigoUsuario(usuario.toUpperCase());
		andamentoProcesso.setSetor(recuperarSetor(usuario));
		andamentoProcesso.setUltimoAndamento(true);
		andamentoProcesso.setLancamentoIndevido(false);
		return andamentoProcesso;
	}

	private String criarObservacaoAndamentoComunicacao(Comunicacao comunicacao) {
		ModeloComunicacao modelo = comunicacao.getModeloComunicacao();
		FlagGenericaAcessoDocumento flagAcessoDocumento = modelo.getFlagTipoAcessoDocumentoPeca();
		String observacao;

		if (flagAcessoDocumento == FlagGenericaAcessoDocumento.I) {
			observacao = null;
		} else {
			observacao = modelo.getDscModelo();
		}

		return observacao;
	}

	private Long recuperarProximoNumeroSequenciaAndamento(ObjetoIncidente<?> ob) throws ServiceException {
		Long numSequencia = andamentoProcessoService.recuperarProximoNumeroSequencia(ob);
		return numSequencia;
	}

	private Setor recuperarSetor(String usuario) throws ServiceException {
		Setor setor;

		try {
			setor = usuarioService.recuperarUsuario(usuario.toUpperCase()).getSetor();
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao recuperar usuário: " + usuario, exception);
		}

		return setor;
	}

	private void atualizarComunicacaoIncidente(ComunicacaoIncidente ci, AndamentoProcesso andamentoProcesso) throws ServiceException {
		if (andamentoProcesso.getCodigoAndamento() == CODIGO_ANDAMENTO_ASSINATURA_CANCELADA) {
			atualizarAndamentoLancamentoIndevido(ci, andamentoProcesso);
		}

		ci.setAndamentoProcesso(andamentoProcesso);

		try {
			comunicacaoIncidenteService.alterar(ci);
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao atualizar o andamento processual da comunicação incidente: " + ci, exception);
		}
	}

	private void atualizarAndamentoLancamentoIndevido(ComunicacaoIncidente ci, AndamentoProcesso andamentoLancamentoIndevido) throws ServiceException {
		AndamentoProcesso andamentoComunicacaoAssinada = ci.getAndamentoProcesso();
		andamentoLancamentoIndevido.setNumeroSequenciaErrado(andamentoComunicacaoAssinada.getNumeroSequencia());

		try {
			andamentoProcessoService.alterar(andamentoLancamentoIndevido);
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao marcar andamento como lançamento indevido: " + andamentoLancamentoIndevido, exception);
		}
	}
}
