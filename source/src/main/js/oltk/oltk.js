;(function() {

// prevent overwriting	
if(window.oltk) {
	return;
}

//============================================================================
// OLTK
//============================================================================
/**
 * The oltk object is a single global object. It contains depdendency management
 * functions and utility methods.
 * oltk resolves file dependencies by establishing a 'scriptpath'.
 * The scriptpath is defined as the url of
 * the directory containing the oltk source folder.  
 * Scripts packaged in subdirectories beneath this directory can be loaded 
 * dynamically using the oltk.include function.
 *
 * Example: A directory structure using the oltk scriptpath
 *
 * webapp
 *   script
 *	   oltk <-- the oltk source folder
 *	   com
 *	     myproj
 *	       MyModule.js
 *
 * MyModule.js can now be loaded using: oltk.include('com/myproj/MyModule.js');
 * 
 * @class oltk
 * @static
 */
oltk = {
	
	//=======================================================================
	// MEMBERS
	//=======================================================================
	/**
	 * Cache of included scripts
	 * 
	 * @type array
	 * @static
	 * @private
	 * @memberOf oltk
	 */
	_scripts: [],

	
	//=======================================================================
	// DEPENDENCY MANAGEMENT
	//=======================================================================
	
	//------------------------------------------------------------------------
	// GET SCRIPTPATH
	//------------------------------------------------------------------------
	/**
	 * Returns the scriptpath of the project. 
	 *
	 * @method getScriptpath;
	 * @static
	 * @memberOf oltk
	 * @return {String} the url of the scriptpath
	 */
	getScriptpath: function() {
		var result = new OltkPathResolver().resolve();
		
		oltk.getScriptpath = function() { return result; };
		return oltk.getScriptpath();
	},

	//-----------------------------------------------------------------------
	// IMPORTS
	//-----------------------------------------------------------------------
	/**
	 * Loads classes from the project's scriptpath.
	 *
	 * The scriptpath is the base URL of the oltk script.
	 * Package names follow the Java convention: Packages are physically 
	 * located in nested directories of the scriptpath.  Package names are
	 * the same as the path, using dots as delimiters.  For example, given
	 * the following directory structure:
	 *
	 *		<scriptpath>
	 *			oltk
	 *				...
	 *			example
	 *				foo
	 *					Bar.js
	 *					Baz.js
	 *					Fuz.min.js
	 *
	 * the classes Bar, Baz and Fuz could be imported using the following:
	 *
	 *		oltk.imports(
	 *			'example.foo.Bar',
	 *			'example.foo.Baz',
	 *			'example.foo.Fuz\\.min'  
 	 *		);
 	 *
	 * Use '\\' to escape '.' characters in the filename.
	 * This method is synchronous, so imported classes may be used immediately
	 * after the imports statement.
	 *
	 * @memberOf	oltk
	 * @method		import
	 * @static
	 * @param		{string*} varags - The fully qualified names of the classes
	 *					to load.
	 */
	imports: function(varargs) {
		for(var i=0; i<arguments.length; i++) {
			var classname = arguments[i];
			if(classname) {
				var path = classname.replace(/(\.|\\\.)/g, function(match){
					return match == '.' ? '/' : '.';
				});
				oltk.include(path + '.js');
			}
		}
	},
	
	//-----------------------------------------------------------------------
	// INCLUDE
	//-----------------------------------------------------------------------
	/**
	 * Loads a script in the project's scriptpath.
	 * The scriptpath is the url of the directory 
	 * containing the oltk source folder.  
	 *
	 * Example: A directory structure using the oltk scriptpath
	 *
	 * webapp
	 *   script
	 *	   oltk <-- the oltk source folder
	 *	   yahoo     <-- the yui build folder
	 *	     yui
	 *			dom
	 *	         dom-min.js
	 *
	 * dom-min.js can now be loaded using: 
	 * 
	 *		oltk.include('yahoo/yui/dom/dom-min.js');
	 * 
	 * oltk.include is synchronous, so included scripts may be used immediately
	 * after they are loaded.
	 * 
	 * Example:
	 * 
	 * oltk.include('yahoo/yui/dom/dom-min.js');
	 * 
	 * YAHOO.util.Dom.get('id'); // can be used right away.
	 * 
	 * @memberOf oltk
	 * @method include
	 * @static
	 * @param {String} file the javascript file to include
	 */
	include: function(file) {
		if(!file) {
			return;
		}
		var url = oltk.getScriptpath() + file;
		
		if(oltk._scripts[url]) {
			return;
		}
		var js = oltk.download(url);
		js = '/* ' + url + ' */\n' + js; // make script easier to spot in firebug
		
		oltk._scripts[url] = true;
		oltk.eval(js);
		oltk._scripts.push(url);
	},
	

	//=======================================================================
	// UTILITY METHODS
	//=======================================================================

	//-----------------------------------------------------------------------
	// EMPTY FN
	//-----------------------------------------------------------------------
	/**
	 * A reusable empty function.
	 * @type function
	 * @static
	 * @memberOf oltk
	 */
	emptyFn: function(){},
	
	//-----------------------------------------------------------------------
	// NAMESPACE
	//-----------------------------------------------------------------------
	/**
	 * Returns the namespace specified and creates it if it does not exist.
	 * 
	 * oltk.namespace("oltk.property.package");
	 *
	 * Will create oltk.property, then oltk.property.package.
	 * Be careful when naming packages. Reserved words may work in some browsers
	 * and not in others.  For instance, the following will fail in Safari:
	 *
	 * oltk.namespace("really.long.nested.namespace");
	 *
	 * This fails because "long" is a future reserved word in ECMAScript
	 *
	 * @method namespace
	 * @static
	 * @memberOf oltk
	 * @param {String} nspace  The namespace to create
	 * @return {Object} A reference to the last namespace object created.
	 */
	namespace: function(nspace) {
		if(!nspace) {
	        return;
		}
		if(_notValidNamespace(nspace)) {
			throw 'oltk.namespace: illegal namespace: ' + nspace;
		}
	    var n = nspace.split('.');
	    var o = window;
	    for(var i=0; i<n.length; i++) {
	        o[n[i]] = o[n[i]] || {};
	        o = o[n[i]];
	    }
	    return o;
	},
	
	//-----------------------------------------------------------------------
	// EVAL
	//-----------------------------------------------------------------------
	/**
	 * Cross browser implementation of window.eval.  Use this method to 
	 * eval javascript in the global scope.
	 *
	 * @method eval
	 * @static
	 * @memberOf oltk
	 * @param {string} js the javascript to eval
	 */
	eval: function(js) {
		// IE
		if(window.execScript) {
			window.execScript(js); 
		}
		// SAFARI
		else if(/KHTML/.test(navigator.userAgent) || 
			(/AppleWebKit\/([^\s]*)/).test(navigator.userAgent)) {
			
			// see http://webreflection.blogspot.com/2007/08/global-scope-evaluation-and-dom.html
			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.appendChild(document.createTextNode(js));
			var head = document.getElementsByTagName('head')[0] || document.documentElement;
			head.insertBefore(script, head.firstChild);
			head.removeChild(script);
		}
		// MOZILLA, OPERA
		else {
			window.eval(js);
		}
	},
	
	//-----------------------------------------------------------------------
	// CREATE REQUEST
	//-----------------------------------------------------------------------
	/**
	 * Creates a cross browser XMLHttpRequest object.
	 * @static
	 * @memberOf oltk
	 * @return {XMLHttpRequest}
	 */
	createRequest: function() {
		var methods = [
			function() { return new ActiveXObject('MSXML2.XMLHTTP'); },
			function() { return new ActiveXObject('Microsoft.XMLHTTP'); },
			function() { return new XMLHttpRequest(); }
		];
		for(var i=0; i<methods.length; i++) {
			try {
				methods[i]();
			}
			catch(e) {
				continue;
			}
			oltk.createRequest = methods[i];
			return oltk.createRequest();
		}
		throw 'oltk.createRequest: could not create XmlHttpRequest';
	},
	
	//-----------------------------------------------------------------------
	// DOWNLOAD
	//-----------------------------------------------------------------------
	/**
	 * Downloads a file synchronously.
	 * @memberOf oltk
	 * @param {url} the url to download
	 * @return {string} the file's contents as a string.
	 */
	download: function(url) {
		var req = oltk.createRequest();
		req.open('GET', url, false);
		req.send(null);
		if(!_isDocumentOk(req)) {
			throw 'oltk.download: url ' + url + ' status: ' + req.status;
		}
		return req.responseText;
	},
	
	//-----------------------------------------------------------------------
	// BIND
	//-----------------------------------------------------------------------
	/**
	 * Wraps this function in another, locking its execution scope to an 
	 * object sepecified by thisObj.
	 * @example
	 * <pre>
	 * 		var obj = {
	 * 			name: 'A nice demo',
	 * 			fx: function() {
	 * 				alert(this.name);
	 * 			}
	 * 		};
	 * 
	 * 		var fx2 = oltk.bind(obj.fx, obj);
	 * 		fx2(); // => alerts 'A nice demo'
	 * </pre>
	 * @param {Function} fn the function who's scope to bind.
	 * @param {Object} thisObj the object who's scope to execute the function in.
	 * @param {array} [args]Overrides arguments for the call.
	 * Defaults to the arguments passed by the caller.
	 * @param {boolean|number} [appendArgs] if true args are appened to 
	 * call args instead of overriding.  If a number the args are inserted 
	 * at the sepecified position.
	 * @return {Function} the new function.
	 */
	bind: function(fn, thisObj, args, appendArgs) {
		var method = fn;
		return function() {
			var callArgs = args || arguments;
			if(appendArgs === true) {
				callArgs = Array.prototype.slice.call(arguments, 0);
				callArgs = callArgs.concat(args);
			}
			else if(typeof appendArgs == 'number') {
				callArgs = Array.prototype.slice.call(arguments, 0);
				var applyArgs = [appendArgs,0].concat(args);
				Array.prototype.splice.apply(callArgs, applyArgs);
			}
			return method.apply(thisObj || window, callArgs);
		};
	},
	
	//=======================================================================
	// LOG
	//=======================================================================
	/**
	 * Internal logger used by oltk.  This stub implementation is meant to be 
	 * overridden.
	 * 
	 * @memberOf oltk
	 * @static
	 * @type Object
	 */
	log: {
		
		/**
		 * Log a trace message
		 * @memberOf oltk.log
		 * @param {string} msg the message to log
		 * @param {Obejct} [o] an optional object to log
		 */
		trace: function(msg, o){},
		
		/**
		 * @memberOf oltk.log
		 * @return {boolean}
		 */
		isTraceEnabled: function() { return false; },
		
		/**
		 * Log a debug message.
		 * @memberOf oltk.log
		 * @param {string} msg the message to log
		 * @param {Object} [o] an optional object to log
		 */
		debug: function(msg, o){},
		
		/**
		 * @memberOf oltk.log
		 * @return {boolean}
		 */
		isDebugEnabled: function() { return false; },
		
		/**
		 * Log an info message.
		 * @memberOf oltk.log
		 * @param {string} msg the message to log.
		 * @param {Object} [o] an optional object to log.
		 */
		info: function(msg, o){},
		
		/**
		 * @return {boolean}
		 */
		isInfoEnabled: function() { return false; },
		
		/**
		 * Log a warning message
		 * @memberOf oltk.log
		 * @param {string} msg the message to log
		 * @param {Error} [e] an optional error to log.
		 */
		warn: function(msg, e){},
		
		/**
		 * @memberOf oltk.log
		 * @return {boolean}
		 */
		isWarnEnabled: function() { return false; },
		
		/**
		 * Log an error message.
		 * @memberOf oltk.log
		 * @param {string} msg the message to log
		 * @param {Error} [e] an optional error to log
		 */
		error: function(msg, e){},
		
		/**
		 * @memberOf oltk.log
		 * @return {boolean}
		 */
		isErrorEnabled: function() { return false; },
		
		/**
		 * Log a fatal message.
		 * @memberOf oltk.log
		 * @param {string} msg the message to log
		 * @param {Error} [e] an optional error to log
		 */
		fatal: function(msg, e){},
		
		/**
		 * @memberOf oltk.log
		 * @return {boolean}
		 */
		isFatalEnabled: function() { return false; }
	}
};

//============================================================================
// PRIVATE IMPLEMENTATION
//============================================================================
/**
 * @private
 * @param {string} s the string to check
 * @return {boolean}
 */
function _notValidNamespace(s) {
	if(!s) {
		return true;
	}
	return /(^\.|\.\.|\.$)/.test(s);
};

/**
 * @private
 * @return {HTMLElement} the oltk script node.
 */
function _oltkScript() {
	var scripts = document.getElementsByTagName('script');
	for(var i=scripts.length -1; i>=0; i-- ) {
		if(/oltk\.js/.test(scripts[i].src)) {
			return scripts[i];
		}
	}
	throw 'oltk._oltkScript: could not locate oltk script';
};

/**
 * @private
 * @param {XMLHttpRequst} http
 * @return {boolean}
 */
function _isDocumentOk(http) {
	var stat = http.status || 0;
    return( (stat >= 200) && (stat < 300)) || 
        (stat==304) || 
        (stat==1223) || 
        (!stat && (location.protocol=='file:' || location.protocol=='chrome:'));
};

//============================================================================
// OLTK PATH RESOLVER
//============================================================================
/**
 * Method object for resolving the oltk script path.
 * @private
 */
var OltkPathResolver = function() {
	
};

OltkPathResolver.prototype = {
	
	/**
	 * @return {string} the absolute url of the script path's context.
	 */
	resolve: function() {
		var path = this._getRawPath();
		if(this._isAbsoluteURL(path)) {
			return path;
		}
		else {
			return this._toAbsoluteURL(path);
		}
	},
	
	/**
	 * @private
	 * @return {string} the raw path to oltk/oltk.js
	 */
	_getRawPath: function() {
		var src = _oltkScript().src;
		
		if(/oltk\/oltk\.js/.test(src)) {
			return src.replace(/oltk\/oltk\.js.*/,'');
		}
		// src is a relative path in the oltk directory
		else if(/oltk\.js/.test(src)){
			return src.replace(/oltk\.js.*/, '') + '../';
		}
		else {
			throw 'OltkPathResolver.getRawPath: could not locate path to oltk.js';
		}
	},
	
	/**
	 * @private
	 * @param {string} path
	 * @return {boolean}
	 */
	_isAbsoluteURL: function(path) {
		var startsWithProtocol = new RegExp('^'+location.protocol);
		return startsWithProtocol.test(path);
	},
	
	/**
	 * @private
	 * @param {string} path
	 * @return {string}
	 */
	_toAbsoluteURL: function(path) {
		if(/^\//.test(path)) {
			var i = location.href.indexOf(location.host) + location.host.length;
			var base = location.href.substring(0, i);
			return base + path;
		}
		var base = location.href;
		base = base.substring(0, base.lastIndexOf('/'));
		while(/^\.\./.test(path)) {
			base = base.substring(0, base.lastIndexOf('/'));
			path = path.substring(3);
		}
		return base + '/' + path;
	}
};

})();
