<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>

<style type="text/css">
	 @import "<c:url value='/css/layout/quiz.css'/>";
</style>

<div class="openapplicant_quiz_info">
	<h2>Hello<c:if test="${tt:notBlank(candidate.name)}"> <c:out value="${candidate.name}"/></c:if>.</h2>
	<pre><c:out value="${welcomeText}"/></pre>
	<a id="next" href="<c:url value='/quiz/info'/>?exam=${examLink.guid}">start</a>
</div>