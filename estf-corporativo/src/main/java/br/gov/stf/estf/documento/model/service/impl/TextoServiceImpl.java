package br.gov.stf.estf.documento.model.service.impl;

import static br.gov.stf.estf.entidade.documento.DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO;
import static br.gov.stf.estf.entidade.documento.DocumentoEletronico.SIGLA_DESCRICAO_STATUS_RASCUNHO;
import static br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto.ASSINAR_DIGITALMENTE;
import static br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto.LIBERAR_PARA_PUBLICACAO;
import static br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto.SUSPENDER_PUBLICACAO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jdom.JDOMException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.DocumentException;

import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.documento.model.dataaccess.TextoDao;
import br.gov.stf.estf.documento.model.exception.TextoException;
import br.gov.stf.estf.documento.model.exception.TextoInvalidoParaPecaException;
import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.ListaTextosService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.documento.model.service.exception.TextoNaoPertenceAListaDeTextosIguaisException;
import br.gov.stf.estf.documento.model.service.exception.TextoNaoPodeSerAlteradoException;
import br.gov.stf.estf.documento.model.service.exception.TransicaoDeFaseInvalidaException;
import br.gov.stf.estf.documento.model.util.AssinaturaDto;
import br.gov.stf.estf.documento.model.util.ConsultaDadosDoTextoVO;
import br.gov.stf.estf.documento.model.util.DadosDoTextoDynamicRestriction;
import br.gov.stf.estf.documento.model.util.IConsultaDadosDoTexto;
import br.gov.stf.estf.documento.model.util.TextoDynamicQuery;
import br.gov.stf.estf.documento.model.util.TextoSearchData;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.AssinaturaDigital;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TransicaoFaseTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.entidade.publicacao.CabecalhoTexto;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.estf.entidade.usuario.Responsavel;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.gov.stf.estf.processostf.model.service.IncidenteJulgamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.service.TipoIncidenteJulgamentoService;
import br.gov.stf.estf.processostf.model.service.exception.IncidenteJulgamentoException;
import br.gov.stf.estf.publicacao.model.service.FaseTextoProcessoService;
import br.gov.stf.estf.repercussaogeral.model.service.RepercussaoGeralService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.EqualCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.SearchCriterion;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.security.user.UserHolder;
import br.gov.stf.framework.util.SearchResult;
import br.jus.stf.estf.montadortexto.ByteArrayOutputStrategy;
import br.jus.stf.estf.montadortexto.DadosMontagemTexto;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;
import br.jus.stf.estf.montadortexto.OpenOfficeMontadorTextoService;
import br.jus.stf.estf.montadortexto.SpecCabecalho;
import br.jus.stf.estf.montadortexto.SpecDadosTexto;
import br.jus.stf.estf.montadortexto.SpecMarcaDagua;
import br.jus.stf.estf.montadortexto.TextoOutputException;
import br.jus.stf.estf.montadortexto.TextoOutputStrategy;
import br.jus.stf.estf.montadortexto.TextoSource;
import br.jus.stf.estf.montadortexto.tools.ByteArrayPersister;
import br.jus.stf.estf.montadortexto.tools.ByteArrayTextoSource;
import br.jus.stf.estf.montadortexto.tools.PDFUtil;

@Service("textoService")
public class TextoServiceImpl extends GenericServiceImpl<Texto, Long, TextoDao> implements TextoService {

	@Autowired
	private RepercussaoGeralService repercussaoGeralService;
	
	@Autowired
	private ArquivoProcessoEletronicoServiceExtra arquivoProcessoEletronicoServiceExtra;
	
	@Autowired
	OpenOfficeMontadorTextoService openOfficeMontadorTextoService;

	private final CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;
	private final ControleVotoService controleVotoService;
	private final DocumentoTextoService documentoTextoService;
	private final FaseTextoProcessoService faseTextoProcessoService;
	private final DocumentoEletronicoService documentoEletronicoService;
	private final ArquivoEletronicoService arquivoEletronicoService;
	private final AssinaturaDigitalService assinaturaDigitalService;
	private final ObjetoIncidenteService objetoIncidenteService;
	private final IncidenteJulgamentoService incidenteJulgamentoService;
	private final ListaTextosService listaTextosService;
	
	public TextoServiceImpl(TextoDao dao, ControleVotoService controleVotoService,
			TipoIncidenteJulgamentoService tipoJulgamentoService, DocumentoTextoService documentoTextoService,
			FaseTextoProcessoService faseTextoProcessoService, DocumentoEletronicoService documentoEletronicoService,
			ArquivoEletronicoService arquivoEletronicoService, AssinaturaDigitalService assinaturaDigitalService,
			ObjetoIncidenteService objetoIncidenteService, ProcessoService processoService,
			IncidenteJulgamentoService incidenteJulgamentoService, ListaTextosService listaTextosService,
			CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService) {
		super(dao);
		this.cabecalhoObjetoIncidenteService = cabecalhoObjetoIncidenteService;
		this.controleVotoService = controleVotoService;
		this.documentoTextoService = documentoTextoService;
		this.faseTextoProcessoService = faseTextoProcessoService;
		this.documentoEletronicoService = documentoEletronicoService;
		this.arquivoEletronicoService = arquivoEletronicoService;
		this.assinaturaDigitalService = assinaturaDigitalService;
		this.objetoIncidenteService = objetoIncidenteService;
		this.incidenteJulgamentoService = incidenteJulgamentoService;
		this.listaTextosService = listaTextosService;
	}

	public List<Texto> pesquisar(String sigla, Long numero, Long codRecurso, Long tipoRecurso, TipoTexto tipoTexto,
			Boolean orderAscDataSessao) throws ServiceException {

		List<Texto> result = null;
		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso, null);
			result = dao.pesquisar(objetoIncidente, tipoTexto, orderAscDataSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;

	}

	public List<Texto> pesquisarTextosPublicacao(String sigla, Long numeroProcessual, Long tipoRecurso,
			Long julgamento, TipoTexto tipoTexto, boolean orderAscDataSessao, Date dataSessao) throws ServiceException {
		List<Texto> result = null;
		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numeroProcessual, tipoRecurso,
					julgamento);
			result = dao.pesquisarTextosPublicacao(objetoIncidente, tipoTexto, orderAscDataSessao, dataSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;
	}

	public Texto recuperarTextoPublicacao(String sigla, Long numero, Long tipoRecurso, Long julgamento,
			TipoTexto tipoTexto) throws ServiceException {
		Texto result = null;

		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso,
					julgamento);
			result = dao.recuperarTextoPublicacao(objetoIncidente, tipoTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return result;
	}

	public Texto recuperar(String sigla, Long numero, Long tipoRecurso, Long tipoJulgamento, TipoTexto tipoTexto)
			throws ServiceException {

		Texto result = null;

		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso,
					tipoJulgamento);
			result = dao.recuperar(objetoIncidente, tipoTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return result;
	}

	public Texto recuperarUltimaDecisao(String sigla, Long numero, Long tipoRecurso, Long julgamento)
			throws ServiceException {
		List<Texto> lista = null;
		Texto texto = null;
		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso,
					julgamento);
			lista = dao.pesquisar(objetoIncidente, TipoTexto.DECISAO, true);
			if (lista != null && lista.size() > 0) {
				texto = lista.get(lista.size() - 1);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return texto;
	}

