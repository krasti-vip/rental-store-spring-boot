document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const bikeId = urlParams.get('id');

    if (!bikeId) {
        showError('ID мотоцикла не указан');
        return;
    }

    loadBikeDetails(bikeId);
});

async function loadBikeDetails(bikeId) {
    try {
        const response = await fetch(`http://localhost:7877/api/bikes/${bikeId}`);

        if (!response.ok) {
            throw new Error('Мотоцикл не найден');
        }

        const bike = await response.json();
        renderBikeDetails(bike);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderBikeDetails(bike) {
    const container = document.getElementById('bike-details-container');
    container.innerHTML = `
        <div class="bike-details">
            <img src="https://i.pinimg.com/736x/8f/4e/cd/8f4ecd938b8fdd349dddd9c9cfa80633.jpg" 
                 alt="${bike.name}" class="bike-image">
            
            <div class="detail-row">
                <div class="detail-label">ID:</div>
                <div class="detail-value">${bike.id}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Название:</div>
                <div class="detail-value">${bike.name}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Цена:</div>
                <div class="detail-value">${bike.price.toFixed(2)} ₽</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Мощность:</div>
                <div class="detail-value">${bike.horsePower} л.с.</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Объем двигателя:</div>
                <div class="detail-value">${bike.volume} л</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">ID пользователя:</div>
                <div class="detail-value">${bike.userId || 'Не назначен'}</div>
            </div>
            
            <div class="bike-actions">
                <a href="edit-bike.html?id=${bike.id}" class="button edit-btn">
                    <i class="fas fa-edit"></i> Редактировать
                </a>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="index-bike.html" class="button back-btn">
                    <i class="fas fa-arrow-left"></i> Назад
                </a>
            </div>
        </div>
    `;

    document.getElementById('delete-btn').addEventListener('click', () => deleteBike(bike.id));
}

async function deleteBike(bikeId) {
    if (!confirm('Вы уверены, что хотите удалить этот мотоцикл?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`http://localhost:7877/api/bikes/${bikeId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить мотоцикл');
        }

        showSuccess('Мотоцикл успешно удален!');
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
    const container = document.getElementById('bike-details-container');
    container.innerHTML = `
        <div class="error-message">
            <p><i class="fas fa-exclamation-circle"></i> ${message}</p>
            <a href="index-bike.html" class="button back-btn">
                <i class="fas fa-arrow-left"></i> Вернуться к списку
            </a>
        </div>
    `;
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const container = document.getElementById('bike-details-container');
    container.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}