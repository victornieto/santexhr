<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<style type="text/css">
	.reserved {
		cursor: pointer;
	}
	
	textarea#template_body {
		height:25em;
	}
</style>

<div id="content">
	<div class="group left" style="width:441px;">
		<h1>Templates</h1>
		<form method="POST" action="<c:url value='updateTemplate'/>">
			<input type="hidden" name="id" value="${template.id}"/>
			<ul>
				<li>
					<label>From:</label>
					<div>
						<input type="text" name="fromAddress" value="${template.fromAddress}"/>
						<span class="error"><c:out value="${fromAddressError}"/></span>
						<span>Default leave blank</span>
					</div>
				</li>
				<li>
					<label>Subject:</label>
					<div>
						<input type="text" name="subject" value="${template.subject}"/>
						<span class="error"><c:out value="${subjectError}"/></span>
					</div>
				</li>
				<li>
					<label>Body:</label>
					<div>
						<textarea name="body" id="template_body"><c:out value="${template.body}"/></textarea>
						<span class="error"><c:out value="${bodyError}"/></span>
					</div>
				</li>
				<li class="actions">
					<input type="submit" value="Save"/>
					<input type="button" value="Revert to Default" id="revert_button"/>
				</li>
			</ul>
		</form>
	</div>
	<div class="group right" style="width:264px;">
		<h2>Insert Tag:</h2>
		<ul style="margin-top:5px">
			<c:forEach var="placeHolder" items="${template.placeHolders}">
				<li>
					<c:out value="${placeHolder.description}"/>: 
					<span class="reserved">
						<c:out value="${placeHolder.symbol}"/>
					</span>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>

<script type="text/javascript">
	function insertAtCursor(myField, myValue) {
	  //IE support
	  if (document.selection) {
	    myField.focus();
	    var sel = document.selection.createRange();
	    sel.text = myValue;
	  }
	
	  //MOZILLA/NETSCAPE support
	  else if (myField.selectionStart || myField.selectionStart == '0') {
	    var startPos = myField.selectionStart;
	    var endPos = myField.selectionEnd;
	    myField.value = myField.value.substring(0, startPos)
	                  + myValue
	                  + myField.value.substring(endPos, myField.value.length);
	                  
	    myField.selectionStart += myValue.length;
	  } else {
	    myField.value += myValue;
	  }
	}
	
	$(document).ready(function() {
	
		var template = $("#template_body");
		$(".reserved").each(function(i) {
			var tag = $(this);
			var text = tag.text().match("<<.*>>")[0];
			tag.bind("click", function(e) {
				insertAtCursor(template.get(0), text);
				template.focus();
			});
		});
		
		$("#revert_button").bind("click", function(e) {
			if (confirm("Revert email template to default message?")) { 
				location.href = "<c:url value='revertTemplate?id=${template.id}' />";
			}
		});
	});
</script>