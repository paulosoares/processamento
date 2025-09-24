describe("BrowserUtil", function() {
	describe('quando está no dispositivo', function() {
		var mockWindow = {
			location : 'https://sistemas.stf.jus.br',
			parent : {
				location : 'file:///home/tomas/aplicativo/index.html'
			}
		};
		var browserUtil = StfDecisao.utils.instanciar('BrowserUtil', [ mockWindow ]);
		it('isMobile deve retornar true', function() {
			var isMobileReturn = browserUtil.isMobile();
			expect(isMobileReturn).toBe(true);
		});

	});

	describe('quando está no ambiente de desenvolvimento desktop', function() {
		var mockWindow = {
			location : 'https://sistemas.stf.jus.br',
			parent: {
				location: 'https://sistemas.stf.jus.br'
			}
		};
		var browserUtil = StfDecisao.utils.instanciar('BrowserUtil', [ mockWindow ]);
		it('isMobile deve retornar false', function() {
			var isMobileReturn = browserUtil.isMobile();
			expect(isMobileReturn).toBe(false);
		});
	});
});