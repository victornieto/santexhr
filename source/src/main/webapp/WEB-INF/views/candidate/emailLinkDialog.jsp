<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div id="candidate_exam_link_email_dialog" style="display:none; overflow: hidden;">
	<form method="POST" action="<c:url value="sendEmail"/>">
		<input type="hidden" name="candidateId" value="${candidateId}"/>
		<input type="hidden" name="examLinkId" value="${examLinkId}"/>
		<ul>
			<li>
				<label class="input">To:</label>
				<div>
					<input type="text" name="to" readonly=readonly value="<c:out value="${to}"/>"/>
				</div>
			</li>
			<li>
				<label class="input">From:</label>
				<div>
					<input type="text" name="from" value="<c:out value="${from}"/>"/>
				</div>
			<li>
				<label class="input">Subject:</label>
				<div>
					<input type="text" name="subject" value="<c:out value="${subject}"/>"/>
				</div>
			</li>
			<li>
				<label class="input">Body:</label>
				<div>
					<textarea name="body"><c:out value="${body}"/></textarea>
				</div>
			</li>
			<li class="save">
				<div>
					<input type="submit" value="Send"/>
				</div>
			</li>
		</ul>
	</form>
</div>