<%@ taglib prefix="c"    uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>

<div id="profile" class="group">
	<h4>Candidate Info</h4>
	
	<div><c:out value="${tt:abbreviateTo(candidate.name.first,15)}"/> <c:out value="${tt:abbreviateTo(candidate.name.last,15)}"/></div>
	<div>Status: <c:out value="${tt:humanize(candidate.status)}"/></div>
	<div>Date: <fmt:formatDate value="${candidate.entityInfo.createdDate.time}" type="date" dateStyle="short"/></div>
	<div>Screen score: 
		<c:choose>
			<c:when test="${not empty candidate.resume.screeningScore}">
				<fmt:formatNumber value="${candidate.resume.screeningScore}"/>
			</c:when>
			<c:otherwise>
				&mdash;
			</c:otherwise>
		</c:choose>
	</div>
	<div>Exam score:
		<c:choose>
			<c:when test="${not empty sitting.score}">
				<c:out value="${sitting.score}"/>
			</c:when>
			<c:otherwise>
				&mdash;
			</c:otherwise>
		</c:choose>
	</div>
	<div>Match score: &mdash;</div>
	<br/>
	<c:if test="${not empty candidate.email}">
		<div>Email: <span title="<c:out value="${candidate.email}"/>"><c:out value="${tt:abbreviate(candidate.email)}"/></span></div>
	</c:if>
	<c:if test="${not empty candidate.cellPhoneNumber.number}">
		<div>(m) <c:out value="${candidate.cellPhoneNumber.number}"/></div>
	</c:if>
	<c:if test="${not empty candidate.workPhoneNumber.number}">
		<div>(o) <c:out value="${candidate.workPhoneNumber.number}"/></div>
	</c:if>
	<c:if test="${not empty candidate.homePhoneNumber.number}">
		<div>(h) <c:out value="${candidate.homePhoneNumber.number}"/></div>
	</c:if>
	<br/>		
	<div><c:out value="${candidate.address}"/></div>
</div>