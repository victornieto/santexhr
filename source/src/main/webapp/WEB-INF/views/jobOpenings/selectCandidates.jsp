<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:checkboxes id="candidates" items="${candidates}" path="candidates" itemValue="id" itemLabel="name"/>