oltk.namespace('openapplicant.quiz.helper');

oltk.include('jquery/jquery.js');

;(function($) {

var start;
var finish;
var dom = {};
var callback1 = {};
var callback2 = {};

var itvl;
	
//============================================================================
// Helper Timer
//============================================================================
/**
 * This is a little timer that latches on to a HTML element and counts down.
 */
var self = openapplicant.quiz.helper.timer = {
	
	init: function(el, val, c1, c2) {
	   dom = $(el);
	
	   var limit = parseInt(val); //in seconds
	   if(limit <= 0 || isNaN(limit)){
		   dom.html('untimed');
		   return;
	   }
	   
	   start = new Date().getTime();
	   finish = new Date(start + limit*1000).getTime();
 	   callback1 = c1;
 	   callback2 = c2;
	   
 	   itvl = setInterval(openapplicant.quiz.helper.timer.update, 100);
	},
	
    /**
	 * Updates #clock element in the DOM with an attractive time
	 * every 100 milliseconds.
	 * 
	 * @private
	 */
	update: function() {
		var time = finish - new Date().getTime();
		
		if(time <= 0) {
			self.destroy();
			callback1.call();
		    alert('Time is up.');
		    callback2.call();
			return;
		}
		
		var seconds = Math.floor(time/1000);
		var tenths = Math.floor(time/100)%10;
		tenths = tenths==0?'0':tenths;
		dom.html(seconds+'.'+tenths+'s');
		
		if(time<30*1000)
		  dom.addClass('urgent');
	},

	destroy: function() {
		clearInterval(itvl);
	}

};
	
})(jQuery);
