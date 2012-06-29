;(function() {
	
oltk.namespace('oltk.util');

oltk.include('oltk/util/LangUtils.js');


var Lang = oltk.util.LangUtils;

//============================================================================
// STRING UTILS
//============================================================================
/**
 * Static class with utility methods for working with strings.
 * 
 * NOTE: for developers.  When adding methods to this class, try to 
 * keep the names consistent with well known string utility classes.  
 * Good sources for names are:
 * 		org.apache.commons.lang.StringUtils
 * 		java.lang.String
 * 		ruby string
 * 		prototype.js string
 * 
 *
 * @class StringUtils
 * @static
 */
var self = oltk.util.StringUtils = {
	
	//========================================================================
	// METHODS
	//========================================================================
	
	//========================================================================
	// DEFAULT STRING
	//========================================================================
	/**
	 * Returns the empty string or a default string
	 * if the given string is not a string.
	 * 
	 * <pre>
	 * StringUtils.defaultString(3, 'three')	= 'three'
	 * StringUtils.defaultString(null) 			= ''
	 * StringUtils.defaultString('abc') 		= 'abc'
	 * </pre>
	 * @param {string} s the string to test
	 * @param {string} [defaultString] a default string to return if s is 
	 * 		not a string.
	 * @return {strng} the default string
	 */
	defaultString: function(s, defaultString) {
		return Lang.isString(s) ? s : defaultString || '';
	},
	
	//========================================================================
	// EQUALS
	//========================================================================
	/**
	 * Compares two Strings, returning <code>true</code> if they are equal.
	 * null/undefined are handled without exceptions.  Two null/undefined
	 * references are considered equal.  Two empty strings are 
	 * considered equal.  
	 *
	 * <p><pre>
	 * StringUtils.equals(null,null)			= true
	 * StringUtils.equals(null) 				= true 
	 * StringUtils.equals('abc', null)			= false
	 * StringUtils.equals('','')				= true 
	 * StringUtils.equals('abc','abc')			= true 
	 * StringUtils.equals('abc','ABC')			= false
	 * </pre>
	 *
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} s1 the first string, may be null/undefined
	 * @param {string} s2 the second string, may be null/undefined
	 * @return {boolean} true if both strings are equal.
	 */
	equals: function(s1, s2) {
		return !Lang.isString(s1) ? !Lang.isString(s2) : s1 === s2;
	},
	
	/**
	 * Compares two Strings, returning <code>true</code> if they are equal.
	 * null/undefined are handled without exceptions.  Two null/undefined
	 * references are considered equal.  Two empty strings are 
	 * considered equal.  The comparison is case insensitive.
	 *
	 * <p><pre>
	 * StringUtils.equalsIgnoreCase(null,null)		= true
	 * StringUtils.equalsIgnoreCase(null) 			= true 
	 * StringUtils.equalsIgnoreCase('abc', null)	= false
	 * StringUtils.equalsIgnoreCase('','')			= true 
	 * StringUtils.equalsIgnoreCase('abc','abc')	= true 
	 * StringUtils.equalsIgnoreCase('abc','ABC')	= true
	 * </pre>
	 *
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} s1 the first string, may be null/undefined
	 * @param {string} s2 the second string, may be null/undefined
	 * @return {boolean} true if both strings are equal.
	 */
	equalsIgnoreCase: function(s1, s2) {
		if(!Lang.isString(s1)) {
			return !Lang.isString(s2);
		}else {
			return Lang.isString(s2) ? s1.toUpperCase() === s2.toUpperCase() : false;
		}
	},
	
	//========================================================================
	// EMPTY/BLANK
	//========================================================================
	/**
	 * Check that the given string is null/undefined or has length 0.  
	 * 
	 * <pre>
	 * StringUtils.isEmpty(null)	=  true
	 * StringUtils.isEmpty('')		= true
	 * StringUtils.isEmpty(' ')		= false
	 * StringUtils.isEmpty('Hello') = false
	 * </pre>
	 * 
	 * @function 
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to check (may be null/undefined)
	 * @return {boolean} true if the string is null/undefined or has length 0.
	 */
	isEmpty: function(str) {
		return !Lang.isString(str) || str.length === 0;
	},
	
	/**
	 * Check that the given string is neither null/undefined nor of length 0. 
	 * Note: Will return true for a string that purely consists of whitespace.
	 *
	 * <p><pre>
	 * StringUtils.isNotEmpty(null)		=  false
	 * StringUtils.isNotEmpty('')		= false
	 * StringUtils.isNotEmpty(' ')		= true
	 * StringUtils.isNotEmpty('Hello') 	= true
	 * </pre>
	 * @function 
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to check (may be null/undefined)
	 * @return {boolean} true if the string is not null/undefined
	 * and has length.
	 */
	isNotEmpty: function(str) {
		return !self.isEmpty(str);
	},
	
	/**
	 * Check whether the given string contains only whitespace or is null/undefined
	 * or empty.
	 * <pre>
	 * StringUtils.isBlank(null)		= true
	 * StringUtils.isBlank('')			= true
	 * StringUtils.isBlank(' ')			= true
	 * StringUtils.isBlank('12345')		= false
	 * StringUtils.isBlank(' 12345 ') 	= false
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to check (may be null/undefined)
	 * @return {boolean} true if the string is null, undefined,
	 * empty, or contains only whitespace.
	 */
	isBlank: function(str) {
		if(self.isEmpty(str)){
			return true;
		}
		for(var i=0, n=str.length; i<n; i++){
			if(!self._isWhitespaceCharacter(str.charAt(i))){
				return false;
			}
		}
		return true;
	},
	
	/**
	 * Check whether the given string has actual text.
	 * More specifically, returns true if the string is not 
	 * null/undefined, its length is greater 
	 * than zero and it contains at least on non-whitespace character.
	 *
	 * <p><pre>
	 * StringUtils.isNotBlank(null)			= false
	 * StringUtils.isNotBlank('')			= false
	 * StringUtils.isNotBlank(' ')			= false
	 * StringUtils.isNotBlank('12345')		= true
	 * StringUtils.isNotBlank(' 12345 ') 	= true
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to check (may be null/undefined)
	 * @return {boolean} true if the string is not null, undefined,
	 * has length greater than 0 and does not contain only whitespace.
	 */
	isNotBlank: function(str) {
		return !self.isBlank(str);
	},
	
	//========================================================================
	// CONTAINS
	//========================================================================
	/**
	 * Test that the given string contains the given substring.  Returns false
	 * if either string is null/undefined.
	 *
	 * <p><pre>
	 * StringUtils.contains(null, *)	= false
	 * StringUtils.contains(*, null)	= false
	 * StringUtils.contains('','')		= true
	 * StringUtils.contains('abc','')	= true
	 * StringUtils.contains('abc', 'a')	= true
	 * StringUtils.contains('abc','z')	= false
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the text to search (may be null/undefined)
	 * @param {string} sub the substring to search for (may be null/undefined)
	 * @return {boolean} true if str contains sub.  Returns false otherwise, or
	 * if either arg is null/undefined.
	 */
	contains: function(str, sub) {
		if(!Lang.isString(str) || !Lang.isString(sub)){
			return false;
		}
		return str.indexOf(sub) >=0;
	},
	
	/**
	 * Test that the given string contains the given substring, case insensitive.
	 * Returns false if either string is null/undefined.
	 *
	 * <p><pre>
	 * StringUtils.containsIgnoreCase(null, *)		= false
	 * StringUtils.containsIgnoreCase(*, null)		= false
	 * StringUtils.containsIgnoreCase('','')		= true
	 * StringUtils.containsIgnoreCase('abc','')		= true
	 * StringUtils.containsIgnoreCase('abc', 'a') 	= true
	 * StringUtils.containsIgnoreCase('abc', 'z') 	= false
	 * StringUtils.containsIgnoreCase('abc', 'A') 	= true
	 * StringUtils.containsIgnoreCoase('abc', 'Z') 	= false
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the text to search (may be null/undefined)
	 * @param {string} sub the substring to search for (may be null/undefined)
	 * @return {boolean} true if str contains sub.  Returns false otherwise,
	 * or if either arg is null/undefined.
	 */
	containsIgnoreCase: function(str, sub) {
		if(!Lang.isString(str) || !Lang.isString(sub)) {
			return false;
		}
		return self.contains(str.toUpperCase(), sub.toUpperCase());
	},
	
	/**
	 * Check if a single character is whitespace.
	 *
	 * @function 
	 * @memberOf oltk.util.StringUtils
	 * @private
	 * @param {char} c the character to check.
	 * @return {boolean} <code>true</code> if the character is whitespace.
	 */
	_isWhitespaceCharacter: function(c) {
		return (/\s/).test(c);
	},
	
	//========================================================================
	// REGION MATCHES
	//========================================================================
	/**
	 * Test if two string regions are equal.  Returns true if both strings
	 * are null/undefined. Returns false iff at least one of the following are true:
	 * <ul>
	 * <li>tooffset is negative</li>
	 * <li>ooffset is negative</li>
	 * <li>tooffset+len is greater than the length of str</li>
	 * <li>ooffset+len is greater than the length of the other argument</li>
	 * <li>There is some nonnegative integer k less than len such that:
	 * str.charAt(toffset+k) != other.charAt(toffset+k)
	 * </ul>
	 * <p><pre>
	 * StringUtils.regionMatches(null,*,null,*,*)		= true
	 * StringUtils.regionMatches(null,*,'abc',*,*) 		= false
	 * StringUtils.regionMatches('', 0, '', 0, 0) 		= true
	 * StringUtils.regionMatches('abc', 0, '', 0, 0) 	= true
	 * StringUtils.regionMatches('abc',0, '', 0, 1) 	= false
	 * StringUtils.regionMatches('abc',1,'bc',0,2)		= true
	 * StringUtils.regionMatches('abc',1,'BC',0,2)		= false
	 * </pre>
	 * @function 
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the source string to test (may be null/undefined)
	 * @param {int} toffset the starting offset of the subregion in str
	 * @param {string} other the string argument
	 * @param {int} ooffset the starting offset of the subregion in other
	 * @param {int} len the number of characters to compare.
	 * @return {boolean} true if the specified subregion or str matches the 
	 * the subregion of other; false otherwise.
	 */
	regionMatches: function(str, toffset, other, ooffset, len) {
		return self._regionMatches(str, toffset, other, ooffset, len, false);
	},
	
	/**
	 * Test if two string regions are equal, cas insensitive.  Returns true if both 
	 * strings are null/undefined. 
	 * Returns false iff at least one of the following are true:
	 * <ul>
	 * <li>tooffset is negative</li>
	 * <li>ooffset is negative</li>
	 * <li>tooffset+len is greater than the length of str</li>
	 * <li>ooffset+len is greater than the length of the other argument</li>
	 * <li>There is some nonnegative integer k less than len such that:
	 * str.charAt(toffset+k) != other.charAt(toffset+k)
	 * </ul>
	 * <p><pre>
	 * StringUtils.regionMatchesIgnoreCase(null,*,null,*,*)		= true
	 * StringUtils.regionMatchesIgnoreCase(null,*,'abc',*,*) 	= false
	 * StringUtils.regionMatchesIgnoreCase('', 0, '', 0, 0) 	= true
	 * StringUtils.regionMatchesIgnoreCase('abc', 0, '', 0, 0) 	= true
	 * StringUtils.regionMatchesIgnoreCase('abc',0, '', 0, 1) 	= false
	 * StringUtils.regionMatchesIgnoreCase('abc',1,'bc',0,2)	= true
	 * StringUtils.regionMatchesIgnoreCase('abc',1,'BC',0,2)	= true
	 * </pre>
	 * @function 
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the source string to test (may be null/undefined)
	 * @param {int} toffset the starting offset of the subregion in str
	 * @param {string} other the string argument
	 * @param {int} ooffset the starting offset of the subregion in other
	 * @param {int} len the number of characters to compare.
	 * @return {boolean} true if the specified subregion or str matches the 
	 * the subregion of other; false otherwise
	 */
	regionMatchesIgnoreCase: function(str, toffset, other, ooffset, len) {
		return self._regionMatches(str, toffset, other, ooffset, len, true);
	},
	
	/**
	 * Worker method for region matches.
	 * @function
	 * @private
	 * @static
	 * @memberOf otlk.lang.StringUtils
	 */
	_regionMatches: function(str, toffset, other, ooffset, len, ignoreCase) {
		if(!Lang.isString(str) || !Lang.isString(other)) {
			return !Lang.isString(str) && !Lang.isString(other);
		}
		if(toffset < 0 || ooffset < 0){
			return false;
		}
		if(str.length < toffset + len || other.length < ooffset + len) {
			return false;
		}
		var s1 = str.substring(toffset,toffset+len);
		var s2 = other.substring(ooffset, ooffset+len);
		return ignoreCase ? self.equalsIgnoreCase(s1, s2) : self.equals(s1,s2);
	},
	
	//========================================================================
	// STARTS WITH/ENDS WITH
	//========================================================================
	/**
	 * Test if the string starts with the specified prefix, case sensitive. 
	 * Two null/undefined references are considered equal.
	 *
	 * <p><pre>
	 * StringUtils.startsWith(null,null)		= true
	 * StringUtils.startsWith(null, 'abcdef') 	= false
	 * StringUtils.startsWith('abc', null) 		= false
	 * StringUtils.startsWith('','') 			= true
	 * StringUtils.startsWith('abcdef', 'abc') 	= true
	 * StringUtils.startsWith('ABCDEF', 'abc') 	= false
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to test (may be null/undefined)
	 * @param {string} prefix the prefix to look for (may be null/undefined)
	 * @return {boolean} true if the give strign starts with the given prefix.
	 */
	startsWith: function(str, prefix) {
		return self._startsWith(str, prefix, false);
	},
	
	/**
	 * Test if the string starts with the specified prefix, ignoring upper/lower
	 * case.  Two null/undefined references are considered equal.
	 *
	 * <p><pre>
	 * StringUtils.startsWithIgnoreCase(null,null)			= true
	 * StringUtils.startsWithIgnoreCase(null, 'abcdef') 	= false
	 * StringUtils.startsWithIgnoreCase('abc', null) 		= false
	 * StringUtils.startsWithIgnoreCase('', '')	 			= true
	 * StringUtils.startsWithIgnoreCase('abcdef', 'abc') 	= true
	 * StringUtils.startsWithIgnoreCase('ABCDEF', 'abc') 	= true
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to test (may be null/undefined)
	 * @param {string} prefix the prefix to look for (may be null/undefined)
	 * @return {boolean} true if the given string starts with with the given 
	 * prefix, ignoring case.
	 */
	startsWithIgnoreCase: function(str, prefix) {
		return self._startsWith(str, prefix, true);
	},
	
	/**
	 * Worker method for starts with.
	 * @function
	 * @private
	 * @static
	 * @memberOf oltk.util.StringUtils
	 */
	_startsWith: function(str, prefix, ignoreCase) {
		if(!Lang.isString(str) || !Lang.isString(prefix)) {
			return (!Lang.isString(str) && !Lang.isString(prefix));
		}
		if(prefix.length > str.length) {
			return false;
		}
		if(ignoreCase) {
			return self.regionMatchesIgnoreCase(str, 0, prefix, 0, prefix.length);
		} else {
		 	return self.regionMatches(str, 0, prefix, 0, prefix.length);
		}
	},
	
	/**
	 * Test if the string ends with the specified suffix, case sensitive. 
	 * Two null/undefined references are considered equal.
	 *
	 * <p><pre>
	 * StringUtils.endsWith(null,null)			= true
	 * StringUtils.endsWith(null, 'abcdef') 	= false
	 * StringUtils.endsWith('abc', null) 		= false
	 * StringUtils.endsWith('','') 				= true
	 * StringUtils.endsWith('abcdef', 'def') 	= true
	 * StringUtils.endsWith('ABCDEF', 'def') 	= false
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to test (may be null/undefined)
	 * @param {string} suffix the suffix to look for (may be null/undefined)
	 * @return {boolean} true if the give string ends with the given suffix.
	 */
	endsWith: function(str, suffix) {
		return self._endsWith(str, suffix, false);
	},
	
	/**
	 * Test if the string ends with the specified suffix, case insensitive. 
	 * Two null/undefined references are considered equal.
	 *
	 * <p><pre>
	 * StringUtils.endsWithIgnoreCase(null,null)		= true
	 * StringUtils.endsWithIgnoreCase(null, 'abcdef') 	= false
	 * StringUtils.endsWithIgnoreCase('abc', null) 		= false
	 * StringUtils.endsWithIgnoreCase('','') 			= true
	 * StringUtils.endsWithIgnoreCase('abcdef', 'def')	= true
	 * StringUtils.endsWithIgnoreCase('ABCDEF', 'def') 	= true
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to test (may be null/undefined)
	 * @param {string} suffix the suffix to look for (may be null/undefined)
	 * @return {boolean} true if the give string ends with the given suffix.
	 */
	endsWithIgnoreCase: function(str, suffix) {
		return self._endsWith(str, suffix, true);
	},
	
	/**
	 * Worker method for ends with
	 * @function
	 * @private
	 * @static
	 * @memberOf oltk.util.StringUtils
	 */
	_endsWith: function(str, suffix, ignoreCase) {
		if(!Lang.isString(str) || !Lang.isString(suffix)) {
			return !Lang.isString(str) && !Lang.isString(suffix);
		}
		if(suffix.length > str.length) {
			return false;
		}
		var offset = str.length - suffix.length;
		if(ignoreCase) {
			return self.regionMatchesIgnoreCase(str,offset,suffix,0,suffix.length);
		} else {
			return self.regionMatches(str,offset,suffix,0,suffix.length);
		}
	},
	
	//========================================================================
	// STRIP
	//========================================================================
	/**
	 * Strips leading and trailing whitespace from the given string,
	 * handling null/undefined/non-strings by returning null.
	 * <pre>
	 * StirngUtils.strip(null)		= null
	 * StringUtils.strip(3)			= null
	 * StringUtils.strip('')			= ''
	 * StringUtils.strip('	')		= ''
	 * StringUtils.strip('abc')		= 'abc'
	 * StringUtils.strip(' abc ')	= 'abc'
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to strip (may be null/undefined)
	 * @return {string} the stripped string or null
	 */
	strip: function(str) {
		if(!Lang.isString(str)) {
			return null;
		}
		return  str.replace(/^\s+|\s+$/g,'');
	},
	
	/**
	 * Strips leading and trailing whitespace from the given string.  Returns an empty
	 * string if str is null/undefined or not a string.
	 * <pre>
	 * StringUtils.stripToEmpty(null)		= ''
	 * StringUtils.stripToEmpty(3)			= ''
	 * StringUtils.stripToEmpty('') 		= ''
	 * StringUtils.stripToEmpty('   ') 		= ''
	 * StringUtils.stripToEmpty('abc') 		= 'abc'
	 * StringUtils.stripToEmpty(' abc ')	= 'abc'
	 * StringUtils.stripToEmpty('ab c') 	= 'ab c'
	 * </pre>
	 * @function
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to strip (may be null/undefined)
	 * @return {string} the stripped string.
	 */
	stripToEmpty: function(str) {
		return self.strip(str) || '';
	},
	
	/**
	 * Strips leading and trailing whitespace from the given string, returning
	 * null if the string is empty after the strip or if it is null.
	 * <pre>
	 * StringUtils.stripToNull(null)		= null
	 * StringUtils.stripToNull(3)		= null
	 * StringUtils.stripToNull('') 		= null
	 * StringUtils.stripToNull('   ') 	= null
	 * StringUtils.stripToNull('abc') 	= 'abc'
	 * StringUtils.stripToNull(' abc ') 	= 'abc'
	 * StringUtils.stripToNull('ab c') 	= 'ab c'
	 * </pre>
	 * @function
	 * @static 
	 * @memberOf olkt.util.StringUtils
	 * @param {string} str the string to strip (may be null/undefined)
	 * @return {string} the stripped string or null
	 */
	stripToNull: function(str) {
		return self.strip(str) || null;
	},
	
	//========================================================================
	// REMOVE
	//========================================================================
	/**
	 * Removes a substring only if it is at the beginning of a source string, 
	 * otherwise returns the source string. 
	 * Returns null/undefined if null/undefined is passed as the target string.  
	 * 
	 * <p><pre>
	 * StringUtils.removeStart(null,*) 						= null 
	 * StringUtils.removeStart('', *) 						= ''
	 * StringUtils.removeStart(*, null) 					= *
	 * StringUtils.removeStart('www.domain.com', 'www.') 	= 'domain.com'
	 * StringUtils.removeStart('domain.com', 'www.') 		='domain.com'
	 * StringUtils.removeStart('www.domain.com', 'domain') 	= 'www.domain.com'
	 * StringUtils.removeStart('abc', '') 					= 'abc'
	 * </pre>
	 * @function removeStart
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the target string
	 * @param {string} remove the substring to remove
	 * @return {string} the string with the substring removed if found.
	 * Returns str if str is not a string.
	 */
	removeStart: function(str, remove) {
		if(self.isEmpty(str) || self.isEmpty(remove)) {
			return str;
		}
		if(self.startsWith(str,remove)) {
			return str.substring(remove.length);
		}
		return str;
	},
	
	/**
	 * Removes a case insensitive substring only if it is at the beginning of a 
	 * source string, otherwise returns the source string. 
	 * Returns null/undefined if null/undefined is passed as the target string.  
	 *
	 * <p><pre>
	 * StringUtils.removeStartIgnoreCase(null,*) 					= null 
	 * StringUtils.removeStartIgnoreCase('', *) 					= ''
	 * StringUtils.removeStartIgnoreCase(*, null) 					= *
	 * StringUtils.removeStartIgnoreCase('www.domain.com', 'www.') 	= 'domain.com'
	 * StringUtils.removeStartIgnoreCase('www.domain.com', 'WWW.') 	= 'domain.com
	 * StringUtils.removeStartIgnoreCase('domain.com', 'www.') 		= 'domain.com'
	 * StringUtils.removeStartIgnoreCase('www.domain.com', 'domain')= 'www.domain.com'
	 * StringUtils.removeStartIgnoreCase('abc', '') 				= 'abc'
	 * </pre>
	 * @function removeStartIgnoreCase
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the target string
	 * @param {string} remove the substring to remove
	 * @return {string} the substring with the string removed if found,
	 * <code>str</code> if str is not a string.
	 */
	removeStartIgnoreCase: function(str, remove) {
		if(self.isEmpty(str) || self.isEmpty(remove)) {
			return str;
		}
		if(self.startsWithIgnoreCase(str, remove)){
			return str.substring(remove.length);
		}
		return str;
	},
	
	/**
	 * Removes a substring only if it as the end of a source string, otherwise
	 * returns the source string. Returns null/undefined if null/undefined 
	 * is passed as the target string.
	 * 
	 * <p><pre>
	 * StringUtils.removeEnd(null, *) 						= null
	 * StringUtils.removeEnd('', *)			 				= ''
	 * StringUtils.removeEnd(*, null) 						= *
	 * StringUtils.removeEnd('www.domain.com','.comm') 		= 'www.domain.com'
	 * StringUtils.removeEnd('www.domain.com','.com') 		= 'www.domain'
 	 * StringUtils.removeEnd('www.domain.com', 'domain') 	= 'www.domain.com'
	 * StringUtils.removeEnd('abc','') 						= 'abc'
	 * </pre>
	 *
	 * @function removeEnd
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the target string
	 * @param {string} remove the substring to remove
	 * @return {string} the substring with the string removed if found,
	 * <code>str</code> if str is not a string.
	 */
	removeEnd: function(str, remove) {
		if(self.isEmpty(str) || self.isEmpty(remove)) {
			return str;
		}
		if(self.endsWith(str, remove)) {
			return str.substring(0, str.length - remove.length);
		}
		return str;
	},
	
	/**
	 * Removes a case insensitive substring only if it as the end of a source 
	 * string, otherwise returns the source string. 
	 * Returns null/undefined if null/undefined is passed as the target string.
	 *
	 * <p><pre>
	 * StringUtils.removeEndIgnoreCase(null, *) 					= null
	 * StringUtils.removeEndIgnoreCase('', *) 						= ''
	 * StringUtils.removeEndIgnoreCase(*, null) 					= *
	 * StringUtils.removeEndIgnoreCase('www.domain.com','.comm') 	= 'www.domain.com'
	 * StringUtils.removeEndIgnoreCase('www.domain.com','.com') 	= 'www.domain'
	 * StringUtils.removeEndIgnoreCase('www.domain.com', '.COM') 	= 'www.domain'
	 * StringUtils.removeEndIgnoreCase('www.domain.com', 'domain') 	= 'www.domain.com'
	 * StringUtils.removeEndIgnoreCase('abc','') 					= 'abc'
	 * </pre>
	 * @function removeEndIgnoreCase
	 * @static
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the target string
	 * @param {string} remove the substring to remove
	 * @return {string} the chopped string or null/undefined if null/undefined
	 * was passed.
	 */
	removeEndIgnoreCase: function(str, remove) {
		if(self.isEmpty(str) || self.isEmpty(remove)) {
			return str;
		}
		if(self.endsWithIgnoreCase(str, remove)){
			return str.substring(str, str.length - remove.length);
		}
		return str;
	},
	
	/**
	 * Removes all occurances of a substring from within th source string.
	 * 
	 * A null/undefined source string will return null/undefined.
	 * An empty ('') source string will return the empty string.
	 * A null/undefined remove string will return the source string.
	 * An empty ('') remove string will return the source string.
	 * 
	 * <pre>
	 * StringUtils.remove(null, *)						= null
	 * StringUtils.remove('', *)						= ''
	 * StringUtils.remove(*, null)						= *
	 * StringUtils.remove(*, '')						= *
	 * StringUtils.remove('queued', 'ue')				= 'qd'
	 * StringUtils.remove('queued', 'zz')				= 'queued'
	 * </pre>
	 * @param {string} str the source string to search
	 * @param {string} remove the string to search for and remove
	 * @return {string} the substring with the string removed if found,
	 * str if str is not a string.
	 */
	remove: function(str, remove) {
		if(self.isEmpty(str) || self.isEmpty(remove)) {
			return str;
		}
		var r = new RegExp(remove, 'g');
		return str.replace(r, '');  // TODO: StringUtils.replace
	},
	
	//========================================================================
	// CAPITALIZATION
	//========================================================================
	/**
	 * Capitalize a string, changing the first letter to upper case.
	 * No other characters are changed.
	 *
	 * @function
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to capitalize.
	 * @return {stirng} the capitalized string, <code>null</code> or 
	 * <code>undefined</code> if null or undefined.
	 */
	capitalize: function(str) {
		return self._changeFirstCharacter(str, true);
	},
	
	/**
	 * Uncapitalize a string, changing the first letter to lower case.
	 * No other letters are changed.
	 *
	 * @function
	 * @memberOf oltk.util.StringUtils
	 * @param {string} str the string to uncapitalize.
	 * @return {string} the uncapitalized string, <code>null</code> if null or
	 * <code>undefined</code> if null or undefined.
	 */
	uncapitalize: function(str) {
		return self._changeFirstCharacter(str, false);
	},
	
	/**
	 * Worker method for capitalize/uncapitalize
	 * @function
	 * @memberOf oltk.util.StringUtils
	 * @private
	 * @param {string} str
	 * @param {boolean} capitalize
	 * @return {string}
	 */
	_changeFirstCharacter: function(str, capitalize) {
		if(self.isEmpty(str)) {
			return str;
		}
		if(capitalize) {
			return str.replace(/^\w/,function(c){return c.toUpperCase();});
		}
		else {
			return str.replace(/^\w/,function(c){return c.toLowerCase();});
		}
	},
	
	//========================================================================
	// ABBREVIATE
	//========================================================================
	/**
	 * <p>Abbreviates a string using ellipses.  This wil turn 'Now is the time
	 * for all good men' into 'Now is the time for...'</p>
	 * 
	 * <p>Specifically:
	 * <ul>
	 * 	<li>
	 * 		if<code>str</code> is less than <code>maxWidth</code> characters
	 * 		long, return it
	 * 	</li>
	 * 	<li>
	 * 		Else abbreviate it to <code>(substring(str, 0, max-3) + '...')</code>.
 	 * 	</li>
	 * 	<li>
	 * 		if<code>maxWidth</code> is less than <code>4</code>, 
	 * 		replace maxWidth with 4.
	 * 	</li>
	 *  <li>
	 *  	in no case will it return a string of length greater than 
	 *  	<code>maxWidth</code>
	 *  </li>
	 *  </p>
	 *  <pre>
     * 	StringUtils.abbreviate(null, *)      = ""
     * 	StringUtils.abbreviate("", 4)        = ""
     * 	StringUtils.abbreviate("abcdefg", 6) = "abc..."
     * 	StringUtils.abbreviate("abcdefg", 7) = "abcdefg"
     * 	StringUtils.abbreviate("abcdefg", 8) = "abcdefg"
     * 	StringUtils.abbreviate("abcdefg", 4) = "a..."
     * 	StringUtils.abbreviate("abcdefg", 3) = "a..."
     * 	</pre>
     * @param {string} str the string to check
     * @param {int} maxWidth the maximum length of the result String,
     * 		defaults to 4 if less than 4.
     * @return {string} the abbreviated string.
	 */
	abbreviate: function(str, maxWidth) {
		if(self.isEmpty(str)) {
			return '';
		}
		maxWidth = maxWidth < 4 ? 4 : maxWidth;
		if(str.length <= maxWidth) {
			return str;
		}
		return str.substring(0, maxWidth -3) + '...';
	}
	
};
	
})();

