<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<div id="content">
	<h1>User Details</h1>
	<form:form commandName="user" cssClass="group" action="update">
		<form:hidden path="id"/>
		<ul class="half">
			<li>
				<label>First Name:</label>
				<div>
					<form:input path="name.first"/>
					<form:errors path="name.first" cssClass="error"/>
				</div>
			</li>
			<li>
				<label>Middle Name:</label>
				<div>
					<form:input path="name.middle"/>
					<form:errors path="name.middle" cssClass="error"/>
				</div>
			</li>
			<li>
				<label>Last Name:</label>
				<div>
					<form:input path="name.last"/>
					<form:errors path="name.last" cssClass="error"/>
				</div>
			</li>
			<li>
				<label>Email:</label>
				<div>
					<form:input path="email"/>
					<form:errors path="email" cssClass="error"/>
				</div>
			</li>
			<li>
				<label>Role:</label>
				<div>
					<form:select path="role">
						<form:options items="<%=org.openapplicant.domain.User.Role.values()%>" itemValue="name" itemLabel="humanString"/>
					</form:select>
				</div>
			</li>
			<li>
				<label>Active:</label>
				<div>
					<form:checkbox path="enabled"/>
				</div>
			</li>
			<li class="actions">
				<input type="submit" class="submit" value="Save"/>
			</li>
		</ul>
	</form:form>
</div>