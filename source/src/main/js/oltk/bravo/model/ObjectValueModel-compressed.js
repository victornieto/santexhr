
oltk.namespace('oltk.bravo.model');oltk.bravo.model.ObjectValueModel=function()
{var _value=null;var _handlers=[];this.getValue=function()
{return _value;};this.setValue=function(value)
{if(_value!==value)
{_value=value;_fireValueChanged();}};this.addValueChangedHandler=function(handler)
{_handlers.push(handler);};this.fireValueChanged=function()
{_fireValueChanged();};var _fireValueChanged=function()
{for(var i=0;i<_handlers.length;i++)
{var handler=_handlers[i];handler();}};};