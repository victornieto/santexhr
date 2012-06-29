<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<div id="content">
	<h1>
	<c:choose>
		<c:when test="${!(parentCategoryId eq null) && parentCategoryId > 0}">
		New Subcategory
		</c:when>
		<c:otherwise>
		New Category
		</c:otherwise>
	</c:choose>
	</h1>
	<form:form commandName="category" action="doAdd" cssClass="group">
		<c:if test="${!(parentCategoryId eq null) && parentCategoryId > 0}">
			<input type="hidden" id="c" name="c" value="${parentCategoryId}" />
		</c:if>
		<ul>
			<li>
				<label>Name:</label>
				<div>
					<form:input path="name"/>
					<form:errors cssClass="error" path="name"/>
				</div>
			</li>
			<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
			<li class="actions">
				<input type="submit" value="Save" />
			</li>
			</security:authorize>
		</ul>
	</form:form>	
</div>
<security:authorize ifAllGranted="<%=ROLE_HR.name()%>">
<script type="text/javascript">
	jQuery(function() {
		jQuery('#content :input').each(function() {
			this.disabled=true;
		});
	});
</script>
</security:authorize>
