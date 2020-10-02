$(document).ready(function(){ $.post('/clearCommandsTable'); createLoadFilePage()});

function createLoadFilePage() {
    var str = '<div id="loadFilePage">\n' +
        '    <h3>Миграция Ip-адресов</h3>\n' +
        '    <p> Загрузите файл с Ip-адресами, чтобы подготовить к переносу данных из системы<br/>\n' +
        '            Вы можете посмотреть какие валидационные команды будут выполнены, <br/>\n' +
        '            а также добавлять,редактировать и менять их порядок выполнения\n' +
        '    </p>\n' +
        '    <input id="loadFile" type="file" accept=".txt"/>\n' +
        '    <h1 id="status"></h1>\n' +
        '    <button id="loadCommandPage">посмотреть валидационные команды</button>\n' +
        '</div>';

    $(str).appendTo(document.body);
    $('#loadCommandPage').click(function () {
        $('#loadFilePage').remove();
        validationCommandPage();
    });

    $('#loadFile').change(function () {
        var data = new FormData();
        var parts = $('#loadFile')[0].value.split('.');
        var typeFile = parts[parts.length - 1];
        if (typeFile === 'txt') {
            data.append('file', $('#loadFile')[0].files[0]);
            $.ajax({
                url: '/load',
                type: 'POST',
                enctype: 'multipart/form-data',
                data: data,
                processData: false,
                contentType: false,
                cache: false,
                beforeSend: function () {
                    $('#loadFilePage #status').text('Идёт загрузка файла. Ждите')
                },
                statusCode: {
                    202: function () {
                        loadReport();
                        $('#loadFilePage').remove();
                        $('#ValidationPage').remove();
                    }
                },
                error: function (jqXHR, status, e) {
                    if (status === "timeout") {
                        alert("Время ожидания ответа истекло!");
                        document.getElementById("fileInput").value = "";
                    } else {
                        alert(status); // Другая ошибка
                        document.getElementById("fileInput").value = "";
                    }
                }
            })
        } else {
            alert(' файла должен быть с расширением txt');
            document.getElementById("fileInput").value = "";
        }
    })
}