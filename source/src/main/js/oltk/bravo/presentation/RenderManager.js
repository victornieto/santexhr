//===========================================================================
//
// RenderManager.js
//
//===========================================================================
oltk.namespace('oltk.bravo.presentation');


//===========================================================================
// RENDER MANAGER
//===========================================================================
/**
 * Manager for rendering presentation content.
 *
 * Note that methods of this class are used purely for generating content;
 * methods of the Display Manager are used to actually present that content
 * on the display.
 *
 * @class	oltk.bravo.presentation.RenderManager
 * @author	bandur
 */
oltk.bravo.presentation.RenderManager = function()
{
	//=======================================================================
	// METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// XSLT RENDER
	//-----------------------------------------------------------------------
	/**
	 * Renders a content fragment using XML data and an XSLT template.
	 *
	 * @param	{string} template - The content of the XSLT template.
	 * @param	{string} data - The XML data.
	 * @param	{object} params - Hash of params to pass to the template.
	 * @return	{string} The result, as a string.
	 */
	this.xsltRender = function(template, data, params)
	{
		//Lazy include.
		oltk.include('gr/abiss/js/sarissa/sarissa.js');

		//Render.
		var xml = (new DOMParser()).parseFromString(data, "text/xml");
		if (Sarissa.getParseErrorText(xml) != Sarissa.PARSED_OK)
		{
			alert(Sarissa.getParseErrorText(xml));
		}
		var xsl = (new DOMParser()).parseFromString(template, "text/xml");
		if (Sarissa.getParseErrorText(xsl) != Sarissa.PARSED_OK)
		{
			alert(Sarissa.getParseErrorText(xsl));
		}

		var xslt = new XSLTProcessor();
		xslt.importStylesheet(xsl);
		if (null != params)
		{
			for (var name in params)
			{
				var value = params[name];
				xslt.setParameter(null, name, value);
			}
		}

		var output = xslt.transformToDocument(xml);
		var result = (new XMLSerializer()).serializeToString(output);

		return result;
	};

	//-----------------------------------------------------------------------
	// EJS RENDER
	//-----------------------------------------------------------------------
	/**
	 * Renders a content fragment using JSON data and an EJS template.
	 *
	 * @param	{string} template - The content of the EJS template.
	 * @param	{object} data - The JSON data.
	 * @return	{string} The result, as a string.
	 */
	this.ejsRender = function(template, data)
	{
		//Lazy include.
		oltk.include('ejs/ejs.js');

		//Render.
		var ejs = new EJS({ text: template });
		var result = ejs.render(data);

		return result;
	};
};


//===========================================================================
// STATIC MEMBERS
//===========================================================================

/**
 * Reference to the global Render Manager singleton.
 *
 * @private
 * @static
 * @final
 */
oltk.bravo.presentation.RenderManager.__instance =
new oltk.bravo.presentation.RenderManager();


//===========================================================================
// STATIC METHODS
//===========================================================================

//---------------------------------------------------------------------------
// GET INSTANCE
//---------------------------------------------------------------------------
/**
 * Returns the singleton instance of the Render Manager.
 *
 * @static
 * @return	{object} The Render Manager.
 */
oltk.bravo.presentation.RenderManager.getInstance = function()
{
	return oltk.bravo.presentation.RenderManager.__instance;
};

