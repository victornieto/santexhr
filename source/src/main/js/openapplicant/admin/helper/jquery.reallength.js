/**
 * Uses 2 bytes for 0x0A characters for calculate a string length.
 */

(function($){
    $.fn.realLength = function() {
        if ($(this).length == 1) {
            return $(this).val().replace(/\n|\r/ig,'  ').length;
        } else {
            return null;
        }
    };

    $.fn.adjustToLength = function(maxLen) {
        if ($(this).length == 1) {
            var lineFeeds = $(this).val().match(/[^\n]*\n[^\n]*/gi).length;
            $(this).val($(this).val().substr(0,maxLen-lineFeeds));
        } else {
            return null;
        }
    }
})(jQuery);