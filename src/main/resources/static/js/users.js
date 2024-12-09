const apiUrl = '/api/users';
const rolesUrl = '/api/roles';

let allRoles = [];

async function loadRoles() {
    try {
        const response = await axios.get(rolesUrl);
        allRoles = response.data;

        const rolesCheckboxes = document.getElementById('roles-checkboxes');
        rolesCheckboxes.innerHTML = allRoles.map(role => `
        <div class="form-check">
          <input class="form-check-input" type="checkbox" value="${role.id}" id="role-${role.id}">
          <label class="form-check-label" for="role-${role.id}">${role.name}</label>
        </div>`).join('');
    } catch (error) {
        console.error('Ошибка загрузки ролей:', error);
    }
}

async function loadUsers() {
    try {
        const response = await axios.get(apiUrl);
        const users = response.data;
        const tableBody = document.getElementById('user-table-body');
        tableBody.innerHTML = '';

        users.forEach(user => {
            const rolesCheckboxes = allRoles.map(role => `
          <div class="form-check">
            <input class="form-check-input" type="checkbox" value="${role.id}" 
                   id="role-${user.id}-${role.id}" 
                   ${user.roles.some(r => r.id === role.id) ? 'checked' : ''}
                   onchange="updateRoles(${user.id})">
            <label class="form-check-label" for="role-${user.id}-${role.id}">${role.name}</label>
          </div>`).join('');

            const row = `
          <tr>
            <td>${user.username}</td>
            <td>${rolesCheckboxes}</td>
            <td>
              <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">Удалить</button>
            </td>
          </tr>`;
            tableBody.innerHTML += row;
        });
    } catch (error) {
        console.error('Ошибка загрузки пользователей:', error);
    }
}

async function deleteUser(userId) {
    try {
        await axios.post(`${apiUrl}/${userId}/delete`);
        console.log(`Пользователь ${userId} удален`);
        loadUsers();
    } catch (error) {
        console.error('Ошибка удаления пользователя:', error);
    }
}

async function updateRoles(userId) {
    try {
        const checkboxes = document.querySelectorAll(`#user-table-body input[id^="role-${userId}-"]`);        const selectedRoles = Array.from(checkboxes)
            .filter(checkbox => checkbox.checked)
            .map(checkbox => parseInt(checkbox.value));
        console.log('Selected Roles:', selectedRoles);
        await axios.post(`${apiUrl}/${userId}/roles`, selectedRoles);
        console.log(`Роли пользователя ${userId} обновлены`);
    } catch (error) {
        console.error('Ошибка обновления ролей:', error);
    }
}

document.getElementById('add-user-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const checkboxes = document.querySelectorAll('#roles-checkboxes input:checked');
    const selectedRoles = Array.from(checkboxes).map(checkbox => parseInt(checkbox.value));

    try {
        await axios.post(`${apiUrl}/addNewUser`, { username, password, roles: selectedRoles });
        console.log('Пользователь добавлен');
        document.getElementById('add-user-form').reset();
        await loadUsers();
    } catch (error) {
        console.error('Ошибка добавления пользователя:', error);
    }
});

loadRoles().then(loadUsers);