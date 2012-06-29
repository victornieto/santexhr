
oltk.namespace('oltk.xmlpull');oltk.xmlpull.XmlSerializer=function()
{var that=this;var _output=[];var _openTag=false;this.attribute=function(ns,name,value)
{if(!_openTag)
{return;}
_output.push(' ');_output.push((null===ns?name:ns+':'+name));_output.push('=');_output.push('"');_output.push(_escapeXml(value));_output.push('"');return this;};this.cdsect=function(text)
{_nodeTransition();_output.push('<![CDATA[');_output.push(text);_output.push(']]>');};this.comment=function(text)
{_nodeTransition();_output.push('<!--');_output.push(_escapeXml(text));_output.push('-->');};this.docdecl=function(text)
{_output.push(text);};this.endDocument=function()
{_nodeTransition();};this.endTag=function(ns,name)
{if(_openTag)
{_output.push('/>');_openTag=false;}
else
{_output.push('</');_output.push((null===ns?name:ns+':'+name));_output.push('>');}
return this;};this.flush=function()
{_nodeTransition();};this.getOutput=function()
{return _output.join('');};this.processingInstruction=function(text)
{_nodeTransition();_output.push('<?');_output.push(text);_output.push('?>');};this.reset=function()
{_output.length=0;_openTag=false;};this.startDocument=function(encoding,standalone)
{if(null!==encoding)
{_output.push('<?xml version="1.0" encoding="'+encoding+'" ?>');}
else
{_output.push('<?xml version="1.0" ?>');}};this.startTag=function(ns,name)
{_nodeTransition();_output.push('<');_output.push((null===ns?name:ns+':'+name));_openTag=true;return this;};this.text=function(text)
{_nodeTransition();_output.push(_escapeXml(text));return this;};var _nodeTransition=function()
{if(_openTag)
{_output.push('>');_openTag=false;}};var _escapeXml=function(att)
{if(undefined===att||null===att)
{return att;}
att=att.toString();att=att.replace(/&/g,"&amp;");att=att.replace(/</g,"&lt;");att=att.replace(/>/g,"&gt;");att=att.replace(/"/g,"&quot;");return att;};};