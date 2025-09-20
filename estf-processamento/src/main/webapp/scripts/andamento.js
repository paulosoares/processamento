

function confirmarMudancaTipoInclusao(nomeCampoExisteProcessoNaLista) {
	var existeProcesso = getValorBooleanDoCampo(nomeCampoExisteProcessoNaLista);
	if (existeProcesso) {
		var cnfrm = confirm('Existem processos inclu\u00eddos na lista e estes ser\u00e3o retirados para a inclus\u00e3o dos novos. \nDeseja continuar?');
		if (cnfrm != true) {
			return false;
		}
	}
	return true;

}

function getValorBooleanDoCampo(nomeDoCampo) {
	return document.getElementById(nomeDoCampo).value == 'true';
}

function aguarde(mostrar, div) {
	if (mostrar == true) {
		document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
	}
}
function caixaAlta(campo) {
	campo.value = campo.value.toUpperCase();
}
function limpar() {
	document.getElementById('btLimpar').click();
}
function noEnter(event) {
	var tecla = event.which;
	if (tecla == null)
		tecla = event.keyCode;
	return !(tecla && tecla == 13);
}

// Esse mï¿½todo ï¿½ chamado pela botï¿½o de lancar andamento.
// Se retornar true, o botï¿½o vai disparar o action para o bean registrar
// o andamento.
// Se retornar false, o registro de andamento ï¿½ cancelado.
function registrarAndamento() {
	
	if (precisaConfirmacaoAndamento) {
		// Se o usuario confirmar o lancamento do andamento.
		if (confirmarLancamentoAndamento()) {
			// Confirma baixa de processo fora do setor do usuï¿½rio antes de prosseguir o lanï¿½amento
   			if (!confirmaBaixaForaSetor()) {
   				return false;
   			}

			// Se houver pedido adicional (peticao, processo, origem
			// decisao, etc), nao registrar por agora.
			return !pedirInformacaoAdicional();
		} else {
			return false;
		}

		// Lancar o andamento.
	} else {
		return !pedirInformacaoAdicional();
	}
}

function solicitarPresidenteInterino() {

	var solicitarPresidenteInterino;
	solicitarPresidenteInterino = document
			.getElementById('idSolicitarPresidenteInterino').value == 'true';

	if (solicitarPresidenteInterino) {
		Richfaces.showModalPanel('modalPanelPresidenteInterino');
		return true;
	} else {

		return false;
	}
}

function solicitarOrigemDecisao() {
	var solicitarOrigemDecisao;
	solicitarOrigemDecisao = document.getElementById('idSolicitarOrigemDecisao').value === 'true';

	if (solicitarOrigemDecisao) {
		Richfaces.showModalPanel('modalPanelOrigemDecisao');
		return true;
	} else {

		return false;
	}
}
function solicitaValidacao(temOrigemSelecionada, origemEstaIntegrada, verificaNumUnico, verificaNumOrigem, unicaBaixa) {
	
	if(!temOrigemSelecionada) {
		alert('Favor selecione uma origem cadastrada.'); 
		return false;
	}
	
	
	if (!origemEstaIntegrada) {
		if (!confirm('ATEN\u00c7\u00c3O: A origem selecionada n\u00e3o est\u00e1 integrada. Deseja realmente baixar o processo para essa origem?')) {
			return false;
		}
	}

	if (!verificaNumUnico){
		if(!confirm('Esse processo n\u00e3o possui o n\u00famero \u00fanico cadastrado. A inclus\u00e3o dessa informa\u00e7\u00e3o \u00e9 importante para que a origem localize esse processo depois de devolvido eletronicamente. Deseja confirmar a baixa sem informar o n\u00famero \u00fanico?')){
			return false;
		}		
	}
	
	if (!verificaNumOrigem){
		if(!confirm('N\u00e3o h\u00e1 indicativo de sigla e n\u00famero do processo de origem. Essa informa\u00e7\u00e3o \u00e9 importante para que a origem localize esse processo. Deseja confirmar a baixa sem informar o n\u00famero de origem?')){
			return false;
		}	
	}
	
	if (!unicaBaixa){
		if(!confirm('ATEN\u00c7\u00c3O: Processo j\u00e1 baixado. Deseja continuar?')){
			return false;
		}	
	}
	
	return true;
}

