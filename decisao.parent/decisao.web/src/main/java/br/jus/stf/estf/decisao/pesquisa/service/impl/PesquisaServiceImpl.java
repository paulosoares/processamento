package br.jus.stf.estf.decisao.pesquisa.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.faces.FacesMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.estf.entidade.usuario.TipoConfiguracaoUsuario;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.usuario.model.service.ConfiguracaoUsuarioService;
import br.gov.stf.estf.usuario.model.service.TipoConfiguracaoUsuarioService;
import br.gov.stf.estf.usuario.model.service.TransacaoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.persistence.PesquisaDao;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.NumeroProcessoNaoInformadoException;
import br.jus.stf.estf.decisao.pesquisa.service.IdentificacaoResolver;
import br.jus.stf.estf.decisao.pesquisa.service.PesquisaService;
import br.jus.stf.estf.decisao.pesquisa.web.PesquisaXMLBind;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;
import br.jus.stf.estf.decisao.support.security.PermissionChecker;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * Implementação de negócio para interface <code>PesquisaService</code>.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 */
@Service("pesquisaService")
public class PesquisaServiceImpl implements PesquisaService {

	private final Log logger = LogFactory.getLog(PesquisaServiceImpl.class);

	@Autowired
	private IdentificacaoResolver idResolver;

	@Autowired
	private PesquisaDao pesquisaDao;

	@Autowired
	private TipoConfiguracaoUsuarioService tipoConfiguracaoUsuarioService;

	@Autowired
	private ConfiguracaoUsuarioService configuracaoUsuarioService;

	@Autowired
	private ComunicacaoService comunicacaoService;

	@Autowired
	private SetorService setorService;

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private TransacaoService transacaoService;

	@Autowired
	private TextoService textoService;
	
