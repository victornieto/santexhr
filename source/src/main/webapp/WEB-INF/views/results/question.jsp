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
<c:choose>
	<c:when test="${not empty response}">
	
		<h1>${question.name}</h1>
		
		<div id="stats" class="setting">
			<ul>
				<li>
					<c:choose>
						<c:when test="${question.timeAllowed != null}">
							Timed: <c:out value="${question.timeAllowed}"/> secs
						</c:when>
						<c:otherwise>
							Time: untimed
						</c:otherwise>
					</c:choose>
				</li>
				<li>Words per Minute: <fmt:formatNumber value="${response.wordsPerMinute}" maxFractionDigits="1"/></li>
				<li>Lines per Hour: <fmt:formatNumber value="${response.linesPerHour}" maxFractionDigits="1"/></li>
			</ul>	
			<ul>
				<li>Total Time: <c:out value="${tt:formatLong(response.totalTime)}"/></li>
				<li>Hesitation: <c:out value="${tt:formatLong(response.hesitationTime)}"/></li>
				<li>Away: <c:out value="${tt:formatLong(response.awayTime)}"/></li>
			</ul>
			<ul>
				<li>Typed: ${response.keyPresses} chars</li>
				<li>Delete: ${response.erasePresses} presses</li>
				<li>Paste: ${response.pastePresses} presses</li>
			</ul>
			<ul>
				<li>Response: ${response.keyChars} chars</li>
				<li>Deleted: ${response.eraseChars} chars</li>
				<li>Pasted: ${response.pasteChars} chars</li>
			</ul>
			<ul>
				<li>Correctness: <c:out value="${response.grade.scores.function}"/></li>
				<li>Style: <c:out value="${response.grade.scores.form}"/></li>
			</ul>
		</div>
		
		<textarea id="response" class="code" readonly="readonly">${response.content}</textarea>
		
		<div id="position" class="ui-slider">
			<img id="handle" class="ui-slider-handle" src="<c:url value='/img/player/position.gif'/>"/>
		</div>
		
		<div id="control_bar">
			<img id="play" src="<c:url value='/img/player/play.gif'/>" class="button"/>
			<img id="stop" src="<c:url value='/img/player/stop.gif'/>" class="button"/>
			<c:choose>
				<c:when test="${index > 0}">
					<a href="<c:url value='question?s=${sitting.id}&i=${index-1}'/>">
						<img id="prev" src="<c:url value='/img/player/prev.gif'/>" class="button"/>
					</a>
				</c:when>
				<c:otherwise>
					<img id="prev" src="<c:url value='/img/player/prev_disabled.gif'/>" class="button"/>
				</c:otherwise>
			</c:choose>
			<img id="slower" src="<c:url value='/img/player/slower.gif'/>" class="button"/>
			<img id="faster" src="<c:url value='/img/player/faster.gif'/>" class="button"/>
			<c:choose>
				<c:when test="${index+1 < fn:length(sitting.questionsAndResponses)}">
					<a href="<c:url value='question?s=${sitting.id}&i=${index+1}'/>">
						<img id="next" src="<c:url value='/img/player/next.gif'/>" class="button"/>
					</a>
				</c:when>
				<c:otherwise>
					<img id="next" src="<c:url value='/img/player/next_disabled.gif'/>" class="button"/>
				</c:otherwise>
			</c:choose>
			<div id="speed" class="button"></div>
			<span id="clock" class="button"></span>
			<script type="text/javascript">
				oltk.include('jquery/jquery.js');
				oltk.include('openapplicant/admin/helper/json2.js');
				oltk.include('openapplicant/admin/helper/swfobject.js');
				oltk.include('openapplicant/admin/helper/analytics.js');
				oltk.include('openapplicant/admin/helper/player.js');
		
				openapplicant.admin.helper.analytics.init();
				openapplicant.admin.helper.analytics.addKeypressEvents(${response.keypressEvents});
				openapplicant.admin.helper.analytics.addPasteEvents(${response.pasteEvents});
				openapplicant.admin.helper.analytics.addFocusEvents(${response.focusEvents});
				
				openapplicant.admin.helper.player.init(openapplicant.admin.helper.analytics.getKeypressEvents(), {
					screen: $('#response'),
					clock: $('#clock'),
					speed: $('#speed'),
					playButton: $('#play'),
					playImagePath: "<c:url value='/img/player/play.gif'/>",
					pauseImagePath: "<c:url value='/img/player/pause.gif'/>",
					position: $('#position'),
					handle: $('#handle')
				});
				
				$('#stop').click(openapplicant.admin.helper.player.stop);
				$('#faster').click(openapplicant.admin.helper.player.faster);
				$('#slower').click(openapplicant.admin.helper.player.slower);
			</script>
		</div>
		
		<div id="response_graph"></div>
		<script type="text/javascript">
			swfobject.embedSWF("<c:url value='/img/open-flash-chart.swf'/>", "response_graph", "100%", "133", "9.0.0", "expressInstall.swf",
								{"get-data":"getResponseGraph"});
			
			function getResponseGraph() {
				return openapplicant.admin.helper.analytics.getResponseGraph();
			}
		</script>
		
		<div id="question_answer_grade" class="setting group">
			<span class="toggler">Question, Correct Answer, and Grading <img src="<c:url value='/img/closed.gif'/>"/></span>
			<div style="display:${response.notGraded ? "block" : "none"};">
				<div class="third">
					<pre>${question.prompt}</pre>
				</div>
				<div class="third">
					<pre>${question.answer}</pre>
				</div>
				<div class="third last">
					<form id="grade_form" action="setGrade" method="POST">
						<input type="hidden" name="s" value="${sitting.id}"/>
						<input type="hidden" name="i" value="${index}"/>

						<input type="hidden" name="responseId" value="${response.id}"/>
						<ul class="smaller fix">
							<li>
								<label>Correctness:</label>
								<div>
									<input type="text" name="correctness" value="<c:out value="${response.grade.scores.function}"/>"/>
									<span id="grade_validation">scale 0-100</span>
								</div>
							</li>
							<li>
								<label>Style:</label>
								<div>
									<input type="text" name="style" value="<c:out value="${response.grade.scores.form}"/>"/>
								</div>
							</li>
							<li class="actions">
							<c:choose>
								<c:when test="${index+1 < fn:length(sitting.questionsAndResponses)}">
									<input type="submit" name="save" value="Save"/>
									<input type="submit" name="next" value="Save and Go To Next"/>
								</c:when>
								<c:otherwise>
									<input type="submit" name="save" value="Save"/>
								</c:otherwise>
							</c:choose>
							</li>
						</ul>
					</form>
					<script type="text/javascript">
					    oltk.include('jquery/validate/jquery.validate.js');
	                    $('#grade_form').validate({
	                        rules: {
	                    		correctness:{	number: true,
												min: 0,
												max: 100
								},
								style:{			number: true,
												min: 0,
						  						max: 100
						  		}
	                        },
	                        showErrors: function(errorMap, errorList) {                                                     						
 								if(errorList.length > 0) {
 									$('#grade_validation').addClass('error');
 								} else {
 									$('#grade_validation').removeClass('error');
 								}
  							}
	                    });
					</script>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			$('#question_answer_grade .toggler').click( function() {
				var img = $(this).find('img');
				if( img.attr('src').indexOf('closed') != -1) {
					img.attr('src', '<c:url value="/img/open.gif"/>');
				} else { 
					img.attr('src', '<c:url value="/img/closed.gif"/>');
				}
				$(this).next().slideToggle();
			});
		</script>
		
		<div class="row">	
			<div class="chart left">
				<c:choose>
					<c:when test="${response.eraseChars gt 0 || response.pasteChars gt 0 || response.keyPresses gt 0}">
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
									"tip": "#label#:\n#val# characters",
									"no-labels": true,
									"values": [
											{ "value": ${response.eraseChars}, "label": "Deleted" },
											{ "value": ${response.pasteChars}, "label": "Pasted" },
											{ "value": ${response.keyPresses}, "label": "Typed" }
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
					<c:when test="${response.hesitationTime gt 0 || response.awayTime gt 0 || response.typingTime gt 0 || response.reviewingTime gt 0}">
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
											"value": ${response.hesitationTime},
											"label": "Hesitation:\n${tt:formatLong(response.hesitationTime)}"
												},{
											"value": ${response.awayTime},
											"label": "Away:\n${tt:formatLong(response.awayTime)}"
												},{
											"value": ${response.typingTime},
											"label": "Typing:\n${tt:formatLong(response.typingTime)}"
												},{
											"value": ${response.reviewingTime},
											"label": "Reviewing:\n${tt:formatLong(response.reviewingTime)}"
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
		
		<div class="row">	
			<div class="chart left">
				<c:choose>
					<c:when test="${correctnessStat.mean != null}">
						<div id="correctness_chart"></div>
						<script type="text/javascript">
							swfobject.embedSWF("<c:url value='/img/open-flash-chart.swf'/>", "correctness_chart", "340", "225", "9.0.0", "expressInstall.swf",
												{"get-data":"getCorrectness"});
												
							function getCorrectness() {
								var specialPoints = [{
									value: '<c:out value="${response.grade.scores.function}"/>',
									label: '<c:out value="${candidate.name}"/>'
								}];
								
								return openapplicant.admin.helper.analytics.getGradeChart("Correctness", ${correctnessStat.mean}, ${correctnessStat.stddev}, specialPoints);
							}
						</script>
					</c:when>
					<c:otherwise>
						This question has no correctness grades.
					</c:otherwise>
				</c:choose>
			</div>		
			<div class="chart right">
				<c:choose>
					<c:when test="${styleStat.mean != null}">
						<div id="style_chart"></div>
						<script type="text/javascript">
							swfobject.embedSWF("<c:url value='/img/open-flash-chart.swf'/>", "style_chart", "340", "225", "9.0.0", "expressInstall.swf",
												{"get-data":"getStyle"});
	
							function getStyle() {
								var specialPoints = [{
									value: '<c:out value="${response.grade.scores.form}"/>',
									label: '<c:out value="${candidate.name}"/>'
								}];
								
								return openapplicant.admin.helper.analytics.getGradeChart("Style", ${styleStat.mean}, ${styleStat.stddev}, specialPoints);
							}	
						</script>
					</c:when>
					<c:otherwise>
						This question has no style grades.
					</c:otherwise>
				</c:choose>
			</div>
		</div><!-- /row -->
		
		<div class="row">	
			<div class="chart left">
				<div id="totalTime_chart"></div>
				<script type="text/javascript">
					swfobject.embedSWF("<c:url value='/img/open-flash-chart.swf'/>", "totalTime_chart", "340", "225", "9.0.0", "expressInstall.swf",
										{"get-data":"getTotalTime"});
										
					function getTotalTime() {
						var specialPoints = [];
						
							specialPoints.push({
								value: '<c:out value="${response.totalTime}"/>',
								label: '<c:out value="${candidate.name}"/>'
							});
						
						return openapplicant.admin.helper.analytics.getPercentileChart("Total Time", ${totalTimeStat.mean}, ${totalTimeStat.stddev}, specialPoints);
					}
				</script>
			</div>		
			<div class="chart right">
				<div id="keyChars_chart"></div>
				<script type="text/javascript">
					swfobject.embedSWF("<c:url value='/img/open-flash-chart.swf'/>", "keyChars_chart", "340", "225", "9.0.0", "expressInstall.swf",
										{"get-data":"getKeyChars"});
	
					function getKeyChars() {
						var specialPoints = [{
							value: '<c:out value="${response.keyChars}"/>',
							label: '<c:out value="${candidate.name}"/>'
						}];
						
						return openapplicant.admin.helper.analytics.getPercentileChart("Response Length", ${keyCharsStat.mean}, ${keyCharsStat.stddev}, specialPoints);
					}	
				</script>
			</div>
		</div><!-- /row -->
	</c:when>
	<c:otherwise>
		<h1>No response.</h1>
	</c:otherwise>
</c:choose>
</div>