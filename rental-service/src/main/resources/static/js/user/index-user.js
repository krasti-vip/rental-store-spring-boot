document.addEventListener('DOMContentLoaded', () => {
    loadUsers();
});

async function loadUsers() {
    try {
        const response = await fetch('/api/users');
        const users = await response.json();
        renderUsers(users);
    } catch (error) {
        console.error('Ошибка загрузки пользователей:', error);
        showError('Произошла ошибка при загрузке пользователей');
    }
}

function renderUsers(users) {
    const container = document.getElementById('users-container');
    container.innerHTML = '';

    users.forEach(user => {
        const userElement = document.createElement('div');
        userElement.className = 'user-card';

        const initials = (user.firstName.charAt(0) + user.lastName.charAt(0)).toUpperCase();

        userElement.innerHTML = `
            <div class="user-avatar">${initials}</div>
            <div class="user-name">${user.firstName} ${user.lastName}</div>
            <div class="user-username">@${user.userName}</div>
            
            <div class="user-details">
                <div class="user-detail">
                    <div class="detail-label">Паспорт:</div>
                    <div class="detail-value">${user.passport}</div>
                </div>
                
                ${user.email ? `
                <div class="user-detail">
                    <div class="detail-label">Email:</div>
                    <div class="detail-value">${user.email}</div>
                </div>
                ` : ''}
            </div>
            
            <div class="user-actions">
                <a href="user-details.html?id=${user.id}" class="button view-btn">
                    <i class="fas fa-eye"></i> Просмотр
                </a>
               
            </div>
        `;
        container.appendChild(userElement);
    });

    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const userId = e.target.closest('button').dataset.id;
            deleteUser(userId);
        });
    });
}

async function deleteUser(userId) {
    if (!confirm('Вы уверены, что хотите удалить этого пользователя?\nЭто действие нельзя отменить.')) {
        return;
    }

    try {
        const response = await fetch(`/api/users/${userId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить пользователя');
        }

        showSuccess('Пользователь успешно удален!');
        loadUsers(); // Перезагружаем список
    } catch (error) {
        console.error('Ошибка удаления:', error);
        showError(error.message);
    }
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    document.body.prepend(errorDiv);
    setTimeout(() => errorDiv.remove(), 3000);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.textContent = message;
    document.body.prepend(successDiv);
    setTimeout(() => successDiv.remove(), 3000);
}