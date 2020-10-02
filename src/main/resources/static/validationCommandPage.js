function validationCommandPage() {

    createValidationCommandsPage();
}

function createValidationCommandsPage() {

    var table = $('<table id="validations"></table>')
        .append($('<thead></thead>')
            .append($('<tr></tr>')
                .append('<th>Id</th>')
                .append('<th>Команда</th>')
                .append('<th>Описание</th>')))
        .append($('<tbody></tbody>'));


    var buttonBack = $('<button id="buttonBackFromCommandsPage" onclick="clickButtonBack()">вернуться назад</button>');
    var buttonUp = $('<button id="buttonUp">вверх</button>');
    var buttonDown = $('<button id="buttonDown">вниз</button>');
    var buttonDeleteSelectedRow = $('<button id="buttonDeleteSelectedRow">удалить выбранную строку</button>');
    var buttonNewRow = $('<button id="newRow">добавить новую команду</button>');
    var divRightValidationPage = $('<div id="divRightValidationPage"></div>');
    var divLeftValidationPage = $('<div id="divLeftValidationPage"></div>');
    var divValidationPage = $('<div id="ValidationPage"></div>');
    var buttonSaveChange = $('<button id="buttonSaveChange">сохранить изменения</button>');
    var buttonLoadDefaultCommands = $('<button id="buttonLoadDefaultCommands">очистить таблицу и загрузить дефолтные команды </button>');
    var divButtonBack = ($('<div id="divButtonBack1"></div>'));
    var divCommandsPageCenter = ($('<div id="divCommandsPageCenter"></div>'));
    var divButtonsDeleteAdd = ($('<div id="divButtonsDeleteAdd"></div>'));
    divButtonBack.append(buttonBack);
    divValidationPage.append(divButtonBack);
    divValidationPage.append(
        divCommandsPageCenter.append(
            divLeftValidationPage.append(table).append(divButtonsDeleteAdd
                .append(buttonNewRow).append(buttonDeleteSelectedRow).append(buttonSaveChange))
                )).append(buttonLoadDefaultCommands);
    divCommandsPageCenter.append(divRightValidationPage.append(buttonUp).append(buttonDown));
    divValidationPage.appendTo(document.body);

    $.ajax({
        url: '/getCommands',
        type: 'POST',
        statusCode: {
            202: function (data,textStatus) {
                getCommandsStatus(data,table);
                eventsTableValidation(table, buttonNewRow, buttonDeleteSelectedRow,
                    buttonUp, buttonDown, buttonSaveChange);
                clickButtonLoadDefaultCommands(buttonLoadDefaultCommands, table);
            },
            303: function (data, textStatus) {
                getCommandsStatus(JSON.parse(data.responseText), table);
                eventsTableValidation(table, buttonNewRow, buttonDeleteSelectedRow,
                    buttonUp, buttonDown, buttonSaveChange);
                clickButtonLoadDefaultCommands(buttonLoadDefaultCommands, table);
            }
        }
    });
}


function getCommandsStatus(data, table) {
    var tbody = table.find('tbody:first');
    var command = data;
    $.each(command, function (i, item) {
        tbody.append($('<tr></tr>')
            .append('<td>' + item.id + '</td>')
            .append($('<td></td>').append('<pre spellcheck="false" contenteditable="true">' + item.command + '</pre>'))
            .append('<td spellcheck="false" contenteditable="true">' + item.type + '</td>'))
    });
}

