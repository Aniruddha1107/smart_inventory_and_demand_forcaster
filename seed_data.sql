-- ═══════════════════════════════════════════════════════════
-- SMART INVENTORY — SEED DATA
-- Run this AFTER Spring Boot has created all tables
-- ═══════════════════════════════════════════════════════════

USE smart_inventory;

-- ─── 1. USERS ────────────────────────────────────────────
-- Passwords are BCrypt hashed:
--   admin123   → $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
--   manager123 → $2a$10$EqKcp1WFKs2bVsNMO4KOOeTaPCriFZJjA7eQBYna6BQBAbx62q2SK

INSERT INTO users (username, password, email, role, is_active, created_at) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@smartinv.com', 'ADMIN', true, NOW()),
('manager1', '$2a$10$EqKcp1WFKs2bVsNMO4KOOeTaPCriFZJjA7eQBYna6BQBAbx62q2SK', 'manager1@smartinv.com', 'MANAGER', true, NOW()),
('manager2', '$2a$10$EqKcp1WFKs2bVsNMO4KOOeTaPCriFZJjA7eQBYna6BQBAbx62q2SK', 'manager2@smartinv.com', 'MANAGER', true, NOW());

-- ─── 2. PRODUCTS ─────────────────────────────────────────
INSERT INTO products (name, category, price, quantity, safety_stock, sku, is_active) VALUES
('Dell Laptop Inspiron 15',     'Electronics',  54999.00,  25,  5,  'ELEC-DELL-001',   true),
('HP Wireless Mouse',           'Electronics',  799.00,    150, 20, 'ELEC-HP-002',     true),
('Samsung 27" Monitor',         'Electronics',  18999.00,  12,  3,  'ELEC-SAM-003',    true),
('Logitech Keyboard K380',      'Electronics',  2499.00,   80,  15, 'ELEC-LOG-004',    true),
('Office Chair Pro',            'Furniture',    8999.00,   30,  5,  'FURN-CHR-001',    true),
('Standing Desk Adjustable',    'Furniture',    15999.00,  10,  2,  'FURN-DSK-002',    true),
('A4 Copier Paper (500 sheets)','Stationery',   299.00,    500, 50, 'STAT-PPR-001',    true),
('Whiteboard Marker Set (10)',  'Stationery',   199.00,    200, 30, 'STAT-MRK-002',    true),
('USB-C Hub 7-in-1',            'Electronics',  1799.00,   45,  10, 'ELEC-USB-005',    true),
('Desk Lamp LED',               'Furniture',    1299.00,   60,  8,  'FURN-LMP-003',    true),
('Low Stock Pen Drive 64GB',    'Electronics',  499.00,    3,   10, 'ELEC-PEN-006',    true),
('Out of Stock Webcam',         'Electronics',  2999.00,   0,   5,  'ELEC-WEB-007',    true),
('Soft-Deleted Old Printer',    'Electronics',  12999.00,  5,   2,  'ELEC-PRT-008',    false);

-- ─── 3. CUSTOMERS ────────────────────────────────────────
INSERT INTO customers (name, email, recency, frequency, monetary_value, rfm_segment) VALUES
('Rahul Sharma',    'rahul@example.com',    0,  0, 0.00, NULL),
('Priya Patel',     'priya@example.com',    0,  0, 0.00, NULL),
('Amit Kumar',      'amit@example.com',     0,  0, 0.00, NULL),
('Sneha Reddy',     'sneha@example.com',    0,  0, 0.00, NULL),
('Vikram Singh',    'vikram@example.com',   0,  0, 0.00, NULL),
('Ananya Gupta',    'ananya@example.com',   0,  0, 0.00, NULL),
('Rohan Joshi',     'rohan@example.com',    0,  0, 0.00, NULL),
('Deepika Nair',    'deepika@example.com',  0,  0, 0.00, NULL);

-- ─── 4. SALES (historical data for forecasting & RFM) ───
INSERT INTO sales (product_id, customer_id, sale_date, quantity_sold, total_amount) VALUES
-- Rahul Sharma — frequent buyer (Champion candidate)
(1, 1, DATE_SUB(CURDATE(), INTERVAL 5 DAY),  1, 54999.00),
(2, 1, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 2, 1598.00),
(4, 1, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 1, 2499.00),
(9, 1, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 1, 1799.00),
(7, 1, DATE_SUB(CURDATE(), INTERVAL 25 DAY), 5, 1495.00),
(2, 1, DATE_SUB(CURDATE(), INTERVAL 30 DAY), 3, 2397.00),
(10,1, DATE_SUB(CURDATE(), INTERVAL 35 DAY), 1, 1299.00),
(4, 1, DATE_SUB(CURDATE(), INTERVAL 40 DAY), 2, 4998.00),
(7, 1, DATE_SUB(CURDATE(), INTERVAL 50 DAY), 10,2990.00),
(2, 1, DATE_SUB(CURDATE(), INTERVAL 55 DAY), 1, 799.00),
(9, 1, DATE_SUB(CURDATE(), INTERVAL 60 DAY), 2, 3598.00),

