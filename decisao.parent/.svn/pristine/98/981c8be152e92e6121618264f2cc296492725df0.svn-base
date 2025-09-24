/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.RelatorioProcessoService;
import br.jus.stf.estf.decisao.objetoincidente.support.ImprimirListaProcessosException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoRecursoRelatorioEnum;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoReport;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.util.ReportUtils;
import br.jus.stf.estf.report.ReportException;
import br.jus.stf.estf.report.ReportOutputStrategy;
import br.jus.stf.estf.report.model.NumeroMaximoDeColunasVioladoException;
import br.jus.stf.estf.report.model.Report;
import br.jus.stf.estf.report.model.Report.LineType;
import br.jus.stf.estf.report.model.Report.Margin;
import br.jus.stf.estf.report.model.Report.Orientation;
import br.jus.stf.estf.report.model.Report.Page;
import br.jus.stf.estf.report.model.Report.ReportType;

/**
 * @author Paulo.Estevao
 * @since 29.09.2010
 */
@Action(id="imprimirResultadoPesquisaProcessosActionFacesBean", 
		name="Imprimir Resultado da Pesquisa", 
		view="/acoes/objetoincidente/imprimirResultadoPesquisa.xhtml",
		height=300,
		width=550)
@Restrict({ActionIdentification.IMPRIMIR_LISTA_DE_PROCESSOS})
@RequiresResources(Mode.Many)
public class ImprimirResultadoPesquisaActionFacesBean extends ActionSupport<ObjetoIncidenteDto> implements ReportOutputStrategy {

	private FileOutputStream reportOutputStream;

	@Qualifier("relatorioProcessoService")
	@Autowired
	private RelatorioProcessoService relatorioProcessoService;
	
	private List<SelectItem> listaColunas;
	private List<String> listaColunasSelecionadas;
	private Map<String, ProcessoRecursoRelatorioEnum> mapaEnums = new HashMap<String, ProcessoRecursoRelatorioEnum>();
	private File reportOutputFile;

	private static final String REPORT_NAME = "Relatório de Lista de Textos de Processos";

	public ImprimirResultadoPesquisaActionFacesBean() {
		listaColunas = new LinkedList<SelectItem>();

		ProcessoRecursoRelatorioEnum[] processoEnums = ProcessoRecursoRelatorioEnum.values();

		for (ProcessoRecursoRelatorioEnum processoEnum : processoEnums) {
			mapaEnums.put(processoEnum.getAtributo(), processoEnum);
			listaColunas.add(new SelectItem(processoEnum.getAtributo(), processoEnum.getDescricao()));
		}
	}

	public void gerarRelatorioListaProcessos(List<ProcessoRecursoRelatorioEnum> camposRelatorio, int fontSize)
			throws ImprimirListaProcessosException, NumeroMaximoDeColunasVioladoException {
		try {

			Map<Margin, Integer> marginMap = new HashMap<Margin, Integer>();
			marginMap.put(Margin.LEFT, 20);
			marginMap.put(Margin.RIGHT, 20);
			marginMap.put(Margin.TOP, 20);
			marginMap.put(Margin.BOTTOM, 20);
			Report report = new Report(REPORT_NAME, Page.A4, Orientation.PORTRAIT, marginMap, LineType.ALL, 5,
					ReportType.PDF, this);

			report.addFields(ProcessoReport.getFields(camposRelatorio));

			Collection<ProcessoReport> listaTextosRelatorio = relatorioProcessoService.recuperaProcessoReport(getResources());

			report.generateReport(listaTextosRelatorio);

		} catch (NumeroMaximoDeColunasVioladoException e) {
			throw e;
		} catch (ReportException e) {
			throw new ImprimirListaProcessosException("Erro ao gerar Relatório", e);
		} catch (SecurityException e) {
			throw new ImprimirListaProcessosException("Erro ao gerar Relatório", e);
		} catch (ServiceException e) {
			throw new ImprimirListaProcessosException("Erro ao gerar Relatório", e);
		}
	}
	
	public void report() {
		try {
			List<ProcessoRecursoRelatorioEnum> parametros = new LinkedList<ProcessoRecursoRelatorioEnum>();

			for (String selectItem : listaColunasSelecionadas) {
				parametros.add(mapaEnums.get(selectItem));
			}

			gerarRelatorioListaProcessos(parametros, 8);
			
			ReportUtils.report(new FileInputStream(reportOutputFile));
		} catch (NumeroMaximoDeColunasVioladoException e) {
			addError(e.getMessage());
		} catch (ImprimirListaProcessosException e) {
			addError(e.getMessage());
		} catch (FileNotFoundException e) {
			addError(e.getMessage());
		}

		if(hasMessages()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
		
	}

	public OutputStream getOutputStreamFor(String reportName) throws IOException {
		reportOutputFile = File.createTempFile(reportName, ".tmp");
		reportOutputStream = new FileOutputStream(reportOutputFile);
		return reportOutputStream;
	}

	public List<SelectItem> getListaColunas() {
		return listaColunas;
	}

	public void setListaColunas(List<SelectItem> listaColunas) {
		this.listaColunas = listaColunas;
	}

	public List<String> getListaColunasSelecionadas() {
		return listaColunasSelecionadas;
	}

	public void setListaColunasSelecionadas(List<String> listaColunasSelecionadas) {
		this.listaColunasSelecionadas = listaColunasSelecionadas;
	}
}
