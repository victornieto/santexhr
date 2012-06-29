<%@ taglib prefix="c"    uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>

<style type="text/css">
		.openapplicant_quiz_question {
			width: 60%;
		}
</style>

<div class="row righty">
	<pre id="prompt">${question.prompt}</pre>
</div>
<ul id="answerChoices" class="multiple_choice">
	<c:forEach var="choice" items="${questionViewHelper.choices}" varStatus="row">
	<li>
		<label>
			<input type="radio" class="radio" name="answerIndex" value="${row.index}"/>
			<c:out value="${choice}"/>
		</label>
	</li>
	</c:forEach>
</ul>

<div style="display:none">
	<textarea id="response"></textarea>
</div>
	
<script type="text/javascript">
	oltk.include('openapplicant/quiz/helper/recorder.js');
	
	$('#answerChoices li').each(function() {
		var self = this;
		var choice = $.trim($('label', this).text());
		var radio = $('input:radio', this)[0];
		function multipleChoiceChange() {
			$('#response').val(choice);
			openapplicant.quiz.helper.recorder.keypressNoPaste();
		};
		$(radio).change(multipleChoiceChange);
	});
	
	openapplicant.quiz.helper.recorder.init('#response');
</script>