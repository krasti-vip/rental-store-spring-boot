document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const cardId = urlParams.get('id');
    loadBankCardDetails(cardId);
});

async function loadBankCardDetails(cardId) {
    try {
        const response = await fetch(`/api/bankcards/${cardId}`);
        const card = await response.json();
        renderBankCardDetails(card);
    } catch (error) {
        console.error('Ошибка загрузки карты:', error);
        alert('Произошла ошибка при загрузке данных карты');
    }
}

function renderBankCardDetails(card) {
    const container = document.getElementById('card-details');
    container.innerHTML = `
        <img src="https://i0.wp.com/www.bestprepaiddebitcards.com/wp-content/uploads/2013/04/girl-holding-debit-card-in-outstretched-hand-sm.jpg"
             alt="Bank Card Image">
        <p><strong>ID пользователя:</strong> <span>${card.userId}</span></p>
        <p><strong>Номер карты:</strong> <span>${card.numberCard}</span></p>
        <p><strong>Срок действия:</strong> <span>${card.expirationDate}</span></p>
        <p><strong>Секретный код:</strong> <span>${card.secretCode}</span></p>
        <div class="actions">
            <a href="edit-bankcard.html?id=${card.id}">Редактировать</a>
            <button onclick="deleteBankCard(${card.id})">Удалить</button>
        </div>
    `;
}

async function deleteBankCard(cardId) {
    if (confirm('Вы уверены, что хотите удалить эту карту?')) {
        try {
            const response = await fetch(`/api/bankcards/${cardId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                window.location.href = 'index-bank-card.html';
            }
        } catch (error) {
            console.error('Ошибка удаления карты:', error);
            alert('Произошла ошибка при удалении карты');
        }
    }
}