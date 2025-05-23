INSERT INTO purchase_debts
(created_at, created_by, updated_at, updated_by, version, amount, debt_note, order_type, paid_date, payment_date, project_id, status, purchase_order_id)
VALUES
('2025-04-10 08:00:00+00', 'admin', '2025-04-10 08:00:00+00', 'admin', 1, 75500.50, 'Đã thanh toán đủ cho PO 1', 'Mua hàng', '2025-04-10 07:55:00+00', '2025-04-08 00:00:00+00', 1, 'Paid', 1),
('2025-04-05 09:15:00+00', 'admin', '2025-04-05 09:15:00+00', 'admin', 1, 60000.00, 'Thanh toán đợt 1 PO 2', 'Mua hàng', '2025-04-05 09:10:00+00', '2025-04-15 00:00:00+00', NULL, 'Paid', 2),
('2025-04-05 09:16:00+00', 'admin', '2025-04-05 09:16:00+00', 'admin', 1, 60300.00, 'Công nợ còn lại PO 2', 'Mua hàng', NULL, '2025-04-25 00:00:00+00', NULL, 'Pending', 2),
('2025-04-12 10:00:00+00', 'admin', '2025-04-12 10:00:00+00', 'admin', 1, 55000.00, 'Công nợ PO 3 (đang xử lý)', 'Mua hàng', NULL, '2025-04-30 00:00:00+00', 7, 'Pending', 3),
('2025-04-15 11:30:00+00', 'admin', '2025-04-15 11:30:00+00', 'admin', 1, 210000.75, 'Đã thanh toán đủ PO 4', 'Mua hàng', '2025-04-15 11:25:00+00', '2025-04-12 00:00:00+00', NULL, 'Paid', 4),
('2025-04-13 14:00:00+00', 'admin', '2025-04-13 14:00:00+00', 'admin', 1, 95800.00, 'Công nợ PO 5 (mới đặt)', 'Mua hàng', NULL, '2025-05-10 00:00:00+00', NULL, 'Pending', 5),
('2025-04-20 15:45:00+00', 'admin', '2025-04-20 15:45:00+00', 'admin', 1, 180000.00, 'Đã thanh toán PO 6 khi nhận hàng', 'Mua hàng', '2025-04-20 15:40:00+00', '2025-04-20 00:00:00+00', 16, 'Paid', 6),
('2025-04-14 08:30:00+00', 'admin', '2025-04-14 08:30:00+00', 'admin', 1, 45200.25, 'Thanh toán đủ PO 7', 'Mua hàng', '2025-04-14 08:25:00+00', '2025-04-11 00:00:00+00', NULL, 'Paid', 7),
('2025-04-10 09:00:00+00', 'admin', '2025-04-10 09:00:00+00', 'admin', 1, 100000.00, 'Thanh toán cọc 100tr cho PO 8', 'Mua hàng', '2025-04-10 08:55:00+00', '2025-04-10 00:00:00+00', NULL, 'Paid', 8),
('2025-04-10 09:01:00+00', 'admin', '2025-04-10 09:01:00+00', 'admin', 1, 200500.00, 'Công nợ còn lại PO 8', 'Mua hàng', NULL, '2025-05-05 00:00:00+00', NULL, 'Pending', 8),
('2025-04-15 10:10:00+00', 'admin', '2025-04-15 10:10:00+00', 'admin', 1, 110000.00, 'Công nợ PO 9 (đã nhận hàng)', 'Mua hàng', NULL, '2025-05-15 00:00:00+00', 25, 'Pending', 9),
('2025-04-06 13:00:00+00', 'admin', '2025-04-06 13:00:00+00', 'admin', 1, 0.00, 'Đơn hàng PO 10 đã bị hủy', 'Mua hàng', NULL, NULL, NULL, 'Cancelled', 10);