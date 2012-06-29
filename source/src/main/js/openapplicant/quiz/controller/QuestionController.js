oltk.namespace('openapplicant.quiz.controller');

oltk.include('openapplicant/quiz/helper/recorder.js');
oltk.include('openapplicant/quiz/helper/exit.js');
oltk.include('openapplicant/quiz/helper/json.js');

//============================================================================
// QUESTION CONTROLLER
//============================================================================
openapplicant.quiz.controller.QuestionController = openapplicant.quiz.controller.ApplicationController.extend({
	
	//------------------------------------------------------------------------
	// PROPERTIES
	//------------------------------------------------------------------------
	/**
	 * A container to render the candidate info view in.  May be a dom id
	 * or an HTMLElement
	 * @type string|HTMLElement
	 */
	container: null,
	
	/**
	 * @type QuizService 
	 */
	quizService: null,
	
	/**
	 * @type Sitting
	 */
	sitting: null,
	
	//------------------------------------------------------------------------
	// ACTIONS
	//------------------------------------------------------------------------
	/**
	 *  Is init used in the oltk.lang? for some reason if this is 'init' it's 
	 * executed immediately on load.
	 * @param {sitting} sitting
	 */
	start: function(sitting) {
		if(sitting.finished) {
			oltk.bravo.execute('message', 'sorry', 'This exam has already been completed.');
			return;
		}
	
		openapplicant.quiz.helper.exit.init();
		this.sitting = sitting;
		this.view();
	},
	
	/**
	 * Fetches and displays the current question.
	 * @param {long} examId
	 */
	view: function() {
		this.quizService.getQuestionBySitting(this.sitting.id, this.continueTo('showQuestion'));
	},
	
	//------------------------------------------------------------------------
	// METHODS
	//------------------------------------------------------------------------
	/**
	 * Refreshes the candidate view.
	 * @param {Candidate} candidate
	 */
	showQuestion: function(question) {
		if(null == question) {
			openapplicant.quiz.helper.exit.destroy();
			this.thanks();
			return;	
		}
		
		this.sitting.nextQuestionIndex++;
		this.replaceHtml(this.container, {
			view: 'question',
			model: {
				sitting: this.sitting,
				question: question
			}
		});
		$('a#next').fadeIn('slow');
	},
	
	/**
	 * Fetches the response from the recorder and submits it
	 * @param the form's parameters, simply the question id
	 */
	submitResponse: function(params) {
		var response = openapplicant.quiz.helper.recorder.getResponse();
		this.quizService.submitResponse(this.sitting.id, params.question.id, response);
	},
	
	thanks: function() {
		this.replaceHtml(this.container, {
			view: 'thanks'
		});
	}
	
});
