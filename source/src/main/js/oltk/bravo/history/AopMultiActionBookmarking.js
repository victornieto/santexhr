;(function(){

oltk.namespace('oltk.bravo.history');

oltk.include('jquery/jquery.js');
oltk.include('jquery/aop/aop.js');

oltk.include('oltk/bravo/history/YuiHistoryManager.js');

// IMPORTS
var History = oltk.bravo.history.YuiHistoryManager;

// CONSTANTS
var DO_NOT_BOOKMARK = false;

//============================================================================
// AOP MULTI ACTION BOOKMARKING
//============================================================================
/**
 * Weaves bookmarking advice before methods of the given target.
 * 
 * @staticoltk.bravo.history.AopMultiActionBookmarking
 * @class 
 */
var self = oltk.bravo.history.AopMultiActionBookmarking = {
	
	//------------------------------------------------------------------------
	// MEMBERS
	//------------------------------------------------------------------------
	/**
	 * Cache of bookmarked actions.  
	 * @private
	 * @type Hash &lt;string,function&gt;
	 */
	_actions: {},
	
	/**
	 * Cache of default target methods.  Key is bookmark name.
	 * @private
	 * @type Hash &lt;string,function&gt;
	 */
	_defaultActions: {},
	
	/**
	 * @private
	 * @type int
	 */
	_actionId: 0,
	
	//------------------------------------------------------------------------
	// METHODS
	//------------------------------------------------------------------------
	/**
	 * Weaves bookmarking before methods of the given target.
	 * @param {Object *} varargs any number of advice definitions.  
	 * Each definition is a hash with the following properties:
	 * {
	 * 		target: the class who's methods to weave
	 * 		methods: {string|regexp} a string or regular expression 
	 * 			specifying which methods to bookmark.
	 * 		name: {string} the name of the bookmark.  Multiple methods/targets
	 * 			may be woven to bookmarks of the same name.
	 * 		[defaultTo]: {string} the method to invoke when the named bookmark
	 * 			has no history.  The target's method will recieve no arguments.
	 * }
	 * @return {Bookmarking} returns self for chaining.
	 */
	weave: function(varargs) {
		for(var i=0; i<arguments.length; i++) {
			var o = arguments[i];
			if(!o) {
				continue;
			}
			self._register(o);
			self._weaveAdvice(o);
		}
		return self;
	},
	
	/**
	 * Initializes bookmarking. This method provides an onDomReady callback
	 * so that it may also be used to initialize the application.
	 * Bookmarking.initialize must be called AFTER all advice has been woven.
	 * @param {Object} [o] a hash with the following options:
	 * {
	 * 		onDomReady: {function} a callback to execute when the dom 
	 * 			has rendered.  Fires before any bookmarked actions are executed.
	 * 
	 * 		onStateReady: {function} a callback to execute after all bookmarked
	 * 			actions have been executed.
	 * }
	 */
	initialize: function(o) {
		o = o || {};
		History.initialize({
			defaultState: self._callDefaultActions,
			onDomReady: o.onDomReady,
			onStateReady: o.onStateReady
		});
	},
	
	//------------------------------------------------------------------------
	// PRIVATE IMPLEMENTATION
	//------------------------------------------------------------------------
	/**
	 * @private
	 * @param {Object} o
	 */
	_register: function(o) {
		if(o.defaultTo) {
			self._cacheDefaultAction(o);
		}
		
		History.register(o.name, function(/*array|""*/ state){
			if(!state) {
				// bookmarking in the default state prevents the user from 
				// leaving the page via the back button.
				DO_NOT_BOOKMARK = true;
				self._getDefaultActionFor(o.name).call();
				return;
			}
			var id = state.pop();
			self._actions[id].apply(null, state);
		});
	},
	
	/**
	 * @private
	 * @param {Object} o
	 */
	_weaveAdvice: function(o) {
		for(var p in o.target) {
			if(self._isNotAdvisableMethodOf(o, p)) {
				continue;
			}
			self._advise(o, p, o.defaultTo);
		}
	},
	
	/**
	 * Needs to be in a separate closure to increment id properly.
	 * @private
	 * @param {Object} o
	 * @param {string} p
	 * @param {boolean} defaultTo
	 */
	_advise: function(o, p, defaultTo) {
		var id = self._cacheAction(o.target, p);
		
		if(p === defaultTo) {
			jQuery.aop.before({target: o.target, method: p}, 
				function(/*arguments*/ args) {
					if(DO_NOT_BOOKMARK){
						DO_NOT_BOOKMARK = false;
						return;
					}
					else {
						args = oltk.toArray(args).concat(id);
						History.bookmark(o.name, args);
					}
				}
			);
		}
		else {
			jQuery.aop.before({target: o.target, method: p}, 
				function(/*arguments*/args) {
					args = oltk.toArray(args).concat(id);
					History.bookmark(o.name, args);
				}	
			);
		}
	},
	
	/**
	 * @private 
	 * @param {Object} target
	 * @param {string} method 
	 */
	_cacheAction: function(target, method) {
		var action = function() {
			target[method].apply(target, arguments);
		}
		var id = self._actionId++;
		self._actions[id] = action;
		return id;
	},
	
	/**
	 * @private
	 * @param {Object} o
	 */
	_cacheDefaultAction: function(o) {
		var action = function() {
			o.target[o.defaultTo].apply(o.target, arguments);
		};
		self._defaultActions[o.name] = action;
	},
	
	/**
	 * @private
	 * @param {string} name the name of the bookmark.
	 * @return {function}
	 */
	_getDefaultActionFor: function(name) {
		return self._defaultActions[name] || oltk.emptyFn;
	},
	
	/**
	 * @private
	 * @param {Object} o
	 * @param {string} p property
	 * @return {boolean} true
	 */
	_isNotAdvisableMethodOf: function(o, p) {
		if(typeof o.target[p] !== 'function') {
			return true;
		}
		else if(typeof o.methods === 'string') {
			return o.methods !== p;
		}
		else {
			return !o.methods.test(p) && p !== o.defaultTo;
		}
	},
	
	/**
	 * @private
	 */
	_callDefaultActions: function() {
		for(var p in self._defaultActions) {
			if(typeof self._defaultActions[p] !== 'function') {
				continue;
			}
			self._defaultActions[p].call();
		}
	}
	
};
	
})();
