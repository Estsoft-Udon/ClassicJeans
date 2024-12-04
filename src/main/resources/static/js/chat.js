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

function listenForMessages() {
    const eventSource = new EventSource('/chat/stream');

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

window.onload = function () {
    listenForMessages();
};

