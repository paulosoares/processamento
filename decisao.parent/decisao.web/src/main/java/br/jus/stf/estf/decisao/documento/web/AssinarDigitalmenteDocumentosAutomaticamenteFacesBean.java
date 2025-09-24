package br.jus.stf.estf.decisao.documento.web;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.documento.service.AssinaturaDocumentoService;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.web.PrincipalFacesBean;

/**
 * Bean JSF (Seam Component) para assinatura de documentos
 * (textos ou comunicações) automaticamente (em apenas um passo).
 * 
 * @author Tomas.Godoi
 * 
 */
@Name("assinarDigitalmenteDocumentosAutomaticamenteFacesBean")
@Scope(ScopeType.CONVERSATION)
public class AssinarDigitalmenteDocumentosAutomaticamenteFacesBean extends AbstractAssinarDocumentosBean {

	@In("#{assinaturaDigitalDocumentoService}")
	private AssinaturaDocumentoService assinaturaDocumentoService;

	@In("#{principalFacesBean}")
	private PrincipalFacesBean principalFacesBean;
	
	@Override
	protected AssinaturaDocumentoService getAssinaturaDocumentoService() {
		return assinaturaDocumentoService;
	}

	public void selecionarTextosParaAssinar() {
		limpar();
		try {
			checarPermissao(permissaoAssinarDigitalmente);
			Ministro ministro = usuarioLogadoService.getMinistro();
			if (ministro != null) {
				Pesquisa pesquisa = getAssinaturaDocumentoService().buildPesquisaTextosParaAssinar(ministro);
				pesquisa.setMaxResults(0); // Setando o valor que já estava na Minhas Pesquisas
				principalFacesBean.selecionarTextosParaAssinar(pesquisa);
			} else {
				addError("Usuário não está associado a um gabinete.");
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e);
		}
	}

	@Override
	protected void limpar() {
		principalFacesBean.limpar();
		super.limpar();
	}

}
