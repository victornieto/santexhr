//===========================================================================
//
// bravo.js
//
//===========================================================================
oltk.namespace('oltk.bravo');

oltk.include('jquery/jquery.js');
oltk.include('oltk/bravo/action/ActionDispatcher.js');


//===========================================================================
// BRAVO
//===========================================================================
/**
 * @fileoverview
 * Collection of global functions for use with the Bravo Javascript Framework.
 */

//---------------------------------------------------------------------------
// REGISTER
//---------------------------------------------------------------------------
/**
 * Registers a controller as a handler for action events.
 *
 * @param	{string} name - The name to register the controller under.
 * @param	{object} controller - The controller object.
 */
oltk.bravo.register = function(name, controller)
{
	var dispatcher = oltk.bravo.action.ActionDispatcher.getInstance();
	dispatcher.registerController(name, controller);
};

//---------------------------------------------------------------------------
// EXECUTE
//---------------------------------------------------------------------------
/**
 * Raises an action event.
 *
 * @param	{string} controller - The name of the controller to which to 
 *			dispatch.
 * @param	{string} action - The name of the action.
 * @param	{object} params - Hash containing the parameters for the action.
 */
oltk.bravo.execute = function(controller, action, params)
{
	var dispatcher = oltk.bravo.action.ActionDispatcher.getInstance();
	dispatcher.dispatch(controller, action, params);
};

//---------------------------------------------------------------------------
// PARAMS
//---------------------------------------------------------------------------
/**
 * Gathers a hash of named parameters.
 *
 * Note that the CSS selector is a highly powerful method of choosing which
 * values to retrieve.  Below are some examples:
 *
 *		"#username,#password" - retrieves the values of the fields with the
 *			IDs "username" and "password".
 *		"#loginform input" - retrieves the values of all input elements that
 *			are descendents of the element with the ID "loginform".
 *			
 * @param	{string} selector - CSS selector for the fields to retrieve
 * @param 	{HTMLElement} [context] an optional context to search within.
 * 			If specified, the selector will be matched against the contents
 * 			of that context.
 * @return	{object} Hash containing the values of the fields with the given
 *			names.
 */
oltk.bravo.params = function(selector, context)
{
	var result = {};

	jQuery(selector, context).each(
		function(index) {
			
			var key = this.name || this.id;
			
			if(!key) 
			{
				return;
			}
			else if(jQuery(this).is(':radio'))
			{
				if(this.checked) 
				{
					result[key] = oltk.bravo._val(this);
				}
			}
			else if(jQuery(this).is(':checkbox'))
			{
				if(this.checked)
				{
					result[key] = result[key] || [];
					result[key].push( oltk.bravo._val(this) );
				}
			}
			else 
			{
				result[key] = oltk.bravo._val(this);
			}
		}
	);
	
	return result;
};

/**
 * @private
 * @param {HTMLElement} el
 * @return {any}
 */
oltk.bravo._val = function(el) 
{
	try
	{
		return jQuery(el).val();
	}
	catch(e)
	{
		// jQuery.val throws TypeError if el.value is not a string (eg. number)
		return el.value;
	}
};

//---------------------------------------------------------------------------
// EXPAND
//---------------------------------------------------------------------------
/**
 * Gathers a hash of named parameters and expands the delimited parameter names 
 * into nested object properties.  
 * @example
 * <pre>
 * Given the following form:
 * 
 * &lt;form onsubmit="oltk.bravo.execute('controller','action', oltk.bravo.expand(*, this));"&gt;
 *   &lt:input type="hidden" name="id" value="1"/&gt;
 *   &lt;input type="text" name="employer.name.first" value=""/&gt;
 *   &lt;input type="text" name="employer.name.last" value=""/&gt;
 *   &lt;input type="text" name="employer.email" value=""/&gt;
 *   &lt;input type="submit" value="Submit"/&gt;
 * &/lt;form&gt;
 * 
 * Hitting the submit button will dispatch the following hash:
 * { 
 *   id: 1,
 *   employer: {
 *      name: {
 *         first: '',
 *         last: ''
 *      },
 *      email: '..'
 *   }
 * }
 * </pre>
 * 
 * @param {string} selector a css selector for the fields to retrieve.
 * @param {Object} [context] an optional context to search within.  
 * @param [string] [delim] the field name delimiter.  Default is '.'
 * @return {Object} a params hash with field names expanded.
 */
oltk.bravo.expand = function(selector, context, delim)
{
	var params = oltk.bravo.params(selector, context);
	return oltk.bravo.expandProperties(params, delim);
};

//---------------------------------------------------------------------------
// EXPAND PROPERTIES
//---------------------------------------------------------------------------
/**
 * Utility method for expanding delimited property names into nested
 * objects.  
 * @example
 * <pre>
 * var o = {
 *   'person.name.first': 'sam',
 *   'person.name.last': 'jackson'
 *   'person.email': 'sam@gmail.com',
 *   'id':1
 * };
 * 
 * var expanded = oltk.bravo.expandProperties(o);
 * 
 * assertObjectEquals(
 * 		expanded,
 * 		{
 * 			id: 1,
 * 			person: {
 * 				name: {
 * 					first: 'sam',
 * 					last: 'jackson'
 * 				},
 * 				email:'sam@gmail.com'
 * 			}
 * 		}
 * );
 * </pre>
 * @param {Object} obj the object to expand
 * @param {string} [delim] the propety name delimiter.  Default is '.'
 * @return {Object} the expanded object.
 */
oltk.bravo.expandProperties = function(obj, delim) 
{
	var result = {};
	
	for(var name in obj) if(obj.hasOwnProperty(name)) 
	{
		var n = name.split(delim || '.');
		var o = result;
		for(var i=0; i<n.length; i++) 
		{
			if(i < n.length -1 )
			{
			 	o[n[i]] = o[n[i]] || {};
				o = o[n[i]];
			}
			else 
			{
				o[n[i]] = obj[name];
			}
		}
	}
	return result;
};

