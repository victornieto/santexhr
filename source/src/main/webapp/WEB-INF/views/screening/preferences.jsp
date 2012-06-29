<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="content">
	<h1>Screening Preferences</h1>
	<form:form commandName="profile" cssClass="group" action="updatePreferences">
		<ul class="numbers">
			<li>
				<label class="input">
					<input type="checkbox" id="autoinvite" name="minCheckBox"
						<c:if test="${!empty profile.minInviteScore}" >checked="checked"</c:if> /> 
					Automatically send exam invitation email to candidates with a resume screening score greater than or equal to: 
				</label>&nbsp;
				<form:input path="minInviteScore" size="3"/>
				<form:errors path="minInviteScore" cssClass="error"/>
			</li>
			<li>
				<label class="input">
					<input type="checkbox" id="autoreject" name="maxCheckBox" 
						<c:if test="${!empty profile.maxRejectScore}" >checked="checked"</c:if> /> 
					Automatically send rejection email to candidates with a resume screen score less than or equal to:
				</label>&nbsp;
				<form:input path="maxRejectScore" size="3"/>
				<form:errors path="maxRejectScore" cssClass="error"/>
			</li>
			<li>
				<label class="input">
					<form:checkbox path="solicitResumes"/>
					Automatically send resume request email back to candidates who send an email without a resume.
				</label>
			</li>
			<li class="actions">
				<input type="submit" id="save" value="Save"/>
			</li>
		</ul>
	</form:form>
</div>

<script type="text/javascript">
	oltk.include('jquery/jquery.js');	
	$(document).ready(function() {
		$("#autoinvite").click(function(){
			if($("#autoinvite").attr("checked")){
				$("#minInviteScore").attr("disabled", false);
			}else{
				$("#minInviteScore").attr("disabled", true);
			}
		});
		$("#autoreject").click(function(){
			if($("#autoreject").attr("checked")){
				$("#maxRejectScore").attr("disabled", false);
			}else{
				$("#maxRejectScore").attr("disabled", true);
			}
		});
		if(!$("#autoreject").attr("checked")){
			$("#maxRejectScore").attr("disabled", true);
		}
		if(!$("#autoinvite").attr("checked")){
			$("#minInviteScore").attr("disabled", true);
		}
	});
</script>
