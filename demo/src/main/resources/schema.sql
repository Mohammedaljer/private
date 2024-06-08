CREATE TABLE IF NOT EXISTS
customer (id int AUTO_INCREMENT PRIMARY KEY,
first_name VARCHAR(255) NOT NULL,
last_name VARCHAR(255) NOT NULL,
data VARCHAR(255) NOT NULL,
CONSTRAINT unique_customer_name UNIQUE (first_name, last_name)
);