function eventsTableValidation(table, buttonNewRow, buttonDeleteSelectedRow, buttonUp, buttonDown, buttonSaveChange ) {

    var tbody = $('#ValidationPage tbody');
    $('#ValidationPage tr').each(function () {
        addListenersForTableRowsCommands(this, tbody);
        addListenersForTableRowsPreCommands($(this).find('pre:first'));
        addListenersForTableRowsType($(this).find('td:last'))
    });

    buttonNewRow.click(function () {
        var id;
        if (tbody.children().length === 0) {
            id = 0;
        }
        else {
            id = parseInt(tbody.find('tr:last').find('td:first-child').text(), 10) + 1;
        }
        var tr = $('<tr></tr>');
        tbody.append(tr
            .append('<td class="id">' + id + '</td>')
            .append($('<td class="command"></td>')
                .append('<pre contenteditable="true" class="empty">' + 'строка не должна быть пустой' +  '</pre>'))
            .append($('<td contenteditable="true"></td>')));
        addListenersForTableRowsCommands(tr, tbody);
        addListenersForTableRowsPreCommands(tr.find('pre:first'));
        addListenersForTableRowsType(tr.find('td:last'));
    });

    buttonDeleteSelectedRow.click(function () {
        tbody.find('tr.selected').remove();
    });

    buttonUp.click(function () {
        if (tbody.find('tr.selected').is('tbody tr:first-child') == false) {
            clickUpDownButton(tbody.find('tr.selected'), tbody.find('tr.selected').prev())
        }
    });

    buttonDown.click(function () {
        if (tbody.find('tr.selected').is('tbody tr:last-child') == false) {
            clickUpDownButton(tbody.find('tr.selected'), tbody.find('tr.selected').next())
        }
    });

    buttonSaveChange.click(function () {

        if (document.getElementsByClassName('empty').length !== 0)
        {
            alert('содержимое в колонке команда не должно быть пустым \n')
        }
        else {
            var commands = [];

            $('#validations tbody').find('tr').each(function () {
                var td = $(this).find('td:first');
                var td2 = td.next();
                var td3 = td2.next();
                commands.push( {'id' : td.text(),
                    'command' : td2.find('pre:first').text(),
                    'type' : td3.text()
                });
            });

            if (commands.length != 0){
                $.ajax({
                    url: '/saveCommands',
                    type: 'POST',
                    data: JSON.stringify(commands),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    statusCode: {
                        200: function () {
                            $('#ValidationPage').remove();
                            createLoadFilePage();
                        }
                    }
                });
            }
            else {
                $('#ValidationPage').remove();
                createLoadFilePage();
            }
        }
    })


}

function addListenersForTableRowsCommands(row, tbody) {
    $(row).click(function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }

        else {
            if ($(this).find('pre:first').hasClass('edit') === false
            && $(this).find('td:last').hasClass('edit') === false) {
                tbody.find('tr.selected').removeClass('selected');
                tbody.find('pre.edit').removeClass('selected edit');
                $(this).addClass('selected');
                $(this).find('pre:first').addClass('selected');
                $(this).find('td:last').addClass('selected');
            }
        }
    });
}

function addListenersForTableRowsPreCommands(pre) {
    $(pre).blur(function () {
        if ($(this).text().length === 0) {
            $(this).addClass('empty');
            $(this).html('строка не должна быть пустой')
        }
        else { $(this).removeClass('empty')}
        if ($(this).hasClass('edit')) { $(this).removeClass('edit')}
        if ($(this).hasClass('selected')) { $(this).removeClass('selected')}
    });

    $(pre).click(function () {
            if ($(this).hasClass('selected')) {
                $(this).addClass('edit');
            }
            if ($(this).hasClass('empty')) { $(this).html('')}
        }
    )
}
function addListenersForTableRowsType(td) {
    $(td).blur(function () {
        if ($(this).hasClass('edit')) { $(this).removeClass('edit')}
        if ($(this).hasClass('selected')) { $(this).removeClass('selected')}
    });

    $(td).click(function () {
            if ($(this).hasClass('selected')) {
                $(this).addClass('edit');
            }
        }
    )
}

function clickButtonLoadDefaultCommands(button, table) {
    button.click(function () {
        $.ajax({
            url: '/getDefaultCommands',
            type: 'POST',
            statusCode: {
                200: function (data, textStatus) {
                    var tbody = table.find('tbody');
                    tbody.empty();
                    getCommandsStatus(data, table);
                    $('#ValidationPage tr').each(function () {
                        addListenersForTableRowsCommands(this, tbody);
                        addListenersForTableRowsPreCommands($(this).find('pre:first'));
                        addListenersForTableRowsType($(this).find('td:last'))
                    });
                }
            }
        });
    })
}

function clickUpDownButton(selectedRow, prevRow ) {
    var selectedRowCommands = selectedRow.find('pre:first-child');
    var previousRowCommands = prevRow.find('pre:first-child');
    var selectedRowTypeCommands = selectedRow.find('td:last');
    var previousRowTypeCommands = prevRow.find('td:last');
    var commandSelected = selectedRowCommands.text();
    var commandTypeSelected = selectedRowTypeCommands.text();
    selectedRowCommands.text(previousRowCommands.text());
    selectedRowTypeCommands.text(previousRowTypeCommands.text());
    previousRowCommands.text(commandSelected);
    previousRowTypeCommands.text(commandTypeSelected);
    prevRow.addClass('selected');
    selectedRow.removeClass('selected');
}