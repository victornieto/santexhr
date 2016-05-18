<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<style type="text/css">
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
                <a href="<c:url value='/admin/jobPositions/view' />">Create New Job Position</a>
            </h1>
        </div>
    </security:authorize>
    <table class="sortable" id="openapplicant_job_positions_list">
        <thead>
        <tr>
            <th class="name">Name</th>
            <th class="col_action">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="jp" items="${jobPositions}">
            <tr>
                <td>
                    <a href="<c:url value='/admin/jobPositions/view?jp=${jp.id}'/>">
                        <c:out value="${tt:abbreviateTo(jp, 45)}"/>
                    </a>
                </td>
                <td>
                    <a id="del_JP${jp.id}">
                        <img src="<c:url value='/img/delete.png' />" alt="Delete" style="width: 16px; height: 16px"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${!(error eq null)}">
        <span class="error"><c:out value="${error}"/></span>
    </c:if>
</div>

<script type="text/javascript">
    oltk.include('jquery/ui/minified/jquery.ui.all.min.js');
    oltk.include('openapplicant/admin/helper/jquery.tablesorter.min.js');

    $.deleteJobPosition = function(jobPositionId) {
        $('<div></div>')
                .html('Are you sure you want to delete this exam definition?')
                .dialog(
                {
                    modal: true,
                    resizable: false,
                    height: 120,
                    buttons: {
                        Yes:function() {
                            $.ajax({
                                type:'POST',
                                url: '<c:url value="deleteJobPosition"/>',
                                data:{jp:jobPositionId},
                                success:function(html) {
                                    document.documentElement.innerHTML = html;
                                    applyOrder();
                                    assignEvents();
                                }
                            });
                            $(this).dialog('close');
                        },
                        No:function() {
                            $(this).dialog('close');
                        }
                    }
                });
    };

    var assignEvents = function() {
        $('a[id^=del_JP]').each(function() {
            $(this).click(function() {
                $.deleteJobPosition($(this).attr('id').substr(6));
            });
        });
    };

    var applyOrder = function() {
        if($("#openapplicant_job_positions_list").find("tr").length < 2)
            return;

        $("#openapplicant_job_positions_list").tablesorter({
            widgets: ['zebra'] //alternating row styles
        });
    };

    $(document).ready(function() {
        applyOrder();
        assignEvents();
    });
</script>
