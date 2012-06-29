oltk.namespace('openapplicant.admin.helper');

oltk.include('jquery/jquery.js');
oltk.include('jquery/ui/ui.core.js');
oltk.include('jquery/ui/ui.slider.js');

(function($) {

var snapshots = [];

/**
 * The dom elements that the player attaches to: 
 *  - screen
 *  - playButton
 *  - clock
 *  - position
 */
var dom = {};

var playing;
var max;
var time;
var lastTick;
var screenFrame;
var speed;

var self = openapplicant.admin.helper.player = {

	/**
	 * Provides high level interaction with the output of {@link recorder} and controls.
	 * @author tommy, ripping off tom's better way to write this
	 * @constructor
	 * @requires jQuery
	 * @requires oltk
	 * @requires StringDelta
	 *
	 * @param String[] a compressed array of snapshots
	 * @param Object an object containing dom elements 
	 */
	init: function(s, els) {
	
		if(s == undefined)
			return;
			
		self.snapshots = snapshots = s;
		self.dom = dom = els;
		playing = false;	

		max = snapshots[snapshots.length-1].m;
		time = max;
		lastTick = null;
		screenFrame = snapshots.length-1;
		
				
		dom.position.slider({
			animate: false,
			handle: self.dom.handle,
			startValue: 0,
			max: max,
			slide: self.sliderChange
		});
		
		dom.playButton.bind('click', self.toggle);
		
		setTimeout(function() { dom.position.slider('moveTo', max) }, 1);
		
		self.setSpeed(1);
		self.update();
	},
	
	/**
	 * For external buttons to set the speed.
	 */
	setSpeed: function(s) {
		speed = s;
		self.updateSpeed();
	},
	
	/**
	 * Doubles the speed of the player until it gets to ludacris speed.
	 */
	faster: function() {
		speed = speed*2;
		if(speed == 32)
		   speed = 16;
		self.updateSpeed();
	},
	
	slower: function() {
		speed = speed/2;
		if(speed == 1/2)
		   speed = 1;
		self.updateSpeed();
	},
	
	updateSpeed: function() {
		var html = '';
		for(var i=1; i<32; i*=2) {
			html += '<a onclick="openapplicant.admin.helper.player.setSpeed('+i+');"><span' + (i==speed?' class="selected"':'') + '>' + i + 'x</span></a>\n';
		}
		dom.speed.html(html);
	},
	
	/**
	 * Switches the state of the player.
	 * 
	 * @private
	 */
	toggle: function() {
		if(playing)
			self.pause();
		else
			self.play();
	},
	
	/**
	 * @private
	 */
	play: function () {
		if(time == max)
		   time = 0;
		playing = true;
		dom.playButton.attr("src", dom.pauseImagePath);		
		self.tick();
	},
	
	/**
	 * @private
	 */
	pause: function () {
		playing = false;
		dom.playButton.attr("src", dom.playImagePath);
	},
	
	stop: function () {
		self.pause();
		time = max;
		self.update();
		dom.position.slider("moveTo", time);
	},
	
	/**
	 * @private
	 */
	tick: function() {
		
		if(!playing) {
			lastTick = null;
			return;
		} 
		else if(!lastTick) {
			lastTick = new Date().getTime();
		} 
		else {	
			var realtime = new Date().getTime();
			time += (realtime - lastTick) * speed;
			lastTick = realtime;
			if(time > max) {
				self.pause();
				time = max;
			}
			self.update();
			dom.position.slider("moveTo", time);
		}
		
		// http://en.wikipedia.org/wiki/Frame_rate
		setTimeout(self.tick, 41);
	},
	
	update: function() {
			var index = self.binaryFind();
			if(index != screenFrame) {
				screenFrame = index;
				dom.screen.val(snapshots[index].t);
			}

			var seconds = Math.floor(time/1000);
			var tenths = Math.floor(time/100)%10;
			tenths = tenths==0?'0':tenths;
			dom.clock.text(seconds+'.'+tenths+'s');
	},
	
	
	/**
	 * @private
	 */
	sliderChange: function(e,ui) {
	
		/* If there is no event then this is sliding as a result of the
		 * slider("moveTo") method and not someone dragging it. */
		if(!e)
			return;
		
		time = ui.value;
		self.update();
	},
	
	//http://en.wikipedia.org/wiki/Binary_search <- handy
	binaryFind: function() {
		var low = 0;
	    var high = snapshots.length - 1;
	       while (low <= high) {
	           var mid = Math.floor((low + high) / 2);
	           if (snapshots[mid].m > time)
	               high = mid - 1;
	           else if (snapshots[mid].m < time) {
	        	   if(mid+1 < snapshots.length && snapshots[mid+1].m > time)
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