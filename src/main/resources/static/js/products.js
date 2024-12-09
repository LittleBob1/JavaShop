const apiUrl = '/api/products';
const brandsUrl = '/api/brands';

let allBrands = [];

function openEditModal(productId, name, description, price, brandId) {
    document.getElementById('editProductName').value = name;
    document.getElementById('editProductDescription').value = description;
    document.getElementById('editProductPrice').value = price;

    const brandSelect = document.getElementById('editProductBrand');
    brandSelect.innerHTML = '<option value="" disabled>Выберите бренд</option>';

    allBrands.forEach(brand => {
        const option = document.createElement('option');
        option.value = brand.id;
        option.textContent = brand.name;

        if (brandId === brand.id) {
            option.selected = true;
        }

        brandSelect.appendChild(option);
    });


    const saveButton = document.getElementById('save-edit-button');
    saveButton.onclick = () => saveProductChanges(productId);

    const modal = new bootstrap.Modal(document.getElementById('editProductModal'));
    modal.show();
}

function saveProductChanges(productId) {
    const form = document.getElementById('edit-product-form');
    const formData = new FormData(form);

    const updatedProduct = {
        name: formData.get('name'),
        description: formData.get('description'),
        price: parseFloat(formData.get('price')),
        brandId: parseInt(formData.get('brandId'))
    };

    axios.post(`${apiUrl}/${productId}/update`, updatedProduct)
        .then(() => {
            console.log('Товар обновлен');
            const modal = bootstrap.Modal.getInstance(document.getElementById('editProductModal'));
            modal.hide();
            loadProducts();
        })
        .catch(error => console.error('Ошибка обновления товара:', error));
}

function loadBrands() {
    return axios.get(brandsUrl)
        .then(response => {
            allBrands = response.data;
        })
        .catch(error => console.error('Ошибка загрузки брендов:', error));
}

function loadProducts() {
    axios.get(apiUrl)
        .then(response => {
            const products = response.data;
            const tableBody = document.getElementById('product-table-body');
            tableBody.innerHTML = '';
            products.forEach(product => {
                const row = `
                    <tr>
                        <td>${product.name}</td>
                        <td>${product.description}</td>
                        <td>${product.price}</td>
                        <td>${product.brand ? product.brand.name : 'Без бренда'}</td>
                        <td>
                            <button class="btn btn-warning btn-sm" onclick="openEditModal(${product.id}, '${product.name}', '${product.description}', '${product.price}', ${product.brand.id})">Редактировать</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteProduct(${product.id})">Удалить</button>
                        </td>
                    </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error('Ошибка загрузки товаров:', error));
}

function addProduct(event) {
    event.preventDefault();
    const form = document.getElementById('add-product-form');
    const formData = new FormData(form);

    const newProduct = {
        name: formData.get('name'),
        description: formData.get('description'),
        price: parseFloat(formData.get('price')),
        brandId: parseInt(formData.get('brandId'))
    };

    axios.post(`${apiUrl}/createNewProduct`, newProduct)
        .then(() => {
            console.log('Товар добавлен');
            form.reset();
            loadProducts();
        })
        .catch(error => console.error('Ошибка добавления товара:', error));
}

function deleteProduct(productId) {
    axios.post(`${apiUrl}/${productId}/delete`)
        .then(() => {
            console.log(`Товар ${productId} удален`);
            loadProducts();
        })
        .catch(error => console.error('Ошибка удаления товара:', error));
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('add-product-form');
    form.onsubmit = addProduct;

    loadBrands().then(loadProducts);
});