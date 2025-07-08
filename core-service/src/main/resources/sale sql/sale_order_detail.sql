-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 1
-- =============================================================================

-- Sản phẩm: "Thép vằn D12" (ID=2)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-10 09:00:00+07', 'admin', '2025-07-10 09:00:00+07', 'admin', 1,
     '{
       "id": 2, "createdAt": "2025-07-07T08:10:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:10:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR12D", "name": "Thép vằn D12", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 10.38, "length": 11700, "diameter": 12, "width": null, "height": null, "thickness": null,
       "importPrice": 155700, "profitPercentage": 0.15, "exportPrice": 179055
     }'::jsonb,
     2.00, 179055.00, 358110.00, 20.76, 1);

-- Sản phẩm: "Thép vằn D18" (ID=5)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-10 09:01:00+07', 'admin', '2025-07-10 09:01:00+07', 'admin', 1,
     '{
       "id": 5, "createdAt": "2025-07-07T08:40:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:40:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR18D", "name": "Thép vằn D18", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 23.37, "length": 11700, "diameter": 18, "width": null, "height": null, "thickness": null,
       "importPrice": 350550, "profitPercentage": 0.15, "exportPrice": 403132
     }'::jsonb,
     3.00, 403132.00, 1209396.00, 70.11, 1);


-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 2
-- =============================================================================

-- Sản phẩm: "Thép vằn D10" (ID=1)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-10 10:00:00+07', 'admin', '2025-07-10 10:00:00+07', 'admin', 1,
     '{
       "id": 1, "createdAt": "2025-07-07T08:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 7.21, "length": 11700, "diameter": 10, "width": null, "height": null, "thickness": null,
       "importPrice": 108150, "profitPercentage": 0.15, "exportPrice": 124372
     }'::jsonb,
     1.00, 124372.00, 124372.00, 7.21, 2);

-- Sản phẩm: "Thép tấm 14ly x 3m" (ID=8)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-10 10:01:00+07', 'admin', '2025-07-10 10:01:00+07', 'admin', 1,
     '{
       "id": 8, "createdAt": "2025-07-07T10:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 329.7, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 14,
       "importPrice": 4945500, "profitPercentage": 0.15, "exportPrice": 5687325
     }'::jsonb,
     2.00, 5687325.00, 11374650.00, 659.40, 2);


-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 3
-- =============================================================================

-- Sản phẩm: "Thép tấm 12ly x 3m" (ID=7)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-11 11:00:00+07', 'admin', '2025-07-11 11:00:00+07', 'admin', 1,
     '{
       "id": 7, "createdAt": "2025-07-07T10:10:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:10:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT12T", "name": "Thép tấm 12ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 282.6, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 12,
       "importPrice": 4239000, "profitPercentage": 0.15, "exportPrice": 4874850
     }'::jsonb,
     3.00, 4874850.00, 14624550.00, 847.80, 3);


-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 4
-- =============================================================================

-- Sản phẩm: "Thép vằn D14" (ID=3)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-11 15:00:00+07', 'admin', '2025-07-11 15:00:00+07', 'admin', 1,
     '{
       "id": 3, "createdAt": "2025-07-07T08:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR14D", "name": "Thép vằn D14", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 14.13, "length": 11700, "diameter": 14, "width": null, "height": null, "thickness": null,
       "importPrice": 211950, "profitPercentage": 0.15, "exportPrice": 243742
     }'::jsonb,
     1.00, 243742.00, 243742.00, 14.13, 4);

-- Sản phẩm: "Thép hình I 200x100x5.5" (ID=13)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-11 15:01:00+07', 'admin', '2025-07-11 15:01:00+07', 'admin', 1,
     '{
       "id": 13, "createdAt": "2025-07-07T14:40:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:40:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI200", "name": "Thép hình I 200x100x5.5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 21.3, "length": 50000, "diameter": null, "width": 100, "height": 200, "thickness": 5.5,
       "importPrice": 319500, "profitPercentage": 0.15, "exportPrice": 367425
     }'::jsonb,
     2.00, 367425.00, 734850.00, 42.60, 4);

-- Sản phẩm: "Thép hình I 300x150x6.5" (ID=14)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-11 15:02:00+07', 'admin', '2025-07-11 15:02:00+07', 'admin', 1,
     '{
       "id": 14, "createdAt": "2025-07-07T14:50:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:50:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI300", "name": "Thép hình I 300x150x6.5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 36.7, "length": 50000, "diameter": null, "width": 150, "height": 300, "thickness": 6.5,
       "importPrice": 550500, "profitPercentage": 0.15, "exportPrice": 633075
     }'::jsonb,
     1.00, 633075.00, 633075.00, 36.70, 4);


-- =============================================================================
-- Chi tiết cho Đơn hàng SO_ID = 5
-- =============================================================================

