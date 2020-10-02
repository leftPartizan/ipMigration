
function divDataTablePage() {
    $('<div id="dataTablePage"></div>').appendTo(document.body);
    createDataTablePage();
}


function createDataTablePage() {
    $.ajax({
        url: '/loadData',
        type: 'POST',
        data: JSON.stringify({"id": null}),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        statusCode: {
            200: function (data) {
                var divDataTablePageCenter = ($('<div id="divDataTablePageCenter"></div>'));
                var divButtonBack = ($('<div id="divButtonBack"></div>'));
                var buttonBack = ($('<button onclick="clickButtonBack()" id="buttonBack2">вернуться на страницу загрузки файла</button>'));
                var buttonClearObjTable = ($('<button onclick="clearObjTable()" id="clearData">очистить базы данных</button>'));

                $('#dataTablePage').append(divButtonBack.append(buttonBack))
                    .append(divDataTablePageCenter.append($('<table id=' + 'tableIp>' + '</table>')
                    .append($('<thead></thead>')
                        .append($('<tr></tr>')
                            .append($('<th>список айпи диапозонов</th>'))))
                    .append($('<tbody></tbody>')))
                );
                createButtonBackToNet();
                addRows(data);
                addRowListener();
                divDataTablePageCenter.append(buttonClearObjTable);
                $('#tableIp').DataTable({'language' : dataTablesLanguage});
            }
        }
    })
}

function addRowListener() {

    var tbody = $('#tableIp tbody');
    tbody.find('tr.range').each(function () {
        $(this).click(function () {
            changeButtonBack(this, 'ip');
            clickOpenList(this.id, $(this).find('td:first').text(), 'ip')
        })
    });

    tbody.find('tr.ip').each(function () {
        $(this).click(function () {
            changeButtonBack(this, 'router');
            clickOpenList(this.id, $(this).find('td:first').text(), 'router')
        })
    })
}

function clickOpenList(id, name, type) {
    var table = $('#tableIp');
    table.DataTable().clear().destroy();
    var url;
    if (type === 'ip') { url = '/loadData'}
    if (type === 'router') { url = '/getObjectByReference'}
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify({"id": id}),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        statusCode: {
            200: function (data) {
                if (type === 'router') {
                    createTableListRouters(data, name, table)
                }
                else {
                    createTableListIps(data,name, table)
                }
            }
        }
    })
}

function changeButtonBack(row, type) {
    var button = $('#backInNet');
    var history = button.data('history');
    button.disabled = false;
    history.push({'id': row.id, 'range' : $(row).find('td:first').text()});
}

function addRows(data) {
    var tbody = $('#tableIp tbody');
    $.each(data.listObject, function (i, item) {
        var typeRow;
        if (data.listObject[i].object_type_id === 1) {
            typeRow = 'ip';
        }
        if (data.listObject[i].object_type_id === 2) {
            typeRow = 'range';
        }
        if (data.listObject[i].object_type_id === 3) {
            typeRow = 'router';
        }
        tbody.append($('<tr id=' + data.listObject[i].id + ' class=' + typeRow + '></tr>')
            .append($('<td>' + data.listObject[i].name + '</td>')));
    });
    if (tbody.children().length ===0) {
        tbody.append('<tr><td>список пуст</td>');
    }
}

function createButtonBackToNet() {
    $('#divDataTablePageCenter').append($('<button id="backInNet">назад</button>'));
    var button = $('#backInNet');
    button.data('history', [{'id' : null, 'range' : ''}]);

    // нажатие на клик
    button.click(function () {
        var arr = button.data('history');
        if (arr.length === 1) {
            button.disabled = true;
        }
        else {
            arr.pop();
            var id = arr[arr.length - 1].id;
            var range = arr[arr.length - 1].range;
            clickOpenList(id, range, 'ip');
        }
    })
}

function createTableListIps(data, cidr, table) {

    addRows(data);
    var str;
    if (data.listObject.length != 0){
        if(data.listObject[0].parent_id === null) { str = 'список айпи диапозонов '}
        else { str = 'список адресов сети '}
    }
    else { str = 'список адресов сети '}
    table.find('thead:first').find('tr:first').find('th:first').text(str + cidr);
    addRowListener();
    table.DataTable({'language' : dataTablesLanguage});
}

function createTableListRouters(data, ip, table) {
    addRows(data);
    table.find('thead:first').find('tr:first').find('th:first').text('список роутеров с Ip-адресом ' + ip);
    addRowListener();
    table.DataTable({'language' : dataTablesLanguage});
}

function clickButtonBack() {
    $('body').empty();
    createLoadFilePage();
}
function clearObjTable() {
    $.post('/clearObjectTable', function () {
        clickButtonBack();
    })
}

dataTablesLanguage = {
    "decimal":        "",
    "emptyTable":     "",
    "info":           "",
    "infoEmpty":      "",
    "infoFiltered":   "",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Показать _MENU_ записей",
    "loadingRecords": "Loading...",
    "processing":     "Processing...",
    "search":         "Поиск:",
    "zeroRecords":    "список пуст",
    "paginate": {
        "first":      "First",
        "last":       "Last",
        "next":       "вперёд",
        "previous":   "назад"
    },
    "aria": {
        "sortAscending":  ": activate to sort column ascending",
        "sortDescending": ": activate to sort column descending"
    }
};