;(function() {

oltk.namespace('oltk.lang');

//============================================================================
// CLASS
//============================================================================
/**
 * Modification of John Resig's Class.
 * @see http://ejohn.org/blog/simple-javascript-inheritance/
 */
oltk.lang.Class = function(){};

/**
 * Extends a class with new prototype functions.
 * @param {Object} props a hash of class configurations. All properties are 
 * applied to the prototype of the new class with the exception of the 
 * following:
 * 	
 * 		$static: {Object} TODO: reserved for static support
 * 		
 * 		$implements: {array|Object} TODO: reserved for interface support
 * 		
 * 		$mixin: {array|Object} TODO: reserved for mixin support
 * 
 * If defined, the following prototype properties have special meaning:
 * 
 * 		$constructor: {Function} This class's constructor.  
 * 						Called with the same arguments as new Class(args...).
 * 
 * 		$inherited: {Function} called whenever a class is extended. 
 * 			Usage is similar to ruby's Class.inherited.
 * 			Example:
 * 				Foo = oltk.lang.Class.extend({
 * 					subclasses: [],
 * 					$inherited: function(subclass) {
 * 						this.subclasses.push(subclass);
 * 					}
 * 				});
 * 				Bar = Foo.extend();
 * 				Baz = Bar.extend();
 * 				assertArrayEquals([Bar,Baz], Foo.prototype.subclasses);
 * 
 * @example
 * <pre>
 * var Person = oltk.lang.Class.extend({
 * 		$constructor: function(isDancing) {
 * 			Person.instances++;
 * 			this.dancing = isDancing;
 * 		},
 * 		dance: function() {
 * 			return this.dancing;
 * 		},
 * 		walk: function() {
 * 			return true;
 * 		}
 * });
 * 
 * var Ninja = Person.extend({
 * 		$constructor: function() {
 * 			this._super(false);
 * 		},
 * 		dance: function() {
 * 			// Call the inherited version of dance()
 * 			return this._super();
 * 		},
 * 		swingSword: function() {
 * 			return true;
 * 		}
 * });
 * 
 * var p = new Person(true);
 * p.dance(); // => true
 * 
 * var n = new Ninja();
 * n.dance(); // => false
 * n.walk();  // => true
 * n.swingSword(); // => true
 * </pre>
 * 
 * @param {Object} {props}
 * @return {oltk.lang.Class} the new class
 */
oltk.lang.Class.extend = function(props) {
	
	var Class = _subClass(this);
	
	for(var name in props) {
		if(name === '$implements') {
			throw 'Class.extend: "$implements" property is reserved';
		}
		else if(name === '$mixin') {
			throw 'Class.extend: "$mixin" property is reserved';
		}
		else if(name === '$static'){
			throw 'Class.extend: "$static" property is reserved'; //_applyStatics(Class, props[name]);
		}
		else {
			_applyPrototype(Class, new Property(name, props));
		}
	}
	if(this.prototype.$inherited) {
		this.prototype.$inherited(Class);
	}
	return Class;
};

//============================================================================
// PRIVATE IMPLEMENTATION
//============================================================================
var initializing = false;
var fnTest = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;

/**
 * @private
 * @param{oltk.lang.Class} superclass
 * @return {Function}
 */
function _subClass(superclass) {
	// The dummy class constructor
	var Class =  function() {
		if(!initializing && this.$constructor) {
			this.$constructor.apply(this, arguments);
		}
	};

	// copy prototype properties
	initializing = true;
	Class.prototype = new superclass();
	initializing = false;
	
	// enforce constructor to be what we expect
	Class.constructor = Class;
	
	Class.extend = superclass.extend;
	
	return Class;
};

/**
 * @private
 * @param {oltk.lang.Class} Class
 * @param {Object} statics
 */
/*
function _applyStatics(Class, statics) {
	for(var each in statics) {
		var property = new Property(each, statics);
		property.append(Class, true);
	}
};
*/

/**
 * @private
 * @param {oltk.lang.Class} Class
 * @param {Property} property
 */
function _applyPrototype(Class, property) {
	if(property.overrides(Class.prototype)) {
		property.override(Class.prototype);
	}
	else {
		property.append(Class.prototype);
	}
};

//============================================================================
// PROPERTY
//============================================================================
/**
 * @private
 * @param {string} s the name of the property
 * @param {Object} o the object with the with property s
 * @return {Property}
 */
function Property(s, o) {
	this.name = s;
	this.isConstructor = this.name === '$constructor';
	this.reference = o[s];
	this.isMethod = typeof this.reference === 'function';
};
Property.prototype = {
	/**
	 * @param {Object} superclass
	 * @return {boolean} true if this property overides a method in the 
	 * superclass
	 */
	overrides: function(superclass) {
		return this.isMethod && 
			typeof superclass[this.name] === 'function' &&
			fnTest.test(this.reference);
	},
	/**
	 * @param {Object} o
	 */
	override: function(o) {
		o[this.name] = (function(fn, superclassFn){
			return function(){
				var tmp = this._super;
				this._super = superclassFn;
				var result = fn.apply(this, arguments);
				return result;
			}
		})(this.reference, o[this.name]);
	},
	
	/**
	 * @param {Object} o
	 * @param {boolean} [bind] true if the o should be the scope of the 
	 * property (only valid for methods)
	 */
	append: function(o, bind) {
		o[this.name] = bind && this.isMethod ? oltk.bind(this.reference, o) : this.reference;
	}
};

})();