	@Autowired
	private MinistroService ministroService;
	
	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#pesquisarObjetosIncidente(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<ObjetoIncidenteDto> pesquisarObjetosIncidente(
			Pesquisa pesquisa) {
		return pesquisaDao
				.pesquisarObjetosIncidente(prepararPesquisa(pesquisa));
	}

	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#pesquisarTextos(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<TextoDto> pesquisarTextos(Pesquisa pesquisa) {
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		PagedList<TextoDto> listaTextos = null;	
		
		if (pesquisa.isNotBlank("painelVisualizacao")
				&& (Boolean) pesquisa.get("painelVisualizacao")) {
			listaTextos = pesquisaDao.pesquisarTextos(prepararPesquisa(pesquisa), true, principal);
		} else {
			listaTextos  = pesquisaDao.pesquisarTextos(prepararPesquisa(pesquisa), principal);
		}
		
		textoService.validaAcessoTextosRestritos(principal, listaTextos.getResults());
		
		return listaTextos;
		
	}

	/**
	 * Busca as comunicações a serem assinadas
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PagedList<ComunicacaoDto> pesquisarComunicacoes(Pesquisa pesquisa) {
		try {
			List<ComunicacaoDocumentoResult> lista = executarPesquisaComunicacoes(pesquisa);
			List<ComunicacaoDto> listaDto = new ArrayList<ComunicacaoDto>();
			if (lista == null) {
				lista = new ArrayList<ComunicacaoDocumentoResult>();
			}
			for (ComunicacaoDocumentoResult comunicacao : lista) {
				String nomeRelator = "";
				
				if (comunicacao.getComunicacao() != null && comunicacao.getComunicacao().getObjetoIncidenteUnico() != null) {
					Long relatorId = comunicacao.getComunicacao().getObjetoIncidenteUnico().getRelatorIncidenteId();
					Ministro ministro = ministroService.recuperarPorId(relatorId);
					nomeRelator = ministro.getNome();
				}
				
				listaDto.add(ComunicacaoDto.valueOf(comunicacao, nomeRelator));
			}
			return new PagedList<ComunicacaoDto>(listaDto,
					pesquisa.getFirstResult(), listaDto.size());
		} catch (Exception e) {
			throw new NestedRuntimeException(e);
		}
	}
	
	
	//TODO implementar esse métoodo para mobile
	public PagedList<ComunicacaoDto> pesquisarComunicacoesMobile(Pesquisa pesquisa) {
		try{
			List<ComunicacaoDocumentoResult> lista = executarPesquisaComunicacoes(pesquisa);
			List<ComunicacaoDto> listaDto = new ArrayList<ComunicacaoDto>();
			if (lista == null) {
				lista = new ArrayList<ComunicacaoDocumentoResult>();
			}
			for (ComunicacaoDocumentoResult comunicacao : lista) {
				listaDto.add(ComunicacaoDto.valueOfForMobile(comunicacao));
			}
			return new PagedList<ComunicacaoDto>(listaDto,
					pesquisa.getFirstResult(), listaDto.size());
		} catch (Exception e) {
			throw new NestedRuntimeException(e);
		}
	}
	
	private List<ComunicacaoDocumentoResult> executarPesquisaComunicacoes(Pesquisa pesquisa){
		try {
			Setor setor = (Setor) pesquisa.get("codSetor");
			setor = setorService.recuperarPorId(setor.getId());
			List<Long> tipoSituacaoDocumento = (List<Long>) pesquisa
					.get("tipoSituacaoDocumento");
			Long situacao = (Long) pesquisa.get("situacao");
			List<ComunicacaoDocumentoResult> lista;
			if (pesquisa.isNotBlank("idsComunicacoes")) {
				lista = comunicacaoService.pesquisarDocumentosAssinatura(setor, tipoSituacaoDocumento, situacao, (List<Long>)pesquisa.get("idsComunicacoes"));
			} else {
				lista = comunicacaoService
						.pesquisarDocumentosAssinatura(setor,
								tipoSituacaoDocumento, situacao);
			}
			return lista;
		} catch (ServiceException e) {
			throw new NestedRuntimeException(e);
		}
	}

	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#pesquisarListasIncidentes(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<ListaIncidentesDto> pesquisarListasIncidentes(
			Pesquisa pesquisa) {
		return pesquisaDao
				.pesquisarListasIncidentes(prepararPesquisa(pesquisa));
	}

	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#pesquisarListasTextos(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<ListaTextosDto> pesquisarListasTextos(Pesquisa pesquisa) {
		return pesquisaDao.pesquisarListasTextos(prepararPesquisa(pesquisa));
	}

	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#pesquisarObjetosIncidente(java.lang.String)
	 */
	@Override
	public List<ObjetoIncidenteDto> pesquisarObjetosIncidente(
			String identificacao) {
		return pesquisarObjetosIncidentes(identificacao, true);
	}

	public List<ObjetoIncidenteDto> pesquisarObjetosIncidentes(
			String identificacao, boolean incluirFake) {
		if (idResolver.isValid(identificacao)) {
			try {
				// Recuperando a lista de todos os incidentes, dado sigla e
				// número do processo...
				List<ObjetoIncidenteDto> incidentes = pesquisaDao
						.pesquisarObjetosIncidente(
								idResolver.getSigla(identificacao),
								idResolver.getNumero(identificacao));
				// Para cada incidente, verificamos se é um processo; se sim,
				// criamos um fake para esse processo a adicionamos
				// a lista resultado. O fake necessário para listar todos os
				// textos do processo na suggestion box da
				// pesquisa principal.
				// A lista encadeada convervará a ordenação da lista original.
				List<ObjetoIncidenteDto> result = new LinkedList<ObjetoIncidenteDto>();
				for (ObjetoIncidenteDto incidente : incidentes) {
					// MinistroDto relator =
					// pesquisaDao.recuperarRelatorIncidente(incidente.getId());
					//
					// if(relator != null) {
					// incidente.setIdRelator(relator.getId());
					// incidente.setNomeRelator(relator.getNome());
					// }
					//
					if (incidente.getTipo()
							.equals(TipoObjetoIncidente.PROCESSO)
							&& incluirFake) {
						ObjetoIncidenteDto fake = new ObjetoIncidenteDto();
						try {
							// Copiando propriedade do processo para o objeto
							// fake...
							BeanUtils.copyProperties(incidente, fake);
						} catch (Exception e) {
							throw new NestedRuntimeException(e);
						}
						// O fake não deve ter cadeia. Setando String vazia...
						fake.setCadeia("");
						// Marcando o objeto como fake...
						fake.setFake(true);
						// O fake será adicionado à lista resultado,
						// assim como o processo original.
						result.add(fake);
					}
					// O incidente sempre será adicionado.
					result.add(incidente);
				}
				return result;
			} catch (NumeroProcessoNaoInformadoException e) {
				// Se o número do processo não foi informado, a pesquisa não foi
				// realizada. Retorna o valor padrão.
				logger.warn(MessageFormat
						.format("Não foi possível identificar o número com a seguinte identificação: {0}.",
								identificacao));
			}
		}
		// TODO: Verificar se uma exceção não é uma opção melhor.
		return new ArrayList<ObjetoIncidenteDto>();
	}

	/**
	 * Prepara o objeto de pesquisa fazendo o parse da identificação do processo
	 * que é dividida em sigla e número do processo.
	 * 
	 * @param pesquisa
	 *            o objeto de pesquisa
	 * @return a pesquisa preparada
	 * 
	 * @see IdentificacaoResolver
	 */
	private Pesquisa prepararPesquisa(Pesquisa pesquisa) {
		ObjetoIncidenteDto objetoIncidente = (ObjetoIncidenteDto) pesquisa
				.get("objetoIncidente");
		String identificacao = (String) pesquisa.get("identificacao");
		if (objetoIncidente != null) {
			if (objetoIncidente.isFake()) {
				pesquisa.put("siglaProcesso",
						objetoIncidente.getSiglaProcesso());
				pesquisa.put("numeroProcesso",
						objetoIncidente.getNumeroProcesso());
			} else {
				pesquisa.put("idObjetoIncidente", objetoIncidente.getId());
			}
			pesquisa.remove("objetoIncidente");
		} else if (identificacao != null && identificacao.trim().length() > 0) {
			String sigla = idResolver.getSigla(identificacao);
			Long numero = idResolver.getNumero(identificacao);
			if (sigla != null)
				pesquisa.put("siglaProcesso", sigla);
			if (numero != null)
				pesquisa.put("numeroProcesso", numero);
		}
		return pesquisa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#
	 * salvarConfiguracaoPesquisa(java.lang.String, java.lang.String,
	 * br.jus.stf.estf.decisao.support.security.Principal,
	 * br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa, java.lang.String)
	 */
	@Override
	public Long salvarConfiguracaoPesquisa(String nomeConfiguracaoPesquisa,
			String escopoConfiguracaoPesquisa, Class tipoPesquisa,
			Integer pageSize, Principal principal, Pesquisa pesquisa) {
		PesquisaXMLBind pesquisaXMLBind = getInstanciaPesquisaXMLBind(pesquisa);
		pesquisaXMLBind.setTipoPesquisa(tipoPesquisa);
		pesquisaXMLBind.setPageSize(pageSize);
		return gerarXMLPesquisaXMLBind(pesquisaXMLBind,
				nomeConfiguracaoPesquisa, escopoConfiguracaoPesquisa, principal);
	}

	/**
	 * Obtém a instância do objeto pesquisaXMLBind equivalente à pesquisa.
	 * 
	 * @param pesquisa
	 * @return
	 */
	private PesquisaXMLBind getInstanciaPesquisaXMLBind(Pesquisa pesquisa) {
		PesquisaXMLBind pesquisaXMLBind = new PesquisaXMLBind();

		// Processo
		if (pesquisa.isNotBlank("identificacao")) {
			pesquisaXMLBind.setIdentificacaoProcesso((String) pesquisa
					.get("identificacao"));
		}
		if (pesquisa.isNotBlank("objetoIncidente")) {
			ObjetoIncidenteDto objetoIncidente = (ObjetoIncidenteDto) pesquisa
					.get("objetoIncidente");
			pesquisaXMLBind.setIdObjetoIncidente(objetoIncidente.getId());
			pesquisaXMLBind.setObjetoIncidenteFake(Boolean
					.valueOf(objetoIncidente.isFake()));
			pesquisaXMLBind.setNumeroProcesso(objetoIncidente
					.getNumeroProcesso());
			pesquisaXMLBind
					.setSiglaProcesso(objetoIncidente.getSiglaProcesso());
		} else {
			if (pesquisa.isNotBlank("idObjetoIncidente")) {
				pesquisaXMLBind.setIdObjetoIncidente((Long) pesquisa
						.get("idObjetoIncidente"));
			}
			if (pesquisa.isNotBlank("siglaProcesso")) {
				pesquisaXMLBind.setSiglaProcesso((String) pesquisa
						.get("siglaProcesso"));
			}
			if (pesquisa.isNotBlank("numeroProcesso")) {
				pesquisaXMLBind.setNumeroProcesso((Long) pesquisa
						.get("numeroProcesso"));
			}
		}
		if (pesquisa.isNotBlank("nomeRelatorAtual")) {
			pesquisaXMLBind.setNomeRelatorAtual((String) pesquisa
					.get("nomeRelatorAtual"));
		}
		if (pesquisa.isNotBlank("idRelatorAtual")) {
			pesquisaXMLBind.setIdRelatorAtual((Long) pesquisa
					.get("idRelatorAtual"));
		}
		if (pesquisa.isNotBlank("idTipoIncidente")) {
			pesquisaXMLBind.setIdTipoIncidente((Long) pesquisa
					.get("idTipoIncidente"));
		}
		if (pesquisa.isNotBlank("originario")) {
			pesquisaXMLBind.setOriginario((String) pesquisa.get("originario"));
		}
		if (pesquisa.isNotBlank("repercussaoGeral")) {
			pesquisaXMLBind.setRepercussaoGeral((String) pesquisa
					.get("repercussaoGeral"));
		}
		if (pesquisa.isNotBlank("controversiaOrigem")) {
			pesquisaXMLBind.setControversiaOrigem((String) pesquisa
					.get("controversiaOrigem"));
		}
		if (pesquisa.isNotBlank("tipoProcesso")) {
			pesquisaXMLBind.setTipoProcesso((String) pesquisa
					.get("tipoProcesso"));
		}
		if (pesquisa.isNotBlank("nomeListaIncidentes")) {
			pesquisaXMLBind.setNomeListaIncidentes((String) pesquisa
					.get("nomeListaIncidentes"));
		}
		if (pesquisa.isNotBlank("idListaIncidentes")) {
			pesquisaXMLBind.setIdListaIncidentes((Long) pesquisa
					.get("idListaIncidentes"));
		}
		if (pesquisa.isNotBlank("situacaoJulgamento")) {
			pesquisaXMLBind.setSituacaoJulgamento((String) pesquisa
					.get("situacaoJulgamento"));
		}
		
		// Julgamento
		if (pesquisa.isNotBlank("agendamento")) {
			pesquisaXMLBind
			.setAgendamento((String) pesquisa.get("agendamento"));
		}
		if (pesquisa.isNotBlank("colegiado")) {
			pesquisaXMLBind.setColegiado((Long) pesquisa.get("colegiado"));
		}
		if (pesquisa.isNotBlank("inicioDataSessaoJulgamento")) {
			pesquisaXMLBind.setInicioDataSessaoJulgamento((Date) pesquisa
					.get("inicioDataSessaoJulgamento"));
		}
		if (pesquisa.isNotBlank("fimDataSessaoJulgamento")) {
			pesquisaXMLBind.setFimDataSessaoJulgamento((Date) pesquisa
					.get("fimDataSessaoJulgamento"));
		}
		if (pesquisa.isNotBlank("pautaExtra")) {
			pesquisaXMLBind.setPautaExtra((String) pesquisa.get("pautaExtra"));
		}
		if (pesquisa.isNotBlank("controleVoto")) {
			pesquisaXMLBind.setControleVoto((String) pesquisa.get("controleVoto"));
		}

		// Texto
		if (pesquisa.isNotBlank("nomeMinistroTexto")) {
			pesquisaXMLBind.setNomeMinistroTexto((String) pesquisa
					.get("nomeMinistroTexto"));
		}
		if (pesquisa.isNotBlank("idMinistroTexto")) {
			pesquisaXMLBind.setIdMinistroTexto((Long) pesquisa
					.get("idMinistroTexto"));
		}
		if (pesquisa.isNotBlank("idTipoTexto")) {
			List<Long> tiposTexto = new ArrayList<Long>();
			tiposTexto.add((Long) pesquisa.get("idTipoTexto"));
			pesquisaXMLBind.setTiposTexto(tiposTexto);
		}
		if (pesquisa.isNotBlank("tiposTexto")) {
			List<Long> tiposTexto = (List<Long>) pesquisa.get("tiposTexto");
			pesquisaXMLBind.setTiposTexto(tiposTexto);
		}
		if (pesquisa.isNotBlank("inicioDataInclusao")) {
			pesquisaXMLBind.setInicioDataInclusao((Date) pesquisa
					.get("inicioDataInclusao"));
		}
		if (pesquisa.isNotBlank("fimDataInclusao")) {
			pesquisaXMLBind.setFimDataInclusao((Date) pesquisa
					.get("fimDataInclusao"));
		}
		if (pesquisa.isNotBlank("inicioDataSessao")) {
			pesquisaXMLBind.setInicioDataSessao((Date) pesquisa
					.get("inicioDataSessao"));
		}
		if (pesquisa.isNotBlank("fimDataSessao")) {
			pesquisaXMLBind.setFimDataSessao((Date) pesquisa
					.get("fimDataSessao"));
		}
		if (pesquisa.isNotBlank("textosIguais")) {
			pesquisaXMLBind.setTextosIguais((String) pesquisa
					.get("textosIguais"));
		}
		if (pesquisa.isNotBlank("idFaseTexto")) {
			pesquisaXMLBind.setIdFaseTexto((Long) pesquisa.get("idFaseTexto"));
		}
		if (pesquisa.isNotBlank("inicioDataFase")) {
			pesquisaXMLBind.setInicioDataFase((Date) pesquisa
					.get("inicioDataFase"));
		}
		if (pesquisa.isNotBlank("fimDataFase")) {
			pesquisaXMLBind.setFimDataFase((Date) pesquisa.get("fimDataFase"));
		}
		if (pesquisa.isNotBlank("ultimaFase")) {
			pesquisaXMLBind.setUltimaFase((Boolean) pesquisa.get("ultimaFase"));
		}
		if (pesquisa.isNotBlank("nomeResponsavel")) {
			pesquisaXMLBind.setNomeResponsavel((String) pesquisa
					.get("nomeResponsavel"));
		}
		if (pesquisa.isNotBlank("idResponsavel")) {
			pesquisaXMLBind.setIdResponsavel((String) pesquisa
					.get("idResponsavel"));
		}
		if (pesquisa.isNotBlank("nomeListaTextos")) {
			pesquisaXMLBind.setNomeListaTextos((String) pesquisa
					.get("nomeListaTextos"));
		}
		if (pesquisa.isNotBlank("idListaTextos")) {
			pesquisaXMLBind.setIdListaTextos((Long) pesquisa
					.get("idListaTextos"));
		}
		if (pesquisa.isNotBlank("palavraChave")) {
			pesquisaXMLBind.setPalavraChave((String) pesquisa
					.get("palavraChave"));
		}
		if (pesquisa.isNotBlank("observacao")) {
			pesquisaXMLBind.setObservacao((String) pesquisa.get("observacao"));
		}

		// Assunto
		if (pesquisa.isNotBlank("idAssunto")) {
			pesquisaXMLBind.setIdAssunto((String) pesquisa.get("idAssunto"));
		}
		if (pesquisa.isNotBlank("descricaoAssunto")) {
			pesquisaXMLBind.setDescricaoAssunto((String) pesquisa
					.get("descricaoAssunto"));
		}

		// Parte
		if (pesquisa.isNotBlank("idCategoriaParte")) {
			pesquisaXMLBind.setIdCategoriaParte((Long) pesquisa
					.get("idCategoriaParte"));
		}
		if (pesquisa.isNotBlank("nomeParte")) {
			pesquisaXMLBind.setNomeParte((String) pesquisa.get("nomeParte"));
		}

		// Outras opções
		if (pesquisa.isNotBlank("ordenacao")) {
			pesquisaXMLBind.setOrdenacao((String) pesquisa.get("ordenacao"));
		}
		if (pesquisa.isNotBlank("painelVisualizacao")) {
			pesquisaXMLBind.setPainelVisualizacao((Boolean) pesquisa
					.get("painelVisualizacao"));
		}
		
		// Lista Julgamento
		if (pesquisa.isNotBlank("idListaJulgamento"))
			pesquisaXMLBind.setIdListaTextos((Long) pesquisa.get("idListaJulgamento"));

		return pesquisaXMLBind;
	}

	/**
	 * Gera o XML da configuração da pesquisa.
	 * 
	 * @param pesquisaXMLBind
	 * @param nomeConfiguracaoPesquisa
	 * @param escopoConfiguracaoPesquisa
	 * @param principal
	 * @return
	 */
	private Long gerarXMLPesquisaXMLBind(PesquisaXMLBind pesquisaXMLBind,
			String nomeConfiguracaoPesquisa, String escopoConfiguracaoPesquisa,
			Principal principal) {
		try {
			if (pesquisaXMLBind != null) {
				JAXBContext context = JAXBContext.newInstance(pesquisaXMLBind
						.getClass());
				Marshaller m = context.createMarshaller();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				m.marshal(pesquisaXMLBind, baos);

				logger.info(baos.toString());

				return salvarXMLPesquisa(baos, pesquisaXMLBind,
						nomeConfiguracaoPesquisa, escopoConfiguracaoPesquisa,
						principal);

			}
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @param baos
	 * @param processoSetorXMLBind
	 * @param nomeConfiguracaoPesquisa
	 * @param escopoConfiguracaoPesquisa
	 * @param principal
	 */
	private Long salvarXMLPesquisa(ByteArrayOutputStream baos,
			PesquisaXMLBind processoSetorXMLBind,
			String nomeConfiguracaoPesquisa, String escopoConfiguracaoPesquisa,
			Principal principal) {

		TipoConfiguracaoUsuario tcu = new TipoConfiguracaoUsuario();
		try {
			tcu = tipoConfiguracaoUsuarioService
					.recuperarPorId(TipoConfiguracaoUsuario.TipoConfiguracaoUsuarioEnum.PESQUISA_AVANCADA
							.getCodigo());

			ConfiguracaoUsuario confUsu = new ConfiguracaoUsuario();
			confUsu.setCodigoChave(ConfiguracaoUsuario.PESQUISA_AVANCADA_XML);
			confUsu.setDescricao(nomeConfiguracaoPesquisa.toUpperCase());
			if (escopoConfiguracaoPesquisa != null
					&& escopoConfiguracaoPesquisa.equals("S"))
				confUsu.setSetor(principal.getMinistro().getSetor());
			confUsu.setTipoConfiguracaoUsuario(tcu);
			confUsu.setUsuario(principal.getUsuario());
			confUsu.setValor(baos.toString());

			Long id = configuracaoUsuarioService.salvar(confUsu).getId();
			if (id != null)
				FacesMessages.instance().add("Configuração de pesquisa salva.");
			return id;
		} catch (ServiceException e) {
			FacesMessages.instance().add(
					"Erro ao salvar configuração da pesquisa.");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#
	 * pesquisarConfiguracoesPesquisa(java.lang.String, java.lang.Long,
	 * java.lang.Long, java.lang.String)
	 */
	@Override
	public List<ConfiguracaoUsuario> pesquisarConfiguracoesPesquisa(
			String idUsuario, Long idSetor, Long tipoConfiguracaoUsuario,
			String subtipoConfiguracao) throws ServiceException {
		return configuracaoUsuarioService.pesquisar(idUsuario, idSetor,
				tipoConfiguracaoUsuario, subtipoConfiguracao);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#
	 * recuperarObjetoPesquisaXMLBind(java.lang.Long)
	 */
	@Override
	public PesquisaXMLBind recuperarObjetoPesquisaXMLBind(
			Long idConfiguracaoUsuario) {
		PesquisaXMLBind pesquisaXMLBind = new PesquisaXMLBind();
		try {
			JAXBContext context;
			context = JAXBContext.newInstance(pesquisaXMLBind.getClass());
			Unmarshaller um = context.createUnmarshaller();
			Object obj = um
					.unmarshal(recuperarConfiguracaoUsuarioXML(idConfiguracaoUsuario));// RECUPERAR
																						// DO
																						// BD
			pesquisaXMLBind = (PesquisaXMLBind) obj;
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}

		return pesquisaXMLBind;
	}

	/**
	 * Recupera o arquivo XML do banco de dados com a configuração da pesquisa.
	 * 
	 * @param idConfiguracaoUsuario
	 * @return
	 */
	private InputStream recuperarConfiguracaoUsuarioXML(
			Long idConfiguracaoUsuario) {
		ConfiguracaoUsuario confUsu = null;

		try {
			confUsu = configuracaoUsuarioService
					.recuperarPorId(idConfiguracaoUsuario);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		}

		InputStream us = new ByteArrayInputStream(confUsu.getValor().getBytes());
		return us;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#
	 * popularPesquisaAvancada(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa,
	 * br.jus.stf.estf.decisao.pesquisa.web.PesquisaXMLBind)
	 */
	@Override
	public void popularPesquisaAvancada(Pesquisa pesquisa,
			PesquisaXMLBind pesquisaXMLBind) {

		// Processo
		if (pesquisaXMLBind.getIdentificacaoProcesso() != null) {
			pesquisa.getParameters().put("identificacao",
					pesquisaXMLBind.getIdentificacaoProcesso());
		}
		if (pesquisaXMLBind.getIdObjetoIncidente() != null) {
			ObjetoIncidenteDto objetoIncidente = new ObjetoIncidenteDto();
			objetoIncidente.setId(pesquisaXMLBind.getIdObjetoIncidente());
			if (pesquisaXMLBind.getObjetoIncidenteFake() != null) {
				objetoIncidente.setFake(pesquisaXMLBind
						.getObjetoIncidenteFake().booleanValue());
				objetoIncidente.setNumeroProcesso(pesquisaXMLBind
						.getNumeroProcesso());
				objetoIncidente.setSiglaProcesso(pesquisaXMLBind
						.getSiglaProcesso());
			}
			pesquisa.getParameters().put("objetoIncidente", objetoIncidente);
		} else if (pesquisaXMLBind.getNumeroProcesso() != null
				|| pesquisaXMLBind.getSiglaProcesso() != null) {
			ObjetoIncidenteDto objetoIncidente = new ObjetoIncidenteDto();
			if (pesquisaXMLBind.getNumeroProcesso() != null) {
				objetoIncidente.setNumeroProcesso(pesquisaXMLBind
						.getNumeroProcesso());
			}
			if (pesquisaXMLBind.getSiglaProcesso() != null) {
				objetoIncidente.setSiglaProcesso(pesquisaXMLBind
						.getSiglaProcesso());
			}
			if (pesquisaXMLBind.getObjetoIncidenteFake() != null) {
				objetoIncidente.setFake(pesquisaXMLBind
						.getObjetoIncidenteFake().booleanValue());
			}
		}
		if (pesquisaXMLBind.getNomeRelatorAtual() != null) {
			pesquisa.getParameters().put("nomeRelatorAtual",
					pesquisaXMLBind.getNomeRelatorAtual());
		}
		if (pesquisaXMLBind.getIdRelatorAtual() != null) {
			pesquisa.getParameters().put("idRelatorAtual",
					pesquisaXMLBind.getIdRelatorAtual());
		}
		if (pesquisaXMLBind.getIdTipoIncidente() != null) {
			pesquisa.getParameters().put("idTipoIncidente",
					pesquisaXMLBind.getIdTipoIncidente());
		}
		if (pesquisaXMLBind.getOriginario() != null) {
			pesquisa.getParameters().put("originario", pesquisaXMLBind.getOriginario());
		}
		if (pesquisaXMLBind.getRepercussaoGeral() != null) {
			pesquisa.getParameters().put("repercussaoGeral",
					pesquisaXMLBind.getRepercussaoGeral());
		}
		if (pesquisaXMLBind.getTipoProcesso() != null) {
			pesquisa.getParameters().put("tipoProcesso",
					pesquisaXMLBind.getTipoProcesso());
		}
		if (pesquisaXMLBind.getNomeListaIncidentes() != null) {
			pesquisa.getParameters().put("nomeListaIncidentes",
					pesquisaXMLBind.getNomeListaIncidentes());
		}
		if (pesquisaXMLBind.getIdListaIncidentes() != null) {
			pesquisa.getParameters().put("idListaIncidentes",
					pesquisaXMLBind.getIdListaIncidentes());
		}
		if (pesquisaXMLBind.getSituacaoJulgamento() != null) {
			pesquisa.getParameters().put("situacaoJulgamento",
					pesquisaXMLBind.getSituacaoJulgamento());
		}
		
		// Julgamento
		if (pesquisaXMLBind.getAgendamento() != null) {
			pesquisa.getParameters().put("agendamento",
					pesquisaXMLBind.getAgendamento());
		}
		if (pesquisaXMLBind.getColegiado() != null) {
			pesquisa.getParameters().put("colegiado",
					pesquisaXMLBind.getColegiado());
		}
		if (pesquisaXMLBind.getInicioDataSessaoJulgamento() != null) {
			pesquisa.getParameters().put("inicioDataSessaoJulgamento",
					pesquisaXMLBind.getInicioDataSessaoJulgamento());
		}
		if (pesquisaXMLBind.getFimDataSessaoJulgamento() != null) {
			pesquisa.getParameters().put("fimDataSessaoJulgamento",
					pesquisaXMLBind.getFimDataSessaoJulgamento());
		}
		if (pesquisaXMLBind.getPautaExtra() != null) {
			pesquisa.getParameters().put("pautaExtra", pesquisaXMLBind.getPautaExtra());
		}
		if (pesquisaXMLBind.getControleVoto() != null) {
			pesquisa.getParameters().put("controleVoto", pesquisaXMLBind.getControleVoto());
		}

		// Texto
		if (pesquisaXMLBind.getNomeMinistroTexto() != null) {
			pesquisa.getParameters().put("nomeMinistroTexto",
					pesquisaXMLBind.getNomeMinistroTexto());
		}
		if (pesquisaXMLBind.getIdMinistroTexto() != null) {
			pesquisa.getParameters().put("idMinistroTexto",
					pesquisaXMLBind.getIdMinistroTexto());
		}
		if (pesquisaXMLBind.getIdTipoTexto() != null) {
			List<Long> tiposTexto = new ArrayList<Long>();
			tiposTexto.add(pesquisaXMLBind.getIdTipoTexto());
			pesquisa.getParameters().put("tiposTexto", tiposTexto);
		}
		if (pesquisaXMLBind.getTiposTexto() != null
				&& pesquisaXMLBind.getTiposTexto().size() > 0) {
			pesquisa.getParameters().put("tiposTexto",
					pesquisaXMLBind.getTiposTexto());
		}
		if (pesquisaXMLBind.getInicioDataInclusao() != null) {
			pesquisa.getParameters().put("inicioDataInclusao",
					pesquisaXMLBind.getInicioDataInclusao());
		}
		if (pesquisaXMLBind.getFimDataInclusao() != null) {
			pesquisa.getParameters().put("fimDataInclusao",
					pesquisaXMLBind.getFimDataInclusao());
		}
		if (pesquisaXMLBind.getInicioDataSessao() != null) {
			pesquisa.getParameters().put("inicioDataSessao",
					pesquisaXMLBind.getInicioDataSessao());
		}
		if (pesquisaXMLBind.getFimDataSessao() != null) {
			pesquisa.getParameters().put("fimDataSessao",
					pesquisaXMLBind.getFimDataSessao());
		}
		if (pesquisaXMLBind.getTextosIguais() != null) {
			pesquisa.getParameters().put("textosIguais",
					pesquisaXMLBind.getTextosIguais());
		}
		if (pesquisaXMLBind.getIdFaseTexto() != null) {
			pesquisa.getParameters().put("idFaseTexto",
					pesquisaXMLBind.getIdFaseTexto());
		}
		if (pesquisaXMLBind.getInicioDataFase() != null) {
			pesquisa.getParameters().put("inicioDataFase",
					pesquisaXMLBind.getInicioDataFase());
		}
		if (pesquisaXMLBind.getFimDataFase() != null) {
			pesquisa.getParameters().put("fimDataFase",
					pesquisaXMLBind.getFimDataFase());
		}
		if (pesquisaXMLBind.getUltimaFase() != null) {
			pesquisa.getParameters().put("ultimaFase",
					pesquisaXMLBind.getUltimaFase());
		}
		if (pesquisaXMLBind.getNomeResponsavel() != null) {
			pesquisa.getParameters().put("nomeResponsavel",
					pesquisaXMLBind.getNomeResponsavel());
		}
		if (pesquisaXMLBind.getIdResponsavel() != null) {
			pesquisa.getParameters().put("idResponsavel",
					pesquisaXMLBind.getIdResponsavel());
		}
		if (pesquisaXMLBind.getNomeListaTextos() != null) {
			pesquisa.getParameters().put("nomeListaTextos",
					pesquisaXMLBind.getNomeListaTextos());
		}
		if (pesquisaXMLBind.getIdListaTextos() != null) {
			pesquisa.getParameters().put("idListaTextos",
					pesquisaXMLBind.getIdListaTextos());
		}
		if (pesquisaXMLBind.getPalavraChave() != null) {
			pesquisa.getParameters().put("palavraChave",
					pesquisaXMLBind.getPalavraChave());
		}
		if (pesquisaXMLBind.getObservacao() != null) {
			pesquisa.getParameters().put("observacao",
					pesquisaXMLBind.getObservacao());
		}

		// Assunto
		if (pesquisaXMLBind.getIdAssunto() != null) {
			pesquisa.getParameters().put("idAssunto",
					pesquisaXMLBind.getIdAssunto());
		}
		if (pesquisaXMLBind.getDescricaoAssunto() != null) {
			pesquisa.getParameters().put("descricaoAssunto",
					pesquisaXMLBind.getDescricaoAssunto());
		}

		// Parte
		if (pesquisaXMLBind.getIdCategoriaParte() != null) {
			pesquisa.getParameters().put("idCategoriaParte",
					pesquisaXMLBind.getIdCategoriaParte());
		}
		if (pesquisaXMLBind.getNomeParte() != null) {
			pesquisa.getParameters().put("nomeParte",
					pesquisaXMLBind.getNomeParte());
		}

		// Ordenação
		if (pesquisaXMLBind.getOrdenacao() != null) {
			pesquisa.getParameters().put("ordenacao",
					pesquisaXMLBind.getOrdenacao());
		}

		if (pesquisaXMLBind.getPainelVisualizacao() != null) {
			pesquisa.getParameters().put("painelVisualizacao",
					pesquisaXMLBind.getPainelVisualizacao());
		}
	}

	/**
	 * Preenche a pesquisaAvancada a partir de um objeto Comunicação. Os valores
	 * referente ao Tipo de Situação do Documento é fixo. Na busca, não é levado
	 * em consideração as comunicações Canceladas pelo Ministro e são
	 * considerados as comunicações que estão Aguardando Assintura.
	 * 
	 * @param pesquisaAvancada
	 * @param Comunicacao
	 */
	@Override
	public void popularPesquisaComunicacao(Pesquisa pesquisa, Setor setor) {
		List<Long> tipoSituacaoDocumento = new LinkedList<Long>();
		tipoSituacaoDocumento.add(TipoSituacaoDocumento.GERADO.getCodigo());
		tipoSituacaoDocumento.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE
				.getCodigo());
		if (setor != null) {
			pesquisa.getParameters().put("codSetor", setor);
		}
		pesquisa.getParameters().put("tipoSituacaoDocumento",
				tipoSituacaoDocumento);
		pesquisa.getParameters().put("tipoFaseComunicacao",
				TipoFaseComunicacao.AGUARDANDO_ASSINATURA.getCodigoFase());
		pesquisa.getParameters().put("situacao",
				TipoSituacaoDocumento.GERADO.getCodigo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.jus.stf.estf.decisao.pesquisa.service.PesquisaService#
	 * excluirConfiguracaoPesquisa(java.lang.Long)
	 */
	@Override
	public void excluirConfiguracaoPesquisa(Long idConfiguracaoPesquisa,
			Principal principal) {
		ConfiguracaoUsuario confUsu;
		try {
			confUsu = configuracaoUsuarioService
					.recuperarPorId(idConfiguracaoPesquisa);

			if (confUsu != null) {
				if (confUsu.getUsuario().getId()
						.equals(principal.getUsuario().getId())
						|| confUsu
								.getSetor()
								.getId()
								.equals(principal.getMinistro().getSetor()
										.getId())) {
					configuracaoUsuarioService.excluir(confUsu);
					FacesMessages.instance().add("Configuração excluída.");
				}
			}
		} catch (ServiceException e) {
			FacesMessages.instance().add("Erro ao excluir configuração.");
			logger.error(e.getMessage(), e);
		}

	}
	//
	// @Override
	// public MinistroDto recuperarRelatorIncidente(Long idObjetoIncidente) {
	// return pesquisaDao.recuperarRelatorIncidente(idObjetoIncidente);
	// }
}
