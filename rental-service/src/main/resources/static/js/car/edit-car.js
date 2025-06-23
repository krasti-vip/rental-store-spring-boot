document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const carId = urlParams.get('id');

    if (!carId) {
        showError('ID автомобиля не указан');
        return;
    }

    loadCar(carId);
});

async function loadCar(carId) {
    try {
        const response = await fetch(`http://localhost:7876/api/cars/${carId}`);

        if (!response.ok) {
            throw new Error('Автомобиль не найден');
        }

        const car = await response.json();
        renderEditForm(car);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderEditForm(car) {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="edit-car-form">
            <input type="hidden" id="car-id" value="${car.id}">
            
            <div class="form-group">
                <label for="title"><i class="fas fa-car"></i> Название:</label>
                <input type="text" id="title" value="${car.title}" required>
            </div>
            
            <div class="form-group">
                <label for="price"><i class="fas fa-tag"></i> Цена (₽):</label>
                <input type="number" id="price" step="0.01" min="0" value="${car.price}" required>
            </div>
            
            <div class="form-group">
                <label for="horsePower"><i class="fas fa-bolt"></i> Мощность (л.с.):</label>
                <input type="number" id="horsePower" min="0" value="${car.horsePower}" required>
            </div>
            
            <div class="form-group">
                <label for="volume"><i class="fas fa-gas-pump"></i> Объем двигателя (л):</label>
                <input type="number" id="volume" step="0.1" min="0" value="${car.volume}" required>
            </div>
            
            <div class="form-group">
                <label for="color"><i class="fas fa-palette"></i> Цвет:</label>
                <input type="text" id="color" value="${car.color}" required>
                <div class="color-preview" id="color-preview" style="background-color: ${car.color}"></div>
            </div>
            
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId" value="${car.userId || ''}">
            </div>
            
            <div class="actions">
                <button type="submit" class="button save-btn">
                    <i class="fas fa-save"></i> Сохранить
                </button>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="car-details.html?id=${car.id}" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('color').addEventListener('input', updateColorPreview);
    document.getElementById('edit-car-form').addEventListener('submit', (e) => saveCar(e, car.id));
    document.getElementById('delete-btn').addEventListener('click', () => deleteCar(car.id));
}

function updateColorPreview() {
    const colorInput = document.getElementById('color');
    const preview = document.getElementById('color-preview');
    preview.style.backgroundColor = colorInput.value;
}

async function saveCar(e, carId) {
    e.preventDefault();

    const saveBtn = document.querySelector('.save-btn');
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const carData = {
            id: carId,
            title: document.getElementById('title').value,
            price: parseFloat(document.getElementById('price').value),
            horsePower: parseInt(document.getElementById('horsePower').value),
            volume: parseFloat(document.getElementById('volume').value),
            color: document.getElementById('color').value,
            userId: document.getElementById('userId').value || null
        };

        const response = await fetch(`http://localhost:7876/api/cars/${carId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(carData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка сохранения');
        }

        showSuccess('Изменения сохранены!');
        setTimeout(() => {
            window.location.href = `car-details.html?id=${carId}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        saveBtn.disabled = false;
        saveBtn.innerHTML = '<i class="fas fa-save"></i> Сохранить';
    }
}

async function deleteCar(carId) {
    if (!confirm('Вы уверены, что хотите удалить этот автомобиль?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`http://localhost:7876/api/cars/${carId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления');
        }

        showSuccess('Автомобиль удален!');
        setTimeout(() => {
            window.location.href = 'index-car.html';
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

    const form = document.getElementById('edit-car-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('edit-car-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}