var isNetscape = navigator.appName.indexOf('Netscape') != -1;
var marcar = true;

//carregar todos os elementos essenciais do formul�rio
function loadAllElements() {

	addStyleAndEventHandlers(document.getElementsByTagName("input"));
    addStyleAndEventHandlers(document.getElementsByTagName("textarea"));

}

function retirarAcento(objResp) {  
	 var varString = new String(objResp.value);  
	 var stringAcentos = new String('����������������������������');  
	 var stringSemAcento = new String('aaeouaoaeioucuAAEOUAOAEIOUCU');
	   
	 var i = new Number();  
	 var j = new Number();  
	 var cString = new String();   
	 var varRes = '';  
	   
	 for (j = 0; j < varString.length; j++) {
		 	i = stringAcentos.indexOf(varString.substring(j, j+1), 0);
		 	if (i>=0){
			 	cString += stringSemAcento.substring(i, i+1);
		 	} else {
			 	cString += varString.substring(j, j+1);
		 	}

	 }  
	 objResp.value = cString.toUpperCase();  
}  


function scrollToTopIfMessage(){
   if(document.getElementById('pnlMessages')!=null){
   	  javascript:scrollTo(0,0);
   }
}

function pesquisar(idBotao,e){  
	var unicode = e.keyCode? e.keyCode : e.charCode;
	if (unicode == 13){
    botao = document.getElementById(idBotao);
    if ( botao!=null ) {
    	botao.click();
    	return false;
    }
  }
}

function pesquisarProcessoClasse(campo){
  if (window.event.keyCode == 13){
	  converterClasseOnBlur(campo);
	  pesquisar();
  }
}

function addStyleAndEventHandlers(elements) {
    for (i=0; i < elements.length; i++) {
        if (elements[i].type != "button" && elements[i].type != "submit" && 
            elements[i].type != "radio" && elements[i].type != "hidden" &&
            elements[i].type != "reset" && elements[i].type != "checkbox" && 
            elements[i].type != "image" && elements[i].type != undefined) {
                if (elements[i].name.toUpperCase().indexOf("NOFOCUS") < 0)
                    elements[i].onfocus = inputFocus;
                if (elements[i].name.toUpperCase().indexOf("NOBLUR") < 0)
                    elements[i].onblur = inputBlur;
                elements[i].id = elements[i].name;
        }
        if (elements[i].type == "submit" && elements[i].name.indexOf("Excluir") == -1 && elements[i].name.indexOf("Cancelar") == -1) {
            var abaCheck;
            if(elements[i].name.indexOf("AbaForm") != -1) {
                if(abaCheck == null) {
                    definirAba();
                    abaCheck = 1;
                }
                //elements[i].onclick = mostrarProcessamento;
                if(elements[i].style.backgroundColor == '#cccccc') {
                    elements[i].style.backgroundColor = '';
                    elements[i].className = 'InputAbaInativo';
                }
                else {
                    elements[i].style.backgroundColor = '';
                    elements[i].className = 'InputAbaAtivo';
                }
            }
            else {
                //elements[i].className = 'Botao';
                //if (elements[i].name.toUpperCase().indexOf("NOCLICK") < 0)
                //    elements[i].onclick = mostrarProcessamento;
                
                /*
                elements[i].onmouseover = styleButtonMouseOver;
                elements[i].onmouseout = styleButtonMouseOut;
                if (elements[i].name.toUpperCase().indexOf("NOBLUR") < 0)
                    elements[i].onblur = styleButtonOnBlur;
                if (elements[i].name.toUpperCase().indexOf("NOFOCUS") < 0)
                    elements[i].onfocus = styleButtonOnFocus;
                */
            }
        }
        else {
            if (elements[i].name.indexOf("Excluir") != -1) {
                elements[i].onmouseover = styleButtonMouseOver;
                elements[i].onmouseout = styleButtonMouseOut;
                elements[i].onblur = styleButtonOnBlur;
                elements[i].onfocus = styleButtonOnFocus;
            }
            if (elements[i].name.indexOf("Cancelar") != -1) {
                elements[i].onmouseover = styleButtonMouseOver;
                elements[i].onmouseout = styleButtonMouseOut;
                elements[i].onblur = styleButtonOnBlur;
                elements[i].onfocus = styleButtonOnFocus;
            }
        }
        /*
        if (elements[i].type == "button") {
            elements[i].className = 'Botao';
            if (elements[i].id == '') {
                elements[i].id = 'botao' + i;
                elements[i].name = 'botao' + i;
            }
            elements[i].style.marginLeft = '3px';
            elements[i].onmouseover = styleButtonMouseOver;
            elements[i].onmouseout = styleButtonMouseOut;
            elements[i].onblur = styleButtonOnBlur;
            elements[i].onfocus = styleButtonOnFocus;
        }
        */
    }
}

