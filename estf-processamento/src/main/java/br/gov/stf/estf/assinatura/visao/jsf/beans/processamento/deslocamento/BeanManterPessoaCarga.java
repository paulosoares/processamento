package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.jurisdicionado.EnderecoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.TelefoneJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.estf.entidade.jurisdicionado.TipoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoTelefone;
import br.gov.stf.estf.entidade.jurisdicionado.util.JurisdicionadoResult;
import br.gov.stf.estf.entidade.util.EnderecoView;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanManterPessoaCarga extends AssinadorBaseBean {

	private static final long serialVersionUID = -1896441891192039643L;
	
	private Integer indiceEnderecoJurisdicionado;
	private Integer indiceTelefoneJurisdicionado;
	private Long idJurisdicionado;
	private String oabJurisdicionado;
	private String cpfJurisdicionado;
	private String rgJurisdicionado;
	private Short tipoDestino;
	private String telefoneJusHifen;
	private String nomeJurisdicionado;
	private String idUf;
	private String ufOAB;
	private String tipoPapelJurisdicionado;
	private Boolean renderizaTelaCadastro;
	private Boolean renderizaTelaDePesquisa;
	private Boolean renderizaBotaoAlterarEndereco;
	private Boolean renderizaBotaoAlterarTelefone;
	private Boolean renderizaBotaoAlterarJurisdicionado;
	private Boolean renderizaTabelaJurisdicionado;
	private Boolean desabilitaSeForAlteracao;
	private Boolean desabilitaRadioPapel;
	private Boolean existeCadastro;
	private Boolean isCPFErrado;
	private Date dataValidadeCadastro;
	private String observacao;
	
	private EnderecoJurisdicionado enderecoJurisdicionado;
	private EnderecoJurisdicionado enderecoJurisdicionadoTMP;
	private Jurisdicionado jurisdicionado;
	private Jurisdicionado jurisdicionadoJaCadastrado;
	private TelefoneJurisdicionado telefoneJurisdicionado;
	private TelefoneJurisdicionado telefoneJurisdicionadoTMP;
	
	private List<SelectItem> itensUf;
	private List<SelectItem> itensUfOAB;
	private List<SelectItem> itensTipoTelefone;
	
	private List<CheckableDataTableRowWrapper> listaJurisdicionado;
	private List<CheckableDataTableRowWrapper> listaEndereco;
	private List<CheckableDataTableRowWrapper> listaTelefone;
	
	private List<Jurisdicionado> listaJurisdicionadoCarregada;
	
	private HtmlDataTable tabelaJurisdicionado;
	private HtmlDataTable tabelaEndereco;
	private HtmlDataTable tabelaTelefone;
	
	private HtmlInputHidden inputVerificaCadastroPessoa;
	private List<EnderecoJurisdicionado> enderecosParaExclusao;
	
	
// ######################### SESSION VARIABLE #################	
	
	private static final Object INDICE_END_JURIS = new Object();
	private static final Object INDICE_TEL_JURIS = new Object();
	private static final Object RENDERED_TELACADASTRO = new Object();
	private static final Object RENDERED_TELAPESQUISA = new Object();
	private static final Object RENDERED_TABELA_JURIS = new Object();
	private static final Object RENDERED_BOTAO_ALTERAR_END = new Object();
	private static final Object RENDERED_BOTAO_ALTERAR_TEL = new Object();
	private static final Object RENDERED_BOTAO_ALTERAR_JURI = new Object();
	private static final Object DESABILITA_ALTERACAO = new Object();
	private static final Object DESABILITA_RADIO_PAPEL = new Object();
	private static final Object EXISTE_CADASTRO = new Object();
	private static final Object IS_CPF_ERRADO = new Object();
	private static final Object CODIGO_JURISDIONADO = new Object();
	private static final Object TIPO_PAPEL_JURISDICIONADO = new Object();
	private static final Object TELEFONE_HIFEN = new Object();
	private static final Object ITENS_UF = new Object();
	private static final Object ITENS_UF_OAB = new Object();
	private static final Object ITENS_TIPO_TELEFONE = new Object();
	private static final Object ENDERECO = new Object();
	private static final Object ENDERECO_JURIS_TMP = new Object();
	private static final Object JURISDICIONADO = new Object();
	private static final Object JURISDICIONADO_CPF = new Object();
	private static final Object JURISDICIONADO_RG = new Object();
	private static final Object JURISDICIONADO_NUM_OAB = new Object();
	private static final Object JURISDICIONADO_JA_CADASTRADO = new Object();
	private static final Object TELEFONE_JURISDICIONADO = new Object();
	private static final Object TELEFONE_JURISDICIONADO_TMP = new Object();
	private static final Object LISTA_ENDERECO = new Object();
	private static final Object LISTA_TELEFONE = new Object();
	private static final Object LISTA_JURISDICIONADO = new Object();
	private static final Object LISTA_JURIS_CARREGADA = new Object();
	private static final Object ENDERECOS_PARA_EXCLUSAO = new Object();
	
	

// ######################### SESSION ##########################	
	
	public BeanManterPessoaCarga() {
		restauraSessao();
	}
	
	private void restauraSessao(){
		
		if( getAtributo(INDICE_END_JURIS) == null ){
			setAtributo(INDICE_END_JURIS, null);
		}
		setIndiceEnderecoJurisdicionado((Integer) getAtributo(INDICE_END_JURIS));

		if( getAtributo(INDICE_TEL_JURIS) == null ){
			setAtributo(INDICE_TEL_JURIS, null);
		}
		setIndiceTelefoneJurisdicionado((Integer) getAtributo(INDICE_TEL_JURIS));
		
		if( getAtributo(JURISDICIONADO) == null ){
			setAtributo(JURISDICIONADO, new Jurisdicionado());
		}
		setJurisdicionado((Jurisdicionado) getAtributo(JURISDICIONADO));
		
		if( getAtributo(TIPO_PAPEL_JURISDICIONADO) == null ){
			setAtributo(TIPO_PAPEL_JURISDICIONADO, null);
		}
		setTipoPapelJurisdicionado((String) getAtributo(TIPO_PAPEL_JURISDICIONADO));
		
		if( getAtributo(JURISDICIONADO_CPF) == null ){
			setAtributo(JURISDICIONADO_CPF, "");
		}
		setCpfJurisdicionado((String) getAtributo(JURISDICIONADO_CPF));
		
		if( getAtributo(JURISDICIONADO_RG) == null ){
			setAtributo(JURISDICIONADO_RG, "");
		}
		setRgJurisdicionado((String) getAtributo(JURISDICIONADO_RG));
		
		if( getAtributo(JURISDICIONADO_NUM_OAB) == null ){
			setAtributo(JURISDICIONADO_NUM_OAB, "");
		}
		setOabJurisdicionado((String) getAtributo(JURISDICIONADO_NUM_OAB));
		
		if( getAtributo(JURISDICIONADO_JA_CADASTRADO) == null ){
			setAtributo(JURISDICIONADO_JA_CADASTRADO, null);
		}
		setJurisdicionadoJaCadastrado((Jurisdicionado) getAtributo(JURISDICIONADO_JA_CADASTRADO));
		
		if( getAtributo(CODIGO_JURISDIONADO) == null ){
			setAtributo(CODIGO_JURISDIONADO, idJurisdicionado);
		}
		setIdJurisdicionado((Long) getAtributo(CODIGO_JURISDIONADO));
		
		if( getAtributo(ENDERECO) == null ){
			setAtributo(ENDERECO, new EnderecoJurisdicionado());
		}
		setEnderecoJurisdicionado((EnderecoJurisdicionado) getAtributo(ENDERECO));
		
		if (getAtributo(ENDERECO_JURIS_TMP) == null){
			setAtributo(ENDERECO_JURIS_TMP,  null);
		}
		setEnderecoJurisdicionadoTMP((EnderecoJurisdicionado) getAtributo(ENDERECO_JURIS_TMP));
		
		if( getAtributo(TELEFONE_JURISDICIONADO) == null ){
			setAtributo(TELEFONE_JURISDICIONADO, new TelefoneJurisdicionado());
		}
		setTelefoneJurisdicionado((TelefoneJurisdicionado) getAtributo(TELEFONE_JURISDICIONADO));
		
		if (getAtributo(TELEFONE_JURISDICIONADO_TMP) == null){
			setAtributo(TELEFONE_JURISDICIONADO_TMP, null);
		}
		setTelefoneJurisdicionadoTMP((TelefoneJurisdicionado) getAtributo(TELEFONE_JURISDICIONADO_TMP));
		
		if (getAtributo(RENDERED_TELACADASTRO) == null) {
			setAtributo(RENDERED_TELACADASTRO, false);
		}
		setRenderizaTelaCadastro((Boolean) getAtributo(RENDERED_TELACADASTRO));
		
		if (getAtributo(RENDERED_TELAPESQUISA) == null) {
			setAtributo(RENDERED_TELAPESQUISA, true);
		}
		setRenderizaTelaDePesquisa((Boolean) getAtributo(RENDERED_TELAPESQUISA));
		
		if (getAtributo(RENDERED_BOTAO_ALTERAR_END) == null) {
			setAtributo(RENDERED_BOTAO_ALTERAR_END, false);
		}
		setRenderizaBotaoAlterarEndereco((Boolean) getAtributo(RENDERED_BOTAO_ALTERAR_END));
		
		if (getAtributo(RENDERED_BOTAO_ALTERAR_TEL) == null) {
			setAtributo(RENDERED_BOTAO_ALTERAR_TEL, false);
		}
		setRenderizaBotaoAlterarTelefone((Boolean) getAtributo(RENDERED_BOTAO_ALTERAR_TEL));
		
		if (getAtributo(RENDERED_BOTAO_ALTERAR_JURI) == null) {
			setAtributo(RENDERED_BOTAO_ALTERAR_JURI, false);
		}
		setRenderizaBotaoAlterarJurisdicionado((Boolean) getAtributo(RENDERED_BOTAO_ALTERAR_JURI));

		if (getAtributo(RENDERED_TABELA_JURIS) == null) {
			setAtributo(RENDERED_TABELA_JURIS, false);
		}
		setRenderizaTabelaJurisdicionado((Boolean) getAtributo(RENDERED_TABELA_JURIS));		
		
		if (getAtributo(DESABILITA_ALTERACAO) == null) {
			setAtributo(DESABILITA_ALTERACAO, false);
		}
		setDesabilitaSeForAlteracao((Boolean) getAtributo(DESABILITA_ALTERACAO));
		
		if (getAtributo(DESABILITA_RADIO_PAPEL) == null) {
			setAtributo(DESABILITA_RADIO_PAPEL, false);
		}
		setDesabilitaRadioPapel((Boolean) getAtributo(DESABILITA_RADIO_PAPEL));
		
		if (getAtributo(EXISTE_CADASTRO) == null) {
			setAtributo(EXISTE_CADASTRO, false);
		}
		setExisteCadastro((Boolean) getAtributo(EXISTE_CADASTRO));
		
		if (getAtributo(IS_CPF_ERRADO) == null) {
			setAtributo(IS_CPF_ERRADO, false);
		}
		setIsCPFErrado((Boolean) getAtributo(IS_CPF_ERRADO));
		
		if (getAtributo(TELEFONE_HIFEN) == null) {
			setAtributo(TELEFONE_HIFEN, "");
		}
		setTelefoneJusHifen((String) getAtributo(TELEFONE_HIFEN));
		
		if( getAtributo(ITENS_UF) == null ){
			setAtributo(ITENS_UF,montarListaUf());
		}
		setItensUf((List<SelectItem>) getAtributo(ITENS_UF));
		
		if( getAtributo(ITENS_UF_OAB) == null ){
			setAtributo(ITENS_UF_OAB,montarListaUf());
		}
		setItensUfOAB((List<SelectItem>) getAtributo(ITENS_UF_OAB));
		
		if( getAtributo(LISTA_JURIS_CARREGADA) == null ){
			setAtributo(LISTA_JURIS_CARREGADA, new LinkedList<Jurisdicionado>());
		}
		setListaJurisdicionadoCarregada((List<Jurisdicionado>) getAtributo(LISTA_JURIS_CARREGADA));
		
		if (getAtributo(ITENS_TIPO_TELEFONE) == null) {
			setAtributo(ITENS_TIPO_TELEFONE, carregarComboTipoTelefone());
		}
		setItensTipoTelefone((List<SelectItem>) getAtributo(ITENS_TIPO_TELEFONE));
		
		setListaJurisdicionado((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_JURISDICIONADO));
		
		setListaEndereco((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_ENDERECO));
		
		setListaTelefone((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_TELEFONE));
		
		if (getAtributo(ENDERECOS_PARA_EXCLUSAO) == null) {
			setAtributo(ENDERECOS_PARA_EXCLUSAO, new ArrayList<EnderecoJurisdicionado>());
		}
		setEnderecosParaExclusao((List<EnderecoJurisdicionado>) getAtributo(ENDERECOS_PARA_EXCLUSAO));
	}
	
	private void atualizaSessao(){

		setAtributo(INDICE_END_JURIS, indiceEnderecoJurisdicionado);
		setAtributo(INDICE_TEL_JURIS, indiceTelefoneJurisdicionado);
		setAtributo(RENDERED_TELACADASTRO, renderizaTelaCadastro);
		setAtributo(RENDERED_TELAPESQUISA, renderizaTelaDePesquisa);
		setAtributo(RENDERED_BOTAO_ALTERAR_END, renderizaBotaoAlterarEndereco);
		setAtributo(RENDERED_BOTAO_ALTERAR_TEL, renderizaBotaoAlterarTelefone);
		setAtributo(RENDERED_BOTAO_ALTERAR_JURI, renderizaBotaoAlterarJurisdicionado);
		setAtributo(DESABILITA_ALTERACAO, desabilitaSeForAlteracao);
		setAtributo(DESABILITA_RADIO_PAPEL, desabilitaRadioPapel);
		setAtributo(EXISTE_CADASTRO, existeCadastro);
		setAtributo(IS_CPF_ERRADO, isCPFErrado);
		setAtributo(RENDERED_TABELA_JURIS, renderizaTabelaJurisdicionado);
		setAtributo(ENDERECO, enderecoJurisdicionado);
		setAtributo(ENDERECO_JURIS_TMP, enderecoJurisdicionadoTMP);
		setAtributo(JURISDICIONADO, jurisdicionado);
		setAtributo(JURISDICIONADO_CPF, cpfJurisdicionado);
		setAtributo(JURISDICIONADO_RG, rgJurisdicionado);
		setAtributo(JURISDICIONADO_NUM_OAB, oabJurisdicionado);
		setAtributo(JURISDICIONADO_JA_CADASTRADO, jurisdicionadoJaCadastrado);
		setAtributo(TELEFONE_JURISDICIONADO, telefoneJurisdicionado);
		setAtributo(TELEFONE_JURISDICIONADO_TMP, telefoneJurisdicionadoTMP);
		setAtributo(LISTA_JURISDICIONADO, listaJurisdicionado);
		setAtributo(LISTA_ENDERECO, listaEndereco);
		setAtributo(LISTA_TELEFONE, listaTelefone);
		setAtributo(LISTA_JURIS_CARREGADA, listaJurisdicionadoCarregada);
		setAtributo(CODIGO_JURISDIONADO, idJurisdicionado);
		setAtributo(TIPO_PAPEL_JURISDICIONADO, tipoPapelJurisdicionado);
		setAtributo(TELEFONE_HIFEN, telefoneJusHifen);
		setAtributo(ITENS_TIPO_TELEFONE, itensTipoTelefone);
		setAtributo(ENDERECOS_PARA_EXCLUSAO, enderecosParaExclusao);
	}
	
// ########################## ACTION ##########################
	
	public void atualizaLabelOAB(ActionEvent evt) throws Exception {
		UIComponent form = FacesContext.getCurrentInstance().getViewRoot().findComponent("form");
		int i = 0;
		while (form.findComponent("obrigatorioOAB") == null){
			form = form.getParent();
			i++;
			if (i > 20) {
				throw new Exception("Componente de interface não localizado");
			}
		}
		if (tipoPapelJurisdicionado == null || !tipoPapelJurisdicionado.equals("P")) {
			form.findComponent("obrigatorioOAB").getAttributes().put("style", "visibility:visible");
		} else {
			form.findComponent("obrigatorioOAB").getAttributes().put("style", "visibility:hidden");
		}
		
	}
	
	public void atualizaSessaoAction(ActionEvent evt){
		atualizaSessao();
	}
	
	public void limparTelaAction(ActionEvent evt){
		limparTela();
		atualizaSessao();
	}
	
	public void limparTelaCadastrarJurisdicionadoAction(ActionEvent evt){
		limparTela();
		setRenderizaTelaCadastro(true);
		setDesabilitaRadioPapel(false);
		setRenderizaTelaDePesquisa(false);
		atualizaSessao();
	}
	
	public void adicionarEnderecoAction(ActionEvent evt){
		adicionarEndereco();
		atualizaSessao();
	}
	
	public void recuperarEnderecoCepAction(ActionEvent evt){
		recuperarEnderecoCep();
		atualizaSessao();
	}
	
	public void verificaSeExisteJurisdicionadoAction(ActionEvent evt){
		verificaSeExisteJurisdicionado();
		atualizaSessao();
	}
	
	public void alterarEnderecoAction(ActionEvent evt){
		setRenderizaBotaoAlterarEndereco(true);
		alterarEndereco();
		atualizaSessao();
	}
	
	public void excluirEnderecoAction(ActionEvent evt){
		excluirEndereco();
		atualizaSessao();
	}
	
	public void novoEnderecoAction(ActionEvent evt){
		setEnderecoJurisdicionado(new EnderecoJurisdicionado());
		setEnderecoJurisdicionadoTMP(null);
		setRenderizaBotaoAlterarEndereco(false);
		atualizaSessao();
	}
	
	public void adicionarTelefoneAction(ActionEvent evt){
		adicionarTelefone();
		atualizaSessao();
	}

	public void alterarTelefoneAction(ActionEvent evt){
		setRenderizaBotaoAlterarTelefone(true);
		alterarTelefone();
		atualizaSessao();
	}
	
	public void novoTelefoneAction(ActionEvent evt){
		setTelefoneJurisdicionado(new TelefoneJurisdicionado());
		setTelefoneJurisdicionadoTMP(null);
		setRenderizaBotaoAlterarTelefone(false);
		atualizaSessao();
	}
	
	public void excluirTelefoneAction(ActionEvent evt){
		excluirTelefone();
		atualizaSessao();
	}
	
	public void pesquisarJurisdicionadoAction(ActionEvent evt){
		pesquisarJurisdicionado();
		atualizaSessao();
	}
	
	public void alterarJurisdicionadoAction(ActionEvent evt){
		alterarJurisdicionado();
		atualizaSessao();
	}
	
	
	public void salvarJurisdicionadoAction(ActionEvent evt){
		salvarJurisdicionado();
		atualizaSessao();
	}
	
	public void fechaTelaCadastrarJurisdicionadoAction(ActionEvent evt){
		limparTela();
		atualizaSessao();
	}

// ######################## METHODs ############################
	
	public Boolean getDesabilitarPesquisa() {
		if (listaJurisdicionado == null) {
			return false;
		}
		return listaJurisdicionado.size()>0;
	}
	
	public void setaValorIdJurisdicionado(){
		atualizaSessao();
		pesquisarJurisdicionado();
		atualizaSessao();
	}
	
	/**
	 * Limpa os campos da tela
	 */
	public void limparTela(){
		setJurisdicionado(new Jurisdicionado());
		setJurisdicionadoJaCadastrado(null);
		setListaEndereco(new LinkedList<CheckableDataTableRowWrapper>());
		setListaTelefone(new LinkedList<CheckableDataTableRowWrapper>());
		setListaJurisdicionado(new LinkedList<CheckableDataTableRowWrapper>());
		setIdUf(null);
		setUfOAB("");
		setEnderecoJurisdicionado(new EnderecoJurisdicionado());
		setTelefoneJurisdicionado(new TelefoneJurisdicionado());
		setNomeJurisdicionado("");
		setTelefoneJusHifen("");
		setCpfJurisdicionado("");
		setRgJurisdicionado("");
		setOabJurisdicionado("");
		setObservacao("");
		setRenderizaTelaCadastro(false);
		setRenderizaTelaDePesquisa(true);
		setRenderizaBotaoAlterarEndereco(false);
		setRenderizaBotaoAlterarTelefone(false);
		setRenderizaBotaoAlterarJurisdicionado(false);
		setRenderizaTabelaJurisdicionado(false);
		setDesabilitaSeForAlteracao(false);
		setDesabilitaRadioPapel(false);
		setExisteCadastro(false);
		setIsCPFErrado(false);
		setIdJurisdicionado(null);
		setEnderecoJurisdicionadoTMP(null);
		setDataValidadeCadastro(null);
		setTipoPapelJurisdicionado(null);
	}
	
	
	public List carregarJurisdicionadosPeloIdNome(Object value){
		
		String juris = null;
		List<JurisdicionadoResult> results = null;
		if (value != null)
			juris = value.toString();

		if (StringUtils.isNotVazia(juris)) {
			try {
				results = getJurisdicionadoService().pesquisarResultCadastro(juris);
			} catch (NumberFormatException e) {
				reportarErro("Pessoa inválida: " + juris);
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar a(s) pessoa(s): " + juris);
			}
		}
		atualizaSessao();
		return results;

	}
	
	/**
	 * Pesquisa os jurisdicionado
	 */
	private void pesquisarJurisdicionado(){
		
		setListaJurisdicionado(null);
		
		if (oabJurisdicionado == null && idUf != null){
			reportarAviso("O número da OAB deve ser informado.");
			return;
		}

/*		if ((nomeJurisdicionado == null || nomeJurisdicionado.trim().length() == 0)
				&& (oabJurisdicionado == null || oabJurisdicionado.trim().length() == 0)){
			reportarAviso("O nome ou a OAB devem ser informados.");
			return;
		}*/
		
		if (idJurisdicionado == null && oabJurisdicionado == null){
			reportarAviso("O nome ou a OAB devem ser informados.");
			return;
		}
		
		List<Jurisdicionado> listaJurisdicionadoPesq = new LinkedList<Jurisdicionado>();
		
		try{
			getJurisdicionadoService().limparSessao();
			listaJurisdicionadoPesq = getJurisdicionadoService().pesquisar(idJurisdicionado, oabJurisdicionado, idUf);
			
		}catch( Exception e) {
			reportarErro("Erro ao pesquisar", e.getMessage());
		}
		if(listaJurisdicionadoPesq == null || listaJurisdicionadoPesq.size() == 0){
			reportarInformacao("Nenhuma pessoa foi encontrada com os parâmetros informados.");
		}else{
			setRenderizaTabelaJurisdicionado(true);
		}
		setListaJurisdicionado(getCheckableDataTableRowWrapperList((listaJurisdicionadoPesq)));
	}
	
		
	/**
	 * Adiciona os dados preenchidos na lista de endereço e exibe
	 * na tabela
	 */
	@SuppressWarnings("unchecked")
	private void adicionarEndereco(){
		

		if (stringEmpty(jurisdicionado.getNome())){
			reportarAviso("O campo nome está vazio. Favor preencher.");
			return;
		}
		
		if (enderecoJurisdicionado.getLogradouro() == null || enderecoJurisdicionado.getLogradouro().trim().length() == 0){
			reportarAviso("O campo endereço deve ser preenchido.");
			return;
		}
		
		if((enderecoJurisdicionado.getNumero() == null || enderecoJurisdicionado.getNumero().trim().length() == 0)){
			reportarAviso("O número deve ser preenchido.");
			return;
		}
		
		if (enderecoJurisdicionado.getUF() == null || enderecoJurisdicionado.getUF().trim().length() == 0){
			reportarAviso("O campo UF deve ser selecionado.");
			return;
		}
		
		if (enderecoJurisdicionado.getMunicipio() == null || enderecoJurisdicionado.getMunicipio().trim().length() == 0){
			reportarAviso("O campo cidade deve ser preenchido");
			return;
		}
		
		if( listaEndereco != null && listaEndereco.size() > 0 ){
			
			List<EnderecoJurisdicionado> lista = getOriginalListForCheckableDataTableRowWrapper(listaEndereco);
			Boolean inicializaListaTroca = false;
			for( int i = 0 ; i < lista.size() ; i++ ){
				
//				EnderecoJurisdicionado end = new EnderecoJurisdicionado();
				EnderecoJurisdicionado end = lista.get(i);
				
				if(valoresIguais(end.getCep(), enderecoJurisdicionado.getCep()) &&
						valoresIguais(end.getLogradouro(), enderecoJurisdicionado.getLogradouro())&&
						valoresIguais(end.getNumero(), enderecoJurisdicionado.getNumero())&&
						valoresIguais(end.getBairro(), enderecoJurisdicionado.getBairro())&&
						valoresIguais(end.getComplemento(), enderecoJurisdicionado.getComplemento())&&
						valoresIguais(end.getMunicipio(), enderecoJurisdicionado.getMunicipio())&&
						valoresIguais(end.getUF(), enderecoJurisdicionado.getUF())){
					reportarAviso("O endereço já esta adicionado.");
					return;
				}	
				
				//reinicializa a lista um única vez para que o registro alterado
				//seja inserido na exata posição do regitro original
				if (!inicializaListaTroca && enderecoJurisdicionadoTMP != null){
					setListaEndereco(new LinkedList<CheckableDataTableRowWrapper>());
					inicializaListaTroca = true;					
				}
				
				if (enderecoJurisdicionadoTMP != null){
					if (i == indiceEnderecoJurisdicionado){
						enderecoJurisdicionado.setJurisdicionado(jurisdicionado);
						listaEndereco.add(new CheckableDataTableRowWrapper(enderecoJurisdicionado));
					}else{
						listaEndereco.add(new CheckableDataTableRowWrapper(end));
					}
				}
			}
			
		}else{
			setListaEndereco(new LinkedList<CheckableDataTableRowWrapper>());			
		}
		
		if (enderecoJurisdicionadoTMP == null){
			enderecoJurisdicionado.setJurisdicionado(jurisdicionado);
			listaEndereco.add(new CheckableDataTableRowWrapper(enderecoJurisdicionado));
		}
		setRenderizaBotaoAlterarEndereco(false);
		setEnderecoJurisdicionado(new EnderecoJurisdicionado());
	}
	
	
	/**
	 * Recupera o endereço de acordo com o CEP digitado
	 */
	private void recuperarEnderecoCep(){
		try {
			EnderecoView end = getEnderecoDestinatarioService().recuperarEnderecoView(Integer.parseInt(enderecoJurisdicionado.getCep()));
			if( end != null ){
				enderecoJurisdicionado.setCep(end.cep.toString());
				enderecoJurisdicionado.setLogradouro(end.logradouro);
				enderecoJurisdicionado.setMunicipio(end.cidade);
				enderecoJurisdicionado.setBairro(end.bairro);
				enderecoJurisdicionado.setUF(end.UF);
			}
			
		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar UF");
		}
	}
	
	
	private void verificaSeExisteJurisdicionado(){
		IdentificacaoPessoa ip = null;
		cpfJurisdicionado = retirarCaracteresNaoNumeros(cpfJurisdicionado);
			
		if (cpfJurisdicionado.trim().length() > 0){
			if (!NumberUtils.validacaoCPF(cpfJurisdicionado)){
				setIsCPFErrado(true);
				reportarErro("CPF incorreto.");
				return;
			}else{
				setIsCPFErrado(false);
			}
			
			try {
				ip = getIdentificacaoPessoaService().recuperarPrimeiroCadastro(cpfJurisdicionado, "CPF");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (ip != null){
				existeCadastro = true;
				jurisdicionadoJaCadastrado = ip.getJurisdicionado();
			}
			
			inputVerificaCadastroPessoa.setValue(existeCadastro);
		}else{
			reportarAviso("O CPF deve ser preenchido.");
			return;
		}
	}
	
	
	/**
	 * Mostra nos campos os dados selecionados na tabela para que o cliente
	 * possa alterar os dados de endereço;
	 */
	private void alterarEndereco(){
		enderecoJurisdicionadoTMP = new EnderecoJurisdicionado();
		enderecoJurisdicionadoTMP = (EnderecoJurisdicionado)((CheckableDataTableRowWrapper)tabelaEndereco.getRowData()).getWrappedObject();
		indiceEnderecoJurisdicionado = tabelaEndereco.getRowIndex();
		enderecoJurisdicionado.setId(enderecoJurisdicionadoTMP.getId());
		enderecoJurisdicionado.setJurisdicionado(enderecoJurisdicionadoTMP.getJurisdicionado());
		if (enderecoJurisdicionadoTMP.getCep() != null)
			enderecoJurisdicionado.setCep(enderecoJurisdicionadoTMP.getCep());
		if (enderecoJurisdicionadoTMP.getLogradouro() != null)
			enderecoJurisdicionado.setLogradouro(enderecoJurisdicionadoTMP.getLogradouro());
		if (enderecoJurisdicionadoTMP.getNumero() != null)
			enderecoJurisdicionado.setNumero(enderecoJurisdicionadoTMP.getNumero());
		if (enderecoJurisdicionadoTMP.getBairro() != null)
			enderecoJurisdicionado.setBairro(enderecoJurisdicionadoTMP.getBairro());
		if (enderecoJurisdicionadoTMP.getComplemento() != null)
			enderecoJurisdicionado.setComplemento(enderecoJurisdicionadoTMP.getComplemento());
		if (enderecoJurisdicionadoTMP.getMunicipio()!= null)
			enderecoJurisdicionado.setMunicipio(enderecoJurisdicionadoTMP.getMunicipio());
		if (enderecoJurisdicionadoTMP.getUF() != null)
			enderecoJurisdicionado.setUF(enderecoJurisdicionadoTMP.getUF());
	}
	
	
	/**
	 * Exclui o endereço da tabela, porém para ser excluído da tabela
	 * será necessário o cliente salvar novamente o destinatário
	 */
	private void excluirEndereco(){
		if ( listaEndereco == null || listaEndereco.size()==0 ) {
			reportarAviso("Selecione pelo menos um endereço.");
			return;
		}

		CheckableDataTableRowWrapper chk = (CheckableDataTableRowWrapper) tabelaEndereco.getRowData();
		EnderecoJurisdicionado endereco = (EnderecoJurisdicionado) chk.getWrappedObject();
		
		listaEndereco.remove( (CheckableDataTableRowWrapper)tabelaEndereco.getRowData() );
		
		// armazena para exclusão quando salvar.
		enderecosParaExclusao.clear();
		enderecosParaExclusao.add(endereco);
		setAtributo(ENDERECOS_PARA_EXCLUSAO, enderecosParaExclusao);

	}
	
	/**
	 * Adiciona o Telefone na tabela. O Telefone somente será salvo quando
	 * o cliente salvar o destinatário.
	 */
	@SuppressWarnings("unchecked")
	private void adicionarTelefone(){
		
		if (telefoneJurisdicionado.getDDD() == null || stringEmpty(telefoneJusHifen)){
			reportarAviso("O DDD e o número do telefone devem ser preenchidos.");
			return;
		}
		
		if( listaTelefone != null && listaTelefone.size() > 0 ){
		
			List<TelefoneJurisdicionado> lista = getOriginalListForCheckableDataTableRowWrapper(listaTelefone);
			Boolean inicializaListaTroca = false;
			
			for( int i = 0 ; i < lista.size() ; i++ ){
				
//				TelefoneJurisdicionado tel = new TelefoneJurisdicionado();
				TelefoneJurisdicionado tel = lista.get(i);
				
				if(valoresIguais(tel.getDDD(), telefoneJurisdicionado.getDDD()) &&
						valoresIguais(tel.getNumero(), retirarHifenTelFax(telefoneJusHifen))){
					reportarAviso("O telefone já está adicionado.");
					return;
				}	
				
				//reinicializa a lista um única vez para que o registro alterado
				//seja inserido na exata posição do regitro original
				if (!inicializaListaTroca && telefoneJurisdicionadoTMP != null){
					setListaTelefone(new LinkedList<CheckableDataTableRowWrapper>());
					inicializaListaTroca = true;					
				}
				
				if (telefoneJurisdicionadoTMP != null){
					if (i == indiceTelefoneJurisdicionado){
						telefoneJurisdicionado.setJurisdicionado(jurisdicionado);
						
						if (telefoneJusHifen != null && telefoneJusHifen.trim().length() > 0){
							telefoneJurisdicionado.setNumero(retirarHifenTelFax(telefoneJusHifen));
						}
						
						listaTelefone.add(new CheckableDataTableRowWrapper(telefoneJurisdicionado));
					}else{
						listaTelefone.add(new CheckableDataTableRowWrapper(tel));
					}
				}
			}
			
			
		}else{
			setListaTelefone(new LinkedList<CheckableDataTableRowWrapper>());			
		}
		
		if (telefoneJurisdicionadoTMP == null){
			telefoneJurisdicionado.setJurisdicionado(jurisdicionado);
			
			if (telefoneJusHifen != null && telefoneJusHifen.trim().length() > 0){
				telefoneJurisdicionado.setNumero(retirarHifenTelFax(telefoneJusHifen));
			}
			
			listaTelefone.add(new CheckableDataTableRowWrapper(telefoneJurisdicionado));
		}
		setRenderizaBotaoAlterarTelefone(false);
		setTelefoneJurisdicionado(new TelefoneJurisdicionado());
		setTelefoneJusHifen("");
	}
	
	
	/**
	 * Retira o hifens dos campos de telefone e fax
	 */
	private String retirarHifenTelFax(String telFax){
		if (telFax.contains("-")){
			return telFax.replaceAll("-", "");
		}
		return telFax;
	}
	
	
	/**
	 * Mostra nos campos os dados selecionados na tabela para que o cliente
	 * possa alterar os dados de contato;
	 */
	private void alterarTelefone(){
		telefoneJurisdicionadoTMP = new TelefoneJurisdicionado();
		telefoneJurisdicionadoTMP = (TelefoneJurisdicionado)((CheckableDataTableRowWrapper)tabelaTelefone.getRowData()).getWrappedObject();
		indiceTelefoneJurisdicionado = tabelaTelefone.getRowIndex();
		telefoneJurisdicionado.setId(telefoneJurisdicionadoTMP.getId());
		telefoneJurisdicionado.setJurisdicionado(telefoneJurisdicionadoTMP.getJurisdicionado());

		if (telefoneJurisdicionadoTMP.getTipoTelefone() != null)
			telefoneJurisdicionado.setTipoTelefone(telefoneJurisdicionadoTMP.getTipoTelefone());
		if (telefoneJurisdicionadoTMP.getDDD() != null)
			telefoneJurisdicionado.setDDD(telefoneJurisdicionadoTMP.getDDD());
		if (telefoneJurisdicionadoTMP.getNumero() != null){
			telefoneJurisdicionado.setNumero(telefoneJurisdicionadoTMP.getNumero());
			setTelefoneJusHifen(telefoneJurisdicionadoTMP.getNumero().toString());
		}
	}
	
	
	/**
	 * Exclui o contato da tabela, porém para ser excluído da tabela
	 * será necessário o cliente salvar novamente o destinatário
	 */
	private void excluirTelefone(){
		if ( listaTelefone == null || listaTelefone.size()==0 ) {
			reportarAviso("Selecione pelo menos um telefone.");
			return;
		}else{
			//	List<CheckableDataTableRowWrapper> selecionados = retornarItensSelecionados(listaEndereco);
			listaTelefone.remove(((CheckableDataTableRowWrapper)tabelaTelefone.getRowData()));
		}
	}
	
	
	public String getColunaTruncadaObs(){
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaJurisdicionado.getRowData();
		Jurisdicionado juris = (Jurisdicionado) chkDataTable.getWrappedObject();
		if (juris.getObservacao() == null) {
			return "";
		}
		if (juris.getObservacao().length() > 100){
			return juris.getObservacao().substring(0, 100)+"...";
		} else {
			return juris.getObservacao();
		}
	}
	
	public Boolean getDesabilitaHintObs(){
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaJurisdicionado.getRowData();
		Jurisdicionado juris = (Jurisdicionado) chkDataTable.getWrappedObject();
		if (juris.getObservacao() == null) {
			return true;
		}
		if (juris.getObservacao().length() > 100){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Salvar ou alterar na base o dados do jurisdicionado.
	 */
	@SuppressWarnings("unchecked")
	private void salvarJurisdicionado(){
		
		if( stringEmpty(tipoPapelJurisdicionado) && !desabilitaRadioPapel ){
			reportarAviso("O papel deve ser selecionado.");
			return;
		}
		
		if( stringEmpty(jurisdicionado.getNome())){
			reportarAviso("O nome deve ser preenchido.");
			return;
		}
		
		if(cpfJurisdicionado == null || cpfJurisdicionado.trim().length() == 0){
			reportarAviso("O CPF deve ser preenchido.");
			return;
		}
		
		if (isCPFErrado){
			reportarAviso("CPF incorreto.");
			return;
		}
		
		if (!tipoPapelJurisdicionado.equals("P")) {
			if (oabJurisdicionado.trim().length() == 0){
				reportarAviso("O campo OAB deve ser preenchido.");
				return;
			}
		}
		
		if (dataValidadeCadastro == null){
			reportarAviso("A data de validade deve ser preenchida.");
			return;
		}else if (dataValidadeCadastro.before(new Date())){
			reportarAviso("A data de validade do cadastro deve ser igual ou maior que a data de hoje!");
			return;
		}
		
		
		if (!stringEmpty(jurisdicionado.getEmail())){
			if (!jurisdicionado.getEmail().contains("@")){
				reportarAviso("Endereço de e-mail inválido.");
				return;
			}
		}
		
		if (listaEndereco == null || listaEndereco.size() == 0 ){
			reportarAviso("A pessoa deve possuir ao menos um endereço adicionado.");
			return;
		}
		
		if (listaTelefone == null || listaTelefone.size() == 0 ){
			reportarAviso("A pessoa deve possuir ao menos um telefone adicionado.");
			return;
		}
		
		if (oabJurisdicionado != null && !oabJurisdicionado.trim().equals("")) {
			if (ufOAB == null || ufOAB.trim().equals("")){
				reportarAviso("OAB não possui uma unidade federativa.");
				return;
			}
		}
		
		jurisdicionado.setDataValidadeCadastro(dataValidadeCadastro);
		jurisdicionado.setTipoPessoa("PF");
		jurisdicionado.setObservacao(observacao);
		
		boolean houveAlteracao = false;
		boolean insercaoDeNovosIdentificacadores = false;
		boolean isInsereCPF = true;
		boolean isInsereOAB = true;
		boolean isInsereRG = true;
		List<IdentificacaoPessoa> listaIdentificacaoJ = new LinkedList<IdentificacaoPessoa>();
		List<IdentificacaoPessoa> identificadoresParaExclusao = new LinkedList<IdentificacaoPessoa>();
		
		
		// se a pessoa possuir identificadores será feito o laço
		// para saber se existe alguma alteração. Os identificadores são CPF, RG, OAB
		if (jurisdicionado.getIdentificadoresJurisdicionado() != null && 
				jurisdicionado.getIdentificadoresJurisdicionado().size() > 0){
			for (IdentificacaoPessoa ip : jurisdicionado.getIdentificadoresJurisdicionado()){
				if (ip.getTipoIdentificacao().getSiglaTipoIdentificacao().equals("CPF")){
					listaIdentificacaoJ.add(ip);
					isInsereCPF = false;
				}else if (ip.getTipoIdentificacao().getSiglaTipoIdentificacao().equals("RG")){
					isInsereRG = false;
					//verifica se o valor foi alterado pelo usuário na tela
					if (!(ip.getDescricaoIdentificacao().equalsIgnoreCase(rgJurisdicionado))){
						houveAlteracao = true;
						// se rgjurisdicionado for null quer dizer que o usuário limpou o campo na tela
						// e deseja excluir este tipo identificação do jurisdicionado
						// logo não será salvo na lista (listaIdentificacaoJ) e no momento de salvar o jurisdicionado
						// a identificação será excluída da tabela
						if (rgJurisdicionado.trim().length() > 0){
							IdentificacaoPessoa ide = new IdentificacaoPessoa();
							ide = ip;
							ide.setDescricaoIdentificacao(rgJurisdicionado);
							listaIdentificacaoJ.add(ide);
						}
					}
				}else if (ip.getTipoIdentificacao().getSiglaTipoIdentificacao().equals("OAB")){
					isInsereOAB = false;
					if (!(ip.getDescricaoIdentificacao().equalsIgnoreCase(oabJurisdicionado)) || 
							!(ip.getSiglaUfOrgaoExpedidor().equalsIgnoreCase(ufOAB))){
						houveAlteracao = true;
						// se oabJurisdicionado for null quer dizer que o usuário limpou o campo na tela
						// e deseja excluir este tipo identificação do jurisdicionado
						// logo não será salvo na lista e no momento de salvar o jurisdicionado
						// a identificação será excluída da tabela
						if (oabJurisdicionado.trim().length() > 0){
							IdentificacaoPessoa ide = new IdentificacaoPessoa();
							ide = ip;
							ide.setDescricaoIdentificacao(oabJurisdicionado);
							ide.setSiglaUfOrgaoExpedidor(ufOAB);
							listaIdentificacaoJ.add(ide);
						}
					}else{
						listaIdentificacaoJ.add(ip);
					}
				}
			}
			
			// caso a pessoa possua outro identificadores e não possua o CPF
			// o CPF deverá ser gravado no banco
			if (isInsereCPF){
				IdentificacaoPessoa ip = new IdentificacaoPessoa();
				ip.setTipoIdentificacao(buscaTipoIdentificacao("CPF"));
				ip.setDescricaoIdentificacao(cpfJurisdicionado.toString());
				ip.setJurisdicionado(jurisdicionado);
				listaIdentificacaoJ.add(ip);
				houveAlteracao = true;
			}
			
			// caso a pessoa não possua RG mas possui outros identificadores
			// e quer inserir o RG 
			if (isInsereRG && rgJurisdicionado.trim().length() > 0){
				IdentificacaoPessoa ip = new IdentificacaoPessoa();
				ip.setTipoIdentificacao(buscaTipoIdentificacao("RG"));
				ip.setDescricaoIdentificacao(rgJurisdicionado.toString());
				ip.setJurisdicionado(jurisdicionado);
				listaIdentificacaoJ.add(ip);
				houveAlteracao = true;
			}
			
			// caso a pessoa não possua OAB mas possui outros identificadores
			// e quer inserir a OAB
			if (isInsereOAB && oabJurisdicionado.trim().length() > 0){
				IdentificacaoPessoa ip = new IdentificacaoPessoa();
				ip.setTipoIdentificacao(buscaTipoIdentificacao("OAB"));
				ip.setDescricaoIdentificacao(oabJurisdicionado.toString());
				ip.setSiglaUfOrgaoExpedidor(ufOAB);
				ip.setJurisdicionado(jurisdicionado);
				listaIdentificacaoJ.add(ip);
				houveAlteracao = true;
			}
		}else{
			insercaoDeNovosIdentificacadores = true;
			if (cpfJurisdicionado != null && cpfJurisdicionado.trim().length() > 0){
				IdentificacaoPessoa ip = new IdentificacaoPessoa();
				ip.setTipoIdentificacao(buscaTipoIdentificacao("CPF"));
				ip.setDescricaoIdentificacao(cpfJurisdicionado.toString());
				ip.setJurisdicionado(jurisdicionado);
				listaIdentificacaoJ.add(ip);
			}
			if (rgJurisdicionado != null && rgJurisdicionado.trim().length() > 0){
				IdentificacaoPessoa ip = new IdentificacaoPessoa();
				ip.setTipoIdentificacao(buscaTipoIdentificacao("RG"));
				ip.setDescricaoIdentificacao(rgJurisdicionado.toString());
				ip.setJurisdicionado(jurisdicionado);
				listaIdentificacaoJ.add(ip);
			}
			if (oabJurisdicionado != null && oabJurisdicionado.trim().length() > 0){
				IdentificacaoPessoa ip = new IdentificacaoPessoa();
				ip.setTipoIdentificacao(buscaTipoIdentificacao("OAB"));
				ip.setDescricaoIdentificacao(oabJurisdicionado.toString());
				ip.setSiglaUfOrgaoExpedidor(ufOAB);
				ip.setJurisdicionado(jurisdicionado);
				listaIdentificacaoJ.add(ip);
			}
		}
		
		//se o usuário alterarou qualquer valor nos campos de RG ou OAB e UFOAB
		//os dados serão salvos na tabela (update ou delete)
		if ((houveAlteracao) && (listaIdentificacaoJ.size() > 0)){
			
			for (IdentificacaoPessoa identificacaoPessoa : jurisdicionado.getIdentificadoresJurisdicionado()) {
			 if  ( listaIdentificacaoJ.indexOf( identificacaoPessoa )  == -1 )
				 identificadoresParaExclusao.add(identificacaoPessoa);
			}
			jurisdicionado.setIdentificadoresJurisdicionado(listaIdentificacaoJ);
		}
		
		if (insercaoDeNovosIdentificacadores){
			jurisdicionado.setIdentificadoresJurisdicionado(listaIdentificacaoJ);
		}

		if (jurisdicionado.getEnderecosJurisdicionado() == null)
			jurisdicionado.setEnderecosJurisdicionado(new LinkedList<EnderecoJurisdicionado>());
		
		jurisdicionado.getEnderecosJurisdicionado().clear();
		List<EnderecoJurisdicionado> enderecos = getOriginalListForCheckableDataTableRowWrapper(listaEndereco);
		
		if (enderecos != null && enderecos.size() > 0){
			jurisdicionado.getEnderecosJurisdicionado().addAll(enderecos);
		}
		
		if (jurisdicionado.getTelefonesJurisdicionado() == null)
			jurisdicionado.setTelefonesJurisdicionado((new LinkedList<TelefoneJurisdicionado>()));
		
		jurisdicionado.getTelefonesJurisdicionado().clear();
		List<TelefoneJurisdicionado> telefonesJurisdicionado = getOriginalListForCheckableDataTableRowWrapper(listaTelefone);
		
		if( telefonesJurisdicionado != null && telefonesJurisdicionado.size() > 0 ){
			jurisdicionado.getTelefonesJurisdicionado().addAll(telefonesJurisdicionado);
		}
		
		if (jurisdicionado.getEnderecosJurisdicionado().size() == 0){
			reportarAviso("A pessoa deve possuir ao menos um endereço adicionado.");
			return;
		}

		jurisdicionado.setEntidadeGovernamental(false);
		//insere um novo jurisdicionado
		if( jurisdicionado.getId() == null ){
			
			jurisdicionado.setAtivo(true);
			PapelJurisdicionado papel = new PapelJurisdicionado();
			papel.setPadrao(true);
			papel.setJurisdicionado(jurisdicionado);
			
			
			if (tipoPapelJurisdicionado.equals("E")){
				papel.setTipoJurisdicionado(buscaPapelJurisdicionado("EST"));
			}else if (tipoPapelJurisdicionado.equals("P")){
				papel.setTipoJurisdicionado(buscaPapelJurisdicionado("PREPO"));
			}else{
				papel.setTipoJurisdicionado(buscaPapelJurisdicionado("ADV"));
			}
			
			List<PapelJurisdicionado> papeis = new ArrayList<PapelJurisdicionado>();
			papeis.add(papel);
			jurisdicionado.setPapeisJurisdicionado(papeis);
			try {
				getJurisdicionadoService().limparSessao();
				getJurisdicionadoService().incluir(jurisdicionado);
			}catch (Exception e) {
				reportarErro("Erro ao salvar a pessoa.");
				return;
			}
			
		}else{
			//Se o usuário não possuir os dois papéis
			if (!desabilitaRadioPapel){
				 Boolean existeAdv = false;
				 Boolean existeEst = false;
				 Boolean existePre = false;
				 //verifica se o jurisdicionado possui o papel advogado
				 existeAdv = verificaTipoJurisdicionado("ADV");
				 //verifica se o jurisdicionado possui o papel estagiário
				 existeEst = verificaTipoJurisdicionado("EST");
				 //verifica se o jurisdicionado possui o papel preposto
				 existePre = verificaTipoJurisdicionado("PREPO");
				 
				 if (existeAdv && tipoPapelJurisdicionado.equals("E")){
					try {
						inserePapelDoJurisdicionado();
					} catch (ServiceException e) {
						e.printStackTrace();
						return;
					}
				 }
				 
				 if (existeAdv && tipoPapelJurisdicionado.equals("P")){
					try {
						inserePapelDoJurisdicionado();
					} catch (ServiceException e) {
						e.printStackTrace();
						return;
					}
				 }
				 
				 if (existeEst && tipoPapelJurisdicionado.equals("A")){
					 try {
						inserePapelDoJurisdicionado();
					} catch (ServiceException e) {
						e.printStackTrace();
						return;
					}
				 }
				 
				 if (existeEst && tipoPapelJurisdicionado.equals("P")){
					 try {
						inserePapelDoJurisdicionado();
					} catch (ServiceException e) {
						e.printStackTrace();
						return;
					}
				 }
				 
				 if (existePre && tipoPapelJurisdicionado.equals("A")){
					 try {
						inserePapelDoJurisdicionado();
					} catch (ServiceException e) {
						e.printStackTrace();
						return;
					}
				 }
				 
				 if (existePre && tipoPapelJurisdicionado.equals("E")){
					 try {
						inserePapelDoJurisdicionado();
					} catch (ServiceException e) {
						e.printStackTrace();
						return;
					}
				 }
				 
				 if (!existeAdv && !existeEst && !existePre){
					 try {
						inserePapelDoJurisdicionado();
					} catch (ServiceException e) {
						e.printStackTrace();
						return;
					}
				 }

			}
			
			try {
				getJurisdicionadoService().limparSessao();
				getJurisdicionadoService().alterarJurusidicionado(enderecosParaExclusao
																, identificadoresParaExclusao
																, jurisdicionado);
//				getJurisdicionadoService().alterar(jurisdicionado);
			} catch (ServiceException e) {
				reportarErro("Erro ao alterar os dados da pessoa.");
				return;
			}
		}

		novoJurisdicionado(); 
		reportarInformacao("Operação realizada com sucesso.");
	
	}
	
	
	private Boolean verificaTipoJurisdicionado(String tipo){
		for (PapelJurisdicionado papel : jurisdicionado.getPapeisJurisdicionado()){
			if (papel.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals(tipo)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * cria um novo papel para o jurisdicionado
	 */
	private void inserePapelDoJurisdicionado() throws ServiceException{
		PapelJurisdicionado papel = new PapelJurisdicionado();
		papel.setJurisdicionado(jurisdicionado);
		papel.setPadrao(true);
		
		
		if (tipoPapelJurisdicionado.equals("E")){ // estagiário
			papel.setTipoJurisdicionado(buscaPapelJurisdicionado("EST"));
		}else if (tipoPapelJurisdicionado.equals("P")){ // preposto
			papel.setTipoJurisdicionado(buscaPapelJurisdicionado("PREPO"));
		}else{ // advogado
			papel.setTipoJurisdicionado(buscaPapelJurisdicionado("ADV"));
		}
		jurisdicionado.getPapeisJurisdicionado().add(papel);
	}
	
	private TipoJurisdicionado buscaPapelJurisdicionado(String papel){
		try {
			return getTipoJurisdicionadoService().buscaTipoPelaSigla(papel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private TipoIdentificacao buscaTipoIdentificacao(String tipo){
		TipoIdentificacao tipoId = null;
		try {
			tipoId = getTipoIdentificacaoService().pesquisaPelaSigla(tipo);
		} catch (Exception e) {
			reportarErro("Erro ao localizar o tipo de identificacao.");
		}
		return tipoId;
	}
	
	/**
	 * Exibe na tela as informações do destinatário para que o cliente
	 * possa alterar. As informações somente serão atualziadas na tabela
	 * quando o cliente clicar no botão alterar
	 */
	private void alterarJurisdicionado(){
		//Jurisdicionado jurisdicionadoEdicao = new Jurisdicionado();
		try {
			if (jurisdicionadoJaCadastrado != null){
				jurisdicionado = jurisdicionadoJaCadastrado;
			}else{
				Jurisdicionado jurisdicionadoEdicao = (Jurisdicionado)((CheckableDataTableRowWrapper)tabelaJurisdicionado.getRowData()).getWrappedObject();				
				jurisdicionado = getJurisdicionadoService().recuperarPorId(jurisdicionadoEdicao.getId());
			}
			
			if (jurisdicionado.getEnderecosJurisdicionado() != null && jurisdicionado.getEnderecosJurisdicionado().size() > 0)
				setListaEndereco(getCheckableDataTableRowWrapperList(jurisdicionado.getEnderecosJurisdicionado()));
			
			if( jurisdicionado.getTelefonesJurisdicionado() != null && jurisdicionado.getTelefonesJurisdicionado().size() > 0 )
				setListaTelefone(getCheckableDataTableRowWrapperList(jurisdicionado.getTelefonesJurisdicionado()));
			
			if (jurisdicionado.getIdentificadoresJurisdicionado() != null && jurisdicionado.getIdentificadoresJurisdicionado().size() > 0){
				for (IdentificacaoPessoa ip : jurisdicionado.getIdentificadoresJurisdicionado()){
					if (ip.getTipoIdentificacao().getSiglaTipoIdentificacao().equals("CPF")){
						setCpfJurisdicionado(ip.getDescricaoIdentificacao());
					}
					if (ip.getTipoIdentificacao().getSiglaTipoIdentificacao().equals("RG")){
						setRgJurisdicionado(ip.getDescricaoIdentificacao());
					}
					if (ip.getTipoIdentificacao().getSiglaTipoIdentificacao().equals("OAB")){
						setOabJurisdicionado(ip.getDescricaoIdentificacao());
						if (ip.getSiglaUfOrgaoExpedidor() != null)
							setUfOAB(ip.getSiglaUfOrgaoExpedidor());
					}
				}
			}
			setObservacao(jurisdicionado.getObservacao());
			Boolean papelAdv = false;
			Boolean papelEst = false;
			Boolean papelPre = false;
			if (jurisdicionado.getPapeisJurisdicionado() != null && jurisdicionado.getPapeisJurisdicionado().size() > 0){
				for (PapelJurisdicionado papel : jurisdicionado.getPapeisJurisdicionado()){
					if (papel.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equalsIgnoreCase("ADV")){
						papelAdv = true;
					}
					if (papel.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equalsIgnoreCase("EST")){
						papelEst = true;
					}
					if (papel.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equalsIgnoreCase("PREPO")){
						papelPre = true;
					}
				}
			}
			
			//caso o destinatário possua tanto o papel estagiário como advogado
			//o radio será será desabilitado.
			if (papelAdv && papelEst){
				setTipoPapelJurisdicionado("A");
				setDesabilitaRadioPapel(true);
			}
			//se o destinatário possuir apenas o papel advogado, o radio será
			//setado como advogado e será desabilitado o radio, já que se o destinatário
			// possui o papel advogado não será necessário ele criar qualquer outro papel
			else if (papelAdv){
				setTipoPapelJurisdicionado("A");
				setDesabilitaRadioPapel(true);
			}
			//se o destinatário possuir apenas o papel estagiário, o radio será
			//setado como estagiário e será e o radio continuará habilitado
			// caso o usuário deseje criar o papel advogado
			else if (papelEst){
				setTipoPapelJurisdicionado("E");
				setDesabilitaRadioPapel(false);
			}else if (papelPre){
				setTipoPapelJurisdicionado("P");
				setDesabilitaRadioPapel(false);
			}else {
				setDesabilitaRadioPapel(false);
			}
			
			//seta esta informação na tela
			if (jurisdicionado.getDataValidadeCadastro() != null){
				setDataValidadeCadastro(jurisdicionado.getDataValidadeCadastro());
			}
			//mostra a tela de cadastro
			setRenderizaTelaCadastro(true);
			//renderiza a tela de pesquisa
			setRenderizaTelaDePesquisa(false);
			setRenderizaTabelaJurisdicionado(false);
			
			//caso o jurisdicionado possuua já a informação de CPF
			// o sistema deve desabilitar o campo para que o usuário
			// não possa alterar esta informação
			if (jurisdicionado.getCpf().trim().length() != 0){
				setDesabilitaSeForAlteracao(true);
			}
			//mostrar o botão de alterar e desabilita o botão salvar
			setRenderizaBotaoAlterarJurisdicionado(true);
			
		} catch (ServiceException e) {
			reportarErro("Erro ao localizar a pessoa selecionada.");
		}
	}
	
	
	private void novoJurisdicionado(){
		limparTela();
		setRenderizaTelaCadastro(true);
		setRenderizaTelaDePesquisa(false);
		atualizaSessao();
	}
	
	/**
	 * monta a lista de UFs
	 * @return
	 */
	private List<SelectItem> montarListaUf(){
		List<SelectItem> lista = new LinkedList<SelectItem>();
		lista.add(new SelectItem("",""));
		try {
			List<String> ufs = getEnderecoDestinatarioService().pesquisarUF();
			if( ufs != null && ufs.size() > 0 ){
				for(String uf : ufs){
					lista.add(new SelectItem(uf,uf));
				}
			}
			
		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar UF.");
		}
		return lista; 
	}
	
	public List<SelectItem> carregarComboAtivoDestinatario(){
		List<SelectItem> listaDestAtivos = new ArrayList<SelectItem>();
		listaDestAtivos.add(new SelectItem(true, "Ativo"));
		listaDestAtivos.add(new SelectItem(false, "Inativo"));
		return listaDestAtivos;
	}
	
	public boolean stringEmpty(String valor){
		return valor != null && valor.trim().length() > 0?false:true;
	}
	
	
	@SuppressWarnings("unused")
	public boolean valoresIguais(Object obj, Object obj2){
		if( obj instanceof String &&  obj2 instanceof String ){
			String valor = (String) obj;
			String valor2 = (String) obj2;
			if( valor != null && valor.trim().length() > 0 &&  
					valor2 != null && valor2.trim().length() > 0){ 
					if(valor.equalsIgnoreCase(valor2) )
						return true;
					else
						return false;
			}else if( (valor == null || valor.trim().length() == 0) &&  
					(valor2 == null || valor2.trim().length() == 0) ){
				return true;
			}else{
				return false;
			}
		}else if( obj instanceof Integer &&  obj2 instanceof Integer ){
			Integer valor = (Integer) obj;
			Integer valor2 = (Integer) obj2;
			if( valor != null && valor2 != null){  
					if(valor.equals(valor2) ){
						return true;
					}else{
						return false;
					}
			}else if( valor == null && valor2 == null){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public List<SelectItem> carregarComboTipoTelefone(){
		List<SelectItem> listaTipoTelefone = new ArrayList<SelectItem>();
		listaTipoTelefone.add(new SelectItem(EnumTipoTelefone.R, EnumTipoTelefone.R.getDescricao()));
		listaTipoTelefone.add(new SelectItem(EnumTipoTelefone.C, EnumTipoTelefone.C.getDescricao()));
		listaTipoTelefone.add(new SelectItem(EnumTipoTelefone.F, EnumTipoTelefone.F.getDescricao()));
		return listaTipoTelefone;
	}
	
	
// ######################## GETs and  SETs ######################
	

	public Short getTipoDestino() {
		return tipoDestino;
	}

	public void setTipoDestino(Short tipoDestino) {
		this.tipoDestino = tipoDestino;
	}

	public List<CheckableDataTableRowWrapper> getListaJurisdicionado() {
		return listaJurisdicionado;
	}

	public void setListaJurisdicionado(
			List<CheckableDataTableRowWrapper> listaJurisdicionado) {
		this.listaJurisdicionado = listaJurisdicionado;
	}

	public HtmlDataTable getTabelaJurisdicionado() {
		return tabelaJurisdicionado;
	}

	public void setTabelaJurisdicionado(HtmlDataTable tabelaJurisdicionado) {
		this.tabelaJurisdicionado = tabelaJurisdicionado;
	}

	public Boolean getRenderizaTelaCadastro() {
		return renderizaTelaCadastro;
	}

	public void setRenderizaTelaCadastro(Boolean renderizaTelaCadastro) {
		this.renderizaTelaCadastro = renderizaTelaCadastro;
	}

	public Long getIdJurisdicionado() {
		return idJurisdicionado;
	}

	public void setIdJurisdicionado(Long idJurisdicionado) {
		this.idJurisdicionado = idJurisdicionado;
	}

	public String getNomeJurisdicionado() {
		return nomeJurisdicionado;
	}

	public void setNomeJurisdicionado(String nomeJurisdicionado) {
		this.nomeJurisdicionado = nomeJurisdicionado;
	}

	public EnderecoJurisdicionado getEnderecoJurisdicionado() {
		return enderecoJurisdicionado;
	}

	public void setEnderecoJurisdicionado(EnderecoJurisdicionado enderecoJurisdicionado) {
		this.enderecoJurisdicionado = enderecoJurisdicionado;
	}

	public List<SelectItem> getItensUf() {
		return itensUf;
	}

	public void setItensUf(List<SelectItem> itensUf) {
		this.itensUf = itensUf;
	}

	public List<CheckableDataTableRowWrapper> getListaEndereco() {
		return listaEndereco;
	}

	public void setListaEndereco(List<CheckableDataTableRowWrapper> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}

	public Jurisdicionado getJurisdicionado() {
		return jurisdicionado;
	}

	public void setJurisdicionado(Jurisdicionado jurisdicionado) {
		this.jurisdicionado = jurisdicionado;
	}

	public HtmlDataTable getTabelaEndereco() {
		return tabelaEndereco;
	}

	public void setTabelaEndereco(HtmlDataTable tabelaEndereco) {
		this.tabelaEndereco = tabelaEndereco;
	}

	public TelefoneJurisdicionado getTelefoneJurisdicionado() {
		return telefoneJurisdicionado;
	}

	public void setTelefoneJurisdicionado(TelefoneJurisdicionado  telefoneJurisdicionado) {
		this. telefoneJurisdicionado =  telefoneJurisdicionado;
	}

	public List<CheckableDataTableRowWrapper> getListaTelefone() {
		return listaTelefone;
	}

	public void setListaTelefone(
			List<CheckableDataTableRowWrapper> listaTelefone) {
		this.listaTelefone = listaTelefone;
	}

	public HtmlDataTable getTabelaTelefone() {
		return tabelaTelefone;
	}

	public void setTabelaTelefone(HtmlDataTable tabelaTelefone) {
		this.tabelaTelefone = tabelaTelefone;
	}

	public Boolean getRenderizaTelaDePesquisa() {
		return renderizaTelaDePesquisa;
	}

	public void setRenderizaTelaDePesquisa(Boolean renderizaTelaDePesquisa) {
		this.renderizaTelaDePesquisa = renderizaTelaDePesquisa;
	}

	public String getTelefoneJusHifen() {
		return telefoneJusHifen;
	}

	public void setTelefoneJusHifen(String telefoneJusHifen) {
		this.telefoneJusHifen = telefoneJusHifen;
	}

	public Boolean getRenderizaBotaoAlterarEndereco() {
		return renderizaBotaoAlterarEndereco;
	}

	public void setRenderizaBotaoAlterarEndereco(
			Boolean renderizaBotaoAlterarEndereco) {
		this.renderizaBotaoAlterarEndereco = renderizaBotaoAlterarEndereco;
	}

	public EnderecoJurisdicionado getEnderecoJurisdicionadoTMP() {
		return enderecoJurisdicionadoTMP;
	}

	public void setEnderecoJurisdicionadoTMP(
			EnderecoJurisdicionado enderecoJurisdicionadoTMP) {
		this.enderecoJurisdicionadoTMP = enderecoJurisdicionadoTMP;
	}

	public TelefoneJurisdicionado getTelefoneJurisdicionadoTMP() {
		return telefoneJurisdicionadoTMP;
	}

	public void setTelefoneJurisdicionadoTMP(TelefoneJurisdicionado telefoneJurisdicionadoTMP) {
		this.telefoneJurisdicionadoTMP = telefoneJurisdicionadoTMP;
	}

	public Boolean getRenderizaBotaoAlterarTelefone() {
		return renderizaBotaoAlterarTelefone;
	}

	public void setRenderizaBotaoAlterarTelefone(Boolean renderizaBotaoAlterarTelefone) {
		this.renderizaBotaoAlterarTelefone = renderizaBotaoAlterarTelefone;
	}

	public Integer getIndiceEnderecoJurisdicionado() {
		return indiceEnderecoJurisdicionado;
	}

	public void setIndiceEnderecoJurisdicionado(Integer indiceEnderecoJurisdicionado) {
		this.indiceEnderecoJurisdicionado = indiceEnderecoJurisdicionado;
	}

	public Integer getIndiceTelefoneJurisdicionado() {
		return indiceTelefoneJurisdicionado;
	}

	public void setIndiceTelefoneJurisdicionado(Integer indiceTelefoneJurisdicionado) {
		this.indiceTelefoneJurisdicionado = indiceTelefoneJurisdicionado;
	}

	public Boolean getRenderizaTabelaJurisdicionado() {
		return renderizaTabelaJurisdicionado;
	}

	public void setRenderizaTabelaJurisdicionado(Boolean renderizaTabelaJurisdicionado) {
		this.renderizaTabelaJurisdicionado = renderizaTabelaJurisdicionado;
	}

	public List<SelectItem> getItensTipoTelefone() {
		return itensTipoTelefone;
	}

	public void setItensTipoTelefone(List<SelectItem> itensTipoTelefone) {
		this.itensTipoTelefone = itensTipoTelefone;
	}

	public Date getDataValidadeCadastro() {
		return dataValidadeCadastro;
	}

	public void setDataValidadeCadastro(Date dataValidadeCadastro) {
		this.dataValidadeCadastro = dataValidadeCadastro;
	}

	public List<Jurisdicionado> getListaJurisdicionadoCarregada() {
		return listaJurisdicionadoCarregada;
	}

	public void setListaJurisdicionadoCarregada(List<Jurisdicionado> listaJurisdicionadoCarregada) {
		this.listaJurisdicionadoCarregada = listaJurisdicionadoCarregada;
	}

	public String getOabJurisdicionado() {
		return oabJurisdicionado;
	}

	public void setOabJurisdicionado(String oabJurisdicionado) {
		this.oabJurisdicionado = oabJurisdicionado;
	}

	public String getCpfJurisdicionado() {
		return cpfJurisdicionado;
	}

	public void setCpfJurisdicionado(String cpfJurisdicionado) {
		this.cpfJurisdicionado = cpfJurisdicionado;
	}

	public String getIdUf() {
		return idUf;
	}

	public void setIdUf(String idUf) {
		this.idUf = idUf;
	}

	public String getTipoPapelJurisdicionado() {
		return tipoPapelJurisdicionado;
	}

	public void setTipoPapelJurisdicionado(String tipoPapelJurisdicionado) {
		this.tipoPapelJurisdicionado = tipoPapelJurisdicionado;
	}

	public String getUfOAB() {
		return ufOAB;
	}

	public void setUfOAB(String ufOAB) {
		this.ufOAB = ufOAB;
	}

	public List<SelectItem> getItensUfOAB() {
		return itensUfOAB;
	}

	public void setItensUfOAB(List<SelectItem> itensUfOAB) {
		this.itensUfOAB = itensUfOAB;
	}

	public String getRgJurisdicionado() {
		return rgJurisdicionado;
	}

	public void setRgJurisdicionado(String rgJurisdicionado) {
		this.rgJurisdicionado = rgJurisdicionado;
	}

	public Boolean getDesabilitaSeForAlteracao() {
		return desabilitaSeForAlteracao;
	}

	public void setDesabilitaSeForAlteracao(Boolean desabilitaSeForAlteracao) {
		this.desabilitaSeForAlteracao = desabilitaSeForAlteracao;
	}

	public Boolean getRenderizaBotaoAlterarJurisdicionado() {
		return renderizaBotaoAlterarJurisdicionado;
	}

	public void setRenderizaBotaoAlterarJurisdicionado(
			Boolean renderizaBotaoAlterarJurisdicionado) {
		this.renderizaBotaoAlterarJurisdicionado = renderizaBotaoAlterarJurisdicionado;
	}

	public Boolean getExisteCadastro() {
		return existeCadastro;
	}

	public void setExisteCadastro(Boolean existeCadastro) {
		this.existeCadastro = existeCadastro;
	}

	public Boolean getDesabilitaRadioPapel() {
		return desabilitaRadioPapel;
	}

	public void setDesabilitaRadioPapel(Boolean desabilitaRadioPapel) {
		this.desabilitaRadioPapel = desabilitaRadioPapel;
	}

	public HtmlInputHidden getInputVerificaCadastroPessoa() {
		return inputVerificaCadastroPessoa;
	}

	public void setInputVerificaCadastroPessoa(
			HtmlInputHidden inputVerificaCadastroPessoa) {
		this.inputVerificaCadastroPessoa = inputVerificaCadastroPessoa;
	}

	public Jurisdicionado getJurisdicionadoJaCadastrado() {
		return jurisdicionadoJaCadastrado;
	}

	public void setJurisdicionadoJaCadastrado(
			Jurisdicionado jurisdicionadoJaCadastrado) {
		this.jurisdicionadoJaCadastrado = jurisdicionadoJaCadastrado;
	}

	public Boolean getIsCPFErrado() {
		return isCPFErrado;
	}

	public void setIsCPFErrado(Boolean isCPFErrado) {
		this.isCPFErrado = isCPFErrado;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<EnderecoJurisdicionado> getEnderecosParaExclusao() {
		return enderecosParaExclusao;
	}

	public void setEnderecosParaExclusao(List<EnderecoJurisdicionado> enderecosParaExclusao) {
		this.enderecosParaExclusao = enderecosParaExclusao;
	}

}