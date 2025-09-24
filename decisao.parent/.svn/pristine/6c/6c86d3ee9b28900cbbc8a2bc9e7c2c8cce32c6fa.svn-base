'use strict';

StfDecisao.service('LoginService', function(browserUtil) {
	var URL_PROTEGIDA = browserUtil.getUrlBase() + '/edecisao';
	var URL_CHECAR_LOGADO = URL_PROTEGIDA + '/seam/resource/rest/security/user/logged';
	var URL_API_CAS = browserUtil.getUrlBaseServidorAutenticacao() + '/cas/api';
	var MSG_ERRO_AUTENTICAR = 'N�o foi poss�vel autenticar. Verifique suas credenciais.';
	var MSG_ERRO_COMUNICACAO_APLICACAO = 'Erro na conex�o com a aplica��o. Tente novamente mais tarde.';
	
	var getServiceBaseUrl = function() {
		//if (URL_PROTEGIDA.indexOf('https://') == 0) {
		//	return URL_PROTEGIDA.replace('https://', 'http://');
		//} else {
			return URL_PROTEGIDA;
		//}
	};
	
	function logarErroAjax(xhr, status){
		console.error('Erro: ' + status);
	};
	
	/**
	 * Faz requisi��es em uma ordem necess�ria ao processo de autentica��o.
	 */
	this.autenticar = function(objetoLogin, prosseguirParaAplicativo, informarErroAoUsuario) {
		// Requisi��o TGT
		$.ajax({
			url: URL_API_CAS+'/tickets', 
			type: 'POST', 
			data: objetoLogin, 
			success: function(data, status, xhr){
				// Aqui vem a URL para fazer o POST da pr�xima requisi��o e pegar o ST
				var tgt_location = xhr.getResponseHeader('Location');
				
				console.log(tgt_location);
				
				// Requisi��o ST
				$.ajax({
					url: tgt_location,
					type: 'POST', 
					data: {service: getServiceBaseUrl() + '/j_spring_cas_security_check'}, 
					success: function(data, status, xhr){
						// Aqui vem a String com o ST
						var st_string = data;
						
						console.log('ST: ' + data);
						
						// Requisi��o para verificar ST com a aplica��o
						$.ajax({
							url: URL_PROTEGIDA + '/j_spring_cas_security_check?ticket=' + st_string,
							type: 'GET', 
							success: function(data, status, xhr){
								console.log('Ticket reconhecido pela aplica��o.');
								
								prosseguirParaAplicativo();
							}, 
							error: function (xhr, status) {
//								logarErroAjax(xhr, status);
//								console.log('Ticket inv�lido.');
//								informarErroAoUsuario(MSG_ERRO_COMUNICACAO_APLICACAO);
								console.log('Algum erro na checagem de ticket reconhecido, mas pode ter sido um redirect para http.');
								prosseguirParaAplicativo();
							}
						});
					}, 
					error: function (xhr, status) {
						logarErroAjax(xhr, status);
						console.log('Falha ao criar ST.');
						informarErroAoUsuario(MSG_ERRO_COMUNICACAO_APLICACAO);
					}
				});
			}, 
			error: function (xhr) {
				logarErroAjax(xhr, status);
				console.log('Falha ao criar TGT.');
				informarErroAoUsuario(MSG_ERRO_AUTENTICAR);
			}
		});
	};
	
	this.efetuarLogout = function(retornarAoLogin){
		$.ajax({
			url: URL_PROTEGIDA + '/j_spring_security_logout',
			type: 'GET',
			success: function(data, status, xhr){
				console.log('Logout efetuado.');
				retornarAoLogin();
			}, 
			error: function (xhr) {
				logarErroAjax(xhr, status);
				console.log('Falha ao deletar TGT.');
			}
		});
	};

	this.efetuarLogoutKeycloak = function() {
		window.location.href = "/edecisao/j_spring_security_logout?logoutSuccessUrl=https://oauth.stf.jus.br/realms/corporativo/protocol/cas/logout?service=" + window.location.origin + "/edecisao/mobile/assinador/";
	};
	
	/**
	 * Checa se o usu�rio pode acessar recurso protegido e chama callback.
	 */
	this.checarSeEstaLogado = function (successCallback, errorCallback){
		/* Requisi��o a um servi�o protegido a fim de criar a sess�o 
		 * (espera-se que o aplicativo guarde o token de sess�o automaticamente, 
		 * como faria o browser)*/
		successCallback(true);
	
	};
});