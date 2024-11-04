CREATE TABLE brand
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_brand PRIMARY KEY (id)
);

CREATE TABLE buyer
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(255),
    CONSTRAINT pk_buyer PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    order_id        BIGINT                                  NOT NULL,
    product_item_id BIGINT                                  NOT NULL,
    quantity        INTEGER,
    CONSTRAINT pk_orderitem PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    sales_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    status     VARCHAR(255)                            NOT NULL,
    buyer_id   BIGINT                                  NOT NULL,
    user_id    BIGINT                                  NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE product
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(255)                            NOT NULL,
    price       FLOAT                                   NOT NULL,
    brand_id    BIGINT                                  NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE product_item
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name       VARCHAR(255)                            NOT NULL,
    quantity   INTEGER                                 NOT NULL,
    size_id    BIGINT                                  NOT NULL,
    product_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_productitem PRIMARY KEY (id)
);

CREATE TABLE role
(
    id   BIGINT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE size
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_size PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username VARCHAR(255)                            NOT NULL,
    password VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    user_id  BIGINT NOT NULL,
    roles_id BIGINT NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, roles_id)
);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDERITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDERITEM_ON_PRODUCT_ITEM FOREIGN KEY (product_item_id) REFERENCES product_item (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_BUYER FOREIGN KEY (buyer_id) REFERENCES buyer (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE product_item
    ADD CONSTRAINT FK_PRODUCTITEM_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE product_item
    ADD CONSTRAINT FK_PRODUCTITEM_ON_SIZE FOREIGN KEY (size_id) REFERENCES size (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_BRAND FOREIGN KEY (brand_id) REFERENCES brand (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_id) REFERENCES role (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);