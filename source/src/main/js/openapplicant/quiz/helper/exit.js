oltk.namespace('openapplicant.quiz.helper');

oltk.include('jquery/jquery.js');

;(function($) {

var self = openapplicant.quiz.helper.exit = {
		
	init: function() {	
		window.onbeforeunload = function(e) {
			e.returnValue = "Exiting will invalidate your exam.";
			e.preventDefault();
			e.stopPropagation();
			e.cancelBubble = true;
		};
	},
	
	destroy: function() {
		window.onbeforeunload = null;
    }
       
};

})(jQuery); 