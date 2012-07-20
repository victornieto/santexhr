<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<div id="content">

    <form:form action="update" commandName="jobPosition">
        <form:hidden path="id"/>
        <div class="row">
            <ul class="half left">
                <li>
                    <label>Name:</label>
                    <div>
                        <form:input path="name"/>
                        <form:errors path="name" cssClass="error"/>
                    </div>
                </li>
                <li>
                    <label>Seniorities:</label>
                    <div>
                        <form:checkboxes path="seniorities" items="${seniorities}" />
                    </div>
                </li>
                <li class="actions">
                    <input type="submit" class="submit" name="save" value="Save"/>
                    <input type="submit" class="submit" name="cancel" value="Cancel"/>
                </li>
            </ul>
        </div>
        <c:if test="${!(success eq null)}">
        <div class="success">
            <span class="">The Job Position was created successfully!</span>
        </div>
        </c:if>
    </form:form>

    <security:authorize ifAllGranted="<%=ROLE_HR.name()%>">
        <script type="text/javascript">
            $('#content input, #content textarea, #content select').each(function() {
                this.disabled=true;
            });
        </script>
    </security:authorize>
</div>