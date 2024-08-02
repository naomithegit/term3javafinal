-- insert_data.sql
INSERT INTO Users (username, password, email, role) VALUES 
('buyer1', '$2a$12$D4G5f18oG5f6Rh7P9W5H1uQ7Yq5J6Y2', 'buyer1@example.com', 'buyer'),
('buyer2', '$2a$12$E1A1A3H1X1Q4B2R7X5Z8I1R4A2B9G5U1', 'buyer2@example.com', 'buyer'),
('seller1', '$2a$12$F1D8D9G7J8F9L3M4N2P5Q6T7H9B2Z7L1', 'seller1@example.com', 'seller'),
('seller2', '$2a$12$G1H7I6L5M4N3P8Q2R7S6T1V2W9X5Y3K1', 'seller2@example.com', 'seller'),
('admin1', '$2a$12$H1J6K5L4M3N8P2Q1R7S6T5U4V9W2X3Y1', 'admin1@example.com', 'admin'),
('admin2', '$2a$12$I1K5L4M3N7P2Q1R6S8T5U4V2W3X9Y5A1', 'admin2@example.com', 'admin');

INSERT INTO Products (name, price, quantity, seller_id) VALUES 
('Product1', 10.99, 100, 3),
('Product2', 15.99, 50, 3),
('Product3', 20.99, 30, 4),
('Product4', 25.99, 20, 4),
('Product5', 30.99, 10, 3),
('Product6', 35.99, 5, 4),
('Product7', 40.99, 8, 3),
('Product8', 45.99, 15, 4),
('Product9', 50.99, 25, 3),
('Product10', 55.99, 40, 4);
