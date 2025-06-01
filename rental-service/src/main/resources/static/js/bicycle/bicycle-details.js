document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const bicycleId = urlParams.get('id');

    if (!bicycleId) {
        showError('ID велосипеда не указан');
        return;
    }

    loadBicycleDetails(bicycleId);
});

async function loadBicycleDetails(bicycleId) {
    try {
        const response = await fetch(`http://localhost:7878/api/bicycles/${bicycleId}`);

        if (!response.ok) {
            throw new Error('Велосипед не найден');
        }

        const bicycle = await response.json();
        renderBicycleDetails(bicycle);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderBicycleDetails(bicycle) {
    const container = document.getElementById('bicycle-details-container');
    container.innerHTML = `
        <div class="bicycle-details">
            <img src="https://media.au.ru/imgs/fd46d027b050c29de6cc7f2072a82c70/kuplyu-velosiped-1-17647917.jpg" 
                 alt="${bicycle.model}" class="bicycle-image">
            
            <div class="detail-row">
                <div class="detail-label">ID:</div>
                <div class="detail-value">${bicycle.id}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Модель:</div>
                <div class="detail-value">${bicycle.model}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Цена:</div>
                <div class="detail-value">${bicycle.price.toFixed(2)} ₽</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Цвет:</div>
                <div class="detail-value color-value">
                    ${bicycle.color}
                    <div class="color-sample" style="background-color: ${bicycle.color}"></div>
                </div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">ID пользователя:</div>
                <div class="detail-value">${bicycle.userId || 'Не назначен'}</div>
            </div>
            
            <div class="bicycle-actions">
                <a href="edit-bicycle.html?id=${bicycle.id}" class="button edit-btn">
                    <i class="fas fa-edit"></i> Редактировать
                </a>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="index-bicycle.html" class="button back-btn">
                    <i class="fas fa-arrow-left"></i> Назад
                </a>
            </div>
        </div>
    `;

    document.getElementById('delete-btn').addEventListener('click', () => deleteBicycle(bicycle.id));
}

async function deleteBicycle(bicycleId) {
    if (!confirm('Вы уверены, что хотите удалить этот велосипед?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`http://localhost:7878/api/bicycles/${bicycleId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить велосипед');
        }

        showSuccess('Велосипед успешно удален!');
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
    const container = document.getElementById('bicycle-details-container');
    container.innerHTML = `
        <div class="error-message">
            <p><i class="fas fa-exclamation-circle"></i> ${message}</p>
            <a href="index-bicycle.html" class="button back-btn">
                <i class="fas fa-arrow-left"></i> Вернуться к списку
            </a>
        </div>
    `;
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const container = document.getElementById('bicycle-details-container');
    container.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}