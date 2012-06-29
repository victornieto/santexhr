<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<c:if test="${not user.name.blank}">
	<ul>
		<li class="user selected">
			<span><c:out value="${tt:abbreviate(user.name)}"/></span>
		</li>
	</ul>
</c:if>