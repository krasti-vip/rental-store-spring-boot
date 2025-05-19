document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const rentalId = urlParams.get('id');

    if (!rentalId) {
        showError('ID аренды не указан');
        return;
    }

    loadRental(rentalId);
});

async function loadRental(rentalId) {
    try {
        const response = await fetch(`/api/rentals/${rentalId}`);

        if (!response.ok) {
            throw new Error('Аренда не найдена');
        }

        const rental = await response.json();
        renderEditForm(rental);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderEditForm(rental) {
    const startDate = rental.startDate ? new Date(rental.startDate) : null;
    const endDate = rental.endDate ? new Date(rental.endDate) : null;

    const vehicleType = rental.carId ? 'car' :
        rental.bikeId ? 'bike' :
            rental.bicycleId ? 'bicycle' : '';
    const vehicleId = rental.carId || rental.bikeId || rental.bicycleId || '';

    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="edit-rental-form">
            <input type="hidden" id="rental-id" value="${rental.id}">
            
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId" value="${rental.userId || ''}" required>
            </div>
            
            <div class="form-group">
                <label><i class="fas fa-car"></i> Транспорт:</label>
                <div class="vehicle-selector">
                    <select id="vehicleType">
                        <option value="">Выберите тип</option>
                        <option value="car" ${vehicleType === 'car' ? 'selected' : ''}>Автомобиль</option>
                        <option value="bike" ${vehicleType === 'bike' ? 'selected' : ''}>Мотоцикл</option>
                        <option value="bicycle" ${vehicleType === 'bicycle' ? 'selected' : ''}>Велосипед</option>
                    </select>
                    <input type="number" id="vehicleId" value="${vehicleId}" ${!vehicleType ? 'disabled' : ''}>
                </div>
            </div>
            
            <div class="form-group">
                <label for="startDate"><i class="fas fa-calendar-alt"></i> Дата начала:</label>
                <div class="datetime-input">
                    <input type="date" id="startDate" value="${startDate ? startDate.toISOString().split('T')[0] : ''}" required>
                    <input type="time" id="startTime" value="${startDate ? startDate.toTimeString().substring(0, 5) : ''}" required>
                </div>
            </div>
            
            <div class="form-group">
                <label for="endDate"><i class="fas fa-calendar-alt"></i> Дата окончания:</label>
                <div class="datetime-input">
                    <input type="date" id="endDate" value="${endDate ? endDate.toISOString().split('T')[0] : ''}">
                    <input type="time" id="endTime" value="${endDate ? endDate.toTimeString().substring(0, 5) : ''}">
                </div>
            </div>
            
            <div class="form-group">
                <label for="rentalAmount"><i class="fas fa-money-bill-wave"></i> Сумма аренды:</label>
                <input type="number" id="rentalAmount" step="0.01" min="0" value="${rental.rentalAmount || ''}" required>
            </div>
            
            <div class="form-group">
                <label for="isPaid"><i class="fas fa-check-circle"></i> Оплачено:</label>
                <select id="isPaid">
                    <option value="false" ${!rental.isPaid ? 'selected' : ''}>Нет</option>
                    <option value="true" ${rental.isPaid ? 'selected' : ''}>Да</option>
                </select>
            </div>
            
            <div class="actions">
                <button type="submit" class="button save-btn">
                    <i class="fas fa-save"></i> Сохранить
                </button>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="rental-details.html?id=${rental.id}" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('vehicleType').addEventListener('change', (e) => {
        document.getElementById('vehicleId').disabled = !e.target.value;
    });

    document.getElementById('edit-rental-form').addEventListener('submit', (e) => saveRental(e, rental.id));
    document.getElementById('delete-btn').addEventListener('click', () => deleteRental(rental.id));
}

async function saveRental(e, rentalId) {
    e.preventDefault();

    const saveBtn = document.querySelector('.save-btn');
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const vehicleType = document.getElementById('vehicleType').value;
        const vehicleId = document.getElementById('vehicleId').value;

        const rentalData = {
            id: rentalId,
            userId: parseInt(document.getElementById('userId').value),
            carId: vehicleType === 'car' ? parseInt(vehicleId) : null,
            bikeId: vehicleType === 'bike' ? parseInt(vehicleId) : null,
            bicycleId: vehicleType === 'bicycle' ? parseInt(vehicleId) : null,
            startDate: combineDateTime(
                document.getElementById('startDate').value,
                document.getElementById('startTime').value
            ),
            endDate: combineDateTime(
                document.getElementById('endDate').value,
                document.getElementById('endTime').value
            ),
            rentalAmount: parseFloat(document.getElementById('rentalAmount').value),
            isPaid: document.getElementById('isPaid').value === 'true'
        };

        const response = await fetch(`/api/rentals/${rentalId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(rentalData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка сохранения');
        }

        showSuccess('Изменения сохранены!');
        setTimeout(() => {
            window.location.href = `rental-details.html?id=${rentalId}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        saveBtn.disabled = false;
        saveBtn.innerHTML = '<i class="fas fa-save"></i> Сохранить';
    }
}

async function deleteRental(rentalId) {
    if (!confirm('Вы уверены, что хотите удалить эту аренду?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`/api/rentals/${rentalId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления');
        }

        showSuccess('Аренда удалена!');
        setTimeout(() => {
            window.location.href = 'index-rental.js';
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
        deleteBtn.disabled = false;
        deleteBtn.innerHTML = '<i class="fas fa-trash-alt"></i> Удалить';
    }
}

function combineDateTime(dateStr, timeStr) {
    if (!dateStr) return null;
    if (!timeStr) timeStr = '00:00';
    return `${dateStr}T${timeStr}:00`;
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${message}`;

    const form = document.getElementById('edit-rental-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('edit-rental-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}