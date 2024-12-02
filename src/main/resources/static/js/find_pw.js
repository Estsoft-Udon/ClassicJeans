let isEmailVerified = false; // 이메일 인증 상태 변수
let isEmailChecked = false; // 존재하는 이메일인지 확인
// 이메일 중복확인
async function checkEmail() {
    const email = document.getElementById('email').value.trim();

    try {
        const response = await fetch('/api/checkEmail', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email })
        });

        const isDuplicate = await response.json();

        if (isDuplicate) {
            alert('이메일 인증 코드가 전송되었습니다.');

            fetch('/send-email', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    email: document.getElementById('email').value, // 이메일 입력값
                })
            })

            isEmailChecked = true;
        } else {
            alert('가입되지 않은 이메일입니다.');

            isEmailChecked = false;
        }
    } catch (error) {
        console.error('이메일 확인 오류:', error);
        isEmailChecked = false;
    }
}

// 인증 코드 제출 함수
function submitAuthCode() {
    const authCode = document.getElementById('authCode').value.trim();
    const email = document.getElementById('email').value.trim();

    if (!authCode || !email) {
        alert('인증 코드를 입력하세요.');
        return;
    }

    // 인증 코드와 이메일을 서버에 전송하여 인증 확인
    fetch('/api/auth/verify', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `authCode=${encodeURIComponent(authCode)}&email=${encodeURIComponent(email)}`
    })
        .then(response => {
            if (response.ok) {
                return response.text(); // 인증 성공 시 메시지 반환
            } else {
                throw new Error('인증번호 확인 실패');
            }
        })
        .then(result => {
            alert(result); // 서버 응답 메시지 표시
            isEmailVerified = true; // 이메일 인증 완료 상태로 변경
            window.location.href = '/change_pw'; // 인증 후 특정 페이지로 리다이렉트
        })
        .catch(error => {
            isEmailVerified = false; // 인증 실패 상태로 변경
            alert(error.message); // 오류 메시지 표시
        });
}