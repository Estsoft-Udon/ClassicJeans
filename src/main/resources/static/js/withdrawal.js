window.onload = function() {
    const password = document.getElementById('password');
    document.getElementById('withdrawal_btn').addEventListener('click', function(event) {
        event.preventDefault(); // 기본 동작 방지 (폼 제출을 막음)

        if (confirm("정말 탈퇴하시겠습니까?")) {
            fetch('/api/users/withdrawal', {
                method: 'POST',  // 탈퇴 처리
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ password: password.value })  // 비밀번호 전송
            })
                .then(response => {
                    if (!response.ok) {
                        alert('비밀번호가 일치하지 않습니다.');
                        throw new Error('탈퇴 처리에 실패했습니다.');
                    }

                    // JSON 응답이 없을 수도 있으므로 먼저 텍스트로 처리
                    return response.text().then(text => {
                        return text ? JSON.parse(text) : {}; // 비어있으면 빈 객체 반환
                    });
                })
                .then(data => {
                    alert("탈퇴 처리가 완료되었습니다.");
                    window.location.href = '/logout';
                })
                .catch(error => {
                    console.error('오류 발생:', error);
                });
        }
    });
};