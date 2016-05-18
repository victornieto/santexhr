<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld" %>

<label>Choices:</label>
<div>
	<ul class="multiple_choice">
        <a id="add_choice" style="float: right; margin-right: 0.85em; margin-bottom: 0.35em; margin-top: 0.35em;">
            <img src="<c:url value='/img/add.png' />" style="height: 16px; width: 16px" alt="Add"/>
        </a>
		<c:forEach var="choice" items="${question.choices}" varStatus="row">
		<li>
			<input type="radio" class="radio" name="answerIndex" id="answerIndex${row.index}" value="${row.index}"
				<c:if test="${row.index eq question.answerIndex}">checked="checked"</c:if> />
			<input type="text" class="prompt" name="choices" id="answerText${row.index}" value="${choice}"/>
            <a id="delete_choice_${row.index}">
                <img src="<c:url value='/img/delete.png' />" style="height: 16px; width: 16px" alt="Delete"/>
            </a>
        </li>
		</c:forEach>
		<c:if test="${not empty choicesValidError}">
		<li>
			<span class="error"><c:out value="${choicesValidError}"/></span>
		</li>
		</c:if>
		<%--
		<li class="save">
			<input type="submit" class="offset" value="add"/>
			<input type="submit" value="remove"/>	
		</li>
		--%>
        <script type="text/javascript">
            function onStart() {
                $('a[id^=delete_]').click(function() {
                    var choiceIndex = $(this).attr('id').substr(14);
                    var name = $('#name').val();
                    var prompt = $('#prompt').val();
                    var timeAllowed = $('#timeAllowed').val();
                    var choicesList = new Array();
                    $('input[id^=answerText]').each(function() {
                        choicesList.push($(this)[0].value);
                    });
                    var dataToSend = {
                        <c:if test="${!(category eq null)}">'c':${category.id},</c:if>
                        <c:if test="${!(isNew eq null)}">'n':true,</c:if>
                        'q':'${question.artifactId}',
                        index:choiceIndex,
                        'name':name,
                        'prompt':prompt,
                        'timeAllowed':timeAllowed,
                        'choices':choicesList};
                    var selectedRadio = $('input:radio[id^=answerIndex]:checked');
                    if (selectedRadio.length == 1) {
                        dataToSend.answerIndex = selectedRadio[0].value;
                    } else {
                        dataToSend.answerIndex = 0;
                    }
                    $.ajax({
                        type:'POST',
                        url: '<c:url value="deleteChoice"/>',
                        data:dataToSend,
                        success:function(html) {
                            document.documentElement.innerHTML = html;
                            onStart();
                        }
                    });
                    return false;
                });

                $('#add_choice').click(function() {
                    var name = $('#name').val();
                    var prompt = $('#prompt').val();
                    var timeAllowed = $('#timeAllowed').val();
                    var choicesList = new Array();
                    $('input[id^=answerText]').each(function() {
                        choicesList.push($(this)[0].value);
                    });
                    var dataToSend = {
                        <c:if test="${!(category eq null)}">'c':${category.id},</c:if>
                        <c:if test="${!(isNew eq null)}">'n':true,</c:if>
                        'q':'${question.artifactId}',
                        'name':name,
                        'prompt':prompt,
                        'timeAllowed':timeAllowed,
                        'choices':choicesList};
                    var selectedRadio = $('input:radio[id^=answerIndex]:checked');
                    if (selectedRadio.length == 1) {
                        dataToSend.answerIndex = selectedRadio[0].value;
                    } else {
                        dataToSend.answerIndex = 0;
                    }
                    $.ajax({
                        type:'POST',
                        url: '<c:url value="appendChoice"/>',
                        data:dataToSend,
                        success:function(html) {
                            document.documentElement.innerHTML = html;
                            onStart();
                        }
                    });
                    return false;
                });
            }
            $(document).ready(onStart());
        </script>
	</ul>
</div>