#Категории
CREATE TABLE `iqmanager`.`category` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `id_parent` BIGINT(19) NOT NULL,
    `final` BOOLEAN NOT NULL DEFAULT FALSE,
    `hidden` BOOLEAN NOT NULL DEFAULT FALSE,
    `image` varchar(50) NULL,
    `blur_image` VARCHAR(100) NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `parent_x_child_category` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `parent_id` BIGINT(19) NOT NULL,
    `child_id` BIGINT(19) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_parent_x_child_category_parent`
    FOREIGN KEY (`parent_id`)
    REFERENCES `category` (`id`),
    CONSTRAINT `fk_parent_x_child_category_child`
    FOREIGN KEY (`child_id`)
    REFERENCES `category` (`id`)
);


CREATE TABLE `iqmanager`.`category_name`(
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(45) NOT NULL,
    `language` VARCHAR(10) NOT NULL,
    `id_category` BIGINT(19) NOT NULL,
    CONSTRAINT fk_category_name FOREIGN KEY (id_category)  REFERENCES category (id)
);

# Комментарии
CREATE TABLE `iqmanager`.`comment` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `user_name` VARCHAR(50) NOT NULL,
    `text` LONGTEXT NULL,
    `stars` TINYINT NOT NULL,
    `date` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
);

# Доп. услуги
CREATE TABLE `iqmanager`.`extra` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `type` VARCHAR(45) NOT NULL,
    `title` LONGTEXT NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `iqmanager`.`rates_and_services` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(45) NOT NULL,
    `price` BIGINT(5) NOT NULL,
    `image` VARCHAR(100) NULL
);



# Элемент заказа
CREATE TABLE `iqmanager`.`order_element` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `date_order` DATETIME NULL,
    `left_to_pay` BIGINT(8) NULL,
    `date_event` DATETIME NULL,
    `order_price` BIGINT(6) NOT NULL,
    `price_delivery` BIGINT(6) NOT NULL,
    `duration` FLOAT DEFAULT 0,
    `people` BIGINT(4) DEFAULT 0,
    `quantity` BIGINT(4) DEFAULT 0,
    `address` VARCHAR(250) NULL,
    `status` VARCHAR(45) DEFAULT 'WAITING_RESPONSE',
    `event_title` VARCHAR(50) NOT NULL,
    `comment` LONGTEXT NULL,
    PRIMARY KEY (`id`)
);



# Детали оплаты
CREATE TABLE `iqmanager`.`payment` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `pay_date` DATETIME NOT NULL,
    `payment_method` VARCHAR(50) NOT NULL,
    `payment_number` VARCHAR(50) NOT NULL,
    `document_number` VARCHAR(50) NOT NULL,
    `payment_status` VARCHAR(50) NOT NULL,
    `price` BIGINT(8) NOT NULL,
    `paid_interest` DOUBLE NOT NULL,
    PRIMARY KEY (`id`)
);

#Объявление
CREATE TABLE `iqmanager`.`post` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `image` VARCHAR(100) NULL,
    `zip_image` VARCHAR(100) NULL,
    `blur_image` VARCHAR(100) NULL,
    `currency` VARCHAR(50) DEFAULT 'RUB',
    `price` BIGINT(7) NOT NULL,
    `price_in_rubles` BIGINT(7) DEFAULT 0,
    `price_before_calendar` BIGINT(7) DEFAULT 0,
    `price_for_delivery_fix` BIGINT(7) DEFAULT 0,
    `price_for_delivery_km` BIGINT(7) DEFAULT 0,
    `title` LONGTEXT NOT NULL,
    `views` BIGINT(19) DEFAULT 0,
    `like` BOOLEAN DEFAULT FALSE,
    `stars` TINYINT DEFAULT 0,
    `status` BOOLEAN DEFAULT FALSE,
    `post_type` VARCHAR(50) NOT NULL,
    `payment_type` VARCHAR(50) NOT NULL,
    `delivery_type` VARCHAR(100) NOT NULL,
    `prepayment` TINYINT NOT NULL,
    `country` VARCHAR(50) NOT NULL,
    `region` VARCHAR(50) NOT NULL,
    `address` VARCHAR(100) NOT NULL,
    `city` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `iqmanager`.`conditions`(
  `id` BIGINT(19)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `min_time` TINYINT NULL,
  `max_time` TINYINT NULL,
  `radius` BIGINT(19) DEFAULT 0,
  `min_person` TINYINT NULL,
  `max_person` TINYINT NULL,
  `ask_for_the_address` BOOLEAN DEFAULT FALSE,
  `ask_the_number_of_people` BOOLEAN DEFAULT FALSE,
  `ask_the_duration` BOOLEAN DEFAULT FALSE,
  `additional_price` BIGINT(19),
  `visible` BOOLEAN DEFAULT FALSE,
  `ransom_price` BIGINT(10) DEFAULT 100
);


# Таблица данных входа пользователя
CREATE TABLE `iqmanager`.`user_login_data` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `registration_type` VARCHAR(45) NULL,
    `phone` VARCHAR(15) NOT NULL UNIQUE,
    `password` VARCHAR(150) NULL,
    PRIMARY KEY (`id`));

# Данные пользователя
CREATE TABLE `iqmanager`.`user_data` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NULL,
    `last_name` VARCHAR(45) NULL,
    `email` VARCHAR(50) NULL,
    `web` VARCHAR(80) NULL,
    `agent` BOOLEAN DEFAULT FALSE,
    `currency` VARCHAR(3) DEFAULT 'RUB',
    `country` VARCHAR(80) NULL,
    `region` VARCHAR(80) NULL,
    `city` VARCHAR(80) NULL,
    `user_login_id` BIGINT(19) NOT NULL,

--     НОВОЕ
--     `user_role` VARCHAR(80) NOT NULL,

    CONSTRAINT login_user_data_fk FOREIGN KEY (user_login_id)  REFERENCES user_login_data (id) ,
    PRIMARY KEY (`id`));

# Картинки к посту
CREATE TABLE `iqmanager`.`images` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `path` VARCHAR(60) NOT NULL,
    `id_post` BIGINT(19) NOT NULL,
    CONSTRAINT fk_img_post FOREIGN KEY (id_post)  REFERENCES post (id) ,
    PRIMARY KEY (`id`));

# Видео к посту
CREATE TABLE `iqmanager`.`video` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `path` VARCHAR(60) NOT NULL,
    `id_post` BIGINT(19) NOT NULL,
    CONSTRAINT fk_video_post FOREIGN KEY (id_post)  REFERENCES post (id) ,
    PRIMARY KEY (`id`));

# Документы к посту
CREATE TABLE `iqmanager`.`pdf` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(60) NULL,
    `path` VARCHAR(60) NOT NULL,
    `id_post` BIGINT(19) NOT NULL,
    CONSTRAINT fk_pdf_post FOREIGN KEY (id_post)  REFERENCES post (id) ,
    PRIMARY KEY (`id`));




CREATE TABLE `iqmanager`.`category_x_post` (
    category_id bigint(19) NOT NULL,
    post_id bigint(19) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (post_id) REFERENCES post (id),
    PRIMARY KEY (category_id, post_id)
);

# Корзина
CREATE TABLE `iqmanager`.`basket` (
    `id_user_data` BIGINT(19) NOT NULL,
    `id_order_element` BIGINT(19) NOT NULL,
    CONSTRAINT fk_basket_user_id FOREIGN KEY (id_user_data) REFERENCES `user_data`(id),
    CONSTRAINT fk_basket_order_element_id FOREIGN KEY (id_order_element) REFERENCES `order_element`(id));

# Избранное
CREATE TABLE `iqmanager`.`favorites`(
    `id_user_data` BIGINT(19) NOT NULL,
    `id_post` BIGINT(19) NOT NULL,
    CONSTRAINT fk_favorites_user_id FOREIGN KEY (id_user_data) REFERENCES `user_data`(id),
    CONSTRAINT fk_favorites_post_id FOREIGN KEY (id_post) REFERENCES `post`(id));


# Доп. услуги к заказу
CREATE TABLE `iqmanager`.`order_extra`(
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(45) NOT NULL,
    `title` LONGTEXT NOT NULL
    );

CREATE TABLE `iqmanager`.`order_extra_service` (
    `order_extra_id` BIGINT(19) NOT NULL,
    `service_id` BIGINT(19) NOT NULL,
    CONSTRAINT fk_order_extra_id FOREIGN KEY (order_extra_id) REFERENCES `order_extra`(id),
    CONSTRAINT fk_service_id FOREIGN KEY (service_id) REFERENCES `rates_and_services`(id),
    UNIQUE (order_extra_id, service_id)
);


#Данные для входа имполнителя
CREATE TABLE `iqmanager`.`performer_login_data` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(45) NOT NULL UNIQUE,
    `password` VARCHAR(150) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `iqmanager`.`master` (
  `id` BIGINT(19) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL,
  `code` VARCHAR(45) NOT NULL UNIQUE
);

# Исполнитель
CREATE TABLE `iqmanager`.`performer_data` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NUll,
    `last_name` VARCHAR(45) NULL,
    `email` VARCHAR(100) NULL,
    `wallet` BIGINT(8) DEFAULT 0,
    `phone` VARCHAR(45) NULL,
    `web` VARCHAR(45) NULL,
    `master` BIGINT(6) DEFAULT 0,
    PRIMARY KEY (`id`)
);

#Календарь
CREATE TABLE `iqmanager`.`calendar` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `begin_date` DATETIME NOT NULL,
    `end_date` DATETIME NOT NULL,
    `change_price` TINYINT NULL,
    `used`         INTEGER(5) NULL,
    `status` VARCHAR(45) NOT NULL,
    `order_id` BIGINT(19) NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`order_id`) REFERENCES `order_element`(`id`)
);


#Данные входа админа
CREATE TABLE `iqmanager`.`login_admin_data`(
    `id` BIGINT(19)  NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(45) NOT NULL,
    `password` VARCHAR(150) NOT NULL,
    `name` VARCHAR(45) NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `iqmanager`.`purchased_phone`(
    `user_id` BIGINT(19) NOT NULL,
    `post_id` BIGINT(19) NOT NULL,
    CONSTRAINT fk_user_id_phone FOREIGN KEY (user_id) REFERENCES `user_data`(id),
    CONSTRAINT fk_performer_id_phone FOREIGN KEY (post_id) REFERENCES `post`(id),
    UNIQUE (user_id, post_id)
);

#Данные админа
CREATE TABLE `iqmanager`.`admin_data` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NULL,
    `last_name` VARCHAR(45) NULL,
    `phone` VARCHAR(45) NULL,
    `admin_login_id` BIGINT(19) NOT NULL,
    CONSTRAINT login_admin_data_fk FOREIGN KEY (admin_login_id)  REFERENCES login_admin_data (id) ,
    PRIMARY KEY (`id`));


CREATE TABLE `iqmanager`.`admin_role`(
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `role` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `iqmanager`.`admin_x_role`(
    `id_admin` BIGINT(19) NOT NULL,
    `id_role` BIGINT(19) NOT NULL,
    CONSTRAINT fk_admin_data FOREIGN KEY (id_admin) REFERENCES `login_admin_data`(id),
    CONSTRAINT fk_role FOREIGN KEY (id_role) REFERENCES `admin_role`(id),
    UNIQUE (id_admin,id_role)
);

#SEO
CREATE TABLE `iqmanager`.`seo_posts` (
  `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
  `title` LONGTEXT NULL,
  `description` LONGTEXT NULL,
  `keywords` LONGTEXT NULL,
  `robots` VARCHAR(45) NULL,
  `content_type` LONGTEXT NULL,
  `canonical` LONGTEXT NULL,
  `post_id` BIGINT(19) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_seo_post FOREIGN KEY (post_id) REFERENCES `post`(id)
);
#SEO
CREATE TABLE `iqmanager`.`seo_categories` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `title` LONGTEXT NULL,
    `description` LONGTEXT NULL,
    `keywords` LONGTEXT NULL,
    `robots` VARCHAR(45) NULL,
    `content_type` LONGTEXT NULL,
    `canonical` LONGTEXT NULL,
    `category_id` BIGINT(19) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT fk_seo_categories FOREIGN KEY (category_id) REFERENCES `category`(id)
);

