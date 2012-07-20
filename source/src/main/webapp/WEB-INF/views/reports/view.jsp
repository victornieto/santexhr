<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>


<script>
	oltk.include('jquery/ui/ui.datepicker.js');
    <c:choose>
	    <c:when test="${!(fn:substring(pageContext.response.locale,0,2) eq 'en')}">
	    oltk.include('jquery/ui/i18n/ui.datepicker-${fn:substring(pageContext.response.locale,0,2)}.js');
	    $(function() {
			$( "#datepickerFromDate" ).datepicker($.datepicker.regional['${fn:substring(pageContext.response.locale,0,2)}']);
			$( "#datepickerToDate" ).datepicker($.datepicker.regional['${fn:substring(pageContext.response.locale,0,2)}']);
		});
	    </c:when>
	    <c:otherwise>
	    $(function() {
			$( "#datepickerFromDate" ).datepicker();
			$( "#datepickerToDate" ).datepicker();
		});
	    </c:otherwise>
    </c:choose>	
	
</script>

<div id="content">
	<div class="row">
		<div class="group left" id="openapplicant_reports_candidateCount" style="width: 420px;">
			<h1>Filtering reports by periods</h1>
			<form:form action="reportCountCandidateByStatus" commandName="report">				
				<ul class="half left">
					<li>
						<label>From date:</label>
						<div>
							<form:input path="periodFrom" id="datepickerFromDate"/>
							<form:errors path="periodFrom" cssClass="error"/>
						</div>
					</li>				
					
				</ul>
				<ul class="half right">
					<li>
						<label>To date:</label>
						<div>
							<form:input path="periodTo" id="datepickerToDate"/>
							<form:errors path="periodTo" cssClass="error"/>
						</div>
					</li>
					
				</ul>
				<li class="actions">
					<input type="submit" class="submit" value="Search"/>
				</li>
			</form:form>
		</div>	
	</div>  
	<c:if test="${fn:length(result) > 0}">
		<div class="group" style="width: 400px;">
			<c:forEach items="${result}" var="report">
				<div id="report_" >					
						<pre>${report}</pre>						
				</div>
			</c:forEach>		
		</div>
	</c:if>	
</div>
