oltk.namespace('openapplicant.quiz.controller');

oltk.include('jquery/jquery.js');
oltk.include('jquery/validate/jquery.validate.js');

oltk.include('openapplicant/quiz/controller/ApplicationController.js');

//============================================================================
// INFO CONTROLLER
//============================================================================
openapplicant.quiz.controller.InfoController = openapplicant.quiz.controller.ApplicationController.extend({
	
	//------------------------------------------------------------------------
	// PROPERTIES
	//------------------------------------------------------------------------
	/**
	 * A container to render the candidate info view in.  May be a dom id
	 * or an HTMLElement
	 * @type string | HTMLElement
	 */
	container: null,
	
	/**
	 * @type QuizService
	 */
	quizService: null,
	
	/**
	 * @type ExamLink
	 */
	examLink: null,
	
	//------------------------------------------------------------------------
	// METHODS
	//------------------------------------------------------------------------	
	/**
	 * Sets the exam link that is checked on document ready.
	 * @param {examLink} examLink
	 */
	setExamLink: function(examLink) {
		
		if(examLink == null) {
			oltk.bravo.execute('message', 'sorry', 'Your exam link is old or invalid.');
			return;
		}
		
		if(examLink.used) {
			oltk.bravo.execute('message', 'sorry', 'This exam link has already been used.');
			return;
		}

		//This is when it's a company exam link, I don't like having to do this.
		if(examLink.candidate == null) {
			examLink.candidate = {
					id: '',
					email: '',
					name: {
						first: '',
						last: ''
					}
			};
		}
		
		//FIXME: for testing
		for(i in examLink.exams) {
			if(!examLink.exams[i].active) {
				alert('Why is ' + examLink.exams[i].description + " disabled and here?");
			}
		}
		
		this.examLink = examLink;
	},
	
	/**
	 * Renders the info view with the examLink
	 */
	showExamLink: function() {
		this.replaceHtml(this.container, {
			view: 'info',
			model: {
				candidate: this.examLink.candidate,
				exams: this.examLink.exams
			}
		});
		$('a#next').fadeIn('slow');
	},
	
	/**
	 * Creates a sitting for the candidate with the selected exam.
	 * @param {examLink} examLink
	 */
	sit: function(params) {
		this.quizService.startExamLink(this.examLink.guid);
		
		this.quizService.createSitting(params.candidate, this.examLink.company.id, params.examArtifactId, function(sitting) {
			oltk.bravo.execute('question', 'start', sitting);
		});
	}
	
});
