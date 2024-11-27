```mermaid
sequenceDiagram
    participant User as 사용자
    participant WebApp as 웹 애플리케이션
    participant AIAPI as AI API
    participant DB as 데이터베이스
    participant Compare as 비교 모듈

    User ->> WebApp: 문진표 작성 및 제출
    WebApp ->> AIAPI: 문진표 데이터 요청
    AIAPI -->> WebApp: 건강지수 및 건강 점수 반환
    WebApp ->> DB: 이전 결과 요청
    DB -->> WebApp: 이전 결과 반환
    WebApp ->> Compare: 결과 비교 요청
    Compare -->> WebApp: 비교 결과 반환
    WebApp -->> User: 비교 결과 응답
```