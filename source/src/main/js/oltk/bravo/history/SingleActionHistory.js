;(function(){
	
oltk.namespace('oltk.bravo.history');

oltk.include('oltk/bravo/bravo.js');
oltk.include('oltk/bravo/history/HistoryManager.js');

//============================================================================
// SINGLE ACTION HISTORY
//============================================================================
/**
 * History management class that supports bookmarking of a single controller 
 * action.  Controller actions are expressed as strings on elements with 
 * href attributes.  Each string has the following format:
 * "#controller/action&param1=value&param2=value2
 * 
 * @example
 * Given a link tag:
 * &lt;a href="#product/find&name=cars&make=bmw"&gtfind cars&lt;/a&gt;
 * 
 * Clicking the above link will invoke oltk.bravo.execute('product','find',{name:'cars',make:'bmw'})
 * and add this controller action to the history stack.  This type of history
 * management is useful for ajax apps with single-page drill down navigation
 * like gmail. 
 */
var self = oltk.bravo.history.SingleActionHistory = {
	
	//========================================================================
	// MEMBERS
	//========================================================================
	/**
	 * @private
	 * @type Command
	 */
	_defaultCommand: null,
	
	//========================================================================
	// METHODS
	//========================================================================
	/**
	 * Initialize SingleActionHistory for the application.
	 * 
	 * @param {function} onReady a function to execute when the bookmarked
	 * state is available.  Gauranteed to run when the dom is ready.  
	 * 
	 * @param {Object} scope the scope of the onReady function.
	 * If the application was accessed from a bookmark.  This function
	 * has the following signature: 
	 * 
	 * 	void f(savedAction, history)
	 *	- savedAction {Object} the bookmarked controller action, may be null if no
	 		action whas bookmarked.  This object has the following properties:
	 * 		{
	 * 			controller: {string} the name of the controller who's action
	 * 				was bookmarked.
	 * 			action: {stirng} the name of the bookmarked action.
	 * 			params: {Object} the bookmarked action parameters.
	 * 			execute: {function} a function invoking
	 * 				oltk.bravo.execute(controller, action, params);
	 * 		}
	 * 	- history {oltk.bravo.history.SingelActionHistory} the history singleton
	 * 
	 * If not accessed from a bookmark, this function recieves null as a parameter.
	 * @example
	 * <pre>
	 * 
	 * SingleActionHistory.setDefaultAction('someController','defaultView');
	 * 
	 * SingleActionHistory.init(function(action, history) {
	 * 
	 * 		dwr.engine.setAsync(false);  
	 * 		dwr.engine.setErrorHandler( function(){ history.getDefaultAction().execute(); } ) // data was corrupted
	 * 		if(action) {
	 * 			try {
	 * 				action.execute();
	 * 			} catch(e) {
	 * 				history.getDefaultAction().execute(); // bookmark was corrupted.  degrade gracefully.
	 * 			}
	 * 		} else {
	 * 			history.getDefaultAction().execute();  // no bookmark. load default view.
	 *      }
	 *      dwr.engine.setAsync(true);
	 *      
	 *      // by setting async to false, we know the content is ready now (assuming all controllers are using dwr)
	 *      jQuery('#loading').hide(); 
	 *      jQuery('#content').show(); 
	 *      
	 * });
	 * </pre>
	 */
	init: function(onReady, scope) {
		oltk.bravo.history.HistoryManager.addHistoryChangeListener(self._dispatch);
		oltk.bravo.history.HistoryManager.init(function(token) {
			var action = self._parseCommand(token);
			onReady.call(scope, action, self);
		});
	},
	
	/**
	 * Sets a default controller action to execute if there is no bookmarked state.
	 * This action recieves no parameters.
	 * 
	 * @param {string} controller the name of the controller who's action to call
	 * @param {string} action a controller method to call
	 */
	setDefaultAction: function(controller ,action) {
		var cmd = new Command();
		cmd.controller = controller;
		cmd.action = action;
		cmd.params = {};
		self._defaultCommand = cmd;
	},
	
	/**
	 * @return {Object} the default action as a command object.  The command object
	 * has the following properties:
	 * {
	 * 		controller: {string} the name of the default controller
	 * 		action: {string} the name of the method to invoke on the default
	 * 					controller
	 * 		execute: {function} executes the given action.  Internally calls
	 * 				oltk.bravo.execute(controller,action)
	 * }
	 */
	getDefaultAction: function() {
		return self._defaultCommand;
	},
	
	//========================================================================
	// PRIVATE IMPLEMENTATION
	//========================================================================
	/**
	 * Dispatches the bookmarked action on history change.
	 * @private
	 * @param {string} token the bookmark token
	 */
	_dispatch: function(token) {
		if(!token) {
			if(self._defaultCommand) {
				self._defaultCommand.execute();
			}
		} else {
			var cmd = self._parseCommand(token);
			if(cmd){
				cmd.execute();
			}
		}
	},
	
	/**
	 * Parse a controller action from the given string.  String has the 
	 * following form: controller/action&param1=value1&param2=value2
	 * @private
	 * @param {string} s the string to parse.
	 * @return {Command} the controller action wrapped as a command.  
	 */
	_parseCommand: function(s) {
		s = s || '';
		var m = s.match(/(.+?)\/(.+?)&(.+)/) || s.match(/(.+?)\/(.+)/);
		if(m) {
			var cmd = new Command();
			cmd.controller = m[1];
			cmd.action = m[2];
			cmd.params = self._parseParams(m[3]);
			return cmd;
		} else {
			return null;
		}
	},
	
	/**
	 * Parse a query string into a params hash.  A query string has 
	 * the form: key1=value1&key2=value2
	 * @private
	 * @param {string} s the string to parse
	 * @return {Object} the query parameters as a hash.
	 */
	_parseParams: function(s) {
		s = s || '';
		var result = {};
		var params = s.split('&');
		for(var i=0; i<params.length; i++) {
			var tokens = params[i].split('=');
			if(tokens.length >= 2) {
				result[tokens[0]] = decodeURIComponent(tokens[1]);
			}
		}
		return result;
	}
};

//============================================================================
// COMMAND
//============================================================================
/**
 * History command object.
 */
function Command() {
	
	/**
	 * @type string
	 */
	this.controller;
	
	/**
	 * @type string
	 */
	this.action;
	
	/**
	 * @type string
	 */
	this.params;
};

/**
 * Dispatches the controller action.
 */
Command.prototype.execute = function() {
	oltk.bravo.execute(this.controller, this.action, this.params);
};
	
})();
