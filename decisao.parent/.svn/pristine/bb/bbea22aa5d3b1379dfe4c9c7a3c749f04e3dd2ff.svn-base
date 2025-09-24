$.extend( $.fn.dataTableExt.oPagination, {
	"swipe": {
		"fnInit": function( oSettings, nPaging, fnDraw ) {
			function swipe(action){
				oSettings.oApi._fnPageChange(oSettings, action);
				fnDraw(oSettings);
			}
			var tableId = oSettings.sTableId;
			var tableSelector = '#' + tableId;
			
			var swipeSelector = tableSelector;
			var swipeEndAnimationTimeout = 1000;
			var swipeAnimationTimeout = 150;
			var animationClasses = ['animated', 'slideOutLeft', 'slideInRight', 'slideOutRight', 'slideInLeft'].join(' ');
			
			var estaNaUltimaPagina = function() {
				return oSettings._iDisplayStart + oSettings._iDisplayLength >= oSettings.fnRecordsDisplay();
			};
			
			$(swipeSelector).die("swipeleft");
			$(swipeSelector).live("swipeleft", function(event) {
				console.log(oSettings);
				if (estaNaUltimaPagina()) {
					return;
				}
				var self = this;
				event.stopImmediatePropagation();
				var animationClassesOut = ['animated', 'slideOutLeft'].join(' ');
				var animationClassesIn = ['animated', 'slideInRight'].join(' ');
				$(self).removeClass(animationClasses).addClass(animationClassesOut);
				setTimeout(function() {
					swipe('next');
					$(self).removeClass(animationClasses).addClass(animationClassesIn);
					setTimeout(function() {
						$(self).removeClass(animationClasses);
					}, swipeEndAnimationTimeout);
				}, swipeAnimationTimeout);
			});
			
			var estaNaPrimeiraPagina = function() {
				return oSettings._iDisplayStart == 0;
			};
			
			$(swipeSelector).die("swiperight");
			$(swipeSelector).live("swiperight", function(event) {
				if (estaNaPrimeiraPagina()) {
					return;
				}
				var self = this;
				event.stopImmediatePropagation();
				var animationClassesOut = ['animated', 'slideOutRight'].join(' ');
				var animationClassesIn = ['animated', 'slideInLeft'].join(' ');
				$(self).removeClass(animationClasses).addClass(animationClassesOut);
				setTimeout(function() {
					swipe('previous');
					$(self).removeClass(animationClasses).addClass(animationClassesIn);
					setTimeout(function() {
						$(self).removeClass(animationClasses);
					}, swipeEndAnimationTimeout);
				}, swipeAnimationTimeout);
			});

			$.fn.dataTableExt.oPagination.bootstrap.fnInit(oSettings, nPaging, fnDraw);
		},

		"fnUpdate": function ( oSettings, fnDraw ) {
			$.fn.dataTableExt.oPagination.bootstrap.fnUpdate(oSettings, fnDraw);
		}
	}
} );