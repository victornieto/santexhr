<%@ taglib prefix="c"    uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>

<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/QuizService.js'/>"></script>
<script type="text/javascript">
	oltk.include('openapplicant/quiz/controller/LoadingController.js');
	dwr.engine.setPreHook( function() { openapplicant.quiz.controller.LoadingController.show(); } );
	dwr.engine.setPostHook( function() { openapplicant.quiz.controller.LoadingController.hide(); } );
</script>

<style type="text/css">
	 @import "<c:url value='/css/layout/quiz.css'/>";
</style>

<form id="openapplicant_question_form" class="openapplicant_quiz_question">
	<input type="hidden" id="sittingId" value="${sitting.id}"/>
	<input type="hidden" id="questionId" value="${question.id}"/>
	<div class="row">
	   <span id="name"><c:out value="${sitting.exam.name}"/>, <c:out value="${sitting.nextQuestionIndex}"/> of <c:out value="${fn:length(sitting.exam.questions)}"/></span>
	   <span id="time_allowed"><c:out value="${question.timeAllowed}"/> s</span>
	</div>
	<tiles:insertAttribute name="questionKind"/>
	<a id="next" style="display:none;">continue</a>
</form>
<script type="text/javascript">
	$('a#next').fadeIn('slow');
	
	oltk.include('openapplicant/quiz/helper/timer.js');
	openapplicant.quiz.helper.timer.init('#time_allowed', ${null==question.timeAllowed ? 0 : question.timeAllowed},
		submitResponse,
		nextQuestion
	);

	var submittedResponse = false;
	
	function canContinue() {
		submittedResponse = true;
	}

	function submitResponse() {
		var sittingId = $('#sittingId').val();
		var questionId = $('#questionId').val();
		var response = openapplicant.quiz.helper.recorder.getResponse();
		QuizService.submitResponse(sittingId, questionId, response, canContinue);
	}
	
	function nextQuestion() {
		if(!submittedResponse) { setTimeout("nextQuestion()", 10); }
		else { window.location = "<c:url value='/quiz/question?s=${sitting.guid}'/>"; }
	}
	
	$('#next').click( function() {
		openapplicant.quiz.helper.timer.destroy();		
		submitResponse();
		nextQuestion();
	});
</script>