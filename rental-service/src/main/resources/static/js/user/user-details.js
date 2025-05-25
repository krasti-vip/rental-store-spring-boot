document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');

    if (!userId) {
        showError('ID пользователя не указан');
        return;
    }

    loadUserDetails(userId);
});

async function loadUserDetails(userId) {
    try {
        const response = await fetch(`/api/users/${userId}`);

        if (!response.ok) {
            throw new Error('Пользователь не найден');
        }

        const user = await response.json();
        renderUserDetails(user);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderUserDetails(user) {
    const initials = (user.firstName.charAt(0) + user.lastName.charAt(0)).toUpperCase();
    const container = document.getElementById('user-details-container');
    container.innerHTML = `
        <div class="user-details">
            <div class="user-header">
                <div class="user-avatar">${initials}</div>
                <div class="user-info">
                    <div class="user-name">${user.firstName} ${user.lastName}</div>
                    <div class="user-username">@${user.username}</div>
                </div>
            </div>
            
            <div class="detail-section">
                <div class="section-title"><i class="fas fa-id-card"></i> Основная информация</div>
                
                <div class="detail-row">
                    <div class="detail-label">ID:</div>
                    <div class="detail-value">${user.id}</div>
                </div>
                
                <div class="detail-row">
                    <div class="detail-label">Паспорт:</div>
                    <div class="detail-value">${user.passport || 'Не указан'}</div>
                </div>
                
                ${user.email ? `
                <div class="detail-row">
                    <div class="detail-label">Email:</div>
                    <div class="detail-value">${user.email}</div>
                </div>
                ` : ''}
                
            </div>
            
            <div class="user-actions">
                <a href="edit-user.html?id=${user.id}" class="button edit-btn">
                    <i class="fas fa-edit"></i> Редактировать
                </a>
                
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="index-user.html" class="button back-btn">
                    <i class="fas fa-arrow-left"></i> Назад
                </a>
            </div>
        </div>
    `;

    document.getElementById('delete-btn').addEventListener('click', () => deleteUser(user.id));
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
            throw new Error('Не удалось удалить пользователя');
        }

        showSuccess('Пользователь успешно удален!');
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
    const container = document.getElementById('user-details-container');
    container.innerHTML = `
        <div class="error-message">
            <p><i class="fas fa-exclamation-circle"></i> ${message}</p>
            <a href="index-user.html" class="button back-btn">
                <i class="fas fa-arrow-left"></i> Вернуться к списку
            </a>
        </div>
    `;
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const container = document.getElementById('user-details-container');
    container.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}