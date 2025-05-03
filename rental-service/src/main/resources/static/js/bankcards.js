document.addEventListener('DOMContentLoaded', () => {
    loadBankCards();
});

async function loadBankCards() {
    try {
        const response = await fetch('/api/bankcards');
        const cards = await response.json();
        renderBankCards(cards);
    } catch (error) {
        console.error('Ошибка загрузки карт:', error);
        alert('Произошла ошибка при загрузке карт');
    }
}

function renderBankCards(cards) {
    const container = document.getElementById('cards-container');
    container.innerHTML = '';

    cards.forEach(card => {
        const cardElement = document.createElement('div');
        cardElement.className = 'bankcard-card';
        cardElement.innerHTML = `
            <img src="https://blizko.by/system/notes/imagefbs/000/038/881/original/karta_%285%29.jpg?1668070571" 
                 alt="Bank Card Image">
            <h3>${card.id}</h3>
            <p><strong>User ID:</strong> <span>${card.userId}</span></p>
            <p><strong>Номер карты:</strong> <span>${card.numberCard}</span></p>
            <p><strong>Срок годности:</strong> <span>${card.expirationDate}</span></p>
            <p><strong>Трехзначный код:</strong> <span>${card.secretCode}</span></p>
            <a href="bankcard-details.html?id=${card.id}">Выбрать эту карту!</a>
        `;
        container.appendChild(cardElement);
    });
}