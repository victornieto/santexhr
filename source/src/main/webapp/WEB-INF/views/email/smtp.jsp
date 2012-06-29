<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="content">
	<h1>SMTP</h1>
	<form:form commandName="smtp" cssClass="group" action="updateSmtp">
	<ul class="half">
		<li>
			<label>Host url:</label>
			<div>
				<form:input path="url"/>
				<form:errors path="url" cssClass="error"/>
			</div>
		</li>
		<li>
			<label for="port">Port:</label>
			<div>
				<form:input path="port" cssClass=""/>
				<form:errors path="port" cssClass="error"/>
			</div>
		</li>
		<li>
			<label for="user">User name:</label>
			<div>
				<form:input path="user" cssClass="email"/>
				<form:errors path="user" cssClass="error"/>
			</div>
		</li>
		<li>
			<label for="pass">Password:</label>
			<div>
				<form:password path="pass" cssClass="required"/>
				<form:errors path="pass" cssClass="error"/>
			</div>
		</li>
		<li>
			<label>Default config:</label>
			<div>
				<label>
					<input type="radio" name="status" checked="checked"/> Yes
				</label>
				<label>
					<input type="radio" name="status" disabled="disabled"/> No
				</label>
			</div>
		</li>
		<li class="actions">
			<input type="submit" value="save"/>
		</li>
	</ul>
	</form:form>
</div>

<script type="text/javascript">
	oltk.include('jquery/validate/jquery.validate.js');
	$('#smtp').validate();
</script>
