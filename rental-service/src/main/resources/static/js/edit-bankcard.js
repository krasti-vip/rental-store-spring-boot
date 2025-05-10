document.addEventListener('DOMContentLoaded', async () => {
    const cardId = new URLSearchParams(window.location.search).get('id');
    document.getElementById('card-id').value = cardId;

    // Загрузка данных карты
    try {
        const response = await fetch(`/api/bankcards/${cardId}`);
        if (!response.ok) throw new Error('Карта не найдена');

        const card = await response.json();
        document.getElementById('edit-userId').value = card.userId;
        document.getElementById('edit-numberCard').value = card.numberCard;
        document.getElementById('edit-expirationDate').value = card.expirationDate;
        document.getElementById('edit-secretCode').value = card.secretCode;
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        alert('Не удалось загрузить данные карты');
        window.location.href = 'indexBankCard.html';
    }

    // Обработка формы
    document.getElementById('edit-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const updates = {
            userId: parseInt(document.getElementById('edit-userId').value),
            numberCard: document.getElementById('edit-numberCard').value,
            expirationDate: document.getElementById('edit-expirationDate').value,
            secretCode: parseInt(document.getElementById('edit-secretCode').value)
        };

        try {
            const response = await fetch(`/api/bankcards/${cardId}`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updates)
            });

            if (response.ok) {
                window.location.href = `bankcard-details.html?id=${cardId}`;
            } else {
                throw new Error('Ошибка обновления');
            }
        } catch (error) {
            console.error('Ошибка:', error);
            alert('Не удалось обновить карту');
        }
    });

    // Кнопка удаления
    document.getElementById('delete-btn').addEventListener('click', async () => {
        if (confirm('Вы уверены, что хотите удалить эту карту?')) {
            try {
                const response = await fetch(`/api/bankcards/${cardId}`, {
                    method: 'DELETE'
                });
                if (response.ok) window.location.href = 'index-bank-card.html';
            } catch (error) {
                console.error('Ошибка удаления:', error);
            }
        }
    });

    // Кнопка отмены
    document.getElementById('cancel-btn').addEventListener('click', () => {
        window.location.href = `bankcard-details.html?id=${cardId}`;
    });
});