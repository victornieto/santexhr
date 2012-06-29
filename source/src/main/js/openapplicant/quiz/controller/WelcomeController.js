oltk.namespace('openapplicant.quiz.controller');

oltk.include('jquery/jquery.js');

oltk.include('openapplicant/quiz/controller/ApplicationController.js');

//============================================================================
// QUIZ CONTROLLER
//============================================================================
openapplicant.quiz.controller.WelcomeController = openapplicant.quiz.controller.ApplicationController.extend({
	
	//------------------------------------------------------------------------
	// PROPERTIES
	//------------------------------------------------------------------------
	/**
	 * A container to render the candidate info view in.  May be a dom id
	 * or an HTMLElement
	 * @type string|HTMLElement
	 */
	container: null,
	
	//------------------------------------------------------------------------
	// ACTIONS
	//------------------------------------------------------------------------
	view: function() {
		this.replaceHtml(this.container, {
			view: 'welcome'
		});
		$('a#next').fadeIn('slow');
	}
});
