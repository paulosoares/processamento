package br.jus.stf.estf.decisao.texto.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;

import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.jus.stf.estf.decisao.texto.support.TextoNaoPodeSerRestritoException;

/**
 * @author Rodrigo Barreiros
 * @see 21.05.2010
 */
@Action(id = "controlarAcessoTextoActionFacesBean", name = "Controlar Acesso ao Texto", view = "/acoes/texto/restringir.xhtml", height = 150, width = 500)
@Restrict({ActionIdentification.RESTRINGIR_ACESSO_AO_TEXTO})
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO})
public class ControlarAcessoTextoActionFacesBean extends ActionSupport<TextoDto> implements ActionInterface<TextoDto> {

	@Autowired
	private TextoService textoService;
	private String idTipoRestricao;

	/**
	 * Executa as regras para restrição de acesso a textos.
	 */
	public void execute() {
		execute(new ActionCallback<TextoDto>() {
			public void doInAction(TextoDto texto) throws Exception {
				doExecute(texto);
			}

		});
		setRefresh(true);
	}

	private void doExecute(TextoDto textoDto) {
		try {
			Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			textoService.alterarAcessoDoTexto(textoDto, getTipoRestricao(), principal.getUsuario().getId());
		} catch (TextoNaoPodeSerRestritoException e) {
			addError(montaMensagem(textoDto, e));
		}
	}

	private String montaMensagem(TextoDto texto, TextoNaoPodeSerRestritoException e) {
		return String.format("%s: %s", texto.toString(), e.getMessage());
	}

	private TipoRestricao getTipoRestricao() {
		return TipoRestricao.valueOf(getIdTipoRestricao());
	}

	public void setIdTipoRestricao(String tipoRestricao) {
		this.idTipoRestricao = tipoRestricao;
	}

	public String getIdTipoRestricao() {
		return idTipoRestricao;
	}

	public TipoRestricao[] getTiposRestricao() {
		return TipoRestricao.values();
	}

}
