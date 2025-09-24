/**
 * 
 */
package br.jus.stf.estf.decisao.support.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.CustomizacaoUsuario;
import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.estf.entidade.usuario.TipoDefinicao;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.usuario.model.enuns.EnumTipoResposavelTexto;
import br.gov.stf.estf.usuario.model.service.CustomizacaoUsuarioService;
import br.gov.stf.estf.usuario.model.service.TipoCustomizacaoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.support.persistence.ConfiguracaoSistemaDao;
import br.jus.stf.estf.decisao.support.security.Principal;

/**
 * @author Paulo.Estevao
 * @since 29.10.2011
 */
@Service("configuracaoSistemaServiceLocal")
public class ConfiguracaoSistemaService {
	
	private static final String PARAMETRO_TEXTO_RESTRITO_RESPONSAVEL = "texto_restrito_responsavel";
	private static final String PARAMETRO_CONTROLE_ACESSO_TEXTO = "tipo_responsavel";
	private static final String PARAMETRO_GERAR_TIMBRE_ASSINATURA = "timbre_assinatura";

	@Autowired
	private ConfiguracaoSistemaDao configuracaoSistemaDao;
	
	@Autowired
	private CustomizacaoUsuarioService customizacaoUsuarioService;

	@Autowired
	private TipoCustomizacaoService tipoCustomizacaoService;
	
	@Autowired
	private SetorService setorService;
	
	public void salvarConfiguracao(String chave, String valor) {
		configuracaoSistemaDao.salvarConfiguracao(chave, valor);
	}
	
	public boolean getSetorTextoRestritoResponsavel(Long idSetor) throws ServiceException{
		TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(PARAMETRO_TEXTO_RESTRITO_RESPONSAVEL);
		
		CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, idSetor);
		
