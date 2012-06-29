
oltk.namespace('oltk.lang');oltk.lang.StringBuffer=function()
{var that=this;var _buffer=[];this.append=function(str)
{_buffer.push(str);return that;};this.charAt=function(index)
{var value=_buffer.join('');_buffer=[];_buffer.push(value);return value.charAt(index);};this.remove=function(start,end)
{var value=_buffer.join('');_buffer=[];_buffer.push(value.substring(0,start));_buffer.push(value.substring(end));return that;};this.deleteCharAt=function(index)
{var value=_buffer.join('');_buffer=[];_buffer.push(value.substring(0,index));_buffer.push(value.substring(index+1));return that;};this.insert=function(offset,str)
{var value=_buffer.join('');_buffer=[];_buffer.push(value.substring(0,offset));_buffer.push(str);_buffer.push(value.substring(offset));return that;};this.length=function()
{var value=_buffer.join('');_buffer=[];_buffer.push(value);return value.length;};this.reverse=function()
{var value=_buffer.join('');_buffer=[];for(var i=value.length-1;i>=0;i--)
{_buffer.push(value.charAt(i));}
return that;};this.setCharAt=function(index,chr)
{var value=_buffer.join('');_buffer=[];_buffer.push(value.substring(0,index));_buffer.push(chr);_buffer.push(value.substring(index+1));};this.setLength=function(len)
{var value=_buffer.join('');_buffer=[];_buffer.push(value.substring(0,len));};this.toString=function()
{var value=_buffer.join('');return value;};};