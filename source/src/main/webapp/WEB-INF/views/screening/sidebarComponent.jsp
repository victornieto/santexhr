<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<ul>
	<li class="preferences ${preferencesSidebar?'selected':''}">
		<a href="<c:url value='preferences' />">
			Preferences
		</a>
	</li>
	<li class="keywords ${keywordsSidebar?'selected':''}">
		<a href="<c:url value='keywords' />">
			Keywords
		</a>
	</li>
</ul>