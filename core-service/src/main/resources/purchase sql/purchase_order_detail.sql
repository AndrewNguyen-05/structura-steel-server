-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 1 (Liên kết với SO_ID = 1)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 2, "code": "PRD20250707RBAR12D", "name": "Thép vằn D12", "importPrice": 155700}'::jsonb, 2.00, 155700.00, 311400.00, 20.76, 1);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 5, "code": "PRD20250707RBAR18D", "name": "Thép vằn D18", "importPrice": 350550}'::jsonb, 3.00, 350550.00, 1051650.00, 70.11, 1);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 2 (Tự do, nhập kho)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 8, "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "importPrice": 4945500}'::jsonb, 20.00, 4945500.00, 98910000.00, 6594.00, 2);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 9, "code": "PRD20250707BOX10010", "name": "Thép hộp vuông 100x100x10", "importPrice": 439350}'::jsonb, 100.00, 439350.00, 43935000.00, 2929.00, 2);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 3 (Liên kết với SO_ID = 4)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 3, "code": "PRD20250707RBAR14D", "name": "Thép vằn D14", "importPrice": 211950}'::jsonb, 1.00, 211950.00, 211950.00, 14.13, 3);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 13, "code": "PRD20250707SHPI200", "name": "Thép hình I 200x100x5.5", "importPrice": 319500}'::jsonb, 2.00, 319500.00, 639000.00, 42.60, 3);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 14, "code": "PRD20250707SHPI300", "name": "Thép hình I 300x150x6.5", "importPrice": 550500}'::jsonb, 1.00, 550500.00, 550500.00, 36.70, 3);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 4 (Tự do, nhập kho)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 1, "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "importPrice": 108150}'::jsonb, 500.00, 108150.00, 54075000.00, 3605.00, 4);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 5 (Liên kết với SO_ID = 5)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 4, "code": "PRD20250707RBAR16D", "name": "Thép vằn D16", "importPrice": 276900}'::jsonb, 2.00, 276900.00, 553800.00, 36.92, 5);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 10, "code": "PRD20250707BOX15010", "name": "Thép hộp vuông 150x150x10", "importPrice": 676350}'::jsonb, 1.00, 676350.00, 676350.00, 45.09, 5);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 6 (Tự do, nhập kho, CANCELLED)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 6, "code": "PRD20250707PLAT10T", "name": "Thép tấm 10ly x 3m", "importPrice": 3532500}'::jsonb, 10.00, 3532500.00, 35325000.00, 2355.00, 6);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 7 (Liên kết với SO_ID = 2)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 1, "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "importPrice": 108150}'::jsonb, 1.00, 108150.00, 108150.00, 7.21, 7);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 8, "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "importPrice": 4945500}'::jsonb, 2.00, 4945500.00, 9891000.00, 659.40, 7);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 8 (Tự do, nhập kho)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, '{"id": 12, "code": "PRD20250707SHPI150", "name": "Thép hình I 150x75x5", "importPrice": 210000}'::jsonb, 50.00, 210000.00, 10500000.00, 700.00, 8);