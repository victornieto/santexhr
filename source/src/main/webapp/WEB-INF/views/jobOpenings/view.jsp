<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<style type="text/css">
    .noneDisplay {
        display: none;
    }
</style>
<div id="content">
    <form:form modelAttribute="jobOpening" action="update" id="formId">
        <c:if test="${!(jobOpening.id eq null)}">
        <form:hidden path="id"/>
        </c:if>
        <ul class="half left">
            <li>
                <label for="jobPosition">Job Position:</label>
                <div>
                    <form:select id="jobPosition" path="jobPosition" items="${jobPositions}" itemLabel="title"/>
                </div>
            </li>
            <li>
                <label for="finishDate">Finish Date:</label>
                <div>
                    <form:input id="finishDate" path="finishDate"/>
                </div>
            </li>
            <li>
                <label for="client">Client:</label>
                <div>
                    <form:input id="client" path="client"/>
                </div>
            </li>
            <li>
                <label for="description">Description:</label>
                <div>
                    <form:textarea id="description" path="description"/>
                </div>
            </li>
            <li>
                <label for="applicants">Applicants:</label>
                <div>
                    <div id="applicantsSection" style="float: left !important; width: 82% !important;;">
                    <tiles:insertAttribute name="applicants"/>
                    </div>
                    <a id="selectCandidates">
                        <img src="<c:url value='/img/add.png' />" style="height: 16px; width: 16px" alt="Add"/>
                    </a>
                    <a id="deleteApplicants">
                        <img src="<c:url value="/img/delete.png" />" style="width: 16px; height: 16px" alt="Delete" />
                    </a>
                </div>
            </li>
            <li class="actions">
                <input id="submit" type="submit" class="submit" name="save" value="Save"/>
                <input type="submit" class="submit" name="cancel" value="Cancel"/>
            </li>
        </ul>
        <c:if test="${!(success eq null)}">
         <div class="row">
            <span class="success">The job opening was saved successfully!</span>
         </div>
        </c:if>
    </form:form>
</div>
<div class="noneDisplay">
    <div id="modalContainer">
        <div id="candidatesList"></div>
        <input type="button" value="Add selected" id="addCandidates"/>
    </div>
</div>

<script type="text/javascript">
    oltk.include('jquery/ui/ui.core.js');
    oltk.include('jquery/ui/ui.dialog.js');
    oltk.include('jquery/ui/ui.datepicker.js');
    <c:choose><c:when test="${!(fn:substring(pageContext.response.locale,0,2) eq 'en')}">oltk.include('jquery/ui/i18n/ui.datepicker-${fn:substring(pageContext.response.locale,0,2)}.js');
    $('#finishDate').datepicker($.datepicker.regional['${fn:substring(pageContext.response.locale,0,2)}']);</c:when><c:otherwise>$('#finishDate').datepicker();</c:otherwise></c:choose>
    $(document).ready(function() {
        $('#modalContainer').dialog({autoOpen:false});
    });

    $('#submit').click(function() {
        var data = $('#formId').serializeArray();
        var ats = new Array();
        $('option[id^="applicants"]').each(function() {
            data.push({
                name:'applicants',
                value:$(this)[0].value
            });
        });
        data.push({
            name:'submit',
            value:'submit'
        })
        $.post('<c:url value="update"/>',$.param(data), function (data, textStatus, jqXHR) {
            document.documentElement.innerHTML = data;
        });
        return false;
    });

    $('#selectCandidates').click(function() {
        var aap = new Array();
        $('option[id^="applicants"]').each(function() {
            aap.push($(this)[0].value);
        });
        $.ajax({
            type:'POST',
            url: '<c:url value="listCandidatesForJobOpening"/>',
            data:{
                'aap':aap
            },
            success:function(html) {
                $('#candidatesList').html(html);
                $('#modalContainer').dialog('open');
            }
        });
        return false;
    });

    $('#addCandidates').click(function() {
        var aap = new Array();
        $('option[id^="applicants"]').each(function() {
            aap.push($(this)[0].value);
        });
        var ata = new Array();
        $(':checked[id^="candidates"]').each(function() {
            ata.push($(this)[0].value);
        });
        $.ajax({
            type:'POST',
            url: '<c:url value="addCandidatesToJobOpening"/>',
            data:{
                'aap':aap,
                'ata':ata},
            success:function(html) {
                $('#applicantsSection').html(html);
                $('#modalContainer').dialog('close');
            }
        });
        return false;
    });

    $('#deleteApplicants').click(function() {
        var aap = new Array();
        $('option[id^="applicants"]').each(function() {
            aap.push($(this)[0].value);
        });
        var atd = new Array();
        $(':selected[id^="applicants"]').each(function() {
            atd.push($(this)[0].value);
        });
        $.ajax({
            type:'POST',
            url: '<c:url value="deleteApplicantsFromJobOpening"/>',
            data:{
                'aap':aap,
                'atd':atd},
            success:function(html) {
                $('#applicantsSection').html(html);
                $('#modalContainer').dialog('close');
            }
        });
        return false;
    });
</script>