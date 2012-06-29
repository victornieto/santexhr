//===========================================================================
//
// Utf8Writer.js
//
//===========================================================================
oltk.namespace('oltk.io');


//===========================================================================
// UTF8 WRITER
//===========================================================================
/**
 * Writer for encoding UTF-8 to a stream.
 *
 * @class	oltk.io.Utf8Writer
 * @author	bandur
 */
oltk.io.Utf8Writer = function(stream)
{
	//=======================================================================
	// CONSTRUCTOR LOGIC
	//=======================================================================
	if (typeof(stream) == 'undefined' || stream === null)
	{
		throw "stream is null!";
	}

	if (typeof(stream.write) == 'undefined'
		|| typeof(stream.writeArray) == 'undefined')
	{
		throw "stream is not an OutputStream";
	}


	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * Internal OutputStream.
	 */
	var _os = stream;


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// CLOSE
	//-----------------------------------------------------------------------
	/**
	 * Closes this writer and the underlying stream.
	 */
	this.close = function()
	{
		_os.close();
		_os = null;
	};

	//-----------------------------------------------------------------------
	// FLUSH
	//-----------------------------------------------------------------------
	/**
	 * Flushes the stream.
	 */
	this.flush = function()
	{
		_os.flush();
	};

	//-----------------------------------------------------------------------
	// WRITE
	//-----------------------------------------------------------------------
	/**
	 * Writes a single character to the stream.
	 *
	 * @param	{char} chr - a single character
	 */
	this.write = function(chr)
	{
		var c = chr.charAt(0);

		_encode(_os, c);
	};

	//-----------------------------------------------------------------------
	// WRITE ARRAY
	//-----------------------------------------------------------------------
	/**
	 * Writes the complete contents of the given character array.
	 *
	 * @param	{array} chars - an Array of characters
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
	 * @param	{array} chars - an Array of characters
	 * @param	{int} offset - the starting offset in the array
	 * @param	{int} length - the number of characters to write
	 */
	this.writeSubarray = function(chars, offset, length)
	{
		for (var i = 0; i < length; i++)
		{
			var c = chars[offset + i];
			_encode(_os, c);
		}
	};

	//-----------------------------------------------------------------------
	// WRITE STRING
	//-----------------------------------------------------------------------
	/**
	 * Writes the given string to the stream.
	 *
	 * @param	{string} str - a String
	 */
	this.writeString = function(str)
	{
		this.writeSubstring(str, 0, str.length);
	};

	//-----------------------------------------------------------------------
	// WRITE SUBSTRING
	//-----------------------------------------------------------------------
	/**
	 * Writes a portion of the given string to the stream.
	 *
	 * @param	{string} str - a string
	 * @param	{int} offset - the starting index in the string
	 * @param	{int} length - the number of characters to write
	 */
	this.writeSubstring = function(str, offset, length)
	{
		for (var i = 0; i < length; i++)
		{
			var c = str.charAt(offset + i);
			_encode(_os, c);
		}
	};


	//=======================================================================
	// PRIVATE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// ENCODE
	//-----------------------------------------------------------------------
	/**
	 * Encodes a unicode character to the given stream.
	 *
	 * The character provided is translated into one or more UTF-8 bytes, then
	 * written to the stream.
	 *
	 * @param	{object} stream - an OutputStream
	 * @param	{char} chr - a unicode character.
	 */
	var _encode = function(stream, chr)
	{
		var code = chr.charCodeAt(0);

		var b1 = 0;
		var b2 = 0;
		var b3 = 0;

		if (code <= 0x7f)
		{
			stream.write(code);
		}
		else if (code > 0x7f && code <= 0x7ff)
		{
			b1 = (0xc0 | (0x1f & (code >> 6)));
			b2 = (0x80 | (0x3f & code));

			stream.write(b1);
			stream.write(b2);
		}
		else if (code > 0x7ff && code <= 0xffff)
		{
			b1 = (0xe0 | (0x0f & (code >> 12)));
			b2 = (0x80 | (0x3f & (code >> 6)));
			b3 = (0x80 | (0x3f & code));

			stream.write(b1);
			stream.write(b2);
			stream.write(b3);
		}
	};
};
