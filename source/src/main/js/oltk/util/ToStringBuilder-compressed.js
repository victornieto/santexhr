;(function(){oltk.namespace('oltk.util');oltk.util.ToStringBuilder=function(data,depth,o){o=o||{};depth=parseInt(depth)||1;depth=Math.max(depth,1);this._data=data;this._recursionDepth=depth;this._inspectWindow=o.inspectWindow||false;this._inspectDom=o.inspectDom||false;};oltk.util.ToStringBuilder.prototype={_skipDomProperties:{document:true,ownerDocument:true,all:true,parentElement:true,parentNode:true,offsetParent:true,children:true,firstChild:true,lastChild:true,previousSibling:true,nextSibling:true,innerHTML:true,outerHTML:true,innerText:true,outerText:true,textContent:true,attributes:true,style:true,currentStyle:true,runtimeStyle:true,parentTextEdit:true},toString:function(){return this._toRecursiveString(this._data,this._recursionDepth,0);},_toRecursiveString:function(data,objectDepth,indentDepth){if(typeof data==='undefined'){return'undefined';}
else if(data===null){return'null';}
else if(typeof data==='function'){return'function';}
else if(typeof data.toUTCString==='function'){return data.toUTCString();}
else if(typeof data==='string'){var self=this;var result=data.replace(/\n|\r/g,function(ch){switch(ch){case'\n':return self._newLine(indentDepth+1);case'\r':return'';}});return'"'+result+'"';}
else if(typeof data.length==='number'&&(data.join||data.callee)){var result=['['];for(var i=0;i<data.length;i++){result.push(this._newLine(indentDepth+1));result.push(this._toRecursiveString(data[i],objectDepth,indentDepth+1));if(i<data.length-1){result.push(', ');}else{result.push(this._newLine(indentDepth));}}
result.push(']');return result.join('');}
else if(data===window&&!this._inspectWindow){return this._detailedTypeOf(data);}
else if(this._isDomNode(data)&&!this._inspectDom){var result=[this._detailedTypeOf(data)];if(data.id){result.push(' id: ',data.id);}
if(data.className){result.push(' className: ',data.className);}
return result.join('');}
else if(typeof data==='object'){if(objectDepth==0){return this._detailedTypeOf(data);}
var result=[];if(this._detailedTypeOf(data)!='Object'){result.push(this._detailedTypeOf(data));result.push(' ');}
result.push('{');for(var p in data){try{if(this._isDomNode(data)){if(!data[p])continue;if(typeof data[p]==='function')continue;if(this._skipDomProperties[p])continue;if(p.toUpperCase()===p)continue;}
result.push(this._newLine(indentDepth+1));result.push(p);result.push(': ');result.push(this._toRecursiveString(data[p],objectDepth-1,indentDepth+1));result.push(',');}catch(e){continue;}}
if(result.length>1){if(result[result.length-1]===','){result.pop();}
result.push(this._newLine(indentDepth));}
result.push('}');return result.join('');}
else{return data+'';}},_detailedTypeOf:function(x){var result=typeof x;if(result=='object'){result=Object.prototype.toString.apply(x);result=result.substring(8,result.length-1);}
return result;},_newLine:function(indent){var sb=['\n'];for(var i=0;i<indent;i++){sb.push('\u00A0\u00A0')}
return sb.join('');},_isDomNode:function(o){return o&&typeof o.nodeName==='string';}};})();