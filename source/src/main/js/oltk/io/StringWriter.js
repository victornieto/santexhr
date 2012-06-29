//===========================================================================
//
// StringWriter.js
//
//===========================================================================
oltk.namespace('oltk.io');

oltk.include('oltk/lang/StringBuffer.js');


//===========================================================================
// STRING WRITER
//===========================================================================
/**
 * A javascript implementation of a StringWriter.
 *
 * @class	oltk.io.StringWriter
 * @author	bandur
 */
oltk.io.StringWriter = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * Internal StringBuffer.
	 */
	var _buffer = new oltk.lang.StringBuffer();


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// CLOSE
	//-----------------------------------------------------------------------
	/**
	 * Closes this writer.
	 */
	this.close = function()
	{
		//noop
	};

	//-----------------------------------------------------------------------
	// FLUSH
	//-----------------------------------------------------------------------
	/**
	 * Flushes the stream.
	 */
	this.flush = function()
	{
		//noop
	};

	//-----------------------------------------------------------------------
	// GET BUFFER
	//-----------------------------------------------------------------------
	/**
	 * Returns the underlying StringBuffer.
	 *
	 * @return	{object} The StringBuffer.
	 */
	this.getBuffer = function()
	{
		return _buffer;
	};

	//-----------------------------------------------------------------------
	// TO STRING
	//-----------------------------------------------------------------------
	/**
	 * Returns the current contents of the buffer as a string.
	 *
	 * @return	{string} The contents.
	 */
	this.toString = function()
	{
		return _buffer.toString();
	};

	//-----------------------------------------------------------------------
	// WRITE
	//-----------------------------------------------------------------------
	/**
	 * Writes a single character to the underlying buffer.
	 *
	 * @param	{char} chr - The character to write.
	 */
	this.write = function(chr)
	{
		_buffer.append(chr.charAt(0));
	};

	//-----------------------------------------------------------------------
	// WRITE ARRAY
	//-----------------------------------------------------------------------
	/**
	 * Writes the contents of the given character array.
	 *
	 * @param	{array} chars - an array of characters.
	 */
	this.writeArray = function(chars)
	{
		this.writeSubarray(chars, 0, chars.length);
	};

	//-----------------------------------------------------------------------
	// WRITE SUBARRAY
	//-----------------------------------------------------------------------
	/**
	 * Writes a portion of the given character array.
	 *
	 * @param	{array} chars - the array of characters
	 * @param	{int} offset - the starting offset in the array
	 * @param	{int} length - the number of characters to write
	 */
	this.writeSubarray = function(chars, offset, length)
	{
		for (var i = 0; i < length; i++)
		{
			var c = chars[offset + i];
			_buffer.append(c);
		}
	};

	//-----------------------------------------------------------------------
	// WRITE STRING
	//-----------------------------------------------------------------------
	/**
	 * Writes a string to the underlying buffer.
	 *
	 * @param	{string} str - The string to write.
	 */
	this.writeString = function(str)
	{
		_buffer.append(str);
	};

	//-----------------------------------------------------------------------
	// WRITE SUBSTRING
	//-----------------------------------------------------------------------
	/**
	 * Writes a portion of the given string.
	 *
	 * @param	{string} str - the string
	 * @param	{int} offset - the starting index in the string
	 * @param	{int} length - the number of characters to write
	 */
	this.writeSubstring = function(str, offset, length)
	{
		var s = str.substring(offset, offset + length);
		_buffer.append(s);
	};
};
