<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<div class="group">
	<h3 class="search">
		<span>Recent Searches</span>
	</h3>
	<ul class="folders">
	<c:forEach var="search" items="${searches}">
		<li class="search_folder ${search.id == searchId ? 'selected':''}">
			<a href="<c:url value='recentsearch?id=${search.id}'/>">
				<c:choose>
					<c:when test="${tt:notBlank(search.searchString)}">
						<c:out value="${tt:abbreviateTo(search.searchString,25)}"/>
					</c:when>
					<c:otherwise>
						[all]
					</c:otherwise>
				</c:choose>
			</a>
		</li>
	</c:forEach>
	</ul>
</div>

<div class="group">
	<h3 class="search">
		<span>Advanced Search</span>
	</h3>
	<form action="<c:url value='find'/>">
		<ul class="smaller">
			<li>
				<label>Name:</label>
				<div>
					<input id="search_name" type="text" name="name" value="<c:out value="${nameQuery}"/>"/>
				</div>
			</li>
			<li>
				<label>Resume:</label>
				<div>
					<input id="search_skills" type="text" name="skills" value="<c:out value="${skillsQuery}"/>"/>
				</div>
			</li>
			<li>
				<label>Dates:</label>
				<div>
					<input id="search_dates" type="text" name="dates" value="<c:out value="${datesQuery}"/>"/>
				</div>
			</li>
			<li class="save">
				<div>
					<input type="submit" value="Search" class="submit"/>
				</div>
			</li>
		</ul>
	</form>
</div>