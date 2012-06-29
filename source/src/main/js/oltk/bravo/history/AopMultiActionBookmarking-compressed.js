;(function(){oltk.namespace('oltk.bravo.history');oltk.include('jquery/jquery.js');oltk.include('jquery/aop/aop.js');oltk.include('oltk/bravo/history/YuiHistoryManager.js');var History=oltk.bravo.history.YuiHistoryManager;var DO_NOT_BOOKMARK=false;var self=oltk.bravo.history.AopMultiActionBookmarking={_actions:{},_defaultActions:{},_actionId:0,weave:function(varargs){for(var i=0;i<arguments.length;i++){var o=arguments[i];if(!o){continue;}
self._register(o);self._weaveAdvice(o);}
return self;},initialize:function(o){o=o||{};History.initialize({defaultState:self._callDefaultActions,onDomReady:o.onDomReady,onStateReady:o.onStateReady});},_register:function(o){if(o.defaultTo){self._cacheDefaultAction(o);}
History.register(o.name,function(state){if(!state){DO_NOT_BOOKMARK=true;self._getDefaultActionFor(o.name).call();return;}
var id=state.pop();self._actions[id].apply(null,state);});},_weaveAdvice:function(o){for(var p in o.target){if(self._isNotAdvisableMethodOf(o,p)){continue;}
self._advise(o,p,o.defaultTo);}},_advise:function(o,p,defaultTo){var id=self._cacheAction(o.target,p);if(p===defaultTo){jQuery.aop.before({target:o.target,method:p},function(args){if(DO_NOT_BOOKMARK){DO_NOT_BOOKMARK=false;return;}
else{args=oltk.toArray(args).concat(id);History.bookmark(o.name,args);}});}
else{jQuery.aop.before({target:o.target,method:p},function(args){args=oltk.toArray(args).concat(id);History.bookmark(o.name,args);});}},_cacheAction:function(target,method){var action=function(){target[method].apply(target,arguments);}
var id=self._actionId++;self._actions[id]=action;return id;},_cacheDefaultAction:function(o){var action=function(){o.target[o.defaultTo].apply(o.target,arguments);};self._defaultActions[o.name]=action;},_getDefaultActionFor:function(name){return self._defaultActions[name]||oltk.emptyFn;},_isNotAdvisableMethodOf:function(o,p){if(typeof o.target[p]!=='function'){return true;}
else if(typeof o.methods==='string'){return o.methods!==p;}
else{return!o.methods.test(p)&&p!==o.defaultTo;}},_callDefaultActions:function(){for(var p in self._defaultActions){if(typeof self._defaultActions[p]!=='function'){continue;}
self._defaultActions[p].call();}}};})();