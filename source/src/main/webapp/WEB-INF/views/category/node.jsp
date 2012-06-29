<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>
<ul class="tests">
	<li id="cat_${node.id}" class="${(node.id eq category.id || (!(parentCategoryId eq null) && parentCategoryId eq node.id) ) ?'selected':''}">
		<a href="<c:url value='/admin/category/view?c=${node.id}'/>">
			<c:out value="${tt:abbreviate(node.name)}"/>
		</a>
	</li>
	<c:forEach var="node" items="${node.children}">
		<c:set var="node" value="${node}" scope="request"/>
		<c:set var="category" value="${category}" scope="request"/>
		<c:set var="parentCategoryId" value="${parentCategoryId}" scope="request"/>
		<jsp:include page="node.jsp"/>
	</c:forEach>
</ul>