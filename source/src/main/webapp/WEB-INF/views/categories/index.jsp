<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<style type="text/css">
	th.cat_name {
		width: 66%;
	}
	
	th.cat_question_quantity {
		width: 24%;
	}

    th.col_action {
        width: 10%;
    }
	
	th.question_name {
		width: 70%;
	}
	
	th.question_type {
		width: 20%;
	}
	
	.action {
		display:inline;
		margin-right:10px;
	}
</style>

<div id="content">
	<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
	<h1 style="display: inline;">
		<img src="<c:url value='/img/settings/new-test.png' />" />
		<a id="addQuestionLink">Add Question of Type:</a>
	</h1>
	<select id="questionType" style="position: relative; top: -2px; left: 4px;">
		<option value="" selected="selected"></option>
		<option value="<c:url value='/admin/category/addEssayQuestion'/>">Essay</option>
		<option value="<c:url value='/admin/category/addCodeQuestion'/>">Code</option>
		<option value="<c:url value='/admin/category/addMultipleChoiceQuestion'/>">Multiple Choice</option>
	</select>
	<script type="text/javascript">
		$('#questionType').change( function() {
			$('#addQuestionLink').attr('href', $(this).val());
		});
	</script>
	</security:authorize>
	<c:if test="${fn:length(questions) > 0}">
	<table class="sortable" id="openapplicant_questions_list">
		<thead>
			<tr>
				<th class="question_name">Name</th>
				<th class="question_type">Type</th>
                <th class="col_action">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="q" items="${questions}">
			<tr>
				<td id="q${q.id}">
					<a href="<c:url value='/admin/category/question?q=${q.artifactId}'/>" >
						<c:out value="${tt:abbreviate(q.name)}"/>								
					</a>
				</td>
				<td><c:out value="${q.class.simpleName}" /></td>
                <td><a id="delQ_${q.artifactId}">
                    <img src="<c:url value="/img/delete.png" />" style="width: 16px; height: 16px" alt="Delete" />
                </a></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>	<br/>
	</c:if>
	<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
	<div>
		<h1 class="action">
			<img src="<c:url value='/img/settings/new-test.png' />" />
			<a href="<c:url value='/admin/category/add' />">Create New Category</a>
		</h1>
	</div>
	</security:authorize>
	<table class="sortable" id="openapplicant_categories_list">
		<thead>
			<tr>
				<th class="cat_name">Name</th>
				<th class="cat_question_quantity">Questions Quantity</th>
                <th class="col_action">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="c" items="${categories}">
			<tr>
				<td>
					<a href="<c:url value='/admin/category/view?c=${c.id}'/>">
						<c:out value="${tt:abbreviateTo(c.name, 45)}"/>
					</a>
				</td>
				<td><c:out value="${fn:length(c.allQuestions)}" /></td>
                <td><a id="delC_${c.id}">
                    <img src="<c:url value="/img/delete.png" />" style="width: 16px; height: 16px" alt="delete" />
                </a></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<script type="text/javascript" src="<c:url value="/dwr/interface/CategoryController.js"/>"></script>
<script type="text/javascript">
	oltk.include('openapplicant/admin/helper/jquery.tablesorter.min.js');
	oltk.include('jquery/ui/minified/jquery.ui.all.min.js');
	categoryId = '';
    var contextPath = '<%=request.getContextPath()%>';
	oltk.include('openapplicant/questions_category_util.js');
</script>