let eventSource = null; // 전역변수로 설정
let currentMessageBuffer = ""; // 메시지 누적 버퍼
let emitterId = null; // 전역 변수로 설정
let messageContainer = null;

function appendMessage(content, isSent) {
    const chatContainer = document.getElementById('chat-container');
    const message = document.createElement('div');
    message.classList.add('message');
    message.classList.add(isSent ? 'sent' : 'received');

    // 개행 문자를 <br> 태그로 변환
    message.innerHTML = content;

    chatContainer.appendChild(message);

    // 스크롤을 가장 아래로 이동
    chatContainer.scrollTop = chatContainer.scrollHeight;
    return message;
}
function responseMessage(responseContent){
    // chat-container 요소 선택
    const chatContainer = document.getElementById('chat-container');

    // 새로운 메시지 div 생성
    const message = document.createElement('div');
    message.classList.add('message', 'received'); // 'received' 클래스를 추가하여 받은 메시지 스타일 적용

    // 응답 내용 삽입
    message.innerHTML = responseContent;

    // 새로운 메시지를 chat-container에 추가
    chatContainer.appendChild(message);

    // 스크롤을 항상 아래로 이동
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

    // // 버튼을 눌렀을 때 새로운 element를 추가한다.
    // // 추가한 element 를
    // let responseBox = document.createElement("div");
    //
    // // id를 동적으로 줘야한다.
    // responseBox.setAttribute("id", "responseBox");
    //
    // responseBox.style.backgroundColor = "red";
    // // 눌렀을때 요소가 생기는지
    // document.body.appendChild(responseBox);
    //
    //

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

        // 기존 메시지 요소가 없으면 새로 생성
        responseMessage(currentMessageBuffer);

        // 스크롤을 항상 아래로 유지
        const chatContainer = document.getElementById('chat-container');
        chatContainer.scrollTop = chatContainer.scrollHeight;

    });

    eventSource.onerror = function (event) {
        console.error("SSE 연결 오류:", event);
        if (eventSource.readyState === EventSource.CLOSED) {
            console.log("SSE 연결이 종료되었습니다.");
        }
    };

    eventSource.addEventListener('completed', function () {
        // chatContainer에서 모든 메시지를 초기화
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
    initialMessage.innerHTML = "<strong>앨런 AI 챗봇에게 궁금한 부분을 물어보세요! </strong><br>" +
        "<br> 질문 예시 " +
        "<br> 맥도날드 키오스크 사용법 알려줘," +
        "<br> 1970년대생이 받을 수 있는 지원금 알려줘," +
        "<br> 갑자기 속이 쓰려 왜 그럴까?";

    chatContainer.appendChild(initialMessage);

    listenForMessages();
};