<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div id="content">
	<h1>Email</h1>
	<div class="group">
		<ul id="menu">
			<li>
				<a href="<c:url value='preferences' />">
					<img src="<c:url value='/img/settings/prefs.jpg' />" />
				</a>
				<h2>Preferences:</h2>
				<p>Manage daily reports and email forwarding.</p>
			</li>
			<li>
				<a href="<c:url value='templates' />">
					<img src="<c:url value='/img/settings/templates.jpg' />" />
				</a>
				<h2>Templates:</h2>
				<p>Customized templating editor for emails sent to candidates.</p>
			</li>

			<li>
				<a href="<c:url value='smtp' />">
					<img src="<c:url value='/img/settings/prefs.jpg' />" />
				</a>
				<h2>SMTP:</h2>
				<p>Setup your mail server configurations.</p>
			</li>
			<li>
				<a href="<c:url value='emailConnect' />">
					<img src="<c:url value='/img/settings/email.jpg' />" />
				</a>
				<h2>Email Connector:</h2>
				<p>Control your email server's settings.</p>
			</li>
		</ul>

	</div>
</div>