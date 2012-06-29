
oltk.namespace('oltk.io');oltk.io.Utf8Writer=function(stream)
{if(typeof(stream)=='undefined'||stream===null)
{throw"stream is null!";}
if(typeof(stream.write)=='undefined'||typeof(stream.writeArray)=='undefined')
{throw"stream is not an OutputStream";}
var that=this;var _os=stream;this.close=function()
{_os.close();_os=null;};this.flush=function()
{_os.flush();};this.write=function(chr)
{var c=chr.charAt(0);_encode(_os,c);};this.writeArray=function(chars)
{this.writeSubarray(chars,0,chars.length);};this.writeSubarray=function(chars,offset,length)
{for(var i=0;i<length;i++)
{var c=chars[offset+i];_encode(_os,c);}};this.writeString=function(str)
{this.writeSubstring(str,0,str.length);};this.writeSubstring=function(str,offset,length)
{for(var i=0;i<length;i++)
{var c=str.charAt(offset+i);_encode(_os,c);}};var _encode=function(stream,chr)
{var code=chr.charCodeAt(0);var b1=0;var b2=0;var b3=0;if(code<=0x7f)
{stream.write(code);}
else if(code>0x7f&&code<=0x7ff)
{b1=(0xc0|(0x1f&(code>>6)));b2=(0x80|(0x3f&code));stream.write(b1);stream.write(b2);}
else if(code>0x7ff&&code<=0xffff)
{b1=(0xe0|(0x0f&(code>>12)));b2=(0x80|(0x3f&(code>>6)));b3=(0x80|(0x3f&code));stream.write(b1);stream.write(b2);stream.write(b3);}};};