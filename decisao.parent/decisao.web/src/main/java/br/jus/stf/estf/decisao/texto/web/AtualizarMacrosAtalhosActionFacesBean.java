package br.jus.stf.estf.decisao.texto.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.jopendocument.dom.ODPackage;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.documento.model.service.ConfiguracaoTextoSetorService;
import br.gov.stf.estf.entidade.documento.ConfiguracaoTextoSetor;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.AllResourcesDto;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.util.ReportUtils;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;
import br.jus.stf.estf.montadortexto.OpenOfficeMontadorTextoService;
import br.jus.stf.estf.montadortexto.TextoSource;

/**
 * Atualiza macros e atalhos de gabinetes.
 * 
 * @author Paulo Estêvão
 * @see 05.05.2011
 */
@Action(id = "atualizarMacrosAtalhosActionFacesBean", name = "Atualizar Macros e Atalhos do Gabinete", view = "/acoes/texto/atualizarMacrosAtalhos.xhtml", height = 250, width = 450)
@Restrict({ActionIdentification.ATUALIZAR_MACROS_ATALHOS})
public class AtualizarMacrosAtalhosActionFacesBean extends
		ActionSupport<AllResourcesDto> implements
		ActionInterface<AllResourcesDto> {

	@Autowired
	private ConfiguracaoTextoSetorService configuracaoTextoSetorService;

	@Autowired
	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;

	private byte[] arquivoMacros;
	private byte[] arquivoAtalhos;
	
	private String erro;
	
	public void execute() {
		try {
			if (getMinistro() != null) {
				ConfiguracaoTextoSetor configuracaoTextoSetor = configuracaoTextoSetorService
						.recuperar(getSetorMinistro().getId());

				if (configuracaoTextoSetor != null) {
					if (arquivoMacros != null && arquivoMacros.length > 0) {
						configuracaoTextoSetor.setMacro(arquivoMacros);
					}

					if (arquivoAtalhos != null && arquivoAtalhos.length > 0) {
						configuracaoTextoSetor.setAtalho(arquivoAtalhos);
					}

					configuracaoTextoSetorService.alterar(configuracaoTextoSetor);
				} else {
					ConfiguracaoTextoSetor novaConfiguracao = new ConfiguracaoTextoSetor();
					novaConfiguracao.setMacro(arquivoMacros);
					novaConfiguracao.setAtalho(arquivoAtalhos);
					novaConfiguracao.setSetor(getSetorMinistro());
					configuracaoTextoSetorService.salvar(novaConfiguracao);
				}
			} else {
				erro = "É necessário selecionar uma base de gabinete para executar a ação.";
				addError(erro);
			}
		} catch (ServiceException e) {
			erro = "Erro ao recuperar configuração de texto do setor. " + e.getMessage();
			addError(erro);
		}		
	}	

	public void uploadListener(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();
		final File arquivoTemp = item.getFile();

		TextoSource textoSource = new TextoSource() {

			@Override
			public byte[] getByteArray() throws IOException,
					MontadorTextoServiceException {
				return ReportUtils.getBytesFromFile(arquivoTemp);
			}

		};

		InputStream is = openOfficeMontadorTextoService.converteArquivo(
				textoSource, ExtensaoEnum.ODT, ExtensaoEnum.ODT);
		ODPackage ott = new ODPackage(is);
		for (String entry : ott.getEntries()) {
			logger.info(entry);
		}
		arquivoMacros = ott.getBinaryFile("Basic/Standard/Module1.xml");
		arquivoAtalhos = ott
				.getBinaryFile("Configurations2/accelerator/current.xml");

		logger.info(new String(arquivoMacros));
		logger.info(new String(arquivoAtalhos));

		execute();
	}
	
	public void concluir() {
		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			addError(erro);
			sendToErrors();
		}
	}
	
	@Override
	protected String getErrorTitle() {
		return "Ocorreu um erro ao atualizar macros e atalhos do gabinete.";
	}

	public byte[] getArquivoMacros() {
		return arquivoMacros;
	}

	public void setArquivoMacros(byte[] arquivoMacros) {
		if (arquivoMacros != null) {
			this.arquivoMacros = Arrays.copyOf(arquivoMacros, arquivoMacros.length);
		} else {
			this.arquivoMacros = null;
		}
	}

	public byte[] getArquivoAtalhos() {
		return arquivoAtalhos;
	}

	public void setArquivoAtalhos(byte[] arquivoAtalhos) {
		if (arquivoAtalhos != null) {
			this.arquivoAtalhos = Arrays.copyOf(arquivoAtalhos, arquivoAtalhos.length);
		} else {
			this.arquivoAtalhos = null;
		}
	}
}
