
oltk.namespace('oltk.bravo.presentation');oltk.bravo.presentation.RenderManager=function()
{this.xsltRender=function(template,data,params)
{oltk.include('gr/abiss/js/sarissa/sarissa.js');var xml=(new DOMParser()).parseFromString(data,"text/xml");if(Sarissa.getParseErrorText(xml)!=Sarissa.PARSED_OK)
{alert(Sarissa.getParseErrorText(xml));}
var xsl=(new DOMParser()).parseFromString(template,"text/xml");if(Sarissa.getParseErrorText(xsl)!=Sarissa.PARSED_OK)
{alert(Sarissa.getParseErrorText(xsl));}
var xslt=new XSLTProcessor();xslt.importStylesheet(xsl);if(null!=params)
{for(var name in params)
{var value=params[name];xslt.setParameter(null,name,value);}}
var output=xslt.transformToDocument(xml);var result=(new XMLSerializer()).serializeToString(output);return result;};this.ejsRender=function(template,data)
{oltk.include('ejs/ejs.js');var ejs=new EJS({text:template});var result=ejs.render(data);return result;};};oltk.bravo.presentation.RenderManager.__instance=new oltk.bravo.presentation.RenderManager();oltk.bravo.presentation.RenderManager.getInstance=function()
{return oltk.bravo.presentation.RenderManager.__instance;};