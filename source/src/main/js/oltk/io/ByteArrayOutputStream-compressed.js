
oltk.namespace('oltk.io');oltk.io.ByteArrayOutputStream=function()
{var that=this;var _buffer=[];this.close=function()
{_buffer=null;};this.flush=function()
{};this.reset=function()
{_buffer=[];};this.size=function()
{return _buffer.length;};this.toByteArray=function()
{var result=[];for(var i=0;i<_buffer.length;i++)
{result[i]=_buffer[i];}
return result;};this.toString=function()
{var result=[];for(var i=0;i<_buffer.length;i++)
{var c=String.fromCharCode(_buffer[i]);result.push(c);}
return result.join('');};this.write=function(b)
{_buffer.push(b);};this.writeArray=function(bytes)
{this.writeSubarray(bytes,0,bytes.length);};this.writeSubarray=function(bytes,offset,length)
{for(var i=0;i<length;i++)
{_buffer.push(bytes[offset+i]);}};};