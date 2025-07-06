-- =============================================================================
-- Ghi chú: Script này tái tạo lại 5 đơn hàng gốc.
-- - Số lượng (quantity) được giữ nguyên.
-- - Sản phẩm không hợp lệ được thay thế bằng sản phẩm chuẩn tương đương.
-- - Mọi giá trị và khối lượng được tính toán lại cho chính xác.
-- =============================================================================

-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 1
-- =============================================================================

-- Thay thế "Thép vằn phi 120" bằng "Thép vằn D12" (ID=2). Giữ nguyên quantity=2.
-- Unit Price = 179,055 (giá/cây) | Subtotal = 2 * 179,055 = 358,110
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-10 09:00:00+07', 'admin', '2025-07-10 09:00:00+07', 'admin', 1,
     '{
       "id": 2, "code": "PRD20250707RBAR12D", "name": "Thép vằn D12", "productType": "RIBBED_BAR",
       "unitWeight": 10.38, "length": 11700, "diameter": 12, "standard": "TCVN 1651-2:2018", "exportPrice": 179055
     }'::jsonb,
     2.00, 179055.00, 358110.00, 20.76, 1);

-- Thay thế "Thép vằn phi 185" bằng "Thép vằn D18" (ID=5). Giữ nguyên quantity=3.
-- Unit Price = 403,132 (giá/cây) | Subtotal = 3 * 403,132 = 1,209,396
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-10 09:01:00+07', 'admin', '2025-07-10 09:01:00+07', 'admin', 1,
     '{
       "id": 5, "code": "PRD20250707RBAR18D", "name": "Thép vằn D18", "productType": "RIBBED_BAR",
       "unitWeight": 23.37, "length": 11700, "diameter": 18, "standard": "TCVN 1651-2:2018", "exportPrice": 403132
     }'::jsonb,
     3.00, 403132.00, 1209396.00, 70.11, 1);


-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 2
-- =============================================================================

-- Thay thế "Thép vằn phi 205" bằng "Thép vằn D10" (ID=1). Giữ nguyên quantity=1.
-- Unit Price = 124,372 (giá/cây) | Subtotal = 1 * 124,372 = 124,372
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-10 10:00:00+07', 'admin', '2025-07-10 10:00:00+07', 'admin', 1,
     '{
       "id": 1, "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "productType": "RIBBED_BAR",
       "unitWeight": 7.21, "length": 11700, "diameter": 10, "standard": "TCVN 1651-2:2018", "exportPrice": 124372
     }'::jsonb,
     1.00, 124372.00, 124372.00, 7.21, 2);

-- Thay thế "Thép tấm dày 24mm" bằng "Thép tấm 14ly x 3m" (ID=8). Giữ nguyên quantity=2.
-- Unit Price = 5,687,325 (giá/mét) | Subtotal = 2 * 5,687,325 = 11,374,650
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-10 10:01:00+07', 'admin', '2025-07-10 10:01:00+07', 'admin', 1,
     '{
       "id": 8, "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "productType": "PLATE",
       "unitWeight": 329.7, "length": 50000, "width": 3000, "thickness": 14, "standard": "JIS G3101", "exportPrice": 5687325
     }'::jsonb,
     2.00, 5687325.00, 11374650.00, 659.40, 2);


-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 3
-- =============================================================================

-- Thay thế "Thép tấm dày 42mm" bằng "Thép tấm 12ly x 3m" (ID=7). Giữ nguyên quantity=3.
-- Unit Price = 4,874,850 (giá/mét) | Subtotal = 3 * 4,874,850 = 14,624,550
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-11 11:00:00+07', 'admin', '2025-07-11 11:00:00+07', 'admin', 1,
     '{
       "id": 7, "code": "PRD20250707PLAT12T", "name": "Thép tấm 12ly x 3m", "productType": "PLATE",
       "unitWeight": 282.6, "length": 50000, "width": 3000, "thickness": 12, "standard": "JIS G3101", "exportPrice": 4874850
     }'::jsonb,
     3.00, 4874850.00, 14624550.00, 847.80, 3);


-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 4
-- =============================================================================

-- Thay thế "Thép vằn phi 140" bằng "Thép vằn D14" (ID=3). Giữ nguyên quantity=1.
-- Unit Price = 243,742 (giá/cây) | Subtotal = 1 * 243,742 = 243,742
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-11 15:00:00+07', 'admin', '2025-07-11 15:00:00+07', 'admin', 1,
     '{
       "id": 3, "code": "PRD20250707RBAR14D", "name": "Thép vằn D14", "productType": "RIBBED_BAR",
       "unitWeight": 14.13, "length": 11700, "diameter": 14, "standard": "TCVN 1651-2:2018", "exportPrice": 243742
     }'::jsonb,
     1.00, 243742.00, 243742.00, 14.13, 4);

-- Thay thế "Thép hình I 216" bằng "Thép hình I 200x100x5.5" (ID=13). Giữ nguyên quantity=2.
-- Unit Price = 367,425 (giá/mét) | Subtotal = 2 * 367,425 = 734,850
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-11 15:01:00+07', 'admin', '2025-07-11 15:01:00+07', 'admin', 1,
     '{
       "id": 13, "code": "PRD20250707SHPI200", "name": "Thép hình I 200x100x5.5", "productType": "SHAPED",
       "unitWeight": 21.3, "length": 50000, "width": 100, "height": 200, "thickness": 5.5, "standard": "JIS G3192", "exportPrice": 367425
     }'::jsonb,
     2.00, 367425.00, 734850.00, 42.60, 4);

