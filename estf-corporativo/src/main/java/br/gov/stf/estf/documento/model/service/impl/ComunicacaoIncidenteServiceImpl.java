package br.gov.stf.estf.documento.model.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.ComunicacaoIncidenteDao;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaAcessoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("comunicacaoIncidenteService")
public class ComunicacaoIncidenteServiceImpl extends GenericServiceImpl<ComunicacaoIncidente, Long, ComunicacaoIncidenteDao> implements ComunicacaoIncidenteService {

	private static final Long CODIGO_ANDAMENTO_ASSINATURA_CANCELADA = 7700L;

	private final AndamentoProcessoService andamentoProcessoService;
	private final UsuarioService usuarioService;

	
	public ComunicacaoIncidenteServiceImpl(ComunicacaoIncidenteDao dao, AndamentoProcessoService andamentoProcessoService, UsuarioService usuarioService) {
		super(dao);
		this.andamentoProcessoService = andamentoProcessoService;
		this.usuarioService = usuarioService;
	}

	@Override
	public ObjetoIncidente<?> selecionaObjetoIncidente(Long idDocumento) throws ServiceException {

		ObjetoIncidente<?> objeto;

		try {
			objeto = dao.selecionaObjetoIncidente(idDocumento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return objeto;
	}

	@Override
	public List<ComunicacaoIncidente> verificaSeExisteProcessosVinculados(Comunicacao comunicacao) throws ServiceException {
		List<ComunicacaoIncidente> listaProcessoVinculados = Collections.emptyList();
		try {
			listaProcessoVinculados = dao.verificaSeExisteProcessosVinculados(comunicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaProcessoVinculados;
	}

	@Override
	public void lancarAndamento(Date data, Comunicacao comunicacao, Long codigoAndamento, String usuario) throws ServiceException {

		List<ComunicacaoIncidente> listaProcessosLote = new ArrayList<ComunicacaoIncidente>();
		listaProcessosLote.add(comunicacao.getComunicacaoIncidentePrincipal());

		List<ComunicacaoIncidente> listaProcessosLoteVinculados = verificaSeExisteProcessosVinculados(comunicacao);
		listaProcessosLote.addAll(listaProcessosLoteVinculados);

		if (CollectionUtils.isNotEmpty(listaProcessosLote)) { 
			for (ComunicacaoIncidente ci : listaProcessosLote) {
				AndamentoProcesso andamentoProcesso = salvarAndamentoProcesso(comunicacao, data, ci.getObjetoIncidente(), codigoAndamento, usuario);
				atualizarComunicacaoIncidente(ci, andamentoProcesso);
			}
		}
	}

	private void atualizarComunicacaoIncidente(ComunicacaoIncidente ci, AndamentoProcesso andamentoProcesso) throws ServiceException {
		if (andamentoProcesso.getCodigoAndamento() == CODIGO_ANDAMENTO_ASSINATURA_CANCELADA) {
			atualizarAndamentoLancamentoIndevido(ci, andamentoProcesso);
		}

		ci.setAndamentoProcesso(andamentoProcesso);
		alterar(ci);
	}

	private void atualizarAndamentoLancamentoIndevido(ComunicacaoIncidente ci, AndamentoProcesso andamentoLancamentoIndevido) throws ServiceException {
		AndamentoProcesso andamentoComunicacaoAssinada = ci.getAndamentoProcesso();
		andamentoLancamentoIndevido.setNumeroSequenciaErrado(andamentoComunicacaoAssinada.getNumeroSequencia());

		try {
			andamentoProcessoService.alterar(andamentoLancamentoIndevido);
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao lançar andamento de lançamento indevido.", exception);
		}
	}

	private AndamentoProcesso salvarAndamentoProcesso(Comunicacao comunicacao, Date data, ObjetoIncidente<?> ob, Long codigoAndamento, String usuario) throws ServiceException {
		Setor setor = null;

		try {
			setor = usuarioService.recuperarUsuario(usuario.toUpperCase()).getSetor();
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao recuperar setor do usuário: " + usuario, exception);
		}

		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		andamentoProcesso.setCodigoAndamento(codigoAndamento);
		andamentoProcesso.setDataAndamento(data);
		andamentoProcesso.setObjetoIncidente(ob);
		andamentoProcesso.setLancamentoIndevido(true);
		Long numSequencia = andamentoProcessoService.recuperarProximoNumeroSequencia(ob);

		andamentoProcesso.setDescricaoObservacaoAndamento(construirObservacaoAndamentoComunicacao(comunicacao));
		andamentoProcesso.setNumeroSequencia(numSequencia);
		andamentoProcesso.setCodigoUsuario(usuario.toUpperCase());
		andamentoProcesso.setSetor(setor);

		try {
			andamentoProcessoService.incluir(andamentoProcesso);
		} catch (ServiceException e) {
			throw new ServiceException("Erro ao lançar o andamento no " + ob.getIdentificacao(), e);
		}

		return andamentoProcesso;
	}

	private String construirObservacaoAndamentoComunicacao(Comunicacao comunicacao) {
		ModeloComunicacao modelo = comunicacao.getModeloComunicacao();
		FlagGenericaAcessoDocumento flagAcessoDocumento = modelo.getFlagTipoAcessoDocumentoPeca();
		String observacao;

		if (flagAcessoDocumento == FlagGenericaAcessoDocumento.I) {
			observacao = null;
		} else {
			observacao = modelo.getTipoComunicacao().getDescricao() + " - " + modelo.getDscModelo();
		}

		return observacao;
	}

	@Override
	protected void preAlteracao(ComunicacaoIncidente comunicacaoIncidente) throws ServiceException {
		atualizarReferenciaAndamentoProcesso(comunicacaoIncidente);
	}

	@Override
	protected void preInclusao(ComunicacaoIncidente comunicacaoIncidente) throws ServiceException {
		atualizarReferenciaAndamentoProcesso(comunicacaoIncidente);
	}

	private void atualizarReferenciaAndamentoProcesso(ComunicacaoIncidente comunicacaoIncidente) {}
	
	@Override 
	public ComunicacaoIncidente recuperarPorAndamento(Long idAndamento) throws ServiceException{
		try {
			return this.dao.recuperarPorAndamento(idAndamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}