;(function() {

oltk.namespace('oltk.util');

//============================================================================
// TO STRING BUILDER
//============================================================================
/**
 * Helper class for converting objects to descriptive strings.
 * NOTE: this class is for debugging and does not produce valid JSON.
 * 
 * Usage: 
 * var builder = new oltk.util.ToStringBuilder(data);
 * var descriptiveString = builder.toString();
 * 
 * @param {any} data the data to convert.
 * @param {int} [depth] the maximum object depth.  Defaults to 1.
 * @param {Object} [o] a hash of options
 * {
 * 		inspectWindow:		{boolean} true if properties of the global window
 * 								object should be included in the output.  This may occur,
 * 								for instance, if data has a reference to window. 
 * 								Defaults to false.
 * 		inspectDom:			{boolean} true if dom properties should be included
 * 								in the output.  If false, only a short list of 
 * 								dom properties will be included.
 * 								Defaults to false.
 * }
 */
oltk.util.ToStringBuilder = function(data, depth, o) {
	o = o || {};
	depth = parseInt(depth) || 1;
	depth = Math.max(depth, 1);
	
	this._data = data;
	this._recursionDepth = depth;
	this._inspectWindow = o.inspectWindow || false;
	this._inspectDom = o.inspectDom || false;
};

oltk.util.ToStringBuilder.prototype = {
	
	/**
	 * Don't print these dom properties.
	 * @private
	 * @type object
	 */
	_skipDomProperties: {
		document:true, ownerDocument:true,
		all:true,
		parentElement:true, parentNode:true, offsetParent:true,
		children:true, firstChild:true, lastChild:true,
		previousSibling:true, nextSibling:true,
		innerHTML:true, outerHTML:true,
		innerText:true, outerText:true, textContent:true,
		attributes:true,
		style:true, currentStyle:true, runtimeStyle:true,
		parentTextEdit:true
	},
	
	/**
	 * @return {string} the data converted to a descriptive string.
	 */
	toString: function() {
		return this._toRecursiveString(this._data, this._recursionDepth, 0);
	},
	
	/**
	 * @private
	 * @param {any} data
	 * @param {int} objectDepth
	 * @param {int} indentDepth
	 */
	_toRecursiveString: function(data, objectDepth, indentDepth) {
		if(typeof data === 'undefined') {
			return 'undefined';
		}
		else if(data === null) {
			return 'null';
		}
		
		// function
		else if(typeof data === 'function') { 
			return 'function';
		}
		
		// date
		else if(typeof data.toUTCString === 'function') { 
			return data.toUTCString();
		}
		
		// string
		else if(typeof data === 'string') { 
			var self = this;
			var result = data.replace(/\n|\r/g, function(ch) {
				switch(ch) {
					case '\n': return self._newLine(indentDepth + 1);
					case '\r': return '';
				}
			});
			return '"' + result + '"';
		}
		
		// array or arguments
		else if(typeof data.length === 'number' && (data.join || data.callee)) { 
			var result = ['['];
			for(var i=0; i<data.length; i++) {
				result.push(this._newLine(indentDepth + 1));
				result.push(this._toRecursiveString(data[i], objectDepth, indentDepth + 1));
				if(i < data.length - 1) {
					result.push(', ');
				} else {
					result.push(this._newLine(indentDepth));
				}
			}
			result.push(']');
			return result.join('');
		}
		
		// window short string
		else if(data === window && !this._inspectWindow) { 
			return this._detailedTypeOf(data); 
		}
		
		// node short string
		else if(this._isDomNode(data) && !this._inspectDom) { 
			var result = [this._detailedTypeOf(data)];
			if(data.id) {
				result.push(' id: ', data.id);
			}
			if(data.className) {
				result.push(' className: ', data.className);
			}
			return result.join('');
		}
		
		// object
		else if(typeof data === 'object') { 
			if(objectDepth == 0) {
				return this._detailedTypeOf(data);
			}
			var result = [];
			if(this._detailedTypeOf(data) != 'Object') {
				result.push(this._detailedTypeOf(data));
				result.push(' ');
			}
			result.push('{');
			for(var p in data) {
				try {
					if(this._isDomNode(data)) { 
						if(!data[p]) continue;
						if(typeof data[p] === 'function') continue;
						if(this._skipDomProperties[p]) continue;
						if(p.toUpperCase() === p) continue;
					}
					result.push(this._newLine(indentDepth + 1));
					result.push(p);
					result.push(': ');
					result.push(this._toRecursiveString(data[p], objectDepth -1, indentDepth + 1));
					result.push(',');
				} catch(e) {
					continue;
				}
			}
			if(result.length > 1) {
				if(result[result.length -1] === ','){
					result.pop();
				}
				result.push(this._newLine(indentDepth));
			}
			result.push('}');
			return result.join('');
		} 
		
		// booleans, numbers, etc.
		else {
			return data + '';
		}
	},
	
	/**
	 * @private
	 * @param {any} x
	 * @return {string}
	 */
	_detailedTypeOf: function(x) {
		var result = typeof x;
		if(result == 'object') {
			result = Object.prototype.toString.apply(x); // returns '[object class]'
			result = result.substring(8, result.length -1); // just get the class part
		}
		return result;
	},
	
	/**
	 * @private
	 * @param {int} indent
	 * @return {string}
	 */
	_newLine: function(indent) {
		var sb = ['\n'];
		for(var i=0; i<indent; i++) {
			sb.push('\u00A0\u00A0')
		}
		return sb.join('');
	},
	
	/**
	 * @private
	 * @param {Object} o the object to test.
	 * @return {boolean} true if the given object is a dom node.
	 */
	_isDomNode: function(o) {
		return o && typeof o.nodeName === 'string';
	}
};
	
})();
