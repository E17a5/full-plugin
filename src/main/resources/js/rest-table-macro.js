AJS.bind("init.rte", function () {

    //Model for a Rest Table
    function RestTable(params) {

        this.getRestURL = function () {
            return params === undefined ? '' : params.restURL;
        };


        this.getValues = function () {
            if (params === undefined) {
                return [{
                    label: '',
                    value: ''
                }];
            } else {
                var counter = Number(params['counter']);
                var values = [];

                //Unfortunately, Confluence cannot parse a JSON string, so one needs to write each parameter individually
                for (var i = 0; i < counter; i++) {
                    values.push({
                        label: params[i.toString() + '_label'] === undefined ? "" : params[i.toString() + '_label'],
                        value: params[i.toString() + '_value'] === undefined ? "" : params[i.toString() + '_value']
                    });
                }
                return values;
            }
        };
    }

    //Fetches all Label/Values from each row, plus if it's selected.
    function getRowValues(values) {
        var counter = 0;

        //Unfortunately, Confluence cannot parse a JSON string, so one needs to write each parameter individually
        AJS.$('[id^="groupRow"]').each(function () {
            var label = $(this).find("input[name='buttonLabel']").val();
            var value = $(this).find("input[name='buttonValue']").val();

            values[counter + '_label'] = label;
            values[counter + '_value'] = value;

            counter++;
        });

        values['counter'] = counter;

        return values;
    }

    function getRestTableParams() {
        var values = {
            restURL: AJS.$('#restURL').val()
        };

        return getRowValues(values);
    }

    function setupEvents() {
        // Setups Tabs in AJS
        AJS.tabs.setup();

        //Hooks up the Cancel Button
        AJS.$('#cancelButtonGroup').click(function (e) {
            e.preventDefault();
            AJS.dialog2('#buttongroup-dialog').remove();
        });

        //Hooks up the Insert Button
        // The hidden button is required to submit the form because the dialog submit button is outside the form element.
        AJS.$('#insertButtonGroup').click(function () {
            AJS.$('#buttonGroupForm').submit();
        });
    }

    function createRestTable(macro) {

        //Creates a new Rest Table object that holds the parameters
        var restTable = new RestTable(macro.params);

        var dialogAndButtonTitle = macro.body === undefined ?
            AJS.I18n.getText('be.edpb.table.buttongroup.insert') :
            AJS.I18n.getText('com.adaptavist.confluence.contentFormattingMacros.buttongroup.save');
        //Calls the dialog with Edit or Insert accordingly.
        AJS.dialog2(ContentFormatting.Macro.RestTable.dialog({
            title: dialogAndButtonTitle + ' ' + AJS.I18n.getText('com.adaptavist.confluence.contentFormattingMacros.rest-table.dialog.title'),
            buttonTitle: dialogAndButtonTitle,
            restURL: restTable.getRestURL(),
            values: restTable.getValues()
        })).show();

        AJS.$('#insertButtonGroup').on('click', function (event) {
            AJS.$('#hiddenButton').click();
        });

        AJS.$('#buttonGroupForm').on('submit', function (e) {
            e.preventDefault();

            var t = tinymce.confluence.macrobrowser;

            AJS.Rte.BookmarkManager.restoreBookmark();

            if (t.editedMacroDiv) {
                delete t.editedMacroDiv;
            }

            //Saves the Macro. Since the values is an array, it needs to convert it to JSON and then parse it back to display.
            tinymce.confluence.macrobrowser.macroBrowserComplete({
                name: "rest-table",
                "bodyHtml": undefined,
                "params": getRestTableParams(),
                "values": restTable.getValues()
            });

            AJS.dialog2('#buttongroup-dialog').remove();
        });

        setupEvents();
    }

    AJS.MacroBrowser.setMacroJsOverride('rest-table', {opener: createRestTable});
});

//Adds a new row Macro
function addRestTableRow(rowIndex) {
    AJS.$('#dataTable > tbody > tr#groupRow' + rowIndex).after(ContentFormatting.Macro.RestTable.addRestTableRow({
        rowIndex: AJS.$('[id^="groupRow"]').length,
        label: '',
        value: ''
    }));

    AJS.$('[id^="removeButton-"]')[0].disabled = false;
}
//Removes a row from Macro
function removeRestTableGroupRow(rowIndex) {
    AJS.$('#groupRow' + rowIndex).remove();

    if (AJS.$('[id^="removeButton-"]').length == 1) {
        AJS.$('[id^="removeButton-"]')[0].disabled = true;
    }
}
