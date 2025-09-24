describe('LoginViewCtrl', function() {
	function carregarFixture() {
		jasmine.getFixtures().fixturesPath = 'src/main/webapp/mobile/assinador/assets_stf/js/app/view/login';
		jasmine.getFixtures().load('login.html');
	}
	
	var loginViewCtrl;
	
	beforeEach(function() {
		carregarFixture();
	});
	
	describe('enter no campo de senha', function() {
		var loginService, flowService;
		
		beforeEach(function() {
			loginService = {
				autenticar: function() {}
			};
			flowService = {
				irParaInicio: function() {}
			};
			
			spyOn(loginService, 'autenticar').andReturn($.when());
			
			loginViewCtrl = StfDecisao.viewCtrls.instanciar('LoginViewCtrl', [loginService, flowService]);
		});
		
		it('deve tentar autenticar', function() {
			$('#usuario').val('usuario')
			$('#senha').val('senha');
			var e = $.Event("keypress");
			e.which = 13;
			e.keyCode = 13;
			$('#senha').trigger(e);
			expect(loginService.autenticar).toHaveBeenCalled();
		});
	});
});