//fun��es implementadas do carregarElementos()
function mostrarProcessamento(e) {
    field = (document.all) ? event.srcElement.name : e.target.name;
    document.getElementById(field).onClick = mostrarDivProcessando(true);
}

function mostrarProcessamentoImg(e) {
    mostrarDivProcessando(true);
}

function styleButtonOnLoad(e) {
    field = (document.all) ? event.srcElement.name : e.target.name;
    document.getElementById(field).className = 'Botao';
}

function styleButtonMouseOver(e) {
    field = (document.all) ? event.srcElement.name : e.target.name;
    document.getElementById(field).className = 'BotaoOn';
}

function styleButtonMouseOut(e) {
    field = (document.all) ? event.srcElement.name : e.target.name;
    document.getElementById(field).className = 'Botao';
}

function styleButtonOnBlur(e) {
    field = (document.all) ? event.srcElement.name : e.target.name;
    document.getElementById(field).className = 'Botao';
}

function styleButtonOnFocus(e) {
    field = (document.all) ? event.srcElement.name : e.target.name;
    document.getElementById(field).className = 'BotaoOn';
}

function inputFocus(e) {
    field = (document.all) ? event.srcElement.name : e.target.name;
    document.getElementById(field).className = 'InputFocus';
}

function inputBlur(e) {
    field = (document.all) ? event.srcElement.name : e.target.name;
    document.getElementById(field).className = 'InputText';
}

function definirAba() {
    var table = document.getElementsByTagName("td");
    for(z = 0; z < table.length; z++) {
        if(table[z].className == "InputAba") {
            table[z].style.borderTop = '';
            table[z].style.borderRight = '1px solid #8F989F';
            table[z].style.borderBottom = '1px solid #8F989F';
            table[z].style.borderLeft = '1px solid #8F989F';
        }
        if(table[z].className == "InputAbaAtivo") {
            table[z].style.borderTop = '1px solid #8F989F';
            table[z].style.borderRight = '1px solid #8F989F';
            table[z].style.borderLeft = '1px solid #8F989F';
            table[z].style.borderBottom = '';
        }
        if(table[z].className == "InputAbaInativo") {
            table[z].style.borderTop = '1px solid #8F989F';
            //table[z].style.borderLeft = '1px solid #8F989F';
            //table[z].style.borderRight = '1px solid #8F989F';
            table[z].style.borderBottom = '';
            table[z].style.backgroundColor = '';
        }
        if(table[z].style.height == '2px') {
            if(table[z].style.borderTop == '0px') {
                table[z].style.borderRight = '1px solid #8F989F';
                table[z].style.borderBottom = '';
                table[z].style.borderLeft = '1px solid #8F989F';
            }
            if(table[z].style.borderTop == '#cccccc 2px outset') {
                table[z].style.borderTop = '1px solid #8F989F';
                if(table[z].style.borderRight == '#cccccc 2px outset') {
                    table[z].style.borderRight = '1px solid #8F989F';
                }
                else {
                    table[z].style.borderRight = '';
                }
                if(table[z].style.borderLeft == '#cccccc 2px outset') {
                    table[z].style.borderLeft = '1px solid #8F989F';
                }
                else {
                    table[z].style.borderLeft = '';
                }
                table[z].style.borderBottom = '';
            }
        }
    }
}

