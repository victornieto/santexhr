oltk.namespace('openapplicant.quiz.controller');

oltk.include('jquery/jquery.js');
oltk.include('openapplicant/quiz/controller/ApplicationController.js');

//============================================================================
// MESSAGE CONTROLLER
//============================================================================
openapplicant.quiz.controller.MessageController = openapplicant.quiz.controller.ApplicationController.extend({

	//------------------------------------------------------------------------
	// PROPERTIES
	//------------------------------------------------------------------------
	/**
	 * A container to render the candidate info view in.  May be a dom id
	 * or an HTMLElement
	 * @type string | HTMLElement
	 */
	container: null,
	
	//------------------------------------------------------------------------
	// METHODS
	//------------------------------------------------------------------------
	/**
	 * Let's the user know they've got a messed up exam link.
	 */
	sorry: function( message ) {
		this.replaceHtml(this.container, {
			view: 'sorry',
			model: {
				error: message
			}
		});
	}
	
});
