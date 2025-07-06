-- Ghi chú: Mỗi chi tiết đơn hàng (sale_order_details) giờ sẽ có một công nợ (sale_debts) tương ứng.

-- =============================================================================
-- Công nợ cho Đơn hàng SO_ID = 1 (Status: DONE) => Tất cả công nợ phải PAID
-- =============================================================================
-- Debt for Detail 1 of SO_ID=1
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-10 09:00:00+07', 'admin', '2025-07-10 09:00:00+07', 'admin', 1, 358110.00, 0.00, 'Công nợ cho sản phẩm Thép vằn D12. Đã thanh toán.', '{"id": 2, "code": "PRD20250707RBAR12D", "name": "Thép vằn D12"}'::jsonb, 'PAID', 1);
-- Debt for Detail 2 of SO_ID=1
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-10 09:01:00+07', 'admin', '2025-07-10 09:01:00+07', 'admin', 1, 1209396.00, 0.00, 'Công nợ cho sản phẩm Thép vằn D18. Đã thanh toán.', '{"id": 5, "code": "PRD20250707RBAR18D", "name": "Thép vằn D18"}'::jsonb, 'PAID', 1);

-- =============================================================================
-- Công nợ cho Đơn hàng SO_ID = 2 (Status: PROCESSING) => Trạng thái nợ tùy ý
-- =============================================================================
-- Debt for Detail 1 of SO_ID=2 - Giả sử đã trả
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-10 10:00:00+07', 'admin', '2025-07-10 10:00:00+07', 'admin', 1, 124372.00, 0.00, 'Công nợ cho sản phẩm Thép vằn D10. Đã thanh toán cọc.', '{"id": 1, "code": "PRD20250707RBAR10D", "name": "Thép vằn D10"}'::jsonb, 'PAID', 2);
-- Debt for Detail 2 of SO_ID=2 - Giả sử chưa trả
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-10 10:01:00+07', 'admin', '2025-07-10 10:01:00+07', 'admin', 1, 11374650.00, 11374650.00, 'Công nợ cho sản phẩm Thép tấm 14ly. Chờ thanh toán.', '{"id": 8, "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly"}'::jsonb, 'UNPAID', 2);

-- =============================================================================
-- Công nợ cho Đơn hàng SO_ID = 3 (Status: CANCELLED) => Trạng thái nợ CANCELLED
-- =============================================================================
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-11 11:00:00+07', 'admin', '2025-07-11 11:00:00+07', 'admin', 1, 14624550.00, 0.00, 'Công nợ đã hủy do đơn hàng bị hủy.', '{"id": 7, "code": "PRD20250707PLAT12T", "name": "Thép tấm 12ly"}'::jsonb, 'CANCELLED', 3);

-- =============================================================================
-- Công nợ cho Đơn hàng SO_ID = 4 (Status: DELIVERED) => Nợ không được PAID
-- =============================================================================
-- Debt for Detail 1 of SO_ID=4 - Giả sử chưa trả
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-11 15:00:00+07', 'admin', '2025-07-11 15:00:00+07', 'admin', 1, 243742.00, 243742.00, 'Công nợ cho sản phẩm Thép vằn D14.', '{"id": 3, "code": "PRD20250707RBAR14D", "name": "Thép vằn D14"}'::jsonb, 'UNPAID', 4);
-- Debt for Detail 2 of SO_ID=4 - Giả sử trả một phần
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-11 15:01:00+07', 'admin', '2025-07-11 15:01:00+07', 'admin', 1, 734850.00, 300000.00, 'Công nợ cho sản phẩm Thép hình I 200. Còn lại 300,000.', '{"id": 13, "code": "PRD20250707SHPI200", "name": "Thép hình I 200"}'::jsonb, 'PARTIALLY_PAID', 4);
-- Debt for Detail 3 of SO_ID=4 - Giả sử chưa trả
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-11 15:02:00+07', 'admin', '2025-07-11 15:02:00+07', 'admin', 1, 633075.00, 633075.00, 'Công nợ cho sản phẩm Thép hình I 300.', '{"id": 14, "code": "PRD20250707SHPI300", "name": "Thép hình I 300"}'::jsonb, 'UNPAID', 4);

-- =============================================================================
-- Công nợ cho Đơn hàng SO_ID = 5 (Status: NEW) => Nợ UNPAID
-- =============================================================================
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-12 08:30:00+07', 'admin', '2025-07-12 08:30:00+07', 'admin', 1, 636870.00, 636870.00, 'Công nợ cho sản phẩm Thép vằn D16.', '{"id": 4, "code": "PRD20250707RBAR16D", "name": "Thép vằn D16"}'::jsonb, 'UNPAID', 5);
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-12 08:31:00+07', 'admin', '2025-07-12 08:31:00+07', 'admin', 1, 777802.00, 777802.00, 'Công nợ cho sản phẩm Thép hộp 150x150.', '{"id": 10, "code": "PRD20250707BOX15010", "name": "Thép hộp 150x150"}'::jsonb, 'UNPAID', 5);

-- =============================================================================
-- Công nợ cho Đơn hàng SO_ID = 6 (Status: IN_TRANSIT) => Trạng thái nợ tùy ý
-- =============================================================================
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-06-27 09:00:00+07', 'admin', '2025-06-27 09:00:00+07', 'admin', 1, 5052520.00, 5052520.00, 'Công nợ cho sản phẩm Thép hộp 100x100.', '{"id": 9, "code": "PRD20250707BOX10010", "name": "Thép hộp 100x100"}'::jsonb, 'UNPAID', 6);
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-06-27 09:01:00+07', 'admin', '2025-06-27 09:01:00+07', 'admin', 1, 6218600.00, 0.00, 'Công nợ cho sản phẩm Thép vằn D10. Đã thanh toán trước.', '{"id": 1, "code": "PRD20250707RBAR10D", "name": "Thép vằn D10"}'::jsonb, 'PAID', 6);

-- =============================================================================
-- Công nợ cho Đơn hàng SO_ID = 7 (Status: DONE) => Tất cả công nợ phải PAID
-- =============================================================================
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-06-17 10:00:00+07', 'admin', '2025-06-17 10:00:00+07', 'admin', 1, 4830000.00, 0.00, 'Công nợ cho sản phẩm Thép hình I 150. Đã thanh toán.', '{"id": 12, "code": "PRD20250707SHPI150", "name": "Thép hình I 150"}'::jsonb, 'PAID', 7);

-- =============================================================================
-- Công nợ cho Đơn hàng SO_ID = 8 (Status: NEW) => Nợ UNPAID
-- =============================================================================
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-06 17:00:00+07', 'admin', '2025-07-06 17:00:00+07', 'admin', 1, 568732500.00, 568732500.00, 'Công nợ cho sản phẩm Thép tấm 14ly.', '{"id": 8, "code": "PRD20250707PLAT14T", "name": "Thép tấm 14ly"}'::jsonb, 'UNPAID', 8);
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-07-06 17:01:00+07', 'admin', '2025-07-06 17:01:00+07', 'admin', 1, 80626400.00, 80626400.00, 'Công nợ cho sản phẩm Thép vằn D18.', '{"id": 5, "code": "PRD20250707RBAR18D", "name": "Thép vằn D18"}'::jsonb, 'UNPAID', 8);