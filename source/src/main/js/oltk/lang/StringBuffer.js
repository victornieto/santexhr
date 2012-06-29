//===========================================================================
//
// StringBuffer.js
//
//===========================================================================
oltk.namespace('oltk.lang');


//===========================================================================
// STRING BUFFER
//===========================================================================
/**
 * A javascript implementation of a StringBuffer
 *
 * @author	bandur
 */
oltk.lang.StringBuffer = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * The internal buffer of string parts.
	 */
	var _buffer = [];


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// APPEND
	//-----------------------------------------------------------------------
	/**
	 * Appends the given string to this buffer.
	 *
	 * @return	This buffer, for chaining.
	 */
	this.append = function(str)
	{
		_buffer.push(str);

		return that;
	};

	//-----------------------------------------------------------------------
	// CHAR AT
	//-----------------------------------------------------------------------
	/**
	 * Returns the character located at the given index.
	 */
	this.charAt = function(index)
	{
		var value = _buffer.join('');

		_buffer = [];
		_buffer.push(value);

		return value.charAt(index);
	};

	//-----------------------------------------------------------------------
	// REMOVE
	//-----------------------------------------------------------------------
	/**
	 * Removes the indicated substring from this buffer.
	 *
	 * @param	start - the starting index, inclusive
	 * @param	end - the ending index, exclusive
	 *
	 * @return	This buffer, for chaining.
	 */
	this.remove = function(start, end)
	{
		var value = _buffer.join('');

		_buffer = [];
		_buffer.push(value.substring(0, start));
		_buffer.push(value.substring(end));

		return that;
	};

	//-----------------------------------------------------------------------
	// DELETE CHAR AT
	//-----------------------------------------------------------------------
	/**
	 * Removes the character at the indicated index from this buffer.
	 *
	 * @return	This buffer, for chaining.
	 */
	this.deleteCharAt = function(index)
	{
		var value = _buffer.join('');

		_buffer = [];
		_buffer.push(value.substring(0, index));
		_buffer.push(value.substring(index + 1));

		return that;
	};

	//-----------------------------------------------------------------------
	// INSERT
	//-----------------------------------------------------------------------
	/**
	 * Inserts the given string at the given location in this buffer.
	 *
	 * @return	This buffer, for chaining.
	 */
	this.insert = function(offset, str)
	{
		var value = _buffer.join('');

		_buffer = [];
		_buffer.push(value.substring(0, offset));
		_buffer.push(str);
		_buffer.push(value.substring(offset));

		return that;
	};

	//-----------------------------------------------------------------------
	// LENGTH
	//-----------------------------------------------------------------------
	/**
	 * Returns the number of characters in this buffer.
	 */
	this.length = function()
	{
		var value = _buffer.join('');

		_buffer = [];
		_buffer.push(value);

		return value.length;
	};

	//-----------------------------------------------------------------------
	// REVERSE
	//-----------------------------------------------------------------------
	/**
	 * Reverses the sequence of characters in this buffer.
	 *
	 * @return	This buffer, for chaining.
	 */
	this.reverse = function()
	{
		var value = _buffer.join('');

		_buffer = [];
		for (var i = value.length - 1; i >= 0; i--)
		{
			_buffer.push(value.charAt(i));
		}

		return that;
	};

	//-----------------------------------------------------------------------
	// SET CHAR AT
	//-----------------------------------------------------------------------
	/**
	 * Sets the character at the given index.
	 */
	this.setCharAt = function(index, chr)
	{
		var value = _buffer.join('');

		_buffer = [];
		_buffer.push(value.substring(0, index));
		_buffer.push(chr);
		_buffer.push(value.substring(index + 1));
	};

	//-----------------------------------------------------------------------
	// SET LENGTH
	//-----------------------------------------------------------------------
	/**
	 * Truncates this buffer to the given length.
	 */
	this.setLength = function(len)
	{
		var value = _buffer.join('');

		_buffer = [];
		_buffer.push(value.substring(0, len));
	};

	//-----------------------------------------------------------------------
	// TO STRING
	//-----------------------------------------------------------------------
	/**
	 * Returns the current string representation of this buffer.
	 */
	this.toString = function()
	{
		var value = _buffer.join('');

		return value;
	};
};
