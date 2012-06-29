//===========================================================================
//
// DisplayManager.js
//
//===========================================================================
oltk.namespace('oltk.bravo.presentation');

oltk.include('jquery/jquery.js');


//===========================================================================
// DISPLAY MANAGER
//===========================================================================
/**
 * Manager for manipulating the display.
 *
 * @class	oltk.bravo.presentation.DisplayManager
 * @author	bandur
 */
oltk.bravo.presentation.DisplayManager = function()
{
	//=======================================================================
	// METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// COUNT
	//-----------------------------------------------------------------------
	/**
	 * Determines the number of elements that match the given selector.
	 *
	 * @param	{string} selector - CSS selector for the elements to match.
	 * @return	{int} The count.
	 */
	this.count = function(selector)
	{
		return jQuery(selector).length;
	};

	//-----------------------------------------------------------------------
	// APPEND
	//-----------------------------------------------------------------------
	/**
	 * Appends the given content to all elements matching the given selector.
	 *
	 * @param	{string} selector - CSS selector indicating the elements to
	 *				which to append.
	 * @param	{string} content - The HTML content to append.
	 */
	this.append = function(selector, content)
	{
		jQuery(selector).append(content);
	};

	//-----------------------------------------------------------------------
	// PREPEND
	//-----------------------------------------------------------------------
	/**
	 * Prepends the given content to all elements matching the given selector.
	 *
	 * @param	{string} selector - CSS selector indicating the elements to
	 * 				which to prepend.
	 * @param	{string} content - The HTML content to prepend.
	 */
	this.prepend = function(selector, content)
	{
		jQuery(selector).prepend(content);
	};

	//-----------------------------------------------------------------------
	// AFTER
	//-----------------------------------------------------------------------
	/**
	 * Inserts the given content immediately following each element matching
	 * the given selector.
	 *
	 * @param	{string} selector - CSS selector indicating the elements after
	 *				which to insert.
	 * @param	{string} content - The HTML content to insert.
	 */
	this.after = function(selector, content)
	{
		jQuery(selector).after(content);
	};

	//-----------------------------------------------------------------------
	// BEFORE
	//-----------------------------------------------------------------------
	/**
	 * Inserts the given content immediately before each element matching the
	 * given selector.
	 *
	 * @param	{string} selector - CSS selector indicating the element before
	 *				which to insert.
	 * @param	{string} content - The HTML content to insert.
	 */
	this.before = function(selector, content)
	{
		jQuery(selector).before(content);
	};

	//-----------------------------------------------------------------------
	// REPLACE
	//-----------------------------------------------------------------------
	/**
	 * Replaces the matching elements with the given content.
	 *
	 * @param	{string} selector - CSS selector indicating the element to
	 * 				replace.
	 * @param	{string} content - The HTML content to set.
	 */
	this.replace = function(selector, content)
	{
		jQuery(selector).replaceWith(content);
	};

	//-----------------------------------------------------------------------
	// EMPTY
	//-----------------------------------------------------------------------
	/**
	 * Empties matching elements of their contents.
	 *
	 * @param	{string} selector - CSS selector indicating the elements to
	 *				empty.
	 */
	this.empty = function(selector)
	{
		jQuery(selector).empty();
	};

	//-----------------------------------------------------------------------
	// REMOVE
	//-----------------------------------------------------------------------
	/**
	 * Removes matching elements.
	 *
	 * @param	{string} selector - CSS selector indicating the elements to
	 *				remove.
	 */
	this.remove = function(selector)
	{
		jQuery(selector).remove();
	};
};

//===========================================================================
// STATIC MEMBERS
//===========================================================================

/**
 * Reference to the global Display Manager singleton.
 *
 * @private
 * @static
 * @final
 */
oltk.bravo.presentation.DisplayManager.__instance = 
	new oltk.bravo.presentation.DisplayManager();


//===========================================================================
// STATIC METHODS
//===========================================================================

//---------------------------------------------------------------------------
// GET INSTANCE
//---------------------------------------------------------------------------
/**
 * Returns the singleton instance of the Display Manager.
 *
 * @static
 * @return	{object} The Display Manager.
 */
oltk.bravo.presentation.DisplayManager.getInstance = function()
{
	return oltk.bravo.presentation.DisplayManager.__instance;
};