function executarBotaoLimpar(){
	document.getElementById('btnLimpar').click();
}

function processoPrincipalOnClick() {
	if (document.getElementById('qtdProcessosTemasSelecionados').value === 0) {
		alert('Favor informar o processo.'); 
		return;
	} else if (document.getElementById('idApensos').value == '') {
		alert('Favor informar a quantidade de apensos.'); 
		return;
	} else {
		Richfaces.hideModalPanel('modalPanelAnexarProcesso');
		if (!pedirInformacaoAdicional()) {
			document.getElementById('btSalvarAndamento').click();
		}
	}
}

function processoTemaOnClick() {
	if (document.getElementById('qtdProcessosTemasSelecionados').value === 0) {
		alert('Favor informar o tema.'); 
		return;
	} else {
		Richfaces.hideModalPanel('modalPanelAnexarTema');
		if (!pedirInformacaoAdicional()) {
			document.getElementById('btSalvarAndamento').click();
		}
	}
}

function processosTemasOnClick() {
	if (document.getElementById('qtdProcessosTemasSelecionados').value === 0) {
		alert('Favor informar o tema.'); 
		return;
	} else {
		Richfaces.hideModalPanel('modalPanelAnexarTema');
		if (!pedirInformacaoAdicional()) {
			document.getElementById('btSalvarAndamento').click();
		}
	}
}


function adicionarProcessoPrincipaisOnClick() {
	if (document.getElementById('identificacaoProcessos').value == '') {
		alert('Favor informar o processo.'); 
		return false;
	} else if (document.getElementById('idProcesso').value == document.getElementById('identificacaoProcessos').value) {
		alert('Um processo n\u00e3o pode ser sobrestado a ele mesmo.');
		return false;
	} else if (document.getElementById('idApensosProcessos').value == '') {
		alert('Favor informar a quantidade de apensos.'); 
		return false;
	} else {
		return true;
	}
}

function processosPrincipaisOnClick() {
	if (document.getElementById('idTabelaProcessos:tb').childNodes.length == 0) {
		alert('Favor informar os processos.'); 
	} else {
		Richfaces.hideModalPanel('modalPanelAnexarProcessos');
		if (!pedirInformacaoAdicional()) {
			document.getElementById('btSalvarAndamento').click();
		}
	}
}

function lancarAndamentoIndevidoOnClick() {

	// Se nï¿½o estivermos cancelando um andamento indevido. 
	if (document.getElementById('idCancelarAndamentoIndevido').value === 'false') {

		if ((document.getElementById('observacao').value == '') || (document.getElementById('observacaoInterna').value == '')) {
			Richfaces.showModalPanel('modalPanelInformacoesLancamentoIndevido');
			return false;
		
		} else { 
			return validarTamanhoObservacoes('observacao','observacaoInterna');
		}
	} else { // Estamos cancelando um andamento indevido.
		return true;
	}
}

function lancarAndamentoIndevidoModalOnClick() {
	if (document.getElementById('idObservacaoIndevido').value == '') {
		alert('Favor informar a observa\u00e7\u00e3o.');
		return false;
	} else {
		if (validarTamanhoObservacoes('idObservacaoIndevido', 'idObservacaoInternaIndevido')) {
			Richfaces.hideModalPanel('modalPanelInformacoesLancamentoIndevido');
			return true;
		} else {
			return false;
		}
	}
}

