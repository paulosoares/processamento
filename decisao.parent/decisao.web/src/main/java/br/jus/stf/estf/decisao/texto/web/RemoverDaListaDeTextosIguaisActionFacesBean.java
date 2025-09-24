package br.jus.stf.estf.decisao.texto.web;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckTextosIguais;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * Retira os textos da recpectiva lista de textos iguais. Ou seja, torna o texto semelhantes (não iguais)
 * àqueles que pertencem a lista de textos iguais.
 * 
 * @author Rodrigo Barreiros
 * @since 22.07.2010
 */
@Action(id="removerDaListaDeTextosIguaisActionFacesBean", name="Remover da lista de textos iguais", view="/acoes/texto/removerDaListaDeTextosIguais.xhtml", height=140, width=500)
@Restrict({ActionIdentification.TORNAR_TEXTO_SEMELHANTE})
@RequiresResources(Mode.Many)
@CheckTextosIguais
@CheckMinisterId
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO})
public class RemoverDaListaDeTextosIguaisActionFacesBean extends ActionSupport<TextoDto> implements ActionInterface<TextoDto> {

	
	@Autowired
	private TextoService textoService;
	
	/**
	 * Executa a ação aplicando as regras de remoção da lista, definidas
	 * na service de textos.
	 * 
	 * @see TextoService#tornarTextoSemelhante(br.gov.stf.estf.entidade.documento.Texto)
	 */
	public void execute()  {
        execute(new ActionCallback<TextoDto>() {
            public void doInAction(TextoDto texto) throws Exception {
            	if(TipoTexto.REVISAO_DE_APARTES.equals(texto.getTipoTexto())) {
					addError("O texto ["
							+ texto.toString()
							+ "] não pode ser retirado da lista de textos iguais pois é do tipo "
							+ TipoTexto.REVISAO_DE_APARTES.getDescricao() + ".");
            	} else {
            		textoService.tornarTextoSemelhante(textoService.recuperarPorId(texto.getId()));
            	}
            }
        });
        
        setRefresh(true);
	}

}
