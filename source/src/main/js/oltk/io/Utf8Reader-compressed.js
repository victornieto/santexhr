
oltk.namespace('oltk.io');oltk.io.Utf8Reader=function(stream)
{if(typeof(stream)=='undefined'||stream===null)
{throw'stream is null!';}
if(typeof(stream.read)=='undefined'||typeof(stream.readArray)=='undefined')
{throw"stream is not an InputStream";}
var that=this;var _is=stream;this.close=function()
{_is.close();_is=null;};this.read=function()
{var c=_decode(_is);if(''===c)
{return-1;}
return c;};this.readArray=function(arr)
{var count=0;while(true)
{var c=_decode(_is);if(''===c)
{if(count===0)
{return-1;}
else
{return count;}}
arr.push(c);count++;}};this.readSubarray=function(arr,offset,length)
{var count=0;for(count=0;count<length;count++)
{var c=_decode(_is);if(''===c)
{if(count===0)
{return-1;}
else
{return count;}}
arr[offset+count]=c;}
return count;};this.skip=function(n)
{var count=0;for(count=0;count<n;count++)
{var c=_decode(_is);if(''===c)
{return count;}}
return count;};var _decode=function(stream)
{var b1=stream.read();var code=0x0;if(-1==b1)
{return'';}
else if((b1&0x80)===0x0)
{code=b1;}
else if((b1&0xe0)==0xc0)
{var b2=stream.read();if(-1==b2)
{return'';}
code=(((b1&0x1f)<<6)|(b2&0x3f));}
else if((b1&0xf0)==0xe0)
{var b2=stream.read();var b3=stream.read();if(-1==b2||-1==b3)
{return'';}
code=(((b1&0x0f)<<12)|((b2&0x3f)<<6)|(b3&0x3f));}
return String.fromCharCode(code);};};