//marcar ou desmarcar todos os checkboxes do datatable
function selecionarCheckBoxes(idCheckbox, imagem) {
    form = document.forms[0];
    if (imagem != null && imagem != undefined) {
        marcar = imagem.src.indexOf('icone_marcar') != -1;
    }
    for(i = 0; i <= form.elements.length -1; i++) {
        input = form.elements[i];
        if(input.id.indexOf(idCheckbox) != -1) {
            if (marcar != undefined) {
                if (marcar) {
                    input.checked = true;
                    if (imagem != null && imagem != undefined) {
                        imagem.src = '../../images/icone_desmarcar.gif';
                        imagem.alt = "Desmarcar todos";
                    }
                } else {
                    input.checked = false;
                    if (imagem != null && imagem != undefined) {
                        imagem.src = '../../images/icone_marcar.gif';
                        imagem.alt = "Marcar todos";
                    }
                }
            } else {
                marcar = !input.checked;
            }
        }
    }
}

function selecionarCheckBoxesForm(form) {       
    for(i = 0; i <= form.elements.length -1; i++) {
        input = form.elements[i];
        if(input.type == 'checkbox') {        	
	        if (marcar != undefined) {
	            if (marcar) {
	                input.checked = true;                    
	            } else {
	                input.checked = false;                    
	            }	           
	        } else {
	            marcar = !input.checked;
	        }
        }                
    }
    marcar = !marcar;
}

function mascaraInputNumLetra(componentes,evento,processo){
	var tecla = (isNetscape?evento.which:evento.keyCode);
	if( tecla == 47 || (tecla >= 48 && tecla <= 57) || ( processo && tecla >= 65 && tecla <= 90) || 
			( processo && tecla >= 97 && tecla <= 122) ){
		return true;
	}else{
		return false;
	}	
}


function mascaraTelefone( campo ) {
    
    function trata( valor,  isOnBlur ) {
        
       valor = valor.replace(/\D/g,"");                       
        
       if( isOnBlur ) {
           
          valor = valor.replace(/(\d)(\d{4})$/,"$1-$2");  
       } else {

          valor = valor.replace(/(\d)(\d{3})$/,"$1-$2");
       }
       return valor;
    }
     
    campo.onkeypress = function (evt) {
         
       var code = (window.event)? window.event.keyCode : evt.which;   
       var valor = this.value;
        
       if(code > 57 || (code < 48 && code != 8 ))  {
          return false;
       } else {
          this.value = trata(valor, false);
       }
    };
     
    campo.onblur = function() {
        
       var valor = this.value;
    
          this.value = trata( this.value, true );
       
    };
     
    campo.maxLength = 10;
 }


function verificarCheckBoxes(idCheckbox, excluir) {
    form = document.forms[0];
    checado = false;
    for(i = 0; i <= form.elements.length -1; i++) {
        input = form.elements[i];
        if(input.id.indexOf(idCheckbox) != -1) {
            if (input.checked) {
                checado = true;
                break;
            }
        }
    }
    if (!checado) {
        self.alert('Nenhum registro foi selecionado.');
    }
    else {
        if (excluir != undefined && excluir == true) {
            return confirm('Deseja excluir o(s) registro(s) selecionado(s)?');
        }
    }
    return checado;
}

//coloca em focus o proximo elemento do form ou o elemento passado pelo parametro m.
function moveNext(campo, qt, campoSeguinte) {
    if(campo.value.length == qt) {
        if(campoSeguinte == undefined) {
            campo.focus();
        } else {
            campoSeguinte.focus();
        }
    }	
}

//preload de imagens
function preloadImages() {
    var preloadImg = new Array();
    var imgCount = preloadImages.arguments; 
    for(i = 0; i < imgCount.length; i++) {
        preloadImg[i] = new Image();
        preloadImg[i].src = preloadImages.arguments[i];
    }
}

function esconderSelects(hide) {
    form = document.forms[0];
    if (form != null && form != 'undefined') {
        for (i = 0; i <= form.elements.length - 1; i++) {
            if (form.elements[i].type == "select-one") {
                if (hide) {
                    form.elements[i].style.display = "none";
                    form.elements[i].style.visibility = "hidden";
                } else {
                    form.elements[i].style.visibility = "visible";
                }
            }
        }
    }
}



