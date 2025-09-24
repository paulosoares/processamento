'use strict';

StfDecisao.service('LoaderService', function() {
	var self = this;
	
	this.loaderOn = function() {
		$('#div-loader').show();
	}
	
	this.loaderOff = function() {
		$('#div-loader').hide();
	}
});