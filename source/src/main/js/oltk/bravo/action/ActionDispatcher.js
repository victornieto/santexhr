//===========================================================================
//
// ActionDispatcher.js
//
//===========================================================================
oltk.namespace('oltk.bravo.action');


//===========================================================================
// ACTION DISPATCHER
//===========================================================================
/**
 * The main dispatcher for application actions.
 *
 * NOTE: The constructor should not be called directly; instead instances
 * should be acquired using getInstance.
 *
 * @class 	oltk.bravo.action.ActionDispatcher
 * @author	bandur
 */
oltk.bravo.action.ActionDispatcher = function()
{
	//=======================================================================
	// PRIVATE MEMBERS
	//=======================================================================
	var that = this;

	/**
	 * Table mapping names to controllers
	 */
	var _controlTable = {};

	
	//=======================================================================
	// METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// REGISTER CONTROLLER
	//-----------------------------------------------------------------------
	/**
	 * Registers the given controller with the given name.
	 *
	 * @param	{string} name - The name to register.
	 * @param	{object} controller - The controller object.
	 */
	this.registerController = function(name, controller)
	{
		_controlTable[name] = controller;
	};

	//-----------------------------------------------------------------------
	// DISPATCH
	//-----------------------------------------------------------------------
	/**
	 * Dispatches the given action to the controller with the given name.
	 *
	 * @param	{string} controllerName - The name of the controller.
	 * @param	{string} action - The name of the action.
	 * @param	{object} params - Hash of parameters, or null.
	 */
	this.dispatch = function(controllerName, action, params)
	{
		var controller = _controlTable[controllerName];
		controller[action](params);
	};
};


//===========================================================================
// STATIC MEMBERS
//===========================================================================

/**
 * Reference to the global Action Dispatcher singleton.
 *
 * @private
 * @static
 * @final
 */
oltk.bravo.action.ActionDispatcher.__instance = 
	new oltk.bravo.action.ActionDispatcher();


//===========================================================================
// STATIC METHODS
//===========================================================================

//---------------------------------------------------------------------------
// GET INSTANCE
//---------------------------------------------------------------------------
/**
 * Returns the singleton instance of the Action Dispatcher.
 *
 * @static
 * @return	{object} The Action Dispatcher.
 */
oltk.bravo.action.ActionDispatcher.getInstance = function()
{
	return oltk.bravo.action.ActionDispatcher.__instance;
};

