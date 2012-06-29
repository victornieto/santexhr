
oltk.namespace('oltk.util');oltk.util.Hashtable=function()
{var that=this;var _table=[];this.clear=function()
{_table=[];};this.containsKey=function(key)
{return(key in _table);};this.containsValue=function(value)
{var exists=false;for(var k in _table)if(_table.hasOwnProperty(k))
{var v=_table[k];if(v==value)
{exists=true;break;}}
return exists;};this.get=function(key)
{if(!(key in _table))
{return null;}
return _table[key];};this.isEmpty=function()
{return(size()===0);};this.keys=function()
{var keyArray=[];for(var key in _table)if(_table.hasOwnProperty(key))
{keyArray.push(key);}
return keyArray;};this.put=function(key,value)
{var prev=null;if(key in _table)
{prev=_table[key];}
_table[key]=value;return prev;};this.remove=function(key)
{var value=_table[key];delete _table[key];return value;};this.size=function()
{var count=0;for(var key in _table)if(_table.hasOwnProperty(key))
{count++;}
return count;};this.toString=function()
{var result=[];for(var key in _table)if(_table.hasOwnProperty(key))
{var value=_table[key];if(result.length>0)
{result.push(',');}
result.push('{');result.push(key);result.push('=');result.push(value);result.push('}');}
return result.join('');};this.values=function()
{var result=[];for(var key in _table)if(_table.hasOwnProperty(key))
{var value=_table[key];result.push(value);}
return result;};};