<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<div class="group">
	<h3>
		<img src="<c:url value='/img/sidebar/candidate.gif' />" />
		<a href="<c:url value='/admin/candidates/index' />">
			Candidates
		</a>
	</h3>
	<div class="status_folders">
		<div>
			<span class="toggler">
				<img src="<c:url value="/img/open.gif"/>"/>
			</span>
			<a href="<c:url value="/admin/candidates/active"/>">Active</a>
		</div>
		<ul class="toggle_target">
			<c:forEach var="status" items="${activeStatuses}">
				<li class="folder ${view_status == status ? 'selected' : ''}">
					<a href="<c:url value="/admin/candidates/viewstatus?status=${status}"/>">
						<c:out value="${tt:humanize(status)}"/>
					</a>
					<ul>
						<c:if test="${candidate.status == status}">
							<jsp:include page="../shared/candidate.jsp" />
						</c:if>
					</ul>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div class="status_folders">
		<div>
			<span class="toggler">
				<img src="<c:url value="/img/open.gif"/>"/>
			</span>
			<a href="<c:url value="/admin/candidates/archived"/>">Archived</a>
		</div>
		<ul class="toggle_target">
			<c:forEach var="status" items="${archivedStatuses}">
				<li class="folder ${view_status == status ? 'selected' : ''}">
					<a href="<c:url value="/admin/candidates/viewstatus?status=${status}"/>">
						<c:out value="${tt:humanize(status)}"/>
					</a>
					<ul>
						<c:if test="${candidate.status == status}">
							<jsp:include page="../shared/candidate.jsp" />
						</c:if>
					</ul>
				</li>
			</c:forEach>
		</ul>
	</div>
	<script type="text/javascript">
		oltk.include('jquery/jquery.js');
		jQuery('.status_folders .toggler').click(function() {
			jQuery(this).parent().siblings('.toggle_target').toggle();
			var img = jQuery('img', this)[0];
			if(/open\.gif/.test(img.src)) {
				img.src='<c:url value="/img/closed.gif"/>';
			} else {
				img.src='<c:url value="/img/open.gif"/>';
			}
		});
	</script>
</div>