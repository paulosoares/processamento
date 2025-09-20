package br.gov.stf.estf.processostf.model.service.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoIntegracaoService;
import br.gov.stf.framework.model.service.ServiceException;

public class VistaAGUHandler extends AndamentoProcessoHandler {
	
	@Autowired
	ProcessoIntegracaoService processoIntegracaoService;
	
	@Autowired
	DeslocaProcessoService deslocaProcessoService;
	
	@Autowired
	ObjetoIncidenteService objetoIncidenteService;

		@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processo, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {
			
		//Somente desloca se o processo for eletrônico
		if (!processo.isEletronico())
				return;
		/*
		try {
			DeslocaProcesso deslocaProcesso = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo);
			if(deslocaProcesso!=null && !deslocaProcesso.getCodigoOrgaoDestino().equals(Setor.CODIGO_SETOR_AGU)) {
				ArrayList<Long> processos = new ArrayList<Long>();
				processos.add(processo.getId());
	
				Guia guia = new Guia();
				Guia.GuiaId guiaId = new Guia.GuiaId();
				guia.setId(guiaId);
	
				guia.setCodigoOrgaoOrigem(deslocaProcesso.getGuia().getCodigoOrgaoDestino());
				guia.setCodigoOrgaoDestino(Setor.CODIGO_SETOR_AGU);
				
				guia.setTipoOrgaoOrigem(deslocaProcesso.getGuia().getTipoOrgaoDestino());
				guia.setTipoOrgaoDestino(DeslocaProcesso.TIPO_ORGAO_EXTERNO);
	
				String numAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, processos, true);
	
				if (numAnoGuia == null) {
					throw new ServiceException("Erro ao efetuar o deslocamento");
				}
	
				deslocaProcesso = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo);
				deslocaProcesso.setAndamentoProcesso(andamentoProcesso);
	
				deslocaProcessoService.alterar(deslocaProcesso);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServiceException("Erro ao efetuar o deslocamento: "
					+ e.getMessage());
		}
		*/
	}
	
}