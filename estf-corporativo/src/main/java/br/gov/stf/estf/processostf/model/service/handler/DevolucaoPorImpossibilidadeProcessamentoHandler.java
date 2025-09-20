package br.gov.stf.estf.processostf.model.service.handler;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.estf.processostf.model.service.HistoricoProcessoOrigemService;
import br.gov.stf.framework.model.service.ServiceException;

public class DevolucaoPorImpossibilidadeProcessamentoHandler extends AndamentoProcessoHandler {

	@Autowired
	HistoricoProcessoOrigemService historicoProcessoOrigemService;
	
	@Autowired
	GuiaService guiaService;
	
	@Autowired
	SetorService setorService;
	
	@Autowired
	DeslocaProcessoService deslocaProcessoService;
	
	@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {
		if (processoAndamento.isEletronico()) {
			
			List<HistoricoProcessoOrigem> lista = historicoProcessoOrigemService.recuperarPorObjetoIncidente(processoAndamento.getId());
			HistoricoProcessoOrigem historicoProcessoOrigem = lista.get(0);
			
			Guia guia = criarGuia(setor, historicoProcessoOrigem);
			
			setor.setNumeroProximaGuia(setor.getNumeroProximaGuia() + 1);
			setorService.salvar(setor);
			
			criarDeslocamento(guia, processoAndamento);
		}
	}
	
	private void criarDeslocamento(Guia guia, Processo processo) throws ServiceException {
		
		DeslocaProcesso deslocaProcesso = new DeslocaProcesso();
		deslocaProcesso.setGuia(guia);
		deslocaProcesso.setClasseProcesso(processo.getSiglaClasseProcessual());
		deslocaProcesso.setNumeroProcesso(processo.getNumeroProcessual());
		deslocaProcesso.setCodigoOrgaoDestino(guia.getCodigoOrgaoDestino());
		deslocaProcesso.setDataRecebimento(new Date());
		deslocaProcesso.setQuantidadeVolumes(processo.getQuantidadeVolumes().shortValue());
		deslocaProcesso.setQuantidadeApensos(processo.getQuantidadeApensos().shortValue());
		deslocaProcesso.setQuantidadeJuntadaLinha(processo.getQuantidadeJuntadasLinha());
		deslocaProcesso.setNumeroSequencia(1);
		deslocaProcesso.setTipoDeslocamento(DeslocaProcesso.TIPO_DESLOCAMENTO_EL);
		deslocaProcesso.setUltimoDeslocamento(true);
		
		DeslocaProcessoId id = new DeslocaProcessoId();
		id.setNumeroGuia(guia.getId().getNumeroGuia());
		id.setAnoGuia(guia.getId().getAnoGuia());
		id.setCodigoOrgaoOrigem(guia.getId().getCodigoOrgaoOrigem());
		id.setProcesso(processo);
		
		deslocaProcesso.setId(id);
		
		deslocaProcessoService.incluir(deslocaProcesso);
	}
	
	private Guia criarGuia(Setor setor, HistoricoProcessoOrigem historicoProcessoOrigem) throws ServiceException {
		
		Guia guia = new Guia();
		
		Guia.GuiaId id = new Guia.GuiaId();
		id.setAnoGuia(setor.getNumeroAnoGuia());
		id.setNumeroGuia(setor.getNumeroProximaGuia());
		id.setCodigoOrgaoOrigem(setor.getId());
		
		guia.setId(id);
		guia.setTipoOrgaoOrigem(Guia.CODIGO_ORIGEM_INTERNO);
		guia.setCodigoOrgaoDestino(historicoProcessoOrigem.getOrigem().getId());
		guia.setTipoOrgaoDestino(Guia.CODIGO_DESTINO_EXTERNO);
		guia.setQuantidadeInternaProcesso(1);
		guia.setDataRemessa(new Date());
		
		guiaService.incluir(guia);
		
		return guia;
	}
}