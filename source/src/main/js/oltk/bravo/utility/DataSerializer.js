//===========================================================================
//
// DataSerializer.js
//
//===========================================================================
oltk.namespace('oltk.bravo.utility');

oltk.include('oltk/util/LangUtils.js');
oltk.include('oltk/xmlpull/XmlSerializer.js');


//===========================================================================
// DATA SERIALIZER
//===========================================================================
/**
 * Utilty for serializing data.
 *
 * @class	oltk.bravo.utility.DataSerializer
 */
oltk.bravo.utility.DataSerializer = function()
{
	//=======================================================================
	// METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// TO XML
	//-----------------------------------------------------------------------
	/**
	 * Serializes the given data as XML.
	 *
	 * The given name is used for the name of the root element.  The names of
	 * subelements are generally derived from property name, except in the 
	 * case of array members.  All array members are named 'item'.  For
	 * example, the following Javascript data:
	 *
	 * <pre>
	 *	[ { name: 'foo', type: 'x' }, { name: 'bar', type: 'y' } ]
	 * </pre>
	 *
	 * would produce the following XML (assuming 'stuff' was provided as the
	 * rootName:
	 *
	 * <pre>
	 *	&lt;stuff&gt;
	 *		&lt;item&gt;
	 *			&lt;name&gt;foo&lt;/name&gt;
	 *			&lt;type&gt;x&lt;/type&gt;
	 *		&lt;/item&gt;
	 *		&lt;item&gt;
	 *			&lt;name&gt;bar&lt;/name&gt;
	 *			&lt;type&gt;y&lt;/type&gt;
	 *		&lt;/item&gt;
	 *	&lt;/stuff&gt;
	 * </pre>
	 *
	 * @param	{string} rootName - The name to use for the root element.
	 * @param	{any} data - The data to serialize.
	 */
	this.toXml = function(rootName, data)
	{
		var xmls = new oltk.xmlpull.XmlSerializer();
		xmls.startDocument(null, true);

		if (undefined === data || null === data)
		{
			_scalarToXml(xmls, rootName, '');
		}
		else if (oltk.util.LangUtils.isArray(data))
		{
			_arrayToXml(xmls, rootName, data);
		}
		else if (oltk.util.LangUtils.isObject(data))
		{
			_objectToXml(xmls, rootName, data);
		}
		else
		{
			_scalarToXml(xmls, rootName, data);
		}

		xmls.endDocument();
		
		return xmls.getOutput();
	};

	//-----------------------------------------------------------------------
	var _objectToXml = function(xmls, name, data)
	{
		xmls.startTag(null, name);

		for (var prop in data) if (data.hasOwnProperty(prop))
		{
			var value = data[prop];
			if (undefined === value || null === value)
			{
				//Property exists, but is not defined.
				_scalarToXml(xmls, prop, '');
			}
			else if (oltk.util.LangUtils.isArray(value))
			{
				_arrayToXml(xmls, prop, value);
			}
			else if (oltk.util.LangUtils.isObject(value))
			{
				_objectToXml(xmls, prop, value);
			}
			else
			{
				_scalarToXml(xmls, prop, value);
			}
		}

		xmls.endTag(null, name);
	};

	//-----------------------------------------------------------------------
	var _arrayToXml = function(xmls, name, data)
	{
		xmls.startTag(null, name);

		for (var i = 0; i < data.length; i++)
		{
			var value = data[i];
			if (undefined === value || null === value)
			{
				//Index exists, but value is undefined.
				_scalarToXml(xmls, 'item', '');
			}
			else if (oltk.util.LangUtils.isArray(value))
			{
				//Value is an array.
				_arrayToXml(xmls, 'item', value);
			}
			else if (oltk.util.LangUtils.isObject(value))
			{
				//Value is an object.
				_objectToXml(xmls, 'item', value);
			}
			else
			{
				_scalarToXml(xmls, 'item', value);
			}
		}

		xmls.endTag(null, name);
	};

	//-----------------------------------------------------------------------
	var _scalarToXml = function(xmls, name, data)
	{
		xmls.startTag(null, name);
		xmls.text(data);
		xmls.endTag(null, name);
	};
};
