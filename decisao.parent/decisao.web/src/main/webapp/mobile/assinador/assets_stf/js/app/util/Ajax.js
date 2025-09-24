'use strict';

StfDecisao.util('AjaxUtil', function(win, nav) {
	var self = this;
	
	var estaOffline = function(xhr) {
		return !nav.onLine;
	};
	
	this.ajax = function(config) {
		var deferred = $.Deferred();
		$.ajax(config).done(function(data) {
			deferred.resolve(data);
		}).fail(function(xhr) {
			if (estaOffline(xhr)) {
				deferred.reject({'errors': ['Erro ao conectar à Internet. Tente novamente mais tarde.'], 'erroImpeditivo': true, 'erroFatal': true});
			} else {
				try {
					if (xhr.status === 0) { // O status 0 foi a forma de detectar o erro de CORS ao redirecionar para https://oauth.stf.jus.br quando a autenticação expirar
						win.location.href = "/edecisao/j_spring_security_logout?logoutSuccessUrl=https://oauth.stf.jus.br/realms/corporativo/protocol/cas/logout?service=" + window.location.origin + "/edecisao/mobile/assinador/";
					} else {
						var data = $.parseJSON(xhr.responseText);
						deferred.reject(data);
					}
				} catch (err) {
					if (xhr.responseText.indexOf('action="/cas/login?service=') > -1) {
						console.log('redirect do cas');
						win.location = 'index.jsp';
					} else {
						console.log('não é redirect do cas');
					}
				}
			}
		});
		return deferred.promise();
	};
	
});