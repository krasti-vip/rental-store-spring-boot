document.addEventListener('DOMContentLoaded', () => {
    renderCreateForm();
});

function renderCreateForm() {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="create-user-form">
            <div class="avatar-preview" id="avatar-preview">
                <i class="fas fa-user"></i>
            </div>
            
            <div class="form-group">
                <label for="userName"><i class="fas fa-at"></i> Имя пользователя:</label>
                <input type="text" id="userName" required>
            </div>
            
            <div class="form-group">
                <label for="firstName"><i class="fas fa-user"></i> Имя:</label>
                <input type="text" id="firstName" required>
            </div>
            
            <div class="form-group">
                <label for="lastName"><i class="fas fa-user"></i> Фамилия:</label>
                <input type="text" id="lastName" required>
            </div>
            
            <div class="form-group">
                <label for="passport"><i class="fas fa-id-card"></i> Паспорт:</label>
                <input type="number" id="passport" required>
            </div>
            
            <div class="form-group">
                <label for="email"><i class="fas fa-envelope"></i> Email:</label>
                <input type="email" id="email">
            </div>
            
            <div class="actions">
                <button type="submit" class="button create-btn">
                    <i class="fas fa-plus"></i> Добавить
                </button>
                <a href="index-user.html" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('create-user-form').addEventListener('submit', createUser);
}

async function createUser(e) {
    e.preventDefault();

    const createBtn = document.querySelector('.create-btn');
    createBtn.disabled = true;
    createBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const userData = {
            userName: document.getElementById('userName').value,
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            passport: document.getElementById('passport').value,
            email: document.getElementById('email').value || null
        };

        const response = await fetch('http://localhost:7873/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка создания пользователя');
        }

        const result = await response.json();
        showSuccess('Пользователь успешно добавлен!');
        setTimeout(() => {
            window.location.href = `user-details.html?id=${result.id}`;
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

    const form = document.getElementById('create-user-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('create-user-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}
