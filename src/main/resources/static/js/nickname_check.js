document.addEventListener('DOMContentLoaded', function() {
    const submitButton = document.querySelector('.save_btn');

    submitButton.disabled = true;
    submitButton.style.backgroundColor = '#d3d3d3';
    submitButton.style.color = '#888';
    submitButton.style.cursor = 'not-allowed';
});

// 닉네임 중복확인
async function checkNickname() {
    const nickname = document.getElementById('nickname').value.trim();
    const messageElement = document.getElementById('nicknameCheckMessage');
    const nicknamePattern = /^[a-zA-Z0-9가-힣]{2,10}$/;
    const submitButton = document.querySelector('.save_btn');

    if (!nickname) {
        disableButton(submitButton, messageElement, '닉네임을 입력하세요.');
        return false;
    }

    if (!nicknamePattern.test(nickname)) {
        disableButton(submitButton, messageElement, '2자 이상 10자 이하, 한글, 영문자 또는 숫자만 포함.');
        return false;
    }

    try {
        const response = await fetch('/api/users/checkNickname', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nickname })
        });

        const isDuplicate = await response.json();

        if (isDuplicate) {
            disableButton(submitButton, messageElement, '이미 사용 중인 닉네임입니다.');
            return false;
        } else {
            messageElement.textContent = '사용 가능한 닉네임입니다.';
            messageElement.style.color = 'green';
            submitButton.disabled = false;
            submitButton.style.backgroundColor = '';
            submitButton.style.color = '';
            submitButton.style.cursor = '';
            return true;
        }
    } catch (error) {
        console.error('닉네임 중복 확인 오류:', error);
        disableButton(submitButton, messageElement, '서버 오류가 발생했습니다.');
        return false;
    }
}
// 폼 제출 제한
document.getElementById('editForm').addEventListener('submit', async function(event) {
    const isNicknameValid = await checkNickname();
    if (!isNicknameValid) {
        event.preventDefault();
        alert('닉네임 중복을 확인해주세요');
    } else {
        alert('닉네임 변경 성공');
    }
});

function disableButton(submitButton, messageElement, message) {
    messageElement.textContent = message;
    messageElement.style.color = 'red';
    submitButton.disabled = true;
    submitButton.style.backgroundColor = '#d3d3d3';
    submitButton.style.color = '#888';
    submitButton.style.cursor = 'not-allowed';
}