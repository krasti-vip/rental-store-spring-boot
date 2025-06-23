document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const rentalId = urlParams.get('id');

    if (!rentalId) {
        showError('ID аренды не указан');
        return;
    }

    loadRentalDetails(rentalId);
});

async function loadRentalDetails(rentalId) {
    try {
        const response = await fetch(`http://localhost:7874/api/rentals/${rentalId}`);

        if (!response.ok) {
            throw new Error('Аренда не найдена');
        }

        const rental = await response.json();
        renderRentalDetails(rental);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderRentalDetails(rental) {
    const statusClass = rental.endDate ? 'status-completed' :
        rental.isPaid ? 'status-active' : 'status-unpaid';
    const statusText = rental.endDate ? 'Завершена' :
        rental.isPaid ? 'Активна' : 'Не оплачена';

    const vehicleType = rental.carId ? 'Автомобиль' :
        rental.bikeId ? 'Мотоцикл' :
            rental.bicycleId ? 'Велосипед' : 'Не указан';
    const vehicleId = rental.carId || rental.bikeId || rental.bicycleId || '';

    const startDate = rental.startDate ? new Date(rental.startDate) : null;
    const endDate = rental.endDate ? new Date(rental.endDate) : null;

    const container = document.getElementById('rental-details-container');
    container.innerHTML = `
        <div class="rental-details">
            <div class="rental-header">
                <div class="rental-id">Аренда #${rental.id}</div>
                <div class="rental-status ${statusClass}">${statusText}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Пользователь:</div>
                <div class="detail-value">${rental.userId || 'Не указан'}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Транспорт:</div>
                <div class="detail-value">${vehicleType} #${vehicleId}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Дата начала:</div>
                <div class="detail-value">${startDate ? startDate.toLocaleString('ru-RU') : 'Не указана'}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Дата окончания:</div>
                <div class="detail-value">${endDate ? endDate.toLocaleString('ru-RU') : 'Не указана'}</div>
            </div>
            
            <div class="rental-amount">${rental.rentalAmount.toFixed(2)} ₽</div>
            
            <div class="rental-actions">
                <a href="edit-rental.html?id=${rental.id}" class="button edit-btn">
                    <i class="fas fa-edit"></i> Редактировать
                </a>
               
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="index-rental.html" class="button back-btn">
                    <i class="fas fa-arrow-left"></i> Назад
                </a>
            </div>
        </div>
    `;

    document.getElementById('delete-btn').addEventListener('click', () => deleteRental(rental.id));
}

async function deleteRental(rentalId) {
    if (!confirm('Вы уверены, что хотите удалить эту аренду?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`http://localhost:7874/api/rentals/${rentalId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить аренду');
        }

        showSuccess('Аренда успешно удалена!');
        setTimeout(() => {
            window.location.href = 'index-rental.html';
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
        deleteBtn.disabled = false;
        deleteBtn.innerHTML = '<i class="fas fa-trash-alt"></i> Удалить';
    }
}

async function payRental(rentalId) {
    if (!confirm('Подтвердить оплату аренды?')) {
        return;
    }

    const payBtn = document.getElementById('pay-btn');
    payBtn.disabled = true;
    payBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Оплата...';

    try {
        const response = await fetch(`http://localhost:7874/api/rentals/${rentalId}/pay`, {
            method: 'POST'
        });

        if (!response.ok) {
            throw new Error('Не удалось выполнить оплату');
        }

        showSuccess('Аренда успешно оплачена!');
        setTimeout(() => {
            window.location.reload();
        }, 1500);
    } catch (error) {
        console.error('Ошибка оплаты:', error);
        showError(error.message);
        payBtn.disabled = false;
        payBtn.innerHTML = '<i class="fas fa-money-bill-wave"></i> Оплатить';
    }
}

function showError(message) {
    const container = document.getElementById('rental-details-container');
    container.innerHTML = `
        <div class="error-message">
            <p><i class="fas fa-exclamation-circle"></i> ${message}</p>
            <a href="index-rental.html" class="button back-btn">
                <i class="fas fa-arrow-left"></i> Вернуться к списку
            </a>
        </div>
    `;
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const container = document.getElementById('rental-details-container');
    container.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}