;(function(){
	
oltk.namespace('openapplicant.quiz.view');

oltk.include('ejs/ejs.js');
oltk.include('ejs/view.js');
oltk.include('jquery/jquery.js');
oltk.include('oltk/util/StringUtils.js');
oltk.include('oltk/util/StringEscapeUtils.js');

oltk.include('openapplicant/quiz/controller/ApplicationController.js');

var StringUtils = oltk.util.StringUtils;
var StringEscapeUtls = oltk.util.StringEscapeUtils;

//============================================================================
// EJS HELPER
//============================================================================
/**
 * EjsView extensions.
 */
var proto = EjsView.prototype;

/**
 * Override EjsView.tag to prevent boolean attributes from rendering if their value 
 * evaluates to false or the string 'false'. If not false, this method 
 * auto corrects boolean attributes to be xhtml compliant (eg. disabled="disabled")
 * 
 * @see http://www.w3.org/TR/xhtml1/#C_10
 * 
 * @example
 * // Before overriding
 * tag('input', {disabled:null, type:'text', value:'foo', name:'foo-field'},'/>')
 * // => '<input type="text" disabled="" value="foo" name="foo-field" />'
 * 
 * // After overriding
 * tag('input', {disabled:null, type:'text', value:'foo', name:'foo-field'}, '/>')
 * // => '<input type="text" value="foo" name="foo-field" />'
 * 
 * tag is used internally for nearly all tag helper methods so
 * this behavior will be applied to higher level functions as well.
 * 
 * @Override
 * @param {string} tag the tag to create (eg. 'input', 'br', 'div');
 * @param {Object} html_options attributes to apply to the tag
 * eg. {type:'text', value:'foo', Class:'foo-class', disabled:false}
 * Note that the capital 'Class' attribute translates to the html 'class'
 * attribute.  Any values set to null or undefined will not render as text.
 * @param {string} end the end tag (eg. '/>')  defaults to '>'
 */
proto.tag = function(tag, html_options, end) {
	end = end || '>'; // this default for the original tag method.  Do not change.
	var txt = [' '];
	for(var attr in html_options) if(html_options.hasOwnProperty(attr)){
		var value = html_options[attr];
		if(_booleanAttribute(attr)) {
			if(value && value != 'false' && value != 'FALSE') {
				txt.push(attr,'=','"',attr,'" ');
			}
		}
		else {
			value = (value != null && typeof value != 'undefined') ? value.toString() : '';
			if(attr == 'Class' || attr == 'klass' || attr == 'clazz') {
				attr = 'class';
			}
			if(value.indexOf("'") != -1) {
				txt.push(attr,'=\"',value,'\" ');
			} else {
				txt.push(attr,"='",value,"' ");
			}
		}
	}
	return '<'+tag+txt.join('')+end;
};

/**
 * @see http://www.w3.org/TR/xhtml1/#C_10
 * @param {string} attr
 * @return {boolean}
 */
function _booleanAttribute(attr) {
	attr = jQuery.trim(attr.toLowerCase());
	return attr === 'compact' ||	
		attr === 'nowrap' ||
		attr === 'ismap' ||
		attr === 'declare' ||
		attr === 'noshade' ||
		attr === 'checked' ||
		attr === 'disabled' ||
		attr === 'readonly' ||
		attr === 'multiple' ||
		attr === 'selected' ||
		attr === 'noresize' ||
		attr === 'defer';
};

/**
 * For each loop with an optional else clause.
 * @param {array} data the data to iterate over (may be null/undefined).
 * @param {function} eachClosure the closure to execute on each element.
 *  signature: 
 *  void f(data, i)
 *  	- data {any} the current element in the array
 *  	- i {int} the current index.
 *      - length {int} the total number of elementa
 *  	- isOdd {boolean} true if i is odd.
 * Scope is EjsView.
 * @param {function} [elseClosure] an optional closure to execute if data is 
 * empty.  Recieves no args. Scope is EjsView.
 */
proto.each = function(data, eachClosure, elseClosure) {
	if(!data || data.length === 0) {
		if(elseClosure) {
			elseClosure.call(this);
		}
		return;
	}
	for(var i=0, n=data.length; i<n; i++) {
		eachClosure.call(this, data[i], i, n, i%2!=0);
	}
};

/**
 * Wrapper for EjsView.partial that allows usage of a relative path.
 * @param {string} partial a relative path to a file from the root of the 
 * 		views directory
 * @param {Object} [data] optional data to pass the partial.
 */
proto.render_partial = function(partial, data) {
	// NOTE: do not default data to an empty object.
	// if data is undefined, the parent templates data is implicitly passed
	// to the partial.
	var url = openapplicant.admin.controller.ApplicationController.prototype.resolveView(partial);
	return this.partial({url: url}, data);
};

/**
 * Abbreviates the given string.
 * @example
 * substring('abcdefg', 5)	->	'ab...'
 * 
 * @param {string} s the string to abbreviate
 * @param {int} [maxLength] the maximum length of the abbreviated string
 * Defaults to 20.
 * @return {string} the abbreviated string
 */
proto.abbreviate = function(s, maxLength) {
	maxLength = parseInt(maxLength) || 20;
	return StringUtils.abbreviate(s, maxLength);
};

/**
 * Check if the given string is blank.  That is check if the string is
 * null/undefined/empty or only whitespace.
 * @param {string} s the string to check
 * @return {boolean}
 */
proto.is_blank = function(s) {
	return StringUtils.isBlank(s);
};

/**
 * Escapes XML reserved characters:
 * <  to &lt;
 * >  to &gt;
 * &  to &amp;
 * "  to &quot;
 * '  to &apos;
 * @param {string} s the string to escape
 * @return {string} the escaped string.
 */
proto.escape_xml = function(str) {
	return StringEscapeUtils.escapeXml(str);
};
	
})();
