INSERT INTO sale_debts
(created_at,
 created_by,
 updated_at,
 updated_by,
 version,
 amount,
 debt_note,
 paid_date,
 payment_date,
 project_id,
 status,
 sale_order_id)
VALUES
    -- 1. Sale order 1 (Đợt 1, nợ một phần).
    ('2025-04-08 08:00:00+00', 'system', '2025-04-08 08:00:00+00', 'system', 1,
     5000.00,  'Partial payment due for order 1', NULL,             '2025-04-15 10:00:00+00',
     1,        'Pending',                                             1),
    -- 2. Sale order 2 (Đã thanh toán đầy đủ).
    ('2025-04-08 08:05:00+00', 'system', '2025-04-08 08:05:00+00', 'system', 1,
     20000.00, 'Full payment received for order 2',    '2025-04-08 12:00:00+00', '2025-04-08 12:00:00+00',
     2,        'Paid',                                               2),
    -- 3. Sale order 3 (Đợt 1, nợ một phần).
    ('2025-04-08 08:10:00+00', 'system', '2025-04-08 08:10:00+00', 'system', 1,
     15000.00, 'Partial debt for order 3',             NULL,             '2025-04-10 09:00:00+00',
     3,        'Pending',                                             3),
    -- 4. Sale order 4 (Đơn hàng đã bị huỷ).
    ('2025-04-08 08:15:00+00', 'system', '2025-04-08 08:15:00+00', 'system', 1,
     0.00,     'Order 4 cancelled, no debt',            NULL,             NULL,
     4,        'Cancelled',                                           4),
    -- 5. Sale order 5 (Nợ toàn bộ số tiền).
    ('2025-04-08 08:20:00+00', 'system', '2025-04-08 08:20:00+00', 'system', 1,
     50000.00, 'Full amount pending for order 5',       NULL,             '2025-04-20 14:00:00+00',
     5,        'Pending',                                             5),
    -- 6. Sale order 6 (Thanh toán 50%).
    ('2025-04-08 08:25:00+00', 'system', '2025-04-08 08:25:00+00', 'system', 1,
     30000.00, 'Partial payment, 50% due for order 6', NULL,             '2025-04-10 15:00:00+00',
     6,        'Pending',                                             6),
    -- 7. Sale order 7 (Thanh toán đầy đủ, trễ hạn).
    ('2025-04-08 08:30:00+00', 'system', '2025-04-08 08:30:00+00', 'system', 1,
     70000.00, 'Full payment received for order 7, paid late', '2025-04-10 16:00:00+00', '2025-04-10 16:00:00+00',
     7,        'Paid',                                               7),
    -- 8. Sale order 8 (Huỷ đơn).
    ('2025-04-08 08:35:00+00', 'system', '2025-04-08 08:35:00+00', 'system', 1,
     0.00,     'Cancelled order, no debt for order 8',  NULL,             NULL,
     8,        'Cancelled',                                           8),
    -- 9. Sale order 9 (Đợt 1, thanh toán một phần).
    ('2025-04-08 08:40:00+00', 'system', '2025-04-08 08:40:00+00', 'system', 1,
     45000.00, 'Partial payment, remaining due for order 9', NULL,             '2025-04-11 11:30:00+00',
     9,        'Pending',                                             9),
    -- 10. Sale order 10 (Thanh toán đầy đủ).
    ('2025-04-08 08:45:00+00', 'system', '2025-04-08 08:45:00+00', 'system', 1,
     100000.00,'Full payment received for order 10',    '2025-04-08 09:00:00+00', '2025-04-08 09:00:00+00',
     10,       'Paid',                                              10),
    -- 11. Sale order 1: Giai đoạn 2 (hoàn tất khoản nợ).
    ('2025-04-08 09:00:00+00', 'system', '2025-04-08 09:00:00+00', 'system', 1,
     5000.00,  'Second installment completed for order 1', '2025-04-09 08:00:00+00', '2025-04-09 08:00:00+00',
     1,        'Paid',                                               1),
    -- 12. Sale order 3: Giai đoạn 2 (hoàn tất khoản nợ).
    ('2025-04-08 09:05:00+00', 'system', '2025-04-08 09:05:00+00', 'system', 1,
     15000.00, 'Second installment for order 3',      '2025-04-09 09:30:00+00', '2025-04-09 09:30:00+00',
     3,        'Paid',                                               3),
    -- 13. Sale order 5: Giai đoạn 1, trả một phần tiền.
    ('2025-04-08 09:10:00+00', 'system', '2025-04-08 09:10:00+00', 'system', 1,
     25000.00, 'First installment for order 5',       NULL,             '2025-04-15 14:00:00+00',
     5,        'Pending',                                           5),
    -- 14. Sale order 5: Giai đoạn 2, phần còn lại.
    ('2025-04-08 09:15:00+00', 'system', '2025-04-08 09:15:00+00', 'system', 1,
     25000.00, 'Second installment for order 5',      NULL,             '2025-04-20 15:00:00+00',
     5,        'Pending',                                           5),
    -- 15. Sale order 6: Đợt 2, hoàn tất thanh toán.
    ('2025-04-08 09:20:00+00', 'system', '2025-04-08 09:20:00+00', 'system', 1,
     30000.00, 'Remaining installment for order 6',   NULL,             '2025-04-11 10:00:00+00',
     6,        'Pending',                                           6),
    -- 16. Sale order 9: Giai đoạn 2.
    ('2025-04-08 09:25:00+00', 'system', '2025-04-08 09:25:00+00', 'system', 1,
     22500.00, 'Second installment for order 9',      '2025-04-09 11:00:00+00', '2025-04-09 11:00:00+00',
     9,        'Paid',                                              9),
    -- 17. Sale order 9: Giai đoạn 3 (đợt cuối).
    ('2025-04-08 09:30:00+00', 'system', '2025-04-08 09:30:00+00', 'system', 1,
     22500.00, 'Final installment for order 9',       NULL,             '2025-04-12 12:00:00+00',
     9,        'Pending',                                           9),
    -- 18. Sale order 2: Ghi nhận khoản overdue.
    ('2025-04-08 09:35:00+00', 'system', '2025-04-08 09:35:00+00', 'system', 1,
     5000.00,  'Overdue amount for order 2, installment not paid', NULL, '2025-04-10 17:00:00+00',
     2,        'Overdue',                                           2),
    -- 19. Sale order 10: Ghi nhận phụ thu, chưa thanh toán.
    ('2025-04-08 09:40:00+00', 'system', '2025-04-08 09:40:00+00', 'system', 1,
     20000.00, 'Partial payment pending for order 10', NULL,   '2025-04-15 16:00:00+00',
     10,       'Pending',                                           10),
    -- 20. Sale order 3: Phí trễ hạn.
    ('2025-04-08 09:45:00+00', 'system', '2025-04-08 09:45:00+00', 'system', 1,
     500.00,   'Late fee for order 3',                 NULL,             '2025-04-12 09:00:00+00',
     3,        'Pending',                                           3),
    -- 21. Sale order 6: Phí trễ hạn bổ sung.
    ('2025-04-08 09:50:00+00', 'system', '2025-04-08 09:50:00+00', 'system', 1,
     800.00,   'Late fee for order 6',                 NULL,             '2025-04-11 16:00:00+00',
     6,        'Pending',                                           6),
    -- 22. Sale order 9: Phí phạt do chậm thanh toán.
    ('2025-04-08 09:55:00+00', 'system', '2025-04-08 09:55:00+00', 'system', 1,
     600.00,   'Penalty fee for order 9',              NULL,             '2025-04-12 10:00:00+00',
     9,        'Overdue',                                           9),
    -- 23. Sale order 2: Phụ phí giao hàng, đã thanh toán.
    ('2025-04-08 10:00:00+00', 'system', '2025-04-08 10:00:00+00', 'system', 1,
     300.00,   'Surcharge fee for order 2',            '2025-04-08 10:30:00+00', '2025-04-08 10:30:00+00',
     2,        'Paid',                                              2),
    -- 24. Sale order 10: Phí giao hàng chưa thanh toán.
    ('2025-04-08 10:05:00+00', 'system', '2025-04-08 10:05:00+00', 'system', 1,
     1500.00,  'Delivery fee for order 10',             NULL,             '2025-04-15 11:00:00+00',
     10,       'Pending',                                           10),
    -- 25. Sale order 10: Ghi nhận phí khác (phụ thu dịch vụ).
    ('2025-04-08 10:10:00+00', 'system', '2025-04-08 10:10:00+00', 'system', 1,
     700.00,   'Additional service fee for order 10',   NULL,             '2025-04-15 11:30:00+00',
     10,       'Pending',                                           10)
;
