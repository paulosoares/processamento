import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.framework.model.service.ServiceException;

@Ignore
public class TesteAbrirArquivo extends AssinadorBaseBean {

	private static final long serialVersionUID = -4023974791801344671L;

	@SuppressWarnings({ "unused", "deprecation" })
	public void teste() throws FileNotFoundException {

		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();

		ModeloComunicacao modelo = new ModeloComunicacao();

		try {
			modelo = modeloComunicacaoService.pesquisarModeloEscolhido(86L, null);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		FileOutputStream fileRecebido = new FileOutputStream("D:\\testeArquivoTemporário.odt");

		ByteArrayInputStream arquivoSaida = new ByteArrayInputStream(modelo.getArquivoEletronico().getConteudo());

		try {
			IOUtils.copy(arquivoSaida, fileRecebido);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
