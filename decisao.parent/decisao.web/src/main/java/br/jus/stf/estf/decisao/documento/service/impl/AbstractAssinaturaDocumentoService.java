package br.jus.stf.estf.decisao.documento.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.context.SecurityContextHolder;

import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.documento.service.AssinaturaDocumentoService;
import br.jus.stf.estf.decisao.documento.service.PermissaoAssinaturaDocumentoService;
import br.jus.stf.estf.decisao.documento.support.DocumentoNaoAssinadoDto;
import br.jus.stf.estf.decisao.documento.support.ValidacaoPermissaoAssinaturaDocumento;
import br.jus.stf.estf.decisao.mobile.assinatura.support.DocumentoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa.TipoPesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.service.PesquisaService;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.service.ConfiguracaoSistemaService;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * Classse base para os serviços de Assinatura. Os métodos que efetivamente
 * realizam a assinatura (digital ou contingencial) serão implementados nas subclasses.
 * 
 * @author Tomas.Godoi
 * 
 */
public abstract class AbstractAssinaturaDocumentoService implements AssinaturaDocumentoService {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private PesquisaService pesquisaService;

	@Autowired
	protected TextoService textoService;

	@Autowired
	private ComunicacaoService comunicacaoService;
	
	@Autowired	
	protected ConfiguracaoSistemaService configuracaoSistemaService;
	
	@Autowired
	@Qualifier("permissaoAssinaturaTextoService")
	private PermissaoAssinaturaDocumentoService<TextoDto> permissaoAssinaturaTextoService;

	@Autowired
	@Qualifier("permissaoAssinaturaComunicacaoService")
	private PermissaoAssinaturaDocumentoService<ComunicacaoDto> permissaoAssinaturaComunicacaoService;

	@Autowired
	protected DocumentoComunicacaoService documentoComunicacaoService;
	
	@Autowired
	protected DocumentoEletronicoService documentoEletronicoService;

	@Override
	public List<TextoDto> recuperarTextosParaAssinar() throws ServiceException {
		return recuperarTextosParaAssinar(new ArrayList<DocumentoNaoAssinadoDto<TextoDto>>());
	}

