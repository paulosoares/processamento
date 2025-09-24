'use strict';

StfDecisao.util('PromiseUtil', function() {
	
	var _setUpPromiseChain = function(chain, deferred) {
		var index = 0;
		var size = chain.length;
		var currentItem = chain[index];
		var currentPromise = currentItem();
		
		var registerCallbacks = function() {
			currentPromise.done(function(data) {
				index++;
				if (index < size) {
					currentItem = chain[index];
					currentPromise = currentItem(data);
					registerCallbacks();
				} else {
					deferred.resolve(data);
				}
			});
			
			currentPromise.fail(function(data) {
				deferred.reject(data);
			});
			currentPromise.progress(function(data) {
				deferred.notify(data);
			});
		};
		registerCallbacks();
	};
	
	this.encadear = function() {
		var deferred = $.Deferred();
		
		_setUpPromiseChain(arguments, deferred);
		
		return deferred.promise();
	};
});