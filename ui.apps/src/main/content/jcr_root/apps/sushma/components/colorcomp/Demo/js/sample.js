console.log("showing");
(function (document, $) {
    "use strict";

    $(document).on("foundation-contentloaded", function () {
        function showHide(component, element) {
            var target = $(element).data("cqDialogShowhideTarget");
            var $target = $(target);

            if (target) {
                $target.find('input, select, textarea, button').prop('disabled', !component.checked);
            }
        }

        $(document).on("change", ".cq-dialog-showhide", function () {
            var component = $(this).data("checkbox") || this;
            showHide(component, this);
        });
    });

})(document, Granite.$);