-- Sản phẩm: "Thép vằn D16" (ID=4)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-12 08:30:00+07', 'admin', '2025-07-12 08:30:00+07', 'admin', 1,
     '{
       "id": 4, "createdAt": "2025-07-07T08:30:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:30:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR16D", "name": "Thép vằn D16", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 18.46, "length": 11700, "diameter": 16, "width": null, "height": null, "thickness": null,
       "importPrice": 276900, "profitPercentage": 0.15, "exportPrice": 318435
     }'::jsonb,
     2.00, 318435.00, 636870.00, 36.92, 5);

-- Sản phẩm: "Thép hộp vuông 150x150x10" (ID=10)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-12 08:31:00+07', 'admin', '2025-07-12 08:31:00+07', 'admin', 1,
     '{
       "id": 10, "createdAt": "2025-07-07T13:10:00Z", "createdBy": "System", "updatedAt": "2025-07-07T13:10:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707BOX15010", "name": "Thép hộp vuông 150x150x10", "productType": "BOX", "standard": "JIS G3466",
       "unitWeight": 45.09, "length": 100000, "diameter": null, "width": 150, "height": 150, "thickness": 10,
       "importPrice": 676350, "profitPercentage": 0.15, "exportPrice": 777802
     }'::jsonb,
     1.00, 777802.00, 777802.00, 45.09, 5);


-- =============================================================================
-- Bổ sung chi tiết cho Đơn hàng SO_ID = 6 (Status: IN_TRANSIT)
-- =============================================================================
-- Sản phẩm: "Thép hộp vuông 100x100x10" (ID=9)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-06-27 09:00:00+07', 'admin', '2025-06-27 09:00:00+07', 'admin', 1,
     '{
       "id": 9, "createdAt": "2025-07-07T13:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T13:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707BOX10010", "name": "Thép hộp vuông 100x100x10", "productType": "BOX", "standard": "JIS G3466",
       "unitWeight": 29.29, "length": 100000, "diameter": null, "width": 100, "height": 100, "thickness": 10,
       "importPrice": 439350, "profitPercentage": 0.15, "exportPrice": 505252
     }'::jsonb,
     10.00, 505252.00, 5052520.00, 292.90, 6);

-- Sản phẩm: "Thép vằn D10" (ID=1)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-06-27 09:01:00+07', 'admin', '2025-06-27 09:01:00+07', 'admin', 1,
     '{
       "id": 1, "createdAt": "2025-07-07T08:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 7.21, "length": 11700, "diameter": 10, "width": null, "height": null, "thickness": null,
       "importPrice": 108150, "profitPercentage": 0.15, "exportPrice": 124372
     }'::jsonb,
     50.00, 124372.00, 6218600.00, 360.50, 6);

-- =============================================================================
-- Bổ sung chi tiết cho Đơn hàng SO_ID = 7 (Status: DONE)
-- =============================================================================
-- Sản phẩm: "Thép hình I 150x75x5" (ID=12)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-06-17 10:00:00+07', 'admin', '2025-06-17 10:00:00+07', 'admin', 1,
     '{
       "id": 12, "createdAt": "2025-07-07T14:30:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:30:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI150", "name": "Thép hình I 150x75x5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 14.0, "length": 50000, "diameter": null, "width": 75, "height": 150, "thickness": 5,
       "importPrice": 210000, "profitPercentage": 0.15, "exportPrice": 241500
     }'::jsonb,
     20.00, 241500.00, 4830000.00, 280.00, 7);

-- =============================================================================
-- Bổ sung chi tiết cho Đơn hàng SO_ID = 8 (Status: NEW)
-- =============================================================================
-- Sản phẩm: "Thép tấm 14ly x 3m" (ID=8)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-06 17:00:00+07', 'admin', '2025-07-06 17:00:00+07', 'admin', 1,
     '{
       "id": 8, "createdAt": "2025-07-07T10:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 329.7, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 14,
       "importPrice": 4945500, "profitPercentage": 0.15, "exportPrice": 5687325
     }'::jsonb,
     100.00, 5687325.00, 568732500.00, 32970.00, 8);

-- Sản phẩm: "Thép vằn D18" (ID=5)
INSERT INTO sale_order_details (created_at, created_by, updated_at, updated_by, version, product, quantity, unit_price, subtotal, weight, id_sale_order) VALUES
    ('2025-07-06 17:01:00+07', 'admin', '2025-07-06 17:01:00+07', 'admin', 1,
     '{
       "id": 5, "createdAt": "2025-07-07T08:40:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:40:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR18D", "name": "Thép vằn D18", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 23.37, "length": 11700, "diameter": 18, "width": null, "height": null, "thickness": null,
       "importPrice": 350550, "profitPercentage": 0.15, "exportPrice": 403132
     }'::jsonb,
     200.00, 403132.00, 80626400.00, 4674.00, 8);