INSERT INTO purchase_debts (
    created_at, created_by, updated_at, updated_by, version,
    original_amount, remaining_amount,
    debt_note, product_id, status, purchase_order_id
) VALUES
-- (NOW(), 'System', NOW(), 'System', 1, original_amount, remaining_amount, debt_note, product_id, status_enum, purchase_order_id)

(NOW(), 'System', NOW(), 'System', 1, 75500.50, 0.00, 'Đã thanh toán đủ cho PO 1', 10, 'PAID', 1),
-- 'Thanh toán đợt 1 PO 2' và status 'Paid' -> Khoản nợ cụ thể 60000 này đã được trả hết.
(NOW(), 'System', NOW(), 'System', 1, 60000.00, 0.00, 'Thanh toán đợt 1 PO 2', 30, 'PAID', 2),
-- 'Công nợ còn lại PO 2' và status 'Pending' -> Khoản nợ 60300 này chưa trả.
(NOW(), 'System', NOW(), 'System', 1, 60300.00, 60300.00, 'Công nợ còn lại PO 2', 35, 'UNPAID', 2),
(NOW(), 'System', NOW(), 'System', 1, 55000.00, 55000.00, 'Công nợ PO 3 (đang xử lý)', 55, 'UNPAID', 3),
(NOW(), 'System', NOW(), 'System', 1, 210000.75, 0.00, 'Đã thanh toán đủ PO 4', 80, 'PAID', 4),
(NOW(), 'System', NOW(), 'System', 1, 95800.00, 95800.00, 'Công nợ PO 5 (mới đặt)', 28, 'UNPAID', 5),
(NOW(), 'System', NOW(), 'System', 1, 180000.00, 0.00, 'Đã thanh toán PO 6 khi nhận hàng', 15, 'PAID', 6),
(NOW(), 'System', NOW(), 'System', 1, 45200.25, 0.00, 'Thanh toán đủ PO 7', 51, 'PAID', 7),
-- 'Thanh toán cọc 100tr cho PO 8' và status 'Paid' -> Khoản cọc 100000 này đã được trả.
(NOW(), 'System', NOW(), 'System', 1, 100000.00, 0.00, 'Thanh toán cọc 100tr cho PO 8', 95, 'PAID', 8),
-- 'Công nợ còn lại PO 8' và status 'Pending' -> Khoản nợ 200500 này chưa trả.
(NOW(), 'System', NOW(), 'System', 1, 200500.00, 200500.00, 'Công nợ còn lại PO 8', 100, 'UNPAID', 8),
(NOW(), 'System', NOW(), 'System', 1, 110000.00, 110000.00, 'Công nợ PO 9 (đã nhận hàng)', 48, 'UNPAID', 9),
-- Đơn hàng hủy, original_amount có thể là giá trị gốc hoặc 0.00 tùy theo cách bạn muốn ghi nhận.
-- Nếu amount cũ là 0.00, thì original_amount và remaining_amount đều là 0.00.
(NOW(), 'System', NOW(), 'System', 1, 0.00, 0.00, 'Đơn hàng PO 10 đã bị hủy', 5, 'CANCELLED', 10);