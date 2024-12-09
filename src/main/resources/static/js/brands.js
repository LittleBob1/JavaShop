const apiUrl = '/api/brands';

function loadBrands() {
    axios.get(apiUrl)
        .then(response => {
            const brands = response.data;
            const tableBody = document.getElementById('brand-table-body');
            tableBody.innerHTML = '';
            brands.forEach(brand => {
                const row = `<tr>
                            <td>${brand.name}</td>
                            <td>
                                <button class="btn btn-warning btn-sm" onclick="showEditModal(${brand.id}, '${brand.name}')">Редактировать</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteBrand(${brand.id})">Удалить</button>
                            </td>
                        </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error('Ошибка загрузки брендов:', error));
}

document.getElementById('create-brand-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const brandName = document.getElementById('brandName').value;
    axios.post(`${apiUrl}/createNewBrand`, {name: brandName})
        .then(() => {
            loadBrands();
            document.getElementById('brandName').value = '';
        })
        .catch(error => console.error('Ошибка создания бренда:', error));
});

function deleteBrand(brandId) {
    axios.post(`${apiUrl}/${brandId}/delete`)
        .then(() => loadBrands())
        .catch(error => {
            console.error('Ошибка удаления бренда:', error);
        });
}

let editingBrandId = null;

function showEditModal(brandId, currentName) {
    editingBrandId = brandId;
    document.getElementById('editBrandName').value = currentName;
    const editModal = new bootstrap.Modal(document.getElementById('editBrandModal'));
    editModal.show();
}

document.getElementById('save-edit-button').addEventListener('click', function () {
    const newBrandName = document.getElementById('editBrandName').value;

    if (!editingBrandId) {
        console.error('Нет редактируемого бренда.');
        return;
    }

    axios.post(`${apiUrl}/${editingBrandId}/update`, { name: newBrandName })
        .then(() => {
            loadBrands();
            const editModal = bootstrap.Modal.getInstance(document.getElementById('editBrandModal'));
            editModal.hide();
        })
        .catch(error => {
            console.error('Ошибка редактирования бренда:', error);
        });
});

loadBrands();