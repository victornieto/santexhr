;(function() {
	
oltk.namespace('oltk.util');

oltk.include('oltk/util/StringUtils.js');


var StringUtils = oltk.util.StringUtils;

//============================================================================
// STRING ESCAPE UTILS
//============================================================================
/**
 * Static utility class for escaping XML, Java, Javascript, SQL, CSV 
 * and other file formats.
 *
 * @class oltk.util.StringEscapeUtils
 * @static
 */
var self = oltk.util.StringEscapeUtils = {
	
	//------------------------------------------------------------------------
	// XML
	//------------------------------------------------------------------------
	/**
	 * Excapes XML reserved characters, replacing < with &lt;, > with &gt;,
	 * & with &amp;, " with &quot; and ' with &apos;
	 *
	 * @example 'some<text&here' -> 'some&lt;text&amp;here'
	 *
	 * @function
	 * @memberOf oltk.util.StringEscapeUtils
	 * @param {string} str the string to escape (may be null/undefined)
	 * @return {string} the escaped string.  Returns null/undefined if 
	 * str was null/undefined.
	 */
	escapeXml: function(str) {
		if(StringUtils.isEmpty(str)) {
			return str;
		}
		var sb = [];
		for(var i=0, n=str.length; i<n; i++) {
			var c = str.charAt(i);
			if('<' === c) {
				sb.push('&lt;');
			} else if('>' === c) {
				sb.push('&gt;');
			} else if ('&' === c) {
				sb.push('&amp;');
			} else if('"' === c) {
				sb.push('&quot;');
			} else if("'" === c) {
				sb.push('&apos;');
			} else {
				sb.push(c);
			}
		}
		return sb.join('');
	},
	
	/**
	 * Converts escaped XML sequences to their corresponding characters, 
	 * replacing &lt; with <, &gt; with >, &amp; with &, &quot; with ",
	 * and &apos; with '
	 *
	 * @example 'some&lt;text&amp;here' -> 'some<text&here'
	 *
	 * @function
	 * @memberOf oltk.util.StringEscapeUtils
	 * @param {string} str the string to unescape (may be null/undefined)
	 * @return {string} the unescaped string.  Returns null/undefined if
	 * str was null/undefined.
	 */
	unescapeXml: function(str) {
		if(StringUtils.isEmpty(str)) {
			return str;
		}
		var sb = [];
		var i=0;
		while(i<str.length) {
			if(StringUtils.regionMatches(str,i,'&lt;',0,4)){
				sb.push('<');
				i+=4;
			} else if(StringUtils.regionMatches(str,i,'&gt;',0,4)){
				sb.push('>');
				i+=4;
			} else if(StringUtils.regionMatches(str,i,'&amp;',0,5)){
				sb.push('&');
				i+=5;
			} else if(StringUtils.regionMatches(str,i,'&quot;',0,6)){
				sb.push('"');
				i+=6;
			} else if(StringUtils.regionMatches(str,i,'&apos;',0,6)){
				sb.push("'");
				i+=6;
			} else {
				sb.push(str.charAt(i));
				i++;
			}
		}
		return sb.join('');
	}
};


})();
