document.addEventListener('DOMContentLoaded', () => {
    renderCreateForm();
});

function renderCreateForm() {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="create-bicycle-form">
            <div class="form-group">
                <label for="model"><i class="fas fa-bicycle"></i> Модель:</label>
                <input type="text" id="model" required>
            </div>
            
            <div class="form-group">
                <label for="price"><i class="fas fa-tag"></i> Цена (₽):</label>
                <input type="number" id="price" step="0.01" min="0" required>
            </div>
            
            <div class="form-group">
                <label for="color"><i class="fas fa-palette"></i> Цвет:</label>
                <input type="text" id="color" required>
                <div class="color-preview" id="color-preview"></div>
            </div>
            
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId">
            </div>
            
            <div class="actions">
                <button type="submit" class="button create-btn">
                    <i class="fas fa-plus"></i> Добавить
                </button>
                <a href="index-bicycle.html" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('color').addEventListener('input', updateColorPreview);
    document.getElementById('create-bicycle-form').addEventListener('submit', createBicycle);
}

function updateColorPreview() {
    const colorInput = document.getElementById('color');
    const preview = document.getElementById('color-preview');
    preview.style.backgroundColor = colorInput.value || 'transparent';
}

async function createBicycle(e) {
    e.preventDefault();

    const createBtn = document.querySelector('.create-btn');
    createBtn.disabled = true;
    createBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const bicycleData = {
            model: document.getElementById('model').value,
            price: parseFloat(document.getElementById('price').value),
            color: document.getElementById('color').value,
            userId: document.getElementById('userId').value || null
        };

        const response = await fetch('http://localhost:7878/api/bicycles', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bicycleData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка создания велосипеда');
        }

        const result = await response.json();
        showSuccess('Велосипед успешно добавлен!');
        setTimeout(() => {
            window.location.href = `bicycle-details.html?id=${result.id}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        createBtn.disabled = false;
        createBtn.innerHTML = '<i class="fas fa-plus"></i> Добавить';
    }
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${message}`;

    const form = document.getElementById('create-bicycle-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('create-bicycle-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}