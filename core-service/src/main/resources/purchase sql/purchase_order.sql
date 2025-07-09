INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
-- PO 1
('2025-07-05 11:40:00+07', 'System', '2025-07-05 11:40:00+07', 'System', 1, 1, 'IMP20250705K1X2Y3ZA4B5C', '{"id": 1, "partnerName": "Công ty TNHH Thép Hòa Hưng"}'::jsonb, '{"id": 1, "projectName": "Nhà xưởng Tân Tạo Mở Rộng"}'::jsonb, 'DONE', 1363050.00, 90.87, 'Mua hàng theo đơn bán hàng #1', 'YES', false),
-- PO 2
('2025-07-06 11:40:00+07', 'System', '2025-07-06 11:40:00+07', 'System', 1, NULL, 'IMP20250706L2X3Y4ZB5C6D', '{"id": 4, "partnerName": "Công ty CP Thép Miền Trung"}'::jsonb, NULL, 'DONE', 142845000.00, 9523.00, 'Nhập thép tấm và thép hộp về kho chính', 'YES', false),
-- PO 3
('2025-07-07 11:40:00+07', 'System', '2025-07-07 11:40:00+07', 'System', 1, 4, 'IMP20250707M3X4Y5ZC6D7E', '{"id": 7, "partnerName": "Nhà Máy Cán Thép Nam Hạnh"}'::jsonb, '{"id": 6, "projectName": "Nâng cấp Cảng Cát Lái"}'::jsonb, 'PROCESSING', 1401450.00, 93.43, 'Mua hàng theo đơn bán hàng #4', 'NO', false),
-- PO 4
('2025-07-08 11:40:00+07', 'System', '2025-07-08 11:40:00+07', 'System', 1, NULL, 'IMP20250708N4X5Y6ZD7E8F', '{"id": 10, "partnerName": "Phân Phối Thép Tuấn Hà"}'::jsonb, NULL, 'NEW', 54075000.00, 3605.00, 'Nhập số lượng lớn thép vằn D10', 'NO', false),
-- PO 5
('2025-07-09 11:40:00+07', 'System', '2025-07-09 11:40:00+07', 'System', 1, 5, 'IMP20250709P5X6Y7ZE8F9G', '{"id": 13, "partnerName": "Thép Xây Dựng Bình Tâm"}'::jsonb, '{"id": 8, "projectName": "Cao ốc Crescent Hub"}'::jsonb, 'NEW', 1230150.00, 82.01, 'Mua hàng theo đơn bán hàng #5', 'NO', false),
-- PO 6
('2025-07-09 11:40:00+07', 'System', '2025-07-09 11:40:00+07', 'System', 1, NULL, 'IMP20250709Q6X7Y8ZF9G0H', '{"id": 16, "partnerName": "Thép Công Nghiệp Phong Hải"}'::jsonb, NULL, 'CANCELLED', 35325000.00, 2355.00, 'Hủy đơn do nhà cung cấp hết hàng', 'NO', true),
-- PO 7
('2025-07-09 11:40:00+07', 'System', '2025-07-09 11:40:00+07', 'System', 1, 2, 'IMP20250709R7X8Y9ZG0H1I', '{"id": 19, "partnerName": "Đại Lý Thép Ống Bảo Linh"}'::jsonb, '{"id": 2, "projectName": "Trung tâm Nghiên cứu CNC"}'::jsonb, 'PROCESSING', 9999150.00, 666.61, 'Mua hàng theo đơn bán hàng #2', 'NO', false),
-- PO 8
('2025-07-09 11:40:00+07', 'System', '2025-07-09 11:40:00+07', 'System', 1, NULL, 'IMP20250709S8X9Y0ZH1I2J', '{"id": 88, "partnerName": "Công ty TNHH Thép Dũng Mai"}'::jsonb, NULL, 'DONE', 10500000.00, 700.00, 'Nhập kho thép hình I-150', 'YES', false);