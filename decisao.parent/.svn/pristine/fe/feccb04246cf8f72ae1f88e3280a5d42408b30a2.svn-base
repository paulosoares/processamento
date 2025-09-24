/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;
import java.util.Arrays;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso.TipoSituacaoPauta;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.InformacaoPautaProcessoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckDestaquePeloMinistro;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * @author Eucilon.Silva / Paulo.Estevao
 * @since 02.08.2011
 */
@Action(id="editarEspelhoActionFacesBean", name="Editar Espelho do Processo", view="/acoes/objetoincidente/editarEspelho.xhtml", height=400, width=700)
@Restrict({ActionIdentification.EDITAR_ESPELHO})
@CheckDestaquePeloMinistro
@RequiresResources(Mode.One)
public class EditarEspelhoActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {
	private String tema;
	private String tese;
	private String agu;
	private String pgr;
	private String votoRelator;
	private String votos;
	private String informacoes;
	private ObjetoIncidente<?> objetoIncidente;
	private InformacaoPautaProcesso informacaoPautaProcesso;
	private String[] classesAGU = {"ADI", "ADC", "ADPF","ADO"}; // Classes que apresentarão o Campo "Parecer da AGU"

	@Autowired
	private InformacaoPautaProcessoService informacaoPautaProcessoService;
	
	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;

