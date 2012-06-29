<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>

<style type="text/css">
    th.category {
        width: 35%;
    }

    th.percentage {
        width: 55%;
    }

    th.col_action {
        width: 10%;
    }
</style>

<div id="content">

	<form:form commandName="examDefinition" action="update" cssClass="group" cssStyle="clear:both;" id="examDefinitionForm">
		<form:hidden path="artifactId"/>
		<ul>
			<li>
				<label>Name:</label>
				<div>
					<form:input path="name"/>
					<form:errors cssClass="error" path="name"/>
				</div>
			</li>
			<li>
				<label>Genre:</label>
				<div>
					<form:input path="genre"/>
					<form:errors cssClass="error" path="genre"/>
				</div>
			</li>
			<li>
				<label>Description:</label>
				<div>
					<form:textarea path="description"/>
					<form:errors cssClass="error" path="description"/>
				</div>
			</li>
			<li>
				<label>Exam Definition Status:</label>
				<div>
					<label>
					    <form:radiobutton path="active" value="true"/> Enabled
				    </label>
					<label>
					    <form:radiobutton path="active" value="false"/> Disabled
				    </label>
				</div>
			</li>
			<li>
				<label>Exam's Number of Questions:</label>
				<div>
					<form:input path="numberOfQuestionsWanted" id="numberOfQuestionsWanted"/>
					<form:errors cssClass="error" path="numberOfQuestionsWanted"/>
				</div>
			</li>
		</ul>
        <a id="addCP" style="float: right; margin-top: 0.35em; margin-bottom: 0.35em;">
            <img src="<c:url value="/img/add.png"/>" alt="Add" style="width: 16px; height: 16px;"/>
        </a>
        <table class="sortable" id="categories_perc_list">
            <thead>
            <tr>
                <th class="category">Category</th>
                <th class="percentage">Percentage</th>
                <th class="col_action">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="cp" items="${examDefinition.categoriesPercentage}" varStatus="row">
                <tr>
                    <td>
                        <form:select path="categoriesPercentage[${row.index}].category">
                            <c:forEach var="c" items="${categories}">
                                <form:option
                                        value="${c}">${c.name}</form:option>
                            </c:forEach>
                        </form:select>
                        <c:if test="${!(categoriesPercentageError[cp.artifactId]['category'] eq null)}">
                            <span class="error"><c:out value="${categoriesPercentageError[cp.artifactId]['category']}"/></span>
                        </c:if>
                    </td>
                    <td>
                        <form:input id="perc_${cp.artifactId}" path="categoriesPercentage[${row.index}].percentage"/>
                        <c:if test="${!(categoriesPercentageError[cp.artifactId]['percentage'] eq null)}">
                            <br/><span class="error"><c:out value="${categoriesPercentageError[cp.artifactId]['percentage']}"/></span>
                        </c:if>
                        <form:hidden path="categoriesPercentage[${row.index}].artifactId" />
                    </td>
                    <td>
                        <a id="delCP_${cp.artifactId}">
                            <img src="<c:url value="/img/delete.png"/>" alt="Delete" style="width: 16px; height: 16px;"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <security:authorize ifNotGranted="<%=ROLE_HR.name()%>">
            <li class="actions">
                <input type="submit" value="Save" />
            </li>
        </security:authorize>
	</form:form>
    <c:if test="${!(categoryError eq null)}">
    <span class="error"><c:out value="${categoryError}"/></span>
    </c:if>

</div>
<security:authorize ifAllGranted="<%=ROLE_HR.name()%>">
<script type="text/javascript">
	jQuery(function() {
		jQuery('#content :input').each(function() {
			this.disabled=true;
		});
	});
</script>
</security:authorize>

<script type="text/javascript">
    oltk.include('jquery/ui/jquery.ui.all.js');
	oltk.include('jquery/validate/jquery.validate.js');

    var assignConstraints = function() {
        $('#content form').validate();
        $('#content #numberOfQuestionsWanted').rules("add",{
            min: 1, max:200
        });
        //$('#content #numberOfQuestionsWanted').autoNumeric({mDec: 0, vMax: '200'});
    };

    var assignEvents = function() {
        $('a[id^=delCP_]').each(function() {
            $(this).click(function() {
                var cp = $(this).attr('id').substr(6);
                var formArray = $('#examDefinitionForm').serializeArray();
                formArray.push({
                    name: 'cp',
                    value: cp
                });
                $('<div></div>')
                        .html('Are you sure you want to delete this registry?')
                        .dialog({
                            height: 100,
                            width: 306,
                            modal: true,
                            buttons: {
                                Yes: function() {
                                    $.ajax({
                                        url: 'deleteCategoryPercentage',
                                        type: 'POST',
                                        data: $.param(formArray),
                                        success: function(html) {
                                            document.documentElement.innerHTML = html;
                                            assignConstraints();
                                            assignEvents();
                                        }
                                    });
                                    $(this).dialog('close');
                                },
                                No: function() {
                                    $(this).dialog('close');
                                }
                            }
                        });
            });
        });
        $('#addCP').click(function() {
            var formStr = $('#examDefinitionForm').serialize();
            $.ajax({
                url: 'addCategoryPercentage',
                type: 'POST',
                data: formStr,
                success:function(html) {
                    document.documentElement.innerHTML = html;
                    assignConstraints();
                    assignEvents();
                }
            });
        });
    };
    $(document).ready(function() {
        assignConstraints();
        assignEvents();
    });
</script>