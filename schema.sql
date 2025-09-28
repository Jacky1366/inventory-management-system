CREATE TABLE Supplier (
  supplier_id   INT          NOT NULL AUTO_INCREMENT,
  name          VARCHAR(100) NOT NULL,
  contact_name  VARCHAR(100) NULL,
  phone         VARCHAR(20)  NULL,
  address       VARCHAR(255) NULL,
  PRIMARY KEY (supplier_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;


  CREATE TABLE Product (
  product_id   INT           NOT NULL AUTO_INCREMENT,
  name         VARCHAR(100)  NOT NULL,
  description  TEXT          NULL,
  unit_price   DECIMAL(10,2) NOT NULL,
  supplier_id  INT           NOT NULL,
  PRIMARY KEY (product_id),
  CONSTRAINT fk_product_supplier
    FOREIGN KEY (supplier_id)
    REFERENCES Supplier(supplier_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;


CREATE TABLE Customer (
  customer_id   INT           NOT NULL AUTO_INCREMENT,
  name          VARCHAR(100)  NOT NULL,
  email         VARCHAR(100)  NOT NULL UNIQUE,
  phone         VARCHAR(20)   NULL,
  address       VARCHAR(255)  NULL,
  PRIMARY KEY (customer_id)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE `Order` (
  order_id      INT           NOT NULL AUTO_INCREMENT,
  order_date    DATE          NOT NULL,
  customer_id   INT           NOT NULL,
  total_amount  DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (order_id),
  CONSTRAINT fk_order_customer
    FOREIGN KEY (customer_id)
    REFERENCES Customer(customer_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;



  CREATE TABLE Order_Item (
  order_id    INT           NOT NULL,
  product_id  INT           NOT NULL,
  quantity    INT           NOT NULL,
  unit_price  DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (order_id, product_id),
  CONSTRAINT fk_orderitem_order
    FOREIGN KEY (order_id)
    REFERENCES `Order`(order_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_orderitem_product
    FOREIGN KEY (product_id)
    REFERENCES Product(product_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;





-- 1. Suppliers
INSERT INTO Supplier (name, contact_name, phone, address) VALUES
  ('Acme Corp',      'John Doe',    '555-1234',    '123 Main St, Springfield'),
  ('Global Supplies','Jane Smith',  '555-5678',    '456 Oak Ave, Rivertown'),
  ('Fresh Produce',  'Carlos Ruiz', '555-9012',    '789 Pine Rd, Farmville');

-- 2. Products
INSERT INTO Product (name, description, unit_price, supplier_id) VALUES
  ('Widget A', 'Standard widget',       9.99,  1),
  ('Widget B', 'Deluxe widget',        14.99,  1),
  ('Gadget X', 'High-tech gadget',     29.95,  2),
  ('Gadget Y', 'Budget gadget',        19.95,  2),
  ('Apple',    'Fresh red apples (lb)', 1.50,  3),
  ('Banana',   'Yellow bananas (lb)',   1.20,  3);

-- 3. Customers
INSERT INTO Customer (name, email,           phone,      address) VALUES
  ('Alice Wong',   'alice@example.com', '555-0001', '12 Elm St, Citytown'),
  ('Bob Johnson',  'bob@example.com',   '555-0002', '34 Cedar Rd, Village'),
  ('Carol Lee',    'carol@example.com', '555-0003', '56 Birch Ln, Hamlet'),
  ('Dave Martinez','dave@example.com',  '555-0004', '78 Maple Blvd, Metropolis');

-- 4. Orders
INSERT INTO `Order` (
  `order_date`,
  `customer_id`,
  `total_amount`
) VALUES
  ('2025-07-01', 1, 59.85),
  ('2025-07-02', 2, 29.95),
  ('2025-07-03', 1, 19.95),
  ('2025-07-04', 3,  3.00),
  ('2025-07-05', 4, 15.00);

-- 5. Order_Items
INSERT INTO Order_Item (order_id, product_id, quantity, unit_price) VALUES
  (1, 1,  2,  9.99),   -- 2 × Widget A
  (1, 3,  1, 29.95),   -- 1 × Gadget X
  (2, 4,  1, 19.95),   -- 1 × Gadget Y
  (3, 2,  1, 14.99),   -- 1 × Widget B
  (4, 5,  2,  1.50),   -- 2× Apple
  (5, 6,  5,  1.20),   -- 5× Banana
  (5, 1,  1,  9.99),   -- 1 × Widget A
  (1, 2,  1, 14.99);   -- 1 × Widget B


-- grab all the data we put into the tables and display them
-- Show all suppliers
SELECT *
  FROM Supplier;

-- Show all products
SELECT *
  FROM Product;

-- Show all customers
SELECT *
  FROM Customer;

-- Show all orders
SELECT *
  FROM `Order`;

-- Show all order-items
SELECT *
  FROM Order_Item;






-- Change "2" to the supplier_id you want
SELECT *
  FROM Product
 WHERE supplier_id = 2;

-- Change "1" to the customer_id you want
SELECT *
  FROM `Order`
 WHERE customer_id = 1;


-- Change "1" to the order_id you want
SELECT *
  FROM Order_Item
 WHERE order_id = 1;


--list each order's customer and every product on that order
SELECT
  o.order_id,
  o.order_date,
  c.name        AS customer_name,
  p.product_id,
  p.name        AS product_name,
  oi.quantity,
  oi.unit_price
FROM Order_Item oi
JOIN `Order` o    ON oi.order_id    = o.order_id
JOIN Customer c   ON o.customer_id  = c.customer_id
JOIN Product p    ON oi.product_id  = p.product_id
ORDER BY o.order_id, p.product_id;





--Total revenue per product
SELECT
  p.product_id,
  p.name             AS product_name,
  SUM(oi.quantity * oi.unit_price) AS total_revenue
FROM Order_Item oi
JOIN Product p
  ON oi.product_id = p.product_id
GROUP BY p.product_id, p.name
ORDER BY total_revenue DESC;

--Top spending customer
SELECT
  c.customer_id,
  c.name             AS customer_name,
  SUM(o.total_amount) AS total_spent
FROM `Order` o
JOIN Customer c
  ON o.customer_id = c.customer_id
GROUP BY c.customer_id, c.name
ORDER BY total_spent DESC
LIMIT 1;


--Number of orders per customer
SELECT
  c.customer_id,
  c.name             AS customer_name,
  COUNT(*)           AS orders_count
FROM `Order` o
JOIN Customer c
  ON o.customer_id = c.customer_id
GROUP BY c.customer_id, c.name;

--Average order value
SELECT
  AVG(total_amount)  AS avg_order_value
FROM `Order`;

--Products never ordered
SELECT
  p.product_id,
  p.name             AS product_name
FROM Product p
LEFT JOIN Order_Item oi
  ON p.product_id = oi.product_id
WHERE oi.product_id IS NULL;





-- 1. add the order
INSERT INTO `Order` (order_date, customer_id, total_amount)
VALUES ('2025-07-06', 2, 45.00);

-- find the new order_id (e.g. in PopSQL you can SELECT LAST_INSERT_ID();)
-- suppose it returns 6

-- 2. add its line-items
INSERT INTO Order_Item (order_id, product_id, quantity, unit_price) VALUES
  (6, 3, 1, 29.95),
  (6, 5, 10, 1.50);


--Update a product's price
UPDATE Product
   SET unit_price = 11.99
 WHERE product_id = 1;


DELETE FROM `Order`
 WHERE order_date < '2025-07-03';



 CREATE TABLE Product (
  product_id     INT AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(100) NOT NULL,
  description    TEXT,
  unit_price     DECIMAL(10,2) CHECK (unit_price > 0),
  supplier_id    INT NOT NULL,
  FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id)
);
CREATE INDEX idx_product_supplier ON Product(supplier_id);
