(function($){
	$.fn.extend({
		styled : function(options) {
		if(!$.browser.msie || ($.browser.msie && $.browser.version > 6)){
			return this.each(function() {
					var selectBox = $(this);    
					// Verifica se o elemento ja foi estilizado
					if(selectBox.hasClass("styled-select"))
						return;
					
					// Resolve problema de componentes que foram reinseridos apos uma requisicao ajax
					$(this).siblings('.styledSelectBox').each(function(){
						if($(this).data().selectBoxId == selectBox.attr('id'))
							$(this).remove();
					});
					
					// Valor inicial
					$(this).after('<span class="styledSelectBox"><span class="styledSelectBoxInner">'+getText(this)+'</span></span>');
					
                    
                    var selectBoxCssWidth = selectBox.css('width');
					var selectBoxSpan = $(this).next();
					selectBoxSpan.data({selectBoxId: $(this).attr('id')});
					var selectBoxSpanWidth = getWidth(selectBox);
					var selectBoxSpanInner = selectBoxSpan.find(':first-child');
					
					// Aplica estilo 
					selectBox.css({position:'absolute', opacity: 0, fontSize: $(this).next().css('font-size'), border: '1px solid #000000'});
					selectBoxSpan.css({display:'inline-block'});
					selectBoxSpanInner.css({display:'inline-block'});
					
					// Corrige a largura dos dois elementos 
					setWidth(selectBox, selectBoxSpanInner, selectBoxSpanWidth);
					
					// Corrige altura do selectbox em relacao ao elemento estilizado 
					selectBox.css({paddingBottom : getPaddingHeight(selectBox, selectBoxSpan)});
					
					// Atribui eventos 
					$(this).focus(function(){
						selectBoxSpanInner.addClass('focus');
					});
					$(this).blur(function(){
						selectBoxSpanInner.removeClass('focus');
					});
					$(this).change(function(){
						selectBoxSpanInner.html(getText(selectBox));
					});
					$(this).keyup(function(e){
						if(e.keyCode == '37' || e.keyCode == '38' || e.keyCode == '39' || e.keyCode == '40')
							selectBoxSpanInner.text(getText(selectBox));
					});
					
					$(this).bind('disableChange', function(){
						if($(selectBox).attr('disabled')) {
							$(selectBoxSpan).attr('disabled', 'disabled');
						}
						else {
							$(selectBoxSpan).removeAttr('disabled');
						}
						selectBoxSpanInner.html(getText(selectBox));
					});
					
					if($(selectBox).attr('disabled')) {
						$(selectBoxSpan).attr('disabled', 'disabled');
					}
					
					// Ajusta largura do elemento quando a janela for redimensionada
					if(selectBoxCssWidth.indexOf("%") >= 0)
					{
						$(window).resize(function() {							
							// Restaura as propriedades inciais do selectbox
							selectBox.css({position: 'static', width : selectBoxCssWidth});
							// Oculta o selectBoxSpan
							selectBoxSpan.css({position: 'absolute', opacity: 0});
							
							// Obtem a largura do selectbox
							selectBoxSpanWidth = getWidth(selectBox);
							
							// Corrige a largura dos dois elementos 
							setWidth(selectBox, selectBoxSpanInner, selectBoxSpanWidth);

							// Oculta o selectbox e restaura o selectBoxSpan
							selectBox.css({position: 'absolute', paddingBottom: getPaddingHeight(selectBox, selectBoxSpan)});
							selectBoxSpan.css({position: 'static', opacity: 1});
						});
					}
					
					$(this).addClass('styled-select');
				});
			}
		}
	});
	
	var setWidth = function(el1, el2, w) {
		if($.browser.msie) {
			$(el1).width(w + 25);
			$(el2).width(w);
		}
		else {
			$(el1).width(w);
			$(el2).width(w);
		}
	} 
	
	var getWidth = function(el) {
		var width = 0;
		if($(el).is(':hidden')) {
			width = $(el).css('width');
			width = width.replace(/\D*/g, ''); 
		}
		else {
			width = $(el).width(); 
		}
			
	    return parseInt(width) + parseInt($(el).css('border-left-width')) + parseInt($(el).css('border-right-width'));
	}
	
	var getPaddingHeight = function(el1, el2) {
	    return Math.max($(el2).height() - $(el1).height(), 0);
	}
	
	var getText = function(el) {
	    if($(el).find(':selected').text() == '')
	        return '&nbsp;';
	        
	    return $(el).find(':selected').text();
	}
	var debug = function(msg) {
		$('#debug').append('<div>'+msg+'</div>');
	}
})(jQuery);