function pedirInformacaoAdicional() {

	var solicitarProcesso;
	var solicitarProcessos;
	var solicitarPeticao;
	var solicitarOrigemDecisao;
	var solicitarTipoDevolucao;
	var solicitarPresidenteInterino;
	var precisaVerificarCodigoOrigem;
	var precisaListarDecisoes;
	var solicitarTema;
	var qtdTemasSelecionados;

	solicitarProcesso = document.getElementById('idSolicitarProcessoEObservacao').value === 'true';
	solicitarProcessos = document.getElementById('idSolicitarProcessos').value === 'true';
	solicitarPeticao = document.getElementById('idSolicitarPeticao').value === 'true';
	solicitarOrigemDecisao = document.getElementById('idSolicitarOrigemDecisao').value === 'true';
	solicitarTipoDevolucao = document.getElementById('idSolicitarTipoDevolucao').value === 'true';
	solicitarPresidenteInterino = document.getElementById('idSolicitarPresidenteInterino').value === 'true';
	precisaVerificarCodigoOrigem = document.getElementById('idPrecisaVerificarCodigoOrigem').value === 'true';
	solicitarTema = document.getElementById('idSolicitarTema').value === 'true';	
	qtdTemasSelecionados = document.getElementById('qtdProcessosTemasSelecionados').value;
	precisaListarDecisoes = document.getElementById('idPrecisaListarDecisoes').value === 'true';
   //console.log('Processo: '+ document.getElementById('idProcesso').value + ', andamento: '+document.getElementById('idAndamento').value+ ', precisaVerificarCodigoOrigem: '+ precisaVerificarCodigoOrigem);
	if (precisaListarDecisoes) {
		if (document.getElementById('btAbrirTelaListarDecisoes') != null) {
			document.getElementById('btAbrirTelaListarDecisoes').click();
			return true;
		} else {
			return false;
		} 
	} else if (precisaVerificarCodigoOrigem) {
		if (document.getElementById('btAbrirTelaConfirmacaoOrigem') != null) {
			document.getElementById('btAbrirTelaConfirmacaoOrigem').click();
			return true;
		} else {
			return false;
		} 
	} else if (solicitarProcesso && document.getElementById('identificacaoProcesso').value == '') {
		Richfaces.showModalPanel('modalPanelAnexarProcesso');
		return true;
	} else if (solicitarTema && qtdTemasSelecionados == 0) {
		document.getElementById("idBotaoRerenderModal").click();
		Richfaces.showModalPanel('modalPanelAnexarTema');
		return true;		
	} else if (solicitarProcessos && document.getElementById('idTabelaProcessos:tb').childNodes.length == 0) {
		Richfaces.showModalPanel('modalPanelAnexarProcessos');
		return true;
	} else if (solicitarPeticao) {
		Richfaces.showModalPanel('modalPanelAnexarPeticao');
		return true;
	} else if (solicitarOrigemDecisao && document.getElementById('idComboOrigemDecisao').value == -1) {
		Richfaces.showModalPanel('modalPanelOrigemDecisao');
		return true;
	} else if (solicitarTipoDevolucao && document.getElementById('idComboDevolucao').value == -1) {
		Richfaces.showModalPanel('modalPanelDevolucao');
		return true;
	} else if (solicitarPresidenteInterino && document.getElementById('idComboPresidenteInterino').value == -1) {
		Richfaces.showModalPanel('modalPanelPresidenteInterino');
		return true;
	} else {
		return false;
	}
}

function limparDadosModais() {
	document.getElementById('identificacaoProcesso').value = '';
	document.getElementById('idComboOrigemDecisao').value = -1;
	document.getElementById('idComboDevolucao').value = -1;
	document.getElementById('idComboPresidenteInterino').value = -1;
	document.getElementById('idComboOrigemDevolucao').value = -1;
	document.getElementById('identificacaoProcessos').value = '';
	document.getElementById('identificacaoTema').value = '';
	document.getElementById('idApensos').value = '';
	document.getElementById('idApensosProcessos').value = '';
	//document.getElementById('idApensosTema').value = '';
}

function precisaConfirmacaoAndamento() {

	var confirmar;
	confirmar = document.getElementById('idPrecisaConfirmacaoLancarAndamento').value === 'true';

	return confirmar;
}

//
function confirmarLancamentoAndamento() {

	var mensagemConfirmacao;
	var confirmar;

	mensagemConfirmacao = document.getElementById('idMensagemConfirmacaoLancarAndamento').value;
	confirmar = document.getElementById('idPrecisaConfirmacaoLancarAndamento').value === 'true';

	if (confirmar) {
		return confirm(mensagemConfirmacao + ' Gostaria de continuar?');
	} else {
		return true;
	}
}

