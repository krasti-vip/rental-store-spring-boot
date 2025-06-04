document.addEventListener('DOMContentLoaded', () => {
    loadRentals();
});

async function loadRentals() {
    try {
        const response = await fetch('http://localhost:7874/api/rentals');
        const rentals = await response.json();
        renderRentals(rentals);
    } catch (error) {
        console.error('Ошибка загрузки аренд:', error);
        showError('Произошла ошибка при загрузке аренд');
    }
}

function renderRentals(rentals) {
    const container = document.getElementById('rentals-container');
    container.innerHTML = '';

    rentals.forEach(rental => {
        const rentalElement = document.createElement('div');
        rentalElement.className = 'rental-card';

        const statusClass = rental.endDate ? 'status-completed' :
            rental.isPaid ? 'status-active' : 'status-unpaid';
        const statusText = rental.endDate ? 'Завершена' :
            rental.isPaid ? 'Активна' : 'Не оплачена';

        rentalElement.innerHTML = `
            <div class="rental-header">
                <div class="rental-id">Аренда #${rental.id}</div>
                <div class="rental-status ${statusClass}">${statusText}</div>
            </div>
            
            <div class="rental-details">
                <div class="rental-detail">
                    <div class="detail-label">Пользователь:</div>
                    <div class="detail-value">${rental.userId || 'Не указан'}</div>
                </div>
                
                <div class="rental-detail">
                    <div class="detail-label">Транспорт:</div>
                    <div class="detail-value">
                        ${rental.carId ? 'Автомобиль #' + rental.carId : ''}
                        ${rental.bikeId ? 'Мотоцикл #' + rental.bikeId : ''}
                        ${rental.bicycleId ? 'Велосипед #' + rental.bicycleId : ''}
                    </div>
                </div>
                
                <div class="rental-detail">
                    <div class="detail-label">Начало:</div>
                    <div class="detail-value">${formatDateTime(rental.startDate)}</div>
                </div>
                
                ${rental.endDate ? `
                <div class="rental-detail">
                    <div class="detail-label">Окончание:</div>
                    <div class="detail-value">${formatDateTime(rental.endDate)}</div>
                </div>
                ` : ''}
            </div>
            
            <div class="rental-amount">${rental.rentalAmount.toFixed(2)} ₽</div>
            
            <div class="rental-actions">
                <a href="rental-details.html?id=${rental.id}" class="button view-btn">
                    <i class="fas fa-eye"></i> Просмотр
                </a>
            </div>
        `;
        container.appendChild(rentalElement);
    });

    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const rentalId = e.target.closest('button').dataset.id;
            deleteRental(rentalId);
        });
    });
}

function formatDateTime(dateTime) {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    return date.toLocaleString('ru-RU');
}

async function deleteRental(rentalId) {
    if (!confirm('Вы уверены, что хотите удалить эту аренду?\nЭто действие нельзя отменить.')) {
        return;
    }

    try {
        const response = await fetch(`http://localhost:7874/api/rentals/${rentalId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить аренду');
        }

        showSuccess('Аренда успешно удалена!');
        loadRentals(); // Перезагружаем список
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