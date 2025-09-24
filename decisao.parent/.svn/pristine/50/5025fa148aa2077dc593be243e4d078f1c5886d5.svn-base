describe('DatatableUtil', function() {
	var datatableUtil;
	var togglesUtil;
	
	function carregarFixture() {
		jasmine.getFixtures().set('<div id="tabela-expedientes"></div>');
	}
	
	beforeEach(function() {
		datatableUtil = StfDecisao.utils.instanciar('DatatableUtil');
		togglesUtil = StfDecisao.utils.instanciar('TogglesUtil');
	});
	
	describe('datatableCtrl', function() {
		var datatableCtrl;
		var expedientesParaAssinar;
		var mockDatatable;
		
		beforeEach(function() {
			var inicializouDatatable = false;
			$.fn.DataTable = {
				fnIsDataTable: function() { return inicializouDatatable; }
			};
			var MockDatatable = function() {
				inicializouDatatable = true;
				this.fnDestroy = function() {};
			}
			mockDatatable = new MockDatatable();
			$.fn.dataTable = function() {
				return mockDatatable;
			};
			
			var newDoc = function(id, processo, descricao) {
				return {
					'id': id,
					'processo': processo,
					'descricao': descricao
				};
			};
			
			var renderExpedientes = function(data, type, row) {
				return '<span>' +  row[1] + '</span><br /><span class="muted">'+ row[2] +'</span>';
		    };
			
		    var botoesAcoesToggle = togglesUtil.viewsToggle(['acoes-sem-selecao', 'acoes-com-selecao']);
		    
		    expedientesParaAssinar = [newDoc(1, 'ADI 100 Mérito', 'OFÍCIO QUALQUER'), newDoc(2, 'ADI 100 Agr', 'OFÍCIO QUALQUER 2')]
		    var getExpedientesParaAssinar = function() {
		    	return expedientesParaAssinar;
		    };
		    
		    var adaptExpedientesToArrayOfArrays = function(documentos) {
				var arrDocs = [];
				for (var i in documentos) {
					var docArr = [];
					docArr.push(documentos[i].id);
					docArr.push(documentos[i].processo);
					docArr.push(documentos[i].descricao);
					arrDocs.push(docArr);
				}
				return arrDocs;
			}
		    
		    var updateBotoesAcoes = function() {

			};
		    
		    var detalheExpedienteHandler = function(id){
				
			};
		    
			datatableCtrl = datatableUtil.datatableCtrl('#tabela-expedientes', renderExpedientes, botoesAcoesToggle,
					getExpedientesParaAssinar, adaptExpedientesToArrayOfArrays, updateBotoesAcoes, detalheExpedienteHandler);
		});
		
		describe('update', function() {
			describe('quando atualizar com 0 documentos para assinar', function() {
				
				// Teste para reproduzir o bug em DECISAO-1907
				it('deve destruir a datatable', function() {
					datatableCtrl.update();
					expedientesParaAssinar = [];
					spyOn(mockDatatable, 'fnDestroy');
					datatableCtrl.update();
					expect(mockDatatable.fnDestroy).toHaveBeenCalled();
				});
			});
		});
	
	})
});