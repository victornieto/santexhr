<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@ taglib prefix="tt" uri="/WEB-INF/tlds/ttTagLibrary.tld"%>

<div id="sidebar">

    <tiles:insertAttribute name="jobOpenings"/>
    <tiles:insertAttribute name="candidateTree"/>
	<tiles:insertAttribute name="profile"/>
	<tiles:insertAttribute name="searches"/>
	
</div>
