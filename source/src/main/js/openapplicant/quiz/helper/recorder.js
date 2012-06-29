oltk.namespace('openapplicant.quiz.helper');

oltk.include('openapplicant/quiz/helper/string_delta.js');
oltk.include('openapplicant/quiz/helper/json.js');

(function($) {
	
var loadTimestamp;


var textarea; //content = textarea.val()

// stored as JSON strings
// [{ t: text/type OR d: diff, m: milliseconds}]
var keypressEvents;
var pasteEvents;

var focusEvents;
var hasFocus;
var focusCheckerInterval;

var keyPresses;
var erasePresses;
var pastePresses;
var keyChars;
var eraseChars;
var pasteChars;

var cutCopy;

var self = openapplicant.quiz.helper.recorder = {
	
	//------------------------------------------------------------------------
	// METHODS
	//------------------------------------------------------------------------
	/**
	 * @param el the html textarea
	 */
	init: function(el) {
	
		loadTimestamp = new Date().getTime();
		hasFocus = true;
				
		textarea = $(el);
		
		keypressEvents = [];
		focusEvents = [];
		pasteEvents = [];
		
		keyPresses = 0;
		erasePresses = 0;
		pastePresses = 0;
		keyChars = 0;
		eraseChars = 0;
		pasteChars = 0;
		
		cutCopy = false;
		
		self._keypress();
   		textarea.bind('keypress', function() {
   			setTimeout(self._keypress, 25);
   		});
   
   		if(self._canCheckFocus()) {
   			focusCheckerInterval = setInterval(self._checkFocus, 50);
   		} else {
			$(window.top).bind('blur', self._focusChange );
			$(window.top).bind('focus', self._focusChange );  
		}
		
		textarea.bind('cut', self._cutCopy );
		textarea.bind('copy', self._cutCopy );
  	},

	/**
	 * Destroys and returns the focus/blur trail.
	 * I know it's bad to have a getter do something destructive whoops.
	 */
	getResponse: function() {
  		
  		textarea[0].disabled = true;
		self._focusChange({type: "focus"}); //FIXME: basically this whole method needs to be fixed, but this prevents
		//someone being away on one question from spilling over into the next question on the exam results page.

  		self._keypress();
  		self._destroy();
  		
  		this.keypressEvents = keypressEvents;
  		this.focusEvents = focusEvents;
  		this.pasteEvents = pasteEvents;
    	
  		var now = new Date().getTime();
  		
  		var hesitationTime = 0;
  		var typingTime = 0;
  		var reviewingTime = 0;
  		var focusTime = 0;
  		var awayTime = 0;
  		var totalTime = now - loadTimestamp;

  		//while they may not have typed anything, they hesitated in hitting submit
  		if(keypressEvents.length > 1) {
  			hesitationTime = keypressEvents[1].m;
  		}
  		
  		//there's an init keypress and one in the beginning of this function
		if(keypressEvents.length > 2) {
			reviewingTime = totalTime - keypressEvents[keypressEvents.length-2].m;
			typingTime = totalTime - (hesitationTime + reviewingTime);
		}
		
		var browserType = ""; // annoying that you can't just lookup the type
		$.each($.browser, function(i, val) {
		    if(val == true) {
		        browserType = i;
		    }
		});

		var browserVersion = $.browser.version;
		
		//duplicate events need to be removed for FF2 and Chrome
		//having this code dosen't hurt other browsers
		self._removeDuplicateFocusEvents();
		
		for(var i=0; i<focusEvents.length; i++) {
			
			if(focusEvents[i].t != "blur")
				continue;
			
			if( i+1 < focusEvents.length) {
				awayTime += focusEvents[i+1].m - focusEvents[i].m;
			} else {
				awayTime += now - focusEvents[i].m;
			}

		}
			
		focusTime = totalTime - awayTime;

		var focusChanges = Math.ceil(focusEvents.length/2);

		var words = [];
		words = textarea.val().replace(/\s/g,' ');
		words = words.split(' ');
		var wordCount = words.length;
		
		var wordsPerMinute = (wordCount*1000*60)/totalTime;
		
		var lines = [];
		lines = textarea.val().split('\n');
		var lineCount = lines.length;
		
		var linesPerHour = (lineCount*1000*60*60)/totalTime;
		
		return {
			loadTimestamp:	loadTimestamp,
			content: 		textarea.val(),
			keypressEvents: JSON.stringify(keypressEvents),
			focusEvents: 	JSON.stringify(focusEvents),
			pasteEvents: 	JSON.stringify(pasteEvents),
			totalTime:		Math.abs(totalTime),
			hesitationTime:	Math.abs(hesitationTime),
			reviewingTime:	Math.abs(reviewingTime),
			typingTime:		Math.abs(typingTime),
			focusTime:		Math.abs(focusTime),
			awayTime:		Math.abs(awayTime),
			keyPresses:		keyPresses,
			erasePresses:	erasePresses,
			pastePresses:	pastePresses,
			keyChars:		keyChars,
			eraseChars:		eraseChars,
			pasteChars:		pasteChars,
			cutCopy:		cutCopy,
			focusChanges:	focusChanges,
			wordCount:		wordCount,
			wordsPerMinute: wordsPerMinute,
			lineCount:		lineCount,
			linesPerHour:	linesPerHour,
			browserType:	browserType,
			browserVersion: browserVersion
		};
      
    },
	
	_canCheckFocus: function() {
		try {
			return window.top.document.hasFocus();
		} catch(err) {
			return false;
		}
	},
	
	_removeDuplicateFocusEvents: function() {
		for(var i=0; i<focusEvents.length; i++) {
			var next = i+1;

			// for some reason this doesn't work if i have the if statement
			// in the while condition?!?
			while( next < focusEvents.length) {
				if(focusEvents[next].t == focusEvents[i].t ||
				   focusEvents[next].m < focusEvents[i].m + 50) {
					focusEvents.splice(next,1);
				} else {
					break;
				}
			}
		}
	},
    
    /**
     * Unbinds the listener functions from the textarea and window
     */
  	_destroy: function() {    	
   		textarea.unbind('change', self._keypress );
   		textarea.unbind('keypress', self._keypress );
   		
   		if(self._canCheckFocus()) {
   			clearInterval(focusCheckerInterval);
   		} else {
			$(window.top).unbind('blur', self._focusChange );
			$(window.top).unbind('focus', self._focusChange );  
		}
		
		textarea.unbind('cut', self._cutCopy );
		textarea.unbind('copy', self._cutCopy );
  	},
    
    /*
     * EVENT HANDLERS
     */
  
    /**
     * Bound to the textarea field's keypress event, this function checks the 
     */
	_keypress: function(e) { 
  		var now = new Date().getTime() - loadTimestamp;
  		
		var currentLength = textarea.val().length;
		var diff = currentLength - keyChars;
		keyChars = currentLength;
		
		if(diff > 0 && diff <= 3) {
			keyPresses++;	
		}
		else if(diff < 0) {
			erasePresses++;
			eraseChars-= diff;
		}
		else if(diff > 2) {  //if you're jamming on keys really quickly you suck
			pastePresses++;
			pasteChars+= diff;
	    	pasteEvents.push({ d: diff, m: now});
		}
		
		keypressEvents.push({
			t: StringDelta.getDelta(textarea.val()),
			m: now
		});
	},
	
	/*
	 * This is specifically for the multiple choice questions that
	 * need to force a keypress but dont want it to look like the
	 * person is pasting.
	 */
	keypressNoPaste: function () {
		var currentLength = textarea.val().length;
		var diff = currentLength - keyChars;
		keyChars = currentLength;
		
		keyPresses++;
		
		keypressEvents.push({
			t: StringDelta.getDelta(textarea.val()),
			m: new Date().getTime() - loadTimestamp
		});
	},

	_checkFocus: function() {
		if(hasFocus != window.top.document.hasFocus()) {
			hasFocus = window.top.document.hasFocus();
			var type = hasFocus ? "focus" : "blur";
			self._focusChange({type: type});
		}
	},
	
	/**
	 * Bound to the window's blur and focus event
	 */
    _focusChange: function(e) {
    	focusEvents.push({ t: e.type, m: new Date().getTime() - loadTimestamp });
    },
    
	/**
	 * Bound to the textarea's cut and copy event. This isn't helpful information 
	 * as it simply tells us if they copied from within the 
	 */
    _cutCopy: function() {
    	cutCopy = true;
    }
	
};
	
})(jQuery);