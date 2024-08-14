function loadUsers() {
    $.ajax({
        url: '/admin/allUsers',
        type: 'GET',
        dataType: 'json',
        success: function (users) {
            let tbody = $('#allUsers');
            tbody.empty();

            users.forEach(function (user) {
                let serversIP = user.servers.map(server => server.ip).join(', ');
                let userRow = `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>  
                            <td>${serversIP}</td>            
                            <td><button type="button" class="btn btn-info btn-edit" data-id="${user.id}" data-toggle="modal" data-target="#ModalEditUser">Edit</button></td>
                            <td><button class="btn btn-danger btn-delete" data-id="${user.id}" data-toggle="modal" data-target="#ModalDeleteUserCentral">Delete</button></td>
                        </tr>
                    `;
                tbody.append(userRow);
            });

            $('.btn-delete').click(function () {

                let userId = $(this).data('id');
                $.ajax({
                    url: '/admin/users/' + userId,
                    type: 'GET',
                    dataType: 'json',
                    success: function (user) {
                        let form = $('#ModalDeleteUserForm');
                        let serverSelect = form.find('#ModalServerDelete');
                        serverSelect.empty();
                        form.find('#ModalIdDelete').val(user.id);
                        form.find('#ModalFirstNameDelete').val(user.name);
                        user.servers.forEach(server => {
                            serverSelect.append(new Option(server.ip, server.ip));
                        });
                        $('#ModalDeleteUserCentral').modal('show');
                    }
                });
            });

            $('.btn-edit').click(function () {
                let userId = $(this).data('id');

                $.ajax({
                    url: '/admin/users/' + userId,
                    type: 'GET',
                    dataType: 'json',
                    success: function (user) {
                        console.log(user.servers);

                        let form = $('#modalEditUserForm');
                        form.find('#ModalInputId').val(user.id);
                        form.find('#ModalInputName').val(user.name);

                        let serverSelect = form.find('#ModalInputServer');
                        serverSelect.empty();
                        user.servers.forEach(server => {
                            let isSelectedServer = user.servers.some(userServer => userServer === server);
                            serverSelect.append(new Option(server.ip, server.ip, isSelectedServer, isSelectedServer));
                        });

                        $('#ModalEditUser').modal('show');
                    },
                    error: function (error) {
                        console.error("error of loading user:", error);
                    }
                });
            });
        },
        error: function (error) {
            console.error("Error of getting users:", error);
        }
    });
}

loadUsers();

// обработчик кнопки сохранения редактирования пользователя
$('#saveEditUser').click(function (event) {
    let user = {};
    let form = $('#modalEditUserForm');
    form.find('input').each(function () {
        let attr = $(this).attr('name');
        user[attr] = $(this).val();
    });

    user['servers'] = form.find('#ModalInputServer').val().map(ip => ({ip}));

    $.ajax({
        url: "/admin/users/" + user.id,
        type: "PATCH",
        data: JSON.stringify(user),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function () {
            loadUsers();
            $('#ModalEditUser').modal('hide');
        },
        error: function (xhr, status, error) {
            console.error('Error of adding new user:', status, error);
        }
    });
});

// Обработчик кнопки подтверждения удаления пользователя
$('#confirmDeleteUser').click(function () {
    let userId = $('#ModalIdDelete').val();
    $.ajax({
        url: '/admin/users/' + userId,
        type: 'DELETE',
        success: function () {
            $('#ModalDeleteUserCentral').modal('hide');
            loadUsers();
        },
        error: function (error) {
            console.error("Error of deleting user:", error);
        }
    });
});