/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.Component;
import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.estf.decisao.pesquisa.domain.AllResourcesDto;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.query.Dto;
import br.jus.stf.estf.decisao.support.service.ConfiguracaoSistemaService;
import br.jus.stf.estf.decisao.support.util.GlobalFacesBean;

/**
 * @author Paulo.Estevao
 * @since 28.10.2011
 */
@Action(id = "configurarSistemaActionFacesBean", name = "Configurar Sistema", view = "/acoes/texto/configurarSistema.xhtml")
@Restrict({ActionIdentification.CONFIGURAR_SISTEMA})
public class ConfigurarSistemaActionFacesBean extends ActionSupport<AllResourcesDto> {
	
	private final String CHAVE_SERVIDORES_CONVERSAO = "converter.servers";
	
	private GlobalFacesBean globalFacesBean;
	
	@Autowired
	private ConfiguracaoSistemaService configuracaoSistemaService;
	
	private List<ServidorDto> servidoresConversao;
	
	private String novoServidor;

	@Override
	public void load() {
		servidoresConversao = new ArrayList<ServidorDto>();
		for (String nome : getGlobalFacesBean().getConverterServers()) {
			ServidorDto servidor = new ServidorDto();
			servidor.setNome(nome);
			servidoresConversao.add(servidor);
		}
	}
	
	public void incluirServidor() {
		if (novoServidor != null && novoServidor.trim().length() > 0) {
			ServidorDto dto = new ServidorDto();
			dto.setNome(novoServidor.trim());
			getServidoresConversao().add(dto);
			novoServidor = null;
		} else {
			addError("O nome do servidor deve conter ao menos um caracter.");
		}
	}
	
	public void excluirServidoresSelecionados() {
		List<ServidorDto> paraRemover = new ArrayList<ServidorDto>();
		for (ServidorDto dto : servidoresConversao) {
			if (dto.isSelected()) {
				paraRemover.add(dto);
			}
		}
		servidoresConversao.removeAll(paraRemover);
	}
	
	public void execute() {
		try {
			StringBuffer valor = new StringBuffer();
			List<String> listaServidores = new ArrayList<String>();
			for (ServidorDto servidor : getServidoresConversao()) {
				if (valor.toString().trim().length() > 0) {
					valor.append(",");
				}
				valor.append(servidor.getNome());
				listaServidores.add(servidor.getNome());
			}
			
			configuracaoSistemaService.salvarConfiguracao(CHAVE_SERVIDORES_CONVERSAO, valor.toString());
			
			globalFacesBean.setConverterServers(listaServidores);
		} catch (Exception e) {
			addError("Erro ao gravar configuração do sistema.");
			logger.error("Erro ao gravar configuração do sistema.", e);
		}
		
		if (hasMessages()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
	}
	
	public List<ServidorDto> getServidoresConversao() {
		if (servidoresConversao == null) {
			servidoresConversao = new ArrayList<ServidorDto>();
		}
		return servidoresConversao;
	}
	
	public void setServidoresConversao(
			List<ServidorDto> servidoresConversao) {
		this.servidoresConversao = servidoresConversao;
	}
	
	public String getNovoServidor() {
		return novoServidor;
	}
	
	public void setNovoServidor(String novoServidor) {
		this.novoServidor = novoServidor;
	}
	
	public GlobalFacesBean getGlobalFacesBean() {
		if (globalFacesBean == null) {
			globalFacesBean = (GlobalFacesBean) Component.getInstance(GlobalFacesBean.class);
		}
		return globalFacesBean;
	}
	
	public static class ServidorDto implements Dto {

		/**
		 * 
		 */
		private static final long serialVersionUID = -313249227550404884L;
		private String nome;
		private boolean selected;
		
		@Override
		public boolean isSelected() {
			return selected;
		}

		@Override
		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public Long getId() {
			return null;
		}

		@Override
		public boolean isFake() {
			return false;
		}
		
		public String getNome() {
			return nome;
		}
		
		public void setNome(String nome) {
			this.nome = nome;
		}
		
	}
}