//javascript respons�vel por fazer o processamento da p�gina
function mostrarDivProcessando(state) {
   var DivRef = document.getElementById('ProcessandoPopupDiv');
   var DivRefBack = document.getElementById('ProcessandoPopupDivBack');
   var IfrRef = document.getElementById('DivShim');
 
    if(state) {
        DivRefBack.style.width = document.body.clientWidth;
        DivRefBack.style.height = document.body.clientHeight + 80;
        DivRefBack.style.display = "block";
        
        DivRef.style.display = "block";
        DivRef.style.left = Math.round(document.body.scrollLeft + document.body.clientWidth/2 - 124);
        DivRef.style.top = Math.round(document.body.scrollTop + document.body.clientHeight/2 - 39);
        
        IfrRef.style.width = DivRef.offsetWidth;
        IfrRef.style.height = DivRef.offsetHeight;
        IfrRef.style.top = DivRef.style.top;
        IfrRef.style.left = DivRef.style.left;
        IfrRef.style.zIndex = DivRef.style.zIndex - 1;
        IfrRef.style.display = "block";
        esconderSelects(true);
    }
    else {
        DivRefBack.style.display = "none";
        DivRef.style.display = "none";
        IfrRef.style.display = "none";
        esconderSelects(false);
    }
    
    /*var visibility = 'hidden';
    if (state) {
        visibility = 'visible';
    }
    
    if(document.layers) {
        document.layers["ProcessandoPopupDiv"].visibility = visibility;
        document.layers["ProcessandoPopupDivBack"].visibility = visibility;
        document.layers["DivShim"].visibility = visibility;
    } else {
        document.all["ProcessandoPopupDiv"].style.visibility = visibility;
        document.all["ProcessandoPopupDivBack"].style.visibility = visibility;
        document.all["DivShim"].style.visibility = visibility;
    }*/
    
    return true;
}

/* Fun��o usada para abrir uma janela pop-up */
function abrirPopup(url, winTitle, width, height) {
	winName = window.open(url + "?popup=true", winTitle, "left=0,top=0,toolbar=no,location=no,status=yes,menubar=no,scrollbars=yes,resizable=no,width=" + width + ",height=" + height);
    winName.focus();
}

function abrePopupInicial(pagina, janela) {
    var tela = screen.width + screen.height;
    var texto = "Voc� possui um bloqueador de POP-UP.\nDesabilite essa op��o e atualize a p�gina atual.";
    
    if (tela <= 1400) {
        popupIni = window.open(pagina, janela,'width=800,height=500,toolbar=no,copyhistory=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,top=0,left=0');
        if(popupIni == null) {
            alert(texto);
            return;
        }
    }
    else {
        popupIni = window.open(pagina, janela,'width=800,height=600,toolbar=no,copyhistory=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,top=0,left=0');
        if(popupIni == null) {
            alert(texto);
            return;
        }
    }
}

function verifyDisabledOrReadOnly(objeto) {
    retorno = objeto.style.clip;
    if ((objeto.type == 'text' || objeto.type == 'textarea') && (objeto.disabled || objeto.readOnly)) {
        objeto.style.color = '#222';
        objeto.style.fontStyle = 'italic';
        objeto.style.background = '#EEE';
    }
    return retorno;
    
}

function defaultTarget(e) {
  changeTarget(document.forms[0], '_self');
}

function changeTarget(form, target) {
  form.target = target;
}

function mascaraInputNumerico(componente,evento) { 
   var tecla = (isNetscape?evento.which:evento.keyCode);
   if (tecla >= 48 && tecla <= 57) {
	   return true;
   } 
   else{
	   if ((tecla == 9) || (tecla == 8) || (tecla == 0)) {
	        return(true);
	   }
       else{
	       return(false);
	   }
   }
}

