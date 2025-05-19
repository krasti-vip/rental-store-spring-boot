document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const cardId = urlParams.get('id');

    if (!cardId) {
        showError('ID карты не указан');
        return;
    }

    loadBankCardDetails(cardId);
});

async function loadBankCardDetails(cardId) {
    try {
        const response = await fetch(`/api/bankcards/${cardId}`);

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Карта не найдена');
        }

        const card = await response.json();
        renderBankCardDetails(card);
    } catch (error) {
        console.error('Ошибка загрузки карты:', error);
        showError(`Ошибка загрузки: ${error.message}`);
    }
}

function renderBankCardDetails(card) {
    const container = document.getElementById('card-details-container');
    container.innerHTML = `
        <div class="card-details">
            <h2>Карта #${card.id}</h2>
            <img src="https://blizko.by/system/notes/imagefbs/000/038/881/original/karta_%285%29.jpg?1668070571" 
                 alt="Bank Card" class="card-image">
            
            <div class="detail-row">
                <div class="detail-label">ID пользователя:</div>
                <div class="detail-value">${card.userId}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Номер карты:</div>
                <div class="detail-value">${formatCardNumber(card.numberCard)}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">Срок действия:</div>
                <div class="detail-value">${card.expirationDate}</div>
            </div>
            
            <div class="detail-row">
                <div class="detail-label">CVV код:</div>
                <div class="detail-value">${card.secretCode}</div>
            </div>
            
            <div class="card-actions">
                <a href="edit-bankcard.html?id=${card.id}" class="button submit-btn">
                    <i class="fas fa-edit"></i> Редактировать
                </a>
                <button type="button" id="delete-btn" class="button delete-btn">
                    <i class="fas fa-trash-alt"></i> Удалить
                </button>
                <a href="index-bank-card.html" class="button back-link">
                    <i class="fas fa-arrow-left"></i> Назад
                </a>
            </div>
        </div>
    `;

    document.getElementById('delete-btn').addEventListener('click', () => deleteBankCard(card.id));
}

function formatCardNumber(number) {
    if (!number) return '';
    // Форматирование номера карты как XXXX XXXX XXXX XXXX
    return number.replace(/(\d{4})(?=\d)/g, '$1 ');
}

async function deleteBankCard(cardId) {
    if (!confirm('Вы уверены, что хотите удалить эту карту?\nЭто действие нельзя отменить.')) {
        return;
    }

    const deleteBtn = document.getElementById('delete-btn');
    try {
        deleteBtn.disabled = true;
        deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Удаление...';

        const response = await fetch(`/api/bankcards/${cardId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Не удалось удалить карту');
        }

        showSuccess('Карта успешно удалена!');
        setTimeout(() => {
            window.location.href = 'index-bank-card.html';
        }, 1500);
    } catch (error) {
        console.error('Ошибка удаления:', error);
        showError(error.message);
        deleteBtn.disabled = false;
        deleteBtn.innerHTML = '<i class="fas fa-trash-alt"></i> Удалить';
    }
}

function showError(message) {
    const container = document.getElementById('card-details-container');
    container.innerHTML = `
        <div class="error-message">
            <p>${message}</p>
            <a href="index-bank-card.html" class="button back-link">
                <i class="fas fa-arrow-left"></i> Вернуться к списку
            </a>
        </div>
    `;
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `
        <p><i class="fas fa-check-circle"></i> ${message}</p>
    `;

    const container = document.getElementById('card-details-container');
    container.prepend(successDiv);

    // Автоматическое скрытие через 3 секунды
    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}