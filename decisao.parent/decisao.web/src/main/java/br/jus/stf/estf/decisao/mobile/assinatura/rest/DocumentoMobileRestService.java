package br.jus.stf.estf.decisao.mobile.assinatura.rest;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.util.TipoAmbiente;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.mobile.assinatura.service.AssinarContingencialmenteMobileService;
import br.jus.stf.estf.decisao.mobile.assinatura.service.ComunicacaoMobileService;
import br.jus.stf.estf.decisao.mobile.assinatura.service.ComunicacaoParaAssinaturaService;
import br.jus.stf.estf.decisao.mobile.assinatura.service.TextoMobileService;
import br.jus.stf.estf.decisao.mobile.assinatura.service.TextoParaAssinaturaService;
import br.jus.stf.estf.decisao.mobile.assinatura.support.BasicAssinadorRestDto;
import br.jus.stf.estf.decisao.mobile.assinatura.support.DocumentoDto;
import br.jus.stf.estf.decisao.mobile.assinatura.support.MinistroAssinadorDto;
import br.jus.stf.estf.decisao.mobile.assinatura.support.SetorDto;
import br.jus.stf.estf.decisao.mobile.assinatura.support.VersaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.security.PermissionChecker;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;
import br.jus.stf.estf.decisao.support.util.GlobalFacesBean;

@Path("/assinador")
@Name("documentoMobileService")
@Transactional
public class DocumentoMobileRestService {

	@In("#{globalFacesBean}")
	private GlobalFacesBean globalFacesBean;

	@In("#{usuarioLogadoService}")
	private UsuarioLogadoService usuarioLogadoService;

	@In("#{assinarContingencialmenteMobileService}")
	private AssinarContingencialmenteMobileService assinarContingencialmenteMobileService;

	@In("#{textoParaAssinaturaService}")
	private TextoParaAssinaturaService textoParaAssinaturaService;

	@In("#{comunicacaoParaAssinaturaFacesBean}")
	private ComunicacaoParaAssinaturaService comunicacaoParaAssinaturaFacesBean;

	@In("#{documentoComunicacaoService}")
	private DocumentoComunicacaoService documentoComunicacaoService;

	@In("#{comunicacaoFacesBeanMobile}")
	private ComunicacaoMobileService comunicacaoFacesBeanMobile;

	@In("#{comunicacaoService}")
	private ComunicacaoService comunicacaoService;

	@In("#{permissionChecker}")
	private PermissionChecker permissionChecker;

	@In("#{textoFacesBeanMobile}")
	private TextoMobileService textoFacesBeanMobile;
	
	@In("#{appVersion}")
	private String versao;

	private TipoAmbiente tipoAmbiente;

	private void checarPermissaoAssinatura() throws ServiceException {
		if (!permissionChecker.hasPermission(usuarioLogadoService.getPrincipal(), ActionIdentification.ASSINAR_DIGITALMENTE)) {
			throw new ServiceException("Usuário não possui permissão para realizar esta operação.");
		}
	}
	
	@Create
	public void create() {
		// Manter essa linha, pois é a que força a inicialização dos
		// dados referentes aos servidores de conversão.
		tipoAmbiente = globalFacesBean.getTipoAmbiente();
	}

	@GET
	@Path("/versao")
	public Response recuperarVersao() {
		return Response.status(Response.Status.OK).entity(new VersaoDto(versao, tipoAmbiente.getDescricao())).build();
	}
	
