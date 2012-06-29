<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/tlds/DefaultCandidateWorkFlowEventVisitorTag.tld" prefix="events" %>

<events:render 
	events="${events}" 
	showIcon="false" 
	maxEvents="10" 
	abbreviateNotes="true" 
	showExamLinkUrl="false"/>