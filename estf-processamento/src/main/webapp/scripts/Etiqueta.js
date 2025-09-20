		//src=<%=request.getContextPath()%>/scripts/default.js
		
	function caneta(campoEtiqueta,campoNumero,campoAno,campoClasseProcesso,campoNumeroProcesso){		
		var valor = trim(campoEtiqueta.value); 
		var retorno = true;
		possuiLetra = false;
		
		if (window.event.keyCode == 13)
		{
			for ( i = 0; i < valor.length; i++) {
				if( (valor.charCodeAt(i) >= 65 && valor.charCodeAt(i) <= 90 )
				    || (valor.charCodeAt(i) >= 97 && valor.charCodeAt(i) <= 122 ) ) {
				 
					possuiLetra = true;
				}
			}
		  if(possuiLetra == true){
		  	campoNumero.value = '';
		  	campoAno.value = '';
		  	canetaProcesso(campoEtiqueta,campoClasseProcesso,campoNumeroProcesso);
		  }else{
		    if( campoClasseProcesso != null && campoNumeroProcesso != null ){
			    campoClasseProcesso.value = '';
			  	campoNumeroProcesso.value = '';
		  	}
		  	canetaProtocolo(campoEtiqueta,campoNumero,campoAno); 		  	
		  }
		  campoEtiqueta.value='';
		  retorno = false;
		  //procurar o botao que efetua a pesquisa
		  elements = document.getElementsByTagName("input");
		  for (i=0;  i <= elements.length - 1; i++) {
		  	if (elements[i].getAttribute('type')=='button'&&
		  		elements[i].getAttribute('title')=='PesquisaCanetaOtica'){
		  		elements[i].click();
		        break;     
		    }
		  }
		  //Fim procura
		  
		}
		return retorno;
	}	

	
	function canetaProtocolo(campoEtiqueta,campoNumero,campoAno){
	   var anterior = 48;
	   var dataAnterior, dataAtual;
		if (dataAnterior == null){
			dataAnterior = new Date();
		}
		
		dataAtual = new Date();
		  
		
		// Se a tecla "ENTER" foi pressionada...
		if (window.event.keyCode == 13)
		{
			var tempoDigitacao = dataAtual - dataAnterior;
		    var entrada = campoEtiqueta.value;
		    var textoDigitado = '';
			
		
			// Se a entrada de dados veio da caneta óptica...
			for ( i = 0; i < entrada.length; i++) {
				if(anterior == 48){
					anterior = entrada.charCodeAt(i);
				}	
				if(anterior>48){
					textoDigitado = textoDigitado + entrada.charAt(i);
				}
				
			}
			
			var quantidadeCaracteres = textoDigitado.length;
			
			if((tempoDigitacao < 100) && (quantidadeCaracteres >= 5)){
				campoNumero.value = textoDigitado.substring(0, quantidadeCaracteres - 4);
				campoAno.value = textoDigitado.substring(quantidadeCaracteres - 4);					
			}
		    campoEtiqueta.value = campoNumero.value +' - '+ campoAno.value;	    
		    dataAnterior = null;
			dataAtual = null;
		}
	}
	
	function canetaProcesso(campoEtiqueta,campoClasseProcesso,campoNumeroProcesso){             
		var entrada = trim(campoEtiqueta.value); 
		var numeroProcesso = '';       
		var classeProcesso = '';
		var contador = 0;
		var anterior = 48;
		
		for ( i = 0; i < entrada.length; i++) {
			if( (entrada.charCodeAt(i) >= 65 && entrada.charCodeAt(i) <= 90 )
			    || (entrada.charCodeAt(i) >= 97 && entrada.charCodeAt(i) <= 122 ) ) {
			 
				classeProcesso = classeProcesso + entrada.charAt(i);
				
			}else if( (entrada.charCodeAt(i) >= 48 && entrada.charCodeAt(i) <= 57 ) ) {
				if(contador<=6){
					if(contador > 0){
					   if(anterior == 48 && entrada.charCodeAt(i) > 48){
					   		numeroProcesso = numeroProcesso + entrada.charAt(i);
					   }else if (anterior > 48){
					   		numeroProcesso = numeroProcesso + entrada.charAt(i);
					   }
					}else if(entrada.charCodeAt(i)>48){
					   numeroProcesso = numeroProcesso + entrada.charAt(i);
					}
					contador++
				}
				if(anterior == 48){
					anterior = entrada.charCodeAt(i)
				}
			}
		}
		
		if( classeProcesso.length > 0 ) {
			classeProcesso = converterClasse(classeProcesso);    
			campoClasseProcesso.value = classeProcesso;
			       
		}
		
		if( numeroProcesso.length > 0 ) {
			campoNumeroProcesso.value = numeroProcesso;   
		}
	}

	function canetaPeticao(campoEtiqueta,campoNumero,campoAno){
		var valor = trim(campoEtiqueta.value); 
		var retorno = true;
		
		if (window.event.keyCode == 13)
		{
		  	//canetaProtocolo(campoEtiqueta,campoNumero,campoAno);
		    var anterior = 48;
		    var dataAnterior, dataAtual;
		    
			if (dataAnterior == null){
				dataAnterior = new Date();
			}
			
			dataAtual = new Date();
			  			
			var tempoDigitacao = dataAtual - dataAnterior;
		    var entrada = campoEtiqueta.value;
		    var textoDigitado = '';
					
			// Se a entrada de dados veio da caneta óptica...
			for ( i = 0; i < entrada.length; i++) {
				if(anterior == 48){
					anterior = entrada.charCodeAt(i);
				}	
				if(anterior>48){
					textoDigitado = textoDigitado + entrada.charAt(i);
				}
				
			}
			
			var quantidadeCaracteres = textoDigitado.length;
			
			if((tempoDigitacao < 100) && (quantidadeCaracteres >= 5)){
				campoNumero.value = textoDigitado.substring(0, quantidadeCaracteres - 4);
				campoAno.value = textoDigitado.substring(quantidadeCaracteres - 4);					
			}
		    campoEtiqueta.value = campoNumero.value +' - '+ campoAno.value;	    
		    dataAnterior = null;
			dataAtual = null;
	  		  
		  campoEtiqueta.value='';
		  retorno = false;
		  //procurar o botao que efetua a pesquisa
		  elements = document.getElementsByTagName("input");
		  for (i=0;  i <= elements.length - 1; i++) {
		  	if (elements[i].getAttribute('type')=='button'&&
		  		elements[i].getAttribute('title')=='PesquisaCanetaOtica'){
		  		elements[i].click();
		        break;     
		    }
		  }
		  //Fim procura
		  
		}
		return retorno;

	}

	function trim(str){
		return str.replace(/^\s*|\s*$/g, "");
	}
	
	function isDigit(num) {
	    if( num.length > 1 ) { 
	        return false;
	    }
	    var string="1234567890";
	    if( string.indexOf( num ) != -1 ) {
	        return true;
	    }
	    return false;
	} 
	
	function converterClasse(classe) {
    if ( classe != "" ) {    
       for ( i = 0; i < classes.length; i++ ) {
           classeNova = classes[i];
           if ( classeNova.toUpperCase() == classe.toUpperCase() ) {				
               return classeNova;
           }
       }	    
       for ( i = 0; i < classesAntigas.length; i++ ) {
           classeAntiga = classesAntigas[i];
           if ( classeAntiga.toUpperCase() == classe.toUpperCase() ) {				
               return classesNovas[i]	;
           }
       }
       alert('Classe processual inválida.');
       return "";
   }
}


