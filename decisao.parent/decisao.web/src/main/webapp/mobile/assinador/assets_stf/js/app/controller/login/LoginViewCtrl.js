'use strict';

/**
 * ViewCtrl para controlar a view de Login.
 *
 * @param loginService LoginService
 * @param flowService FlowService
 */
StfDecisao.viewCtrl('LoginViewCtrl', 'login', function(loginService, flowService) {
	var self = this;
	
	/**
	 * Seletores
	 */
	var SELETOR_TELA_LOGIN = "#login";
	var SELETOR_CAMPO_USUARIO = '#usuario';
	var SELETOR_CAMPO_SENHA = '#senha';
	var SELETOR_CAMPO_BOTAO_LOGIN = '#botaoLogin';
	var SELETOR_BOTAO_LOGOUT = '.botaoLogout';
	var SELETOR_MENSAGEM_ERRO = '.mensagem-erro-login';
	var SELETOR_FORMULARIO_LOGIN = '#div-formulario-login';
	var SELETOR_LOADER_LOGIN = '#div-loader-login';
	
	var ANIMACAO_LOGIN = 'slideOutLeft';
	var ANIMACAO_LOGOUT = 'slideInLeft';
	
	var urlTgtCorrente = '';
	
	function getObjetoLogin() {
		return {
			username: $(SELETOR_CAMPO_USUARIO).val(),
			password: $(SELETOR_CAMPO_SENHA).val()
		};
	};
	
	function informarErroAoUsuario(mensagem) {
		$(SELETOR_FORMULARIO_LOGIN).show();
		$(SELETOR_LOADER_LOGIN).hide();
		$(SELETOR_MENSAGEM_ERRO).show();
		$(SELETOR_MENSAGEM_ERRO).text(mensagem ? mensagem : '');
	}
	
	this.viewOut = function() {
		$('.mensagem-erro-login').hide();
		$('.escondido-ate-login').show();
		$(SELETOR_TELA_LOGIN)
			.removeClass(['animated', ANIMACAO_LOGIN, ANIMACAO_LOGOUT].join(' '))
			.addClass(['animated', ANIMACAO_LOGIN].join(' '));
	}
	
	this.viewIn = function() {
		$('.escondido-ate-login').hide();
		$(SELETOR_TELA_LOGIN)
			.removeClass(['animated', ANIMACAO_LOGIN, ANIMACAO_LOGOUT].join(' '))
			.addClass(['animated', ANIMACAO_LOGOUT].join(' '));
	}
	
	function autenticar() {
		var objLogin = getObjetoLogin();
		if (!objLogin.username || !objLogin.password) {
			informarErroAoUsuario("Usuário e senha são obrigatórios.");
		} else {
			$('#div-formulario-login').hide();
			$('#div-loader-login').show();
			loginService.autenticar(objLogin, function() {
				$('#div-formulario-login').show();
				$('#div-loader-login').hide();
				flowService.irParaSistema();
			}, informarErroAoUsuario);
			$(SELETOR_CAMPO_USUARIO).val('');
			$(SELETOR_CAMPO_SENHA).val('');
		}
	}
	
	function efetuarLogout(e) {
		e.preventDefault();
		loginService.efetuarLogoutKeycloak();
	}
	
	function registerEvents() {
		$(SELETOR_CAMPO_BOTAO_LOGIN).click(autenticar);
		$(SELETOR_BOTAO_LOGOUT).click(efetuarLogout);
		$(SELETOR_CAMPO_SENHA).keypress(function(e) {
			if (e.which == 13) {
				autenticar();
			}
		});
	}
	
	registerEvents();
});