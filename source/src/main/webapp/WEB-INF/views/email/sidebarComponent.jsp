<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<ul>
	<li class="preferences ${preferencesSidebar?'selected':''}">
		<a href="<c:url value='preferences' />">
			Preferences
		</a>
	</li>
	<li class="templates ${templatesSidebar?'selected':''}">
		<a href="<c:url value='templates' />">
			Templates
		</a>
		<c:if test="${templates != null}">
			<ul class="templates">
			<c:forEach var="t" items="${templates}">
				<li class="${t.id eq template.id ? 'selected':''}">
					<a href="<c:url value='template?id=${t.id}' />" >
						<c:out value="${t.name}" />
					</a>
				</li>
			</c:forEach>
			</ul>
		</c:if>
	</li>

	<li class="preferences ${smtpSidebar?'selected':''}">
		<a href="<c:url value='smtp' />">
			SMTP
		</a>
	</li>
	<li class="email ${emailConnectSidebar?'selected':''}">
		<a href="<c:url value='emailConnect' />">
			Email Connector
		</a>
	</li>

</ul>