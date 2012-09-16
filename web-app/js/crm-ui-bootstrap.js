/*
 *  Copyright 2012 Goran Ehrsson.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

if (jQuery)( function() {
    $.extend($.fn, {

        masterDetail: function(cfg) {
            var addURL = cfg.url;
            if (!addURL) {
                addURL = "addRow";
            }
            var callback = cfg.callback;
            $(this).each(function() {
                var el = $(this);
                $("tr:last td:first a", el).click(function(event) {
                    event.stopPropagation();
                    var tr = $('tbody tr:last', el);
                    $.get(addURL, function(data) {
                        var newTR = $(data);
                        if (tr.length > 0) {
                            tr.after(newTR);
                        } else {
                            $('tbody', el).append(newTR);
                        }
                        // Animate background color to green and put cursor in first input field.
                        $('td', newTR).animate({
                            backgroundColor: "#aaf201"
                        }, 600).delay(300).animate({
                                backgroundColor: "#fff"
                            }, 400);
                        renumberTableInput(el);
                        if (callback != null) {
                            callback(newTR);
                        }
                        $('input:visible:first', newTR).focus();
                    });
                    return false;
                });

                // Add one default row if table body is empty.
                if (cfg.addDefaultRow == true && $('tbody tr', el).length == 0) {
                    $("tr:last td:first a", el).click();
                }
            });
        },

        renumberInputNames: function() {
            renumberTableInput($(this));
        },

        /* Charles Lawrence - Feb 16, 2012. Free to use and modify. Please attribute back to @geuis if you find this useful
         * Twitter Bootstrap Typeahead doesn't support remote data querying. This is an expected feature in the future. In the meantime, others have submitted patches to the core bootstrap component that allow it.
         * The following will allow remote autocompletes *without* modifying any officially released core code.
         * If others find ways to improve this, please share.
         */
        crmAutocomplete: function(url, params, callback) {
            $(this).typeahead().on('keyup', function(ev) {

                ev.stopPropagation();
                ev.preventDefault();

                if(!callback) {
                    callback = params;
                    params = null;
                }

                //filter out up/down, tab, enter, and escape keys
                if ($.inArray(ev.keyCode, [40,38,9,13,27]) === -1) {

                    var self = $(this);

                    //set typeahead source to empty
                    self.data('typeahead').source = [];
                    self.data('typeahead').values = [];

                    //active used so we aren't triggering duplicate keyup events
                    if (!self.data('active') && self.val().length > 0) {

                        self.data('active', true);

                        var getParams = params;
                        if(! getParams) {
                            getParams = {};
                        }
                        getParams.q = $(this).val();

                        //Do data request. Insert your own API logic here.
                        $.getJSON(url, getParams, function(data) {
                            //set this to true when your callback executes
                            self.data('active', true);

                            var result = callback(data);

                            //set your results into the typehead's source
                            self.data('typeahead').source = result.labels;
                            self.data('typeahead').values = result.values;

                            //trigger keyup on the typeahead to make it search
                            self.trigger('keyup');

                            //All done, set to false to prepare for the next remote query.
                            self.data('active', false);
                        });
                    }
                }
            });
        }
    });
})(jQuery);


function deleteTableRow(elem) {
    var tr = $(elem).closest('tr');
    var table = tr.closest('table');
    tr.remove();
    renumberTableInput(table);
    return false;
}

function renumberTableInput(table) {
    var lastIndex = -1;
    var rx = new RegExp(/\[[0-9]+\](.+)/);
    $('tbody tr', table).each(function(row) {
        $(':input', $(this)).each(function(col) {
            var input = $(this);
            var name = input.attr('name')
            if(name) {
                name = name.replace(rx, "[" + row + "]\$1");
                input.attr('id', name);
                input.attr('name', name);
            }
            lastIndex = row;
        });
    });
    return lastIndex;
}

var pageIsDirty = false;
var pageIsDirtyMessage = 'Du har inte sparat dina ändringar, är du säker på att du vill lämna sidan utan att spara?'; // TODO i18n

function okToClose(arg) {
    pageIsDirty = !arg;
    return arg;
}

function locationHashChanged() {
    var tab = decodeURIComponent(document.location.hash);
    if (tab) {
        $("a[href='" + tab + "'][data-toggle='tab']").tab('show');
    }
}

function fixPlaceholderText() {
    $('[placeholder]:not(:password)').focus(
        function() {
            var input = $(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
                input.removeClass('placeholder');
            }
        }).blur(
        function() {
            var input = $(this);
            if (input.val() == '' || input.val() == input.attr('placeholder')) {
                input.addClass('placeholder');
                input.val(input.attr('placeholder'));
            }
        }).blur();
    $('[placeholder]').parents('form').submit(function() {
        $(this).find('[placeholder]').each(function() {
            var input = $(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
            }
        })
    });
}

jQuery(document).ready(function() {
    $("#content :input").change(function() {
        if (!pageIsDirty) {
            pageIsDirty = true;
        }
    });
    window.onbeforeunload = function() {
        if (pageIsDirty) {
            return pageIsDirtyMessage;
        }
    };
    $(".btn,.btn-primary,button.dismiss,a.dismiss,input.dismiss").click(function(event) {
        return okToClose(true);
    });

    // Listen for location hash changes and activate specified tab.
    if ("onhashchange" in window) {
        window.onhashchange = locationHashChanged;
    }

    // Activate tab specified in location hash
    locationHashChanged();
    $('a[data-toggle="tab"]').on('shown', function (e) {
        // Change location hash to the selected tab
        document.location.hash = $(e.target).attr("href");
    });

    if (!Modernizr.input.placeholder) {
        fixPlaceholderText();
    }

    $("#global-message div").each(function() {
        var div = $(this);
        var messageText = $.trim(div.text());
        if(div.hasClass('alert-info')) Notifier.info(messageText);
        if(div.hasClass('alert-success')) Notifier.success(messageText);
        if(div.hasClass('alert-warning')) Notifier.warning(messageText);
        if(div.hasClass('alert-error')) Notifier.error(messageText);
    });

    // Slide down tag panel when mouse is over.
    $("#tags").hoverIntent(function(event) {
      $("form", $(this)).slideDown('fast', function() {
        $("input:first", $(this)).focus();
      });
    }, function(event) {
      $("form", $(this)).slideUp('fast');
    });
});
