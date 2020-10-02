
function loadReport() {

    $.ajax({
    url: '/loadReport',
    type: 'POST',
    statusCode: {
        200: function (data) {
            $('<div id="reportPage"></div>').appendTo(document.body);
            $('#ValidationPage').remove();
            $('#loadFilePage').remove();
            createReportPage(data);
        },
        400: function (data) {
            $('<div id="reportPage"></div>').appendTo(document.body);
            $('#ValidationPage').remove();
            $('#loadFilePage').remove();
            createBadRequestPage(data);
        }
    }
})
}

function createBadRequestPage(data) {
    data = JSON.parse(data.responseText);
    var id = Object.keys(data)[0];

    var divPageError = $('<div id="divErrorPage"></div>');
    var page = $('#reportPage');
    var buttonBackToLoadPage = $('<button id="buttonBack3">вернуться на страницу загрузки</button>');
    buttonBackToLoadPage.click(function () {
        $('#reportPage').remove();
        createLoadFilePage();
    });

    var status = $('<p></p>');
    status.html('Во время выполнения валидационной команды произошла ошибка.<br />'+
        'Поправьте скрипт под номером ' + id + ' чтобы продолжить.');

    var pre = $('<pre id="commandWithError">' +'</pre>');
    var buttonShowCommand = $('<button id="showScript">нажмите чтобы посмотреть скрипт</button>');
    buttonShowCommand.click(function () {
        buttonShowCommand.remove();
        pre.html(data[id]);
        pre.toggle();
    });
    pre.toggle();
    page.append(divPageError.append(status).append(pre).append(buttonShowCommand).append(buttonBackToLoadPage));
    // pre.toggle()
}

function createReportPage(data) {

    var reportPage = $('#reportPage');
    var divRightReportPage = $('<div id="divRightReportPage"></div>');
    var divLeftReportPage = $('<div id="divLeftReportPage"></div>');
    var divLeftReportPageReport = $('<div id="divLeftReportPageReport"></div>');
    var buttonBack = $('<button id="buttonBack" onclick="clickButtonBack()">вернуться назад</button>');
    var divReportPageCenter = ($('<div id="divReportPageCenter"></div>'));
    var divButtonBack = ($('<div id="divButtonBack"></div>'));


    createReportPageLeftPart(data, divLeftReportPageReport, divLeftReportPage, reportPage);
    createReportPageRightPart(divRightReportPage);
    reportPage.append(divButtonBack.append(buttonBack))
        .append(divReportPageCenter.append(divLeftReportPage.prepend(divLeftReportPageReport)).append(divRightReportPage))
    }

function createReportPageLeftPart(data , divLeftReportPageReport, divLeftReportPage) {

    var countIncorrectLines = data['countIncorrectLines'];
    var countValidationErrorLines = data['countValidationErrorLines'];
    var countRouter = data['countRouter'];
    var countIpRouter = data['countIpRouter'];
    var countIpRange = data['countIpRange'];
    var countInvalidRouter = data['countInvalidRouter'];
    var countInvalidIpRouter = data['countInvalidIpRouter'];
    var countInvalidIpRange = data['countInvalidIpRange'];
    var tableReport = $('<table id="reportTable"></table>')
        .append($('<thead></thead>')
            .append($('<tr></tr>')
                .append('<th>Отчёт о загруженных данных</th>')))
        .append('<tbody></tbody>');

    divLeftReportPage.append(tableReport);
    var tbody = tableReport.find('tbody');
    tbody.append('<tr><td>обнаружено  роутеров: '+ countRouter + '</td></tr>')
         .append('<tr><td>обнаружено IP-адресов: ' + countIpRouter + '</td></tr>')
         .append('<tr><td>обнаружено Ip-диапозонов: ' + countIpRange + '</td></tr>');

    if (countValidationErrorLines === 0 && countIncorrectLines === 0) {
        tbody.append('<tr><td>все данные успешно прошли валидацию и готовы к загрузке в систему</td></tr>');
    }
    else {
        if (countIncorrectLines > 0 ) {
            tbody.append('<tr><td>строк не прошедших индефикацию: '+ countIncorrectLines + '</td></tr>');
        }
        if (countValidationErrorLines > 0) {
            tbody.append('<tr><td>роутеров не прошедших валидацию: ' + countInvalidRouter + '</td></tr>')
                 .append('<tr><td>Ip-адреса не прошедших валидацию: ' + countInvalidIpRouter + '</td></tr>')
                 .append('<tr><td>Ip-подсетей не прошедших валидацию: ' + countInvalidIpRange + '</td></tr>');
        }
        var buttonValidationTable = $('<button id="lookError">показать отчёт о ошибках</button>');
        divLeftReportPage.append(buttonValidationTable);

        buttonValidationTable.click(function () {
            $.post('/loadValidationTable', function (data2) {
                buttonValidationTable.parent().prepend(createValidationTable(data2));
                $('#validationTable').DataTable({'language' : dataTablesLanguage});
                buttonValidationTable.remove();
            })
        });
    }
}

function createReportPageRightPart(divRightReportPage) {
    var buttonLoadToSystem = $('<button id="buttonLoadToSystem">загрузить данные в систему </button>');
    divRightReportPage.append(buttonLoadToSystem);
    buttonLoadToSystem.click(function () {

        $.post('/saveData', function () {
            divDataTablePage();
            $('#reportPage').remove();
            buttonLoadToSystem.remove();
        });
    })
}

function createValidationTable(data) {

    var table = $('<table id="validationTable"></table>')
        .append($('<thead></thead>')
            .append($('<tr></tr>')
                .append('<th>Имя таблицы</th>')
                .append('<th>Объект</th>')
                .append('<th>Описание ошибки</th>')))
        .append($('<tbody></tbody>'));
    var tbody = table.find('tbody');
    $.each(data, function (i,item) {

        tbody.append($('<tr></tr>').append('<td>'+ data[i].table_name +'</td>')
            .append('<td>'+ data[i].incorrect_value +'</td>')
            .append('<td>'+ data[i].text +'</td>'));
    });
    return table;
}
