```mermaid
sequenceDiagram
actor User
participant Server
participant UserController
participant UserService
participant UserRepository

    User->>Server: 회원가입 요청
    Server->>UserController: 요청 전달
    UserController->>UserService: 회원가입 서비스 호출
    UserService->>UserRepository: 사용자 정보 저장
    UserRepository-->>UserService: 저장 결과
    UserService-->>UserController: 회원가입 처리 결과
    UserController-->>Server: 응답 전달
    Server-->>User: 회원가입 응답
    
    User->>Server: 로그인 요청
    Server->>UserController: 요청 전달
    UserController->>UserService: 로그인 서비스 호출
    UserService->>UserRepository: 사용자 정보 조회
    UserRepository-->>UserService: 사용자 정보 반환
    UserService-->>UserController: 로그인 처리 결과
    UserController-->>Server: 응답 전달
    Server-->>User: 로그인 응답

    User->>Server: 소셜 로그인 요청
    Server->>UserController: 요청 전달
    UserController->>UserService: 소셜 로그인 서비스 호출
    UserService->>UserRepository: 소셜 로그인 처리
    UserRepository-->>UserService: 소셜 로그인 결과
    UserService-->>UserController: 소셜 로그인 결과
    UserController-->>Server: 응답 전달
    Server-->>User: 소셜 로그인 응답
```