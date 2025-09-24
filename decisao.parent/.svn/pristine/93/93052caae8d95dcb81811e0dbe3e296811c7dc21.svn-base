'use strict';

StfDecisao.service('MessageServiceNative', function() {
	var msgVisualizar = 'Clique aqui para visualiz&#xE1;-los';
	var msgEsconder = 'Clique aqui para escond&#xEA;-los';
	
	var mostrarMensagemVisualizarWarning = true;
	var hasMsgs = false;
	
	this.clear = function() {
		$('#success-panel-native').hide();
		$('#error-panel-native').hide();
		$('#warning-panel-native').hide();
		$('#msg-errors-native').html('');
		$('#msg-warnings-native').html('');
		$('#msg-sucesso-native').html('');
		$('#open-msg-warnings-native').html(msgVisualizar);
		mostrarMensagemVisualizarWarning = true;
		hasMsgs = false;
	};
	
	this.show = function() {
		if (hasMsgs) {
			$('#painel-mensagens-nativo').show();
		} else {
			$('#painel-mensagens-nativo').hide();
		}
	}
	
	var append = function(idPanel, idMsg, msg) {
		var el = $('#' + idMsg);
		el.append('<div class="msg-item-div">' + msg + '</div>');
		$('#' + idPanel).show();
	}
	
	var appendMsg = function(idPanel, idMsg, msgs) {
		if (typeof msgs === 'string') {
			append(idPanel, idMsg, msgs);
		} else {
			for (var i in msgs) {
				append(idPanel, idMsg, msgs[i]);
			}
		}
	};
	
	this.addError = function(msg) {
		hasMsgs = true;
		appendMsg('error-panel-native', 'msg-errors-native', msg);
	};
	this.addWarning = function(msg) {
		hasMsgs = true;
		appendMsg('warning-panel-native', 'msg-warnings-native', msg);
	};
	this.addSucesso = function(msg) {
		hasMsgs = true;
		appendMsg('success-panel-native', 'msg-sucesso-native', msg);
	}
	
	$('#open-msg-warnings-native').click(function(e) {
		e.preventDefault();
		mostrarMensagemVisualizarWarning = !mostrarMensagemVisualizarWarning;
		if (mostrarMensagemVisualizarWarning) {
			$(this).html(msgVisualizar);
		} else {
			$(this).html(msgEsconder);
		}
		$('#msg-warnings-native').slideToggle();
	});
	$('#open-msg-warnings-native').html(msgVisualizar);
});