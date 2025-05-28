INSERT INTO sale_debts (
    created_at, created_by, updated_at, updated_by, version,
    original_amount, remaining_amount, -- Thay thế 'amount'
    debt_note, product_id, status, sale_order_id
) VALUES
-- (NOW(), 'System', NOW(), 'System', 1, original_amount, remaining_amount, debt_note, product_id, status_enum, sale_order_id)

-- 1. Sale order 1 (Đợt 1, nợ một phần).
('2025-04-08 08:00:00+00', 'System', '2025-04-08 08:00:00+00', 'System', 1, 5000.00, 5000.00, 'Partial payment due for order 1', 1, 'UNPAID', 1),
-- 2. Sale order 2 (Đã thanh toán đầy đủ).
('2025-04-08 08:05:00+00', 'System', '2025-04-08 08:05:00+00', 'System', 1, 20000.00, 0.00, 'Full payment received for order 2', 2, 'PAID', 2),
-- 3. Sale order 3 (Đợt 1, nợ một phần).
('2025-04-08 08:10:00+00', 'System', '2025-04-08 08:10:00+00', 'System', 1, 15000.00, 15000.00, 'Partial debt for order 3', 3, 'UNPAID', 3),
-- 4. Sale order 4 (Đơn hàng đã bị huỷ).
('2025-04-08 08:15:00+00', 'System', '2025-04-08 08:15:00+00', 'System', 1, 0.00, 0.00, 'Order 4 cancelled, no debt', 4, 'CANCELLED', 4),
-- 5. Sale order 5 (Nợ toàn bộ số tiền).
('2025-04-08 08:20:00+00', 'System', '2025-04-08 08:20:00+00', 'System', 1, 50000.00, 50000.00, 'Full amount pending for order 5', 5, 'UNPAID', 5),
-- 6. Sale order 6 (Thanh toán 50% - hiểu là khoản nợ gốc 30000 này chưa trả).
('2025-04-08 08:25:00+00', 'System', '2025-04-08 08:25:00+00', 'System', 1, 30000.00, 30000.00, 'Partial payment, 50% due for order 6', 6, 'UNPAID', 6),
-- 7. Sale order 7 (Thanh toán đầy đủ, trễ hạn).
('2025-04-08 08:30:00+00', 'System', '2025-04-08 08:30:00+00', 'System', 1, 70000.00, 0.00, 'Full payment received for order 7, paid late', 7, 'PAID', 7),
-- 8. Sale order 8 (Huỷ đơn).
('2025-04-08 08:35:00+00', 'System', '2025-04-08 08:35:00+00', 'System', 1, 0.00, 0.00, 'Cancelled order, no debt for order 8', 8, 'CANCELLED', 8),
-- 9. Sale order 9 (Đợt 1, thanh toán một phần - hiểu là khoản nợ gốc 45000 này chưa trả).
('2025-04-08 08:40:00+00', 'System', '2025-04-08 08:40:00+00', 'System', 1, 45000.00, 45000.00, 'Partial payment, remaining due for order 9', 9, 'UNPAID', 9),
-- 10. Sale order 10 (Thanh toán đầy đủ).
('2025-04-08 08:45:00+00', 'System', '2025-04-08 08:45:00+00', 'System', 1, 100000.00, 0.00, 'Full payment received for order 10', 10, 'PAID', 10),
-- 11. Sale order 1: Giai đoạn 2 (hoàn tất khoản nợ - đây là một khoản nợ riêng đã được thanh toán).
('2025-04-08 09:00:00+00', 'System', '2025-04-08 09:00:00+00', 'System', 1, 5000.00, 0.00, 'Second installment completed for order 1', 1, 'PAID', 1),
-- 12. Sale order 3: Giai đoạn 2 (hoàn tất khoản nợ - đây là một khoản nợ riêng đã được thanh toán).
('2025-04-08 09:05:00+00', 'System', '2025-04-08 09:05:00+00', 'System', 1, 15000.00, 0.00, 'Second installment for order 3', 3, 'PAID', 3),
-- 13. Sale order 5: Giai đoạn 1, trả một phần tiền (khoản nợ 25000 này chưa trả).
('2025-04-08 09:10:00+00', 'System', '2025-04-08 09:10:00+00', 'System', 1, 25000.00, 25000.00, 'First installment for order 5', 5, 'UNPAID', 5),
-- 14. Sale order 5: Giai đoạn 2, phần còn lại (khoản nợ 25000 này chưa trả).
('2025-04-08 09:15:00+00', 'System', '2025-04-08 09:15:00+00', 'System', 1, 25000.00, 25000.00, 'Second installment for order 5', 5, 'UNPAID', 5),
-- 15. Sale order 6: Đợt 2, hoàn tất thanh toán (khoản nợ 30000 này chưa trả, "Pending" có thể hiểu là đang chờ thanh toán).
('2025-04-08 09:20:00+00', 'System', '2025-04-08 09:20:00+00', 'System', 1, 30000.00, 30000.00, 'Remaining installment for order 6', 6, 'UNPAID', 6),
-- 16. Sale order 9: Giai đoạn 2 (khoản nợ 22500 này đã được thanh toán).
('2025-04-08 09:25:00+00', 'System', '2025-04-08 09:25:00+00', 'System', 1, 22500.00, 0.00, 'Second installment for order 9', 9, 'PAID', 9),
-- 17. Sale order 9: Giai đoạn 3 (đợt cuối - khoản nợ 22500 này chưa trả).
('2025-04-08 09:30:00+00', 'System', '2025-04-08 09:30:00+00', 'System', 1, 22500.00, 22500.00, 'Final installment for order 9', 9, 'UNPAID', 9),
-- 18. Sale order 2: Ghi nhận khoản overdue (khoản nợ 5000 này chưa trả, 'Overdue' -> UNPAID).
('2025-04-08 09:35:00+00', 'System', '2025-04-08 09:35:00+00', 'System', 1, 5000.00, 5000.00, 'Overdue amount for order 2, installment not paid', 2, 'UNPAID', 2),
-- 19. Sale order 10: Ghi nhận phụ thu, chưa thanh toán.
('2025-04-08 09:40:00+00', 'System', '2025-04-08 09:40:00+00', 'System', 1, 20000.00, 20000.00, 'Partial payment pending for order 10', 10, 'UNPAID', 10),
-- 20. Sale order 3: Phí trễ hạn.
('2025-04-08 09:45:00+00', 'System', '2025-04-08 09:45:00+00', 'System', 1, 500.00, 500.00, 'Late fee for order 3', 3, 'UNPAID', 3),
-- 21. Sale order 6: Phí trễ hạn bổ sung.
('2025-04-08 09:50:00+00', 'System', '2025-04-08 09:50:00+00', 'System', 1, 800.00, 800.00, 'Late fee for order 6', 6, 'UNPAID', 6),
-- 22. Sale order 9: Phí phạt do chậm thanh toán ('Overdue' -> UNPAID).
('2025-04-08 09:55:00+00', 'System', '2025-04-08 09:55:00+00', 'System', 1, 600.00, 600.00, 'Penalty fee for order 9', 9, 'UNPAID', 9),
-- 23. Sale order 2: Phụ phí giao hàng, đã thanh toán.
('2025-04-08 10:00:00+00', 'System', '2025-04-08 10:00:00+00', 'System', 1, 300.00, 0.00, 'Surcharge fee for order 2', 2, 'PAID', 2),
-- 24. Sale order 10: Phí giao hàng chưa thanh toán.
('2025-04-08 10:05:00+00', 'System', '2025-04-08 10:05:00+00', 'System', 1, 1500.00, 1500.00, 'Delivery fee for order 10', 10, 'UNPAID', 10),
-- 25. Sale order 10: Ghi nhận phí khác (phụ thu dịch vụ).
('2025-04-08 10:10:00+00', 'System', '2025-04-08 10:10:00+00', 'System', 1, 700.00, 700.00, 'Additional service fee for order 10', 10, 'UNPAID', 10);