-- Priya Patel — moderate buyer (Loyal candidate)
(3, 2, DATE_SUB(CURDATE(), INTERVAL 8 DAY),  1, 18999.00),
(5, 2, DATE_SUB(CURDATE(), INTERVAL 22 DAY), 1, 8999.00),
(2, 2, DATE_SUB(CURDATE(), INTERVAL 45 DAY), 5, 3995.00),
(7, 2, DATE_SUB(CURDATE(), INTERVAL 60 DAY), 3, 897.00),
(10,2, DATE_SUB(CURDATE(), INTERVAL 75 DAY), 2, 2598.00),

-- Amit Kumar — bought long ago (At-Risk / Lost candidate)
(1, 3, DATE_SUB(CURDATE(), INTERVAL 120 DAY), 1, 54999.00),
(6, 3, DATE_SUB(CURDATE(), INTERVAL 150 DAY), 1, 15999.00),
(3, 3, DATE_SUB(CURDATE(), INTERVAL 180 DAY), 1, 18999.00),
(5, 3, DATE_SUB(CURDATE(), INTERVAL 200 DAY), 2, 17998.00),

-- Sneha Reddy — recent single purchase
(2, 4, DATE_SUB(CURDATE(), INTERVAL 3 DAY),  1, 799.00),

-- Vikram Singh — a few purchases spread out
(7, 5, DATE_SUB(CURDATE(), INTERVAL 12 DAY), 10, 2990.00),
(8, 5, DATE_SUB(CURDATE(), INTERVAL 40 DAY), 5,  995.00),

-- Walk-in sales (no customer)
(7, NULL, DATE_SUB(CURDATE(), INTERVAL 2 DAY),  20, 5980.00),
(2, NULL, DATE_SUB(CURDATE(), INTERVAL 7 DAY),  3,  2397.00),
(4, NULL, DATE_SUB(CURDATE(), INTERVAL 18 DAY), 2,  4998.00);

-- ─── 5. INVENTORY LOG ───────────────────────────────────
INSERT INTO inventory_log (product_id, change_type, qty_change, user_id, timestamp) VALUES
(1, 'SALE',       -1,   1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 'SALE',       -2,   1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(2, 'RESTOCK',    100,  1, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(7, 'SALE',       -20,  1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(7, 'RESTOCK',    200,  1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(11,'SALE',       -7,   1, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(11,'ADJUSTMENT', -2,   1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(12,'SALE',       -5,   1, DATE_SUB(NOW(), INTERVAL 3 DAY));

-- ─── 6. ALERTS ──────────────────────────────────────────
INSERT INTO alerts (product_id, alert_type, status, triggered_at) VALUES
(11, 'LOW_STOCK',     'ACTIVE',   NOW()),
(12, 'OUT_OF_STOCK',  'ACTIVE',   NOW()),
(2,  'LOW_STOCK',     'RESOLVED', DATE_SUB(NOW(), INTERVAL 9 DAY));

-- ─── 7. FORECASTS (sample predictions) ──────────────────
INSERT INTO forecasts (product_id, forecast_date, predicted_demand, mape_score, model_used, generated_at) VALUES
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY),  2.5,  12.50, 'moving_average', NOW()),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY),  3.1,  12.50, 'moving_average', NOW()),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY),  2.8,  12.50, 'moving_average', NOW()),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY),  8.0,  10.20, 'moving_average', NOW()),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY),  7.5,  10.20, 'moving_average', NOW());

-- ═══════════════════════════════════════════════════════════
-- VERIFICATION
-- ═══════════════════════════════════════════════════════════
SELECT 'users' AS tbl, COUNT(*) AS cnt FROM users
UNION ALL SELECT 'products', COUNT(*) FROM products
UNION ALL SELECT 'customers', COUNT(*) FROM customers
UNION ALL SELECT 'sales', COUNT(*) FROM sales
UNION ALL SELECT 'inventory_log', COUNT(*) FROM inventory_log
UNION ALL SELECT 'alerts', COUNT(*) FROM alerts
UNION ALL SELECT 'forecasts', COUNT(*) FROM forecasts;
