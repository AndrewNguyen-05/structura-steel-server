-- Ghi chú: Đã bổ sung đầy đủ thông tin chi tiết cho từng sản phẩm trong công nợ mua hàng.

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 1 & 2 (Status: DONE)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 311400.00, 0.00, 'Đã thanh toán đủ cho PO#1.',
     '{
       "id": 2, "createdAt": "2025-07-07T08:10:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:10:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR12D", "name": "Thép vằn D12", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 10.38, "length": 11700, "diameter": 12, "width": null, "height": null, "thickness": null,
       "importPrice": 155700, "profitPercentage": 0.15, "exportPrice": 179055
     }'::jsonb, 'PAID', 1);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 1051650.00, 0.00, 'Đã thanh toán đủ cho PO#1.',
     '{
       "id": 5, "createdAt": "2025-07-07T08:40:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:40:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR18D", "name": "Thép vằn D18", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 23.37, "length": 11700, "diameter": 18, "width": null, "height": null, "thickness": null,
       "importPrice": 350550, "profitPercentage": 0.15, "exportPrice": 403132
     }'::jsonb, 'PAID', 1);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 98910000.00, 0.00, 'Đã thanh toán đủ cho PO#2.',
     '{
       "id": 8, "createdAt": "2025-07-07T10:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 329.7, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 14,
       "importPrice": 4945500, "profitPercentage": 0.15, "exportPrice": 5687325
     }'::jsonb, 'PAID', 2);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 43935000.00, 0.00, 'Đã thanh toán đủ cho PO#2.',
     '{
       "id": 9, "createdAt": "2025-07-07T13:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T13:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707BOX10010", "name": "Thép hộp vuông 100x100x10", "productType": "BOX", "standard": "JIS G3466",
       "unitWeight": 29.29, "length": 100000, "diameter": null, "width": 100, "height": 100, "thickness": 10,
       "importPrice": 439350, "profitPercentage": 0.15, "exportPrice": 505252
     }'::jsonb, 'PAID', 2);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 3 (Status: PROCESSING)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 211950.00, 0.00, 'Đã thanh toán cọc cho PO#3.',
     '{
       "id": 3, "createdAt": "2025-07-07T08:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR14D", "name": "Thép vằn D14", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 14.13, "length": 11700, "diameter": 14, "width": null, "height": null, "thickness": null,
       "importPrice": 211950, "profitPercentage": 0.15, "exportPrice": 243742
     }'::jsonb, 'PAID', 3);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 639000.00, 639000.00, 'Chưa thanh toán cho PO#3.',
     '{
       "id": 13, "createdAt": "2025-07-07T14:40:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:40:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI200", "name": "Thép hình I 200x100x5.5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 21.3, "length": 50000, "diameter": null, "width": 100, "height": 200, "thickness": 5.5,
       "importPrice": 319500, "profitPercentage": 0.15, "exportPrice": 367425
     }'::jsonb, 'UNPAID', 3);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 550500.00, 550500.00, 'Chưa thanh toán cho PO#3.',
     '{
       "id": 14, "createdAt": "2025-07-07T14:50:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:50:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI300", "name": "Thép hình I 300x150x6.5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 36.7, "length": 50000, "diameter": null, "width": 150, "height": 300, "thickness": 6.5,
       "importPrice": 550500, "profitPercentage": 0.15, "exportPrice": 633075
     }'::jsonb, 'UNPAID', 3);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 4 & 5 (Status: NEW)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 54075000.00, 54075000.00, 'Công nợ cho đơn hàng mới PO#4.',
     '{
       "id": 1, "createdAt": "2025-07-07T08:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 7.21, "length": 11700, "diameter": 10, "width": null, "height": null, "thickness": null,
       "importPrice": 108150, "profitPercentage": 0.15, "exportPrice": 124372
     }'::jsonb, 'UNPAID', 4);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 553800.00, 553800.00, 'Công nợ cho đơn hàng mới PO#5.',
     '{
       "id": 4, "createdAt": "2025-07-07T08:30:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:30:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR16D", "name": "Thép vằn D16", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 18.46, "length": 11700, "diameter": 16, "width": null, "height": null, "thickness": null,
       "importPrice": 276900, "profitPercentage": 0.15, "exportPrice": 318435
     }'::jsonb, 'UNPAID', 5);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 676350.00, 676350.00, 'Công nợ cho đơn hàng mới PO#5.',
     '{
       "id": 10, "createdAt": "2025-07-07T13:10:00Z", "createdBy": "System", "updatedAt": "2025-07-07T13:10:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707BOX15010", "name": "Thép hộp vuông 150x150x10", "productType": "BOX", "standard": "JIS G3466",
       "unitWeight": 45.09, "length": 100000, "diameter": null, "width": 150, "height": 150, "thickness": 10,
       "importPrice": 676350, "profitPercentage": 0.15, "exportPrice": 777802
     }'::jsonb, 'UNPAID', 5);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 6 (Status: CANCELLED)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 35325000.00, 0.00, 'Công nợ đã hủy do đơn hàng PO#6 bị hủy.',
     '{
       "id": 6, "createdAt": "2025-07-07T10:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT10T", "name": "Thép tấm 10ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 235.5, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 10,
       "importPrice": 3532500, "profitPercentage": 0.15, "exportPrice": 4062375
     }'::jsonb, 'CANCELLED', 6);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 7 (Status: PROCESSING)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 108150.00, 108150.00, 'Công nợ cho PO#7.',
     '{
       "id": 1, "createdAt": "2025-07-07T08:00:00Z", "createdBy": "System", "updatedAt": "2025-07-07T08:00:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707RBAR10D", "name": "Thép vằn D10", "productType": "RIBBED_BAR", "standard": "TCVN 1651-2:2018",
       "unitWeight": 7.21, "length": 11700, "diameter": 10, "width": null, "height": null, "thickness": null,
       "importPrice": 108150, "profitPercentage": 0.15, "exportPrice": 124372
     }'::jsonb, 'UNPAID', 7);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 9891000.00, 5000000.00, 'Công nợ cho PO#7, còn lại 5tr.',
     '{
       "id": 8, "createdAt": "2025-07-07T10:20:00Z", "createdBy": "System", "updatedAt": "2025-07-07T10:20:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly x 3m", "productType": "PLATE", "standard": "JIS G3101",
       "unitWeight": 329.7, "length": 50000, "diameter": null, "width": 3000, "height": null, "thickness": 14,
       "importPrice": 4945500, "profitPercentage": 0.15, "exportPrice": 5687325
     }'::jsonb, 'PARTIALLY_PAID', 7);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 8 (Status: DONE)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 10500000.00, 0.00, 'Đã thanh toán đủ cho PO#8.',
     '{
       "id": 12, "createdAt": "2025-07-07T14:30:00Z", "createdBy": "System", "updatedAt": "2025-07-07T14:30:00Z", "updatedBy": "System", "version": 1,
       "code": "PRD20250707SHPI150", "name": "Thép hình I 150x75x5", "productType": "SHAPED", "standard": "JIS G3192",
       "unitWeight": 14.0, "length": 50000, "diameter": null, "width": 75, "height": 150, "thickness": 5,
       "importPrice": 210000, "profitPercentage": 0.15, "exportPrice": 241500
     }'::jsonb, 'PAID', 8);