-- Thay thế "Thép hình I 296" bằng "Thép hình I 300x150x6.5" (ID=14). Giữ nguyên quantity=1.
-- Unit Price = 633,075 (giá/mét) | Subtotal = 1 * 633,075 = 633,075
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-11 15:02:00+07', 'admin', '2025-07-11 15:02:00+07', 'admin', 1,
     '{
       "id": 14, "code": "PRD20250707SHPI300", "name": "Thép hình I 300x150x6.5", "productType": "SHAPED",
       "unitWeight": 36.7, "length": 50000, "width": 150, "height": 300, "thickness": 6.5, "standard": "JIS G3192", "exportPrice": 633075
     }'::jsonb,
     1.00, 633075.00, 633075.00, 36.70, 4);


-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 5
-- =============================================================================

-- Thay thế "Thép vằn phi 160" bằng "Thép vằn D16" (ID=4). Giữ nguyên quantity=2.
-- Unit Price = 318,435 (giá/cây) | Subtotal = 2 * 318,435 = 636,870
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-12 08:30:00+07', 'admin', '2025-07-12 08:30:00+07', 'admin', 1,
     '{
       "id": 4, "code": "PRD20250707RBAR16D", "name": "Thép vằn D16", "productType": "RIBBED_BAR",
       "unitWeight": 18.46, "length": 11700, "diameter": 16, "standard": "TCVN 1651-2:2018", "exportPrice": 318435
     }'::jsonb,
     2.00, 318435.00, 636870.00, 36.92, 5);

-- Thay thế "Thép hộp vuông 150x150x10" bằng sản phẩm chuẩn cùng tên (ID=10). Giữ nguyên quantity=1.
-- Unit Price = 777,802 (giá/mét) | Subtotal = 1 * 777,802 = 777,802
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-12 08:31:00+07', 'admin', '2025-07-12 08:31:00+07', 'admin', 1,
     '{
       "id": 10, "code": "PRD20250707BOX15010", "name": "Thép hộp vuông 150x150x10", "productType": "BOX",
       "unitWeight": 45.09, "length": 100000, "width": 150, "height": 150, "thickness": 10, "standard": "JIS G3466", "exportPrice": 777802
     }'::jsonb,
     1.00, 777802.00, 777802.00, 45.09, 5);

-- =============================================================================
-- Bổ sung chi tiết cho Đơn hàng SO_ID = 6 (Status: IN_TRANSIT)
-- =============================================================================
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-06-27 09:00:00+07', 'admin', '2025-06-27 09:00:00+07', 'admin', 1,
     '{
       "id": 9, "code": "PRD20250707BOX10010", "name": "Thép hộp vuông 100x100x10", "productType": "BOX",
       "unitWeight": 29.29, "length": 100000, "width": 100, "height": 100, "thickness": 10, "standard": "JIS G3466", "exportPrice": 505252
     }'::jsonb,
     10.00, 505252.00, 5052520.00, 292.90, 6);

INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-06-27 09:01:00+07', 'admin', '2025-06-27 09:01:00+07', 'admin', 1,
     '{
       "id": 1, "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "productType": "RIBBED_BAR",
       "unitWeight": 7.21, "length": 11700, "diameter": 10, "standard": "TCVN 1651-2:2018", "exportPrice": 124372
     }'::jsonb,
     50.00, 124372.00, 6218600.00, 360.50, 6);

-- =============================================================================
-- Bổ sung chi tiết cho Đơn hàng SO_ID = 7 (Status: DONE)
-- =============================================================================
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-06-17 10:00:00+07', 'admin', '2025-06-17 10:00:00+07', 'admin', 1,
     '{
       "id": 12, "code": "PRD20250707SHPI150", "name": "Thép hình I 150x75x5", "productType": "SHAPED",
       "unitWeight": 14.0, "length": 50000, "width": 75, "height": 150, "thickness": 5, "standard": "JIS G3192", "exportPrice": 241500
     }'::jsonb,
     20.00, 241500.00, 4830000.00, 280.00, 7);

-- =============================================================================
-- Bổ sung chi tiết cho Đơn hàng SO_ID = 8 (Status: NEW)
-- =============================================================================
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-06 17:00:00+07', 'admin', '2025-07-06 17:00:00+07', 'admin', 1,
     '{
       "id": 8, "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "productType": "PLATE",
       "unitWeight": 329.7, "length": 50000, "width": 3000, "thickness": 14, "standard": "JIS G3101", "exportPrice": 5687325
     }'::jsonb,
     100.00, 5687325.00, 568732500.00, 32970.00, 8);

INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-06 17:01:00+07', 'admin', '2025-07-06 17:01:00+07', 'admin', 1,
     '{
       "id": 5, "code": "PRD20250707RBAR18D", "name": "Thép vằn D18", "productType": "RIBBED_BAR",
       "unitWeight": 23.37, "length": 11700, "diameter": 18, "standard": "TCVN 1651-2:2018", "exportPrice": 403132
     }'::jsonb,
     200.00, 403132.00, 80626400.00, 4674.00, 8);