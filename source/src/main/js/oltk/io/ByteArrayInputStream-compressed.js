
oltk.namespace('oltk.io');oltk.io.ByteArrayInputStream=function(bytes)
{var that=this;var _buffer=bytes;var _index=0;this.available=function()
{return _buffer.length-_index;};this.close=function()
{_buffer=null;};this.read=function()
{if(_index>=_buffer.length)
{return-1;}
return _buffer[_index++];};this.readArray=function(arr,offset,length)
{if(_index>=_buffer.length)
{return-1;}
var count=Math.min(length,_buffer.length-_index);for(var i=0;i<count;i++)
{var b=_buffer[_index++];arr[offset+i]=b;}
return count;};this.skip=function(count)
{var n=Math.min(count,_buffer.length-_index);_index+=n;return n;};};