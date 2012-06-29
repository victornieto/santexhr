
oltk.namespace('oltk.bravo.action');oltk.bravo.action.ActionDispatcher=function()
{var that=this;var _controlTable={};this.registerController=function(name,controller)
{_controlTable[name]=controller;};this.dispatch=function(controllerName,action,params)
{var controller=_controlTable[controllerName];controller[action](params);};};oltk.bravo.action.ActionDispatcher.__instance=new oltk.bravo.action.ActionDispatcher();oltk.bravo.action.ActionDispatcher.getInstance=function()
{return oltk.bravo.action.ActionDispatcher.__instance;};