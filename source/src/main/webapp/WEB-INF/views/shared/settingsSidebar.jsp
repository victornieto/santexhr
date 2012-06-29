<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_ADMIN"%>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_SETTINGS"%>

<div id="sidebar">
	<div class="group">
		<h3 class="${settingsSidebar? 'selected':''}">
			<img src="<c:url value='/img/sidebar/settings.gif' />" />
			<a href="<c:url value='/admin/settings/index' />">
				Settings
			</a>
		</h3>
		<ul>
			<li class="exam ${categoriesSidebar || (!(question eq null) && category eq null) ?'selected':''}" id="cat_">
				<a href="<c:url value='/admin/categories/index' />">
					Categories
				</a>
			</li>
			<tiles:insertAttribute name="categories"/>			
			<li class="exam ${examsSidebar?'selected':''}">
				<a href="<c:url value='/admin/examDefinitions/index' />">
					Exam Definitions
				</a>
				<tiles:insertAttribute name="examDefinitions"/>
			</li>
			<security:authorize ifAnyGranted="<%=ROLE_ADMIN.name() + \",\" + ROLE_SETTINGS.name()%>">
				<li class="email ${emailSidebar?'selected':''}">
					<a href="<c:url value='/admin/email/index' />">
						Email
					</a>
					<tiles:insertAttribute name="email"/>
				</li>
				<li class="screening ${screeningSidebar?'selected':''}">
					<a href="<c:url value='/admin/screening/index' />">
						Screening
					</a>
					<tiles:insertAttribute name="screening"/>
				</li>
				<li class="users ${usersSidebar?'selected':''}">
					<a href="<c:url value='/admin/users/index' />">
						Users
					</a>
					<tiles:insertAttribute name="users"/>
				</li>
			</security:authorize>
		</ul>
	</div>
</div>