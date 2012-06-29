<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page import="org.openapplicant.domain.User"%>

<div id="content">
	<div id="openapplicant_user_contact_info" class="group">
		<h1>New User</h1>
		<form:form commandName="user" action="create">
			<form:hidden path="id"/>
			<ul class="half left smaller">
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
					<label>Password:</label>
					<div>
						<form:password path="password"/>
						<form:errors path="password" cssClass="error"/>
					</div>
				</li>
				<li>
					<label>Confirm Password:</label>
					<div>
						<input type="password" name="confirmPassword"/>
						<c:if test="${not empty confirmPasswordError}">
							<span class="error"><c:out value="${confirmPasswordError}"/></span>
						</c:if>
					</div>
				</li>
				<li>
					<label>Role:</label>
					<div>
						<form:select path="role">
							<form:options items="<%=User.Role.values()%>" itemValue="name" itemLabel="humanString"/>
						</form:select>
					</div>
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
</div>