const apiUrl = '/api/sizes';

function loadSizes() {
    axios.get(apiUrl)
        .then(response => {
            const sizes = response.data;
            const tableBody = document.getElementById('size-table-body');
            tableBody.innerHTML = '';
            sizes.forEach(size => {
                const row = `<tr>
                            <td>${size.name}</td>
                            <td>
                            <button class="btn btn-warning btn-sm" onclick="showEditModal(${size.id}, '${size.name}')">Редактировать</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteSize(${size.id})">Удалить</button>
                            </td>
                        </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error('Ошибка загрузки размеров:', error));
}

document.getElementById('create-size-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const sizeName = document.getElementById('sizeName').value;
    axios.post(`${apiUrl}/createNewSize`, {name: sizeName})
        .then(() => {
            loadSizes();
            document.getElementById('sizeName').value = '';
        })
        .catch(error => console.error('Ошибка создания размера:', error));
});

function deleteSize(sizeId) {
    axios.post(`${apiUrl}/${sizeId}/delete`)
        .then(() => loadSizes())
        .catch(error => {
            console.error('Ошибка удаления размера:', error);
        });
}

let editingSizeId = null;

function showEditModal(sizeId, currentName) {
    editingSizeId = sizeId;
    document.getElementById('editSizeName').value = currentName;
    const editModal = new bootstrap.Modal(document.getElementById('editSizeModal'));
    editModal.show();
}

document.getElementById('save-edit-button').addEventListener('click', function () {
    const newSizeName = document.getElementById('editSizeName').value;

    if (!editingSizeId) {
        console.error('Нет редактируемого размера.');
        return;
    }

    axios.post(`${apiUrl}/${editingSizeId}/update`, { name: newSizeName })
        .then(() => {
            loadSizes();
            const editModal = bootstrap.Modal.getInstance(document.getElementById('editSizeModal'));
            editModal.hide();
        })
        .catch(error => {
            console.error('Ошибка редактирования размера:', error);
        });
});

loadSizes();