;(function(){oltk.namespace('oltk.util');oltk.include('oltk/util/LangUtils.js');var Lang=oltk.util.LangUtils;var self=oltk.util.StringUtils={defaultString:function(s,defaultString){return Lang.isString(s)?s:defaultString||'';},equals:function(s1,s2){return!Lang.isString(s1)?!Lang.isString(s2):s1===s2;},equalsIgnoreCase:function(s1,s2){if(!Lang.isString(s1)){return!Lang.isString(s2);}else{return Lang.isString(s2)?s1.toUpperCase()===s2.toUpperCase():false;}},isEmpty:function(str){return!Lang.isString(str)||str.length===0;},isNotEmpty:function(str){return!self.isEmpty(str);},isBlank:function(str){if(self.isEmpty(str)){return true;}
for(var i=0,n=str.length;i<n;i++){if(!self._isWhitespaceCharacter(str.charAt(i))){return false;}}
return true;},isNotBlank:function(str){return!self.isBlank(str);},contains:function(str,sub){if(!Lang.isString(str)||!Lang.isString(sub)){return false;}
return str.indexOf(sub)>=0;},containsIgnoreCase:function(str,sub){if(!Lang.isString(str)||!Lang.isString(sub)){return false;}
return self.contains(str.toUpperCase(),sub.toUpperCase());},_isWhitespaceCharacter:function(c){return(/\s/).test(c);},regionMatches:function(str,toffset,other,ooffset,len){return self._regionMatches(str,toffset,other,ooffset,len,false);},regionMatchesIgnoreCase:function(str,toffset,other,ooffset,len){return self._regionMatches(str,toffset,other,ooffset,len,true);},_regionMatches:function(str,toffset,other,ooffset,len,ignoreCase){if(!Lang.isString(str)||!Lang.isString(other)){return!Lang.isString(str)&&!Lang.isString(other);}
if(toffset<0||ooffset<0){return false;}
if(str.length<toffset+len||other.length<ooffset+len){return false;}
var s1=str.substring(toffset,toffset+len);var s2=other.substring(ooffset,ooffset+len);return ignoreCase?self.equalsIgnoreCase(s1,s2):self.equals(s1,s2);},startsWith:function(str,prefix){return self._startsWith(str,prefix,false);},startsWithIgnoreCase:function(str,prefix){return self._startsWith(str,prefix,true);},_startsWith:function(str,prefix,ignoreCase){if(!Lang.isString(str)||!Lang.isString(prefix)){return(!Lang.isString(str)&&!Lang.isString(prefix));}
if(prefix.length>str.length){return false;}
if(ignoreCase){return self.regionMatchesIgnoreCase(str,0,prefix,0,prefix.length);}else{return self.regionMatches(str,0,prefix,0,prefix.length);}},endsWith:function(str,suffix){return self._endsWith(str,suffix,false);},endsWithIgnoreCase:function(str,suffix){return self._endsWith(str,suffix,true);},_endsWith:function(str,suffix,ignoreCase){if(!Lang.isString(str)||!Lang.isString(suffix)){return!Lang.isString(str)&&!Lang.isString(suffix);}
if(suffix.length>str.length){return false;}
var offset=str.length-suffix.length;if(ignoreCase){return self.regionMatchesIgnoreCase(str,offset,suffix,0,suffix.length);}else{return self.regionMatches(str,offset,suffix,0,suffix.length);}},strip:function(str){if(!Lang.isString(str)){return null;}
return str.replace(/^\s+|\s+$/g,'');},stripToEmpty:function(str){return self.strip(str)||'';},stripToNull:function(str){return self.strip(str)||null;},removeStart:function(str,remove){if(self.isEmpty(str)||self.isEmpty(remove)){return str;}
if(self.startsWith(str,remove)){return str.substring(remove.length);}
return str;},removeStartIgnoreCase:function(str,remove){if(self.isEmpty(str)||self.isEmpty(remove)){return str;}
if(self.startsWithIgnoreCase(str,remove)){return str.substring(remove.length);}
return str;},removeEnd:function(str,remove){if(self.isEmpty(str)||self.isEmpty(remove)){return str;}
if(self.endsWith(str,remove)){return str.substring(0,str.length-remove.length);}
return str;},removeEndIgnoreCase:function(str,remove){if(self.isEmpty(str)||self.isEmpty(remove)){return str;}
if(self.endsWithIgnoreCase(str,remove)){return str.substring(str,str.length-remove.length);}
return str;},remove:function(str,remove){if(self.isEmpty(str)||self.isEmpty(remove)){return str;}
var r=new RegExp(remove,'g');return str.replace(r,'');},capitalize:function(str){return self._changeFirstCharacter(str,true);},uncapitalize:function(str){return self._changeFirstCharacter(str,false);},_changeFirstCharacter:function(str,capitalize){if(self.isEmpty(str)){return str;}
if(capitalize){return str.replace(/^\w/,function(c){return c.toUpperCase();});}
else{return str.replace(/^\w/,function(c){return c.toLowerCase();});}},abbreviate:function(str,maxWidth){if(self.isEmpty(str)){return'';}
maxWidth=maxWidth<4?4:maxWidth;if(str.length<=maxWidth){return str;}
return str.substring(0,maxWidth-3)+'...';}};})();