
oltk.namespace('oltk.bravo.presentation');oltk.include('jquery/jquery.js');oltk.bravo.presentation.DisplayManager=function()
{this.count=function(selector)
{return jQuery(selector).length;};this.append=function(selector,content)
{jQuery(selector).append(content);};this.prepend=function(selector,content)
{jQuery(selector).prepend(content);};this.after=function(selector,content)
{jQuery(selector).after(content);};this.before=function(selector,content)
{jQuery(selector).before(content);};this.replace=function(selector,content)
{jQuery(selector).replaceWith(content);};this.empty=function(selector)
{jQuery(selector).empty();};this.remove=function(selector)
{jQuery(selector).remove();};};oltk.bravo.presentation.DisplayManager.__instance=new oltk.bravo.presentation.DisplayManager();oltk.bravo.presentation.DisplayManager.getInstance=function()
{return oltk.bravo.presentation.DisplayManager.__instance;};