<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="content">
	<h1>Email Connector</h1>
	<form:form commandName="facilitator" cssClass="group" action="updateFacilitator">
		<ul class="half">
			<li>
				<label for="user">Mail user:</label>
				<div>
					<form:input path="user"/>
					<form:errors path="user" cssClass="error"/>
				</div>
			</li>
			<li>
				<label for="pass">Password:</label>
				<div>
					<form:password path="pass"/>
					<form:errors path="pass" cssClass="error"/>
				</div>
			</li>
			<li>
				<label for="url">URL:</label>
				<div>
					<form:input path="url"/>
					<form:errors path="url" cssClass="error"/>
				</div>
			</li>
			<li>
				<label for="port">Protocol:</label>
				<div>
					<form:select path="protocol">
						<form:options items="<%=org.openapplicant.domain.setting.Facilitator.Protocol.values()%>" itemValue="name" itemLabel="name"/>
					</form:select>
					<form:errors path="protocol" cssClass="error"/>
				</div>
			</li>
			<li>
				<label for="host">Folder name:</label>
				<div>
					<input id="host" type="text" value="INBOX" disabled="disabled"/>
				</div>
			</li>
			<li>
				<label for="fetch">Fetch interval:</label>
				<div>
					<input id="fetch" type="text" value="60000" disabled="disabled"/>
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
	$('#facilitator').validate();
	$('#facilitator #pass').rules("add",{
		minlength: 6
	});
</script>
