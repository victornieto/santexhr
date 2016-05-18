<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<select id="applicants" name="applicants" multiple="multiple">
    <c:forEach items="${applicants}" varStatus="row" var="applicant">
        <option id="applicants${row.count}" value="${applicant.id}">${applicant.name}</option>
    </c:forEach>
</select>