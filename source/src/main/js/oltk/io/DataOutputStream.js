//===========================================================================
//
// DataOutputStream.js
//
//===========================================================================
oltk.namespace('oltk.io');

oltk.include('oltk.io.ByteArrayOutputStream.js');

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
	if (typeof(oltk.io.ByteArrayOutputStream) == 'undefined')
	{
		throw "";
	}
}
catch (e)
{
	throw "oltk.io.ByteArrayOutputStream required!";
}


//===========================================================================
// DATA OUTPUT STREAM
//===========================================================================
/**
 * A javascript implementation of a DataOutputStream.
 *
 * Note, there are issues in javascript with integers greater than 4 bytes 
 * long, therefore writeLong has been omitted.
 * 
 * @author	bandur
 */
oltk.io.DataOutputStream = function(stream)
{
	//=======================================================================
	// CONSTRUCTOR LOGIC
	//=======================================================================
	if (typeof(stream) == 'undefined' || stream === null)
	{
		throw "stream is null!";
	}

	if (typeof(stream.write) == 'undefined' 
		|| typeof(stream.writeArray) == 'undefined'
		|| typeof(stream.writeSubarray) == 'undefined')
	{
		throw "stream is not an OutputStream!";
	}


	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * Internal OutputStream
	 */
	var _os = stream;


	//=======================================================================
	// INSTANCE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// CLOSE
	//-----------------------------------------------------------------------
	/**
	 * Closes this stream and releases the underlying stream.
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
	 * Flushes this stream.
	 */
	this.flush = function()
	{
		_os.flush();
	};

	//-----------------------------------------------------------------------
	// WRITE BOOLEAN
	//-----------------------------------------------------------------------
	/**
	 * Writes a boolean to the underlying stream.
	 *
	 * Writes a single byte, 0x0 for false, 0x1 for true.
	 */
	this.writeBoolean = function(b)
	{
		if (b)
		{
			_os.write(0x1);
		}
		else
		{
			_os.write(0x0);
		}
	};

	//-----------------------------------------------------------------------
	// WRITE BYTE
	//-----------------------------------------------------------------------
	/**
	 * Writes a byte to the underlying stream.
	 */
	this.writeByte = function(b)
	{
		_os.write(b);
	};

	//-----------------------------------------------------------------------
	// WRITE CHAR
	//-----------------------------------------------------------------------
	/**
	 * Writes a single character to the underlying stream.
	 *
	 * Writes the two-byte representation of the character, highest byte 
	 * first.
	 */
	this.writeChar = function(c)
	{
		var code = c.charCodeAt(0);

		this.writeShort(code);
	};

	//-----------------------------------------------------------------------
	// WRITE INT
	//-----------------------------------------------------------------------
	/**
	 * Writes an integer to the underlying stream.
	 *
	 * Writes four bytes, high byte first.
	 */
	this.writeInt = function(i)
	{
		for (var x = 3; x >= 0; x--)
		{
			var b = ((i >> (x * 8)) & 0xff);
			_os.write(b);
		}
	};

	//-----------------------------------------------------------------------
	// WRITE SHORT
	//-----------------------------------------------------------------------
	/**
	 * Writes a short to the underlying stream.
	 *
	 * Writes two bytes, high byte first.
	 */
	this.writeShort = function(s)
	{
		for (var x = 1; x >= 0; x--)
		{
			var b = ((s >> (x * 8)) & 0xff);
			_os.write(b);
		}
	};

	//-----------------------------------------------------------------------
	// WRITE UTF
	//-----------------------------------------------------------------------
	/**
	 * Writes a string to the underlying stream using UTF-8 encoding.
	 *
	 * Writes two bytes, indicating the number of bytes in the string,
	 * followed by the encoded string.
	 */
	this.writeUtf = function(str)
	{
		//Convert the string to a UTF8-encoded byte array.
		var baos = new oltk.io.ByteArrayOutputStream();
		for (var i = 0; i < str.length; i++)
		{
			oltk.io.Utf8Encoder.encode(baos, str.charAt(i));
		}
		var bytes = baos.toByteArray();

		//Write the length.
		this.writeShort(bytes.length);

		//Write the bytes.
		for (var w = 0; w < bytes.length; w++)
		{
			this.writeByte(bytes[w]);
		}
	};
};
