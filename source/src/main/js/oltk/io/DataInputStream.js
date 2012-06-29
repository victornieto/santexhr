//===========================================================================
//
// DataInputStream.js
//
//===========================================================================
oltk.namespace('oltk.io');

oltk.include('oltk.io.ByteArrayInputStream.js');

//===========================================================================
// DEPENDENCIES
//===========================================================================
try
{
	if (typeof(oltk.io.Utf8Encoder) == 'undefined')
	{
		throw "";
	}
}
catch (e)
{
	throw "oltk.io.Utf8Encoder required!";
}

try
{
	if (typeof(oltk.io.ByteArrayInputStream) == 'undefined')
	{
		throw "";
	}
}
catch (e)
{
	throw "oltk.io.ByteArrayInputStream required!";
}

//===========================================================================
// DATA INPUT STREAM
//===========================================================================
/**
 * A javascript implementation of a DataInputStream.
 *
 * Note, there are issues in javascript with integers greater than 4 bytes
 * long, therefore, readLong has been omitted.
 *
 * @author	bandur
 */
oltk.io.DataInputStream = function(stream)
{
	//=======================================================================
	// CONSTRUCTOR LOGIC
	//=======================================================================
	if (typeof(stream) == 'undefined' || stream === null)
	{
		throw "stream is null";
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
	 * Internal InputStream.
	 */
	var _is = stream;


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// AVAILABLE
	//-----------------------------------------------------------------------
	/**
	 * Returns the number of bytes that can be read without blocking.
	 */
	this.available = function()
	{
		return _is.available();
	};

	//-----------------------------------------------------------------------
	// CLOSE
	//-----------------------------------------------------------------------
	/**
	 * Closes this stream and releases the underlying stream.
	 */
	this.close = function()
	{
		_is.close();
		_is = null;
	};

	//-----------------------------------------------------------------------
	// READ BOOLEAN
	//-----------------------------------------------------------------------
	/**
	 * Reads a boolean from the underlying stream.
	 *
	 * Expects a single byte, zero for false, non-zero for true.
	 */
	this.readBoolean = function()
	{
		var b = _is.read();
		if (-1 == b)
		{
			throw "EOF!";
		}

		if (b === 0x0)
		{
			return false;
		}
		
		return true;
	};

	//-----------------------------------------------------------------------
	// READ BYTE
	//-----------------------------------------------------------------------
	/**
	 * Reads a single byte from the underlying stream.
	 */
	this.readByte = function()
	{
		var b = _is.read();
		if (-1 == b)
		{
			throw "EOF!";
		}

		return b;
	};

	//-----------------------------------------------------------------------
	// READ CHAR
	//-----------------------------------------------------------------------
	/**
	 * Reads a single character from the underlying stream.
	 *
	 * Expects two bytes, high byte first.
	 */
	this.readChar = function()
	{
		var code = this.readShort();

		return String.fromCharCode(code);
	};

	//-----------------------------------------------------------------------
	// READ INT
	//-----------------------------------------------------------------------
	/**
	 * Reads an integer from the underlying stream.
	 *
	 * Expects four bytes, high byte first.
	 */
	this.readInt = function()
	{
		var i = 0;

		for (var x = 0; x < 4; x++)
		{
			var b = _is.read();
			if (-1 == b)
			{
				throw "EOF!";
			}

			i = ((i << 8) | (b & 0xff));
		}

		return i;
	};

	//-----------------------------------------------------------------------
	// READ SHORT
	//-----------------------------------------------------------------------
	/**
	 * Reads a short from the underlying stream.
	 *
	 * Expects two bytes, high byte first.
	 */
	this.readShort = function()
	{
		var s = 0;

		for (var x = 0; x < 2; x++)
		{
			var b = _is.read();
			if (-1 == b)
			{
				throw "EOF!";
			}

			s = ((s << 8) | (b & 0xff));
		}

		return s;
	};

	//-----------------------------------------------------------------------
	// READ UTF
	//-----------------------------------------------------------------------
	/**
	 * Reads a string from the underlying stream.
	 *
	 * Expects two bytes, indicating the number of bytes to follow, then the
	 * string bytes, UTF8-encoded.
	 */
	this.readUtf = function()
	{
		//Read the length.
		var length = this.readShort();

		//Read the bytes.
		var bytes = [];
		for (var i = 0; i < length; i++)
		{
			var b = this.readByte();
			if (-1 == b)
			{
				throw "EOF!";
			}

			bytes.push(b);
		}

		//Convert the bytes to a string.
		var buffer = [];

		var baos = new oltk.io.ByteArrayInputStream(bytes);
		while (true)
		{
			var c = oltk.io.Utf8Encoder.decode(baos);
			if ('' === c)
			{
				break;
			}

			buffer.push(c);
		}

		return buffer.join('');
	};
};