		return ( customizacaoUsuario != null && customizacaoUsuario.getDescricao().equals("S") );
	}

	public boolean getSetorGerarTimbreAssinatura(Long idSetor) throws ServiceException{
		TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(PARAMETRO_GERAR_TIMBRE_ASSINATURA);
		
		CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, idSetor);
		
		return ( customizacaoUsuario != null && customizacaoUsuario.getDescricao().equals("S") );
	}
	
	public boolean isGerarTimbreAssinatura() throws ServiceException{
		Principal principal = getPrincipal();
		return getSetorGerarTimbreAssinatura(principal.getIdSetor());
	}
	
	public boolean isTextoRestritoResponsavel() throws ServiceException{
		Principal principal = getPrincipal();
		return getSetorTextoRestritoResponsavel(principal.getIdSetor());
	}
	
	public EnumTipoResposavelTexto retornaOpcaoTiposResponsaveis() throws ServiceException{
		Principal principal = getPrincipal();

		TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(PARAMETRO_CONTROLE_ACESSO_TEXTO);
		
		CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, principal.getIdSetor());
		
		if (customizacaoUsuario != null && customizacaoUsuario.getDescricao() != null )
			return EnumTipoResposavelTexto.getTipoByCodigo(customizacaoUsuario.getDescricao());
		else
			return EnumTipoResposavelTexto.USUARIO;
	}

	public void gravarConfiguracaoTextoRestritoResponsavel(String configuracao) throws ServiceException{
		Principal principal = getPrincipal();

		Setor setorUsuario = setorService.recuperarPorId( principal.getIdSetor() );

		TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(PARAMETRO_TEXTO_RESTRITO_RESPONSAVEL);
		
		CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, principal.getIdSetor());

		boolean jaExiste = true;
		if (customizacaoUsuario == null) {
			jaExiste = false;
			customizacaoUsuario = new CustomizacaoUsuario();
		}
		
		customizacaoUsuario.setUsuario(principal.getUsuario());
		customizacaoUsuario.setSetor(setorUsuario);
		customizacaoUsuario.setTipo(tipoCustomizacao);
		customizacaoUsuario.setNome("Restrição de acesso a textos " + principal.getMinistro().getSigla() );
		customizacaoUsuario.setDescricao(configuracao);

		
		if (jaExiste)
			customizacaoUsuarioService.alterar(customizacaoUsuario);
		else
			customizacaoUsuarioService.incluir(customizacaoUsuario);
		
	}
	
	
	public void gravarConfiguracaoTipoResponsavel(EnumTipoResposavelTexto configuracao) throws ServiceException{
		Principal principal = getPrincipal();

		Setor setorUsuario = setorService.recuperarPorId( principal.getIdSetor() );

		TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(PARAMETRO_CONTROLE_ACESSO_TEXTO);
		
		CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, principal.getIdSetor());

		boolean jaExiste = true;
		if (customizacaoUsuario == null) {
			jaExiste = false;
			customizacaoUsuario = new CustomizacaoUsuario();
		}
		
		customizacaoUsuario.setUsuario(principal.getUsuario());
		customizacaoUsuario.setSetor(setorUsuario);
		customizacaoUsuario.setTipo(tipoCustomizacao);
		customizacaoUsuario.setNome("Controle de acesso a textos " + principal.getMinistro().getSigla() );
		customizacaoUsuario.setDescricao(configuracao.getCodigo());
		
		if (jaExiste)
			customizacaoUsuarioService.alterar(customizacaoUsuario);
		else
			customizacaoUsuarioService.incluir(customizacaoUsuario);
	}
	
	public boolean isOrdenacaoNumerica() {
		Principal principal = getPrincipal();

		CustomizacaoUsuario customizacaoUsuario = null;
		try {
			TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(TipoCustomizacao.ORDENACAO_NUMERICA);
			customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, principal.getIdSetor());
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return (customizacaoUsuario != null && customizacaoUsuario.getDescricao().equals("S"));
	}
	
	public boolean isProibirAgendamentoVirtual() {
		Principal principal = getPrincipal();

		CustomizacaoUsuario customizacaoUsuario = null;
		try {
			TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(TipoCustomizacao.PROIBIR_AGENDAMENTO_VIRTUAL);
			customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, principal.getIdSetor());
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return (customizacaoUsuario != null && customizacaoUsuario.getDescricao().equals("S"));
	}
	
	public boolean isProibirNovoTexto() {
		Principal principal = getPrincipal();

		CustomizacaoUsuario customizacaoUsuario = null;
		try {
			TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(TipoCustomizacao.PROIBIR_NOVO_TEXTO);
			customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, principal.getIdSetor());
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return (customizacaoUsuario != null && customizacaoUsuario.getDescricao().equals("S"));
	}

	public void setOrdenacaoNumerica(boolean ordenacaoNumerica) {
		try {
			Principal principal = getPrincipal();
			Setor setorUsuario = setorService.recuperarPorId(principal.getIdSetor());
			TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(TipoCustomizacao.ORDENACAO_NUMERICA);

			if (tipoCustomizacao == null) {
				tipoCustomizacao = new TipoCustomizacao();
				tipoCustomizacao.setDescricao("Ordenação Numérica nas pré-listas de julgamento");
				tipoCustomizacao.setAtivo(Boolean.TRUE);
				tipoCustomizacao.setParametro(TipoCustomizacao.ORDENACAO_NUMERICA);
				tipoCustomizacao.setSiglaSistema("ESTFDECISAO");
				tipoCustomizacao.setDefinicao(TipoDefinicao.PARAMETRO_VALOR);

				tipoCustomizacao = tipoCustomizacaoService.salvar(tipoCustomizacao);
			}

			CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, setorUsuario.getId());

			if (customizacaoUsuario == null) {
				customizacaoUsuario = new CustomizacaoUsuario();
				customizacaoUsuario.setTipo(tipoCustomizacao);
				customizacaoUsuario.setSetor(setorUsuario);
				customizacaoUsuario.setUsuario(principal.getUsuario());
				customizacaoUsuario.setNome(TipoCustomizacao.ORDENACAO_NUMERICA);
			}

			customizacaoUsuario.setDescricao(ordenacaoNumerica ? "S" : "N");
			customizacaoUsuarioService.salvar(customizacaoUsuario);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	public void gravarConfiguracaoGerarTimbreAssinatura(String configuracao) throws ServiceException{
		Principal principal = getPrincipal();

		Setor setorUsuario = setorService.recuperarPorId( principal.getIdSetor() );

		TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(PARAMETRO_GERAR_TIMBRE_ASSINATURA);
		
		CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, principal.getIdSetor());

		boolean jaExiste = true;
		if (customizacaoUsuario == null) {
			jaExiste = false;
			customizacaoUsuario = new CustomizacaoUsuario();
		}
		
		customizacaoUsuario.setUsuario(principal.getUsuario());
		customizacaoUsuario.setSetor(setorUsuario);
		customizacaoUsuario.setTipo(tipoCustomizacao);
		customizacaoUsuario.setNome("Gerar timbre automaticamente ao realizar a assinatura digital de documentos  " + principal.getMinistro().getSigla() );
		customizacaoUsuario.setDescricao(configuracao);

		
		if (jaExiste)
			customizacaoUsuarioService.alterar(customizacaoUsuario);
		else
			customizacaoUsuarioService.incluir(customizacaoUsuario);
		
	}
	
	protected Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public void setProibirAgendamentoVirtual(boolean proibirAgendamentoVirtual) {
		try {
			Principal principal = getPrincipal();
			Setor setorUsuario = setorService.recuperarPorId(principal.getIdSetor());
			TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(TipoCustomizacao.PROIBIR_AGENDAMENTO_VIRTUAL);

			if (tipoCustomizacao == null) {
				tipoCustomizacao = new TipoCustomizacao();
				tipoCustomizacao.setDescricao("Proibir agendamento de processos no plenário virtual?");
				tipoCustomizacao.setAtivo(Boolean.TRUE);
				tipoCustomizacao.setParametro(TipoCustomizacao.PROIBIR_AGENDAMENTO_VIRTUAL);
				tipoCustomizacao.setSiglaSistema("ESTFDECISAO");
				tipoCustomizacao.setDefinicao(TipoDefinicao.PARAMETRO_VALOR);

				tipoCustomizacao = tipoCustomizacaoService.salvar(tipoCustomizacao);
			}

			CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, setorUsuario.getId());

			if (customizacaoUsuario == null) {
				customizacaoUsuario = new CustomizacaoUsuario();
				customizacaoUsuario.setTipo(tipoCustomizacao);
				customizacaoUsuario.setSetor(setorUsuario);
				customizacaoUsuario.setUsuario(principal.getUsuario());
				customizacaoUsuario.setNome(TipoCustomizacao.PROIBIR_AGENDAMENTO_VIRTUAL);
			}

			customizacaoUsuario.setDescricao(proibirAgendamentoVirtual ? "S" : "N");
			customizacaoUsuarioService.salvar(customizacaoUsuario);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setProibirNovoTexto(boolean proibirNovoTexto) {
		try {
			Principal principal = getPrincipal();
			Setor setorUsuario = setorService.recuperarPorId(principal.getIdSetor());
			TipoCustomizacao tipoCustomizacao = tipoCustomizacaoService.buscaPorDscParametro(TipoCustomizacao.PROIBIR_NOVO_TEXTO);

			if (tipoCustomizacao == null) {
				tipoCustomizacao = new TipoCustomizacao();
				tipoCustomizacao.setDescricao("Proibir criação de novos textos?");
				tipoCustomizacao.setAtivo(Boolean.TRUE);
				tipoCustomizacao.setParametro(TipoCustomizacao.PROIBIR_NOVO_TEXTO);
				tipoCustomizacao.setSiglaSistema("ESTFDECISAO");
				tipoCustomizacao.setDefinicao(TipoDefinicao.PARAMETRO_VALOR);

				tipoCustomizacao = tipoCustomizacaoService.salvar(tipoCustomizacao);
			}

			CustomizacaoUsuario customizacaoUsuario = customizacaoUsuarioService.retornaCustomizacaoSetor(tipoCustomizacao, setorUsuario.getId());

			if (customizacaoUsuario == null) {
				customizacaoUsuario = new CustomizacaoUsuario();
				customizacaoUsuario.setTipo(tipoCustomizacao);
				customizacaoUsuario.setSetor(setorUsuario);
				customizacaoUsuario.setUsuario(principal.getUsuario());
				customizacaoUsuario.setNome(TipoCustomizacao.PROIBIR_NOVO_TEXTO);
			}

			customizacaoUsuario.setDescricao(proibirNovoTexto ? "S" : "N");
			customizacaoUsuarioService.salvar(customizacaoUsuario);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
}
