<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<ul>
	<li class="selected">
		<c:out value="${tt:abbreviate(jobPosition.name)}"/>
	</li>
</ul>