// verifica se dentro da string digitada, possui algum nï¿½mero, se tiver,
// exibe o suggestionBox
function exibirSuggestionBoxPesquisaProcesso(campoProcesso, suggestionBox) {
	var valor = campoProcesso.value;
	var possui = false;
	var i;
	for (i = 0; i < valor.length; i++) {
		// possui valor entre 0 e 9
		if (valor.charCodeAt(i) >= 48 && valor.charCodeAt(i) <= 57) {
			possui = true;
			break;
		}
	}

	if (possui) {
		suggestionBox.callSuggestion(true);
	} else {
		suggestionBox.hide();
	}
}

function exibirSuggestionBox(campo, suggestionBox) {
	suggestionBox.callSuggestion(true);
}

function verificarConfirmacaoSelecaoProcesso() {

	var mensagem;
	var confirmarSelecaoProcesso;

	if (document.getElementById('idConfirmarSelecaoProcesso')) {

		mensagem = document.getElementById('idMensagemConfirmacaoProcesso').value;
		confirmarSelecaoProcesso = document
				.getElementById('idConfirmarSelecaoProcesso').value === 'true';

		if (confirmarSelecaoProcesso) {
			if (!confirm(mensagem + ' Gostaria de continuar?')) {
				limpar();
			} else {
				confirmarSelecaoProcesso = false;
			}
		}
	}
}


function verificaAlteracaoTipoGuia () {
	
			if (document.getElementById('chkTipoPesquisa').value == 'PRO') 
				{
				document.getElementById('chkTipoPesquisa:0').checked = 1;
				document.getElementById('chkTipoPesquisa:1').checked = 0;
				} 
			if (document.getElementById('chkTipoPesquisa').value == 'PET') 
				{
				document.getElementById('chkTipoPesquisa:0').checked = 0;
				document.getElementById('chkTipoPesquisa:1').checked = 1;
				}
			return false;
}	

function validarTamanhoObservacoes(idObservacao, idObservacaoInterna) {
	var obs = true;
	var obs_interna = true;
	var mensagem;
	if(document.getElementById(idObservacaoInterna).value.length > 200) {
		obs_interna = false;
	}
	if(document.getElementById(idObservacao).value.length > 4000) {
		obs = false;
	}
	if ((obs == false) || (obs_interna == false)) {
		mensagem = 'O texto excedeu o limite de tamanho:\n';
		if (obs == false) {
			mensagem = mensagem + '\n "Observa\u00e7\u00e3o" - m\u00e1ximo at\u00e9 4000 caracteres. Foram digitados ' + document.getElementById(idObservacao).value.length + ' caracteres.\n';
		} 
		if (obs_interna == false) {
			mensagem = mensagem + '\n "Obs. interna" - m\u00e0ximo at\u00e9 200 caracteres. Foram digitados ' + document.getElementById(idObservacaoInterna).value.length + ' caracteres.' ;
		}
		alert(mensagem);
		return false;
	} else {
		return true;
		}
	}

function confirmaParaVariosProcessos(){
	
	if(!confirm('Gostaria de registrar o andamento para os processos selecionados?')) {
	   return false;
    } else {
    	if(verficaProcessoFindoLista()) { 
    		if(!confirm('Existe processo findo na lista. Deseja continuar?')) { 
    		  return false;
    		} else {
    			if (!validaForm()) {
    				return false;
    			} else {
    		        return true;
    			}
    		} 
    	} else {
			if (!validaForm()) {
				return false;
			} else {
		        return true;
			}
    	}	
    }
}

function validaForm() {
	if (!validarTamanhoObservacoes('observacao','observacaoInterna')) {
		return false;
	} else if (document.getElementById('idAndamento').value == '') {
		alert('Favor selecione um andamento para ser registrado.'); 
		return false;
	} else {
		return registrarAndamento(); 
	}
}

function validaFormApensos(possuiApensos) {
	if (!validarTamanhoObservacoes('observacao','observacaoInterna')) {
		return false;
	} else if (document.getElementById('idAndamento').value == '') {
		alert('Favor selecione um andamento para ser registrado.'); 
		return false;
	} else {
		if (possuiApensos) {
			//if (confirm('Este processo possui apenso(s). Deseja continuar?'))
			if (confirm(document.getElementById('mensagemApensos').value))
				return  registrarAndamento();
		}
		else {
			return registrarAndamento(); 
		}
	}
}

