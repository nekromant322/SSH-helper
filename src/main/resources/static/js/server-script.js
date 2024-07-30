function loadServers() {
    $.ajax({
        url: '/server-panel/allServers',
        type: 'GET',
        dataType: 'json',
        success: function (servers) {
            let tbody = $('#allServers');
            tbody.empty();

            servers.forEach(function (server) {

                let serverRow = `
                        <tr>
                            <td>${server.id}</td>
                            <td>${server.name}</td>
                            <td>${server.ip}</td>
                            <td><button type="button" class="btn btn-info btn-edit1" data-id="${server.id}" data-toggle="modal" data-target="#ModalEditServer">Edit</button></td>
                            <td><button class="btn btn-danger btn-delete1" data-id="${server.id}" data-toggle="modal" data-target="#ModalDeleteServerCentral">Delete</button></td>
                        </tr>
                    `;
                tbody.append(serverRow);
            });

            $('.btn-edit1').click(function () {
                let serverId = $(this).data('id');
                $.ajax({
                    url: '/server-panel/server/' + serverId,
                    type: 'GET',
                    dataType: 'json',
                    success: function (server) {
                        console.log(server);
                        let form = $('#modalEditServerForm');
                        form.find('#ServerInputId').val(server.id);
                        form.find('#ServerInputName').val(server.name);
                        form.find('#ServerInputIp').val(server.ip);

                        $('#ModalEditServer').modal('show');
                    },
                    error: function (error) {
                        console.error("error of edit server:", error);
                    }
                });
            });

            $('.btn-delete1').click(function () {
                let serverId = $(this).data('id');
                $.ajax({
                    url: '/server-panel/server/' + serverId,
                    type: 'GET',
                    dataType: 'json',
                    success: function (server) {
                        $('#ServerIdDelete').val(server.id);
                        $('#ServerNameDelete').val(server.name);
                        $('#ServerIpDelete').val(server.ip);
                        $('#ModalDeleteServerCentral').modal('show');
                    }
                });
            });
        },
        error: function (error) {
            console.error("Error of getting servers:", error);
        }
    });
}

loadServers();

// форма добавления нового сервера
$('#addServer').click(function (event) {
    event.preventDefault();

    let server = {};

    $('#newServerForm').find('input').each(function () {
        let attr = $(this).attr('name');
        server[attr] = $(this).val();
    });

    $.ajax({
        url: "/server-panel",
        type: "POST",
        data: JSON.stringify(server),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function () {
            loadServers();
            window.location.href = '/server-panel';
        },
        error: function (xhr, status, error) {
            console.error('Error adding new server:', status, error);
        }
    });
});

// обработчик кнопки сохранения редактирования сервера
$('#saveEditServer').click(function (event) {
    let server = {};

    $('#modalEditServerForm').find('input').each(function () {
        let attr = $(this).attr('name');
        server[attr] = $(this).val();
    });

    $.ajax({
        url: "/server-panel/server/" + server.id,
        type: "PATCH",
        data: JSON.stringify(server),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function () {
            loadServers();
            $('#ModalEditServer').modal('hide');
        },
        error: function (xhr, status, error) {
            console.error('Error of editing server:', status, error);
        }
    });
});

// Обработчик кнопки подтверждения удаления сервера
$('#confirmDeleteServer').click(function () {
    let serverId = $('#ServerIdDelete').val();
    $.ajax({
        url: "/server-panel/server/" + serverId,
        type: "DELETE",
        success: function () {
            $('#ModalDeleteServerCentral').modal('hide');
            loadServers();
        },
        error: function (error) {
            console.error("Error of deleting server:", error);
        }
    });
});