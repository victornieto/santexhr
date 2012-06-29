<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<div id="content">
	<h1>
		New Exam Definition
	</h1>
	<form:form commandName="examDefinition" action="doAdd" cssClass="group">
		<ul>
			<li>
				<label>Name:</label>
				<div>
					<form:input path="name"/>
					<form:errors cssClass="error" path="name"/>
				</div>
			</li>
			<li>
				<label>Genre:</label>
				<div>
					<form:input path="genre"/>
					<form:errors cssClass="error" path="genre"/>
				</div>
			</li>
			<li>
				<label>Description:</label>
				<div>
					<form:textarea path="description"/>
					<form:errors cssClass="error" path="description"/>
				</div>
			</li>
			<li>
				<label>Exam Definition Status:</label>
				<div>
					<label>
					    <form:radiobutton path="active" value="true"/> Enabled
				    </label>
					<label>
					    <form:radiobutton path="active" value="false"/> Disabled
				    </label>
				</div>
			</li>
			<li>
				<label>Total Number of Questions Wanted:</label>
				<div>
					<form:input path="numberOfQuestionsWanted"/>
					<form:errors cssClass="error" path="numberOfQuestionsWanted"/>
				</div>
			</li>
			<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
			<li class="actions">
				<input type="submit" value="Save" />
			</li>
			</security:authorize>
		</ul>
	</form:form>	
</div>
<security:authorize ifAllGranted="<%=ROLE_HR.name()%>">
<script type="text/javascript">
	jQuery(function() {
		jQuery('#content :input').each(function() {
			this.disabled=true;
		});
	});
</script>
</security:authorize>
