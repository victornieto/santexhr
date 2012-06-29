<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>

<style type="text/css">
	#container {
		min-height: 400px;
	}

	#openapplicant_login {
		width: 25em;
		margin: 4em auto 20em;
	}
	
	#openapplicant_login .support {
		padding-top:25px;
		text-align: center;
	}
</style>

<script type="text/javascript">

	function prepareLogin() {
		// Restore the user's ajax history if they were redirected here.
		var i = top.location.href.indexOf('#');
		var hash = i >= 0 ? top.location.href.substring(i) : '';
		document.login_form.location_hash.value = hash;
		
		// Set the user's time zone offset
		var offset = new Date().getTimezoneOffset();   
		document.login_form.timezone_offset.value =  offset * (-1);     
	};
</script>

<div id="openapplicant_login">
	<c:if test="${not empty param.login_error }">
		<div class="error">Incorrect email or password. Please try again.</div>
	</c:if>
	<form name="login_form" method="POST"
		action="<c:url value='/j_spring_security_check'/>" 
		onsubmit="prepareLogin(); return true;">
		<input type="hidden" name="location_hash" />
		<input type="hidden" name="timezone_offset"/>
		<ul class="small">
			<li>
				<label>Email:</label>
				<div>
					<input type="text" name="j_username" value="<c:if test='${not empty param.login_error }'><c:out value='${SPRING_SECURITY_LAST_USERNAME }'/></c:if>" />
				</div>
			</li>
			<li>
				<label>Password:</label>
				<div>
					<input type="password" name="j_password" />
				</div>
			</li>
			<li class="save">
				<div>
					<input type="submit" value="Login" />
				</div>
			</li>
			<li>
				<div>
					<a href="<c:url value="/admin/forgotPassword/index"/>">
						I cannot access my account
					</a>
				</div>
			</li>
		</ul>		
	</form>
</div>