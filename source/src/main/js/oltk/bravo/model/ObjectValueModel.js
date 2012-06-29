//===========================================================================
//
// ObjectValueModel.js
//
//===========================================================================
oltk.namespace('oltk.bravo.model');


//===========================================================================
// OBJECT VALUE MODEL
//===========================================================================
/**
 * Data model for a single object value.
 *
 * The idea behind this model is that listeners should be notified if any of
 * the properties of the object are altered; however, it is not possible
 * to detect when properties are set on the object itself.  Therefore, this
 * class provides a method that can be used explicitly notify listeners of
 * modifications.  Each time a change is made to a property of a model object,
 * the fireValueChanged method should be called to notify change listeners.
 *
 * @class	oltk.bravo.model.ObjectValueModel
 * @author	bandur
 */
oltk.bravo.model.ObjectValueModel = function()
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

	//-----------------------------------------------------------------------
	// FIRE VALUE CHANGED
	//-----------------------------------------------------------------------
	/**
	 * Notifies change listeners that the current value has been updated.
	 */
	this.fireValueChanged = function()
	{
		_fireValueChanged();
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
