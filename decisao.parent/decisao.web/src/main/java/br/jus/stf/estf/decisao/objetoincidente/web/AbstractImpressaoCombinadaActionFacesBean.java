/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.converter.DocumentTarget;
import br.gov.stf.estf.converter.target.FileDocumentTarget;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.util.ReportUtils;
import br.jus.stf.estf.decisao.texto.support.DadosMontagemTextoBuilder;
import br.jus.stf.estf.montadortexto.DadosMontagemTexto;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;
import br.jus.stf.estf.montadortexto.OpenOfficeMontadorTextoService;
import br.jus.stf.estf.montadortexto.TextoSource;

/**
 * @author Paulo.Estevao
 * @since 09.08.2010
 */
public abstract class AbstractImpressaoCombinadaActionFacesBean<T> extends ActionSupport<ObjetoIncidenteDto> {

	private static final String PROCESSO_NAO_POSSUI_TEXTO = "O %s não possui %s para o %s";

	private static final String PROCESSO_NAO_POSSUI_TEXTOS = "O %s não possui %s e %s para o %s";
	
	private static final String PAGINA_SEM_MENSAGEM = "semMensagem";
	
	private List<String> listaMsgProcessosSemTexto;
	
	private List<DadosMontagemTexto<Long>> listaDadosMontagemTexto;
	
	@Autowired
	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;
	
	@Qualifier("textoService") 
	@Autowired 
	private TextoService textoService;
	
	@Qualifier("objetoIncidenteServiceLocal") 
	@Autowired 
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private DadosMontagemTextoBuilder textoBuilder;
	
