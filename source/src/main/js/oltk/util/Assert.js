;(function() {
	
oltk.namespace('oltk.util');

oltk.include('oltk/util/LangUtils.js');
oltk.include('oltk/util/StringUtils.js');

//============================================================================
// ASSERT
//============================================================================
/**
 * Class of static assertion utility methods.
 * @class oltk.util.Assert
 */
var Assert = oltk.util.Assert = {
	
	//========================================================================
	// METHODS
	//========================================================================
	
	//------------------------------------------------------------------------
	// OFF
	//------------------------------------------------------------------------
	/**
	 * Disable assertions permanently.
	 * 
	 * @method
	 * @static
	 * @memberOf oltk.util.Assert
	 */
	off: function() {
		for(var p in Assert) {
			if(!oltk.util.LangUtils.isFunction(Assert[p])) {
				continue;
			}
			Assert[p] = oltk.emptyFn;
		}
	},
	
	/**
	 * Assert a boolean expression, throwing an error if the test
	 * result is false.  This method will throw an error if the expression
	 * is not a boolean type.  Use <a href="oltk.util.Assert.html#isValue>
	 * isValue</a> to test if an object is null/undefined/NaN.
	 *
	 * <p><pre>
	 * Assert.isTrue(1 < 2)    								= pass
	 * Assert.isTrue(true)     								= pass
	 * Assert.isTrue(false)    								= fail
	 * Assert.isTrue(null)     								= throws error.  null is not a boolean
	 * Assert.isTrue(window != null) = pass
	 * Assert.isTrue(1 != 0, 'The value cannot equal zero') = pass
	 * </pre>
	 *
	 * @function
	 * @memberOf oltk.util.Assert
	 * @static
	 * @param {boolean} expression a boolean expression
	 * @param {string}[message] an optional error message to throw if 
	 * the assertion fails.
	 */
	isTrue: function(expression, message) {
	    Assert.isBoolean(expression, 'oltk.util.Assert.isTrue: expression must be a boolean ' + expression);
	    if(!expression) {
	        throw message || '[Assertion failed] - this expression must be true';
	    }
	},

	//========================================================================
	// TYPE ASSERTIONS
	//========================================================================
	/**
	 * Assert that an object is a legitimate value and not null/undefined/NaN.
	 * 0/false/'' are treated as legitimate values.
	 *
	 * <p><pre>
	 * Assert.isValue(null)    										= fail
	 * Assert.isValue()        										= fail
	 * Assert.isValue(Number.NaN) 									= fail
	 * Assert.isValue(0)       										= pass
	 * Assert.isValue(false)   										= pass
	 * Assert.isValue('')      										= pass
	 * Assert.isValue(window)  										= pass
	 * Assert.isValue({}, 'This object cannot be null/undefined') 	= pass
	 * </pre>
	 *
	 * @function
	 * @memberOf oltk.util.Assert
	 * @static
	 * @param {Object} obj the object to test
	 * @param {string} [message] an optional error message to throw if the 
	 * assertion fails.
	 */
	isValue: function(obj, message) {
	    if(!oltk.util.LangUtils.isValue(obj)) {
	        throw message || '[Assertion failed] - this object cannot be null/undefined/NaN';
	    }
	},
	
	/**
	 * Assert that the provided object is an instance of the provided class.
	 * Throws an error if the provided class is null/undefined/NaN.
	 *
	 * <p><pre>
	 * Assert.instanceOf(*,null)     						= Error.  Class cannot be null/undefined/NaN
	 * Assert.instanceOf(null, *)    						= fail
	 * Assert.instanceOf('s', String) 						= fail
	 * Assert.instanceOf(new String('s'), String) 			= pass
	 * Assert.instanceOf(/^$/, RegExp, 'must be a Regex') 	= pass
	 * </pre>
	 *
	 * @function
	 * @static
	 * @memberOf oltk.util.Assert
	 * @param {Object} obj the object to check
	 * @param {Class} clazz the required class
	 * @param {string} [message] an optional error message to throw if the 
	 * assertion fails.
	 */
	instanceOf: function(obj, clazz, message) {
	    Assert.isValue(clazz, 'oltk.util.Assert.isValue: Class to check against must not be null/undefined/NaN');
	    if(!(obj instanceof clazz)) {
	        throw message || '[Assertion failed] this object must be an instance of the provided class. object: ' + obj + ' class: ' + clazz;
	    }
	},
	
	/**
	 * Assert that an object is a given type.
	 *
	 * <p><pre>
	 * Assert.typeOf('s', 'string')  						= pass
	 * Assert.typeOf(null, 'string') 						= fail
	 * Assert.typeOf(null, 'object') 						= pass
	 * Assert.typeOf(1, 'number', 'Must be a number'); 	= pass
	 * </pre>
	 *
	 * @function 
	 * @static
	 * @memberOf oltk.util.Assert
	 * @param {Object} obj the object to check
	 * @param {string} type the given type
	 * @param {string}[message] an optional error message to throw if the 
	 * assertion fails.
	 */
	typeOf: function(obj, type, message) {
	    if(typeof obj !== type) {
	        throw message || '[Assertion failed] - the object must a type of ' + type;
	    }
	},
	
	/**
	 * Assert that an object is an array.  Because of restrictions in safari,
	 * this method tests well-known properties of the Array object, rather
	 * than using instanceof/typeof/constructor comparison. See 
	 * <a href='oltk.util.LangUtils.html#isArray>LangUtils.isArray</a>.
	 *
	 * <p><pre>
	 * Assert.isArray([1,2,3])     		= pass
	 * Assert.isArray(new Array()) 		= pass
	 * Assert.isArray(null)        		= fail
	 * Assert.isArray('str')       		= fail
	 * Assert.isArray(arguments)		= fail
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.Assert
	 * @param {Object} obj the object to check
	 * @param {string} [message] an optional error message to throw if the 
	 * assertion fails.
	 */
	isArray: function(obj, message) {
	    if(!oltk.util.LangUtils.isArray(obj)) {
	        throw message || '[Assertion failed] - this object must be an array. ' + obj;
	    }
	},
	
	/**
	 * Assert that an object is a boolean.
	 *
	 * <p><pre>
	 * Assert.isBoolean(false)             = pass
	 * Assert.isBoolean(new Boolean(true)) = pass
	 * Assert.isBoolean(null)              = fail
	 * Assert.isBoolean('true')            = fail
	 * </pre>
	 *
	 * @function
	 * @static
	 * @memberOf oltk.util.Assert
	 * @param {Object} obj the object to check
	 * @param {String} [message] an optional error message to throw if the
	 * assertion fails.
	 */
	isBoolean: function(obj, message) {
	    if(!oltk.util.LangUtils.isBoolean(obj)) {
	        throw message || '[Assertion failed] - this object must be a boolean. object: ' + obj;
	    }
	},
	
	/**
	 * Assert that an object is a string.
	 *
	 * <p><pre>
	 * Assert.isString('s')                = pass
	 * Assert.isString(new String('s'))    = pass
	 * Assert.isString(null)               = fail
	 * Assert.isString('')                 = pass
	 * </pre>
	 *
	 * @function
	 * @static
	 * @memberOf oltk.util.Assert
	 * @param {Object} obj the object to check
	 * @param {String} [message] an optional error message to throw if hte 
	 * assertion fails.
	 */
	isString: function(obj, message) {
	    if(!oltk.util.LangUtils.isString(obj)) {
	        throw message || '[Assertion failed] - this object must be a string. object: ' + obj;
	    }
	},
	
	//------------------------------------------------------------------------
	// RESPONDS TO
	//------------------------------------------------------------------------
	/**
	 * Assert that a function or object declares a function of a given name.
	 * 
	 * @example
	 * Assert.respondsTo(obj, 'toString')		= pass
	 * Assert.respondsTo(Array, 'foo')			= fail
	 * Assert.respondsTo(String, 'length')		= fail
	 * 
	 * @method
	 * @static
	 * @memberOf oltk.util.Assert
	 * @param {Function|Object} obj the item to test.
	 * @param {string} method the name of the function to test.
	 * @param {string} [message] an optional error message to throw if 
	 * the assertion fails.
	 */
	respondsTo: function(obj, method, message) {
		if(!oltk.util.LangUtils.hasMethod(obj, method)) {
			throw message || '[Assertion failed] - this object must respond to ' + method;
		}
	},
	
	//========================================================================
	// STRING ASSERTIONS
	//========================================================================
	/**
	 * Assert that the given string is not empty; that is, it must not be
	 * null, undefined or the empty string.
	 *
	 * <p><pre>
	 * Assert.notEmpty(null)  		=  fail
	 * Assert.notEmpty('')    		= fail
	 * Assert.notEmpty(' ')   		= pass
	 * Assert.notEmpty('Hello') 	= pass
	 * </pre>
	 *
	 * @function 
	 * @memberOf oltk.util.Assert
	 * @static
	 * @param {string} text the string to check
	 * @param {string}[message] an optional error message to throw if the 
	 * assertion fails.
	 */
    notEmpty: function(text, message) {
	    if(oltk.util.StringUtils.isEmpty(text)) {
	        throw message || '[Assertion failed] - this string must not be empty';
	    }
	},

	/**
	 * Assert that the given string has valid text content; that is, it must
	 * not be null/undefined and must contain at least one non-whitepace character.
	 *
	 * <p><pre>
	 * Assert.notBlank(null)    	= fail
	 * Assert.notBlank('')      	= fail
	 * Assert.notBlank(' ')     	= fail
	 * Assert.notBlank('12345') 	= pass
	 * Assert.notBlank(' 12345 ') 	= pass
	 * </pre>
	 *
	 * @function 
	 * @memberOf oltk.util.Assert
	 * @static
	 * @param {string} text the string to check
	 * @param {string}[message] an optional error message to throw if the 
	 * assertion fails.
	 */
	notBlank: function(text, message) {
	    if(oltk.util.StringUtils.isBlank(text)) {
	        throw message || '[Assertion failed] - this string must not be blank';
	    }
	},
	
	//========================================================================
	// DOM ASSERTIONS
	//========================================================================
	/**
	 * Assert that the given string is a dom id.
	 * <p><pre>
	 * Assert.id('someId')
	 * </pre>
	 * @param {string} id the id to check
	 * @param {string} [message] an optional error message to throw
	 * if the assertion fails.
	 */
	id: function(id, message) {
		if(!document.getElementById(id)) {
			throw message || '[Assertion failed] - this id was not in the dom: ' + id;
		}
	}
};
	
})();

 