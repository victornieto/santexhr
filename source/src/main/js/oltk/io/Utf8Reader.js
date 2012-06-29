//===========================================================================
//
// Utf8Reader.js
//
//===========================================================================
oltk.namespace('oltk.io');


//===========================================================================
// UTF8 READER
//===========================================================================
/**
 * Reader for decoding UTF-8 streams.
 *
 * @author	bandur
 */
oltk.io.Utf8Reader = function(stream)
{
	//=======================================================================
	// CONSTRUCTOR LOGIC
	//=======================================================================
	if (typeof(stream) == 'undefined' || stream === null)
	{
		throw 'stream is null!';
	}

	if (typeof(stream.read) == 'undefined'
		|| typeof(stream.readArray) == 'undefined')
	{
		throw "stream is not an InputStream";
	}


	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * Internal input stream.
	 */
	var _is = stream;


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// CLOSE
	//-----------------------------------------------------------------------
	/**
	 * Closes this reader and releases the underlying stream.
	 */
	this.close = function()
	{
		_is.close();
		_is = null;
	};

	//-----------------------------------------------------------------------
	// READ
	//-----------------------------------------------------------------------
	/**
	 * Reads a single character from the stream.
	 *
	 * @return	{char} The character, or -1, if EOF.
	 */
	this.read = function()
	{
		var c = _decode(_is);
		if ('' === c)
		{
			return -1;
		}

		return c;
	};

	//-----------------------------------------------------------------------
	// READ ARRAY
	//-----------------------------------------------------------------------
	/**
	 * Reads an array of characters from the stream.
	 *
	 * @param	{array} arr - an empty array
	 * @return	{int} The number of characters read, or -1, if EOF.
	 */
	this.readArray = function(arr)
	{
		var count = 0;
		while (true)
		{
			var c = _decode(_is);
			if ('' === c)
			{
				if (count === 0)
				{
					return -1;
				}
				else
				{
					return count;
				}
			}
			
			arr.push(c);
			count++;
		}
	};

	//-----------------------------------------------------------------------
	// READ SUBARRAY
	//-----------------------------------------------------------------------
	/**
	 * Reads an array of characters into a portion of an array.
	 *
	 * @param	{array} arr - an Array
	 * @param	{int} offset - the starting index in the array
	 * @param	{int} length - the number of characters to read
	 * @return	{int} The number of characters read, or -1, if EOF
	 */
	this.readSubarray = function(arr, offset, length)
	{
		var count = 0;
		for (count = 0; count < length; count++)
		{
			var c = _decode(_is);
			if ('' === c)
			{
				if (count === 0)
				{
					return -1;
				}
				else
				{
					return count;
				}
			}

			arr[offset + count] = c;
		}

		return count;
	};

	//-----------------------------------------------------------------------
	// SKIP
	//-----------------------------------------------------------------------
	/**
	 * Skips the indicated number of character in the stream.
	 *
	 * @param	{int} n - the number of characters to skip
	 * @return	{int} The actual number of characters skipped.
	 */
	this.skip = function(n)
	{

		var count = 0;
		for (count = 0; count < n; count++)
		{
			var c = _decode(_is);
			if ('' === c)
			{
				return count;
			}
		}

		return count;
	};
	

	//=======================================================================
	// PRIVATE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// DECODE
	//-----------------------------------------------------------------------
	/**
	 * Decodes the next unicode character from the given stream.
	 *
	 * One or more UTF-8 bytes are read from the stream and translated to a
	 * unicode character.
	 *
	 * @param	{object} stream - an InputStream
	 *
	 * @return	{char} A single unicode character, or the empty string, if EOF
	 */
	var _decode = function(stream)
	{
		var b1 = stream.read();

		var code = 0x0;
		if (-1 == b1)
		{
			//EOF.
			return '';
		}
		else if ((b1 & 0x80) === 0x0)
		{
			//Single-byte code.
			code = b1;
		}
		else if ((b1 & 0xe0) == 0xc0)
		{
			//Double-byte code.
			var b2 = stream.read();
			if (-1 == b2)
			{
				return '';
			}

			code = (((b1 & 0x1f) << 6) | (b2 & 0x3f));
		}
		else if ((b1 & 0xf0) == 0xe0)
		{
			//Triple-byte code.
			var b2 = stream.read();
			var b3 = stream.read();
			if (-1 == b2 || -1 == b3)
			{
				return '';
			}

			code = (((b1 & 0x0f) << 12) | ((b2 & 0x3f) << 6) | (b3 & 0x3f));
		}

		return String.fromCharCode(code);
	};
};
