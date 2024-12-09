const apiUrl = '/api/buyers';

function loadBuyers() {
    axios.get(apiUrl)
        .then(response => {
            const buyers = response.data;
            const tableBody = document.getElementById('buyer-table-body');
            tableBody.innerHTML = '';
            buyers.forEach(buyer => {
                const row = `<tr>
                            <td>${buyer.name}</td>
                            <td>${buyer.email}</td>
                            <td>${buyer.phone}</td>
                            <td>
                                <button class="btn btn-warning btn-sm" onclick="showEditModal(${buyer.id}, '${buyer.name}', '${buyer.email}', '${buyer.phone}')">Редактировать</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteBuyer(${buyer.id})">Удалить</button>
                            </td>
                        </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error('Ошибка загрузки покупателей:', error));
}

document.getElementById('create-buyer-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const buyerName = document.getElementById('buyerName').value;
    const buyerEmail = document.getElementById('buyerEmail').value;
    const buyerPhone = document.getElementById('buyerPhone').value;
    axios.post(`${apiUrl}/createNewBuyer`, { name: buyerName, email: buyerEmail, phone: buyerPhone })
        .then(() => {
            loadBuyers();
            document.getElementById('buyerName').value = '';
            document.getElementById('buyerEmail').value = '';
            document.getElementById('buyerPhone').value = '';
        })
        .catch(error => console.error('Ошибка создания покупателя:', error));
});

function deleteBuyer(buyerId) {
    axios.post(`${apiUrl}/${buyerId}/delete`)
        .then(() => loadBuyers())
        .catch(error => {
            console.error('Ошибка удаления покупателя:', error);
        });
}

let editingBuyerId = null;

function showEditModal(buyerId, currentName, email, phone) {
    editingBuyerId = buyerId;
    document.getElementById('editBuyerName').value = currentName;
    document.getElementById('editBuyerEmail').value = email;
    document.getElementById('editBuyerPhone').value = phone;
    const editModal = new bootstrap.Modal(document.getElementById('editBuyerModal'));
    editModal.show();
}

document.getElementById('save-edit-button').addEventListener('click', function () {
    const newBuyerName = document.getElementById('editBuyerName').value;
    const newBuyerEmail = document.getElementById('editBuyerEmail').value;
    const newBuyerPhone = document.getElementById('editBuyerPhone').value;

    if (!editingBuyerId) {
        console.error('Нет редактируемого покупателя.');
        return;
    }

    axios.post(`${apiUrl}/${editingBuyerId}/update`, { name: newBuyerName, email: newBuyerEmail, phone: newBuyerPhone })
        .then(() => {
            loadBuyers();
            const editModal = bootstrap.Modal.getInstance(document.getElementById('editBuyerModal'));
            editModal.hide();
        })
        .catch(error => {
            console.error('Ошибка редактирования покупателя:', error);
        });
});

loadBuyers();