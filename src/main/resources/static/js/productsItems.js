const apiUrl = '/api/productsItems';
const productsUrl = '/api/products';
const sizesUrl = '/api/sizes';

let allSizes = [];
let allProducts = [];

function openEditModal(productItemId, name, quantity, sizeId, productId) {
    document.getElementById('editProductItemName').value = name;
    document.getElementById('editProductItemQuantity').value = quantity;

    const sizeSelect = document.getElementById('editProductItemSizeId');
    sizeSelect.innerHTML = '<option value="" disabled>Выберите размер</option>';

    allSizes.forEach(size => {
        const option = document.createElement('option');
        option.value = size.id;
        option.textContent = size.name;

        if (sizeId === size.id) {
            option.selected = true;
        }

        sizeSelect.appendChild(option);
    });

    const productSelect = document.getElementById('editProductItemProductId');
    productSelect.innerHTML = '<option value="" disabled>Выберите товар</option>';

    allProducts.forEach(product => {
        const option = document.createElement('option');
        option.value = product.id;
        option.textContent = product.name;

        if (productId === product.id) {
            option.selected = true;
        }

        productSelect.appendChild(option);
    });

    const saveButton = document.getElementById('save-edit-button');
    saveButton.onclick = () => saveProductItemChanges(productItemId);

    const modal = new bootstrap.Modal(document.getElementById('editProductItemModal'));
    modal.show();
}

function saveProductItemChanges(productItemId) {
    const form = document.getElementById('edit-productItem-form');
    const formData = new FormData(form);

    const updatedProductItem = {
        name: formData.get('name'),
        quantity: formData.get('quantity'),
        sizeId: parseInt(formData.get('sizeId')),
        productId: parseInt(formData.get('productId'))
    };

    axios.post(`${apiUrl}/${productItemId}/update`, updatedProductItem)
        .then(() => {
            console.log('Позиция обновлена');
            const modal = bootstrap.Modal.getInstance(document.getElementById('editProductItemModal'));
            modal.hide();
            loadProductsItems();
        })
        .catch(error => console.error('Ошибка обновления позиции:', error));
}

function loadSizes() {
    return axios.get(sizesUrl)
        .then(response => {
            allSizes = response.data;
        })
        .catch(error => console.error('Ошибка загрузки размеров:', error));
}
function loadProducts() {
    return axios.get(productsUrl)
        .then(response => {
            allProducts = response.data;
        })
        .catch(error => console.error('Ошибка загрузки товаров:', error));
}

function loadProductsItems() {
    axios.get(apiUrl)
        .then(response => {
            const productsItems = response.data;
            const tableBody = document.getElementById('productItem-table-body');
            tableBody.innerHTML = '';
            productsItems.forEach(productItem => {
                const row = `
                    <tr>
                        <td>${productItem.name}</td>
                        <td>${productItem.quantity}</td>
                        <td>${productItem.size ? productItem.size.name : 'Без размера'}</td>
                        <td>${productItem.product ? productItem.product.name : 'Без товара'}</td>
                        <td>
                            <button class="btn btn-warning btn-sm" onclick="openEditModal(${productItem.id}, '${productItem.name}', '${productItem.quantity}', '${productItem.size.id}', ${productItem.product.id})">Редактировать</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteProductItem(${productItem.id})">Удалить</button>
                        </td>
                    </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error('Ошибка загрузки позиций:', error));
}

function addProductItem(event) {
    event.preventDefault();
    const form = document.getElementById('add-productItem-form');
    const formData = new FormData(form);

    const newProductItem = {
        name: formData.get('name'),
        quantity: formData.get('quantity'),
        sizeId: parseInt(formData.get('sizeId')),
        productId: parseInt(formData.get('productId'))
    };

    axios.post(`${apiUrl}/createNewProductItem`, newProductItem)
        .then(() => {
            console.log('Позиция добавлен');
            form.reset();
            loadProductsItems();
        })
        .catch(error => console.error('Ошибка добавления позиции:', error));
}

function deleteProductItem(productItemId) {
    axios.post(`${apiUrl}/${productItemId}/delete`)
        .then(() => {
            console.log(`Позиция ${productItemId} удален`);
            loadProductsItems();
        })
        .catch(error => console.error('Ошибка удаления позиции:', error));
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('add-productItem-form');
    form.onsubmit = addProductItem;

    loadSizes().then(loadProducts).then(loadProductsItems);
});