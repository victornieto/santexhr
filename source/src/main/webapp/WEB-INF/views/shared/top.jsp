<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div id="top">
	<a id="logo_link" href="<c:url value='/admin/candidates/index'/>">
		<img id="logo" src="<c:url value='/img/logo.png'/>"/>
	</a>
	<tiles:insertAttribute name="toolbar"/>
</div>

