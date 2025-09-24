package br.jus.stf.estf.decisao.mobile.assinatura.rest;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.documento.web.AssinarExternamenteDocumentosFacesBean;
import br.jus.stf.estf.decisao.mobile.assinatura.support.AssinaturaDocumentoDto;
import br.jus.stf.estf.decisao.mobile.assinatura.support.AssinaturaExternaDocumentoDto;

@Path("/assinador/externo")
@Name("assinarExternamenteDocumentoService")
@Transactional
public class AssinarExternamenteDocumentoService {

	@In("#{assinarExternamenteDocumentosFacesBean}")
	private AssinarExternamenteDocumentosFacesBean assinarExternamenteDocumentosFacesBean;

	/**
	 * Serviço REST que recebe o Certificado (público) do usuário que vai
	 * realizar a assinatura do texto e retorna o hash para assinatura.
	 * 
	 * @param id
	 * @param certificado
	 * @return
	 * @throws ServiceException
	 */
	@POST
	@Path("/texto/{id}/pre-assinar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public Response preAssinarTexto(@PathParam("id") Long id, String[] cadeia) throws ServiceException {
		AssinaturaExternaDocumentoDto aedd = assinarExternamenteDocumentosFacesBean.preAssinarTextos(Arrays.asList(id), cadeia);
		if (!aedd.getErrors().isEmpty() || !aedd.getWarnings().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity(aedd).build();
		} else {
			return Response.status(Response.Status.ACCEPTED).entity(aedd).build();
		}
	}

	@POST
	@Path("/texto/{id}/pos-assinar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response posAssinarTexto(@PathParam("id") Long id, @FormParam("idContexto") String idContexto, @FormParam("assinatura") String assinatura) throws ServiceException {
		List<Long> asList = Arrays.asList(id);
		AssinaturaDocumentoDto add = assinarExternamenteDocumentosFacesBean.posAssinarTextos(asList, idContexto, assinatura);
		if (!add.getErrors().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity(add).build();
		} else {
			return Response.status(Response.Status.ACCEPTED).entity(add).build();
		}
	}

	/**
	 * Serviço REST que recebe o Certificado (público) do usuário que vai
	 * realizar a assinatura do texto e retorna o hash para assinatura.
	 * 
	 * @param id
	 * @param certificado
	 * @return
	 * @throws ServiceException
	 */
	@POST
	@Path("/comunicacao/{id}/pre-assinar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public Response preAssinarComunicacao(@PathParam("id") Long id, String[] cadeia) throws ServiceException {
		AssinaturaExternaDocumentoDto aedd = assinarExternamenteDocumentosFacesBean.preAssinarComunicacoes(Arrays.asList(id), cadeia);
		if (!aedd.getErrors().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity(aedd).build();
		} else {
			return Response.status(Response.Status.ACCEPTED).entity(aedd).build();
		}
	}

	@POST
	@Path("/comunicacao/{id}/pos-assinar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response posAssinarComunicacao(@PathParam("id") Long id, @FormParam("idContexto") String idContexto, @FormParam("assinatura") String assinatura)
			throws ServiceException {
		AssinaturaDocumentoDto add = assinarExternamenteDocumentosFacesBean.posAssinarComunicacoes(Arrays.asList(id), idContexto, assinatura);
		if (!add.getErrors().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity(add).build();
		} else {
			return Response.status(Response.Status.ACCEPTED).entity(add).build();
		}
	}

}