CREATE TABLE `iqmanager`.`contract` (
  `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
  `date_conclusion_contract` DATETIME NULL,
  `number_contract` BIGINT(8) NULL,
  `type` VARCHAR(45) NOT NULL,
  `activities` VARCHAR(45) NOT NULL,
  `contract_kind` VARCHAR(45)  NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `patronymic` VARCHAR(45) NOT NULL,
  `mail` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100)  NULL,
  `series` VARCHAR(45) NULL,
  `number` VARCHAR(45) NULL,
  `who_issued` VARCHAR(45) NULL,
  `when_issued` VARCHAR(45) NULL,
  `unit_code` VARCHAR(45) NULL,
  `inn` VARCHAR(45) NULL,
  `address_org` VARCHAR(100) NULL,
  `bank` VARCHAR(45) NULL,
  `settlement_account` VARCHAR(45) NULL,
  `correspondent_account` VARCHAR(45) NULL,
  `bik` VARCHAR(45) NULL,
  `ogrn` VARCHAR(45) NULL,
  `kpp` VARCHAR(45) NULL,
  `phone_org` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  `country` VARCHAR(45) NULL,
  `region` VARCHAR(45) NULL,
  `full_name` VARCHAR(45) NULL,
  `ip_name` VARCHAR(45) NULL,
  `legal_status` VARCHAR(45) NULL,
  `acting_on` VARCHAR(45) NULL,
  `full_name_org` VARCHAR(45) NULL,
  `address_registration` VARCHAR(100) NULL,
  `passport_main` VARCHAR(100) NOT NULL,
  `passport_registration` VARCHAR(100) NOT NULL,
  `passport_with_person` VARCHAR(100) NOT NULL,
  `signature` VARCHAR(100) NULL,
  `path` VARCHAR(100) NOT NULL,
  `performer_id` BIGINT(19),
  PRIMARY KEY (`id`),
  CONSTRAINT fk_contract_performer_id FOREIGN KEY (performer_id) REFERENCES `performer_data`(id)
);

CREATE TABLE currency(
  `id` VARCHAR(10) PRIMARY KEY NOT NULL,
  `attitude_to_ruble` BIGINT(6) NULL
);


CREATE TABLE `contract_annex` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `path` VARCHAR(255) NOT NULL,
    `type` VARCHAR(255) NOT NULL,
    `contract_id` BIGINT,
    FOREIGN KEY (`contract_id`) REFERENCES `contract`(`id`) ON DELETE SET NULL
);

CREATE TABLE country(
    `id` BIGINT NOT NULL PRIMARY KEY ,
    `name` VARCHAR(128) NOT NULL);

create table region(
    `id` BIGINT not null primary key,
    `country_id` BIGINT not null REFERENCES country (id),
    `name` VARCHAR(128) NOT NULL);

create table city(
    `id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `region_id` BIGINT not null REFERENCES region (id),
    `name` VARCHAR(128) NOT NULL);


