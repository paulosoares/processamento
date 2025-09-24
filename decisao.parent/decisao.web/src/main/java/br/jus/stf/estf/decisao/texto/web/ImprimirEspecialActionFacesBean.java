/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.converter.target.ByteArrayDocumentTarget;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ReportAction;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
import br.jus.stf.estf.decisao.texto.support.DadosMontagemTextoBuilder;
import br.jus.stf.estf.montadortexto.DadosMontagemTexto;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;
import br.jus.stf.estf.montadortexto.OpenOfficeMontadorTextoService;

/**
 * Ação que permite que o usuário imprima a coleção de textos selecionados com um tamanho de fonte customizável.
 * @author Demetrius.Jube
 *
 */
@Action(id = "imprimirEspecialActionFacesBean", name = "Imprimir Especial", view = "/acoes/texto/imprimirEspecial.xhtml", height = 150, width = 300)
@Restrict({ ActionIdentification.IMPRIMIR_ESPECIAL })
@RequiresResources(Mode.Many)
@CheckRestrictions
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA,
		FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO })
public class ImprimirEspecialActionFacesBean extends ReportAction<TextoDto> {

	@Autowired
	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;

	@Autowired
	private DadosMontagemTextoBuilder dadosMontagemTextoBuilder;

	@Autowired
	private TextoService textoService;

	private Integer tamanhoDaFonte = 16;

	public Integer getTamanhoDaFonte() {
		return tamanhoDaFonte;
	}

	public void setTamanhoDaFonte(Integer tamanhoDaFonte) {
		this.tamanhoDaFonte = tamanhoDaFonte;
	}

	@Override
	protected InputStream doReport(Set<TextoDto> resources) throws IOException {
		try {
			List<DadosMontagemTexto<Long>> dadosMontagemTexto = new ArrayList<DadosMontagemTexto<Long>>();

			for (final TextoDto dto : resources) {
				Texto texto = textoService.recuperarPorId(dto.getId());
				DadosMontagemTexto<Long> dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto, true,
						null, "Cópia", false);
				dadosMontagemTexto.add(dadosMontagem);
			
					try {
						objetoIncidenteService.registrarLogSistema(texto.getObjetoIncidente(), "CONSULTA_TEXTO", "Imprimir Especial Texto: "+texto.getId()+" - "+texto.getTipoTexto().getDescricao(),texto.getId(),"STF.TEXTOS");
					} catch (DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			}

			// File outputFile = File.createTempFile("relatorio", ".pdf");

			ByteArrayDocumentTarget target = new ByteArrayDocumentTarget();
			openOfficeMontadorTextoService.criarTextosPDFUnicoFonteAlterada(dadosMontagemTexto, target, true,
					tamanhoDaFonte);
			// openOfficeMontadorTextoService.criarTextosPDFUnico(dadosMontagemTexto, target, true);
			return new ByteArrayInputStream(target.getByteArray());

			// return new FileInputStream(outputFile);

		} catch (MontadorTextoServiceException e) {
			throw new NestedRuntimeException(e);
		} catch (ServiceException e) {
			throw new NestedRuntimeException(e);
		}
	}

}
