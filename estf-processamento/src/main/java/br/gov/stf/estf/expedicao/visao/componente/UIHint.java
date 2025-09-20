package br.gov.stf.estf.expedicao.visao.componente;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.icu.text.SimpleDateFormat;

import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.model.util.RelatorioRemessaDTO;

public class UIHint extends UIOutput {

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		Object value = getAttributes().get("value");
		
		RelatorioRemessaDTO relatorio = (RelatorioRemessaDTO)((ValueExpression)value).getValue(context.getELContext());
		
		ListaRemessa lista = relatorio.getRemessa().getListaRemessa();
		
		String usuarioCriacao = "Usuário de criação:";
		String dataCriacao = "Data de criação:";
		String usuarioFinalizacao = "Usuário de finalização:";
		String dataFinalizacao = "Data de finalização:";
		String peso = "Peso:";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		StringBuilder texto = new StringBuilder("<table style=\"text-align: left;\">");
		texto.append("<tr><td style=\"width: 115px;\"><strong>").append(usuarioCriacao).append("</strong></td><td style=\"text-align: left; width: 100px;\">").append(lista.getUsuarioCriacao()).append("</td><td><strong>").append(dataCriacao).append("</strong></td><td style=\"text-align: left;\">").append(sdf.format(lista.getDataCriacao())).append("</td></tr>");
		texto.append("</table>");
		texto.append("<table style=\"text-align: left;\">");
		texto.append("<tr><td><strong>").append(usuarioFinalizacao).append("</strong></td><td style=\"text-align: left;\">").append(lista.getUsuarioEnvio() != null ? lista.getUsuarioEnvio() : "Sem Usuário").append("</td><td><strong>").append(dataFinalizacao).append("</strong></td><td style=\"text-align: left;\">").append(lista.getDataFinalizacao() != null ? sdf.format(lista.getDataFinalizacao()) : "Sem Data").append("</td></tr>");
		texto.append("</table>");
		texto.append("<table style=\"text-align: left;\">");
		texto.append("<tr><td><strong>").append(peso).append("</strong></td><td style=\"text-align: left;\">").append(relatorio.getVolume().getPesoGramas()).append(" gramas</td>").append("</tr>");
		texto.append("</table>");
			
		writer.startElement("div", this);
		writer.write(texto.toString());
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
}
