'use strict';

StfDecisao.util('TogglesUtil', function() {
	var self = this;
	
	this.viewsToggle = function(itemsToToggle) {
		return new ViewsToggle(itemsToToggle);
	};
	
	this.toggle = function(itemsToToggle, clazz) {
		return new Toggle(itemsToToggle, clazz);
	};
	
	var ViewsToggle = function(itemsToToggle) {
		var items = itemsToToggle;
		
		this.addItem = function(item) {
			items.push(item);
		};
		
		this.toggle = function(id, promise) {
			for (var i in items) {
				var currentId = items[i];
				if (currentId == id) {
					$.when(promise).then(function(pId){
						return function() {
							$('#' + pId).show();
						}
					}(currentId));
				} else {
					$('#' + currentId).hide();
				}
			}
		};
	};
	
	var Toggle = function(itemsToToggle, clazz) {
		var self = this;
		var items = itemsToToggle;
		var toggleCallback;
		this.toggle = function(domEl, id, e) {
			if (toggleCallback) {
				toggleCallback(domEl, id, e);
			}
		};
		this.toggleWith = function(event) {
			for (var i in items) {
				var currentId = items[i];
				$('#' + currentId).on(event, (function(id) {
					return function(e) {
						self.toggle(this, id, e);
					}
				})(currentId));
			}
		};
		
		this.toggleCallback = function(callback) {
			toggleCallback = callback;
		};
	};
});