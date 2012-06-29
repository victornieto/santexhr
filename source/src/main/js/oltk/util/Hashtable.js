//===========================================================================
//
// Hashtable.js
//
//===========================================================================
oltk.namespace('oltk.util');


//===========================================================================
// HASHTABLE
//===========================================================================
/**
 * A javascript implementation of a Hashtable.
 *
 * @class	oltk.util.Hashtable
 * @author	bandur
 */
oltk.util.Hashtable = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * The internal table of mappings.
	 */
	var _table = [];


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// CLEAR
	//-----------------------------------------------------------------------
	/**
	 * Clears this hashtable.
	 */
	this.clear = function()
	{
		_table = [];
	};

	//-----------------------------------------------------------------------
	// CONTAINS KEY
	//-----------------------------------------------------------------------
	/**
	 * Tests if the specified key is used in this hashtable.
	 *
	 * @return	{boolean} True, if the key exists; false, otherwise.
	 */
	this.containsKey = function(key)
	{
		return (key in _table);
	};

	//-----------------------------------------------------------------------
	// CONTAINS VALUE
	//-----------------------------------------------------------------------
	/**
	 * Tests if some key maps to the specified value in this hashtable.
	 *
	 * @return	{boolean} True, if the given value exists; false, otherwise.
	 */
	this.containsValue = function(value)
	{
		var exists = false;
		for (var k in _table) if (_table.hasOwnProperty(k))
		{
			var v = _table[k];
			if (v == value)
			{
				exists = true;
				break;
			}
		}

		return exists;
	};

	//-----------------------------------------------------------------------
	// GET
	//-----------------------------------------------------------------------
	/**
	 * Retrieves the value to which the specified key is mapped in this
	 * hashtable.
	 *
	 * @return	{any} The value, or null, if the key does not exist.
	 */
	this.get = function(key)
	{
		if (!(key in _table))
		{
			return null;
		}

		return _table[key];
	};

	//-----------------------------------------------------------------------
	// IS EMPTY
	//-----------------------------------------------------------------------
	/**
	 * Tests if this hashtable is empty.
	 *
	 * @return	{boolean} True, if empty; false, otherwise.
	 */
	this.isEmpty = function()
	{
		return (size() === 0);
	};

	//-----------------------------------------------------------------------
	// KEYS
	//-----------------------------------------------------------------------
	/**
	 * Returns an array of the keys in this hashtable.
	 *
	 * @return	{Array} An array of keys.
	 */
	this.keys = function()
	{
		var keyArray = [];
		for (var key in _table) if (_table.hasOwnProperty(key))
		{
			keyArray.push(key);
		}

		return keyArray;
	};

	//-----------------------------------------------------------------------
	// PUT
	//-----------------------------------------------------------------------
	/**
	 * Maps the specified key to the specified value in this hashtable.
	 *
	 * @return	{any} The previous value mapped to the given key, or null, 
	 * 			if no previous value.
	 */
	this.put = function(key, value)
	{
		var prev = null;
		if (key in _table)
		{
			prev = _table[key];
		}

		_table[key] = value;

		return prev;
	};
	
	//-----------------------------------------------------------------------
	// REMOVE
	//-----------------------------------------------------------------------
	/**
	 * Removes the specified key and its corresponding value from this 
	 * hashtable.
	 *
	 * @return	{any} The value previously mapped to the key.
	 */
	this.remove = function(key)
	{
		var value = _table[key];

		delete _table[key];

		return value;
	};

	//-----------------------------------------------------------------------
	// SIZE
	//-----------------------------------------------------------------------
	/**
	 * Returns the number of entries in this hashtable.
	 *
	 * @return	{int} The number of entries.
	 */
	this.size = function()
	{
		var count = 0;
		for (var key in _table) if (_table.hasOwnProperty(key))
		{
			count++;
		}

		return count;
	};

	//-----------------------------------------------------------------------
	// TO STRING
	//-----------------------------------------------------------------------
	/**
	 * Returns a string representation of this hashtable.
	 *
	 * @return	{String} The string.
	 */
	this.toString = function()
	{
		var result = [];
		for (var key in _table) if (_table.hasOwnProperty(key))
		{
			var value = _table[key];

			if (result.length > 0)
			{
				result.push(',');
			}

			result.push('{');
			result.push(key);
			result.push('=');
			result.push(value);
			result.push('}');
		}

		return result.join('');
	};

	//-----------------------------------------------------------------------
	// VALUES
	//-----------------------------------------------------------------------
	/**
	 * Returns a list of values contained in this hashtable.
	 *
	 * @return	{Array} The list.
	 */
	this.values = function()
	{
		var result = [];
		for (var key in _table) if (_table.hasOwnProperty(key))
		{
			var value = _table[key];

			result.push(value);
		}

		return result;
	};
};