	@GET
	@Path("/ministro")
	@Produces(MediaType.APPLICATION_JSON)
	public Response recuperarMinistro() {
		Ministro ministro = usuarioLogadoService.getMinistro();
		if (ministro != null) {
			return Response.status(Response.Status.ACCEPTED).entity(MinistroAssinadorDto.from(ministro)).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto("Usuário não está associado a um gabinete")).build();
		}
	}

	@GET
	@Path("/setores")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setores() {
		List<SetorDto> setores = SetorDto.from(usuarioLogadoService.getPrincipal().getSetores());
		if (!usuarioLogadoService.getPrincipal().getSetores().contains(usuarioLogadoService.getUsuario().getSetor())) {
			setores.add(SetorDto.from(usuarioLogadoService.getUsuario().getSetor()));
		}
		return Response.status(Response.Status.ACCEPTED).entity(setores).build();
	}
	
	@GET
	@Path("/setor/atual")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setorAtual() {
		SetorDto setorDto = new SetorDto();
		Long idSetor = usuarioLogadoService.getPrincipal().getIdSetor();
		boolean isSetorDeGabinete = false;
		for (Setor s : usuarioLogadoService.getPrincipal().getSetores()) {
			if (s.getId().equals(idSetor)) {
				setorDto.setId(idSetor);
				setorDto.setSigla(s.getSigla());
				setorDto.setNome(s.getNome());
				isSetorDeGabinete = true;
			}
		}
		if (!isSetorDeGabinete) {
			Setor setorUsuario = usuarioLogadoService.getPrincipal().getUsuario().getSetor();
			setorDto.setId(setorUsuario.getId());
			setorDto.setSigla(setorUsuario.getSigla());
			setorDto.setNome(setorUsuario.getNome());
		}
		return Response.status(Response.Status.ACCEPTED).entity(setorDto).build();
	}
	
	@POST
	@Path("/setor/mudar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mudarSetor(@PathParam("id") Long id) {
		try {
			usuarioLogadoService.changeSetor(id);
			return Response.status(Response.Status.ACCEPTED).build();
		} catch (ServiceException se) {
			return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto(se.getMessage())).build();
		}
	}
	
	@GET
	@Path("/documentos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response documentosParaAssinar() {
		try {
			checarPermissaoAssinatura();
			return buildResponse(assinarContingencialmenteMobileService.getDocumentosParaAssinar());
		} catch (ServiceException se) {
			return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto(se.getMessage())).build();
		}
	}

	@POST
	@Path("/comunicacao/{id}/devolver")
	@Produces(MediaType.APPLICATION_JSON)
	public Response devolverComunicacao(@PathParam("id") Long id) {
		try {
			if (!permissionChecker.hasPermission(usuarioLogadoService.getPrincipal(), ActionIdentification.DEVOLVER_COMUNICACOES)) {
				throw new ServiceException("Usuário não tem permissão para executar a ação.");
			}

			if (!comunicacaoParaAssinaturaFacesBean.isComunicacaoLiberadaParaAssinaturaNoGabinete(id)) {
				throw new ServiceException("Documento não pode ser devolvido!");
			}

			Comunicacao comunicacao = comunicacaoService.recuperarPorId(id);
			DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarNaoCancelado(comunicacao);
			if (documentoComunicacao == null) {
				throw new ServiceException("Documento não encontrado ou cancelado!");
			}

			if (documentoComunicacao.getTipoSituacaoDocumento().equals(TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO)) {
				throw new ServiceException("Documento previamente cancelado pelo Ministro!");
			}

			documentoComunicacaoService.devolverDocumentoeSTFDecisao(documentoComunicacao, null, usuarioLogadoService.getUsuario().getId());
			return Response.status(Response.Status.OK).build();
		} catch (ServiceException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto(e.getMessage())).build();
		}
	}

	@POST
	@Path("/texto/{id}/suspender")
	@Produces(MediaType.APPLICATION_JSON)
	public Response suspenderLiberacao(@PathParam("id") Long id) {
		try {
			List<DocumentoDto<TextoDto>> textosParaAssinar = textoParaAssinaturaService.getTextosParaAssinar(Arrays.asList(id));
			if (textosParaAssinar.size() == 1) {
				if (!permissionChecker.hasPermission(usuarioLogadoService.getPrincipal(), ActionIdentification.SUSPENDER_LIBERACAO)) {
					throw new ServiceException("Usuário não tem permissão para executar a ação.");
				} else {
					textoFacesBeanMobile.suspenderLiberacao(textosParaAssinar.get(0).getDocumentoFonte());
					return Response.status(Response.Status.ACCEPTED).build();
				}
			} else {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(buildErrorDto("Texto não está liberado para assinatura ou não pertence ao gabinete.")).build();
			}
		} catch (ServiceException se) {
			return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto(se.getMessage())).build();
		}
	}

	@GET
	@Path("/texto/{id}/detalhes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response detalhesTexto(@PathParam("id") Long id) throws ServiceException {
		try {
			checarPermissaoAssinatura();
			List<DocumentoDto<TextoDto>> textos = assinarContingencialmenteMobileService.getTextosComDetalhesParaAssinar(Arrays.asList(id));
			if (textos.size() == 1) {
				return buildResponse(textos.get(0).createDocumentoSemConteudo());
			} else if (textos.size() == 0) {
				return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto("O texto não foi encontrado.")).build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto("Apenas um texto é permitido por vez.")).build();
			}
		} catch (ServiceException se) {
			return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto(se.getMessage())).build();
		}
	}

	@GET
	@Path("/expediente/{id}/detalhes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response detalhesExpediente(@PathParam("id") Long id) throws ServiceException {
		try {
			checarPermissaoAssinatura();
			List<DocumentoDto<ComunicacaoDto>> expedientes = assinarContingencialmenteMobileService.getComunicacoesComDetalhesParaAssinar(Arrays.asList(id));
			if (expedientes.size() == 1) {
				return buildResponse(expedientes.get(0).createDocumentoSemConteudo());
			} else if (expedientes.size() == 0) {
				return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto("O expediente não foi encontrado.")).build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto("Apenas um expediente é permitido por vez.")).build();
			}
		} catch (ServiceException se) {
			return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorDto(se.getMessage())).build();
		}
	}

	@GET
	@Path("/texto/{id}/html")
	@Produces(MediaType.TEXT_HTML + "; charset=ISO-8859-1")
	public Response htmlTexto(@PathParam("id") Long id) throws ServiceException {
		try {
			checarPermissaoAssinatura();
			List<DocumentoDto<TextoDto>> textos = assinarContingencialmenteMobileService.getTextosComDetalhesParaAssinar(Arrays.asList(id));
			if (textos.size() == 1) {
				return Response.status(Response.Status.OK).entity(textos.get(0).getConteudo()).build();
			} else if (textos.size() == 0) {
				return Response.status(Response.Status.BAD_REQUEST).entity("O texto não foi encontrado.").build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity("Apenas um texto é permitido por vez.").build();
			}
		} catch (ServiceException se) {
			return Response.status(Response.Status.BAD_REQUEST).entity(se.getMessage()).build();
		}
	}

	@GET
	@Path("/expediente/{id}/pdf")
	public Response pdfExpediente(@PathParam("id") Long id) throws ServiceException {
		try {
			checarPermissaoAssinatura();
			List<ComunicacaoDto> expedientes = comunicacaoParaAssinaturaFacesBean.getComunicacoesParaAssinar(Arrays.asList(id));
			if (expedientes.size() == 1) {
				ResponseBuilder responseBuilder = Response.status(200).entity(comunicacaoFacesBeanMobile.gerarPDFComunicacao(expedientes.get(0)))
						.type("application/pdf");
				return responseBuilder.build();
			} else {
				throw new ServiceException("Apenas um expediente é permitido por vez.");
			}
		} catch (ServiceException se) {
			return Response.status(Response.Status.BAD_REQUEST).entity(se.getMessage()).build();
		}
	}

	private BasicAssinadorRestDto buildErrorDto(String errorMessage) {
		BasicAssinadorRestDto dto = new BasicAssinadorRestDto();
		dto.setErrors(Arrays.asList(errorMessage));
		return dto;
	}
	
	private Response buildResponse(List<DocumentoDto<?>> docs) {
		return Response.status(Response.Status.OK).entity(docs).build();
	}

	private Response buildResponse(DocumentoDto<?> doc) {
		return Response.status(Response.Status.OK).entity(doc).build();
	}
	
}
