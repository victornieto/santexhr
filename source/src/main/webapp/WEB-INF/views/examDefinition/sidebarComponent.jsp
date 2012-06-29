<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<ul class="tests">
<c:forEach var="ed" items="${examDefinitions}">
	<li class="${ed.artifactId eq examDefinition.artifactId && categoryPercentage eq null ?'selected':''}">
		<a href="<c:url value='view?ed=${ed.artifactId}'/>">
			<c:out value="${tt:abbreviate(ed.name)}"/>
		</a>
	</li>
</c:forEach>
</ul>
