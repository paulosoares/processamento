package br.gov.stf.estf.expedicao.model.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.entidade.RemessaVolume;
import br.gov.stf.estf.expedicao.visao.comparator.ListaRemessaNumeroLista;
import br.gov.stf.estf.expedicao.visao.comparator.RemessaListaRemessaNumeroLista;

/**
 * 
 * @author Filipe.Gomes
 *
 */
public class RelatorioRemessaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final static ListaRemessaNumeroLista listaRemessaNumeroLista = new ListaRemessaNumeroLista();
	private final static RemessaListaRemessaNumeroLista remessaListaRemessaNumeroLista = new RemessaListaRemessaNumeroLista(listaRemessaNumeroLista);
	
	private String listasAssociadas;
    private RemessaVolume volume;
    private Remessa remessa;
    
	public Remessa getRemessa() {
		return remessa;
	}
	
	public static List<RelatorioRemessaDTO> criar(ListaRemessa listaRemessa) {
		List<RelatorioRemessaDTO> resultado = criar(listaRemessa.getRemessas());
		return resultado;
	}

	public static List<RelatorioRemessaDTO> criar(List<Remessa> remessas) {
		List<RelatorioRemessaDTO> resultado = new ArrayList<RelatorioRemessaDTO>();
		Collections.sort(remessas, remessaListaRemessaNumeroLista);
		for (Remessa remessa : remessas) {
			resultado.addAll(criar(remessa));
		}
		return resultado;
	}

	public static List<RelatorioRemessaDTO> criar(Remessa remessa) {
		List<RelatorioRemessaDTO> resultado = new ArrayList<RelatorioRemessaDTO>();
		for (RemessaVolume remessaVolume : remessa.getVolumes()) {
			RelatorioRemessaDTO remessaDto = new RelatorioRemessaDTO();					
			remessaDto.setRemessa(remessa);
			remessaDto.setVolume(remessaVolume);					
			StringBuilder listasAssociadas = new StringBuilder();
			for (ListaRemessa r2 : remessa.getListasEnviadas()) {
				listasAssociadas.append(r2.getNumeroListaRemessaAnoFormato());
				listasAssociadas.append("; ");
			}
			remessaDto.setListasAssociadas((listasAssociadas.toString().length() > 0 ? (listasAssociadas.toString().substring(0, listasAssociadas.toString().length() - 2)) : listasAssociadas.toString()));	
			resultado.add(remessaDto);
		}		
		return resultado;
	}

	public void setRemessa(Remessa remessa) {
		this.remessa = remessa;
	}

	public RemessaVolume getVolume() {
		return volume;
	}
	
	public void setVolume(RemessaVolume volume) {
		this.volume = volume;
	}
	
	public String getListasAssociadas() {
		return listasAssociadas;
	}

	public void setListasAssociadas(String listasAssociadas) {
		this.listasAssociadas = listasAssociadas;
	}
	
	public String getVolumeFormatado() {
		return volume.getNumeroVolume() + "/" + remessa.getVolumes().size(); 
	}	
}