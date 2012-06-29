//===========================================================================
//
// ByteArrayOutputStream.js
//
//===========================================================================
oltk.namespace('oltk.io');


//===========================================================================
// BYTE ARRAY OUTPUT STREAM
//===========================================================================
/**
 * A javascript implementation of a ByteArrayOutputStream.
 *
 * @class	oltk.io.ByteArrayOutputStream
 * @author	bandur
 */
oltk.io.ByteArrayOutputStream = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * The internal buffer.
	 */
	var _buffer = [];


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// CLOSE
	//-----------------------------------------------------------------------
	/**
	 * Closes this stream and releases any resources associated with it.
	 */
	this.close = function()
	{
		_buffer = null;
	};

	//-----------------------------------------------------------------------
	// FLUSH
	//-----------------------------------------------------------------------
	/**
	 * Flushes any buffered output.
	 */
	this.flush = function()
	{
		//noop
	};

	//-----------------------------------------------------------------------
	// RESET
	//-----------------------------------------------------------------------
	/**
	 * Resets the buffer to empty.
	 */
	this.reset = function()
	{
		_buffer = [];
	};

	//-----------------------------------------------------------------------
	// SIZE
	//-----------------------------------------------------------------------
	/**
	 * Returns the current size of the buffer.
	 *
	 * @return	{int} The size.
	 */
	this.size = function()
	{
		return _buffer.length;
	};

	//-----------------------------------------------------------------------
	// TO BYTE ARRAY
	//-----------------------------------------------------------------------
	/**
	 * Returns a copy of the contents of the buffer.
	 *
	 * @return	{array} The contents.
	 */
	this.toByteArray = function()
	{
		var result = [];
		for (var i = 0; i < _buffer.length; i++)
		{
			result[i] = _buffer[i];
		}

		return result;
	};

	//-----------------------------------------------------------------------
	// TO STRING
	//-----------------------------------------------------------------------
	/**
	 * Converts the buffer's contents to a string, decoding bytes using
	 * ASCII encoding.  Note, the preferred method of converting bytes to
	 * a string is to use a Reader.
	 *
	 * @return	{string} The contents.
	 */
	this.toString = function()
	{
		var result = [];
		for (var i = 0; i < _buffer.length; i++)
		{
			var c = String.fromCharCode(_buffer[i]);
			result.push(c);
		}

		return result.join('');
	};

	//-----------------------------------------------------------------------
	// WRITE
	//-----------------------------------------------------------------------
	/**
	 * Writes a single byte to this stream.
	 *
	 * @param	{int} The byte to write.
	 */
	this.write = function(b)
	{
		_buffer.push(b);
	};

	//-----------------------------------------------------------------------
	// WRITE ARRAY
	//-----------------------------------------------------------------------
	/**
	 * Writes the complete contents of the given array.
	 *
	 * @param	{array} bytes - a byte array
	 */
	this.writeArray = function(bytes)
	{
		this.writeSubarray(bytes, 0, bytes.length);
	};

	//-----------------------------------------------------------------------
	// WRITE SUBARRAY
	//-----------------------------------------------------------------------
	/**
	 * Writes the specified bytes from the given byte array.
	 *
	 * @param	{array} bytes - the byte array
	 * @param	{int} offset - the starting offset in the byte array
	 * @param	{int} length - the number of bytes to write
	 */
	this.writeSubarray = function(bytes, offset, length)
	{
		for (var i = 0; i < length; i++)
		{
			_buffer.push(bytes[offset + i]);
		}
	};
};
