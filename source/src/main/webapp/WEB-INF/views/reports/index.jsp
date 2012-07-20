<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_ADMIN"%>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_SETTINGS"%>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR_MANAGER"%>

<div id="content">
	<h1>Reports</h1>
	<div class="group">
	
		<ul id="menu">
			<li>
				<a href="<c:url value='/admin/reports/view' />">
					<img src="<c:url value='/img/settings/exams.jpg' />" />
				</a>
				<h2>Count of Candidates per status:</h2>
				<p>Show current candidates count per status.</p>				
			</li>	
		</ul>	
		
	</div>
</div>