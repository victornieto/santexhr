<%@ taglib prefix="c"    uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page import="org.openapplicant.domain.Candidate"%>

<style type="text/css">
	 @import "<c:url value='/css/layout/quiz.css'/>";
</style>

<form:form id="openapplicant_quiz_info" commandName="candidate" action="info" cssClass="openapplicant_quiz_info" method="POST">
	<input type="hidden" name="examLink" value="${examLink.guid}"/> 
	<form:hidden path="id"/>
    <h2>Please enter your information:</h2>
    <ul class="small">
        <li>
            <label>First Name:</label>
           	<div>
            	<form:input path="name.first" cssClass="required"/>
				<form:errors path="name.first" cssClass="error"/>
            </div>
        </li>
        <li>
            <label>Last Name:</label>
            <div>
        	    <form:input path="name.last" cssClass="required"/>
				<form:errors path="name.last" cssClass="error"/>
        	</div>
        </li>
        <li>
            <label>Email:</label>
            <div>
				<form:input path="email" cssClass="required email"/>
				<form:errors path="email" cssClass="error"/>
			</div>
        </li>
        <c:choose>
        	<c:when test="${fn:length(examLink.exams) le 1}">
        		<input type="hidden" name="examArtifactId" value="${examLink.exams[0].artifactId}"/>
         	</c:when>
        	<c:otherwise>
	        	<li>
	            	 <label>Exam:</label>
	            	 <div>
		                <select name="examArtifactId">
		                <c:forEach var="exam" items="${examLink.exams}">		                
		                    <option value="${exam.artifactId}"><c:out value="${exam.name}"/></option>
		                </c:forEach>
		                </select>
		            </div>
	            </li>
        	</c:otherwise>
        </c:choose>
    </ul>        
	<a id="next" style="display:none;">continue</a>
</form:form>
<script type="text/javascript">	
	oltk.include('jquery/validate/jquery.validate.js');

	$('a#next').fadeIn('slow');
	
	$('#next').click( function() {
		$('#openapplicant_quiz_info').submit();
	});
	
	$('#openapplicant_quiz_info').validate();
</script>