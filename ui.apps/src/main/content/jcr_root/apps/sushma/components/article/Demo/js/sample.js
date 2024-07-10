(function (document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", function (e) {
        // if there is already an initial value make sure the according target element becomes visible
        checkboxShowHideHandler($(".cq-dialog-checkbox-showhide", e.target));
    });

    $(document).on("change", ".cq-dialog-checkbox-showhide", function (e) {
        checkboxShowHideHandler($(this));
    });

    function checkboxShowHideHandler(el) {
        el.each(function (i, element) {
            if ($(element).is("coral-checkbox")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                var component = $(element).data("checkbox");
                if (component) {
                    showHide(component, element);
                }
            }
        });
    }

    function showHide(component, element) {
        console.log('showing');
        // get the selector to find the target elements. it's stored as a data- attribute
        var target = $(element).data("cqDialogCheckboxShowhideTarget");
        var $target = $(target);

        if (target) {
            if (component.checked) {
                $target.find('input, select, textarea, button').prop('disabled', false);
            } else {
                $target.find('input, select, textarea, button').prop('disabled', true).val(null);
                // Get the path of the component that opened the dialog
                var componentPath = Granite.author.DialogFrame.currentDialog.editable.path;
                console.log('Component Path:', componentPath);
                sendPathToServlet(componentPath);
            }
        }
    }

    function sendPathToServlet(path) {
        $.ajax({
            url: '/bin/yourServletPath/removeproprty',
            type: 'POST',
            data: {
                componentPath: path
            },
            success: function(response) {
                console.log('Path sent to servlet:', response);
            },
            error: function(xhr, status, error) {
                console.error('Error sending path to servlet:', error);
            }
        });
    }
})(document, Granite.$);