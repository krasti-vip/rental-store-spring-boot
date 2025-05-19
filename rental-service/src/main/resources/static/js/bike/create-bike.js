document.addEventListener('DOMContentLoaded', () => {
    renderCreateForm();
});

function renderCreateForm() {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="create-bike-form">
            <div class="form-group">
                <label for="name"><i class="fas fa-motorcycle"></i> Название:</label>
                <input type="text" id="name" required>
            </div>
            
            <div class="form-group">
                <label for="price"><i class="fas fa-tag"></i> Цена (₽):</label>
                <input type="number" id="price" step="0.01" min="0" required>
            </div>
            
            <div class="form-group">
                <label for="horsePower"><i class="fas fa-bolt"></i> Мощность (л.с.):</label>
                <input type="number" id="horsePower" min="0" required>
            </div>
            
            <div class="form-group">
                <label for="volume"><i class="fas fa-gas-pump"></i> Объем двигателя (л):</label>
                <input type="number" id="volume" step="0.1" min="0" required>
            </div>
            
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId">
            </div>
            
            <div class="actions">
                <button type="submit" class="button create-btn">
                    <i class="fas fa-plus"></i> Добавить
                </button>
                <a href="index-bike.html" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('create-bike-form').addEventListener('submit', createBike);
}

async function createBike(e) {
    e.preventDefault();

    const createBtn = document.querySelector('.create-btn');
    createBtn.disabled = true;
    createBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const bikeData = {
            name: document.getElementById('name').value,
            price: parseFloat(document.getElementById('price').value),
            horsePower: parseInt(document.getElementById('horsePower').value),
            volume: parseFloat(document.getElementById('volume').value),
            userId: document.getElementById('userId').value || null
        };

        const response = await fetch('/api/bikes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bikeData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка создания мотоцикла');
        }

        const result = await response.json();
        showSuccess('Мотоцикл успешно добавлен!');
        setTimeout(() => {
            window.location.href = `bike-details.html?id=${result.id}`;
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

    const form = document.getElementById('create-bike-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('create-bike-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}