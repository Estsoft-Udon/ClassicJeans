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
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE
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

CREATE TABLE health_data
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id   BIGINT       NOT NULL,
    family_id BIGINT       NOT NULL,
    date      DATE         NOT NULL, -- 검사 날짜
    content   TEXT         NOT NULL, -- 검사 내용
    analysis  VARCHAR(100) NOT NULL,
    UNIQUE (family_id, date),        -- 중복 검사 방지
    UNIQUE (user_id, date),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (family_id) REFERENCES family_info (id) ON DELETE CASCADE
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

CREATE TABLE sanatorium_data
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255)  NOT NULL,
    address VARCHAR(255) NOT NULL,
    state   VARCHAR(255),
    city    VARCHAR(255)
);

CREATE TABLE address_code
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    code    VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL
);
```