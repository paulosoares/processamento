package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.IncidenteJulgamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * @author Eucilon Silva
 * @since 23.04.2018
 */
@Action(id = "excluirIncidenteActionFacesBean", name = "Excluir Incidente de Julgamento", view = "/acoes/objetoincidente/excluirIncidente.xhtml", height = 100, width = 730)
@Restrict({ActionIdentification.EXCLUIR_INCIDENTE })
@RequiresResources(Mode.Many)


public class ExcluirIncidenteActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {

	private Set<ObjetoIncidenteDto> processosInvalidos = new HashSet<ObjetoIncidenteDto>();

	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;

	private ObjetoIncidente<?> objetoIncidente;
	
	private String nomeRecurso; 
	
	private String nomeUsuario;
	
	private String msgRestricao = "";
	
	@Autowired
	private IncidenteJulgamentoService incidenteJulgamentoService;
	
	@Override
	public void load() {
		if ( existeProcessoSelecionado() ){
			try {				
				if (getResources().size() <= 1) {
					objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(((ObjetoIncidenteDto) getResources().iterator().next()).getId()); 
					setNomeRecurso(objetoIncidente.getIdentificacaoCompleta());
					if(objetoIncidente.getTipoObjetoIncidente().equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO)) {
						if((objetoIncidente.getUsuarioCriacao() == null && objetoIncidente.getRelatorIncidenteId().equals(getMinistro().getId())) || (objetoIncidente.getUsuarioCriacao() != null && objetoIncidente.getUsuarioCriacao().getId().toString().equalsIgnoreCase(getUsuario().getId().toString())
								|| objetoIncidente.getUsuarioCriacao() != null && objetoIncidente.getUsuarioCriacao().getSetor().getId().equals(getUsuario().getSetor().getId())
								|| objetoIncidente.getUsuarioCriacao().getSetor().getId() == getSetorMinistro().getId() )
								) {
							if(objetoIncidente.getTextos().isEmpty()) {
									getDefinition().setFacet("confirmar");
							}else{
								addError (" Incidente possui textos. Mova o texto para outro incidente e tente excluir novamente.");
								sendToErrors();
								} 											
						}else{
							addError (" O incidente não pode ser excluído. Favor verificar com a seção que realizou a criação/autuação. ");
							if (objetoIncidente.getUsuarioCriacao() == null && objetoIncidente.getRelatorIncidenteId() !=null) {
								addError (" Esse incidente foi criado por outro setor para o ministro: "+ objetoIncidente.getRelatorIncidenteId());
							}else {
								addError (" Esse incidente foi criado por: "+ objetoIncidente.getUsuarioCriacao().getSetor().getNome());
							}
							sendToErrors();
						}						
					}else {
						addError (" Esse tipo de incidente não pode ser excluído. Favor verificar com a seção que realizou a criação/autuação. ");
						if (objetoIncidente.getUsuarioCriacao() != null && objetoIncidente.getUsuarioCriacao().getSetor() != null) {
							addError (" Incidente: "+objetoIncidente.getIdentificacaoCompleta() );
							addError (" Criação: " + objetoIncidente.getUsuarioCriacao().getSetor().getNome());
						}
						sendToErrors();
					}
				}
					addWarning(" Incidente: "+ objetoIncidente.getIdentificacaoCompleta() );
					if (objetoIncidente.getUsuarioCriacao().getSetor() != null)	{
						addWarning(" Setor: "+ objetoIncidente.getUsuarioCriacao().getSetor().getNome());
					}
					if (objetoIncidente.getUsuarioCriacao() != null)	{
						addWarning(" Criação: "+ objetoIncidente.getUsuarioCriacao().getNome());
					}
						addWarning(" Exclusão: "+ getUsuario().getNome());

			} catch (Exception e) {
				addError(e.getMessage());
				logger.error(e.getMessage(), e);
			}
		}
	}

	public void excluirIncidente() throws Exception {
		if (existeProcessoSelecionado()) {
			try {
				objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(((ObjetoIncidenteDto) getResources().iterator().next()).getId());
				incidenteJulgamentoService.excluirIncidenteJulgamento(objetoIncidente.getId());
				
			} catch (Exception e) {
				try {
					msgRestricao = e.toString();
					throw new DaoException(e);
				} catch (DaoException e1) {
					e1.printStackTrace();
				}
			}
			cleanMessages();
			if(msgRestricao.contains("POSSUI DEPENDÊNCIA") || msgRestricao.contains("SQLException")){
				addError (" Incidente possui incidentes filhos.");
				addError (msgRestricao);
				sendToErrors();
			}else {
				addInformation(" Registro excluído com sucesso.");
				sendToInformations();
				getDefinition().setFacet("confirmado");
			}
		} 
	}

	private boolean existeProcessoSelecionado() {
		int tamanhoDosRecursos = getResources().size();
		int tamanhoDeExcluidos = processosInvalidos.size();
		return tamanhoDosRecursos > tamanhoDeExcluidos;
	}

	public String getNomeRecurso() {
		return nomeRecurso;
	}

	public void setNomeRecurso(String nomeRecurso) {
		this.nomeRecurso = nomeRecurso;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
	public String getMsgRestricao() {
		return msgRestricao;
	}

}
