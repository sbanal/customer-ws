
CREATE TABLE IF NOT EXISTS customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    title VARCHAR(32) NOT NULL DEFAULT '',
    first_name VARCHAR(128) NOT NULL,
    middle_name VARCHAR(128) NOT NULL,
    surname VARCHAR(128) NOT NULL,
    initials VARCHAR(128) NOT NULL,
    gender VARCHAR(2) NOT NULL,
    marital_status VARCHAR(32) NOT NULL,
    existing_customer BOOLEAN NOT NULL,
    credit_rating TINYINT NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS first_name_IDX ON customer(first_name);
CREATE INDEX IF NOT EXISTS surname_IDX ON customer(surname);
CREATE INDEX IF NOT EXISTS middle_name_IDX ON customer(middle_name);

CREATE TABLE IF NOT EXISTS address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    customer_id BIGINT NOT NULL,
    address_type VARCHAR(2) NOT NULL,
    street_type VARCHAR(128) NOT NULL,
    street_name VARCHAR(128) NOT NULL,
    suburb VARCHAR(128) NOT NULL,
    city VARCHAR(128) NOT NULL,
    state VARCHAR(5) NOT NULL,
    country VARCHAR(128) NOT NULL,
    postal_code VARCHAR(128) NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY address_type_UNIQUE (customer_id, address_type),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);


INSERT INTO customer VALUES(1, 'Mr', 'stephen', 'banal', 'b', 'slbb', 'M', 'M', false, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO customer VALUES(2, 'Dr', 'joe', 'go', 'a', 'jga', 'M', 'S', true, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO address VALUES(1, 1, 'R', 'St', 'Some Street Name', 'Suburb name', 'Melbourne', 'VIC', 'AU','3000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO address VALUES(2, 1, 'M', 'Rd', 'Some mailing street Name', 'Mailing suburb', 'Sydney', 'NSW', 'AU','2000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
