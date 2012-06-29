<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<style type="text/css">
	#exams_site_form textarea {
		height:150px;
	}
</style>
<div id="content">
	<form id="exams_site_form" action="updateSite" method="POST" class="group">
		<ul>
			<li>
				<h3>Edit the text to display before and after each exam.</h3>
			</li>
			<li>
				<label>Welcome Text:</label>
				<textarea name="welcomeText"><c:out value="${welcomeText}"/></textarea>
			</li>
			<li>
				<label>Completion Text:</label>
				<textarea name="completionText"><c:out value="${completionText}"/></textarea>
			</li>
			<li class="actions">
				<input type="submit" value="Save"/>
			</li>
		</ul>
	</form>
	<script type="text/javascript">
		jQuery(function() {
			jQuery('#exams_site_form').submit(function() {
				if(!jQuery.trim(this.welcomeText.value)) {
					if(!confirm('Save without any welcome text?')) {
						return false;
					}
				}
				if(!jQuery.trim(this.completionText.value)) {
					if(!confirm('Save without any completion text?')) {
						return false;
					}
				}
				return true;
			});
		});
	</script>
</div>