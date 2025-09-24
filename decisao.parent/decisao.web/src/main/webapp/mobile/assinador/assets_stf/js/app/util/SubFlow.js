'use strict';

StfDecisao.util('SubFlowUtil', function(togglesUtil) {
	
	var SubFlow = function() {
		var self = this;
		var viewCtrls;
		var viewsToggle;
		this.init = function(_viewCtrls, _viewsToggle) {
			viewCtrls = _viewCtrls;
			viewsToggle = _viewsToggle;
		}
		this.irPara = function(id) {
			viewCtrls[id].viewIn();
			viewsToggle.toggle(id);
		};
		this.start = function() {
			for (var id in viewCtrls) { // Vai para o primeiro viewCtrl do fluxo.
				self.irPara(id);
				break;
			}
		};
		var endCallback = null;
		this.setEndCallback = function(callback) {
			endCallback = callback;
		};
		this.end = function() {
			viewsToggle.toggle();
			if (endCallback) {
				endCallback();
			}
		};
	};
	
	this.subFlow = function(sfDefinition) {
		var subFlow = new SubFlow();
		var viewCtrls = {};
		var viewsIds = [];
		for (var id in sfDefinition) {
			viewsIds.push(id);
			viewCtrls[id] = StfDecisao.instanciar(sfDefinition[id], [subFlow]);
		}
		var viewsToggle = togglesUtil.viewsToggle(viewsIds);
		subFlow.init(viewCtrls, viewsToggle)
		return subFlow;
	};
	
});