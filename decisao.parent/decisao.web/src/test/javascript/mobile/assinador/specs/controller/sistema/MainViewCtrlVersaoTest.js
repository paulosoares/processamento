describe('MainViewCtrl', function() {
	function carregarFixture() {
		jasmine.getFixtures().fixturesPath = 'src/main/webapp/mobile/assinador/assets_stf/js/app/view/sistema';
		jasmine.getFixtures().load('main.html');
	}
	beforeEach(function() {
		carregarFixture();
	});
	
	var mainViewCtrl;
	
	describe('versão do sistema', function() {
		
		beforeEach(function() {
			var versaoService = {
				getVersao: function (){
					return $.when("Versão 1.0.0/4.3.3");
				}
			};
			var certificadoService = {
				getCertificado: function() { return $.Deferred().promise(); }
			};
			var certificadoUtil = {
				cadeiaInHtml: function() { return ''; }
			};
			var flowService = {
				
			};
			var msgService = {
					
			};
			mainViewCtrl = StfDecisao.viewCtrls.instanciar('MainViewCtrl', [versaoService, certificadoService, certificadoUtil, flowService, msgService]);
			mainViewCtrl.viewIn();
		});
		
		it('deve mostrar a versão', function() {
			$('.versao-sistema').each(function() {
				expect($(this).text()).toEqual("Versão 1.0.0/4.3.3");
			})
		});
	});
});