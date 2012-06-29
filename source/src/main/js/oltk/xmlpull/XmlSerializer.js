//===========================================================================
//
// XmlSerializer.js
//
//===========================================================================
oltk.namespace('oltk.xmlpull');


//===========================================================================
// XML SERIALIZER
//===========================================================================
/**
 * Utility for serializing XML.
 * 
 * This API is designed to be similar to the org.xmlpull.v1.XmlSerializer;
 * however, without streams, this class simply maintains the result in an
 * internal buffer.  This buffer can be retrieved with getOutput(), and 
 * reset with reset().
 *
 * A few things aren't completely implemented, or aren't as correct as
 * they should be.
 *
 * @author	bandur
 */
oltk.xmlpull.XmlSerializer = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================

	var that = this;

	/**
	 * The XML output buffer.
	 */
	var _output = [];

	/**
	 * Whether currently in an open start tag.
	 */
	var _openTag = false;


	//=======================================================================
	// PUBLIC METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// ATTRIBUTE
	//-----------------------------------------------------------------------
	/**
	 * @return	 A reference to this XmlSerializer, for chaining.
	 */
	this.attribute = function(ns, name, value)
	{
		if (!_openTag)
		{
			return;
		}

		_output.push(' ');
		_output.push((null === ns ? name : ns + ':' + name));
		_output.push('=');
		_output.push('"' );
		_output.push(_escapeXml(value));
		_output.push('"');

		return this;
	};

	//-----------------------------------------------------------------------
	// CDSECT
	//-----------------------------------------------------------------------
	this.cdsect = function(text)
	{
		_nodeTransition();

		_output.push('<![CDATA[');
		_output.push(text);
		_output.push(']]>');
	};

	//-----------------------------------------------------------------------
	// COMMENT
	//-----------------------------------------------------------------------
	this.comment = function(text)
	{
		_nodeTransition();

		_output.push('<!--');
		_output.push(_escapeXml(text));
		_output.push('-->');
	};

	//-----------------------------------------------------------------------
	// DOCDECL
	//-----------------------------------------------------------------------
	this.docdecl = function(text)
	{
		_output.push(text);
	};

	//-----------------------------------------------------------------------
	// END DOCUMENT
	//-----------------------------------------------------------------------
	this.endDocument = function()
	{
		_nodeTransition();
	};

	//-----------------------------------------------------------------------
	// END TAG
	//-----------------------------------------------------------------------
	/**
	 * @return	A reference to this XmlSerializer, for chaining.
	 */
	this.endTag = function(ns, name)
	{
		if (_openTag)
		{
			//Empty element; use abbreviated form.
			_output.push('/>');
			_openTag = false;
		}
		else
		{
			_output.push('</');
			_output.push((null === ns ? name : ns + ':' + name));
			_output.push('>');
		}

		return this;
	};

	//-----------------------------------------------------------------------
	// FLUSH
	//-----------------------------------------------------------------------
	/**
	 * Ensures that the output is in a consistent state (e.g., closes any 
	 * open start tags) for retrieval.
	 *
	 * This might be used to retrieve output when endDocument cannot be used.
	 */
	this.flush = function()
	{
		_nodeTransition();
	};

	//-----------------------------------------------------------------------
	// GET OUTPUT
	//-----------------------------------------------------------------------
	/**
	 * Returns the current result.
	 */
	this.getOutput = function()
	{
		return _output.join('');
	};

	//-----------------------------------------------------------------------
	// PROCESSING INSTRUCTION
	//-----------------------------------------------------------------------
	this.processingInstruction = function(text)
	{
		_nodeTransition();

		_output.push('<?');
		_output.push(text);
		_output.push('?>');
	};

	//-----------------------------------------------------------------------
	// RESET
	//-----------------------------------------------------------------------
	this.reset = function()
	{
		_output.length = 0;
		_openTag = false;
	};

	//-----------------------------------------------------------------------
	// START DOCUMENT
	//-----------------------------------------------------------------------
	this.startDocument = function(encoding, standalone)
	{
		if (null !== encoding)
		{
			_output.push('<?xml version="1.0" encoding="' + encoding + '" ?>');
		}
		else
		{
			_output.push('<?xml version="1.0" ?>');
		}
	};

	//-----------------------------------------------------------------------
	// START TAG
	//-----------------------------------------------------------------------
	/**
	 * @return	A reference to this XmlSerializer, for chaining.
	 */
	this.startTag = function(ns, name)
	{
		_nodeTransition();

		_output.push('<');
		_output.push((null === ns ? name : ns + ':' + name));

		_openTag = true;

		return this;
	};

	//-----------------------------------------------------------------------
	// TEXT
	//-----------------------------------------------------------------------
	/**
	 * @return	A reference to this XmlSerializer, for chaining.
	 */
	this.text = function(text)
	{
		_nodeTransition();

		_output.push(_escapeXml(text));

		return this;
	};


	//=======================================================================
	// PRIVATE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// NODE TRANSITION
	//-----------------------------------------------------------------------
	/**
	 * Handles closing the last open tag when changing node types.
	 *
	 * @private
	 */
	var _nodeTransition = function()
	{
		if (_openTag)
		{
			_output.push('>');
			_openTag = false;
		}
	};

	//-----------------------------------------------------------------------
	// ESCAPE XML
	//-----------------------------------------------------------------------
	/**
	 * @private
	 */
	var _escapeXml = function(att)
	{
		if (undefined === att || null === att)
		{
			return att;
		}

		att = att.toString();

		att = att.replace(/&/g, "&amp;");
		att = att.replace(/</g, "&lt;");
		att = att.replace(/>/g, "&gt;");
		att = att.replace(/"/g, "&quot;");

		return att;
	};
};
