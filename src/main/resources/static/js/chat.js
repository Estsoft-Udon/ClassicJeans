let eventSource = null; // 전역변수로 설정
let currentMessageBuffer = ""; // 메시지 누적 버퍼
let emitterId = null; // 전역 변수로 설정
let messageContainer = null;

const chatContainer = document.getElementById('chat-container');

function appendMessage(content, isSent) {
    const message = document.createElement('div');
    message.classList.add('message');
    message.classList.add(isSent ? 'sent' : 'received');

    message.innerHTML = content;
    chatContainer.appendChild(message);

    scroll();
    return message;
}

function sendMessage() {
    const input = document.getElementById('chat-input');
    const content = input.value;

    if (content.trim()) {
        // 전송된 메세지를 화면에 추가
        appendMessage(content, true);
        input.value = '';

        // 새로운 응답 메세지 요소 생성
        const responseBox = document.createElement("div");
        responseBox.classList.add('message', 'received');
        responseBox.setAttribute("id", `responseBox-${Date.now()}`); // 고유 ID 부여

        responseBox.innerHTML = "응답 대기 중..."; // 기본 텍스트 설정
        chatContainer.appendChild(responseBox);

        // 메시지를 서버로 전송
        fetch('/chat/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(content)
        }).catch((err) => console.error("메시지 전송 오류:", err));

        scroll();

        // 동적으로 생성된 responseBox를 전역 변수 또는 다른 함수에서 사용할 수 있음
        messageContainer = responseBox;
    }
}

function responseMessage(responseContent) {
    if (messageContainer) {
        // 응답 내용을 업데이트
        messageContainer.innerHTML = responseContent;

        scroll();
    }
}

function listenForMessages() {
    if (eventSource) {
        eventSource.close();// 기존 연결 닫기
    }

    eventSource = new EventSource('/chat/stream');

    // 서버로부터 emitterId 수신
    eventSource.addEventListener('emitterId', function (event) {
        emitterId = event.data; // 서버에서 받은 emitterId 저장
    });

    // 현재 메세지를 추가할 요소
    eventSource.addEventListener('message', function (event) {
        currentMessageBuffer = event.data.trim();

        responseMessage(currentMessageBuffer);

        scroll();
    });

    eventSource.onerror = function (event) {
        console.error("SSE 연결 오류:", event);
        if (eventSource.readyState === EventSource.CLOSED) {
            console.log("SSE 연결이 종료되었습니다.");
        }
    };

    eventSource.addEventListener('completed', function () {
        currentMessageBuffer = null;
        console.log("메시지 초기화 완료");
    });
}

function closeConnection() {
    if (!emitterId) {
        console.error("emitterId를 찾을 수 없습니다!");
        return;
    }

    // SSE 연결 종료 API 호출
    fetch('/chat/stream/close', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({emitterId}) // emitterId 필요
    })
        .then(response => {
            if (response.ok) {
                // 연결 종료 후 메시지 표시
                const message = document.createElement('div');
                message.classList.add('message', 'received');
                message.innerHTML = '<strong>연결이 종료되었습니다.</strong>';
                chatContainer.appendChild(message);
                scroll();

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
    const initialMessage = document.createElement('div');
    initialMessage.classList.add('message', 'received')
    initialMessage.innerHTML = "<strong>앨런 AI 챗봇에게 궁금한 부분을 물어보세요! </strong><br>" +
        "<br> 질문 예시 " +
        "<br> 맥도날드 키오스크 사용법 알려줘," +
        "<br> 1970년대생이 받을 수 있는 지원금 알려줘," +
        "<br> 갑자기 속이 쓰려 왜 그럴까?";

    chatContainer.appendChild(initialMessage);
    listenForMessages();
};

function scroll() {
    chatContainer.scrollTop = chatContainer.scrollHeight;
}
