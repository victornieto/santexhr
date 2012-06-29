
oltk.namespace('oltk.bravo.model');oltk.bravo.model.SimpleListModel=function()
{var _list=[];var _contentHandlers=[];this.addContentsChangedHandler=function(handler)
{_contentHandlers.push(handler);};this.append=function(item)
{_list.push(item);_fireContentsChanged();};this.clear=function()
{var sel=_selection;_list=[];_fireContentsChanged();};this.get=function(index)
{return _list[index];};this.set=function(index,item)
{_list[index]=item;_fireContentsChanged();};this.size=function()
{return _list.length;};this.indexOf=function(item)
{var result=-1;for(var i=0;i<_list.length;i++)
{if(_list[i]==item)
{result=i;}}
return result;};this.toArray=function()
{var result=[];for(var i=0;i<_list.length;i++)
{result.push(_list[i]);}
return result;};var _fireContentsChanged=function()
{for(var i=0;i<_contentHandlers.length;i++)
{var handler=_contentHandlers[i];handler();}};};