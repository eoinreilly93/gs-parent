CREATE TABLE product (
   product_id INT NOT NULL,
   name VARCHAR(255) NOT NULL,
   price DECIMAL NOT NULL,
   stock_status VARCHAR(20) NOT NULL,
   stock_count INT NOT NULL,
   CONSTRAINT pk_product PRIMARY KEY (product_id)
);