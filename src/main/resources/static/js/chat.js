function appendMessage(content, isSent) {
    const chatContainer = document.getElementById('chat-container');
    const message = document.createElement('div');
    message.classList.add('message');
    message.classList.add(isSent ? 'sent' : 'received');

    // 개행 문자를 <br> 태그로 변환
    message.innerHTML = content;

    chatContainer.appendChild(message);
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

function sendMessage() {
    const input = document.getElementById('chat-input');
    const content = input.value;
    if (content.trim()) {
        appendMessage(content, true);
        input.value = '';

        // 메시지를 서버로 전송
        fetch('/chat/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(content)
        }).catch((err) => console.error("메시지 전송 오류:", err));
    }
}

let eventSource = null; // 전역 변수로 설정

function listenForMessages() {
    if (eventSource) {
        eventSource.close(); // 기존 연결 닫기
    }

    eventSource = new EventSource('/chat/stream');

    eventSource.addEventListener('message', function (event) {
        const content = event.data;
        appendMessage(content, false);
    });

    eventSource.onerror = function (event) {
        console.error("SSE 연결 오류:", event);
        if (eventSource.readyState === EventSource.CLOSED) {
            console.log("SSE 연결이 종료되었습니다.");
        }
    };
}

function closeConnection() {
    // SSE 연결 종료 API 호출
    fetch('/chat/stream/close', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({emitterId: 'your-emitter-id-here'}) // emitterId 필요
    })
        .then(response => {
            if (response.ok) {
                // 연결 종료 후 메시지 표시
                const chatContainer = document.getElementById('chat-container');
                const message = document.createElement('div');
                message.classList.add('message', 'received');
                message.innerHTML = '<strong>연결이 종료되었습니다.</strong>';
                chatContainer.appendChild(message);

                // 입력 필드와 버튼을 비활성화 시킬 수 있습니다.
                document.getElementById('chat-input').disabled = true;
                document.querySelector('button[type="button"]').disabled = true;
            } else {
                alert("연결 종료에 실패했습니다.");
            }
        })
        .catch(error => console.error("Error:", error));
}

window.onload = function () {
    // 기본 메시지 추가
    const chatContainer = document.getElementById('chat-container');
    const initialMessage = document.createElement('div');
    initialMessage.classList.add('message', 'received')
    initialMessage.innerHTML = "<strong>앨런 AI 챗봇에게 궁금한 부분을 물어보세요! </strong>" +
        "   <br> " +
        "   <br> 질문 예시 " +
        "            맥도날드 키오스크 사용법 알려줘,<br>" +
        "            1970년대생이 받을 수 있는 지원금 알려줘,<br>" +
        "            갑자기 속이 쓰려 왜 그럴까?<br>";

    chatContainer.appendChild(initialMessage);

    listenForMessages();
};

