
document.addEventListener('DOMContentLoaded', () => {
    loadBicycles();
});

async function loadBicycles() {
    try {
        const response = await fetch('http://localhost:7878/api/bicycles');
        const bicycles = await response.json();
        renderBicycles(bicycles);
    } catch (error) {
        console.error('Ошибка загрузки велосипедов:', error);
        showError('Произошла ошибка при загрузке велосипедов');
    }
}

function renderBicycles(bicycles) {
    const container = document.getElementById('bicycles-container');
    container.innerHTML = '';

    bicycles.forEach(bicycle => {
        const bicycleElement = document.createElement('div');
        bicycleElement.className = 'bicycle-card';
        bicycleElement.innerHTML = `
            <img src="https://avatars.mds.yandex.net/i?id=da4c58fc92d1b88ea25d3539f864136a_l-5340058-images-thumbs&n=13" 
                 alt="${bicycle.model}" class="bicycle-image">
            <div class="bicycle-info">
                <h3>${bicycle.model}</h3>
                <div class="bicycle-price">${bicycle.price.toFixed(2)} ₽</div>
                <div class="bicycle-color">${bicycle.color}</div>
                <div class="bicycle-actions">
                    <a href="bicycle-details.html?id=${bicycle.id}" class="button view-btn">
                        <i class="fas fa-eye"></i> Просмотр
                    </a>
                
                    <button class="button delete-btn" data-id="${bicycle.id}">
                        <i class="fas fa-trash-alt"></i> Удалить
                    </button>
                </div>
            </div>
        `;
        container.appendChild(bicycleElement);
    });

    // Добавляем обработчики для кнопок удаления
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const bicycleId = e.target.closest('button').dataset.id;
            deleteBicycle(bicycleId);
        });
    });
}

async function deleteBicycle(bicycleId) {
    if (!confirm('Вы уверены, что хотите удалить этот велосипед?\nЭто действие нельзя отменить.')) {
        return;
    }

    try {
        const response = await fetch(`http://localhost:7878/api/bicycles/${bicycleId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить велосипед');
        }

        showSuccess('Велосипед успешно удален!');
        loadBicycles(); // Перезагружаем список
    } catch (error) {
        console.error('Ошибка удаления:', error);
        showError(error.message);
    }
}

function showError(message) {
    alert(message); // Можно заменить на более красивый вывод
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.textContent = message;

    document.body.prepend(successDiv);
    setTimeout(() => successDiv.remove(), 3000);
}