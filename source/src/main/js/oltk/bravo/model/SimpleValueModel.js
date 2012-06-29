//===========================================================================
//
// SimpleValueModel.js
//
//===========================================================================
oltk.namespace('oltk.bravo.model');


//===========================================================================
// SIMPLE VALUE MODEL
//===========================================================================
/**
 * Basic data model for a single value.
 *
 * In addition to maintaining the state of the value, this model maintains
 * a list of listeners to notify of any changes.
 *
 * @class	oltk.bravo.model.SimpleValueModel
 * @author	bandur
 */
oltk.bravo.model.SimpleValueModel = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================

	/**
	 * The value.
	 */
	var _value = null;

	/**
	 * List of listeners.
	 */
	var _handlers = [];


	//=======================================================================
	// PUBLIC METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// GET VALUE
	//-----------------------------------------------------------------------
	/**
	 * Returns the current value.
	 *
	 * @return	{object} The value, or null, if not set.
	 */
	this.getValue = function()
	{
		return _value;
	};

	//-----------------------------------------------------------------------
	// SET VALUE
	//-----------------------------------------------------------------------
	/**
	 * Sets the current value.
	 *
	 * Setting the value notifies all handlers of the change.
	 *
	 * @param	{object} value - The value to set.
	 */
	this.setValue = function(value)
	{
		if (_value !== value)
		{
			_value = value;
			_fireValueChanged();
		}
	};

	//-----------------------------------------------------------------------
	// ADD VALUE CHANGED HANDLER
	//-----------------------------------------------------------------------
	/**
	 * Adds a handler to notify when the current value is updated.
	 *
	 * @param	{function} handler - Callback that takes no arguments.
	 */
	this.addValueChangedHandler = function(handler)
	{
		_handlers.push(handler);
	};


	//=======================================================================
	// PRIVATE METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// FIRE VALUE CHANGED
	//-----------------------------------------------------------------------
	/**
	 * @private
	 */
	var _fireValueChanged = function()
	{
		for (var i = 0; i < _handlers.length; i++)
		{
			var handler = _handlers[i];
			handler();
		}
	};
};
