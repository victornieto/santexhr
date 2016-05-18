<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_ADMIN" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR_MANAGER" %>
<style type="text/css">
	 @import "<c:url value='/css/layout/candidate_list.css'/>";
</style>

<script type="text/javascript">
	oltk.include('jquery/jquery.js');
	oltk.include('jquery/ui/ui.core.js');
	oltk.include('jquery/cluetip/jquery.cluetip.js');		
	
	$('#content').ready(function() {
		oltk.include('jquery/ui/ui.datepicker.js');
		

		/* TODO: JQuery UI's datepicker has a DST bug in JQuery UI 1.5.2.  The bug 
		   should no longer appear after we upgrade to JQuery UI 1.6 or later. */
		$("#search_dates").datepicker({
			rangeSelect:true
		});
	
		$('.candidate_name').cluetip({
			width:425,
			cluetipClass:'history',
			ajaxSettings: {
				type: 'POST'
			}
		});
	
		$('.tooltip').each( function() {
			var activation = $(this).hasClass('hover') ? 'hover' : 'click';
			$(this).cluetip({
				activation: activation,
				width:175,
				cluetipClass:'jtip',
				local:true,
				arrows:true,
				closeText: '<img src="<c:url value="/img/jquery_cluetip/close-gray.png"/>"/>'
			});
		});
	});
