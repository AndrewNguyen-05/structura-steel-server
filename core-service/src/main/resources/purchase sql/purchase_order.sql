INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, import_code, project_id, purchase_orders_note, status, supplier_id, total_amount) VALUES
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250401CDEFGH1JKLMA1', 1, 'Đặt hàng thép vằn cho dự án 1', 'DONE', 22, 75500.50),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250401CDEFGH2JKLMB2', 2, 'Nhập thép tấm bổ sung kho 4', 'DONE', 5, 120300.00),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250402CDEFGH3JKLMC3', 3, 'Đơn hàng thép hộp dự án 7', 'PROCESSING', 78, 55000.00),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250402CDEFGH4JKLMD4', 4, 'Nhập thép hình cho kho 10 (theo SO 10)', 'DONE', 41, 210000.75),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250403CDEFGH5JKLME5', 5, 'Đặt thép tấm kho 13', 'NEW', 99, 95800.00),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250403CDEFGH6JKLMF6', 6, 'Thép công nghiệp dự án 16', 'DONE', 15, 180000.00),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250404CDEFGH7JKLMG7', 7, 'Nhập thép ống kho 19', 'DONE', 88, 45200.25),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250404CDEFGH8JKLMH8', 8, 'Đặt hàng thép hình từ NCC 22 (theo SO 1)', 'PROCESSING', 5, 300500.00),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250405CDEFGH9JKLMI9', 9, 'Thép tấm cho dự án 25', 'DONE', 63, 110000.00),
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250405CDEFGH0JKLMJA', 10, 'Đơn hàng bị hủy từ NCC 28', 'CANCELED', 10, 88000.00); -- <<< Đã đổi thành CANCELED