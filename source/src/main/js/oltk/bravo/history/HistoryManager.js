;(function() {
	
oltk.namespace('oltk.bravo.history');

oltk.include('jquery/jquery.js');

//============================================================================
// HISTORY MANAGER
//============================================================================
/**
 * History management component that allows you to register arbitrary tokens
 * that signify application history state on navigation actions.  You
 * can handle the history change event in order to reset your application UI
 * to the appropriate state when the user navigates forward or backward 
 * through the browser history stack.  Based on History.js in the Ext library.
 * This implementation does not support Safari 2.x and below.  This class
 * requires that the following markup be placed as early as possible in the 
 * dom:
 * &lt;input type="hidden" id="oltk-history-field"/&gt;
 * &lt;iframe src="blank.html" id="oltk-history-iframe" style="position:absolute;top:0px;left:0px;width:1px;height:1px;visibility:hidden"&gt;&lt;/iframe&gt;
 * 
 * The iframe is required for Internet Explorer only and blank.html can be
 * any resource on the server.
 */
var self = oltk.bravo.history.HistoryManager = {
	
	//========================================================================
	// MEMBERS
	//========================================================================
	/**
	 * Array of listener functions.  Signature:
	 * void f(string token)
	 * 	- token the current history hash (excludes # character)
	 * @type array&lt;function&gt;
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_listeners: [],
	
	/**
	 * @type HTMLElement
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_iframe: null,
	
	/**
	 * @type HTMLElement
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_hiddenField: null,
	
	/**
	 * @type boolean
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_ready: false,
	
	/**
	 * @type string
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_currentToken: '',
	
	/**
	 * @type function
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_onReady: oltk.emptyFn,
	
	//========================================================================
	// PROPERTIES
	//========================================================================
	/**
	 * Configurable dom id of a hidden input field to hold the history token.  
	 * Default is "oltk-history-field"
	 * @type string
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	fieldId: 'oltk-history-field',
	
	/**
	 * Configurable dom id of a hidden iframe to hold the history token (ie only).  
	 * Default is "oltk-history-iframe"
	 * @type string
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	iframeId: 'oltk-history-iframe',
	
	//========================================================================
	// METHODS
	//========================================================================
	/**
	 * Initialize the global history instance.
	 * 
	 * @param {Function} [onReady] a callback to fire when the history 
	 * manager is initialized.  This callback will always fire on dom ready 
	 * an should be used to restore a bookmarked state.
	 * signature: void f(string token);
	 * 	- token the current token.
	 * 
	 * @param {Object} scope the scope of the onReady callback.
	 * 
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	init: function(onReady, scope) {
		if(typeof onReady === 'function') {
			self._onReady = oltk.bind(onReady, scope);
		}
		jQuery(function(){
			self._hiddenField = document.getElementById(self.fieldId);
			if(jQuery.browser.msie) { // TODO: && jQuery.browser.msie < 8
				self._iframe = document.getElementById(self.iframeId);
			}
			self._startUp();
		});
	},
	
	/**
	 * Add a new token to the history stack.  This can be any arbitrary value,
	 * although it would commonly be the concatenation of a component id and
	 * another id marking the specific history state of that component.
	 * 
	 * @memberOf oltk.bravo.history.HistoryManager
	 * @param {string} token the value that defines a particular  
	 * application-specific history state.
	 * 
	 * @param {boolean} preventDup when true, if the passed token matches
	 * the current token it will not save a new history step.  Set to false
	 * if the same state can be saved more than once at the same history
	 * stack location. (defaults to true).
	 * 
	 * @return {boolean} true if the state was saved successfully.
	 */
	add: function(token, preventDup) {
		if(preventDup !== false) {
			if(self.getToken() == token) {
				return true;
			}
		}
		if(jQuery.browser.msie) { // TODO: && jQuery.browser.msie < 8
			return self._updateIFrame(token);
		} else {
			top.location.hash = token;
			return true;
		}
	},
	
	/**
	 * Retrieves the currently-active history token.
	 * @memberOf oltk.bravo.history.HistoryManager
	 * @return {string} the token.
	 */
	getToken: function() {
		return self._ready ? self._currentToken : self._getHash();
	},
	
	/**
	 * Adds a new history change listener.
	 * @memberOf oltk.bravo.history.HistoryManager
	 * @param {function} listener.  The listener function:
	 * void f(string token)
	 * 	- token the current history token.
	 */
	addHistoryChangeListener: function(listener) {
		if(typeof listener !== 'function') {
			return;
		}
		self._listeners.push(listener);
	},
	
	//========================================================================
	// PRIVATE IMPLEMENTATION
	//========================================================================
	/**
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 * @return {string}
	 */
	_getHash: function() {
		var href = top.location.href;
		var i = href.indexOf('#');
		return i >= 0 ? href.substr(i + 1) : '';
	},
	
	/**
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_doSave: function() {
		self._hiddenField.value = self._currentToken;
	},
	
	/**
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 * @param {string} token
	 */
	_handleStateChange: function(token) {
		self._currentToken = token;
		for(var i=0; i<self._listeners.length; i++) {
			self._listeners[i](token);
		}
	},
	
	/**
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 * @param {string} token
	 */
	_updateIFrame: function(token) {
		var html = ['<html><body><div id="state">',token,'</div></body></html>'].join('');
		try {
			var doc = self._iframe.contentWindow.document;
			doc.open();
			doc.write(html);
			doc.close();
			return true;
		} catch(e) {
			return false;
		}
	},
	
	/**
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_checkIFrame: function() {
		if(!self._iframe.contentWindow || !self._iframe.contentWindow.document) {
			setTimeout(self._checkIFrame, 10);
			return;
		}
		
		var doc = self._iframe.contentWindow.document;
		var elem = doc.getElementById('state');
		var token = elem ? elem.innerText: '';
		var hash = self._getHash();
		
		setInterval(function(){
			doc = self._iframe.contentWindow.document;
			elem = doc.getElementById('state');
			var newToken = elem ? elem.innerText : '';
			var newHash = self._getHash();
			if(newToken !== token) {
				token = newToken;
				self._handleStateChange(token);
				top.location.hash = token;
				hash = token;
				self._doSave();
			} else if(newHash !== hash) {
				hash = newHash;
				self._updateIFrame(newHash);
			}
		}, 50);
		self._ready = true;
		self._onReady(hash);
	},
	
	/**
	 * @private
	 * @memberOf oltk.bravo.history.HistoryManager
	 */
	_startUp: function() {
		self._currentToken = self._hiddenField.value;
		
		if(jQuery.browser.msie) { // TODO: && jQuery.browser.msie < 8. use jQuery.on(window,'hashchange') for ie8. See History._initialize in YUI
			self._checkIFrame();
		} else {
			var hash = self._getHash();
			setInterval(function(){
				var newHash = self._getHash();
				if(newHash !== hash) {
					hash = newHash;
					self._handleStateChange(hash);
					self._doSave();
				}
			}, 50);
			self._ready = true;
			self._onReady(hash);
		}
	}
	
};
	
})();
