describe('PromiseUtil', function() {
	
	var promiseUtil = StfDecisao.utils.instanciar('PromiseUtil');
	
	var gerarObjAleatorio = function() {
		return {id: (Math.floor(Math.random() * 1000))};
	};
	
	describe('encadear uma promisable function', function() {
		
		var deferred;
		var resolveData;
		
		describe('deve seguir fluxo da promise original', function() {
			
			beforeEach(function() {
				deferred = $.Deferred();
				resolveData = gerarObjAleatorio();
			});
			
			it('deve resolver a encadeada quando a promise da promisable function for resolvida, repassando os dados', function() {
				var promiseEncadeada;
				var receivedData;
				
				runs(function() {
					promiseEncadeada = promiseUtil.encadear(function(data) {
						expect(data).toBeUndefined();
						return deferred.promise();
					});
					
					promiseEncadeada.done(function(data) {
						receivedData = data;
					});
					deferred.resolve(resolveData);
				});
				
				waitsFor(function() {
					return receivedData;
				}, "Promise deveria ter sido concluída", 500);
				
				runs(function() {
					expect(receivedData).toEqual(resolveData);
				});
			});
			
			it('deve rejeitar a encadeada quando a promise da promisable function for rejeitada, repassando os dados', function() {
				var promiseEncadeada;
				var receivedData;
				
				runs(function() {
					promiseEncadeada = promiseUtil.encadear(function(data) {
						expect(data).toBeUndefined();
						return deferred.promise();
					});
					
					promiseEncadeada.fail(function(data) {
						receivedData = data;
					});
					deferred.reject(resolveData);
				});
				
				waitsFor(function() {
					return receivedData;
				}, 'Promise deveria ter sido concluída', 500);
				
				runs(function() {
					expect(receivedData).toEqual(resolveData);
				});
			});
			
		});
		
	});
	
	describe('encadear duas promisable functions', function() {
		
		var deferred1;
		var deferred2;
		var resolveData1;
		var resolveData2;
		
		beforeEach(function() {
			deferred1 = $.Deferred();
			deferred2 = $.Deferred();
			resolveData1 = gerarObjAleatorio();
			resolveData2 = gerarObjAleatorio();
		});
		
		describe('quando a primeira promise é resolvida', function() {

			it('deve repassar o dado para a segunda promisable function', function() {
				var promiseEncadeada;
				var dadoPassadoParaSegundaPromisableFn;
				var receivedData;
				
				runs(function() {
					promiseEncadeada = promiseUtil.encadear(function() {
						return deferred1.promise();
					}, function(data) {
						dadoPassadoParaSegundaPromisableFn = data;
						return deferred2.promise();
					});
					
					promiseEncadeada.done(function(data) {
						receivedData = data;
					});
					
					deferred1.resolve(resolveData1);
					deferred2.resolve(resolveData2);
				});
				
				waitsFor(function() {
					return receivedData;
				}, 'Promise deveria ter sido concluída.', 500);
				
				runs(function() {
					expect(dadoPassadoParaSegundaPromisableFn).toEqual(resolveData1);
					expect(receivedData).toEqual(resolveData2);
				});
			});
			
		});
		
		describe('quando notifica-se do progresso', function() {

			it('deve repassar a notificação', function() {
				var promiseEncadeada;
				var receivedData;
				var progressData1, progressData2;
				var receivedProgressData = [];
				
				runs(function() {
					promiseEncadeada = promiseUtil.encadear(function() {
						return deferred1.promise();
					}, function(data) {
						return deferred2.promise();
					});
					
					promiseEncadeada.done(function(data) {
						receivedData = data;
					}).progress(function(data) {
						receivedProgressData.push(data);
					});
					
					progressData1 = gerarObjAleatorio();
					deferred1.notify(progressData1)
					deferred1.resolve(resolveData1);
					progressData2 = gerarObjAleatorio();
					deferred2.notify(progressData2);
					deferred2.resolve(resolveData2);
				});
				
				waitsFor(function() {
					return receivedData;
				}, 'Promise deveria ter sido concluída.', 500);
				
				runs(function() {
					expect(progressData1).toEqual(receivedProgressData[0]);
					expect(progressData2).toEqual(receivedProgressData[1]);
				});
			});
			
		});
		
	});
	
});