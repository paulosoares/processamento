package br.jus.stf.estf.decisao.texto.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.converter.DocumentTarget;
import br.gov.stf.estf.converter.target.FileDocumentTarget;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
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
 * Gera um relatório PDF contendo todos os textos iguais aos textos selecionados. Ou seja,
 * para cada texto selecionado, verifica a lista de textos iguais e adiciona esses textos
 * à lista textos para geração do relatório.
 * 
 * @author Rodrigo Barreiros
 * @see 21.07.2010
 */
@Action(id="imprimirTextosIguaisActionFacesBean", name="Imprimir Textos Iguais", report = true)
@Restrict({ActionIdentification.IMPRIMIR_TEXTO})
@RequiresResources(Mode.Many)
@CheckRestrictions
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO})
public class ImprimirTextosIguaisActionFacesBean extends ReportAction<TextoDto> {
	
	@Autowired
	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;
	
	@Autowired
	private DadosMontagemTextoBuilder dadosMontagemTextoBuilder;
    
	@Autowired
	private TextoService textoService;
	
	/**
	 * @see br.jus.stf.estf.decisao.support.action.support.ReportAction#doReport(java.util.Set)
	 */
	@Override
	protected InputStream doReport(Set<TextoDto> resources) throws IOException {
		try {
			Collection<Texto> textosComTextosIguais = getListaDeTextosIguais(resources);
			
			List<DadosMontagemTexto<Long>> dadosParaMontagem = new ArrayList<DadosMontagemTexto<Long>>();

			for (final Texto texto : textosComTextosIguais) {
				DadosMontagemTexto<Long> dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto);
				dadosParaMontagem.add(dadosMontagem);
			}
			
			File outputFile = File.createTempFile("relatorio", ".pdf");
			DocumentTarget target = new FileDocumentTarget(outputFile);
			
			openOfficeMontadorTextoService.criarTextosPDFUnico(dadosParaMontagem, target, true);
			
			return new FileInputStream(outputFile);
		} catch (MontadorTextoServiceException e) {
			throw new NestedRuntimeException(e);
		} catch (ServiceException e) {
			throw new NestedRuntimeException(e);
		} catch (IOException e) {
			throw new NestedRuntimeException(e);
		}
	}
	
	/**
	 * Recupera a lista de textos iguais aos textos da lista informada.
	 * 
	 * @param textos a lista de textos selecionada
	 * @return a lista de textos iguais
	 */
	private Collection<Texto> getListaDeTextosIguais(Set<TextoDto> textos) {
		try {
			Collection<Texto> listaDeTextosComTextosIguais = new ArrayList<Texto>();
			for (TextoDto dto : textos) {
				Texto texto = textoService.recuperarPorId(dto.getId());
				listaDeTextosComTextosIguais.addAll(textoService.pesquisarTextosIguais(texto, true));

				listaDeTextosComTextosIguais.add(texto);
			}
			return listaDeTextosComTextosIguais;
		} catch (ServiceException e) {
			throw new NestedRuntimeException(e);
		}
	}
	
}