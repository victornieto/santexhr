<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<p>
	Account Creation Index Page
</p>
<form method="POST" action="<c:url value="sendEmail"/>">
	<ul class="small">
		<li>
			<label>Email:</label>
			<div>
				<input type="text" name="email" />
			</div>
			<c:if test="${not empty error }">
				<div class="error">
					<c:out value="${error}"/>
				</div>
			</c:if>
		</li>
		<li class="save">
			<div>
				<input type="submit" value="Submit"/>
			</div>
		</li>
	</ul>
</form>
