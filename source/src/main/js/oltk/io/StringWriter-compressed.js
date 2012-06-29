
oltk.namespace('oltk.io');oltk.include('oltk/lang/StringBuffer.js');oltk.io.StringWriter=function()
{var that=this;var _buffer=new oltk.lang.StringBuffer();this.close=function()
{};this.flush=function()
{};this.getBuffer=function()
{return _buffer;};this.toString=function()
{return _buffer.toString();};this.write=function(chr)
{_buffer.append(chr.charAt(0));};this.writeArray=function(chars)
{this.writeSubarray(chars,0,chars.length);};this.writeSubarray=function(chars,offset,length)
{for(var i=0;i<length;i++)
{var c=chars[offset+i];_buffer.append(c);}};this.writeString=function(str)
{_buffer.append(str);};this.writeSubstring=function(str,offset,length)
{var s=str.substring(offset,offset+length);_buffer.append(s);};};