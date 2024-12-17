let isIdChecked = false;
let isNicknameChecked = false;
let isEmailChecked = false;
let isEmailVerified = false; // 이메일 인증 상태 (기본값: 인증되지 않음)

// 아이디 중복확인
function checkId() {
    const loginId = document.getElementById('loginId').value.trim();
    const messageElement = document.getElementById('idCheckMessage');
    const idPattern = /^[a-zA-Z0-9]{4,20}$/;

    if (!loginId) {
        messageElement.textContent = '아이디를 입력하세요.';
        messageElement.style.color = 'red';
        return Promise.resolve(false);
    }

    if (!idPattern.test(loginId)) {
        messageElement.textContent = '4자 이상 20자 이하, 영문자와 숫자만 포함';
        messageElement.style.color = 'red';
        return Promise.resolve(false);
    }

    return fetch('/api/users/checkId', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({loginId})
    })
        .then(response => response.json())
        .then(isDuplicate => {
            if (isDuplicate) {
                messageElement.textContent = '이미 사용 중인 아이디입니다.';
                messageElement.style.color = 'red';
                isIdChecked = false;
            } else {
                messageElement.textContent = '사용 가능한 아이디입니다.';
                messageElement.style.color = 'green';
                isIdChecked = true;
            }
        })
        .catch(error => {
            console.error('아이디 중복 확인 오류:', error);
            isIdChecked = false;
        });
}

// 닉네임 중복확인
async function checkNickname() {
    const nickname = document.getElementById('nickname').value.trim();
    const messageElement = document.getElementById('nicknameCheckMessage');
    const nicknamePattern = /^[a-zA-Z0-9가-힣]{2,10}$/;

    if (!nickname) {
        messageElement.textContent = '닉네임을 입력하세요.';
        messageElement.style.color = 'red';
        return Promise.resolve(false);
    }

    if (!nicknamePattern.test(nickname)) {
        messageElement.textContent = '2자 이상 10자 이하, 한글, 영문자 또는 숫자만 포함.';
        messageElement.style.color = 'red';
        return Promise.resolve(false);
    }

    try {
        const response = await fetch('/api/users/checkNickname', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({nickname})
        });

        const isDuplicate = await response.json();

        if (isDuplicate) {
            messageElement.textContent = '이미 사용 중인 닉네임입니다.';
            messageElement.style.color = 'red';
            isNicknameChecked = false;
        } else {
            messageElement.textContent = '사용 가능한 닉네임입니다.';
            messageElement.style.color = 'green';
            isNicknameChecked = true;
        }
    } catch (error) {
        console.error('닉네임 중복 확인 오류:', error);
        isNicknameChecked = false;
    }
}

// 이메일 실시간 중복확인
document.getElementById('email').addEventListener('input', function () {
    const email = this.value.trim();
    const messageElement = document.getElementById('emailCheckMessage');
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email) {
        messageElement.textContent = '이메일을 입력하세요.';
        messageElement.style.color = 'red';
        isEmailChecked = false;
        document.getElementById('emailAuthBtn').disabled = true;  // 이메일 인증 버튼 비활성화
        return;
    }

    if (!emailPattern.test(email)) {
        messageElement.textContent = '유효한 이메일 주소를 입력하세요.';
        messageElement.style.color = 'red';
        isEmailChecked = false;
        document.getElementById('emailAuthBtn').disabled = true;  // 이메일 인증 버튼 비활성화
        return;
    }

    // 이메일 중복 확인 API 호출
    fetch('/api/users/checkEmail', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({email})
    })
        .then(response => response.json())
        .then(isDuplicate => {
            if (isDuplicate) {
                messageElement.textContent = '이미 사용 중인 이메일입니다.';
                messageElement.style.color = 'red';
                isEmailChecked = false;
                document.getElementById('emailAuthBtn').disabled = true;  // 이메일 인증 버튼 비활성화
            } else {
                messageElement.textContent = '사용 가능한 이메일입니다.';
                messageElement.style.color = 'green';
                isEmailChecked = true;
                document.getElementById('emailAuthBtn').disabled = false;  // 이메일 인증 버튼 활성화
            }
        })
        .catch(error => {
            console.error('이메일 중복 확인 오류:', error);
            messageElement.textContent = '이메일 확인 중 오류가 발생했습니다.';
            messageElement.style.color = 'red';
            isEmailChecked = false;
            document.getElementById('emailAuthBtn').disabled = true;  // 이메일 인증 버튼 비활성화
        });
});

// 이메일 인증 버튼 클릭 시 모달 띄우기
document.getElementById('emailAuthBtn').addEventListener('click', function () {
    if (isEmailChecked) {
        showEmailModal();  // 인증 모달 창을 여는 함수 호출
    } else {
        alert('이메일 중복 확인을 먼저 해주세요');
    }
});

