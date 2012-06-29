
oltk.namespace('oltk.io');oltk.io.StringReader=function(str)
{if(typeof(str)!='string')
{throw"str is not a string!";}
var that=this;var _buffer=str;var _index=0;this.close=function()
{_buffer=null;};this.read=function()
{if(_index>=_buffer.length)
{return-1;}
return _buffer.charAt(_index++);};this.readArray=function(arr)
{if(_index>=_buffer.length)
{return-1;}
var count=_buffer.length-_index;for(var i=0;i<count;i++)
{var c=_buffer.charAt(_index++);arr.push(c);}
return count;};this.readSubarray=function(arr,offset,length)
{if(_index>=_buffer.length)
{return-1;}
var count=Math.min(_buffer.length-_index,length);for(var i=0;i<count;i++)
{var c=_buffer.charAt(_index++);arr[offset+i]=c;}
return count;};this.skip=function(n)
{var count=Math.min(_buffer.length-_index,n);_index+=count;return count;};};