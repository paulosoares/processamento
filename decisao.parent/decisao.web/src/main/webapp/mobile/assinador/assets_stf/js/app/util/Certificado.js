'use strict';

StfDecisao.util('CertificadoUtil', function() {
	var self = this;
	
	this.cadeiaInHtml = function(data) {
		var cadeiaString = "";
        var cadeiaSemTitular = data.cadeia.slice(1); // Removendo o titular da cadeia a ser apresentada.
        for (var i in cadeiaSemTitular) {
            cadeiaString += "<strong>Emitido por:</strong>&nbsp;" + cadeiaSemTitular[i] + "<br>";
        }
        return "<strong>Titular:</strong>&nbsp; " + data.cn + "<br>" + cadeiaString;
	};
});