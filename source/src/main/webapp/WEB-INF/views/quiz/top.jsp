<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div id="top">
	<c:choose>
		<c:when test="${examLink.company.imageUrl != null}">
			<img id="logo" src="${examLink.company.imageUrl}"/>
		</c:when>
		<c:when test="${sitting.candidate.company.imageUrl != null}">
			<img id="logo" src="${sitting.candidate.company.imageUrl}"/>
		</c:when>
		<c:otherwise>
			<img id="logo" src="<c:url value='/img/logo.png'/>"/>
		</c:otherwise>
	</c:choose>
	<tiles:insertAttribute name="toolbar"/>
</div>

