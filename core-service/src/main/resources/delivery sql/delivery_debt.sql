INSERT INTO delivery_debts (
    created_at, created_by, updated_at, updated_by, version,
    original_amount, remaining_amount, -- Thay thế 'amount'
    debt_note, order_type, status, delivery_order_id
) VALUES
-- (NOW(), 'System', NOW(), 'System', 1, original_amount, remaining_amount, debt_note, order_type, status_enum, delivery_order_id)

(NOW(), 'System', NOW(), 'System', 1, 425000.00, 0.00, 'Đã thanh toán cước VC DO 1', 'PURCHASE', 'PAID', 1),
(NOW(), 'System', NOW(), 'System', 1, 10200000.00, 10200000.00, 'Công nợ vận chuyển DO 2', 'PURCHASE', 'UNPAID', 2),
(NOW(), 'System', NOW(), 'System', 1, 320000.00, 0.00, 'Thanh toán cước giao hàng DO 3', 'SALE', 'PAID', 3),
(NOW(), 'System', NOW(), 'System', 1, 810000.00, 810000.00, 'Công nợ nhập hàng DO 4', 'PURCHASE', 'UNPAID', 4),
(NOW(), 'System', NOW(), 'System', 1, 555000.00, 0.00, 'Đã thanh toán cước VC DO 5', 'SALE', 'PAID', 5),
(NOW(), 'System', NOW(), 'System', 1, 584000.00, 584000.00, 'Công nợ nhập hàng DO 6', 'PURCHASE', 'UNPAID', 6),
(NOW(), 'System', NOW(), 'System', 1, 200000.00, 0.00, 'Thanh toán cước giao hàng DO 7', 'SALE', 'PAID', 7),
(NOW(), 'System', NOW(), 'System', 1, 770000.00, 770000.00, 'Công nợ nhập hàng DO 8 - Quá hạn', 'PURCHASE', 'UNPAID', 8),
(NOW(), 'System', NOW(), 'System', 1, 1170000.00, 0.00, 'Đã thanh toán cước VC DO 9', 'SALE', 'PAID', 9),
(NOW(), 'System', NOW(), 'System', 1, 512000.00, 512000.00, 'Công nợ nhập hàng DO 10', 'PURCHASE', 'UNPAID', 10),
(NOW(), 'System', NOW(), 'System', 1, 1150000.00, 0.00, 'Đã thanh toán cước giao hàng DO 11', 'SALE', 'PAID', 11),
(NOW(), 'System', NOW(), 'System', 1, 425000.00, 0.00, 'Đã thanh toán cước VC DO 12 (chuyến 2 PO 1)', 'PURCHASE', 'PAID', 12),
(NOW(), 'System', NOW(), 'System', 1, 300000.00, 300000.00, 'Công nợ giao hàng DO 13 (đợt 2 SO 2)', 'SALE', 'UNPAID', 13),
(NOW(), 'System', NOW(), 'System', 1, 810000.00, 810000.00, 'Công nợ nhập hàng DO 14 (đợt 2 PO 4)', 'PURCHASE', 'UNPAID', 14),
(NOW(), 'System', NOW(), 'System', 1, 1140000.00, 0.00, 'Thanh toán cước VC DO 15 (đợt 2 SO 7)', 'SALE', 'PAID', 15);