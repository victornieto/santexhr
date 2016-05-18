<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_ADMIN"%>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_SETTINGS"%>

<div id="sidebar">
	<div class="group">
		<h3 class="${reportsSidebar? 'selected':''}">
			<img src="<c:url value='/img/sidebar/settings.gif' />" />
			<a href="<c:url value='/admin/settings/index' />">
				Reports
			</a>
		</h3>
		<ul>
			<li class="exam ${reportsSidebar? 'selected':''}" id="rep_">
				<a href="<c:url value='/admin/reports/index' />">
					Candidates
				</a>
				<tiles:insertAttribute name="candidates"/>		
			</li>
			
		</ul>
	</div>
</div>