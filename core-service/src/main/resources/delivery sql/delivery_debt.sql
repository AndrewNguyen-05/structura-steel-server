INSERT INTO delivery_debts (
    created_at, created_by, updated_at, updated_by, version,
    original_amount, remaining_amount,
    debt_note, order_type, status, delivery_order_id
) VALUES
-- Giả sử ID của 7 delivery order trên lần lượt là 1, 2, 3, 4, 5, 6, 7
(NOW(), 'System', NOW(), 'System', 1, 425000.00, 0.00, 'Đã thanh toán cước VC DO 1', 'PURCHASE', 'PAID', 1),
(NOW(), 'System', NOW(), 'System', 1, 10200000.00, 10200000.00, 'Công nợ vận chuyển DO 2', 'PURCHASE', 'UNPAID', 2),
(NOW(), 'System', NOW(), 'System', 1, 320000.00, 0.00, 'Thanh toán cước giao hàng DO 3', 'SALE', 'PAID', 3),
(NOW(), 'System', NOW(), 'System', 1, 810000.00, 810000.00, 'Công nợ nhập hàng DO 4', 'PURCHASE', 'UNPAID', 4),
(NOW(), 'System', NOW(), 'System', 1, 555000.00, 0.00, 'Đã thanh toán cước VC DO 5', 'SALE', 'PAID', 5),
(NOW(), 'System', NOW(), 'System', 1, 200000.00, 0.00, 'Thanh toán cước giao hàng DO 6', 'SALE', 'PAID', 6),
(NOW(), 'System', NOW(), 'System', 1, 1150000.00, 1150000.00, 'Công nợ giao hàng DO 7 (SO 10)', 'SALE', 'UNPAID', 7);