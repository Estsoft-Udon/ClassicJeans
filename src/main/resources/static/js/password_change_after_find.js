
function validatePassword() {
    const newPassword = document.getElementById('newPassword').value.trim();
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    const passwordMessage = document.getElementById('passwordMessage');

    // 비밀번호 패턴 검증
    if (!passwordPattern.test(newPassword)) {
        passwordMessage.textContent = '최소 8자 이상, 영문자, 숫자, 특수문자가 포함되어야 합니다.';
        passwordMessage.style.color = 'red';
        return false;
    }  else {
        passwordMessage.textContent = '';
        return true;
    }
}
// 비밀번호 확인
function checkPasswordMatch() {
    const newPassword = document.getElementById('newPassword').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();
    const messageElement = document.getElementById('passwordMatchMessage');
    if (!newPassword || !confirmPassword) {
        messageElement.textContent = '';
        return true;
    }
    if (newPassword !== confirmPassword) {
        messageElement.textContent = '비밀번호가 일치하지 않습니다.';
        messageElement.style.color = 'red';
        return false;
    } else {
        messageElement.textContent = '비밀번호가 일치합니다.';
        messageElement.style.color = 'green';
        return true;
    }
}

// 폼 제출 시 비밀번호 확인과 검증
document.getElementById('changePasswordForm').addEventListener('submit', function(event) {
    const isPasswordValid = validatePassword();
    const isPasswordMatch = checkPasswordMatch();

    // 검증 실패 시 폼 제출 막기
    if (!isPasswordValid || !isPasswordMatch) {
        event.preventDefault();
        if (!isPasswordValid) {
            alert('비밀번호가 정규식에 맞지 않습니다.');
        } else if (!isPasswordMatch) {
            alert('비밀번호가 일치하지 않습니다.');
        }
    }
});


// 비밀번호 입력 시 실시간 정규식 검사
document.getElementById('newPassword').addEventListener('input', function() {
    validatePassword();
    checkPasswordMatch();
});
// 실시간으로 비밀번호 확인 입력 시 검증
document.getElementById('confirmPassword').addEventListener('input', checkPasswordMatch);
