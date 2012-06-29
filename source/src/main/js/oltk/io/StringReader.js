//===========================================================================
//
// StringReader.js
//
//===========================================================================
oltk.namespace('oltk.io');


//===========================================================================
// STRING READER
//===========================================================================
/**
 * A javascript implementation of a StringReader.
 *
 * @class	oltk.io.StringReader
 * @author	bandur
 */
oltk.io.StringReader = function(str)
{
	//=======================================================================
	// CONSTRUCTOR LOGIC
	//=======================================================================
	if (typeof(str) != 'string')
	{
		throw "str is not a string!";
	}


	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * Internal string buffer.
	 */
	var _buffer = str;

	/**
	 * The current index in the buffer.
	 */
	var _index = 0;


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// CLOSE
	//-----------------------------------------------------------------------
	/**
	 * Closes this reader and releases all resources.
	 */
	this.close = function()
	{
		_buffer = null;
	};

	//-----------------------------------------------------------------------
	// READ
	//-----------------------------------------------------------------------
	/**
	 * Reads a single character from the stream.
	 *
	 * @return	{char} The character, or -1, if EOF
	 */
	this.read = function()
	{
		if (_index >= _buffer.length)
		{
			return -1;
		}

		return _buffer.charAt(_index++);
	};

	//-----------------------------------------------------------------------
	// READ ARRAY
	//-----------------------------------------------------------------------
	/**
	 * Reads an array of characters from the stream.
	 *
	 * @param	{array} arr - the array to fill
	 * @return	{int} The number of characters read, or -1, if EOF
	 */
	this.readArray = function(arr)
	{
		if (_index >= _buffer.length)
		{
			return -1;
		}

		var count = _buffer.length - _index;
		for (var i = 0; i < count; i++)
		{
			var c = _buffer.charAt(_index++);
			arr.push(c);
		}

		return count;
	};

	//-----------------------------------------------------------------------
	// READ SUBARRAY
	//-----------------------------------------------------------------------
	/**
	 * Reads an array of characters from the stream into the indicated 
	 * portion of an array.
	 *
	 * @param	{array} arr - the array to fill
	 * @param	{int} offset - the starting index in the array
	 * @param	{int} length - the number of characters to read
	 * @return	{int} The number of characters read, or -1, if EOF
	 */
	this.readSubarray = function(arr, offset, length)
	{
		if (_index >= _buffer.length)
		{
			return -1;
		}

		var count = Math.min(_buffer.length - _index, length);
		for (var i = 0; i < count; i++)
		{
			var c = _buffer.charAt(_index++);
			arr[offset + i] = c;
		}

		return count;
	};

	//-----------------------------------------------------------------------
	// SKIP
	//-----------------------------------------------------------------------
	/**
	 * Skips the indicated number of characters in the stream.
	 *
	 * @param	{int} n - the number of characters to skip
	 * @return	{int} The number of characters actually skipped
	 */
	this.skip = function(n)
	{
		var count = Math.min(_buffer.length - _index, n);
		_index += count;

		return count;
	};
};
