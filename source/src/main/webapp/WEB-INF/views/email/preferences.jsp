<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="content">
	<h1>Email Preferences</h1>
	<form:form commandName="profile" id="prefsform" cssClass="group" action="updatePreferences">
	<ul>
		<li class="fieldset">
			<label class="input">
				<form:checkbox cssClass="send_to" path="forwardDailyReports"/>
				Send Daily Reports
			</label>
			<ul class="smaller">
				<li>
					<label class="input">
						<input type="radio" name="forward_daily_reports_to_user" value="false"
							<c:if test="${empty profile.dailyReportsRecipient}">checked="checked"</c:if> />
						to all administrators
					</label>
				</li>
				<li>
					<label>
						<input type="radio" name="forward_daily_reports_to_user" value="true" class="user" 
							<c:if test="${!empty profile.dailyReportsRecipient}">checked="checked"</c:if> />
						to this email:
					</label>
					<div>
						<form:input path="dailyReportsRecipient" cssClass="user_email"/>
						<form:errors path="dailyReportsRecipient" cssClass="error"/>
					</div>
				</li>
			</ul>
		</li>
		<li class="fieldset">
			<label>
				<form:checkbox cssClass="send_to" path="forwardCandidateEmails"/>
				Forward email from candidates
			</label>
			<ul class="smaller">
				<li>
					<label class="input">
						<input type="radio" name="forward_candidate_emails_to_user" value="false" 
							<c:if test="${empty profile.candidateEmailsRecipient}" >checked="checked"</c:if> />
						to all administrators
					</label>
				</li>
				<li>
					<label class="input">
						<input type="radio" name="forward_candidate_emails_to_user" value="true" class="user"
							<c:if test="${!empty profile.candidateEmailsRecipient}">checked="checked"</c:if> />
						to this email:
					</label>
					<div>
						<form:input path="candidateEmailsRecipient" cssClass="user_email" />
						<form:errors path="candidateEmailsRecipient" cssClass="error" />
					</div>
				</li>
			</ul>
		</li>
		<li class="fieldset">
			<label class="input">
				<form:checkbox cssClass="send_to" path="forwardJobBoardEmails"/>
				Forward email from job boards
			</label>
			<ul class="smaller">
				<li>
					<label class="input">
						<input type="radio" name="forward_job_board_emails_to_user" value="false"
							<c:if test="${empty profile.jobBoardEmailsRecipient}">checked="checked"</c:if> />
						to all administrators
					</label>
				</li>
				<li>
					<label class="input">
						<input type="radio" name="forward_job_board_emails_to_user" value="true" class="user"
							<c:if test="${!empty profile.jobBoardEmailsRecipient}">checked=checked"</c:if> />
							to this email:
					</label>
					<div>
						<form:input path="jobBoardEmailsRecipient" cssClass="user_email"/>
						<form:errors path="jobBoardEmailsRecipient" cssClass="error"/>
					</div>
				</li>
				<li>
					<div>	
						<form:select path="jobBoards" size="5">
							<form:options items="${profile.jobBoards}"/>
						</form:select>
					</div>
				</li>
				<li>
					<div>
						<input type="text" name="job_board" style="width:50%"/> 
						<input type="submit" value="Add New" style="width:auto"/>
						<input type="submit" value="Delete" name="delete_job_boards" style="width:auto"/>
					</div>
				</li>
			</ul>
		</li>
		<li class="actions">
	   		<input type="submit" id="save" value="save"/>
		</li>
	</ul>
	</form:form>
</div>

<script type="text/javascript">
	jQuery('#prefsform .fieldset').each(function() {
		var self = this;
		var checkbox = jQuery('.send_to:checkbox', this)[0];
		function setDisabled() {
			jQuery(':input:not(:hidden)', self).each(function() {
				if(this !== checkbox) {
					this.disabled = !checkbox.checked;
				}
			});
		};
		setDisabled();
		jQuery(checkbox).click(setDisabled);
		jQuery('.user_email', self).click(function() {
			jQuery('.user', self).attr('checked', true);
		});
	});
</script>