	@Override
	public void load() {
		try {
			objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(((ObjetoIncidenteDto) getResources().iterator().next()).getId());
			Hibernate.initialize(objetoIncidente.getPrincipal());				
			informacaoPautaProcesso = informacaoPautaProcessoService.recuperar(objetoIncidente);
			if(informacaoPautaProcesso != null){
				setTema(informacaoPautaProcesso.getTemaEspelho());
				setTese(informacaoPautaProcesso.getTeseEspelho());
				setPgr(informacaoPautaProcesso.getParecerPGREspelho());
				setAgu(informacaoPautaProcesso.getParecerAGUEspelho());
				setVotoRelator(informacaoPautaProcesso.getVotoRelatorEspelho());
				setVotos(informacaoPautaProcesso.getVotosEspelho());
				setInformacoes(informacaoPautaProcesso.getInformacoesEspelho());
			}
		} catch (ServiceException e) {
			addError("Erro ao Recuperar Espelho. "+e.getMessage());
			logger.error("Erro ao Recuperar Espelho.", e);
		}
	}
	public void execute() {			
		try {
			boolean alterar = false;
			informacaoPautaProcesso = informacaoPautaProcessoService.recuperar(objetoIncidente);
			if (informacaoPautaProcesso == null){
				informacaoPautaProcesso = new InformacaoPautaProcesso();
				informacaoPautaProcesso.setObjetoIncidente(objetoIncidente);
				informacaoPautaProcesso.setMateriaRelevante(Boolean.FALSE);
				informacaoPautaProcesso.setRepercussaoGeral(Boolean.FALSE);
				informacaoPautaProcesso.setAguardaLiberacao(Boolean.FALSE);
				informacaoPautaProcesso.setSituacaoPauta(TipoSituacaoPauta.P);
			}		
			if ((getTema() != null
					&& getTema().trim().length() > 0
					&& !getTema().equals(informacaoPautaProcesso.getTemaEspelho()) 
					|| ( (getTema() == null || getTema().trim().length() == 0)
					      && informacaoPautaProcesso.getTemaEspelho() != null 
					      && informacaoPautaProcesso.getTemaEspelho().trim().length() > 0))) {
				alterar = true;
				informacaoPautaProcesso.setTemaEspelho(getTema());
			}
			if ((getTese() != null
					&& getTese().trim().length() > 0
					&& !getTese().equals(informacaoPautaProcesso.getTeseEspelho()) 
					|| ( (getTese() == null || getTese().trim().length() == 0)
					      && informacaoPautaProcesso.getTeseEspelho() != null 
					      && informacaoPautaProcesso.getTeseEspelho().trim().length() > 0))) {
				alterar = true;
				informacaoPautaProcesso.setTeseEspelho(getTese());
			}
			if ((getPgr() != null
					&& getPgr().trim().length() > 0
					&& !getPgr().equals(informacaoPautaProcesso.getParecerPGREspelho()) 
					|| ( (getPgr() == null || getPgr().trim().length() == 0)
					      && informacaoPautaProcesso.getParecerPGREspelho() != null 
					      && informacaoPautaProcesso.getParecerPGREspelho().trim().length() > 0))) {
				alterar = true;
				informacaoPautaProcesso.setParecerPGREspelho(getPgr());
			}
			if ((getAgu() != null
					&& getAgu().trim().length() > 0
					&& !getAgu().equals(informacaoPautaProcesso.getParecerAGUEspelho()) 
					|| ( (getAgu() == null || getAgu().trim().length() == 0)
					      && informacaoPautaProcesso.getParecerAGUEspelho() != null 
					      && informacaoPautaProcesso.getParecerAGUEspelho().trim().length() > 0))) {
				alterar = true;
				informacaoPautaProcesso.setParecerAGUEspelho(getAgu());
			}
			if ((getVotoRelator() != null
					&& getVotoRelator().trim().length() > 0
					&& !getVotoRelator().equals(informacaoPautaProcesso.getVotoRelatorEspelho()) 
					|| ( (getVotoRelator() == null || getVotoRelator().trim().length() == 0)
					      && informacaoPautaProcesso.getVotoRelatorEspelho() != null 
					      && informacaoPautaProcesso.getVotoRelatorEspelho().trim().length() > 0))) {
				alterar = true;
				informacaoPautaProcesso.setVotoRelatorEspelho(getVotoRelator());
			}
			if ((getVotos() != null
					&& getVotos().trim().length() > 0
					&& !getVotos().equals(informacaoPautaProcesso.getVotosEspelho()) 
					|| ( (getVotos() == null || getVotos().trim().length() == 0)
					      && informacaoPautaProcesso.getVotosEspelho() != null 
					      && informacaoPautaProcesso.getVotosEspelho().trim().length() > 0))) {
				alterar = true;
				informacaoPautaProcesso.setVotosEspelho(getVotos());
			}
			if ((getInformacoes() != null
					&& getInformacoes().trim().length() > 0
					&& !getInformacoes().equals(informacaoPautaProcesso.getInformacoesEspelho()) 
					|| ( (getInformacoes() == null || getInformacoes().trim().length() == 0)
					      && informacaoPautaProcesso.getInformacoesEspelho() != null 
					      && informacaoPautaProcesso.getInformacoesEspelho().trim().length() > 0))) {
				alterar = true;
				informacaoPautaProcesso.setInformacoesEspelho(getInformacoes());
			}
			if (alterar) {
				informacaoPautaProcessoService.salvar(informacaoPautaProcesso);
				setRefresh(true);
			}
		} catch (ServiceException e) {
			addError("Erro ao Gravar Espelho. "+e.getMessage());
			logger.error("Erro ao Gravar Espelho.", e);
		}
		if (hasErrors()){
			sendToErrors();
		}else{
			sendToConfirmation();
		}
	}

	/* Mostra no campo de espelhos do Processo o Parecer da AGU, caso exista */
	public boolean isMostrarParecerAGU() {
		if (Arrays.asList(classesAGU).contains(((Processo) objetoIncidente.getPrincipal()).getSiglaClasseProcessual())) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getErrorTitle() {
		return "Erro ao Alterar o Espelho do Processos";
	}
	public void voltar() {
    	getDefinition().setFacet("principal");
    }
	public void setTema(String tema){
		this.tema = tema;
	}
	public String getTema() {
		return tema;
	}
	public void setTese(String tese){
		this.tese = tese;
	}
	public String getTese() {
		return tese;
	}
	public void setPgr(String pgr){
		this.pgr = pgr;
	}
	public String getPgr() {
		return pgr;
	}
	public void setAgu(String agu){
		this.agu = agu;
	}
	public String getAgu() {
		return agu;
	}
	public void setVotoRelator(String votoRelator){
		this.votoRelator = votoRelator;
	}
	public String getVotoRelator() {
		return votoRelator;
	}
	public void setVotos(String votos){
		this.votos = votos;
	}
	public String getVotos() {
		return votos;
	}
	public void setInformacoes(String informacoes){
		this.informacoes = informacoes;
	}
	public String getInformacoes() {
		return informacoes;
	}
}