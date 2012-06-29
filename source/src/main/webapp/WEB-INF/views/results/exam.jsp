<%@ taglib prefix="c"    uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="/WEB-INF/tlds/ttTagLibrary.tld" prefix="tt" %>

<style type="text/css">
	 @import "<c:url value='/css/layout/sitting.css'/>";
</style>

<!--[if lt IE 8]>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/ie/sitting.css'/>">
<![endif]-->

<div id="content">	
	<h1>${sitting.exam.name}</h1>
	
		<div id="stats" class="setting">
			<ul>
				<li>Questions: ${questions}</li>
				<li>Untimed Questions: ${untimedQuestions}</li>
				<li>Words per Minute: <fmt:formatNumber value="${sitting.responseSummary.wordsPerMinute}" maxFractionDigits="1"/></li>
			</ul>	
			<ul>
				<li>Total Time: <c:out value="${tt:formatLong(sitting.responseSummary.totalTime)}"/></li>
				<li>Away Time: <c:out value="${tt:formatLong(sitting.responseSummary.awayTime)}"/></li>
				<li>Lines per Hour: <fmt:formatNumber value="${sitting.responseSummary.linesPerHour}" maxFractionDigits="1"/></li>
			</ul>
			<ul>
				<li>Typed: ${sitting.responseSummary.keyPresses} chars</li>
				<li>Delete: ${sitting.responseSummary.erasePresses} presses</li>
				<li>Paste: ${sitting.responseSummary.pastePresses} presses</li>
			</ul>
			<ul>
				<li>Response: ${sitting.responseSummary.keyChars} chars</li>
				<li>Deleted: ${sitting.responseSummary.eraseChars} chars</li>
				<li>Pasted: ${sitting.responseSummary.pasteChars} chars</li>
			</ul>
			<ul>
				<li>Exam Score:
					<c:choose>
						<c:when test="${not empty sitting.score}">
							<b><c:out value="${sitting.score}"/></b>
						</c:when>
						<c:otherwise>
							&mdash;
						</c:otherwise>
					</c:choose>
				</li>
			</ul>
		</div>	
		<script type="text/javascript">
			oltk.include('jquery/jquery.js');
			oltk.include('openapplicant/admin/helper/json2.js');
			oltk.include('openapplicant/admin/helper/swfobject.js');
			oltk.include('openapplicant/admin/helper/analytics.js');

			openapplicant.admin.helper.analytics.init();
			<c:forEach var="questionAndResponse" items="${sitting.questionsAndResponses}" varStatus="i">
				openapplicant.admin.helper.analytics.addKeypressEvents(${questionAndResponse.response.keypressEvents});
				openapplicant.admin.helper.analytics.addPasteEvents(${questionAndResponse.response.pasteEvents});
				openapplicant.admin.helper.analytics.addFocusEvents(${questionAndResponse.response.focusEvents});
			</c:forEach>
		</script>

		<div id="response_graph"></div>
		<script type="text/javascript">
			swfobject.embedSWF("<c:url value='/img/open-flash-chart.swf'/>", "response_graph", "100%", "133", "9.0.0", "expressInstall.swf",
								{"get-data":"getResponseGraph"});
			
			function getResponseGraph() {
				return openapplicant.admin.helper.analytics.getResponseGraph();
			}
		</script>
		
		<div class="row">	
			<div class="chart left">
				<c:choose>
					<c:when test="${sitting.responseSummary.eraseChars gt 0 || sitting.responseSummary.pasteChars gt 0 || sitting.responseSummary.keyPresses gt 0}">
						<ul class="legend">
							<li>
								<span>Deleted</span>
								<div style="background:#999999;"></div>
							</li>
							<li>
								<span>Pasted</span>
								<div style="background:#FF3A03;"></div>
							</li>
							<li>
								<span>Typed</span>
								<div style="background:#8CC63F;"></div>
							</li>
						</ul>
						<div id="code_breakdown"></div>
						<script type="text/javascript">
							swfobject.embedSWF("<c:url value='/img/open-flash-chart.swf'/>", "code_breakdown", "250", "250", "9.0.0", "expressInstall.swf",
												{"get-data":"getCodeBreakdown"});
												
							function getCodeBreakdown() {
								return JSON.stringify(code_breakdown);
							}
									
							var code_breakdown = {
								"title": {
									"text": "Keystroke breakdown",
									"style": "{font-size: 20px; color: #808080; text-align: left;}" 
								 },
								"elements": [{
									"type": "pie",
									"colours": [ "#999999", "#FF3A03", "#8CC63F"],			
									"animate": true,
									"gradient-fill": true,
									"tip": "#label#:\n#val# chars",
									"no-labels": true,
									"values": [
											{ "value": ${sitting.responseSummary.eraseChars}, "label": "Deleted" },
											{ "value": ${sitting.responseSummary.pasteChars}, "label": "Pasted" },
											{ "value": ${sitting.responseSummary.keyPresses}, "label": "Typed" }
										]
									}],
									"bg_colour": "#FFFFFF"
								};
						</script>			
					</c:when>
					<c:otherwise>
						<h4>Candidate did not type anything.</h4>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="chart right">
				<c:choose>
					<c:when test="${sitting.responseSummary.hesitationTime gt 0 || sitting.responseSummary.awayTime gt 0 || sitting.responseSummary.typingTime gt 0 || sitting.responseSummary.reviewingTime gt 0}">
						<ul class="legend">
							<li>
								<span>Hesitation</span>
								<div style="background:#FFFF00;"></div>
							</li>
							<li>
								<span>Away</span>
								<div style="background:#FF3A03;"></div>
							</li>
							<li>
								<span>Typing</span>
								<div style="background:#8CC63F;"></div>
							</li>
							<li>
								<span>Reviewing</span>
								<div style="background:#0071BC;"></div>
							</li>
						</ul>
						<div id="time_breakdown"></div>
						<script type="text/javascript">
							swfobject.embedSWF("<c:url value='/img/open-flash-chart.swf'/>", "time_breakdown", "250", "250", "9.0.0", "expressInstall.swf",
												{"get-data":"getTimeBreakdown"});
												
							function getTimeBreakdown() {
								return JSON.stringify(time_breakdown);
							}
					
							var time_breakdown = {
									"title": {
										"text": "Time breakdown",
										"style": "{font-size: 20px; color: #808080; text-align: left;}" 
									 },
									"bg_colour": "#FFFFFF",
									"elements": [{
										"type": "pie",
										"colours": [ "#FFFF00", "#FF3A03", "#8CC63F", "#0071BC"],
										"animate": true,
										"gradient-fill": true,
										"tip": "#label#",
										"no-labels": true,
										"values":[{
											"value": ${sitting.responseSummary.hesitationTime},
											"label": "Hesitation:\n${tt:formatLong(sitting.responseSummary.hesitationTime)}"
												},{
											"value": ${sitting.responseSummary.awayTime},
											"label": "Away:\n${tt:formatLong(sitting.responseSummary.awayTime)}"
												},{
											"value": ${sitting.responseSummary.typingTime},
											"label": "Typing:\n${tt:formatLong(sitting.responseSummary.typingTime)}"
												},{
											"value": ${sitting.responseSummary.reviewingTime},
											"label": "Reviewing:\n${tt:formatLong(sitting.responseSummary.reviewingTime)}"
										}]
									}]
								};
						</script>
					</c:when>
					<c:otherwise>
						<h4>Candidate did not submit a response.</h4>
					</c:otherwise>
				</c:choose>
			</div>
		</div><!-- /row -->
</div>