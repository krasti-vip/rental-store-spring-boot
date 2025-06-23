document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const cardId = urlParams.get('id');

    if (!cardId) {
        showError('ID карты не указан');
        return;
    }

    loadBankCard(cardId);
});

async function loadBankCard(cardId) {
    try {
        const response = await fetch(`http://localhost:7875/api/bankcards/${cardId}`);

        if (!response.ok) {
            throw new Error('Карта не найдена');
        }

        const card = await response.json();
        renderEditForm(card);
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showError(error.message);
    }
}

function renderEditForm(card) {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="edit-card-form">
            <input type="hidden" id="card-id" value="${card.id}">
            
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId" value="${card.userId}" required>
            </div>
            
            <div class="form-group">
                <label for="numberCard"><i class="fas fa-credit-card"></i> Номер карты:</label>
                <input type="text" id="numberCard" value="${card.numberCard}" required>
            </div>
            
            <div class="form-group">
                <label for="expirationDate"><i class="far fa-calendar-alt"></i> Срок действия:</label>
                <input type="text" id="expirationDate" value="${card.expirationDate}" placeholder="MM/YY" required>
            </div>
            
            <div class="form-group">
                <label for="secretCode"><i class="fas fa-lock"></i> CVV код:</label>
                <input type="number" id="secretCode" value="${card.secretCode}" required>
            </div>
            
            <div class="actions">
                <button type="submit" class="button save-btn">
                    <i class="fas fa-save"></i> Сохранить
                </button>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="bankcard-details.html?id=${card.id}" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    document.getElementById('edit-card-form').addEventListener('submit', (e) => saveCard(e, card.id));
    document.getElementById('delete-btn').addEventListener('click', () => deleteCard(card.id));
}

async function saveCard(e, cardId) {
    e.preventDefault();

    const saveBtn = document.querySelector('.save-btn');
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Сохранение...';

    try {
        const cardData = {
            id: cardId,
            userId: document.getElementById('userId').value,
            numberCard: document.getElementById('numberCard').value,
            expirationDate: document.getElementById('expirationDate').value,
            secretCode: document.getElementById('secretCode').value
        };

        const response = await fetch(`http://localhost:7875/api/bankcards/${cardId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cardData)
        });

        if (!response.ok) {
            throw new Error('Ошибка сохранения');
        }

        showSuccess('Изменения успешно сохранены!');
        setTimeout(() => {
            window.location.href = `bankcard-details.html?id=${cardId}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        saveBtn.disabled = false;
        saveBtn.innerHTML = '<i class="fas fa-save"></i> Сохранить';
    }
}

async function deleteCard(cardId) {
    if (!confirm('Вы уверены, что хотите удалить эту карту?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

    try {
        const response = await fetch(`http://localhost:7875/api/bankcards/${cardId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления');
        }

        showSuccess('Карта успешно удалена!');
        setTimeout(() => {
            window.location.href = 'index-bank-card.html';
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
        deleteBtn.disabled = false;
        deleteBtn.innerHTML = '<i class="fas fa-trash-alt"></i> Удалить';
    }
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${message}`;

    const container = document.getElementById('form-container');
    container.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const container = document.getElementById('form-container');
    container.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}