'use strict';

StfDecisao.viewCtrl('ImportarCertificadoViewCtrl', 'importacao-container', function(subFlowUtil, mni, flowServiceResolver, msgService, certificadoUtil) {
	var self = this;
	
	var SELETOR_SELF = '#importacao-container';
	
	this.finishedCallback = null;
	
	this.setFinishedCallback = function(fc) {
		self.finishedCallback = fc;
	};
	
	function irParaSistema() {
		mni.zerarImportacao();
		StfDecisao.loginService.checarSeEstaLogado(function(estaLogado) {
			if (estaLogado) {
				flowServiceResolver.instance.irParaSistema();
			} else {
				flowServiceResolver.instance.irParaLogin();
			}
		}, function() {
			flowServiceResolver.instance.irParaLogin();
		});
	}
	
	var subFlowDefinition = {};
	var FLOW_IMPORTAR = 'painel-importar';
	subFlowDefinition[FLOW_IMPORTAR] = function(subFlow) {
		var subFlowSelf = this;
		var SELETOR_SENHA_IMPORTACAO = '#senha-importacao';
		var SELETOR_BOTAO_CONTINUAR = "#continuar-importacao";
		var SELETOR_BOTAO_CANCELAR = "#cancelar-importacao";
		this.viewIn = function() {
			
		};
		var registerEvents = function() {
			$(SELETOR_BOTAO_CONTINUAR).click(function() {
				var senha = $(SELETOR_SENHA_IMPORTACAO).val();
				$(SELETOR_SENHA_IMPORTACAO).val(''); // Zera a senha rapidamente da tela por questões de segurança.
	            if (senha) {
	            	msgService.clear();
	                mni.recuperarDadosImportacao(senha).done(function(data) {
	                	subFlow.irPara(FLOW_CONFIRMAR_IMPORTACAO);
	                    $('#dados-importacao').html(certificadoUtil.cadeiaInHtml(data));
	                }).fail(function(data) {
	                	msgService.addError(data.errors[0]);
	                	msgService.show();
	                });
	            } else {
	            	msgService.addError("A senha estava vazia.");
                	msgService.show();
	            }
			});
			$(SELETOR_BOTAO_CANCELAR).click(function() {
				subFlow.end();
			});
		};
		registerEvents();
	};
	
	var FLOW_CONFIRMAR_IMPORTACAO = 'painel-importar-com-confirmacao';
	subFlowDefinition[FLOW_CONFIRMAR_IMPORTACAO] = function(subFlow) {
		var subFlowSelf = this;
		var SELETOR_BOTAO_CANCELAR_IMPORTACAO = '#cancelar-importacao-com-confirmacao';
		var SELETOR_BOTAO_CONFIRMAR_IMPORTACAO = '#continuar-importacao-com-confirmacao';
		this.viewIn = function() {
			
		};
		var registerEvents = function() {
			$(SELETOR_BOTAO_CANCELAR_IMPORTACAO).click(function() {
				subFlow.end();
			});
			
			$(SELETOR_BOTAO_CONFIRMAR_IMPORTACAO).click(function() {
				mni.importarCertificado().done(function(data) {
					subFlow.irPara(FLOW_ULTIMO_PASSO);
					msgService.addSucesso("Certificado importado com sucesso.");
					msgService.show();
				}).fail(function(data) {
					subFlow.irPara(FLOW_ERRO_IMPORTACAO);
					msgService.addError(data.errors[0]);
					msgService.show();
				});
			});
		}
		registerEvents();
	};
	
	var FLOW_ULTIMO_PASSO = 'ultimo-passo-importacao';
	subFlowDefinition[FLOW_ULTIMO_PASSO] = function(subFlow) {
		var subFlowSelf = this;
		var SELETOR_BOTAO_CONTINUAR_SUCESSO = '#btn-importacao-fechar';
		this.viewIn = function() {
			
		};
		var registerEvents = function() {
			$(SELETOR_BOTAO_CONTINUAR_SUCESSO).click(function() {
				subFlow.end();
			});
		}
		registerEvents();
	};
	
	var FLOW_ERRO_IMPORTACAO = 'reset-importacao';
	subFlowDefinition[FLOW_ERRO_IMPORTACAO] = function(subFlow) {
		var subFlowSelf = this;
		var SELETOR_BOTAO_CONTINUAR_SEM_IMPORTACAO = '#voltar-sem-importacao';
		this.viewIn = function() {
			
		};
		var registerEvents = function() {
			$(SELETOR_BOTAO_CONTINUAR_SEM_IMPORTACAO).click(function(){
				subFlow.end();
			});
		}
		registerEvents();
	};
	
	this.subFlow = subFlowUtil.subFlow(subFlowDefinition);
	this.subFlow.setEndCallback(function() {
		msgService.clear();
		$(SELETOR_SELF).hide();
		if (self.finishedCallback) {
			self.finishedCallback();
		} else {
			irParaSistema();
		}
	});
	
	function registerEvents() {
		mni.setCallbackIniciarImportacaoCertificado(function() {
			$('.escondido-ate-login').hide();
			flowServiceResolver.instance.reset();
			self.finishedCallback = null;
			$(SELETOR_SELF).show();
			self.subFlow.start();
		});
	};
	
	this.show = function() {
		$(SELETOR_SELF).show();
		self.subFlow.start();
	}
	
	registerEvents();
});