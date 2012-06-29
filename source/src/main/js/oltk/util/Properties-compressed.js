
oltk.namespace('oltk.util');oltk.include('oltk/util/Hashtable.js');oltk.util.Properties=function()
{var that=this;var _table=new oltk.util.Hashtable();var TOKEN_SP=' ';var TOKEN_TAB='\t';var TOKEN_FF='\f';var TOKEN_CR='\r';var TOKEN_NL='\n';var TOKEN_SHARP='#';var TOKEN_BANG='!';var TOKEN_ESC='\\';var TOKEN_COLON=':';var TOKEN_EQUAL='=';var TOKEN_R='r';var TOKEN_N='n';var TOKEN_T='t';var TOKEN_F='f';var TOKEN_U='u';this.clear=function()
{_table.clear();};this.getProperty=function(key)
{var value=null;if(_table.containsKey(key))
{value=_table.get(key);}
return value;};this.isEmpty=function()
{return _table.isEmpty();};this.remove=function(key)
{return _table.remove(key);};this.setProperty=function(key,value)
{return _table.put(key,value);};this.size=function()
{return _table.size();};this.toString=function()
{return _table.toString();};this.load=function(input)
{var LOAD_START=0;var LOAD_COM=LOAD_START+1;var LOAD_KEY=LOAD_COM+1;var LOAD_KES=LOAD_KEY+1;var LOAD_KCC=LOAD_KES+1;var LOAD_KCN=LOAD_KCC+1;var LOAD_S1=LOAD_KCN+1;var LOAD_S2=LOAD_S1+1;var LOAD_VAL=LOAD_S2+1;var LOAD_VES=LOAD_VAL+1;var LOAD_VCC=LOAD_VES+1;var LOAD_VCN=LOAD_VCC+1;var LOAD_U1=LOAD_VCN+1;var LOAD_U2=LOAD_U1+1;var LOAD_U3=LOAD_U2+1;var LOAD_U4=LOAD_U3+1;var LOAD_V1=LOAD_U4+1;var LOAD_V2=LOAD_V1+1;var LOAD_V3=LOAD_V2+1;var LOAD_V4=LOAD_V3+1;var state=LOAD_START;var keyBuffer=[];var valBuffer=[];var utfBuffer=0;var d=0;for(var i=0;i<input.length;i++)
{var c=input.charAt(i);switch(state)
{case LOAD_START:switch(c)
{case TOKEN_SP:case TOKEN_TAB:case TOKEN_FF:case TOKEN_CR:case TOKEN_NL:break;case TOKEN_SHARP:case TOKEN_BANG:state=LOAD_COM;break;default:keyBuffer.push(c);state=LOAD_KEY;break;}
break;case LOAD_COM:switch(c)
{case TOKEN_CR:case TOKEN_NL:state=LOAD_START;break;default:break;}
break;case LOAD_KEY:switch(c)
{case TOKEN_CR:case TOKEN_NL:_table.put(keyBuffer.join(''),"");keyBuffer.length=0;state=LOAD_START;break;case TOKEN_ESC:state=LOAD_KES;break;case TOKEN_SP:case TOKEN_TAB:case TOKEN_FF:state=LOAD_S1;break;case TOKEN_COLON:case TOKEN_EQUAL:state=LOAD_S2;break;default:keyBuffer.push(c);break;}
break;case LOAD_KES:switch(c)
{case TOKEN_CR:state=LOAD_KCC;break;case TOKEN_NL:state=LOAD_KCN;break;case TOKEN_R:keyBuffer.push(TOKEN_CR);state=LOAD_KEY;break;case TOKEN_N:keyBuffer.push(TOKEN_NL);state=LOAD_KEY;break;case TOKEN_F:keyBuffer.push(TOKEN_FF);state=LOAD_KEY;break;case TOKEN_T:keyBuffer.push(TOKEN_TAB);state=LOAD_KEY;break;case TOKEN_U:state=LOAD_U1;break;default:keyBuffer.push(c);state=LOAD_KEY;break;}
break;case LOAD_KCC:switch(c)
{case TOKEN_SP:case TOKEN_TAB:case TOKEN_FF:case TOKEN_NL:state=LOAD_KCN;break;case TOKEN_CR:_table.put(keyBuffer.join(''),"");keyBuffer.length=0;state=LOAD_START;break;case TOKEN_ESC:state=LOAD_KES;break;default:keyBuffer.push(c);state=LOAD_KEY;break;}
break;case LOAD_KCN:switch(c)
{case TOKEN_SP:case TOKEN_TAB:case TOKEN_FF:break;case TOKEN_CR:case TOKEN_NL:_table.put(keyBuffer.join(''),"");keyBuffer.length=0;state=LOAD_START;break;case TOKEN_ESC:state=LOAD_KES;break;default:keyBuffer.push(c);state=LOAD_KEY;break;}
break;case LOAD_S1:switch(c)
{case TOKEN_SP:case TOKEN_TAB:case TOKEN_FF:break;case TOKEN_CR:case TOKEN_NL:_table.put(keyBuffer.join(''),"");keyBuffer.length=0;state=LOAD_START;break;case TOKEN_COLON:case TOKEN_EQUAL:state=LOAD_S2;break;case TOKEN_ESC:state=LOAD_VES;break;default:valBuffer.push(c);state=LOAD_VAL;break;}
break;case LOAD_S2:switch(c)
{case TOKEN_SP:case TOKEN_TAB:case TOKEN_FF:break;case TOKEN_CR:case TOKEN_NL:_table.put(keyBuffer.join(''),"");keyBuffer.length=0;state=LOAD_START;break;case TOKEN_ESC:state=LOAD_VES;break;default:valBuffer.push(c);state=LOAD_VAL;break;}
break;case LOAD_VAL:switch(c)
{case TOKEN_CR:case TOKEN_NL:_table.put(keyBuffer.join(''),valBuffer.join(''));keyBuffer.length=0;valBuffer.length=0;state=LOAD_START;break;case TOKEN_ESC:state=LOAD_VES;break;default:valBuffer.push(c);break;}
break;case LOAD_VES:switch(c)
{case TOKEN_CR:state=LOAD_VCC;break;case TOKEN_NL:state=LOAD_VCN;break;case TOKEN_R:valBuffer.push(TOKEN_CR);state=LOAD_VAL;break;case TOKEN_N:valBuffer.push(TOKEN_NL);state=LOAD_VAL;break;case TOKEN_F:valBuffer.push(TOKEN_FF);state=LOAD_VAL;break;case TOKEN_T:valBuffer.push(TOKEN_TAB);state=LOAD_VAL;break;case TOKEN_U:state=LOAD_V1;break;default:valBuffer.push(c);state=LOAD_VAL;break;}
break;case LOAD_VCC:switch(c)
{case TOKEN_SP:case TOKEN_TAB:case TOKEN_FF:case TOKEN_NL:state=LOAD_VCN;break;case TOKEN_CR:_table.put(keyBuffer.join(''),valBuffer.join(''));keyBuffer.length=0;valBuffer.length=0;state=LOAD_START;break;case TOKEN_ESC:state=LOAD_VES;break;default:valBuffer.push(c);state=LOAD_VAL;break;}
break;case LOAD_VCN:switch(c)
{case TOKEN_SP:case TOKEN_TAB:case TOKEN_FF:break;case TOKEN_CR:case TOKEN_NL:_table.put(keyBuffer.join(''),valBuffer.join(''));keyBuffer.length=0;valBuffer.length=0;state=LOAD_START;break;case TOKEN_ESC:state=LOAD_VES;break;default:valBuffer.push(c);state=LOAD_VAL;break;}
break;case LOAD_U1:d=parseInt(c,16);if(-1==d)
{throw new Exception("Invalid unicode escape sequence");}
utfBuffer=d;state=LOAD_U2;break;case LOAD_U2:d=parseInt(c,16);if(-1==d)
{throw new Exception("Invalid unicode escape sequence");}
utfBuffer=(utfBuffer<<4)+d;state=LOAD_U3;break;case LOAD_U3:d=parseInt(c,16);if(-1==d)
{throw new Exception("Invalid unicode escape sequence");}
utfBuffer=(utfBuffer<<4)+d;state=LOAD_U4;break;case LOAD_U4:d=parseInt(c,16);if(-1==d)
{throw new Exception("Invalid unicode escape sequence");}
utfBuffer=(utfBuffer<<4)+d;keyBuffer.push(String.fromCharCode(utfBuffer));utfBuffer=0;state=LOAD_KEY;break;case LOAD_V1:d=parseInt(c,16);if(-1==d)
{throw new Exception("Invalid unicode escape sequence");}
utfBuffer=d;state=LOAD_V2;break;case LOAD_V2:d=parseInt(c,16);if(-1==d)
{throw new Exception("Invalid unicode escape sequence");}
utfBuffer=(utfBuffer<<4)+d;state=LOAD_V3;break;case LOAD_V3:d=parseInt(c,16);if(-1==d)
{throw new Exception("Invalid unicode escape sequence");}
utfBuffer=(utfBuffer<<4)+d;state=LOAD_V4;break;case LOAD_V4:d=parseInt(c,16);if(-1==d)
{throw new Exception("Invalid unicode escape sequence");}
utfBuffer=(utfBuffer<<4)+d;valBuffer.push(String.fromCharCode(utfBuffer));utfBuffer=0;state=LOAD_VAL;break;}}
if((LOAD_U1==state)||(LOAD_U2==state)||(LOAD_U3==state)||(LOAD_U4==state)||(LOAD_V1==state)||(LOAD_V2==state)||(LOAD_V3==state)||(LOAD_V4==state))
{throw new Exception("Invalid unicode escape sequence");}
else if(keyBuffer.length>0)
{_table.put(keyBuffer.join(''),valBuffer.join(''));}};this.store=function(header)
{var output=[];var j=0;var c=0;var code=0;if(undefined!==header&&null!==header&&header.length>0)
{output.push(TOKEN_SHARP);output.push(header);output.push(TOKEN_NL);}
var keys=_table.keys();for(var i=0;i<keys.length;i++)
{var key=keys[i];var value=_table.get(key);for(j=0;j<key.length;j++)
{c=key.charAt(j);code=c.charCodeAt(0);if(code<32||code>126)
{output.push(TOKEN_ESC);output.push(TOKEN_U);output.push(_toHexString(code));}
else
{switch(c)
{case TOKEN_ESC:output.push(TOKEN_ESC);output.push(TOKEN_ESC);break;case TOKEN_TAB:output.push(TOKEN_ESC);output.push(TOKEN_T);break;case TOKEN_FF:output.push(TOKEN_ESC);output.push(TOKEN_F);break;case TOKEN_NL:output.push(TOKEN_ESC);output.push(TOKEN_N);break;case TOKEN_CR:output.push(TOKEN_ESC);output.push(TOKEN_R);break;case TOKEN_SP:case TOKEN_SHARP:case TOKEN_BANG:case TOKEN_EQUAL:case TOKEN_COLON:output.push(TOKEN_ESC);output.push(c);break;default:output.push(c);break;}}}
output.push(TOKEN_EQUAL);var isLeading=true;for(j=0;j<value.length;j++)
{c=value.charAt(j);if(TOKEN_SP!=c)
{isLeading=false;}
code=c.charCodeAt(0);if(code<32||code>126)
{output.push(TOKEN_ESC);output.push(TOKEN_U);output.push(_toHexString(code));}
else
{switch(c)
{case TOKEN_ESC:output.push(TOKEN_ESC);output.push(TOKEN_ESC);break;case TOKEN_TAB:output.push(TOKEN_ESC);output.push(TOKEN_T);break;case TOKEN_FF:output.push(TOKEN_ESC);output.push(TOKEN_F);break;case TOKEN_NL:output.push(TOKEN_ESC);output.push(TOKEN_N);break;case TOKEN_CR:output.push(TOKEN_ESC);output.push(TOKEN_R);break;case TOKEN_SP:if(isLeading)
{output.push(TOKEN_ESC);output.push(c);}
else
{output.push(c);}
break;case TOKEN_SHARP:case TOKEN_BANG:case TOKEN_EQUAL:case TOKEN_COLON:output.push(TOKEN_ESC);output.push(c);break;default:output.push(c);break;}}}
output.push(TOKEN_NL);}
return output.join('');};var _toHexString=function(decNumber)
{var dec=decNumber;hex='0000'+dec.toString(16);hex=hex.substring(hex.length-4).toUpperCase();return hex;};};