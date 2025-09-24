'use strict';

StfDecisao.util('DatatableUtil', function() {
	var self = this;
	
	this.datatableCtrl = function(seletorTabela, renderDefinition, botoesAcoesToggle, getDocumentosParaAssinar, adaptFunction, 
			updateBotoesAcoes, detalheHandler, arrayToDocEntityAdapter, seletorPainelNenhumDoc) {
		return new DatatableCtrl(seletorTabela, renderDefinition, botoesAcoesToggle, getDocumentosParaAssinar, adaptFunction, 
			updateBotoesAcoes, detalheHandler, arrayToDocEntityAdapter, seletorPainelNenhumDoc);
	};
	
	var DatatableCtrl = function(seletorTabela, renderDefinition, botoesAcoesToggle, getDocumentosParaAssinar, adaptFunction, 
			updateBotoesAcoes, detalheHandler, arrayToDocEntityAdapter, seletorPainelNenhumDoc) {
		var self = this;
		
		this.dataTable = null;
		
		this.update = function() {
			if ($.fn.DataTable.fnIsDataTable($(seletorTabela).get(0))) {
				$(seletorTabela).dataTable().fnDestroy();
			}
			
			self.dataTable = $(seletorTabela).dataTable({
				sDom: 'ft<"outer-center"p>',
				sPaginationType: "swipe",
				sSwipePaginationSelector: '#painel-docs-pendentes',
				oLanguage: {
					sSearch: '<i class="fa fa-search"> </i>'
				},
				aaData : adaptFunction(getDocumentosParaAssinar()),
				iDisplayLength: 9,
				bAutoWidth: false,
				aoColumnDefs: [{
					"mRender": function (data, type, row) {
						var checkboxExtra = '';
						var finalRendered = '<div class="checkbox check-success "><input class="check-bt" id="'+ seletorTabela + '-checkbox-' +
							data + '" type="checkbox" ' + checkboxExtra + ' value="' + data + '"><label for="'+ seletorTabela + '-checkbox-' + data + '"></label></div>';
						
						return finalRendered;
					},
					"aTargets" : [ 0 ]
				},{
					"mRender": renderDefinition, 
					"aTargets" : [ 1 ]
				}
				],
				aoColumns: [ {
					"sClass" : "small-cell v-align-middle",	"sTitle" : "Id"
				}, {
					"sClass" : "clickable v-align-middle",	"sTitle" : "Documento"
				}],
				"fnCreatedRow": function( nRow, aData, iDataIndex ) {
					$('input[type=checkbox]', nRow).click( function() {			
						$(this).closest('tr').toggleClass('row_selected');
						var qtdeSelecionados = $('input[type=checkbox]:checked', self.dataTable.fnGetNodes()).length;
						if (qtdeSelecionados > 0) {
							botoesAcoesToggle.toggle('acoes-com-selecao');
						} else {
							botoesAcoesToggle.toggle('acoes-sem-selecao');
						}
						updateBotoesAcoes();
					});
					$('td.clickable', nRow).click(function (){
						detalheHandler(aData[0]);
					}); 
				}
			});
			if (getDocumentosParaAssinar().length == 0) {
				$(seletorTabela).parents('div.dataTables_wrapper').first().hide();
				$(seletorPainelNenhumDoc).show();
			} else {
				$(seletorTabela).parents('div.dataTables_wrapper').first().show();
				$(seletorPainelNenhumDoc).hide();
			}
			botoesAcoesToggle.toggle('acoes-sem-selecao');
		}
		
		this.getTotalSelecionados = function() {
			if (self.dataTable){
				return $('input[type=checkbox]:checked', self.dataTable.fnGetNodes()).length;
			}else {
				return 0;
			}
		}
		
		this.getSelecionados = function(){
			if (self.dataTable){
				return $('input[type=checkbox]:checked', self.dataTable.fnGetNodes());
			} else {
				return $();
			}
		}
	};
	
});