<%@ taglib prefix="c"    uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>

	<li class="candidate">
		<a href="<c:url value='/admin/candidates/detail?id=${candidate.id}'/>" title="${candidate.name}">
			<c:out value="${tt:abbreviateTo(candidate.name, 19)}"/>
		</a>
		<ul class="exams">
		<c:forEach var="s" items="${candidate.sittings}">
			<li class="${s.id == sitting.id && index == null ? 'selected' : ''}">
				<a href="<c:url value='/admin/results/exam?s=${s.id}'/>">
					<c:out value="${tt:abbreviateTo(s.exam.name, 19)}"/>
				</a>
				<c:if test="${s.id == sitting.id}">
					<ul class="questions">
					<c:forEach var="questionAndResponse" items="${s.questionsAndResponses}" varStatus="i">
						<c:choose>
							<c:when test="${index==i.index}">
								<li class="selected">
									<span title="${questionAndResponse.question.name}">
										<c:out value="${tt:abbreviateTo(questionAndResponse.question.name, 17)}"/>
									</span>
								</li>
							</c:when>
							<c:otherwise>
								<li>
									<a href="<c:url value='/admin/results/question?s=${s.id}&i=${i.index}'/>" title="${questionAndResponse.question.name}">
										<c:out value="${tt:abbreviateTo(questionAndResponse.question.name, 17)}"/>
									</a>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					</ul>
				</c:if>
			</li>
		</c:forEach>
		</ul>
	</li>