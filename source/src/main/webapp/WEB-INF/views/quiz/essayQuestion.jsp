<%@ taglib prefix="c"    uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>

<%@ page import="org.openapplicant.domain.question.EssayQuestion" %>

<div class="row righty">
	<pre id="prompt">${question.prompt}</pre>
</div>
<textarea id="response" name="content" class="essay"></textarea>
<script type="text/javascript">
	oltk.include('openapplicant/quiz/helper/recorder.js');
	openapplicant.quiz.helper.recorder.init('#response');

	oltk.include('openapplicant/quiz/helper/tab.js');
	openapplicant.quiz.helper.tab.init('#response');
</script>