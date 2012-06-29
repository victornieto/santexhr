/**
 * @author Eugene Efimochkin
 * @extends jQuery()
 * @extends $()
 * @addon
 * 
 * This is a plugin for jQuery JavaScript library
 * It extends the jQuery() class with `.placehold()` method,
 * which is effective for text inputs and treats the default 
 * value those inputs have in HTML markup as a placehold value
 * which has to be cleared out, when a user wants to input 
 * a text in it and returned back if user had put nothing.
 * Additionally, this method can add a specified class to
 * active and inactive fields separately, enabling you to 
 * style and detect placeholded and active inputs. 
 * 
 * !!! REQUIRES jQuery v.1.2+ !!!
 * 
 * Example calls:
 *     $(":text").placehold()  -- treats a `value` attribute of each text input individually as its placeholder value
 *     $(":text").placehold({blurClass: "placehold"})  -- the same as above, but add a "placehold" to `class` of each element, when it is not in focus
 *     $(":text").placehold({focusClass: "focus"})  -- same as first, but adds a "focus" to active input class
 *     $("input[name='email']").placehold({placeholdValue: "Your email here"}) -- uses the value as a common placeholder string (a value from markup will be omitted)
 *     
 * You can use any combination of `blurClass`, `focusClass` and `value`
 * fields of `options` hash.
 *     
 * Please, remember to validate the form on submit, additionally 
 * checking fields for containing a placeholder, because 
 * placeholder strings are obviously treated as normal input 
 * values by UAs. If a user submits such form, it might even 
 * appear valid on the server side, filling your DB with bogus data
 *     
 */
(function(){
    jQuery.fn.extend(
        {
            /**
             * placehold()
             * @return {jQuery} - unchanged current jQuery selection
             * @param {Object} options Optional, an object with focusClass, blurClass and placeholdValue fields 
             */
            placehold: function(){
                var $query = this; // this is a current jQuery selection
                
                var placeholdOptions = arguments[0] || {}; 
                
                var $inputs = $query.filter(":text, :password");
                
                $inputs
                    .each(
                        function(){
                           var $this = jQuery(this);
                           this.placeholdValue = placeholdOptions.placeholdValue || $.trim($this.val());
                           $this.val(this.placeholdValue);                                        
                           $this.addClass(placeholdOptions.blurClass || "");
                        }    
                    )
                   .bind("focus",function(){
                       var $this = jQuery(this);
                       var val = $.trim($this.val());
                       if(
                          val == this.placeholdValue || 
                          val == ""
                       )
                       {
                           $this
                               .val("")
                               .removeClass(placeholdOptions.blurClass || "")
                               .addClass(placeholdOptions.focusClass || "");
                       }
                   })
                   .bind("blur",function(){
                       var $this = jQuery(this);
                       var val = $.trim($this.val());
                       if(
                          val == this.placeholdValue || 
                          val == ""
                       )
                       {
                           $this
                               .val(this.placeholdValue)
                               .addClass(placeholdOptions.blurClass || "")
                               .removeClass(placeholdOptions.focusClass || "")
                       }
                       
                           
                   });
               return $query;
                       
            }
        }
    )
})()    
