package br.gov.stf.estf.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jsoup.Jsoup;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;

public class Excel {
	
	public static void incluirCabecalhoArquivo(Sheet sheet, String descricaoPesquisa){
		int rowNum = 0;
		CellStyle style = sheet.getRow(0).getCell(0).getCellStyle();
		HSSFFont font = (HSSFFont)sheet.getWorkbook().createFont(); 
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);	
		font.setFontHeight((short) 14);
		style.setFont(font);
		sheet.createRow(rowNum++);
		CellStyle rowStyle = sheet.getWorkbook ().createCellStyle();
		rowStyle.setVerticalAlignment((short) 1);
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 12);
		font.setColor(HSSFColor.DARK_BLUE.index);
		rowStyle.setFont(font);
		rowStyle.setAlignment((short) 2);

		
		sheet.setDefaultColumnStyle(0,style);
		HSSFRow rowDesc = (HSSFRow) sheet.createRow(rowNum++);
		HSSFCell cellDesc = rowDesc.createCell(sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5)));			
		cellDesc.setCellValue(descricaoPesquisa);	
		sheet.autoSizeColumn(0);
		cellDesc.setCellStyle(rowStyle);
		cellDesc.getRow().setHeight((short)700);
		
		String[] colunas = {"Colegiado", "Relator", "Lista","Tipo de Lista", "Sessão", "Dispositivo"};
		HSSFRow row = (HSSFRow) sheet.createRow(rowNum++);		
		
		int countHeaders = 0;
		for (int i = 0; i < colunas.length; i++){
			HSSFCell cell = row.createCell(countHeaders++);			
			cell.setCellValue(colunas[i]);	
			sheet.autoSizeColumn(i);
			cell.setCellStyle(rowStyle);
			cell.getRow().setHeight((short)700);
		}

		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);	
		style.setFont(font);
		sheet.createRow(rowNum++);
	}
	
	public static void incluirCabecalhoArquivoProcesso(Sheet sheet, String descricaoPesquisa, String[] colunas, HSSFFont font){
		int rowNum = 0;
		CellStyle style = sheet.getRow(0).getCell(0).getCellStyle();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);	
		font.setFontHeight((short) 14);
		style.setFont(font);
		sheet.createRow(rowNum++);
		CellStyle rowStyle = sheet.getWorkbook ().createCellStyle();
		rowStyle.setVerticalAlignment((short) 1);
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 12);
		font.setColor(HSSFColor.DARK_BLUE.index);
		rowStyle.setFont(font);
		rowStyle.setAlignment((short) 2);

		
		sheet.setDefaultColumnStyle(0,style);
		HSSFRow rowDesc = (HSSFRow) sheet.createRow(rowNum++);
		HSSFCell cellDesc = rowDesc.createCell(sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9)));			
		cellDesc.setCellValue(descricaoPesquisa);	
		sheet.autoSizeColumn(0);
		cellDesc.setCellStyle(rowStyle);
		cellDesc.getRow().setHeight((short)700);

		
		HSSFRow row = (HSSFRow) sheet.createRow(rowNum++);		
		
		int countHeaders = 0;
		for (int i = 0; i < colunas.length; i++){
			HSSFCell cell = row.createCell(countHeaders++);			
			cell.setCellValue(colunas[i]);	
			sheet.autoSizeColumn(i);
			cell.setCellStyle(rowStyle);
			cell.getRow().setHeight((short)700);
		}

	}
	
	public static void incluirDadosLista(Sheet sheet, ListaJulgamento lista, int numRow){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		HSSFRow row = (HSSFRow) sheet.createRow(numRow);
		HSSFFont font = (HSSFFont)sheet.getWorkbook().createFont();
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);	
		font.setColor((short) 0);
		CellStyle rowStyle = sheet.getWorkbook ().createCellStyle();
		rowStyle.setVerticalAlignment((short) 1);
		font.setFontName("Arial");
		font.setFontHeightInPoints((short)8);
		rowStyle.setFont(font);
		rowStyle.setAlignment((short) 2);

		HSSFCell colegiadoCell = row.createCell(0);				
		colegiadoCell.setCellValue(lista.getSessao().getColegiado().getDescricao());
		colegiadoCell.getCellStyle().setFont(font);
		sheet.autoSizeColumn(0);
		colegiadoCell.setCellStyle(rowStyle);
		
		HSSFCell relatorCell = row.createCell(1);
		relatorCell.setCellValue(lista.getMinistro().getNome());
		relatorCell.getCellStyle().setFont(font);
		sheet.autoSizeColumn(1);
		relatorCell.setCellStyle(rowStyle);
		
		HSSFCell listaCell = row.createCell(2);
		listaCell.setCellValue(lista.getNome() + "(" + lista.getQuantidadeProcessos() + ")");
		listaCell.getCellStyle().setFont(font);
		sheet.autoSizeColumn(2);
		listaCell.setCellStyle(rowStyle);
		
		HSSFCell TipoListaCell = row.createCell(3);
		TipoListaCell.setCellValue(lista.getTipoListaJulgamento().getDescricao());
		TipoListaCell.getCellStyle().setFont(font);
		sheet.autoSizeColumn(3);
		TipoListaCell.setCellStyle(rowStyle);
		
		HSSFCell sessaoCell = row.createCell(4);
		sessaoCell.setCellValue(sdf.format(lista.getSessao().getDataInicio() == null ? lista.getSessao().getDataPrevistaInicio() : lista.getSessao().getDataInicio()) + 
				" a " + sdf.format(lista.getSessao().getDataFim() == null ? lista.getSessao().getDataPrevistaFim() : lista.getSessao().getDataFim() ));
		sessaoCell.getCellStyle().setFont(font);
		sheet.autoSizeColumn(4);
		sessaoCell.setCellStyle(rowStyle);
				
		
		CellStyle DispositivoStyle = sheet.getWorkbook ().createCellStyle ();
		DispositivoStyle.setWrapText(true);
		DispositivoStyle.setFont ( font );
		DispositivoStyle.setVerticalAlignment((short) 1);
		
		HSSFCell dispositivoCell = row.createCell(5);
		Long tam = 1/100L;
		if (lista.getCabecalho() != null && !lista.getCabecalho().isEmpty()) {
			dispositivoCell.setCellValue(Jsoup.parse(lista.getCabecalho()).text());
			tam = (long) (Jsoup.parse(lista.getCabecalho()).text().length()/100);
		}
		dispositivoCell.getCellStyle().setFont(font);
		sheet.autoSizeColumn(5);	
		dispositivoCell.setCellStyle(DispositivoStyle);
		dispositivoCell.getRow().setHeight((short)(400+(300*tam)));
		sheet.setColumnWidth(5, 25000);

	}
	
	public static void incluirDadosListaProcesso(Sheet sheet, ListaJulgamento lista, int numRow, ObjetoIncidente<?> oi,
			Processo processo, ProcessoListaJulgamento plj, VotoJulgamentoProcesso voto,
			Map<String, CellStyle> estilosDasCelulas, HSSFFont font, Font linkProcessoFonte) {

		HSSFRow row = (HSSFRow) sheet.createRow(numRow);
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font.setColor((short) 0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		CellStyle rowStyle = estilosDasCelulas.get("rowStyle");
		rowStyle.setVerticalAlignment((short) 1);
		font.setFontName("Arial");
		rowStyle.setFont(font);
		rowStyle.setAlignment((short) 2);

		HSSFCell colegiadoCell = row.createCell(0);
		colegiadoCell.setCellValue(lista.getSessao().getColegiado().getDescricao());
		colegiadoCell.getCellStyle().setFont(font);
//		sheet.autoSizeColumn(0);
		colegiadoCell.setCellStyle(rowStyle);

		HSSFCell sessaoCell = row.createCell(1);
		sessaoCell.setCellValue(sdf
				.format(lista.getSessao().getDataInicio() == null ? lista.getSessao().getDataPrevistaInicio()
						: lista.getSessao().getDataInicio())
				+ " a " + sdf.format(lista.getSessao().getDataFim() == null ? lista.getSessao().getDataPrevistaFim()
						: lista.getSessao().getDataFim()));
		sessaoCell.getCellStyle().setFont(font);
//		sheet.autoSizeColumn(1);
		sessaoCell.setCellStyle(rowStyle);

		HSSFCell relatorCell = row.createCell(2);
		relatorCell.setCellValue(lista.getMinistro().getNome());
		relatorCell.getCellStyle().setFont(font);
//		sheet.autoSizeColumn(2);
		relatorCell.setCellStyle(rowStyle);

		HSSFCell listaCell = row.createCell(3);
		listaCell.setCellValue(lista.getNome() + " (" + lista.getQuantidadeProcessos() + ")");
		listaCell.getCellStyle().setFont(font);
//		sheet.autoSizeColumn(3);
		listaCell.setCellStyle(rowStyle);

		linkProcessoFonte.setFontName("Arial");
		linkProcessoFonte.setFontHeightInPoints((short) 9);
		linkProcessoFonte.setColor(HSSFColor.BLUE.index);
		linkProcessoFonte.setUnderline((byte) 1);
		CellStyle linkProcessoStyle = estilosDasCelulas.get("linkProcessoStyle");
		linkProcessoStyle.setFont(linkProcessoFonte);
		linkProcessoStyle.setVerticalAlignment((short) 1);

		HSSFHyperlink url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		url_link.setAddress("http://portal.stf.jus.br/processos/detalhe.asp?incidente=" + processo.getPrincipal().getId());
		HSSFCell LinkProcesso = row.createCell(4);
		LinkProcesso.setCellValue(oi.getPrincipal().getIdentificacao());
		LinkProcesso.setHyperlink(url_link);
		LinkProcesso.setCellStyle(linkProcessoStyle);
//		sheet.autoSizeColumn(4);
		linkProcessoStyle.setVerticalAlignment((short) 1);

		
		HSSFHyperlink url_link_pecas = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		url_link_pecas.setAddress(
				"https://digital.stf.jus.br/informacoes/processos/" + processo.getSiglaClasseProcessual()+processo.getNumeroProcessual()+"/pecas");
		HSSFCell LinkPecas = row.createCell(5);
		LinkPecas.setCellValue("Peças");
		LinkPecas.setHyperlink(url_link_pecas);
		LinkPecas.setCellStyle(linkProcessoStyle);
//		sheet.autoSizeColumn(4);
		linkProcessoStyle.setVerticalAlignment((short) 1);
		
		
		String texto_ementa =" ";
		List<Texto> textos = plj.getObjetoIncidente().getTextos();
		for (Texto texto : textos) {
			if(texto.getTipoTexto().equals(TipoTexto.EMENTA)) {
				texto_ementa = extrairStringRTF(texto.getArquivoEletronico().getConteudo());
			}
		}
		
		CellStyle EmentaStyle = sheet.getWorkbook ().createCellStyle ();
		EmentaStyle.setWrapText(true);
		EmentaStyle.setFont ( font );
		EmentaStyle.setVerticalAlignment((short) 1);
		HSSFHyperlink url_link_ementa = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		HSSFCell LinkEmenta = row.createCell(6);
		LinkEmenta.setCellValue(texto_ementa);
		EmentaStyle.setWrapText(true);
		LinkEmenta.setCellStyle(EmentaStyle);
//		sheet.autoSizeColumn(2);
		linkProcessoStyle.setVerticalAlignment((short) 1);
		
		
		HSSFCell SiglaClasse = row.createCell(7);
		CellStyle style1 = estilosDasCelulas.get("style1");
		SiglaClasse.setCellValue(processo.getSiglaClasseProcessual());
		SiglaClasse.getCellStyle().setFont(font);
//		sheet.autoSizeColumn(5);
		SiglaClasse.setCellStyle(rowStyle);

		HSSFCell NumeroProcesso = row.createCell(8);
		NumeroProcesso.setCellValue(processo.getNumeroProcessual());
		NumeroProcesso.getCellStyle().setFont(font);
		NumeroProcesso.setCellStyle(style1);
//		sheet.autoSizeColumn(6);
		NumeroProcesso.setCellStyle(rowStyle);

		HSSFCell Cadeia = row.createCell(9);
		Cadeia.setCellValue(plj.getOrdemNaLista() + " - " + oi.getIdentificacao());
		Cadeia.getCellStyle().setFont(font);
		Cadeia.setCellStyle(style1);
//		sheet.autoSizeColumn(7);
		Cadeia.setCellStyle(rowStyle);

		HSSFCell pedido = row.createCell(10);
		pedido.setCellValue(plj.getJulgamentoProcesso() == null? "": plj.getJulgamentoProcesso().getObservacao());		
		pedido.getCellStyle().setFont(font);
		pedido.setCellStyle(style1);
//		sheet.autoSizeColumn(8);
		pedido.setCellStyle(rowStyle);

		CellStyle AssuntosStyle = estilosDasCelulas.get("AssuntosStyle");
		AssuntosStyle.setWrapText(true);
		AssuntosStyle.setFont(font);
		AssuntosStyle.setVerticalAlignment((short) 1);

		HSSFCell dispositivoCell = row.createCell(11);
		if (lista.getCabecalho() != null && !lista.getCabecalho().isEmpty())
			dispositivoCell.setCellValue(Jsoup.parse(lista.getCabecalho()).text());
		dispositivoCell.getCellStyle().setFont(font);
		dispositivoCell.setCellStyle(AssuntosStyle);
//		sheet.autoSizeColumn(9);
		sheet.setColumnWidth(9, 30000);

		HSSFCell Rascunho = row.createCell(12);
		if (voto != null && voto.getId() != null) {
			Rascunho.setCellValue(voto.getTipoVoto().getDescricao());
		}
		Rascunho.getCellStyle().setFont(font);
//		sheet.autoSizeColumn(10);
		Rascunho.setCellStyle(rowStyle);

		String assuntos = "";
		String assuntoPai = "";
		String assuntosPai = "";
		assuntoPai = "";
		for (int i = 0; i < processo.getAssuntos().size(); i++) {
			assuntoPai = "";
			if (processo.getAssuntos().get(i) != null && processo.getAssuntos().get(i).getAssuntoPai() != null) {
				if (assuntos == "") {
					assuntos = processo.getAssuntos().get(i).getDescricaoCompleta();
				} else {
					assuntos = assuntos + "\n" + processo.getAssuntos().get(i).getDescricaoCompleta();
				}
				for (int j = 0; j < processo.getAssuntos().get(i).getDescricaoCompleta().length(); j++) {

					if (processo.getAssuntos().get(i).getDescricaoCompleta().subSequence(j, j + 1).equals("|")) {
						j = processo.getAssuntos().get(i).getDescricaoCompleta().length();
					} else {
						assuntoPai = assuntoPai
								+ processo.getAssuntos().get(i).getDescricaoCompleta().subSequence(j, j + 1);
					}
				}
			}
			if (!assuntosPai.contains(assuntoPai) && assuntoPai != "") {
				if (i == 0) {
					assuntosPai = assuntoPai;
				} else {
					assuntosPai = assuntoPai + "\n" + assuntosPai;
				}
			}
		}

		HSSFCell AssuntoPai = row.createCell(13);
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);
		CellStyle AssuntoPaiStyle = estilosDasCelulas.get("AssuntoPaiStyle");
		AssuntoPaiStyle.setWrapText(true);
		AssuntoPaiStyle.setFont(font);
		AssuntoPaiStyle.setVerticalAlignment((short) 1);

		AssuntoPai.setCellStyle(AssuntoPaiStyle);
		AssuntoPai.setCellValue(assuntosPai);
		AssuntoPai.getCellStyle().setFont(font);
//		sheet.autoSizeColumn(11);

		HSSFCell Assuntos = row.createCell(14);
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);

		Assuntos.setCellStyle(AssuntosStyle);
		Assuntos.setCellValue(assuntos);
		Assuntos.getCellStyle().setFont(font);
//		sheet.autoSizeColumn(12);
		Assuntos.getRow().setHeight((short) (450 * processo.getAssuntos().size()));
	}
	
	public static String extrairStringRTF(byte[] bytes) {
		RTFEditorKit rtfe = new RTFEditorKit();
		DefaultStyledDocument doc = new DefaultStyledDocument();

		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			rtfe.read(bais, doc, 0);
			return doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}

}
