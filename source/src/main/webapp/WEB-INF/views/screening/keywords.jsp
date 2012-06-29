<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style type="text/css">
	@import "<c:url value='/css/vendor/jquery.autocomplete.css'/>";
</style>

<div id="content">
	<h1>Screening Keywords</h1>
	<form action="<c:url value='updateKeywords' />" class="group" method="post">
		<table class="sortable" id="openapplicant_keyword_list">
			<thead>
				<tr>
					<th>Keyword</th>
					<th>Priority</th>	
					<th>Remove</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="k" items="${keywords}">
				<tr>
					<td><c:out value="${k.key}" /></td>
					<td><c:out value="${k.value.humanString}" /></td>	
					<td align="center"><input type="checkbox" name="remove" value="<c:out value='${k.key}' />"/></td>
				</tr>
			</c:forEach>
			</tbody>
			<tfoot>
				<tr class="inputfield">
					<th class="arrow"><input type="text" class="field" id="keyword" name="keyword" /></th>
					<th>
						<select name="priority">
						<c:forEach var="p" items="${priorities}" >
							<option value="<c:out value='${p}' />" selected><c:out value='${p.humanString}' /></option>
						</c:forEach>
						</select>
					</th>	
					<th>&nbsp;</th>
				</tr>
			</tfoot>
		</table>
		<div class="actions">	
			<input type="submit" class="applykeyword" value="Apply" />
		</div>
	</form>
</div>

<script type="text/javascript">
	oltk.include('jquery/jquery.js');
	oltk.include('jquery/autocomplete/jquery.autocomplete.js');
	oltk.include('openapplicant/admin/helper/jquery.bgiframe.min.js');
	oltk.include('openapplicant/admin/helper/thickbox-compressed.js');
	oltk.include('openapplicant/admin/helper/jquery.tablesorter.min.js');

	$(document).ready(function() {
		$("#openapplicant_keyword_list").tablesorter({
			widgets: ['zebra'] //alternating row styles
		});

		var data = ".NET, AJAX, Android, ASP, Assembly, Brew, CGI, CoCo, CSS, C#, C++, Fortran, Hibernate, HTML, Java, Javascript, J#, Linux, Lisp, Matlab, Maven, MIPS, MySQL, Object C, Oracle, Perl, PHP, Python, Ruby, Solaris, Spring-framework, SQL, Unix, Verliog, VmWare, VxWorks".split(", ");
		$("#keyword").autocomplete(data, {
			minChars: 0,
			autoFill: false,
			max: 50
		});

		$("#keyword").focus();
	});	
</script>
