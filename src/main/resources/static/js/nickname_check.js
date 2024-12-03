// 닉네임 중복확인
async function checkNickname() {
    const nickname = document.getElementById('nickname').value.trim();
    const messageElement = document.getElementById('nicknameCheckMessage');
    const nicknamePattern = /^[a-zA-Z0-9가-힣]{2,10}$/;

    if (!nickname) {
        messageElement.textContent = '닉네임을 입력하세요.';
        messageElement.style.color = 'red';
        return false;
    }

    if (!nicknamePattern.test(nickname)) {
        messageElement.textContent = '2자 이상 10자 이하, 한글, 영문자 또는 숫자만 포함.';
        messageElement.style.color = 'red';
        return false;
    }

    try {
        const response = await fetch('/api/checkNickname', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nickname })
        });

        const isDuplicate = await response.json();

        if (isDuplicate) {
            messageElement.textContent = '이미 사용 중인 닉네임입니다.';
            messageElement.style.color = 'red';
            return false;
        } else {
            messageElement.textContent = '사용 가능한 닉네임입니다.';
            messageElement.style.color = 'green';
            return true;
        }
    } catch (error) {
        console.error('닉네임 중복 확인 오류:', error);
        return false;
    }
}
// 폼 제출 제한
document.getElementById('signupForm').addEventListener('submit', async function(event) {
    const isNicknameValid = await checkNickname();
    if (!isNicknameValid) {
        event.preventDefault();
        alert('닉네임을 확인해주세요');
    }
});