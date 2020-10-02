
function createHomePage() {

        $.post("/loadTable", {id : 0}, function(data) {
            // $('#result').text(Object.keys(data).length);

            $('#homePage').append($('<table id=' + 'tableIp>' + '</table>')
                .append($('<thead></thead>')
                    .append($('<tr></tr>')
                        .append($('<th>список</th>'))))
                .append($('<tbody></tbody>'))
            )
            $('#tableIp').find('thead').text("список");
            var tbody = $('#tableIp').find('tbody');

            // if ($(Object.keys(data.listRanges).length) != 0) {
            //     $.each(data.listRanges, function (i, item) {
            //         createRow(data.listRanges[i], tbody);
            //     })
            // }


            var arrIP = [];
            $.each(data.listRanges, function (i, item) {
                var temp = [];
                temp.push(data.listRanges[i].cidr);
                arrIP.push(temp);
                arrID.push(data.listRanges[i].inRange_id);
            })
            var table = $('#tableIp').DataTable({
                "pagingType": "full_numbers",
                "data" : arrIP,
                "columns" : [ {'title' : 'spisok'}]

            });
            $('#tableIp tbody').on( 'click', 'tr', function () {
                $(this).toggleClass('selected');
            } );

            $('#button').click( function () {
                alert(JSON.stringify(table.rows('.selected').data()) +' row(s) selected' );
            } );
        })
}

function addRows(list) {


}

function createRow(ip, tbody) {
    var tr = $('<th></th>');
    $('#result').text(ip.id);
    var row = $('<tr id=' +  '></tr>').append(tr);
    // var row = $('<tr id=' + ip.id + '><td></td></tr>');
    tr.text(ip.cidr);
    addRowListener(row, tbody, ip.id);
    tbody.append(row);

}


function addRowListener(row, tbody, id) {

    row.click(function () {
        $.post('/loadTable', {id: id}, function (data) {
            tbody.empty();
            // $('#result').text(JSON.stringify(data.listRanges));
            if ($(Object.keys(data.listRanges).length) != 0) {
                $.each(data.listRanges, function (i, item) {
                    createRow(data[i], tbody);
                })
            }

        })
    })
}