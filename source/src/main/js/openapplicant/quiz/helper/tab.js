oltk.namespace('openapplicant.quiz.helper');

oltk.include('jquery/jquery.js');

;(function($) {

var textarea;

var self = openapplicant.quiz.helper.tab = {
		
	init: function(el) {	
		textarea = $(el);
		textarea.bind('keydown', function(e) {
	        if(e.keyCode == 9) {	        	
	            if(e.which) {	e.preventDefault();		}
	            else {			e.returnValue = false;	}
	            self.handler();
	        }
	    });
		textarea = textarea[0];
	},
	
	handler: function() {
        if(document.selection) {
            var curSelect = document.selection.createRange();   
            curSelect.text = "\t" + String(curSelect.text).replace(/\n/g, "\n\t");
        }
        else if (typeof textarea.selectionStart != "undefined") {
            var selStartMarker = textarea.selectionEnd;
            var selStart = textarea.value.substr(0, textarea.selectionStart);
            var selEnd = textarea.value.substr(textarea.selectionEnd, textarea.value.length);
            var curSelection = textarea.value.replace(selStart, '').replace(selEnd, '');
            
            curSelection = "\t" + String(curSelection).replace(/\n/g, "\n\t");
            
            textarea.value = selStart + curSelection + selEnd;
            
            var extra = String(curSelection).search("\n");
            if (extra == -1)
                extra = 1;
            
            textarea.selectionStart = selStartMarker + extra;
            textarea.selectionEnd = selStartMarker + extra;
        }
    }
       
};

})(jQuery); 