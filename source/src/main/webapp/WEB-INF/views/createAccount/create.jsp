<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<h2>Free Trial Account Creation</h2>

<p>
Almost there.  Specify the hostname for your company, which will be used for
the public URL and any e-mail addresses that you may create, and identify
the names, e-mail addresses, and roles of up to three users. 
They'll be sent e-mail with instructions for activating their user accounts.
</p>
<div>

<script type="text/javascript">
function changeText(html){
	document.getElementById('hostnameHtml').innerHTML = html + ".openapplicant.org";	
	document.getElementById('emailHtml').innerHTML = html + "@" + html + ".openapplicant.org";
}
</script>

<hr>

<form method="post" action="create">
	<input type="hidden" name="token" value="<c:out value="${token}"/>"/>
	Company Alias: <input type="text" name="alias" size="32" value="<c:out value="${company}"/>" onkeyup="changeText(value)"/><br/>
	<p>
	URL: http://<span id="hostnameHtml">${company}.openapplicant.org/</span><br>
	Email address:  <span id="emailHtml">${company}@${company}.openapplicant.org</span>
	</p>
	Users:<br/>
		<table >
			<tr><td></td><td style="text-align: center;">Name</td><td  style="text-align: center;">Email</td><td  style="text-align: center;">User type</td></tr>
		    <tr>
				<td>User #1</td>
				<td><input type="text" name="user1name" size="24" value="<c:out value="${name}"/>"/></td>
				<td><input type="text" name="user1email" size="24" value="<c:out value="${email}"/>"/></td>
				<td><select size="1" name="user1role"><option>Admin</option><option>Settings</option><option>Grader</option><option>HR</option></select></td>
			</tr>
			 

			<tr>
				<td>User #2</td>
				<td><input type="text" name="user2name" size="24" value=""/></td>
				<td><input type="text" name="user2email" size="24" value=""/></td>
				<td><select size="1" name="user2role"><option>Admin</option><option>Settings</option><option>Grader</option><option>HR</option></select></td>
			</tr>

			<tr>
				<td>User #3</td>
				<td><input type="text" name="user3name" size="24" value=""/></td>
				<td><input type="text" name="user3email" size="24" value=""/></td>
				<td><select size="1" name="user3role"><option>Admin</option><option>Settings</option><option>Grader</option><option>HR</option></select></td>
			</tr>

		</table>

<input type="submit" name="submit" value="Create Account"/>
</form>

</div>