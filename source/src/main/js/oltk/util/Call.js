;(function() {
	
oltk.namespace('oltk.util');

oltk.include('oltk/util/LangUtils.js');

var Lang = oltk.util.LangUtils;

//============================================================================
// CALL
//============================================================================
/**
 * Utility class for chaining a list of functions.
 */
oltk.util.Call = {
	
	/** 
	 * Wraps an array of functions as a single function that notifies each.
	 * 
	 * @example
	 * 
	 * function action1(fooParam, barParam) {
	 * 		console.log(fooParam);
	 * };
	 * 
	 * function action2(fooParam, barParam) {
	 * 		console.log(barParam);
	 * };
	 * 
	 * var call = oltk.util.Call.these(
	 * 		action1,
	 * 		action2,
	 * );
	 * 
	 * call('foo', 'bar');
	 * 
	 * // logs 'foo', 'bar'
	 * 
	 * @param {array|arguments} an array of functions to execute.  Each 
	 * function recieves the parameters passed to the return function of this
	 * method. This argument may be an array or varargs.
	 * @return {Function} a single function that notifies each of the 
	 * parameter function.
	 */
	these: function() {
		if(Lang.isArray(arguments[0])) {
			return _createCallWorker(arguments[0]);
		}
		else {
			return _createCallWorker(arguments);
		}
	}
};

/**
 * @private
 * @param {array|arguments} calls
 * @return {function}
 */
function _createCallWorker(calls) {
	return function() {
		for(var i=0; i<calls.length; i++) {
			if(!Lang.isFunction(calls[i])) {
				continue;
			}
			calls[i].apply(null, arguments);
		}
	};
};
	
})();
