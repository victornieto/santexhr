oltk.namespace('oltk.util');

oltk.include('oltk/lang/Class.js');
oltk.include('oltk/util/LangUtils.js');

//============================================================================
// LIST ORDERED MAP
//============================================================================
/**
 * A collection class that maintains both numeric indexes and keys using a 
 * backing array.
 */
oltk.util.ListOrderedMap = oltk.lang.Class.extend({
	
	/**
	 * @param {array} [values] an array of values to initialize the map with.
	 * @param {function} [getKey] a strategy for retrieving each key's value.
	 * See putAll method.  Required if values is specified.
	 */
	$constructor: function(values, getKey) {
		/**
		 * Array of keys in their insert order.
		 * @private
		 * @type array<String>
		 */
		this._insertOrder = [];
		
		/**
		 * Map of keys to objects.
		 * @private
		 * @type Object
		 */
		this._map = {};
		
		if(values) {
			if(typeof getKey !== 'function') {
				throw 'ListOrderedMap.constructor: a getKey function is required for these values';
			}
			this.putAll(values, getKey);
		}
	},
	
	/**
	 * Inserts a new object into the map.
	 * @param {string} key the object's key
	 * @param {Object} value the object to insert.
	 * @return {Object} value the put object
	 * @throws TypeError if key is null|undefined|empty|NaN
	 */
	put: function(key, value) {
		if(!oltk.util.LangUtils.isValue(key) || '' === key) {
			throw new TypeError('ListOrderedMap.put: key is null|undefined|empty|NaN');
		}
		if(this.containsKey(key)) {
			this._map[key] = value;
		}
		else {
			this._map[key] = value;
			this._insertOrder.push(key);
		}
		return value;
	},
	
	/**
	 * Inserts an array of objects into the map.
	 * @param {array} values the values to insert
	 * @param {function} getKey a strategy for retrieving each value's key.
	 * Recieves each array element once and should return that element's key.
	 * @return {oltk.util.ListOrderedMap} the ordered map.
	 */
	putAll: function(values, getKey) {
		value = values || [];
		for(var i=0, n=values.length; i<n; i++) {
			var key = getKey(values[i]);
			this.put(key, values[i]);
		}
		return this;
	},
	
	/**
	 * Removes an object at the given key.
	 * @param {string} key the object's key
	 * @return {Object} the removed object or null if it's key did not exist.  
	 */
	remove: function(key) {
		if(!this.containsKey(key)){
			return null;
		}
		var result = this._map[key];
		var i = this.indexOf(key);
		this._insertOrder.splice(i,1);
		delete this._map[key];
		return result;
	},
	
	/**
	 * Finds the index of the given key, returning -1 if the key is not
	 * found.
	 * @param {string} key the key to find the index of.
	 * @return {int}
	 */
	indexOf: function(key) {
		for(var i=0; i<this._insertOrder.length; i++) {
			if(this._insertOrder[i] == key) {
				return i;
			}
		}
		return -1;
	},
	
	/**
	 * Retrieves a value with the given key.  Returns null if there is no
	 * mapping for key.
	 * @param {string} key the object's key
	 * @return {Object} 
	 */
	get: function(key) {
		return this.containsKey(key) ? this._map[key] : null;
	},
	
	/**
	 * Retrieves a value by it's numeric index.  Returns null if the index
	 * is out of range.
	 * @param {int} index the index of the value to get
	 * @return {any} the value or null.
	 */
	getByIndex: function(index) {
		if(index < 0 || index >= this.size()) {
			return null;
		}
		return this.get(this._insertOrder[index]);
	},
	
	/**
	 * Returns the maps values in the order they were inserted.
	 * @return {array}
	 */
	values: function() {
		var result = new Array(this.size());
		for(var i=0, n=result.length; i<n; i++) {
			result[i] = this.get(this._insertOrder[i]);
		}
		return result;
	},
	
	/**
	 * Returns the maps keys in the order they were inserted.
	 * @return {array}
	 */
	keys: function() {
		return this._insertOrder.slice();
	},
	
	/**
	 * Returns the size of the map.
	 * @return {int}
	 */
	size: function() {
		return this._insertOrder.length;
	},
	
	/**
	 * Returns true if the map contains no mappings.
	 * @return {boolean}
	 */
	isEmpty: function() {
		return this.size() == 0;
	},
	
	/**
	 * Removes all mappings from this map.
	 */
	clear: function() {
		this._map = {};
		this._insertOrder = [];
	},
	
	/**
	 * Returns true if the map contains an entry for the given key.
	 * @param {string} key the key to check
	 * @return {boolean} 
	 */
	containsKey: function(key) {
		return key in this._map;
	},
	
	/**
	 * Returns true if the map maps one or more keys to the specified value.
	 * @param {Object} value the value to test.
	 * @return {boolean}
	 */
	containsValue: function(value) {
		for(var key in this._map) if(this._map.hasOwnProperty(key)) {
			if(value == this._map[key]) {
				return true;
			}
		}
		return false;
	},
	
	/**
	 * Executes the given function once for each value in the
	 * map, passing the following arguments:
	 * {any} value the map value
	 * {int} index the index of value
	 * {int} size the total number of items in the collection.
	 * @param {function} fn the fucntion to execute for each item.  If this
	 * function returns 'break', the iterator will break.
	 * @param {Object} [scope] the scope in which to execute the function.
	 * Defaults to window.
	 */
	each: function(fn, scope) {
		var values = this.values(); // each safe for removal
		for(var i=0, n=values.length; i<n; i++) {
			if(fn.call(scope || window, values[i], i, n) === 'break') {
				break;
			}
		}
	},
	
	/**
	 * @return {string} the map's first key.  Returns null if the map is empty.
	 */
	firstKey: function() {
		return this._insertOrder[0] || null;
	},
	
	/**
	 * @return {any} the map's first value.  Returns null if the map is empty.
	 */
	firstValue: function() {
		return this.get(this.firstKey());
	},
	
	/**
	 * @return {string} the map's last key.  Returns null if the map is empty.
	 */
	lastKey: function() {
		return this._insertOrder[this._insertOrder.length -1] || null;
	},
	
	/**
	 * @return {any} the map's last value.  Returns null if the map is emtpy.
	 */
	lastValue: function() {
		return this.get(this.lastKey());
	}
	
});

