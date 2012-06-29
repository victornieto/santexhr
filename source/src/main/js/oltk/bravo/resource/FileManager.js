//===========================================================================
//
// FileManager.js
//
//===========================================================================
oltk.namespace('oltk.bravo.resource');

oltk.include('jquery/jquery.js');


//===========================================================================
// FILE MANAGER
//===========================================================================
/**
 * Manager for handling external resources.
 *
 * NOTE: The constructor should not be called directly; instead, instances
 * should be acquired using getInstance.
 *
 * @class	oltk.bravo.resource.FileManager
 * @author	bandur
 */
oltk.bravo.resource.FileManager = function()
{
	//=======================================================================
	// METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// FETCH
	//-----------------------------------------------------------------------
	/**
	 * Retrieves the given resource synchronously.
	 *
	 * @param	{string} path - The path to the resource.
	 * @return	{string} The content of the given resource.
	 */
	this.fetch = function(path)
	{
		var result = '';

		var options = {
			type: 'GET',
			url: path,
			async: false
		};

		result = jQuery.ajax(options).responseText;

		return result;
	};
};


//===========================================================================
// STATIC MEMBERS
//===========================================================================

/**
 * Reference to the global File Manager singleton.
 *
 * @private
 * @static
 * @final
 */
oltk.bravo.resource.FileManager.__instance = 
	new oltk.bravo.resource.FileManager();


//===========================================================================
// STATIC METHODS
//===========================================================================

//---------------------------------------------------------------------------
// GET INSTANCE
//-----------------------------------------------------------------------
/**
 * Returns the singleton instance of the File Manager.
 *
 * @static
 * @return	{object} The File Manager.
 */
oltk.bravo.resource.FileManager.getInstance = function()
{
	return oltk.bravo.resource.FileManager.__instance;
};

