
oltk.namespace('oltk.bravo');oltk.include('jquery/jquery.js');oltk.include('oltk/bravo/action/ActionDispatcher.js');oltk.bravo.register=function(name,controller)
{var dispatcher=oltk.bravo.action.ActionDispatcher.getInstance();dispatcher.registerController(name,controller);};oltk.bravo.execute=function(controller,action,params)
{var dispatcher=oltk.bravo.action.ActionDispatcher.getInstance();dispatcher.dispatch(controller,action,params);};oltk.bravo.params=function(selector,context)
{var result={};jQuery(selector,context).each(function(index){var key=this.name||this.id;if(!key)
{return;}
else if(jQuery(this).is(':radio'))
{if(this.checked)
{result[key]=oltk.bravo._val(this);}}
else if(jQuery(this).is(':checkbox'))
{if(this.checked)
{result[key]=result[key]||[];result[key].push(oltk.bravo._val(this));}}
else
{result[key]=oltk.bravo._val(this);}});return result;};oltk.bravo._val=function(el)
{try
{return jQuery(el).val();}
catch(e)
{return el.value;}};oltk.bravo.expand=function(selector,context,delim)
{var params=oltk.bravo.params(selector,context);return oltk.bravo.expandProperties(params,delim);};oltk.bravo.expandProperties=function(obj,delim)
{var result={};for(var name in obj)if(obj.hasOwnProperty(name))
{var n=name.split(delim||'.');var o=result;for(var i=0;i<n.length;i++)
{if(i<n.length-1)
{o[n[i]]=o[n[i]]||{};o=o[n[i]];}
else
{o[n[i]]=obj[name];}}}
return result;};