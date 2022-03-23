AJS.toInit(function () {

    $(".lazyLoadingTabPage").change(function () {
        var lazyTab = true;
        triggerAdaptavistRestTables(lazyTab);
    });

    function triggerAdaptavistRestTables(lazyTab) {
        if (lazyTab) {
            $(".active-pane").find(".adaptavist-rest-table").each(function () {
                renderRestTable($(this));
            });
        } else {
            AJS.$(".adaptavist-rest-table").each(function () {
                renderRestTable($(this));
            });
        }
    }

    function renderRestTable(element) {
        AJS.$.ajaxSetup({cache: true});

        var restURL = element.attr('restURL');
        var values = JSON.parse(element.attr('values'));
        var columnArray = [];
        var table = jQuery(element);

        if (restURL.length > 0) {
            $.ajax({
                url: restURL,
                dataType: 'json',
                success: function () {
                    AJS.$.each(values, function (i) {
                        var value = values[i];
                        value.column = {};
                        value.column.header = value.header;
                        value.column.id = value.id;
                        columnArray.push(value.column);
                    });

                    new AJS.RestfulTable({
                        el: table,
                        allowEdit: false,
                        allowCreate: false,
                        allowDelete: false,
                        resources: {
                            all: restURL
                        },
                        columns: columnArray
                    });
                },
                error: function () {
                    AJS.messages.error(table, {
                        title: 'Error.',
                        body: '<p> Invalid URL or no JSON response</p>'
                    });
                }
            });
        } else {
            AJS.messages.error(table, {
                title: 'Error.',
                body: '<p> Please enter a valid URL</p>'
            });
        }
    }

    triggerAdaptavistRestTables();
});
