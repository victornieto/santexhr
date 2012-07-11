<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<style type="text/css">
    th.name {
        width: 60%;
    }

    th.genre {
        width: 30%;
    }

    th.action {
       width: 10%;
    }
</style>

<div id="content">
	
	<form:form commandName="category" action="update" cssClass="group" cssStyle="clear:both;">
		<form:hidden path="id"/>
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
	<br/>
	
	<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
	<h1 style="display: inline;">
		<img src="<c:url value='/img/settings/new-test.png' />" />
		<a id="addQuestionLink">Add Question of Type:</a>
	</h1>
	<select id="questionType" style="position: relative; top: -2px; left: 4px;">
		<option value="" selected="selected"></option>
		<option value="<c:url value='addEssayQuestion?c=${category.id}'/>">Essay</option>
		<option value="<c:url value='addCodeQuestion?c=${category.id}'/>">Code</option>
		<option value="<c:url value='addMultipleChoiceQuestion?c=${category.id}'/>">Multiple Choice</option>
	</select>
	<script type="text/javascript">
		$('#questionType').change( function() {
			$('#addQuestionLink').attr('href', $(this).val());
		});
	</script>
	</security:authorize>
	<c:if test="${fn:length(category.questions) > 0}">
	<table class="sortable" id="openapplicant_questions_list">
		<thead>
			<tr>
				<th class="name">Name</th>
				<th class="genre">Type</th>
                <th class="action">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="q" items="${category.questions}">
			<tr>
				<td id="q${q.id}">
					<a href="<c:out value='question?c=${category.id}&q=${q.artifactId}'/>" >
						<c:out value="${tt:abbreviate(q.name)}"/>								
					</a>
				</td>
				<td><c:out value="${q['class'].simpleName}" /></td>
                <td><a id="delQ_${q.artifactId}">
                    <img src="<c:url value="/img/delete.png" />" style="width: 16px; height: 16px" alt="Delete" />
                </a></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</c:if>
	<br/><br/>
	<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
	<h1 style="display: inline;">
		<img src="<c:url value='/img/settings/new-test.png' />" />
		<a href="add?c=${category.id}">Add Subcategory</a>
	</h1>
	</security:authorize>	
	<c:if test="${fn:length(category.children) > 0}">
	<table class="sortable" id="openapplicant_categories_list">
		<thead>
			<tr>
				<th class="name">Name</th>
				<th class="genre">Questions Quantity</th>
                <th class="action">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="c" items="${category.children}">
			<tr>
				<td>
					<a href="<c:url value='/admin/category/view?c=${c.id}'/>">
						<c:out value="${tt:abbreviateTo(c.name, 45)}"/>
					</a>
				</td>
				<td><c:out value="${fn:length(c.allQuestions)}" /></td>
                <td><a id="delC_${c.id}">
                    <img src="<c:url value="/img/delete.png" />" style="width: 16px; height: 16px" alt="Delete" />
                </a></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>	
	</c:if>
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
<script type="text/javascript" src="<c:url value="/dwr/interface/CategoryController.js"/>"></script>
<script type="text/javascript">
	oltk.include('openapplicant/admin/helper/jquery.tablesorter.min.js');
	var categoryId = '${category.id}';
    var contextPath = '<%=request.getContextPath()%>';
	oltk.include('jquery/ui/minified/jquery.ui.all.min.js');
	oltk.include('openapplicant/questions_category_util.js');
</script>