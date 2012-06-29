
oltk.namespace('oltk.bravo.resource');oltk.include('jquery/jquery.js');oltk.bravo.resource.FileManager=function()
{this.fetch=function(path)
{var result='';var options={type:'GET',url:path,async:false};result=jQuery.ajax(options).responseText;return result;};};oltk.bravo.resource.FileManager.__instance=new oltk.bravo.resource.FileManager();oltk.bravo.resource.FileManager.getInstance=function()
{return oltk.bravo.resource.FileManager.__instance;};