function validarData(campo) {
    
    Str = campo.value; 
    if(Str == "" || Str == null){return true;}
    vlraux = trim(Str);
    if ((vlraux == "") || (vlraux.length != 10) ||
       (vlraux.charAt(2) != "/") || (vlraux.charAt(5)!= "/")){
          limparCampos(campo);
          campo.focus();
          exibirMensagem("Data inv�lida, utilize o formato 'DD/MM/YYYY'.");
          return false;
    }

    dia = parseInt(vlraux.substring(0,2),10);
    mes = parseInt(vlraux.substring(3,5),10);
    ano = parseInt(vlraux.substring(6,10),10);

	if(ano < 1840){
            limparCampos(campo);
            campo.focus();
            exibirMensagem("Data inv�lida, o menor ano para pesquisa � 1840.");            
            return false;
    }

    if (isNaN(dia) || isNaN(mes) || isNaN(ano) || (mes < 1) || (mes > 12) || (dia < 1)) {
        limparCampos(campo);
        campo.focus();    
        exibirMensagem("Data inv�lida.");        
        return false;
    }

    tabmes = "312831303130313130313031";

    if ((dia == 29) && (mes == 2)){
      if ((ano == 0) || ((ano % 4) != 0)){
          limparCampos(campo);
          campo.focus();
          exibirMensagem("Data inv�lida.");
          return false;
      }
      
      else { return true; }
    }

    k = (mes * 2 - 2);

    if (dia > tabmes.substring(k,k + 2)) {
        limparCampos(campo);
        campo.focus();
        exibirMensagem("Data inv�lida.");
        return false;
    }
    else { return true;}
 
   exibirMensagem("Data inv�lida.");
   limparCampos(campo);
   campo.focus();
  
   return false;
}

function mascaraInputData(componente,evento) {
   var este = componente;
   var tecla = evento.keyCode;
   
   if(!mascaraInputNumerico(componente,evento)){
    return false;
   }
   
   if (( (este.value.length==0 || este.value.length==1 || este.value.length==2 || este.value.length==3 || este.value.length==4 || este.value.length==5 || este.value.length==6 || este.value.length==7 || este.value.length==8 || este.value.length==9) && ((tecla >= 48 && tecla <= 57)))) {
	   if (este.value.length=='2') {
		   este.value = este.value + '/';
	   }
	   if (este.value.length=='5') {
		   este.value = este.value + '/';
	   }
	   return true;
   } 
   else {
       if ((tecla == 9) || (tecla == 8) || (tecla == 0)) {
           return(true);
	   } 
       else {
	       return(false);
	   }
   }
}

function mascaraInputHora(componente,evento) {
   var este = componente;
   var tecla = evento.keyCode;
   
   if(!mascaraInputNumerico(componente,evento)){
    return false;
   }
   
   if (( (este.value.length==0 || este.value.length==1 || este.value.length==2 || este.value.length==3 || este.value.length==4) && ((tecla >= 48 && tecla <= 57)))) {
	   if (este.value.length=='2') {
		   este.value = este.value + ':';
	   }
	   return true;
   } 
   else {
       if ((tecla == 9) || (tecla == 8) || (tecla == 0)) {
           return(true);
	   } 
       else {
	       return(false);
	   }
   }
}

function validarHora( campoHora ){
	
	if( campoHora != "" ){
		if( campoHora.value.length == 5 ){
			hora = campoHora.value.substr(0,2);
			min  = campoHora.value.substr(3,2);
			if( hora >= 0 && hora <= 24 && min >=0 && min <= 59 ){
				return true;
			}else{
				alert("Hora '"+campoHora.value+"' inv�lida");
				limparCampos(campoHora);
				return false;
			}
		}else{
			alert("A hora deve ter o seguinte formato '00:00'");
			limparCampos(campoHora);
			return false;
		}
	}else{
		return false;
	}
	return true;	
}

function compararHora( data_inicial ,  data_final, hora_Inicial, hora_Final ){
 	if( validarData( data_inicial ) && validarData( data_final ) && validarHora(hora_Inicial) &&  validarHora(hora_Final)){
 		if( data_inicial.value == data_final.value ){
	 		valorHoraInicial = hora_Inicial.value;
	 		valorHoraFinal = hora_Final.value;
	 		horaInicial = valorHoraInicial.substr(0,2);
			minInicial  = valorHoraInicial.substr(3,2);	
			horaFinal   = valorHoraFinal.substr(0,2);
			minFinal    = valorHoraFinal.substr(3,2);
			
			if( horaInicial > horaFinal ){				
				limparCampos(hora_Final);
				hora_Final.focus();	
				alert("A hora inicial deve ser menor que a hora final");
				return false; 		
	 		}else if( horaInicial == horaFinal && minInicial > minFinal ){	 			
				limparCampos(hora_Final);
				hora_Final.focus();
				alert("A hora inicial deve ser menor que a hora final");
				return false; 		
	 		}else if( horaInicial == horaFinal && minInicial == minFinal ){	 			
				limparCampos(hora_Final);	
				hora_Final.focus();
				alert("A hora inicial deve ser menor que a hora final");
				return false; 		
	 		}
 		}
 		
 	}else{
 		return false;
 	}
 	
 	return true;
 }
 


