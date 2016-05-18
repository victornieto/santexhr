<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<div class="group">
    <h3 class="${jobOpeningsSidebar ? 'selected':''}">
        <img src="<c:url value='/img/sidebar/candidate.gif' />" />
        <a href="<c:url value='all' />">Job Openings</a>
    </h3>
    <div class="status_folders">
        <div class="${viewActive ? 'selected' : ''}">
			<span class="toggler">
				<img src="<c:url value="/img/open.gif"/>"/>
			</span>
            <a href="<c:url value="active"/>">Active</a>
        </div>
        <ul class="toggle_target">
            <c:forEach var="status" items="${activeStatuses}">
                <li class="folder ${view_status == status ? 'selected' : ''}">
                    <a href="<c:url value="viewstatus?status=${status}"/>">
                        <c:out value="${tt:humanize(status)}"/>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
    <div class="status_folders">
        <div class="${viewArchived ? 'selected' : ''}">
			<span class="toggler">
				<img src="<c:url value="/img/open.gif"/>"/>
			</span>
            <a href="<c:url value="archived"/>">Archived</a>
        </div>
        <ul class="toggle_target">
            <c:forEach var="status" items="${archivedStatuses}">
                <li class="folder ${view_status == status ? 'selected' : '' }">
                    <a href="<c:url value="viewstatus?status=${status}"/>">
                        <c:out value="${tt:humanize(status)}"/>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<div class="group">
    <h3>
        <img src="<c:url value='/img/sidebar/candidate.gif' />" />
        <a href="<c:url value='/admin/candidates/all' />">Candidates</a>
    </h3>
</div>