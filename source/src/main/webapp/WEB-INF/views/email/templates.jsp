<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div id="content">
	<h1>Email Templates</h1>
	<table class="sortable" id="openapplicant_templates_list">
		<thead>
			<tr>
				<th>Name</th>
				<th>Profile</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="t" items="${templates}" >
			<tr>
				<td>
					<a href="<c:url value='template?id=${t.id}' />" >
						<c:out value="${t.name}" />
					</a>
				</td>
				<td>Global</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<script type="text/javascript">
	oltk.include('openapplicant/admin/helper/jquery.tablesorter.min.js');
	
	$(document).ready(function() {
		if($("#openapplicant_templates_list").find("tr").length < 2)
			return;
		
		$("#openapplicant_templates_list").tablesorter({
			widgets: ['zebra'] //alternating row styles
		});
	});	
</script>
