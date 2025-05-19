document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const carId = urlParams.get('id');

    if (!carId) {
        showError('ID автомобиля не указан');
        return;
    }

    loadCarDetails(carId);
});

async function loadCarDetails(carId) {
    try {
        const response = await fetch(`/api/cars/${carId}`);

        if (!response.ok) {
            throw new Error('Автомобиль не найден');
        }

        const car = await response.json();
        renderCarDetails(car);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderCarDetails(car) {
    const container = document.getElementById('car-details-container');
    container.innerHTML = `
        <div class="car-details">
            <img src="https://avatars.mds.yandex.net/i?id=d51cabdbc0229b16a1232b9682bcc2aece7da925-3986577-images-thumbs&n=13" 
                 alt="${car.title}" class="car-image">
            
            <div class="detail-row">
                <div class="detail-label">ID:</div>
                <div class="detail-value">${car.id}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Название:</div>
                <div class="detail-value">${car.title}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Цена:</div>
                <div class="detail-value">${car.price.toFixed(2)} ₽</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Мощность:</div>
                <div class="detail-value">${car.horsePower} л.с.</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Объем двигателя:</div>
                <div class="detail-value">${car.volume} л</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Цвет:</div>
                <div class="detail-value color-value">
                    ${car.color}
                    <div class="color-sample" style="background-color: ${car.color}"></div>
                </div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">ID пользователя:</div>
                <div class="detail-value">${car.userId || 'Не назначен'}</div>
            </div>
            
            <div class="car-actions">
                <a href="edit-car.html?id=${car.id}" class="button edit-btn">
                    <i class="fas fa-edit"></i> Редактировать
                </a>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="index-car.html" class="button back-btn">
                    <i class="fas fa-arrow-left"></i> Назад
                </a>
            </div>
        </div>
    `;

    document.getElementById('delete-btn').addEventListener('click', () => deleteCar(car.id));
}

async function deleteCar(carId) {
    if (!confirm('Вы уверены, что хотите удалить этот автомобиль?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`/api/cars/${carId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить автомобиль');
        }

        showSuccess('Автомобиль успешно удален!');
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
    const container = document.getElementById('car-details-container');
    container.innerHTML = `
        <div class="error-message">
            <p><i class="fas fa-exclamation-circle"></i> ${message}</p>
            <a href="index-car.html" class="button back-btn">
                <i class="fas fa-arrow-left"></i> Вернуться к списку
            </a>
        </div>
    `;
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const container = document.getElementById('car-details-container');
    container.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}