CREATE TABLE IF NOT EXISTS customer_summary(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    customer_id BIGINT NOT NULL,
    unique_products INT NOT NULL,
    total_products INT NOT NULL,
    total_price BIGINT NOT NULL
);