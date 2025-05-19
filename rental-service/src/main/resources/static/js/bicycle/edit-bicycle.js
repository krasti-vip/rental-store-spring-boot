document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const bicycleId = urlParams.get('id');

    if (!bicycleId) {
        showError('ID велосипеда не указан');
        return;
    }

    loadBicycle(bicycleId);
});

async function loadBicycle(bicycleId) {
    try {
        const response = await fetch(`/api/bicycles/${bicycleId}`);

        if (!response.ok) {
            throw new Error('Велосипед не найден');
        }

        const bicycle = await response.json();
        renderEditForm(bicycle);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderEditForm(bicycle) {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="edit-bicycle-form">
            <input type="hidden" id="bicycle-id" value="${bicycle.id}">
            
            <div class="form-group">
                <label for="model"><i class="fas fa-bicycle"></i> Модель:</label>
                <input type="text" id="model" value="${bicycle.model}" required>
            </div>
            
            <div class="form-group">
                <label for="price"><i class="fas fa-tag"></i> Цена (₽):</label>
                <input type="number" id="price" step="0.01" min="0" value="${bicycle.price}" required>
            </div>
            
            <div class="form-group">
                <label for="color"><i class="fas fa-palette"></i> Цвет:</label>
                <input type="text" id="color" value="${bicycle.color}" required>
                <div class="color-preview" id="color-preview" style="background-color: ${bicycle.color}"></div>
            </div>
            
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId" value="${bicycle.userId || ''}">
            </div>
            
            <div class="actions">
                <button type="submit" class="button save-btn">
                    <i class="fas fa-save"></i> Сохранить
                </button>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="bicycle-details.html?id=${bicycle.id}" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('color').addEventListener('input', updateColorPreview);
    document.getElementById('edit-bicycle-form').addEventListener('submit', (e) => saveBicycle(e, bicycle.id));
    document.getElementById('delete-btn').addEventListener('click', () => deleteBicycle(bicycle.id));
}

function updateColorPreview() {
    const colorInput = document.getElementById('color');
    const preview = document.getElementById('color-preview');
    preview.style.backgroundColor = colorInput.value;
}

async function saveBicycle(e, bicycleId) {
    e.preventDefault();

    const saveBtn = document.querySelector('.save-btn');
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const bicycleData = {
            id: bicycleId,
            model: document.getElementById('model').value,
            price: parseFloat(document.getElementById('price').value),
            color: document.getElementById('color').value,
            userId: document.getElementById('userId').value || null
        };

        const response = await fetch(`/api/bicycles/${bicycleId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bicycleData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка сохранения');
        }

        showSuccess('Изменения сохранены!');
        setTimeout(() => {
            window.location.href = `bicycle-details.html?id=${bicycleId}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        saveBtn.disabled = false;
        saveBtn.innerHTML = '<i class="fas fa-save"></i> Сохранить';
    }
}

async function deleteBicycle(bicycleId) {
    if (!confirm('Вы уверены, что хотите удалить этот велосипед?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`/api/bicycles/${bicycleId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления');
        }

        showSuccess('Велосипед удален!');
        setTimeout(() => {
            window.location.href = 'index-bicycle.html';
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

    const form = document.getElementById('edit-bicycle-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('edit-bicycle-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}