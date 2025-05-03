document.getElementById('create-bankcard-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = {
        userId: parseInt(document.getElementById('userId').value),
        numberCard: document.getElementById('numberCard').value,
        expirationDate: document.getElementById('expirationDate').value,
        secretCode: parseInt(document.getElementById('secretCode').value)
    };

    try {
        const response = await fetch('/api/bankcards', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            window.location.href = 'index-bank-card.html';
        } else {
            throw new Error('Ошибка создания карты');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Произошла ошибка при создании карты');
    }
});