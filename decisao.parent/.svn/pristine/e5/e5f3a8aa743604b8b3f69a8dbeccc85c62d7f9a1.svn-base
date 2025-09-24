package br.jus.stf.estf.decisao.texto.web;

import java.util.LinkedList;
import java.util.List;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.gov.stf.estf.documento.model.service.ControleVotoService;

/**
 * @author Eucilon Silva
 * @see 05.05.2018
 */
@Action(id="moverTextosActionFacesBean", name="Mover Texto", view="/acoes/texto/moverTextos.xhtml", height = 100, width = 800)
@Restrict({ActionIdentification.MOVER_TEXTOS})
@RequiresResources(Mode.One)
@CheckMinisterId
@CheckRestrictions
public class MoverTextosActionFacesBean extends ActionSupport<TextoDto> implements ActionInterface<TextoDto>  {

	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Qualifier("textoServiceLocal") 
	@Autowired 
	private TextoService textoService;
	
	private Texto texto;
	
	private Long idIncidente;
	
	private Long codigoRecurso = 0L;
	
	private ObjetoIncidente<?> objetoIncidente;
	
	private TipoIncidenteJulgamento tipoJulgamento = TipoIncidenteJulgamento.MERITO;

	@Autowired 
	private  ControleVotoService controleVotoService;

    /**
     * Executa as regras para mover o texto.
     */
	public void execute() throws ServiceException {
    	try {
	    	if(idIncidente != -1L) {
	    		ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId( idIncidente);
	    		if(objetoIncidente.getTipoObjetoIncidente() == TipoObjetoIncidente.RECURSO) {
	    				RecursoProcesso recursoProcesso = (RecursoProcesso) objetoIncidenteService.recuperarObjetoIncidentePorId( idIncidente);
	    				codigoRecurso = recursoProcesso.getCodigoRecurso();
	    				tipoJulgamento = TipoIncidenteJulgamento.MERITO;
	    		}else {
	    			if(objetoIncidente.getTipoObjetoIncidente() == TipoObjetoIncidente.INCIDENTE_JULGAMENTO) {
	    				IncidenteJulgamento incidenteJulgamento = (IncidenteJulgamento) objetoIncidenteService.recuperarObjetoIncidentePorId( idIncidente);
	    				codigoRecurso = incidenteJulgamento.getCodigoRecurso();
	    				tipoJulgamento = incidenteJulgamento.getTipoJulgamento();
	    			}
	    		}
	    				texto = textoService.recuperarTextoPorId(getResources().iterator().next().getId());
	    				ControleVoto cv = controleVotoService.recuperar(texto.getObjetoIncidente(), texto.getTipoTexto(), texto.getMinistro());
	    				texto.setObjetoIncidente(objetoIncidente);
	    				texto.setCodigoRecurso(codigoRecurso);
	    				texto.setTipoJulgamento(tipoJulgamento.getSigla());
	    				if(cv != null) {
	    					cv.setObjetoIncidente(objetoIncidente);
	    					cv.setTipoJulgamento(tipoJulgamento.getSigla());
	    					cv.setCodigoRecurso(codigoRecurso);
	    					addInformation(" O controle de votos foi movido com sucesso para o novo incidente.");
	    				}
	    				
	    		    	addInformation(" O texto foi movido com sucesso para o novo incidente.");
	    		    	sendToConfirmation();
	    		    	getDefinition().setFacet("confirmado");		
	    		    	setRefresh(true);
	    		    	return;
	    		} else {
	    			addInformation(" Por favor, informe um incidente.");
					sendToInformations();
	    			return;
	    		}

    	} catch(Exception e) {
			sendToErrors();
			addError(" Não foi possível mover o texto. Favor entrar em contato com o ServiceDesk");
			addError(" Ocorreu o seguinte erro: "+ e.toString());
			getDefinition().setFacet("warnings");	
		}

    }
    
    public void voltar() {
    	getDefinition().setFacet("principal");
    }
    
    public List<SelectItem> getIncidentesDisponiveis() throws ServiceException {
		List<SelectItem> itens = new LinkedList<SelectItem>();
		objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(getResources().iterator().next().getIdObjetoIncidente());
		List<ObjetoIncidente<?>> cadeia = objetoIncidente.getPrincipal().getCadeia();
		if(cadeia.size() > 2) {
			itens.add(new SelectItem(-1, "Selecione um incidente..."));
		}
		if (objetoIncidente.getId() != objetoIncidente.getPrincipal().getId()){
			itens.add(new SelectItem(objetoIncidente.getPrincipal().getId(), "(" + objetoIncidente.getPrincipal().getIdentificacao() +" Mérito ) "+objetoIncidente.getPrincipal().getIdentificacaoCompleta()));
		}
		for (ObjetoIncidente<?> lista : cadeia) {
			if (lista.getId() != objetoIncidente.getId() && (
					   lista.getTipoObjetoIncidente().equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO)
					|| lista.getTipoObjetoIncidente().equals(TipoObjetoIncidente.RECURSO)
					|| lista.getTipoObjetoIncidente().equals(TipoObjetoIncidente.PROCESSO))) {
				itens.add(new SelectItem(lista.getId(), "(" + lista.getIdentificacao() +") "+ lista.getIdentificacaoCompleta()));
			}
		}
		return itens;	
	}
    
	public Long getIdIncidente() {
		return idIncidente;
	}

	public void setIdIncidente(Long idIncidente) {
		this.idIncidente = idIncidente;
	}
	
	public void load() {
		try {
			texto = textoService.recuperarTextoPorId(getResources().iterator().next().getId());
			objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(getResources().iterator().next().getIdObjetoIncidente());
			if(objetoIncidente.getPrincipal().getCadeia().size() == 1L){
				addError (" Não há incidentes para mover o texto.");
				sendToErrors();
			}
			if(texto.getUsuarioInclusao() != null && texto.getUsuarioInclusao().getId() != getUsuario().getId() && !texto.getTipoRestricao().equals(TipoRestricao.P)) {
				addError (" Texto possui restrições de acesso. Não pode ser movido.");
				sendToErrors();
			}
			//if(texto.getUsuarioInclusao() != null && !texto.getUsuarioInclusao().equals(getUsuario())) {
			//	addError(" O texto somente pode ser movido por quem o criou. Usuário de criação do texto: " + texto.getUsuarioInclusao().getNome());
			//	sendToErrors();
			//}
			if(texto.getCodigoMinistro() != null && texto.getCodigoMinistro().longValue()!= getMinistro().getId().longValue()) {
				addError (" Não é permitido excluir texto de outro setor. O texto pertence ao " + texto.getMinistro().getNome());
				sendToErrors();
			}
			//if(texto.getTipoFaseTextoDocumento().equals(FaseTexto.PUBLICADO)){
			//	addError(" Não é permitido mover um texto já publicado.");
			//	sendToErrors();
			//}
					
			
		} catch (Exception e) {
			addError( e.getMessage() );
			logger.error( e.getMessage(), e );
		}
		if ( hasErrors() ) {
			sendToErrors();
		}
	}
}