function limparCampos(campo){

	campo.value = "";
	return true;
}
function exibirMensagem(Str){
	
	alert( Str );
	return true;
	
}
function compararDatas( data_inicial ,  data_final ){
 	
 	str_data_inicial = data_inicial.value;
 	str_data_final   = data_final.value;
 	
 	if( str_data_final == "" ){
 		validarData( data_inicial );
 		return true;
 	}
 		
 	else if( validarData( data_inicial ) && validarData( data_final )  ){
 	 			 		
 		dia_inicial      = data_inicial.value.substr(0,2);
 		dia_final        = data_final.value.substr(0,2);
 		mes_inicial      = data_inicial.value.substr(3,2);
 		mes_final        = data_final.value.substr(3,2);
 		ano_inicial      = data_inicial.value.substr(6,4);
 		ano_final        = data_final.value.substr(6,4);
 	if(ano_inicial > ano_final){ 		 
 		//limparCampos(data_inicial);
 		limparCampos(data_final);
 		//data_inicial.focus();
 		data_final.focus();
 		alert("A data inicial deve ser menor que a data final.");
 		return false
 	}else{
  	if(ano_inicial == ano_final){
   	if(mes_inicial > mes_final){    	
 				//limparCampos(data_inicial);
 				limparCampos(data_final);
 				//data_inicial.focus();
 				data_final.focus();
 				alert("A data inicial deve ser menor que a data final.");
 				return false
 			}else{
 				if(mes_inicial == mes_final){
 					if(dia_inicial > dia_final){ 						
 						//limparCampos(data_inicial);
 						limparCampos(data_final);
 						//data_inicial.focus();
 						data_final.focus();
 						alert("A data inicial deve ser menor que a data final.");
 						return false
 					}
 				}
 			}
 		}
 	}
 
 		return true;
 	} 
 	else
 		return false;
 }

var classesAntigas = null;
var classesNovas = null;
var classes = null; 

