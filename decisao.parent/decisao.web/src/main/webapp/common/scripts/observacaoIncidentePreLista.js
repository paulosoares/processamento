function limitTextArea(element, limit) {
	var tamanho = element.value.length;
	var restante = limit - tamanho;
	var idPai = element.name;
	var vetorId = idPai.split(":");
	var id = vetorId[0]+":"+vetorId[1]+":obsIncSize";
	var nomeTexto = restante +"/"+limit;
	document.getElementById(id).innerHTML = nomeTexto;
	if (tamanho > limit) {
		element.value = element.value.substring(0, limit);
		document.getElementById(id).innerHTML = "Limite do texto atingido!!";
	}
}