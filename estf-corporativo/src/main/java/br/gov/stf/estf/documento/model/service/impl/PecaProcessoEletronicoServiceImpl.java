package br.gov.stf.estf.documento.model.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.PecaProcessoEletronicoDao;
import br.gov.stf.estf.documento.model.exception.TextoInvalidoParaPecaException;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.TipoPecaProcessoService;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.documento.model.util.ConsultaDePecaProcessoEletronicoDoTextoAdapter;
import br.gov.stf.estf.documento.model.util.IConsultaPecaProcessoEletronicoDoTexto;
import br.gov.stf.estf.documento.model.util.PecaProcessoEletronicoDoTextoDynamicQuery;
import br.gov.stf.estf.documento.model.util.PecaProcessoEletronicoDynamicRestriction;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.DescendantOrder;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.EqualCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.InCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.NotEqualCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.SearchCriterion;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("pecaProcessoEletronicoService")
public class PecaProcessoEletronicoServiceImpl extends GenericServiceImpl<PecaProcessoEletronico, Long, PecaProcessoEletronicoDao> implements PecaProcessoEletronicoService {
	
	private final TipoPecaProcessoService tipoPecaProcessoService;

	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;

	@Autowired
	public PecaProcessoEletronicoServiceImpl(PecaProcessoEletronicoDao dao,
			TipoPecaProcessoService tipoPecaProcessoService) {
		super(dao);
		this.tipoPecaProcessoService = tipoPecaProcessoService;
	}

	@Override
	public void excluir(PecaProcessoEletronico entidade)
			throws ServiceException {
		excluir(entidade, true);
	}

	@Override
	public void excluir(PecaProcessoEletronico entidade, boolean exclusaoLogica)
			throws ServiceException {
		excluir(entidade, exclusaoLogica, true);
	}

	@Override
	public void excluir(PecaProcessoEletronico entidade, String motivo,
			boolean exclusaoLogica) throws ServiceException {
		excluir(entidade, motivo, exclusaoLogica, true);
	}

	@Override
	public void excluir(PecaProcessoEletronico entidade,
			boolean exclusaoLogica, boolean cancelarPDF)
			throws ServiceException {
		excluir(entidade, "", exclusaoLogica, cancelarPDF);
	}

