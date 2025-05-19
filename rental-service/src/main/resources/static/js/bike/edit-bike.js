document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const bikeId = urlParams.get('id');

    if (!bikeId) {
        showError('ID мотоцикла не указан');
        return;
    }

    loadBike(bikeId);
});

async function loadBike(bikeId) {
    try {
        const response = await fetch(`/api/bikes/${bikeId}`);

        if (!response.ok) {
            throw new Error('Мотоцикл не найден');
        }

        const bike = await response.json();
        renderEditForm(bike);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderEditForm(bike) {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="edit-bike-form">
            <input type="hidden" id="bike-id" value="${bike.id}">
            
            <div class="form-group">
                <label for="name"><i class="fas fa-motorcycle"></i> Название:</label>
                <input type="text" id="name" value="${bike.name}" required>
            </div>
            
            <div class="form-group">
                <label for="price"><i class="fas fa-tag"></i> Цена (₽):</label>
                <input type="number" id="price" step="0.01" min="0" value="${bike.price}" required>
            </div>
            
            <div class="form-group">
                <label for="horsePower"><i class="fas fa-bolt"></i> Мощность (л.с.):</label>
                <input type="number" id="horsePower" min="0" value="${bike.horsePower}" required>
            </div>
            
            <div class="form-group">
                <label for="volume"><i class="fas fa-gas-pump"></i> Объем двигателя (л):</label>
                <input type="number" id="volume" step="0.1" min="0" value="${bike.volume}" required>
            </div>
            
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId" value="${bike.userId || ''}">
            </div>
            
            <div class="actions">
                <button type="submit" class="button save-btn">
                    <i class="fas fa-save"></i> Сохранить
                </button>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="bike-details.html?id=${bike.id}" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('edit-bike-form').addEventListener('submit', (e) => saveBike(e, bike.id));
    document.getElementById('delete-btn').addEventListener('click', () => deleteBike(bike.id));
}

async function saveBike(e, bikeId) {
    e.preventDefault();

    const saveBtn = document.querySelector('.save-btn');
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const bikeData = {
            id: bikeId,
            name: document.getElementById('name').value,
            price: parseFloat(document.getElementById('price').value),
            horsePower: parseInt(document.getElementById('horsePower').value),
            volume: parseFloat(document.getElementById('volume').value),
            userId: document.getElementById('userId').value || null
        };

        const response = await fetch(`/api/bikes/${bikeId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bikeData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка сохранения');
        }

        showSuccess('Изменения сохранены!');
        setTimeout(() => {
            window.location.href = `bike-details.html?id=${bikeId}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        saveBtn.disabled = false;
        saveBtn.innerHTML = '<i class="fas fa-save"></i> Сохранить';
    }
}

async function deleteBike(bikeId) {
    if (!confirm('Вы уверены, что хотите удалить этот мотоцикл?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`/api/bikes/${bikeId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления');
        }

        showSuccess('Мотоцикл удален!');
        setTimeout(() => {
            window.location.href = 'index-bike.html';
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
        deleteBtn.disabled = false;
        deleteBtn.innerHTML = '<i class="fas fa-trash-alt"></i> Удалить';
    }
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${message}`;

    const form = document.getElementById('edit-bike-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('edit-bike-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}