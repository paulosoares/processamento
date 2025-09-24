'use strict';

StfDecisao.service('MessageService', function MessageService() {
	
	var msgVisualizar = 'Clique aqui para visualiz&#xE1;-los';
	var msgEsconder = 'Clique aqui para escond&#xEA;-los';
	
	var mostrarMensagemVisualizarWarning = true;
	var hasMsgs = false;
	
	this.clear = function() {
		$('#success-panel').hide();
		$('#error-panel').hide();
		$('#warning-panel').hide();
		$('#msg-errors').html('');
		$('#msg-warnings').html('');
		$('#msg-sucesso').html('');
		$('#open-msg-warnings').html(msgVisualizar);
		mostrarMensagemVisualizarWarning = true;
		hasMsgs = false;
	};
	
	this.show = function() {
		if (hasMsgs) {
			$('#painel-mensagens').show();
		} else {
			$('#painel-mensagens').hide();
		}
	}
	
	this.hide = function() {
		$('#painel-mensagens').hide();
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
		appendMsg('error-panel', 'msg-errors', msg);
	};
	this.addWarning = function(msg) {
		hasMsgs = true;
		appendMsg('warning-panel', 'msg-warnings', msg);
	};
	this.addSucesso = function(msg) {
		hasMsgs = true;
		appendMsg('success-panel', 'msg-sucesso', msg);
	}
	
	$('#open-msg-warnings').click(function(e) {
		e.preventDefault();
		mostrarMensagemVisualizarWarning = !mostrarMensagemVisualizarWarning;
		if (mostrarMensagemVisualizarWarning) {
			$(this).html(msgVisualizar);
		} else {
			$(this).html(msgEsconder);
		}
		$('#msg-warnings').slideToggle();
	});
	$('#open-msg-warnings').html(msgVisualizar);
});