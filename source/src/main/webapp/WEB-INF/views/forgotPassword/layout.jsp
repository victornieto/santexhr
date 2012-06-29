<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<style type="text/css">
	.forgot_password_container {
		width:auto;
		margin-left:28%;
		margin-right:28%;
		margin-top:5%;
	}
</style>
<div class="forgot_password_container">
	<div>
		<h3><tiles:insertAttribute name="header"/></h3>
	</div>
	<div>
		<tiles:insertAttribute name="content"/>
	</div>
</div>