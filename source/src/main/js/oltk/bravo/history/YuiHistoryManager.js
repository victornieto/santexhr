;(function(){
	
oltk.namespace('oltk.bravo.history');

oltk.include('yui/yahoo/yahoo.js');
oltk.include('yui/event/event.js');
oltk.include('yui/history/history.js');
oltk.include('yui/json/json.js');
oltk.include('jquery/jquery.js');

//============================================================================
// YUI HISTORY MANAGER
//============================================================================
/**
 * Wrapper for the YAHOO Browser History Manager. 
 * 
 * NOTE: this class is for internal use only.  See 
 * oltk.bravo.history.SingleActionBookmarking and 
 * oltk.bravo.history.AopMultiActionBookmarking for easy history management.
 * 
 * @class oltk.bravo.history.YuiHistoryManager
 */
var self = oltk.bravo.history.YuiHistoryManager = {

	//------------------------------------------------------------------------
	// MEMBERS
	//------------------------------------------------------------------------
	/**
	 * Map of moduleId to onHistoryChange callback function
	 * 
	 * @type Hash {string,Function}
	 * @private
	 * @memberOf oltk.bravo.history.YuiHistoryManager
	 */
    _callbacks: {},

	//------------------------------------------------------------------------
	// METHODS
	//------------------------------------------------------------------------
	/**
	 * Initialize the History manager.
	 * 
	 * @memberOf oltk.bravo.history.YuiHistoryManager
	 * @param {Hash} o a hash of options:
	 * {
	 * 		defaultState: {Function} a a default state to execute if there is
	 * 			no bookmarked state.
	 * 		onDomReady: {Function} a callback to execute when the dom has 
	 * 			rendered.  This fires before any history modules are notified.
	 * 		onStateReady: {Function} a callback to execute after the
	 * 			bookmark state or default state has executed.
	 * }
	 */
    initialize: function(o) {
		o = jQuery.extend({
			defaultState: oltk.emptyFn,
			onDomReady: oltk.emptyFn,
			onStateReady: oltk.emptyFn
		}, o);
		
		YAHOO.util.Event.onDOMReady(function(){
			
			o.onDomReady();
			
			jQuery('<input id="oltk-history-field" type="hidden"/>').prependTo(document.body);
			if(YAHOO.env.ua.ie) {
				jQuery('<iframe id="oltk-history-iframe" src="javascript:false;" style="position:absolute;top:0px;left:0px;width:1px;height:1px;visibility:hidden"></iframe>').prependTo(document.body);
			}
			
			YAHOO.util.History.onReady(function() {
	            if(!self._restoreToBookmarkState()) {
					o.defaultState();
				}
				o.onStateReady();
	        });
			
	        try {
	            YAHOO.util.History.initialize('oltk-history-field', 'oltk-history-iframe');
	        } catch(e) {
				oltk.log.warn('History.initialize: ', e);
	            o.defaultState(); 
	            o.onStateReady();
	        }
		});
    },
    
	/**
	 * Register a new module with the history manager.
	 * Modules must be registered before History.initialize is invoked.
	 * This method ignores duplicate registrations for the same module id
	 *
	 * @memberOf oltk.bravo.history.YuiHistoryManager
	 * @param {string} moduleId the id of the module to register
	 * @param {function} onHistoryChange void f(Object bookmarkData);
	 * 		a callback function to invoke.  Fired when the 
	 * 		forward/back button is pressed, or when a bookmarked state is 
	 * 		retrieved on page load.  This function recieves the state passed
	 * 		to bookmark.  Recieves an EMPTY STRING if no state was bookmarked.
	 */
    register: function(moduleId, onHistoryChange) {
		if(self._callbacks[moduleId]) {
			return;
		}
		
		var callback = self._wrapCallback(moduleId, onHistoryChange);
		
		// NOTE: calling YAHOO.util.History.register with an empty string occasionally causes 
		// erratic callback behavior.  YAHOO.lang.JSON.stringify('') is a workaround
        var initialState = YAHOO.util.History.getBookmarkedState(moduleId) || YAHOO.lang.JSON.stringify('');
        YAHOO.util.History.register(moduleId, initialState, callback);
        self._callbacks[moduleId] = callback;
    },

	/**
	 * Add a new state to the history stack.
	 * 
	 * @memberOf oltk.bravo.history.YuiHistoryManager
	 * @param {string} moduleId the id of the module to bookmark
	 * @param {Object} state the state to serialize.  NOTE: this object
	 * needs to be serializable to json.  This means state cannot be HTMLElements,
	 * window, XMLHttpRequest, functions or other built in classes.
	 * Preferably state should be a string, number or hash/array of strings
	 * and numbers.  Nested hashes and arrays are supported. 
	 * RegExp types are not supported.  Support for dates may come later.
	 * @see http://json.org/json2.js for more info on object serialization.
	 */
    bookmark: function(moduleId, state) {
		if(!YAHOO.util.Lang.isValue(moduleId)) {
			return;
		}
        
		state = YAHOO.lang.JSON.stringify(state);
        moduleId = moduleId + '';
        
		if(self._isCurrentStateOf(moduleId, state)) {
            return;
        }
        try {
			// push state onto the history stack, but don't trigger the 
			// history change callback
			self._callbacks[moduleId].prevent = true;
            YAHOO.util.History.navigate(moduleId, state);
        } catch(e) {
			oltk.log.warn('History.bookmark: ', e);
			self._callbacks[moduleId].prevent = false;
        }
    },
    
	//------------------------------------------------------------------------
	// PRIVATE IMPLEMENTATION
	//------------------------------------------------------------------------
	/**
	 * Decorates a YUI history callback.  Prevents autofire when manually
	 * bookmarked and allows the function to take an object as a parameter.
	 * 
	 * @private
	 * @param {string} moduleId
	 * @param {Function} onHistoryChange
	 * @return {Function} 
	 */
	_wrapCallback: function(moduleId, onHistoryChange) {
		return function(/*string*/state) {
			if(self._callbacks[moduleId].prevent) {
				self._callbacks[moduleId].prevent = false; // see History.bookmark
				return;
			}
			var o = '';
			try {
				o = YAHOO.lang.JSON.parse(state)
			} catch(e) {
				oltk.log.warn('History._wrapCallback: state=' + state,  e);  // state was corrupted
			}
			onHistoryChange.call(null, o);
		};
	},
	
	/**
	 * Check if the given state is the current state of moduleId
	 * 
	 * @private
	 * @param {string} moduleId the id of the module to check
	 * @param {string} state the state to check
	 */
    _isCurrentStateOf: function(moduleId, state) {
        return YAHOO.util.History.getCurrentState(moduleId) == state;
    },
	
	/**
	 * @private
	 * @return {boolean} true if any bookmarked state was restored.
	 */
	_restoreToBookmarkState: function() {
		var result = false;
		for(var id in self._callbacks) {
            if(!YAHOO.util.Lang.hasOwnProperty(self._callbacks, id)) {
                continue;
            }
            var state = YAHOO.util.History.getCurrentState(id);
            self._callbacks[id].call(null, state);
			result = true;
        }
		return result;
	}
};

//----------------------------------------------------------------------------
// (PRIVATE) IE FIXES
//----------------------------------------------------------------------------
/**
 * Fixes for IE
 * @private
 */
(function() {
	
	if(!YAHOO.env.ua.ie) {
		return;
	}
	// when the page is accessed via a redirct, IE7 does a double take
	// upon navigating away from the bookmarked section.  This seems to
	// fix the problem.
	top.location.hash = top.location.hash;
	
})();


	
})();
