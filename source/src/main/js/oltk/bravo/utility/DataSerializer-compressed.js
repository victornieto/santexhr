
oltk.namespace('oltk.bravo.utility');oltk.include('oltk/util/LangUtils.js');oltk.include('oltk/xmlpull/XmlSerializer.js');oltk.bravo.utility.DataSerializer=function()
{this.toXml=function(rootName,data)
{var xmls=new oltk.xmlpull.XmlSerializer();xmls.startDocument(null,true);if(undefined===data||null===data)
{_scalarToXml(xmls,rootName,'');}
else if(oltk.util.LangUtils.isArray(data))
{_arrayToXml(xmls,rootName,data);}
else if(oltk.util.LangUtils.isObject(data))
{_objectToXml(xmls,rootName,data);}
else
{_scalarToXml(xmls,rootName,data);}
xmls.endDocument();return xmls.getOutput();};var _objectToXml=function(xmls,name,data)
{xmls.startTag(null,name);for(var prop in data)if(data.hasOwnProperty(prop))
{var value=data[prop];if(undefined===value||null===value)
{_scalarToXml(xmls,prop,'');}
else if(oltk.util.LangUtils.isArray(value))
{_arrayToXml(xmls,prop,value);}
else if(oltk.util.LangUtils.isObject(value))
{_objectToXml(xmls,prop,value);}
else
{_scalarToXml(xmls,prop,value);}}
xmls.endTag(null,name);};var _arrayToXml=function(xmls,name,data)
{xmls.startTag(null,name);for(var i=0;i<data.length;i++)
{var value=data[i];if(undefined===value||null===value)
{_scalarToXml(xmls,'item','');}
else if(oltk.util.LangUtils.isArray(value))
{_arrayToXml(xmls,'item',value);}
else if(oltk.util.LangUtils.isObject(value))
{_objectToXml(xmls,'item',value);}
else
{_scalarToXml(xmls,'item',value);}}
xmls.endTag(null,name);};var _scalarToXml=function(xmls,name,data)
{xmls.startTag(null,name);xmls.text(data);xmls.endTag(null,name);};};