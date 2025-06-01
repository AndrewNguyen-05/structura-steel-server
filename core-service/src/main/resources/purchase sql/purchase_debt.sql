-- Debt for PO_ID=1, relating to Product_ID=10
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 75500.50, 0.00, 'Đã thanh toán đủ cho PO 1',
     '{
       "id": 10, "code": "PRD202503225EWPET61T6KA", "name": "Thép vằn phi 145", "unitWeight": 129.7, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 145, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 1);

-- Debt for PO_ID=2, relating to Product_ID=30
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 60000.00, 0.00, 'Thanh toán đợt 1 PO 2',
     '{
       "id": 30, "code": "PRD202503225EWPEW5O8CSS", "name": "Thép tấm dày 14mm", "unitWeight": 329.7, "productType": "PLATE",
       "length": 50, "width": 3.0, "height": null, "thickness": 14, "diameter": null, "standard": "Big Plate",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 2);

-- Debt for PO_ID=2, relating to Product_ID=35
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 60300.00, 60300.00, 'Công nợ còn lại PO 2',
     '{
       "id": 35, "code": "PRD202503225EWPF09L7W10", "name": "Thép tấm dày 28mm", "unitWeight": 659.4, "productType": "PLATE",
       "length": 50, "width": 3.0, "height": null, "thickness": 28, "diameter": null, "standard": "Big Plate",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 2);

-- Debt for PO_ID=3, relating to Product_ID=55
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 55000.00, 55000.00, 'Công nợ PO 3 (đang xử lý)',
     '{
       "id": 55, "code": "PRD202503225EWPF3LWI41L", "name": "Thép hộp vuông 150x150x10", "unitWeight": 72.2, "productType": "BOX",
       "length": 100, "width": 150, "height": 150, "thickness": 10, "diameter": null, "standard": "Big Box",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 3);

-- Debt for PO_ID=4, relating to Product_ID=80
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 210000.75, 0.00, 'Đã thanh toán đủ PO 4',
     '{
       "id": 80, "code": "PRD202503225EWPF7N9UK2A", "name": "Thép hình I 240", "unitWeight": 240.0, "productType": "SHAPED",
       "length": 50, "width": null, "height": null, "thickness": null, "diameter": null, "standard": "Big Shape",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 4);

-- Debt for PO_ID=5, relating to Product_ID=28
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 95800.00, 95800.00, 'Công nợ PO 5 (mới đặt)',
     '{
       "id": 28, "code": "PRD202503225EWPEW09WQRR", "name": "Thép tấm dày 12mm", "unitWeight": 282.6, "productType": "PLATE",
       "length": 50, "width": 3.0, "height": null, "thickness": 12, "diameter": null, "standard": "Big Plate",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 5);

-- Debt for PO_ID=6, relating to Product_ID=15
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 180000.00, 0.00, 'Đã thanh toán PO 6 khi nhận hàng',
     '{
       "id": 15, "code": "PRD202503225EWPEU0POGPF", "name": "Thép vằn phi 170", "unitWeight": 178.5, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 170, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 6);

-- Debt for PO_ID=7, relating to Product_ID=51
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 45200.25, 0.00, 'Thanh toán đủ PO 7',
     '{
       "id": 51, "code": "PRD202503225EWPF2SW8S1G", "name": "Thép hộp vuông 100x100x10", "unitWeight": 28.3, "productType": "BOX",
       "length": 100, "width": 100, "height": 100, "thickness": 10, "diameter": null, "standard": "Big Box",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 7);

-- Debt for PO_ID=8, relating to Product_ID=95
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 100000.00, 0.00, 'Thanh toán cọc 100tr cho PO 8',
     '{
       "id": 95, "code": "DRD202503225EWPF9FD9U2L", "name": "Thép hình I 328", "unitWeight": 328.0, "productType": "SHAPED",
       "length": 50, "width": null, "height": null, "thickness": null, "diameter": null, "standard": "Big Shape",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 8);

-- Debt for PO_ID=8, relating to Product_ID=100
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 200500.00, 200500.00, 'Công nợ còn lại PO 8',
     '{
       "id": 100, "code": "PRD202503225EWPFARZ202T", "name": "Thép hình I 392", "unitWeight": 392.0, "productType": "SHAPED",
       "length": 50, "width": null, "height": null, "thickness": null, "diameter": null, "standard": "Big Shape",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 8);