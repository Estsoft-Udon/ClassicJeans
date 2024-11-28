```mermaid
sequenceDiagram
    actor User
    participant Server
    participant MainController

    %% 오늘의 운세 제공
    User->>Server: 운세 요청
    Server->>MainController: 요청 전달
    MainController-->>Server: 운세 정보
    Server-->>User: 운세 정보 응답

    %% 시니어 정보 분석 페이지로 이동
    User->>Server: 시니어 정보 분석 페이지 이동
    Server->>MainController: 요청 전달
    MainController-->>Server: 분석 페이지 정보
    Server-->>User: 시니어 분석 페이지로 이동

    %% AI 대화 동반자 페이지로 이동
    User->>Server: 대화 동반자 페이지 요청
    Server->>MainController: 요청 전달
    MainController-->>Server: 대화 동반자 페이지 정보
    Server-->>User: AI 대화 동반자 페이지로 이동

    %% 병원 검색 페이지로 이동
    User->>Server: 병원 검색 페이지 요청
    Server->>MainController: 요청 전달
    MainController-->>Server: 병원 검색 페이지 정보
    Server-->>User: 병원 검색 페이지로 이동

    %% 요양원 검색 페이지로 이동
    User->>Server: 요양원 검색 페이지 요청
    Server->>MainController: 요청 전달
    MainController-->>Server: 요양원 검색 페이지 정보
    Server-->>User: 요양원 검색 페이지로 이동

```