CREATE TABLE orders (
   id SERIAL NOT NULL,
   order_id VARCHAR(255),
   status VARCHAR(15) NOT NULL,
   price DECIMAL NOT NULL,
   product_ids VARCHAR(50),
   CONSTRAINT pk_orders PRIMARY KEY (id)
);