```mysql
use chungbaji;

CREATE TABLE users
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id      VARCHAR(50)  NOT NULL,
    name          VARCHAR(50)  NOT NULL,
    nickname      VARCHAR(50)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    grade         VARCHAR(255)          DEFAULT NULL, -- Grade Enum 값을 사용자가 정의해야 합니다.
    password      VARCHAR(255) NOT NULL,
    date_of_birth DATE         NOT NULL,
    is_lunar      BOOLEAN      NOT NULL DEFAULT FALSE,
    hour_of_birth INT                   DEFAULT NULL,
    gender        VARCHAR(10)           DEFAULT NULL,
    created_at    DATETIME     NOT NULL,
    updated_at    DATETIME              DEFAULT NULL,
    deleted_at    DATETIME              DEFAULT NULL,
    last_login    DATETIME              DEFAULT NULL,
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    unique_key    VARCHAR(255) UNIQUE
);

CREATE TABLE nursing_home_data
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50)  NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone   VARCHAR(255) NOT NULL
);

CREATE TABLE family_info
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    name          VARCHAR(150) NOT NULL, -- 가족 이름
    gender        VARCHAR(10)  NOT NULL, -- 성별
    date_of_birth DATE         NOT NULL, -- 생년월일
    relationship  VARCHAR(255) NOT NULL, -- 가족 관계
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE subsidy_data
(
    id      BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date    DATE   NOT NULL, -- 지원 신청 날짜
    content TEXT   NOT NULL, -- 지원 내용
    UNIQUE (user_id, date),  -- 동일 사용자와 날짜 중복 방지
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE bazi
(
    id      BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date    DATE   NOT NULL, -- 분석 날짜
    content TEXT   NOT NULL, -- 사주 데이터
    UNIQUE (user_id, date),  -- 중복 방지
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE hospital_data
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL, -- 병원 이름
    address    VARCHAR(255) NOT NULL, -- 병원 주소
    latitude   DOUBLE       NULL,     -- 위도
    longitude  DOUBLE       NULL,     -- 경도
    phone      VARCHAR(255) NULL,     -- 병원 연락처
    region     VARCHAR(255) NULL,     -- 병원이 위치한 지역
    sub_region VARCHAR(255) NULL      -- 세부 지역
);

CREATE TABLE dementia_data
(
    id                              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                         BIGINT  NOT NULL,
    family_id                       BIGINT,
    date                            DATE    NOT NULL, -- 검사 날짜
    memory_change                   VARCHAR(255),     -- 기억력 변화 (ENUM 값으로 관리 가능)
    daily_confusion                 VARCHAR(255),     -- 일상 혼란 (ENUM 값)
    problem_solving_change          VARCHAR(255),     -- 문제 해결 능력 변화 (ENUM 값)
    language_change                 VARCHAR(255),     -- 언어 사용 변화 (ENUM 값)
    knows_date                      BOOLEAN NOT NULL, -- 날짜와 요일 인지 여부
    knows_location                  BOOLEAN NOT NULL, -- 현재 위치 인지 여부
    remembers_recent_events         BOOLEAN NOT NULL, -- 최근 사건 기억 여부
    frequency_of_repetition         VARCHAR(255),     -- 반복적 질문 빈도 (ENUM 값)
    lost_items_frequency            VARCHAR(255),     -- 물건 잃어버림 빈도 (ENUM 값)
    daily_activity_difficulty       VARCHAR(255),     -- 일상 활동 난이도 (ENUM 값)
    going_out_alone                 VARCHAR(255),     -- 혼자 외출 난이도 (ENUM 값)
    financial_management_difficulty VARCHAR(255),     -- 금전 관리 난이도 (ENUM 값)
    anxiety_or_aggression           VARCHAR(255),     -- 불안, 공격성 빈도 (ENUM 값)
    hallucination_or_delusion       VARCHAR(255),     -- 환각/망상 빈도 (ENUM 값)
    sleep_pattern_change            VARCHAR(255),     -- 수면 패턴 변화 빈도 (ENUM 값)
    has_chronic_diseases            BOOLEAN NOT NULL, -- 만성 질환 여부
    has_stroke_history              BOOLEAN NOT NULL, -- 뇌졸중 병력 여부
    has_family_dementia             BOOLEAN NOT NULL, -- 치매 가족력 여부
    summary_evaluation              TEXT,             -- 종합 평가 내용 (TEXT 컬럼)
    improvement_suggestions         TEXT,             -- 개선 방법 (TEXT 컬럼)
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (family_id) REFERENCES family_info (id) ON DELETE CASCADE
);

CREATE TABLE questionnaire_data
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                 BIGINT       NOT NULL,
    family_id               BIGINT,
    date                    DATE         NOT NULL, -- 설문지 작성 날짜
    age                     INT          NOT NULL, -- 나이
    gender                  VARCHAR(10)  NOT NULL, -- 성별
    height                  DOUBLE       NOT NULL, -- 키
    weight                  DOUBLE       NOT NULL, -- 체중
    chronic_disease         VARCHAR(255),          -- 만성 질환 (ENUM 값)
    hospital_visit          VARCHAR(255),          -- 병원 방문 여부 (ENUM 값)
    current_medication      VARCHAR(255),          -- 복용 약물 (ENUM 값)
    smoking_status          VARCHAR(255),          -- 흡연 여부 (ENUM 값)
    alcohol_consumption     VARCHAR(255),          -- 음주 빈도 (ENUM 값)
    exercise_frequency      VARCHAR(255),          -- 운동 빈도 (ENUM 값)
    diet_pattern            VARCHAR(255),          -- 식습관 (ENUM 값)
    mood_status             VARCHAR(255),          -- 기분 상태 (ENUM 값)
    sleep_pattern           VARCHAR(255),          -- 수면 패턴 (ENUM 값)
    independence_level      VARCHAR(255),          -- 자립 수준 (ENUM 값)
    social_participation    VARCHAR(255),          -- 사회적 활동 참여 (ENUM 값)
    has_genetic_disease     BOOLEAN      NOT NULL, -- 유전적 질환 여부
    weight_change           VARCHAR(255),          -- 체중 변화 (ENUM 값)
    has_allergy             BOOLEAN      NOT NULL, -- 알레르기 여부
    age_group               VARCHAR(255) NOT NULL, -- 연령대
    average_height          DOUBLE       NOT NULL, -- 한국인 평균 키
    average_weight          DOUBLE       NOT NULL, -- 한국인 평균 체중
    smoking_rate            DOUBLE       NOT NULL, -- 한국인 평균 흡연율
    drinking_rate           DOUBLE       NOT NULL, -- 한국인 평균 음주율
    exercise_rate           DOUBLE       NOT NULL, -- 한국인 평균 운동 실천율
    summary_evaluation      TEXT,                  -- 종합 평가 내용 (TEXT)
    improvement_suggestions TEXT,                  -- 개선 방법 (TEXT)
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (family_id) REFERENCES family_info (id) ON DELETE CASCADE
);

CREATE TABLE sanatorium_data
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    address    VARCHAR(255) NOT NULL,
    region     VARCHAR(255),
    sub_region VARCHAR(255)
);

CREATE TABLE address_code
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    code    VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255)        NOT NULL

);

CREATE TABLE reservation
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    reserver_name   VARCHAR(255) NOT NULL,
    hospital_id BIGINT       NOT NULL,
    time        DATETIME     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (hospital_id) REFERENCES hospital_data (id) ON DELETE CASCADE
);
```