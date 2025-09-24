function setUpHTMLFixture() {
	jasmine.getFixtures().set('<div id="div-loader" style="display: none;"></div>');
}

describe('LoaderService', function() {
	var loaderService = StfDecisao.services.instanciar('LoaderService');
	
	beforeEach(function() {
		setUpHTMLFixture();
	});
	
	describe('loaderOn', function() {
		it('deve mostrar o elemento do loader', function() {
			loaderService.loaderOn();
			expect($('#div-loader')).toBeVisible();
		});
	});
});