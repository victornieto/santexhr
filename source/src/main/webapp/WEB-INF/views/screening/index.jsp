<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div id="content">
	<h1>Screening</h1>
	<div class="group">
		<ul id="menu">
			<li>
				<a href="<c:url value='preferences' />">
					<img src="<c:url value='/img/settings/prefs.jpg' />" />
				</a>
				<h2>Preferences:</h2>
				<p>Automatic exam invitations, rejections, and resume requests.</p>
			</li>
			<li>
				<a href="<c:url value='keywords' />">
					<img src="<c:url value='/img/settings/keywords.jpg' />" />
				</a>
				<h2>Keywords:</h2>
				<p>Required, preferred, and optional keyword filters to be used during the resume screening process.</p>
			</li>
		</ul>
	</div>
</div>