-- =============================================================================
-- Ghi chú: Script này tạo các chi tiết đơn hàng nhập kho.
-- - Các trường trong object product JSONB đã được đồng bộ hóa với dữ liệu gốc.
-- - Mọi giá trị khác như số lượng, đơn giá, và tổng tiền được giữ nguyên.
-- =============================================================================

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 1 (Liên kết với SO_ID = 1)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 2, "createdAt": "2025-07-07T08:10:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:10:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR12D", "name": "Thép vằn D12", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 10.38, "length": 11700, "diameter": 12, "width": null, "height": null, "thickness": null,
       "importPrice": 155700, "profitPercentage": 0.15, "exportPrice": 179055
     }'::jsonb,
     2.00, 155700.00, 311400.00, 20.76, 1);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 5, "createdAt": "2025-07-07T08:40:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:40:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR18D", "name": "Thép vằn D18", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 23.37, "length": 11700, "diameter": 18, "width": null, "height": null, "thickness": null,
       "importPrice": 350550, "profitPercentage": 0.15, "exportPrice": 403132
     }'::jsonb,
     3.00, 350550.00, 1051650.00, 70.11, 1);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 2 (Tự do, nhập kho)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 8, "createdAt": "2025-07-07T10:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 329.7, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 14,
       "importPrice": 4945500, "profitPercentage": 0.15, "exportPrice": 5687325
     }'::jsonb,
     20.00, 4945500.00, 98910000.00, 6594.00, 2);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 9, "createdAt": "2025-07-07T13:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T13:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707BOX10010", "name": "Thép hộp vuông 100x100x10", "productType": "BOX", "standard": "JIS G3466",
       "unitWeight": 29.29, "length": 100000, "diameter": null, "width": 100, "height": 100, "thickness": 10,
       "importPrice": 439350, "profitPercentage": 0.15, "exportPrice": 505252
     }'::jsonb,
     100.00, 439350.00, 43935000.00, 2929.00, 2);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 3 (Liên kết với SO_ID = 4)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 3, "createdAt": "2025-07-07T08:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR14D", "name": "Thép vằn D14", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 14.13, "length": 11700, "diameter": 14, "width": null, "height": null, "thickness": null,
       "importPrice": 211950, "profitPercentage": 0.15, "exportPrice": 243742
     }'::jsonb,
     1.00, 211950.00, 211950.00, 14.13, 3);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 13, "createdAt": "2025-07-07T14:40:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:40:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI200", "name": "Thép hình I 200x100x5.5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 21.3, "length": 50000, "diameter": null, "width": 100, "height": 200, "thickness": 5.5,
       "importPrice": 319500, "profitPercentage": 0.15, "exportPrice": 367425
     }'::jsonb,
     2.00, 319500.00, 639000.00, 42.60, 3);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 14, "createdAt": "2025-07-07T14:50:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:50:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI300", "name": "Thép hình I 300x150x6.5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 36.7, "length": 50000, "diameter": null, "width": 150, "height": 300, "thickness": 6.5,
       "importPrice": 550500, "profitPercentage": 0.15, "exportPrice": 633075
     }'::jsonb,
     1.00, 550500.00, 550500.00, 36.70, 3);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 4 (Tự do, nhập kho)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 1, "createdAt": "2025-07-07T08:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 7.21, "length": 11700, "diameter": 10, "width": null, "height": null, "thickness": null,
       "importPrice": 108150, "profitPercentage": 0.15, "exportPrice": 124372
     }'::jsonb,
     500.00, 108150.00, 54075000.00, 3605.00, 4);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 5 (Liên kết với SO_ID = 5)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 4, "createdAt": "2025-07-07T08:30:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:30:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR16D", "name": "Thép vằn D16", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 18.46, "length": 11700, "diameter": 16, "width": null, "height": null, "thickness": null,
       "importPrice": 276900, "profitPercentage": 0.15, "exportPrice": 318435
     }'::jsonb,
     2.00, 276900.00, 553800.00, 36.92, 5);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 10, "createdAt": "2025-07-07T13:10:00Z", "createdBy": "System", "updatedAt": "2025-07-07T13:10:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707BOX15010", "name": "Thép hộp vuông 150x150x10", "productType": "BOX", "standard": "JIS G3466",
       "unitWeight": 45.09, "length": 100000, "diameter": null, "width": 150, "height": 150, "thickness": 10,
       "importPrice": 676350, "profitPercentage": 0.15, "exportPrice": 777802
     }'::jsonb,
     1.00, 676350.00, 676350.00, 45.09, 5);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 6 (Tự do, nhập kho, CANCELLED)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 6, "createdAt": "2025-07-07T10:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT10T", "name": "Thép tấm 10ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 235.5, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 10,
       "importPrice": 3532500, "profitPercentage": 0.15, "exportPrice": 4062375
     }'::jsonb,
     10.00, 3532500.00, 35325000.00, 2355.00, 6);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 7 (Liên kết với SO_ID = 2)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 1, "createdAt": "2025-07-07T08:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 7.21, "length": 11700, "diameter": 10, "width": null, "height": null, "thickness": null,
       "importPrice": 108150, "profitPercentage": 0.15, "exportPrice": 124372
     }'::jsonb,
     1.00, 108150.00, 108150.00, 7.21, 7);
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 8, "createdAt": "2025-07-07T10:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 329.7, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 14,
       "importPrice": 4945500, "profitPercentage": 0.15, "exportPrice": 5687325
     }'::jsonb,
     2.00, 4945500.00, 9891000.00, 659.40, 7);

-- =============================================================================
-- Chi tiết cho Đơn hàng PO_ID = 8 (Tự do, nhập kho)
-- =============================================================================
INSERT INTO purchase_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1,
     '{
       "id": 12, "createdAt": "2025-07-07T14:30:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:30:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI150", "name": "Thép hình I 150x75x5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 14.0, "length": 50000, "diameter": null, "width": 75, "height": 150, "thickness": 5,
       "importPrice": 210000, "profitPercentage": 0.15, "exportPrice": 241500
     }'::jsonb,
     50.00, 210000.00, 10500000.00, 700.00, 8);