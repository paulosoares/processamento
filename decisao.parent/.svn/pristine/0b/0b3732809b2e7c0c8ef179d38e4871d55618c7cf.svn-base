package br.jus.stf.estf.decisao.objetoincidente.support;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class JasperReportBuilder {

	public static void main(String[] args) {
		try {
			List<ListaProcessosReport> processos = recuperaDadosRelatorio();
			List<ListaGenericaReport> processos2 = recuperaDadosRelatorio2();
			//new JasperReportBuilder().build(processos2, "Relatorio.jasper", null);
			//new JasperReportBuilder().build();
			//new JasperReportBuilder().rela();
			//new JasperReportBuilder().build();
			//new JasperReportBuilder().novo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<ListaProcessosReport> recuperaDadosRelatorio() {
		List<ListaProcessosReport> dados = new ArrayList<ListaProcessosReport>();
		List<ListaGenericaReport> dados2 = new ArrayList<ListaGenericaReport>();
		for (int i = 0; i < 10; i++) {
			ListaProcessosReport processo = new ListaProcessosReport();
			//processo.setCabecalho("<b>cabecalho</b> teste teste");
			processo.setIdentificacaoCompletaProcesso("SUPREMO TRIBUNAL FEDERAL "
					+ i);
			List<ListaGenericaReport> titulos = new ArrayList<ListaGenericaReport>();
			for (int j = 0; j < 4; j++) {
				titulos.add(new ListaGenericaReport("Titulo " + i + "-" + j,
						"Descricao " + i + "-" + j));
			}
			dados2.addAll( titulos );
			processo.setInformacoesProcesso(titulos);
			dados.add(processo);
		}
		return dados;
	}
	
	
	private static List<ListaGenericaReport> recuperaDadosRelatorio2() {
		List<ListaProcessosReport> dados = new ArrayList<ListaProcessosReport>();
		List<ListaGenericaReport> dados2 = new ArrayList<ListaGenericaReport>();
		for (int i = 0; i < 10; i++) {
			ListaProcessosReport processo = new ListaProcessosReport();
			//processo.setCabecalho("<b>cabecalho</b> teste teste");
			processo.setIdentificacaoCompletaProcesso("SUPREMO TRIBUNAL FEDERAL "
					+ i);
			List<ListaGenericaReport> titulos = new ArrayList<ListaGenericaReport>();
			for (int j = 0; j < 4; j++) {
				titulos.add(new ListaGenericaReport("Titulo " + i + "-" + j,
						"Descricao " + i + "-" + j));
			}
			dados2.addAll( titulos );
			processo.setInformacoesProcesso(titulos);
			dados.add(processo);
		}
		return dados2;
	}


//	public void build(Collection dados, String nomeDoRelatorio, Map parametros)
//			throws JRException {
//
//		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
////		InputStream relatorio = Thread.currentThread().getContextClassLoader()
////				.getResourceAsStream(nomeDoRelatorio);
//
//		InputStream relatorio = null;
//		try {
//			relatorio = new FileInputStream("D:\\sdk\\ide\\eclipse\\eclipse-jee-helios-SR2-win32\\workspace\\ProcessoJudicial\\decisao.parent-CP\\decisao.web\\src\\main\\resources\\Relatorio.jasper");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		JasperPrint jasperPrint = JasperFillManager.fillReport(relatorio, parametros, dataSource);
//		JasperExportManager.exportReportToPdfFile(jasperPrint,"d:\\temp\\Reports\\relatorio.pdf");
//	}
//	
//	
//	
//	
//	
//		public void build() throws Exception {
//			InputStream inputStream = new FileInputStream ("D:\\sdk\\ide\\eclipse\\eclipse-jee-helios-SR2-win32\\workspace\\ProcessoJudicial\\decisao.parent-CP\\decisao.web\\src\\main\\resources\\Relatorio.jasper");
//			
//			List<ListaGenericaReport> dados2 = new ArrayList<ListaGenericaReport>();
//			dados2 = recuperaDadosRelatorio2();
//			 
//			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource( dados2 );
//			 
//			Map parameters = new HashMap();
//			 
//			JasperDesign jasperDesign = JRXmlLoader.load( inputStream );
//			JasperReport jasperReport = JasperCompileManager.compileReport( jasperDesign );
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
//			JasperExportManager.exportReportToPdfFile(jasperPrint, "c:/reports/test_jasper.pdf");
//		}
//
//		public void temp(){
//			String fileName = "D:\\sdk\\ide\\eclipse\\eclipse-jee-helios-SR2-win32\\workspace\\ProcessoJudicial\\decisao.parent-CP\\decisao.web\\src\\main\\resources\\Relatorio.jrxml";
//	        String outFileName = "D:\\sdk\\ide\\eclipse\\eclipse-jee-helios-SR2-win32\\workspace\\ProcessoJudicial\\decisao.parent-CP\\decisao.web\\src\\main\\resources\\teste.pdf";
//	        HashMap hm = new HashMap();
//	        
//	        
//	        try {
//	        	InputStream inputStream = new FileInputStream ("D:\\sdk\\ide\\eclipse\\eclipse-jee-helios-SR2-win32\\workspace\\ProcessoJudicial\\decisao.parent-CP\\decisao.web\\src\\main\\resources\\teste.jasper");
//	            // Fill the report using an empty data source
//	        	
//	            JasperPrint print = JasperFillManager.fillReport(inputStream, new HashMap(), new JREmptyDataSource());
//	            
//	            // Create a PDF exporter
//	            JRExporter exporter = new JRPdfExporter();
//	            
//	            // Configure the exporter (set output file name and print object)
//	            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
//	            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//	            
//	            // Export the PDF file
//	            exporter.exportReport();
//	            
//	        } catch (JRException e) {
//	            e.printStackTrace();
//	            System.exit(1);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            System.exit(1);
//	        }
//		}
//		
//		
//		
//		public void novo(){
//			JasperPrint jasperPrint;
//			try {
//				jasperPrint = JasperFillManager.fillReport( new FileInputStream("D:\\relatorio\\Relatorio.jasper"),
//						new HashMap<String, Object>(), 
//						new JREmptyDataSource());
//				JasperExportManager.exportReportToPdfFile(jasperPrint, "D:\\relatorio\\Relatorio.pdf");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		    public void rela() throws Exception {  
		        // inicializando o velocity  
		        VelocityEngine ve = new VelocityEngine();  
		        ve.init();  
		  
		        // criando o contexto que liga o java ao template  
		        VelocityContext context = new VelocityContext();  
		  
		        // escolhendo o template  
		        Template t = ve.getTemplate("template1.vm");  
		      
		        // variavel que sera acessada no template:  
		        ArrayList list = new ArrayList();  
		        list.add("Item 1");  
		        list.add("Item 2");  
		        list.add("Item 3");  
		        list.add("Item 4");  
		        list.add("Item 5");  
		  
		        // aqui! damos a variavel list para  
		        // o contexto!  
		        context.put("lista", list);   
		        StringWriter writer = new StringWriter();  
		  
		        // mistura o contexto com o template  
		        t.merge(context, writer);  
		  
		        System.out.println(writer.toString());  
		    }  
	
	
}