	@Override
	public List<TextoDto> recuperarTextosParaAssinar(List<DocumentoNaoAssinadoDto<TextoDto>> naoAssinados) throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			PagedList<TextoDto> textosParaAssinatura = pesquisaService.pesquisarTextos(buildPesquisaTextosParaAssinar(ministro));
			return removerTextosNaoPodemSerAssinados(textosParaAssinatura.getResults(), naoAssinados);
		} else {
			throw new ServiceException("Usuário não está associado a um gabinete.");
		}
	}

	@Override
	public List<TextoDto> recuperarTextosParaAssinar(List<Long> textos, List<DocumentoNaoAssinadoDto<TextoDto>> textosNaoAssinados) throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			PagedList<TextoDto> textosParaAssinatura = pesquisaService.pesquisarTextos(buildPesquisaTextosParaAssinar(ministro, textos));
			return removerTextosNaoPodemSerAssinados(textosParaAssinatura.getResults(), textosNaoAssinados);
		} else {
			throw new ServiceException("Usuário não está associado a um gabinete.");
		}
	}

	@Override
	public List<DocumentoDto<TextoDto>> recuperarTextosParaAssinarMantendoNaoPermitidos() throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			PagedList<TextoDto> textosParaAssinatura = pesquisaService.pesquisarTextos(buildPesquisaTextosParaAssinar(ministro));
			return preencherDadosTextosNaoPodemSerAssinados(textosParaAssinatura.getResults());
		} else {
			throw new ServiceException("Usuário não está associado a um gabinete.");
		}
	}
	
	@Override
	public List<DocumentoDto<TextoDto>> recuperarTextosParaAssinarMantendoNaoPermitidos(List<Long> textos) throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			PagedList<TextoDto> textosParaAssinatura = pesquisaService.pesquisarTextos(buildPesquisaTextosParaAssinar(ministro, textos));
			return preencherDadosTextosNaoPodemSerAssinados(textosParaAssinatura.getResults());
		} else {
			throw new ServiceException("Usuário não está associado a um gabinete.");
		}
	}
	
	private List<TextoDto> removerTextosNaoPodemSerAssinados(List<TextoDto> textos, List<DocumentoNaoAssinadoDto<TextoDto>> naoAssinados) {
		List<TextoDto> textosPodeAsssinar = new ArrayList<TextoDto>();
		for (TextoDto texto : textos) {
			ValidacaoPermissaoAssinaturaDocumento<TextoDto> validacao = permissaoAssinaturaTextoService.documentoPodeSerAssinado(texto);
			if (validacao.isPodeAssinar()) {
				textosPodeAsssinar.add(validacao.getDocumento());
			} else {
				naoAssinados.add(validacao.getNaoAssinado());
			}
		}
		return textosPodeAsssinar;
	}
	
	private List<DocumentoDto<TextoDto>> preencherDadosTextosNaoPodemSerAssinados(List<TextoDto> textos) throws ServiceException {
		List<DocumentoDto<TextoDto>> textosAsDoc = new ArrayList<DocumentoDto<TextoDto>>();
		List<ValidacaoPermissaoAssinaturaDocumento<TextoDto>> validacoes =  permissaoAssinaturaTextoService.documentosPodemSerAssinados(textos);
		for (ValidacaoPermissaoAssinaturaDocumento<TextoDto> validacao : validacoes) {			
			if (validacao.isPodeAssinar()) {
				textosAsDoc.add(DocumentoDto.from(validacao.getDocumento(), validacao.getDevemSerAssinadosJunto()));
			} else {
				textosAsDoc.add(DocumentoDto.fromNaoAssinadoTexto(validacao.getNaoAssinado()));
			}
		}
		return textosAsDoc;
	} 
	
	@Override
	public int recuperarTotalTextosParaAssinar() {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			Pesquisa pesquisa = buildPesquisaTextosParaAssinar(ministro);
			pesquisa.setMaxResults(0); // Apenas contagem
			PagedList<TextoDto> textos = pesquisaService.pesquisarTextos(pesquisa);
			return textos.getTotal();
		} else {
			return 0; // Não pode assinar textos sem estar em um Gabinete de Ministro
		}
	}

	@Override
	public List<ComunicacaoDto> recuperarComunicacoesParaAssinar() throws ServiceException {
		return recuperarComunicacoesParaAssinar(new ArrayList<DocumentoNaoAssinadoDto<ComunicacaoDto>>());
	}

	@Override
	public List<ComunicacaoDto> recuperarComunicacoesParaAssinar(List<DocumentoNaoAssinadoDto<ComunicacaoDto>> naoAssinados) throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			PagedList<ComunicacaoDto> comunicacoesParaAssinatura = pesquisaService.pesquisarComunicacoes(buildPesquisaComunicacoesParaAssinar(ministro));
			return removerComunicacoesNaoPodemSerAssinadas(comunicacoesParaAssinatura.getResults(), naoAssinados);
		} else {
			throw new ServiceException("Usuário não está associado a um gabinete.");
		}
	}

	@Override
	public List<ComunicacaoDto> recuperarComunicacoesParaAssinar(List<Long> comunicacoes, List<DocumentoNaoAssinadoDto<ComunicacaoDto>> naoAssinados) throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			PagedList<ComunicacaoDto> comunicacoesParaAssinatura = pesquisaService.pesquisarComunicacoes(buildPesquisaComunicacoesParaAssinar(ministro, comunicacoes));
			return removerComunicacoesNaoPodemSerAssinadas(comunicacoesParaAssinatura.getResults(), naoAssinados);
		} else {
			throw new ServiceException("Usuário não está associado a um gabinete.");
		}
	}
	
	@Override
	public List<DocumentoDto<ComunicacaoDto>> recuperarComunicacoesParaAssinarMantendoNaoPermitidos() throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			PagedList<ComunicacaoDto> comunicacoesParaAssinatura = pesquisaService.pesquisarComunicacoes(buildPesquisaComunicacoesParaAssinar(ministro));
			return preencherDadosComunicacoesNaoPodemSerAssinadas(comunicacoesParaAssinatura.getResults());
		} else {
			throw new ServiceException("Usuário não está associado a um gabinete.");
		}
	}
	
	@Override
	public List<DocumentoDto<ComunicacaoDto>> recuperarComunicacoesParaAssinarMantendoNaoPermitidosMobile() throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			PagedList<ComunicacaoDto> comunicacoesParaAssinatura = pesquisaService.pesquisarComunicacoesMobile(buildPesquisaComunicacoesParaAssinar(ministro));
			return preencherDadosComunicacoesNaoPodemSerAssinadas(comunicacoesParaAssinatura.getResults());
		} else {
			throw new ServiceException("Usuário não está associado a um gabinete.");
		}
	}
	
	private List<ComunicacaoDto> removerComunicacoesNaoPodemSerAssinadas(List<ComunicacaoDto> comunicacoes,
			List<DocumentoNaoAssinadoDto<ComunicacaoDto>> naoAssinados) {
		List<ComunicacaoDto> comunicacoesPodeAssinar = new ArrayList<ComunicacaoDto>();
		for (ComunicacaoDto comunicacao : comunicacoes) {
			ValidacaoPermissaoAssinaturaDocumento<ComunicacaoDto> validacao = permissaoAssinaturaComunicacaoService.documentoPodeSerAssinado(comunicacao);
			if (validacao.isPodeAssinar()) {
				comunicacoesPodeAssinar.add(comunicacao);
			} else {
				naoAssinados.add(validacao.getNaoAssinado());
			}
		}
		return comunicacoesPodeAssinar;
	}

	private List<DocumentoDto<ComunicacaoDto>> preencherDadosComunicacoesNaoPodemSerAssinadas(List<ComunicacaoDto> comunicacoes) {
		List<DocumentoDto<ComunicacaoDto>> comunicacoesAsDoc = new ArrayList<DocumentoDto<ComunicacaoDto>>();
		for (ComunicacaoDto comunicacao : comunicacoes) {
			ValidacaoPermissaoAssinaturaDocumento<ComunicacaoDto> validacao = permissaoAssinaturaComunicacaoService.documentoPodeSerAssinado(comunicacao);
			if (validacao.isPodeAssinar()) {
				comunicacoesAsDoc.add(DocumentoDto.from(comunicacao));
			} else {
				comunicacoesAsDoc.add(DocumentoDto.fromNaoAssinadoComunicacao(validacao.getNaoAssinado()));
			}
		}
		return comunicacoesAsDoc;
	}
	
	@Override
	public int recuperarTotalComunicacoesParaAssinar() throws ServiceException {
		Ministro ministro = getMinistroGabineteUsuarioLogado();
		if (ministro != null) {
			Pesquisa pesquisa = buildPesquisaComunicacoesParaAssinar(ministro);
			return comunicacaoService.pesquisarPainelControle(TipoFaseComunicacao.valueOf((Long) pesquisa.get("tipoFaseComunicacao")),
					(Setor) pesquisa.get("codSetor"), null);
		} else {
			return 0; // Não pode assinar textos sem estar em um Gabinete de Ministro
		}
	}

	public Pesquisa buildPesquisaTextosParaAssinar(Ministro ministro) {
		Pesquisa pesquisa = new Pesquisa();
		pesquisa.setTipoPesquisa(TipoPesquisa.TEXTOS);
		pesquisa.put("ordenacao", "OCP");
		pesquisa.put("ultimaFase", false);
		pesquisa.put("nomeMinistroTexto", ministro.getNome());
		pesquisa.put("idMinistroTexto", ministro.getId());
		pesquisa.put("painelVisualizacao", false);
		pesquisa.put("idFaseTexto", FaseTexto.LIBERADO_ASSINATURA.getCodigoFase());
		pesquisa.setFirstResult(0);
		/*
		 * Valor alto necessário para a pesquisa não paginar.
		 * Não permitirá assinar mais do que essa quantidade ao mesmo tempo.
		 */
		pesquisa.setMaxResults(1000000);
		return pesquisa;
	}

	private Pesquisa buildPesquisaTextosParaAssinar(Ministro ministro, List<Long> textos) {
		Pesquisa pesquisa = buildPesquisaTextosParaAssinar(ministro);
		pesquisa.put("idsTextos", textos);
		return pesquisa;
	}
	
	public Pesquisa buildPesquisaComunicacoesParaAssinar(Ministro ministro) {
		Pesquisa pesquisa = new Pesquisa();
		pesquisa.setFirstResult(0);
		/*
		 * Valor alto necessário para a pesquisa não paginar.
		 * Não permitirá assinar mais do que essa quantidade ao mesmo tempo.
		 */
		pesquisa.setMaxResults(1000000);
		pesquisaService.popularPesquisaComunicacao(pesquisa, ministro.getSetor());
		pesquisa.setTipoPesquisa(TipoPesquisa.COMUNICACOES);

		if (ministro != null && ministro.getSetor() != null) {
			pesquisa.put("idSetorMinistroLogado", ministro.getSetor().getId());
		}

		return pesquisa;
	}

	private Pesquisa buildPesquisaComunicacoesParaAssinar(Ministro ministro, List<Long> comunicacoes) {
		Pesquisa pesquisa = buildPesquisaComunicacoesParaAssinar(ministro);
		pesquisa.put("idsComunicacoes", comunicacoes);
		return pesquisa;
	}
	
	/**
	 * Recupera o Ministro cujo gabinete o usuário logado está lotado.
	 * 
	 * @return
	 */
	protected Ministro getMinistroGabineteUsuarioLogado() {
		return getPrincipal().getMinistro();
	}

	/**
	 * Recupera o usuário autenticado.
	 * 
	 * @return o usuário logado
	 */
	protected Usuario getUsuario() {
		return getPrincipal().getUsuario();
	}

	private Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