	@Override
	public void excluir(PecaProcessoEletronico entidade, String motivo,
			boolean exclusaoLogica, boolean cancelarPDF)
			throws ServiceException {
		try {
			entidade = recuperarPorId(entidade.getId());
			if (exclusaoLogica) {
				dao.excluirPecaProcessoEletronico(entidade, motivo, cancelarPDF);
			} else {
				if (cancelarPDF) {
					dao.cancelaPdfsDaPeca(entidade, motivo);
				}

				super.excluir(entidade);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void excluirTodos(Collection<PecaProcessoEletronico> list)
			throws ServiceException {
		excluirTodos(list, true);
	}

	@Override
	public void excluirTodos(Collection<PecaProcessoEletronico> list,
			boolean exclusaoLogica) throws ServiceException {
		for (PecaProcessoEletronico pecaProcessoEletronico : list) {
			excluir(pecaProcessoEletronico, exclusaoLogica);
		}
	}

	@Override
	public PecaProcessoEletronico recuperarPeca(DocumentoTexto documentoTexto)
			throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = null;
		try {
			pecaProcessoEletronico = dao.recuperarPeca(documentoTexto
					.getDocumentoEletronico());
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return pecaProcessoEletronico;
	}

	@Override
	public List<PecaProcessoEletronico> recuperarListaPecasComunicacao(
			DocumentoComunicacao documentoComunicacao) throws ServiceException {
		List<PecaProcessoEletronico> pecaProcessoEletronico = null;

		try {
			pecaProcessoEletronico = dao
					.recuperarListaPecasComunicacao(documentoComunicacao
							.getDocumentoEletronico());
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return pecaProcessoEletronico;
	}

	@Override
	public PecaProcessoEletronico  recuperaPecaComunicacao(Long idDocumento, Long idObjetoIncidente) throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = null;

		try {
			pecaProcessoEletronico = dao.recuperaPecaComunicacao(idDocumento, idObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return pecaProcessoEletronico;
	}
	
	@Override
	public List<PecaProcessoEletronico> pesquisarPecaProcessoEletronicoDoTexto(
			IConsultaPecaProcessoEletronicoDoTexto consulta)
			throws ServiceException {
		try {
			PecaProcessoEletronicoDoTextoDynamicQuery consultaDinamica = montaConsultaDePecaProcessoEletronico(consulta);
			return dao.pesquisar(consultaDinamica);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private PecaProcessoEletronicoDoTextoDynamicQuery montaConsultaDePecaProcessoEletronico(
			IConsultaPecaProcessoEletronicoDoTexto consulta) {
		PecaProcessoEletronicoDoTextoDynamicQuery consultaDinamica = new PecaProcessoEletronicoDoTextoDynamicQuery();
		consultaDinamica.setSequencialObjetoIncidente(consulta
				.getSequencialObjetoIncidente());
		consultaDinamica.setSequencialArquivoEletronico(consulta
				.getSequencialArquivoEletronico());
		consultaDinamica.setRetirarPecasExcluidas(true);
		return consultaDinamica;
	}

	@Override
	public Long recuperarProximoNumeroDeOrdem(ObjetoIncidente<?> objetoIncidente)
			throws ServiceException {
		try {
			Long numeroDeOrdemMaximo = dao
					.recuperarNumeroDeOrdemMaximo(objetoIncidente);
			return numeroDeOrdemMaximo + 1;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public PecaProcessoEletronico consultarPecaProcessoEletronicoDoTexto(
			Texto texto) throws ServiceException,
			TextoInvalidoParaPecaException {
		ConsultaDePecaProcessoEletronicoDoTextoAdapter consulta = new ConsultaDePecaProcessoEletronicoDoTextoAdapter(texto);
		List<PecaProcessoEletronico> pecasProcessoEletronico = pesquisarPecaProcessoEletronicoDoTexto(consulta);
		return getPrimeiraPecaProcessoEletronico(pecasProcessoEletronico);
	}
	
	@Override
	public List<PecaProcessoEletronico> pecaProcessoEletronicoPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws ServiceException, DaoException {
		return dao.pecasPendenteVisualizacao(objetoIncidente);
	}	
	
	@Override
	public List<PecaProcessoEletronico> pecaProcessoEletronicoJuntadaPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws ServiceException, DaoException {
		return dao.pecasJuntadasPendenteVisualizacao(objetoIncidente);
	}	
	
	private PecaProcessoEletronico getPrimeiraPecaProcessoEletronico(
			List<PecaProcessoEletronico> pecasProcessoEletronico)
			throws TextoInvalidoParaPecaException {
		if (pecasProcessoEletronico.size() == 0) {
			return null;
		}
		if (pecasProcessoEletronico.size() == 1) {
			return pecasProcessoEletronico.get(0);
		}
		throw new TextoInvalidoParaPecaException(
				"Foi encontrada mais de uma peça ativa para o texto selecionado. Por favor, contate a informática.");
	}

	@Override
	public PecaProcessoEletronico recuperarPecaProcessoEletronico(Long id)
			throws ServiceException {
		return recuperarPorId(id);
	}

	@Override
	public List<PecaProcessoEletronico> pesquisar(
			ObjetoIncidente<?> objetoIncidente,
			TipoSituacaoPeca... tipoSituacaoPeca) throws ServiceException {
		try {
			PecaProcessoEletronicoDynamicRestriction consulta = new PecaProcessoEletronicoDynamicRestriction();
			consulta.setSequencialObjetoIncidente(objetoIncidente.getId());
			consulta.setSituacaoPeca(tipoSituacaoPeca);
			return dao.pesquisar(consulta);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<PecaProcessoEletronico> pesquisar(
			ObjetoIncidente<?> objetoIncidente, Long tipoPecaProcesso,
			Boolean pesquisarCancelados) throws ServiceException {
		try {
			PecaProcessoEletronicoDynamicRestriction consulta = new PecaProcessoEletronicoDynamicRestriction();
			consulta.setSequencialObjetoIncidente(objetoIncidente.getId());
			consulta.setTipoPecaProcesso(tipoPecaProcesso);
			if (pesquisarCancelados != null) {
				if (pesquisarCancelados) {
					consulta.setSituacaoPeca(TipoSituacaoPeca.EXCLUIDA);
				} else {
					consulta.setPossuiArquivoEletronico(true);
				}
			}
			return dao.pesquisar(consulta);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public PecaProcessoEletronico montaPecaParaJuntada(
			String siglaTipoPecaProcesso, ObjetoIncidente<?> objetoIncidente,
			TipoSituacaoPeca tipoSituacaoPeca, Setor setor)
			throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = montaPecaProcessoEletronico(
				siglaTipoPecaProcesso, objetoIncidente, tipoSituacaoPeca, setor);
		// Jubé - Inclusão de dados devido ao STF-Decisão. Dados definidos pelo
		// textual
		Long numeroDeOrdemDaPeca = recuperarProximoNumeroDeOrdem(objetoIncidente);
		pecaProcessoEletronico.setNumeroOrdemPeca(numeroDeOrdemDaPeca);
		pecaProcessoEletronico.setNumeroPagInicio(1L);
		pecaProcessoEletronico.setNumeroPagFim(1L);
		return pecaProcessoEletronico;
	}

	private PecaProcessoEletronico montaPecaProcessoEletronico(
			String siglaTipoPecaProcesso, ObjetoIncidente<?> objetoIncidente,
			TipoSituacaoPeca tipoSituacao, Setor setor) throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = new PecaProcessoEletronico();
		pecaProcessoEletronico.setObjetoIncidente(objetoIncidente);
		TipoPecaProcesso tipoPecaProcesso = tipoPecaProcessoService
				.recuperar(siglaTipoPecaProcesso);
		pecaProcessoEletronico.setTipoPecaProcesso(tipoPecaProcesso);
		pecaProcessoEletronico.setTipoSituacaoPeca(tipoSituacao);
		pecaProcessoEletronico
				.setTipoOrigemPeca(PecaProcessoEletronico.TIPO_ORIGEM_INTERNA);
		pecaProcessoEletronico.setSetor(setor);
		// COMENTADO POIS JÁ ESTÁ SENDO INSERIDO O OBJETO INCIDENTE
		// pecaProcessoEletronico.setProtocolo(
		// ObjetoIncidenteUtil.getProtocolo(objetoIncidente) );
		return pecaProcessoEletronico;
	}

	@Override
	public boolean temPecasPendenteVisualizacao(
			ObjetoIncidente<?> objetoIncidente) throws ServiceException {

		try {
			return dao.temPecasPendenteVisualizacao(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void tornarPublicasPecasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			dao.tornarPublicasPecasPendenteVisualizacao(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public PecaProcessoEletronico recuperarPecaInteiroTeor(
			DocumentoEletronico documentoEletronico) throws ServiceException {
		try {
			return dao.recuperarPeca(documentoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public PecaProcessoEletronico copiarPeca(
			PecaProcessoEletronico pecaProcessoEletronicoOriginal,
			ObjetoIncidente<?> objetoIncidenteDestino,
			Long numeroOrdemPeca, String descricaoPeca)
			throws ServiceException {
		try {
			
			PecaProcessoEletronico pecaEletronico = clonaPecaProcessoEletronico(pecaProcessoEletronicoOriginal);
			pecaEletronico.setObjetoIncidente(objetoIncidenteDestino);
			pecaEletronico.setNumeroOrdemPeca(numeroOrdemPeca);
			pecaEletronico.setDescricaoPeca(descricaoPeca);
			
			salvar(pecaEletronico);
			
			return pecaEletronico;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<PecaProcessoEletronico> pesquisarPorProcesso(Processo processo,
			Boolean incluirCancelados) throws ServiceException {
		try {
			return dao.pesquisarPorProcesso(processo, incluirCancelados);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	private PecaProcessoEletronico clonaPecaProcessoEletronico(PecaProcessoEletronico ppe){
		PecaProcessoEletronico pecaEletronico = new PecaProcessoEletronico();
		
		pecaEletronico.setNumeroOrdemPeca(ppe.getNumeroOrdemPeca());
		pecaEletronico.setNumeroPagInicio(ppe.getNumeroPagInicio());
		pecaEletronico.setNumeroPagFim(ppe.getNumeroPagFim());
		pecaEletronico.setSetor(ppe.getSetor());
		pecaEletronico.setTipoOrigemPeca(ppe.getTipoOrigemPeca());
		pecaEletronico.setDescricaoPeca(ppe.getDescricaoPeca());
		pecaEletronico.setTipoSituacaoPeca(ppe.getTipoSituacaoPeca());
		pecaEletronico.setTipoPecaProcesso(ppe.getTipoPecaProcesso());
		pecaEletronico.setObjetoIncidente(ppe.getObjetoIncidente());
		
		return pecaEletronico;
	}

	@Override
	public List<PecaProcessoEletronico> listarInteiroTeorObjetoIncidente(List<ObjetoIncidente> listaObjetoIncidente) throws ServiceException {
		
		if (listaObjetoIncidente != null && listaObjetoIncidente.size() > 0) {
			List<SearchCriterion> criterion = new ArrayList<SearchCriterion>();
			criterion.add(new EqualCriterion<Long>("tipoPecaProcesso.id", TipoPecaProcesso.CODIGO_INTEIRO_TEOR.longValue()));
			criterion.add(new NotEqualCriterion<TipoSituacaoPeca>("tipoSituacaoPeca", TipoSituacaoPeca.EXCLUIDA));
			criterion.add(new InCriterion<ObjetoIncidente>("objetoIncidente", listaObjetoIncidente));
			criterion.add(new DescendantOrder("id"));
			
			return pesquisarPorExemplo(new PecaProcessoEletronico() , criterion);
		}
		
		return null;
	}

	@Override
	public void normalizaPecasObjetoIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException, DaoException{
		List<PecaProcessoEletronico> listaPecasPendentes = pecaProcessoEletronicoJuntadaPendenteVisualizacao(objetoIncidente);
		if(listaPecasPendentes != null && !listaPecasPendentes.isEmpty()){
			for( PecaProcessoEletronico ppv : listaPecasPendentes){
				for(ArquivoProcessoEletronico doc : ppv.getDocumentos()){
					documentoEletronicoService.alterarTipoDeAcessoDoDocumento(doc.getDocumentoEletronico(), TipoDeAcessoDoDocumento.PUBLICO);
				}
			}
		}
	}

	@Override
	public List<PecaProcessoEletronico> listarPecas(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.listarPecas(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	

}
