
oltk.namespace('oltk.bravo.model');oltk.bravo.model.SelectListModel=function()
{var _list=[];var _selection=-1;var _contentHandlers=[];var _selectionHandlers=[];this.addContentsChangedHandler=function(handler)
{_contentHandlers.push(handler);};this.addSelectionChangedHandler=function(handler)
{_selectionHandlers.push(handler);};this.append=function(item)
{_list.push(item);_fireContentsChanged();};this.clear=function()
{var sel=_selection;_list=[];_selection=-1;_fireContentsChanged();if(sel>=0)
{_fireSelectionChanged();}};this.get=function(index)
{return _list[index];};this.set=function(index,item)
{_list[index]=item;_fireContentsChanged();};this.size=function()
{return _list.length;};this.indexOf=function(item)
{var result=-1;for(var i=0;i<_list.length;i++)
{if(_list[i]==item)
{result=i;}}
return result;};this.toArray=function()
{var result=[];for(var i=0;i<_list.length;i++)
{result.push(_list[i]);}
return result;};this.getSelectedIndex=function()
{return _selection;};this.setSelectedIndex=function(index)
{if(index!=_selection)
{_selection=index;_fireSelectionChanged();}};var _fireContentsChanged=function()
{for(var i=0;i<_contentHandlers.length;i++)
{var handler=_contentHandlers[i];handler();}};var _fireSelectionChanged=function()
{for(var i=0;i<_selectionHandlers.length;i++)
{var handler=_selectionHandlers[i];handler();}};};