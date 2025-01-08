const apiUrl = '/api/orders';
const productsItemsUrl = '/api/productsItems';
const buyersUrl = '/api/buyers';
const usersUrl = '/api/sellers';

let allBuyers = [];
let allSellers = [];
let allProductsItems = [];

function loadBuyers() {
    return axios.get(buyersUrl)
        .then(response => {
            allBuyers = response.data;
            const buyerSelect = document.getElementById('buyerId');
            const editBuyerSelect = document.getElementById('editOrderBuyerId');

            [buyerSelect, editBuyerSelect].forEach(select => {
                select.innerHTML = '<option value="" disabled selected>Выберите покупателя</option>';
                allBuyers.forEach(buyer => {
                    const option = document.createElement('option');
                    option.value = buyer.id;
                    option.textContent = buyer.name;
                    select.appendChild(option);
                });
            });
        })
        .catch(error => console.error('Ошибка загрузки покупателей:', error));
}

function loadSellers() {
    return axios.get(usersUrl)
        .then(response => {
            allSellers = response.data;
            const sellerSelect = document.getElementById('sellerId');
            const editSellerSelect = document.getElementById('editOrderUserId');

            [sellerSelect, editSellerSelect].forEach(select => {
                select.innerHTML = '<option value="" disabled selected>Выберите продавца</option>';
                allSellers.forEach(seller => {
                    const option = document.createElement('option');
                    option.value = seller.id;
                    option.textContent = seller.username;
                    select.appendChild(option);
                });
            });
        })
        .catch(error => console.error('Ошибка загрузки продавцов:', error));
}

function loadProductsItems() {
    return axios.get(productsItemsUrl)
        .then(response => {
            allProductsItems = response.data;
            const selects = document.querySelectorAll('.product-item-select');
            selects.forEach(select => {
                select.innerHTML = '<option value="" disabled selected>Выберите товар</option>';
                allProductsItems.forEach(item => {
                    const option = document.createElement('option');
                    option.value = item.id;
                    option.textContent = item.name;
                    select.appendChild(option);
                });
            });
        })
        .catch(error => console.error('Ошибка загрузки товаров:', error));
}

