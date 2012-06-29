<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld" %>

<label>Answer:</label>
<div>
	<textarea name="answer" class="essay required"><c:out value="${question.answer}"/></textarea>
	<span class="error"><c:out value="${answerError}"/></span>
</div>