CREATE TABLE IF NOT EXISTS `member`
(
    `id`         BIGINT       NOT NULL auto_increment,
    `name`       VARCHAR(255) NOT NULL,
    `email`      VARCHAR(255) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `image`      VARCHAR(255),
    `course`     VARCHAR(255) NOT NULL,
    `state`      VARCHAR(255) NOT NULL,
    `sleep_date` DATE,
    `created_at` TIMESTAMP    NOT NULL,
    `updated_at` TIMESTAMP,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `badge`
(
    `id`         BIGINT       NOT NULL auto_increment,
    `name`       VARCHAR(255) NOT NULL,
    `member_id`  BIGINT       NOT NULL,
    `created_at` TIMESTAMP    NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `subject`
(
    `id`              BIGINT       NOT NULL auto_increment,
    `name`            VARCHAR(255) NOT NULL,
    `member_id`       BIGINT       NOT NULL,
    `study_record_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `study_record`
(
    `id`        BIGINT    NOT NULL auto_increment,
    `time`      MEDIUMINT NOT NULL,
    `date`      DATE      NOT NULL,
    `member_id` BIGINT    NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `statistics`
(
    `id`           BIGINT NOT NULL auto_increment,
    `monthly_time` MEDIUMINT,
    `weekly_time`  MEDIUMINT,
    `daily_time`   MEDIUMINT,
    `total_time`   INT,
    `member_id`    BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `study_subject`
(
    `id`              BIGINT NOT NULL auto_increment,
    `study_record_id` BIGINT NOT NULL,
    `subject_id`      BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