function confirmLancarAndamentoProcessoFindo() {
	var processoFindo;

	processoFindo = document.getElementById('idProcessoFindo').value === 'true';

	if (processoFindo) {
		if (confirm('Este processo \u00e9 findo. Deseja continuar?')) {
    		return true;
		} else {
    		return false;
		}
	} else {
		return true;
	}
}

function verficaProcessoFindoLista(){
	if(document.getElementById('idProcessoFindoLista').value == 'true'){
		return true;
	}
	return false;	
}	

function gerarCertidaoBaixa(){
	if (document.getElementById('hidSucesso').value == 'false'){
		return false;
	}
	if (document.getElementById('hidTemCertidao').value == 'false'){
		return false;
	}
	
	if ((document.getElementById('hidCodigoAndamento').value == '7104' || 
		document.getElementById('hidCodigoAndamento').value == '7101' ||
		document.getElementById('hidCodigoAndamento').value == '7108')) {
		document.getElementById('abrePdfCertidaoBaixa').click();
		return true;
	}
	return false;
}

function verificaPendenciasEApensos(existemPendencias, possuiApensos) {
	if (existemPendencias){
		Richfaces.showModalPanel('modalPanelConfirmacaoRegistro');
	} else {
		return confirmaLancamentoEApensos(possuiApensos);
	}
}

function verificaPendencias(existemPendencias) {
	if (existemPendencias){
		Richfaces.showModalPanel('modalPanelConfirmacaoRegistro');
	} else {
		return confirmaLancamento();
	}
}

function confirmaLancamentoEApensos(possuiApensos) {
	if(confirmLancarAndamentoProcessoFindo()) {
		if (!validaFormApensos(possuiApensos)) {
			return false;
		} else {
			return true;
		}
	} else {
		return false;
	}
}

function confirmaLancamento() {
	if(confirmLancarAndamentoProcessoFindo()) {
		if (!validaForm()) {
			return false;
		} else {
			return true;
		}
	} else {
		return false;
	}
}

function confirmaBaixaForaSetor(){
	if (document.getElementById('hidProcessoDifereSetorUsuario').value === 'true'){
		if (confirm( "O processo n\u00e3o se encontra neste Setor. Caso opte por continuar, o processo ser\u00e1 deslocado para esse Setor e, posteriormente, para a Origem selecionada. Deseja continuar?")){
			document.getElementById('btConfirmarDeslocamento').click();
			return true;
		} else {
			return false;
		}
	} else {
		return true;
	}
}

function limparCampoIdentificacaoTema(){
	document.getElementById("identificacaoTema").value = "";
}

/*Aqui devem ser fetias as validções adicionais específicas para cada código. O return false impede a continuidade do botão "Registrar", desse modo a continuidade fica a cargo da modal. */
function validacoesAdicionais(){
	if (document.getElementById('hidCodigoAndamento').value == '8558'){
		Richfaces.showModalPanel('modalPanelConfirmacaoAndamento');
		return true;
	}
	
	//Somente continua com a ação do botão "Registrar";
	document.getElementById('idBotaoRegistrarAndamento').click();
}

function executarRegistrarAndamento(mensagemDeRestricaoRegistroDeAndamento, alertaApensos, validarDadosBaixa){
	if (!verificaPendenciasEApensos(mensagemDeRestricaoRegistroDeAndamento,alertaApensos)) {
		return false;
	} else {
		if (validarDadosBaixa){
			document.getElementById('idBotaoValidaBaixa').click();
		} else {
			return validacoesAdicionais();
		}
	}
}

function submitCheckedRadio() {
	var radios = document.getElementsByName('idTabelaOrigensCadastradas:idRadiosOrigens');
	var value;
	for (var i = 0; i < radios.length; i++) {
		if (radios[i].type === 'radio' && radios[i].checked) {
			// get value, set checked flag or do whatever you need to
			value = radios[i].value;
			setOrigemSelecionada(value);
			break;
		}
	}
}