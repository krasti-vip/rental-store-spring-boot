document.addEventListener('DOMContentLoaded', () => {
    renderCreateForm();
});

function renderCreateForm() {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="create-rental-form">
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId" required>
            </div>
            
            <div class="form-group">
                <label><i class="fas fa-car"></i> Транспорт:</label>
                <div class="vehicle-selector">
                    <select id="vehicleType">
                        <option value="">Выберите тип</option>
                        <option value="car">Автомобиль</option>
                        <option value="bike">Мотоцикл</option>
                        <option value="bicycle">Велосипед</option>
                    </select>
                    <input type="number" id="vehicleId" placeholder="ID транспорта" disabled>
                </div>
            </div>
            
            <div class="form-group">
                <label for="startDate"><i class="fas fa-calendar-alt"></i> Дата начала:</label>
                <div class="datetime-input">
                    <input type="date" id="startDate" required>
                    <input type="time" id="startTime" required>
                </div>
            </div>
            
            <div class="form-group">
                <label for="endDate"><i class="fas fa-calendar-alt"></i> Дата окончания (необязательно):</label>
                <div class="datetime-input">
                    <input type="date" id="endDate">
                    <input type="time" id="endTime">
                </div>
            </div>
            
            <div class="form-group">
                <label for="rentalAmount"><i class="fas fa-money-bill-wave"></i> Сумма аренды:</label>
                <input type="number" id="rentalAmount" step="0.01" min="0" required>
            </div>
            
            <div class="form-group">
                <label for="isPaid"><i class="fas fa-check-circle"></i> Оплачено:</label>
                <select id="isPaid">
                    <option value="false">Нет</option>
                    <option value="true">Да</option>
                </select>
            </div>
            
            <div class="actions">
                <button type="submit" class="button create-btn">
                    <i class="fas fa-plus"></i> Добавить
                </button>
                <a href="index-rental.html" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('vehicleType').addEventListener('change', (e) => {
        document.getElementById('vehicleId').disabled = !e.target.value;
    });

    document.getElementById('create-rental-form').addEventListener('submit', createRental);
}

async function createRental(e) {
    e.preventDefault();

    const createBtn = document.querySelector('.create-btn');
    createBtn.disabled = true;
    createBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const vehicleType = document.getElementById('vehicleType').value;
        const vehicleId = document.getElementById('vehicleId').value;

        const rentalData = {
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

        const response = await fetch('/api/rentals', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(rentalData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка создания аренды');
        }

        const result = await response.json();
        showSuccess('Аренда успешно создана!');
        setTimeout(() => {
            window.location.href = `rental-details.html?id=${result.id}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        createBtn.disabled = false;
        createBtn.innerHTML = '<i class="fas fa-plus"></i> Добавить';
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

    const form = document.getElementById('create-rental-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('create-rental-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}