function loadOrders() {
    axios.get('/api/orders/sortByDate')
        .then(response => {
            const orders = response.data;
            const tableBody = document.getElementById('order-table-body');
            tableBody.innerHTML = '';
            orders.forEach(order => {
                let actionButtons = '';

                if (order.status !== 'CANCELLED' && order.status !== 'RETURNED' && order.status !== "COMPLETED") {
                    actionButtons = `
                        <button class="btn btn-warning btn-sm" onclick="openEditOrderModal(${order.id}, '${order.salesDate}', '${order.status}', '${order.buyer.id}', '${order.userId}', '${encodeURIComponent(JSON.stringify(order.orderItems))}')">Редактировать</button><!--                        <br>-->
<!--                        <br>-->
                        <button class="btn btn-danger btn-sm" onclick="completeOrder(${order.id})">Завершить</button>
<!--                        <br>-->
<!--                        <br>-->
                        <button class="btn btn-danger btn-sm" onclick="cancelOrder(${order.id})">Отменить</button>
<!--                        <br>-->
<!--                        <br>-->
                        <button class="btn btn-danger btn-sm" onclick="returnOrder(${order.id})">Возврат</button>`;
                }

                const row = `
                    <tr>
                        <td>${formatDate(order.salesDate)}</td>
                        <td>${order.status}</td>
                        <td>${order.buyer ? order.buyer.name : 'Без покупателя'}</td>
                        <td>${order.user ? order.user.username : 'Без продавца'}</td>
                        <td>${order.orderItems.map(item => item.productItem.name + ", Размер: " + item.productItem.size.name).join('<br>')}</td>
                        <td>${order.orderItems.map(item => item.quantity).join('<br>')}</td>
                        <td>
                            ${actionButtons}
                        </td>
                    </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error('Ошибка загрузки заказов:', error));
}

function openEditOrderModal(orderId, date, status, buyerId, userId, orderItemsJson) {
    document.getElementById('editOrderDate').value = new Date(date).toISOString().split('T')[0];
    document.getElementById('editOrderStatus').value = status;
    document.getElementById('editOrderBuyerId').value = buyerId;
    document.getElementById('editOrderUserId').value = userId;

    const container = document.getElementById('edit-order-items-container');
    const template = document.getElementById('edit-order-item-template');
    container.innerHTML = '';
    container.appendChild(template);

    const orderItems = JSON.parse(decodeURIComponent(orderItemsJson));
    orderItems.forEach(item => {
        const row = template.cloneNode(true);
        row.style.display = '';
        row.removeAttribute('id');
        const productItemSelect = row.querySelector('.edit-product-item-select');
        const quantityInput = row.querySelector('.edit-quantity-input');
        const removeButton = row.querySelector('.remove-item-btn');

        allProductsItems.forEach(productItem => {
            const option = document.createElement('option');
            option.value = productItem.id;
            option.textContent = productItem.name;
            productItemSelect.appendChild(option);
        });
        productItemSelect.value = item.productItem.id;
        quantityInput.value = item.quantity;

        removeButton.addEventListener('click', () => {
            container.removeChild(row);
        });

        container.appendChild(row);
    });

    document.getElementById('edit-add-item-btn').onclick = () => {
        const newItemRow = template.cloneNode(true);
        newItemRow.style.display = '';
        newItemRow.removeAttribute('id');

        const productItemSelect = newItemRow.querySelector('.edit-product-item-select');
        const removeButton = newItemRow.querySelector('.remove-item-btn');
        productItemSelect.innerHTML = '<option value="" disabled selected>Выберите позицию</option>';
        allProductsItems.forEach(productItem => {
            const option = document.createElement('option');
            option.value = productItem.id;
            option.textContent = productItem.name;
            productItemSelect.appendChild(option);
        });

        removeButton.addEventListener('click', () => {
            container.removeChild(newItemRow);
        });

        container.appendChild(newItemRow);
    };

    const saveButton = document.getElementById('save-edit-button');
    saveButton.replaceWith(saveButton.cloneNode(true));
    document.getElementById('save-edit-button').addEventListener('click', () => {
        const orderItems = Array.from(container.querySelectorAll('.order-item-row'))
            .filter(row => row.style.display !== 'none')
            .map(row => ({
                productItemId: parseInt(row.querySelector('.edit-product-item-select').value),
                quantity: parseInt(row.querySelector('.edit-quantity-input').value)
            }));

        const newOrder = {
            salesDate: new Date(document.getElementById('editOrderDate').value).toISOString().slice(0, 19),
            status: document.getElementById('editOrderStatus').value,
            buyerId: parseInt(document.getElementById('editOrderBuyerId').value),
            userId: parseInt(document.getElementById('editOrderUserId').value),
            orderItems
        };

        axios.post(`${apiUrl}/${orderId}/update`, newOrder)
            .then(() => {
                console.log('Заказ добавлен');
                document.getElementById('edit-order-form').reset();
                container.innerHTML = '';
                container.appendChild(template);
                loadOrders();
                const modal = bootstrap.Modal.getInstance(document.getElementById('editOrderModal'));
                modal.hide();            })
            .catch(error =>
            {
                console.error('Ошибка добавления заказа:', error)
            });
    });

    new bootstrap.Modal(document.getElementById('editOrderModal')).show();
}

function completeOrder(orderId) {
    axios.post(`${apiUrl}/${orderId}/complete`)
        .then(() => {
            console.log(`Заказ ${orderId} завершен`);
            loadOrders();
        })
        .catch(error => console.error('Ошибка завершения заказа:', error));
}

function cancelOrder(orderId) {
    axios.post(`${apiUrl}/${orderId}/cancel`)
        .then(() => {
            console.log(`Заказ ${orderId} отменен`);
            loadOrders();
        })
        .catch(error => console.error('Ошибка отмены заказа:', error));
}

function returnOrder(orderId) {
    axios.post(`${apiUrl}/${orderId}/return`)
        .then(() => {
            console.log(`Заказ ${orderId} возвращен`);
            loadOrders();
        })
        .catch(error => console.error('Ошибка возврата заказа:', error));
}

document.addEventListener('DOMContentLoaded', () => {
    loadBuyers().then(loadSellers).then(loadProductsItems).then(loadOrders);
});

document.addEventListener('DOMContentLoaded', () => {
    const orderItemsContainer = document.getElementById('order-items-container');
    const orderItemTemplate = document.getElementById('order-item-template');
    const addItemButton = document.getElementById('add-item-btn');

    addItemButton.addEventListener('click', () => {
        const newItemRow = orderItemTemplate.cloneNode(true);
        newItemRow.style.display = '';
        newItemRow.removeAttribute('id');

        orderItemsContainer.appendChild(newItemRow);

        const removeButton = newItemRow.querySelector('.remove-item-btn');
        removeButton.addEventListener('click', () => {
            orderItemsContainer.removeChild(newItemRow);
        });

        const productItemSelect = newItemRow.querySelector('.product-item-select');
        productItemSelect.innerHTML = '<option value="" disabled selected>Выберите позицию</option>';
        allProductsItems.forEach(productItem => {
            const option = document.createElement('option');
            option.value = productItem.id;
            option.textContent = productItem.name;
            productItemSelect.appendChild(option);
        });
    });

    const addOrderForm = document.getElementById('add-order-form');
    addOrderForm.addEventListener('submit', event => {
        event.preventDefault();

        const formData = new FormData(addOrderForm);
        const orderItems = Array.from(orderItemsContainer.querySelectorAll('.order-item-row:not(#order-item-template)')).map(row => {
            const productItemId = row.querySelector('.product-item-select').value;
            const quantity = row.querySelector('.quantity-input').value;
            return { productItemId: parseInt(productItemId), quantity: parseInt(quantity) };
        });

        const date = formData.get('date');

        const formattedDate = new Date(date);

        const salesDate = formattedDate.toISOString().slice(0, 19);

        const newOrder = {
            salesDate: salesDate,
            status: formData.get('status'),
            buyerId: parseInt(formData.get('buyerId')),
            userId: parseInt(formData.get('sellerId')),
            orderItems: orderItems
        };

        console.log(newOrder);

        axios.post('/api/orders/createNewOrder', newOrder)
            .then(() => {
                console.log('Заказ добавлен');
                addOrderForm.reset();
                orderItemsContainer.querySelectorAll('.order-item-row:not(#order-item-template)').forEach(row => row.remove());
                loadOrders();
            })
            .catch(error => console.error('Ошибка добавления заказа:', error));
    });
});

const formatDate = (dateString) => {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('ru-RU', { dateStyle: 'medium' }).format(date);
};