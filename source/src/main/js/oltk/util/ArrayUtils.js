;(function() {

oltk.namespace('oltk.util');

oltk.include('oltk/util/LangUtils.js');

//============================================================================
// ARRAY UTILS
//============================================================================
/**
 * Class of static methods for working with arrays.
 * 
 * @class
 * @static
 */
var ArrayUtils = oltk.util.ArrayUtils = {
	
	/**
	 * Converts the given object to an array.  If an array is passed in,
	 * it will be returned without modification.
	 * 
	 * @example
	 * <pre>
	 * ArrayUtils.toArray(null) 			-> []
	 * ArrayUtils.toArray(undefined)		-> []
	 * ArrayUtils.toArray(Number.NaN)		-> []
	 * ArrayUtils.toArray('')				-> ['']
	 * ArrayUtils.toArray(0)				-> [0]
	 * ArrayUtils.toArray(false)			-> [false]
	 * ArrayUtils.toArray('foo')			-> ['foo']
	 * ArrayUtils.toArray([1,2,'foo']) 		-> [1,2,'foo'] 
	 * ArrayUtils.toArray({foo: 'bar'})		-> [ {foo: 'bar' } ]
	 * ArrayUtils.toArray(arguments)		-> instanceof Array
	 * </pre>
	 * 
	 * @param {any} a the object to convert to an array
	 * @return {array}
	 */
	toArray: function(a) {
		var Lang = oltk.util.LangUtils;
		
		if(!Lang.isValue(a)) {
			return [];
		} else if(Lang.isArray(a)) {
			return a;
		} else if(Lang.isArguments(a)) {
			return Array.prototype.slice.call(a);
		} else {
			return [a];
		}
	},
	
	/**
	 * Join all elments in an array into a single string using the given callback.
	 * The callback may return an array of strings or a single string.
	 * Returns '' if the array is empty and no defaultString is provided.
	 * 
	 * @example
	 * <pre>
	 * var html = ''.concat(
	 * 		'&lt;ol&gt;',
 	 * 			ArrayUtils.join(arr, function(each, i) {
 	 * 				return ['&lt;li&gt;',each.name,'&lt;/li&gt;'];
 	 * 			}),
	 * 		'&lt;/ol&gt;'
	 * );
	 * </pre>
	 * @param {array} a the array to join.
	 * @param {function} callback a callback to process each element. signature:
	 * 		string|array f(o, i);
	 * 		- o {Object} the current object in the array
	 * 		- i {int} the index of o.
	 * @param {string} [defaultString] a defaultString to return if
	 * 		a is empty.
	 * @return {string}
	 */
	join: function(a, callback, defaultString) {
		if(!a || !a.length) {
			return defaultString || '';
		}
		callback = callback || oltk.emptyFn;
		var result = [];
		for(var i=0; i<a.length; i++) {
			var s = callback(a[i], i);
			if(typeof s === 'string') {
				result.push(s);
			} else {
				result = result.concat(s);
			}
		}
		return result.join('');
	}
};
	
})();
