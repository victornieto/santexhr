<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="content">
	<h1>
		<img src="<c:url value='/img/settings/add-user.png' />" />
		<a href="<c:url value='/admin/user/add' />">Add New User</a>
	</h1>
	
 	<table class="sortable" id="openapplicant_users_list">
		<thead>
			<tr>
				<th class="header">Name</th>
				<th class="header">Email</th>	
				<th class="header">Role</th>
				<th class="header">Last Login</th>
				<th class="icon header"><img src="<c:url value='/img/table/change-password.gif'/>" title="Change Password"/></th>
				<th class="icon header"><img src="<c:url value='/img/table/change-role.gif'/>" title="Change Role"/></th>
				<th class="icon header"><img src="<c:url value='/img/table/user-remove.gif'/>" title="Deactivate User"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${users}">
			<tr>
				<td>
					<a href="<c:url value='/admin/user/view?id=${user.id}' /> ">
						<span><c:out value="${user.name.blank ? '[No Name]' : user.name}"/></span>
					</a>
				</td>
				<td>
					<a href="mailto:<c:out value='${user.email}' />" >
						<c:out value="${user.email}" />
					</a>
				</td>	
				<td><c:out value="${user.role.humanString}"/></td>
				<td>
					<c:if test="${not empty user.lastLoginTime }">
						<fmt:formatDate value="${user.lastLoginTime.time}" type="both" dateStyle="short" timeStyle="short"/>
					</c:if>
				</td>
				
				<!-- Column: change user password -->
				<td class="icon">
					<a class="tooltip" rel="#tooltip_password${user.id }" title="Change password for <c:out value="${user.name.first}"/>">
						<img src="<c:url value='/img/table/change-password.gif' />" />
					</a>
					<div style="display:none" id="tooltip_password<c:out value="${user.id}"/>">
						<form onsubmit="changePassword('<c:out value="${user.id}"/>', this); return false;">
							<ul class="small">
								<li>
									<span style="display:none" class="status">&nbsp;</span>
									<span style="display:none" class="error">&nbsp;</span>
								</li>
								<li>
									<label>Password:</label>
									<div>
										<input type="password" name="password"/>
									</div>
								</li>
								<li>
									<label>Confirm:</label>
									<div>
										<input type="password" name="confirmPassword"/>
									</div>
								</li>
								<li class="save">
									<div>
										<input type="submit" class="save" value="Save"/>
									</div>
								</li>
							</ul>
						</form>
					</div>
				</td>
				
				<!-- Column: change user role -->
				<td class="icon">
					<a class="tooltip" rel="#tooltip_role${user.id }" title="Change role for <c:out value="${user.name.first}"/>">
						<img src="<c:url value='/img/table/change-role.gif' />" />
					</a>
					<div style="display:none" id="tooltip_role${user.id}">
						<form method="POST" action="<c:url value="changeRole"/>">
							<input type="hidden" name="id" value="<c:out value='${user.id}'/>"/>
							<ul>
								<li>
									<select name="role">
										<c:forEach var="role" items="<%=org.openapplicant.domain.User.Role.values()%>">
											<option value="<c:out value="${role.name}"/>" 
												<c:if test="${user.role eq role}">selected="selected"</c:if>>
												<c:out value="${role.humanString}"/>
											</option>
										</c:forEach>
									</select>
								</li>
								<li class="save">
									<input type="submit" class="save" value="Save"/>
								</li>
							</ul>
						</form>
					</div>
				</td>

				<!-- Column: change status -->
				<td class="icon">
					<a class="tooltip" rel="#tooltip_delete${user.id }" 
						title="<c:out value="${user.enabled ? 'Deactivate' : 'Activate'} ${user.name.first}"/>">
						<img src="<c:url value="/img/table/${user.enabled ? 'user-remove.gif' : 'user-add.gif'}"/>"/>
					</a>
					<div style="display:none" id="tooltip_delete${user.id}">
						<form action="<c:url value='toggleEnabled' />" method="post">
							<input type="hidden" name="id" value="<c:out value='${user.id}'/>" />
							<ul>
								<li>
									<input type="submit" class="save" value="Confirm" />
								</li>
							</ul>
						</form>
					</div>
				</td>
				
			</tr>
			</c:forEach>		
		</tbody>
	</table>
</div>

<script type="text/javascript" src="<c:url value="/dwr/interface/UsersController.js"/>"></script>
<script type="text/javascript">
	oltk.include('jquery/jquery.js');
	oltk.include('jquery/cluetip/jquery.cluetip.js');
	oltk.include('openapplicant/admin/helper/jquery.tablesorter.min.js');
	
	$.tablesorter.addParser({ 
	    id: 'lastName', 
	    is: function(s) { 
	        // return false so this parser is not auto detected 
	        return false; 
	    }, 
	    format: function(s) {
	        return $(s).find("strong").text().toLowerCase();
	    }, 
	    // set type, either numeric or text 
	    type: 'text' 
	}); 
	
	$(document).ready(function() {

		if($("#openapplicant_users_list").find("tr").length < 2)
			return;
		
		$("#openapplicant_users_list").tablesorter({
			sortList: [[0,0]],
			widgets: ['zebra'], //alternating row styles
			headers: {
				0: {sorter:'lastName'},
				4: {sorter: false },
				5: {sorter: false },
				6: {sorter: false }
			}
		});

		$('.tooltip').cluetip({
			activation: 'click',
			cluetipClass:'jtip',
			mouseOutClose: false,
			local:true,
			sticky:true,
			arrows:true,
			showTitle:true,
			closePosition: 'top',
			closeText: '<img src="<c:url value="/img/jquery_cluetip/close-gray.png"/>"/>'
		});

		$('.cancel').click(function(){
			$("#cluetip-close").click();
		});
	});
	
	/**
	 * @param {string|int} userId
	 * @param {HTMLElement} form
     */
	function changePassword(userId, form) {
		jQuery('.status', form).html('Saving...').show();
		jQuery('.error',form).html('&nbsp;').hide();
		UsersController.changePassword(userId, form.password.value, form.confirmPassword.value, 
			function(model) {
				if(model.error) {
					jQuery('.status',form).html('&nbsp;').hide();
					jQuery('.error',form).html(model.error).show();
				} else {
					jQuery('.status', form).html('Saved!').show();
					jQuery('.error', form).hide();
				}
			}
		);
	};
</script>
