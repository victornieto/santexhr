<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<style type="text/css">
    th.name {
        width: 22.5%;
    }

    th.genre {
        width: 15%;
    }

    th.desc {
        width: 35%;
    }

    th.state {
        width: 5%;
    }

    th.perc_complete {
        width: 12.5%;
    }

    th.col_action {
        width: 10%;
    }

    td.perc_complete {
        text-align: right;
    }

    td.exam_completed {
        background-color: #90EE90 !important;
    }

    .action {
        display:inline;
        margin-right:10px;
    }
</style>

<div id="content">
    <security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
        <div>
            <h1 class="action">
                <img src="<c:url value='/img/settings/new-test.png' />" />
                <a href="<c:url value='/admin/jobOpenings/add' />">Create New Job Opening</a>
            </h1>
        </div>
    </security:authorize>
    <display:table name="jobOpenings" id="jobOpening" export="true" pagesize="10" htmlId="job_opening_list"
                   class="sortable" requestURI="${requestScope['javax.servlet.forward.request_uri']}" sort="list">
        <display:column title="Job Position" headerClass="header" sortable="true" sortProperty="jobPosition.title">
            <a href="view?jo=${jobOpening.id}">${jobOpening.jobPosition.title}</a>
        </display:column>
        <display:column title="Client" headerClass="header" sortable="true" sortProperty="client">
            ${jobOpening.client}
        </display:column>
        <display:column title="Finish Date" headerClass="header" sortable="true" sortProperty="finishDate">
            <fmt:formatDate value="${jobOpening.finishDate}" dateStyle="MEDIUM"/>
        </display:column>
        <display:column title="Start Date" headerClass="header" sortable="true" sortProperty="startDate">
            <fmt:formatDate value="${jobOpening.startDate}" dateStyle="MEDIUM"/>
        </display:column>
        <c:if test="${!(jobOpeningsSidebar eq null && viewActive eq null && viewArchived eq null)}">
            <display:column title="State" headerClass="header" sortable="true" sortProperty="status">
                ${tt:humanize(jobOpening.status)}
            </display:column>
        </c:if>
    </display:table>
</div>
</script>