CREATE TABLE request(
    `id` BIGINT(19) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `comment` LONGTEXT NULL,
    `date_order` DATETIME NOT NULL,
    `image` VARCHAR(60)
);

CREATE TABLE photo_report(
    `id` BIGINT(19) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `path` VARCHAR(100) NOT NULL
);

CREATE TABLE banner (
    `id` BIGINT(19) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `title`  VARCHAR(255),
    `link_presence` BOOLEAN,
    `link_address` VARCHAR(255),
    `link_inscription` VARCHAR(255),
    `serial_number` INT NOT NULL,
    `banner_visible` BOOLEAN NOT NULL,
    `banner_background` VARCHAR(255)
);

CREATE TABLE promo (
    `id` BIGINT(19) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`  VARCHAR(255),
    `status` VARCHAR(255) NOT NULL,
    `date` DATETIME,
    `rules` VARCHAR(255)
);

CREATE TABLE images_x_promo (
    `id` BIGINT(19) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `path` VARCHAR(60) NOT NULL,
    `id_promo` BIGINT(19) NOT NULL,
    CONSTRAINT fk_img_promo FOREIGN KEY (id_promo)  REFERENCES promo (id)
);



ALTER TABLE order_extra ADD COLUMN `order_element_id` BIGINT(19) NULL;
ALTER TABLE order_extra ADD CONSTRAINT fk_order_extra_order_element FOREIGN KEY (order_element_id) REFERENCES `order_element`(id);

ALTER TABLE performer_data ADD COLUMN  `contract_id` BIGINT(19) NULL;
ALTER TABLE performer_data ADD CONSTRAINT fk_contract_id FOREIGN KEY (contract_id) REFERENCES `contract`(id);

ALTER TABLE rates_and_services ADD COLUMN `extra_id` BIGINT(19) NULL;
ALTER TABLE rates_and_services ADD CONSTRAINT fk_extra_rates_and_services FOREIGN KEY (extra_id) REFERENCES `extra`(id);

ALTER TABLE performer_data ADD COLUMN `performer_login_data_id` BIGINT(19) NOT NULL;
ALTER TABLE performer_data ADD CONSTRAINT fk_performer_login_data FOREIGN KEY (performer_login_data_id) REFERENCES `performer_login_data`(id);

ALTER TABLE comment ADD COLUMN `post_id` BIGINT(19) NOT NULL;
ALTER TABLE comment ADD CONSTRAINT fk_post_comment_id FOREIGN KEY (post_id) REFERENCES `post`(id);

ALTER TABLE post ADD COLUMN `id_performer` BIGINT(19) NOT NULL;
ALTER TABLE post ADD CONSTRAINT fk_post_performer_id FOREIGN KEY (id_performer) REFERENCES `performer_data`(id);

ALTER TABLE conditions ADD COLUMN `post_id` BIGINT(19) NOT NULL;
ALTER TABLE conditions ADD CONSTRAINT fk_post_conditions_id FOREIGN KEY (post_id) REFERENCES `post`(id);

ALTER TABLE payment
    ADD COLUMN `order_element_id` BIGINT(19) NOT NULL;
ALTER TABLE payment
    ADD CONSTRAINT fk_payment_order_id FOREIGN KEY (order_element_id) REFERENCES `order_element` (id);

ALTER TABLE `iqmanager`.`order_element`
    ADD COLUMN `calendar_id` BIGINT(19) NULL;

-- Устанавливаем внешний ключ, связывающий calendar_id с id таблицы calendar
ALTER TABLE `iqmanager`.`order_element`
    ADD CONSTRAINT `fk_order_element_calendar`
        FOREIGN KEY (`calendar_id`) REFERENCES `calendar`(`id`);



ALTER TABLE `order_element`
    ADD COLUMN `post_id` BIGINT(19) NOT NULL;
ALTER TABLE `order_element`
    ADD CONSTRAINT fk_post_order_element FOREIGN KEY (post_id) REFERENCES `post` (id);


ALTER TABLE `extra`
    ADD COLUMN `post_id` BIGINT(19) NOT NULL;
ALTER TABLE `extra`
    ADD CONSTRAINT fk_extra_x_post FOREIGN KEY (post_id) REFERENCES `post` (id);

ALTER TABLE `calendar`
    ADD COLUMN `performer_id` BIGINT(19) NOT NULL;
ALTER TABLE `calendar`
    ADD CONSTRAINT fk_calendar_x_performer FOREIGN KEY (performer_id) REFERENCES `performer_data` (id);



INSERT INTO user_login_data(`id`, `registration_type`, `phone`, `password`)
VALUES (1, 'DEFAULT', '+79112223344', '$2a$10$btJJkmOFgsBuR.raCGev9uK/7NAaUQ5/lht0kmYKiI9N26Qt2drh6');
INSERT INTO user_data(`id`, `name`, `user_login_id`)
VALUES (1, 'IqManager', 1);



CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(200) NOT NULL,
    CALENDAR      BLOB         NOT NULL
);

CREATE TABLE QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(120) NOT NULL,
    TIME_ZONE_ID    VARCHAR(80)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    ENTRY_ID          VARCHAR(95)  NOT NULL,
    TRIGGER_NAME      VARCHAR(200) NOT NULL,
    TRIGGER_GROUP     VARCHAR(200) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    FIRED_TIME        BIGINT       NOT NULL,
    SCHED_TIME        BIGINT       NOT NULL,
    PRIORITY          INTEGER      NOT NULL,
    STATE             VARCHAR(16)  NOT NULL,
    JOB_NAME          VARCHAR(200) NULL,
    JOB_GROUP         VARCHAR(200) NULL,
    IS_NONCONCURRENT  BOOLEAN NULL,
    REQUESTS_RECOVERY BOOLEAN NULL
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL
);

CREATE TABLE QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT       NOT NULL,
    CHECKIN_INTERVAL  BIGINT       NOT NULL
);

CREATE TABLE QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40)  NOT NULL
);

CREATE TABLE QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    JOB_NAME          VARCHAR(200) NOT NULL,
    JOB_GROUP         VARCHAR(200) NOT NULL,
    DESCRIPTION       VARCHAR(250) NULL,
    JOB_CLASS_NAME    VARCHAR(250) NOT NULL,
    IS_DURABLE        BOOLEAN      NOT NULL,
    IS_NONCONCURRENT  BOOLEAN      NOT NULL,
    IS_UPDATE_DATA    BOOLEAN      NOT NULL,
    REQUESTS_RECOVERY BOOLEAN      NOT NULL,
    JOB_DATA          BLOB NULL
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR (200)  NOT NULL ,
    TRIGGER_GROUP VARCHAR (200)  NOT NULL ,
    REPEAT_COUNT BIGINT NOT NULL ,
    REPEAT_INTERVAL BIGINT NOT NULL ,
    TIMES_TRIGGERED BIGINT NOT NULL
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1    VARCHAR(512) NULL,
    STR_PROP_2    VARCHAR(512) NULL,
    STR_PROP_3    VARCHAR(512) NULL,
    INT_PROP_1    INTEGER NULL,
    INT_PROP_2    INTEGER NULL,
    LONG_PROP_1   BIGINT NULL,
    LONG_PROP_2   BIGINT NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   BOOLEAN NULL,
    BOOL_PROP_2   BOOLEAN NULL
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA     BLOB NULL
);

CREATE TABLE QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR(120) NOT NULL,
    TRIGGER_NAME   VARCHAR(200) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    JOB_NAME       VARCHAR(200) NOT NULL,
    JOB_GROUP      VARCHAR(200) NOT NULL,
    DESCRIPTION    VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT NULL,
    PREV_FIRE_TIME BIGINT NULL,
    PRIORITY       INTEGER NULL,
    TRIGGER_STATE  VARCHAR(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
    START_TIME     BIGINT       NOT NULL,
    END_TIME       BIGINT NULL,
    CALENDAR_NAME  VARCHAR(200) NULL,
    MISFIRE_INSTR  SMALLINT NULL,
    JOB_DATA       BLOB NULL
);

ALTER TABLE QRTZ_CALENDARS
    ADD CONSTRAINT PK_QRTZ_CALENDARS PRIMARY KEY (
                                                  SCHED_NAME,
                                                  CALENDAR_NAME
        );

ALTER TABLE QRTZ_CRON_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_CRON_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_FIRED_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_FIRED_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             ENTRY_ID
                );

ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS
    ADD
        CONSTRAINT PK_QRTZ_PAUSED_TRIGGER_GRPS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_SCHEDULER_STATE
    ADD
        CONSTRAINT PK_QRTZ_SCHEDULER_STATE PRIMARY KEY
            (
             SCHED_NAME,
             INSTANCE_NAME
                );

ALTER TABLE QRTZ_LOCKS
    ADD
        CONSTRAINT PK_QRTZ_LOCKS PRIMARY KEY
            (
             SCHED_NAME,
             LOCK_NAME
                );

ALTER TABLE QRTZ_JOB_DETAILS
    ADD
        CONSTRAINT PK_QRTZ_JOB_DETAILS PRIMARY KEY
            (
             SCHED_NAME,
             JOB_NAME,
             JOB_GROUP
                );

ALTER TABLE QRTZ_SIMPLE_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_SIMPLE_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_SIMPROP_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_SIMPROP_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_TRIGGERS
    ADD
        CONSTRAINT PK_QRTZ_TRIGGERS PRIMARY KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                );

ALTER TABLE QRTZ_CRON_TRIGGERS
    ADD
        CONSTRAINT FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                ) REFERENCES QRTZ_TRIGGERS (
                                            SCHED_NAME,
                                            TRIGGER_NAME,
                                            TRIGGER_GROUP
                ) ON DELETE CASCADE;


ALTER TABLE QRTZ_SIMPLE_TRIGGERS
    ADD
        CONSTRAINT FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                ) REFERENCES QRTZ_TRIGGERS (
                                            SCHED_NAME,
                                            TRIGGER_NAME,
                                            TRIGGER_GROUP
                ) ON DELETE CASCADE;

ALTER TABLE QRTZ_SIMPROP_TRIGGERS
    ADD
        CONSTRAINT FK_QRTZ_SIMPROP_TRIGGERS_QRTZ_TRIGGERS FOREIGN KEY
            (
             SCHED_NAME,
             TRIGGER_NAME,
             TRIGGER_GROUP
                ) REFERENCES QRTZ_TRIGGERS (
                                            SCHED_NAME,
                                            TRIGGER_NAME,
                                            TRIGGER_GROUP
                ) ON DELETE CASCADE;


ALTER TABLE QRTZ_TRIGGERS
    ADD
        CONSTRAINT FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS FOREIGN KEY
            (
             SCHED_NAME,
             JOB_NAME,
             JOB_GROUP
                ) REFERENCES QRTZ_JOB_DETAILS (
                                               SCHED_NAME,
                                               JOB_NAME,
                                               JOB_GROUP
                );

CREATE TABLE `scheduler_job_info`
(
    `id`              VARCHAR(120) PRIMARY KEY,
    `job_name`        VARCHAR(120),
    `job_group`       VARCHAR(120),
    `job_status`      VARCHAR(120),
    `job_class`       VARCHAR(120),
    `cron_expression` VARCHAR(120),
    `description`     VARCHAR(120),
    `interface_name`  VARCHAR(120),
    `repeat_time`     BIGINT(19),
    `cron_job`        BOOLEAN
);
