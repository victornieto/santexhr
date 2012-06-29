
oltk.namespace('oltk.xmlpull');oltk.xmlpull.XmlPullParser=function()
{XmlPullParser.prototype.START_DOCUMENT=0;XmlPullParser.prototype.END_DOCUMENT=1;XmlPullParser.prototype.START_TAG=2;XmlPullParser.prototype.END_TAG=3;XmlPullParser.prototype.TEXT=4;XmlPullParser.prototype.CDSECT=5;XmlPullParser.prototype.ENTITY_REF=6;XmlPullParser.prototype.IGNORABLE_WHITESPACE=7;XmlPullParser.prototype.PROCESSING_INSTRUCTION=8;XmlPullParser.prototype.COMMENT=9;XmlPullParser.prototype.DOCDECL=10;XmlPullParser.prototype.NO_NAMESPACE='';XmlPullParser.prototype.TYPES=["START_DOCUMENT","END_DOCUMENT","START_TAG","END_TAG","TEXT","CDSECT","ENTITY_REF","IGNORABLE_WHITESPACE","PROCESSING_INSTRUCTION","COMMENT","DOCDECL"];var TextSE="[^<]+";var UntilHyphen="[^-]*-";var Until2Hyphens=UntilHyphen+"([^-]"+UntilHyphen+")*-";var CommentCE=Until2Hyphens+">?";var UntilRSBs="[^\\]]*]([^\\]]+])*]+";var CDATA_CE=UntilRSBs+"([^\\]>]"+UntilRSBs+")*>";var S="[ \\n\\t\\r]+";var NameStrt="[A-Za-z_:]|[^\\x00-\\x7F]";var NameChar="[A-Za-z0-9_:.-]|[^\\x00-\\x7F]";var Name="("+NameStrt+")("+NameChar+")*";var QuoteSE='"[^"]'+"*"+'"'+"|'[^']*'";var DT_IdentSE=S+Name+"("+S+"("+Name+"|"+QuoteSE+"))*";var MarkupDeclCE="([^]\"'><]+|"+QuoteSE+")*>";var S1="[\\n\\r\\t ]";var UntilQMs="[^?]*\\?+";var PI_Tail="\\?>|"+S1+UntilQMs+"([^>?]"+UntilQMs+")*>";var DT_ItemSE="<(!(--"+Until2Hyphens+">|[^-]"+MarkupDeclCE+")|\\?"+Name+"("+PI_Tail+"))|%"+Name+";|"+S;var DocTypeCE=DT_IdentSE+"("+S+")?(\\[("+DT_ItemSE+")*]("+S+")?)?>?";var DeclCE="--("+CommentCE+")?|\\[CDATA\\[("+CDATA_CE+")?|DOCTYPE("+DocTypeCE+")?";var PI_CE=Name+"("+PI_Tail+")?";var EndTagCE=Name+"("+S+")?>?";var AttValSE='"[^<"]'+"*"+'"'+"|'[^<']*'";var ElemTagCE=Name+"("+S+Name+"("+S+")?=("+S+")?("+AttValSE+"))*("+S+")?/?>?";var MarkupSPE="<(!("+DeclCE+")?|\\?("+PI_CE+")?|/("+EndTagCE+")?|("+ElemTagCE+")?)";var XML_SPE=TextSE+"|"+MarkupSPE;var CharRefCE="[0-9]+\\;?|x[0-9a-fA-F]+\\;?";var EntityRefCE=Name+";?";var ReferenceSPE="\\&(#("+CharRefCE+")?|("+EntityRefCE+")?)";TextSE="[^<&]+";XML_SPE=TextSE+"|"+MarkupSPE+"|"+ReferenceSPE;var PI_RE="<\\?"+Name+PI_Tail;var CommentRE="<!--"+Until2Hyphens+">";var CDATA_RE="<!\\[CDATA\\["+CDATA_CE;var ElemTagRE="<"+Name+"("+S+Name+"("+S+")?=("+S+")?("+AttValSE+"))*("+S+")?/?>";var EndTagRE="<\\/"+Name+"("+S+")?>";var DocTypeSPE="<!DOCTYPE("+DocTypeCE+")?";var EmptyTagRE="<"+Name+"("+S+Name+"("+S+")?=("+S+")?("+AttValSE+"))*("+S+")?/>";var CharRefRE="&#[0-9]+;|&#x[0-9a-fA-F]+;";var EntityRefRE="&"+Name+";";var ReferenceRE=EntityRefRE+"|"+CharRefRE;var that=this;var _tokens;var _index=-1;var _eventType=this.START_DOCUMENT;var _attData=null;var _textData=null;var _emptyEnd=false;var _stagTest=new RegExp("^"+ElemTagRE+"$");var _etagTest=new RegExp("^"+EndTagRE+"$");var _textTest=new RegExp("^"+TextSE+"$");var _wsTest=new RegExp("^"+S+"$");var _cdsectTest=new RegExp("^"+CDATA_RE+"$");var _entityTest=new RegExp("^"+ReferenceRE+"$");var _piTest=new RegExp("^"+PI_RE+"$");var _commentTest=new RegExp("^"+CommentRE+"$");var _docdeclTest=new RegExp("^"+DocTypeSPE+"$");var _emptyTest=new RegExp("^"+EmptyTagRE+"$");var _attsExtract=new RegExp("("+Name+"("+S+")?=("+S+")?("+AttValSE+"))","g");var _nameExtract=new RegExp(Name);var _cdataExtract=new RegExp("^<!\\[CDATA\\[(.*)\\]\\]>$");var _commentExtract=new RegExp("^<!--(.*)-->$");var _piExtract=new RegExp("^<\\?(.*)\\?>$");this.getAttributeCount=function()
{if(this.START_TAG!=_eventType)
{return-1;}
return _attData.length;};this.getAttributeName=function(index)
{if(this.START_TAG!=_eventType)
{throw("Must be START_TAG to retrieve attributes");}
if(undefined===_attData||null===_attData)
{throw("Must be START_TAG to retrieve attributes");}
var att=_attData[index];return att.name;};this.getAttributeNamespace=function(index)
{throw("Not implemented");};this.getAttributePrefix=function(index)
{throw("Not implemented");};this.getAttributeType=function(index)
{return"CDATA";};this.getAttributeValue=function(index)
{if(this.START_TAG!=_eventType)
{throw("Must be START_TAG to retrieve attributes");}
if(undefined===_attData||null===_attData)
{throw("Must be START_TAG to retrieve attributes");}
var att=_attData[index];return att.value;};this.getAttributeValueByName=function(ns,name)
{if(this.START_TAG!=_eventType)
{throw("Must be START_TAG to retrieve attributes");}
if(undefined===_attData||null===_attData)
{throw("Must be START_TAG to retrieve attributes");}
var value=_attData[name];return value;};this.getColumnNumber=function()
{throw("Not implemented");};this.getDepth=function()
{throw("Not implemented");};this.getEventType=function()
{return _eventType;};this.getInputEncoding=function()
{throw("Not implemented");};this.getLineNumber=function()
{throw("Not implemented");};this.getName=function()
{var token=_currentToken();if(undefined===token||null===token)
{return null;}
var matches=token.match(_nameExtract);if(undefined===matches||null===matches||matches.length==0)
{return null;}
return matches[0];};this.getNamespace=function()
{throw("Not implemented");};this.getNamespaceByPrefix=function(prefix)
{throw("Not implemented");};this.getNamespaceCount=function(depth)
{throw("Not implemented");};this.getNamespacePrefix=function(pos)
{throw("Not implemented");};this.getNamespaceUri=function(pos)
{throw("Not implemented");};this.getPrefix=function()
{throw("Not implemented");};this.getText=function()
{return _textData;};this.isAttributeDefault=function(index)
{return false;};this.isEmptyElementTag=function()
{if(this.START_TAG!=_eventType)
{throw("Current event must be START_TAG");}
var token=_currentToken();if(undefined===token||null===token)
{throw("Current event must be START_TAG");}
return(_emptyTest.test(token));};this.isWhitespace=function()
{var token=_currentToken();return _wsTest.test(token);};this.next=function()
{var textData=null;while(this.END_DOCUMENT!=this.nextToken())
{if(this.PROCESSING_INSTRUCTION==_eventType||this.COMMENT==_eventType||this.DOCDECL==_eventType)
{continue;}
else if(this.TEXT==_eventType||this.ENTITY_REF==_eventType||this.CDSECT==_eventType)
{textData=this.getText();break;}
else
{break;}}
if(undefined!==textData&&null!==textData)
{var index=0;for(var i=_index+1;i<_tokens.length;i++)
{var eventType=_getTokenType(_getToken(i));if(this.START_TAG==eventType||this.END_TAG==eventType||this.END_DOCUMENT==eventType)
{break;}
else if(this.TEXT==eventType||this.ENTITY_REF==eventType||this.CDSECT==eventType)
{index=i;}}
var count=index-_index;for(var m=0;m<count;m++)
{var eventType=this.nextToken();if(this.TEXT==eventType||this.ENTITY_REF==eventType||this.CDSECT==eventType)
{textData+=this.getText();}}
_eventType=this.TEXT;_textData=textData;}
return _eventType;};this.nextTag=function()
{var eventType=this.next();if(this.TEXT==eventType&&this.isWhitespace())
{eventType=this.next();}
if(eventType!=this.START_TAG&&eventType!=this.END_TAG)
{throw("Expected start or end tag");}
return eventType;};this.nextText=function()
{if(this.getEventType()!=this.START_TAG)
{throw("Parser must be on START_TAG to read next text");}
var eventType=this.next();if(this.TEXT==eventType)
{var result=this.getText();eventType=this.next();if(eventType!=this.END_TAG)
{throw("Event TEXT must be immediately followed by END_TAG");}
return result;}
else if(eventType==this.END_TAG)
{return"";}
else
{throw("Parser must be on START_TAG or TEXT to read text.");}};this.nextToken=function()
{if(this.START_TAG==_eventType)
{if(this.isEmptyElementTag())
{_eventType=this.END_TAG;}
else
{_index++;_eventType=_getTokenType(_currentToken());}}
else
{_index++;_eventType=_getTokenType(_currentToken());}
_attData=null;_textData=null;switch(_eventType)
{case this.START_TAG:_attData=_extractAttributes(_currentToken());break;case this.TEXT:_textData=_currentToken();break;case this.CDSECT:_textData=_extractCdataText(_currentToken());break;case this.ENTITY_REF:_textData=_extractEntityText(_currentToken());break;case this.COMMENT:_textData=_extractCommentText(_currentToken());break;case this.PROCESSING_INSTRUCTION:_textData=_extractPiText(_currentToken());break;}
return _eventType;};this.require=function(type,ns,name)
{if((type!=this.getEventType())||(undefined!==ns&&null!==ns&&ns!=this.getNamespace())||(undefined!==name&&null!==name&&name!=this.getName()))
{throw("Expected "+this.TYPES[type]);}};this.setInput=function(input)
{_tokens=input.match(new RegExp(XML_SPE,"g"));};var _currentToken=function()
{if(undefined===_tokens||null===_tokens||_index<0||_index>_tokens.length)
{return null;}
return _tokens[_index];};var _getToken=function(index)
{if(undefined===_tokens||null===_tokens||index<0||index>_tokens.length)
{return null;}
return _tokens[index];};var _extractAttributes=function(token)
{var attributes=[];if(undefined===token||null===token)
{return attributes;}
var atts=token.match(_attsExtract);if(undefined===atts||null===atts)
{return attributes;}
for(var i=0;i<atts.length;i++)
{var att=atts[i];parts=att.split("=");var name=parts[0];delete parts[0];var value=parts.join('');value=value.substring(1,value.length-1);attribute={};attribute.name=name;attribute.value=value;attributes.push(attribute);attributes[name]=value;}
return attributes;};var _extractCdataText=function(token)
{if(undefined===token||null===token)
{return'';}
var text='';var matches=token.match(_cdataExtract);if(undefined!==matches&&null!==matches)
{text=matches[1];}
return text;};var _extractCommentText=function(token)
{if(undefined===token||null===token)
{return'';}
var text='';var matches=token.match(_commentExtract);if(undefined!==matches&&null!==matches)
{text=matches[1];}
return text;};var _extractPiText=function(token)
{if(undefined===token||null===token)
{return'';}
var text='';var matches=token.match(_piExtract);if(undefined!==matches&&null!==matches)
{text=matches[1];}
return text;};var _extractEntityText=function(token)
{if(undefined===token||null===token)
{return'';}
var text='';if(token.substring(0,1)=="&")
{if(token.substring(0,2)=="&#")
{if(token.substring(0,3)=="&#x")
{text=String.fromCharCode(parseInt(token.substring(3,token.length-1),16));}
else
{text=String.fromCharCode(parseInt(token.substring(2,token.length-1),10));}}
else
{if("&gt;"==token)
{text=">";}
else if("&lt;"==token)
{text="<";}
else if("&amp;"==token)
{text="&";}
else if("&quot;"==token)
{text="\"";}
else if("&apos;"==token)
{text="'";}
else
{text=token;}}}
else
{text=token;}
return text;};var _getTokenType=function(token)
{if(undefined===token||null===token)
{return that.END_DOCUMENT;}
else if(_stagTest.test(token))
{return that.START_TAG;}
else if(_etagTest.test(token))
{return that.END_TAG;}
else if(_textTest.test(token))
{return that.TEXT;}
else if(_cdsectTest.test(token))
{return that.CDSECT;}
else if(_entityTest.test(token))
{return that.ENTITY_REF;}
else if(_piTest.test(token))
{return that.PROCESSING_INSTRUCTION;}
else if(_commentTest.test(token))
{return that.COMMENT;}
else if(_docdeclTest.test(token))
{return that.DOCDECL;}
throw("Invalid token: "+token);};};