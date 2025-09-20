package br.gov.stf.estf.assinatura.visao.jsf.beans.permissaoDeslocamento;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.entidade.documento.PermissaoDeslocamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanManterPermissaoDeslocamento extends AssinadorBaseBean{
	
	private static final long serialVersionUID = -1896441891192039643L;
	
	private Long codigoOrigem;
	private Long codigoDestinatario;
	private String nomeOrigem;
	private String nomeDestinatario;
	private Boolean renderizaTelaCadastro;
	private Boolean renderizaTelaDePesquisa;
	private Boolean renderizaTabelaDestinatario;
	
	private PermissaoDeslocamento permissaoDeslocamento;
	private List<SelectItem> itensPermissaoDeslocamento;
	
	private List<CheckableDataTableRowWrapper> listaPermissoes;
	private List<Setor> listaOrigemCarregada;
	private List<Setor> listaDestinatarioCarregada;
	
	private HtmlDataTable tabelaDestinatarios;
	private static final Object RENDERED_TELACADASTRO = new Object();
	private static final Object RENDERED_TELAPESQUISA = new Object();
	private static final Object RENDERED_TABELA_DEST = new Object();
	private static final Object CODIGO_ORIGEM = new Object();
	private static final Object CODIGO_DESTINATARIO = new Object();
	private static final Object PERMISSAO_DESLOCAMENTO = new Object();
	private static final Object LISTA_PERMISSOES = new Object();
	private static final Object LISTA_ORIGEM_CARREGADA = new Object();
	private static final Object LISTA_DESTINO_CARREGADA = new Object();
	

// ######################### SESSION ##########################	
	
	public BeanManterPermissaoDeslocamento() {
		restauraSessao();
	}
	
	private void restauraSessao(){
		if( getAtributo(PERMISSAO_DESLOCAMENTO) == null ){
			setAtributo(PERMISSAO_DESLOCAMENTO, new PermissaoDeslocamento());
		}
		setPermissaoDeslocamento((PermissaoDeslocamento) getAtributo(PERMISSAO_DESLOCAMENTO));

		if( getAtributo(CODIGO_ORIGEM) == null ){
			setAtributo(CODIGO_ORIGEM, null);
		}
		setCodigoOrigem((Long) getAtributo(CODIGO_ORIGEM));
		
		if( getAtributo(CODIGO_DESTINATARIO) == null ){
			setAtributo(CODIGO_DESTINATARIO, null);
		}
		setCodigoDestinatario((Long) getAtributo(CODIGO_DESTINATARIO));

		if (getAtributo(RENDERED_TELACADASTRO) == null) {
			setAtributo(RENDERED_TELACADASTRO, false);
		}
		setRenderizaTelaCadastro((Boolean) getAtributo(RENDERED_TELACADASTRO));
		
		
		if (getAtributo(RENDERED_TELAPESQUISA) == null) {
			setAtributo(RENDERED_TELAPESQUISA, true);
		}
		setRenderizaTelaDePesquisa((Boolean) getAtributo(RENDERED_TELAPESQUISA));

		if( getAtributo(LISTA_ORIGEM_CARREGADA) == null ){
			setAtributo(LISTA_ORIGEM_CARREGADA, new LinkedList<Setor>());
		}
		setListaOrigemCarregada((List<Setor>) getAtributo(LISTA_ORIGEM_CARREGADA));
		
		if( getAtributo(LISTA_DESTINO_CARREGADA) == null ){
			setAtributo(LISTA_DESTINO_CARREGADA, new LinkedList<Setor>());
		}
		setListaDestinatarioCarregada((List<Setor>) getAtributo(LISTA_DESTINO_CARREGADA));
		
		setListaPermissoes((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_PERMISSOES));
	}
	

	private void atualizaSessao(){
		setAtributo(RENDERED_TELACADASTRO, renderizaTelaCadastro);
		setAtributo(RENDERED_TELAPESQUISA, renderizaTelaDePesquisa);
		setAtributo(RENDERED_TABELA_DEST, renderizaTabelaDestinatario);
		setAtributo(PERMISSAO_DESLOCAMENTO, permissaoDeslocamento);
		setAtributo(LISTA_PERMISSOES, listaPermissoes);
		setAtributo(LISTA_ORIGEM_CARREGADA, listaOrigemCarregada);
		setAtributo(LISTA_DESTINO_CARREGADA, listaDestinatarioCarregada);
		setAtributo(CODIGO_ORIGEM, codigoOrigem);		
		setAtributo(CODIGO_DESTINATARIO, codigoDestinatario);
	}
	
// ########################## ACTION ##########################
	
	public void atualizaSessaoAction(ActionEvent evt){
		atualizaSessao();
	}
	
	public void limparTelaAction(ActionEvent evt){
		limparTela();
		atualizaSessao();
	}
	
	public void limparTelaCadastrarPermissaoDeslocamentoAction(ActionEvent evt){
		limparTela();
		setRenderizaTelaCadastro(true);
		setRenderizaTelaDePesquisa(false);
		atualizaSessao();
	}
	
	public void carregarOrigensAtivasAction(ActionEvent evt){
		carregarOrigensAtivas();
		atualizaSessao();
	}
	
	public void carregarDestinatarioPelaOrigemAction(ActionEvent evt){
		carregarDestinosAtivos();
		atualizaSessao();
	}
	


	public void salvarPermissaoDeslocamentoAction(ActionEvent evt){
		salvarPermissaoDeslocamento();
		atualizaSessao();
	}
	
	public void pesquisarPermissaoDeslocamentoAction(ActionEvent evt){
		pesquisarPermissaoDeslocamento();
		atualizaSessao();
	}
	
	public void alterarPermissaoDeslocamentoAction(ActionEvent evt){
		alterarPermissaoDeslocamento();
		atualizaSessao();
	}
	
	public void excluirPermissaoDeslocamentoAction(ActionEvent evt){
		excluirPermissaoDeslocamento();
		atualizaSessao();
	}
	
	
	public void fechaTelaCadastrarPermissaoDeslocamentoAction(ActionEvent evt){
		limparTela();
		atualizaSessao();
	}

// ######################## METHODs ############################
	
	/**
	 * Limpa os campos da tela
	 */
	public void limparTela(){
		setPermissaoDeslocamento(new PermissaoDeslocamento());
		setListaPermissoes(new LinkedList<CheckableDataTableRowWrapper>());
		setNomeDestinatario("");
		setNomeOrigem("");
		setRenderizaTelaCadastro(false);
		setRenderizaTelaDePesquisa(true);
		setRenderizaTabelaDestinatario(false);
		setCodigoOrigem(null);
		setCodigoDestinatario(null);
	}
	

	private void carregarOrigensAtivas() {
		
		listaOrigemCarregada = new LinkedList<Setor>();
		List<Setor> origens = new ArrayList<Setor>();
		
		try {
			origens = getSetorService().recuperarSetorPorIdOuDescricao(null,true);					
		} catch (ServiceException e) {
			reportarErro("Erro ao buscar a lista de origens.");
		}

		listaOrigemCarregada.addAll(origens);

	}
	
	
	private void carregarDestinosAtivos() {
		
		listaDestinatarioCarregada = new LinkedList<Setor>();
		List<Setor> destinos = new ArrayList<Setor>();
		
		try {
			destinos = getSetorService().recuperarSetorPorIdOuDescricao(null,true);				
		} catch (ServiceException e) {
			reportarErro("Erro ao buscar a lista de origens.");
		}

		listaDestinatarioCarregada.addAll(destinos);

	}

	
	@SuppressWarnings("rawtypes")
	public List pesquisarOrigensPelaListaSelecionada(Object value) {

		if (value == null) {
			return null;
		}
		if (value.toString().trim() == "") {
			return null;
		}

		List<Setor> listOrigens = new ArrayList<Setor>();
		
		if(listaOrigemCarregada != null && listaOrigemCarregada.size() > 0){
			for (Setor orig : listaOrigemCarregada){
				if( NumberUtils.soNumeros(value.toString())){
					if (orig.getId().toString().contains(value.toString())){
						listOrigens.add(orig);									
					}
					
				}else {
					if (orig.getNome().contains(value.toString().toUpperCase())){
						listOrigens.add(orig);
					}
				}
			}
		}

		return listOrigens;
	}
	
	@SuppressWarnings("rawtypes")
	public List pesquisarDestinatarioPelaListaSelecionada(Object value) {

		if (value == null) {
			return null;
		}
		if (value.toString().trim() == "") {
			return null;
		}

		List<Setor> listDestinatario = new ArrayList<Setor>();
		
		if(listaDestinatarioCarregada != null && listaDestinatarioCarregada.size() > 0){
			for (Setor dest : listaDestinatarioCarregada){
				if( NumberUtils.soNumeros(value.toString())){
					if (dest.getId().toString().contains(value.toString())){
						listDestinatario.add(dest);									
					}
					
				}else {
					if (dest.getNome().contains(value.toString().toUpperCase())){
						listDestinatario.add(dest);
					}
				}
			}
		}
		return listDestinatario;
	}
		
	
	private void pesquisarPermissaoDeslocamento(){
		
		List<PermissaoDeslocamento> lista = new LinkedList<PermissaoDeslocamento>();
		
		try{
			getDestinatarioService().limparSessao();
			lista = getPermissaoDeslocamentoService().pesquisar(codigoOrigem, codigoDestinatario,null);
			
		}catch( Exception e) {
			e.printStackTrace();
			reportarErro("Erro ao pesquisar", e.getMessage());
		}
		if(lista == null || lista.size() == 0){
			reportarInformacao("Nenhum registro encontrado para os parâmetros informados.");
		}else{
			setRenderizaTabelaDestinatario(true);
		}
		setListaPermissoes(getCheckableDataTableRowWrapperList((lista)));
	}
	
	
	private void salvarPermissaoDeslocamento(){
		
		if( stringEmpty(nomeOrigem)){
			reportarAviso("A origem deve ser selecionada");
			return;
		}
		
		if( stringEmpty(nomeDestinatario) ){
			reportarAviso("O nome do destinatário deve ser preenchido.");
			return;
		}		
		
		if(permissaoDeslocamento.getPermissao() == null){
			reportarAviso("Necessário informar se o descolamento é permitido.");
			return;
		}
	
		try {
			Setor origem = getSetorService().recuperarPorId(codigoOrigem);
			permissaoDeslocamento.setSetorOrigem(origem);
			Setor destino = getSetorService().recuperarPorId(codigoDestinatario);
			permissaoDeslocamento.setSetorDestino(destino);
			if(!existeRestricaoExclusiva()){
				getPermissaoDeslocamentoService().salvar(permissaoDeslocamento);
				reportarInformacao("Operação realizada com sucesso.");
				limparTela();
				pesquisarPermissaoDeslocamento();
				atualizaSessao();			
			}else{
				reportarAviso("Já existe um cadastro realizado entre a origem e o destino informado.");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean existeRestricaoExclusiva() throws ServiceException{
		List<PermissaoDeslocamento> lista = getPermissaoDeslocamentoService().pesquisar(codigoOrigem, codigoDestinatario, null);	
		return lista != null && !lista.isEmpty();
	}
	
	private void alterarPermissaoDeslocamento(){
		try {
			PermissaoDeslocamento permDesl = (PermissaoDeslocamento)((CheckableDataTableRowWrapper)tabelaDestinatarios.getRowData()).getWrappedObject();
			permissaoDeslocamento = getPermissaoDeslocamentoService().recuperarPorId(permDesl.getId());		
						
			setCodigoOrigem(permissaoDeslocamento.getSetorOrigem().getId());
			setNomeOrigem(permissaoDeslocamento.getSetorOrigem().getId().toString() + " - " + permissaoDeslocamento.getSetorOrigem().getNome().toUpperCase());
			setCodigoDestinatario(permissaoDeslocamento.getSetorDestino().getId());
			setNomeDestinatario(permissaoDeslocamento.getSetorDestino().getId().toString() + " - " + permissaoDeslocamento.getSetorDestino().getNome());
		
			setRenderizaTelaCadastro(true);
			setRenderizaTelaDePesquisa(false);
			setRenderizaTabelaDestinatario(false);
		} catch (ServiceException e) {
			reportarErro("Erro ao localizar o registro.");
			e.printStackTrace();
		}
	}

	private void excluirPermissaoDeslocamento(){
		PermissaoDeslocamento permDesl = (PermissaoDeslocamento)((CheckableDataTableRowWrapper)tabelaDestinatarios.getRowData()).getWrappedObject();
		try {
			permissaoDeslocamento = getPermissaoDeslocamentoService().recuperarPorId(permDesl.getId());			
			getPermissaoDeslocamentoService().excluir(permissaoDeslocamento);
			reportarAviso("Registro excluído com sucesso.");
			limparTela();
			pesquisarPermissaoDeslocamento();
			atualizaSessao();			
		} catch (ServiceException e) {
			reportarErro("Erro ao localizar o registro.");
			e.printStackTrace();
		}
	}
	
	private boolean stringEmpty(String valor){
		return valor != null && valor.trim().length() > 0?false:true;
	}
	
	
	
	
// ######################## GETs and  SETs ######################
	
	public Long getCodigoOrigem() {
		return codigoOrigem;
	}

	public void setCodigoOrigem(Long codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public String getNomeOrigem() {
		return nomeOrigem;
	}

	public void setNomeOrigem(String nomeOrigem) {
		this.nomeOrigem = nomeOrigem;
	}

	public List<CheckableDataTableRowWrapper> getListaPermissoes() {
		return listaPermissoes;
	}

	public void setListaPermissoes(List<CheckableDataTableRowWrapper> listaPermissoes) {
		this.listaPermissoes = listaPermissoes;
	}

	public HtmlDataTable getTabelaDestinatarios() {
		return tabelaDestinatarios;
	}

	public void setTabelaDestinatarios(HtmlDataTable tabelaDestinatarios) {
		this.tabelaDestinatarios = tabelaDestinatarios;
	}

	public Boolean getRenderizaTelaCadastro() {
		return renderizaTelaCadastro;
	}

	public void setRenderizaTelaCadastro(Boolean renderizaTelaCadastro) {
		this.renderizaTelaCadastro = renderizaTelaCadastro;
	}

	public Long getCodigoDestinatario() {
		return codigoDestinatario;
	}

	public void setCodigoDestinatario(Long codigoDestinatario) {
		this.codigoDestinatario = codigoDestinatario;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public PermissaoDeslocamento getPermissaoDeslocamento() {
		return permissaoDeslocamento;
	}

	public void setPermissaoDeslocamento(PermissaoDeslocamento permissaoDeslocamento) {
		this.permissaoDeslocamento = permissaoDeslocamento;
	}

	public List<Setor> getListaOrigemCarregada() {
		return listaOrigemCarregada;
	}

	public void setListaOrigemCarregada(List<Setor> listaOrigemCarregada) {
		this.listaOrigemCarregada = listaOrigemCarregada;
	}

	public List<Setor> getListaDestinatarioCarregada() {
		return listaDestinatarioCarregada;
	}

	public void setListaDestinatarioCarregada(List<Setor> listaDestinatarioCarregada) {
		this.listaDestinatarioCarregada = listaDestinatarioCarregada;
	}

	public List<SelectItem> getItensPermissaoDeslocamento() {
		return itensPermissaoDeslocamento;
	}

	public void setItensPermissaoDeslocamento(List<SelectItem> itensPermissaoDeslocamento) {
		this.itensPermissaoDeslocamento = itensPermissaoDeslocamento;
	}

	public Boolean getRenderizaTelaDePesquisa() {
		return renderizaTelaDePesquisa;
	}

	public void setRenderizaTelaDePesquisa(Boolean renderizaTelaDePesquisa) {
		this.renderizaTelaDePesquisa = renderizaTelaDePesquisa;
	}

	public Boolean getRenderizaTabelaDestinatario() {
		return renderizaTabelaDestinatario;
	}

	public void setRenderizaTabelaDestinatario(Boolean renderizaTabelaDestinatario) {
		this.renderizaTabelaDestinatario = renderizaTabelaDestinatario;
	}

}
