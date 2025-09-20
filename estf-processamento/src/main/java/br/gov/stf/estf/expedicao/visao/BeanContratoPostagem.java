package br.gov.stf.estf.expedicao.visao;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.expedicao.entidade.ContratoPostagem;
import br.gov.stf.estf.expedicao.model.service.ContratoPostagemService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Bean que mantém informações a respeito dos contratos de postagem.
 *
 * @author roberio.fernandes
 */
public class BeanContratoPostagem extends AssinadorBaseBean {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(BeanContratoPostagem.class);

	class ContratoPostagemDataVigenciaFinalComparator implements Comparator<ContratoPostagem> {

		@Override
		public int compare(ContratoPostagem contratoPostagem1, ContratoPostagem contratoPostagem2) {
			int resultado;
			if (contratoPostagem1.getDataVigenciaFinal() == null) {
				resultado = 1;
			} else if (contratoPostagem2.getDataVigenciaFinal() == null) {
				resultado = -1;
			} else {
				resultado = contratoPostagem1.getDataVigenciaFinal().compareTo(contratoPostagem2.getDataVigenciaFinal());
			}
			return resultado;
		}
	}

	private ContratoPostagem contratoPostagem;
	private ContratoPostagem contratoPostagemNovo;
	private List<ContratoPostagem> contratosEncerrados;
	private ContratoPostagemDataVigenciaFinalComparator contratoPostagemDataVigenciaFinalComparator;
	private String oncompleteModalCadastro = "Richfaces.hideModalPanel('idPnlContratoPostagemCadastro');";

	public BeanContratoPostagem() {
		inicializar();
	}

	private void inicializar() {
		contratoPostagemDataVigenciaFinalComparator = new ContratoPostagemDataVigenciaFinalComparator();
		contratoPostagemNovo = new ContratoPostagem();
		contratoPostagemNovo.setDataVigenciaInicial(new Date());
		ContratoPostagemService contratoPostagemService = getContratoPostagemService();
		try {
			contratosEncerrados = contratoPostagemService.listar();
			Collections.sort(contratosEncerrados, contratoPostagemDataVigenciaFinalComparator);
			Collections.reverse(contratosEncerrados);
		} catch (ServiceException e) {
			reportarErro("Erro ao listar contratos encerrados.");
		}
		try {
			contratoPostagem = contratoPostagemService.buscarVigente(new Date());
			if (contratoPostagem == null) {
				contratoPostagem = new ContratoPostagem();
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao buscar contrato vigente.");
		}
	}

	public String getOncompleteModalCadastro() {
		return oncompleteModalCadastro;
	}

	public void setOncompleteModalCadastro(String oncompleteModalCadastro) {
		this.oncompleteModalCadastro = oncompleteModalCadastro;
	}

	public ContratoPostagem getContratoPostagem() {
		return contratoPostagem;
	}

	public void setContratoPostagem(ContratoPostagem contratoPostagem) {
		this.contratoPostagem = contratoPostagem;
	}

	public ContratoPostagem getContratoPostagemNovo() {
		return contratoPostagemNovo;
	}

	public void setContratoPostagemNovo(ContratoPostagem contratoPostagemNovo) {
		this.contratoPostagemNovo = contratoPostagemNovo;
	}

	public List<ContratoPostagem> getContratosEncerrados() {
		return contratosEncerrados;
	}

	public void setContratosEncerrados(List<ContratoPostagem> contratosEncerrados) {
		this.contratosEncerrados = contratosEncerrados;
	}

	public int getQtdRegistros() {
		return contratosEncerrados.size();
	}

	public boolean isExisteContratoVigente() {
		return (contratoPostagem != null && contratoPostagem.getId() != null);
	}

	public void alterarContratoVigente(ActionEvent event) {
		ContratoPostagemService contratoPostagemService = getContratoPostagemService();
		try {
			if (!isErroValidacaoContrato("alteracao")) {
				contratoPostagemService.alterar(contratoPostagem);
				reportarInformacao("Contrato de postagem alterado com sucesso!");
			}
		} catch (ServiceException e) {
			reportarErro("", e, LOG);
		}
	}

	public void criarNovoContratoVigente(ActionEvent event) {
		ContratoPostagemService contratoPostagemService = getContratoPostagemService();
		try {
			if (!isErroValidacaoContrato("cadastro")) {
				contratoPostagemService.incluir(contratoPostagemNovo);
				inicializar();
				setOncompleteModalCadastro("Richfaces.hideModalPanel('idPnlContratoPostagemCadastro');");
				reportarInformacao("Contrato de postagem cadastrado com sucesso!");
			} else {
				setOncompleteModalCadastro("");
			}
		} catch (ServiceException e) {
			setOncompleteModalCadastro("");
			reportarErro("", e, LOG);
		}
	}

	private String getMensagemCampoObrigatorio(String nomeCampo) {
		return "Favor preencher o campo obrigatório '" + nomeCampo + "'.";
	}

	private boolean isErroValidacaoContrato(String acao) {
		String mensagemErro = "";
		if (acao.equals("cadastro")) {
			mensagemErro = getMensagemCadastro();
		} else if (acao.equals("alteracao")) {
			mensagemErro = getMensagemAlteracao();
		}

		if (!mensagemErro.isEmpty()) {
			reportarAviso(mensagemErro);
		}

		return !mensagemErro.isEmpty();
	}
	
	private String getMensagemCadastro(){
		
		String mensagemErro = "";
		
		if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagemNovo.getNumero())) {
			mensagemErro = getMensagemCampoObrigatorio("Número do Contrato");
		} else if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagemNovo.getCartao())) {
			mensagemErro = getMensagemCampoObrigatorio("Número do Cartão");
		} else if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagemNovo.getCodigoAdministrativo())) {
			mensagemErro = getMensagemCampoObrigatorio("Código Administrativo");
		} else if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagemNovo.getNumeroDiretoriaRegional())) {
			mensagemErro = getMensagemCampoObrigatorio("Número Diretoria Regional");
		} else if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagemNovo.getDataVigenciaInicial().toString())) {
			mensagemErro = getMensagemCampoObrigatorio("Data de Início");
		} else if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagemNovo.getUsuarioAutenticacaoWS())) {
			mensagemErro = getMensagemCampoObrigatorio("Login Web Service");
		} else if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagemNovo.getSenhaAutenticacaoWS())) {
			mensagemErro = getMensagemCampoObrigatorio("Senha Web Service");
		}
		
		return mensagemErro;
	}
	
	private String getMensagemAlteracao(){
		
		String mensagemErro = "";
		
		if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagem.getUsuarioAutenticacaoWS())) {
			mensagemErro = getMensagemCampoObrigatorio("Login Web Service");
		} else if (br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util
				.isStringNulaOuVazia(contratoPostagem.getSenhaAutenticacaoWS())) {
			mensagemErro = getMensagemCampoObrigatorio("Senha Web Service");
		}
		
		return mensagemErro;
	}
}