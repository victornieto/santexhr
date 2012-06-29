<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
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
			<a href="<c:url value='/admin/examDefinition/add' />">Create New Exam Definition</a>
		</h1>
		<h1 class="action">
			<img src="<c:url value="/img/settings/settings.png"/>"/>
			<a href="<c:url value="/admin/examDefinitions/site"/>">Configure Portal</a>
		</h1>
	</div>
	</security:authorize>
	<table class="sortable" id="openapplicant_exam_definitions_list">
		<thead>
			<tr>
				<th class="name">Name</th>
				<th class="genre">Genre</th>	
				<th class="desc">Description</th>
				<th class="state">State</th>
				<th class="perc_complete">% Completed</th>
                <th class="col_action">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="ed" items="${examDefinitions}">
			<tr>
				<td>
					<a href="<c:url value='/admin/examDefinition/view?ed=${ed.artifactId}'/>">
						<c:out value="${tt:abbreviateTo(ed.name, 45)}"/>
					</a>
				</td>
				<td><c:out value="${ed.genre}" /></td>	
				<td><c:out value="${ed.description}" /></td>
				<td>
					<c:choose>
						<c:when test="${ed.active}">Active</c:when>
						<c:otherwise>Disabled</c:otherwise>
					</c:choose>
				</td>
				<td class="perc_complete${ed.totalPercentage == 100.0 ? ' exam_completed' : ''}">
					<c:out value="${ed.totalPercentage}" />
				</td>
                <td>
                    <a id="del_ED${ed.artifactId}">
                        <img src="<c:url value='/img/delete.png' />" alt="Delete" style="width: 16px; height: 16px"/>
                    </a>
                </td>
			</tr>
			</c:forEach>
		</tbody>
	</table>	
</div>

<script type="text/javascript">
    oltk.include('jquery/ui/minified/jquery.ui.all.min.js');
	oltk.include('openapplicant/admin/helper/jquery.tablesorter.min.js');

    $.deleteQuestion = function(examDefinitionId) {
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
                                url: '<c:url value="deleteExamDefinition"/>',
                                data:{ed:examDefinitionId},
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
        $('a[id^=del_ED]').each(function() {
            $(this).click(function() {
                $.deleteQuestion($(this).attr('id').substr(6));
            });
        });
    };

    var applyOrder = function() {
        if($("#openapplicant_exam_definitions_list").find("tr").length < 2)
            return;

        $("#openapplicant_exam_definitions_list").tablesorter({
            widgets: ['zebra'] //alternating row styles
        });
    };

    $(document).ready(function() {
        applyOrder();
        assignEvents();
    });
</script>
