<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_ADMIN" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_SETTINGS" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<div id="toolbar">
	<security:authorize ifNotGranted="<%=ROLE_SETTINGS.name()%>">
	<div class="toolbar_item" id="candidates_button">
		<div>
			<a href="<c:url value='/admin/candidates/index'/>">
				<img src="<c:url value='/img/candidates_list.png'/>"/>
			</a>
			<security:authorize ifAnyGranted="<%=ROLE_ADMIN.name() + \",\" + ROLE_HR.name() %>">
			<a href="<c:url value='/admin/candidates/create'/>">
				<img src="<c:url value='/img/candidates_new.png'/>"/>
			</a>
			</security:authorize>
		</div>
		<div class="label">Candidates</div>
	</div>
	</security:authorize>
	<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
	<div class="toolbar_item">
		<div>
			<a href="<c:url value='/admin/settings/index'/>">
				<img src="<c:url value='/img/settings.png'/>"/>
			</a>
		</div>
		<div class="label">Settings</div>
	</div>
	</security:authorize>
	<div class="toolbar_item">
		<div>
			<a href="<c:url value='/admin/help/index'/>">
				<img src="<c:url value='/img/help.png'/>"/>
			</a>
		</div>
		<div class="label">Help</div>
	</div>
	<div class="toolbar_item">
		<div>
			<a href="<c:url value='/j_spring_security_logout'/>">
				<img src="<c:url value='/img/logout.png'/>"/>
			</a>
		</div>
		<div class="label">Logout</div>
	</div>
	<div class="toolbar_item" id="email">
        <div>

        </div>
        <div class="label">Email: <%=request.getUserPrincipal().getName()%></div>
	</div>
</div>
<security:authorize ifNotGranted="<%=ROLE_SETTINGS.name()%>">
<form action="<c:url value='/admin/candidates/search'/>">
	<input class="search" id="search" type="text" name="q" value="<c:out value="${fullTextQuery}"/>"/>
	<input class="search" id="search_placeholder" type="text" value="search candidates" />
</form>
<script type="text/javascript">
	$(document).ready(function() {
	/* Press enter to submit search form in IE. */
	$('#search').bind("keydown", function(e){
		if (e.keyCode == 13) {
			this.form.submit();
			return false;
		}
	});

	/* Disappearing placeholder text in search field (cross-browser compatible). */
	$("#search_placeholder").bind("focus", function(e){
		$("#search_placeholder").hide();
		$("#search").show().focus();
	});
	$("#search").bind("blur", function(e){
		if ($("#search").val().length == 0) {
			$("#search").hide();
			$("#search_placeholder").show();
		}
	});
	<c:if test="${!empty fullTextQuery}">
		$("#search_placeholder").focus();
	</c:if>
});
</script>
</security:authorize>