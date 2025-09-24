package br.jus.stf.estf.decisao.objetoincidente.web.support;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.montadortexto.DadosMontagemMinuta;
import br.jus.stf.estf.montadortexto.SpecCabecalho;
import br.jus.stf.estf.montadortexto.SpecDadosMinuta;

public class DadosMontagemMinutaBuilder {

	private final CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;

	@Autowired
	private DadosMontagemMinutaBuilder(CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService) {
		this.cabecalhoObjetoIncidenteService = cabecalhoObjetoIncidenteService;
	}

	public DadosMontagemMinuta<Long> dadosMontagemMinuta(ObjetoIncidente<?> oi, String colegiado)
			throws ServiceException {
		return montaDadosMontagemMinuta(oi, colegiado);
	}

	public DadosMontagemMinuta<Long> montaDadosMontagemMinuta(ObjetoIncidente<?> oi, String colegiado)
			throws ServiceException {
		DadosMontagemMinuta<Long> dadosMontagem = new DadosMontagemMinuta<Long>();
		insereCabecalho(oi, dadosMontagem);
		dadosMontagem.setSpecDados(montaDadosDeInformacaoDaMinuta(oi, colegiado));
		return dadosMontagem;
	}

	private void insereCabecalho(ObjetoIncidente<?> oi, DadosMontagemMinuta<Long> dadosMontagem)
			throws ServiceException {
		CabecalhoObjetoIncidente cabecalho = cabecalhoObjetoIncidenteService.recuperarCabecalho(oi.getId());
		SpecCabecalho<Long> specCabecalho = cabecalhoObjetoIncidenteService.getSpecCabecalho(cabecalho);
		dadosMontagem.setSpecCabecalho(specCabecalho);
	}

	private SpecDadosMinuta montaDadosDeInformacaoDaMinuta(ObjetoIncidente<?> oi, String colegiado) {
		SpecDadosMinuta specDados = new SpecDadosMinuta();
		if (oi != null && ObjetoIncidenteUtil.getProcesso(oi) != null
				&& ObjetoIncidenteUtil.getProcesso(oi).getReuPreso() != null)
			specDados.setReuPreso(ObjetoIncidenteUtil.getProcesso(oi).getReuPreso().booleanValue());
		else
			specDados.setReuPreso(false);

		specDados.setColegiado(colegiado.toUpperCase());
		return specDados;
	}

}
