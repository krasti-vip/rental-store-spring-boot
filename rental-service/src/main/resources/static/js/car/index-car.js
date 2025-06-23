document.addEventListener('DOMContentLoaded', () => {
    loadCars();
});

async function loadCars() {
    try {
        const response = await fetch('http://localhost:7876/api/cars');
        const cars = await response.json();
        renderCars(cars);
    } catch (error) {
        console.error('Ошибка загрузки автомобилей:', error);
        showError('Произошла ошибка при загрузке автомобилей');
    }
}

function renderCars(cars) {
    const container = document.getElementById('cars-container');
    container.innerHTML = '';

    cars.forEach(car => {
        const carElement = document.createElement('div');
        carElement.className = 'car-card';
        carElement.innerHTML = `
            <img src="https://sun9-48.userapi.com/s/v1/ig2/a4eaNaFDs7TV5JeB5jLRWoJWJ7ge8yWXop1pNvfFtjm3BIzCPRrYoTH8ObrvH9TWW5EgtLOCpbxRL7kNxOchcV88.jpg?quality=95&as=32x21,48x32,72x48,108x72,160x107,240x160,360x240,480x320,540x360,640x426,720x480,1080x720,1280x853,1440x960,2560x1706&from=bu&u=dwEzUcER5fM87idWU2eAs6jYaaIvt7FLKRtc1fREqnQ&cs=807x538" 
                 alt="${car.title}" class="car-image">
            <div class="car-info">
                <h3>${car.title}</h3>
                <div class="car-specs">
                    <div class="car-spec">
                        <span class="spec-label">Мощность:</span>
                        <span class="spec-value">${car.horsePower} л.с.</span>
                    </div>
                    <div class="car-spec">
                        <span class="spec-label">Объем:</span>
                        <span class="spec-value">${car.volume} л</span>
                    </div>
                </div>
                <div class="car-price">${car.price.toFixed(2)} ₽</div>
                <div class="car-color">${car.color}</div>
                <div class="car-actions">
                    <a href="car-details.html?id=${car.id}" class="button view-btn">
                        <i class="fas fa-eye"></i> Просмотр
                    </a>
                   
                    <button class="button delete-btn" data-id="${car.id}">
                        <i class="fas fa-trash-alt"></i> Удалить
                    </button>
                </div>
            </div>
        `;
        container.appendChild(carElement);
    });

    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const carId = e.target.closest('button').dataset.id;
            deleteCar(carId);
        });
    });
}

async function deleteCar(carId) {
    if (!confirm('Вы уверены, что хотите удалить этот автомобиль?\nЭто действие нельзя отменить.')) {
        return;
    }

    try {
        const response = await fetch(`http://localhost:7876/api/cars/api/cars/${carId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить автомобиль');
        }

        showSuccess('Автомобиль успешно удален!');
        loadCars(); // Перезагружаем список
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