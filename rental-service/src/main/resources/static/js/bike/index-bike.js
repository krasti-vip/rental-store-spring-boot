document.addEventListener('DOMContentLoaded', () => {
    loadBikes();
});

async function loadBikes() {
    try {
        const response = await fetch('http://localhost:7877/api/bikes');
        const bikes = await response.json();
        renderBikes(bikes);
    } catch (error) {
        console.error('Ошибка загрузки мотоциклов:', error);
        showError('Произошла ошибка при загрузке мотоциклов');
    }
}

function renderBikes(bikes) {
    const container = document.getElementById('bikes-container');
    container.innerHTML = '';

    bikes.forEach(bike => {
        const bikeElement = document.createElement('div');
        bikeElement.className = 'bike-card';
        bikeElement.innerHTML = `
            <img src="https://i.pinimg.com/originals/e5/5b/a1/e55ba1c95fd457baf566e49da8a55204.jpg" 
                 alt="${bike.name}" class="bike-image">
            <div class="bike-info">
                <h3>${bike.name}</h3>
                <div class="bike-specs">
                    <div class="bike-spec">
                        <span class="spec-label">Мощность:</span>
                        <span class="spec-value">${bike.horsePower} л.с.</span>
                    </div>
                    <div class="bike-spec">
                        <span class="spec-label">Объем:</span>
                        <span class="spec-value">${bike.volume} л</span>
                    </div>
                </div>
                <div class="bike-price">${bike.price.toFixed(2)} ₽</div>
                <div class="bike-actions">
                    <a href="bike-details.html?id=${bike.id}" class="button view-btn">
                        <i class="fas fa-eye"></i> Просмотр
                    </a>
                 
                    <button class="button delete-btn" data-id="${bike.id}">
                        <i class="fas fa-trash-alt"></i> Удалить
                    </button>
                </div>
            </div>
        `;
        container.appendChild(bikeElement);
    });

    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const bikeId = e.target.closest('button').dataset.id;
            deleteBike(bikeId);
        });
    });
}

async function deleteBike(bikeId) {
    if (!confirm('Вы уверены, что хотите удалить этот мотоцикл?\nЭто действие нельзя отменить.')) {
        return;
    }

    try {
        const response = await fetch(`http://localhost:7877/api/bikes/${bikeId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить мотоцикл');
        }

        showSuccess('Мотоцикл успешно удален!');
        loadBikes(); // Перезагружаем список
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