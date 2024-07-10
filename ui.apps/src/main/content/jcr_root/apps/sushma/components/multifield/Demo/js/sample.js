console.log('hi');
(function($, document) {
    "use strict";

    $(document).on("foundation-contentloaded", function() {
        // Select the multifield
        var multifield = $(".coral-Form-fieldwrapper[data-init='multifield']").first();

        // Count the number of existing items
        var itemCount = multifield.find(".coral-Multifield-input").length;

        // Limit the number of items to 5
        var maxItems = 5;

        // Disable the "Add" button when the maximum number of items is reached
        if (itemCount >= maxItems) {
            multifield.find(".coral-Multifield-add").prop("disabled", true);
        }

        // Listen for the "coral-collection:add" event
        multifield.on("coral-collection:add", function(event) {
            // Increment the item count
            itemCount++;

            // Check if the maximum number of items has been reached
            if (itemCount >= maxItems) {
                // Disable the "Add" button
                multifield.find(".coral-Multifield-add").prop("disabled", true);

                // Display an alert
                alert("You have reached the maximum limit of " + maxItems + " items.");
            }
        });
    });
})(jQuery, document);
