let isEmailVerified = false; // 이메일 인증 상태 변수
let isEmailChecked = false; // 존재하는 이메일인지 확인

// 이메일 존재 확인
async function checkEmail() {
    const email = document.getElementById('email').value.trim();
    const loginId = document.getElementById('loginId').value.trim();

    try {
        // 첫 번째 API 호출: 이메일과 로그인 ID 확인
        const response = await fetch('/api/checkEmailAndLoginId', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, loginId })
        });

        const data = await response.json(); // JSON 응답 파싱

        if (response.ok) {
            // 성공 메시지 처리
            alert(data.message);

            // 두 번째 API 호출: 이메일 인증 코드 전송
            const emailResponse = await fetch('/email/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({ email }) // email만 전송
            });

            if (emailResponse.ok) {
                isEmailChecked = true;
            } else {
                alert('이메일 인증 코드 전송에 실패했습니다.');
                isEmailChecked = false;
            }
        } else {
            // 실패 메시지 처리
            alert(data.error || '가입되지 않은 이메일입니다.');
            isEmailChecked = false;
        }
    } catch (error) {
        console.error('이메일 확인 오류:', error);
        alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
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
            window.location.href = `/change_pw`;
        })
        .catch(error => {
            isEmailVerified = false; // 인증 실패 상태로 변경
            alert(error.message); // 오류 메시지 표시
        });
}