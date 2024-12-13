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
        body: JSON.stringify({ loginId })
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
            body: JSON.stringify({ nickname })
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
document.getElementById('email').addEventListener('input', function() {
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
        body: JSON.stringify({ email })
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
document.getElementById('emailAuthBtn').addEventListener('click', function() {
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

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    const modal = document.getElementById("emailAuthModal");
    if (event.target === modal) {
        modal.style.display = "none";  // 모달을 숨기기
    }
}

// 폼 제출 시 아이디, 닉네임, 이메일 유효성 체크
document.getElementById('signupForm').addEventListener('submit', async function(event) {
    console.log("폼 제출 이벤트 발생");

    console.log("유효성 검사 결과", { isIdChecked, isNicknameChecked, isEmailChecked, isEmailVerified });

    if (!isIdChecked || !isNicknameChecked || !isEmailChecked || !isEmailVerified) {
        event.preventDefault();  // 폼 제출을 막기
        console.log("폼 제출이 막혔습니다");

        if (!isEmailVerified) {
            alert('이메일 인증을 완료해주세요');
        } else {
            alert('아이디, 닉네임, 이메일을 확인 해주세요');
        }
    }
});

document.getElementById("emailAuthForm").addEventListener("submit", function (event) {
    event.preventDefault(); // 폼 기본 동작 중단

    const authCode = document.getElementById("emailAuthCode").value;
    const email = document.getElementById("email").value; // 이메일 입력 필드 값 가져오기

    fetch('/api/auth/verify', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
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