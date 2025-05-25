INSERT INTO purchase_debts (created_at, created_by, updated_at, updated_by, version, amount, debt_note, product_id, status, purchase_order_id) VALUES
(NOW(), 'System', NOW(), 'System', 1, 75500.50, 'Đã thanh toán đủ cho PO 1', 10, 'Paid', 1),
(NOW(), 'System', NOW(), 'System', 1, 60000.00, 'Thanh toán đợt 1 PO 2', 30, 'Paid', 2),
(NOW(), 'System', NOW(), 'System', 1, 60300.00, 'Công nợ còn lại PO 2', 35, 'Pending', 2),
(NOW(), 'System', NOW(), 'System', 1, 55000.00, 'Công nợ PO 3 (đang xử lý)', 55, 'Pending', 3),
(NOW(), 'System', NOW(), 'System', 1, 210000.75, 'Đã thanh toán đủ PO 4', 80, 'Paid', 4),
(NOW(), 'System', NOW(), 'System', 1, 95800.00, 'Công nợ PO 5 (mới đặt)', 28, 'Pending', 5),
(NOW(), 'System', NOW(), 'System', 1, 180000.00, 'Đã thanh toán PO 6 khi nhận hàng', 15, 'Paid', 6),
(NOW(), 'System', NOW(), 'System', 1, 45200.25, 'Thanh toán đủ PO 7', 51, 'Paid', 7),
(NOW(), 'System', NOW(), 'System', 1, 100000.00, 'Thanh toán cọc 100tr cho PO 8', 95, 'Paid', 8),
(NOW(), 'System', NOW(), 'System', 1, 200500.00, 'Công nợ còn lại PO 8', 100, 'Pending', 8),
(NOW(), 'System', NOW(), 'System', 1, 110000.00, 'Công nợ PO 9 (đã nhận hàng)', 48, 'Pending', 9),
(NOW(), 'System', NOW(), 'System', 1, 0.00, 'Đơn hàng PO 10 đã bị hủy', 5, 'Cancelled', 10);