// 모달 띄우기
function showEmailModal() {
    const modal = document.getElementById("emailAuthModal");
    const email = document.getElementById("email").value; // 이메일 입력 필드 값 가져오기
    fetch('/api/email/send', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            email: document.getElementById('email').value, // 이메일 입력값
        })
    })
    document.getElementById('authEmail').value = email; // 숨겨진 필드에 이메일 값 설정
    modal.style.display = "flex";
}

// 모달 닫기
function closeEmailModal() {
    const modal = document.getElementById("emailAuthModal");
    modal.style.display = "none";  // 모달을 숨기기
}

document.getElementById("emailAuthForm").addEventListener("submit", function (event) {
    event.preventDefault(); // 폼 기본 동작 중단

    const authCode = document.getElementById("emailAuthCode").value;
    const email = document.getElementById("email").value; // 이메일 입력 필드 값 가져오기

    fetch('/api/auth/verify', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `authCode=${encodeURIComponent(authCode)}&email=${encodeURIComponent(email)}`,
    })
        .then(response => {
            if (response.ok) {
                return response.text(); // 응답이 성공적일 경우, 응답 본문을 텍스트로 반환
            } else {
                throw new Error('이메일 인증에 실패하였습니다.'); // 응답이 실패한 경우
            }
        })
        .then(result => {
            alert(result); // 서버 응답 메시지 표시
            isEmailVerified = true; // 이메일 인증 완료 상태로 변경
            document.getElementById('emailCheckMessage').textContent = '이메일 인증이 완료되었습니다.';

            closeEmailModal(); // 모달 닫기 (옵션)

        })
        .catch(error => {
            isEmailVerified = false; // 이메일 인증 완료 상태로 변경
            alert(error.message); // 오류 메시지 표시
        });
});

// 생년월일 입력값을 기준으로 연령 계산 및 제한
function validateAge() {
    const dateOfBirth = document.getElementById('dateOfBirth').value;
    const messageElement = document.getElementById('ageMessage');
    if (!dateOfBirth) {
        messageElement.textContent = '생년월일을 입력해주세요.';
        messageElement.style.color = 'red';
        return false;
    }

    const today = new Date();
    const birthDate = new Date(dateOfBirth);
    const ageLimit = 18;

    // 만 18세가 되는 날짜 계산
    const eighteenYearsLater = new Date(birthDate);
    eighteenYearsLater.setFullYear(birthDate.getFullYear() + ageLimit);

    // 만 18세가 되지 않은 날짜를 선택한 경우
    if (today < eighteenYearsLater) {
        messageElement.innerHTML = `죄송합니다.<br>만 ${ageLimit}세가 되어야 가입이 가능합니다.`;
        messageElement.style.color = 'red';
        return false;
    } else {
        // 만 18세 이상인 경우
        messageElement.textContent = '가입 가능합니다.';
        messageElement.style.color = 'green';
        return true;
    }
}

// 실시간으로 연령 제한 검사 및 메시지 업데이트
document.getElementById('dateOfBirth').addEventListener('input', validateAge);

// 폼 제출 이벤트에 연령 제한 검사 추가
document.getElementById('signupForm').addEventListener('submit', function (event) {
    if (!validateAge()) {
        event.preventDefault();
    }
});

// 당일 날짜까지만 표시
document.addEventListener('DOMContentLoaded', () => {
    const today = new Date();
    const maxDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
    document.getElementById('dateOfBirth').max = maxDate.toISOString().split('T')[0];
});


document.getElementById('signupForm').addEventListener('submit', async function (event) {
    event.preventDefault(); // 폼 제출 방지

    const loginId = document.getElementById('loginId').value.trim();
    const nickname = document.getElementById('nickname').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();

    try {
        // 서버에 최종 유효성 검사 요청
        const response = await fetch('/api/users/validateSignup', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({loginId, nickname, email, password, confirmPassword})
        });

        const result = await response.json();

        if (response.ok && result.isValid) {
            // 서버 검증 통과 시 폼 제출
            if (!isIdChecked || !isNicknameChecked || !isEmailChecked || !isEmailVerified) {
                event.preventDefault();  // 폼 제출을 막기
                alert('회원가입이 정상처리되지 않았습니다. 중복 확인이나 이메일 인증을 진행해주세요.');
                return;
            }
            this.submit();
        } else {
            alert(result.message || '유효성 검사에 실패했습니다.');
        }
    } catch (error) {
        console.error('유효성 검사 중 오류 발생:', error);
        alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
    }
});