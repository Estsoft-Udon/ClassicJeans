let eventSource = null; // 전역변수로 설정
let currentMessageBuffer = ""; // 메시지 누적 버퍼
let userId = null; // 전역 변수로 설정
let messageContainer = null;
let isWaitingForResponse = null;

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

    if (!content.trim()) {
        appendMessage("질문을 입력해주세요", false);
        return;
    }

    if (content.trim()) {
        // 전송된 메세지를 화면에 추가
        appendMessage(content, true);
        input.value = '';

        isWaitingForResponse = true;

        // 새로운 응답 메세지 요소 생성
        const responseBox = document.createElement("div");
        responseBox.classList.add('message', 'received');
        responseBox.setAttribute("id", `responseBox-${Date.now()}`); // 고유 ID 부여

        // 텍스트 하나씩 나타내기 위한 span 추가
        const waitingText = document.createElement('span');
        waitingText.classList.add('waiting');
        responseBox.appendChild(waitingText);

        // 텍스트 하나씩
        const textSpan = document.createElement('span');
        textSpan.innerHTML = "답변을 생성 중이에요 잠시만 기다려 주세요!";
        waitingText.appendChild(textSpan);

        // 로딩 원
        const loadingCircle = document.createElement('div');
        loadingCircle.classList.add('loading-circle');
        loadingCircle.style.display = 'none';
        responseBox.appendChild(loadingCircle);

        chatContainer.appendChild(responseBox);

        // 메시지를 서버로 전송
        fetch('api/chat/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(content)
        }).catch((err) => {
            console.error("메시지 전송 오류:", err);
            isWaitingForResponse = false;
        });

        scroll();

        // 동적으로 생성된 responseBox를 전역 변수 또는 다른 함수에서 사용할 수 있음
        messageContainer = responseBox;

        // 타이핑 애니메이션 끝나고 로딩 원 노출
        setTimeout(() => {
            loadingCircle.style.display = 'inline-block';
            setTimeout(() => {
                loadingCircle.style.opacity = 1;
            }, 50);
        }, 3000);
    }
    inputText.value = '';
    charCount.textContent = '0 / 1,000 자';
}

function responseMessage(responseContent) {
    if (messageContainer) {
        // 응답 내용을 업데이트
        messageContainer.innerHTML = responseContent;

        isWaitingForResponse = false;

        scroll();
    }
}

function listenForMessages() {
    if (eventSource) {
        eventSource.close();// 기존 연결 닫기
    }

    eventSource = new EventSource('api/chat/stream');

    // 서버로부터 userId 수신
    eventSource.addEventListener('userId', function (event) {
        userId = event.data; // 서버에서 받은 userId 저장
    });

    // 현재 메세지를 추가할 요소
    eventSource.addEventListener('message', function (event) {
        currentMessageBuffer = event.data.trim();

        responseMessage(currentMessageBuffer);

        scroll();
    });

    eventSource.addEventListener('completed', function () {
        console.log("메시지 초기화 완료");
        currentMessageBuffer = null;
    });
}

function closeConnection() {
    if (isWaitingForResponse) {
        alert("현재 답변이 생성 중입니다.\n" +
            "답변이 완료된 후에 종료해주세요!");
        return;
    }

    if (eventSource) {
        // SSE 연결 종료
        eventSource.close();
        eventSource = null;
        console.log("SSE 연결이 종료되었습니다.")
    }

    // SSE 연결 종료 API 호출
    fetch(`/api/chat/stream/close?userId=${userId}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
    })
        .then(response => {
            if (response.ok) {
                // 연결 종료 후 메시지 표시
                const message = document.createElement('div');
                message.classList.add('message', 'received');
                message.innerHTML = '<span>연결이 종료되었습니다. <br> 재시작을 원하시면 새로고침을 해주세요.</span>';
                chatContainer.appendChild(message);
                scroll();

                // form 안의 모든 입력 필드와 버튼 비활성화
                const formElements = document.querySelectorAll('.input-container input, .input-container button');
                formElements.forEach(element => {
                    element.disabled = true;
                    element.style.cursor = 'not-allowed'; // 커서를 비활성화 스타일로 변경
                });

                // 연결 종료 버튼 비활성화
                const closeButton = document.getElementById('closeBtn'); // 버튼 ID로 선택
                if (closeButton) {
                    closeButton.disabled = true;
                    closeButton.style.cursor = 'not-allowed'; // 커서 변경
                }

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
    initialMessage.innerHTML = "<strong>앨런 AI 챗봇에게 궁금한 부분을 물어보세요! </strong>" +
        "<br> 질문 예시 1) ➡️ 맥도날드 키오스크 사용법 알려줘" +
        "<br> 질문 예시 2) ➡️ 1970년대생이 받을 수 있는 지원금 알려줘" +
        "<br> 질문 예시 3) ➡️ 갑자기 속이 쓰려 왜 그럴까?";

    chatContainer.appendChild(initialMessage);
    listenForMessages();
};

function scroll() {
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

const inputText = document.getElementById('chat-input');
const charCount = document.getElementById('charCount');
const maxLength = 1000;

// 글자 수 제한 및 실시간 카운트 업데이트
inputText.addEventListener('input', () => {
    const currentLength = inputText.value.length;

    // 글자 수가 1000자를 초과하지 않도록 제한 - 1000자 이상 시 잘라냄
    if (currentLength > maxLength) {
        inputText.value = inputText.value.substring(0, maxLength);
    }

    // 글자 수 표시 업데이트
    charCount.textContent = `${inputText.value.length} / 1,000 자`;
});