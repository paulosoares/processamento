package br.jus.stf.estf.decisao.objetoincidente.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.converter.DocumentTarget;
import br.gov.stf.estf.converter.target.FileDocumentTarget;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.web.support.TipoDeColegiado;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
import br.jus.stf.estf.decisao.support.util.ReportUtils;
import br.jus.stf.estf.montadortexto.MinutaSource;
import br.jus.stf.estf.montadortexto.MontadorMinutaService;
import br.jus.stf.estf.montadortexto.MontadorMinutaServiceException;
import br.jus.stf.estf.montadortexto.Pair;
import br.jus.stf.estf.montadortexto.SpecCabecalho;
import br.jus.stf.estf.montadortexto.SpecDadosMinuta;
import br.jus.stf.estf.montadortexto.SpecMarcaDagua;

@Action(id="imprimirMinutaActionFacesBean", name="Imprimir minuta", view="/acoes/objetoincidente/imprimirMinuta.xhtml", height=150, width=420)
@Restrict({ActionIdentification.IMPRIMIR_MINUTA})
@RequiresResources(Mode.Many)
public class ImprimirMinutaActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {

	private ObjetoIncidente<?> objetoIncidente;
	
	private String colegiado;
	
	@Autowired
	private MontadorMinutaService montadorMinutaService;
	
	@Qualifier("objetoIncidenteServiceLocal") 
	@Autowired 
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private CabecalhoObjetoIncidenteService cabecalhoBuilderService;
	
	/**
     * Executa as regras para geração de minuta.
     */
    public void execute() {
    	try {
			Collection<ObjetoIncidenteDto> listaObjetoIncidenteDto = getResources();
			
			Map<Long, SpecMarcaDagua> specMarcaDaguaMap = new HashMap<Long, SpecMarcaDagua>();
			
			List<Pair<MinutaSource, SpecCabecalho<Long>>> sources = new LinkedList<Pair<MinutaSource, SpecCabecalho<Long>>>();
			
			for (final ObjetoIncidenteDto objetoIncidenteDto : listaObjetoIncidenteDto) {
				
				objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidenteDto.getId()) ;
				
				MinutaSource minutaSource = new MinutaSource() {
						public String getDescricaoTipoDeColegiado()	throws IOException,	MontadorMinutaServiceException {
							return TipoDeColegiado.getTipoDeColegiadoPelaChave(getColegiado()).getDescricao().toUpperCase();
						}

						public boolean isReuPreso() throws IOException,	MontadorMinutaServiceException {
							return ObjetoIncidenteUtil.getProcesso(objetoIncidente).getReuPreso().booleanValue();
						}

						@Override
						public List<String> getTextosDecisoes() throws IOException, MontadorMinutaServiceException {
							// TODO Auto-generated method stub
							return null;
						}
					};
					
					CabecalhoObjetoIncidente cabecalho = cabecalhoBuilderService.recuperarCabecalho(objetoIncidente.getId());
					SpecCabecalho<Long> specCabecalho = cabecalhoBuilderService.getSpecCabecalho(cabecalho);

					Pair<MinutaSource, SpecCabecalho<Long>> pair = new Pair<MinutaSource, SpecCabecalho<Long>>(minutaSource, specCabecalho);
					
					sources.add(pair);
					
					SpecMarcaDagua specMarcaDagua = new SpecMarcaDagua(null);
					
					specMarcaDaguaMap.put(null, specMarcaDagua);
			}
			
			SpecDadosMinuta specDados = new SpecDadosMinuta();
			specDados.setColegiado(TipoDeColegiado.getTipoDeColegiadoPelaChave(getColegiado()).getDescricao().toUpperCase());
			if (sources.isEmpty()) {
				getFacesMessages().add("Não há textos para os tipos de selecionados.");
				return;
			}

			File outputFile = File.createTempFile("relatorioMinutaTemp", ".pdf");

			DocumentTarget target = new FileDocumentTarget(outputFile);
			
			montadorMinutaService.createMinutasUnicoPDF(sources, specMarcaDaguaMap, target, specDados);
			
			ReportUtils.report(new ByteArrayInputStream(ReportUtils.getBytesFromFile(outputFile)));
		} catch(Exception e) {
			throw new NestedRuntimeException(e);
		}
		
    	if (!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
    	
    }
    
	/**
	 * Retorna a lista colegiados.
	 * 
	 * @return a lista de colegiados
	 */
	public List<SelectItem> getColegiados() {
		List<SelectItem> itens =  new ArrayList<SelectItem>();
		itens.add(new SelectItem(TipoDeColegiado.PRIMEIRA_TURMA.getChave(), TipoDeColegiado.PRIMEIRA_TURMA.getDescricao()));
		itens.add(new SelectItem(TipoDeColegiado.SEGUNDA_TURMA.getChave(), TipoDeColegiado.SEGUNDA_TURMA.getDescricao()));
		itens.add(new SelectItem(TipoDeColegiado.PLENO.getChave(), TipoDeColegiado.PLENO.getDescricao()));
		return itens;
	}    

	public String getColegiado() {
		return colegiado;
	}

	public void setColegiado(String colegiado) {
		this.colegiado = colegiado;
	}

}
