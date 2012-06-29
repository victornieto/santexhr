//===========================================================================
//
// SimpleListModel.js
//
//===========================================================================
oltk.namespace('oltk.bravo.model');


//===========================================================================
// SIMPLE LIST MODEL
//===========================================================================
/**
 * Basic data model for a list.
 *
 * This model maintains the state of the list, and a list of listeners to 
 * notify of changes.
 *
 * @class	oltk.bravo.model.SimpleListModel
 * @author	bandur
 */
oltk.bravo.model.SimpleListModel = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================

	/**
	 * The list.
	 */
	var _list = [];

	/**
	 * Content handlers.
	 */
	var _contentHandlers = [];


	//=======================================================================
	// PUBLIC METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// ADD CONTENTS CHANGED HANDLER
	//-----------------------------------------------------------------------
	/**
	 * Adds a handler for content-change events.
	 *
	 * @param	{function} handler - Callback that takes no arguments.
	 */
	this.addContentsChangedHandler = function(handler)
	{
		_contentHandlers.push(handler);
	};

	//-----------------------------------------------------------------------
	// APPEND
	//-----------------------------------------------------------------------
	/**
	 * Appends an item to the list.
	 *
	 * Notifies content listeners of the change.
	 *
	 * @param	{object} item - The item to append.
	 */
	this.append = function(item)
	{
		_list.push(item);

		_fireContentsChanged();
	};

	//-----------------------------------------------------------------------
	// CLEAR
	//-----------------------------------------------------------------------
	/**
	 * Removes all items from the list.
	 *
	 * Notifies content, selection listeners of the change.
	 */
	this.clear = function()
	{
		var sel = _selection;

		_list = [];

		_fireContentsChanged();
	};

	//-----------------------------------------------------------------------
	// GET
	//-----------------------------------------------------------------------
	/**
	 * Returns the item at the given index.
	 *
	 * @param	{int} index - The index.
	 * @return	{object} The item.
	 */
	this.get = function(index)
	{
		return _list[index];
	};

	//-----------------------------------------------------------------------
	// SET
	//-----------------------------------------------------------------------
	/**
	 * Sets the item at the given index.
	 *
	 * Notifies content listeners of the change.
	 *
	 * @param	{int} index - The index.
	 * @param	{object} item - The item to set.
	 */
	this.set = function(index, item)
	{
		_list[index] = item;

		_fireContentsChanged();
	};

	//-----------------------------------------------------------------------
	// SIZE
	//-----------------------------------------------------------------------
	/**
	 * Returns the current size of the list.
	 *
	 * @return	{int} The length.
	 */
	this.size = function()
	{
		return _list.length;
	};

	//-----------------------------------------------------------------------
	// INDEX OF
	//-----------------------------------------------------------------------
	/**
	 * Returns the index of the given item in the list.
	 *
	 * @return	{int} The index, or -1, if not found.
	 */
	this.indexOf = function(item)
	{
		var result = -1;

		for (var i = 0; i < _list.length; i++)
		{
			if (_list[i] == item)
			{
				result = i;
			}
		}

		return result;
	};

	//-----------------------------------------------------------------------
	// TO ARRAY
	//-----------------------------------------------------------------------
	/**
	 * Returns the current list as an array.
	 *
	 * @return	{array} The array.
	 */
	this.toArray = function()
	{
		var result = [];

		for (var i = 0; i < _list.length; i++)
		{
			result.push(_list[i]);
		}

		return result;
	};


	//=======================================================================
	// PRIVATE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// FIRE CONTENTS CHANGED
	//-----------------------------------------------------------------------
	/**
	 * @private
	 */
	var _fireContentsChanged = function()
	{
		for (var i = 0; i < _contentHandlers.length; i++)
		{
			var handler = _contentHandlers[i];
			handler();
		}
	};
};
