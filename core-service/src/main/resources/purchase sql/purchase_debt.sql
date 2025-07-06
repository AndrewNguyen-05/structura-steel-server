-- Ghi chú: Đã sửa lại cú pháp và bổ sung đầy đủ các cột theo yêu cầu.

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 1 & 2 (Status: DONE) => Tất cả công nợ PAID
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 311400.00, 0.00, 'Đã thanh toán đủ cho PO#1.', '{"id": 2, "name": "Thép vằn D12"}'::jsonb, 'PAID', 1);
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 1051650.00, 0.00, 'Đã thanh toán đủ cho PO#1.', '{"id": 5, "name": "Thép vằn D18"}'::jsonb, 'PAID', 1);

INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 98910000.00, 0.00, 'Đã thanh toán đủ cho PO#2.', '{"id": 8, "name": "Thép tấm 14ly"}'::jsonb, 'PAID', 2);
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 43935000.00, 0.00, 'Đã thanh toán đủ cho PO#2.', '{"id": 9, "name": "Thép hộp 100x100"}'::jsonb, 'PAID', 2);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 3 (Status: PROCESSING) => Giả sử trả một phần
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 211950.00, 0.00, 'Đã thanh toán cọc cho PO#3.', '{"id": 3, "name": "Thép vằn D14"}'::jsonb, 'PAID', 3);
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 639000.00, 639000.00, 'Chưa thanh toán cho PO#3.', '{"id": 13, "name": "Thép hình I 200"}'::jsonb, 'UNPAID', 3);
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 550500.00, 550500.00, 'Chưa thanh toán cho PO#3.', '{"id": 14, "name": "Thép hình I 300"}'::jsonb, 'UNPAID', 3);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 4 & 5 (Status: NEW) => Tất cả công nợ UNPAID
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 54075000.00, 54075000.00, 'Công nợ cho đơn hàng mới PO#4.', '{"id": 1, "name": "Thép vằn D10"}'::jsonb, 'UNPAID', 4);
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 553800.00, 553800.00, 'Công nợ cho đơn hàng mới PO#5.', '{"id": 4, "name": "Thép vằn D16"}'::jsonb, 'UNPAID', 5);
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 676350.00, 676350.00, 'Công nợ cho đơn hàng mới PO#5.', '{"id": 10, "name": "Thép hộp 150x150"}'::jsonb, 'UNPAID', 5);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 6 (Status: CANCELLED)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 35325000.00, 0.00, 'Công nợ đã hủy do đơn hàng PO#6 bị hủy.', '{"id": 6, "name": "Thép tấm 10ly"}'::jsonb, 'CANCELLED', 6);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 7 (Status: PROCESSING)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 108150.00, 108150.00, 'Công nợ cho PO#7.', '{"id": 1, "name": "Thép vằn D10"}'::jsonb, 'UNPAID', 7);
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 9891000.00, 5000000.00, 'Công nợ cho PO#7, còn lại 5tr.', '{"id": 8, "name": "Thép tấm 14ly"}'::jsonb, 'PARTIALLY_PAID', 7);

-- =============================================================================
-- Công nợ cho Đơn hàng PO_ID = 8 (Status: DONE)
-- =============================================================================
INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, purchase_order_id) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 10500000.00, 0.00, 'Đã thanh toán đủ cho PO#8.', '{"id": 12, "name": "Thép hình I 150"}'::jsonb, 'PAID', 8);