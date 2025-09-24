package br.jus.stf.estf.decisao.pesquisa.web.texto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import br.gov.stf.estf.entidade.usuario.Responsavel;

@Converter
@BypassInterceptors
@Name(value = "responsavelConverter")
public class ResponsavelConverter implements javax.faces.convert.Converter {

	@In("#{responsavelFacesBean}")
	private ResponsavelFacesBean responsavelFacesBean;
	
	@Override
	public Responsavel getAsObject(FacesContext context, UIComponent component,
			String value) {
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		Responsavel responsavel = (Responsavel) value;
		return responsavel.getNome();
	}

}
