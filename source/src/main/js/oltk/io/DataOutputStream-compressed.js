
oltk.namespace('oltk.io');oltk.include('oltk.io.ByteArrayOutputStream.js');try
{if(typeof(oltk.io.Utf8Encoder)=='undefined')
{throw"";}}
catch(e)
{throw"oltk.io.Utf8Encoder required!";}
try
{if(typeof(oltk.io.ByteArrayOutputStream)=='undefined')
{throw"";}}
catch(e)
{throw"oltk.io.ByteArrayOutputStream required!";}
oltk.io.DataOutputStream=function(stream)
{if(typeof(stream)=='undefined'||stream===null)
{throw"stream is null!";}
if(typeof(stream.write)=='undefined'||typeof(stream.writeArray)=='undefined'||typeof(stream.writeSubarray)=='undefined')
{throw"stream is not an OutputStream!";}
var that=this;var _os=stream;this.close=function()
{_os.close();_os=null;};this.flush=function()
{_os.flush();};this.writeBoolean=function(b)
{if(b)
{_os.write(0x1);}
else
{_os.write(0x0);}};this.writeByte=function(b)
{_os.write(b);};this.writeChar=function(c)
{var code=c.charCodeAt(0);this.writeShort(code);};this.writeInt=function(i)
{for(var x=3;x>=0;x--)
{var b=((i>>(x*8))&0xff);_os.write(b);}};this.writeShort=function(s)
{for(var x=1;x>=0;x--)
{var b=((s>>(x*8))&0xff);_os.write(b);}};this.writeUtf=function(str)
{var baos=new oltk.io.ByteArrayOutputStream();for(var i=0;i<str.length;i++)
{oltk.io.Utf8Encoder.encode(baos,str.charAt(i));}
var bytes=baos.toByteArray();this.writeShort(bytes.length);for(var w=0;w<bytes.length;w++)
{this.writeByte(bytes[w]);}};};