function recuperarProcesso(campoClasse, campoNumeroProcesso, botao) {            
    var entrada = campoClasse.value;        
    var index = 0;
    submeter = true;
    
    for ( var i = 0; i < entrada.length; i ++ ) {
        if ( isDigit(entrada.charAt(i)) ) {
            index = i;
            break;
        }
    }        
    if ( entrada != "" ) {
        entrada = trim(entrada);        
        processo = new String(entrada);
        tamanho = Number(processo.length);
        
        if ( index == 0 ) {
            submeter = false;            
            index = tamanho;
        }
        
        var classe = processo.substring(0,index);        
        if ( classe != "" ) {
            classe = trim(classe)
        }
        classe = converterClasse(classe);    
        var numeroProcesso;
        
        if ( index != tamanho ) {
            numeroProcesso = Number(processo.substring(index,tamanho - 1 ));            
        } else {
            numeroProcesso = '';
        }             
        if ( classe == "" )
            campoClasse.focus();
            
        campoClasse.value = classe;
        campoNumeroProcesso.value = numeroProcesso;
        
        if ( submeter ) 
            botao.click();            
        }
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
    
function trim(str){
  return str.replace(/^\s*|\s*$/g, "");
}

//----------------------------------------- LEONARDO ALMEIDA ----------------------------------------//

function converterClasse(campo) {		
	for(i=0; i < classes.length; i++ ){
		if(classes[i].toUpperCase() == campo.value.toUpperCase()){
			campo.value = classes[i];
			return true;			
		}
	}		
}

function validarClasse(campo){	
	classeExistente = false 
	for(i=0; i < classes.length; i++ ){
		if(classes[i].toUpperCase() == campo.value.toUpperCase()){			
			classeExistente = true;
		}
	}
	
	if(classeExistente == false){
		alert("Classe n�o existente!");
		campo.value = ""
	}
}

//----------------------------------------- LEONARDO ALMEIDA ----------------------------------------//
    
function converterClasseOnBlur(campoClasse) {
   var classe = campoClasse.value;  
   if ( classe != null & classe != "" ) {
	  classe = trim(classe);
       for ( i = 0; i < classes.length; i++ ) {
           classeNova = classes[i];
           if ( classeNova.toUpperCase() == classe.toUpperCase() ) {				
               campoClasse.value = classeNova;
               return true;
           }
       }	    
       for ( i = 0; i < classesAntigas.length; i++ ) {
           classeAntiga = classesAntigas[i];
           if ( classeAntiga.toUpperCase() == classe.toUpperCase() ) {				
               campoClasse.value = classesNovas[i];
			return true;
           }
       }
       alert('Classe processual inv�lida.');
       campoClasse.value = "";
       campoClasse.focus();
       return false;
  }
}
    
// M�todo que � disparado quando uma tecla � pressionada
function eventoOnKeyPress(campo, tipoCampo, campoProximoFoco)
{    	
   // Se a tecla "ENTER" foi pressionada...
   if (window.event.keyCode == 13)
   {
       var i = 0;
       var quantidadeForms = document.forms.length;
       
       for (i = 0; i < quantidadeForms; i++)
       {
           var campo = null;
           var nomeForm = document.forms[i].id;
   
           if(campoProximoFoco.indexOf("()")>=0){
               //eval(metodoPesquisa); // Invocar a execu��o do m�todo
               return false;
           }
           else{
               try
               {
                   campo = document.getElementById(nomeForm + ':' + campoProximoFoco);
                   campo.focus();
                   return false;
               }
               catch(e)
               {
               }
           }
           //return false;
       }          
    }   
	// Se o campo passado como parametro for numerico...
	if (tipoCampo == "NUMERICO") {
	    	return mascaraInputNumerico(campo, event);		    
	} else if (tipoCampo == "DATA") {
	    return mascaraInputData(campo, event);            	
	}
}

function mascaraInputLetra(componentes,evento){
   var tecla = (isNetscape?evento.which:evento.keyCode);
   if ((tecla >= 65 && tecla <= 90)||(tecla >= 97 && tecla <= 122)) {
	   return (true);
   }else{
	   return(false);
   }
}

function abrirRelatorio(form) {                                
	mudarformOutraPagina(form);
	nomeFormulario = form.id;
	formulario = document.getElementById(nomeFormulario);
	setTimeout( 'formulario.target = ""' ,2000);
}
   
function mudarformOutraPagina(form){
	form.target='_blank';
}


function verificaNumero(e) {
	if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		return false;
	}
}

function caixaAlta(campo) {
	campo.value = campo.value.toUpperCase();
}

function exibirMsgProcessando(state) {
   var DivRef = document.getElementById('ProcessandoPopupDiv');
   var DivRefBack = document.getElementById('ProcessandoPopupDivBack');
   var IfrRef = document.getElementById('ProcessandoDivShim');
 
    if(state) {
        DivRefBack.style.width = document.body.clientWidth;
        DivRefBack.style.height = document.body.clientHeight*2;
        DivRefBack.style.display = "block";
        
        DivRef.style.display = "block";
        DivRef.style.left = Math.round(document.body.scrollLeft + document.body.clientWidth/2 - 200);
        DivRef.style.top = Math.round(document.body.scrollTop + document.body.clientHeight/2 - 20);
        
        IfrRef.style.width = DivRef.offsetWidth;
        IfrRef.style.height = DivRef.offsetHeight;
        IfrRef.style.top = DivRef.style.top;
        IfrRef.style.left = DivRef.style.left;
        IfrRef.style.zIndex = DivRef.style.zIndex - 1;
        IfrRef.style.display = "block";
    }
    else {
        DivRefBack.style.display = "none";
        DivRef.style.display = "none";
        IfrRef.style.display = "none";
    }
    
    return true;
}

 function contadorTextArea(campoTextArea,campoMostraContagem,tamanho){
	if(campoTextArea.value.length>tamanho){
		campoTextArea.value=campoTextArea.value.substring(0,tamanho);
	}else{
		if(campoMostraContagem!=null)
	{
	   campoMostraContagem.value=tamanho-campoTextArea.value.length +'/'+tamanho;
 }}}
    
window.onload = loadAllElements; 



