$(document).ready(function() {	

/*Setup Area*/

	/*Colapsa sidebar ao carregar a página (não para xs-devices)*/
	 if ($(window).width() > 767) {	
	 	$('#layout-condensed-toggle').click();
	 };
	/*Habilita tooltip nas páginas*/
	$('[data-toggle=tooltip]').tooltip();

	/*Formulários*/
	$("#selecionarOabPeticionador").select2();
    $("#selecionarOabPeticionadorColapsed").select2();
	$("#selecionarCPFPeticionador").select2();	
	$("#selecionarClasseProcessual").select2();	
    $("#selecionarTipoDocumento-3").select2(); 
    $("#selecionarTipoDocumento-4").select2();  
    $("#selecionarNatureza").select2();
    $("#naturezaClassificaoColapsed").select2();   
    $("#tipoPeticaoColapsed").select2();    
      

    /*toggle*/
    //Accordians
    $('#grupoProcessoParte').collapse({
      toggle: false
    })

    /*tabs*/

    $('#processoParte a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });    

           

  	//Multiselect - Select2 plug-in
  	$("#selecionarMarcacoesEPreferencias").val(["1","2"]).select2();	
  	$("#selecionarProcessoRelacionado").select2();	
  	$("div#myId").dropzone({ url: "/file/post" });

/*Testes de interesse*/
	/*Implementar um contador para verificar o interesse em alterar a foto*/
	$("#usuario-imagem").click(function(){
		alert("Estamos coletando interesse dos usuários em incluir sua foto no sistema. Obrigado, você será um dos primeiros a ser notificado quando implementarmos novas funcionalidades!");
	});
	$("#usuario-imagem-xs").click(function(){
		alert("Estamos coletando interesse dos usuários em incluir sua foto no sistema. Obrigado, você será um dos primeiros a ser notificado quando implementarmos novas funcionalidades!");
	});	

	 $("#profile-acao-nova-peticao").click(function(){
	  	$.scrollTo("#usuario-resumo", 700, {offset:-60});	  	
	 });

/*Form Validations*/
	/*Formulário Peticionador*/


	//Traditional form validation sample
		$('#peticionador').validate({
            focusInvalid: false, 
            ignore: "",
            rules: {
                nome: {
                    minlength: 2,
                    required: true
                }
            },
            messages: {
                nome: {
                    minlength: "Digite pelo menos dois caracteres",
                    required: "O campo nome é obrigatório"
                }
            },            

            invalidHandler: function (event, validator) {
				//display error alert on form submit    
            },

            errorPlacement: function (label, element) { // render error placement for each input type   
				$('<span class="error"></span>').insertAfter(element).append(label)
                var parent = $(element).parent('.input-with-icon');
                parent.removeClass('success-control').addClass('error-control');  
            },

            highlight: function (element) { // hightlight error inputs
				
            },

            unhighlight: function (element) { // revert the change done by hightlight
                
            },

            success: function (label, element) {
				var parent = $(element).parent('.input-with-icon');
				parent.removeClass('error-control').addClass('success-control'); 
            },

            submitHandler: function (form) {
            
            }
        });		

});