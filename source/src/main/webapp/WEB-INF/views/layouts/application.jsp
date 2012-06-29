<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%--

==============================================================================

	application.jsp

==============================================================================

	Base layout for the application.

--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
		<title><tiles:getAsString name="title"/></title>

		<script type="text/javascript" src="<c:url value="/dwr/engine.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/lib/js/oltk/oltk.js"/>"></script>
		<script type="text/javascript">
			oltk.include('jquery/jquery.js');
		</script>
		
   		<link rel="shortcut icon" type="image/x-icon" href="<c:url value='/img/favicon.png'/>"/>
		
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/base.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/layout.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/style.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/input.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/forms.css'/>"/>
		
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/layout/sidebar.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/layout/top.css'/>"/>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/layout/tablesorter.css'/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/layout/pagination.css'/>"/>
				
		<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" href="<c:url value='/css/ie/ie7.css'/>">
			<link rel="stylesheet" type="text/css" href="<c:url value='/css/ie/forms-7.css'/>">
		<![endif]-->
		
		<!--[if lt IE 7]>
			<link rel="stylesheet" type="text/css" href="<c:url value='/css/ie/ie6.css'/>">
			<link rel="stylesheet" type="text/css" href="<c:url value='/css/ie/forms-6.css'/>">
			<script type="text/javascript">
				oltk.include('openapplicant/admin/helper/jquery.ifixpng.js');
			   	$(document).ready(function(){
					$('img[@src$=.png]').ifixpng();
					$('#sidebar li').ifixpng();
					$('#sidebar h3.search').ifixpng();
			    }); 
			</script>
		<![endif]-->
	
	</head>
	<body>

		<%-- required markup --%>
		<input type="hidden" id="oltk-history-field"/>
		<c:if test="${fn:containsIgnoreCase(header['user-agent'], 'MSIE')}">
			<iframe src="<c:url value='/blank.html'/>" id="oltk-history-iframe" style="position:absolute;top:0px;left:0px;width:1px;height:1px;visibility:hidden"></iframe>
		</c:if>

		<div id="container">
			<div class="openapplicant_application_top">

				<tiles:insertAttribute name="top"/>

			</div>

			<div class="openapplicant_application_sidebar">

				<tiles:insertAttribute name="sidebar"/>

			</div>

			<div class="openapplicant_application_content">

				<tiles:insertAttribute name="content"/>

			</div>

			<div class="openapplicant_application_bottom">

				<tiles:insertAttribute name="bottom"/>

			</div>
		</div>
		<div id="copyright">&copy; Copyright Santex Group 2012, All Rights Reserved.</div>
	</body>
</html>