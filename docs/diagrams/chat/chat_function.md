```mermaid
sequenceDiagram
    actor User
    participant Server
    participant ChatController
    participant ChatService

%% AI 기반 대화 동반자
    User->>Server: AI 대화 요청
    Server->>ChatController: 요청 전달
    ChatController->>ChatService: AI 대화 처리 요청
    ChatService-->>ChatController: 대화 결과
    ChatController-->>Server: 응답 전달
    Server-->>User: AI 대화 응답

%% AI 비대면 진단
    User->>Server: AI 진단 요청
    Server->>ChatController: 요청 전달
    ChatController->>ChatService: AI 비대면 진단 처리
    ChatService-->>ChatController: 진단 결과
    ChatController-->>Server: 응답 전달
    Server-->>User: 진단 결과 응답
```