</script>
<div id="content" style="width: 728px;">
    <display:table export="true" name="candidates" id="c" htmlId="openapplicant_candidate_list" class="sortable" pagesize="10" keepStatus="true" requestURI="${requestScope['javax.servlet.forward.request_uri']}" sort="list">
        <display:column headerClass="header" title="Name" media="html" sortable="true">
            <a class="candidate_name" href="<c:url value='detail?id=${c.id}'/>" rel="<c:url value='history?id=${c.id}'/>" title="History: ${tt:abbreviateTo(c.name.fullName,37)}">
                <c:choose>
                    <c:when test="${!empty c.name.first}">
                        <span><c:out value="${tt:abbreviateTo(c.name.first,14)}"/> <strong><c:out value="${tt:abbreviateTo(c.name.last,14)}"/></strong></span>
                    </c:when>
                    <c:otherwise>
                        <span><i>Information Required</i> <img src="<c:url value='/img/candidate_icons/warning.png'/>"/></span>
                    </c:otherwise>
                </c:choose>
            </a>
        </display:column>
        <display:column title="Name" media="csv excel xml"  sortable="true">
            <c:choose>
                <c:when test="${!empty c.name.first}">
                    <c:out value="${c.name}"/>
                </c:when>
                <c:otherwise>
                    Information Required
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column headerClass="header" title="Date"  sortable="true">
            <fmt:formatDate value="${c.entityInfo.createdDate.time}" type="date" dateStyle="short" timeStyle="short" />
        </display:column>
        <display:column headerClass="header" title="Screen" class="numerical" media="html"  sortable="true">
            <c:choose>
                <c:when test="${!empty c.resume.screeningScore}">
                    <c:out value="${c.resume.screeningScore}"/>
                </c:when>
                <c:otherwise>
                    &mdash;
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column title="Screen" media="csv excel xml"  sortable="true">
            <c:choose>
                <c:when test="${!empty c.resume.screeningScore}">
                    <c:out value="${c.resume.screeningScore}"/>
                </c:when>
                <c:otherwise>
                    -
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column headerClass="header" title="Exam" class="numerical" media="html" sortable="true">
            <c:choose>
                <c:when test="${!empty c.lastSitting}">
                    <c:out value="${c.lastSitting.score}"/>
                </c:when>
                <c:otherwise>
                    &mdash;
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column title="Exam" media="csv excel xml" sortable="true">
            <c:choose>
                <c:when test="${!empty c.lastSitting}">
                    <c:out value="${c.lastSitting.score}"/>
                </c:when>
                <c:otherwise>
                    -
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column headerClass="header" title="Match" class="numerical" media="html" sortable="true">
            <c:choose>
                <c:when test="${!empty c.matchScore}">
                    <c:out value="${c.matchScore}"/>
                </c:when>
                <c:otherwise>
                    &mdash;
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column title="Match" media="csv excel xml" sortable="true">
            <c:choose>
                <c:when test="${!empty c.matchScore}">
                    <c:out value="${c.matchScore}"/>
                </c:when>
                <c:otherwise>
                    -
                </c:otherwise>
            </c:choose>
        </display:column>
        <display:column headerClass="header" title="Status" class="${c.status}" sortable="true">
            <c:out value="${tt:humanize(c.status)}"/>
        </display:column>
        <display:column media="html" headerClass="icon header" class="icon" title="<img src=\"${pageContext.request.contextPath}/img/table/phone.gif\" title=\"Sort by Contact Info\"/>" sortable="true">
            <c:if test="${!empty c.cellPhoneNumber.number || !empty c.homePhoneNumber.number || !empty c.workPhoneNumber.number}">
                <a class="tooltip hover" rel="#phone_tooltip_${c.id}" title="<c:out value="${tt:abbreviateTo(c.name.first, 15)}"/>">
                    <img src="<c:url value='/img/table/phone.gif'/>"/>
                </a>
            </c:if>
            <div style="display:none" id="phone_tooltip_${c.id}">
                <ul>
                    <c:if test="${!empty c.cellPhoneNumber.number}">
                        <li>
                            (c) <c:out value="${c.cellPhoneNumber.number}"/>
                        </li>
                    </c:if>
                    <c:if test="${!empty c.homePhoneNumber.number}">
                        <li>
                            (h) <c:out value="${c.homePhoneNumber.number}"/>
                        </li>
                    </c:if>
                    <c:if test="${!empty c.workPhoneNumber.number}">
                        <li>
                            (w) <c:out value="${c.workPhoneNumber.number}"/>
                        </li>
                    </c:if>
                </ul>
            </div>
        </display:column>
        <display:column media="html" headerClass="icon header" class="icon" title="<img src=\"${pageContext.request.contextPath}/img/table/email.gif\" title=\"Sort by Email\"/>" sortable="true">
            <c:if test="${!empty c.email}">
                <a href='mailto:<c:out value="${c.email}"/>'>
                    <img src="<c:url value='/img/table/email.gif'/>" title="Send Email to ${tt:abbreviateTo(c.name.first,15)} at ${tt:abbreviateTo(c.email,30)}"/>
                </a>
            </c:if>
        </display:column>
        <display:column media="html" headerClass="icon header" class="icon" title="<img src=\"${pageContext.request.contextPath}/img/table/resume.gif\" title=\"Sort by Resume\"/>" sortable="true">
            <c:if test="${!empty c.resume}">
                <a href="<c:url value='/admin/file?guid=${c.resume.guid}' />" target="_blank">
                    <img src="<c:url value='/img/table/resume.gif'/>" title="Download ${tt:abbreviateTo(c.name.first,15)}'s Resume"/>
                </a>
            </c:if>
        </display:column>
        <security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
        <display:column media="html" headerClass="icon header" class="icon" title="<img src=\"${pageContext.request.contextPath}/img/table/analytics.gif\" title=\"Sort by Exam Results\"/>" sortable="true">
            <c:if test="${!empty c.lastSitting}">
                <a href="<c:url value='/admin/results/exam?s=${c.lastSitting.id}' />">
                    <img src="<c:url value='/img/table/analytics.gif'/>" title="${tt:abbreviateTo(c.name.first,15)}'s Exam Results"/>
                </a>
            </c:if>
        </display:column>
        </security:authorize>
        <security:authorize ifAnyGranted="<%=ROLE_HR_MANAGER.name() + \",\" + ROLE_HR.name() %>">
	        <display:column media="html" headerClass="icon header" class="icon" title="<img src=\"${pageContext.request.contextPath}/img/table/packet.gif\" title=\"Sort by Printable Packet\"/>">
	            <a href="<c:url value='/admin/report.pdf?candidate=${c.id}' />">
	                <img src="<c:url value='/img/table/packet.gif'/>" title="${tt:abbreviateTo(c.name.first,15)}'s Packet"/>
	            </a>
	        </display:column>
        </security:authorize>
        <display:column media="html" headerClass="icon header" class="icon" title="<img src=\"${pageContext.request.contextPath}/img/table/archive.gif\" title=\"Sort by Archive\"/>">
            <a class="tooltip" rel="#tooltip_${c.id }" title="Move to">
                <img src="<c:url value='/img/table/archive.gif'/>"/>
            </a>
            <div style="display:none" id="tooltip_${c.id}">
                <ul>
                    <c:if test="${c.archived}">
                        <li>
                            <a href="<c:url value="updateStatus?id=${c.id}&status=${c.lastActiveStatus}&view_status=${view_status}&search_id=${searchId}"/>">
                                <c:out value="${tt:humanize(c.lastActiveStatus)}"/>
                            </a>
                        </li>
                    </c:if>
                    <c:forEach var="status" items="${archivedStatuses}">
                        <c:if test="${c.status != status}">
                            <li>
                                <a href="<c:url value="updateStatus?id=${c.id}&status=${status}&view_status=${view_status}&search_id=${searchId}"/>">
                                    <c:out value="${tt:humanize(status)}"/>
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </display:column>
        <security:authorize ifAnyGranted="<%=(ROLE_ADMIN.name() + \",\" + ROLE_HR_MANAGER.name())%>">
        <display:column media="html" headerClass="icon header" class="icon" title="<img src=\"${pageContext.request.contextPath}/img/table/notes.png\" title=\"Notes\"/>">
            <a href="<c:url value='/admin/candidates/notes?candidate=${c.id}' />">
                <img src="<c:url value='/img/table/notes.png'/>" title="${tt:abbreviateTo(c.name.first,15)}'s Notes"/>
            </a>
        </display:column>
        </security:authorize>
    </display:table>
</div>
