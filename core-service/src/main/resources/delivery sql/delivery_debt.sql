INSERT INTO delivery_debts (
    created_at, created_by, updated_at, updated_by, version,
    original_amount, remaining_amount,
    debt_note, status, delivery_order_id
) VALUES
-- Đã thanh toán xong
(NOW(), 'System', NOW(), 'System', 1, 425000.00, 0.00, 'Đã thanh toán cước VC DO 1', 'PAID', 1),

-- Chưa thanh toán gì
(NOW(), 'System', NOW(), 'System', 1, 10200000.00, 10200000.00, 'Công nợ vận chuyển DO 2', 'UNPAID', 2),

-- Đã thanh toán xong
(NOW(), 'System', NOW(), 'System', 1, 320000.00, 0.00, 'Thanh toán cước giao hàng DO 3', 'PAID', 3),

-- Chưa thanh toán gì
(NOW(), 'System', NOW(), 'System', 1, 810000.00, 810000.00, 'Công nợ nhập hàng DO 4', 'UNPAID', 4),

-- Đã thanh toán xong
(NOW(), 'System', NOW(), 'System', 1, 555000.00, 0.00, 'Đã thanh toán cước VC DO 5', 'PAID', 5),

-- Đã thanh toán xong
(NOW(), 'System', NOW(), 'System', 1, 200000.00, 0.00, 'Thanh toán cước giao hàng DO 6', 'PAID', 6),

-- Chưa thanh toán gì
(NOW(), 'System', NOW(), 'System', 1, 1150000.00, 1150000.00, 'Công nợ giao hàng DO 7 (SO 10)', 'UNPAID', 7),

-- ====== Bổ sung các delivery order mới ======

-- Giao hàng DO 8: thanh toán một phần (còn lại 150,000)
(NOW(), 'System', NOW(), 'System', 1, 300000.00, 150000.00, 'Đã thanh toán một phần cước VC DO 8', 'PARTIALLY_PAID', 8),

-- Giao hàng DO 4: thêm một khoản giả định partial khác (còn lại 400,000)
(NOW(), 'System', NOW(), 'System', 1, 810000.00, 400000.00, 'Đã thanh toán một phần công nợ nhập hàng DO 4 (bổ sung)', 'PARTIALLY_PAID', 4),

-- Giao hàng DO 2: giả sử mới thanh toán 2 triệu, còn 8,200,000
(NOW(), 'System', NOW(), 'System', 1, 10200000.00, 8200000.00, 'Đã thanh toán một phần công nợ vận chuyển DO 2', 'PARTIALLY_PAID', 2)
;
