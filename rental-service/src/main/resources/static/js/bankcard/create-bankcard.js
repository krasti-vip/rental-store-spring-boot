document.addEventListener('DOMContentLoaded', () => {
    renderCreateForm();

    // Инициализация маски для номера карты
    document.getElementById('numberCard')?.addEventListener('input', formatCardNumber);
    document.getElementById('expirationDate')?.addEventListener('input', formatExpirationDate);
});

function renderCreateForm() {
    const container = document.getElementById('form-container');
    container.innerHTML = `
        <form id="create-card-form">
            <div class="card-preview">
                <div>НОВАЯ КАРТА</div>
                <div class="card-number-preview" id="card-number-preview">•••• •••• •••• ••••</div>
                <div class="card-details-preview">
                    <span id="expiration-preview">••/••</span>
                    <span id="cvv-preview">•••</span>
                </div>
            </div>
            
            <div class="form-group">
                <label for="userId"><i class="fas fa-user"></i> ID пользователя:</label>
                <input type="number" id="userId" required>
            </div>
            
            <div class="form-group">
                <label for="numberCard"><i class="fas fa-credit-card"></i> Номер карты:</label>
                <input type="text" id="numberCard" maxlength="19" placeholder="XXXX XXXX XXXX XXXX" required>
            </div>
            
            <div class="form-group">
                <label for="expirationDate"><i class="far fa-calendar-alt"></i> Срок действия:</label>
                <input type="text" id="expirationDate" maxlength="5" placeholder="MM/YY" required>
            </div>
            
            <div class="form-group">
                <label for="secretCode"><i class="fas fa-lock"></i> CVV код:</label>
                <input type="number" id="secretCode" maxlength="3" placeholder="123" required>
            </div>
            
            <div class="actions">
                <button type="submit" class="button create-btn">
                    <i class="fas fa-plus-circle"></i> Добавить карту
                </button>
                <a href="index-bank-card.html" class="button cancel-btn">
                    <i class="fas fa-times"></i> Отмена
                </a>
            </div>
        </form>
    `;

    // Добавляем обработчики для динамического обновления превью
    document.getElementById('numberCard').addEventListener('input', updateCardPreview);
    document.getElementById('expirationDate').addEventListener('input', updateCardPreview);
    document.getElementById('secretCode').addEventListener('input', updateCardPreview);
    document.getElementById('create-card-form').addEventListener('submit', createCard);
}

function formatCardNumber(e) {
    let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
    let formatted = '';

    for (let i = 0; i < value.length; i++) {
        if (i > 0 && i % 4 === 0) formatted += ' ';
        formatted += value[i];
    }

    e.target.value = formatted;
    updateCardPreview();
}

function formatExpirationDate(e) {
    let value = e.target.value.replace(/[^0-9]/g, '');

    if (value.length > 2) {
        value = value.substring(0, 2) + '/' + value.substring(2, 4);
    }

    e.target.value = value.substring(0, 5);
    updateCardPreview();
}

function updateCardPreview() {
    const numberInput = document.getElementById('numberCard');
    const expirationInput = document.getElementById('expirationDate');
    const cvvInput = document.getElementById('secretCode');

    // Обновление номера карты
    const cardNumber = numberInput.value || '•••• •••• •••• ••••';
    document.getElementById('card-number-preview').textContent = cardNumber;

    // Обновление срока действия
    const expiration = expirationInput.value || '••/••';
    document.getElementById('expiration-preview').textContent = expiration;

    // Обновление CVV
    const cvv = cvvInput.value ? '•'.repeat(cvvInput.value.length) : '•••';
    document.getElementById('cvv-preview').textContent = cvv;
}

async function createCard(e) {
    e.preventDefault();

    const createBtn = document.querySelector('.create-btn');
    createBtn.disabled = true;
    createBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Добавление...';

    try {
        const cardData = {
            userId: document.getElementById('userId').value,
            numberCard: document.getElementById('numberCard').value.replace(/\s+/g, ''),
            expirationDate: document.getElementById('expirationDate').value,
            secretCode: document.getElementById('secretCode').value
        };

        const response = await fetch('/api/bankcards', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cardData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.message || 'Ошибка создания карты');
        }

        const result = await response.json();
        showSuccess('Карта успешно добавлена!');
        setTimeout(() => {
            window.location.href = `bankcard-details.html?id=${result.id}`;
        }, 1500);
    } catch (error) {
        console.error('Ошибка:', error);
        showError(error.message);
    } finally {
        createBtn.disabled = false;
        createBtn.innerHTML = '<i class="fas fa-plus-circle"></i> Добавить карту';
    }
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${message}`;

    const form = document.getElementById('create-card-form');
    form.prepend(errorDiv);
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;

    const form = document.getElementById('create-card-form');
    form.prepend(successDiv);

    setTimeout(() => {
        successDiv.style.opacity = '0';
        setTimeout(() => successDiv.remove(), 300);
    }, 3000);
}