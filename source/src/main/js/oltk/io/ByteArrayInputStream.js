//===========================================================================
//
// ByteArrayInputStream.js
//
//===========================================================================
oltk.namespace('oltk.io');


//===========================================================================
// BYTE ARRAY INPUT STREAM
//===========================================================================
/**
 * A javascript implementation of a ByteArrayInputStream.
 *
 * @class	oltk.io.ByteArrayInputStream
 * @author	bandur
 */
oltk.io.ByteArrayInputStream = function(bytes)
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * The internal buffer.
	 */
	var _buffer = bytes;

	/**
	 * The current position in the buffer.
	 */
	var _index = 0;


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// AVAILABLE
	//-----------------------------------------------------------------------
	/**
	 * Returns the number of bytes availble for reading.
	 *
	 * @return	{int} The number of bytes.
	 */
	this.available = function()
	{
		return _buffer.length - _index;
	};

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
	// READ
	//-----------------------------------------------------------------------
	/**
	 * Reads a single byte from the stream.
	 *
	 * @return	{int} The byte, or -1, if EOF
	 */
	this.read = function()
	{
		if (_index >= _buffer.length)
		{
			return -1;
		}

		return _buffer[_index++];
	};

	//-----------------------------------------------------------------------
	// READ ARRAY
	//-----------------------------------------------------------------------
	/**
	 * Reads an array of bytes from the stream.
	 *
	 * @param	{Array} arr - the array in which to put the bytes
	 * @param	{int} offset - the index in the array to start copying
	 * @param	{int} length - the number of bytes to read
	 * @return	{int} The total number of bytes read, or -1, if EOF
	 */
	this.readArray = function(arr, offset, length)
	{
		if (_index >= _buffer.length)
		{
			return -1;
		}

		var count = Math.min(length, _buffer.length - _index);
		for (var i = 0; i < count; i++)
		{
			var b = _buffer[_index++];
			arr[offset + i] = b;
		}

		return count;
	};

	//-----------------------------------------------------------------------
	// SKIP
	//-----------------------------------------------------------------------
	/**
	 * Skips a given number of bytes from the stream.
	 *
	 * @param	{int} count - the number of bytes to skip
	 * @return	{int} The actual number of bytes skipped
	 */
	this.skip = function(count)
	{
		var n = Math.min(count, _buffer.length - _index);
		_index += n;

		return n;
	};
};
