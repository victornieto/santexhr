<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ page import="static org.openapplicant.domain.User.Role.ROLE_HR" %>
<style type="text/css">
	 @import "<c:url value='/css/layout/candidate_list.css'/>";
</style>

<script type="text/javascript">
	oltk.include('jquery/jquery.js');
	oltk.include('jquery/ui/ui.core.js');
	oltk.include('jquery/cluetip/jquery.cluetip.js');		
	
	$('#content').ready(function() {
		oltk.include('jquery/ui/ui.datepicker.js');
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

		$.tablesorter.addParser({
	        id: 'score',
	        is: function(s) {
	            return false;
	        },
	        format: function(s) {
	            return s.replace(/\u2014/, -1);
	        }, 
	        type: 'numericHack'
	    });

		if( $("#openapplicant_candidate_list tr").length > 1) {
			$("#openapplicant_candidate_list").tablesorter({
				sortList: [[1,0]],
				widgets: ['zebra']/*, //alternating row styles
				headers: {
					0: {sorter:'lastName'},
					2: {sorter:'score'},
					3: {sorter:'score'},
					4: {sorter:'score'},
					11: {sorter: false }
				}                       */
			});
		}

		/* TODO: JQuery UI's datepicker has a DST bug in JQuery UI 1.5.2.  The bug 
		   should no longer appear after we upgrade to JQuery UI 1.6 or later. */
		$("#search_dates").datepicker({
			rangeSelect:true
		});
	
		$('.candidate_name').cluetip({
			width:425,
			cluetipClass:'history',
			ajaxSettings: {
				type: 'POST'
			}
		});
	
		$('.tooltip').each( function() {
			var activation = $(this).hasClass('hover') ? 'hover' : 'click';
			$(this).cluetip({
				activation: activation,
				width:175,
				cluetipClass:'jtip',
				local:true,
				arrows:true,
				closeText: '<img src="<c:url value="/img/jquery_cluetip/close-gray.png"/>"/>'
			});
		});
	});
</script>
<div id="content" style="width: 728px;">
    <display:table name="candidatesNotes" id="cn" htmlId="openapplicant_candidate_list" class="sortable" pagesize="10" requestURI="${requestScope['javax.servlet.forward.request_uri']}">
        <display:column headerClass="header" title="Name" >
            <a class="candidate_name" href="<c:url value='detail?id=${cn.candidate.id}'/>" rel="<c:url value='history?id=${cn.candidate.id}'/>" title="History: ${tt:abbreviateTo(cn.candidate.name.fullName,37)}">
                <c:choose>
                    <c:when test="${!empty cn.candidate.name.first}">
                        <span><c:out value="${tt:abbreviateTo(cn.candidate.name.first,14)}"/> <strong><c:out value="${tt:abbreviateTo(cn.candidate.name.last,14)}"/></strong></span>
                    </c:when>
                    <c:otherwise>
                        <span><i>Information Required</i> <img src="<c:url value='/img/candidate_icons/warning.png'/>"/></span>
                    </c:otherwise>
                </c:choose>
            </a>
        </display:column>
        <display:column headerClass="header" title="Date" >
            <fmt:formatDate value="${cn.note.entityInfo.createdDate.time}" type="date" dateStyle="short" timeStyle="short"/>
        </display:column>
        <display:column headerClass="header" title="Note">
            <c:out value="${tt:abbreviateTo(cn.note.body, 30)}"/>
        </display:column>
        <display:column headerClass="header" title="Author">
            <c:choose>
                <c:when test="${!empty cn.candidate.name.first}">
                    <span><c:out value="${tt:abbreviateTo(cn.note.author.name.first,14)}"/> <strong><c:out value="${tt:abbreviateTo(cn.note.author.name.last,14)}"/></strong></span>
                </c:when>
                <c:otherwise>
                    <span><i>Information Required</i> <img src="<c:url value='/img/candidate_icons/warning.png'/>"/></span>
                </c:otherwise>
            </c:choose>
        </display:column>
    </display:table>
</div>
