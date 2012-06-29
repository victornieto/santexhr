;(function() {
	
oltk.namespace('oltk.util');

//============================================================================
// LANG UTILS
//============================================================================
/**
 * Provides language utilities and extensions.  
 *
 * @class
 * @static
 */
var Lang = oltk.util.LangUtils = {
	
	//========================================================================
	// METHODS
	//========================================================================

	/**
	 * Determines whether or not the provided object is an array.  
	 * Testing typeof/instanceof/constructor of arrays across frame boudnaries
	 * isn't possible in Safari unless you have a reference to the other frame
	 * to test against its Array prototype.  To handle this case, we test
	 * well-known array properties instead. See YAHOO.lang.isArray
	 *
	 * <p><pre>
	 * LangUtils.isArray([1,2,3]);  			= true
	 * LangUtils.isArray(new Array(1,2,3)); 	= true
	 * LangUtils.isArray(arguments)				= false
	 * LangUtils.isArray('hello')				= false
	 * </pre>
	 *
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the object is an array.
	 */
	isArray: function(o) {
		if(!o) {
			return false;
		}
		return Lang.isNumber(o.length) && 
			Lang.isFunction(o.pop) && 
			Lang.isFunction(o.push) &&
			Lang.isFunction(o.reverse) &&
			Lang.isFunction(o.shift) &&
			Lang.isFunction(o.sort) &&
			Lang.isFunction(o.splice) &&
			Lang.isFunction(o.unshift) &&
			Lang.isFunction(o.concat) &&
			Lang.isFunction(o.join) &&
			Lang.isFunction(o.slice) &&
			!Lang.isArguments(o);
	},
	
	/**
	 * Determine whether the given object is the arguments object.'
	 * @example
	 * <p><pre>
	 * LangUtils.isArguments(arguments)		= true
	 * LangUtils.isArguments([])			= false
	 * LangUtils.isArguments(undefined)		= false
	 * </pre>
	 * 
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the object is arguments.
	 */
	isArguments: function(o) {
		if(!o) {
			return false;
		}
		return Lang.isNumber(o.length) && Lang.isFunction(o.callee);
	},
	
	/**
	 * Determines whether or not the provideed object is a boolean.
	 *
	 * <p><pre>
	 * LangUtils.isBoolean(false); 					= true
	 * LangUtils.isBoolean(new Boolean(false)); 	= true
	 * </pre>
	 *
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the obejct is a boolean.
	 */
	isBoolean: function(o) {
		return typeof o === 'boolean' || o instanceof Boolean;
	},
	
	/**
	 * Determines whether or not the provided object is a function.
	 * 
	 * <p><pre>
	 * LangUtils.isFunction(document.getElementById)		= true
	 * LangUtils.isFunction(new Function())					= true
	 * </pre>
	 * 
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the provided object is a function.
	 */
	isFunction: function(o) {
		return typeof o === 'function' || o instanceof Function;
	},
	
	/**
	 * Determines whether or not the provided object is null.
	 *
	 * @function 
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the object is null
	 */
	isNull: function(o) {
		return o === null;
	},
	
	/**
	 * Determines whether or not the provided object is a legalnumber.
	 *
	 * <p><pre>
	 * LangUtils.isNumber(3); 				= true 
	 * LangUtils.isNumber(new Number(3)); 	= false
	 * LangUtils.isNumber(Number.NaN); 		= false
	 * </pre>
	 *
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the object is a number.
	 */
	isNumber: function(o) {
		return (typeof o === 'number' || o instanceof Number) && isFinite(o);
	},
	
	/**
	 * Determines whether or not the provided object is of type object or 
	 * function.
	 * 
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the object is of type object or function
	 */
	isObject: function(o) {
		return (o &&(typeof o === 'object' || Lang.isFunction(o))) || false;
	},
	
	/**
	 * Determines whether or not the provided object is a string
	 *
	 * <p><pre>
	 * LangUtils.isString('hi'); 				= true
	 * LangUtils.isString(new String('hi')); 	= true
	 * </pre>
	 *
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the object is a string.
	 */
	isString: function(o) {
		return typeof o === 'string' || o instanceof String;
	},
	
	/**
	 * Determines whether or not the provided object is undefined
	 * See YAHOO.lang.isUndefined
	 *
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the object being tested
	 * @return {boolean} true if the object is undefined
	 */
	isUndefined: function(o) {
		return typeof o === 'undefined';
	},
	
	/**
	 * Convenience method for detecting a legitimate non-null value.
	 * Returns false for null/undefined/NaN, true for other values,
	 * including 0/false/'' 
	 * See YAHOO.lang.isValue
	 *
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the item to test
	 * @return {boolean} true if it is not null/undefined/NaN.
	 */
	isValue: function(o) {
		return (Lang.isObject(o) || Lang.isString(o) || Lang.isNumber(o) || Lang.isBoolean(o));
	},
	
	/**
	 * Check if the given object implements a method of the given name.
	 * 
	 * @function
	 * @memberOf oltk.util.LangUtils
	 * @static
	 * @param {any} o the item to test (may be null)
	 * @param {string} method the name of the method to look for.
	 * @return {boolean} true if o has a method of the given name.
	 */
	hasMethod: function(o, method) {
		if(!o) {
			return false;
		}
		else if(Lang.isFunction(o)) {
			return Lang.isFunction(o[method]) || Lang.isFunction(o.prototype[method]);
		}
		else {
			return Lang.isFunction(o[method]);
		}
	},
	
	/**
	 * Retrieve a global variable/property by it's string name.
	 * Returns null  if the variable/property is null or undefined.
	 * 
	 * @example
	 * LangUtils.forName('String')			-> String
	 * LangUtils.forName('oltk')			-> oltk
	 * LangUtils.forName('window.oltk')		-> oltk
	 * LangUtils.forName('console.log')		-> log function (if firebug is enabled)
	 * 
	 * @memberOf oltk
	 * @param {string} name the name of the global variable/property to retrieve.
	 * @return {any|null} 
	 */
	forName: function(name) {
		if(!name) {
			return null;
		}
		if(name === 'window') {
			return window;
		}
		name = name.replace(/^window\./,'');
		var n = name.split('.');
		var o = window;
		for(var i=0; i<n.length; i++) {
			o = o[n[i]];
			if(typeof o === 'undefined' || o === null) {
				return null;
			}
		}
		return o;
	},
	
	/**
	 * Check if the given string is a global variable.  That is,
	 * check that the global object is not null or undefined.
	 * 
	 * @example
	 * LangUtils.exists('String');						->	true
	 * LangUtils.exists('window');					    ->  true
	 * LangUtils.exists('window.document');				-> 	true
	 * LangUtils.exists('console.log');					->  true if firebug is enabled
	 * LangUtils.exists('window.console.log');			->  same as above
	 * 
	 * LangUtils.namespace('some.namespace');
	 * LangUtils.exists('some.namespace');				-> true
	 * 
	 * @method exists
	 * @memberOf oltk
	 * @param {s} the global variable to check.
	 * @return {boolean} true if the global variable is not null or undefined.
	 */
	exists: function(s) {
		return null !== Lang.forName(s);
	}
};
	
})();
