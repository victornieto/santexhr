//===========================================================================
//
// SelectListModel.js
//
//===========================================================================
oltk.namespace('oltk.bravo.model');


//===========================================================================
// SELECT LIST MODEL
//===========================================================================
/**
 * Data model for a list that supports selection.
 *
 * This model maintains the state of the list, the state of a single 
 * selection into the list, and a list of listeners to notify of changes.
 *
 * @class	oltk.bravo.model.SelectListModel
 * @author	bandur
 */
oltk.bravo.model.SelectListModel = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================

	/**
	 * The list.
	 */
	var _list = [];

	/**
	 * The selected index.
	 */
	var _selection = -1;

	/**
	 * Content handlers.
	 */
	var _contentHandlers = [];

	/**
	 * Selection handlers.
	 */
	var _selectionHandlers = [];


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
	// ADD SELECTION CHANGED HANDLER
	//-----------------------------------------------------------------------
	/**
	 * Adds a handler for selection-change events.
	 *
	 * @param	{function} handler - Callback that takes no arguments.
	 */
	this.addSelectionChangedHandler = function(handler)
	{
		_selectionHandlers.push(handler);
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
		_selection = -1;

		_fireContentsChanged();
		if (sel >= 0)
		{
			_fireSelectionChanged();
		}
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

	//-----------------------------------------------------------------------
	// GET SELECTED INDEX
	//-----------------------------------------------------------------------
	/**
	 * Returns the currently selected index.
	 *
	 * @return	{int} The index, or -1, if nothing selected.
	 */
	this.getSelectedIndex = function()
	{
		return _selection;
	};

	//-----------------------------------------------------------------------
	// SET SELECTED INDEX
	//-----------------------------------------------------------------------
	/**
	 * Sets the currently selected item.
	 *
	 * Notifies selection listeners of the change.
	 *
	 * @param	{int} index - The index to set.
	 */
	this.setSelectedIndex = function(index)
	{
		if (index != _selection)
		{
			_selection = index;
			_fireSelectionChanged();
		}
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

	//-----------------------------------------------------------------------
	// FIRE SELECTION CHANGED
	//-----------------------------------------------------------------------
	/**
	 * @private
	 */
	var _fireSelectionChanged = function()
	{
		for (var i = 0; i < _selectionHandlers.length; i++)
		{
			var handler = _selectionHandlers[i];
			handler();
		}
	};
};
