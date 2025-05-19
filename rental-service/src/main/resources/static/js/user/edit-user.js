
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');

    if (!userId) {
        showError('ID пользователя не указан');
        return;
    }

    loadUser(userId);
});

async function loadUser(userId) {
    try {
        const response = await fetch(`/api/users/${userId}`);

        if (!response.ok) {
            throw new Error('Пользователь не найден');
        }

        const user = await response.json();
        renderEditForm(user);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderEditForm(user) {
    const initials = (user.firstName.charAt(0) + user.lastName.charAt(0)).toUpperCase();

    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="edit-user-form">
            <input type="hidden" id="user-id" value="${user.id}">
            
            <div class="avatar-preview" id="avatar-preview">
                ${initials}
            </div>
            
            <div class="form-group">
                <label for="userName"><i class="fas fa-at"></i> Имя пользователя:</label>
                <input type="text" id="userName" value="${user.userName}" required>
            </div>
            
            <div class="form-group">
                <label for="firstName"><i class="fas fa-user"></i> Имя:</label>
                <input type="text" id="firstName" value="${user.firstName}" required>
            </div>
            
            <div class="form-group">
                <label for="lastName"><i class="fas fa-user"></i> Фамилия:</label>
                <input type="text" id="lastName" value="${user.lastName}" required>
            </div>
            
            <div class="form-group">
                <label for="passport"><i class="fas fa-id-card"></i> Паспорт:</label>
                <input type="number" id="passport" value="${user.passport}" required>
            </div>
            
            <div class="form-group">
                <label for="email"><i class="fas fa-envelope"></i> Email:</label>
                <input type="email" id="email" value="${user.email || ''}">
            </div>
            
            <div class="actions">
                <button type="submit" class="button save-btn">
                    <i class="fas fa-save"></i> Сохранить
                </button>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="user-details.html?id=${user.id}" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('edit-user-form').addEventListener('submit', (e) => saveUser(e, user.id));
    document.getElementById('delete-btn').addEventListener('click', () => deleteUser(user.id));
}

async function saveUser(e, userId) {
    e.preventDefault();

    const saveBtn = document.querySelector('.save-btn');
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const userData = {
            id: userId,
            userName: document.getElementById('userName').value,
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            passport: document.getElementById('passport').value,
            email: document.getElementById('email').value || null
        };

        const response = await fetch(`/api/users/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка сохранения');
        }

        showSuccess('Изменения сохранены!');
        setTimeout(() => {
            window.location.href = `user-details.html?id=${userId}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        saveBtn.disabled = false;
        saveBtn.innerHTML = '<i class="fas fa-save"></i> Сохранить';
    }
}

async function deleteUser(userId) {
    if (!confirm('Вы уверены, что хотите удалить этого пользователя?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`/api/users/${userId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления');
        }

        showSuccess('Пользователь удален!');
        setTimeout(() => {
            window.location.href = 'index-user.html';
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

    const form = document.getElementById('edit-user-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('edit-user-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}