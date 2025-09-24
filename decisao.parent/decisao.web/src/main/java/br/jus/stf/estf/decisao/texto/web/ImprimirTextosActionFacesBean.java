package br.jus.stf.estf.decisao.texto.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.converter.DocumentTarget;
import br.gov.stf.estf.converter.target.FileDocumentTarget;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
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
 * Gera um relatório PDF contendo todos os textos selecionados.
 * 
 * @author Rodrigo Barreiros
 * @see 20.07.2010
 */
@Action(id="imprimirTextosActionFacesBean", name="Imprimir Textos", report = true)
@Restrict({ActionIdentification.IMPRIMIR_TEXTO})
@RequiresResources(Mode.Many)
@CheckRestrictions
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO})
public class ImprimirTextosActionFacesBean extends ReportAction<TextoDto> {
	
	@Autowired
	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;
	
	@Autowired
	private DadosMontagemTextoBuilder dadosMontagemTextoBuilder;
    
	@Autowired
	private TextoService textoService;
	
	@Autowired
	private DocumentoTextoService documentoTextoService;
	
	/**
	 * @see br.jus.stf.estf.decisao.support.action.support.ReportAction#doReport(java.util.Set)
	 */
	@Override
	protected InputStream doReport(Set<TextoDto> resources) throws IOException {
		try {
			if (resources.size() == 1 && FaseTexto.fasesComTextoAssinado.contains(resources.iterator().next().getFase())) {
				Texto texto = textoService.recuperarPorId(resources.iterator().next().getId());
				DocumentoTexto documentoTexto = documentoTextoService.recuperarNaoCancelado(texto, null);
				
				if (documentoTexto == null) {
					List<DadosMontagemTexto<Long>> dadosMontagemTexto = new ArrayList<DadosMontagemTexto<Long>>();
					
					DadosMontagemTexto<Long> dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto);
					dadosMontagemTexto.add(dadosMontagem);
					
					File outputFile = File.createTempFile("relatorio", ".pdf");
					
					DocumentTarget target = new FileDocumentTarget(outputFile);
					openOfficeMontadorTextoService.criarTextosPDFUnico(dadosMontagemTexto, target, true);
					
					return new FileInputStream(outputFile);
				}
				
				return new ByteArrayInputStream(documentoTexto.getDocumentoEletronico().getArquivo());
			} else {
				List<DadosMontagemTexto<Long>> dadosMontagemTexto = new ArrayList<DadosMontagemTexto<Long>>();
				
				for (final TextoDto dto : resources) {
					Texto texto = textoService.recuperarPorId(dto.getId());
					DadosMontagemTexto<Long> dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto);
					dadosMontagemTexto.add(dadosMontagem);
					
			
						try {
							objetoIncidenteService.registrarLogSistema(texto.getObjetoIncidente(), "CONSULTA_TEXTO", "Imprimir Texto: "+texto.getId()+" - "+texto.getTipoTexto().getDescricao(),texto.getId(),"STF.TEXTOS");
						} catch (DaoException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			
					
				}
				
				File outputFile = File.createTempFile("relatorio", ".pdf");
				
				DocumentTarget target = new FileDocumentTarget(outputFile);
				openOfficeMontadorTextoService.criarTextosPDFUnico(dadosMontagemTexto, target, true);
				
				return new FileInputStream(outputFile);
			}
		} catch (MontadorTextoServiceException e) {
			throw new NestedRuntimeException(e);
		} catch (ServiceException e) {
			throw new NestedRuntimeException(e);
		}
	}

}
