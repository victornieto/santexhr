
oltk.namespace('oltk.io');oltk.include('oltk.io.ByteArrayInputStream.js');try
{if(typeof(oltk.io.Utf8Encoder)=='undefined')
{throw"";}}
catch(e)
{throw"oltk.io.Utf8Encoder required!";}
try
{if(typeof(oltk.io.ByteArrayInputStream)=='undefined')
{throw"";}}
catch(e)
{throw"oltk.io.ByteArrayInputStream required!";}
oltk.io.DataInputStream=function(stream)
{if(typeof(stream)=='undefined'||stream===null)
{throw"stream is null";}
if(typeof(stream.read)=='undefined'||typeof(stream.readArray)=='undefined')
{throw"stream is not an InputStream";}
var that=this;var _is=stream;this.available=function()
{return _is.available();};this.close=function()
{_is.close();_is=null;};this.readBoolean=function()
{var b=_is.read();if(-1==b)
{throw"EOF!";}
if(b===0x0)
{return false;}
return true;};this.readByte=function()
{var b=_is.read();if(-1==b)
{throw"EOF!";}
return b;};this.readChar=function()
{var code=this.readShort();return String.fromCharCode(code);};this.readInt=function()
{var i=0;for(var x=0;x<4;x++)
{var b=_is.read();if(-1==b)
{throw"EOF!";}
i=((i<<8)|(b&0xff));}
return i;};this.readShort=function()
{var s=0;for(var x=0;x<2;x++)
{var b=_is.read();if(-1==b)
{throw"EOF!";}
s=((s<<8)|(b&0xff));}
return s;};this.readUtf=function()
{var length=this.readShort();var bytes=[];for(var i=0;i<length;i++)
{var b=this.readByte();if(-1==b)
{throw"EOF!";}
bytes.push(b);}
var buffer=[];var baos=new oltk.io.ByteArrayInputStream(bytes);while(true)
{var c=oltk.io.Utf8Encoder.decode(baos);if(''===c)
{break;}
buffer.push(c);}
return buffer.join('');};};