CREATE TABLE user
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    email         VARCHAR(255)          NULL,
    password      VARCHAR(255)          NULL,
    first_name    VARCHAR(255)          NULL,
    last_name     VARCHAR(255)          NULL,
    phone_number  VARCHAR(255)          NULL,
    pp_name       VARCHAR(255)          NULL,
    `role`        VARCHAR(255)          NULL,
    refresh_token VARCHAR(255)          NULL,
    active        BIT(1)                NOT NULL,
    `locked`      BIT(1)                NOT NULL,
    expired       BIT(1)                NOT NULL,
    created_at    datetime              NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);
CREATE TABLE product
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NULL,
    sku_code      VARCHAR(255)          NULL,
    `description` VARCHAR(255)          NULL,
    price         DOUBLE                NOT NULL,
    stock         INT                   NOT NULL,
    category      VARCHAR(255)          NULL,
    image_name    VARCHAR(255)          NULL,
    created_at    datetime              NULL,
    seller_id     BIGINT                NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_SELLER FOREIGN KEY (seller_id) REFERENCES user (id);