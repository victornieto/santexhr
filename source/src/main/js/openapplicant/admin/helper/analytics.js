oltk.namespace('openapplicant.admin.helper');

oltk.include('openapplicant/admin/helper/StringDelta.js');
oltk.include('openapplicant/admin/helper/json2.js');
oltk.include('openapplicant/admin/helper/zCalc.js');

(function($) {

var endTime; // the most recently added set of events end time
var keypressEvents;
var pasteEvents;
var focusEvents;

var granularity = 400;
	
var self = openapplicant.admin.helper.analytics = {
	
	init: function() {
		endTime = 0;
		keypressEvents = [];
		pasteEvents = [];
		focusEvents = [];
	},
	
	addKeypressEvents: function(compressedKeypressEvents) {
		
		if(undefined == compressedKeypressEvents)
			return;
		
		var decompressedEvents = StringDelta.decompressKeypresses(compressedKeypressEvents);
		
		if (keypressEvents.length != 0)
			endTime = keypressEvents[keypressEvents.length-1].m;
			
		keypressEvents = self._addEvents(keypressEvents, decompressedEvents);
	},
	
	getKeypressEvents: function() {
		return keypressEvents;
	},
	
	addPasteEvents: function(events) {
		pasteEvents = self._addEvents(pasteEvents, events);
	},
	
	addFocusEvents: function(events) {
		focusEvents = self._addEvents(focusEvents, events);
	},
	
	_addEvents: function(local, eventsToAdd) {
		if(undefined == eventsToAdd)
			return local;
			
		if (endTime != 0) {
			$(eventsToAdd).each( function(){
				this.m += endTime;
			});
		}
		
		return local.concat(eventsToAdd);
	},
		
	/**
	 * Preps the keypressEvents and provides easier interaction with other events.
	 * 
	 * @author tommy
	 * @param keypressEvents: [{m: milliseconds: t: text}]
	 * @param pasteEvents: [{m: milliseconds, d: diff}]
	 * @param focusEvents: [{m: milliseconds: t: type}]
	 * }
	 */
	getResponseGraph: function() {		
			
		var charValues = [];
		
		var pasteValues = [];
		var pasteI = 0;
		
		var awayValues = [];
		var awayI = 0;
		var away = false;
		
		var chartLabelsX = [];
		
		var maxChars = 0;
		var maxTime = keypressEvents[keypressEvents.length-1].m;
		
		for(var i=0; i<=granularity; i++) {
			
			var t1 = Math.floor((i/granularity) * maxTime);
			var t2 = Math.floor(((i+1)/granularity) * maxTime);
			var interval = t2-t1;
			
			var trailingFrame = self.binaryFind(t1);
			var chars = keypressEvents[trailingFrame].t.length;
			
			if( maxChars < chars )
			    maxChars = chars;
			
			charValues[i] = {
					value: chars,
					tip: chars + " keystrokes\n"
			};
			
			//these pastes are moved up half an interval so they are sure
			//to go before the spike in the graph
			if( pasteI < pasteEvents.length &&
					pasteEvents[pasteI].m >= (t1 + interval/2) &&
					pasteEvents[pasteI].m < (t2 + interval/2)) {

				var charsPastedOverInterval = 0;
				
					while( pasteI < pasteEvents.length && 
							pasteEvents[pasteI].m < (t2 + interval/2)) {
						
						charsPastedOverInterval += pasteEvents[pasteI].d;
						pasteI++;
					}
					
					pasteValues[i] = {
							"value": chars,
			 				"dot-size": 4,
			 				"colour": "#FF3A03"
					};
					
					charValues[i].tip += "Pasted: " + charsPastedOverInterval + " chars\n";

			}
			else {
				pasteValues[i] = {
					"value": 0,
	 				"tip": chars + " keystrokes\n"
				};
			}
			
			if( awayI < focusEvents.length &&
				focusEvents[awayI].m >= t1 &&
				focusEvents[awayI].m < t2) {

				while( awayI < focusEvents.length && 
					   focusEvents[awayI].m < t2) {
					
					away = (focusEvents[awayI].t == "focus") ? false : true;
					awayI++;
				}
			}
			
			if(away) {
				awayValues[i] = chars;
				charValues[i].tip += "Away\n";
			}
			else {
				awayValues[i] = {
					"value": 0,
	 				"tip": chars + " keystrokes\n"
				};
			}
			
			
			var seconds = Math.floor(t1/1000);
			var tenths = Math.floor(t1/100)%10;
			
			tenths = tenths==0 ? '0' : tenths+'';
			chartLabelsX[i] = seconds+'.'+tenths+'s';
		}
		
		maxChars += 4 - maxChars%4;	
		var chartSteps = maxChars/4;
		
		return	JSON.stringify({
					title: {
		 				text: "Keystrokes",
		 				style: " { font-size:20px; color:#808080; padding-top:10px; margin-bottom:-30px; margin-left:25px; text-align:left; }" 
					},
					elements: [{
						type: "area_line",
						colour: "#8CC63F",
						fill: "#8CC63F",
						values: charValues
							  },{
						type: "line_dot",
						weight: 1,
						"dot-size": 0,
				    	colour: "#BADD8B",
				    	values: pasteValues
							  },{
				    	type: "area_line",
				    	colour: "#8CC63F",
				    	fill: "#FF3A03",
				    	"fill-alpha": 0.7,
				    	values: awayValues
					}],
					y_axis: {
						min: 0,
						max: maxChars,
						steps: chartSteps,
						offset: false,
						colour: "#D5D5D5",
						"grid-colour": "#D5D5D5",
						stroke: 2
					},
					x_axis: {
						steps: 40, //want to display one out of 40 labels... 400/40 = 10
						offset: false,
						colour: "#D5D5D5",
						"grid-colour": "#D5D5D5",
						stroke: 2,
						labels: {
							steps: 40,
							labels: chartLabelsX
						}	
					},
					bg_colour: "#FFFFFF"		
				});
	
	},
	
	/**
	 * @params double average
	 * @params double standardDev
	 * @params [{value, label}]
	 */
	getGradeChart: function(title, average, standardDev, specialPoints) {
		
		if(standardDev < 3)
			standardDev = 3;
	
		var step = Math.floor(8*standardDev)/8;
		var scale = 1 / (Math.pow(2 * Math.PI, 1/2) * standardDev);

		var low = average - step * 8;
		var high = average + step * 8;
		
		//the distribution of probability values
		var distValues = [];
		var distI = 0;
		
		var maxY = 0;

		var chartLabelsX = [];
		
		for(var i=0; i <= 100; i += 2) {
			
			var shape = - Math.pow(i-average, 2) / (2 * Math.pow(standardDev,2));
			var probability = scale * Math.pow(Math.E, shape) * 100; //let's make these percentage points.. easier on the charts
			
			var percentile = calc_z((i-average)/standardDev) * 100;
			percentile = Math.floor(percentile*100)/100;
			
			if(probability < Math.pow(10,-5)) //is javascript going to display this in scientific notation and thus throw an error in the chart?
				probability = 0;
			probability = Math.floor(probability*100)/100;
			
			if(	maxY < probability)
				maxY = probability;
			
			distValues[distI] = {
					value: probability,
					tip:   "Score: " + i + "\n" + 
						   percentile + " percentile\n" +
						   probability + "% likelihood\n"
			};
			
			for(var p=0; p<specialPoints.length; p++) {
				if( specialPoints[p].value == "" )
					continue;
				
				if( specialPoints[p].value >= i &&
					specialPoints[p].value < i+2) {
					
					distValues[distI]["dot-size"] = 4;
					distValues[distI]["colour"] = "#8CC63F";
					distValues[distI]["tip"] = specialPoints[p].label + "\n" + distValues[distI]["tip"]; 
				}		
			}
			
			chartLabelsX[distI] = i+"";
			distI++;
		}
		
		maxY += 2 - maxY%2;
		var chartSteps = maxY/2;
		
		return	JSON.stringify({
					title: {
						text: title,
						style: "{font-size: 20px; color: #808080; margin-left: 47px; margin-bottom: 10px; text-align: left;}"
					},
					elements: [{
						type: "line_dot",
						"dot-size": 2,
						tip: "#val#%",
						values: distValues
				  }],
				  y_axis: {
						min: 0,
						max: maxY,
						steps: chartSteps,
						colour: "#D5D5D5",
						"grid-colour": "#FFFFFF",
						stroke: 2
				  },
				  x_axis: {
						colour: "#D5D5D5",
						"grid-colour": "#FFFFFF",
						labels: {
							steps: 10,
							labels: chartLabelsX
						}
				  },
				  bg_colour: "#FFFFFF"
			});
	},
	
	/**
	 * @params double average
	 * @params double standardDev
	 * @params [{value, label}]
	 */
	getPercentileChart: function(title, average, standardDev, specialPoints) {
		
		if(standardDev < 3)
			standardDev = 3;
		
		var scale = 1 / (Math.pow(2 * Math.PI, 1/2) * standardDev);

		var low = Math.floor(average - standardDev * 3);
		var high = Math.floor(average + standardDev * 3);

		low -= low%5;
		
		if(low < 0)
			low = 0;
		
		high += 5 - high%5;
		
		var step = (high - low) / 5;
		
		var maxY = 0;
		
		var distValues = [];
		var chartLabelsX = [];
		
		var distI = 0;
		
		for(var i=low; i <= high; i += step/10) {
			
			var shape = - Math.pow(i-average, 2) / (2 * Math.pow(standardDev,2));
			
			var probability = scale * Math.pow(Math.E, shape) * 100; //let's make these percentage points.. easier on the charts		
			probability = Math.floor(probability*100)/100;
			
			var percentile = calc_z((i-average)/standardDev)*100;
			percentile = Math.floor(percentile*100)/100;
			if(percentile < Math.pow(10,-5)) //is javascript going to display this in scientific notation and thus throw an error in the chart?
				percentile = 0;
									
			distValues[distI] = {
					value: percentile,
					tip:   "Value: " + Math.floor(i) + "\n" + 
						   percentile + " percentile\n"
			};
			
			for(var p=0; p<specialPoints.length; p++) {
				if( specialPoints[p].value == "" )
					continue;
				
				if( specialPoints[p].value >= i &&
					specialPoints[p].value < i+step/10) {
					
					distValues[distI]["dot-size"] = 4;
					distValues[distI]["colour"] = "#8CC63F";
					distValues[distI]["tip"] = specialPoints[p].label + "\n" + distValues[distI]["tip"]; 
				}		
			}
			
			chartLabelsX[distI] = Math.ceil(i)+"";
			distI++;
		}
				
		return	JSON.stringify({
					title: {
						text: title,
						style: "{font-size: 20px; color: #808080; margin-left: 47px; margin-bottom: 10px; text-align: left;}"
					},
					elements: [{
						type: "line_dot",
						"dot-size": 2,
						tip: "#val#%",
						values: distValues
				  }],
				  y_axis: {
						min: 0,
						max: 100,
						steps: 20,
						colour: "#D5D5D5",
						"grid-colour": "#FFFFFF",
						stroke: 2
				  },
				  x_axis: { 
						colour: "#D5D5D5",
						"grid-colour": "#FFFFFF",
						labels: {
					  		steps: 10,
							labels: chartLabelsX
						}
				  },
				  bg_colour: "#FFFFFF"
			});
	},
	
	//http://en.wikipedia.org/wiki/Binary_search <- handy
	binaryFind: function(time) {
		var low = 0;
		var high = keypressEvents.length - 1;
		while (low <= high) {
			var mid = Math.floor((low + high) / 2);
			if (keypressEvents[mid].m > time)
				high = mid - 1;
			else if (keypressEvents[mid].m < time) {
				if(mid+1 < keypressEvents.length && keypressEvents[mid+1].m > time)
					return mid;
				low = mid + 1;
			}
			else
				return mid;
		}
		return 0;
	}
	
};
	
})(jQuery);