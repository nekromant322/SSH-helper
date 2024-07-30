$(document).ready(function() {
    $.ajax({
        url: '/user/me',
        method: 'GET',
        dataType: 'json',
        success: function(user) {
            userNavigationPanel(user)
            let roles = user.roles.map(role => role.role).join(', ');
            // let servers = user.servers.map(server => server.ip).join('\n');
            let userRow = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
       <!--         <td>${servers}</td>-->
                    <td>${roles}</td>
                </tr>
            `;
            $('#currentUser').html(userRow);
        },
        error: function(error) {
            console.error('Error of loading user:', error);
        }
    });
});

function userNavigationPanel(user) {
    let name = `<strong>${user.name}</strong>`;
    let roles = user.roles.map(role => role.role.replace('ROLE_', '')).join(', ');
    let content = `${name} with roles: ${roles}`;
    $("#userWithRoles").html(content);
}