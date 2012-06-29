oltk.namespace('openapplicant.quiz.controller');

oltk.include('jquery/jquery.js');
oltk.include('ejs/ejs.js');
oltk.include('ejs/view.js');
oltk.include('oltk/lang/Class.js');
oltk.include('oltk/bravo/bravo.js');
oltk.include('openapplicant/quiz/controller/EjsHelper.js');

//============================================================================
// APPLICATION CONTROLLER
//============================================================================
/**
 * Provides common methods for controller subclasses.
 */
openapplicant.quiz.controller.ApplicationController = oltk.lang.Class.extend({
	
	//------------------------------------------------------------------------
	// METHODS
	//------------------------------------------------------------------------
	/**
	 * Returns a function that when called, calls the action with parameters
	 * passed to the function. 
	 * 
	 * Inspired by javascriptmvc Controller.continue_to
	 * @see http://javascriptmvc.com/learningcenter/controller/api.html
	 * @example
	 * @param {string} action the name of the action to continue to.
	 * @param {any*} [args] overrides arguments for the call.
	 * @param {boolean} [appendArgs] if true args are appended to call 
	 * args instead of overriding.  If a number the args are inserted at 
	 * the specified position.
	 * @return {Function} the new function.
	 */
	continueTo: function(action, args, appendArgs) {
		return oltk.bind(this[action], this, args, appendArgs);
	},
	
	/**
	 * Generates a url for a given view.
	 * 
	 * @param {string} filename the logical name of the template who's url
	 * to generate.  
	 * (eg. 'exam_link/index' )
	 * @return {string}
	 */
	resolveView: function(name) {
		return oltk.getScriptpath() + 'openapplicant/quiz/view/' + name + '.ejs';
	},
	
	/**
	 * Replaces the inner html of a given element.
	 * 
	 * @example
	 * SomeController = ApplicationController.extend({
	 * 
	 * 		render: function(user) {
	 * 			this.replaceHtml('#container', {
	 * 				view: 'index',
	 * 				model: { user: user }
	 * 			});
	 * 		}
	 * });
	 * 
	 * @param {string|HTMLElement} el the element who's innerHTML to replace.
	 * 			may be an html element, jquery expression, or dom Id.
	 * 			eg:  all are valid
	 * 				document.getElementById('someId');
	 * 				'someId'
	 * 				'div#someId'
	 * 
	 * @param {Object|string} [o] If an object, the element's innerHTML will be
	 * replaced with the rendered template.  See render method for options.  
	 * If a string, the element's innerHTML will be updated with the given string.  
	 * If null or undefined, the element's content will be emptied.
	 */
	replaceHtml: function(el, o) {
		if(!el) {
			return;
		}
		var query = jQuery(document.getElementById(el) || el);
		if(query.length) {
			if(typeof o === 'string') {
				query.html(o);
			}
			else if(typeof o === 'object') {
				query.html(this.render(o));
			}
			else {
				query.empty();
			}
		}
	},
	
	/**
	 * Renders the given template.
	 * @param {Object} o a hash of rendering options.
	 * {
	 * 		[view]: {string} the logical name of the template to render. 
	 * 			eg.  'exam_link/index'
	 * 		[text]: {string} the provided text to use as a template.
	 * 		[model]: {Object} an hash of data to pass to the view template.
	 * }
	 * @return {string} the rendered html
	 */
	render: function(o) {
		return new EJS({
			url: o.view ? this.resolveView(o.view) : undefined,
			text: typeof o.text === 'string' ? o.text : undefined
		}).render(o.model || {});
	}
});

