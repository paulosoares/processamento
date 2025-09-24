package br.jus.stf.estf.decisao.texto.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.util.DadosTextoDecisao;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.montadortexto.DadosMontagemRepercussaoGeral;
import br.jus.stf.estf.montadortexto.SpecDadosRepercussaoGeral;

@Component
public class DadosMontagemRepercussaoGeralBuilder {

	private final SessaoService sessaoService;

	@Autowired
	private DadosMontagemRepercussaoGeralBuilder(SessaoService sessaoService) {
		this.sessaoService = sessaoService;
	}

	public DadosMontagemRepercussaoGeral<Long> montaDadosMontagemRepercussaoGeral(ObjetoIncidente objetoIncidente, DadosTextoDecisao dadosTextoDecisao, Texto texto, boolean novaDecisao) throws ServiceException {
		DadosMontagemRepercussaoGeral<Long> dados = new DadosMontagemRepercussaoGeral<Long>();
		
		SpecDadosRepercussaoGeral specDados = new SpecDadosRepercussaoGeral();
		specDados.setDescricaoCurtaProcesso(objetoIncidente.getIdentificacao());
		specDados.setTextoDecisaoNova(novaDecisao);
		if(dadosTextoDecisao != null) {
			specDados.setNomeMinistro(dadosTextoDecisao.getNomeMinistroRelator());
			specDados.setRelator(dadosTextoDecisao.getRelator());
		}
		
		if(texto != null && texto.getControleVoto() != null) {
			specDados.setColegiado(texto.getControleVoto().getSessao().getDescricao());
			specDados.setDataSessao(texto.getControleVoto().getDataSessao());
		} else {
			specDados.setColegiado(TipoSessaoControleVoto.PLENARIO.getDescricao());
			Sessao sessao = sessaoService.recuperar(objetoIncidente.getId());
			if(sessao != null && sessao.getDataFim() != null)
				specDados.setDataSessao(sessao.getDataFim());
		}
			
		dados.setSpecDados(specDados);
		return dados;
	}
	
}
