'use strict';

StfDecisao.model('AssinaturaModel', function(acoes) {
	var self = this;
	
	this.documentosParaAssinar = new Array();
	this.textosParaAssinar = new Array();
	this.comunicacoesParaAssinar = new Array();
	
	var documentosASeremAssinados = this.documentosParaAssinar;
	
	var getTextoFromId = function(id) {
		for (var i in self.textosParaAssinar) {
			if (self.textosParaAssinar[i].id == id) {
				return self.textosParaAssinar[i];
			}
		}
		return null;
	};
	
	var indiceNoArray = function(arr, el) {
		for (var i in arr) {
			if (arr[i].id == el.id) {
				return i;
			}
		}
		return -1;
	};
	
	var preencherAssinaturaConjunta = function(currDoc) {
		if (currDoc.devemSerAssinadosJunto.length > 0) {
			for (var j in currDoc.devemSerAssinadosJunto) {
				var indice = indiceNoArray(documentosASeremAssinados, currDoc.devemSerAssinadosJunto[j]);
				if (indice == -1) {
					documentosASeremAssinados.push(currDoc.devemSerAssinadosJunto[j]);
				}
			}
		}
	};
	
	var saoTextosIguais = function(texto1, texto2) {
		return texto1.id != texto2.id &&
			texto1.textosIguais && texto2.textosIguais &&
			texto1.idArquivoEletronico == texto2.idArquivoEletronico;
	};
	
	var preencherTextosIguais = function(docs, currDoc) {
		if (currDoc.textosIguais) {
			for (var i in self.textosParaAssinar) {
				var currTexto = self.textosParaAssinar[i];
				if (saoTextosIguais(currDoc, currTexto)) {
					if (indiceNoArray(docs, currTexto) == -1) {
						docs.push(currTexto);
					}
				}
			}
		}
	};
	
	var preencherAssinaturaTextosIguais = function(currDoc) {
		preencherTextosIguais(documentosASeremAssinados, currDoc);
	};
	
	this.setDocumentosASeremAssinados = function(docs) {
		documentosASeremAssinados = [];
		for (var i in docs) {
			var currDoc = docs[i];
			preencherAssinaturaConjunta(currDoc);
			if (indiceNoArray(documentosASeremAssinados, currDoc) == -1) {
				documentosASeremAssinados.push(currDoc);
			}
			preencherAssinaturaTextosIguais(currDoc);
		}
	};
	
	this.getDocumentosASeremAssinados = function() {
		return documentosASeremAssinados;
	};
	
	this.getDocumentosComListaTextosIguais = function(idDoc) {
		var docs = [];
		var documento = getTextoFromId(idDoc);
		preencherTextosIguais(docs, documento);
		docs.push(documento);
		return docs;
	};
	
	this.servicoSelecionado = acoes.ASSINAR_TEXTOS; // Service default
});