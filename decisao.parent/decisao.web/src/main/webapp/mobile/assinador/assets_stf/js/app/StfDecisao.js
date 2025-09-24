/**
 * Esse arquivo deve ser incluído no HTML antes de todos os outros arquivos dentro de app.
 */

/**
 * Definição do namespace StfDecisao
 */
var StfDecisao = StfDecisao || {};

/**
 * Closure para não poluir o escopo global.
 */
+function(StfDecisao) {
	StfDecisao._definitions = {};

	var _criarDefinitionNamespace = function(defName, refName, withId) {
		(function(def, ref) {
			StfDecisao[ref] = {};
			StfDecisao._definitions[ref] = {};
			var defFunction = function(name, definitionFn) {
				if (StfDecisao._definitions[ref].hasOwnProperty(name)) {
					throw defName + " [" + name + "] já foi definido.";
				} else {
					StfDecisao._definitions[ref][name] = definitionFn;
				}
			};
			
			StfDecisao[ref].instanciar = function(name, params) {
				if (StfDecisao._definitions[ref].hasOwnProperty(name)) {
					var definitionFn = StfDecisao._definitions[ref][name];
					return _instanciar(definitionFn, params);
				} else {
					throw def + " [" + name + "] não foi definido.";
				}
			};
			if (withId) {
				StfDecisao._definitions[ref]['ids'] = {};
				StfDecisao[def] = function(name, id, definitionFn) {
					StfDecisao._definitions[ref]['ids'][name] = id;
					defFunction(name, definitionFn);
				};
				StfDecisao[ref].getId = function(name) {
					if (StfDecisao._definitions[ref]['ids'].hasOwnProperty(name)) {
						return StfDecisao._definitions[ref]['ids'][name];
					} else {
						throw "id para " + " [" + name + "] não foi encontrado.";
					}
				};
			} else {
				StfDecisao[def] = defFunction;
			}
		})(defName, refName);
	}
	
	var _instanciar = function(definitionFn, params) {
		var instance = Object.create(definitionFn.prototype);
		definitionFn.apply(instance, params);
		return instance;
	};
	
	StfDecisao.instanciar = function(definitionFn, params) {
		return _instanciar(definitionFn, params);
	};
	
	_criarDefinitionNamespace('component', 'components');
	_criarDefinitionNamespace('util', 'utils');
	_criarDefinitionNamespace('facade', 'facades');
	_criarDefinitionNamespace('service', 'services');
	_criarDefinitionNamespace('viewCtrl', 'viewCtrls', true);
	_criarDefinitionNamespace('model', 'models');
	
	StfDecisao.component('AssinadorApp', function() {
		var preInitSteps = [];
		var initSteps = [];
		var initStepAfterHooks = [];
		
		this.addPreInitStep = function(name, stepFunc, override) {
			if (!override) {
				preInitSteps.push({"name": name, "fn": stepFunc});
			} else {
				for (var i in preInitSteps) {
					if (preInitSteps[i].name == name) {
						preInitSteps[i]['fn'] = stepFunc;
						break;
					}
				}
			}
		};
		
		this.addInitStep = function(name, stepFunc, override) {
			if (!override) {
				initSteps.push({"name": name, "fn": stepFunc});
			} else {
				for (var i in initSteps) {
					if (initSteps[i].name == name) {
						initSteps[i]['fn'] = stepFunc;
						break;
					}
				}
			}
		};
		
		this.addInitStepAfterHook = function(name, stepFunc) {
			initStepAfterHooks.push({"name": name, "fn": stepFunc});
		};
		
		this.init = function() {
			$.when().then(executePreInitSteps).then(executeInitSteps);
		};
		
		function executeInitStepAfterHooks(name) {
			if (initStepAfterHooks.length > 0) {
				for (var i in initStepAfterHooks) {
					if (initStepAfterHooks[i]["name"] == name) {
						initStepAfterHooks[i]["fn"]();
					}
				}
			}
		}
		
		function executeInitSteps() {
			if (initSteps.length > 0) {
				for (var i in initSteps) {
					initSteps[i]["fn"]();
					executeInitStepAfterHooks(initSteps[i]["name"]);
				}
			}
		}
		
		function executePreInitSteps() {
			var currentPromise = $.when();
			if (preInitSteps.length > 0) {
				for (var i in preInitSteps) {
					currentPromise = currentPromise.then(preInitSteps[i]["fn"]);
				}
			}
			return currentPromise;
		}
	});
	
	StfDecisao.carregarView = function(url, selector) {
		return function() {
			if (!selector) {
				selector = 'body';
			}
			
			var deferred = $.Deferred();
			
			$.ajax({
				'url': url,
				cache: false,
				dataType: 'html',
				beforeSend: function(xhr) {
					xhr.overrideMimeType('text/html; charset=ISO-8859-1');
				},
				success: function(data) {
					$(selector).append(data);
					deferred.resolve();
				}
			});
			
			return deferred.promise();
		}
	}
	
	StfDecisao.initApp = [];
}(StfDecisao);