	public Texto recuperarDecisaoAta(String sigla, Long numero, Long tipoRecurso, Long julgamento, Date dataAta)
			throws ServiceException {
		Texto texto = null;
		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso,
					julgamento);
			texto = dao.recuperarDecisaoAta(objetoIncidente, dataAta);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return texto;
	}

	public List<Texto> pesquisarDecisoesAta(String sigla, Long numero, Long tipoRecurso, Long julgamento, Date dataAta)
			throws ServiceException {
		List<Texto> textos = null;
		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso,
					julgamento);
			textos = dao.pesquisarDecisoesAta(objetoIncidente, dataAta);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return textos;
	}

	@SuppressWarnings("rawtypes")
	public List<Texto> pesquisarDecisoesAta(ObjetoIncidente objetoIncidente, Date dataAta) throws ServiceException {
		List<Texto> textos = null;
		try {
			textos = dao.pesquisarDecisoesAta(objetoIncidente, dataAta);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return textos;
	}

	public Date recuperarUltimaDataSessao(String sigla, Long numero, Long tipoRecurso, Long julgamento,
			TipoTexto tipoTexto) throws ServiceException {
		Date dataSessao = null;
		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso,
					julgamento);
			List<Texto> textos = dao.pesquisar(objetoIncidente, tipoTexto, true);
			if (textos != null && textos.size() > 0) {
				dataSessao = textos.get(textos.size() - 1).getDataSessao();
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return dataSessao;
	}

	public List<Texto> pesquisar(String sigla, Long numero, Long tipoRecurso, Long tipoJulgamento, Long codigoMinistro)
			throws ServiceException {
		List<Texto> textos = null;
		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso,
					tipoJulgamento);
			textos = dao.pesquisar(objetoIncidente, codigoMinistro, null);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return textos;
	}

	public List<Texto> pesquisar(String sigla, Long numero, Long tipoRecurso, Long tipoJulgamento, TipoTexto tipoTexto,
			Long codigoMinistro, Boolean orderAscDataSessao) throws ServiceException {
		List<Texto> result = null;
		try {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(sigla, numero, tipoRecurso,
					tipoJulgamento);
			result = dao.pesquisar(objetoIncidente, codigoMinistro, orderAscDataSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;
	}

	public SearchResult<Texto> pesquisar(TextoSearchData sd) throws ServiceException {
		try {
			return dao.pesquisar(sd);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<Texto> pesquisarTexto(ObjetoIncidente<?> objetoIncidente, Ministro ministroAutenticado,
			Boolean ministroDiferenteAutenticado) throws ServiceException, TextoException {

		List<Texto> textos = new LinkedList<Texto>();

		if (ministroAutenticado == null)
			throw new TextoException("O ministro do setor autenticado deve ser informado.");

		TextoSearchData sd = new TextoSearchData();
		sd.objetoIncidente = objetoIncidente;
		sd.codigoMinistro = ministroAutenticado.getId();
		sd.ministroDiferenteAutenticado = ministroDiferenteAutenticado;

		SearchResult<Texto> sr = pesquisar(sd);

		if (sr != null && sr.getTotalResult() > 0) {
			textos = (List<Texto>) sr.getResultCollection();
		}

		// consulta dos textos para gabinente do ministro autenticado
		if (ministroDiferenteAutenticado == null || !ministroDiferenteAutenticado) {

			objetoIncidente = objetoIncidenteService.recuperarPorId(objetoIncidente.getId());

			// recupera os controles do processo
			List<ControleVoto> listaControleVoto = controleVotoService.pesquisarControleVoto(objetoIncidente, null,
					null, null, null);

			if (listaControleVoto != null && listaControleVoto.size() > 0) {

				for (ControleVoto controle : listaControleVoto) {
					boolean contem = false;

					if (textos != null) {
						for (Texto texto : textos) {
							// verifica se possui o texto no controle de
							// voto
							if (controle.getTexto() != null && controle.getTexto().getId().equals(texto.getId())) {
								contem = true;
								break;
							}
						}
					}
					if (!contem) {
						// cria um instancia de texto para a ocorrencia do
						// controle de voto
						textos.add(controle.getInstanciaTexto());
					}
				}

			}

			List<TipoTexto> listaTipoTexto = recuperarTipoTextoPadrao(objetoIncidente, ministroAutenticado);
			List<Texto> listaTextoPadrao = getInstanciaTextos(objetoIncidente, ministroAutenticado, listaTipoTexto);
			;

			if (listaTextoPadrao != null && listaTextoPadrao.size() > 0) {
				for (Texto textoPadrao : listaTextoPadrao) {

					boolean contem = false;
					if (textos != null) {
						for (Texto texto : textos) {
							if (textoPadrao.getTipoTexto().equals(texto.getTipoTexto())) {
								contem = true;
							}
						}
					}

					if (!contem) {
						textos.add(textoPadrao);
					}
				}

			}

		}

		Collections.sort(textos, new NomeMinistroTextoComparator());
		return textos;
	}

	private static class NomeMinistroTextoComparator implements Comparator<Texto> {
		public int compare(Texto obj, Texto obj2) {
			if (obj == null || obj2 == null)
				return 0;

			return obj2.getMinistro().getNome().compareTo(obj.getMinistro().getNome());
		}
	}

	/**
	 * método reponsavel por instanciar objetos de tipo de textos
	 * 
	 * @param objetoIncidente
	 * @param ministro
	 * @param tipoTexto
	 * @return
	 * @throws ServiceException
	 */
	private List<Texto> getInstanciaTextos(ObjetoIncidente<?> objetoIncidente, Ministro ministro,
			List<TipoTexto> tipoTextos) throws ServiceException {
		List<Texto> textos = new LinkedList<Texto>();
		if (tipoTextos != null && tipoTextos.size() > 0) {
			for (TipoTexto tipoTexto : tipoTextos) {
				Texto texto = new Texto();
				texto.setObjetoIncidente(objetoIncidente);
				texto.setMinistro(ministro);
				texto.setTipoTexto(tipoTexto);
				textos.add(texto);
			}
		}
		return textos;
	}

	@Deprecated
	public List<TipoTexto> recuperarTipoTextoPadrao(ObjetoIncidente<?> objetoIncidente, Ministro ministroAutenticado)
			throws ServiceException {
		List<TipoTexto> lista = new LinkedList<TipoTexto>();

		ObjetoIncidente<?> obj = objetoIncidenteService.recuperarPorId(objetoIncidente.getId());
		Processo processo = ObjetoIncidenteUtil.getProcesso(obj);
		TipoIncidenteJulgamento tipoIncidenteJulgamento = ObjetoIncidenteUtil.getTipoJulgamento(obj);

		lista.add(TipoTexto.DESPACHO);
		lista.add(TipoTexto.DECISAO_MONOCRATICA);

		// Tipo de texto padrão para o ministro relator

		// Verifica se o processo foi distribuído
		if (processo.getMinistroRelatorAtual() != null) {
			if (processo.getMinistroRelatorAtual().getId().equals(ministroAutenticado.getId())) {
				lista.add(TipoTexto.EMENTA);
				lista.add(TipoTexto.ACORDAO);
				lista.add(TipoTexto.RELATORIO);
				lista.add(TipoTexto.VOTO);
				//Jubé - Incluído em 9/9/2010 a pedido do Tiago
				lista.add(TipoTexto.OFICIO);
			}
			//O tipo "Voto Vista" é liberado para qualquer Ministro, mesmo o
			//próprio relator do processo, conforme solicitação da ISSUE 941.
			lista.add(TipoTexto.VOTO_VISTA);

			// quando o tipo de julgamento for de repercussão geral é inserido
			// os
			// tipo de texto padrao para o plenário virtual.
			if (tipoIncidenteJulgamento != null && tipoIncidenteJulgamento.getSigla() != null && (tipoIncidenteJulgamento.getSigla().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL)
					|| tipoIncidenteJulgamento.getSigla().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO))) {
				// recupera se o julgametno da repercussão geral foi finalizado.

				Boolean finalizado = repercussaoGeralService.julgamentoFinalizado(processo);
/*				Boolean finalizado = false;
				if (processo.getRepercussaoGeral()){
					List<JulgamentoProcesso> lj = julgamentoProcessoService.pesquisarJulgamentoProcesso(processo.getId(), null,
							TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL);
					JulgamentoProcesso julgamento = (lj != null && lj.size() > 0) ? lj.get(0) : null;
					
					SituacaoJulgamento situacao = (julgamento != null) ? julgamento.getSituacaoAtual() : null;
					finalizado = (situacao != null
							&& situacao.getTipoSituacaoJulgamento().getId().equals(TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo()));
				}	*/		

				if (!finalizado) {
					lista.add(TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL);
				}
				if (processo.getMinistroRelatorAtual().getId().equals(ministroAutenticado.getId())) {
					if (finalizado) {
						lista.add(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL);
					}
					lista.add(TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL);
				}
			}
		}

		return lista;

	}

	@Deprecated
	public void alterarFaseDoTexto(Texto texto, TipoTransicaoFaseTexto tipoTransicao, Set<Long> textosProcessados)
			throws ServiceException, TransicaoDeFaseInvalidaException {
		if (!textosProcessados.contains(texto.getId())) {
			// Verifica se existem textos iguais ao texto informado.
			if (texto.getTextosIguais()) {
				// Se existirem textos iguais, devemos alterar o estado de todos
				// eles. Essa pesquisa não retorna o próprio texto!
				List<Texto> textosIguais = pesquisarTextosIguaisParaTransicaoFase(texto, tipoTransicao);
				for (Texto textoIgual : textosIguais) {
					alterarFase(textoIgual, tipoTransicao, textosProcessados);
				}
			}
			alterarFase(texto, tipoTransicao, textosProcessados);
		}
	}

	/**
	 * Pesquisa os textos iguais que devem ser levados em consideração para a
	 * transição de fase.
	 * 
	 * @param texto
	 * @param tipoTransicao
	 * @return
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 * @deprecated Deverá ser movida para a classe br.jus.stf.estf.decisao.texto.service.TextoServiceImpl, pois esse método só faz sentido no contexto do Decisão, assim como seus correlatos.
	 */
	@Deprecated
	public List<Texto> pesquisarTextosIguaisParaTransicaoFase(Texto texto, TipoTransicaoFaseTexto tipoTransicao)
			throws ServiceException, TransicaoDeFaseInvalidaException {
		List<Texto> textosIguais = pesquisarTextosIguais(texto, true);
		if (!tipoTransicao.equals(TipoTransicaoFaseTexto.LIBERAR_PARA_PUBLICACAO)) {
			textosIguais = ajustarListaDeTextosIguaisParaTransicaoConjunta(texto, textosIguais, tipoTransicao);
		} else {
			textosIguais = ajustarListaParaLiberarParaPublicacao(textosIguais);
		}
		return textosIguais;
	}

	private List<Texto> ajustarListaParaLiberarParaPublicacao(List<Texto> textosIguais)
			throws TransicaoDeFaseInvalidaException {
		List<Texto> textosComStatusProibido = new ArrayList<Texto>();
		List<Texto> textosValidos = new ArrayList<Texto>();
		for (Texto texto : textosIguais) {
			if (isFaseMenor(texto.getTipoFaseTextoDocumento(), FaseTexto.ASSINADO)) {
				textosComStatusProibido.add(texto);
			} else {
				if (texto.getTipoFaseTextoDocumento().equals(FaseTexto.ASSINADO)) {
					textosValidos.add(texto);
				}
			}
		}
		if (textosComStatusProibido.size() > 0) {
			throw new TransicaoDeFaseInvalidaException(montaMensagemDeErro(textosComStatusProibido));
		}
		return textosValidos;
	}

	private String montaMensagemDeErro(List<Texto> textosComStatusProibido) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("Não é possível liberar o texto para publicação, pois os seguintes textos iguais se encontram em fases anteriores a Assinado:");
		mensagem.append("\n");
		for (Texto texto : textosComStatusProibido) {
			mensagem.append(texto.getIdentificacaoCompleta());
			mensagem.append(": ");
			mensagem.append(texto.getTipoFaseTextoDocumento().getDescricao());
			mensagem.append("\n");
		}
		return mensagem.toString();
	}

	private void alterarFase(Texto textoIgual, TipoTransicaoFaseTexto tipoTransicao, Set<Long> textosProcessados)
			throws ServiceException, TransicaoDeFaseInvalidaException {
		alterarFase(textoIgual, tipoTransicao);
		textosProcessados.add(textoIgual.getId());
	}

	/**
	 * Método que ajusta as fases dos textos que estiverem em fases diferentes
	 * do texto-base, de forma que a transição ocorra em todos os textos que
	 * estiverem com a fase inferior à fase de destino.
	 * 
	 * @param texto
	 *            Texto base da mudança
	 * @param textosIguais
	 *            Coleção de textos iguais que deve ser ajustada
	 * @param tipoTransicao
	 *            O tipo de transição executada
	 * @return A lista de textos ajustada
	 */
	private List<Texto> ajustarListaDeTextosIguaisParaTransicaoConjunta(Texto texto, List<Texto> textosIguais,
			TipoTransicaoFaseTexto tipoTransicao) {
		FaseTexto faseTextoBase = texto.getTipoFaseTextoDocumento();
		TransicaoFaseTexto transicao = getTransicaoDoTexto(texto, tipoTransicao);
		boolean isTransicaoProgressiva = isFaseDoTextoMenorIgualQueFaseDaTransicao(texto, transicao);
		List<Texto> textosIguaisAjustados = new ArrayList<Texto>();
		for (Texto textoIgual : textosIguais) {
			// Modificação. Agora apenas os textos que estiverem com fase menor
			// terão a fase ajustada. Os de fase igual ficarão inalterados.
			if ((isTransicaoProgressiva && isFaseMenor(textoIgual.getTipoFaseTextoDocumento(), transicao.destino))
					|| (!isTransicaoProgressiva && isTextoIgualPodeRegredirDeFase(textoIgual, transicao, faseTextoBase))) {
				// Iguala a fase de texto para a fase do texto-base.
				textoIgual.setTipoFaseTextoDocumento(faseTextoBase);
				textosIguaisAjustados.add(textoIgual);
			}
		}
		return textosIguaisAjustados;
	}

	private boolean isTextoIgualPodeRegredirDeFase(Texto textoIgual, TransicaoFaseTexto transicao,
			FaseTexto faseTextoBase) {
		return !isFaseDoTextoMenorIgualQueFaseDaTransicao(textoIgual, transicao)
				&& isFaseMenorOuIgual(textoIgual.getTipoFaseTextoDocumento(), faseTextoBase);

	}

	/**
	 * Verifica se a transição é progressiva ou regressiva. Caso a fase do texto
	 * seja menor que a fase de destino, é progressiva. Caso contrário, é
	 * regressiva.
	 * 
	 * @param textoIgual
	 * @param transicao
	 * @return
	 */
	private boolean isFaseDoTextoMenorIgualQueFaseDaTransicao(Texto textoIgual, TransicaoFaseTexto transicao) {
		return isFaseMenorOuIgual(textoIgual.getTipoFaseTextoDocumento(), transicao.destino);
	}

	private boolean isFaseMenorOuIgual(FaseTexto faseTextoBase, FaseTexto faseTextoComparada) {
		return faseTextoBase.compareTo(faseTextoComparada) <= 0;
	}

	private boolean isFaseMenor(FaseTexto faseTextoBase, FaseTexto faseTextoComparada) {
		return faseTextoBase.compareTo(faseTextoComparada) < 0;
	}

	/**
	 * @throws TransicaoDeFaseInvalidaException
	 */
	@Override
	@Transactional
	public void alterarFase(Texto texto, TipoTransicaoFaseTexto tipoTransicao) throws ServiceException,
			TransicaoDeFaseInvalidaException {

		// -------------------------------------------------------------------------------------
		TransicaoFaseTexto transicao = getTransicaoDoTexto(texto, tipoTransicao);

		if (transicao == null)
			throw new TransicaoDeFaseInvalidaException(montaMensagemDeTransicaInvalida(texto, tipoTransicao));
			
		// -------------------------------------------------------------------------------------
		// Registrando o histórico da fase atual em FaseTextoProcesso...
		FaseTextoProcesso faseTextoProcesso = new FaseTextoProcesso();
		faseTextoProcesso.setTipoFaseTextoDocumento(texto.getTipoFaseTextoDocumento());
		faseTextoProcesso.setTipoFaseTextoDocumentoDestino(transicao.destino);
		faseTextoProcesso.setArquivoEletronico(recuperarArquivoEletronico(texto, faseTextoProcesso));
		faseTextoProcesso.setCabecalhoTexto(buildCabecalho(texto, tipoTransicao));
		faseTextoProcesso.setDocumentoEletronico(recuperarDocumentoGravado(texto, tipoTransicao));
		faseTextoProcesso.setDataTransicao(new Date());
		faseTextoProcesso.setTexto(texto);
		faseTextoProcessoService.salvar(faseTextoProcesso);
		
		List<FaseTexto> listaDeFasesParaResetarLiberacaoAntecipada = Arrays.asList(FaseTexto.EM_ELABORACAO,FaseTexto.EM_REVISAO);
		if (transicao != null && transicao.destino != null && listaDeFasesParaResetarLiberacaoAntecipada.contains(transicao.destino))
			texto.setLiberacaoAntecipada(false);
		
		// -------------------------------------------------------------------------------------
		// Alterando Status de Texto e Documento...
		texto.setTipoFaseTextoDocumento(transicao.destino);
		salvar(texto);
		alteraSituacaoDoDocumentoTexto(texto, transicao);
	}
	
	/**
	 * @throws TransicaoDeFaseInvalidaException
	 */
	@Override
	public void alterarFase(Texto texto, TipoTransicaoFaseTexto tipoTransicao, String observacao, Responsavel responsavel) throws ServiceException, TransicaoDeFaseInvalidaException {
		this.alterarFase(null,texto, tipoTransicao, observacao, responsavel);
	}
	
	/**
	 * 
	 * @param assinatura  objeto completo da assinatura 
	 * @param texto  - mantido por retrocompatibilidade 
	 * @param tipoTransicao
	 * @param observacao - mantido por retrocompatibilidade
	 * @param responsavel
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 */
	@Override
	public void alterarFase(AssinaturaDto assinatura, Texto texto, TipoTransicaoFaseTexto tipoTransicao, String observacao, Responsavel responsavel) throws ServiceException,
		TransicaoDeFaseInvalidaException {		

		// -------------------------------------------------------------------------------------
		TransicaoFaseTexto transicao = getTransicaoDoTexto(texto, tipoTransicao);
		if (transicao == null) {
			throw new TransicaoDeFaseInvalidaException(montaMensagemDeTransicaInvalida(texto, tipoTransicao));
		}
		// -------------------------------------------------------------------------------------
		String assinadorCertificado = this.getInfoUsuarioAssinadorCertificado(assinatura, tipoTransicao);
		
		// Registrando o histórico da fase atual em FaseTextoProcesso...
		FaseTextoProcesso faseTextoProcesso = new FaseTextoProcesso();
		faseTextoProcesso.setTipoFaseTextoDocumento(texto.getTipoFaseTextoDocumento());
		faseTextoProcesso.setTipoFaseTextoDocumentoDestino(transicao.destino);
		faseTextoProcesso.setArquivoEletronico(recuperarArquivoEletronico(texto, faseTextoProcesso));
		faseTextoProcesso.setCabecalhoTexto(buildCabecalho(texto, tipoTransicao));
		faseTextoProcesso.setDocumentoEletronico(recuperarDocumentoGravado(texto, tipoTransicao));
		faseTextoProcesso.setDataTransicao(new Date());
		faseTextoProcesso.setTexto(texto);
		faseTextoProcesso.setObservacao(observacao);
		faseTextoProcesso.setAssinadorCertificado(assinadorCertificado);
		faseTextoProcessoService.salvar(faseTextoProcesso);

		// Salvando o responsável
		try {
			
			if (responsavel != null)
				texto.setResponsavel(responsavel);
			
			dao.salvar(texto);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<FaseTexto> listaDeFasesParaResetarLiberacaoAntecipada = Arrays.asList(FaseTexto.EM_ELABORACAO,FaseTexto.EM_REVISAO);
		if (transicao != null && transicao.destino != null && listaDeFasesParaResetarLiberacaoAntecipada.contains(transicao.destino))
			texto.setLiberacaoAntecipada(false);
		
		// -------------------------------------------------------------------------------------
		// Alterando Status de Texto e Documento...
		texto.setTipoFaseTextoDocumento(transicao.destino);
		salvar(texto);
		alteraSituacaoDoDocumentoTexto(texto, transicao);
	}

	/**
	 * Retorna string com informações do usuario do certificado digital utilizado para assinar
	 * DECISAO-2489
	 * @param assinatura
	 * @param tipoTransicao
	 * @return
	 */
	String getInfoUsuarioAssinadorCertificado(AssinaturaDto assinatura, TipoTransicaoFaseTexto tipoTransicao) {
		String infoUsuarioAssinadorCertificado = null;
		if(assinatura != null && TipoTransicaoFaseTexto.ASSINAR_DIGITALMENTE.equals(tipoTransicao)){
			String usuario        = null;
			Date dataCarimboTempo = assinatura.getDataCarimboTempo();
			String dataAssinatura =  null;
			if(dataCarimboTempo==null){
				Date now = DataUtil.getNowDate();
				dataAssinatura = DataUtil.date2String(now, true);
			}else{
				dataAssinatura = DataUtil.date2String(dataCarimboTempo, true);				
			}
			if(assinatura.getSubjectDN()==null){
				usuario = assinatura.getUsuarioLogado();
			}else{
				usuario = this.getAliasUsuarioCertificado(assinatura);
			}			
			infoUsuarioAssinadorCertificado = usuario+" "+dataAssinatura;			
		}
		return infoUsuarioAssinadorCertificado;
	}

	String getAliasUsuarioCertificado(AssinaturaDto assinatura) {
		String subjectDN = assinatura.getSubjectDN();
		String[] textoSeparado = subjectDN.split(",");
		textoSeparado = textoSeparado[0].split("=");
		textoSeparado = textoSeparado[1].split(":");
		return textoSeparado[0];
	}

	private TransicaoFaseTexto getTransicaoDoTexto(Texto texto, TipoTransicaoFaseTexto tipoTransicao) {
		return tipoTransicao.getTransicao(texto.getTipoFaseTextoDocumento());
	}

	private CabecalhoTexto buildCabecalho(Texto texto, TipoTransicaoFaseTexto tipoTransicao) throws ServiceException {
		if (tipoTransicao.equals(TipoTransicaoFaseTexto.LIBERAR_PARA_PUBLICACAO)
				|| tipoTransicao.equals(TipoTransicaoFaseTexto.SUSPENDER_PUBLICACAO)) {
			// Se texto estiver em alguma das duas fases acima, devemos apontar
			// o
			// cabecalho da nova fase para o cabecalho
			// gerado na Assinatura
			FaseTextoProcesso faseTextoProcesso = faseTextoProcessoService.recuperarUltimaFaseDoTexto(texto);
			// A cópia só deve ser realizada se existir alguma fase para o
			// texto.
			if (faseTextoProcesso != null) {
				return faseTextoProcesso.getCabecalhoTexto();
			}
		}
		// Caso contrário, devemos recuperar o XML do cabecalho criando uma
		// nova instancia de CabecalhoTexto e
		// associa-la a nova fase do texto.
		CabecalhoTexto cabecalhoTexto = new CabecalhoTexto();
		byte[] xmlCabecalho = cabecalhoObjetoIncidenteService
				.recuperarXmlCabecalhos(texto.getObjetoIncidente().getId());
		try {
			cabecalhoTexto.setXml(new String(xmlCabecalho, "UTF-8"));
			return cabecalhoTexto;
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}

	private String montaMensagemDeTransicaInvalida(Texto texto, TipoTransicaoFaseTexto tipoTransicao) {
		return "O texto " + texto.getIdentificacao() + " não pode fazer a transição " + tipoTransicao.getDescricao()
				+ ", pois se encontra na fase " + texto.getTipoFaseTextoDocumento().getDescricao();
	}

	/**
	 * Recupera uma instância de Arquivo Eletrônico seguindo a seguinte regra:
	 * Se o arquivo eletrônico associado ao texto selecionado for igual ao
	 * arquivo eletrônico gravado na fase anterior, retornar o mesmo arquivo
	 * gravado na fase anterior, senão, retornar uma nova cópia do arquivo
	 * associado ao texto selecionado;
	 */
	private ArquivoEletronico recuperarArquivoEletronico(Texto texto, FaseTextoProcesso faseAtual)
			throws ServiceException {
		// Recuperando Arquivo Eletronico atual... Associado ao texto.
		ArquivoEletronico arquivoAtual = arquivoEletronicoService.recuperarArquivoEletronico(texto.getArquivoEletronico().getId());
		// Recuperando a ultima fase pela qual o texto passou...
		FaseTextoProcesso faseTextoProcesso = faseTextoProcessoService.recuperarUltimaFaseDoTexto(texto);
		if (faseTextoProcesso == null) {
			// Se não existe nenhuma fase registrada, devemos copiar o arquivo
			// atual para grava um novo registro
			faseAtual.setHashArquivoEletronico(arquivoAtual.hash());
			return copiarArquivoAtual(arquivoAtual);
		}
		// Recuperando o Hash do Arquivo Eletronico construido na fase passada.
		byte[] hashUltimoArquivo = faseTextoProcesso.getHashArquivoEletronico();
		if (conteudosIguais(arquivoAtual.hash(), hashUltimoArquivo)) {
			// Se o arquivo atual for igual ao registrado na fase passada,
			// devemos retornar
			// o arquivo registrado, sem gravar um novo registro
			faseAtual.setHashArquivoEletronico(faseTextoProcesso.getHashArquivoEletronico());
			return faseTextoProcesso.getArquivoEletronico();
		} else {
			// Se o arquivo atual for diferente do registrado na fase passada,
			// devemos copiar
			// o arquivo atual para grava um novo registro
			faseAtual.setHashArquivoEletronico(arquivoAtual.hash());
			return copiarArquivoAtual(arquivoAtual);
		}
	}

	/**
	 * Verifica se o conteúdo de dois arquivos são iguais, baseado no hash de
	 * cada um deles.
	 */
	private boolean conteudosIguais(byte[] hashArquivoAtual, byte[] hashUltimoArquivo) {
		if (hashUltimoArquivo != null && hashArquivoAtual != null) {
			return Arrays.equals(hashUltimoArquivo, hashArquivoAtual);
		}
		return false;
	}

	private DocumentoEletronico recuperarDocumentoGravado(Texto texto, TipoTransicaoFaseTexto tipoTransicao)
			throws ServiceException {
		if (tipoTransicao == ASSINAR_DIGITALMENTE) {
			return documentoEletronicoService.recuperarUltimoDocumentoEletronicoAtivo(texto);
		}
		if (tipoTransicao == LIBERAR_PARA_PUBLICACAO || tipoTransicao == SUSPENDER_PUBLICACAO) {
			FaseTextoProcesso faseTextoProcesso = faseTextoProcessoService.recuperarUltimaFaseDoTexto(texto);
			if (faseTextoProcesso == null) {
				DocumentoEletronico documentoEletronico = documentoEletronicoService
						.recuperarUltimoDocumentoEletronicoAtivo(texto);
				if (documentoEletronico == null && tipoTransicao == LIBERAR_PARA_PUBLICACAO) {
					throw new ServiceException("Não é possível liberar para publicação textos sem assinatura digital. " +											   
											   "Cancele a assinatura e libere o texto para assinatura digital do Ministro.");
				}			
				return documentoEletronico;
			} else {
				return faseTextoProcesso.getDocumentoEletronico();
			}
		}
		return null;
	}

	/**
	 * Copia um dado Arquivo Eletronico. Retorna uma nova instancia com o mesmo
	 * conteudo.
	 */
	private ArquivoEletronico copiarArquivoAtual(ArquivoEletronico aeAtual) throws ServiceException {
		// Instancia um novo Arquivo Eletronico: a copia do atual.
		ArquivoEletronico copiaAeAtual = new ArquivoEletronico();
		try {
			// Copia todas as propriedades.
			BeanUtils.copyProperties(aeAtual, copiaAeAtual);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		// A chave deve ser null para que o Hibernate cadastre um novo registro.
		copiaAeAtual.setId(null);

		return arquivoEletronicoService.salvar(copiaAeAtual);
	}

	private void alteraSituacaoDoDocumentoTexto(Texto texto, TransicaoFaseTexto transicao) throws ServiceException {
		// Caso não tenha alteração, não realizará alterações para o textual.
		// (Caso de republicar)
		if (transicao.tipoSituacaoDocumento != null) {
			DocumentoTexto documentoTexto = documentoTextoService.recuperarDocumentoTextoMaisRecenteNaoCancelado(texto);
			if (documentoTexto != null) {
				documentoTexto.setTipoSituacaoDocumento(transicao.tipoSituacaoDocumento);
				documentoTextoService.alterar(documentoTexto);
			}
		}
	}

	public void verificaTextoLiberadoParaPublicacao(Texto texto) throws ServiceException,
			TextoNaoPodeSerAlteradoException {
		Collection<String> motivosDeNaoLiberacao = new ArrayList<String>();
		verificaTextoPublicado(motivosDeNaoLiberacao, texto);
		validaLiberacaoDeTextosIguais(texto, motivosDeNaoLiberacao);
		if (motivosDeNaoLiberacao.size() > 0) {
			throw new TextoNaoPodeSerAlteradoException(montaMotivosDeNaoLiberacao(motivosDeNaoLiberacao));
		}
	}

	private void validaLiberacaoDeTextosIguais(Texto texto, Collection<String> motivosDeNaoLiberacao)
			throws ServiceException {
		List<Texto> textoIguais = pesquisarTextosIguais(texto, true);
		verificaLiberacaoDosTextosIguais(motivosDeNaoLiberacao, textoIguais);
	}

	private void verificaLiberacaoDosTextosIguais(Collection<String> motivosDeNaoLiberacao, List<Texto> textoIguais) {
		for (Texto textoIgual : textoIguais) {
			if (!textoIgual.getTipoTexto().equals(TipoTexto.REVISAO_DE_APARTES)) {
				verificaTextoPublicado(motivosDeNaoLiberacao, textoIgual);
				verificaSituacoesDosDocumentos(motivosDeNaoLiberacao, textoIgual);
			}
		}
	}

	private void verificaTextoPublicado(Collection<String> motivosDeNaoLiberacao, Texto textoIgual) {
		if (textoIgual.getPublico()) {
			adicionaMotivoDePublicacao(motivosDeNaoLiberacao, textoIgual);
		}
	}

	private void verificaSituacoesDosDocumentos(Collection<String> motivosDeNaoLiberacao, Texto textoIgual) {
		for (DocumentoTexto documentoTexto : textoIgual.getDocumentosTexto()) {
			if (restricoesParaAlteracaoDoTexto.contains(documentoTexto.getTipoSituacaoDocumento().getSigla())) {
				adicionaMotivoDeSituacaoDeDocumento(motivosDeNaoLiberacao, documentoTexto);
			}
		}
	}

	private String montaMotivosDeNaoLiberacao(Collection<String> motivos) {
		StringBuilder descricaoDosMotivos = new StringBuilder();
		for (String motivo : motivos) {
			descricaoDosMotivos.append(motivo);
			descricaoDosMotivos.append("\n");
		}
		return descricaoDosMotivos.toString();
	}

	private void adicionaMotivoDeSituacaoDeDocumento(Collection<String> motivosDeNaoLiberacao,
			DocumentoTexto documentoTexto) {
		String mensagemDeErro = " - Texto " + documentoTexto.getTipoSituacaoDocumento().getDescricao();
		motivosDeNaoLiberacao.add(montaMensagemDeErroDePublicacaoDoTexto(documentoTexto.getTexto(), mensagemDeErro));
	}

	private void adicionaMotivoDePublicacao(Collection<String> motivosDeNaoLiberacao, Texto texto) {
		motivosDeNaoLiberacao.add(montaMensagemDeTextoLiberadoParaPublicacao(texto));
	}

	private String montaMensagemDeTextoLiberadoParaPublicacao(Texto texto) {
		String mensagemDeErro = " - Texto liberado para Publicação";
		return montaMensagemDeErroDePublicacaoDoTexto(texto, mensagemDeErro);
	}

	private String montaMensagemDeErroDePublicacaoDoTexto(Texto texto, String mensagemDeErro) {
		StringBuilder sb = new StringBuilder();
		Processo processo = ObjetoIncidenteUtil.getProcesso(texto.getObjetoIncidente());
		TipoRecurso tipoJulgamento = ObjetoIncidenteUtil.getTipoRecurso(texto.getObjetoIncidente());

		sb.append(processo.getClasseProcessual().getId());
		if (tipoJulgamento == null) {
			sb.append(" - ");
		} else {
			sb.append(tipoJulgamento.getSigla());
		}
		sb.append(processo.getNumeroProcessual());
		sb.append(mensagemDeErro);
		return sb.toString();
	}

	private static final Collection<TipoSituacaoDocumento> restricoesParaAlteracaoDoTexto = new HashSet<TipoSituacaoDocumento>(
			Arrays.asList(TipoSituacaoDocumento.REJEITADO_MINISTRO, TipoSituacaoDocumento.REVISADO,
					TipoSituacaoDocumento.ASSINADO_DIGITALMENTE, TipoSituacaoDocumento.LIBERADO_PARA_REVISAO,
					TipoSituacaoDocumento.ASSINADO_MANUALMENTE));

	public List<FaseTextoProcesso> recuperarVersoesTexto(Long textoId) throws ServiceException {
		try {
			return dao.recuperarVersoesTexto(textoId);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public boolean isTextoJaRegistradoParaProcesso(IConsultaDadosDoTexto consulta) throws ServiceException {
		List<Texto> textos = pesquisar(consulta);
		return !textos.isEmpty();
	}

	public Texto pesquisarEmentaParaTextoIgual(IConsultaDadosDoTexto consulta) throws ServiceException {
		try {
			DadosDoTextoDynamicRestriction consultaDinamica = montaConsultaDeTextoDoProcesso(consulta);
			consultaDinamica.setSequencialDoArquivoDiferente(consulta.getSequencialDoArquivoEletronico());
			List<Texto> textos = dao.pesquisarTextoDoProcesso(consultaDinamica);
			if (textos.isEmpty()) {
				return null;
			}
			return textos.get(0);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private DadosDoTextoDynamicRestriction montaConsultaDeTextoDoProcesso(IConsultaDadosDoTexto consulta)
			throws ServiceException {
		DadosDoTextoDynamicRestriction consultaDinamica = new DadosDoTextoDynamicRestriction();
		consultaDinamica.setSequencialObjetoIncidente(consulta.getSequencialObjetoIncidente());
		consultaDinamica.setCodigoDoMinistro(consulta.getCodigoDoMinistro(), consulta.isIncluirPresidencia());
		consultaDinamica.setTipoDeTexto(consulta.getTipoDeTexto());
		consultaDinamica.setTiposDeTextoParaExcluir(consulta.getTiposDeTextoParaExcluir());
		return consultaDinamica;
	}

	public List<Texto> pesquisar(IConsultaDadosDoTexto consulta) throws ServiceException {
		try {
			DadosDoTextoDynamicRestriction consultaDinamica = montaConsultaDeTextoDoProcesso(consulta);
			List<Texto> textos = dao.pesquisarTextoDoProcesso(consultaDinamica);
			return textos;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Transactional(rollbackFor = { ServiceException.class, TextoNaoPertenceAListaDeTextosIguaisException.class })
	public void tornarTextoSemelhante(Texto texto) throws ServiceException,
			TextoNaoPertenceAListaDeTextosIguaisException {
		// Recuperando os textos iguais ao informado...
		// Atentar que o método perquisarTextosIguais exclui da lista
		// o texto passado como parâmetro
		List<Texto> textosIguais = pesquisarTextosIguais(texto, true);
		if (textosIguais.size() > 0) {

			// Se existir apenas um texto igual ao atual, marcá-lo
			// como não igual
			if (textosIguais.size() == 1){
				Texto textoIgual = textosIguais.get(0);
				textoIgual.setTextosIguais(false);
				alterar(textoIgual);
			}

			// Apontar o texto para uma cópia do arquivo eletrônico atual
			copiarArquivoParaTexto(texto, texto.getArquivoEletronico());
			texto.setTextosIguais(false);

			//texto.setTipoFaseTextoDocumento(texto.getTipoFaseTextoDocumento());
			//texto.setPublico(false);
			/*texto.setDataComposicaoParcial(null);
			texto.setSalaJulgamento(false);
			texto.setDataSessao(null);
			texto.setSequenciaVoto(0L);*/
			alterar(texto);

		} else {
			// Se não existirem textos iguais, lançar exceção.
			throw new TextoNaoPertenceAListaDeTextosIguaisException();
		}

	}

	public void copiarArquivoParaTexto(Texto texto, ArquivoEletronico arquivoEletronicoOriginal)
			throws ServiceException {
		texto.setArquivoEletronico(copiarArquivoAtual(arquivoEletronicoOriginal));
	}

	/**
	 * Recupera o sequencial que será utilizado para gravar o
	 * DocumentoEletronico daquele texto.
	 */
	public Long recuperarSequencialDoDocumentoEletronico(Texto texto) throws ServiceException {
		DocumentoEletronico documentoEletronico = documentoEletronicoService
				.recuperarUltimoDocumentoEletronicoAtivo(texto);
		if (documentoEletronico != null) {
			if (!documentoEletronico.getDescricaoStatusDocumento().equals(SIGLA_DESCRICAO_STATUS_ASSINADO)) {
				// Se existe um documento ativo e ele não foi assinado, devemos
				// substitui-lo. Dessa forma, vamos retornar o id já existente.
				return documentoEletronico.getId();
			}
		}
		// Nos outros casos, o documento deverá ser um novo DocumentoEletronico.
		// Se estiver assinado, o documento deverá ser cancelado antes.
		return documentoEletronicoService.recuperarProximaSequenceDocumentoEletronico();
	}

	@Deprecated
	@Transactional
	public void assinarTexto(Texto texto, TipoDocumentoTexto tipo, byte[] conteudoAssinado, byte[] carimbo,
			Date dataCarimboTempo, String siglaSistema) throws ServiceException, TransicaoDeFaseInvalidaException {
		assinarTexto(texto, tipo, conteudoAssinado, carimbo, dataCarimboTempo, siglaSistema, null);
	}

	private void assinaTexto(AssinaturaDto assinatura, DocumentoEletronico documentoEletronico)
			throws ServiceException, TransicaoDeFaseInvalidaException {
		// Criando Assinatura Digital persistente... A Assinatura já foi feita
		// pela Servlet Responsável.
		AssinaturaDigital assinaturaDigital = new AssinaturaDigital();
		assinaturaDigital.setDataCarimboTempo(assinatura.getDataCarimboTempo());
		assinaturaDigital.setDocumentoEletronico(documentoEletronico);
		assinaturaDigital.setAssinatura(documentoEletronico.getArquivo());
		assinaturaDigital.setCarimboTempo(assinatura.getCarimbo());
		assinaturaDigital.setTipoAssinatura(assinatura.getTipoAssinatura());

		// Salvando Assinatura Digital...
		assinaturaDigitalService.salvar(assinaturaDigital);

		// Alterando a fase do texto.
		// Ele deve chamar o método alterarFase, pois essa mudança é feita para
		// cada um dos textos iguais separadamente.
		this.alterarFase(assinatura,assinatura.getTexto(), TipoTransicaoFaseTexto.ASSINAR_DIGITALMENTE, assinatura.getObservacao(), null);
	}

	
	@Deprecated
	@Transactional
	public void assinarTexto(Texto texto, TipoDocumentoTexto tipo, byte[] conteudoAssinado, byte[] carimbo,
			Date dataCarimboTempo, String siglaSistema, Long sequencialDocumentoEletronico) throws ServiceException,
			TransicaoDeFaseInvalidaException {
		AssinaturaDto assinatura = new AssinaturaDto();
		assinatura.setTexto(texto);
		assinatura.setCarimbo(carimbo);
		assinatura.setConteudoAssinado(conteudoAssinado);
		assinatura.setDataCarimboTempo(dataCarimboTempo);
		assinatura.setSequencialDocumentoEletronico(sequencialDocumentoEletronico);
		assinatura.setSiglaSistema(siglaSistema);
		assinatura.setTipo(tipo);
		assinatura.setUsuarioLogado(UserHolder.get().getUsername().toUpperCase());
		assinarTexto(assinatura);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void assinarTexto(AssinaturaDto assinatura) throws ServiceException, TransicaoDeFaseInvalidaException{
		DocumentoEletronico documentoEletronico = criarRascunhoDeDocumentoEletronico(assinatura);
		documentoEletronicoService.salvar(documentoEletronico);
		assinaTexto(assinatura, documentoEletronico);
	}

	private DocumentoEletronico criarRascunhoDeDocumentoEletronico(AssinaturaDto assinatura) throws ServiceException {
		DocumentoEletronico documentoEletronico = montaDocumentoEletronicoDeRascunho(assinatura);
		return documentoEletronico;
	}

	private DocumentoEletronico montaDocumentoEletronicoDeRascunho(AssinaturaDto assinatura) throws ServiceException {
		// Recuperando o último Documento Eletrônico ativo (não cancelado) para
		// o Texto corrente...
		Texto texto = assinatura.getTexto();
		DocumentoEletronico documentoEletronico = documentoEletronicoService.recuperarUltimoDocumentoEletronicoAtivo(texto);

		if (documentoEletronico != null) {
			if (documentoEletronico.getDescricaoStatusDocumento().equals(SIGLA_DESCRICAO_STATUS_ASSINADO)) {
				// Se existe um documento ativo, mas ele está assinado, devemos
				// cancela-lo e criar um novo.
				DocumentoTexto documentoTexto = documentoTextoService.recuperar(documentoEletronico);
				List<DocumentoTexto> singletonList = Collections.singletonList(documentoTexto);
				String motivoCancelamento = "Documento cancelado por nova assinatura no eSTF-Decisão.";
				documentoTextoService.cancelarDocumentos(singletonList,motivoCancelamento,TipoSituacaoDocumento.CANCELADO_AUTOMATICAMENTE);
				documentoEletronico = novoDocumentoEletronico(assinatura);
			} else {
				// Se existe um documento ativo e ele não foi assinado, devemos
				// substitui-lo.
				substituirConteudoDoDocumentoEletronico(documentoEletronico, assinatura);
			}
		} else {
			// Se não existe nenhum documento eletrônico ativo, devemos criar um
			// novo
			documentoEletronico = novoDocumentoEletronico(assinatura);
		}
		return documentoEletronico;
	}
	
//	private void criaDocumentoTexto(Texto texto, TipoDocumentoTexto tipo, DocumentoEletronico documentoEletronico)
//			throws ServiceException {
//		// Criando Documento Texto... Associação entre Documento Eletronico e
//		// Texto.
//		DocumentoTexto documentoTexto = new DocumentoTexto();
//		documentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.REVISADO);
//		documentoTexto.setTipoDocumentoTexto(tipo);
//		documentoTexto.setUsuarioRevisao(UserHolder.get().getUsername().toUpperCase());
//		documentoTexto.setDataRevisao(new Date());
//		documentoTexto.setDocumentoEletronico(documentoEletronico);
//		documentoTexto.setTexto(texto);
//
//		// Salvando o documento texto criado...
//		documentoTextoService.incluir(documentoTexto);
//	}
	

	private void criaDocumentoTexto(AssinaturaDto assinatura, DocumentoEletronico documentoEletronico)
			throws ServiceException {
		// Criando Documento Texto... Associação entre Documento Eletronico e
		// Texto.
		DocumentoTexto documentoTexto = new DocumentoTexto();
		documentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.REVISADO);
		documentoTexto.setTipoDocumentoTexto(assinatura.getTipo());
		documentoTexto.setUsuarioRevisao(assinatura.getUsuarioLogado());
		documentoTexto.setDataRevisao(new Date());
		documentoTexto.setDocumentoEletronico(documentoEletronico);
		documentoTexto.setTexto(assinatura.getTexto());

		// Salvando o documento texto criado...
		documentoTextoService.incluir(documentoTexto);
	}



	@Deprecated
	@Transactional
	public void cancelarAssinatura(Texto texto, TipoTransicaoFaseTexto transicao, Set<Long> textosProcessados)
			throws TransicaoDeFaseInvalidaException, TextoInvalidoParaPecaException, ServiceException {
		if (!textosProcessados.contains(texto.getId())) {
			List<Texto> textosParaCancelar = new ArrayList<Texto>();
			textosParaCancelar.add(texto);
			List<Texto> textosIguais = pesquisarTextosIguaisParaTransicaoFase(texto, transicao);
			textosParaCancelar.addAll(textosIguais);
			for (Texto textoParaCancelar : textosParaCancelar) {
				cancelarAssinaturaDoTexto(textoParaCancelar);
				// Se não nenhuma transição for informada, setar a default:
				// cancelar
				// assinatura.
				if (transicao == null) {
					transicao = TipoTransicaoFaseTexto.CANCELAR_ASSINATURA;
				}
				// Realizando transição de estados...
				alterarFase(textoParaCancelar, transicao, textosProcessados);
			}
		}
	}

	public void cancelarAssinaturaDoTexto(Texto textoParaCancelar)
			throws ServiceException, TextoInvalidoParaPecaException {
		FaseTexto faseTexto = textoParaCancelar.getTipoFaseTextoDocumento();
		// Só devemos cancelar a assinatura se o texto foi efetivamente
		// assinado
		// (Fases "Assinado" e "Publicado")
		if (FaseTexto.fasesComTextoAssinado.contains(faseTexto)) {
			// Só devemos cancelar a assinatura se não foi excluída a
			// juntada de
			// peças. Essa exclusão já cancela a assinatura
			arquivoProcessoEletronicoServiceExtra.excluirJuntadaDePecas(textoParaCancelar, false);
			DocumentoEletronico documento = documentoEletronicoService
					.recuperarUltimoDocumentoEletronicoAtivo(textoParaCancelar);
			if (documento != null) {
				documentoEletronicoService.cancelarDocumento(documento, "Assinatura Cancelada.");
			}
		}
	}

	/**
	 * Retorna uma nova instância de Documento Eletrônico setando as
	 * propriedades de acordo com a ação em questão
	 * 
	 * @param sequencialDocumentoEletronico
	 */
	private DocumentoEletronico novoDocumentoEletronico(AssinaturaDto assinatura) throws ServiceException {

		DocumentoEletronico documentoEletronico = new DocumentoEletronico();
		popularDocumentoEletronico(documentoEletronico, assinatura);
		if (assinatura.getSequencialDocumentoEletronico()!= null) {
			// Utilizado para inclusão quando o sequencial for obtido
			// anteriormente
			documentoEletronico.setId(assinatura.getSequencialDocumentoEletronico());
			documentoEletronico.setHashValidacao(assinatura.getHashValidacao());
			documentoEletronicoService.incluirDocumentoEletronicoSQL(documentoEletronico);
		} else {
			// Inclui o documento do modo convencional
			documentoEletronicoService.salvar(documentoEletronico);
		}
		criaDocumentoTexto(assinatura, documentoEletronico);
		return documentoEletronico;

	}


	/**
	 * Substitui o contéudo de um dado Documento Eletrônico.
	 */
	private void substituirConteudoDoDocumentoEletronico(DocumentoEletronico documento,AssinaturaDto assinatura) throws ServiceException {

		popularDocumentoEletronico(documento, assinatura);
		documentoEletronicoService.salvar(documento);

	}

	
	/**
	 * Seta as propriedades de Documento Eletrônico de acordo com a ação em
	 * questão
	 */
	private void popularDocumentoEletronico(DocumentoEletronico documento, AssinaturaDto assinatura) throws ServiceException {

		// Setando propriedades básicas...
		documento.setDescricaoStatusDocumento(SIGLA_DESCRICAO_STATUS_RASCUNHO);
		documento.setTipoArquivo(TipoArquivo.PDF);
		documento.setSiglaSistema(assinatura.getSiglaSistema());
		documento.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_INTERNO);
		
		if (documento.getHashValidacao() == null || documento.getHashValidacao().isEmpty()) {
			
			if (assinatura.getHashValidacao() == null || assinatura.getHashValidacao().isEmpty())
				assinatura.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
			
			documento.setHashValidacao(assinatura.getHashValidacao());
		}

		// Setando o texto assinado...
		documento.setArquivo(assinatura.getConteudoAssinado());
	}

	public List<Texto> recuperarTextosProcessos(List<Processo> listaProcessos, List<TipoTexto> listaTipoTexto)
			throws ServiceException {
		try {
			List<Texto> textos = dao.recuperarTextosProcessos(listaProcessos, listaTipoTexto);
			return textos;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public boolean existeEmentaParaTextoIgual(ConsultaDadosDoTextoVO consulta) {
		return false;
		// Método criado porque alguem esqueceu de subir para o SVN. Por favor
		// substitua.
	}

	/**
	 * método resposnsavel por excluir um textos
	 */
	public void validaExclusaoTexto(Texto texto) throws TextoException, ServiceException {
		if (texto == null) {
			throw new TextoException("O texto deve ser informado.");
		}

		List<Texto> textosIguais = pesquisarTextosIguais(texto);
		if (textosIguais != null && textosIguais.size() >= 1) {
			throw new TextoException("O texto está em lista de textos iguais");
		}
	}

	/**
	 * Método responsavel por recuperar os textos iguais de um determinado
	 * texto. Esse método não retorna o texto-base da pesquisa.
	 */
	public List<Texto> pesquisarTextosIguais(Texto texto) throws ServiceException {
		return pesquisarTextosIguais(texto, false);
	}

	/**
	 * Método que retorna os textos iguais de um determinado texto, restringindo
	 * os textos ao ministro vinculado ao texto. Esse método não retorna o
	 * texto-base da pesquisa.
	 * 
	 * @param texto
	 *            Texto-base para a pesquisa
	 * @param limitarTextosDoMinistro
	 *            Indicação se é para restringir os textos ao Ministro
	 * @return
	 * @throws ServiceException
	 */
	public List<Texto> pesquisarTextosIguais(Texto texto, boolean limitarTextosDoMinistro) throws ServiceException {
		try {
			return dao.pesquisarTextosIguais(texto, limitarTextosDoMinistro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	public void restaurarVersaoTexto(FaseTextoProcesso faseTextoProcesso) throws ServiceException {

		Texto texto = faseTextoProcesso.getTexto();
		copiarArquivoParaTexto(texto, faseTextoProcesso.getArquivoEletronico());
		// dao.copiarConteudoArquivoEletronico(faseTextoProcesso.getArquivoEletronico(),
		// faseTextoProcesso.getTexto()
		// .getArquivoEletronico());
	}

	public List<Texto> pesquisarTextosExtratoAta(String siglaClasse, Long numProcesso, Long tipoRecurso,
			Long tipoJulgamento) throws ServiceException {
		List<Texto> textos = null;
		try {
			textos = dao.pesquisarTextosExtratoAta(siglaClasse, numProcesso, tipoRecurso, tipoJulgamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return textos;
	}

	public List<Texto> pesquisarTextosExtratoAta(Long idObjetoIncidente) throws ServiceException {
		try {
			TextoDynamicQuery consultaDinamica = new TextoDynamicQuery();
			consultaDinamica.setIdObjetoIncidente(idObjetoIncidente);
			consultaDinamica.setTipoTexto(TipoTexto.DECISAO);
			consultaDinamica.setTipoDocumentoTexto(TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_EXTRADO_ATA);
			// consultaDinamica.setOrgaoDestino(Setor.CODIGO_SETOR_COMPOSICAO_ACORDAOS);
			consultaDinamica.setTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE,
					TipoSituacaoDocumento.ASSINADO_MANUALMENTE);

			return dao.pesquisarTextosExtratoAta(consultaDinamica);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<Texto> pesquisarTextosPublicacao(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto,
			Boolean orderAscDataSessao, Date dataSessao) throws ServiceException {
		List<Texto> textos = null;

		try {
			textos = dao.pesquisarTextosPublicacao(objetoIncidente, tipoTexto, orderAscDataSessao, dataSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return textos;
	}

	public Texto recuperarDecisaoAta(ObjetoIncidente<?> objetoIncidente, Date dataSessao) throws ServiceException {
		Texto texto = null;

		try {
			texto = dao.recuperarDecisaoAta(objetoIncidente, dataSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return texto;
	}

	public Texto recuperarTextoPublicacao(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto)
			throws ServiceException {
		Texto texto = null;

		try {
			texto = dao.recuperarTextoPublicacao(objetoIncidente, tipoTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return texto;
	}

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Boolean orderAscDataSessao)
			throws ServiceException {
		List<Texto> textos = null;

		try {
			textos = dao.pesquisar(objetoIncidente, tipoTexto, orderAscDataSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return textos;
	}

	public Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto) throws ServiceException {
		try {
			return dao.recuperar(objetoIncidente, tipoTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Texto recuperarTextoEletronico(Integer idArquivoEletronico) throws ServiceException {
		try {
			return dao.recuperarTextoEletronico(idArquivoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Texto recuperar(Long idObjetoIncidente, TipoTexto tipoTexto, Long idMinistro) throws ServiceException {
		try {
			return dao.recuperar(idObjetoIncidente, tipoTexto, idMinistro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Texto recuperarTextoSemControleVoto(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws ServiceException {
		try {
			return dao.recuperarTextoSemControleVoto(objetoIncidente, tipoTexto, ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Long idMinistro) throws ServiceException {
		return recuperar(objetoIncidente.getId(), tipoTexto, idMinistro);
	}

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto... tipoTexto) throws ServiceException {
		try {
			return dao.pesquisar(objetoIncidente, tipoTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Texto> recuperarTextosReferendo(Long idObjetoIncidente) throws ServiceException {
		try {
			return dao.recuperarTextosReferendo(idObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * método responsavel por persistir texto
	 * 
	 * @author GuilhermeA
	 */
	public Texto persistir(Texto texto) throws ServiceException, TextoException {
		try {
			if (texto != null) {

				// quando o texto for novo, verifica se o mesmo pode ser
				// inserido na base de dados
				if (texto.getId() == null)
					validarNovoTexto(texto);

				// quando o objeto for do tipo IncidenteJulgamento e ainda não
				// tenha sido inserido no banco, e chamada a função para
				// inseri-lo.
				if (texto.getObjetoIncidente() instanceof IncidenteJulgamento
						&& texto.getObjetoIncidente().getId() == null) {
					IncidenteJulgamento ij = (IncidenteJulgamento) texto.getObjetoIncidente();
					try {
						incidenteJulgamentoService.inserirIncidenteJulgamento(ij.getPai().getId(), ij
								.getTipoJulgamento().getId(), 1);
					} catch (IncidenteJulgamentoException e) {
						throw new TextoException(e);
					}
				}
			}

			if (texto.getTipoFaseTextoDocumento() == null)
				texto.setTipoFaseTextoDocumento(FaseTexto.EM_ELABORACAO);

			dao.salvar(texto);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return texto;
	}

	/**
	 * método reponsável por verificar se o texto informado já existe;
	 * 
	 * @author GuilhermeA
	 */
	public void validarNovoTexto(Texto texto) throws TextoException, ServiceException {
		if (texto != null && texto.getObjetoIncidente() != null) {
			List<Texto> listaTexto = pesquisar(texto.getObjetoIncidente(), texto.getTipoTexto(), false);
			// somente um texto por processo
			if (somenteUmTexto(texto.getTipoTexto(), false)) {
				if (listaTexto != null && listaTexto.size() > 0) {
					if (!criarNovoTextoJaExistente(texto, listaTexto)) {
						throw new TextoException("O texto \"" + texto.getTipoTexto().getDescricao() + "\" já existe.");
					}
				}
			}
			// pode possuir vários textos mas somente um por ministro
			if (somenteUmTexto(texto.getTipoTexto(), true)) {
				if (listaTexto != null && listaTexto.size() > 0) {
					for (Texto textoMinistro : listaTexto) {
						if (textoMinistro.getMinistro().getId().equals(texto.getMinistro().getId()))
							throw new TextoException("O texto \"" + texto.getTipoTexto().getDescricao() + "\" já existe.");
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * Quando um texto de ementa, acórdão ou relatório é criado, ele valida se já existe algum criado, independente do ministro.
	 * 
	 * Para solucionar o problema de outros ministros não conseguirem criar o texto também, a seguinte ação deve ser executada:
	 * 	- pegar o código tipo texto e colocar no campo de observação, e atualizar o código para 60 (despacho)
	 * 
	 * Restrições: não existir nenhum público e nenhum para o ministro
	 * 
	 * Provavelmente o parâmetro listaTexto sempre virá com tamanho = 1, mas por prevenção, usei o for
	 * 
	 * @author Rodrigo.Lisboa
	 *
	 */	
	private boolean criarNovoTextoJaExistente(Texto texto, List<Texto> listaTexto) throws ServiceException {
		TipoTexto tipoTexto = texto.getTipoTexto();
		if (tipoTexto.equals(TipoTexto.EMENTA) || tipoTexto.equals(TipoTexto.ACORDAO) || tipoTexto.equals(TipoTexto.RELATORIO)) {
			// valida se existe texto público
			for(Texto textoExistente : listaTexto) {
				if (textoExistente != null && textoExistente.getPublico() != null && textoExistente.getPublico().booleanValue()) {
					return false;
				}
				Ministro redator = incidenteJulgamentoService.recuperarRedatorIncidente(textoExistente.getObjetoIncidente());
				if (tipoTexto.equals(TipoTexto.ACORDAO) && textoExistente.getMinistro().equals(redator)) {
					return false;
				}
			}
			// caso não exista texto público, inclui na observação o tipo de texto atual e altera o tipo de texto para Despacho (60)
			for(Texto textoExistente : listaTexto) {
				// o texto não pode ser do mesmo ministro
				if (!texto.getMinistro().getId().equals(textoExistente.getMinistro().getId())) {
					textoExistente.setObservacao(textoExistente.getTipoTexto().getCodigo().toString());
					textoExistente.setTipoTexto(TipoTexto.DESPACHO);
					try {
						dao.salvar(textoExistente);
						return true;
					} catch (DaoException e) {
						return false;
	//					throw new ServiceException(e);
					}
				}
			} 
		}
		return false;
	}
	
	
	@Override
	public boolean verificarRestricaoTextos(Collection<Texto> textos, String idUsuario, Long idSetor) {
		try {
			boolean ret = true;

			if (textos == null || textos.size() == 0)
				return true;
			if (idUsuario == null)
				return false;

			for (Texto t : textos) {
				Texto textoRecarregado = recuperarPorId(t.getId());
				TipoRestricao tr = textoRecarregado.getTipoRestricao();
				Usuario usuarioInclusao = textoRecarregado.getUsuarioInclusao();
				Responsavel responsavel = textoRecarregado.getResponsavel();

				if (tr == TipoRestricao.P) {
					ret &= true;
				}
				if (tr == TipoRestricao.U) {
					if (usuarioInclusao != null && idUsuario.equalsIgnoreCase(usuarioInclusao.getId()))
						ret &= true;
					else if (responsavel != null && idUsuario.equalsIgnoreCase(responsavel.getId().toString()))
						ret &= true;
					else
						ret &= false;
				}
				if (tr == TipoRestricao.S) {
					if (textoRecarregado.getMinistro() == null || textoRecarregado.getMinistro().getSetor() == null)
						ret &= false;
					else if (idSetor != null && idSetor.equals(textoRecarregado.getMinistro().getSetor().getId()))
						ret &= true;
					else
						ret &= false;
				}
			}

			return ret;
		} catch (Exception e) {
		}
		return true;
	}

	/**
	 * método responsável por verificar se o texto informado só 
	 * pode ter um por processo de acordo com o ministro informado.
	 * 
	 * @return
	 * @author GuilhermeA
	 */
	private boolean somenteUmTexto(TipoTexto tipoTexto, boolean porMininstro) {
		//VOTO VISTA Adicionado em 28.03.2011, conforme ISSUE 941.
		if (tipoTexto != null) {
			if (!porMininstro) {
				return tipoTexto.equals(TipoTexto.EMENTA) || tipoTexto.equals(TipoTexto.ACORDAO)
						|| tipoTexto.equals(TipoTexto.RELATORIO)
						|| tipoTexto.equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL)
						|| tipoTexto.equals(TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL);
			} else {
				return tipoTexto.equals(TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL)
						|| tipoTexto.equals(TipoTexto.VOTO)
						|| tipoTexto.equals(TipoTexto.VOTO_VISTA)
					    || tipoTexto.equals(TipoTexto.VOTO_VOGAL);
			}
		}
		return false;
	}

	public List<Texto> pesquisar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Ministro ministro)
			throws ServiceException {
		try {
			return dao.pesquisar(objetoIncidente, tipoTexto, ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void excluirTextosComRelacionamentos(Collection<Texto> textos) throws ServiceException {
		for (Texto texto : textos) {
			excluirTextoComRelacionamentos(texto);
		}
	}

	public void excluirTextoComRelacionamentos(Texto texto) throws ServiceException {
		try {
			excluirDocumentoTexto(texto);
			excluirDaListaDeTexto(texto);
			excluirDaFaseTexto(texto);
			excluirTextoDoControleDeVotos(texto);
			excluir(texto);
			dao.flushSession();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private void excluirTextoDoControleDeVotos(Texto texto) throws ServiceException {
		controleVotoService.excluirTextoDoControleDeVotos(texto);
	}

	private void excluirDaFaseTexto(Texto texto) throws ServiceException {
		faseTextoProcessoService.excluirFasesDoTexto(texto);

	}

	private void excluirDaListaDeTexto(Texto texto) throws ServiceException {
		listaTextosService.excluirTextoDasListas(texto);
	}

	private void excluirDocumentoTexto(Texto texto) throws ServiceException {
		documentoTextoService.excluirDocumentosTextoDoTexto(texto);
	}

	public static Boolean isTextoSomenteLeitura(Texto texto) {
		if (texto != null) {
			if (FaseTexto.fasesComTextoAssinado.contains(texto.getTipoFaseTextoDocumento()) || texto.getPublico().booleanValue())
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		}
		return true;
	}

	public static Boolean isDespacho(Long codigoTipoTexto) {
		return codigoTipoTexto.equals(TipoTexto.DESPACHO);
	}
	
	public static Boolean isDecisaoMonocratica(Long codigoTipoTexto) {
		return codigoTipoTexto.equals(TipoTexto.DECISAO_MONOCRATICA);
	}

	public static Boolean isVoto(Long codigoTipoTexto) {
		return codigoTipoTexto >= TipoTexto.VOTO.getCodigo()
				&& codigoTipoTexto <= TipoTexto.REVISAO_DE_APARTES.getCodigo();
	}

	public Texto verificaArqEletronico(ControleVoto cv) throws ServiceException {
		try {
			return dao.recuperaArquivoEletronico(cv);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public Texto recarregar(Texto texto) throws ServiceException {
		try {
			return dao.recarregar(texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Texto recuperarTextoEmentaSobreRepercussaoGeral(Long seqObjetoIncidente, Long codigoMinistro) throws ServiceException {
		try {
			return dao.recuperarTextoEmentaSobreRepercussaoGeral(seqObjetoIncidente, codigoMinistro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Texto recuperarTextoDecisaoSobreRepercussaoGeral(Long seqObjetoIncidente, Long codigoMinistro) throws ServiceException {
		try {
			return dao.recuperarTextoDecisaoSobreRepercussaoGeral(seqObjetoIncidente, codigoMinistro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Texto> pesquisarTextoRepercussaoGeralVotoValido(Long seqObjetoIncidente) throws ServiceException {
		try {
			return dao.pesquisarTextoRepercussaoGeralVotoValido(seqObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Texto recuperar(Long seqObjetoIncidente, Long codigoMinistro, TipoTexto tipoTexto, Date dataFim, Long seqVoto)
			throws ServiceException {
		try {
			return dao.recuperar(seqObjetoIncidente, codigoMinistro, tipoTexto, dataFim, seqVoto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Texto recuperar(Long seqObjetoIncidente, String tipoJulgamento,
			TipoTexto tipoTexto, Long codigoMinistro) throws ServiceException {
		try {
			return dao.recuperar(seqObjetoIncidente, tipoJulgamento, tipoTexto, codigoMinistro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void assinarTextoContingencialmente(AssinaturaDto assinatura) throws ServiceException, TransicaoDeFaseInvalidaException {
//		DocumentoEletronico documentoEletronico = documentoEletronicoService.recuperarUltimoDocumentoEletronicoAtivo(assinatura.getTexto());
//		if (documentoEletronico != null && documentoEletronico.getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_DIGITALIZADO)) {
//			documentoEletronico = criarRascunhoDeDocumentoEletronico(assinatura);
//		}
		DocumentoEletronico documentoEletronico = criarRascunhoDeDocumentoEletronico(assinatura);
		
		documentoEletronicoService.salvarDocumentoEletronicoAssinadoContingencialmente(documentoEletronico);
		this.alterarFase(assinatura, assinatura.getTexto(), TipoTransicaoFaseTexto.ASSINAR_DIGITALMENTE, assinatura.getObservacao(), null);
	}

	@Override
	public Texto recuperar(ObjetoIncidente<?> objetoIncidente, TipoTexto tipoTexto, Ministro ministro,
			Boolean textoPublico, Boolean dataSessaoPreenchida) throws ServiceException {
		try {
			return dao.recuperar(objetoIncidente, tipoTexto, ministro, textoPublico, dataSessaoPreenchida);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<Texto> recuperarListaTextoEletronico(Integer idArquivoEletronico) throws ServiceException {
		try {
			return dao.recuperarListaTextoEletronico(idArquivoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean verificarExistenciaTexto(TextoSearchData sd)
			throws ServiceException {
		try{
			return dao.verificarExistenciaTexto(sd);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<Texto> pesquisar(ArquivoEletronico arquivoEletronico) throws ServiceException {
		List<SearchCriterion> criterioArquivoEletronico = new ArrayList<SearchCriterion>();
		criterioArquivoEletronico.add(new EqualCriterion<ArquivoEletronico>("arquivoEletronico", arquivoEletronico));
		return pesquisarPorExemplo(new Texto(), criterioArquivoEletronico);
	}
	
	@Override
	public Texto liberarAntecipadamente(Long textoId, Boolean liberar) throws ServiceException {
		Texto texto = recuperarPorId(textoId);
		texto.setLiberacaoAntecipada(liberar);
		
		try {
			return dao.salvar(texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public SearchResult<Texto> pesquisarTextoPorObjIncidenteTipoTextoMinistroOuPresidente(TextoSearchData sd) throws ServiceException{
		try {
			return dao.pesquisarTextoPorObjIncidenteTipoTextoMinistroOuPresidente(sd);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void preservarTextosRelatorAnteriorNaDesignacaoRedatorAcordao(ObjetoIncidente<?> objetoIncidente) throws ServiceException {

		try {
			List<Texto> textos = dao.pesquisar(objetoIncidente, TipoTexto.EMENTA, TipoTexto.ACORDAO);

			for (Texto texto : textos) {
				if (texto.getTipoTexto().equals(TipoTexto.EMENTA)) {
					texto.setObservacao(
							"Ementa(80) convertida automaticamente após designação de relator para o acórdão.");
				}
				if (texto.getTipoTexto().equals(TipoTexto.ACORDAO)) {
					texto.setObservacao(
							"Acordao(70) convertido automaticamente após designação de relator para o acórdão.");
				}
				texto.setTipoTexto(TipoTexto.MINISTRO_VENCIDO);
				texto.setSequenciaVoto(null);
				dao.alterar(texto);
			}
		} catch (DaoException e) {
			throw new ServiceException("Erro nao foi possivel ajustar o texto de ementa ou acordao.", e);
		}
	}
	
	
	
	//**********
	
	public byte[] montarPDFAssinatura(Texto texto) throws FileNotFoundException, ServiceException, JDOMException,
			IOException, MontadorTextoServiceException, DocumentException {

		DadosMontagemTexto<Long> dadosMontagem;

		if (texto.getTipoTexto().equals(TipoTexto.ACORDAO)) {

			dadosMontagem = montaDadosMontagemTexto(texto, false, montaArquivoDeEmentaAcordao(texto));

		} else if (texto.getTipoTexto().equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL)) {

			dadosMontagem = montaDadosMontagemTexto(texto, false,
					montaArquivoDeEmentaDecisaoSobreRepercussaoGeral(texto));

		} else {
			dadosMontagem = montaDadosMontagemTexto(texto, false);
		}

		final ByteArrayOutputStream conteudo = new ByteArrayOutputStream();
		TextoOutputStrategy<Long> outputStrategy = new ByteArrayOutputStrategy<Long>(new ByteArrayPersister<Long>() {
			public void persistByteArray(Long textoId, byte[] data) throws TextoOutputException, IOException {
				conteudo.write(data);
			}
		});

		openOfficeMontadorTextoService.criarTextoPDF(dadosMontagem, outputStrategy, true);

		ByteArrayInputStream conteudoStream = new ByteArrayInputStream(conteudo.toByteArray());
		return PDFUtil.getInstancia().inserirCabecalhoArquivoPDF(conteudoStream);

	}
	
	private byte[] montaArquivoDeEmentaAcordao(Texto texto)
			throws ServiceException, FileNotFoundException, JDOMException, IOException, MontadorTextoServiceException {
		Texto acordao = texto;
		Texto ementa = recuperar(texto.getObjetoIncidente(), TipoTexto.EMENTA,
				texto.getCodigoMinistro());
		return concatenarArquivos(ementa, acordao);
	}

	private byte[] montaArquivoDeEmentaDecisaoSobreRepercussaoGeral(Texto texto)
			throws ServiceException, FileNotFoundException, JDOMException, IOException, MontadorTextoServiceException {
		Texto decisao = texto;
		Texto ementa = recuperarTextoEmentaSobreRepercussaoGeral(texto.getObjetoIncidente().getId(),
				texto.getCodigoMinistro());
		return concatenarArquivos(ementa, decisao);
	}

	private byte[] concatenarArquivos(Texto ementa, Texto acordao)
			throws JDOMException, IOException, MontadorTextoServiceException, FileNotFoundException {
		File ementaAsOdt = converterArquivoParaOdt(ementa);
		File acordaoAsOdt = converterArquivoParaOdt(acordao);

		File resultado = openOfficeMontadorTextoService.concatenaArquivosOdt(ementaAsOdt, acordaoAsOdt, false);

		return IOUtils.toByteArray(new FileInputStream(resultado));
	}

	private File converterArquivoParaOdt(Texto texto)
			throws MontadorTextoServiceException, IOException, FileNotFoundException {
		InputStream odtAsInputStream = openOfficeMontadorTextoService.converteArquivo(getTextoSource(texto),
				ExtensaoEnum.RTF, ExtensaoEnum.ODT);
		File odtAsFile = File.createTempFile(texto.getIdentificacao(), ".odt");
		FileOutputStream fos = new FileOutputStream(odtAsFile);
		IOUtils.copy(odtAsInputStream, fos);
		return odtAsFile;
	}

	private TextoSource getTextoSource(final Texto texto) {
		return new TextoSource() {
			@Override
			public byte[] getByteArray() throws IOException, MontadorTextoServiceException {
				return texto.getArquivoEletronico().getConteudo();
			}
		};
	}
	
	public DadosMontagemTexto<Long> montaDadosMontagemTexto(ObjetoIncidente objetoIncidente) throws ServiceException {
		DadosMontagemTexto<Long> dados = criaDadosMontagemTexto();
		SpecDadosTexto specDados = new SpecDadosTexto();
		carregarDescricaoCurtaProcesso(objetoIncidente, specDados);
		dados.setSpecDados(specDados);
		return dados;
	}

	public DadosMontagemTexto<Long> montaDadosMontagemTexto(Texto texto) throws ServiceException {
		return montaDadosMontagemTexto(texto, true);
	}

	public DadosMontagemTexto<Long> montaDadosMontagemTexto(Texto texto, boolean inserirMarcaDagua)
			throws ServiceException {
		return montaDadosMontagemTexto(texto, inserirMarcaDagua, null, null, true);
	}
	
	public DadosMontagemTexto<Long> montaDadosMontagemTexto(Texto texto, boolean inserirMarcaDagua,
			String textoMarcaDagua) throws ServiceException {
		return montaDadosMontagemTexto(texto, inserirMarcaDagua, null, textoMarcaDagua, true);
	}

	public DadosMontagemTexto<Long> montaDadosMontagemTexto(Texto texto, boolean inserirMarcaDagua, byte[] conteudo)
			throws ServiceException {
		return montaDadosMontagemTexto(texto, inserirMarcaDagua, conteudo, null, true);
	}

	public DadosMontagemTexto<Long> montaDadosMontagemTexto(Texto texto, boolean inserirMarcaDagua, byte[] conteudo,
			String textoMarcaDagua, boolean usarPdfAssinado) throws ServiceException {
		DadosMontagemTexto<Long> dadosMontagem = criaDadosMontagemTexto();
		dadosMontagem.setSpecDados(montaDadosDeInformacaoDoTexto(texto));
		defineConteudoTexto(texto, conteudo, dadosMontagem, usarPdfAssinado);
		insereCabecalho(texto, dadosMontagem);
		verificaMarcaDagua(texto, inserirMarcaDagua, textoMarcaDagua, dadosMontagem);
		return dadosMontagem;
	}

	/**
	 * Carrega no {@link DadosMontagemTexto} o conteúdo do documento. Caso o conteúdo tenha sido repassado para
	 * o método, utiliza ele mesmo. Caso não tenha, monta dinamicamente
	 * @param texto
	 * @param conteudo
	 * @param dadosMontagem
	 * @param usarPdfAssinado Indica se o conteúdo que deve ser utilizado é o do PDF do texto assinado, se existir
	 * @throws ServiceException
	 */
	private void defineConteudoTexto(Texto texto, byte[] conteudo, DadosMontagemTexto<Long> dadosMontagem,
			boolean usarPdfAssinado) throws ServiceException {
		dadosMontagem.setPdfGerado(false);
		if (conteudo == null) {
			conteudo = geraConteudoDoTexto(texto, conteudo, dadosMontagem, usarPdfAssinado);
		}
		insereTextoSource(conteudo, dadosMontagem);
	}

	/**
	 * Gera o conteúdo do texto, obedecendo à seguinte regra:
	 * 1) Se o texto já estiver assinado, e a variável usarTextoAssinado estiver como true, recupera o conteúdo do PDF
	 * 2) Caso contrário, recupera o conteúdo do RTF original
	 * @param texto
	 * @param conteudo
	 * @param dadosMontagem
	 * @param usarPdfAssinado
	 * @return
	 * @throws ServiceException
	 */
	private byte[] geraConteudoDoTexto(Texto texto, byte[] conteudo, DadosMontagemTexto<Long> dadosMontagem,
			boolean usarPdfAssinado) throws ServiceException {
		if (usarPdfAssinado && FaseTexto.fasesComTextoAssinado.contains(texto.getTipoFaseTextoDocumento())) {
			DocumentoEletronico documento = documentoEletronicoService
					.recuperarUltimoDocumentoEletronicoAtivo(texto);
			if (documento != null
					&& DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO.equals(documento
							.getDescricaoStatusDocumento())) {
				dadosMontagem.setPdfGerado(true);
				conteudo = documento.getArquivo();
			} else {
				conteudo = texto.getArquivoEletronico().getConteudo();
			}
		} else {
			conteudo = texto.getArquivoEletronico().getConteudo();
		}
		return conteudo;
	}

	private void verificaMarcaDagua(Texto texto, boolean inserirMarcaDagua, String textoMarcaDagua,
			DadosMontagemTexto<Long> dadosMontagem) {
		if (textoMarcaDagua == null) {
			insereDadosMarcaDagua(texto, inserirMarcaDagua, dadosMontagem);
		} else {
			insereDadosMarcaDagua(dadosMontagem, textoMarcaDagua);
		}
	}
	
	private DadosMontagemTexto<Long> criaDadosMontagemTexto() {
		return new DadosMontagemTexto<Long>();
	}

	private void insereDadosMarcaDagua(Texto texto, boolean inserirMarcaDagua, DadosMontagemTexto<Long> dadosMontagem) {
		if (inserirMarcaDagua && texto.getTipoFaseTextoDocumento() != null
				&& !FaseTexto.fasesComTextoAssinado.contains(texto.getTipoFaseTextoDocumento())) {
			insereDadosMarcaDagua(dadosMontagem, texto.getTipoFaseTextoDocumento().getDescricao());
		} else {
			insereDadosMarcaDagua(dadosMontagem, "");
		}
	}

	private void insereDadosMarcaDagua(DadosMontagemTexto<Long> dadosMontagem, String textoMarcaDagua) {
		SpecMarcaDagua specMarcaDagua = new SpecMarcaDagua(textoMarcaDagua);
		dadosMontagem.setSpecMarcaDagua(specMarcaDagua);
	}

	private void insereCabecalho(Texto texto, DadosMontagemTexto<Long> dadosMontagem) throws ServiceException {
		CabecalhoObjetoIncidente cabecalho = cabecalhoObjetoIncidenteService.recuperarCabecalho(texto
				.getObjetoIncidente().getId());
		cabecalho.setSequencialTexto(texto.getId());
		SpecCabecalho<Long> specCabecalho = cabecalhoObjetoIncidenteService.getSpecCabecalho(cabecalho);
		dadosMontagem.setSpecCabecalho(specCabecalho);
	}

	private void insereTextoSource(byte[] conteudo, DadosMontagemTexto<Long> dadosMontagem) {
		ByteArrayTextoSource source = new ByteArrayTextoSource(conteudo);
		dadosMontagem.setTextoSource(source);
	}

	private SpecDadosTexto montaDadosDeInformacaoDoTexto(Texto texto) {
		SpecDadosTexto specDados = new SpecDadosTexto();
		ControleVoto controleVoto = texto.getControleVoto();
		long codigoTipoTexto = texto.getTipoTexto().getCodigo();
		specDados.setMostrarDadosSessao(isMostrarDadosSessao(controleVoto, codigoTipoTexto));
		if (controleVoto != null) {
			specDados.setDataSessao(controleVoto.getDataSessao());
			specDados.setColegiado(controleVoto.getSessao().getDescricao());
		}
		specDados.setVoto(isVoto(codigoTipoTexto));
		carregarDescricaoCurtaProcesso(texto.getObjetoIncidente(), specDados);
		return specDados;
	}

	private void carregarDescricaoCurtaProcesso(ObjetoIncidente<?> objetoIncidente, SpecDadosTexto specDados) {
		specDados.setDescricaoCurtaProcesso(objetoIncidente.getIdentificacao());
	}

	private boolean isMostrarDadosSessao(ControleVoto controleVoto, long codigoTipoTexto) {
		return (!isDespacho(codigoTipoTexto))
				&& (!isDecisaoMonocratica(codigoTipoTexto))
				&& (controleVoto != null)
				&& (controleVoto.getDataSessao() != null);
	}
	
	@Override
	public void refresh(Texto entity) throws ServiceException {
		try {
			dao.refresh(entity);
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<Texto> recuperarTextosPorFaseTipoEspecificos(Long seqObjetoIncidente, List<Long> tipoTextos, FaseTexto fase) throws ServiceException {
		try {
			return dao.recuperarTextosPorFaseTipoEspecificos(seqObjetoIncidente, tipoTextos, fase);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}
	
}
