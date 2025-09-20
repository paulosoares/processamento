package br.gov.stf.estf.assinatura.visao.jsf.beans.destinatario;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.entidade.localizacao.ContatoDestinatario;
import br.gov.stf.estf.entidade.localizacao.Destinatario;
import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.entidade.util.EnderecoView;
import br.gov.stf.estf.localizacao.model.util.DestinatarioOrgaoOrigemResult;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanManterDestinatariosBaixaExpedicao extends AssinadorBaseBean{
	
	private static final long serialVersionUID = -1896441891192039643L;
	
	private Integer indiceEnderecoDestinatario;
	private Integer indiceContatoDestinatario;
	private Long codigoOrgao;
	private Long codigoProcedencia;
	private Long codigoOrigem;
	private Long codigoDestinatario;
	private Short tipoDestino;
	private String nomeOrgao;
	private String nomeProcedencia;
	private String nomeOrigem;
	private String telefoneDestHifen;
	private String faxDestHifen;
	private String nomeDestinatario;
	private Boolean renderizaTelaCadastro;
	private Boolean renderizaTelaDePesquisa;
	private Boolean renderizaBotaoAlterarEndereco;
	private Boolean renderizaBotaoAlterarContato;
	private Boolean renderizaTabelaDestinatario;
	
	private EnderecoDestinatario enderecoDestinatario;
	private EnderecoDestinatario enderecoDestinatarioTMP;
	private Destinatario destinatario;
	private ContatoDestinatario contatoDestinatario;
	private ContatoDestinatario contatoDestinatarioTMP;
	
	private List<SelectItem> itensUf;
	private List<SelectItem> itensEnderecoAtivoDestinatario;
	private List<SelectItem> itensAtivoDestinatario;
	
	private List<CheckableDataTableRowWrapper> listaDestinatarios;
	private List<CheckableDataTableRowWrapper> listaEndereco;
	private List<CheckableDataTableRowWrapper> listaContatoDestinatario;
	
	private List<Procedencia> listaProcedenciaCarregada;
	private List<Origem> listaOrigemCarregada;
	private List<Destinatario> listaDestinatarioCarregada;
	
	private HtmlDataTable tabelaDestinatarios;
	private HtmlDataTable tabelaEndereco;
	private HtmlDataTable tabelaContatoDestinatario;;
	
	
// ######################### SESSION VARIABLE #################	
	
	private static final Object INDICE_END_DEST = new Object();
	private static final Object INDICE_CONT_DEST = new Object();
	private static final Object RENDERED_TELACADASTRO = new Object();
	private static final Object RENDERED_TELAPESQUISA = new Object();
	private static final Object RENDERED_TABELA_DEST = new Object();
	private static final Object RENDERED_BOTAO_ALTERAR_END = new Object();
	private static final Object RENDERED_BOTAO_ALTERAR_CONT = new Object();
	private static final Object CODIGO_ORGAO = new Object();
	private static final Object CODIGO_PROCEDENCIA = new Object();
	private static final Object CODIGO_ORIGEM = new Object();
	private static final Object CODIGO_DESTINATARIO = new Object();
	private static final Object TELEFONE_HIFEN = new Object();
	private static final Object FAX_HIFEN = new Object();
	private static final Object ITENS_UF = new Object();
	private static final Object ITENS_END_ATIVOS_DEST = new Object();
	private static final Object ITENS_ATIVOS_DEST = new Object();
	private static final Object ENDERECO = new Object();
	private static final Object ENDERECO_DEST_TMP = new Object();
	private static final Object DESTINATARIO = new Object();
	private static final Object CONTATO_DESTINATARIO = new Object();
	private static final Object CONTATO_DESTINATARIO_TMP = new Object();
	private static final Object LISTA_ENDERECO = new Object();
	private static final Object LISTA_CONTATO_DESTIN = new Object();
	private static final Object LISTA_DESTINATARIO = new Object();
	private static final Object LISTA_PROC_CARREGADA = new Object();
	private static final Object LISTA_ORIGEM_CARREGADA = new Object();
	private static final Object LISTA_DESTINO_CARREGADA = new Object();
	

// ######################### SESSION ##########################	
	
	public BeanManterDestinatariosBaixaExpedicao() {
		restauraSessao();
	}
	
	private void restauraSessao(){
		
		if( getAtributo(INDICE_END_DEST) == null ){
			setAtributo(INDICE_END_DEST, null);
		}
		setIndiceEnderecoDestinatario((Integer) getAtributo(INDICE_END_DEST));

		if( getAtributo(INDICE_CONT_DEST) == null ){
			setAtributo(INDICE_CONT_DEST, null);
		}
		setIndiceContatoDestinatario((Integer) getAtributo(INDICE_CONT_DEST));
		
		if( getAtributo(DESTINATARIO) == null ){
			setAtributo(DESTINATARIO, new Destinatario());
		}
		setDestinatario((Destinatario) getAtributo(DESTINATARIO));
		
		if( getAtributo(CODIGO_ORGAO) == null ){
			setAtributo(CODIGO_ORGAO, null);
		}
		setCodigoOrgao((Long) getAtributo(CODIGO_ORGAO));
		
		if( getAtributo(CODIGO_PROCEDENCIA) == null ){
			setAtributo(CODIGO_PROCEDENCIA, null);
		}
		setCodigoProcedencia((Long) getAtributo(CODIGO_PROCEDENCIA));
		
		if( getAtributo(CODIGO_ORIGEM) == null ){
			setAtributo(CODIGO_ORIGEM, null);
		}
		setCodigoOrigem((Long) getAtributo(CODIGO_ORIGEM));
		
		if( getAtributo(CODIGO_DESTINATARIO) == null ){
			setAtributo(CODIGO_DESTINATARIO, null);
		}
		setCodigoDestinatario((Long) getAtributo(CODIGO_DESTINATARIO));
		
		if( getAtributo(ENDERECO) == null ){
			setAtributo(ENDERECO, new EnderecoDestinatario());
		}
		setEnderecoDestinatario((EnderecoDestinatario) getAtributo(ENDERECO));
		
		if (enderecoDestinatarioTMP == null){
			enderecoDestinatarioTMP = null;
		}
		setEnderecoDestinatarioTMP((EnderecoDestinatario) getAtributo(ENDERECO_DEST_TMP));
		
		if( getAtributo(CONTATO_DESTINATARIO) == null ){
			setAtributo(CONTATO_DESTINATARIO, new ContatoDestinatario());
		}
		setContatoDestinatario((ContatoDestinatario) getAtributo(CONTATO_DESTINATARIO));
		
		if (contatoDestinatarioTMP == null){
			contatoDestinatarioTMP = null;
		}
		setContatoDestinatarioTMP((ContatoDestinatario) getAtributo(CONTATO_DESTINATARIO_TMP));
		
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
		
		if (getAtributo(RENDERED_BOTAO_ALTERAR_CONT) == null) {
			setAtributo(RENDERED_BOTAO_ALTERAR_CONT, false);
		}
		setRenderizaBotaoAlterarContato((Boolean) getAtributo(RENDERED_BOTAO_ALTERAR_CONT));

		if (getAtributo(RENDERED_TABELA_DEST) == null) {
			setAtributo(RENDERED_TABELA_DEST, false);
		}
		setRenderizaTabelaDestinatario((Boolean) getAtributo(RENDERED_TABELA_DEST));		
		
		if (getAtributo(TELEFONE_HIFEN) == null) {
			setAtributo(TELEFONE_HIFEN, "");
		}
		setTelefoneDestHifen((String) getAtributo(TELEFONE_HIFEN));
		
		if (getAtributo(FAX_HIFEN) == null) {
			setAtributo(FAX_HIFEN, "");
		}
		setFaxDestHifen((String) getAtributo(FAX_HIFEN));
		
		if( getAtributo(ITENS_UF) == null ){
			setAtributo(ITENS_UF,montarListaUf());
		}
		setItensUf((List<SelectItem>) getAtributo(ITENS_UF));
		
		if( getAtributo(LISTA_PROC_CARREGADA) == null ){
			setAtributo(LISTA_PROC_CARREGADA, new LinkedList<Procedencia>());
		}
		setListaProcedenciaCarregada((List<Procedencia>) getAtributo(LISTA_PROC_CARREGADA));
		
		if( getAtributo(LISTA_ORIGEM_CARREGADA) == null ){
			setAtributo(LISTA_ORIGEM_CARREGADA, new LinkedList<Origem>());
		}
		setListaOrigemCarregada((List<Origem>) getAtributo(LISTA_ORIGEM_CARREGADA));
		
		if( getAtributo(LISTA_DESTINO_CARREGADA) == null ){
			setAtributo(LISTA_DESTINO_CARREGADA, new LinkedList<Destinatario>());
		}
		setListaDestinatarioCarregada((List<Destinatario>) getAtributo(LISTA_DESTINO_CARREGADA));
		
		if (getAtributo(ITENS_END_ATIVOS_DEST) == null) {
			setAtributo(ITENS_END_ATIVOS_DEST, carregarComboAtivoDestinatario());
		}
		setItensEnderecoAtivoDestinatario((List<SelectItem>) getAtributo(ITENS_END_ATIVOS_DEST));
		
		if (getAtributo(ITENS_ATIVOS_DEST) == null) {
			setAtributo(ITENS_ATIVOS_DEST, carregarComboAtivoDestinatario());
		}
		setItensAtivoDestinatario((List<SelectItem>) getAtributo(ITENS_ATIVOS_DEST));
		
		setListaDestinatarios((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_DESTINATARIO));
		
		setListaEndereco((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_ENDERECO));
		
		setListaContatoDestinatario((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_CONTATO_DESTIN));
	}
	
	private void atualizaSessao(){

		setAtributo(INDICE_END_DEST, indiceEnderecoDestinatario);
		setAtributo(INDICE_CONT_DEST, indiceContatoDestinatario);
		setAtributo(RENDERED_TELACADASTRO, renderizaTelaCadastro);
		setAtributo(RENDERED_TELAPESQUISA, renderizaTelaDePesquisa);
		setAtributo(RENDERED_BOTAO_ALTERAR_END, renderizaBotaoAlterarEndereco);
		setAtributo(RENDERED_BOTAO_ALTERAR_CONT, renderizaBotaoAlterarContato);
		setAtributo(RENDERED_TABELA_DEST, renderizaTabelaDestinatario);
		setAtributo(ENDERECO, enderecoDestinatario);
		setAtributo(ENDERECO_DEST_TMP, enderecoDestinatarioTMP);
		setAtributo(DESTINATARIO, destinatario);
		setAtributo(CONTATO_DESTINATARIO, contatoDestinatario);
		setAtributo(CONTATO_DESTINATARIO_TMP, contatoDestinatarioTMP);
		setAtributo(LISTA_DESTINATARIO, listaDestinatarios);
		setAtributo(LISTA_ENDERECO, listaEndereco);
		setAtributo(LISTA_CONTATO_DESTIN, listaContatoDestinatario);
		setAtributo(LISTA_PROC_CARREGADA, listaProcedenciaCarregada);
		setAtributo(LISTA_ORIGEM_CARREGADA, listaOrigemCarregada);
		setAtributo(LISTA_DESTINO_CARREGADA, listaDestinatarioCarregada);
		setAtributo(CODIGO_ORGAO, codigoOrgao);
		setAtributo(CODIGO_PROCEDENCIA, codigoProcedencia);
		setAtributo(CODIGO_ORIGEM, codigoOrigem);
		setAtributo(CODIGO_DESTINATARIO, codigoDestinatario);
		setAtributo(TELEFONE_HIFEN, telefoneDestHifen);
		setAtributo(FAX_HIFEN, faxDestHifen);
		setAtributo(ITENS_ATIVOS_DEST, itensAtivoDestinatario);
		setAtributo(ITENS_END_ATIVOS_DEST, itensEnderecoAtivoDestinatario);
	}
	
// ########################## ACTION ##########################
	
	public void atualizaSessaoAction(ActionEvent evt){
		atualizaSessao();
	}
	
	public void limparTelaAction(ActionEvent evt){
		limparTela();
		atualizaSessao();
	}
	
	public void limparTelaCadastrarDestinatarioAction(ActionEvent evt){
		limparTela();
		setRenderizaTelaCadastro(true);
		setRenderizaTelaDePesquisa(false);
		atualizaSessao();
	}
	
	
	public void carregaProcedenciaAction(ActionEvent evt){
		carregaListaProcecedenciaPeloOrgao(codigoOrgao);
		atualizaSessao();
	}
	
	public void carregarOrigensDevolucaoAction(ActionEvent evt){
		//limpa o código do órgão ou da procedencia caso o usuário tenha
		//escolhido um órgão ou procedência por engano.
		if (nomeOrgao == null || nomeOrgao.trim().length() == 0){
			codigoOrgao = null;
		}
		
		if (nomeProcedencia == null || nomeProcedencia.trim().length() == 0){
			codigoProcedencia = null;
		}
		
		carregarOrigensDevolucao(codigoOrgao, codigoProcedencia);
		atualizaSessao();
	}
	
	public void carregarOrigensAtivasAction(ActionEvent evt){
		
		carregarOrigensAtivas();
		atualizaSessao();
	}
	
	public void carregarDestinatarioPelaOrigemAction(ActionEvent evt){
		//limpa o código da origem caso o usuário tenha
		//escolhido uma origem por engano.
		if (nomeOrigem == null || nomeOrigem.trim().length() == 0){
			codigoOrigem = null;
		}
		carregarListaDestinatarioPelaOrigem(codigoOrigem);
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
		setEnderecoDestinatario(new EnderecoDestinatario());
		setEnderecoDestinatarioTMP(null);
		setRenderizaBotaoAlterarEndereco(false);
		atualizaSessao();
	}
	
	public void adicionarContatoAction(ActionEvent evt){
		adicionarContato();
		atualizaSessao();
	}

	public void alterarContatoAction(ActionEvent evt){
		setRenderizaBotaoAlterarContato(true);
		alterarContato();
		atualizaSessao();
	}
	
	public void novoContatoAction(ActionEvent evt){
		setContatoDestinatario(new ContatoDestinatario());
		setContatoDestinatarioTMP(null);
		setRenderizaBotaoAlterarContato(false);
		atualizaSessao();
	}
	
	public void excluirContatoAction(ActionEvent evt){
		excluirContato();
		atualizaSessao();
	}
	
	public void salvarDestinatarioAction(ActionEvent evt){
		salvarDestinatario();
		atualizaSessao();
	}
	
	public void pesquisarDestinatarioAction(ActionEvent evt){
		pesquisarDestinatario();
		atualizaSessao();
	}
	
	public void alterarDestinatarioAction(ActionEvent evt){
		alterarDestinatario();
		atualizaSessao();
	}
	
	public void excluirDestinatarioAction(ActionEvent evt){
		excluirDestinatario();
		atualizaSessao();
	}
	
	
	public void fechaTelaCadastrarDestinatarioAction(ActionEvent evt){
		limparTela();
		atualizaSessao();
	}

// ######################## METHODs ############################
	
	public void incluiDestinatario(){
		destinatario.setNomDestinatario(nomeDestinatario);
		atualizaSessao();
	}
	
	public void limparCamposNovoDestinatario(){
		setDestinatario(new Destinatario());
		setListaEndereco(new LinkedList<CheckableDataTableRowWrapper>());
		setListaContatoDestinatario(new LinkedList<CheckableDataTableRowWrapper>());
		setListaDestinatarios(new LinkedList<CheckableDataTableRowWrapper>());
		setListaProcedenciaCarregada(new LinkedList<Procedencia>());
		setItensUf(new LinkedList<SelectItem>());
		setItensAtivoDestinatario(carregarComboAtivoDestinatario());
		setItensEnderecoAtivoDestinatario(carregarComboAtivoDestinatario());
		setEnderecoDestinatario(new EnderecoDestinatario());
		setContatoDestinatario(new ContatoDestinatario());
		setTelefoneDestHifen("");
		setFaxDestHifen("");
		setRenderizaTelaCadastro(true);
		setRenderizaTelaDePesquisa(false);
		setRenderizaBotaoAlterarEndereco(false);
		setRenderizaBotaoAlterarContato(false);
		setEnderecoDestinatarioTMP(null);
	}
	
	
	/**
	 * Limpa os campos da tela
	 */
	public void limparTela(){
		setDestinatario(new Destinatario());
		setListaEndereco(new LinkedList<CheckableDataTableRowWrapper>());
		setListaContatoDestinatario(new LinkedList<CheckableDataTableRowWrapper>());
		setListaDestinatarios(new LinkedList<CheckableDataTableRowWrapper>());
		setListaProcedenciaCarregada(new LinkedList<Procedencia>());
		setItensUf(new LinkedList<SelectItem>());
		setItensAtivoDestinatario(carregarComboAtivoDestinatario());
		setItensEnderecoAtivoDestinatario(carregarComboAtivoDestinatario());
		setEnderecoDestinatario(new EnderecoDestinatario());
		setContatoDestinatario(new ContatoDestinatario());
		setNomeDestinatario("");
		setNomeOrgao("");
		setNomeOrigem("");
		setNomeProcedencia("");
		setTelefoneDestHifen("");
		setFaxDestHifen("");
		setRenderizaTelaCadastro(false);
		setRenderizaTelaDePesquisa(true);
		setRenderizaBotaoAlterarEndereco(false);
		setRenderizaBotaoAlterarContato(false);
		setRenderizaTabelaDestinatario(false);
		setCodigoOrgao(null);
		setCodigoProcedencia(null);
		setCodigoOrigem(null);
		setCodigoDestinatario(null);
		setEnderecoDestinatarioTMP(null);
	}
	
	
	private void carregaListaProcecedenciaPeloOrgao(Long codOrgao) {

		listaProcedenciaCarregada = new LinkedList<Procedencia>();
		if (codOrgao != null) {
			try {
				Orgao orgao = getOrgaoService().recuperarPorId(codOrgao);
				List<Procedencia> listaProcedenciaT =  getProcedenciaService().pesquisarProcedenciasComOrigemAtiva(orgao);

				for (Procedencia procedencia :listaProcedenciaT) {
					listaProcedenciaCarregada.add(procedencia);
				}
			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao pesquisar procedências.", e.getMessage());
			}
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public List pesquisarProcedenciaPelaListaSelecionada(Object value) {
		
		// recupera a lista de Procedência e adiciona os objetos a cada objeto result (utilizado no suggestion)
		List<Procedencia> listProcedencia = new ArrayList<Procedencia>();
		
		if(value != null){
			if(listaProcedenciaCarregada != null && listaProcedenciaCarregada.size() > 0){
				for (Procedencia proc : listaProcedenciaCarregada){
					if( NumberUtils.soNumeros(value.toString())){
						if (proc.getId().toString().contains(value.toString())){
							listProcedencia.add(proc);									
						}
						
					}else {
						if (proc.getDescricao().contains(value.toString().toUpperCase())){
							listProcedencia.add(proc);
						}
					}
				}
			}
		}
		/*if(listProcedencia.size() == 0){
			reportarAviso("O órgão deve ser selecionado;");
		}*/
		return listProcedencia;
	}		
	
	
	private void carregarOrigensDevolucao(Long codOrgao, Long codProcedencia) {
		if(codOrgao == null || codProcedencia == null){
			reportarAviso("Órgão e a procedência devem ser selecionados.");
			return;
		}

		listaOrigemCarregada = new LinkedList<Origem>();
		try {
			Orgao orgao = getOrgaoService().recuperarPorId(codOrgao);
			Procedencia procedencia = getProcedenciaService().recuperarPorId(codProcedencia);

			for (Origem origem : getOrigemService().pesquisarOrigensAtivas(orgao, procedencia)) {
				if (getOrigemService().isOrigemIntegrada(origem)) {
					listaOrigemCarregada.add(origem);
				}
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar origens.", e.getMessage());
		}
	}
	
	private void carregarOrigensAtivas() {
		
		listaOrigemCarregada = new LinkedList<Origem>();
		List<Origem> origens = new ArrayList<Origem>();
		
		try {
			origens = getOrigemService().recuperarOrigemPorId(null, true);				
		} catch (ServiceException e) {
			reportarErro("Erro ao buscar a lista de origens.");
		}

		listaOrigemCarregada.addAll(origens);

	}
	
	
	@SuppressWarnings("rawtypes")
	public List pesquisarOrigensPelaListaSelecionada(Object value) {

		if (value == null) {
			return null;
		}
		if (value.toString().trim() == "") {
			return null;
		}

		// recupera a lista de Procedência e adiciona os objetos a cada objeto result (utilizado no suggestion)
		List<Origem> listOrigens = new ArrayList<Origem>();
		
		if(listaOrigemCarregada != null && listaOrigemCarregada.size() > 0){
			for (Origem orig : listaOrigemCarregada){
				if( NumberUtils.soNumeros(value.toString())){
					if (orig.getId().toString().contains(value.toString())){
						listOrigens.add(orig);									
					}
					
				}else {
					if (orig.getDescricao().contains(value.toString().toUpperCase())){
						listOrigens.add(orig);
					}
				}
			}
		}

		return listOrigens;
	}
	
	
	private void carregarListaDestinatarioPelaOrigem(Long codOrigem){
		listaDestinatarioCarregada = new LinkedList<Destinatario>();

		try {
			List<Destinatario> listaDestinatarioOrigem = getDestinatarioService().recuperarDestinatarioDaOrigem(codOrigem,  null);
			for (Destinatario dest : listaDestinatarioOrigem) {
					listaDestinatarioCarregada.add(dest);
			
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar os destinatários.", e.getMessage());
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public List pesquisarDestinatarioPelaListaSelecionada(Object value) {

		if (value == null) {
			return null;
		}
		if (value.toString().trim() == "") {
			return null;
		}

		// recupera a lista de Procedência e adiciona os objetos a cada objeto result (utilizado no suggestion)
		List<Destinatario> listDestinatario = new ArrayList<Destinatario>();
		
		if(listaDestinatarioCarregada != null && listaDestinatarioCarregada.size() > 0){
			for (Destinatario dest : listaDestinatarioCarregada){
				if( NumberUtils.soNumeros(value.toString())){
					if (dest.getId().toString().contains(value.toString())){
						listDestinatario.add(dest);									
					}
					
				}else {
					if (dest.getNomDestinatario().contains(value.toString().toUpperCase())){
						listDestinatario.add(dest);
					}
				}
			}
		}
		return listDestinatario;
	}
		
	
	/**
	 * Pesquisa os destinatários
	 */
	private void pesquisarDestinatario(){
		
		if (codigoDestinatario == null && codigoOrigem == null ){
			reportarAviso("Um dos parâmetros deverá ser informado.");
			return;
		}
		
		if (nomeOrigem == null || nomeOrigem.trim().length() == 0){
			codigoOrigem = null;
		}
		
		if (nomeDestinatario == null || nomeDestinatario.trim().length() == 0){
			codigoDestinatario = null;
		}
		
		List<DestinatarioOrgaoOrigemResult> listaDestinatario = new LinkedList<DestinatarioOrgaoOrigemResult>();
		
		try{
			getDestinatarioService().limparSessao();
			listaDestinatario = getDestinatarioService().pesquisarDestinatario(codigoOrigem, codigoDestinatario);
			
		}catch( Exception e) {
			e.printStackTrace();
			reportarErro("Erro ao pesquisar", e.getMessage());
		}
		if(listaDestinatario == null || listaDestinatario.size() == 0){
			reportarInformacao("Nenhum endereço encontrado para os parâmetros informados.");
		}else{
			setRenderizaTabelaDestinatario(true);
		}
		setListaDestinatarios(getCheckableDataTableRowWrapperList((listaDestinatario)));
	}
	
		
	/**
	 * Adiciona os dados preenchidos na lista de endereço e exibe
	 * na tabela
	 */
	private void adicionarEndereco(){
		
		if (codigoOrigem == null){
			reportarAviso("O campo origem deve ser selecionado.");
			return;
		}
		
		if (stringEmpty(nomeDestinatario)){
			reportarAviso("Nome do destinatário está vazio. Favor preencher.");
			return;
		}
		
		if (enderecoDestinatario.getLogradouro() == null || enderecoDestinatario.getLogradouro().trim().length() == 0){
			reportarAviso("O campo endereço deve ser preenchido.");
			return;
		}
		
		if((enderecoDestinatario.getNumeroLocalizacao() == null  || enderecoDestinatario.getNumeroLocalizacao().trim().length() == 0)){
			reportarAviso("O número deve ser preenchido.");
			return;
		}
		
		if (enderecoDestinatario.getUf() == null || enderecoDestinatario.getUf().trim().length() == 0){
			reportarAviso("O campo UF deve ser selecionado.");
			return;
		}
		
		if (enderecoDestinatario.getMunicipio() == null || enderecoDestinatario.getMunicipio().trim().length() == 0){
			reportarAviso("O campo cidade deve ser preenchido");
			return;
		}
		
		if( listaEndereco != null && listaEndereco.size() > 0 ){
			
			List<EnderecoDestinatario> lista = getOriginalListForCheckableDataTableRowWrapper(listaEndereco);
			Boolean inicializaListaTroca = false;
			for( int i = 0 ; i < lista.size() ; i++ ){
				
				EnderecoDestinatario end = lista.get(i);
				
				if(valoresIguais(end.getCep(), enderecoDestinatario.getCep()) &&
						valoresIguais(end.getLogradouro(), enderecoDestinatario.getLogradouro())&&
						valoresIguais(end.getNumeroLocalizacao(), enderecoDestinatario.getNumeroLocalizacao())&&
						valoresIguais(end.getBairro(), enderecoDestinatario.getBairro())&&
						valoresIguais(end.getComplemento(), enderecoDestinatario.getComplemento())&&
						valoresIguais(end.getMunicipio(), enderecoDestinatario.getMunicipio())&&
						valoresIguais(end.getUf(), enderecoDestinatario.getUf())){
					reportarAviso("O endereço já esta adicionado.");
					return;
				}	
				
				//reinicializa a lista um única vez para que o registro alterado
				//seja inserido na exata posição do regitro original
				if (!inicializaListaTroca && enderecoDestinatarioTMP != null){
					setListaEndereco(new LinkedList<CheckableDataTableRowWrapper>());
					inicializaListaTroca = true;					
				}
				
				if (enderecoDestinatarioTMP != null){
					if (i == indiceEnderecoDestinatario){
						enderecoDestinatario.setDestinatario(destinatario);
						listaEndereco.add(new CheckableDataTableRowWrapper(enderecoDestinatario));
					}else{
						listaEndereco.add(new CheckableDataTableRowWrapper(end));
					}
				}
			}
			
			
		}else{
			setListaEndereco(new LinkedList<CheckableDataTableRowWrapper>());			
		}
		
		if (enderecoDestinatarioTMP == null){
			destinatario.setNomDestinatario(nomeDestinatario);
			enderecoDestinatario.setDestinatario(destinatario);
			listaEndereco.add(new CheckableDataTableRowWrapper(enderecoDestinatario));
		}
		setRenderizaBotaoAlterarEndereco(false);
		setEnderecoDestinatario(new EnderecoDestinatario());
	}
	
	
	/**
	 * Recupera o endereço de acordo com o CEP digitado
	 */
	private void recuperarEnderecoCep(){
		try {
			EnderecoView end = getEnderecoDestinatarioService().recuperarEnderecoView(enderecoDestinatario.getCep());
			if( end != null ){
				enderecoDestinatario.setCep(end.cep);
				enderecoDestinatario.setLogradouro(end.logradouro);
				enderecoDestinatario.setMunicipio(end.cidade);
				enderecoDestinatario.setBairro(end.bairro);
				enderecoDestinatario.setUf(end.UF);
			}
			
		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar UF");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Mostra nos campos os dados selecionados na tabela para que o cliente
	 * possa alterar os dados de endereço;
	 */
	private void alterarEndereco(){
		enderecoDestinatarioTMP = new EnderecoDestinatario();
		enderecoDestinatarioTMP = (EnderecoDestinatario)((CheckableDataTableRowWrapper)tabelaEndereco.getRowData()).getWrappedObject();
		indiceEnderecoDestinatario = tabelaEndereco.getRowIndex();
		enderecoDestinatario.setId(enderecoDestinatarioTMP.getId());
		enderecoDestinatario.setDestinatario(enderecoDestinatarioTMP.getDestinatario());
		if (enderecoDestinatarioTMP.getCep() != null)
			enderecoDestinatario.setCep(enderecoDestinatarioTMP.getCep());
		if (enderecoDestinatarioTMP.getLogradouro() != null)
			enderecoDestinatario.setLogradouro(enderecoDestinatarioTMP.getLogradouro());
		if (enderecoDestinatarioTMP.getNumeroLocalizacao() != null)
			enderecoDestinatario.setNumeroLocalizacao(enderecoDestinatarioTMP.getNumeroLocalizacao());
		if (enderecoDestinatarioTMP.getBairro() != null)
			enderecoDestinatario.setBairro(enderecoDestinatarioTMP.getBairro());
		if (enderecoDestinatarioTMP.getComplemento() != null)
			enderecoDestinatario.setComplemento(enderecoDestinatarioTMP.getComplemento());
		if (enderecoDestinatarioTMP.getMunicipio()!= null)
			enderecoDestinatario.setMunicipio(enderecoDestinatarioTMP.getMunicipio());
		if (enderecoDestinatarioTMP.getAtivo() == true){
			enderecoDestinatario.setAtivo(true);
		}else{
			enderecoDestinatario.setAtivo(false);
		}
		if (enderecoDestinatarioTMP.getUf() != null)
			enderecoDestinatario.setUf(enderecoDestinatarioTMP.getUf());
		
	//	setItensUf(montarListaUf());
		setItensEnderecoAtivoDestinatario(carregarComboAtivoDestinatario());
	}
	
	
	/**
	 * Exclui o endereço da tabela, porém para ser excluído da tabela
	 * será necessário o cliente salvar novamente o destinatário
	 */
	private void excluirEndereco(){
		if ( listaEndereco == null || listaEndereco.size()==0 ) {
			reportarAviso("Selecione pelo menos um endereço.");
			return;
		}else{
			if (destinatario.getListaEnderecoDestinatario() != null &&
					destinatario.getListaEnderecoDestinatario().size() > 0){
			
				EnderecoDestinatario end = (EnderecoDestinatario)((CheckableDataTableRowWrapper)tabelaEndereco.getRowData()).getWrappedObject();
				
				if (verificaEnderecoNaGuia(end)){
					reportarAviso("O endereço não pode ser excluído, pois o mesmo se encontra vinculado a uma guia.");
					return;
				}
				
				listaEndereco.remove(((CheckableDataTableRowWrapper)tabelaEndereco.getRowData()));
			}
		}
	}
	
	/**
	 * Adiciona o contato na tabela. O contato somente será salvo quando
	 * o cliente salvar o destinatário.
	 */
	private void adicionarContato(){
		
		if (stringEmpty(contatoDestinatario.getNomContato())){
			reportarAviso("Nome do contato está vazio. Favor preencher.");
			return;
		}
		
		if (contatoDestinatario.getEnderecoEmail() != null && 
				contatoDestinatario.getEnderecoEmail().trim().length() > 0){
			if (!contatoDestinatario.getEnderecoEmail().contains("@")){
				reportarAviso("Endereço de e-mail inválido.");
				return;
			}
		}
		
		if( listaContatoDestinatario != null && listaContatoDestinatario.size() > 0 ){
		
			List<ContatoDestinatario> lista = getOriginalListForCheckableDataTableRowWrapper(listaContatoDestinatario);
			Boolean inicializaListaTroca = false;
			
			for( int i = 0 ; i < lista.size() ; i++ ){
				
				ContatoDestinatario cont = lista.get(i);
				
				if(valoresIguais(cont.getNomContato(), contatoDestinatario.getNomContato()) &&
						valoresIguais(cont.getCodigoAreaFax(), contatoDestinatario.getCodigoAreaFax())&&
						valoresIguais(cont.getNumeroFax(), contatoDestinatario.getNumeroFax())&&
						valoresIguais(cont.getCodigoAreaTelefone(), contatoDestinatario.getCodigoAreaTelefone())&&
						valoresIguais(cont.getNumeroTelefone(), contatoDestinatario.getNumeroTelefone())&&
						valoresIguais(cont.getEnderecoEmail(), contatoDestinatario.getEnderecoEmail())){
					reportarAviso("O contato já está adicionado.");
					return;
				}	
				
				//reinicializa a lista um única vez para que o registro alterado
				//seja inserido na exata posição do regitro original
				if (!inicializaListaTroca && contatoDestinatarioTMP != null){
					setListaContatoDestinatario(new LinkedList<CheckableDataTableRowWrapper>());
					inicializaListaTroca = true;					
				}
				
				if (contatoDestinatarioTMP != null){
					if (i == indiceContatoDestinatario){
						contatoDestinatario.setDestinatario(destinatario);
						
						if (telefoneDestHifen != null && telefoneDestHifen.trim().length() > 0){
							contatoDestinatario.setNumeroTelefone(retirarHifenTelFax(telefoneDestHifen));
						}
						
						if (faxDestHifen != null && faxDestHifen.trim().length() > 0){
							contatoDestinatario.setNumeroFax(retirarHifenTelFax(faxDestHifen));
						}
						
						listaContatoDestinatario.add(new CheckableDataTableRowWrapper(contatoDestinatario));
					}else{
						listaContatoDestinatario.add(new CheckableDataTableRowWrapper(cont));
					}
				}
			}
			
			
		}else{
			setListaContatoDestinatario(new LinkedList<CheckableDataTableRowWrapper>());			
		}
		
		if (contatoDestinatarioTMP == null){
			contatoDestinatario.setDestinatario(destinatario);
			
			if (telefoneDestHifen != null && telefoneDestHifen.trim().length() > 0){
				contatoDestinatario.setNumeroTelefone(retirarHifenTelFax(telefoneDestHifen));
			}
			
			if (faxDestHifen != null && faxDestHifen.trim().length() > 0){
				contatoDestinatario.setNumeroFax(retirarHifenTelFax(faxDestHifen));
			}
			
			listaContatoDestinatario.add(new CheckableDataTableRowWrapper(contatoDestinatario));
		}
		setRenderizaBotaoAlterarContato(false);
		setContatoDestinatario(new ContatoDestinatario());
		setTelefoneDestHifen("");
		setFaxDestHifen("");

	}
	
	
	/**
	 * Retira o hifens dos campos de telefone e fax
	 */
	private Integer retirarHifenTelFax(String telFax){
		if (telFax.contains("-")){
			return Integer.parseInt(telFax.replaceAll("-", ""));
		}
		return Integer.parseInt(telFax);
	}
	
	
	/**
	 * Mostra nos campos os dados selecionados na tabela para que o cliente
	 * possa alterar os dados de contato;
	 */
	private void alterarContato(){
		contatoDestinatarioTMP = new ContatoDestinatario();
		contatoDestinatarioTMP = (ContatoDestinatario)((CheckableDataTableRowWrapper)tabelaContatoDestinatario.getRowData()).getWrappedObject();
		indiceContatoDestinatario = tabelaContatoDestinatario.getRowIndex();
		contatoDestinatario.setId(contatoDestinatarioTMP.getId());
		contatoDestinatario.setDestinatario(contatoDestinatarioTMP.getDestinatario());
		if (contatoDestinatarioTMP.getCodigoAreaFax() != null)
			contatoDestinatario.setCodigoAreaFax(contatoDestinatarioTMP.getCodigoAreaFax());
		if (contatoDestinatarioTMP.getCodigoAreaTelefone() != null)
			contatoDestinatario.setCodigoAreaTelefone(contatoDestinatarioTMP.getCodigoAreaTelefone());
		if (contatoDestinatarioTMP.getEnderecoEmail() != null)
			contatoDestinatario.setEnderecoEmail(contatoDestinatarioTMP.getEnderecoEmail());
		if (contatoDestinatarioTMP.getNomContato() != null)
			contatoDestinatario.setNomContato(contatoDestinatarioTMP.getNomContato());
		if (contatoDestinatarioTMP.getNumeroFax() != null){
			contatoDestinatario.setNumeroFax(contatoDestinatarioTMP.getNumeroFax());
			setFaxDestHifen(contatoDestinatarioTMP.getNumeroFax().toString());
		}
		if (contatoDestinatarioTMP.getNumeroTelefone() != null){
			contatoDestinatario.setNumeroTelefone(contatoDestinatarioTMP.getNumeroTelefone());
			setTelefoneDestHifen(contatoDestinatarioTMP.getNumeroTelefone().toString());
		}
	}
	
	
	/**
	 * Exclui o contato da tabela, porém para ser excluído da tabela
	 * será necessário o cliente salvar novamente o destinatário
	 */
	private void excluirContato(){
		if ( listaContatoDestinatario == null || listaContatoDestinatario.size()==0 ) {
			reportarAviso("Selecione pelo menos um contato.");
			return;
		}else{
			//	List<CheckableDataTableRowWrapper> selecionados = retornarItensSelecionados(listaEndereco);
			listaContatoDestinatario.remove(((CheckableDataTableRowWrapper)tabelaContatoDestinatario.getRowData()));
		}
	}
	
	
	/**
	 * Salvar ou alterar na base o dados do destinatário.
	 */
	@SuppressWarnings("unchecked")
	private void salvarDestinatario(){
		
		if( stringEmpty(nomeOrigem)){
			reportarAviso("A origem deve ser selecionada");
			return;
		}
		
		if( stringEmpty(nomeDestinatario) ){
			reportarAviso("O nome do destinatário deve ser preenchido.");
			return;
		}
		
		if (enderecoDestinatario.getLogradouro() == null){
			reportarAviso("O destinatário deve possuir ao menos um endereço.");
			return;
		}
		
		if (destinatario.getAtivo() == null){
			reportarAviso("Seleciona se o destinatário será ativo ou não.");
			return;
		}

		if (destinatario.getListaEnderecoDestinatario() == null)
			destinatario.setListaEnderecoDestinatario(new LinkedList<EnderecoDestinatario>());
		
		destinatario.getListaEnderecoDestinatario().clear();
		
		List<EnderecoDestinatario> enderecos = getOriginalListForCheckableDataTableRowWrapper(listaEndereco);
		if (enderecos != null && enderecos.size() > 0){
			destinatario.getListaEnderecoDestinatario().addAll(enderecos);
		}
		
		if( destinatario.getListaContatoDestinatario() == null)
			destinatario.setListaContatoDestinatario(new LinkedList<ContatoDestinatario>());
		
		destinatario.getListaContatoDestinatario().clear();
		List<ContatoDestinatario> contatosDestinatario = getOriginalListForCheckableDataTableRowWrapper(listaContatoDestinatario);
		
		if( contatosDestinatario != null && contatosDestinatario.size() > 0 ){
			destinatario.getListaContatoDestinatario().addAll(contatosDestinatario);
		}
		
		if (destinatario.getListaEnderecoDestinatario().size() == 0){
			reportarAviso("O contato deve possuir ao menos um endereço adicionado.");
			return;
		}
		
		if (destinatario.getListaContatoDestinatario().size() == 0){
			reportarAviso("A pessoa deve possuir ao menos um contato adicionado.");

		}
		
		//recupera a origem selecionada
		try {
			Origem origem = getOrigemService().recuperarPorId(codigoOrigem);
			destinatario.setOrigem(origem);
		} catch (Exception e) {
			e.printStackTrace();
			reportarErro("Erro ao pesquisar a origem selecionada na base de dados.");
		}
		
	//	destinatario.setAtivo(true);
		try {
			if( destinatario.getId() == null ){
				getDestinatarioService().limparSessao();
				getDestinatarioService().incluir(destinatario);
			}else{
				destinatario.setNomDestinatario(nomeDestinatario);
				getDestinatarioService().limparSessao();
				getDestinatarioService().alterar(destinatario);
			}
			novoDestinatario();
			
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao salvar o destinatário.");
			return;
		}
		
		reportarInformacao("Operação realizada com sucesso.");
	
	}
	
	/**
	 * Exibe na tela as informações do destinatário para que o cliente
	 * possa alterar. As informações somente serão atualziadas na tabela
	 * quando o cliente clicar no botão salvar
	 */
	private void alterarDestinatario(){
		try {
			DestinatarioOrgaoOrigemResult destinatarioOOR = (DestinatarioOrgaoOrigemResult)((CheckableDataTableRowWrapper)tabelaDestinatarios.getRowData()).getWrappedObject();
			destinatario = getDestinatarioService().recuperarPorId(destinatarioOOR.getCodigoDestinatario());
			
			if (destinatario.getListaEnderecoDestinatario() != null && destinatario.getListaEnderecoDestinatario().size() > 0)
				setListaEndereco(getCheckableDataTableRowWrapperList(destinatario.getListaEnderecoDestinatario()));
			
			if( destinatario.getListaContatoDestinatario() != null && destinatario.getListaContatoDestinatario().size() > 0 )
				setListaContatoDestinatario(getCheckableDataTableRowWrapperList(destinatario.getListaContatoDestinatario()));
			
			setCodigoOrigem(destinatario.getOrigem().getId());
			setNomeOrigem(destinatario.getOrigem().getId().toString() + " - " + destinatario.getOrigem().getDescricao().toUpperCase());
			setNomeDestinatario(destinatario.getNomDestinatario());
			setNomeProcedencia("");
			setNomeOrgao("");
			
			setRenderizaTelaCadastro(true);
			setRenderizaTelaDePesquisa(false);
			setRenderizaTabelaDestinatario(false);
			
		} catch (ServiceException e) {
			reportarErro("Erro ao localizar o destinatário.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Exclui da tabela o destinatario selecionado
	 */
	private void excluirDestinatario(){
		DestinatarioOrgaoOrigemResult destinatarioOOR = (DestinatarioOrgaoOrigemResult)((CheckableDataTableRowWrapper)tabelaDestinatarios.getRowData()).getWrappedObject();
		try {
			destinatario = getDestinatarioService().recuperarPorId(destinatarioOOR.getCodigoDestinatario());
			
			if (destinatario.getListaEnderecoDestinatario() != null && destinatario.getListaContatoDestinatario().size() > 0){
				for (EnderecoDestinatario end : destinatario.getListaEnderecoDestinatario()){
					if (verificaEnderecoNaGuia(end)){
						reportarAviso("O destinatário não pode ser excluído, pois existe um endereço vinculado a uma guia.");
						return;
					}
				}
			}
			
			getDestinatarioService().excluir(destinatario);
			reportarAviso("Destinatário excluído com sucesso.");
		} catch (ServiceException e) {
			reportarErro("Erro ao localizar o destinatário.");
			e.printStackTrace();
		}
		limparTela();
	}
	
	
	private Boolean verificaEnderecoNaGuia(EnderecoDestinatario end){
		try {
			return getGuiaService().existeEndereco(end);
		} catch (ServiceException e) {
			reportarErro("Erro ao localizar a guia pelo endereço.");
		}
		return false;
	}
	
	private void novoDestinatario(){
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
			e.printStackTrace();
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
	
// ######################## GETs and  SETs ######################
	
	public String getNomeOrgao() {
		return nomeOrgao;
	}
	
	public void setNomeOrgao(String nomeOrgao) {
		this.nomeOrgao = nomeOrgao;
	}

	public Short getTipoDestino() {
		return tipoDestino;
	}

	public void setTipoDestino(Short tipoDestino) {
		this.tipoDestino = tipoDestino;
	}

	public Long getCodigoOrgao() {
		return codigoOrgao;
	}

	public void setCodigoOrgao(Long codigoOrgao) {
		this.codigoOrgao = codigoOrgao;
	}

	public String getNomeProcedencia() {
		return nomeProcedencia;
	}

	public void setNomeProcedencia(String nomeProcedencia) {
		this.nomeProcedencia = nomeProcedencia;
	}

	public Long getCodigoProcedencia() {
		return codigoProcedencia;
	}

	public void setCodigoProcedencia(Long codigoProcedencia) {
		this.codigoProcedencia = codigoProcedencia;
	}

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

	public List<CheckableDataTableRowWrapper> getListaDestinatarios() {
		return listaDestinatarios;
	}

	public void setListaDestinatarios(
			List<CheckableDataTableRowWrapper> listaDestinatarios) {
		this.listaDestinatarios = listaDestinatarios;
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

	public EnderecoDestinatario getEnderecoDestinatario() {
		return enderecoDestinatario;
	}

	public void setEnderecoDestinatario(EnderecoDestinatario enderecoDestinatario) {
		this.enderecoDestinatario = enderecoDestinatario;
	}

	public List<SelectItem> getItensUf() {
		return itensUf;
	}

	public void setItensUf(List<SelectItem> itensUf) {
		this.itensUf = itensUf;
	}

	public List<SelectItem> getItensEnderecoAtivoDestinatario() {
		return itensEnderecoAtivoDestinatario;
	}

	public void setItensEnderecoAtivoDestinatario(List<SelectItem> itensEnderecoAtivoDestinatario) {
		this.itensEnderecoAtivoDestinatario = itensEnderecoAtivoDestinatario;
	}

	public List<CheckableDataTableRowWrapper> getListaEndereco() {
		return listaEndereco;
	}

	public void setListaEndereco(List<CheckableDataTableRowWrapper> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}

	public Destinatario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}

	public HtmlDataTable getTabelaEndereco() {
		return tabelaEndereco;
	}

	public void setTabelaEndereco(HtmlDataTable tabelaEndereco) {
		this.tabelaEndereco = tabelaEndereco;
	}

	public ContatoDestinatario getContatoDestinatario() {
		return contatoDestinatario;
	}

	public void setContatoDestinatario(ContatoDestinatario contatoDestinatario) {
		this.contatoDestinatario = contatoDestinatario;
	}

	public List<CheckableDataTableRowWrapper> getListaContatoDestinatario() {
		return listaContatoDestinatario;
	}

	public void setListaContatoDestinatario(
			List<CheckableDataTableRowWrapper> listaContatoDestinatario) {
		this.listaContatoDestinatario = listaContatoDestinatario;
	}

	public HtmlDataTable getTabelaContatoDestinatario() {
		return tabelaContatoDestinatario;
	}

	public void setTabelaContatoDestinatario(HtmlDataTable tabelaContatoDestinatario) {
		this.tabelaContatoDestinatario = tabelaContatoDestinatario;
	}

	public List<Procedencia> getListaProcedenciaCarregada() {
		return listaProcedenciaCarregada;
	}

	public void setListaProcedenciaCarregada(
			List<Procedencia> listaProcedenciaCarregada) {
		this.listaProcedenciaCarregada = listaProcedenciaCarregada;
	}

	public List<Origem> getListaOrigemCarregada() {
		return listaOrigemCarregada;
	}

	public void setListaOrigemCarregada(List<Origem> listaOrigemCarregada) {
		this.listaOrigemCarregada = listaOrigemCarregada;
	}

	public List<Destinatario> getListaDestinatarioCarregada() {
		return listaDestinatarioCarregada;
	}

	public void setListaDestinatarioCarregada(
			List<Destinatario> listaDestinatarioCarregada) {
		this.listaDestinatarioCarregada = listaDestinatarioCarregada;
	}

	public List<SelectItem> getItensAtivoDestinatario() {
		return itensAtivoDestinatario;
	}

	public void setItensAtivoDestinatario(List<SelectItem> itensAtivoDestinatario) {
		this.itensAtivoDestinatario = itensAtivoDestinatario;
	}

	public Boolean getRenderizaTelaDePesquisa() {
		return renderizaTelaDePesquisa;
	}

	public void setRenderizaTelaDePesquisa(Boolean renderizaTelaDePesquisa) {
		this.renderizaTelaDePesquisa = renderizaTelaDePesquisa;
	}

	public String getTelefoneDestHifen() {
		return telefoneDestHifen;
	}

	public void setTelefoneDestHifen(String telefoneDestHifen) {
		this.telefoneDestHifen = telefoneDestHifen;
	}

	public String getFaxDestHifen() {
		return faxDestHifen;
	}

	public void setFaxDestHifen(String faxDestHifen) {
		this.faxDestHifen = faxDestHifen;
	}

	public Boolean getRenderizaBotaoAlterarEndereco() {
		return renderizaBotaoAlterarEndereco;
	}

	public void setRenderizaBotaoAlterarEndereco(
			Boolean renderizaBotaoAlterarEndereco) {
		this.renderizaBotaoAlterarEndereco = renderizaBotaoAlterarEndereco;
	}

	public EnderecoDestinatario getEnderecoDestinatarioTMP() {
		return enderecoDestinatarioTMP;
	}

	public void setEnderecoDestinatarioTMP(
			EnderecoDestinatario enderecoDestinatarioTMP) {
		this.enderecoDestinatarioTMP = enderecoDestinatarioTMP;
	}

	public ContatoDestinatario getContatoDestinatarioTMP() {
		return contatoDestinatarioTMP;
	}

	public void setContatoDestinatarioTMP(ContatoDestinatario contatoDestinatarioTMP) {
		this.contatoDestinatarioTMP = contatoDestinatarioTMP;
	}

	public Boolean getRenderizaBotaoAlterarContato() {
		return renderizaBotaoAlterarContato;
	}

	public void setRenderizaBotaoAlterarContato(Boolean renderizaBotaoAlterarContato) {
		this.renderizaBotaoAlterarContato = renderizaBotaoAlterarContato;
	}

	public Integer getIndiceEnderecoDestinatario() {
		return indiceEnderecoDestinatario;
	}

	public void setIndiceEnderecoDestinatario(Integer indiceEnderecoDestinatario) {
		this.indiceEnderecoDestinatario = indiceEnderecoDestinatario;
	}

	public Integer getIndiceContatoDestinatario() {
		return indiceContatoDestinatario;
	}

	public void setIndiceContatoDestinatario(Integer indiceContatoDestinatario) {
		this.indiceContatoDestinatario = indiceContatoDestinatario;
	}

	public Boolean getRenderizaTabelaDestinatario() {
		return renderizaTabelaDestinatario;
	}

	public void setRenderizaTabelaDestinatario(Boolean renderizaTabelaDestinatario) {
		this.renderizaTabelaDestinatario = renderizaTabelaDestinatario;
	}

}
