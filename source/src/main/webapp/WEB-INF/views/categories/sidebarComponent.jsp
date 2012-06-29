<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>
<ul class="tests">
<c:forEach var="c" items="${categories}">
	<li id="cat_${c.id}" class="${c.id eq category.id ? 'selected':''}">
		<a href="<c:url value='/admin/category/view?c=${c.id}'/>">
			<c:out value="${tt:abbreviate(c.name)}"/>
		</a>
	</li>
	<c:forEach var="node" items="${c.children}">
		<c:set var="node" value="${node}" scope="request"/>
		<c:set var="category" value="${category}" scope="request"/>
		<jsp:include page="node.jsp"/>
	</c:forEach>
</c:forEach>
</ul>