	@Override
	public void load() {
		try {

			Collection<ObjetoIncidenteDto> recursos = getResources();

			for (ObjetoIncidenteDto objetoIncidente : recursos) {

				ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId());
				
				// Recuperando texto associado ao objeto incidente...
				Texto texto1 = textoService.recuperar(oi, getTipoPrimeiroTexto(), getMinistro().getId());

				// Recuperando texto associado ao objeto incidente...
				Texto texto2 = textoService.recuperar(oi, getTipoSegundoTexto(), getMinistro().getId());

				if (texto1 == null && texto2 != null) {
					String mensagem =  String.format(PROCESSO_NAO_POSSUI_TEXTOS,
							objetoIncidente.getIdentificacao(),
							getTipoPrimeiroTexto().getDescricao(),
							getTipoSegundoTexto().getDescricao(),
							getMinistro().getNome());
					getListaMsgProcessosSemTexto().add(mensagem);
				} else if (texto1 == null) {
					String mensagem =  String.format(PROCESSO_NAO_POSSUI_TEXTO,
							objetoIncidente.getIdentificacao(),
							getTipoPrimeiroTexto().getDescricao(),
							getMinistro().getNome());
					getListaMsgProcessosSemTexto().add(mensagem);
				} else if (texto2 == null) {
					String mensagem = String.format(PROCESSO_NAO_POSSUI_TEXTO,
							objetoIncidente.getIdentificacao(),
							getTipoSegundoTexto().getDescricao(),
							getMinistro().getNome());
					getListaMsgProcessosSemTexto().add(mensagem);
				} else {
					// Concatenando os dois textos...
					byte[] arquivosConcatenados = concatenarArquivos(texto1, texto2, quebrarPagina());

					if (texto2.getTipoTexto().equals(TipoTexto.ACORDAO) && FaseTexto.fasesComTextoAssinado.contains(texto2.getTipoFaseTextoDocumento())) {
						DadosMontagemTexto<Long> dadosRelatorio = textoBuilder.montaDadosMontagemTexto(texto2, false);
						getListaDadosMontagemTexto().add(dadosRelatorio);
					} else {
						// Montando dados do relatório e adicionando à lista de
						// textos para impressão...
						Texto dadosParaImpressao = (texto1.getTipoFaseTextoDocumento().getCodigoFase() <= texto2.getTipoFaseTextoDocumento().getCodigoFase() ? texto1 : texto2);
						DadosMontagemTexto<Long> dadosRelatorio = textoBuilder.montaDadosMontagemTexto(dadosParaImpressao, true, arquivosConcatenados);
						getListaDadosMontagemTexto().add(dadosRelatorio);
					}
				}
			}

			if (getListaMsgProcessosSemTexto().size() <= 0) {
				getDefinition().setFacet(PAGINA_SEM_MENSAGEM);
				getDefinition().setHeight(100);
			} else {
				for(String mensagem : getListaMsgProcessosSemTexto()) {
					addInformation(mensagem);
				}
			}

		} catch (Exception e) {
			addError(e.getMessage());
			sendToErrors();
		}
	}
	
	public void execute() {
		try {
			// Criando arquivo temporário para armazenar PDF de saída...
			File outputFile = File.createTempFile("report", ".pdf");

			DocumentTarget target = new FileDocumentTarget(outputFile);

			// Gerando relatório...
			openOfficeMontadorTextoService.criarTextosPDFUnico(
					getListaDadosMontagemTexto(), target, true);

			ReportUtils.report(new ByteArrayInputStream(ReportUtils.getBytesFromFile(outputFile)));
			
			getListaMsgProcessosSemTexto().clear();
			getListaDadosMontagemTexto().clear();
			
		} catch (MontadorTextoServiceException e) {
			addError(e.getMessage());
		} catch (IOException e) {
			addError(e.getMessage());
		} catch (Exception e) {
			addError(e.getMessage());
		}
		
		if(hasErrors()) {
			sendToErrors();
		}
	}
	
	/**
	 * Retorna o tipo do primeiro texto. Usado para recuperar o texto a partir
	 * do objeto incidente em questão.
	 * 
	 * <p>
	 * Deve ser implementado pelas subclasses.
	 * 
	 * @return o tipo do primeiro texto
	 */
	protected abstract TipoTexto getTipoPrimeiroTexto();

	/**
	 * Retorna o tipo do segundo texto. Usado para recuperar o texto a partir do
	 * objeto incidente em questão.
	 * 
	 * <p>
	 * Deve ser implementado pelas subclasses.
	 * 
	 * @return o tipo do segundo texto
	 */
	protected abstract TipoTexto getTipoSegundoTexto();

	/**
	 * Indica se deve ser inserida uma quebra de página ao concatenar os dois
	 * textos.
	 * 
	 * @return true, para inserir quebra de página, false, caso contrário
	 */
	protected abstract boolean quebrarPagina();

	/**
	 * Concatena dois arquivos RTF, retornando um único arquivo ODT. Permite
	 * adicionar uma quebra de página entre os dois arquivos.
	 * 
	 * @param texto1
	 *            o primeiro texto a ser concatenado
	 * @param texto2
	 *            o segundo texto a ser concatenado
	 * @param quebrarPagina
	 *            indica se a quebra de página deve, ou não, ser inserida
	 * 
	 * @return o array de bytes do arquvios concatenado
	 */
	private byte[] concatenarArquivos(Texto texto1, Texto texto2,
			boolean quebrarPagina) throws JDOMException, IOException,
			MontadorTextoServiceException, FileNotFoundException {
		File texto1AsOdt = converterArquivoParaOdt(texto1);
		File texto2AsOdt = converterArquivoParaOdt(texto2);

		File resultado = openOfficeMontadorTextoService.concatenaArquivosOdt(
				texto1AsOdt, texto2AsOdt, quebrarPagina);

		return IOUtils.toByteArray(new FileInputStream(resultado));
	}

	/**
	 * Converte o conteúdo (RTF) de um dado texto em um arquivo(File) ODT.
	 * 
	 * @param texto
	 *            o texto de entrada
	 * 
	 * @return o arquivo ODT
	 */
	private File converterArquivoParaOdt(Texto texto)
			throws MontadorTextoServiceException, IOException,
			FileNotFoundException {
		InputStream odtAsInputStream = openOfficeMontadorTextoService
				.converteArquivo(getTextoSource(texto), ExtensaoEnum.RTF,
						ExtensaoEnum.ODT);
		File odtAsFile = File.createTempFile(texto.getIdentificacao(), ".odt");
		FileOutputStream fos = new FileOutputStream(odtAsFile);
		IOUtils.copy(odtAsInputStream, fos);
		return odtAsFile;
	}

	/**
	 * Retorna o <code>TextoSource</code> para o conteúdo de um dado texto.
	 * 
	 * @param texto
	 *            o texto de entrada
	 * 
	 * @return o <code>TextoSource</code>
	 */
	private TextoSource getTextoSource(final Texto texto) {
		return new TextoSource() {
			@Override
			public byte[] getByteArray() throws IOException,
					MontadorTextoServiceException {
				return texto.getArquivoEletronico().getConteudo();
			}
		};
	}

	/**
	 * @param listaMsgProcessosSemTexto
	 *            the listaMsgProcessosSemTexto to set
	 */
	public void setListaMsgProcessosSemTexto(
			List<String> listaMsgProcessosSemTexto) {
		this.listaMsgProcessosSemTexto = listaMsgProcessosSemTexto;
	}

	/**
	 * @return the listaMsgProcessosSemTexto
	 */
	public List<String> getListaMsgProcessosSemTexto() {
		if (listaMsgProcessosSemTexto == null) {
			listaMsgProcessosSemTexto = new ArrayList<String>();
		}
		return listaMsgProcessosSemTexto;
	}


	/**
	 * @param listaDadosMontagemTexto
	 *            the listaDadosMontagemTexto to set
	 */
	public void setListaDadosMontagemTexto(
			List<DadosMontagemTexto<Long>> listaDadosMontagemTexto) {
		this.listaDadosMontagemTexto = listaDadosMontagemTexto;
	}

	/**
	 * @return the listaDadosMontagemTexto
	 */
	public List<DadosMontagemTexto<Long>> getListaDadosMontagemTexto() {
		if (listaDadosMontagemTexto == null) {
			listaDadosMontagemTexto = new ArrayList<DadosMontagemTexto<Long>>();
		}
		return listaDadosMontagemTexto;

	}
	
}
