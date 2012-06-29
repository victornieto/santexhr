    jQuery.makeDraggable = function() {
		$('td[id^=q]').draggable({
			cursor: 'move',
			stop: handleDragStop,
			helper: 'clone',
			drag: handleDragDrag
		});
	};
	jQuery.makeOrder = function() {
		if($("#openapplicant_categories_list").find("tr").length >= 2) {
			$("#openapplicant_categories_list").tablesorter({
				widgets: ['zebra'], //alternating row styles
				textExtraction: "complex"
			});
		}
		if($("#openapplicant_questions_list").find("tr").length >= 2) {
			$("#openapplicant_questions_list").tablesorter({
				widgets: ['zebra'], //alternating row styles
				textExtraction: "complex"
			});
		}
	};

	function handleDragStop(event, ui) {
		var offsetXPos = parseInt( ui.absolutePosition.left );
		var offsetYPos = parseInt( ui.absolutePosition.top );
		jQuery("li[id^=cat_]").each(function() {
			if (offsetXPos >= jQuery(this).offset().left && offsetXPos <= jQuery(this).offset().left + jQuery(this).width() &&
					offsetYPos >= jQuery(this).offset().top && offsetYPos <= jQuery(this).height() + jQuery(this).offset().top) {
				var categoryId = jQuery(this).attr('id');
				var questionId = null;
				if (event.originalTarget.tagName == 'A') {
					questionId = event.target.parentNode.id;
				} else if (event.originalTarget.tagName == 'TD') {
					questionId = event.target.id;
				} else if (event.originalTarget.tagName == 'LI') {
					questionId = jQuery(ui.helper[0]).attr('id');
				} else {
					return;
				}
				CategoryController.moveQuestionToCategory(questionId.substr(1), categoryId.substr(4),
						function(response) {
							if (response == 0) {
								jQuery.get(location.href, function(data) {
									document.documentElement.innerHTML = data;
									jQuery.makeDraggable();		
									jQuery.makeOrder();
                                    jQuery.assingEvents();
									alert('Question moved successfully.');
								});
							} else {
								alert('There was something wrong with the operation. Please, contact the systems department.');
							}
						});
			}
		});
	}

	function handleDragDrag (event, ui) {
		var offsetXPos = parseInt( ui.absolutePosition.left );
		var offsetYPos = parseInt( ui.absolutePosition.top );
		jQuery("li[id^=cat_]").each(function() {
			if (offsetXPos >= jQuery(this).offset().left && offsetXPos <= jQuery(this).offset().left + jQuery(this).width() &&
					offsetYPos >= jQuery(this).offset().top && offsetYPos <= jQuery(this).height() + jQuery(this).offset().top) {
				jQuery(this).addClass('selected');
			} else if (jQuery(this).attr('id').substr(4) != categoryId) {
				jQuery(this).removeClass('selected');
			}
		});
	}

    jQuery.deleteCategory = function(categoryId) {
        $('<div></div>')
            .html('This category and all of its subcategories will be deleted. If there\'s any category configured in an Exam Definition, it will also be deleted.<br/>'
            + 'You can choose whether to delete all the category and its subcategories questions, or leave them without a category.')
            .dialog({
                resizable: false,
                height:150,
                width:525,
                modal: true,
                buttons: {
                    "Delete all": function() {
                        $.ajax({
                            type:'POST',
                            url: contextPath + '/admin/category/deleteCategory',
                            data:{
                                c:categoryId,
                                q:true},
                            success:function(html) {
                                document.documentElement.innerHTML = html;
                                jQuery.makeDraggable();
                                jQuery.makeOrder();
                                jQuery.assingEvents();
                            }
                        });
                        $( this ).dialog( "close" );
                    },
                    "Delete category(ies) only": function() {
                        $.ajax({
                            type:'POST',
                            url: contextPath + '/admin/category/deleteCategory',
                            data:{
                                c:categoryId,
                                q:false},
                            success:function(html) {
                                document.documentElement.innerHTML = html;
                                jQuery.makeDraggable();
                                jQuery.makeOrder();
                                jQuery.assingEvents();
                            }
                        });
                        $( this ).dialog( "close" );
                    },
                    Cancel: function() {
                        $( this ).dialog( "close" );
                    }
                }
            });
    };

    jQuery.assingEvents = function() {
        $('a[id^=delQ_]').each(function() {
            var artifactId = $(this).attr('id').substr(5);
            $(this).click(function() {
                $.ajax({
                    type:'POST',
                    url: contextPath + '/admin/category/deleteQuestion',
                    data:{q:artifactId},
                    success:function(html) {
                        document.documentElement.innerHTML = html;
                        jQuery.makeDraggable();
                        jQuery.makeOrder();
                        jQuery.assingEvents();
                    }
                });
            });
        });
        $('a[id^=delC_]').each(function() {
            var categoryId = $(this).attr('id').substr(5);
            $(this).click(function() {
                jQuery.deleteCategory(categoryId);
            });
        });
    };

    $(document).ready(function() {
        jQuery.makeDraggable();
        jQuery.makeOrder();
        jQuery.assingEvents();
    });