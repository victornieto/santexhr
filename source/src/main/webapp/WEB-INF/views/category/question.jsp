<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<div id="content">

	<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
		<h1 style="display: inline;">
			<img src="<c:url value='/img/settings/new-test.png' />" alt=""/>
			<a id="addQuestionLink">Add Question of Type:</a>
		</h1>
		<select id="questionType" style="position: relative; top: -2px; left: 4px;">
			<c:choose>
			<c:when test="${category eq null}">
			<option value="<c:url value='addEssayQuestion'/>"
                    <c:if test="${question.class.simpleName eq 'EssayQuestion'}">selected="selected"</c:if>>Essay</option>
			<option value="<c:url value='addCodeQuestion'/>"
                    <c:if test="${question.class.simpleName eq 'CodeQuestion'}">selected="selected"</c:if>>Code</option>
			<option value="<c:url value='addMultipleChoiceQuestion'/>"
                    <c:if test="${question.class.simpleName eq 'MultipleChoiceQuestion'}">selected="selected"</c:if>>Multiple Choice</option>
			</c:when>
			<c:otherwise>
			<option value="<c:url value='addEssayQuestion?c=${category.id}'/>"
                    <c:if test="${question.class.simpleName eq 'EssayQuestion'}">selected="selected"</c:if>>Essay</option>
			<option value="<c:url value='addCodeQuestion?c=${category.id}'/>"
                    <c:if test="${question.class.simpleName eq 'CodeQuestion'}">selected="selected"</c:if>>Code</option>
			<option value="<c:url value='addMultipleChoiceQuestion?c=${category.id}'/>"
                    <c:if test="${question.class.simpleName eq 'MultipleChoiceQuestion'}">selected="selected"</c:if>>Multiple Choice</option>
			</c:otherwise>
			</c:choose>
		</select>
		<script type="text/javascript">
			$('#questionType').change( function() {
				$('#addQuestionLink').attr('href', $(this).val());
			});
		</script>
	</security:authorize>
	
	<!-- it's lame that a tile cant insert this into a spring form... i could have used the binding -->
	<form action="<tiles:insertAttribute name="action"/>" method="POST" class="group" style="clear:both;">
		<c:if test="${!(category eq null)}">
		<input type="hidden" name="c" value="<c:out value='${category.id}' />"/>
		</c:if>
		<input type="hidden" name="q" value="<c:out value='${question.artifactId}' />"/>
        <c:if test="${!(isNew eq null)}">
            <input type="hidden" name="n" value="true"/>
        </c:if>
		<ul>
			<li>
				<label>Name:</label>
				<div>
					<input type="text" name="name" id="name" value="<c:out value='${question.name}'/>"/>
				</div>
			</li>
			<li>
				<label>Prompt:</label>
				<div>
					<textarea name="prompt" id="prompt" class="required"><c:out value="${question.prompt}"/></textarea>
				</div>
			</li>
			<li>
				<tiles:insertAttribute name="questionKind"/>
			</li>
			<li>
				<label>Time allowed:</label>
				<div>
					<input type="text" id="timeAllowed" name="timeAllowed" value="<c:out value='${question.timeAllowed}'/>" class="half number"/>
					<br/>
					<span>(seconds, blank is untimed)</span>
				</div>
			</li>
			<security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
			<li class="actions">
				<input type="submit" class="submit" name="save" value="Save"/>
                <input type="submit" class="submit" name="cancel" value="Cancel"/>
			</li>
			</security:authorize>
		</ul>
	</form>

    <c:if test="${success}">
        <span class="success">
            The Question was created successfully!
        </span>
    </c:if>
	
	<security:authorize ifAllGranted="<%=ROLE_HR.name()%>">
		<script type="text/javascript">
			$('#content input, #content textarea, #content select').each(function() {
				this.disabled=true;
			});
		</script>
	</security:authorize>
	
	<script type="text/javascript">
		oltk.include('jquery/validate/jquery.validate.js');
		$('#content form').validate();
		$('#content #timeAllowed').rules("add",{
			min: 0
		});
	</script>
</div>