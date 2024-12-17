```mermaid
sequenceDiagram
    actor User
    participant Server
    participant AlanBaziController as controller
    participant AlanBaziService as service

%% 생년월일 기반 운세 정보 제공
User->>Server: 운세 정보 요청
Server->>AlanBaziController: 요청 전달
AlanBaziController->>AlanBaziService: 운세 정보 제공 요청
AlanBaziService-->>AlanBaziController: 운세 정보 전달
AlanBaziController-->>Server: 응답 전달
Server-->>User: 운세 정보 응답
```