oltk.namespace('openapplicant.quiz.controller');

oltk.include('jquery/jquery.js');

//============================================================================
// LOADING CONTROLLER
//============================================================================
openapplicant.quiz.controller.LoadingController = {
		
	//------------------------------------------------------------------------
	// ACTIONS
	//------------------------------------------------------------------------
	/**
	 * Displays a loading message.
	 */
	show: function() {
		this._getContainer().show();
	},
	
	/**
	 * Hides the loading message
	 */
	hide: function() {
		this._getContainer().hide();
	},
	
	//------------------------------------------------------------------------
	// PRIVATE IMPLEMENTATION
	//------------------------------------------------------------------------
	/**
	 * @private
	 * @return {jQuery}
	 */
	_getContainer: function() {
		var result = jQuery('<div id="openapplicant_loading" class="loading">loading</div>').prependTo(document.body);
		this._getContainer = function() { return result };
		return this._getContainer();
	}
	
};
