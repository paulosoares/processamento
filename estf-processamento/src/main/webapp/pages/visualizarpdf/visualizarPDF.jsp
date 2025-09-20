<%@page import="br.gov.stf.estf.assinatura.visao.jsf.beans.visualizarpdf.BeanVisualizarPDF"%>
<%@page import="java.io.OutputStream"%>
<%@page import="br.gov.stf.framework.util.ConstantesMIME"%>

<%@ page session="false"%>
<%
	
	// scriptlets
	String mensagem = "";
	try {	
		
		BeanVisualizarPDF beanVisualizarPDF = new BeanVisualizarPDF();
		byte[] ret = beanVisualizarPDF.recuperarDocumentoPorId(Long.parseLong(request.getParameter("id")));
		
		//byte[] ret = (byte[])request.getAttribute(AssinadorBaseBean.VISUALIZAR_DOCUMENTO_PDF);
        if ( ret != null ) {
            response.setContentType(ConstantesMIME.PDF_MIME_TYPE);
            response.setContentLength(ret.length);

            OutputStream os = response.getOutputStream();

            os.write(ret);
            os.flush();    				
        } else {
            mensagem = mensagem + "Documento n&atilde;o encontrado.";
        }                             
	} catch(Exception e) {
            mensagem += "Ocorreu uma falha ao carregar o documento. " + e.getMessage();                    
            e.printStackTrace();
	} 

        if ( mensagem != null && !"".equals(mensagem) ) {
            response.setContentType("text/html; charset=windows-1252");            
            ServletOutputStream os2 = response.getOutputStream();
            os2.println("<HTML>");
            os2.println(mensagem);
            os2.println("</HTML>");               
            os2.flush();
            os2.close();
        }

%>