<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<p>
	Enter a new password.
</p>
<form action="doConfirm" method="POST">
	<ul class="small">
		<li>
			<label>Password</label>
			<div>
				<input type="password" name="password"/>
				<c:if test="${not empty passwordError}">
					<div class="error">
						<c:out value="${passwordError}"/>
					</div>
				</c:if>
			</div>
		</li>
		<li>
			<label>Confirm password:</label>
			<div>
				<input type="password" name="confirmPassword"/>
				<c:if test="${not empty confirmError}">
					<div class="error">
						<c:out value="${confirmError}"/>
					</div>
				</c:if>
			</div>
		</li>
		<li class="save">
			<div>
				<input type="hidden" name="userId" value="${userId}"/>
				<input type="submit" value="Submit"/>
			</div>
		</li>
	</ul>
</form>